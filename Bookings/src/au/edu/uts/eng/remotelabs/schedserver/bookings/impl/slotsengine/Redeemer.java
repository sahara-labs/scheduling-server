/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2011, University of Technology, Sydney
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
 * @date 11th November 2011
 */
package au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import au.edu.uts.eng.remotelabs.schedserver.bookings.BookingActivator;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.BookingManagementTask;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Bookings;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.rigoperations.RigAllocator;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.RigEventListener;

/**
 * Tasks that converts bookings to sessions (i.e. redeems the booking).
 */
public class Redeemer implements BookingManagementTask, RigEventListener
{
    /** The number of seconds between redeem runs. */
    public static final int REDEEM_INTERVAL = 10;
    
    /** The current day bookings. */
    private DayBookings currentDay;
    
    /** The current time slot. */
    private int currentSlot;
    
    /** The time at which the slot was rolled. */
    private long rollTime;
    
    /** List of bookings that are currently being redeemed. */
    private Map<String, MBooking> redeemingBookings;
    
    /** List oif bookings that are currently in session. */
    private Map<String, MBooking> runningBookings;    
    
    /** The next day string. */
    private String nextDay;
    
    /** Logger. */
    private Logger logger;
    
    /** Flag to specify if this is a test run. */
    private boolean notTest = true;
    
    public Redeemer(DayBookings startDay)
    {
        this.logger = LoggerActivator.getLogger();
        
        this.redeemingBookings = new HashMap<String, MBooking>();
        this.runningBookings = Collections.synchronizedMap(new HashMap<String, MBooking>());
        
        this.currentDay = startDay;
        
        Calendar next = TimeUtil.getDayBegin(this.currentDay.getDay());
        next.add(Calendar.DAY_OF_MONTH, 1);
        this.nextDay = TimeUtil.getDateStr(next);
    }
    
    @Override
    public void run()
    {
        org.hibernate.Session db = null;
        try
        {
            db = DataAccessActivator.getNewSession();
            
            synchronized (this)
            {
                Calendar now = Calendar.getInstance();
                String nowDay = TimeUtil.getDateStr(now);
                int nowSlot = TimeUtil.getDaySlotIndex(now, nowDay);

                if (now.equals(this.nextDay))
                {
                    /* The day has rolled over. */
                    this.logger.debug("Rolling day from " + this.currentDay.getDay() + " to " + nowDay + ".");

                    /* Clean up the old day bookings. We don't need that resident 
                     * any more as it is only historical. */
                    SlotBookingEngine engine = (SlotBookingEngine)BookingActivator.getBookingEngine();
                    engine.removeDay(this.currentDay.getDay());

                    /* Set the new day. */
                    this.currentDay = engine.getDayBookings(nowDay);
                    this.currentDay.fullLoad(db);
                    
                    /* Set the next day. */
                    Calendar next = Calendar.getInstance();
                    next.add(Calendar.DAY_OF_MONTH, 1);
                    this.nextDay = TimeUtil.getDateStr(next);
                }

                if (nowSlot != this.currentSlot)
                {
                    /* The slot is being rolled over. */
                    this.rollTime = now.getTimeInMillis();            
                    this.currentSlot = nowSlot;


                    /* Cancel the remaining previous slot bookings. */
                    for (MBooking mb : this.redeemingBookings.values())
                    {
                        Bookings b = (Bookings) db.merge(mb.getBooking());
                        b.setActive(false);
                        b.setCancelReason("No resources free to redeem booking too.");
                        this.logger.warn("Unable to redeem booking (" + b.getId() + ") for " + b.getUser().qName() + 
                        " because no free resources were found in the slot period.");
                    }

                    if (this.redeemingBookings.size() > 0)
                    {
                        this.logger.warn("Cancelling " + this.redeemingBookings.size() + " bookings that were on " +
                                this.currentDay.getDay() + ", slot " + (this.currentSlot - 1) + '.');
                        db.beginTransaction();
                        db.flush();
                        db.getTransaction().commit();
                    }

                    /* Get the new list of bookings. */
                    synchronized (this.currentDay)
                    {
                        this.redeemingBookings = this.currentDay.getSlotStartingBookings(this.currentSlot);
                    }

                    if (this.redeemingBookings.size() == 0)
                    {
                        this.logger.debug("No bookings are starting on " + this.currentDay.getDay() + ", slot " + 
                                this.currentSlot + '.');
                        return;
                    }

                    RigDao rigDao = new RigDao(db);
                    Iterator<Entry<String, MBooking>> it = this.redeemingBookings.entrySet().iterator();
                    while (it.hasNext())
                    {
                        Entry<String, MBooking> e = it.next();

                        Rig rig = rigDao.findByName(e.getKey());
                        if (rig != null && rig.isActive() && rig.isOnline() && !rig.isInSession())
                        {
                            /* Rig is free so assign it. */
                            this.logger.info("Rig " + rig.getName() + " is free so is having booking redeemed to it.");
                            this.redeemBooking(e.getValue(), rig, db);
                            it.remove();
                        }
                        else if (rig == null)
                        {
                            /* Rig is not found, serious issue. */
                            this.logger.warn("Booking on " + this.currentDay.getDay() + ", slot " + this.currentSlot + 
                                    " for rig " + e.getKey() + " that doesn't exist.");
                        }
                        else if (!(rig.isActive() && rig.isOnline()))
                        {
                            this.logger.debug("Booking on " + this.currentDay.getDay() + ", slot " + this.currentSlot + 
                                    " for rig " + e.getKey() + " cannot be redeemed because the rig is currently " +
                                    "offline.");
                        }
                        else if (rig.isInSession())
                        {
                            this.logger.debug("Booking on " + this.currentDay.getDay() + ", slot " + this.currentSlot + 
                                    " for rig " + e.getKey() + " cannot be redeemed because the rig is currently " +
                                    "in session.");
                        }
                    }
                }
                
                /* Try to load balance existing booking to a new rig. */
                Iterator<MBooking> it = this.redeemingBookings.values().iterator();
                while (it.hasNext())
                {
//                    MBooking mb = it.next();
                }
            }
        }
        catch (Throwable thr)
        {
            this.logger.error("Unchecked exception caught in redeemer task. Exception type: " + 
                    thr.getClass().getName() + ", message: " + thr.getMessage() + '.');
            
            // FIXME Remove following
            thr.printStackTrace();
            System.exit(-1);
        }
        finally 
        {
            if (db != null) db.close();
        }
        
    }
    
