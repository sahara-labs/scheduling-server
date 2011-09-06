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
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.BookingsDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RemotePermissionDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RemoteSiteDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigTypeDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.SessionDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Bookings;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RemotePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RemoteSite;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
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
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.ResourceType;
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
           
           respType.setWasSuccessful(true);
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
        String remoteSite = request.getSiteID();
        String pId = request.getPermission().getPermissionID();
        this.logger.debug("Received " + this.getClass().getSimpleName() + "#sessionStarted with params: site=" + 
                remoteSite + ", user=" + request.getUserID() + ", permission=" + pId);
        
        SessionStartedResponse response = new SessionStartedResponse();
        OperationResponseType respType = new OperationResponseType();
        respType.setWasSuccessful(false);
        response.setSessionStartedResponse(respType);
        
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        try
        {
            User user = this.getUser(request.getUserID(), db);
            if (user == null)
            {
                this.logger.info("Unable to start session for user '" + request.getUserID() + "' because the user was " +
                		"not found.");
                respType.setReason("User not found");
                return response;
            }
            
            RemotePermission remotePerm = new RemotePermissionDao(db).findByGuid(pId);
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
            
            SessionType provSes = request.getSession();
            Session ses;
            Bookings booking = null;
            
            if ((ses = new SessionDao(db).findActiveSession(user)) != null && ses.getAssignmentTime() != null)
            {
                /* User already has a session. */
                this.logger.info("Unable to start session for user '" + user.qName() + "' because they are " +
                            "already in session.");
                respType.setReason("User already in session");
                return response;
            }
            else if (ses != null)
            {
                /* This is a remote queuing assignment if the queued permission
                 * is the same as the assignment permission. */
                if (!remotePerm.getPermission().getId().equals(ses.getResourcePermission().getId()))
                {
                    /* FAILURE CONDITION: The sites do not match. */
                    this.logger.info("Unable to start session for user '" + user.qName() + "' because they are " +
                            "in the queue for a different resource.");
                    respType.setReason("User already in queue");
                    return response;
                }
            }
            else
            {
                /* The starting session maybe because the user has a booking
                 *  scheduled to start now. */
                booking = new BookingsDao(db).getNextBookingWithin(user, 450); // HACK number that is half a slot
                if (booking == null)
                {
                    /* FAILURE CONDITION: This is an uninitiated session. This 
                     * isn't allowed for no good reason. */
                    this.logger.info("Unable to start session for user '" + user.qName() + "' because no session is " +
                    		"expected. The user is currently neither in queue or in session.");
                    respType.setReason("Unexpected session.");
                    return response;
                }
                else if (!booking.getResourcePermission().getId().equals(remotePerm.getPermission().getId()))
                {
                    /* FAILURE CONDITION: The user has a booking, but not for
                     * this is session. */
                    this.logger.info("Unable to start session for user '" + user.qName() + "' because the user has a " +
                    		"booking for a different permission starting soon.");
                    respType.setReason("User is booked for another permission.");
                    return response;
                }
                
                /* User isn't in session, so create them a session. */
                ses = new Session();
                ses.setActive(true);
                
                ses.setRequestTime(new Date());
                ses.setPriority((short)1);
                ses.setResourcePermission(remotePerm.getPermission());
                ses.setRequestedResourceId(-1L);
                
                ResourceType res = provSes.getResource();
                if (res == null)
                {
                    this.logger.error("Provider site did not provide resource information when starting a session.");
                    ses.setResourceType(remotePerm.getPermission().getType());
                    ses.setRequestedResourceName("NoT_PROvIDED_BY_CONSUMER");
                }
                else
                {
                    ses.setResourceType(res.getType());
                    ses.setRequestedResourceName(res.getName());
                }
                
                ses.setUser(user);
                ses.setUserNamespace(user.getNamespace());
                ses.setUserName(user.getName());
            }
            
            /* Remote rig type may not exist either, if not will need to be added. */
            RigType remoteType = new RigTypeDao(db).
                    loadOrCreate(request.getSiteID() + ':' + provSes.getRigType(), false, "provider=" + remoteSite,
                    remotePerm.getSite());
            
            RigDao rigDao = new RigDao(db);
            Rig remoteRig = rigDao.findMetaRig(remoteSite + ':' + provSes.getRigName(), "provider=" + remoteSite);
            if (remoteRig == null)
            {
                /* Provider rig has not previously been used at site so it 
                 * must be created. */
                remoteRig = new Rig();
                /* Rig name format is <Provider>:<Specificed rig name>. */
                remoteRig.setName(remoteSite + ':' + provSes.getRigName());
                /* Meta information about source. */
                remoteRig.setMeta("provider=" + remoteSite);
                remoteRig.setActive(false);  // The rig cannot take local sessions so cannot be online
                remoteRig.setOnline(false);  
                remoteRig.setManaged(false); // Is not managed because normal watch dogging does not apply
                remoteRig.setContactUrl(provSes.getContactURL());
                remoteRig.setLastUpdateTimestamp(new Date());
                remoteRig.setRigType(remoteType);
                remoteRig.setSite(remotePerm.getSite());
                
                rigDao.persist(remoteRig);
            }
            else
            {
                /* Rig details may have changed since last used session. */
                boolean requiresFlush = false;
                if (remoteRig.getContactUrl() == null || !remoteRig.getContactUrl().equals(provSes.getContactURL()))
                {
                    remoteRig.setContactUrl(provSes.getContactURL());
                    remoteRig.setLastUpdateTimestamp(new Date());
                    requiresFlush = true;
                }
                
                if (!remoteRig.getRigType().getId().equals(remoteType.getId()))
                {
                    remoteRig.setRigType(remoteType);
                    requiresFlush = true;
                }
                      
                if (requiresFlush) rigDao.flush();
            }
            
            ses.setRig(remoteRig);
            ses.setAssignmentTime(new Date());
            
            this.updateSessionDetails(ses, provSes, db);
            
            /* mark the booking as redeemed, if this was caused by a booking. */
            if (booking != null)
            {
                booking.setActive(true);
                booking.setSession(ses);
                
                db.beginTransaction();
                db.flush();
                db.getTransaction().commit();
            }
            
            respType.setWasSuccessful(true);
        }
        finally
        {
           db.close();
        }
        
        return response;
    }

    @Override
    public SessionUpdateResponse sessionUpdate(final SessionUpdate sessionUpdate)
    {
        UserSessionType request = sessionUpdate.getSessionUpdate();
        String siteId = request.getSiteID();
        String userId = request.getUserID();
        this.logger.debug("Received " + this.getClass().getSimpleName() + "#sessionUpdate with params: site=" +
                siteId + ", user=" + userId + ".");
        
        SessionUpdateResponse response = new SessionUpdateResponse();
        OperationResponseType opResp = new OperationResponseType();
        opResp.setWasSuccessful(false);
        response.setSessionUpdateResponse(opResp);
        
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        try
        {
            RemoteSite site = new RemoteSiteDao(db).findSite(siteId);
            if (site == null)
            {
                this.logger.warn("Unable to update session for user '" + userId + "' because the site '" + siteId + 
                        "' was not found.");
                opResp.setReason("Site not found");
                return response;
            }
            
            Session ses = (Session) db.createCriteria(Session.class)
                    .add(Restrictions.eq("active", Boolean.TRUE))
                    .createCriteria("user")
                        .add(Restrictions.eq("namespace", site.getUserNamespace()))
                        .add(Restrictions.eq("name", userId))
                    .uniqueResult();
            if (ses == null)
            {
                this.logger.warn("Unable to update session for user '" + userId + "' because they do not have " +
                		"an active session.");
                opResp.setReason("Session not found.");
            }
            else
            {
                this.logger.info("Updating session for user '" + userId + "' because of notification from " +
                		"provider site '" + site.getName() + "'.");

                this.updateSessionDetails(ses, request.getSession(), db);
                opResp.setWasSuccessful(true);
            }
        }
        finally
        {
            db.close();
        }
        
        return response;     
    }
    
    @Override
    public SessionFinishedResponse sessionFinished(final SessionFinished sessionFinished)
    {
        String siteId = sessionFinished.getSessionFinished().getSiteID();
        String userId = sessionFinished.getSessionFinished().getUserID();
        this.logger.debug("Received " + this.getClass().getSimpleName() + "#sessionFinished with params: site=" +
                siteId + ", user=" + userId + '.');
        
        SessionFinishedResponse response = new SessionFinishedResponse();
        OperationResponseType opResp = new OperationResponseType();
        opResp.setWasSuccessful(false);
        response.setSessionFinishedResponse(opResp);
        
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        try
        {
            RemoteSite site = new RemoteSiteDao(db).findSite(siteId);
            if (site == null)
            {
                this.logger.warn("Unable to finish session for user '" + userId + "' because the site '" + siteId + 
                        "' was not found.");
                opResp.setReason("Site not found");
                return response;
            }
            
            Session ses = (Session) db.createCriteria(Session.class)
                    .add(Restrictions.eq("active", Boolean.TRUE))
                    .createCriteria("user")
                        .add(Restrictions.eq("namespace", site.getUserNamespace()))
                        .add(Restrictions.eq("name", userId))
                    .uniqueResult();
            if (ses == null)
            {
                this.logger.warn("Unable to terminate session for user '" + userId + "' because they do not have " +
                		"an active session.");
                opResp.setReason("Session not found.");
            }
            else
            {
                this.logger.info("Terminating session for user '" + userId + "' because of notification from " +
                		"provider site '" + site.getName() + "'.");
                
                ses.setActive(false);
                ses.setRemovalTime(new Date());
                ses.setRemovalReason("Provider notification.");
                
                db.beginTransaction();
                db.flush();
                db.getTransaction().commit();
                
                opResp.setWasSuccessful(true);
            }
        }
        finally
        {
            db.close();
        }
        
        return response;
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
    
    /**
     * Update session details with provider provided details.
     * 
     * @param ses session
     * @param provSes provider session details
     * @param db database
     */
    private void updateSessionDetails(Session ses, SessionType provSes, org.hibernate.Session db)
    {
        ses.setActivityLastUpdated(new Date());
        
        ses.setReady(provSes.getIsReady());
        
        // FIXME warning message
        ses.setInGrace(provSes.getInGrace());
        ses.setExtensions((short) provSes.getExtensions());
        ses.setDuration(provSes.getTime() + provSes.getTimeLeft());
        
        db.beginTransaction();
        if (ses.getId() == null) db.persist(ses);
        else db.flush();
        db.getTransaction().commit();
    }
}
