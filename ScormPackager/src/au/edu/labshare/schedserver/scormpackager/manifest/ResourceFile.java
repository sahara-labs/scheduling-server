package au.edu.labshare.schedserver.scormpackager.manifest;

public class ResourceFile 
{
	public static final int STRING_HREF_MAX_LENGTH = 2000;
	
	MetaData metadata;
	String   href;
	
	public MetaData getMetaData()
	{
		return metadata;
	}
	
	public void setMetaData(MetaData metadata)
	{
		this.metadata = metadata;
	}
	
	public String getFileReference()
	{
		return href;
	}
	
	public void setFileReference(String filePathName)
	{
		if(filePathName.length() <= STRING_HREF_MAX_LENGTH)
			href = filePathName;
	}
}
