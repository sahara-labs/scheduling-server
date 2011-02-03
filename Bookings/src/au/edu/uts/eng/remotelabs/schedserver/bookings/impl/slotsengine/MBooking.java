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

import static au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine.SlotBookingEngine.TIME_QUANTUM;

import java.util.Calendar;
import java.util.Date;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Bookings;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RequestCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigOfflineSchedule;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;

/**
 * In-memory representation of a booking.
 */
public class MBooking
{
    /**
     * Booking rigType.
     */
    public enum BType
    {
        /** Specific rig booking. */
        RIG, /** Rig rigType booking. */
        TYPE, /** Request capabilities booking. */
        CAPABILITY
    }
    
    /** The transient booking of this rig. */
    private Bookings booking;
    
    /** The transient session of this booking. */
    private Session session;
    
    /** Whether this booking is in session. */
    private boolean inSession;
    
    /** Whether this is a maintenance holding booking. */
    private final boolean isMaintenance;
    
    /** The day the booking is located on. */
    private final String day;
    
    /** Whether this booking spans multiple day. */
    private boolean isMultiDay;
    
    /** The booking rigType. */
    private final BType bType;
    
    /** Booking / session start. */
    private final Date start;
    
    /** Booking / session duration. */
    private int duration;
    
    /** The index of this bookings start slot. */
    private final int startSlot;
    
    /** The index of the last slot used by this booking. */
    private int endSlot;
    
    /** The number of slots this booking uses. */
    private int numSlots;
    
    /** The rig rigType of this booking, if it is a rigType booking. */
    private RigType rigType;
    
    /** The request capabilities of this booking, if it is a request capabilities 
     * booking. */
    private RequestCapabilities reqCaps;
    
    /** The rig of this booking, if it is a rig booking. */
    private Rig rig;
    
    public MBooking(Bookings b, String day)
    {
        this.booking = b;
        this.day = day;
        this.isMultiDay = false;
        this.start = b.getStartTime();
        this.duration = b.getDuration();
        this.inSession = false;
        this.isMaintenance = false;
        
        if (ResourcePermission.CAPS_PERMISSION.endsWith(b.getResourceType()))
        {
            this.bType = BType.CAPABILITY;
            this.reqCaps = b.getRequestCapabilities();
        }
        else if (ResourcePermission.TYPE_PERMISSION.endsWith(b.getResourceType()))
        {
            this.bType = BType.TYPE;
            this.rigType = b.getRigType();
        }
        else 
        {
            this.bType = BType.RIG;
            this.rig = b.getRig();
        }

        if (TimeUtil.getDayBegin(this.day).getTime().after(b.getStartTime()))
        {
            /* The booking starts on the previous day and continues today. */
            this.startSlot = 0;
            this.isMultiDay = true;
        }
        else
        {
            /* The start slot is always the slot where the time lies in. */
            this.startSlot = TimeUtil.getSlotIndex(b.getStartTime());
        }
        
        if (TimeUtil.getDayEnd(this.day).getTime().before(b.getEndTime()))
        {
            /* The booking continues the following day. */
            this.endSlot = 24 * 60 * 60 / SlotBookingEngine.TIME_QUANTUM - 1;
            this.isMultiDay = true;
        }
        else
        {
            /* The end slot may be where the time falls or the preceding slot if
             * the booking ends exactly when the next slot starts. */
            Calendar end = Calendar.getInstance();
            end.setTime(b.getEndTime());
            this.endSlot = TimeUtil.getSlotIndex(end);
            
            if (this.endSlot * TIME_QUANTUM == 
                end.get(Calendar.HOUR_OF_DAY) * 3600 + end.get(Calendar.MINUTE) * 60 + end.get(Calendar.SECOND))
            {
                /* Not point wasting a slot when its time isn't used. */
                this.endSlot--;
            }
        }
        
        /* The number of slots between start and end inclusive. */
        this.numSlots = this.endSlot - this.startSlot + 1;  
    }
    
    public MBooking(Session ses, Rig rig, Calendar start, String day)
    {
        this.day = day;
        this.isMultiDay = false;
        this.start = start.getTime();
        this.duration = ses.getDuration();
        this.inSession = true;
        this.session = ses;
        this.isMaintenance = false;
        
        /* Must be a rig booking because the session is only ever committed to
         * a singular system. */
        this.bType = BType.RIG;
        this.rig = rig;

        if (TimeUtil.getDayBegin(this.day).after(start))
        {
            /* The booking starts on the previous day and continues today. */
            this.startSlot = 0;
            this.isMultiDay = true;
        }
        else
        {
            /* The start slot is always the slot where the time lies in. */
            this.startSlot = TimeUtil.getSlotIndex(start);
        }
        
        Calendar end = Calendar.getInstance();
        end.setTimeInMillis(start.getTimeInMillis());
        end.add(Calendar.SECOND, this.duration);
        if (TimeUtil.getDayEnd(this.day).before(end))
        {
            /* The booking continues the following day. */
            this.endSlot = 24 * 60 * 60 / SlotBookingEngine.TIME_QUANTUM - 1;
            this.isMultiDay = true;
        }
        else
        {
            /* The end slot may be where the time falls or the preceding slot if
             * the booking ends exactly when the next slot starts. */
            this.endSlot = TimeUtil.getSlotIndex(end);
            
            if (this.endSlot * TIME_QUANTUM == 
                end.get(Calendar.HOUR_OF_DAY) * 3600 + end.get(Calendar.MINUTE) * 60 + end.get(Calendar.SECOND))
            {
                /* Not point wasting a slot when its time isn't used. */
                this.endSlot--;
            }
        }
        
        /* The number of slots between start and end inclusive. */
        this.numSlots = this.endSlot - this.startSlot + 1;
    }
    
