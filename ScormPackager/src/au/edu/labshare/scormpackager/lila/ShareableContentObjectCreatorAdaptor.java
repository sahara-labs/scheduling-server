package au.edu.labshare.scormpackager.lila;

import java.io.File;
import java.util.Collection;
import java.util.zip.*;

import au.edu.labshare.scormpackager.IShareableContentObjectCreator;

public abstract class ShareableContentObjectCreatorAdaptor implements IShareableContentObjectCreator
{	
	

	@Override 
	public ZipFile createPIF(File manifest, Collection <?> content) 
	{
		//Will use the default LMS value
		return createSCO(manifest, content, null); 
	}
	
	/*
	 * Validate dtd file which is an xml file verifying correctness of Application.xml
	 */
	public boolean validateDTDFile(ZipFile DTDFile)
	{
		//Default is to return true; 
		return true;
	}
	
	/*
	 * Validate the Javascript file associated with LMS
	 * Need to identify that the files has the following to be executed:
	 * <-- and //--> tags
	 */
	public boolean validateLMS(ZipFile jsLMSFile)
	{
		return true;
	}

}
