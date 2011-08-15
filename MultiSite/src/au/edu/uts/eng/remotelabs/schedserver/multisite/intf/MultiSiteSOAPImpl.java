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
 * @date 17th July 2011
 */
package au.edu.uts.eng.remotelabs.schedserver.multisite.intf;

import java.util.Calendar;

import au.edu.uts.eng.remotelabs.schedserver.bookings.BookingsActivator;
import au.edu.uts.eng.remotelabs.schedserver.bookings.pojo.BookingsService;
import au.edu.uts.eng.remotelabs.schedserver.bookings.pojo.types.BookingOperation;
import au.edu.uts.eng.remotelabs.schedserver.bookings.pojo.types.BookingsPeriod;
import au.edu.uts.eng.remotelabs.schedserver.bookings.pojo.types.BookingsPeriod.BookingSlot;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.BookingsDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RemotePermissionDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RemoteSiteDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.SessionDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserAssociationDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Bookings;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RemotePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RemoteSite;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserAssociation;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserAssociationId;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClass;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.multisite.MultiSiteActivator;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.AddToQueue;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.AddToQueueResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.AvailabilityResponseType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.BookingIDType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.BookingResponseType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.BookingSlotState;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.BookingSlotType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.BookingType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.CancelBooking;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.CancelBookingResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.CheckAvailability;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.CheckAvailabilityResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.CreateBooking;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.CreateBookingResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.CreateBookingType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.FindFreeBookings;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.FindFreeBookingsResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.FindFreeBookingsResponseType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.FindFreeBookingsType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.FinishSession;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.FinishSessionResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.GetQueuePosition;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.GetQueuePositionResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.GetSessionInformation;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.GetSessionInformationResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.GetUserStatus;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.GetUserStatusResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.OperationResponseType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.QueueRequestType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.QueueTargetType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.ResourceType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.SessionType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.TimePeriodType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.UserStatusType;
import au.edu.uts.eng.remotelabs.schedserver.queuer.pojo.QueuerService;
import au.edu.uts.eng.remotelabs.schedserver.queuer.pojo.types.QueueAvailability;
import au.edu.uts.eng.remotelabs.schedserver.queuer.pojo.types.QueueSession;
import au.edu.uts.eng.remotelabs.schedserver.session.pojo.SessionService;

/**
 * MultiSite service implementation.
 */
public class MultiSiteSOAPImpl implements MultiSiteSOAP
{
    /** Logger. */
    private Logger logger;
    
    public MultiSiteSOAPImpl()
    {
        this.logger = LoggerActivator.getLogger();
    }
    
    @Override
    public GetUserStatusResponse getUserStatus(GetUserStatus getUserStatus)
    {
        String siteId = getUserStatus.getGetUserStatus().getSiteID();
        String userId = getUserStatus.getGetUserStatus().getUserID();
        
        this.logger.debug("Received " + this.getClass().getSimpleName() + "#getUserStatus with params site=" + siteId +
                ", user=" + userId + '.');
        
        GetUserStatusResponse response = new GetUserStatusResponse();
        UserStatusType status = new UserStatusType();
        response.setGetUserStatusResponse(status);
        
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        try
        {
            RemoteSite site = new RemoteSiteDao(db).findSite(siteId);
            if (site == null)
            {
                this.logger.info("Cannot get user status for user '" + userId + "' because the site '" + siteId + 
                        "' because the site was not found.");
                return response;
            }
            
            User user = new UserDao(db).findByName(site.getUserNamespace(), userId);
            if (user == null)
            {
                this.logger.info("Cannot get user status for user '" + userId + "' because the site '" + siteId + 
                        "' because the user was not found.");
                return response;
            }
            
            Session session = new SessionDao(db).findActiveSession(user);
            if (session != null)
            {
                /* The user has an active session which accounts for the queuing 
                 * and actual session usage. */
                this.populateStatusDetails(status, session);
                return response;
            }

            /* The other status a user may have is a 'booking' status where 
             * they are in a booking stand-off. */
            Bookings booking = new BookingsDao(db).getNextBookingWithin(user, BookingsActivator.BOOKING_STANDOFF);
            if (booking != null)
            {
                status.setInBooking(true);
                
                ResourceType res = new ResourceType();
                res.setType(booking.getResourceType());
                if (ResourcePermission.RIG_PERMISSION.equals(booking.getResourceType()))
                {
                    res.setName(booking.getRig().getName());
                }
                else if (ResourcePermission.TYPE_PERMISSION.equals(booking.getResourceType()))
                {
                    res.setName(booking.getRigType().getName());
                }
                else if (ResourcePermission.CAPS_PERMISSION.equals(booking.getResourceType()))
                {
                    res.setName(booking.getRequestCapabilities().getCapabilities());
                }
                status.setBookedResource(res);
            }
        }
        finally
        {
            db.close();
        }
               
        return response;
    }

