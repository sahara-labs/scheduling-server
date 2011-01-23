/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2010, University of Technology, Sydney
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
 * @date 15th November 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import au.edu.uts.eng.remotelabs.schedserver.bookings.BookingEngineService;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.BookingEngine;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.BookingNotification;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Bookings;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RequestCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;

/**
 * The slot booking engine. This is an in-memory booking engine with uses 
 * aligned booking time.
 */
public class SlotBookingEngine implements BookingEngine, BookingEngineService
{
    /** The length of each booking slot in seconds. */
    public static final int TIME_QUANTUM = 15 * 60;
    
    /** The number of slots in a day. */
    public static final int NUM_SLOTS = 24 * 60 * 60 / SlotBookingEngine.TIME_QUANTUM;
    
    /** The loaded of day bookings. */
    private Map<String, DayBookings> days;
    
    /** Redeemer which redeems and cleans bookings. */
    private Redeemer redeemer;
    
    /** Logger. */
    private Logger logger;
    
    public SlotBookingEngine()
    {
        this.logger = LoggerActivator.getLogger();
        this.days = new TreeMap<String, DayBookings>();
    }
    
    @Override
    public synchronized BookingInit init()
    {
        this.logger.debug("Initalising the slot booking engine...");
        
        Calendar today = Calendar.getInstance();
        
        /* Cancel all bookings in the past. */
        Session db = DataAccessActivator.getNewSession();
        @SuppressWarnings("unchecked")
        List<Bookings> bookings = db.createCriteria(Bookings.class)
            .add(Restrictions.eq("active", Boolean.TRUE))
            .add(Restrictions.lt("endTime", today.getTime()))
            .list();
        for (Bookings bk : bookings)
        {
            this.logger.warn("Cancelling booking (" + bk.getId() + ") which expired when the Scheduling Server " +
            		"wasn't running for user " +   bk.getUser().qName() + " which expired on " + bk.getEndTime() + '.');
            bk.setActive(false);
            bk.setCancelReason("Expired when Scheduling Server wasn't running.");
            
            new BookingNotification(bk).notifyCancel();
        }
        if (bookings.size() > 0)
        {
            db.beginTransaction();
            db.flush();
            db.getTransaction().commit();
        }
        
        /* Load up the current day bookings. */
        DayBookings day = this.getDayBookings(TimeUtil.getDateStr(today));
        day.fullLoad(db);
        
        /* Initalise the management tasks. */
        BookingInit init = new BookingInit();
        this.redeemer = new Redeemer(day);
        init.addTask(this.redeemer);
        init.addListener(this.redeemer);
        init.addListener(new RigRegisteredListener());
        return init;
    }

    @Override
    public BookingCreation createBooking(User user, ResourcePermission perm, TimePeriod tp, Session ses)
    {
        Calendar start = tp.getStartTime();
        Calendar end = tp.getEndTime();
        
        BookingCreation response = new BookingCreation();
        
        Bookings bk = new Bookings();
        bk.setActive(true);
        bk.setResourcePermission(perm);
        
        /* Timing information. */
        bk.setStartTime(start.getTime());
        bk.setEndTime(end.getTime());
        bk.setDuration((int) (end.getTimeInMillis() - start.getTimeInMillis()) / 1000);
        
        /* User information. */
        bk.setUser(user);
        bk.setUserNamespace(user.getNamespace());
        bk.setUserName(user.getName());
        
        DayBookings db;
        List<String> dayKeys = TimeUtil.getDayKeys(start.getTime(), end.getTime());
        if (dayKeys.size() == 1)
        {
            /* Single day booking so we can proceed normally creating the booking. */
            String day = dayKeys.get(0);
            
            bk.setResourceType(perm.getType());
            bk.setRig(perm.getRig());
            bk.setRigType(perm.getRigType());
            bk.setRequestCapabilities(perm.getRequestCapabilities());
            
            MBooking mb = new MBooking(bk, day);
            synchronized (db = this.getDayBookings(day))
            {
                if (db.createBooking(mb, ses))
                {
                    response.setWasCreated(true);
                    try
                    {
                        /* Save the booking to the database. */
                        ses.beginTransaction();
                        ses.save(bk);
                        ses.getTransaction().commit();
                        response.setWasCreated(true);
                        response.setBooking(bk);
                        
                        String info = "Successfully created booking for " + user.getNamespace() + ':' + user.getName() + 
                                " on ";
                        switch (mb.getType())
                        {
                            case RIG:        info += "rig " + mb.getRig().getName(); break;
                            case TYPE:       info += "rig type " + mb.getRigType().getName(); break;
                            case CAPABILITY: info += "capabilities " + mb.getRequestCapabilities().getCapabilities(); break;
                        }
                        info += " to start at " + bk.getStartTime() + " and finish " + bk.getEndTime() + '.';
                        this.logger.info(info);
                    }
                    catch (HibernateException ex)
                    {
                        this.logger.error("Failed to persist a booking to the databse. Error: " + ex.getMessage() + 
                                ". Rolling back and removing the booking.");
                        ses.getTransaction().rollback();
                        response.setWasCreated(false);
                        db.removeBooking(mb);
                    }
                }
                else
                {
                    response.setWasCreated(false);
                    for (MRange range : db.findBestFits(mb, ses))
                    {
                        response.addBestFit(new TimePeriod(range.getStart(), range.getEnd()));
                    }
                }
            }
        }
        else
        {
            /* Multi-day booking so we will need to do some fiddling. */
            
            /* 1) If the booking is less than an hour each way of a hour divide,
             * give a best fit solution and not allow the booking to be created. */
            // TODO multi-day
        }
        
        return response;
    }

