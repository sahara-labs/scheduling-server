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

package au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf;

import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.RegisterRig;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.RegisterRigResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.RegisterRigType;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.RemoveRig;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.RemoveRigResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.RemoveRigType;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.StatusType;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.UpdateRigStatus;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.UpdateRigStatusResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.UpdateRigType;


/**
 * LocalRigProviderSkeleton java skeleton for the axisService
 */
public class LocalRigProviderSkeleton implements LocalRigProviderSkeletonInterface
{
    /** Logger. */
    private Logger logger;

    public LocalRigProviderSkeleton()
    {
        this.logger = LoggerActivator.getLogger();
    }
    
    @Override
    public RegisterRigResponse registerRig(final RegisterRig registerRig)
    {
        RegisterRigType type = registerRig.getRegisterRig();
        StatusType status = type.getStatus();
        this.logger.info("Called " + this.getClass().getName() + "#registerRig with parameters: name=" + type.getName()
                + ", type=" + type.getType() + ", capabilities=" + type.getCapabilities() + ", isOnline=" + 
                status.getIsOnline() + ", offlineReason=" + status.getOfflineReason() + ".");
        throw new UnsupportedOperationException("Mock " + this.getClass().getName() + "#registerRig implementation.");
    }

    @Override
    public RemoveRigResponse removeRig(final RemoveRig removeRig)
    {
        RemoveRigType rem = removeRig.getRemoveRig();
        this.logger.info("Called " + this.getClass().getName() + "#removeRig with parameters: name=" + rem.getName() 
                + ", removal " + "reason=" + rem.getRemovalReason() + '.');
        throw new UnsupportedOperationException("Mock " + this.getClass().getName() + "#removeRig implementation.");
    }

    @Override
    public UpdateRigStatusResponse updateRigStatus(final UpdateRigStatus updateRigStatus)
    {
        UpdateRigType type = updateRigStatus.getUpdateRigStatus();
        StatusType status = type.getStatus();
        this.logger.info("Called " + this.getClass().getName() + "#updateRigStatus with parameters: name=" + type.getName()
                + ", isOnline=" + status.getIsOnline() + ", offlineReason=" + status.getOfflineReason() + '.');
        throw new UnsupportedOperationException("Mock " + this.getClass().getName() + "#updateRigStatus implementation.");
    }
}
