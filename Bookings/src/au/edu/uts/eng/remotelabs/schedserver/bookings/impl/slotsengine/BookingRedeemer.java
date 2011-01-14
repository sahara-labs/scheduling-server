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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import au.edu.uts.eng.remotelabs.schedserver.bookings.BookingActivator;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.BookingManagementTask;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Bookings;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.rigoperations.RigAllocator;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.RigEventListener;

/**
 * Tasks that converts bookings to sessions (i.e. redeems the booking).
 */
public class BookingRedeemer implements BookingManagementTask, RigEventListener
{
    /** The number of seconds between redeem runs. */
    public static final int REDEEM_INTERVAL = 30;
    
    /** The current day bookings. */
    private DayBookings currentDay;
    
    /** The current time slot. */
    private int currentSlot;
    
    /** The time at which the slot was rolled. */
    private long rollTime;
    
    /** List of bookings that are currently being redeemed. */
    private Map<String, MBooking> redeemingBookings;
    
    /** Logger. */
    private Logger logger;
    
    /** Flag to specify if this is a test run. */
    private boolean notTest = true;
    
    public BookingRedeemer(DayBookings startDay)
    {
        this.redeemingBookings = new HashMap<String, MBooking>();
        this.currentDay = startDay;
    }
    
    @Override
    public void run()
    {
        org.hibernate.Session db = null;
        try
        {
            db = DataAccessActivator.getNewSession();
            
            Calendar now = Calendar.getInstance();
            String nowDay = TimeUtil.getDateStr(now);
            int nowSlot = TimeUtil.getDaySlotIndex(now, nowDay);
            
            if (!nowDay.equals(this.currentDay.getDay()))
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
            }
            
            if (nowSlot != this.currentSlot)
            {
                /* The slot is being rolled over. */
                this.rollTime = now.getTimeInMillis();            
                this.currentSlot = nowSlot;
                
                synchronized (this)
                {
                    /* Cancel the remaining previous slot bookings. */
                    for (MBooking mb : this.redeemingBookings.values())
                    {
                        Bookings b = (Bookings) db.merge(mb.getBooking());
                        b.setActive(false);
                        b.setCancelReason("No resources free to redeem booking too.");
                        this.logger.debug("Unable to redeem booking (" + b.getId() + ") for " + b.getUser().qName() + 
                                " because no free resources were found in the slot period.");
                    }
                    
                    if (this.redeemingBookings.size() > 0)
                    {
                        db.beginTransaction();
                        db.flush();
                        db.getTransaction().commit();
                    }
                    
                    /* Get the new list of bookings. */
                    synchronized (this.currentDay)
                    {
                        this.redeemingBookings = this.currentDay.getSlotStartingBookings(this.currentSlot);
                    }
                    
                    RigDao rigDao = new RigDao(db);
                    // TODO continue
                }
            }
            

        }
        catch (Throwable thr)
        {
            // TODO 
        }
        finally 
        {
            if (db != null) db.close();
        }
        
    }
    
    @Override
    public void eventOccurred(RigStateChangeEvent event, Rig rig, org.hibernate.Session db)
    {
        switch (event)
        {
            case ONLINE:
                /* Falls through. */
            case FREE:
                if (this.redeemingBookings.containsKey(rig.getName()))
                {
                    MBooking mb;
                    synchronized (this)
                    {
                        mb = this.redeemingBookings.remove(rig.getName());
                    }
                    this.redeemBooking(mb, rig, db);
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
        db.save(membooking);
        booking.setSession(session);
        db.getTransaction().commit();
        
        membooking.setBooking(booking);
        membooking.setSession(session);
        
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
        return BookingRedeemer.REDEEM_INTERVAL;
    }

    @Override
    public void cleanUp()
    {
       /* Does nothing. */
    }
}
