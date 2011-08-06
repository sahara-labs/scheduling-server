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
 * @date 28th July 2011
 */

package au.edu.uts.eng.remotelabs.schedserver.multisite.provider;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.hibernate.Session;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RemotePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.AddToQueue;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.PermissionIDType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.QueueRequestType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.UserIDType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.UserStatusType;

/**
 * Adds a user to the queue.
 */
public class QueueRequest extends AbstractRequest
{
    /** Request response. */
    private UserStatusType response;
    
    public boolean addToQueue(RemotePermission remotePerm, User user, Session ses)
    {
        this.site = remotePerm.getSite();
        this.session = ses;
        
        if (!this.checkPreconditions()) return false;
        
        /* Set up request parameters. */
        AddToQueue request = new AddToQueue();
        QueueRequestType type = new QueueRequestType();
        this.addSiteID(type);
        request.setAddToQueue(type);
        
        PermissionIDType pid = new PermissionIDType();
        this.addSiteID(pid);
        pid.setPermissionID(remotePerm.getGuid());
        type.setPermission(pid);
        
        UserIDType uid = new UserIDType();
        this.addSiteID(uid);
        uid.setUserID(user.getName());
        type.setUser(uid);
        
        /* Make the remote call. */
        try
        {
            this.response = this.getStub().addToQueue(request).getAddToQueueResponse();
        }
        catch (AxisFault e)
        {
            this.failed = true;
            this.failureReason = "Fault (" + e.getReason() + ")";
            this.logger.warn("SOAP fault making checking request, error reason '" + e.getReason() +
                    "', error message is '" + e.getMessage() + "'.");
            this.offlineSite(e);
            return false;
        }
        catch (RemoteException e)
        {
            this.failed = true;
            this.failureReason = "Remote error (" + e.getMessage() + ")";
            this.logger.warn("Remote error making checking request, error  message is '" + e.getMessage() + "'.");
            this.offlineSite(e);
            return false;
        }
        
        return true;
    }
    
    public boolean wasOperationSuccessful()
    {
        return this.response.getOperation() == null ? false : this.response.getOperation().getWasSuccessful();
    }
    
    public String getOperationReason()
    {
        return this.response.getOperation() == null ? null : this.response.getOperation().getReason();
    }
   
    public boolean isInQueue()
    {
        return this.response.getInQueue();
    }
    
    public boolean isInBooking()
    {
        return this.response.getInBooking();
    }
    
    public boolean isInSession()
    {
        return this.response.getInSession();
    }
    
    public String getResourceName()
    {
        if (this.response.getInQueue() && this.response.getQueuedResource() != null)
        {
            return this.response.getQueuedResource().getName();
        }
        else if (this.response.getInBooking() && this.response.getBookedResource() != null)
        {
            return this.response.getBookedResource().getName();
        }
        
        return null;
    }
    
    public String getResourceType()
    {
        if (this.response.getInQueue() && this.response.getQueuedResource() != null)
        {
            return this.response.getQueuedResource().getType();
        }
        else if (this.response.getInBooking() && this.response.getBookedResource() != null)
        {
            return this.response.getBookedResource().getType();
        }
        
        return null;
    }

    public SessionInformation getSession()
    {
        return this.response.getSession() == null ? null : SessionInformation.load(this.response.getSession());
    }
}
