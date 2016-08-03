/*
 * VAS / RLN project.
 *
 * @author Michael Diponio <michael.diponio@uts.edu.au>
 * @date  30th July 2016
 */

package io.rln.node.ss.client;

import java.util.Date;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigLogDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.RigEventListener.RigStateChangeEvent;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import io.rln.node.ss.NodeProviderActivator;

public class NodeReleaser implements Runnable
{
    /** The timeout in seconds before an asynchronous SOAP operation fails. */
    public static final int RIG_CLIENT_ASYNC_TIMEOUT = 120;

    /** Rig that is being freed. */
    private Rig rig;
    
    /** The client to contact the node. */
    private final NodeClient client;
    
    /** Request to send to the node. */
    private final ReleaseRequest request;
    
    /** Logger. */
    private final Logger logger;
    
    public NodeReleaser(Session session)
    {
        this.logger = LoggerActivator.getLogger();
        
        this.rig = session.getRig();
        this.client = new NodeClient(this.rig);
        this.request = new ReleaseRequest(session);
    }
    
    /**
     * Releases a user from a node.
     * 
     * @param ses session information
     * @param db database session
     * @return releaser instance
     */
    public static NodeReleaser release(Session ses, org.hibernate.Session db)
    {
        return new NodeReleaser(ses);
    }
    
    @Override
    public void run()
    {
        OperationResponse response = this.client.request(NodeClient.RELEASE, this.request);
        
        RigDao dao = new RigDao();
        this.rig = dao.merge(this.rig);

        if (response.ok())
        {
            this.logger.debug("Received release callback, releasing " + this.rig.getName() + " was successful.");
            
            this.rig.setLastUpdateTimestamp(new Date());
            this.rig.setInSession(false);
            this.rig.setSession(null);
            dao.flush();

            /* Fire event the rig is free. */
            NodeProviderActivator.notifyRigEvent(RigStateChangeEvent.FREE, this.rig, dao.getSession());
        }
        else
        {
            /* Failed release so take rig offline. */
            this.rig.setOnline(false);
            this.rig.setOfflineReason("Release failed with reason: " + response.error());
            this.rig.setInSession(false);
            this.rig.setSession(null);
            dao.flush();
            
            /* Log when the rig is offline. */
            RigLogDao rigLogDao = new RigLogDao(dao.getSession());
            rigLogDao.addOfflineLog(this.rig, "Release failed with reason: " + response.error());
            
            /* Fire event the rig is offline. */
            NodeProviderActivator.notifyRigEvent(RigStateChangeEvent.OFFLINE, this.rig, dao.getSession());
        }
        
        dao.closeSession();
    }
    
    /**
     * Release request. 
     */
    public static class ReleaseRequest
    {
        /** Name of user to allocate. */
        private String name;
        
        public ReleaseRequest(Session ses)
        {
            User user = ses.getUser();
            this.name = user.getName();
        }

        public String getName()
        {
            return this.name;
        }
    }
}
