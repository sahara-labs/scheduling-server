package au.edu.labshare.schedserver.scormpackager.manifest;

import java.util.ArrayList;

import au.edu.labshare.schedserver.scormpackager.manifest.MetaData;

public class Item 
{
	public static final int STRING_PARAM_MAX_LENGTH = 1000;
		
	String title;
	String id;
	String ref;
	String parameters;
	boolean isVisible;
	
	ArrayList<Item> item;
	MetaData metadata;
	
	public Item()
	{
		item = new ArrayList<Item>();
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public String getID()
	{
		return id;
	}
	
	public void setID(String identifier)
	{
		id = identifier; 
	}
	
	public String getReference()
	{
		return ref;
	}
	
	public void setReference(String identifierref)
	{
		ref = identifierref;
	}
	
	public boolean getVisibility()
	{
		return isVisible;
	}
	
	public void setVisibility(boolean visibility)
	{
		this.isVisible = visibility;
	}
	
	public String getParameters()
	{
		return parameters;
	}
	
	public void setParameters(String parameters)
	{
		if(parameters.length() <= STRING_PARAM_MAX_LENGTH)
			this.parameters = parameters;
	}

	public Item getSubItem(int index)
	{
		return item.get(index);
	}
	
	public void setSubItem(Item item)
	{
		if(item != null)
			this.item.add(item);
	}
	
	public MetaData getMetadata()
	{
		return metadata;
	}
	
	public void setMetaData(MetaData metadata)
	{
		this.metadata = metadata;
	}
}
