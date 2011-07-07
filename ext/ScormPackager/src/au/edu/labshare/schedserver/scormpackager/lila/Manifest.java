/**
 * SAHARA Scheduling Server
 *
 * Manfiest Class. 
 * This is the structure of the Manifest as prescribed by SCORM 2004 standard:
 * http://en.wikipedia.org/wiki/Sharable_Content_Object_Reference_Model
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
 */
package au.edu.labshare.schedserver.scormpackager.lila;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import au.edu.labshare.schedserver.scormpackager.IManifest;
import au.edu.labshare.schedserver.scormpackager.manifest.Dependency;
import au.edu.labshare.schedserver.scormpackager.manifest.MetaData;
import au.edu.labshare.schedserver.scormpackager.manifest.Organization;
import au.edu.labshare.schedserver.scormpackager.manifest.Resource;
import au.edu.labshare.schedserver.scormpackager.manifest.ResourceFile;
import au.edu.labshare.schedserver.scormpackager.utilities.ScormUtilities;

public class Manifest implements IManifest
{
	public static final String GENERIC_IDENTIFER = "RigSCO"; //TODO: Should be able to change this title.
	public static final String SCHEMA_VERSION = "1.0";		
 	
	public static final String ITEM_NAME = "item";
	public static final String HTML_EXT = "html";
	public static final String NAMESPACE = "http://labshare.edu.au";
	
	MetaData metadata;
	ArrayList<Organization> organizations;
	ArrayList<Resource> resources;
	ArrayList<IManifest> submanifests;
	
	public Manifest()
	{
		metadata = new MetaData();
		organizations = new ArrayList<Organization>();
		resources = new ArrayList<Resource>();
		submanifests = new ArrayList<IManifest>();
	}

	@Override
	public MetaData getMetaData() 
	{
		return metadata;
	}

	@Override
	public Collection<Organization> getOrganizations() 
	{
		return organizations;
	}

	@Override
	public Collection<Resource> getResources() 
	{
		return resources;
	}

	@Override
	public Collection<IManifest> getSubManifests() 
	{
		return submanifests;
	}

	@Override
	public void setMetaData(MetaData metadata) 
	{
		this.metadata = metadata; 
	}

	@Override
	public void setOrganizations(Collection<Organization> organizations) 
	{
		this.organizations = (ArrayList<Organization>) organizations;
	}

	@Override
	public void setResources(Collection<Resource> resources) 
	{
		this.resources = (ArrayList<Resource>) resources;
	}

	@Override
	public void setSubManifests(Collection<IManifest> submanifests) 
	{
		this.submanifests = (ArrayList<IManifest>) submanifests;
	}

	@Override
	public void addOrganization(Organization organization) 
	{
		this.organizations.add(organization);
	}

	@Override
	public void addResource(Resource resource) 
	{
		this.resources.add(resource);
	}

	@Override
	public void addSubManifest(IManifest submanifest) 
	{
		this.submanifests.add(submanifest);
	}
	
	@Override
	public Collection<Organization> generateOrganisations(String[] institutions, String[] titles, File[] items) 
	{
		//Cycle through the institutions and titles and items to generate the 
		//<organization> and <item> elements
		Organization organization = null;
		String filenameExt = null;
		String filenameWithoutExtension = null;
		
		for(int i = 0; i < institutions.length; i++)
		{
			organization = new Organization();
			organization.setID(institutions[i]);
			organization.setTitle(Manifest.GENERIC_IDENTIFER);
			
			for(int j = 0; j < items.length; j++)
			{
				//Extrapolate the name from the items and pull out the *.html. Check that it is a html file first
				String filename = items[j].getName(); 
				
				filenameWithoutExtension = ScormUtilities.getFileNameWithoutExtension(filename);
                filenameExt = ScormUtilities.getFileExtension(filename);
				
				//Assign the filename without extension to the identifierref=attribute and itemX to identifier=attribute
				if(filenameExt.equals(HTML_EXT)) 
					organization.addItem(titles[j], ITEM_NAME + Integer.toString(j+1), filenameWithoutExtension);
			}
			
			organizations.add(organization);
		}
		
	
		return organizations;
	}

	@Override
	public Collection<Resource> generateResources(File[] files) 
	{
		//Initialise variables
		Resource resource = null;
		ResourceFile resourceFile = null;
		Dependency  dependency = null;
		String filepath   = null;
		String filename   = null;
		String filenameExt = null;
		String filenameWithoutExtension = null;
		
		//Place values into respective sections
		for(int i = 0; i < files.length; i++)
		{	
			filepath = files[i].getPath(); //Assumes that the path is a relative path e.g. lib/random.jar
			filename = files[i].getName();
			
			filenameExt = ScormUtilities.getFileExtension(filename);
			filenameWithoutExtension = ScormUtilities.getFileNameWithoutExtension(filename);
			
			//Try to decipher the values to fill in <resource> tag/element.
			resource = new Resource(filename); //use the filename to place as the identifier
			resourceFile = new ResourceFile();
			
			//If we see a file that is *.html we treat as a SCO
			if(filenameExt.equals(HTML_EXT))
			{
				//1. adlscp:scormtype="sco"
				resource.setScormType(Resource.SCORMTYPE_SCO);
				
				//2. ResourceFile href="XXX.html" 
				resourceFile.setFileReference(filepath);
				
				//3. Dependency identifierref="stub"
				dependency = new Dependency();
				dependency.setIdentifierRef(Dependency.LILA_SCO_IDENTIFIERREF);
				resource.addDependency(dependency);
			}
			else //Treat as Asset
			{
				//1. adlcp:scomtype="asset"  
				resource.setScormType(Resource.SCORMTYPE_ASSET);
				
				//2. ResourceFile href="lmsstub.js"
				resourceFile.setFileReference(ResourceFile.LILA_ASSET_IDENTIFIERREF);
				
				//3. No Dependencies - Do nothing because resource is new object. 
			}		
			
			//Certain attributes are common to both - add these in:
			//1. type="webcontent"
			resource.setType(Resource.LILA_TYPE);
			
			//2. href=filename
			resource.setHRef(filename);
			
			//3. ResourceFile href=filename
			resourceFile.setFileReference(filepath);
			resource.addPath(resourceFile);
			
			//4. identifer=filenameWithoutExtension
			resource.setIdentifier(filenameWithoutExtension);
			
			//Add the resources to the collection
			resources.add(resource);
		}
		
		return resources;
	}

	@Override
	public Collection<IManifest> generateSubManifests() 
	{
		// TODO Auto-generated method stub
		return null;
	}
}