    @Override
    public CheckAvailabilityResponse checkAvailability(CheckAvailability checkAvailability)
    {
        String site = checkAvailability.getCheckAvailability().getSiteID();
        String pid = checkAvailability.getCheckAvailability().getPermissionID();
        
        this.logger.debug("Received " + this.getClass().getSimpleName() + "#checkAvailability with params site=" + 
                site + ", permission=" + pid + '.');
        
        /* Response parameters. */
        CheckAvailabilityResponse response = new CheckAvailabilityResponse();
        AvailabilityResponseType avail = new AvailabilityResponseType();
        response.setCheckAvailabilityResponse(avail);
        ResourceType res = new ResourceType();
        res.setName("");
        res.setType("");
        avail.setQueuedResource(res);
        
        RemotePermissionDao dao = new RemotePermissionDao();
        try
        {
            RemotePermission remotePerm = dao.findByGuid(pid);
            if (remotePerm == null)
            {
                /* Permission not found. */
                this.logger.warn("Cannot provide availability of permission '" + pid + "' because it was not found.");
                return response;
            }
            
            if (!remotePerm.getSite().getGuid().equals(site))
            {
                /* Origin is incorrect. */
                this.logger.warn("Cannot provide availability of permission '" + pid + "' because the site '" + 
                        site + "' does not match the stored site '" + remotePerm.getSite().getGuid() + "'.");
                return response;
            }
            
            QueuerService queuer = MultiSiteActivator.getQueuerService();
            if (queuer == null)
            {
                this.logger.warn("Cannot provider availability of permission '" + pid + "' becase the Queuer " +
                		"service was not found.");
                return response;
            }
            
            ResourcePermission perm = remotePerm.getPermission();
            avail.setIsBookable(perm.getUserClass().isBookable());
            avail.setIsQueueable(perm.getUserClass().isQueuable());
            
            QueueAvailability availability = queuer.checkAvailability(perm, dao.getSession());
            avail.setViable(availability.isViable());
            avail.setHasFree(availability.isHasFree());
            avail.setIsCodeAssignable(availability.isCodeAssignable());
            res.setName(availability.getName());
            res.setType(availability.getType());
            
            for (Rig rig : availability.getTargets())
            {
                QueueTargetType target = new QueueTargetType();
                target.setIsFree(availability.isTargetFree(rig));
                target.setViable(rig.isActive() && rig.isOnline());
                
                ResourceType targetRes = new ResourceType();
                targetRes.setName(rig.getName());
                targetRes.setType(ResourcePermission.RIG_PERMISSION);
                target.setResource(targetRes);
                
                avail.addQueueTarget(target);
            }
        }
        finally
        {
            dao.closeSession();
        }
        
        return response;
    }

