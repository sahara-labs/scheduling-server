/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2011, University of Technology, Sydney
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
package au.edu.uts.eng.remotelabs.schedserver.multisite.impl;

import org.apache.axis2.AxisFault;
import org.hibernate.Session;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RemoteSite;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.multisite.MultiSiteActivator;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.callback.client.MultiSiteCallbackClientStub;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.SiteIDType;

/**
 * Base class of callback classes.
 */
public class AbstractCallbackRequest
{
    /** Dummy GUID. */
    public static final String DEFAULT_SITE_ID = "00000000-0000-0000-0000-000000000000";
    
    /** Logger. */
    protected Logger logger;
    
    public AbstractCallbackRequest()
    {
        this.logger = LoggerActivator.getLogger();
    }
    
    /**
     * Checks whether the site is receiving requests.
     * 
     * @param site remote site
     * @param db database session
     * @return true if it is OK to send request
     */
    protected boolean checkPreconditions(RemoteSite site, Session db)
    {
        // FIXME Check whether the site is live
        
        return true;
    }
    
    /**
     * Gets the 
     * @return
     * @throws AxisFault
     */
    protected MultiSiteCallbackClientStub getStub(RemoteSite site) throws AxisFault
    {
        return new MultiSiteCallbackClientStub(site.getServiceAddress());
    }
 
    /**
     * Adds the site ID of this site to the request.
     * 
     * @param sid site ID.
     */
    protected void addSiteID(SiteIDType sid)
    {
        String guid = MultiSiteActivator.getConfigValue("Site_ID", null);
        if (guid == null)
        {
            this.logger.error("Could not load this site's identifier. Without having this identifier MultiSite " +
            		"communication will fail. Make sure the property 'Site_ID' with an appropriate GUID.");
            sid.setSiteID(DEFAULT_SITE_ID);
        }
        else sid.setSiteID(guid);
    }
}
