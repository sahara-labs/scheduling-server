package au.edu.labshare.schedserver.scormpackager.sahara;

import biz.source_code.miniTemplator.MiniTemplator;

public class LaunchButtonDecorator extends LaunchPageDecorator 
{

	// Stores actual value of string in question (the content)
	private String launchButtonPicLocation = "css/blueprint/plugins/buttons/icons/package_go.png";  // Default. But can be overridden 
	private String buttonStyle = "button negative";
	private String href = "http://remotelabs.eng.uts.edu.au/login/?";
	private String alignment = "span-10";		  //Derived from typograhpic.css - blueprintCSS 

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
		t.setVariable(BUTTON_PIC, this.launchButtonPicLocation);
		t.setVariable (BUTTON_STYLE, this.buttonStyle);
		t.setVariable (BUTTON_HREF, this.href);
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
}
