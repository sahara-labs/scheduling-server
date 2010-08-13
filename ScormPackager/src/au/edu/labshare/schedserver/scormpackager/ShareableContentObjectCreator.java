package au.edu.labshare.schedserver.scormpackager;

import au.edu.labshare.schedserver.scormpackager.IPackageInterchangeFile;

import java.io.File;
import java.util.Collection;
import java.util.zip.ZipFile;

public abstract class ShareableContentObjectCreator implements IPackageInterchangeFile
{
	/** 
	 * A PIF file needs to contain the following information
	 */
	private IManifest manifest;
	private File content[];
	
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
	public abstract String createSCO(String mainfest, Collection <File> assets, String lmsName);
	
	/**
	 * Validate that the LMS (URL) is handled 
	 */
	public abstract boolean validateLMSConnection(ZipFile SCO);

	public void setManifest(IManifest manifest) 
	{
		this.manifest = manifest;
	}

	public IManifest getManifest() 
	{
		return manifest;
	}

	public void setContent(File content[]) 
	{
		this.content = content;
	}

	public File[] getContent() 
	{
		return content;
	} 
}
