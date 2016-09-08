/*
 * VAS / RLN project.
 *
 * @author Michael Diponio <michael.diponio@uts.edu.au>
 * @date  8th September 2016
 */

package io.rln.node.ss.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigLogDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.SessionDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.RigEventListener.RigStateChangeEvent;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.SessionEventListener.SessionEvent;
import io.rln.node.ss.NodeProviderActivator;

/**
 * Session API. 
 */
public class SessionApi extends ApiBase
{
    public static final String PATH = "/session";
    
    private static final long serialVersionUID = -8723760108570931573L;
    
    public SessionApi(List<String> hosts)
    {
        super(hosts);
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response)
    {
        /* Put request is node session allocation has completed. */
        String id = request.getParameter("session");
        String name = request.getParameter("node");
        if (id == null || name == null)
        {
            this.logger.info("Not accepting allocation notification, parameters are not specified.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        SessionDao dao = null;
        try
        {
            dao = new SessionDao();
            
            Session session = dao.get(Long.parseLong(id));
            if (session == null)
            {
                this.logger.warn("Cannot process allocation result because session with id " + id + " was not found.");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            
            Rig node = new RigDao(dao.getSession()).findByName(name);
            if (node == null)
            {
                this.logger.warn("Cannot process allocation result because node " + name + " was not found.");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            
            if (session.isActive())
            {
                /* Broadcast the session is ready. */
                this.logger.info("Session " + id + " has successfully finished allocation.");
                NodeProviderActivator.notifySessionEvent(SessionEvent.READY, session, dao.getSession());
                response.setStatus(HttpServletResponse.SC_OK);
            }
            else
            {
                this.logger.info("Session " + id + " has failed allocation.");
                NodeProviderActivator.notifySessionEvent(SessionEvent.FINISHED, session, dao.getSession());
                
                /* Log when the rig is offline. */
                RigLogDao rigLogDao = new RigLogDao(dao.getSession());
                rigLogDao.addOfflineLog(node, "Allocation failed");

                /* Fire event the rig is offline. */
                NodeProviderActivator.notifyRigEvent(RigStateChangeEvent.OFFLINE, node, dao.getSession());
            }
        }
        finally
        {
            if (dao != null) dao.closeSession();
        }
    }

    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response)
    {
        /* Delete request is node session release has completed. */
        String name = request.getParameter("node");
        if (name == null)
        {
            this.logger.debug("Not accepting release notification, parameters are not specified.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        RigDao dao = null;
        try
        {
            dao = new RigDao();
            Rig node = dao.findByName(name);
            if (node == null)
            {
                this.logger.warn("Cannot notify node release complete because node with name " + 
                        name + " was not found.");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            
            if (node.isOnline())
            {
                this.logger.debug("Release complete of node " + node.getName() + ", making avaliable for " +
                        "further sessions.");
                NodeProviderActivator.notifyRigEvent(RigStateChangeEvent.FREE, node, dao.getSession());
            }
            else
            {
                this.logger.warn("Release of node " + node.getName() + " has failed, not avaliable for " +
                        "further sessions..");
            }
        }
        finally
        {
            if (dao != null) dao.closeSession();
        }
    }
}
