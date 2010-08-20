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
}
