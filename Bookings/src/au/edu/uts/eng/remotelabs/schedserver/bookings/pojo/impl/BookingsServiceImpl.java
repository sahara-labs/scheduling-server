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
 * @date 7th August 2011
 */
package au.edu.uts.eng.remotelabs.schedserver.bookings.pojo.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import au.edu.uts.eng.remotelabs.schedserver.bookings.BookingsActivator;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.BookingEngine;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.BookingEngine.BookingCreation;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.BookingEngine.TimePeriod;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.BookingNotification;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine.TimeUtil;
import au.edu.uts.eng.remotelabs.schedserver.bookings.pojo.BookingsService;
import au.edu.uts.eng.remotelabs.schedserver.bookings.pojo.types.BookingOperation;
import au.edu.uts.eng.remotelabs.schedserver.bookings.pojo.types.BookingsPeriod;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.BookingsDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Bookings;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.requests.BookingsTimesRequest;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.requests.BookingsTimesRequest.BookingTime;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.requests.CancelBookingRequest;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.requests.CreateBookingRequest;

/**
 * Bookings service implementation.
 */
public class BookingsServiceImpl implements BookingsService
{
    /** Bookings engine. */
    private BookingEngine engine;
    
    /** Logger. */
    private Logger logger;
    
    public BookingsServiceImpl()
    {
        this.logger = LoggerActivator.getLogger();
        this.engine = BookingsActivator.getBookingEngine();
    }
    
