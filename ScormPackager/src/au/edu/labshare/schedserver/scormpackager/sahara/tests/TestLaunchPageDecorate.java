package au.edu.labshare.schedserver.scormpackager.sahara.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import au.edu.labshare.schedserver.scormpackager.sahara.ILaunchPage;
import au.edu.labshare.schedserver.scormpackager.sahara.LaunchPage;
import au.edu.labshare.schedserver.scormpackager.sahara.BlurbDecorator;
import au.edu.labshare.schedserver.scormpackager.sahara.PictureDecorator;
import au.edu.labshare.schedserver.scormpackager.sahara.TitleDecorator;

public class TestLaunchPageDecorate 
{
	ILaunchPage decoratedPage;
	
	@Before
	public void Setup()
	{
		decoratedPage = new BlurbDecorator(new PictureDecorator(new TitleDecorator(new LaunchPage())));
	}
	
	@Test
	public void testGenerateFile()
	{
		
		decoratedPage.render();

		//Use mocks to see if render is invoked correctly.
		fail("Not implemented yet");
	}

	@Test
	public void testFileName()
	{
		assertTrue(new String("ExampleExperiment.zip").equals(decoratedPage.getLaunchPageName()));
	}
}
