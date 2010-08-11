package au.edu.labshare.schedserver.scormpackager.lila;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import java.util.zip.Deflater;
import java.util.Collection;
import java.util.Iterator;

import au.edu.labshare.schedserver.scormpackager.lila.ShareableContentObjectCreatorAdaptor;

public class ShareableContentObjectCreator extends ShareableContentObjectCreatorAdaptor  
{
	private static final String SCO_FILENAME = null;
	private static final String GENERIC_TITLE = "SCORM";
	private static final String SCO_FILEPATH = "/SCOs/";
	private static final int BUFFER_SIZE = 18024;
	
	@Override
	public String createSCO(String title, Collection <File> assets, String LMSName) 
	{
		String filePathSCO = null;
		String filePathManifest = null;
		FileInputStream fileInStream = null;
		FileOutputStream fileOutStream = null;
		ZipOutputStream zipFileOutStream = null;
		byte[] buffer = new byte[BUFFER_SIZE];
		
		//If no title was provided use generic name placeholder.
		if(title == null)
			title = GENERIC_TITLE;
		
		// TODO Need to do the autogeneration of the manifest file.
		filePathManifest = generateManifest(title, assets);
		
		//Zip the contents into a file
		try 
		{
			fileOutStream = new FileOutputStream(SCO_FILENAME);
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace(); //TODO Replace with Sahara Logger as part of refactoring.
		}

		if(fileOutStream != null)
		{
			try
			{
				zipFileOutStream  = new ZipOutputStream(fileOutStream);
				zipFileOutStream.setLevel(Deflater.DEFAULT_COMPRESSION);
				
				Iterator <File> iter = assets.iterator();
				while(iter.hasNext())
				{
					fileInStream = new FileInputStream(iter.next());
					//TODO The below string conversion does not handle the lib/ direcctory structure
					zipFileOutStream.putNextEntry(new ZipEntry(iter.next().getName()));
					int len;
				    while ((len = fileInStream.read(buffer)) > 0)
				    {
				    	zipFileOutStream.write(buffer, 0, len);
				    }
				    				    	
				    zipFileOutStream.closeEntry();
				    fileInStream.close();
				}
				
				//Add the imsmanifest.xml file to SCO
				fileInStream = new FileInputStream(filePathManifest);
				zipFileOutStream.putNextEntry(new ZipEntry(filePathManifest));
				int len;
			    while ((len = fileInStream.read(buffer)) > 0)
			    {
			    	zipFileOutStream.write(buffer, 0, len);
			    }
			    zipFileOutStream.closeEntry();
			    fileInStream.close();
			    
			    
			    filePathSCO = SCO_FILEPATH + title + ".zip";
				zipFileOutStream.close();
				
				//TODO Need to associate the ZipFile object type to the zipFileOutStream/ZipEntries.
				return filePathSCO;
			}
			catch(IOException e)
			{
				e.printStackTrace(); //TODO replace with Sahara Logger as part of refactoring.
			}
			
			return null;
		}
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
	public String createPIF(String title, Collection<File> content) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean validateContent(ZipFile PIF) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * Generates the lmsmanifest.xml file. This is not going to be public
	 * @param title
	 * @param assets
	 */
	private String generateManifest(String title, Collection<File> assets) {
		// TODO Auto-generated method stub
		return null;
	}
}
