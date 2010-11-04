/**
 * SAHARA Scheduling Server
 *
 * LaunchButtonDecorator Class. 
 * Allows the adding of a launch button to the html template to allow
 * the user to be redirected to the URL of the rig client with the necessary parameters.
 * This allows ONLY one experiment (rig type) to be accessed. Part of the RigSCO concept.
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

import biz.source_code.miniTemplator.MiniTemplator;

public class LaunchButtonDecorator extends LaunchPageDecorator 
{

	// Stores actual value of string in question (the content)
	private String launchButtonPicLocation = "blueprint/plugins/buttons/icons/package_go.png";  // Default. But can be overridden 
	private String buttonStyle = "button negative";
	private String href = null;
	private String alignment = "span-10";		  //Derived from typograhpic.css - blueprintCSS 
	private String href_base_url = "http://sahara2.eng.uts.edu.au/?";

	// Variables and Constants used for alignment of Launch Button
	public final String BUTTON_PIC = "BUTTON_PIC";
	public final String BUTTON_STYLE = "BUTTON_STYLE";
	public final String BUTTON_HREF = "BUTTON_HREF";
	public final String ALIGN_ELEM = "BUTTON_ALIGNMENT";
	
	public LaunchButtonDecorator(ILaunchPage decoratedPage) 
	{
		super(decoratedPage);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getLaunchPageName() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void render(MiniTemplator template) 
	{
		template = addButton(template);
		
	}
	
	// Method to setup the launch button to access Sahara
	// Note: this assumes that there is only one button used
	private MiniTemplator addButton(MiniTemplator t)
	{
		// Add picture to template location. This should be aligned above the blurb and below title
		String urlToUse = this.href_base_url + this.href;
		t.setVariable(BUTTON_PIC, this.launchButtonPicLocation);
		t.setVariable (BUTTON_STYLE, this.buttonStyle);
		t.setVariable (BUTTON_HREF, urlToUse);
		t.setVariable (ALIGN_ELEM, this.alignment);
		t.addBlock ("launchbuttonblock"); 
		
		return t;
	}
	
	public void setLaunchbuttonPicLocation(String picLocation)
	{
		this.launchButtonPicLocation = picLocation;
	}
	
	public String getLaunchButtonPicLocation()
	{
		return this.launchButtonPicLocation;
	}
	
	public void setButtonStyle(String buttonStyle)
	{
		this.buttonStyle = buttonStyle;
	}
	
	public String getButtonStyle()
	{
		return this.buttonStyle;
	}
	
	public void setButtonLink(String hrefLink)
	{
		this.href = hrefLink;
	}
	
	public String getButtonLink()
	{
		return this.href;
	}
	
	public void setBaseURL(String baseHREF)
	{
		this.href_base_url = baseHREF;
	}
	
	public String getBaseURL()
	{
		return this.href_base_url;
	}
}
