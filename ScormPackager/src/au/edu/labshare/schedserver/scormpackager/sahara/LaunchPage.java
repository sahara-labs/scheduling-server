package au.edu.labshare.schedserver.scormpackager.sahara;

import java.io.File;

import biz.source_code.miniTemplator.MiniTemplator;

public class LaunchPage implements ILaunchPage 
{
	private String description;
	private String experimentName;
	private String title;
	private File   picture;
	
	@Override
	public String getLaunchPageName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getLaunchPagePath() 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void render(MiniTemplator template) 
	{
		// TODO Auto-generated method stub
		
	}
}
