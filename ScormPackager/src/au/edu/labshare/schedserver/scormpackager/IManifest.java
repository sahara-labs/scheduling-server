package au.edu.labshare.schedserver.scormpackager;

import java.util.Collection;

import au.edu.labshare.schedserver.scormpackager.manifest.MetaData;
import au.edu.labshare.schedserver.scormpackager.manifest.Organization;
import au.edu.labshare.schedserver.scormpackager.manifest.Resource;

public interface IManifest 
{
	//Getter and Setters for the data that is necessary in immanifest.xml	
	public MetaData                 getMetaData();
	public Collection<Organization> getOrganizations();
	public Collection<Resource>     getResources();
	public Collection<IManifest>    getSubManifests();
	
	public void setMetaData(MetaData metadata);
	public void setOrganizations(Collection<Organization> organizations);
	public void setResources(Collection<Resource> resources);
	public void setSubManifests(Collection<IManifest> submanifests);	
	
	//Extra methods to add to Collections as these have multiplicity 0 to Many.
	public void addOrganization(Organization organization);
	public void addResource(Resource resource);
	public void addSubManifest(IManifest submanifest);
}
