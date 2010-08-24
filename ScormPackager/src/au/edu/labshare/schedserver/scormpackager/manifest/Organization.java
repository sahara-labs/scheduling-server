package au.edu.labshare.schedserver.scormpackager.manifest;

import java.util.ArrayList;

import au.edu.labshare.schedserver.scormpackager.manifest.Item;
import au.edu.labshare.schedserver.scormpackager.manifest.MetaData;


public class Organization 
{
	public static final int STRING_STRUCT_MAX_LENGTH = 200; 
	
	String id;
	String title;
	String structure;
	Item item; 
	MetaData metadata;
	
	ArrayList<Item> itemList;

	
	public Organization()
	{
		itemList = new ArrayList<Item>();
	}
	
	public String getID()
	{
		return id;
	}
	
	public void setID(String identifier)
	{
		id = identifier; 
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String titleText)
	{
		title = titleText;
	}
	
	public Item getItem(int index)
	{
		return itemList.get(index);
	}
	
	public void addItem(String title, String identifier, String identifierRef)
	{
		item = new Item();
		item.setID(identifier);
		item.setReference(identifierRef);
		item.setTitle(title);
		itemList.add(item);
	}
	
	public ArrayList<Item> getItemList()
	{
		return itemList;
	}
	
	public MetaData getMetaData()
	{
		return metadata;
	}
	
	public void setMetaData(MetaData metadata)
	{
		this.metadata = metadata;
	}
	
	public String getStructure()
	{
		return structure;
	}
	
	public void setStructure(String structure)
	{
		if(structure.length() <= STRING_STRUCT_MAX_LENGTH)
			this.structure = structure;
	}
}
