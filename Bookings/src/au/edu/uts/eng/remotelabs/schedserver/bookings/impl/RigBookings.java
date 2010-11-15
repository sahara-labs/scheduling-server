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

import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import au.edu.uts.eng.remotelabs.schedserver.bookings.BookingActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Bookings;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RequestCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;

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
    
    
    public RigBookings(Rig rig, String day, Session ses)
    {
        this.rig = rig;
        this.dayKey = day;
        
        this.slots = new MBooking[(24 * 60 * 60) / BookingActivator.TIME_QUANTUM];
        
        /* Load the rig bookings. */
        Criteria cri = ses.createCriteria(Bookings.class)
            .add(Restrictions.eq("active", Boolean.TRUE))
            .add(Restrictions.eq("resourceType", ResourcePermission.RIG_PERMISSION))
            .add(Restrictions.eq("rig", this.rig))
            .add(Restrictions.gt("endTime", TimeUtil.getDayBegin(this.dayKey)))
            .add(Restrictions.lt("startTime", TimeUtil.getDayEnd(this.dayKey)))
            .addOrder(Order.asc("id"));
        
        @SuppressWarnings("unchecked")
        List<Bookings> rigBookings = cri.list();
        for (Bookings b : rigBookings)
        {
            MBooking mb = new MBooking(b);
            
            /* Sanity check to ensure we are not over allocated. */
            if (this.areSlotsFree(mb.getStartSlot(), mb.getNumSlots()))
            {
                for (int i = mb.getStartSlot(); i <= mb.getEndSlot(); i++) this.slots[i] = mb;
            }
            else
            {
                /* We are somehow over-allocated, so terminate the booking. */
                b.setActive(false);
                b.setCancelReason("Rig over allocated.");
                ses.beginTransaction();
                ses.flush();
                ses.getTransaction().commit();
                
                // TODO Broadcast cancellation
            }
        }
    }
    
    public boolean commitBooking(MBooking booking)
    {
        return false;
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
     * Returns true if the number of slots free.
     * 
     * @param start the start slot
     * @param num number of slots
     * @return true if slots are free
     */
    public boolean areSlotsFree(int start, int num)
    {
        return false;
    }
    
    /**
     * 
     * 
     * @param caps
     * @param next
     */
    public void addCapsNext(RequestCapabilities caps, RigBookings next)
    {
        
    }
}   
