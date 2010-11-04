/**
 * SAHARA Scheduling Server
 *
 * PictureDecorator Class. 
 * Allows the adding of a pic to the html template, to show what the rig looks like.
 * Limitation: this only allows the adding of one picture, known as the 'profilepic'.
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

public class PictureDecorator extends LaunchPageDecorator
{
	// Stores actual value of string in question (the content)
	private String pictureLocation = null;        //Needs to be provided by the user 	
	private String alignment = "span-16";		  //Derived from typograhpic.css - blueprintCSS 
	
	// Variables and Constants used for alignment of Picture
	public final String PIC_LOCATION = "PROFILE_PIC";
	public final String ALIGN_ELEM = "PIC_ALIGNMENT";
	
	public PictureDecorator(ILaunchPage decoratedPage) 
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
		template = addPic(template);
	}
	
	// Method to align the picture
	// Note: this assumes that there is only one picture in use
	// TODO: If there are more, this method should be extended to accommodate for this
	private MiniTemplator addPic(MiniTemplator t)
	{
		// Add picture to template location. This should be aligned above the blurb and below title
		t.setVariable(PIC_LOCATION, this.pictureLocation);
		t.setVariable (ALIGN_ELEM, this.alignment);
		t.addBlock ("profilepicblock"); 
		
		return t;
	}
	
	public void setPicLocation(String picToAdd)
	{
		this.pictureLocation = picToAdd;
	}
	
	public String getPicLocation()
	{
		return this.pictureLocation;
	}
	
	// Decorators specific to the Blurb
	public void setTemplateAttribute(String attributeName, String attributeValue)
	{
		if(attributeName.equals(ALIGN_ELEM))
			this.alignment = attributeValue;
	}
	
	public String getTemplateValue(String attributeName)
	{
		if(attributeName.equals(ALIGN_ELEM))
			return this.alignment;
		
		return null;
	}

}
