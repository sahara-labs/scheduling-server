package au.edu.labshare.schedserver.scormpackager.lila;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;

//Needed for XML generation
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

//Needed for constructing the Manifest in object format
import au.edu.labshare.schedserver.scormpackager.lila.Manifest;
import au.edu.labshare.schedserver.scormpackager.manifest.Dependency;
import au.edu.labshare.schedserver.scormpackager.manifest.Item;
import au.edu.labshare.schedserver.scormpackager.manifest.MetaData;
import au.edu.labshare.schedserver.scormpackager.manifest.Organization;
import au.edu.labshare.schedserver.scormpackager.manifest.Resource;
import au.edu.labshare.schedserver.scormpackager.manifest.ResourceFile;

public class ManifestXMLDecorator 
{
	private Manifest     imsmanifest;
	private Dependency   depedency;
	private Item         item;
	private MetaData     metadata;
	private Organization organization;
	private Resource     resource;
	private ResourceFile resourceFile;
	
	//TODO Should place in properties file
	private static final String SCO_INSTITUTION = "UTS";
	
	//The name of the manifest. This should never change
	public static final String MANFEST_NAME = "imsmanfest.xml";
	
	//Properties that are associated with the imsmanifest file
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

	public ManifestXMLDecorator()
	{
		imsmanifest = new Manifest();
	}
	
	public ManifestXMLDecorator(Manifest imsmanifest)
	{
		this.imsmanifest = imsmanifest;
	}
	
	/**
	 * Generates the lmsmanifest.xml file. This is not going to be public
	 * 
	 * Based on: http://www.vogella.de/articles/JavaXML/article.html
	 * @param title
	 * @param assets. Assumption 1st collection element is the *.html file to be rendered 
	 */
	public String decorateManifest(String title, Collection<File> assets)
	{
		XMLEventFactory eventFactory = null;
		XMLEventWriter  eventWriter  = null;
		XMLEvent        end          = null;
		String 			zipFileName  = null;
		
		// TODO Need to process the title and replace the name with underscores
		zipFileName  = removeWhiteSpace(title);
		
		//Generate the manifest file imsmanifest.xml
		try
		{
			// Create a XMLOutputFactory
			XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
			// Create XMLEventWriter
			eventWriter = outputFactory.createXMLEventWriter(new FileOutputStream(MANFEST_NAME));
			// Create a EventFactory
			eventFactory = XMLEventFactory.newInstance();
			end = eventFactory.createDTD("\n");
			// Create and write Start Tag
			StartDocument startDocument = eventFactory.createStartDocument();
			eventWriter.add(startDocument);
			//return null;

			//Invoke the following to generate the imsmanifest.xml file:
			//1. decorateManifestHeader()
			//2. decorateOrganizations()
			//3. decoreateResources()
			decorateManifestHeader(title, eventFactory, eventWriter, end);
			decorateOrganizations(title, assets, eventFactory, eventWriter, end);
			decorateResources(title, assets, eventWriter);
			
			//Close off the XML Manifest node
			eventWriter.add(eventFactory.createEndElement("", "", MANIFEST_NODE_NAME));
			eventWriter.add(end);
			eventWriter.add(eventFactory.createEndDocument());
			eventWriter.close();		
		}
		catch(XMLStreamException e)
		{
			e.printStackTrace(); //TODO replace with SchedServer Logger.
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace(); //TODO replace with SchedServer Logger.
		}
		
		return null; //TODO Need to actually return the path of the file in question. 
	}
	
	private void decorateManifestHeader(String title, XMLEventFactory eventFactory, XMLEventWriter eventWriter, XMLEvent end)
	{
		ArrayList<Attribute> attributeList = null;
		
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
	    try 
	    {
			eventWriter.add(manifestStartElem);
			eventWriter.add(end);
		} 
	    catch (XMLStreamException e) 
	    {
			e.printStackTrace();	// TODO Replace with SchedServer Logger 
		}
	}
	
