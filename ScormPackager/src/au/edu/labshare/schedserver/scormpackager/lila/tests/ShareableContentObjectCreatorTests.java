package au.edu.labshare.schedserver.scormpackager.lila.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import au.edu.labshare.schedserver.scormpackager.lila.ShareableContentObjectCreator;

public class ShareableContentObjectCreatorTests 
{
	/* Variables used for testing */
	String manifest;
	Collection<File> content;
	
	
	/* These variables are pertaining to the file list of */
	String fileName = "../../../../../../imsmanifest.xml"; 
	String contentFiles;
	
	@Before
	public void Setup()
	{
		manifest = fileName;
		content = new LinkedList<File>();
	}
	
	
	@Test
	public void testCreatePIFWithManifest() 
	{
		//Invoke the creation of the PIF 
		ShareableContentObjectCreator shrContentObj = new ShareableContentObjectCreator();
		assertNotNull((shrContentObj.createPIF(manifest, content)));
	}
	@Test
	public void testCreatePIFEmptyManifest() 
	{
		//Invoke the creation of the PIF 
		ShareableContentObjectCreator shrContentObj = new ShareableContentObjectCreator();
		assertNotNull((shrContentObj.createPIF(null, content)));
		
		//fail("Not yet implemented");
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
