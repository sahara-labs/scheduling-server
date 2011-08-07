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
 * @date 17th July 2011
 */
package au.edu.uts.eng.remotelabs.schedserver.multisite.provider.requests;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.hibernate.Session;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RemotePermission;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.AvailabilityResponseType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.CheckAvailability;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.PermissionIDType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.QueueTargetType;

/**
 * Checks the availability of a remote permission.
 */
public class PermissionAvailabilityCheck extends AbstractRequest
{
    /** Request response. */
    private AvailabilityResponseType response;
    

    public boolean checkAvailability(RemotePermission permission, Session ses)
    {
        this.site = permission.getSite();
        this.session = ses;
        
        if (!this.checkPreconditions()) return false;
        
        CheckAvailability request = new CheckAvailability();
        PermissionIDType pid = new PermissionIDType();
        this.addSiteID(pid);
        pid.setPermissionID(permission.getGuid());
        request.setCheckAvailability(pid);
        
        try
        {
            this.logger.debug("Making request to MultiSite#checkAvailability for permission '" + 
                    pid.getPermissionID() + "'.");
            this.response = this.getStub().checkAvailability(request).getCheckAvailabilityResponse();
            return true;
        }
        catch (AxisFault e)
        {
            this.failed = true;
            this.failureReason = "Fault (" + e.getReason() + ")";
            this.logger.warn("SOAP fault making request, error reason '" + e.getReason() + "', error message is '" + 
                    e.getMessage() + "'.");
            this.offlineSite(e);
            return false;
        }
        catch (RemoteException e)
        {
            this.failed = true;
            this.failureReason = "Remote error (" + e.getMessage() + ")";
            this.logger.warn("Remote error making request, error  message is '" + e.getMessage() + "'.");
            this.offlineSite(e);
            return false;
        }
    }
    
    public boolean isViable()
    {
        return this.response.getViable();
    }
    
    public boolean hasFree()
    {
        return this.response.getHasFree();
    }
    
    public boolean isQueueable()
    {
        return this.response.getIsQueueable();
    }
    
    public boolean isBookable()
    {
        return this.response.getIsBookable();
    }
    
    public boolean isCodeAssignable()
    {
        return this.response.getIsCodeAssignable();
    }
    
    public String getResourceType()
    {
        return this.response.getQueuedResource().getType().toString();
    }
    
    public String getResourceName()
    {
        return this.response.getQueuedResource().getName();
    }

    public List<QueueTarget> getQueueTargets()
    {
        List<QueueTarget> targets = new ArrayList<QueueTarget>();
        
        if (this.response.getQueueTarget() != null)
        {
            for (QueueTargetType qt : this.response.getQueueTarget()) targets.add(new QueueTarget(qt));
        }
        
        return targets;
    }
    
    /**
     * A resource which may be assigned as the result of a successful queue 
     * attempt. 
     */
    public class QueueTarget
    {
        /** Whether this is resource is viable. */
        private boolean viable;
        
        /** Whether this resource is free. */
        private boolean free;
        
        /** The name of the resource. */
        private String name;
        
        /** The type of the resource. Should be 'RIG'. */
        private String type;
        
        QueueTarget(QueueTargetType target)
        {
            this.viable = target.getViable();
            this.free = target.getIsFree();
            this.name = target.getResource().getName();
            this.type = target.getResource().getType();
        }

        public boolean isVaiable()
        {
            return this.viable;
        }

        public boolean isFree()
        {
            return this.free;
        }

        public String getName()
        {
            return this.name;
        }
        
        public String getType()
        {
            return this.type;
        }
    }
}
