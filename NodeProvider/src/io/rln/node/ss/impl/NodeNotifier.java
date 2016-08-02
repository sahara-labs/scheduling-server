/*
 * VAS / RLN project.
 *
 * @author Michael Diponio <michael.diponio@uts.edu.au>
 * @date 2nd July 2016
 */

package io.rln.node.ss.impl;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigLogDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.RigEventListener.RigStateChangeEvent;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import io.rln.node.ss.NodeProviderActivator;

/**
 * Calls notification operation on a node.
 */
public class NodeNotifier implements Runnable
{
    /** Node to notify. */
    private Rig node;
    
    /** Client to contact the node. */
    private final NodeClient client;
    
    /** Request to send to the node. */
    private final NotifyRequest request;
    
    /** Logger. */
    private Logger logger;
    
    public NodeNotifier(String message, Session ses) 
    {
        this.node = ses.getRig();
        this.client = new NodeClient(this.node);
        this.request = new NotifyRequest(message);
        
        this.logger = LoggerActivator.getLogger();
    }
    
    /**
     * Notifies a node of a message
     * 
     * @param message message to send
     * @param ses session to notify from
     * @param db database session
     * @return built notifier 
     */
    public static NodeNotifier notify(String message, Session ses, org.hibernate.Session db)
    {
        return new NodeNotifier(message, ses);
    }
    
    @Override
    public void run()
    {
        OperationResponse response = this.client.request(NodeClient.NOTIFY, this.request);
        
        RigDao dao = new RigDao();
        this.node = dao.merge(this.node);
        
        if (!response.ok())
        {
            this.logger.error("Failed notifying node " + this.node.getName() + " at " + 
                    this.node.getContactUrl() + " because of error code: " + response.getCode() + 
                    ", message: " + response.getMessage() + ".");

            /* Put the rig offline. */
            this.node.setInSession(false);
            this.node.setOnline(false);
            this.node.setOfflineReason("Notify to send notification message");            
            dao.flush();

            
            /* Log when the rig is offline. */
            RigLogDao rigLogDao = new RigLogDao(dao.getSession());
            rigLogDao.addOfflineLog(this.node, "Notify failed with code: " + response.getCode() + ", error: " +
                    response.getMessage());
            
            
            /* Fire event the rig is offline. */
            NodeProviderActivator.notifyRigEvent(RigStateChangeEvent.OFFLINE, this.node, dao.getSession());
        }
    }
    
    /**
     * Notification request. 
     */
    public static class NotifyRequest
    {
        /** Message to send. */
        private String message;

        public NotifyRequest(String message)
        {
            this.message = message;
        }

        public String getMessage()
        {
            return this.message;
        }
    }
}
