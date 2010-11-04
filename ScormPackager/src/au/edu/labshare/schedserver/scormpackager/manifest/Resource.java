/**
 * SAHARA Scheduling Server
 *
 * Resource Class. 
 * The resource attribute identifier in the manifest file.
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

import au.edu.labshare.schedserver.scormpackager.manifest.Dependency;
import au.edu.labshare.schedserver.scormpackager.manifest.ResourceFile;
import au.edu.labshare.schedserver.scormpackager.manifest.MetaData;

public class Resource 
{
	/* Unique to the adlscp xsd schema types that are defined by . Can be extended.*/
	public static final String SCORMTYPE_SCO = "sco";
	public static final String SCORMTYPE_ASSET = "asset";
	
	/* Technically can be any path, but we are going to have a unique default path specific 
	 * to LiLa but will allow for different paths */
	public static final String RESOURCE_LILA_PATH = "lib";
	
	/* Type is set to webcontent and there is no reference where this is otherwise. */
	public static final String LILA_TYPE = "webcontent";
	
	String id;
	String type; //According to the adlcp any type is allowed but needs to be specified. 
	String scormtype; //Can be either SCO or asset as designated by adlscp:scormtype
	String href;
	MetaData metadata;
	ArrayList<ResourceFile> fileList;
	ArrayList<Dependency> dependencyList;
	
	public Resource(String identifier)
	{
		id = identifier;
		type = LILA_TYPE;
		
		fileList = new ArrayList<ResourceFile>();
		dependencyList = new ArrayList<Dependency>();
	}
	
	public String getIdentifier()
	{
		return id;
	}
	
	public void setIdentifier(String identifier)
	{
		id = identifier;
	}
	
	public String getType()
	{
		return type;
	}
	
	public void setType(String type)
	{
		this.type = type;
	}
	
	public String getScormType()
	{
		return scormtype;
	}
	
	public void setScormType(String assetType)
	{	
		if(assetType.equals(SCORMTYPE_SCO) || assetType.equals(SCORMTYPE_ASSET))
			scormtype = assetType; 
		else //Set it to most generic type - asset. Cannot be underfined
			scormtype = SCORMTYPE_ASSET;
	}
	
	public String getHRef()
	{
		return href;
	}
	
	public void setHRef(String href)
	{
		if(href.length() <= ResourceFile.STRING_HREF_MAX_LENGTH)
			this.href = href;
	}
	
	public MetaData getMetaData()
	{
		return metadata;
	}
	
	public void setMetaData(MetaData metadata)
	{
		this.metadata = metadata;
	}
	
	public ResourceFile getPath(int index)
	{
		return fileList.get(index);
	}
	
	public void addPath(ResourceFile file)
	{
		fileList.add(file);
	}
	
	public Dependency getDepedency(int index)
	{
		return dependencyList.get(index);
	}
	
	public void addDependency(Dependency dependency)
	{
		dependencyList.add(dependency);
	}
}