    @Override
    public BookingsPeriod getFreeBookings(Calendar reqStart, Calendar reqEnd, ResourcePermission perm, Session db)
    {
        BookingsPeriod response = new BookingsPeriod();
    
        /* Make sure the permission a valid booking permission. */
        if (!(perm.getUserClass().isActive() && perm.getUserClass().isBookable()))
        {
            this.logger.warn("Unable to provide free times because the permission is not a valid booking permission.");
            response.addSlot(reqStart, reqEnd, BookingsPeriod.NO_PERMISSION);
            return response;
        }
        
        /* ----------------------------------------------------------------
         * -- Check permission times to make sure the request is within  --
         * -- the permission start and expiry range.                     --
         * ---------------------------------------------------------------- */
        Calendar permStart = Calendar.getInstance();
        permStart.setTime(perm.getStartTime());

        /* The /actual/ permission start time may either be the permission 
         * start time or the time horizon time. This is a second offset from
         * the current time (i.e. it atleast stops bookings being made for
         * the past. */
        Calendar horizonTime = Calendar.getInstance();
        horizonTime.add(Calendar.SECOND, perm.getUserClass().getTimeHorizon());
        if (horizonTime.after(permStart))
        {
            /* Which ever comes later. */
            permStart = TimeUtil.coerceToNextSlotTime(horizonTime);
        }

        Calendar permEnd = Calendar.getInstance();
        permEnd.setTime(perm.getExpiryTime());

        if (reqEnd.before(permStart) || reqStart.after(permEnd) || permEnd.before(permStart))
        {
            /* In this case the requested range is, after the end of the 
             * permission region, before the start of the permission 
             * region or the permission start is after the permission end
             * (the permission start is always sliding for horizion). */
            response.addSlot(reqStart, reqEnd, BookingsPeriod.NO_PERMISSION);
            return response;
        }

        if (reqStart.before(permStart))
        {
            /* Here the permission start time is after the requested start time 
             * so the start partial date has no permission. */
            response.addSlot(reqStart, permStart, BookingsPeriod.NO_PERMISSION);
            
            /* The permission start time is now the search start time. */
            reqStart = permStart;
        }

        Calendar searchEnd = reqEnd;
        if (reqEnd.after(permEnd))
        {
            /* Here the permission end time is before the requested end time
             * so the end partial date has no permission. We will search the free
             * search end to the permission end but add the no permission period
             * last. */
            searchEnd = permEnd;
        }

        /* ----------------------------------------------------------------
         * -- Get the free times for the permission resource.            --
         * ---------------------------------------------------------------- */
        List<TimePeriod> free = null;
        if (ResourcePermission.RIG_PERMISSION.equals(perm.getType()))
        {
            free = this.engine.getFreeTimes(perm.getRig(), new TimePeriod(reqStart, searchEnd),
                    perm.getSessionDuration() / 2, db);
        }
        else if (ResourcePermission.TYPE_PERMISSION.equals(perm.getType()))
        { 
            free = this.engine.getFreeTimes(perm.getRigType(), new TimePeriod(reqStart, searchEnd),
                    perm.getSessionDuration() / 2, db);
        }
        else if (ResourcePermission.CAPS_PERMISSION.equals(perm.getType()))
        {
            free = this.engine.getFreeTimes(perm.getRequestCapabilities(), new TimePeriod(reqStart, searchEnd),
                    perm.getSessionDuration() / 2, db);
        }
        else if (ResourcePermission.CONSUMER_PERMISSION.equals(perm.getType()))
        {
            free = new ArrayList<BookingEngine.TimePeriod>();
            /* We need to ask the remote site about times. */
            BookingsTimesRequest multiSiteReq = new BookingsTimesRequest();
            if (multiSiteReq.findFreeTimes(perm.getRemotePermission(), reqStart, searchEnd, db))
            {
                /* We are integrated the multisite result set with the local 
                 * times so we are only interested in the free times. */
                for (BookingTime t : multiSiteReq.getTimes())
                {
                    if (BookingsPeriod.FREE.equals(t.getState())) free.add(new TimePeriod(t.getStart(), t.getEnd()));
                }
            }
            else
            {
                this.logger.warn("Unable to find free times because multisite call failed.");
            }
        }

        /* ----------------------------------------------------------------
         * -- Populate the resource with free and booked time            --
         * ---------------------------------------------------------------- */
        Calendar c = reqStart;
        if (free.size() > 0)
        {
            for (TimePeriod period : free)
            {
                if (Math.abs(period.getStartTime().getTimeInMillis() - c.getTimeInMillis()) > 60000)
                {
                    /* The difference with the last time and the next time is
                     * more than a minute, then there should be a booked period. */
                    response.addSlot(c, period.getStartTime(), BookingsPeriod.BOOKED);
                }
                
                response.addSlot(period.getStartTime(), period.getEndTime(), BookingsPeriod.FREE);
                c = period.getEndTime();
            }

            /* There is a booked spot at the end. */
            if (Math.abs(searchEnd.getTimeInMillis() - c.getTimeInMillis()) > 60000)
            {
                /* The difference with the last time and the next time is
                 * more than a minute, then there should be a booked period. */
                response.addSlot(c, searchEnd, BookingsPeriod.BOOKED);
            }
        }
        else
        {
            /* There is no free times on the day. */
            response.addSlot(reqStart, searchEnd, BookingsPeriod.BOOKED);
        }

        if (reqEnd.after(permEnd))
        {
            /* Add a no permission at the end. */
            response.addSlot(permEnd, reqEnd, BookingsPeriod.NO_PERMISSION);
        }

        return response;
    }

