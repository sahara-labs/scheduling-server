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
 * @date 2nd September 2011
 */
package au.edu.uts.eng.remotelabs.schedserver.multisite.provider.requests.callback;

import java.rmi.RemoteException;
import java.util.Date;

import org.apache.axis2.AxisFault;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RemotePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.callback.types.SessionStarted;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.callback.types.UserSessionType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.PermissionIDType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.ResourceType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.SessionType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.requests.AbstractRequest;

/**
 * Notifies a consumer site that a session has started.
 */
public class SessionStartedAsyncRequest extends AbstractRequest
{
    /**
     * Notifies a consumer site a session has started.
     * 
     * @param ses session
     * @param db database 
     * @param callback callback when response or error received
     * @return true if successful
     */
    public boolean sessionStarted(Session ses, org.hibernate.Session db, MultiSiteCallbackHandler callback)
    {
        this.session = db;
        
        RemotePermission remotePerm = ses.getResourcePermission().getRemotePermission();
        if (remotePerm == null)
        {
            this.logger.warn("Unable to send session started notification because the session permission does not " +
            		"have a remote permission mapping.");
            return false;
        }
        
        if (ses.getRig() == null || ses.getAssignmentTime() == null)
        {
            this.logger.error("Unable to send session started notificatio because the session is not assigned to a rig.");
            return false;
        }
        
        this.site = remotePerm.getSite();

        this.logger.debug("Sending session starting notification for session for user '" + ses.getUser().qName() + 
                "' to site '" + this.site.getName() + "'.");
        
        if (!this.checkPreconditions()) return false;
        
        SessionStarted request = new SessionStarted();
        UserSessionType userSession = new UserSessionType();
        this.addSiteID(userSession);
        userSession.setUserID(ses.getUser().getName());
        request.setSessionStarted(userSession);
        
        PermissionIDType permId = new PermissionIDType();
        this.addSiteID(permId);
        permId.setPermissionID(remotePerm.getGuid());
        userSession.setPermission(permId);
        
        SessionType sesType = new SessionType();
        sesType.setIsReady(ses.isReady());
        sesType.setInGrace(ses.isInGrace());
        userSession.setSession(sesType);
        
        ResourceType resType = new ResourceType();
        resType.setType(ses.getResourceType());
        resType.setName(ses.getRequestedResourceName());
        sesType.setResource(resType);
        
        {
            Rig rig = ses.getRig();
            sesType.setRigType(rig.getRigType().getName());
            sesType.setRigName(rig.getName());
            sesType.setContactURL(rig.getContactUrl());
        }
        
        Date assignmentTime = ses.getAssignmentTime();
        Date now = new Date();
        
        sesType.setExtensions(ses.getExtensions());
        
        int time = (int)(now.getTime() - assignmentTime.getTime()) / 1000;
        sesType.setTime(time);
        
        /* Time left is allowed guarenteed time plus extension time minus elapsed time. */
        sesType.setTimeLeft(ses.getDuration() + ((ses.getResourcePermission().getAllowedExtensions() - ses.getExtensions()) 
                    * ses.getResourcePermission().getExtensionDuration()) - time);
        
        // FIXME warning messages
        
        try
        {
            this.getCallbackStub().startSessionStarted(request, callback);
        }
        catch (AxisFault e)
        {
            this.failed = true;
            this.failureReason = "Fault (" + e.getReason() + ")";
            this.logger.warn("SOAP fault initiating async SOAP request, error reason '" + e.getReason() +
                    "', error message is '" + e.getMessage() + "'.");
            this.offlineSite(e);
            return false;
        }
        catch (RemoteException e)
        {
            this.failed = true;
            this.failureReason = "Remote error (" + e.getMessage() + ")";
            this.logger.warn("Remote error initiating async request, error  message is '" + e.getMessage() + "'.");
            this.offlineSite(e);
            return false;
        }
        
        return true;
    }
}
