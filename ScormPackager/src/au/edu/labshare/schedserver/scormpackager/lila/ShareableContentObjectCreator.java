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

//Needed for XML generation
import javax.imageio.IIOException;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import au.edu.labshare.schedserver.scormpackager.lila.ShareableContentObjectCreatorAdaptor;

public class ShareableContentObjectCreator extends ShareableContentObjectCreatorAdaptor  
{
	public static final String MANFEST_NAME = "imsmanfest.xml";
	
	private static final String SCO_FILENAME = null;
	private static final String GENERIC_TITLE = "Generic Title";
	private static final String SCO_FILEPATH = "/SCOs";
	private static final int BUFFER_SIZE = 18024;
	
	@Override
	public String createSCO(String title, Collection <File> assets, String LMSName) 
	{
		String zipFileName = null;
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
			
		// TODO Need to process the title and replace the name with underscores
		zipFileName  = removeWhiteSpace(title);
		
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
			    
			    
			    filePathSCO = SCO_FILEPATH + "/" + zipFileName + ".zip";
				zipFileOutStream.close();
				
				//TODO Need to associate the ZipFile object type to the zipFileOutStream/ZipEntries.
				return filePathSCO;
			}
			catch(IOException e)
			{
				e.printStackTrace(); //TODO replace with Sahara Logger as part of refactoring.
			}
			catch(NullPointerException e)
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
	public boolean validateContent(ZipFile PIF) 
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	/**
	 * Generates the lmsmanifest.xml file. This is not going to be public
	 * 
	 * Based on: http://www.vogella.de/articles/JavaXML/article.html
	 * @param title
	 * @param assets
	 */
	private String generateManifest(String title, Collection<File> assets) 
	{
		//Create the XML file 
		try
		{
			// Create a XMLOutputFactory
			XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
			// Create XMLEventWriter
			XMLEventWriter eventWriter = outputFactory
					.createXMLEventWriter(new FileOutputStream(MANFEST_NAME));
			// Create a EventFactory
			XMLEventFactory eventFactory = XMLEventFactory.newInstance();
			XMLEvent end = eventFactory.createDTD("\n");
			// Create and write Start Tag
			StartDocument startDocument = eventFactory.createStartDocument();
			eventWriter.add(startDocument);

			// Create config open tag
			StartElement configStartElement = eventFactory.createStartElement("",
					"", "config");
			eventWriter.add(configStartElement);
			eventWriter.add(end);
			// Write the different nodes
			createNode(eventWriter, "mode", "1");
			createNode(eventWriter, "unit", "901");
			createNode(eventWriter, "current", "0");
			createNode(eventWriter, "interactive", "0");

			eventWriter.add(eventFactory.createEndElement("", "", "config"));
			eventWriter.add(end);
			eventWriter.add(eventFactory.createEndDocument());
			eventWriter.close();
			
		}
		catch (XMLStreamException e) 
		{
			e.printStackTrace();//TODO replace with Sahara Logger as part of refactoring.
		}
		catch (IOException e) 
		{
			e.printStackTrace();//TODO replace with Sahara Logger as part of refactoring.
		}
		
		return null;
	}
	
	/**
	 * Takes the value of a name to produce an underscore version of the name.
	 * This is to allow naming consistency.
	 * 
	 * This is from: http://www.vogella.de/articles/JavaXML/article.html
	 * @param name
	 */
	private void createNode(XMLEventWriter eventWriter, String name, String value)
	{
		try
		{
			XMLEventFactory eventFactory = XMLEventFactory.newInstance();
			XMLEvent end = eventFactory.createDTD("\n");
			XMLEvent tab = eventFactory.createDTD("\t");
			// Create Start node
			StartElement sElement = eventFactory.createStartElement("", "", name);
			eventWriter.add(tab);
			eventWriter.add(sElement);
			// Create Content
			Characters characters = eventFactory.createCharacters(value);
			eventWriter.add(characters);
			// Create End node
			EndElement eElement = eventFactory.createEndElement("", "", name);
			eventWriter.add(eElement);
			eventWriter.add(end);
		}
		catch(XMLStreamException e)
		{
			e.printStackTrace(); //TODO replace with Sahara Logger as part of refactoring.
		}

	}
	
	/**
	 * Takes the value of a name to produce an underscore version of the name.
	 * This is to allow naming consistency.
	 * @param name
	 */
	private String removeWhiteSpace(String name) 
	{
		// TODO Auto-generated method stub
		return null;
	}

}
