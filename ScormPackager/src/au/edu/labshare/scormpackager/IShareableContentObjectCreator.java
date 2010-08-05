package au.edu.labshare.scormpackager;

import au.edu.labshare.scormpackager.IPackageInterchangeFile;

import java.io.File;
import java.util.Collection;
import java.util.zip.ZipFile;

public interface IShareableContentObjectCreator extends IPackageInterchangeFile
{
	/**
	 * Creates the Shareable Content Object (SCO) in zip format. 
	 * All files are zipped, not just the directory.
	 * According to standard need the following:
	 * @param manifest	Filename for the SCO in place of imsmanifest.xml. 
	 * 					If null is supplied imsmanifest.xml is used.   
	 * @param assets  	LearningResource e.g. WebPage, AudioFile, GIF imgae etc. [1..*]
	 * @param lmsName 	Accessible Learning Management System (e.g. Blackboard, Moodle, Sakai )
	 * 					If null is supplied a default LMS value is used.
	 */
	public ZipFile createSCO(String mainfest, Collection <File> assets, String lmsName);
	
	/**
	 * Validate that the LMS (URL) is handled 
	 */
	public boolean validateLMSConnection(ZipFile SCO); 
	
}
