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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

//Needed for XML generation
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.events.Attribute;

import au.edu.labshare.schedserver.scormpackager.lila.ShareableContentObjectCreatorAdaptor;

public class ShareableContentObjectCreator extends ShareableContentObjectCreatorAdaptor  
{
	
	//TODO - this is to be replaced by reading from Properties file. 
	private static final String SCO_FILENAME = null;
	private static final String GENERIC_TITLE = "Generic Title";
	private static final String SCO_FILEPATH = "/SCOs";
	private static final String SCO_INSTITUTION = "UTS";
	private static final int BUFFER_SIZE = 18024;
	
	//Properties that are associated with the imsmanifest file
	public static final String MANFEST_NAME = "imsmanfest.xml";
	public static final String MANIFEST_NODE_NAME = "manifest";
	public static final String MANIFEST_NODE_VERSION = "1.0";
	public static final String MANIFEST_XMLNS_IMSCP = "http://www.imsproject.org/xsd/imscp_rootv1p1p2";
	public static final String MANIFEST_XMLNS_ADLCP = "http://www.adlnet.org/xsd/adlcp_rootv1p2";
	public static final String MANIFEST_XMLNS_XSI = "http://www.w3.org/2001/XMLSchema-instance";
	public static final String MANIFEST_XSI_IMSCP_SCHEMALOC = "http://www.imsproject.org/xsd/imscp_rootv1p1p2 imscp_rootv1p1p2.xsd";
	public static final String MANIFEST_XSI_IMSMD_SCHEMALOC = "http://www.imsglobal.org/xsd/imsmd_rootv1p2p1 imsmd_rootv1p2p1.xsd";
	public static final String MANIFEST_XSI_ADLCP_SCHEMALOC = "http://www.adlnet.org/xsd/adlcp_rootv1p2 adlcp_rootv1p2.xsd";
	public static final String MANIFEST_ORG_NODE_NAME = "organizations";
	public static final String MANIFEST_ORG_ELEM_NAME = "organization";
	public static final String MANIFEST_TITLE = "title";
	public static final String MANIFEST_ITEM = "item";
	public static final String MANIFEST_RESOURCES_NODE_NAME = "resources";
	public static final String MANIFEST_RESOURCE_ELEM_NAME = "resource";
	public static final String MANIFEST_FILE_ELEM_NAME = "file";
	public static final String MANIFEST_DEPENDENCY_ELEM_NAME = "dependency";
	public static final String MANIFEST_SCORMTYPE_SCO = "sco";
	public static final String MANIFEST_SCORMTYPE_ASSET = "asset";
	
	
	
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
	 * @param assets. Assumption 1st collection element is the *.html file to be rendered 
	 */
	private String generateManifest(String title, Collection<File> assets) 
	{	
		ArrayList<Attribute> attributeList = null;
		File htmlFile = (File)(assets.toArray()[0]);
		
		//Create the XML file 
		try
		{
			//TODO Need to refactorthis section - too big for one method
			
			// Create a XMLOutputFactory
			XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
			// Create XMLEventWriter
			XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(new FileOutputStream(MANFEST_NAME));
			// Create a EventFactory
			XMLEventFactory eventFactory = XMLEventFactory.newInstance();
			XMLEvent end = eventFactory.createDTD("\n");
			// Create and write Start Tag
			StartDocument startDocument = eventFactory.createStartDocument();
			eventWriter.add(startDocument);

			// Create manifest node name open tag with relevant attributes	
			attributeList = new ArrayList<Attribute>();
			attributeList.add(eventFactory.createAttribute("identifier", title));
			attributeList.add(eventFactory.createAttribute("version", MANIFEST_NODE_VERSION));
			attributeList.add(eventFactory.createAttribute("xmlns", MANIFEST_XMLNS_IMSCP));
			attributeList.add(eventFactory.createAttribute("xmlns:adlcp", MANIFEST_XSI_ADLCP_SCHEMALOC));
			attributeList.add(eventFactory.createAttribute("xmlns:xsi",MANIFEST_XMLNS_XSI));
			attributeList.add(eventFactory.createAttribute("xsi:schemalocation", MANIFEST_XSI_IMSCP_SCHEMALOC 
															+ "        " + MANIFEST_XSI_IMSMD_SCHEMALOC  
															+ "        " + MANIFEST_XSI_ADLCP_SCHEMALOC));
		    StartElement manifestStartElem = eventFactory.createStartElement("", "",  MANIFEST_NODE_NAME, attributeList.iterator(), attributeList.iterator());
		    eventWriter.add(manifestStartElem);
			eventWriter.add(end);
			
			
			// Write the different nodes
			// Create Organization Manifest component information
			attributeList = new ArrayList<Attribute>();
			attributeList.add(eventFactory.createAttribute("default", SCO_INSTITUTION));
			StartElement organizationsStartElem = eventFactory.createStartElement("", "",  MANIFEST_ORG_NODE_NAME, attributeList.iterator(), attributeList.iterator());
		    eventWriter.add(organizationsStartElem);
			eventWriter.add(end);
				// Create the organization element node
				attributeList = new ArrayList<Attribute>();
				attributeList.add(eventFactory.createAttribute("identifier", SCO_INSTITUTION));
				StartElement organizationStartElem = eventFactory.createStartElement("", "",  MANIFEST_ORG_ELEM_NAME, attributeList.iterator(), attributeList.iterator());
			    eventWriter.add(organizationStartElem);
				eventWriter.add(end);
				createNode(eventWriter, MANIFEST_TITLE, title);
					//TODO Not sure if more than one 'item' is handled by the LILA SCORM engine
					attributeList = new ArrayList<Attribute>();
					attributeList.add(eventFactory.createAttribute("identifier", "item1"));
					attributeList.add(eventFactory.createAttribute("identifierref", htmlFile.getName())); //TODO remove the .html suffix from getName()
					StartElement itemStartElem = eventFactory.createStartElement("", "",  MANIFEST_ITEM, attributeList.iterator(), attributeList.iterator());
				    eventWriter.add(itemStartElem);
					eventWriter.add(end);
					createNode(eventWriter, MANIFEST_TITLE, title);  
					EndElement itemEndElem = eventFactory.createEndElement("", "",  MANIFEST_ITEM);
				    eventWriter.add(itemEndElem);
					eventWriter.add(end);
				EndElement organizationEndElem = eventFactory.createEndElement("", "", MANIFEST_ORG_ELEM_NAME);
				eventWriter.add(organizationEndElem);
				eventWriter.add(end);
			EndElement organizationsEndElem = eventFactory.createEndElement("", "",  MANIFEST_ORG_NODE_NAME);
			eventWriter.add(organizationsEndElem);
			eventWriter.add(end);
			
			
			//Create Resources section - File References
			for(int i = 0; i < assets.size(); i++)
			{
				File assetFile = (File)assets.toArray()[i];
				createNode(eventWriter, "File", assetFile.getPath());
			}

			eventWriter.add(eventFactory.createEndElement("", "", MANIFEST_NODE_NAME));
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
