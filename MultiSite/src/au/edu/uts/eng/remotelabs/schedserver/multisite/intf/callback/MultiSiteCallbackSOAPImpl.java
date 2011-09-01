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
 * @date 28th August 2011
 */

package au.edu.uts.eng.remotelabs.schedserver.multisite.intf.callback;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.criterion.Restrictions;

import au.edu.uts.eng.remotelabs.schedserver.bookings.BookingsActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RemotePermissionDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.SessionDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Bookings;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RemotePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.messenger.MessengerService;
import au.edu.uts.eng.remotelabs.schedserver.multisite.MultiSiteActivator;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.callback.types.BookingCancelled;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.callback.types.BookingCancelledResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.callback.types.SessionFinished;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.callback.types.SessionFinishedResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.callback.types.SessionStarted;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.callback.types.SessionStartedResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.callback.types.SessionUpdate;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.callback.types.SessionUpdateResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.callback.types.UserSessionType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.OperationResponseType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.SessionType;

/**
 * Implementation of the MultiSite Callback service.
 */
public class MultiSiteCallbackSOAPImpl implements MultiSiteCallbackSOAP
{
    /** Logger. */
    private Logger logger;
    
    public MultiSiteCallbackSOAPImpl()
    {
        this.logger = LoggerActivator.getLogger();
    }
    
    @Override
    public BookingCancelledResponse bookingCancelled(final BookingCancelled request)
    {
        String site = request.getBookingCancelled().getSiteID();
        String username = request.getBookingCancelled().getUser().getUserID();
        String pId = request.getBookingCancelled().getBooking().getPermission().getPermissionID();
        Date start = request.getBookingCancelled().getBooking().getStart().getTime();
        Date end = request.getBookingCancelled().getBooking().getEnd().getTime();
        
        this.logger.debug("Received " + this.getClass().getSimpleName() + "#bookingCancelled with params site=" + 
                site + ", user=" + username + ", permission=" + pId + ", start=" + start + ", end=" + end + '.');
        
        BookingCancelledResponse response = new BookingCancelledResponse();
        OperationResponseType respType = new OperationResponseType();
        respType.setWasSuccessful(false);
        response.setBookingCancelledResponse(respType);
        
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        try
        {
            User user = this.getUser(username, db);
            if (user == null)
            {
                this.logger.info("Unable to cancel remote site booking because the user '" + username + 
                        "' was not found.");
                respType.setReason("User not found.");
                return response;
            }
            
            RemotePermission perm = new RemotePermissionDao(db).findByGuid(pId);
            if (perm == null)
            {
                this.logger.info("Unable to cancel remote site booking because the remote permission '" + 
                        pId + "' was not found.");
                respType.setReason("Permission not found.");
                return response;
            }
            
            /* Sanity check to ensure the correct site is making the request. */
            if (!perm.getSite().getGuid().equals(site))
            {
                this.logger.warn("Unable to cancel remote site booking because the incorrect site is requesting the " +
                		" cancellation. Requesting site '" + site + "', stored site '" + perm.getSite().getGuid() + "'.");
                respType.setReason("Stored site does not match remote site.");
                return response;
            }
            
            Bookings booking = (Bookings) db.createCriteria(Bookings.class)
                    .add(Restrictions.eq("active", Boolean.TRUE))
                    .add(Restrictions.eq("user", user))
                    .add(Restrictions.ge("startTime", start))
                    .add(Restrictions.le("endTime", end))
                    .add(Restrictions.eq("resourcePermission", perm.getPermission()))
                    .uniqueResult();
            if (booking == null)
            {
                this.logger.info("Unable to cancel remote site booking for user '" + username + "' because the " +
                		"booking was not found.");
                respType.setReason("Booking not found.");
                return response;
            }
            
            this.logger.info("Cancelling provider site booking (id=" + booking.getId() + ") for user '" + 
                    user.qName() + "' because of provider request.");
            
            booking.setActive(false);
            booking.setCancelReason("Provider site request");
            db.beginTransaction();
            db.flush();
            db.getTransaction().commit();
            
            /* Notify the user of cancellation. */
           MessengerService service = BookingsActivator.getMessengerService();
           if (service == null)
           {
               this.logger.warn("Unable send booking cancellation email to user '" + user.qName() +
                       "' because the messenger service wasn't loaded.");
           }
           else
           {
               Map<String, String> macros = new HashMap<String, String>();
               macros.put("firstname", user.getFirstName());
               macros.put("lastname", user.getLastName());
               macros.put("resource", booking.getResourcePermission().getDisplayName());
               macros.put("starttime", start.toString());
               macros.put("cancelreason", "Provider cancelled booking.");

               service.sendTemplatedMessage(user, "BOOKING_CANCELLATION", macros);
           }
        }
        finally
        {
            db.close();
        }
        
        return response;
    }

