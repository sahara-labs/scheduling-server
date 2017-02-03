/*
 * VAS / RLN project.
 *
 * @author Michael Diponio <michael.diponio@uts.edu.au>
 * @date  25th January 2017
 */

package io.rln.node.ss.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import au.edu.uts.eng.remotelabs.schedserver.bookings.pojo.types.BookingOperation;
import au.edu.uts.eng.remotelabs.schedserver.bookings.pojo.types.BookingsPeriod;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.ResourcePermissionDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import io.rln.node.ss.NodeProviderActivator;

/**
 * Rest API to access booking functions. 
 */
public class BookingsApi extends ApiBase
{
    private static final long serialVersionUID = 8477398260270971095L;
    
    /** External path to bookings function. */
    public static final String PATH = "/bookings";

    public BookingsApi(List<String> hosts)
    {
        super(hosts);
    }
    
    /**
     * The GET method list times a rig type is free over a date period.
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
    {
        String permId = req.getParameter("permission");
        Calendar from = this.parseTimestamp(req.getParameter("from"));
        Calendar to = this.parseTimestamp(req.getParameter("to"));
        if (permId == null || from == null || to == null)
        {
            this.logger.info("Not accepting machine free times request, parameters not specified.");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        ResourcePermissionDao dao = null;
        try
        {
            dao = new ResourcePermissionDao();
            ResourcePermission perm = dao.get(Long.parseLong(permId));
            if (perm == null)
            {
                this.logger.info("Failed booking free times request, resource permission with identifier " + permId + " was not found");
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            
            BookingsPeriod period = NodeProviderActivator.getBookingService().getFreeBookings(from, to, perm, dao.getSession());
            
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
            
            PrintWriter writer = resp.getWriter();
            writer.println(this.getJson(period));
        }
        catch (IOException e)
        {
            this.logger.error("Failed generating free periods JSON response timing " + e.getClass() + ": " + e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        finally
        {
            if (dao != null) dao.closeSession();
        }
    }
    
    /**
     * THe PUT method is to create a booking.
     */
    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse resp)
    {
        String permId = req.getParameter("permission");
        String userId = req.getParameter("user");
        Calendar start = this.parseTimestamp(req.getParameter("start"));
        Calendar finish = this.parseTimestamp(req.getParameter("finish"));
        if (permId == null || userId == null || start == null || finish == null)
        {
            this.logger.info("Not accepting create booking request, parameters not specified.");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        ResourcePermissionDao dao = null;
        try
        {
            dao = new ResourcePermissionDao();
            
            ResourcePermission perm = dao.get(Long.parseLong(permId));
            if (perm == null)
            {
                this.logger.info("Failed create booking request, resource permission " + permId + " not found.");
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            
            User user = new UserDao(dao.getSession()).get(Long.parseLong(userId));
            if (user == null)
            {
                this.logger.info("Failed create booking request, user " + userId + " not found.");
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            
            BookingOperation result = NodeProviderActivator.getBookingService().createBooking(start, finish, user, perm, dao.getSession());
            
            resp.setContentType("application/json");
            resp.setStatus(HttpServletResponse.SC_OK);
            
            Map<String, Object> json = new HashMap<>();
            json.put("success", result.successful());
            if (result.successful())
            {
                json.put("booking", result.getBooking().getId());
            }
            else if (result.getBestFits().size() > 0)
            {
                json.put("bestFits", result.getBestFits());
            }
            else
            {
                json.put("failure", result.getFailureReason());
            }
            
            PrintWriter writer = resp.getWriter();
            writer.println(this.getJson(json));
        }
        catch (IOException e)
        {
            this.logger.error("Failed create booking JSON response, " + e.getClass() + ": " + e.getMessage());
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        finally 
        {
            if (dao != null) dao.closeSession();
        }
    }
}
