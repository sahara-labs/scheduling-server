/**
 * SAHARA Scheduling Server
 *
 * Manfiest XML Decorator (structures the XML using Decorator pattern).
 * This actually generates the stipulated lmsmanifest.xml file that is needed by the SCO.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2010, University of Technology, Sydney
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright 
 *    notice, this list of conditions and the following disclaimer in the 
 *    documentation and/or other materials provided with the distribution.
 *  * Neither the name of the University of Technology, Sydney nor the names 
 *    of its contributors may be used to endorse or promote products derived from 
 *    this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE 
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, 
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE 
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * @author Herber Yeung
 * @date 4th November 2010
 * 
 * Modification: Added staxutils IndentingXMLEventWriter for pretty printing of imsmanifest.xml
 * @author Herbert Yeung
 * @date 18th November 2010
 */
package au.edu.labshare.schedserver.scormpackager.lila;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

//Needed for XML generation
import javanet.staxutils.IndentingXMLEventWriter;

import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.StartDocument;
import javax.xml.stream.events.StartElement;

//Needed for constructing the Manifest in object format
import au.edu.labshare.schedserver.scormpackager.lila.Manifest;
import au.edu.labshare.schedserver.scormpackager.manifest.Item;
import au.edu.labshare.schedserver.scormpackager.manifest.Organization;
import au.edu.labshare.schedserver.scormpackager.manifest.Resource;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;

public class ManifestXMLDecorator 
{	
	//TODO Should place in properties file
	private static final String SCO_INSTITUTION = "UTS";
	
	//The name of the manifest. This should never change
	public static final String MANFEST_NAME = "imsmanifest.xml";
	
	//Properties that are associated with the imsmanifest file
	public static final String MANIFEST_NODE_NAME = "manifest";	
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
	public static final String SCHEMA_EXT = "xsd";
	public static final String RESOURCES_PATH = "resources";
	
	//Log any error
	private Logger saharaLogger;
	
	public ManifestXMLDecorator(Logger logger)
	{
		saharaLogger = logger;
	}
	
	public ManifestXMLDecorator()
	{
		saharaLogger = au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator.getLogger();
	}
	
	/**
	 * Generates the lmsmanifest.xml file. This is not going to be public
	 * 
	 * Based on: http://www.vogella.de/articles/JavaXML/article.html
	 * @param Manifest. This param is compulsory in order to decorate the Manifest Object. 
	 */
	public String decorateManifest(Manifest manifest, String outputFilePath)
	{
		XMLEventFactory eventFactory = null;
		XMLEventWriter  eventWriter  = null;
			
		//Generate the manifest file imsmanifest.xml
		try
		{
			// Create a XMLOutputFactory
			XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
			
			// Create XMLEventWriter
			eventWriter = outputFactory.createXMLEventWriter(new FileOutputStream(outputFilePath + MANFEST_NAME));
			eventWriter = new IndentingXMLEventWriter(eventWriter); /* Pretty printing using stax-utils */
			
			// Create a EventFactory
			eventFactory = XMLEventFactory.newInstance();
			
			// Create and write Start Tag
			StartDocument startDocument = eventFactory.createStartDocument();
			eventWriter.add(startDocument);
				
			//Invoke the following to generate the imsmanifest.xml file:
			//1. decorateManifestHeader()
			//2. decorateOrganizations()
			//3. decoreateResources()
			decorateManifestHeader(manifest, eventWriter);
			decorateOrganizations(manifest, eventWriter);
			decorateResources(manifest, eventWriter);

			//Close off the XML Manifest node
			eventWriter.add(eventFactory.createEndElement("", "", MANIFEST_NODE_NAME));
			eventWriter.flush();
			eventWriter.add(eventFactory.createEndDocument());
			eventWriter.close();		
		}
		catch(XMLStreamException e)
		{
			e.printStackTrace();
			//Log any exception output
            //this.saharaLogger.debug("Received " + this.getClass().getName() + e.toString());
		} 
		catch (FileNotFoundException e) 
		{
			//Log any exception output
            this.saharaLogger.debug("Received " + this.getClass().getName() + e.toString());
		}
		
		return outputFilePath + MANFEST_NAME;
	}
	
