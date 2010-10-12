package au.edu.labshare.schedserver.scormpackager.lila;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import java.util.zip.Deflater;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

//Needed for Manifest creation
import au.edu.labshare.schedserver.scormpackager.lila.Manifest;
import au.edu.labshare.schedserver.scormpackager.manifest.MetaData;
import au.edu.labshare.schedserver.scormpackager.manifest.Organization;
import au.edu.labshare.schedserver.scormpackager.manifest.Resource;
import au.edu.labshare.schedserver.scormpackager.sahara.RigLaunchPageCreator;
import au.edu.labshare.schedserver.scormpackager.utilities.ScormUtilities;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;

public class ShareableContentObjectCreator extends au.edu.labshare.schedserver.scormpackager.ShareableContentObjectCreator 
{
	private ManifestXMLDecorator manifestXMLDecorator;
	private Logger saharaLogger;
	
	public static final int VERSION_NUM = 1;
	
	//TODO - this is to be replaced by reading from Properties file. 
	public static final String OUTPUT_PATH = "/tmp";
	
	private static final String GENERIC_TITLE = "Generic Title";
	private static final String DEFAULT_INSTITUTION = "LabShare";
	private static final int BUFFER_SIZE = 18024;
	
	public ShareableContentObjectCreator(Logger logger)
	{
		saharaLogger = logger;
	}
	
	public ShareableContentObjectCreator()
	{
		saharaLogger = au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator.getLogger();
	}
	
	@Override
	public String createSCO(String title, Collection <File> assets, String outputPath) 
	{
		String filePathSCO = null;
		String filePathManifest = null;
		FileInputStream fileInStream = null;
		FileOutputStream fileOutStream = null;
		ZipOutputStream zipFileOutStream = null;
		byte[] buffer = new byte[BUFFER_SIZE];
		Manifest imsmanifest = null;
		
		//If no title was provided use generic name placeholder.
		if(title == null)
			title = GENERIC_TITLE;
		
		filePathSCO = outputPath + ScormUtilities.replaceWhiteSpace(title, null) + ".zip";

		//Create the manifest
		//Extract the names from the *.html files
		int iTitleIter = 0;
		String[] titles = new String[assets.size()];   
		
		for(Iterator<File> iter = assets.iterator(); iter.hasNext();)
		{
			String str = iter.next().getName();
			
			if(ScormUtilities.getFileExtension(str).equals(Manifest.HTML_EXT))
				titles[iTitleIter] = ScormUtilities.getFileNameWithoutExtension(str);
			
			iTitleIter++;
		}

		imsmanifest = createManifest(titles, assets);
		
		//Decorate the manifest with relevant XML information
		manifestXMLDecorator = new ManifestXMLDecorator(saharaLogger);
		filePathManifest = manifestXMLDecorator.decorateManifest(imsmanifest, outputPath); 
		
		//Zip the contents into a file
		try 
		{
			fileOutStream = new FileOutputStream(filePathSCO);
		} 
		catch (FileNotFoundException e) 
		{
			//Log any exception output
            this.saharaLogger.debug("Received " + this.getClass().getName() + e.toString());
		}

		//TODO This needs to be refactored as there is alot of zipping which should occur in separate methods
		if(fileOutStream != null)
		{
			try
			{
				zipFileOutStream  = new ZipOutputStream(fileOutStream);
				zipFileOutStream.setLevel(Deflater.DEFAULT_COMPRESSION);
				
				Iterator <File> iter = assets.iterator();
				while(iter.hasNext())
				{
					File assetFile = iter.next();
					fileInStream = new FileInputStream(assetFile);
					//TODO The below string conversion does not handle the lib/ directory structure
					zipFileOutStream.putNextEntry(new ZipEntry(assetFile.getName()));
					int len;
				    while ((len = fileInStream.read(buffer)) > 0)
				    {
				    	zipFileOutStream.write(buffer, 0, len);
				    }
				    				    	
				    zipFileOutStream.closeEntry();
				    fileInStream.close();
				}
				
				// Add the imsmanifest.xml file to SCO
				fileInStream = new FileInputStream(filePathManifest);
				zipFileOutStream.putNextEntry(new ZipEntry(ScormUtilities.getFilenameFromPath(filePathManifest, "/")));
				int len;
			    while ((len = fileInStream.read(buffer)) > 0)
			    {
			    	zipFileOutStream.write(buffer, 0, len);
			    }
			    zipFileOutStream.closeEntry();
			    fileInStream.close();
				
				// Add the SCORM XML schemas to the SCO 
				ArrayList<File> xmlSchemaList = ManifestXMLDecorator.addXMLSchemas(outputPath); 
				Iterator <File> iterXMLSchemas = xmlSchemaList.iterator();
				while(iterXMLSchemas.hasNext())
				{
					File xmlSchemaFile = iterXMLSchemas.next();
					fileInStream = new FileInputStream(xmlSchemaFile);
					//TODO The below string conversion does not handle the lib/ directory structure
					zipFileOutStream.putNextEntry(new ZipEntry(xmlSchemaFile.getName()));
					int lenSchemaFile;
				    while ((lenSchemaFile = fileInStream.read(buffer)) > 0)
				    {
				    	zipFileOutStream.write(buffer, 0, lenSchemaFile);
				    }
				    				    	
				    zipFileOutStream.closeEntry();
				    fileInStream.close();
				}
				
				
				//Add the relevant LAUNCHPAGE CSS - in this case we are using BluePrintCSS
				//Derived from: http://stackoverflow.com/questions/1399126/java-util-zip-recreating-directory-structure
				RigLaunchPageCreator rigLaunchPageCreator = new RigLaunchPageCreator();
				//TODO: Bit of a hack - this should technically be more elegant, assumption output path has css files:
				String cssDirStr =  rigLaunchPageCreator.addCSS();
				File cssFilesDir = null;
				
				cssFilesDir = new File(OUTPUT_PATH + cssDirStr);
				
				URI base = cssFilesDir.toURI();
				Deque<File> queue = new LinkedList<File>();
				queue.push(cssFilesDir);

				while(!queue.isEmpty())
				{
					cssFilesDir = queue.pop();
					for (File childFile : cssFilesDir.listFiles())
					{
						String name = base.relativize(childFile.toURI()).getPath();
									
						//Ignore .svn directory
						if(name.contains(".svn"))
						{
							queue.remove(childFile);
						}
						else
						{
							if(childFile.isDirectory())
							{
								queue.push(childFile);
								name = name.endsWith("/") ? name : name + "/";
								zipFileOutStream.putNextEntry(new ZipEntry(name));
							}
							else
							{
								zipFileOutStream.putNextEntry(new ZipEntry(name));
								ScormUtilities.copy(childFile, zipFileOutStream);
								zipFileOutStream.closeEntry();
							}
						}
					}
				}

				zipFileOutStream.close();
				fileOutStream.close();
			}
			catch(IOException e)
			{
				//Log any exception output
	            this.saharaLogger.debug("Received " + this.getClass().getName() + e.toString());
			}
			catch(NullPointerException e)
			{
				//Log any exception output
	            this.saharaLogger.debug("Received " + this.getClass().getName() + e.toString());
			}
			
			return filePathSCO;
		}
		else
			return null;
	}