    @Override
    public SessionStartedResponse sessionStarted(final SessionStarted sessionStarted)
    {
        UserSessionType request = sessionStarted.getSessionStarted();
        String pId = request.getPermission().getPermissionID();
        SessionType sesInfo = request.getSession();
        this.logger.debug("Received " + this.getClass().getSimpleName() + "#sessionStarted with params: site=" + 
                request.getSiteID() + ", user=" + request.getUserID() + ", permission=" + pId);
        
        SessionStartedResponse response = new SessionStartedResponse();
        OperationResponseType respType = new OperationResponseType();
        respType.setWasSuccessful(false);
        response.setSessionStartedResponse(respType);
        
        SessionDao dao = new SessionDao();
        try
        {
            User user = this.getUser(request.getUserID(), dao.getSession());
            if (user == null)
            {
                this.logger.info("Unable to start session for user '" + request.getUserID() + "' because the user was " +
                		"not found.");
                respType.setReason("User not found");
                return response;
            }
            
            RemotePermission remotePerm = new RemotePermissionDao(dao.getSession()).findByGuid(pId);
            if (remotePerm == null)
            {
                this.logger.info("Unable to start session for user '" + user.qName() + "' because the permission " +
                		"was not found.");
                respType.setReason("Permission not found");
                return response;
            }
            
            /* Sanity check - may sure the requesting site is correct. */
            if (!remotePerm.getSite().getGuid().equals(request.getSiteID()))
            {
                this.logger.info("Unable to start session for user '" + user.qName() + "' because the requesting " +
                        "site does not match the stored permission site.");
                respType.setReason("Site does not match");
                return response;
            }
            
            Session userSes;
            if ((userSes = dao.findActiveSession(user)) != null)
            {
                if (userSes.getAssignmentTime() != null)
                {
                    this.logger.info("Unable to start session for user '" + user.qName() + "' because they are "
                            + "already in session.");
                    respType.setReason("User already in session");
                }
                else
                {
                    /* May be remote queuing assignment. */
                    
                }
            }
        }
        finally
        {
           dao.closeSession();
        }
        
        return response;
    }

    @Override
    public SessionFinishedResponse sessionFinished(final SessionFinished sessionFinished4)
    {
        //TODO : fill this with the necessary business logic
        throw new java.lang.UnsupportedOperationException("Please implement " + this.getClass().getName()
                + "#sessionFinished");
    }

    @Override
    public SessionUpdateResponse sessionUpdate(final SessionUpdate sessionUpdate6)
    {
        //TODO : fill this with the necessary business logic
        throw new java.lang.UnsupportedOperationException("Please implement " + this.getClass().getName()
                + "#sessionUpdate");
    }

    /**
     * Returns the user with the specified name. The user must belong to this 
     * site.
     * 
     * @param username name of user
     * @return user or null if not found
     */
    private User getUser(String username, org.hibernate.Session db)
    {
        String ns = MultiSiteActivator.getConfigValue("Site_Namespace", null);
        if (ns == null)
        {
            this.logger.error("Unable to find local user because the local site namespace is not configured.");
            return null;
        }
        
        return new UserDao(db).findByName(ns, username);
    }
}
