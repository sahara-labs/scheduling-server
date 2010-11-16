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
package au.edu.uts.eng.remotelabs.schedserver.bookings.impl;

import static au.edu.uts.eng.remotelabs.schedserver.bookings.BookingActivator.TIME_QUANTUM;

import java.util.Calendar;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Bookings;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RequestCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;

/**
 * In-memory representation of a booking.
 */
public class MBooking
{
    /**
     * Booking type.
     */
    public enum BType
    {
        /** Specific rig booking. */
        RIG, /** Rig type booking. */
        TYPE, /** Request capabilities booking. */
        CAPABILITY
    }
    
    /** The transient booking of this rig. */
    private Bookings booking;
    
    /** The booking type. */
    private BType bType;
    
    /** The index of this bookings start slot. */
    private int startSlot;
    
    /** The index of the last slot used by this booking. */
    private int endSlot;
    
    /** The number of slots this booking uses. */
    private int numSlots;
    
    /** The request capabilities of this booking, if it is a request capabilities 
     * booking. */
    private RequestCapabilities reqCaps;
    
    public MBooking(Bookings b)
    {
        this.booking = b;
        
        if (ResourcePermission.CAPS_PERMISSION.endsWith(b.getResourceType()))
        {
            this.bType = BType.CAPABILITY;
            this.reqCaps = b.getRequestCapabilities();
        }
        else if (ResourcePermission.TYPE_PERMISSION.endsWith(b.getResourceType())) this.bType = BType.TYPE;
        else this.bType = BType.RIG;
        
        /* The start slot is always the slot where the time lies in. */
        this.startSlot = TimeUtil.getSlotIndex(b.getStartTime());
        
        /* The end slot may be where the time falls or the preceding slot if
         * the booking ends exactly when the next slot starts. */
        Calendar end = Calendar.getInstance();
        end.setTime(b.getEndTime());
        this.endSlot = TimeUtil.getSlotIndex(end);
        
        if (this.endSlot * TIME_QUANTUM == 
            end.get(Calendar.HOUR) * 3600 + end.get(Calendar.MINUTE) * 60 + end.get(Calendar.SECOND))
        {
            /* Not point wasting a slot when its time isn't used. */
            this.endSlot--;
        }
        
        /* The number of slots between start and end inclusive. */
        this.numSlots = this.endSlot - this.startSlot + 1;  
    }

    public Bookings getBooking()
    {
        return this.booking;
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

    public RequestCapabilities getRequestCaps()
    {
        return this.reqCaps;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (o == null) return false;
        if (!(o instanceof MBooking)) return false;
        
        return ((MBooking)o).getBooking().getId().equals(this.booking.getId());
    }
    
    @Override
    public int hashCode()
    {
        return this.booking.getId().hashCode();
    }
}
