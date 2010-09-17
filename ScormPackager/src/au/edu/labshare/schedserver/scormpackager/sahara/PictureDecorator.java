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
