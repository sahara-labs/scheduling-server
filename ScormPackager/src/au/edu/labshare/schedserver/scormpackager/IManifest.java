/**
 * SAHARA Scheduling Server
 *
 * IManifest Interface
 * Manifest properties to be populated/decorated. 
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
package au.edu.labshare.schedserver.scormpackager;

import java.io.File;
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
	
	//Utility methods to help with creating the structure of the manifest
	public Collection<Organization> generateOrganisations(String[] institutions, String[] titles, File[] items);
	public Collection<Resource>     generateResources(File[] files);
	public Collection<IManifest>    generateSubManifests();
}
