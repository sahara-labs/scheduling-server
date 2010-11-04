/**
 * SAHARA Scheduling Server
 *
 * ScormPackagerSkeletonInterface.java Class. 
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
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4  Built on : Apr 26, 2008 (06:24:30 EDT)
 *
 *
 * @author Herber Yeung
 * @date 4th November 2010
 */
package au.edu.labshare.schedserver.scormpackager.service;
    
/**
 *  ScormPackagerSkeletonInterface java skeleton interface for the axisService
 */
public interface ScormPackagerSkeletonInterface 
{
	/**
	 * Auto generated method signature
	 * 
	 * @param deleteSCO
	 */
	public au.edu.labshare.schedserver.scormpackager.types.DeleteSCOResponse deleteSCO(au.edu.labshare.schedserver.scormpackager.types.DeleteSCO deleteSCO);
        
         
	/**
	 * Auto generated method signature
	 * 
	 * @param validateSCO
	 */
	public au.edu.labshare.schedserver.scormpackager.types.ValidateSCOResponse validateSCO(au.edu.labshare.schedserver.scormpackager.types.ValidateSCO validateSCO);
        
	/**
	 * Auto generated method signature
	 * 
	 * @param createPIF
	 */
	public au.edu.labshare.schedserver.scormpackager.types.CreatePIFResponse createPIF(au.edu.labshare.schedserver.scormpackager.types.CreatePIF createPIF);
        
         
	/**
	 * Auto generated method signature
	 * 
	 * @param createSCO
	 */
	public au.edu.labshare.schedserver.scormpackager.types.CreateSCOResponse createSCO(au.edu.labshare.schedserver.scormpackager.types.CreateSCO createSCO);
        
         
	/**
	 * Auto generated method signature
	 * 
	 * @param validatePIF
	 */
	public au.edu.labshare.schedserver.scormpackager.types.ValidatePIFResponse validatePIF(au.edu.labshare.schedserver.scormpackager.types.ValidatePIF validatePIF);
        
         
	/**
	 * Auto generated method signature
	 * 
     * @param deletePIF
     */
	public au.edu.labshare.schedserver.scormpackager.types.DeletePIFResponse deletePIF(au.edu.labshare.schedserver.scormpackager.types.DeletePIF deletePIF);
        
         
	/**
	 * Auto generated method signature
	 * 
	 * @param validateManifest
	 */
	public au.edu.labshare.schedserver.scormpackager.types.ValidateManifestResponse validateManifest(au.edu.labshare.schedserver.scormpackager.types.ValidateManifest validateManifest);        
}
    