    @Override
    public AddToQueueResponse addToQueue(AddToQueue addToQueue)
    {
        QueueRequestType request = addToQueue.getAddToQueue();
        final String site = request.getSiteID();
        final String pid = request.getPermission().getPermissionID();
        final String username = request.getUser().getUserID();
        
        this.logger.debug("Received " + this.getClass().getSimpleName() + "#addToQueue with params site=" + site + 
                ", permission=" + pid + ", user=" + username + '.');
        
        AddToQueueResponse response = new AddToQueueResponse();
        OperationResponseType opResp = new OperationResponseType();
        UserStatusType userStatus = new UserStatusType();
        response.setAddToQueueResponse(userStatus);
        userStatus.setOperation(opResp);
        opResp.setWasSuccessful(false);
                
        RemotePermissionDao dao = new RemotePermissionDao();
        try
        {
            RemotePermission remotePerm = dao.findByGuid(pid);
            if (remotePerm == null)
            {
                /* Permission not found. */
                this.logger.warn("Cannot add user '" + username + "' to queue because the remote permission '" + pid + 
                        "' was not found.");
                opResp.setReason("Permission not found.");
                return response;
            }
            
            if (!remotePerm.getSite().getGuid().equals(site))
            {
                this.logger.warn("Cannot add user '" + username + "' to queue because the because the site '" + site + 
                        "' does not match the store site '" + remotePerm.getSite().getGuid() + "'.");
                opResp.setReason("Incorrect origin.");
                return response;
            }
            
            QueuerService queuer = MultiSiteActivator.getQueuerService();
            if (queuer == null)
            {
                this.logger.warn("Cannot add user to '" + username + "' to queue because the because the Queuer " +
                		"service was not found.");
                opResp.setReason("Queuer service not found.");
                return response;
            }
            
            User user = this.getUser(username, remotePerm.getSite(), dao.getSession());
            this.ensureAssociation(user, remotePerm.getPermission().getUserClass(), dao.getSession());
            
            // TODO Batch support
            QueueSession ses = queuer.addUserToQueue(user, remotePerm.getPermission(), null, dao.getSession());
            if (ses.wasSuccessful())
            {
                opResp.setWasSuccessful(true);
                this.populateStatusDetails(userStatus, ses.getSession());
            }
            else
            {
                opResp.setReason(ses.getErrorMessage());
            }
        }
        finally
        {
            dao.closeSession();
        }

        return response;
    }

    @Override
    public GetQueuePositionResponse getQueuePosition(GetQueuePosition getQueuePosition)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GetSessionInformationResponse getSessionInformation(GetSessionInformation getSessionInformation)
    {
        String siteId = getSessionInformation.getGetSessionInformation().getSiteID();
        String userId = getSessionInformation.getGetSessionInformation().getUserID();
        
        this.logger.debug("Received " + this.getClass().getSimpleName() + "#getSessionInformation with params site=" + siteId +
                ", user=" + userId + '.');

        GetSessionInformationResponse response = new GetSessionInformationResponse();
        SessionType sesType = new SessionType();
        response.setGetSessionInformationResponse(sesType);
        
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        try
        {
            RemoteSite site = new RemoteSiteDao(db).findSite(siteId);
            if (site == null)
            {
                this.logger.info("Cannot get session information for user '" + userId + "' because the site '" + siteId + 
                        "' was not found.");
                return response;
            }
            
            User user = new UserDao(db).findByName(site.getUserNamespace(), userId);
            if (user == null)
            {
                this.logger.info("Cannot get session information for user '" + userId + "' because the user was not found.");
                return response;
            }
            
            Session session = new SessionDao(db).findActiveSession(user);
            if (session != null)
            {
                /* The user has an active session which accounts for the queuing 
                 * and actual session usage. */
                this.populateSessionDetails(sesType, session);
            }
            else
            {
                this.logger.debug("Session information for user '" + userId + "' returning dummy information because " +
                		"the user does not have an active session.");
                sesType.setTime(-1);
                sesType.setTimeLeft(-1);
                sesType.setExtensions(-1);
            }
        }
        finally
        {
            db.close();
        }
        
        
        return response;
    } 

