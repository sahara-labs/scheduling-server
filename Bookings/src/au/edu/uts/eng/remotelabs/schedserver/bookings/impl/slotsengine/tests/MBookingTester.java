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
package au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine.tests;

import java.util.Calendar;

import junit.framework.TestCase;

import org.junit.Test;

import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine.MBooking;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine.MBooking.BType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine.TimeUtil;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Bookings;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;

/**
 * Tests the {@link MBooking} class.
 */
public class MBookingTester extends TestCase
{
    @Test
    public void testMBooking()
    {
        Bookings b = new Bookings();
        b.setResourceType("RIG");
        
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 1);
        cal.set(Calendar.MINUTE, 15);
        cal.set(Calendar.SECOND, 0);
        b.setStartTime(cal.getTime());
        
        cal.add(Calendar.HOUR, 1);
        b.setEndTime(cal.getTime());
        
        MBooking m = new MBooking(b, TimeUtil.getDayKey(cal));
 
        assertEquals(b, m.getBooking());
        assertEquals(BType.RIG, m.getType());
        assertEquals(5, m.getStartSlot());
        assertEquals(8, m.getEndSlot());
        assertEquals(4, m.getNumSlots());
    }
    
    @Test
    public void testMBookingSession()
    {
        Session ses = new Session();
        ses.setDuration(1800);
        
        Calendar start = TimeUtil.getDayBegin(Calendar.getInstance());
        start.add(Calendar.MINUTE, 120);

        MBooking m = new MBooking(ses, new Rig(), start, TimeUtil.getDayKey(start));

        assertEquals(BType.RIG, m.getType());
        assertEquals(8, m.getStartSlot());
        assertEquals(9, m.getEndSlot());
        assertEquals(2, m.getNumSlots());
    }
    
    @Test
    public void testMBookingExtension()
    {
        Session ses = new Session();
        ses.setDuration(3600);
        
        Calendar start = TimeUtil.getDayBegin(Calendar.getInstance());
        start.add(Calendar.MINUTE, 120);

        MBooking m = new MBooking(ses, new Rig(), start, TimeUtil.getDayKey(start));
        m.extendBooking(3600);
        
        assertEquals(BType.RIG, m.getType());
        assertEquals(8, m.getStartSlot());
        assertEquals(16, m.getEndSlot());
    }
    
    @Test
    public void testMBookingExtension2()
    {
        Session ses = new Session();
        ses.setDuration(3600);
        
        Calendar start = TimeUtil.getDayBegin(Calendar.getInstance());
        start.add(Calendar.HOUR, 22);
        start.add(Calendar.MINUTE, 30);

        MBooking m = new MBooking(ses, new Rig(), start, TimeUtil.getDayKey(start));
        m.extendBooking(3600);
        
        assertEquals(BType.RIG, m.getType());
        assertEquals(90, m.getStartSlot());
        assertEquals(95, m.getEndSlot());
        assertEquals(6, m.getNumSlots());
        assertTrue(m.isMultiDay());
    }
    
    @Test
    public void testMBookingSession2()
    {
        Session ses = new Session();
        ses.setDuration(3700);
        
        Calendar start = TimeUtil.getDayBegin(Calendar.getInstance());
        start.add(Calendar.MINUTE, 119);

        MBooking m = new MBooking(ses, new Rig(), start, TimeUtil.getDayKey(start));

        assertEquals(BType.RIG, m.getType());
        assertEquals(7, m.getStartSlot());
        assertEquals(12, m.getEndSlot());
        assertEquals(6, m.getNumSlots());
    }
    
    @Test
    public void testMBookingC()
    {
        Bookings b = new Bookings();
        b.setResourceType("RIG");
        
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 13);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        b.setStartTime(cal.getTime());
        
        cal.add(Calendar.HOUR, 1);
        cal.add(Calendar.MINUTE, 30);
        b.setEndTime(cal.getTime());
        
        MBooking m = new MBooking(b, TimeUtil.getDayKey(cal));
 
        assertEquals(b, m.getBooking());
        assertEquals(BType.RIG, m.getType());
        assertEquals(52, m.getStartSlot());
        assertEquals(57, m.getEndSlot());
        assertEquals(6, m.getNumSlots());
    }
    
    @Test
    public void testMBooking2()
    {
        Bookings b = new Bookings();
        b.setResourceType("RIG");
        
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 1);
        cal.set(Calendar.MINUTE, 15);
        cal.set(Calendar.SECOND, 0);
        b.setStartTime(cal.getTime());
        
        cal.add(Calendar.HOUR, 1);
        cal.add(Calendar.MINUTE, 30);
        b.setEndTime(cal.getTime());
        
        MBooking m = new MBooking(b, TimeUtil.getDayKey(cal));
 
        assertEquals(b, m.getBooking());
        assertEquals(BType.RIG, m.getType());
        assertEquals(5, m.getStartSlot());
        assertEquals(10, m.getEndSlot());
        assertEquals(6, m.getNumSlots());
    }

    @Test
    public void testMBooking3()
    {
        Bookings b = new Bookings();
        b.setResourceType("RIG");
        
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 1);
        cal.set(Calendar.MINUTE, 15);
        cal.set(Calendar.SECOND, 0);
        b.setStartTime(cal.getTime());
        
        cal.add(Calendar.HOUR, 1);
        cal.add(Calendar.MINUTE, 30);
        cal.add(Calendar.SECOND, 1);
        b.setEndTime(cal.getTime());
        
        MBooking m = new MBooking(b, TimeUtil.getDayKey(cal));
 
        assertEquals(b, m.getBooking());
        assertEquals(BType.RIG, m.getType());
        assertEquals(5, m.getStartSlot());
        assertEquals(11, m.getEndSlot());
        assertEquals(7, m.getNumSlots());
    }
    
    @Test
    public void testMBooking4()
    {
        Bookings b = new Bookings();
        b.setResourceType("RIG");
        
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 1);
        cal.set(Calendar.MINUTE, 15);
        cal.set(Calendar.SECOND, 0);
        b.setStartTime(cal.getTime());
        
        cal.add(Calendar.HOUR, 1);
        cal.add(Calendar.MINUTE, 29);
        cal.add(Calendar.SECOND, 59);
        b.setEndTime(cal.getTime());
        
        MBooking m = new MBooking(b, TimeUtil.getDayKey(cal));
 
        assertEquals(b, m.getBooking());
        assertEquals(BType.RIG, m.getType());
        assertEquals(5, m.getStartSlot());
        assertEquals(10, m.getEndSlot());
        assertEquals(6, m.getNumSlots());
    }
}