    @Override
    public BookingOperation createBooking(Calendar start, Calendar end, User user, ResourcePermission perm, Session db)
    {
        BookingOperation response = new BookingOperation();
        response.setSuccess(false);
        
        /* ----------------------------------------------------------------
         * -- Check permission constraints.                              --
         * ---------------------------------------------------------------- */
        Date startDate = start.getTime();
        Date endDate = end.getTime();
        
        if (!perm.getUserClass().isBookable())
        {
            this.logger.info("Unable to create a booking because the permission " + perm.getId() + " is not bookable.");
            response.setFailureReason("Permission not bookable.");
            return response;
        }

        if (startDate.before(perm.getStartTime()) || endDate.after(perm.getExpiryTime()))
        {
            this.logger.info("Unable to create a booking because the booking time is outside the permission time. " +
                    "Permission start: " + perm.getStartTime() + ", expiry: " + perm.getExpiryTime() + 
                    ", booking start: " + startDate + ", booking end: " + endDate + '.');
            response.setFailureReason("Booking time out of permission range.");
            return response;
        }

        /* Time horizon is a moving offset when bookings can be made. */
        Calendar horizon = Calendar.getInstance();
        horizon.add(Calendar.SECOND, perm.getUserClass().getTimeHorizon());
        if (horizon.after(start))
        {
            this.logger.info("Unable to create a booking because the booking start time (" + startDate +
                    ") is before the time horizon (" + horizon.getTime() + ").");
            response.setFailureReason("Before time horizon.");
            return response;
        }

        /* Maximum concurrent bookings. */
        int numBookings = (Integer) db.createCriteria(Bookings.class)
                .add(Restrictions.eq("active", Boolean.TRUE))
                .add(Restrictions.eq("user", user))
                .add(Restrictions.eq("resourcePermission", perm))
                .setProjection(Projections.rowCount())
                .uniqueResult();
        if (numBookings >= perm.getMaximumBookings())
        {
            this.logger.info("Unable to create a booking because the user " + user.getNamespace() + ':' + 
                    user.getName() + " already has the maxiumum numnber of bookings (" + numBookings + ").");
            response.setFailureReason("User has maximum number of bookings.");
            return response;
        }

        /* User bookings at the same time. */
        numBookings = (Integer) db.createCriteria(Bookings.class)
                .setProjection(Projections.rowCount())
                .add(Restrictions.eq("active", Boolean.TRUE))
                .add(Restrictions.eq("user", user))
                .add(Restrictions.disjunction()
                        .add(Restrictions.and(
                                Restrictions.gt("startTime", startDate),
                                Restrictions.lt("startTime", endDate))) 
                        .add(Restrictions.and(
                                Restrictions.gt("endTime", startDate),
                                Restrictions.le("endTime", endDate)))
                        .add(Restrictions.and(
                                Restrictions.le("startTime", startDate),
                                Restrictions.gt("endTime", endDate)))
                 ).uniqueResult();
        
        if (numBookings > 0)
        {
            this.logger.info("Unable to create a booking because the user " + user.getNamespace() + ':' +
                    user.getName() + " has concurrent bookings.");
            response.setFailureReason("User has concurrent bookings.");
            return response;
        }

        /* ----------------------------------------------------------------
         * -- Create booking.                                            --
         * ---------------------------------------------------------------- */
        if (ResourcePermission.CONSUMER_PERMISSION.equals(perm.getType()))
        {
            CreateBookingRequest request = new CreateBookingRequest();
            if (request.createBooking(user, perm.getRemotePermission(), start, end, db))
            {
                response.setSuccess(request.wasSuccessful());
                response.setFailureReason(request.getReason());
                if (request.wasSuccessful())
                {
                    /* Provider created booking so we now need to create it 
                     * locally. */
                    this.logger.debug("Successfullly created booking at provider with identifier " + 
                            request.getBookingID() + '.');
                    Bookings bk = new Bookings();
                    bk.setActive(true);
                    bk.setStartTime(startDate);
                    bk.setEndTime(endDate);
                    bk.setDuration((int)(end.getTimeInMillis() - start.getTimeInMillis()) / 1000);
                    bk.setResourcePermission(perm);
                    bk.setResourceType(ResourcePermission.CONSUMER_PERMISSION);
                    bk.setProviderId(request.getBookingID());
                    bk.setUser(user);
                    bk.setUserNamespace(user.getNamespace());
                    bk.setUserName(user.getName());
                    response.setBooking(new BookingsDao(db).persist(bk));
                    
                    /* Notification emails are only sent to home users. */
                    new BookingNotification(bk).notifyCreation();
                }
                else
                {
                    this.logger.info("Provider failed to create booking with reason " + request.getReason());
                    
                    /* Provider returned that it couldn't create booking. */
                    for (BookingTime bt : request.getBestFits())
                    {
                        response.addBestFit(bt.getStart(), bt.getEnd());
                    }
                }
            }
            else
            {
                /* Provider call failed. */
                this.logger.info("Provider call to create booking failed with reason " + request.getFailureReason());
                response.setSuccess(false);
                response.setFailureReason("Provider request failed (" + request.getFailureReason()  + ")");
            }
        }
        else
        {
            BookingCreation bc = this.engine.createBooking(user, perm, new TimePeriod(start, end), db);
            if (bc.wasCreated())
            {
                response.setSuccess(true);
                response.setBooking(bc.getBooking());
    
                /* Notification emails are only sent to home users. */
                if (perm.getRemotePermission() == null) new BookingNotification(bc.getBooking()).notifyCreation();
            }
            else
            {
                response.setSuccess(false);
                response.setFailureReason("Resource not free.");
    
                for (TimePeriod tp : bc.getBestFits())
                {
                    numBookings = (Integer) db.createCriteria(Bookings.class)
                            .setProjection(Projections.rowCount())
                            .add(Restrictions.eq("active", Boolean.TRUE))
                            .add(Restrictions.eq("user", user))
                            .add(Restrictions.disjunction()
                                    .add(Restrictions.and(
                                            Restrictions.gt("startTime", tp.getStartTime().getTime()),
                                            Restrictions.lt("startTime", tp.getEndTime().getTime())
                                            ))       
                                            .add(Restrictions.and(
                                                    Restrictions.gt("endTime", tp.getStartTime().getTime()),
                                                    Restrictions.le("endTime", tp.getEndTime().getTime())
                                            ))
                                            .add(Restrictions.and(
                                                    Restrictions.le("startTime", tp.getStartTime().getTime()),
                                                    Restrictions.gt("endTime", tp.getEndTime().getTime()))
                                            )
                                    ).uniqueResult();
                    if (numBookings > 0)
                    {
                        this.logger.debug("Excluding best fit option for user " + user.qName() + " because it is " +
                                "concurrent with an existing.");
                        continue;
                    }
                    
                    response.addBestFit(tp.getStartTime(), tp.getEndTime());
                }
            }
        }

        return response;
    }

