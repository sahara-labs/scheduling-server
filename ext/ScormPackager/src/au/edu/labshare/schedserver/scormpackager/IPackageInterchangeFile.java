/**
 * SAHARA Scheduling Server
 *
 * IPackageInterchangeFile Interface
 * Package Interchange File (PIF) is the non LMS form of a SCO.
 * Should only contain content information, but no LMS related info.
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
import java.util.zip.ZipFile;

public interface IPackageInterchangeFile 
{	

	/**
	 * A generic form of a SCO or Asset is known as a Package Interchange File (PIF).
	 * This assumes manifest file is: "imsmanifest.xml"
	 * @return Path of the generated PIF file.
	 */
	public String createPIF(String experimentName, Collection <File> content, String outputPath);
	
	/**
	 * Validates the manifest file as according the SCORM: 
	 * 1. Manifest file is titled imsmanifest.xml
	 * 2. Needs to be at root of the content package (SCO)
	 * 3. Contains the following information:
	 * 	a) Metadata
	 * 	b) Organisations
	 * 	c) Resources
	 * 	d) sub-Manifests (if there are any)
	 */
	public boolean validateManifest(ZipFile PIF);
	
	/**
	 * Validates that there is one content component in the PIF
	 */
	public boolean validateContent(ZipFile PIF);
	
	/**
	 * Validates the application.xml/profile file that describes the experiment of the lab
	 */
	public boolean validateApplicationProfile(ZipFile PIF);
		
	/**
	 * Validates the <sequencing> and <sequencingCollection> tags
	 */
	public boolean validateSequencing(ZipFile PIF);
	
	/**
	 * Validates the <presentation> and <navigationInterface> tags
	 */
	public boolean validatePresentationNavigationInfo(ZipFile PIF);
	
	/**
	 * Validates all the metadata fields under the <lom> tag. Note that all Meta-data is optional. 
	 * As specified in section 4.3 of Sharable Content Object Reference Model
	 */
	public boolean validateMetaData(ZipFile SCO);
}
