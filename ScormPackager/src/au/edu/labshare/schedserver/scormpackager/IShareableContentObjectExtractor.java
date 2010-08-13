package au.edu.labshare.schedserver.scormpackager;
import java.io.File;

public interface IShareableContentObjectExtractor 
{
	/*
	 * Extract the zip/PIF (Package Interchange File) file
	 */
	public boolean ValidatePIF(File SCOFile);
	
	/*
	 * Retrieve contents from file 
	 */
	public File getManifestFile();
	
	/*
	 * Retrieve HTML Lab description file
	 */
	public File getHTMLFile();
	
	/*
	 * Retrieve assets 
	 */
	public File[] getContent();	
}
