/**
 * SAHARA Scheduling Server
 *
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
 * @date 31st July 2016
 */

package au.edu.uts.eng.remotelabs.schedserver.rigprovider.requests;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.RigCommunicationProxy.ActivityAsyncCallback;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.proxy.RigClientAsyncService;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.proxy.RigClientAsyncServiceCallbackHandler;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.proxy.RigClientService;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.proxy.intf.types.IsActivityDetectableResponse;

/**
 * Checks whether activity exists for a rig.
 */
public class RigActivity extends RigClientAsyncServiceCallbackHandler
{
    /** Callback when response is received for async activity operation. */
    private ActivityAsyncCallback callback;
    
    /** Logger. */
    private final Logger logger;
    
    public RigActivity()
    {
        this.logger = LoggerActivator.getLogger();
    }
    
    /**
     * Calls activity detection operation.
     * 
     * @param ses session information
     * @param db database session
     * @return true if has activity
     */
    public boolean isActivityDetectable(Session ses, org.hibernate.Session db)
    {
        try
        {
            RigClientService service = new RigClientService(ses.getRig(), db);
            IsActivityDetectableResponse response = service.isActivityDetectable();
            return response.getIsActivityDetectableResponse().getActivity();
        }
        catch (Exception e)
        {
            this.logger.warn("Failed to cal acitivty detection on a rig '" + ses.getRig().getName() + "', error " +
                    e.getClass().getSimpleName() + ": " + e.getMessage());
            return false;
        }        
    }
    
    /**
     * Calls activity detection operation async style.
     *  
     * @param ses session information
     * @param db database session
     * @param callback callback to use when received response
     */
    public void isActivityDetectable(Session ses, org.hibernate.Session db, ActivityAsyncCallback callback)
    {
        try
        {
            this.callback = callback;
            RigClientAsyncService service = new RigClientAsyncService(ses.getRig().getName(), db);
            
            service.isActivityDetectable(this);
        }
        catch (Exception e)
        {
            this.logger.error("Failed making async error call, error " + e.getClass().getSimpleName() + ": " + e.getMessage());
            this.callback.error(e);
        }        
    }
    
    @Override
    public void activityDetectionResponseCallback(final IsActivityDetectableResponse response)
    { 
        this.callback.response(response.getIsActivityDetectableResponse().getActivity());
    }

    @Override
    public void activityDetectionErrorCallback(final Exception e)
    {
        this.callback.error(e);
    }

}