	private void decorateOrganizations(String title, Collection<File> assets, XMLEventFactory eventFactory, XMLEventWriter eventWriter, XMLEvent end)
	{
		ArrayList<Attribute> attributeList = null;
		File htmlFile = (File)assets.toArray()[0];
		
		// Create Organization Manifest component information
		attributeList = new ArrayList<Attribute>();
		attributeList.add(eventFactory.createAttribute("default", SCO_INSTITUTION));
		StartElement organizationsStartElem = eventFactory.createStartElement("", "",  MANIFEST_ORG_NODE_NAME, attributeList.iterator(), attributeList.iterator());
	    try 
	    {
			eventWriter.add(organizationsStartElem);
			eventWriter.add(end);
		} 
	    catch (XMLStreamException e) 
	    {
			e.printStackTrace(); // TODO Replace with SchedServer Logger 
		}
		
		// Create the organization element node
		attributeList = new ArrayList<Attribute>();
		attributeList.add(eventFactory.createAttribute("identifier", SCO_INSTITUTION));
		StartElement organizationStartElem = eventFactory.createStartElement("", "",  MANIFEST_ORG_ELEM_NAME, attributeList.iterator(), attributeList.iterator());
	    try 
	    {
			eventWriter.add(organizationStartElem);
			eventWriter.add(end);
		} 
	    catch (XMLStreamException e) 
	    {
			e.printStackTrace(); // TODO Replace with SchedServer Logger 
		}
		createNode(eventWriter, MANIFEST_TITLE, title);

		//TODO Not sure if more than one 'item' is handled by the LILA SCORM engine
		attributeList = new ArrayList<Attribute>();
		attributeList.add(eventFactory.createAttribute("identifier", "item1"));
		attributeList.add(eventFactory.createAttribute("identifierref", htmlFile.getName())); //TODO remove the .html suffix from getName()
		StartElement itemStartElem = eventFactory.createStartElement("", "",  MANIFEST_ITEM, attributeList.iterator(), attributeList.iterator());
	    try 
	    {
			eventWriter.add(itemStartElem);
			eventWriter.add(end);
		} 
	    catch (XMLStreamException e) 
	    {
			e.printStackTrace(); // TODO Replace with SchedServer Logger 
		}
		createNode(eventWriter, MANIFEST_TITLE, title);  
		EndElement itemEndElem = eventFactory.createEndElement("", "",  MANIFEST_ITEM);
	    try 
	    {
			eventWriter.add(itemEndElem);
			eventWriter.add(end);
		} 
	    catch (XMLStreamException e) 
		{
			e.printStackTrace(); 
		}
		
		
		// Close the organization element node
		EndElement organizationEndElem = eventFactory.createEndElement("", "", MANIFEST_ORG_ELEM_NAME);
		try 
		{
			eventWriter.add(organizationEndElem);
			eventWriter.add(end);
		} 
		catch (XMLStreamException e) 
		{
			e.printStackTrace(); // TODO Replace with SchedServer Logger
		}
		
		EndElement organizationsEndElem = eventFactory.createEndElement("", "",  MANIFEST_ORG_NODE_NAME);
		try 
		{
			eventWriter.add(organizationsEndElem);
			eventWriter.add(end);
		} 
		catch (XMLStreamException e) 
		{
			e.printStackTrace(); // TODO Replace with SchedServer Logger
		}
	}

	private void decorateResources(String title, Collection<File> assets, XMLEventWriter eventWriter)
	{
		//TODO Create Resources section - File References
		for(int i = 0; i < assets.size(); i++)
		{
			File assetFile = (File)assets.toArray()[i];
			createNode(eventWriter, "File", assetFile.getPath());
		}
	}
	
	
	/**
	 * Takes the value of a name to produce an underscore version of the name.
	 * This is to allow naming consistency.
	 * 
	 * This is from: http://www.vogella.de/articles/JavaXML/article.html
	 * @param eventWriter
	 * @param name 
	 * @param value
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