    @Override
    public FindFreeBookingsResponse findFreeBookings(FindFreeBookings findFreeBookings)
    {
        FindFreeBookingsType request = findFreeBookings.getFindFreeBookings();
        String siteId = request.getSiteID();
        String permId = request.getPermission().getPermissionID();
        Calendar start = request.getPeriod().getStart();
        Calendar end = request.getPeriod().getEnd();
        
        this.logger.debug("Received " + this.getClass().getSimpleName() + "#findFreeBookings with params: site=" +
                siteId + ", permission=" + permId + ", start=" + start.getTime() + ", end=" + end.getTime() + ".");
        
        /* Response types. */
        FindFreeBookingsResponse response = new FindFreeBookingsResponse();
        FindFreeBookingsResponseType respType = new FindFreeBookingsResponseType();
        response.setFindFreeBookingsResponse(respType);
        respType.setPermission(request.getPermission());
        
        ResourceType res = new ResourceType();
        respType.setResource(res);
        res.setName("");
        res.setType("");
        
        RemotePermissionDao dao = new RemotePermissionDao();
        try
        {
            RemotePermission remotePerm = dao.findByGuid(permId);
            if (remotePerm == null)
            {
                this.logger.warn("Cannot find free times of remote permission because the permission '" + permId + 
                		"' was not found.");
                return response;
            }
            
            /* Make sure the site is correct. */
            if (!remotePerm.getSite().getGuid().equals(siteId))
            {
                this.logger.warn("Cannot find free times of remote permission because the site identifier '" +
                        siteId + "' was not found.");
                return response;
            }
            
            BookingsService bookings = MultiSiteActivator.getBookingsService();
            if (bookings == null)
            {
                this.logger.error("Cannot find free times of remote permission because the Bookings service was not " +
                        "running.");
                return response;
            }
            
            BookingsPeriod periods = bookings.getFreeBookings(start, end, remotePerm.getPermission(), dao.getSession());
            for (BookingSlot slot : periods.getSlots())
            {
                BookingSlotType s = new BookingSlotType();
                s.setStart(slot.getStart());
                s.setEnd(slot.getEnd());
                s.setState(BookingSlotState.Factory.fromValue(slot.getState()));
                respType.addSlot(s);
            }
        }
        finally
        {
            dao.closeSession();
        }
        
        return response;
    }

    @Override
    public CreateBookingResponse createBooking(CreateBooking createBooking)
    {
        CreateBookingType request = createBooking.getCreateBooking();
        String siteId = request.getSiteID();
        String userId = request.getUser().getUserID();
        
        BookingType booking = request.getBooking();
        String permId = booking.getPermission().getPermissionID();
        Calendar start = booking.getStart();
        Calendar end = booking.getEnd();
        this.logger.debug("Received " + this.getClass().getSimpleName() + "#createBooking with params: site=" + siteId + 
                ", user=" + userId + ", permission=" + permId + ", start=" + start.getTime() + ", end=" + end.getTime() + '.');
        
        CreateBookingResponse response = new CreateBookingResponse();
        BookingResponseType respType = new BookingResponseType();
        response.setCreateBookingResponse(respType);
                
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        try
        {
            RemotePermission remotePerm = new RemotePermissionDao(db).findByGuid(permId);
            if (remotePerm == null)
            {
                this.logger.warn("Cannot create booking because the remote permission '" + permId + "' was not found.");
                return response;
            }
            
            /* Sanity check for origin. */
            if (!remotePerm.getSite().getGuid().equals(siteId))
            {
                this.logger.warn("Cannot create booking because the site origin '" + siteId + "' was incorrect.");
                return response;
            }
            
            User user = this.getUser(userId, remotePerm.getSite(), db);
            
            BookingsService service = MultiSiteActivator.getBookingsService();
            if (service == null)
            {
                this.logger.error("Cannot create booking because the Bookings service was not loaded.");
                return response;
            }
            
            BookingOperation opResp = service.createBooking(start, end, user, remotePerm.getPermission(), db);
            respType.setWasSuccessful(opResp.successful());
            respType.setReason(opResp.getFailureReason());
            if (opResp.successful())
            {
                BookingIDType bid = new BookingIDType();
                bid.setId(opResp.getBooking().getId().intValue());
                bid.setSiteID(siteId);
                respType.setBookingID(bid);
            }
            else if (opResp.getBestFits().size() > 0)
            {
                for (BookingOperation.BestFit bf : opResp.getBestFits())
                {
                    TimePeriodType tp = new TimePeriodType();
                    tp.setStart(bf.getStart());
                    tp.setEnd(bf.getEnd());
                    respType.addBestFits(tp);
                }
            }
        }
        finally
        {
            db.close();
        }
        
        
        return response;
    }

