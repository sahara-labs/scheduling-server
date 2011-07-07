/**
 * SAHARA Scheduling Server
 *
 * RigLaunchPageCreator Class. 
 * Layout of what is known as the launch page of the RigSCO.
 * A RigSCO is based on Sahara's RigType, a generalised grouping for a rig.
 * Therefore, does not contain any lesson plans or specific experiment meta-data.
 * It does contain the information pertaining to the RigType of the experiment.
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
package au.edu.labshare.schedserver.scormpackager.sahara;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import biz.source_code.miniTemplator.MiniTemplator;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigTypeMedia;
import au.edu.labshare.schedserver.scormpackager.sahara.RigMedia;

import au.edu.labshare.schedserver.scormpackager.sahara.BlurbDecorator;
import au.edu.labshare.schedserver.scormpackager.sahara.PictureDecorator;
import au.edu.labshare.schedserver.scormpackager.sahara.LaunchButtonDecorator;
import au.edu.labshare.schedserver.scormpackager.sahara.TitleDecorator;
import au.edu.labshare.schedserver.scormpackager.utilities.ScormUtilities;


public class RigLaunchPageCreator 
{
	private MiniTemplator templator;
	
	ILaunchPage decoratedPage;
	
	public static final String TEMPLATE_DOCUMENT_TYPE = "scormpackager_template_1";
	
	private LaunchPage launchPage;
	private TitleDecorator titleDecorator;
	private PictureDecorator picDecorator;
	private BlurbDecorator blurbDecorator;
	private LaunchButtonDecorator launchButtonDecorator;
	
	/*
	 * This is the output path we are going to use using the templating engine
	 */
	private String path = null;
	
	public RigLaunchPageCreator()
	{
		try
		{
			Properties defaultProps = new Properties();
		    InputStream in = null;
			//InputStream in = null;
		    //in = new FileInputStream("resources/scormpackager.properties"); //TODO: Should place this as a static string
			try
			{
				in = this.getClass().getClassLoader().getResourceAsStream("resources/scormpackager.properties");
			}
			catch(Exception e)
			{
				e.printStackTrace(); //TODO: Need to replace with Sahara Logger			
			}
		    
	        defaultProps.load(in);
	        
			templator = new MiniTemplator(defaultProps.getProperty(TEMPLATE_DOCUMENT_TYPE));
			in.close();
		}
		catch(IOException e)
		{
			e.printStackTrace(); //TODO: Need to replace with Sahara Logger
		}
	}
	
	public void setOutputPath(String path)
	{
		this.path = path;
	}
	
	public String getOutputPath()
	{
		return this.path;
	}
	
	// We want to create the LaunchPage by tapping into DataAccess Layer
	public String createLaunchPage(String experimentName, org.hibernate.Session session)
	{		
		launchPage = new LaunchPage();
		RigMedia rigMedia = new RigMedia(session);
		RigType rigType = rigMedia.getRigType(experimentName);
		
		Set<RigTypeMedia> rigTypeMediaSet;
		rigTypeMediaSet = rigType.getMedia();
		
		for(RigTypeMedia itemRigTypeMedia : rigTypeMediaSet)
		{
			// Should decorate based on the type of the media to update
			if(itemRigTypeMedia.getMime().toString().equals(RigMedia.MIME_JPEG) || 
			   itemRigTypeMedia.getMime().toString().equals(RigMedia.MIME_PNG)  ||
			   itemRigTypeMedia.getMime().toString().equals(RigMedia.MIME_GIF))
			{
				picDecorator = new PictureDecorator(launchPage);
				picDecorator.setPicLocation(ScormUtilities.getFilenameFromPath(itemRigTypeMedia.getFileName(), "/")); //TODO: Not sure if this only works on UNIX systems
				decoratePage(picDecorator);
			}
			else if(itemRigTypeMedia.getMime().toString().equals(RigMedia.MIME_TXT)) //If it is just plain txt we are assuming blurb
			{
				blurbDecorator = new BlurbDecorator(launchPage);
				
				File file = new File(itemRigTypeMedia.getFileName());
				StringBuffer contents = new StringBuffer();
			    BufferedReader reader = null;
			    try
			    {
			    	reader = new BufferedReader(new FileReader(file));
			    	String text = null;

			            // repeat until all lines is read
			            while ((text = reader.readLine()) != null)
			            {
			                contents.append(text).append(System.getProperty("line.separator"));
			            }
			    } 
			    catch(FileNotFoundException e)
			    {
			    	e.printStackTrace();  //TODO: Need to replace with Sahara Logger
			    } 
			    catch(IOException e)
			    {
			    	e.printStackTrace();
			    } 
			    finally
			    {
			    	try
			    	{
			    		if(reader != null)
			    		{
			    			reader.close();
			    		}
			    	} 
			    	catch (IOException e)
			    	{
			    		e.printStackTrace();
			    	}
			    }

				blurbDecorator.setBlurb(contents.toString());
				decoratePage(blurbDecorator);
			}
		}
		
		/*
		 *  Decorate the rest of the fields on template:
		 */

		// 1. Decorate Title
		titleDecorator = new TitleDecorator(launchPage);
		titleDecorator.setTitle(experimentName);
		decoratePage(titleDecorator);
		
		// 2. Decorate with a LaunchButton
		launchButtonDecorator = new LaunchButtonDecorator(launchPage);
		launchButtonDecorator.setButtonLink("rigtype=" + rigType.getName());
		decoratePage(launchButtonDecorator);
		
		// 3. Render the page. Need to set the path first though
		if(this.path != null) // Should never be the case if it is invoked
		{
			launchPage.setLaunchPagePath(this.path);
			launchPage.render(templator);
		}
		
		return launchPage.getLaunchPagePath(); 
	}
	
	public String decoratePage(ILaunchPage launchPageDecorator)
	{
		// Appears to be issue with reflections in OSGi - limitation of 15 times - http://dev.eclipse.org/mhonarc/lists/eclipselink-dev/msg04431.html
		decoratedPage = launchPageDecorator;
		decoratedPage.render(templator);
		return decoratedPage.getLaunchPageName();
	}
	
	public String addCSS()
	{	
		return "/resources/templates/launchpage/css"; //TODO: Should place this as a static string;
	}
}
