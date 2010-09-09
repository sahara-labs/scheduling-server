package au.edu.labshare.schedserver.scormpackager;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigTypeDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigTypeMedia;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigTypeInformation;


/** 
 * Class to access the relevant database entities:
 * 1. RigType
 * 2. RigTypeMedia
 * 3. RigTypeInformation 
 * 
 */
public class SaharaRigMedia 
{
	private RigType rigType;
	//private RigTypeMedia rigMediaType;
	//private RigTypeInformation rigTypeInformation;
	
	private org.hibernate.Session db; 
	
	public SaharaRigMedia(org.hibernate.Session session)
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
