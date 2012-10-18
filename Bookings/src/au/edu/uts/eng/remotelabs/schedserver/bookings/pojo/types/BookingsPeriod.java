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
 * @date 30th January 2011
 */
package au.edu.uts.eng.remotelabs.schedserver.bookings.pojo.types;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.SlotState;


/**
 * Bookings period.
 */
public class BookingsPeriod
{
    /** Time designators. */
    public static final String NO_PERMISSION = SlotState._NOPERMISSION;
    public static final String BOOKED = SlotState._BOOKED;
    public static final String FREE = SlotState._FREE;
    
    /** List of slots. */
    private List<BookingSlot> slots;
    
    public BookingsPeriod()
    {
        this.slots = new ArrayList<BookingSlot>();
    }
    
    public void addSlot(Calendar start, Calendar end, String state)
    {
        BookingSlot slot = new BookingSlot();
        slot.start = start;
        slot.end = end;
        slot.state = state;
        this.slots.add(slot);
    }
    
    public List<BookingSlot> getSlots()
    {
        return this.slots;
    }
    
    /**
     * Booking slot.
     */
    public class BookingSlot
    {
        private Calendar start;
        private Calendar end;
        private String state;

        public Calendar getStart()
        {
            return this.start;
        }

        public Calendar getEnd()
        {
            return this.end;
        }

        public String getState()
        {
            return this.state;
        }
    }
}
