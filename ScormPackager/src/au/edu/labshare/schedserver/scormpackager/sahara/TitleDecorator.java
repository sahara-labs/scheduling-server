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
