/**
 * SAHARA Scheduling Server
 * Schedules and assigns local laboratory rigs.
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
 * @author Michael Diponio (mdiponio)
 * @date 18th January 2010
 */

package au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf;

import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.types.RegisterRig;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.types.RegisterRigResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.types.RemoveRig;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.types.RemoveRigResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.types.UpdateRigStatus;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.types.UpdateRigStatusResponse;

/**
 * LocalRigProviderSkeletonInterface, interface for the local rig provider 
 * SOAP operations.
 */
public interface LocalRigProviderSkeletonInterface
{
    /**
     * Registers a rig with the scheduling server.
     * 
     * @param registerRig request parameters
     * @return response parameters
     */
    public RegisterRigResponse registerRig(RegisterRig registerRig);

    /**
     * Updates the status of a rig.
     * 
     * @param updateRigStatus request parameters
     * @return response parameters
     */
    public UpdateRigStatusResponse updateRigStatus(UpdateRigStatus updateRigStatus);

    /**
     * Removes a rig registration.
     * 
     * @param removeRig request parameters
     * @return response parameters
     */
    public RemoveRigResponse removeRig(RemoveRig removeRig);
}
