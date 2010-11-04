/**
 * SAHARA Scheduling Server
 *
 * LaunchPageDecorate Unit Test. 
 * Tests that all the decorators (that uses the decorator pattern) work accordingly.
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
			
			// Fail if exception is raised
			fail("IOException caused");
		}

		//If no exception was thrown this passes.
		assertTrue(true);
	}

	@Test
	public void testFileName()
	{
		assertTrue(new String("ExampleExperiment.zip").equals(decoratedPage.getLaunchPageName()));
	}
}
