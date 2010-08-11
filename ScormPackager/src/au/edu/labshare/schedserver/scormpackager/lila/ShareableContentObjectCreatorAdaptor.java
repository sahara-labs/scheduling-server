package au.edu.labshare.schedserver.scormpackager.lila;

import java.io.File;
import java.util.Collection;
import java.util.zip.*;

import au.edu.labshare.schedserver.scormpackager.IShareableContentObjectCreator;

public abstract class ShareableContentObjectCreatorAdaptor implements IShareableContentObjectCreator
{	
	@Override
	public String createPIF(String title, Collection <File> content)
	{
		return createSCO(title, content, null);
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
