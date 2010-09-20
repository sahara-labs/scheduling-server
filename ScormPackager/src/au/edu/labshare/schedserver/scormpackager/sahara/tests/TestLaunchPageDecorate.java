package au.edu.labshare.schedserver.scormpackager.sahara.tests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import biz.source_code.miniTemplator.MiniTemplator;

import au.edu.labshare.schedserver.scormpackager.sahara.ILaunchPage;
import au.edu.labshare.schedserver.scormpackager.sahara.LaunchButtonDecorator;
import au.edu.labshare.schedserver.scormpackager.sahara.LaunchPage;
import au.edu.labshare.schedserver.scormpackager.sahara.BlurbDecorator;
import au.edu.labshare.schedserver.scormpackager.sahara.PictureDecorator;
import au.edu.labshare.schedserver.scormpackager.sahara.TitleDecorator;

public class TestLaunchPageDecorate 
{
	ILaunchPage decoratedPage;
	
	private static final String TEMPLATE_DOCUMENT = "resources/templates/launchpage/launchpagetemplate.html";
	
	private LaunchPage launchPage;
	private TitleDecorator titleDecorator;
	private PictureDecorator picDecorator;
	private BlurbDecorator blurbDecorator;
	private LaunchButtonDecorator launchButtonDecorator;
	
	@Before
	public void Setup()
	{	
		// Need to initialise the variables needed for the launchpage
		launchPage = new LaunchPage();
		titleDecorator = new TitleDecorator(launchPage);
		titleDecorator.setTitle("Test Experiment Title");
		picDecorator = new PictureDecorator(titleDecorator);
		picDecorator.setPicLocation("test/resources/lila/lib/uts_tower.jpeg");
		blurbDecorator = new BlurbDecorator(picDecorator);
		blurbDecorator.setBlurb("Blurb String Text example...");
		launchButtonDecorator = new LaunchButtonDecorator(blurbDecorator);
		
		
		//decoratedPage = new BlurbDecorator(new PictureDecorator(new TitleDecorator(new LaunchPage())));
		decoratedPage = titleDecorator;
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
		
		// Render the respective elements
		
		decoratedPage.render(templator);
		decoratedPage = picDecorator;
		decoratedPage.render(templator);
		decoratedPage = blurbDecorator;
		decoratedPage.render(templator);
		decoratedPage = launchButtonDecorator;
		decoratedPage.render(templator);
		
		// Output the file
		try
		{
			templator.generateOutput("/tmp/launchpage.html");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}

		//Use mocks to see if render is invoked correctly.s
		fail("Not implemented yet");
	}

	@Test
	public void testFileName()
	{
		assertTrue(new String("ExampleExperiment.zip").equals(decoratedPage.getLaunchPageName()));
	}
}
