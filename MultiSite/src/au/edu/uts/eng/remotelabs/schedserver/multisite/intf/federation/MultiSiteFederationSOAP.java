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

import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.DiscoverResources;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.DiscoverResourcesResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.GetRequests;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.GetRequestsResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.InitiateSite;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.InitiateSiteResponse;
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

/**
 * MultiSite federation service interface.
 */
public interface MultiSiteFederationSOAP
{
    /**
     * Initiates a peer connection between two sites such that they can 
     * subsequently share resources.
     * 
     * @param initiateSite request
     * @return response
     */
    public InitiateSiteResponse initiateSite(InitiateSite initiateSite);

    public SiteShutdownResponse siteShutdown(SiteShutdown siteShutdown);

    public SiteReconnectResponse siteReconnect(SiteReconnect siteReconnect);

    public RequestResourceResponse requestResource(RequestResource requestResource);

    public NotifyCancelResponse notifyCancel(NotifyCancel notifyCancel);

    public NotifyAcceptResponse notifyAccept(NotifyAccept notifyAccept);

    public NotifyModifyResponse notifyModify(NotifyModify notifyModify);

    

    

    public GetRequestsResponse getRequests(GetRequests getRequests);

    public SiteStatusResponse siteStatus(SiteStatus siteStatus);

  
    public DiscoverResourcesResponse discoverResources(DiscoverResources discoverResources);
}
