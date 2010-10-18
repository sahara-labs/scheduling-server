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
 * @date 17th October 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.service;

import java.util.Date;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.service.types.AllocateCallback;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.service.types.AllocateCallbackResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.service.types.CallbackRequestType;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.service.types.CallbackResponseType;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.service.types.ErrorType;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.service.types.ReleaseCallback;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.service.types.ReleaseCallbackResponse;

/**
 * Rig Client proxy implementation.
 */
public class RigClientProxy implements RigClientProxyInterface
{
    /** Logger. */
    private Logger logger;
    
    public RigClientProxy()
    {
        this.logger = LoggerActivator.getLogger();
    }
    
    @Override
    public AllocateCallbackResponse allocateCallback(AllocateCallback allocateCallback)
    {
        CallbackRequestType request = allocateCallback.getAllocateCallback();
        this.logger.debug("Received allocate callback with params: rigname=" + request.getRigname() + ", success=" +
                request.getSuccess() + '.');
        
        AllocateCallbackResponse response = new AllocateCallbackResponse();
        CallbackResponseType status = new CallbackResponseType();
        response.setAllocateCallbackResponse(status);
        
        /* Load session from rig. */
        RigDao dao = new RigDao();
        Rig rig = dao.findByName(request.getRigname());
        Session ses = null;
        if (rig == null)
        {
            /* If the rig wasn't found, something is seriously wrong. */
            this.logger.error("Received allocate callback for rig '" + request.getRigname() + "' that doesn't exist.");
            status.setSuccess(false);
        }
        else if ((ses = rig.getSession()) == null)
        {
            this.logger.warn("Received allocate callback for session that doesn't exist. Rig who sent callback " +
            		"response was '" + request.getRigname() + "'.");
            status.setSuccess(false);
            
            /* Make sure the rig is no marked as in session. */
            rig.setInSession(false);
        }
        else if (request.getSuccess())
        {
           /* If the response from allocate is successful, put the session to ready. */
           ses.setReady(true);
           status.setSuccess(true);
        }
        else
        {
            ErrorType err = request.getError();
            this.logger.error("Received allocate response for " + ses.getUserNamespace() + ':' + 
                    ses.getUserName() + ", allocation not successful. Error reason is '" + err.getReason() + "'.");
            
            /* Allocation failed so end the session and take the rig offline depending on error. */
            ses.setActive(false);
            ses.setReady(false);
            ses.setRemovalReason("Allocation failure with reason '" + err.getReason() + "'.");
            ses.setRemovalTime(new Date());
            
            if (err.getCode() == 4) // Error code 4 is an existing session exists
            {
                this.logger.error("Allocation failure reason was caused by an existing session, so not putting rig offline " +
                        "because a session already has it.");
            }
            else
            {
                rig.setInSession(false);
                rig.setOnline(false);
                rig.setOfflineReason("Allocation failured with reason '" + err.getReason() + "'.");
                rig.setSession(null);
            }
            
            /* Whilst allocation was not successful, the process was clean. */
            status.setSuccess(true);
        }
        
        dao.flush();
        dao.closeSession();
        return response;
    }

    @Override
    public ReleaseCallbackResponse releaseCallback(ReleaseCallback releaseCallback)
    {
        return null;
    }
}
