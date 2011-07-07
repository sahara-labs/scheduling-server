/**
 * SAHARA Scheduling Server
 *
 * BlurbDecorator Class. 
 * Allows the adding a blurb/text description to the html template
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

public class BlurbDecorator extends LaunchPageDecorator 
{
	// Stores actual value of string in question (the content)
	private String blurb = null;                  //Needs to be provided by the user 	
	private String fontType =	"Helvetica Neue"; //Derived from typographic.css - blueprintCSS
	private int    fontSize = 12;				  //Derived from typographic.css - blueprintCSS
	private String alignment = "span-16";		  //Derived from typograhpic.css - blueprintCSS 
	
	// Variables and Constants used for typography and alignment of Blurb
	
	public final String BLURB_ELEM = "BLURB";    //Element to fill in text
	public final String FONT_FAMILY_ELEM = "BLURB_FONT_FAMILY";
	public final String FONT_SIZE_ELEM = "BLURB_FONT_SIZE";
	public final String FONT_ALIGN_ELEM = "BLURB_ALIGNMENT";

	
	public BlurbDecorator(ILaunchPage decoratedPage) 
	{
		super(decoratedPage);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getLaunchPageName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void render(MiniTemplator template) 
	{
		template = addBlurb(template);
	}
	
	private MiniTemplator addBlurb(MiniTemplator t)
	{
		// Add the blurb to the centre of the page. This should be aligned below the picture
		//t = new MiniTemplator(decoratedPage.getLaunchPageName());
		t.setVariable (BLURB_ELEM, this.blurb);
		//t.setVariable (FONT_FAMILY_ELEM, this.fontType);
		//t.setVariable (FONT_SIZE_ELEM.toString(), Integer.toString(this.fontSize));
		t.setVariable (FONT_ALIGN_ELEM, this.alignment);
		t.addBlock ("blurbblock");  
		//t.generateOutput (outputFileName); 
		
		return t;
	}
	
	public void setBlurb(String blurbToAdd)
	{
		this.blurb = blurbToAdd;
	}
	
	public String getBlurb()
	{
		return this.blurb;
	}
	
	// Decorators specific to the Blurb
	public void setTemplateAttribute(String attributeName, String attributeValue)
	{
		if(attributeName.equals(BLURB_ELEM))
			this.blurb = attributeValue;
		else if(attributeName.equals(FONT_FAMILY_ELEM))
			this.fontType = attributeValue;
		else if(attributeName.equals(FONT_SIZE_ELEM))
			this.fontSize = Integer.getInteger(attributeValue).intValue();
		else if(attributeName.equals(FONT_ALIGN_ELEM))
			this.alignment = attributeValue;
	}
	
	public String getTemplateValue(String attributeName)
	{
		if(attributeName.equals(BLURB_ELEM))
			return this.blurb;
		else if(attributeName.equals(FONT_FAMILY_ELEM))
			return this.fontType;
		else if(attributeName.equals(FONT_SIZE_ELEM))
			return Integer.toString(this.fontSize);
		else if(attributeName.equals(FONT_ALIGN_ELEM))
			return this.alignment;
		else
			return null;
	}
	
	
}
