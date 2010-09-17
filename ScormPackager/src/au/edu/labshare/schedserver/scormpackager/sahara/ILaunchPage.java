package au.edu.labshare.schedserver.scormpackager.sahara;

import biz.source_code.miniTemplator.MiniTemplator;

public interface ILaunchPage 
{	
	public void render(MiniTemplator template);
	public String getLaunchPageName();
}
