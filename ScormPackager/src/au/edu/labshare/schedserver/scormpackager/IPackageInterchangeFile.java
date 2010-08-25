package au.edu.labshare.schedserver.scormpackager;

import java.io.File;
import java.util.Collection;
import java.util.zip.ZipFile;

public interface IPackageInterchangeFile 
{	

	/**
	 * A generic form of a SCO or Asset is known as a Package Interchange File (PIF).
	 * This assumes manifest file is: "imsmanifest.xml"
	 * @return Path of the generated PIF file.
	 */
	public String createPIF(String experimentName, Collection <File> content, String outputPath);
	
	/**
	 * Validates the manifest file as according the SCORM: 
	 * 1. Manifest file is titled imsmanifest.xml
	 * 2. Needs to be at root of the content package (SCO)
	 * 3. Contains the following information:
	 * 	a) Metadata
	 * 	b) Organisations
	 * 	c) Resources
	 * 	d) sub-Manifests (if there are any)
	 */
	public boolean validateManifest(ZipFile PIF);
	
	/**
	 * Validates that there is one content component in the PIF
	 */
	public boolean validateContent(ZipFile PIF);
	
	/**
	 * Validates the application.xml/profile file that describes the experiment of the lab
	 */
	public boolean validateApplicationProfile(ZipFile PIF);
		
	/**
	 * Validates the <sequencing> and <sequencingCollection> tags
	 */
	public boolean validateSequencing(ZipFile PIF);
	
	/**
	 * Validates the <presentation> and <navigationInterface> tags
	 */
	public boolean validatePresentationNavigationInfo(ZipFile PIF);
	
	/**
	 * Validates all the metadata fields under the <lom> tag. Note that all Meta-data is optional. 
	 * As specified in section 4.3 of Sharable Content Object Reference Model
	 */
	public boolean validateMetaData(ZipFile SCO);
}