    public MBooking(RigOfflineSchedule off, String day)
    {
        this.day = day;
        this.isMultiDay = false;
        this.start = off.getStartTime();
        this.duration = (int) ((off.getEndTime().getTime() - off.getStartTime().getTime()) / 1000);
        this.inSession = false;
        this.isMaintenance = true;
        
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(off.getStartTime());
        if (TimeUtil.getDayBegin(this.day).after(startCal))
        {
            this.startSlot = 0;
            this.isMultiDay = true;
        }
        else this.startSlot = TimeUtil.getSlotIndex(startCal);
        
        Calendar end = Calendar.getInstance();
        end.setTime(off.getEndTime());
        if (TimeUtil.getDayEnd(this.day).before(end))
        {
            /* The booking continues the following day. */
            this.endSlot = 24 * 60 * 60 / SlotBookingEngine.TIME_QUANTUM - 1;
            this.isMultiDay = true;
        }
        else
        {
            /* The end slot may be where the time falls or the preceding slot if
             * the booking ends exactly when the next slot starts. */
            this.endSlot = TimeUtil.getSlotIndex(end);
            
            if (this.endSlot * TIME_QUANTUM == 
                end.get(Calendar.HOUR_OF_DAY) * 3600 + end.get(Calendar.MINUTE) * 60 + end.get(Calendar.SECOND))
            {
                /* Not point wasting a slot when its time isn't used. */
                this.endSlot--;
            }
        }
        
        this.bType = BType.RIG;
        this.rig = off.getRig();
    }
    
    public MBooking(int start, int end, BType type, String day)
    {
        this.startSlot = start;
        this.endSlot = end;
        this.numSlots = this.endSlot - this.startSlot + 1;
        this.bType = type;
        this.day = day;
        this.start = TimeUtil.getCalendarFromSlot(this.day, start).getTime();
        this.duration = (end - start + 1) * SlotBookingEngine.TIME_QUANTUM;
        this.isMaintenance = false;
    }
    
    /**
     * Extends the booking by the number of seconds.    
     * 
     * @param seconds seconds to extend
     * @return new end slot
     */
    public boolean extendBooking(int seconds)
    {
        this.duration += seconds;
        Calendar end = this.getEnd();
        this.endSlot = TimeUtil.getDaySlotIndex(end, this.day);
        this.numSlots = this.endSlot - this.startSlot + 1;
        
        if (!this.isMultiDay && this.endSlot == SlotBookingEngine.NUM_SLOTS - 1 && !this.day.equals(TimeUtil.getDayKey(end)))
        {
            /* The session has been extended over a day. */
            this.isMultiDay = true;
        }
         
        return true;
    }

    public Bookings getBooking()
    {
        return this.booking;
    }
    
    public void setBooking(Bookings b)
    {
        this.booking = b;
        
        if (this.bType == BType.TYPE)
        {
            this.rigType = b.getRigType();
        }
        else if (this.bType == BType.CAPABILITY)
        {
            this.reqCaps = b.getRequestCapabilities();
        }
        else
        {
            this.rig = b.getRig();
        }
    }

    public BType getType()
    {
        return this.bType;
    }

    public int getStartSlot()
    {
        return this.startSlot;
    }

    public int getEndSlot()
    {
        return this.endSlot;
    }

    public int getNumSlots()
    {
        return this.numSlots;
    }
    
    public Rig getRig()
    {
        return this.rig;
    }
    
    public RigType getRigType()
    {
        return this.rigType;
    }

    public RequestCapabilities getRequestCapabilities()
    {
        return this.reqCaps;
    }
    
    public String getDay()
    {
        return this.day;
    }

    public boolean isMultiDay()
    {
        return this.isMultiDay;
    }

    public boolean isMaintenanceHolder()
    {
        return this.isMaintenance;
    }

    public void setSession(Session session)
    {
        this.inSession = true;
        this.session = session;
    }
    
    public boolean isInSession()
    {
        return this.inSession;
    }

    public Session getSession()
    {
        return this.session;
    }
    
    public Calendar getStart()
    {
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(this.start);
        return startCal;
    }
    
    public int getDuration()
    {
        return this.duration;
    }
    
    public Calendar getEnd()
    {
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(this.start);
        endCal.add(Calendar.SECOND, this.duration);
        return endCal;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == null) return false;
        if (o == this) return true;
        if (!(o instanceof MBooking)) return false;
        
        MBooking m = (MBooking)o;
        if (this.inSession != m.isInSession()) return false;
        if (!this.day.equals(m.getDay())) return false;
        
        if (this.inSession)
        {
            if (this.session != null && m.getSession() != null) 
            {
                return this.session.getId().equals(m.getSession().getId());
            }
        }
        else
        {
            if (this.booking != null && m.getBooking() != null)
            {
                return this.getBooking().getId().equals(m.booking.getId());
            }
        }
        
        return false;
    }
    
    @Override
    public int hashCode()
    {
        int result = 23;
        result = result * 17 + this.day.hashCode();
        
        if (this.inSession)
        {
            if (this.session == null) return super.hashCode();
            result = result * 17 + this.session.getId().hashCode();
        }
        else
        {
            if (this.booking == null) return super.hashCode();
            result = result * 17 + this.booking.getId().hashCode();
        }
        
        return result;
    }
}