    @Override
    public BookingOperation cancelBooking(Bookings booking, String reason, boolean user, Session db)
    {
        BookingOperation status = new BookingOperation();
        
        if (!booking.isActive())
        {
            this.logger.info("Unable to delete booking because the booking has already been canceled or redeemed.");
            status.setFailureReason("Booking already canceled or redeemed.");
            return status;
        }
        
        if (ResourcePermission.CONSUMER_PERMISSION.equals(booking.getResourcePermission().getType()))
        {
            CancelBookingRequest multiSite = new CancelBookingRequest();
            if (multiSite.cancelBooking(booking, db) && multiSite.wasSuccessful())
            {
                status.setSuccess(true);
                this.logger.debug("Provider successfully cancelled booking with ID '" + booking.getId() + "'.");
                
                /* Since the provider has cancelled the booking, the local mapped
                 * booked also needs to be cancelled. */
                db.beginTransaction();
                booking.setActive(false);
                booking.setCancelReason(reason);
                db.getTransaction().commit();
            }
            else
            {
                String error = multiSite.getFailureReason();
                if (multiSite.getReason() != null) error = multiSite.getReason();
                this.logger.warn("Failed to cancel booking because provider failed with reason: " + error);
                status.setFailureReason(error);
            }
        }
        else
        {
            if (!this.engine.cancelBooking(booking, reason, db))
            {
                status.setFailureReason("System error.");
            }
            else
            {
                status.setSuccess(true);
            }
        }
        
        /* Only send notification to home users. */
        if (booking.getResourcePermission().getRemotePermission() == null || 
                ResourcePermission.CONSUMER_PERMISSION.equals(booking.getResourceType()))
        {
            if (user) new BookingNotification(booking).notifyUserCancel();
            else      new BookingNotification(booking).notifyCancel();
        }
        
        return status;
    }
}
