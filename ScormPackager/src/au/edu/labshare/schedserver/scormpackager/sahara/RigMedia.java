/**
 * SAHARA Scheduling Server
 *
 * RigMedia Class. 
 * Allows the extracting of the following information from the DB:
 * 1. RigType
 * 2. RigTypeMedia
 * 3. RigTypeInformation 
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
package au.edu.labshare.schedserver.scormpackager.sahara;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigTypeDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;

/** 
 * Class to access the relevant database entities:
 * 1. RigType
 * 2. RigTypeMedia
 * 3. RigTypeInformation 
 * 
 */
public class RigMedia 
{
	private RigType rigType;
	//private RigTypeMedia rigMediaType;
	//private RigTypeInformation rigTypeInformation;
	
	// Rig Media MIME types supported
	public static final String MIME_JPEG = "image/jpeg";
	public static final String MIME_PNG= "image/png";
	public static final String MIME_GIF= "image/gif";
	public static final String MIME_SVG= "image/svg+xml";
	public static final String MIME_TXT = "text/plain"; //Support just .txt format
	
	
	private org.hibernate.Session db; 
	
	public RigMedia(org.hibernate.Session session)
	{
		this.db = session;
	}
	
	//Allow access methods to the media as we want to abstract the DAO end
	public RigType getRigType(String rigTypeName)
	{
		//Initialise the following entities as we need to access the DAO to configure these.
		//RigTypeDao rigTypeDao = new RigTypeDao();
		this.rigType = new RigTypeDao(db).findByName(rigTypeName);
		
		return this.rigType;
	}
}
