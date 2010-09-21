package au.edu.labshare.schedserver.scormpackager.sahara;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigTypeDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;

/** 
 * Class to access the relevant database entities:
 * 1. RigType
 * 2. RigTypeMedia
 * 3. RigTypeInformation 
 * 
 */
public class RigMedia 
{
	private RigType rigType;
	//private RigTypeMedia rigMediaType;
	//private RigTypeInformation rigTypeInformation;
	
	// Rig Media MIME types supported
	public static final String MIME_JPEG = "image/jpeg";
	public static final String MIME_PNG= "image/png";
	public static final String MIME_GIF= "image/gif";
	public static final String MIME_SVG= "image/svg+xml";
	public static final String MIME_TXT = "text/plain"; //Support just .txt format
	
	
	private org.hibernate.Session db; 
	
	public RigMedia(org.hibernate.Session session)
	{
		this.db = session;
	}
	
	//Allow access methods to the media as we want to abstract the DAO end
	public RigType getRigType(String rigTypeName)
	{
		//Initialise the following entities as we need to access the DAO to configure these.
		//RigTypeDao rigTypeDao = new RigTypeDao();
		this.rigType = new RigTypeDao(db).findByName(rigTypeName);
		
		return this.rigType;
	}
}