    @Override
    public List<TimePeriod> getFreeTimes(Rig rig, TimePeriod period, int minDuration, Session ses)
    {
        /* Work out the slots that the minimum duration requires. */
        int minSlots = (int)Math.ceil(minDuration / TIME_QUANTUM);
        
        /* Which days this falls across. */
        List<String> dayKeys = TimeUtil.getDayKeys(period);
        List<MRange> free = new ArrayList<MRange>();
        
        DayBookings db;
        for (String dayKey : dayKeys)
        {
            synchronized (db = this.getDayBookings(dayKey))
            {
                free.addAll(db.getFreeSlots(rig, 
                        TimeUtil.getDaySlotIndex(period.getStartTime(), dayKey), 
                        TimeUtil.getDaySlotIndex(period.getEndTime(), dayKey), 
                        minSlots, ses));
            }
        }
        
        return this.periodRangeCheck(MRange.rangeToTimePeriod(free), period);
    }

    @Override
    public List<TimePeriod> getFreeTimes(RigType rigType, TimePeriod period, int minDuration, Session ses)
    {
        int minSlots = (int)Math.ceil(minDuration / TIME_QUANTUM);
        List<String> dayKeys = TimeUtil.getDayKeys(period);
        List<MRange> free = new ArrayList<MRange>();
        
        DayBookings db;
        for (String dayKey : dayKeys)
        {
            synchronized (db = this.getDayBookings(dayKey))
            {
                free.addAll(db.getFreeSlots(rigType, 
                        TimeUtil.getDaySlotIndex(period.getStartTime(), dayKey), 
                        TimeUtil.getDaySlotIndex(period.getEndTime(), dayKey), 
                        minSlots, ses));
            }
        }
        
        return this.periodRangeCheck(MRange.rangeToTimePeriod(free), period);
    }

    @Override
    public List<TimePeriod> getFreeTimes(RequestCapabilities caps, TimePeriod period, int minDuration, Session ses)
    {
        int minSlots = (int)Math.ceil(minDuration / TIME_QUANTUM);
        List<String> dayKeys = TimeUtil.getDayKeys(period);
        List<MRange> free = new ArrayList<MRange>();
        
        DayBookings db;
        for (String dayKey : dayKeys)
        {
            synchronized (db = this.getDayBookings(dayKey))
            {
                free.addAll(db.getFreeSlots(caps, 
                        TimeUtil.getDaySlotIndex(period.getStartTime(), dayKey), 
                        TimeUtil.getDaySlotIndex(period.getEndTime(), dayKey), 
                        minSlots, ses));
            }
        }
        
        return this.periodRangeCheck(MRange.rangeToTimePeriod(free), period);
    }
    
