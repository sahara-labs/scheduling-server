/**
 * SAHARA Scheduling Server
 *
 * ShareableContentObjectCreator Abstract class
 * Defintion of methods to generate a SCO.
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

import au.edu.labshare.schedserver.scormpackager.IPackageInterchangeFile;

import java.io.File;
import java.util.Collection;
import java.util.zip.ZipFile;

public abstract class ShareableContentObjectCreator implements IPackageInterchangeFile
{
	/** 
	 * A PIF file needs to contain the following information
	 */
	private IManifest manifest;
	private File content[];
	
	/**
	 * Creates the Shareable Content Object (SCO) in zip format. 
	 * All files are zipped, not just the directory.
	 * According to standard need the following:
	 * @param manifest	Filename for the SCO in place of imsmanifest.xml. 
	 * 					If null is supplied imsmanifest.xml is used.   
	 * @param assets  	LearningResource e.g. WebPage, AudioFile, GIF imgae etc. [1..*]
	 * @param ouputPath The output location of the SCO.
	 */
	public abstract String createSCO(String mainfest, Collection <File> assets, String outputPath);
	
	/**
	 * Validate that the LMS (URL) is handled 
	 */
	public abstract boolean validateLMSConnection(ZipFile SCO);

	public void setManifest(IManifest manifest) 
	{
		this.manifest = manifest;
	}

	public IManifest getManifest() 
	{
		return manifest;
	}

	public void setContent(File content[]) 
	{
		this.content = content;
	}

	public File[] getContent() 
	{
		return content;
	} 
}
