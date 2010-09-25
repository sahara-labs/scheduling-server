package au.edu.labshare.schedserver.scormpackager.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;

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

	
	public static String getFilenameFromPath(String path, String pathSeparator) 
	{ 
		 // gets filename without extension
		 int sep = path.lastIndexOf(pathSeparator);
		 return path.substring(sep + 1, path.length());
	}
	
	public static LinkedList<File> getFilesFromPath(String[] listOfFiles)
	{
		LinkedList<File> content = new LinkedList<File>();
		
		
		for(int i = 0; i < listOfFiles.length; i++)
		{
			content.add(new File(listOfFiles[i]));
		}
		
		return content;
	}

	// Derived from: http://stackoverflow.com/questions/1399126/java-util-zip-recreating-directory-structure
	public static void copy(InputStream in, OutputStream out) throws IOException 
	{
		byte[] buffer = new byte[1024];
		
		while (true) 
		{
			int readCount = in.read(buffer);
			if(readCount < 0) 
			{
				break;
			}
			
			out.write(buffer, 0, readCount);
		}
	 }
	
	// Derived from: http://stackoverflow.com/questions/1399126/java-util-zip-recreating-directory-structure
	public static void copy(File file, OutputStream out) throws IOException 
	{
		InputStream in = new FileInputStream(file);
	
		try 
		{
		 copy(in, out);
		}
		finally 
		{
		  in.close();
		}
	}

	// Derived from: http://stackoverflow.com/questions/1399126/java-util-zip-recreating-directory-structure
	public static void copy(InputStream in, File file) throws IOException 
	{
		OutputStream out = new FileOutputStream(file);
		
		try 
		{
			copy(in, out);
		} 
		finally 
		{
			out.close();
		}
	}
}
