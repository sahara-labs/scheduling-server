
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
 * MultiSite federation service implementation.
 */
public class MultiSiteFederationSOAPImpl implements MultiSiteFederationSOAP
{


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
    public InitiateSiteResponse initiateSite(final InitiateSite initiateSite12)
    {
        //TODO : fill this with the necessary business logic
        throw new java.lang.UnsupportedOperationException("Please implement " + this.getClass().getName()
                + "#initiateSite");
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
