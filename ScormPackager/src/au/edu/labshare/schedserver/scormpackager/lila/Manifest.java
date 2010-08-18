package au.edu.labshare.schedserver.scormpackager.lila;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import au.edu.labshare.schedserver.scormpackager.IManifest;
import au.edu.labshare.schedserver.scormpackager.manifest.MetaData;
import au.edu.labshare.schedserver.scormpackager.manifest.Organization;
import au.edu.labshare.schedserver.scormpackager.manifest.Resource;

public class Manifest implements IManifest
{
	public static final String ITEM_NAME = "item";
	public static final String HTML_EXT = "html";
	
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
	
	@Override
	public Collection<Organization> generateOrganisations(String[] institutions, String[] titles, File[] items) 
	{
		//Cycle through the institutions and titles and items to generate the 
		//<organization> and <item> elements
		Organization organization = null;
		String filenameExt = null;
		String filenameWithoutExtension = null;
		
		for(int i = 0; i < institutions.length; i++)
		{
			organization = new Organization();
			organization.setID(institutions[i]);
			
			for(int j = 0; j < items.length; j++)
			{
				//Extrapolate the name from the items and pull out the *.html. Check that it is a html file first
				String filename = items[j].getName(); 
				
				if(filename.lastIndexOf(".") != -1)
				{
					filenameExt = filename.substring(filename.lastIndexOf(".") + 1,filename.length());
					filenameWithoutExtension  = filename.substring(0, filename.lastIndexOf("."));
				}
				else
				{
					filenameExt = "";
					filenameWithoutExtension = filename.substring(0, filename.length());
				}
				
				//Assign the filename without extension to the identifierref=attribute and itemX to identifier=attribute
				if(filenameExt.equals(HTML_EXT)) 
				{
					organization.addItem(titles[j], ITEM_NAME + Integer.toString(j+1), filenameWithoutExtension);
					organizations.add(organization);
				}
			}
		}
		
	
		return organizations;
	}

	@Override
	public Collection<Resource> generateResources(File[] files) 
	{
		//TODO Need to complete
		
		//Initialise variables
		Resource resource = null;
		String filePath   = null;
		

		//Place values into respective sections
		for(int i = 0; i < files.length; i++)
		{
			filePath = files[i].getPath(); //Assumes that the path is a relative path e.g. lib/random.jar
		}
		
		return resources;
	}

	@Override
	public Collection<IManifest> generateSubManifests() 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
}
