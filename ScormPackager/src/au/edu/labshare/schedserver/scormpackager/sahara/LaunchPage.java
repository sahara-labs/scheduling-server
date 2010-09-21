package au.edu.labshare.schedserver.scormpackager.sahara;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import biz.source_code.miniTemplator.MiniTemplator;

public class LaunchPage implements ILaunchPage 
{
	private String path = null;
	
	@Override
	public String getLaunchPageName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getLaunchPagePath() 
	{
		return path;
	}
	
	public void setLaunchPagePath(String path) 
	{
		this.path = path;
	}
	
	@Override
	public void render(MiniTemplator template) 
	{
		// Output the file
		if(this.path != null)
		{
			try
			{
				template.generateOutput(this.path);
			}
			catch(IOException e)
			{
				e.printStackTrace(); //TODO: Need to replace with Sahara Logger
			}
		}
	}
}
