package au.edu.labshare.scormpackager.lila;

import java.io.File;
import java.util.zip.ZipFile;
import java.util.Collection;

import au.edu.labshare.scormpackager.lila.ShareableContentObjectCreatorAdaptor;

public class ShareableContentObjectCreator extends ShareableContentObjectCreatorAdaptor  
{
	
	@Override
	public ZipFile createSCO(String manifest, Collection <File> assets, String LMSName) 
	{
		// TODO Auto-generated method stub
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
	public boolean validateDTDFile(ZipFile DTDFile)
	{
		// TODO Depending on the structure of the 
		return false;
	}
	
	@Override
	public boolean validateLMSConnection(ZipFile jsLMSFile)
	{
		// TODO Depending on the structure of the 
		return false;
	}

	@Override
	public ZipFile createPIF(String manifest, Collection<File> content) 
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
