package au.edu.labshare.schedserver.scormpackager;

import au.edu.labshare.schedserver.scormpackager.manifest.MetaData;
import au.edu.labshare.schedserver.scormpackager.manifest.Organization;
import au.edu.labshare.schedserver.scormpackager.manifest.Resource;

public interface IManifest 
{
	//Getter and Setters for the data that is necessary in immanifest.xml	
	public MetaData       getMetaData();
	public Organization[] getOrganizations();
	public Resource[]     getResources();
	public IManifest[]    getSubManifests();
	
	public void setMetaData();
	public void setOrganizations();
	public void setResources();
	public void setSubManifests();	
}
