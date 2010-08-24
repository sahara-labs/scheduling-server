package au.edu.labshare.schedserver.scormpackager.manifest;

import au.edu.labshare.schedserver.scormpackager.utilities.ScormUtilities;

public class MetaData 
{
	public static final int STRING_SCHEMA_MAX_LENGTH = 100;
	public static final int STRING_SCHEMA_VERSION_MAX_LENGTH = 20;
	
	String schema;
	String schemaVersion;
	String identifier; 
	
	public String getSchema()
	{
		return schema;
	}
	
	public void setSchema(String schema)
	{
		if(schema.length() <= STRING_SCHEMA_MAX_LENGTH)
			this.schema = schema;
	}
	
	public String getSchemaVersion()
	{
		return schemaVersion;
	}
	
	public void setSchemaVersion(String schemaVersion)
	{
		if(schemaVersion.length() <= STRING_SCHEMA_VERSION_MAX_LENGTH)
			this.schemaVersion = schemaVersion;
	}
	
	public String getIdentifer()
	{
		return identifier;
	}
	
	public void setIdentifer(String experimentIdentifer)
	{
		//Replace any whitespace from name with underscore 
		if(experimentIdentifer != null)
			this.identifier = ScormUtilities.replaceWhiteSpace(experimentIdentifer, null);
	}
}
