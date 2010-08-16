package au.edu.labshare.schedserver.scormpackager.manifest;

public class MetaData 
{
	public static final int STRING_SCHEMA_MAX_LENGTH = 100;
	public static final int STRING_SCHEMA_VERSION_MAX_LENGTH = 20;
	
	String schema;
	String schemaVersion;
	
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
}
