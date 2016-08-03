/*
 * VAS / RLN project.
 *
 * @author Michael Diponio <michael.diponio@uts.edu.au>
 * @date 3rd August 2016
 */

package io.rln.node.ss.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.RigEventListener.RigStateChangeEvent;
import io.rln.node.ss.NodeProviderActivator;

/**
 * Node registration API.
 */
public class NodeRegistrationApi extends ApiBase 
{
    public static final String PATH = "/node";
    
    private static final long serialVersionUID = -6345876694064643195L;
    
    public NodeRegistrationApi(List<String> hosts)
    {
        super(hosts);
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response)
    {
        /* Put requests are a node has been registered. */
        String name = request.getParameter("name");
        if (name == null)
        {
            this.logger.debug("Not accepting node registration request name parameter not specified.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        
        RigDao dao = new RigDao();
        Rig rig = dao.findByName(name);
        
        if (rig == null)
        {
            this.logger.info("Cannot register node, node with ID " + name + " not found.");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        if (rig.getContext() != RigType.Context.VAS)
        {
            this.logger.info("Cannot broadcast request for " + name + " as it is not a VAS node.");
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            return;
        }
        
        /* Broadcast the node has been registered. */
        this.logger.info("Registered node " + rig.getName() + ".");
        NodeProviderActivator.notifyRigEvent(RigStateChangeEvent.REGISTERED, rig, dao.getSession());
        response.setStatus(HttpServletResponse.SC_OK);
        dao.closeSession();
        
        response.setStatus(HttpServletResponse.SC_OK);
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    {
        /* Post requests are status updates. */
        String name = request.getParameter("name");
        String status = request.getParameter("online");
        if (name == null || status == null)
        {
            this.logger.debug("Not accepting node registration parameters not specified.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        
        RigDao dao = new RigDao();
        Rig rig = dao.findByName(name);
        
        if (rig == null)
        {
            this.logger.warn("Cannot update node, node " + name + " not found.");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        if (rig.getContext() != RigType.Context.VAS)
        {
            this.logger.warn("Cannot broadcast request for " + name + " as it is not a VAS node.");
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            return;
        }
        
        /* Broadcast the status update. */
        this.logger.info("Changed status of node " + rig.getName() + " to " + (rig.isOnline() ? "online." : "offline."));
        NodeProviderActivator.notifyRigEvent(
                "true".equals(status) ? RigStateChangeEvent.ONLINE : RigStateChangeEvent.OFFLINE, rig, dao.getSession());
        
        dao.closeSession();
        
        response.setStatus(HttpServletResponse.SC_OK);
    }
    
    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response)
    {
        /* Delete requests are a node has been removed. */
        String name = request.getParameter("name");
        if (name == null)
        {
            this.logger.debug("Not accepting node removal, request name parameter not specified.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        
        RigDao dao = new RigDao();
        Rig node = dao.findByName(name);
        
        if (node == null)
        {
            this.logger.warn("Cannot remove node, node " + name + " not found.");
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        if (node.getContext() != RigType.Context.VAS)
        {
            this.logger.warn("Cannot broadcast request for " + name + " as it is not a VAS node.");
            response.setStatus(HttpServletResponse.SC_CONFLICT);
            return;
        }
        
        /* Broadcast the node has been removed. */
        this.logger.info("Removed node " + node.getName() + ".");
        NodeProviderActivator.notifyRigEvent(RigStateChangeEvent.REMOVED, node, dao.getSession());
        dao.closeSession();
        
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
