package au.edu.labshare.schedserver.scormpackager.sahara;

abstract public class LaunchPageDecorator implements ILaunchPage
{
	protected ILaunchPage decoratedPage;
	
	public LaunchPageDecorator(ILaunchPage decoratedPage)
	{
		this.decoratedPage = decoratedPage;
	}

}
