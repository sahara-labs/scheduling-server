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
 * @date 30th January 2011
 */
package au.edu.uts.eng.remotelabs.schedserver.multisite.provider.requests;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RemoteSite;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.GetSessionInformation;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.SessionType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.UserIDType;

/**
 * Makes a session information request.
 */
public class SessionInformationRequest extends AbstractRequest
{
    /** Response. */
    private SessionType response;
    
    public boolean getSessionInformation(User user, RemoteSite site, org.hibernate.Session db)
    {
        this.session = db;
        this.site = site;
        
        if (!this.checkPreconditions()) return false;
        
        /* Set up request parameters. */
        GetSessionInformation request = new GetSessionInformation();
        UserIDType uid = new UserIDType();
        this.addSiteID(uid);
        uid.setUserID(user.getName());
        request.setGetSessionInformation(uid);
        
        /* Make the remote call. */
        try
        {
            this.response = this.getStub().getSessionInformation(request).getGetSessionInformationResponse();
        }
        catch (AxisFault e)
        {
            this.failed = true;
            this.failureReason = "Fault (" + e.getReason() + ")";
            this.logger.warn("SOAP fault request, error reason '" + e.getReason() +
                    "', error message is '" + e.getMessage() + "'.");
            this.offlineSite(e);
            return false;
        }
        catch (RemoteException e)
        {
            this.failed = true;
            this.failureReason = "Remote error (" + e.getMessage() + ")";
            this.logger.warn("Remote error request, error  message is '" + e.getMessage() + "'.");
            this.offlineSite(e);
            return false;
        }
        
        return true;
    }
    
    static SessionInformationRequest load(SessionType type)
    {
        SessionInformationRequest info = new SessionInformationRequest();
        info.response = type;
        return info;
    }
    
    public boolean isReady()
    {
        return this.response.getIsReady();
    }
    
    public boolean isCodeAssigned()
    {
        return this.response.getIsCodeAssigned();
    }
    
    public boolean isInGrace()
    {
        return this.response.getInGrace();
    }
    
    public String getResourceName()
    {
        return this.response.getResource() == null ? null : this.response.getResource().getName();
    }
    
    public String getResourceType()
    {
        return this.response.getResource() == null ? null :this.response.getResource().getType();
    }
    
    public String getRigType()
    {
        return this.response.getRigType();
    }
    
    public String getRigName()
    {
        return this.response.getRigName();
    }
    
    public String getContactURL()
    {
        return this.response.getContactURL();
    }
    
    public int getDuration()
    {
        return this.response.getDuration();
    }
    
    public int getTimeLeft()
    {
        return this.response.getTimeLeft();
    }
    
    public int getExtensions()
    {
        return this.response.getExtensions();
    }
    
    public String getWarningMessage()
    {
        return this.response.getWarningMessage();
    }
}
