package au.edu.labshare.schedserver.scormpackager.sahara;

import biz.source_code.miniTemplator.MiniTemplator;

public class TitleDecorator extends LaunchPageDecorator
{
	// Stores actual value of string in question (the content)
	private String title = null;                  //Needs to be provided by the user 	
	private String alignment = "span-16 last";	  //Derived from typograhpic.css - blueprintCSS
	
	// Variables and Constants used for typography and alignment of Blurb
	public final String TITLE_ELEM = "EXPERIMENT_NAME";    //Element to fill in text
	public final String FONT_ALIGN_ELEM = "TITLE_ALIGNMENT";

	
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
		t.addBlock ("headingblock");  
		
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
}
