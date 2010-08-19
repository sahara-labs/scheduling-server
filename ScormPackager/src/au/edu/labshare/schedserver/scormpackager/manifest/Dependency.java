package au.edu.labshare.schedserver.scormpackager.manifest;

public class Dependency 
{
	public static final int STRING_MAX_LENGTH = 2000;
	public static final String LILA_SCO_IDENTIFIERREF = "stub";
	
	String identifierref;
	
	public Dependency()
	{
		identifierref = new String();
	}
	
	public String getIdentiferRef()
	{
		return identifierref;
	}
	
	public void setIdentifierRef(String identifierref)
	{
		if(identifierref.length() <= STRING_MAX_LENGTH)
			this.identifierref = identifierref;
	}
}