    @Override
    public void eventOccurred(RigStateChangeEvent event, Rig rig, org.hibernate.Session db)
    {
        /* Clean the previous session. */
        if (this.runningBookings.containsKey(rig.getName()))
        {
            MBooking old = this.runningBookings.remove(rig.getName());
            this.currentDay.removeBooking(old);
            
            if (old.isMultiDay() && old.getEndSlot() == SlotBookingEngine.NUM_SLOTS - 1)
            {
                /* Booking rolls to the next day. */
                DayBookings nextBookings = ((SlotBookingEngine)BookingActivator.getBookingEngine()).getDayBookings(this.nextDay);
                nextBookings.removeBooking(new MBooking(old.getBooking(), this.nextDay));
            }
            
            /* If the rig event was free, and the rig isn't booked, we need to 
             * fire another free broadcast to trigger another queue run. This is
             * because if the initial broadcast ran before this in queuer, then
             * the queue attempt would of falsely been blocked by the memory
             * representation of the terminated session.
             */
        }
        
                
        switch (event)
        {
            case ONLINE:
                /* Falls through. */
            case FREE:
                /* Remove the finished session. */                
                if (this.redeemingBookings.containsKey(rig.getName()))
                {
                    synchronized (this)
                    {
                        this.redeemBooking(this.redeemingBookings.remove(rig.getName()), rig, db);
                    }
                }
                break;
            default:
                /* Don't care about the other states. */
        }
        
    }
    
    /**
     * Redeems a booking by creating a session and allocating a rig to it.
     * 
     * @param membooking membooking to convert to session
     * @param rig rig to allocate
     * @param db database connection
     */
    private void redeemBooking(MBooking membooking, Rig rig, org.hibernate.Session db)
    {
        Date now = new Date();
        Bookings booking = (Bookings)db.merge(membooking.getBooking());
        this.logger.info("Redeeming a booking (" + booking.getId() + ") for " + booking.getUser().qName() + " using rig" + 
                rig.getName() + " at " + now + ".");

        Session session = new Session();
        session.setActive(true);
        session.setInGrace(false);
        session.setActivityLastUpdated(now);
        session.setReady(false);
        session.setPriority((short) 0);
        
        session.setRequestTime(now);
        session.setAssignmentTime(now);
        
        session.setUser(booking.getUser());
        session.setUserName(booking.getUserName());
        session.setUserNamespace(booking.getUserNamespace());
        
        session.setResourcePermission(booking.getResourcePermission());
        
        /* We need to remove the lag in redeeming the booking so we don't
         * propogate the lag to other bookings. */
        session.setDuration(booking.getDuration() - (int)(now.getTime() - this.rollTime) / 1000);
        session.setExtensions(booking.getResourcePermission().getAllowedExtensions());
        
        session.setResourceType(booking.getResourceType());
        if (ResourcePermission.RIG_PERMISSION.equals(booking.getResourceType()))
        {
            session.setRequestedResourceId(booking.getRig().getId());
            session.setRequestedResourceName(booking.getRig().getName());
        }
        else if (ResourcePermission.TYPE_PERMISSION.equals(booking.getResourceType()))
        {
            session.setRequestedResourceId(booking.getRigType().getId());
            session.setRequestedResourceName(booking.getRigType().getName());
        }
        else if (ResourcePermission.CAPS_PERMISSION.equals(booking.getResourceType()))
        {
            session.setRequestedResourceId(booking.getRequestCapabilities().getId());
            session.setRequestedResourceName(booking.getRequestCapabilities().getCapabilities());
        }
        session.setAssignedRigName(rig.getName());
        session.setRig(rig);
        
        session.setCodeReference(booking.getCodeReference());
        
        db.beginTransaction();
        db.save(session);
        booking.setActive(false);
        booking.setSession(session);
        db.getTransaction().commit();
        
        membooking.setBooking(booking);
        membooking.setSession(session);
        this.runningBookings.put(rig.getName(), membooking);
        
        this.logger.info("Assigned " + session.getUser().qName() + " to rig " + rig.getName() + " (session=" +
                session.getId() + ").");

        if (this.notTest)
        {
            new RigAllocator().allocate(session, db);
        }
    }
    
    @Override
    public int getPeriod()
    {
        return Redeemer.REDEEM_INTERVAL;
    }

    @Override
    public void cleanUp()
    {
       /* Does nothing. */
    }
}
