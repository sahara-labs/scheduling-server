/**
 * SAHARA Scheduling Server
 *
 * Item Class. 
 * The item attribute identifier in the manifest file.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2010, University of Technology, Sydney
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright 
 *    notice, this list of conditions and the following disclaimer in the 
 *    documentation and/or other materials provided with the distribution.
 *  * Neither the name of the University of Technology, Sydney nor the names 
 *    of its contributors may be used to endorse or promote products derived from 
 *    this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE 
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, 
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE 
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * @author Herber Yeung
 * @date 4th November 2010
 */
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
	
	public void addSubItem(Item item)
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