    @Override
    public CancelBookingResponse cancelBooking(CancelBooking cancelBooking)
    {
        String siteId = cancelBooking.getCancelBooking().getSiteID();
        long bId = cancelBooking.getCancelBooking().getId();
        
        this.logger.debug("Received " + this.getClass().getSimpleName() + "#cancelBooking with params site=" + siteId +
                ", booking identifer=" + bId + '.');
        
        CancelBookingResponse response = new CancelBookingResponse();
        OperationResponseType opResp = new OperationResponseType();
        response.setCancelBookingResponse(opResp);
                
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        try
        {
            RemoteSite site = new RemoteSiteDao(db).findSite(siteId);
            if (site == null)
            {
                this.logger.warn("Unable to cancel booking (" + bId + ") because the site '" + siteId + "' was not " +
                		"found.");
                opResp.setReason("Site not found.");
                return response;
            }
            
            Bookings booking = new BookingsDao(db).get(bId);
            if (booking == null)
            {
                this.logger.warn("Unable to cancel booking (" + bId + ") because the booking was not found.");
                opResp.setReason("Booking not found.");
                return response;
            }
                                    
            RemotePermission remotePerm = booking.getResourcePermission().getRemotePermission();
            if (remotePerm == null || !remotePerm.getSite().getGuid().equals(siteId))
            {
                this.logger.warn("Unable to cancel booking (" + bId + ") because it is not from the correct site.");
                opResp.setReason("Not from correct site.");
                return response;
            }
            
            BookingsService bookingsService = MultiSiteActivator.getBookingsService();
            if (bookingsService == null)
            {
                this.logger.error("Cannot cancel booking because the bookings service was not loaded.");
                opResp.setReason("Booking service not loaded.");
                return response;
            }
            
            BookingOperation bk = bookingsService.cancelBooking(booking, "Multisite request.", false, db);
            opResp.setWasSuccessful(bk.successful());
            opResp.setReason(bk.getFailureReason());
        }
        finally
        {
            db.close();
        }

        return response;
    }

    @Override
    public FinishSessionResponse finishSession(FinishSession finishSession)
    {
        String siteId = finishSession.getFinishSession().getSiteID();
        String userId = finishSession.getFinishSession().getUserID();
       
        this.logger.debug("Received " + this.getClass().getSimpleName() + "#finishSession with params site=" + siteId +
                ", user=" + userId + '.');
        
        FinishSessionResponse response = new FinishSessionResponse();
        OperationResponseType opResp = new OperationResponseType();
        response.setFinishSessionResponse(opResp);
        
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        try
        {
            /* Load site. */
            RemoteSite site = new RemoteSiteDao(db).findSite(siteId);
            if (site == null)
            {
                this.logger.warn("Unable to finish session for user '" + userId + "' because the site '" + siteId + 
                        "' was not found.");
                opResp.setReason("Site not found");
                return response;
            }
            
            /* Load user. */
            User user = new UserDao(db).findByName(site.getUserNamespace(), userId);
            if (user == null)
            {
                this.logger.warn("Unable to finish session for user '" + userId + "' because the user was not found.");
                opResp.setReason("User not found.");
                return response;
            }
            
            Session ses = new SessionDao(db).findActiveSession(user);
            if (ses == null)
            {
                this.logger.warn("Unable to finish session for user '" + userId + "' because the user does not have " +
                		"an active session.");
                opResp.setReason("User does not have active session");
                return response;
            }
            
            SessionService service = MultiSiteActivator.getSessionService();
            if (service == null)
            {
                this.logger.error("Unable to finish session because the Session Service was not loaded.");
                opResp.setReason("Session not loaded.");
                return response;
            }
            
            opResp.setWasSuccessful(service.finishSession(ses, db));
        }
        finally
        {
            db.close();
        }

        return response;
    }

