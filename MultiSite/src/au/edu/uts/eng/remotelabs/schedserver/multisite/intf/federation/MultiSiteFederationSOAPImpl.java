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
 * @date 26th October 2011
 */

package au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation;

import static au.edu.uts.eng.remotelabs.schedserver.multisite.MultiSiteActivator.getConfigValue;

import java.util.Date;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RemoteSiteDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RemoteSite;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.DiscoverResources;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.DiscoverResourcesResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.GetRequests;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.GetRequestsResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.InitiateSite;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.InitiateSiteResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.InitiateSiteResponseType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.NotifyAccept;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.NotifyAcceptResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.NotifyCancel;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.NotifyCancelResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.NotifyModify;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.NotifyModifyResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.RequestResource;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.RequestResourceResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.SiteReconnect;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.SiteReconnectResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.SiteShutdown;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.SiteShutdownResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.SiteStatus;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.SiteStatusResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.SiteType;

/**
 * MultiSite federation service implementation.
 */
public class MultiSiteFederationSOAPImpl implements MultiSiteFederationSOAP
{
    /** Logger. */
    private final Logger logger;
    
    public MultiSiteFederationSOAPImpl()
    {
        this.logger = LoggerActivator.getLogger();
    }
    
    @Override
    public InitiateSiteResponse initiateSite(final InitiateSite request)
    {
        final String name = request.getInitiateSite().getName();
        final String siteId = request.getInitiateSite().getSiteID();
        final String namespace = request.getInitiateSite().getNamespace();
        final String address = request.getInitiateSite().getAddress();
        this.logger.debug("Received " + this.getClass().getSimpleName() + "#initiateSite with params: name=" + name +
                ", site identifier=" + siteId + ", namespace=" + namespace + ", address=" + address);
        
        /* Setup response parameters. */
        InitiateSiteResponse response = new InitiateSiteResponse();
        InitiateSiteResponseType responseType = new InitiateSiteResponseType();
        responseType.setWasSuccessful(false);
        response.setInitiateSiteResponse(responseType);
        
        SiteType siteType = new SiteType();
        if (getConfigValue("Site_Name", null) == null || getConfigValue("Site_Namespace", null) == null ||
                getConfigValue("Site_ID", null) == null || getConfigValue("Multisite_Address", null) == null)
        {
            this.logger.warn("Unable to initiate the remote site '" + name + "' with this site because this site " +
            		"is not correctly configured. Ensure the properites 'Site_Name', 'Site_Namespace', 'Site_ID' " +
            		"and 'MultiSite_Address' is correctly configured.");
            responseType.setReason("Site incorrectly configured.");
        }
        
        siteType.setName(getConfigValue("Site_Name", null));
        siteType.setNamespace(getConfigValue("Site_Namespace", null));
        siteType.setAddress(getConfigValue("Site_Address", null) + "/SchedulingServer-Multisite/services/MultiSite");
        siteType.setSiteID(getConfigValue("Site_ID", null));
        responseType.setSite(siteType);
        
        RemoteSiteDao dao = new RemoteSiteDao();
        try
        {
            boolean requiresPersist = false;
            
            /* We can only initiate a site if the site doesn't exist or the 
             * site exists but is offline. */
            RemoteSite site = dao.findSite(siteId);
            if (site == null)
            {
                /* Site doesn't exist so we can safely add it as initiation. */
                this.logger.info("Adding a new remote site with name '" + name + "'.");
                
                site = new RemoteSite();
                site.setGuid(siteId);
                
                site.setViewer("true".equals(getConfigValue("Default_Allow_Viewers", "false")));
                site.setRequestor("true".equals(getConfigValue("Default_Allow_Requestors", "false")));
                site.setAuthorizer(false); // The authorizer delegation is higher, so it must be explicit.
                site.setRedirectee(false); // No unexpected redirections.
                
                requiresPersist = true;
            }
            else if (site.isOnline())
            {
                /* Site exists and is online, this is an error. Either the site
                 * is sending spurious initiations or there a unlikely GUID
                 * collision has occurred. */
                this.logger.warn("Unable to add site '" + site.getName() + "' as the site already exists and is " +
                		"online.");
                responseType.setReason("Site already connected");
                return response;
            }
            else
            {
                /* Site is currently offline so we can initiate it. The permission
                 * set is left unchanged. */
                this.logger.info("Offline site '" + name + "' is being put back online from a site initiation.");
            }
            
            site.setOnline(true);
            site.setName(name);
            site.setUserNamespace(namespace);
            site.setServiceAddress(address);
            site.setLastPush(new Date());
            
            if (requiresPersist) dao.persist(site);
            else dao.flush();
            
            responseType.setWasSuccessful(true);
        }
        finally
        {
            dao.closeSession();
        }
                
        return response;
    }

    @Override
    public SiteShutdownResponse siteShutdown(final SiteShutdown siteShutdown0)
    {
        //TODO : fill this with the necessary business logic
        throw new java.lang.UnsupportedOperationException("Please implement " + this.getClass().getName()
                + "#siteShutdown");
    }

    @Override
    public SiteReconnectResponse siteReconnect(final SiteReconnect siteReconnect2)
    {
        //TODO : fill this with the necessary business logic
        throw new java.lang.UnsupportedOperationException("Please implement " + this.getClass().getName()
                + "#siteReconnect");
    }

    @Override
    public RequestResourceResponse requestResource(final RequestResource requestResource4)
    {
        //TODO : fill this with the necessary business logic
        throw new java.lang.UnsupportedOperationException("Please implement " + this.getClass().getName()
                + "#requestResource");
    }

    @Override
    public NotifyCancelResponse notifyCancel(final NotifyCancel notifyCancel6)
    {
        //TODO : fill this with the necessary business logic
        throw new java.lang.UnsupportedOperationException("Please implement " + this.getClass().getName()
                + "#notifyCancel");
    }

    @Override
    public NotifyAcceptResponse notifyAccept(final NotifyAccept notifyAccept8)
    {
        //TODO : fill this with the necessary business logic
        throw new java.lang.UnsupportedOperationException("Please implement " + this.getClass().getName()
                + "#notifyAccept");
    }

    @Override
    public NotifyModifyResponse notifyModify(final NotifyModify notifyModify10)
    {
        //TODO : fill this with the necessary business logic
        throw new java.lang.UnsupportedOperationException("Please implement " + this.getClass().getName()
                + "#notifyModify");
    }

    @Override
    public GetRequestsResponse getRequests(final GetRequests getRequests14)
    {
        //TODO : fill this with the necessary business logic
        throw new java.lang.UnsupportedOperationException("Please implement " + this.getClass().getName()
                + "#getRequests");
    }

    @Override
    public SiteStatusResponse siteStatus(final SiteStatus siteStatus16)
    {
        //TODO : fill this with the necessary business logic
        throw new java.lang.UnsupportedOperationException("Please implement " + this.getClass().getName()
                + "#siteStatus");
    }

    @Override
    public DiscoverResourcesResponse discoverResources(final DiscoverResources discoverResources18)
    {
        //TODO : fill this with the necessary business logic
        throw new java.lang.UnsupportedOperationException("Please implement " + this.getClass().getName()
                + "#discoverResources");
    }

}
