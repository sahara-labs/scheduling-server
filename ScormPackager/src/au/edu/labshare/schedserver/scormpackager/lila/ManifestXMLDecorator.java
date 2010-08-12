package au.edu.labshare.schedserver.scormpackager.lila;

import java.io.File;

public class ManifestXMLDecorator 
{
	public static final String MANFEST_NAME = "imsmanfest.xml";
	private File manifestFile = null;
	
	public ManifestXMLDecorator() 
	{
		//Initialise the file for the 
		manifestFile = new File(MANFEST_NAME);
	}
	
	public void decorateManifest()
	{
		//Invoke the following to generate the imsmanifest.xml file:
		//1. decorateManifestHeader()
		//2. decorateOrganizations()
		//3. decoreateResources()
		decorateManifestHeader();
		decorateOrganizations();
		decorateResources();
	}
	
	private void decorateManifestHeader()
	{
		// TODO Need to shift generateManifest() from ShareableContentObjectCreator class
		//return null;	
	}
	
	private void decorateOrganizations()
	{
		// TODO Need to shift generateManifest() from ShareableContentObjectCreator class
		//return null;
	}

	private void decorateResources()
	{
		// TODO Need to shift generateManifest() from ShareableContentObjectCreator class
		//return null;
	}
}
