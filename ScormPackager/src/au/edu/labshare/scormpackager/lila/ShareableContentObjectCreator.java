package au.edu.labshare.scormpackager.lila;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.CharBuffer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;
import java.util.zip.Deflater;
import java.util.Collection;
import java.util.Iterator;

import au.edu.labshare.scormpackager.lila.ShareableContentObjectCreatorAdaptor;

public class ShareableContentObjectCreator extends ShareableContentObjectCreatorAdaptor  
{
	
	@Override
	public ZipFile createSCO(String manifest, Collection <File> assets, String LMSName) 
	{

		ZipFile zipFileSCO = null;
		FileInputStream fileInStream = null;
		FileOutputStream fileOutStream = null;
		ZipOutputStream zipFileOutStream = null;
		BufferedReader  bufferInputRdr = null;
		byte[] buffer = new byte[18024];
		
		//Test to see if the manifest is not null, if so, auto-generate one.
		if(manifest == null)
		{
			return null;  // TODO Need to do the autogeneration of the zip file.
		}
		
		//Zip the contents into a file
		try 
		{
			fileOutStream = new FileOutputStream("scorm.zip");
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace(); //TODO Replace with Sahara Logger as part of refactoring.
		}

		if(fileOutStream != null)
		{
			try
			{
				zipFileOutStream  = new ZipOutputStream(fileOutStream);
				zipFileOutStream.setLevel(Deflater.DEFAULT_COMPRESSION);
				
				Iterator <File> iter = assets.iterator();
				while(iter.hasNext())
				{
					fileInStream = new FileInputStream(iter.next());
					//TODO The below string conversion does not handle the lib/ direcctory structure
					zipFileOutStream.putNextEntry(new ZipEntry(iter.next().getName()));
					int len;
				    while ((len = fileInStream.read(buffer)) > 0)
				    {
				    	zipFileOutStream.write(buffer, 0, len);
				    }
				    zipFileOutStream.closeEntry();
				    fileInStream.close();
				    zipFileOutStream.close();
				}
				
				//TODO Need to associate the ZipFile object type to the zipFileOutStream/ZipEntries.
				return zipFileSCO;
			}
			catch(IOException e)
			{
				e.printStackTrace(); //TODO replace with Sahara Logger as part of refactoring.
			}
			
			return null;
		}
		else
			return null;
	}

	@Override
	public boolean validateApplicationProfile(ZipFile SCO) 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validateManifest(ZipFile SCO) 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validateMetaData(ZipFile SCO) 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validatePresentationNavigationInfo(ZipFile SCO) 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validateSequencing(ZipFile SCO) 
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean validateLMSConnection(ZipFile jsLMSFile)
	{
		// TODO Depending on the structure of the 
		return false;
	}

	@Override
	public ZipFile createPIF(Collection<File> content) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean validateContent(ZipFile PIF) {
		// TODO Auto-generated method stub
		return false;
	}
}
