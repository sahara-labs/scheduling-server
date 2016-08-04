package io.rln.node.ss.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import au.edu.uts.eng.remotelabs.schedserver.bookings.pojo.BookingEngineService;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.ResourcePermissionDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.queuer.pojo.QueuerService;
import au.edu.uts.eng.remotelabs.schedserver.queuer.pojo.types.QueueSession;
import io.rln.node.ss.NodeProviderActivator;

/**
 * Queue access API.
 */
public class AccessApi extends ApiBase
{
    private static final long serialVersionUID = -3676979340749696907L;
    
    public static final String PATH = "/access";

    public AccessApi(List<String> hosts)
    {
        super(hosts);
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response)
    {
        /* Put request is assigning access. */
        String name = request.getParameter("user");
        String rId= request.getParameter("permission");
        if (name == null || rId == null)
        {
            this.logger.info("Rejecting access request, not all parameter specified.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        UserDao dao = null;
        try
        {
            dao = new UserDao();

            User user = dao.findByName(name);
            if (user == null)
            {
                this.logger.info("Rejecting access request, node " + name + " not found");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
           
            ResourcePermission perm = new ResourcePermissionDao(dao.getSession()).get(Long.parseLong(rId));
            if (perm == null)
            {
                this.logger.info("Rejecting access request, permission with ID " + rId + " not found.");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            
            if (!ResourcePermission.RIG_PERMISSION.equals(perm.getType()) || perm.getRig() == null)
            {
                this.logger.info("Rejecting access request, request access permission is not a rig permission, "
                        + "other permission types are not supported.");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            
            Rig node = perm.getRig();
            if (!(node.isActive() && node.isOnline()) || node.isInSession())
            {
                this.logger.debug("Rejecting access request, node " + node.getName() + " is not free, rejecting access request.");
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                return;
            }
            
            QueuerService queuer = NodeProviderActivator.getQueuer();
            BookingEngineService bookingEngine = NodeProviderActivator.getBookingEngine();
            if (queuer == null || bookingEngine == null)
            {
                this.logger.error("Queuer or booking engine services not found, cannot access required functions.");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
            
            if (!bookingEngine.isFreeFor(node, perm.getSessionDuration(), dao.getSession()))
            {
                this.logger.debug("Cannot assign access to " + perm.getRig().getName() + " as it is currently reserved.");
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                return;
            }
            
            QueueSession queue = queuer.addToQueue(user, perm, null, dao.getSession());
            if (queue.getSession() != null)
            {
                this.logger.debug("Successfully assigned session for " + user.getName() + " to directly access " + 
                        perm.getRig().getName() + ".");
                
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_OK);
                
                PrintWriter writer = response.getWriter();
                writer.println(this.getJson(new SessionResponse(queue.getSession())));
            }
            else
            {
                this.logger.warn("Failed assignin queue session for " + user.getName() + " to use node " + node.getName() + 
                        ". Error: " + queue.getErrorMessage());
                response.setStatus(520);
            }
        }
        catch (IOException e)
        {
            this.logger.error("Failed access request, exception " + e.getClass().getSimpleName() + ": " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        finally 
        {
            if (dao != null) dao.closeSession();
        }
    }
    
    public static class SessionResponse
    {
        private long session;
        
        public SessionResponse(Session ses)
        {
            this.session = ses.getId();
        }
        
        public long getSession()
        {
            return this.session;
        }
    }
    
    public void doDelete(HttpServletRequest request, HttpServletResponse response)
    {
        /* Delete request is removing access. */
    }
}