	private void decorateManifestHeader(Manifest manifest, XMLEventWriter eventWriter)
	{
		ArrayList<Attribute> attributeList = null;
		ArrayList<Namespace> namespaceList = null;
		XMLEventFactory eventFactory = null;
				
		eventFactory = XMLEventFactory.newInstance();

		StartElement manifestStartElem = eventFactory.createStartElement("", "",  MANIFEST_NODE_NAME);
	    try 
	    {
			eventWriter.add(manifestStartElem);
			eventWriter.flush();
		} 
	    catch (XMLStreamException e) 
	    {
	    	//Log any exception output
            this.saharaLogger.debug("Received " + this.getClass().getName() + e.toString());
		}
		
		// Create manifest node name open tag with relevant attributes	
		attributeList = new ArrayList<Attribute>();
		namespaceList = new ArrayList<Namespace>();
		//attributeList.add(eventFactory.createAttribute("identifier", manifest.getMetaData().getSchemaValue("identifier")));

		try 
		{
			eventWriter.add(eventFactory.createAttribute("identifier", manifest.getMetaData().getSchemaValue("identifier")));
		} 
		catch (XMLStreamException e) 
		{
			//Log any exception output
            this.saharaLogger.debug("Received " + this.getClass().getName() + e.toString());
		}
		
		attributeList.add(eventFactory.createAttribute("version", manifest.getMetaData().getSchemaValue("version")));
		attributeList.add(eventFactory.createAttribute("xmlns:adlcp", manifest.getMetaData().getSchemaValue("xmlns:adlcp")));
		attributeList.add(eventFactory.createAttribute("xmlns:xsi",manifest.getMetaData().getSchemaValue("xmlns:xsi")));
		attributeList.add(eventFactory.createAttribute("xsi:schemalocation", manifest.getMetaData().getSchemaValue("xsi:schemalocation")));
		namespaceList.add(eventFactory.createNamespace( manifest.getMetaData().getSchemaValue("xmlns"))); 
	}
	
	private void decorateOrganizations(Manifest manifest, XMLEventWriter eventWriter)
	{
		int i = 0;
		ArrayList<Attribute> attributeList = null;
		ArrayList<Namespace> namespaceList = null;
		XMLEventFactory eventFactory = null;
				
		eventFactory = XMLEventFactory.newInstance();
		
		// Create Organization Manifest component information
		// Create the attribute default="" for organizations node
		attributeList = new ArrayList<Attribute>();
		namespaceList = new ArrayList<Namespace>();
		attributeList.add(eventFactory.createAttribute("default", SCO_INSTITUTION));
		//namespaceList.add(eventFactory.createNamespace(Manifest.NAMESPACE));
		StartElement organizationsStartElem = eventFactory.createStartElement("", "",  MANIFEST_ORG_NODE_NAME, attributeList.iterator(), namespaceList.iterator());
	    try 
	    {
			eventWriter.add(organizationsStartElem); // Write the organizations start node to file
			eventWriter.flush();
		} 
	    catch (XMLStreamException e) 
	    {
	    	//Log any exception output
            this.saharaLogger.debug("Received " + this.getClass().getName() + e.toString());
		}

		//Add each organization
		for(Iterator<Organization> iterOrg = manifest.getOrganizations().iterator(); iterOrg.hasNext();)
		{
			// Create the organization element node
			attributeList = new ArrayList<Attribute>();
			attributeList.add(eventFactory.createAttribute("identifier", SCO_INSTITUTION));
			StartElement organizationStartElem = eventFactory.createStartElement("", "",  MANIFEST_ORG_ELEM_NAME, attributeList.iterator(), namespaceList.iterator());
		    try 
		    {
				eventWriter.add(organizationStartElem); // Write the <organization> start elem to file
				eventWriter.flush();
			} 
		    catch (XMLStreamException e) 
		    {
		    	//Log any exception output
	            this.saharaLogger.debug("Received " + this.getClass().getName() + e.toString());
			}
		    
			//Get the actual organization object to obtain information
			Organization organization = iterOrg.next();
		
			//Decorate the <title></title> nodes
			createNode(eventWriter, MANIFEST_TITLE, organization.getTitle());
			
			//Extract the title from the <item identifierref> atttribute.
		    for(Iterator<Item> iterItem = organization.getItemList().iterator(); iterItem.hasNext();)
		    {
		    	Item orgItem = iterItem.next();
		    	attributeList = new ArrayList<Attribute>();
				attributeList.add(eventFactory.createAttribute("identifier", "item" + Integer.toString(i)));
				attributeList.add(eventFactory.createAttribute("identifierref", orgItem.getReference())); 
				StartElement itemStartElem = eventFactory.createStartElement("", "",  MANIFEST_ITEM, attributeList.iterator(), namespaceList.iterator());
				try 
			    {
					eventWriter.add(itemStartElem);
					eventWriter.flush();
				} 
			    catch (XMLStreamException e) 
			    {
			    	//Log any exception output
		            this.saharaLogger.debug("Received " + this.getClass().getName() + e.toString()); 
				}
			    
			    //Extract the title from the <item identifierref> attribute. 
				createNode(eventWriter, MANIFEST_TITLE, orgItem.getTitle());  
				EndElement itemEndElem = eventFactory.createEndElement("", "",  MANIFEST_ITEM);
				
				try 
			    {
					eventWriter.add(itemEndElem);
					eventWriter.flush();
				} 
			    catch (XMLStreamException e) 
				{
			    	//Log any exception output
		            this.saharaLogger.debug("Received " + this.getClass().getName() + e.toString());
				}

				i++;
		    }
		    
		    //Close the organization element node
			EndElement organizationEndElem = eventFactory.createEndElement("", "", MANIFEST_ORG_ELEM_NAME);
			try 
			{
				eventWriter.add(organizationEndElem);
				eventWriter.flush();
			} 
			catch (XMLStreamException e) 
			{
				//Log any exception output
	            this.saharaLogger.debug("Received " + this.getClass().getName() + e.toString());
			}
		}
		
		EndElement organizationsEndElem = eventFactory.createEndElement("", "",  MANIFEST_ORG_NODE_NAME);
		try 
		{
			eventWriter.add(organizationsEndElem);
			eventWriter.flush();
		} 
		catch (XMLStreamException e) 
		{
			//Log any exception output
            this.saharaLogger.debug("Received " + this.getClass().getName() + e.toString());
		}
	}

