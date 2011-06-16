/**
 * SAHARA Scheduling Server
 *
 * Manfiest Unit Tests.
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
package au.edu.labshare.schedserver.scormpackager.lila.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;


import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import au.edu.labshare.schedserver.scormpackager.lila.Manifest;
import au.edu.labshare.schedserver.scormpackager.manifest.Dependency;
import au.edu.labshare.schedserver.scormpackager.manifest.Organization;
import au.edu.labshare.schedserver.scormpackager.manifest.Resource;

public class ManifestTests 
{
	Manifest   manifest; 
	File[]     testItems;
	String[]   institutionNames;
	String[]   titles;
	
	File       testItem;   //1st test item for applet.html
	File       testItem2;  //2nd test item for applet2.html
	File       testItem3;  //3rd test item for applet3.html
	File       testResourceFile;
	
	/* These variables are pertaining to the file list of */
	private static final String COMPLIANT_FILE_PATH = "../../../../../../../../test/resources/lila/";
	//private static final String NON_COMPILANT_PATH = "../../../../../../../../test/resources/noncompliant/";
	//private static final String EXPECTED_MANIFEST_NAME = "imsmanifest.xml";
	private static final String ITEM1_NAME = "applet.html";
	private static final String ITEM2_NAME = "applet2.html";
	private static final String ITEM3_NAME = "applet3.html";
	private static final String ATTACHMENT_NAME = "lib/uts_tower.jpeg";
	private static final String STUB_NAME = "lmsstub.js";
	private static final String STUB_IDENTIFIERREF = "stub";
	
	@Before
	public void Setup()
	{
		manifest = new Manifest();
		
		//Item as HTML files that are from the directory
		testItem = new File(COMPLIANT_FILE_PATH + ITEM1_NAME);
		testItem2 = new File(COMPLIANT_FILE_PATH + ITEM2_NAME);
		testItem3 = new File(COMPLIANT_FILE_PATH + ITEM3_NAME);	
		
		//Other resources to be added to the system
	}
	
	@Test
	public void testGenerateOrganisations() 
	{
		Organization expectedOrganization = null;
		testItems = new File[3];
		titles    = new String[3];
		institutionNames = new String[1];
		
		//Compare the actual vs. expected values.
		Collection<Organization> actualValues;
		ArrayList<Organization> expectedValues;
		
		
		//Items to be added for testing
		testItems[0] = testItem;
		testItems[1] = testItem2;
		testItems[2] = testItem3;
		
		titles[0] = "Ideal Gas Experiment";
		titles[1] = "Another Experiment";
		titles[2] = "";
		
		institutionNames[0] = "UTS";
		
		//Setup the expected values
		expectedValues = new ArrayList<Organization>();
		expectedOrganization = new Organization();
		expectedOrganization.setID("UTS");
		expectedOrganization.addItem(titles[0], "item1", "applet");
		expectedValues.add(expectedOrganization);
		expectedOrganization.addItem(titles[1], "item2", "applet2");
		expectedValues.add(expectedOrganization);
		expectedOrganization.addItem(titles[2], "item3", "applet3");
		expectedValues.add(expectedOrganization);

		actualValues = manifest.generateOrganisations(institutionNames, titles, testItems); 

		assertSame(expectedValues.get(0).getID(), ((ArrayList<Organization>)actualValues).get(0).getID());
		assertTrue(expectedValues.get(0).getItem(0).getReference().equals(((ArrayList<Organization>)actualValues).get(0).getItem(0).getReference()));
		assertTrue(expectedValues.get(0).getItem(1).getID().equals(((ArrayList<Organization>)actualValues).get(0).getItem(1).getID()));
		assertTrue(expectedValues.get(0).getItem(2).getTitle().equals(((ArrayList<Organization>)actualValues).get(0).getItem(2).getTitle()));
	}
	
	@Test 
	public void testGenerateResources()
	{
		//Compare the actual vs. expected values.
		Collection<Resource> actualValues;
		ArrayList<Resource> expectedValues;
		Resource expectedResource;
		Dependency expectedDependency;
		
		//Create the expected resources
		expectedValues = new ArrayList<Resource>();
		expectedResource = new Resource("applet");
		expectedDependency = new Dependency();
		
		expectedDependency.setIdentifierRef(STUB_IDENTIFIERREF);
		expectedResource.addDependency(expectedDependency);
		expectedResource.setHRef(testItem.getName());
		expectedResource.setScormType(Resource.SCORMTYPE_SCO);
		expectedResource.setType(Resource.LILA_TYPE);
		expectedValues.add(expectedResource);
		
		expectedResource = new Resource("applet2");
		expectedResource.addDependency(expectedDependency);
		expectedResource.setHRef(testItem2.getName());
		expectedResource.setScormType(Resource.SCORMTYPE_SCO);
		expectedResource.setType(Resource.LILA_TYPE);
		expectedValues.add(expectedResource);
		
		expectedResource = new Resource("applet3");
		expectedResource.addDependency(expectedDependency);
		expectedResource.setHRef(testItem3.getName());
		expectedResource.setScormType(Resource.SCORMTYPE_SCO);
		expectedResource.setType(Resource.LILA_TYPE);
		expectedValues.add(expectedResource);
		
		//Create the Files that are needed for this
		//First associate the SCOs
		testItems = new File[5]; //Files include: 
								 //->*.html for SCOs
								 //->lmsstub.js for stub (asset)
								 //->lib/* for assets
		
		testItems[0] = testItem;
		testItems[1] = testItem2;
		testItems[2] = testItem3;
		
		//Secondly create the assets
		File assetFile1 = new File(STUB_NAME);  //lmsstub.js
		File assetFile2 = new File(ATTACHMENT_NAME); //the jpeg file in lib directory
		testItems[3] = assetFile1;
		testItems[4] = assetFile2;
		
		expectedResource = new Resource("stub");
		expectedResource.setHRef(testItems[3].getName());
		expectedResource.setScormType(Resource.SCORMTYPE_ASSET);
		expectedResource.setType(Resource.LILA_TYPE);
		expectedValues.add(expectedResource);
		
		expectedResource = new Resource("uts_tower");
		expectedResource.setHRef(testItems[4].getName());
		expectedResource.setScormType(Resource.SCORMTYPE_ASSET);
		expectedResource.setType(Resource.LILA_TYPE);
		expectedValues.add(expectedResource);
		
		
		actualValues = manifest.generateResources(testItems);
		
		//Test Resources designated as sco(s)
		assertTrue(expectedValues.get(0).getHRef().equals(((ArrayList<Resource>)actualValues).get(0).getHRef()));
		assertTrue(expectedValues.get(1).getIdentifier().equals(((ArrayList<Resource>)actualValues).get(1).getIdentifier()));
		assertTrue(expectedValues.get(2).getScormType().equals(((ArrayList<Resource>)actualValues).get(2).getScormType()));
		
		//Test Resources designated as asset(s)
		assertTrue(expectedValues.get(3).getScormType().equals(((ArrayList<Resource>)actualValues).get(3).getScormType()));
		assertTrue(expectedValues.get(4).getIdentifier().equals(((ArrayList<Resource>)actualValues).get(4).getIdentifier()));
	}
	
	@Test
	public void testGenerateSubManifests()
	{
		//TODO We want to fail this until we need to use it. At the moment not used. 
		fail("Not implemented yet");
	}
}