	private Manifest createManifest(String[] titles, Collection<File> assets) 
	{
		Manifest manifest = null;
		MetaData metadata = null;
		Collection<Resource> resources;
		Collection<Organization> organizations;
		String[] institutions = new String[1];
		
		institutions[0] = ShareableContentObjectCreator.DEFAULT_INSTITUTION;

		manifest = new Manifest();
		File[] files;
		files = (File[])assets.toArray(new File[assets.size()]);
		organizations = manifest.generateOrganisations(institutions, titles, files);
		resources = manifest.generateResources(files);

		//Generate the MetaData associated with Manifest
		metadata = new MetaData();
		metadata.setIdentifer(Manifest.GENERIC_IDENTIFER); //TODO Grab the experiment id from <title/> 
		metadata.setSchemaVersion(Manifest.SCHEMA_VERSION);
		manifest.setMetaData(metadata);
		
		
		//Do a quick check to see if the resources and organizations are not empty
		if(organizations != null && resources !=null)
			return manifest;
		else
			return null;
	}
	
	@Override
	public boolean validateApplicationProfile(ZipFile SCO) 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validateManifest(ZipFile SCO) 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validateMetaData(ZipFile SCO) 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validatePresentationNavigationInfo(ZipFile SCO) 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validateSequencing(ZipFile SCO) 
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean validateLMSConnection(ZipFile jsLMSFile)
	{
		// TODO Depending on the structure of the 
		return false;
	}

	@Override
	public String createPIF(String title, Collection<File> content, String outputPath) 
	{
		return createSCO(title, content, outputPath);
	}

	@Override
	public boolean validateContent(ZipFile PIF) 
	{
		// TODO Auto-generated method stub
		return false;
	}
}
