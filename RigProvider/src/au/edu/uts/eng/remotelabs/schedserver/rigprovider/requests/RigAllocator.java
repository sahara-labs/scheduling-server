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
 * @date 4th January 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.rigprovider.requests;

import java.util.Date;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigLogDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.SessionDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.RigEventListener.RigStateChangeEvent;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.SessionEventListener.SessionEvent;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.RigProviderActivator;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.proxy.RigClientAsyncService;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.proxy.RigClientAsyncServiceCallbackHandler;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.proxy.intf.types.AllocateResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.proxy.intf.types.ErrorType;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.proxy.intf.types.OperationResponseType;

/**
 * Call back handler which sets the session to ready.
 */
public class RigAllocator extends RigClientAsyncServiceCallbackHandler
{
    /** The timeout in seconds before an asynchronous SOAP operation fails. */
    public static final int RIG_CLIENT_ASYNC_TIMEOUT = 120;
    
    
    /** Session that is being allocated. */
    private Session session;
    
    /** Logger. */
    private Logger logger;
    
    public RigAllocator()
    {
        this.logger = LoggerActivator.getLogger();
    }
    
    /**
     * Allocates the user in the session to the rig client by calling the rig 
     * client allocate operation.
     * 
     * @param ses session information
     * @param db database session
     */
    public void allocate(Session ses, org.hibernate.Session db)
    {
        Rig rig = ses.getRig();
        this.session = ses;
        
        try
        {
            int setupTime = rig.getRigType().getSetUpTime();
            boolean async = setupTime > 0 && setupTime > RigAllocator.RIG_CLIENT_ASYNC_TIMEOUT - 10;
            
            RigClientAsyncService service = new RigClientAsyncService(rig.getName(), db);
            service.allocate(ses.getUserName(), async, this);
        }
        catch (Exception e)
        {
            this.logger.error("Failed calling rig client allocate to " + rig.getName() + " at " + 
                    rig.getContactUrl() + " because of error " + e.getMessage() + ".");

            /* Terminate the session. */
            ses.setActive(false);
            ses.setRemovalReason("Allocation fail to " + rig.getName() + ", with error " + 
                    e.getMessage() + ".");
            ses.setRemovalTime(new Date());
            db.beginTransaction();
            db.flush();
            db.getTransaction().commit();
            
            /* Fire event the session is terminated. */
            RigProviderActivator.notifySessionEvent(SessionEvent.FINISHED, ses, db);

            /* Put the rig offline. */
            rig.setInSession(false);
            rig.setOnline(false);
            rig.setOfflineReason("Allocation failed for session " + ses.getId() + ".");
            rig.setSession(null);
            db.beginTransaction();
            db.flush();
            db.getTransaction().commit();
            
            /* Log when the rig is offline. */
            RigLogDao rigLogDao = new RigLogDao(db);
            rigLogDao.addOfflineLog(rig, "Allocation failed with error: " + e.getMessage());
            
            /* Fire event the rig is offline. */
            RigProviderActivator.notifyRigEvent(RigStateChangeEvent.OFFLINE, rig, db);
        }
    }
    
    @Override
    public void allocateResponseCallback(final AllocateResponse response)
    {
        OperationResponseType op = response.getAllocateResponse();        
        if (op.getWillCallback())
        {
            /* The response will come in a callback request so no work required now. */
            this.logger.debug("Received notification allocation for rig " + this.session.getAssignedRigName() + 
                    " will come in a callback message.");
            return;
        }
        
        SessionDao dao = new SessionDao();
        this.session = dao.merge(this.session);
        if (op.getSuccess())
        {
            this.logger.debug("Received allocate response for " + this.session.getUserNamespace() + ':' + 
                    this.session.getUserName() + ", allocation successful.");
            
            /* The session is being set to ready, so it may be used. */
            this.session.setReady(true);
            dao.flush();
            
            RigProviderActivator.notifySessionEvent(SessionEvent.READY, this.session, dao.getSession());
        }
        else
        {
            ErrorType err = op.getError();
            this.logger.error("Received allocate response for " + this.session.getUserNamespace() + ':' + 
                    this.session.getUserName() + ", allocation not successful. Error reason is '" + err.getReason() + "'.");
            
            /* Allocation failed so end the session and take the rig off line depending on error. */
            this.session.setActive(false);
            this.session.setReady(false);
            this.session.setRemovalReason("Allocation failure with reason: " + err.getReason());
            this.session.setRemovalTime(new Date());
            dao.flush();
            RigProviderActivator.notifySessionEvent(SessionEvent.FINISHED, this.session, dao.getSession());
            
            if (err.getCode() == 4) // Error code 4 is an existing session exists
            {
                this.logger.error("Allocation failure reason was caused by an existing session, so not putting rig offline " +
                        "because a session already has it.");
            }
            else
            {
                Rig rig = this.session.getRig();
                rig.setInSession(false);
                rig.setOnline(false);
                rig.setOfflineReason("Allocation failed with reason: '" + err.getReason() + "'.");
                rig.setSession(null);
                dao.flush();
                
                /* Log when the rig is offline. */
                RigLogDao rigLogDao = new RigLogDao(dao.getSession());
                rigLogDao.addOfflineLog(rig, "Allocation failed with reason: " + err.getReason());
                
                /* Fire event the rig is offline. */
                RigProviderActivator.notifyRigEvent(RigStateChangeEvent.OFFLINE, rig, dao.getSession());
            }
        }
        
        dao.closeSession();
    }
    
    @Override
    public void allocateErrorCallback(final Exception e)
    {
        SessionDao dao = new SessionDao();
        this.session = dao.merge(this.session);
        Rig rig = this.session.getRig();
        
        this.logger.error("Received error response from allocation of " + this.session.getUserNamespace() + ':' + 
                this.session.getUserName() + " to rig " + rig.getName() + " at " + rig.getContactUrl() + ". Error message" +
                " is '" + e.getMessage() + "'.");
        
        /* Allocation failed so end the session and take the rig offline. */
        this.session.setActive(false);
        this.session.setReady(false);
        this.session.setRemovalReason("Allocation failure with SOAP error '" + e.getMessage() + "'.");
        this.session.setRemovalTime(new Date());
        dao.flush();
        RigProviderActivator.notifySessionEvent(SessionEvent.FINISHED, this.session, dao.getSession());
        
        rig.setInSession(false);
        rig.setOnline(false);
        rig.setOfflineReason("Allocation failed with SOAP error '" + e.getMessage() + "'.");
        rig.setSession(null);
        dao.flush();
        
        /* Log when the rig is offline. */
        RigLogDao rigLogDao = new RigLogDao(dao.getSession());
        rigLogDao.addOfflineLog(rig, "Allocation failed with error: " + e.getMessage());
        
        
        /* Fire event the rig is offline. */
        RigProviderActivator.notifyRigEvent(RigStateChangeEvent.OFFLINE, rig, dao.getSession());

        dao.closeSession();
    }
}
