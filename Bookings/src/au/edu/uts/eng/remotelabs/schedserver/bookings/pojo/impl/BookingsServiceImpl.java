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
import java.util.List;

import org.hibernate.Session;

import au.edu.uts.eng.remotelabs.schedserver.bookings.BookingsActivator;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.BookingEngine;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.BookingEngine.TimePeriod;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine.TimeUtil;
import au.edu.uts.eng.remotelabs.schedserver.bookings.pojo.BookingsService;
import au.edu.uts.eng.remotelabs.schedserver.bookings.pojo.types.BookingsPeriod;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.requests.BookingsTimes;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.requests.BookingsTimes.BookingTime;

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
            BookingsTimes multiSiteReq = new BookingsTimes();
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

}