    @Override
    public boolean cancelBooking(Bookings booking, String reason, Session ses)
    {
        boolean response = true;
        
        /* Remove the booking from the memory allocations. */
        DayBookings db;
        for (String dayKey : TimeUtil.getDayKeys(booking.getStartTime(), booking.getEndTime()))
        {
            synchronized (this.days)
            {
                if (this.days.containsKey(dayKey))
                {
                    synchronized (db = this.days.get(dayKey))
                    {
                        if (!db.removeBooking(new MBooking(booking, dayKey))) response = false; 
                    }
                }
            }
        }
        
        /* Cancel the booking. */ 
        booking.setActive(false);
        booking.setCancelReason(reason);
        ses.beginTransaction();
        ses.flush();
        ses.getTransaction().commit();
        
        return response;
    }
    
    @Override
    public boolean putQueuedSession(Rig rig, au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session ses,
           Session db)
    {   
        Calendar now = Calendar.getInstance();
        String dayKey = TimeUtil.getDateStr(now);
        MBooking mb = new MBooking(ses, rig, now, dayKey);
        boolean success = false;
        
        DayBookings dayb;
        if (mb.isMultiDay())
        {
            Calendar end = Calendar.getInstance();
            end.add(Calendar.SECOND, ses.getDuration());
            
            Map<String, MBooking> allocs = new HashMap<String, MBooking>();
            for (String day : TimeUtil.getDayKeys(now.getTime(), end.getTime()))
            {
                synchronized (dayb = this.getDayBookings(dayKey))
                {
                    if (!dayKey.equals(day)) mb = new MBooking(ses, rig, TimeUtil.getDayBegin(day), day);
                    
                }
            }
        }
        else
        {
            /* The booking is only on a single day so we don't need to go 
             * across days. */
            synchronized (dayb = this.getDayBookings(dayKey))
            {
                success = dayb.createBooking(mb, db);
            }
        }
        
        if (success)
        {
            this.redeemer.putRunningSession(rig, mb);
        }
        return success;
    }

    @Override
    public boolean extendQueuedSession(Rig rig, au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session ses,
            int duration, Session db)
    {
        // TODO Auto-generated method stub
        return false;
    }
    
    /**
     * Checks the range of the time period such that the first time period starts 
     * later or equal to the specified period start and ends earlier or equal to
     * specified period end.
     * 
     * @param fp list of time periods
     * @param period specified period
     * @return fp parameter
     */
    private List<TimePeriod> periodRangeCheck(List<TimePeriod> fp, TimePeriod period)
    {
        if (fp.size() > 0)
        {   
            /* Ranging check. Make sure the free start and end times are not 
             * earlier or later than the specified start or end periods 
             * respectively. */
            if (fp.get(0).getStartTime().before(period.getStartTime()))
            {
                fp.set(0, new TimePeriod(period.getStartTime(), fp.get(0).getEndTime()));
            }
            
            if (fp.get(fp.size() - 1).getEndTime().after(period.getEndTime()))
            {
                fp.set(fp.size() - 1, new TimePeriod(fp.get(fp.size() - 1).getStartTime(), period.getEndTime()));
            }
        }

        return fp;
    }
    
    /**
     * A rig has been registered. So notify each day to update
     * the resource loop mappings.
     * 
     * @param rig rig that was registered
     * @param db database session
     */
    public void rigRegistered(Rig rig, Session db)
    {
        this.logger.debug("Received rig " + rig.getName() + " registered event.");
        synchronized (this.days)
        {
            for (DayBookings day : this.days.values())
            {
                synchronized (day)
                {
                    day.rigRegistered(rig, db);
                }
            }
        }
    }
    
    /**
     * Returns the bookings for that day. If that day isn't loaded, it is 
     * loaded.
     * 
     * @param dayKey day key
     * @return bookings
     */
    public DayBookings getDayBookings(String dayKey)
    {
        if (!this.days.containsKey(dayKey))
        {
            synchronized (this.days)
            {
                if (!this.days.containsKey(dayKey))
                {
                    this.days.put(dayKey, new DayBookings(dayKey));
                }
            }
        }
        
        return this.days.get(dayKey);
    }
    
    /**
     * Removes the day from the day listing. 
     * 
     * @param day day bookings
     */
    public void removeDay(String day)
    {
        synchronized (this.days)
        {
            this.days.remove(day);
        }
    }
    
    @Override
    public void cleanUp()
    {
        this.logger.debug("Cleaning up the slot booking engine by clearing all days.");
        this.days.clear();
    }
}