	private void decorateResources(Manifest manifest, XMLEventWriter eventWriter)
	{
		ArrayList<Attribute> attributeList = null;
		ArrayList<Namespace> namespaceList = null;
		XMLEventFactory eventFactory = null;
		
		eventFactory = XMLEventFactory.newInstance();
				
		//Create the resources node
		StartElement resourcesStartElem = eventFactory.createStartElement("", "",  MANIFEST_RESOURCES_NODE_NAME);
		
		//namespaceList = new ArrayList<Namespace>();
		//namespaceList.add(eventFactory.createNamespace(Manifest.NAMESPACE));
		
		try 
	    {
	    	// Write the resources start node to file
			eventWriter.add(resourcesStartElem);
			eventWriter.flush();
		} 
	    catch (XMLStreamException e) 
	    {
	    	//Log any exception output
            this.saharaLogger.debug("Received " + this.getClass().getName() + e.toString());
		}
		
		
		//Create each separate resource section
		for(Iterator<Resource> iterResource = manifest.getResources().iterator(); iterResource.hasNext();)
		{
			Resource resourceManifest = iterResource.next();
			
			//Decorate the attributes: identifier=; type=; adlcp:scormtype=; href=;
			attributeList = new ArrayList<Attribute>();
			namespaceList = new ArrayList<Namespace>();
			attributeList.add(eventFactory.createAttribute("identifier", resourceManifest.getIdentifier()));
			attributeList.add(eventFactory.createAttribute("type", resourceManifest.getType()));
			attributeList.add(eventFactory.createAttribute("adlcp:scormtype", resourceManifest.getScormType()));
			attributeList.add(eventFactory.createAttribute("href", resourceManifest.getHRef()));
				
			StartElement resourceStartElem = eventFactory.createStartElement("", "",  MANIFEST_RESOURCE_ELEM_NAME, attributeList.iterator(), namespaceList.iterator());

			try 
		    {
				eventWriter.add(resourceStartElem);
				eventWriter.flush();
			} 
		    catch (XMLStreamException e) 
		    {
		    	//Log any exception output
	            this.saharaLogger.debug("Received " + this.getClass().getName() + e.toString()); 
			}
			
			//Add File node and HREF attribute
		    attributeList = new ArrayList<Attribute>();
		    attributeList.add(eventFactory.createAttribute("href", resourceManifest.getHRef()));
		    StartElement fileStartElem = eventFactory.createStartElement("", "",  MANIFEST_FILE_ELEM_NAME, attributeList.iterator(), namespaceList.iterator());
			
		    try 
		    {
				eventWriter.add(fileStartElem);
				eventWriter.flush();
			} 
		    catch (XMLStreamException e) 
		    {
		    	//Log any exception output
	            this.saharaLogger.debug("Received " + this.getClass().getName() + e.toString());
			}
		    
		    // Close the file element node
			EndElement fileEndElem = eventFactory.createEndElement("", "", MANIFEST_FILE_ELEM_NAME);
			try 
			{
				eventWriter.add(fileEndElem);
				eventWriter.flush();
			} 
			catch (XMLStreamException e) 
			{
				//Log any exception output
	            this.saharaLogger.debug("Received " + this.getClass().getName() + e.toString());
			}
		    
			//Create a Dependency node if it is a SCO
			if(resourceManifest.getScormType().equals(Resource.SCORMTYPE_SCO))
			{
				attributeList = new ArrayList<Attribute>();
				attributeList.add(eventFactory.createAttribute("identifierref", "stub"));
				StartElement dependencyStartElem = eventFactory.createStartElement("", "",  MANIFEST_DEPENDENCY_ELEM_NAME, attributeList.iterator(), namespaceList.iterator());
				 
				try 
				{
					eventWriter.add(dependencyStartElem);
					eventWriter.flush();
				} 
				catch (XMLStreamException e) 
				{
					//Log any exception output
		            this.saharaLogger.debug("Received " + this.getClass().getName() + e.toString());
				}
							
				
				 // Close the Dependency element node
				EndElement dependencyEndElem = eventFactory.createEndElement("", "", MANIFEST_DEPENDENCY_ELEM_NAME);
				try 
				{
					eventWriter.add(dependencyEndElem);
					eventWriter.flush();
				} 
				catch (XMLStreamException e) 
				{
					//Log any exception output
		            this.saharaLogger.debug("Received " + this.getClass().getName() + e.toString());
				}
			}
			
		    //Close the resource element tag
		    EndElement resourceEndElem = eventFactory.createEndElement("", "", MANIFEST_RESOURCE_ELEM_NAME);
			try 
			{
				eventWriter.add(resourceEndElem);
				eventWriter.flush();
			} 
			catch (XMLStreamException e) 
			{
				//Log any exception output
	            this.saharaLogger.debug("Received " + this.getClass().getName() + e.toString());
			}
		}
		
		//Add lmsstub.js at the end before closing off resources node!!!
		attributeList = new ArrayList<Attribute>();
		attributeList.add(eventFactory.createAttribute("identifier", "stub"));
		attributeList.add(eventFactory.createAttribute("type", Resource.LILA_TYPE));
		attributeList.add(eventFactory.createAttribute("adlcp:scormtype", Resource.SCORMTYPE_ASSET));
		StartElement lmsstubStartElem = eventFactory.createStartElement("", "",  MANIFEST_RESOURCE_ELEM_NAME, attributeList.iterator(), namespaceList.iterator());

		try 
	    {
			eventWriter.add(lmsstubStartElem);
			eventWriter.flush();
		} 
	    catch (XMLStreamException e) 
	    {
	    	//Log any exception output
            this.saharaLogger.debug("Received " + this.getClass().getName() + e.toString());
		}
		
	    //Add File node and HREF attribute
	    attributeList = new ArrayList<Attribute>();
	    attributeList.add(eventFactory.createAttribute("href", "lmsstub.js"));
	    StartElement fileStartElem = eventFactory.createStartElement("", "",  MANIFEST_FILE_ELEM_NAME, attributeList.iterator(), namespaceList.iterator());
		
	    try 
	    {
			eventWriter.add(fileStartElem);
			eventWriter.flush();
		} 
	    catch (XMLStreamException e) 
	    {
	    	//Log any exception output
            this.saharaLogger.debug("Received " + this.getClass().getName() + e.toString());
		}
	    
	    // Close the file element node
		EndElement fileEndElem = eventFactory.createEndElement("", "", MANIFEST_FILE_ELEM_NAME);
		try 
		{
			eventWriter.add(fileEndElem);
			eventWriter.flush();
		} 
		catch (XMLStreamException e) 
		{
			//Log any exception output
            this.saharaLogger.debug("Received " + this.getClass().getName() + e.toString());
		}
	    
	    //Close the resource node for lmsstub.js
	    EndElement lmsstubEndElem = eventFactory.createEndElement("", "", MANIFEST_RESOURCE_ELEM_NAME);
		try 
		{
			eventWriter.add(lmsstubEndElem);
			eventWriter.flush();
		} 
		catch (XMLStreamException e) 
		{
			//Log any exception output
            this.saharaLogger.debug("Received " + this.getClass().getName() + e.toString());
		}
	    
		//Close the resources node
		EndElement resourcesEndElem = eventFactory.createEndElement("", "", MANIFEST_RESOURCES_NODE_NAME);
		try 
		{
			eventWriter.add(resourcesEndElem);
			eventWriter.flush();
		} 
		catch (XMLStreamException e) 
		{
			//Log any exception output
            this.saharaLogger.debug("Received " + this.getClass().getName() + e.toString());
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

			// Create Start node
			StartElement sElement = eventFactory.createStartElement("", "", name);
			eventWriter.add(sElement);

			// Create Content
			Characters characters = eventFactory.createCharacters(value);
			eventWriter.add(characters);

			// Create End node
			EndElement eElement = eventFactory.createEndElement("", "", name);
			eventWriter.add(eElement);
			
			eventWriter.flush();
		}
		catch(XMLStreamException e)
		{
			//Log any exception output
            this.saharaLogger.debug("Received " + this.getClass().getName() + e.toString());
		}
	}
	
