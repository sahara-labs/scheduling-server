package au.edu.labshare.scormpackager;

import au.edu.labshare.scormpackager.IPackageInterchangeFile;

import java.io.File;
import java.util.zip.ZipFile;
import java.util.Collection;

public interface IShareableContentObjectCreator extends IPackageInterchangeFile
{
	/*
	 * Creates the Shareable Content Object (SCO) in zip format. 
	 * All files are zipped, not just the directory.
	 * According to standard need the following:
	 * 
	 * 1. Asset (LearningResource) e.g. WebPage, AudioFile, GIF imgae etc. [must containg 1 or more]
	 * 2. Must be able to  communicate with LMS
	 * 
	 * 
	 */
	public ZipFile createSCO(File manifest, Collection <?> resources, String LMSName);
	
	/*
	 * Validate that the LMS (URL) is handled 
	 */
	public boolean validateLMSConnection(ZipFile SCO); 
	
}
