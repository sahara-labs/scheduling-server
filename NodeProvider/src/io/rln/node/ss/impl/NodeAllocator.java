/*
 * VAS / RLN project.
 *
 * @author Michael Diponio <michael.diponio@uts.edu.au>
 * @date  30th July 2016
 */

package io.rln.node.ss.impl;

import java.util.Date;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigLogDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.SessionDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.RigEventListener.RigStateChangeEvent;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.SessionEventListener.SessionEvent;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import io.rln.node.ss.NodeProviderActivator;

/**
 * Allocates a user to a node and handles any failures. 
 */
public class NodeAllocator implements Runnable
{
    /** The timeout in seconds before an asynchronous SOAP operation fails. */
    public static final int RIG_CLIENT_ASYNC_TIMEOUT = 120;

    /** Session that is being allocated. */
    private Session session;
    
    /** Client to contact the node. */
    private final NodeClient client;
    
    /** Request to send to the node. */
    private final AllocateRequest request;
    
    /** Logger. */
    private final Logger logger;
    
    private NodeAllocator(Session ses)
    {
        this.logger = LoggerActivator.getLogger();
        
        this.session = ses;
        this.client = new NodeClient(ses.getRig());
        this.request = new AllocateRequest(ses);
    }
    
    /**
     * Allocates a session to the node. 
     * 
     * @param ses session information
     * @param db database session
     * @return this 
     */
    public static NodeAllocator allocate(Session ses, org.hibernate.Session db)
    {
        NodeAllocator allocator = new NodeAllocator(ses);
        return allocator;
    }
    
    @Override
    public void run()
    {
        OperationResponse response = this.client.request(NodeClient.ALLOCATE, this.request);

        SessionDao dao = new SessionDao();
        this.session = dao.merge(this.session);
        
        if (response.ok())
        {
            this.logger.debug("Received allocate response for " + this.session.getUserNamespace() + ':' + 
                    this.session.getUserName() + ", allocation successful.");
            
            /* The session is being set to ready, so it may be used. */
            this.session.setReady(true);
            dao.flush();
            
            NodeProviderActivator.notifySessionEvent(SessionEvent.READY, this.session, dao.getSession());
        }
        else
        {
            this.logger.error("Received allocate response for " + this.session.getUserNamespace() + ':' + 
                    this.session.getUserName() + ", allocation not successful. Error response code is " +
                    response.getCode() + ": " + response.getMessage());
            
            /* Allocation failed so end the session and take the rig off line depending on error. */
            this.session.setActive(false);
            this.session.setReady(false);
            this.session.setRemovalReason("Allocation failure with reason: " + response.getMessage());
            this.session.setRemovalTime(new Date());
            dao.flush();
            NodeProviderActivator.notifySessionEvent(SessionEvent.FINISHED, this.session, dao.getSession());
            
            Rig rig = this.session.getRig();
            rig.setInSession(false);
            rig.setOnline(false);
            rig.setOfflineReason("Allocation failed with reason: '" + response.error() + "'.");
            rig.setSession(null);
            dao.flush();

            /* Log when the rig is offline. */
            RigLogDao rigLogDao = new RigLogDao(dao.getSession());
            rigLogDao.addOfflineLog(rig, "Allocation failed with reason: " + response.error());

            /* Fire event the rig is offline. */
            NodeProviderActivator.notifyRigEvent(RigStateChangeEvent.OFFLINE, rig, dao.getSession());
        }

        dao.closeSession();
    }
    
    /**
     * Allocation request. 
     */
    public static class AllocateRequest
    {
        /** Name of user to allocate. */
        private String name;
        
        /** Full name of user. */
        private String fullName;
        
        /** Card identifier. */
        private String cid;
        
        public AllocateRequest(Session ses)
        {
            User user = ses.getUser();
            this.name = user.getName();
            this.fullName = !(user.getFirstName() == null || user.getLastName() == null) ?
                    user.getFirstName() + ' ' + user.getLastName() : "";
            this.cid = user.getCard();
        }

        public String getName()
        {
            return this.name;
        }

        public String getFullName()
        {
            return this.fullName;
        }

        public String getCid()
        {
            return this.cid;
        }
    }
}
