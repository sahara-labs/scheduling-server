package io.rln.node.ss.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;

import au.edu.uts.eng.remotelabs.schedserver.bookings.pojo.BookingEngineService;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.ResourcePermissionDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.SessionDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.queuer.pojo.QueuerService;
import au.edu.uts.eng.remotelabs.schedserver.queuer.pojo.types.QueueSession;
import au.edu.uts.eng.remotelabs.schedserver.session.pojo.SessionService;
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

    /**
     * The PUT method assigns access to a node for a user.
     */
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
    
    /**
     * Session allocation response type. 
     */
    public static class SessionResponse
    {
        private long session;
        
        public SessionResponse(Session ses)
        {
            this.session = ses.getId();
        }
        
        public long getSession() { return this.session; }
    }
    
    /**
     * The GET method returns the session time limit allowable within the bounds of permissions and
     * scheduling. 
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    {
        String sesId = request.getParameter("session");
        if (sesId == null)
        {
            this.logger.debug("Bad session time limit, required parameters not specified.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        SessionDao dao = null;
        try
        {
            dao = new SessionDao();
            
            Session ses = dao.get(Long.parseLong(sesId));
            if (ses == null)
            {
                this.logger.debug("Cannot find session time limit because session with id " + sesId + " was not found.");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            
            if (!ses.isActive() || ses.getRig() == null)
            {
                this.logger.debug("Cannot find session time limit because session with id " + sesId + " is not current running.");
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                return;
            }
            
            /* The remaining permission allowed time will give the maximum time that 
             * the session could be extended. */
            SessionTimingResponse timing = new SessionTimingResponse(ses);
            timing.remaining = this.getRemaining(ses);
            timing.elapsed = this.getElapsed(ses);
            
            if (!ses.isDisableExtensions() && ses.getExtensions() > 0)
            {
                ResourcePermission perm = ses.getResourcePermission();
                
                /* The maximum allowed permission time. */
                int duration = perm.getSessionDuration() + perm.getAllowedExtensions() * perm.getExtensionDuration();
                
                /* Minus the allowed extension period gives the possible extension period. */
                duration -= timing.elapsed;
                
                if (perm.getMaximumBookingDuration() > 0 && duration > perm.getMaximumBookingDuration())
                {
                    /* If a maximum booking duration has been set and it is less than the maximum booking duration,
                     * we will restrict the extension to that duration. */
                    this.logger.debug("Possible extension duration " + duration + " seconds for session " + ses.getId() + " is " +
                            "longer than the maximum booking duration, limiting duration to " + perm.getMaximumBookingDuration() + 
                            "seconds.");
                    duration = perm.getMaximumBookingDuration();
                }
                
                BookingEngineService bookings = NodeProviderActivator.getBookingEngine();
                timing.extendable = bookings.getPossibleExtension(ses.getRig(), ses, duration, dao.getSession());
            }
            else
            {
                timing.extendable = 0;
            }
            
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            
            PrintWriter writer = response.getWriter();
            writer.println(this.getJson(timing));
        }
        catch (JsonProcessingException e)
        {
            this.logger.error("Failed generating session timing JSON response message, error: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        catch (IOException e)
        {
            this.logger.warn("Failed to return session time limit for session " + sesId + ", exception: " + e.getClass().getSimpleName() + 
                    ", error: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        finally
        {
            if (dao != null) dao.closeSession();
        }
    }
    
    /**
     * Session timing response type. 
     */
    public static class SessionTimingResponse extends SessionResponse
    {
        /** Session elapsed time in seconds. */
        private int elapsed;
        
        /** Session remaining time in seconds. */
        private int remaining;
        
        /** Session possible time extensions. */
        private int extendable;
        
        public SessionTimingResponse(Session ses) 
        {
            super(ses);
        }
        
        public int getElapsed()    { return this.elapsed; }
        public int getRemaining()  { return this.remaining; }
        public int getExtendable() { return this.extendable; }
    }
    
    /**
     * The POST method sets the remaining duration for an existing session. 
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
    {
        String sesId = request.getParameter("session");
        String extStr = request.getParameter("extension");
        if (sesId == null || extStr == null)
        {
            this.logger.debug("Bad session extend request, required parameters not specified.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        int extension = Integer.parseInt(extStr);
        
        SessionDao dao = null;
        try
        {
            dao = new SessionDao();
            org.hibernate.Session db = dao.getSession();
            
            Session ses = dao.get(Long.parseLong(sesId));
            if (ses == null)
            {
                this.logger.warn("Cannot extend session because session with id " + sesId + " was not found.");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            
            if (!ses.isActive() || ses.getRig() == null)
            {
                this.logger.warn("Cannot extend session because sesssion with id " + ses.getId() + " has not been " +
                        "allocated to a node.");
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            
            if (ses.isDisableExtensions())
            {
                this.logger.warn("Cannot extend session because session with id " + ses.getId() + " has " + 
                        "extensions disabled.");
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                return;
            }
            
            ResourcePermission perm = ses.getResourcePermission();
            int remaining = this.getRemaining(ses);            
            if (extension > remaining)
            {                
                if (perm.getSessionDuration() + perm.getAllowedExtensions() * perm.getExtensionDuration() 
                        - this.getElapsed(ses) < extension)
                {
                    /* Make sure the session elapsed duration plus extension is within the permission allowed
                     * maximum duration with all extensions applied. */
                    this.logger.warn("Cannot extend session " + ses.getId() + " because an extension of " + extension + 
                            "seconds will put the total session time outside permission allowed time.");
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    return;
                }
                
                if (perm.getMaximumBookingDuration() > 0 && extension > perm.getMaximumBookingDuration())
                {
                    this.logger.warn("Cannot extend session " + ses.getId() + " because the extension is longed the maximum " +
                            "allowed booking duration.");
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
                    return;
                }
                
                BookingEngineService bookings = NodeProviderActivator.getBookingEngine();                
                if (bookings.extendQueuedSession(ses.getRig(), ses, extension - remaining, db))
                {
                    this.logger.info("Extension time (" + extension + ") seconds for session " + ses.getId() + " has been " +
                            "granted by the booking system.");
                }
                else
                {
                    this.logger.info("Extension time (" + extension + ") seconds for session " + ses.getId() + " was not granted by "
                            + "the booking system.");
                    
                    response.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
                    return;
                }
            }
            else
            {
                this.logger.info("Extension time (" + extension + ") seconds for session " + ses.getId() + " is granted as " + 
                        "it already within the allocated session time.");
            }
            
            /* Are setting extension, the session will be locked to this period with no further extensions. */
            db.beginTransaction();
            ses.setDisableExtensions(true);
            ses.setDuration(remaining + extension);
            ses.setExtensions(perm.getAllowedExtensions());
            db.flush();
            db.getTransaction().commit();
            
            SessionTimingResponse timing = new SessionTimingResponse(ses);
            timing.elapsed = this.getElapsed(ses);
            timing.remaining = ses.getDuration() - timing.elapsed;
            timing.extendable = 0;
            
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            
            PrintWriter writer = response.getWriter();
            writer.println(this.getJson(timing));
        }
        catch (JsonProcessingException e)
        {
            this.logger.error("Failed generating session timing JSON response message, error: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        catch (IOException e)
        {
            this.logger.error("Failed to extension session " + sesId + ", exception: " + e.getClass().getSimpleName() + ", error: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        finally
        {
            if (dao != null) dao.closeSession();
        }
    }

    
    /**
     * Gets the sessions remaining time.
     * 
     * @param ses session
     * @return int remaining time in seconds
     */
    private int getRemaining(Session ses)
    {
        ResourcePermission perm = ses.getResourcePermission();
        return ses.getDuration() + // The session time
                (perm.getAllowedExtensions() - ses.getExtensions()) * perm.getExtensionDuration() -  // Extension time
                this.getElapsed(ses); // In session time
    }
    
    /**
     * Gets the sessions elapsed time.
     * 
     * @param ses session
     * @return elapsed time in seconds
     */
    private int getElapsed(Session ses)
    {
        return (int)(Math.round((System.currentTimeMillis() - ses.getAssignmentTime().getTime()) / 1000));
    }
    
    /**
     * The DELETE method finishes a session.
     */
    public void doDelete(HttpServletRequest request, HttpServletResponse response)
    {
        /* Delete request is removing access. */
        String sesId = request.getParameter("session");
        String reason = request.getParameter("reason");
        if (sesId == null || reason == null)
        {
            this.logger.warn("Bad session delete request, required parameters not specified.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        SessionDao dao = null;
        try
        {
            dao = new SessionDao();
            
            Session ses = dao.get(Long.parseLong(sesId));
            if (ses == null)
            {
                this.logger.warn("Cannot end node session " + sesId + " as the session was not found.");
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            
            if (!ses.isActive())
            {
                this.logger.info("No need to end node session " + ses.getId() + " as it not active.");
                response.setStatus(HttpServletResponse.SC_OK);
            }
            
            SessionService service = NodeProviderActivator.getSession();
            if (service == null)
            {
                this.logger.error("Unable to obtain Session service, cannot obtain session functions.");
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
            
            if (service.finishSession(ses, reason, dao.getSession()))
            {
                this.logger.debug("Successfully ended node session for " + ses.getAssignedRigName() + ".");
                response.setStatus(HttpServletResponse.SC_OK);
            }
            else
            {
                this.logger.info("Failed session " + ses.getId() + " not successfully ended for " + ses.getAssignedRigName());
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            }
        }
        finally
        {
            if (dao != null) dao.closeSession();
        }
    }
}
