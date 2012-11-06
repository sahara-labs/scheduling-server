/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2011, University of Technology, Sydney
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
 * @author Michael Diponio (mdiponio)
 * @date 29th January 2011
 */

package au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf;

import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.CancelRigOffline;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.CancelRigOfflineResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.FreeRig;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.FreeRigResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.GetRig;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.GetRigResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.GetTypeStatus;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.GetTypeStatusResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.GetTypes;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.GetTypesResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.PutRigOffline;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.PutRigOfflineResponse;

/**
 * Rig Management SOAP service interface.
 */
public interface RigManagementSOAP
{
    /**
     * Get all rig types.
     * 
     * @param request
     * @return response
     */
    public GetTypesResponse getTypes(GetTypes request);
    
    /**
     * Gets a rig type status. 
     * 
     * @param request
     * @return response
     */
    public GetTypeStatusResponse getTypeStatus(GetTypeStatus request);
    
    /**
     * Gets a rig's details.
     * 
     * @param request
     * @return response
     */
    public GetRigResponse getRig(GetRig request);
    
    /**
     * Puts a rig off-line for a period of time either now or in the future.
     * 
     * @param request 
     * @return response
     */
    public PutRigOfflineResponse putRigOffline(PutRigOffline request);
    
    /**
     * Cancels a scheduled rig offline period. 
     * 
     * @param request
     * @return response
     */
    public CancelRigOfflineResponse cancelRigOffline(CancelRigOffline request);
    
    /**
     * Frees a rig by terminating any in progress session on the rig.
     * 
     * @param request
     * @return response
     */
    public FreeRigResponse freeRig(FreeRig request);
}
