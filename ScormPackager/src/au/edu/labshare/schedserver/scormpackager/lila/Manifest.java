package au.edu.labshare.schedserver.scormpackager.lila;

import java.util.ArrayList;
import java.util.Collection;

import au.edu.labshare.schedserver.scormpackager.IManifest;
import au.edu.labshare.schedserver.scormpackager.manifest.MetaData;
import au.edu.labshare.schedserver.scormpackager.manifest.Organization;
import au.edu.labshare.schedserver.scormpackager.manifest.Resource;

public class Manifest implements IManifest
{
	MetaData metadata;
	ArrayList<Organization> organizations;
	ArrayList<Resource> resources;
	ArrayList<IManifest> submanifests;
	
	public Manifest()
	{
		metadata = new MetaData();
		organizations = new ArrayList<Organization>();
		resources = new ArrayList<Resource>();
		submanifests = new ArrayList<IManifest>();
	}

	@Override
	public MetaData getMetaData() 
	{
		return metadata;
	}

	@Override
	public Collection<Organization> getOrganizations() 
	{
		return organizations;
	}

	@Override
	public Collection<Resource> getResources() 
	{
		return resources;
	}

	@Override
	public Collection<IManifest> getSubManifests() 
	{
		return submanifests;
	}

	@Override
	public void setMetaData(MetaData metadata) 
	{
		this.metadata = metadata; 
	}

	@Override
	public void setOrganizations(Collection<Organization> organizations) 
	{
		this.organizations = (ArrayList<Organization>) organizations;
	}

	@Override
	public void setResources(Collection<Resource> resources) 
	{
		this.resources = (ArrayList<Resource>) resources;
	}

	@Override
	public void setSubManifests(Collection<IManifest> submanifests) 
	{
		this.submanifests = (ArrayList<IManifest>) submanifests;
	}

	@Override
	public void addOrganization(Organization organization) 
	{
		this.organizations.add(organization);
	}

	@Override
	public void addResource(Resource resource) 
	{
		this.resources.add(resource);
	}

	@Override
	public void addSubManifest(IManifest submanifest) 
	{
		this.submanifests.add(submanifest);
	}
}
