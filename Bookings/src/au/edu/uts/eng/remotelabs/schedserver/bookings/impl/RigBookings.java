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
 * @date 11th November 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.bookings.impl;

import static au.edu.uts.eng.remotelabs.schedserver.bookings.impl.MBooking.BType.RIG;
import static au.edu.uts.eng.remotelabs.schedserver.bookings.impl.MBooking.BType.TYPE;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import au.edu.uts.eng.remotelabs.schedserver.bookings.BookingActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RequestCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;

/**
 * Contains the bookings for a rig on a single day in terms of slots.
 */
public class RigBookings
{
    /** The day these booking refer to. */
    private String dayKey;
    
    /** Transient rig whose bookings these belong to. */
    private Rig rig;
    
    /** The list of booking slots of this rig. */
    private MBooking[] slots;
    
    /** Next rig to attempt load balancing to. */
    private RigBookings typeNext;
    
    /** Next request capabilities to load balance. */
    private Map<RequestCapabilities, RigBookings> capsNext;
    
    /** Logger. */
    private Logger logger;
    
    public RigBookings(Rig rig, String day)
    {
        this.logger = LoggerActivator.getLogger();
        
        this.rig = rig;
        this.dayKey = day;
        
        this.slots = new MBooking[(24 * 60 * 60) / BookingActivator.TIME_QUANTUM];
    }
    
    
    /** 
     * Returns true if the number of rig slots free.
     * 
     * @param start the start slot
     * @param num number of slots
     * @return true if slots are free
     */
    public boolean areSlotsFree(int start, int num)
    {
        int end = start + num;
        List<RigBookings> checked = new ArrayList<RigBookings>();
        
        for ( ; start < end; start++)
        {
            /* Free slot. */
            if (this.slots[start] == null) continue;
            
            checked.add(this);
            
            MBooking mb = this.slots[start];
            boolean canBalance = false;
            switch (mb.getType())
            {
                case RIG:
                    /* If booking is a rig booking, it cannot be moved. */
                    return false;
                    
                case TYPE:
                    /* If a booking is a type booking, it may be moved to 
                     * another type rig, so circle through the type rigs to
                     * try and load balance. */
                    RigBookings nextType = this.typeNext;
                    while (nextType != this)
                    {
                        if (nextType.canBalanceTypeSlots(mb.getStartSlot(), mb.getNumSlots(), checked))
                        {
                            canBalance = true;
                            break;
                        }
                        nextType = nextType.getTypeNext();
                    }
                    
                    if (!canBalance) return false;
                        
                    break;
                case CAPABILITY:
                    /* If a booking is a capability booking, it may be moved
                     * to another matching rig, so circle through the capability
                     * rigs to try and free up some space. */
                    RigBookings nextCaps = this.capsNext.get(mb.getRequestCaps());
                    while (nextCaps != this)
                    {
                        if (nextCaps.canBalanceCapabilitySlots(mb.getRequestCaps(), mb.getStartSlot(), mb.getNumSlots()
                                , checked))
                        {
                            canBalance = true;
                            break;
                        }
                        nextCaps = nextCaps.getCapsNext(mb.getRequestCaps());
                    }
            }
            
            start = mb.getEndSlot() + 1;
            checked.clear();
        }
        
        return true;
    }
    
    /**
     * Returns true if a type booking can be balanced to this rig. 
     * 
     * @param start start slot
     * @param num number of slots
     * @param checked rigs that have already been excluding for satisfying these slots
     * @return true if can balance
     */
    public boolean canBalanceTypeSlots(int start, int num, List<RigBookings> checked)
    {
        if (checked.contains(this)) return false;
        checked.add(this);
        
        int end = start + num;
        for ( ; start < end ; start++)
        {
            /* Free slot. */
            if (this.slots[start] == null) continue;
            
            MBooking mb = this.slots[start];
            if (mb.getType() == RIG || mb.getType() == TYPE) 
            {
                /* In the context of load balancing a type booking, a type
                 * cannot be moved. */
                return false;
            }
            else if (!this.canBalanceCapabilitySlots(mb.getRequestCaps(), start, num, checked))
            {
                /* Can't move the capability booking so all in use. */
                return false;
            }
            start = mb.getEndSlot() + 1;
        }
        
        return true;
    }
    
    /**
     * Returns true if a capability booking can be balanced to this rig.
     * 
     * @param caps request capabilities
     * @param start slot start
     * @param num number of slots
     * @return true if it can be balanced
     */
    public boolean canBalanceCapabilitySlots(RequestCapabilities caps, int start, int num, List<RigBookings> checked)
    {   
        if (checked.contains(this)) return false;
        checked.add(this);
        
        int end = start + num;
        for ( ; start < end; start++)
        {
            if (this.slots[start] != null) return false;
        }
        
        return true;
    }
    
    /**
     * Returns the free slots in the form &lt;start slot&;gt; 
     * @return
     */
    public int[][] getFreeSlots()
    {
        return null;
    }
    
    /**
     * 
     * 
     * @param booking
     * @return
     */
    public boolean commitBooking(MBooking booking)
    {
        return false;
    }
    
    /**
     * Removes a booking from a rig.
     * 
     * @return
     */
    public boolean removeBooking(MBooking booking)
    {
        
        
        return false;
    }
    
    /**
     * 
     * @param caps request capabilities
     * @return caps next 
     */
    public RigBookings getCapsNext(RequestCapabilities caps)
    {
        return this.capsNext.get(caps);
    }
    
    /**
     * Puts the next hop in a request capabilities resource circle.
     * 
     * @param caps request capabilities.
     * @param next next circle hop
     */
    public void putCapsNext(RequestCapabilities caps, RigBookings next)
    {
        this.capsNext.put(caps, next);
    }
    
    /**
     * Gets the next hop in the rig type resource circle.
     * 
     * @return type next 
     */
    public RigBookings getTypeNext()
    {
        return this.typeNext;
    }
    
    /** 
     * Sets the next hop in the rig type resource circle.
     * 
     * @param next next hop in hop
     */
    public void setTypeNext(RigBookings next)
    {
        this.typeNext = next;
    }
}   
