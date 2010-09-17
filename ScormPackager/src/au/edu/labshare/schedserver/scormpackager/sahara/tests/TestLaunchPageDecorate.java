package au.edu.labshare.schedserver.scormpackager.sahara.tests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import biz.source_code.miniTemplator.MiniTemplator;

import au.edu.labshare.schedserver.scormpackager.sahara.ILaunchPage;
import au.edu.labshare.schedserver.scormpackager.sahara.LaunchPage;
import au.edu.labshare.schedserver.scormpackager.sahara.BlurbDecorator;
import au.edu.labshare.schedserver.scormpackager.sahara.PictureDecorator;
import au.edu.labshare.schedserver.scormpackager.sahara.TitleDecorator;

public class TestLaunchPageDecorate 
{
	ILaunchPage decoratedPage;
	
	private static final String TEMPLATE_DOCUMENT = "resources/templates/launchpage/launchpagetemplate.html";
	
	@Before
	public void Setup()
	{
		decoratedPage = new BlurbDecorator(new PictureDecorator(new TitleDecorator(new LaunchPage())));
	}
	
	@Test
	public void testGenerateFile()
	{
		
		//Create the MiniTemplate in order to decorate it.
		MiniTemplator templator = null;
		try
		{
			templator = new MiniTemplator(TEMPLATE_DOCUMENT);
		}
		catch(IOException e)
		{
			e.printStackTrace(); 
		}
		
		decoratedPage.render(templator);

		//Use mocks to see if render is invoked correctly.s
		fail("Not implemented yet");
	}

	@Test
	public void testFileName()
	{
		assertTrue(new String("ExampleExperiment.zip").equals(decoratedPage.getLaunchPageName()));
	}
}
