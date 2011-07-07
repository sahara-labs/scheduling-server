/**
 * SAHARA Scheduling Server
 *
 * TitleDecorator Class. 
 * Decorates the title of the launch page. 
 * Should be either the rigtype or experiment name, using the setTitle() method. 
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

public class TitleDecorator extends LaunchPageDecorator
{
	// Stores actual value of string in question (the content)
	private String title = null;                  //Needs to be provided by the user 	
	private String alignment = "span-16 last";	  //Derived from typograhpic.css - blueprintCSS
	private String header_alignment = "span-24 last"; //Derived from typographics.css - blueprintCSS
	
	// Variables and Constants used for typography and alignment of Blurb
	public final String TITLE_ELEM = "EXPERIMENT_NAME";    //Element to fill in text
	public final String FONT_ALIGN_ELEM = "TITLE_ALIGNMENT";
	public final String HDR_ALIGN_ELEM = "HEADER_ALIGNMENT";

	
	public TitleDecorator(ILaunchPage decoratedPage) 
	{
		super(decoratedPage);
	}
	
	private MiniTemplator addTitle(MiniTemplator t)
	{
		// Add the blurb to the centre of the page. This should be aligned below the picture
		//t = new MiniTemplator(decoratedPage.getLaunchPageName());
		t.setVariable (TITLE_ELEM, this.title);
		t.setVariable (FONT_ALIGN_ELEM, this.alignment);
		t.setVariable (HDR_ALIGN_ELEM, this.header_alignment);
		t.addBlock ("headingblock");  
		
		// Add the blurb to the centre of the page. This should be aligned below the picture
		//t = new MiniTemplator(decoratedPage.getLaunchPageName());
		t.setVariable (TITLE_ELEM, this.title);
		t.addBlock ("titleblock");  
		
		return t;
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
		template = addTitle(template);
		
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public String getTitle()
	{
		return this.title;
	}
}
