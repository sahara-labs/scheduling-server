package au.edu.labshare.schedserver.scormpackager.lila.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import au.edu.labshare.schedserver.scormpackager.lila.ShareableContentObjectCreator;
import au.edu.labshare.schedserver.scormpackager.utilities.ScormUtilities;
import au.edu.labshare.schedserver.scormpackager.lila.Manifest;

public class ShareableContentObjectCreatorTests 
{
	/* Variables used for testing */
	String manifest;
	Collection<File> content;
	
	
	/* These variables are pertaining to the file list of */
	String fileNameManifest = "../../../../../../imsmanifest.xml"; 
	String title = "LabShare";
	//String resourcesPath = "../../../../../../../../test/resources";
	String resourcesPath = "test/resources";
	String contentLilaPath = resourcesPath + "/lila/";
	String contentNonCompliantPath = resourcesPath + "/noncompliant/";
	
	@Before
	public void Setup()
	{
		content = new LinkedList<File>();
		
		File LilaDirectory = new File(contentLilaPath);
		File LilaLibDirectory = new File(contentLilaPath+"/lib");
		
		File[] listOfLilaSCOFiles = LilaDirectory.listFiles();
		File[] listofLilaAssetFiles = LilaLibDirectory.listFiles();
		
		//Add all html files as SCOs
		for(int i = 0; i < listOfLilaSCOFiles.length; i++) 
		{
			//Check that the file has an extension with *.html and add it to the content as it is a sco
			if (listOfLilaSCOFiles[i].isFile() 
			&& ScormUtilities.getFileExtension(listOfLilaSCOFiles[i].getName()).equals(Manifest.HTML_EXT)) 
			{
				content.add(listOfLilaSCOFiles[i]); //Assume in test that *.html are scormtype:sco
			} 
		}
		 
		//Add the assets in lib directory.
		for(int i = 0; i < listofLilaAssetFiles.length; i++)
		{
			//If file add it as it is an asset. 
			if (listofLilaAssetFiles[i].isFile()) 
			{
				content.add(listofLilaAssetFiles[i]); //Assume in test that all files are scormtype:asset
			} 
		}
		
		 //Add the lmsstub.js
		 content.add(new File(contentLilaPath + "/lmsstub.js")); 
	}
	
	@Test
	public void testCreatePIFWithManifest() 
	{
		//Invoke the creation of the PIF 
		ShareableContentObjectCreator shrContentObj = new ShareableContentObjectCreator();
		assertNotNull((shrContentObj.createPIF(title, content)));
	}
	
	@Test
	public void testCreatePIFEmptyManifest() 
	{
		//Invoke the creation of the PIF 
		ShareableContentObjectCreator shrContentObj = new ShareableContentObjectCreator();
		assertNotNull((shrContentObj.createPIF(null, content)));
	}

	@Test
	public void testCreateManifest()
	{
		fail("Not yet implemented");
	}
	
	@Test
	public void testValidateDTDFile() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testCreateSCO() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testValidateApplicationProfile() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testValidateManifest() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testValidateMetaData() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testValidatePresentationNavigationInfo() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testValidateSequencing() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testValidateLMSConnection() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testValidateContent() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testValidateLMS() 
	{
		fail("Not yet implemented");
	}

}