    /**
     * Gets a user. If the user does not exist, a new user is created.
     * 
     * @param name the name of a user
     * @param site the site the user is from
     * @param session database session
     * @return user 
     */
    private User getUser(String name, RemoteSite site, org.hibernate.Session db)
    {
        UserDao dao = new UserDao(db);
        String ns = site.getUserNamespace();

        User user = dao.findByName(ns, name);
        if (user == null)
        {
            this.logger.info("Remote user '" + name + "' from site '" + site + "' with namespace '" + ns + 
                    "' does not exist so will be created.");
            
            user = new User();
            user.setNamespace(ns);
            user.setName(name);
            user.setPersona(User.USER); // Remote users are always uses.
            dao.persist(user);
        }
        
        return user;
    }
    
    /**
     * Makes sure the user has an association to the user class. If the user doesn't have
     * the association, then the association is added.
     * 
     * @param user user 
     * @param userClass class to
     * @param db
     */
    private void ensureAssociation(User user, UserClass userClass, org.hibernate.Session db)
    {
        UserAssociationDao dao = new UserAssociationDao(db);
        UserAssociationId id = new UserAssociationId(user.getId(), userClass.getId());
        
        if (dao.get(id) == null)
        {
            /* User association does not exist, so add it. */
            UserAssociation assoc = new UserAssociation();
            assoc.setId(id);
            assoc.setUser(user);
            assoc.setUserClass(userClass);
            dao.persist(assoc);
        }
    }
    
    /**
     * Populates the details of a session to a user status response type.
     * 
     * @param status user status type
     * @param session session
     */
    private void populateStatusDetails(UserStatusType status, Session session)
    {
        status.setInBooking(false);
        status.setInQueue(false);
        status.setInSession(false);
        
        if (session.getAssignmentTime() == null)
        {
            /* User in queue. */
            status.setInQueue(true);
            ResourceType res = new ResourceType();
            res.setType(session.getResourceType());
            res.setName(session.getRequestedResourceName());
            status.setQueuedResource(res);
        }
        else
        {
            /* User in session. */
            status.setInSession(true);
            
            SessionType sesType = new SessionType();
            status.setSession(sesType);
            this.populateSessionDetails(sesType, session);
        }
    }
    
    /**
     * Populates session details to session type.
     * 
     * @param sesType session type
     * @param session session
     */
    private void populateSessionDetails(SessionType sesType, Session session)
    {
        sesType.setIsReady(true); //ses.isReady());
        sesType.setIsCodeAssigned(session.getCodeReference() != null);
        sesType.setInGrace(session.isInGrace());

        ResourceType res = new ResourceType();
        res.setType(session.getResourceType());
        res.setName(session.getRequestedResourceName());


        sesType.setResource(res);

        Rig rig = session.getRig();
        sesType.setRigType(rig.getRigType().getName());
        sesType.setRigName(rig.getName());
        sesType.setContactURL(rig.getContactUrl());

        // FIXME  Add session timing details & warning details
        sesType.setTime(100);
        sesType.setTimeLeft(200);
        sesType.setExtensions(2);
    }
}
