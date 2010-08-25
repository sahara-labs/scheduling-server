package au.edu.labshare.schedserver.scormpackager.manifest;

import java.util.HashMap;

import au.edu.labshare.schedserver.scormpackager.lila.Manifest;
import au.edu.labshare.schedserver.scormpackager.utilities.ScormUtilities;

public class MetaData 
{
	public static final int STRING_SCHEMA_MAX_LENGTH = 100;
	public static final int STRING_SCHEMA_VERSION_MAX_LENGTH = 20;
	
	public static final String XMLNS_IMSCP = "http://www.imsproject.org/xsd/imscp_rootv1p1p2";
	public static final String XMLNS_ADLCP = "http://www.adlnet.org/xsd/adlcp_rootv1p2";
	public static final String XMLNS_XSI = "http://www.w3.org/2001/XMLSchema-instance";
	public static final String XSI_IMSCP_SCHEMALOC = "http://www.imsproject.org/xsd/imscp_rootv1p1p2 imscp_rootv1p1p2.xsd";
	public static final String XSI_IMSMD_SCHEMALOC = "http://www.imsglobal.org/xsd/imsmd_rootv1p2p1 imsmd_rootv1p2p1.xsd";
	public static final String XSI_ADLCP_SCHEMALOC = "http://www.adlnet.org/xsd/adlcp_rootv1p2 adlcp_rootv1p2.xsd";
	
	HashMap<String, String> schema;
	String schemaVersion;
	String identifier; 
	
	public MetaData()
	{
		schema = new HashMap<String, String>();
		schema.put("version", Manifest.SCHEMA_VERSION);
		schema.put("xmlns", XSI_ADLCP_SCHEMALOC);
		schema.put("xmlns:adlcp", XMLNS_IMSCP);
		schema.put("xmlns:xsi", XMLNS_XSI);
		schema.put("xsi:schemalocation", XSI_IMSCP_SCHEMALOC	
										+ "        " + XSI_IMSMD_SCHEMALOC  
										+ "        " + XSI_ADLCP_SCHEMALOC);
	}
	
	public String getSchemaValue(String key)
	{
		if(schema.containsKey(key))
		{
			return schema.get(key);
		}
		else
			return null;
	}
	
	public void addSchemaAttribute(String key, String value)
	{
		if(key != null && value != null)
		{
			schema.put(key, value);
		}
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
	
	public void setIdentifer(String experimentIdentifier)
	{
		//Replace any whitespace from name with underscore 
		if(experimentIdentifier != null)
		{
			this.identifier = ScormUtilities.replaceWhiteSpace(experimentIdentifier, null);
			schema.put("identifier", experimentIdentifier);	//Add this also to the schema
		}
	}
}
