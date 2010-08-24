package au.edu.labshare.schedserver.scormpackager.utilities;

public class ScormUtilities 
{
	public static String getFileNameWithoutExtension(String filename)
	{
		if(filename.lastIndexOf(".") != -1)
			return filename.substring(0, filename.lastIndexOf("."));
		else
			return filename.substring(0, filename.length());
	}
		
	public static String getFileExtension(String filename)
	{
		if(filename.lastIndexOf(".") != -1)
			return filename.substring(filename.lastIndexOf(".") + 1,filename.length());
		else
			return "";
	}
	
	
	/**
	 * Takes the value of a name to produce an underscore version of the name.
	 * This is to allow naming consistency.
	 * @param name
	 * @param replacementStr
	 */
	public static String replaceWhiteSpace(String name, String replacementStr) 
	{
		if(replacementStr == null)
			replacementStr = "_"; 
				
		return name.replaceAll("\\W", replacementStr);
	}
}
