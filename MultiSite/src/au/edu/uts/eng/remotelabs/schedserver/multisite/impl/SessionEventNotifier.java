/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2011, Michael Diponio
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
 * @date 6th September 2011
 */
package au.edu.uts.eng.remotelabs.schedserver.multisite.impl;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RemoteSite;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.SessionEventListener;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.callback.types.SessionFinished;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.callback.types.SessionUpdate;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.callback.types.UserSessionType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.PermissionIDType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.ResourceType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.SessionType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.UserIDType;

/**
 * Notifies a consumer site of session events such as session starting, 
 * session updates or session termination.
 */
public class SessionEventNotifier extends AbstractCallbackRequest implements SessionEventListener
{
    @Override
    public void eventOccurred(SessionEvent event, Session session, org.hibernate.Session db)
    {
        if (!session.getResourcePermission().isRemote()) return;
        
        if (session.getResourcePermission().getRemotePermission() == null)
        {
            this.logger.error("Unable to provide consumer notification of session termination of session '" + 
                    session.getId() + "' because there is no remote permission mapped.");
            return;
        }

        switch (event)
        {
            /* Queued session requires no notification because to queue a 
             * session is by consumer command, i.e. we don't need to tell
             * them what they have already told us. */
            case QUEUED:
                break;
                
            /* We only need to tell the consumer for every case, except 
             * if they have queued the rig and have been directly assigned. */
            case ASSIGNED:
                break;
                
            /* We need to always tell the consumer about a session being ready
             * because they need to set their mapped session to ready. */
            case READY:                
            /* We need to always specify time extensions because the consumer
             * needs to update the timing of their mapped session. */
            case EXTENDED:
            /* We need to always specify grace period because the consumer 
             * needs to mark their mapped session as in grace. */
            case GRACE:
                this.notifySessionUpdate(session, db);
                break;
                
            /* We need to always specify session termination because the 
             * consumer needs to terminate the users session. */
            case FINISHED:
                this.notifySessionFinished(session, db);
                break;
        }
    }
    
    /**
     * Notify a consumer site of updated session details.
     * 
     * @param session updated session
     * @param db database session
     */
    private void notifySessionUpdate(Session session, org.hibernate.Session db)
    {
        RemoteSite site = session.getResourcePermission().getRemotePermission().getSite();
        if (!this.checkPreconditions(site, db)) return;
        
        this.logger.debug("Notifying consumer site '" + site.getName() + "' of session update for user '" + 
                session.getUser().qName() + "'.");
        
        SessionUpdate request = new SessionUpdate();
        request.setSessionUpdate(this.getUserSession(session));
        
        SessionUpdateCallback cb = new SessionUpdateCallback(session);
        try
        {
            this.getStub(site).startSessionUpdate(request, cb);
        }
        catch (AxisFault e)
        {
            this.logger.warn("Unable to provide consumer notification of session termination of '" + 
                    session.getId() + "'. SOAP fault: " + e.getMessage());
            cb.terminateSession(db);
        }
        catch (RemoteException e)
        {
            this.logger.warn("Unable to provide consumer notification of session termination of '" + 
                    session.getId() + "'. Exception " + e.getClass().getSimpleName() + ": " + e.getMessage());
            cb.terminateSession(db);
        }
    }
    
    /**
     * Returns a populate user session type from an assigned session. 
     * 
     * @param session user's session
     * @return populate user session type
     */
    private UserSessionType getUserSession(Session session)
    {
        UserSessionType userSession = new UserSessionType();
        this.addSiteID(userSession);
        userSession.setUserID(session.getUser().getName());
        
        PermissionIDType permId = new PermissionIDType();
        this.addSiteID(permId);
        permId.setPermissionID(session.getResourcePermission().getRemotePermission().getGuid());
        userSession.setPermission(permId);
        
        SessionType sesType = new SessionType();
        sesType.setIsReady(true); //ses.isReady());
        sesType.setIsCodeAssigned(session.getCodeReference() != null);
        sesType.setInGrace(session.isInGrace());

        ResourceType res = new ResourceType();
        res.setType(session.getResourceType());
        res.setName(session.getRequestedResourceName());
        sesType.setResource(res);

        Rig rig = session.getRig();
        sesType.setRigType(rig.getRigType().getName());
        sesType.setRigName(rig.getName());
        sesType.setContactURL(rig.getContactUrl());

        sesType.setDuration(session.getDuration());
        /* Time left is allowed duration plus extension time minus elapsed
         * time. */
        sesType.setTimeLeft(session.getDuration() + ((session.getResourcePermission().getAllowedExtensions() - 
                session.getExtensions()) * session.getResourcePermission().getExtensionDuration()) - 
                (int)(System.currentTimeMillis() - session.getAssignmentTime().getTime()) / 1000);
        sesType.setExtensions(session.getExtensions());
        
        userSession.setSession(sesType);
        return userSession;
    }

    /**
     * Notify a consumer site that a session has finished.
     * 
     * @param session session that has finished
     * @param db database session
     */
    private void notifySessionFinished(Session session, org.hibernate.Session db)
    {
        RemoteSite site = session.getResourcePermission().getRemotePermission().getSite();
        if (!this.checkPreconditions(site, db)) return;
        
        this.logger.debug("Notifying consumer site '" + site.getName() + "' that the session for '" + 
                session.getUser().qName() + "' has finished.");
        
        SessionFinished request = new SessionFinished();
        UserIDType user = new UserIDType();
        this.addSiteID(user);
        user.setUserID(session.getUser().getName());
        request.setSessionFinished(user);
        
        try
        {
            this.getStub(site).startSessionFinished(request, new SessionFinishedCallback(session));
        }
        catch (AxisFault e)
        {
            this.logger.warn("Unable to provide consumer notification of session termination of '" + 
                    session.getId() + "'. SOAP fault: " + e.getMessage());
        }
        catch (RemoteException e)
        {
            this.logger.warn("Unable to provide consumer notification of session termination of '" + 
                    session.getId() + "'. Exception " + e.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }
}