	//XML Schema adding for each SCO as defined by SCORM 1.2
	public static ArrayList<File> addXMLSchemas(String tmpLocationStore)
	{
		ArrayList<File> fileList = new ArrayList<File>();
		
		int len;
		byte buf[]=new byte[1024];
		File tmpFile = null;
		FileOutputStream fileOutStream = null;
		InputStream in = null;
		
		// Need to check where we are running in and get path 
		//File resourcesDirectory = null;
		
		try
		{
			//TODO: Should replace this - bit of a hack 
			//Write all the Schema files to a temporary location 
			in = ManifestXMLDecorator.class.getClassLoader().getResourceAsStream(ManifestXMLDecorator.RESOURCES_PATH + "/adlcp_rootv1p2.xsd");
			
			//Write the adlcp_rootv1p2.xsd schema 
			tmpFile = new File(tmpLocationStore + "adlcp_rootv1p2.xsd");
			fileList.add(tmpFile);
			fileOutStream = new FileOutputStream(tmpFile);

		    while((len=in.read(buf))>0)
		    	fileOutStream.write(buf,0,len);

		    in.close();
		    fileOutStream.close();
		    
		    //Write the ims_xml.xsd schema 
		    in = ManifestXMLDecorator.class.getClassLoader().getResourceAsStream(ManifestXMLDecorator.RESOURCES_PATH + "/ims_xml.xsd");
			tmpFile = new File(tmpLocationStore + "ims_xml.xsd");
			fileList.add(tmpFile);
			fileOutStream = new FileOutputStream(tmpFile);

		    while((len=in.read(buf))>0)
		    	fileOutStream.write(buf,0,len);

		    in.close();
		    fileOutStream.close();
		    
		    //Write the imscp_rootv1p1p2.xsd schema 
		    in = ManifestXMLDecorator.class.getClassLoader().getResourceAsStream(ManifestXMLDecorator.RESOURCES_PATH + "/imscp_rootv1p1p2.xsd");
			tmpFile = new File(tmpLocationStore + "imscp_rootv1p1p2.xsd");
			fileList.add(tmpFile);
			fileOutStream = new FileOutputStream(tmpFile);

		    while((len=in.read(buf))>0)
		    	fileOutStream.write(buf,0,len);

		    in.close();
		    fileOutStream.close();
		    
		    //Write the imscp_rootv1p2p1.xsd schema 
		    in = ManifestXMLDecorator.class.getClassLoader().getResourceAsStream(ManifestXMLDecorator.RESOURCES_PATH + "/imsmd_rootv1p2p1.xsd");
			tmpFile = new File(tmpLocationStore + "imsmd_rootv1p2p1.xsd");
			fileList.add(tmpFile);
			fileOutStream = new FileOutputStream(tmpFile);

		    while((len=in.read(buf))>0)
		    	fileOutStream.write(buf,0,len);

		    in.close();
		    fileOutStream.close();
			
			//resourcesDirectory = new File(ManifestXMLDecorator.RESOURCES_PATH);
			//resourcesDirectory = new File(resrcDirURL.getFile());
		}
		catch(Exception e)
		{
			e.printStackTrace(); //TODO: Need to replace with Sahara Logger			
		}
		
		/*File[] listOfSchemaFiles = resourcesDirectory.listFiles();
		
		//Add the schemas *.xsd to SCOs
		for(int i = 0; i < listOfSchemaFiles.length; i++) 
		{
			//Check that the file has an extension with *.xsd and add it
			if (listOfSchemaFiles[i].isFile() 
			&& ScormUtilities.getFileExtension(listOfSchemaFiles[i].getName()).equals(ManifestXMLDecorator.SCHEMA_EXT)) 
			{
				fileList.add(listOfSchemaFiles[i]);
			} 
		}*/
				
		return fileList;
	}
}
