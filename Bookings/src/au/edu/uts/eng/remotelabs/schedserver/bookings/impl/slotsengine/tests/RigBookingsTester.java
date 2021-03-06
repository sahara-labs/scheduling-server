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
 * @date 17th November 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine.tests;


import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;

import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine.MBooking;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine.MRange;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine.RigBookings;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine.TimeUtil;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine.MBooking.BType;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Bookings;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RequestCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
import au.edu.uts.eng.remotelabs.schedserver.logger.impl.SystemErrLogger;

/**
 * Tests the {@link RigBookings} class.
 */
public class RigBookingsTester extends TestCase
{
    /** Object of class under test. */
    private RigBookings bookings;
    
    /** Day key. */
    private String dayKey;

    @Override
    @Before
    public void setUp() throws Exception
    {
        Rig r = new Rig();
        RigType rt = new RigType();
        r.setRigType(rt);
        
        this.bookings = new RigBookings(r, TimeUtil.getDayKey(new Date()));
        Field f = RigBookings.class.getDeclaredField("logger");
        f.setAccessible(true);
        f.set(this.bookings, new SystemErrLogger());
        
        this.dayKey = TimeUtil.getDayKey(Calendar.getInstance());
    }
    
    public void testGetTypeBookings() throws Throwable
    {
        Field f = RigBookings.class.getDeclaredField("slots");
        f.setAccessible(true);
        MBooking slots[] = (MBooking[]) f.get(this.bookings);
        
        MBooking m = new MBooking(10, 20, BType.RIG, this.dayKey);
        for (int i = 10; i <= 20; i++)
        {
            slots[i] = m;
        }
        
        MBooking mb = new MBooking(30, 40, BType.TYPE, this.dayKey);
        for (int i = 30; i <= 40; i++)
        {
            slots[i] = mb;
        }
        
        m = new MBooking(50, 70, BType.RIG, this.dayKey);
        for (int i = 50; i <= 70; i++)
        {
            slots[i] = m;
        }
        
        
        f = RigBookings.class.getDeclaredField("startSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 10);
        f = RigBookings.class.getDeclaredField("endSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 70);
        f = RigBookings.class.getDeclaredField("numBookings");
        f.setAccessible(true);
        f.setInt(this.bookings, 3);
        
        List<MBooking> tb = this.bookings.getTypeBookings();
        assertNotNull(tb);
        assertEquals(1, tb.size());
        assertEquals(tb.get(0).getType(), BType.TYPE);
        assertEquals(tb.get(0).getStartSlot(), 30);
        assertEquals(tb.get(0).getEndSlot(), 40);
        assertEquals(tb.get(0), mb);
    }
    
    public void testGetTypeBookingsNoRig() throws Throwable
    {
        Field f = RigBookings.class.getDeclaredField("slots");
        f.setAccessible(true);
        MBooking slots[] = (MBooking[]) f.get(this.bookings);
        
        MBooking m = new MBooking(10, 20, BType.RIG, this.dayKey);
        for (int i = 10; i <= 20; i++)
        {
            slots[i] = m;
        }
        
        m = new MBooking(30, 40, BType.RIG, this.dayKey);
        for (int i = 30; i <= 40; i++)
        {
            slots[i] = m;
        }
        
        m = new MBooking(50, 70, BType.RIG, this.dayKey);
        for (int i = 50; i <= 70; i++)
        {
            slots[i] = m;
        }
        
        
        f = RigBookings.class.getDeclaredField("startSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 10);
        f = RigBookings.class.getDeclaredField("endSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 70);
        f = RigBookings.class.getDeclaredField("numBookings");
        f.setAccessible(true);
        f.setInt(this.bookings, 3);
        
        List<MBooking> tb = this.bookings.getTypeBookings();
        assertNotNull(tb);
        assertEquals(0, tb.size());
    }
    
    public void testGetTypeBookingsNoBookings() throws Throwable
    {
        List<MBooking> tb = this.bookings.getTypeBookings();
        assertNotNull(tb);
        assertEquals(0, tb.size());
    }
    
    public void testGetCapsBookingsS() throws Throwable
    {
        Field f = RigBookings.class.getDeclaredField("slots");
        f.setAccessible(true);
        MBooking slots[] = (MBooking[]) f.get(this.bookings);
        
        MBooking m = new MBooking(10, 20, BType.RIG, this.dayKey);
        for (int i = 10; i <= 20; i++)
        {
            slots[i] = m;
        }
        
        MBooking mb = new MBooking(30, 40, BType.TYPE, this.dayKey);
        for (int i = 30; i <= 40; i++)
        {
            slots[i] = mb;
        }
        
        m = new MBooking(50, 70, BType.CAPABILITY, this.dayKey);
        f = MBooking.class.getDeclaredField("reqCaps");
        f.setAccessible(true);
        f.set(m, new RequestCapabilities("foo,foo"));
        for (int i = 50; i <= 70; i++)
        {
            slots[i] = m;
        }
        
        
        f = RigBookings.class.getDeclaredField("startSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 10);
        f = RigBookings.class.getDeclaredField("endSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 70);
        f = RigBookings.class.getDeclaredField("numBookings");
        f.setAccessible(true);
        f.setInt(this.bookings, 3);
        
        List<MBooking> tb = this.bookings.getCapabilitiesBookings("foo,foo");
        assertNotNull(tb);
        assertEquals(1, tb.size());
        assertEquals(tb.get(0).getType(), BType.CAPABILITY);
        assertEquals(tb.get(0).getStartSlot(), 50);
        assertEquals(tb.get(0).getEndSlot(), 70);
        assertEquals(tb.get(0), m);
    }
    
    public void testGetCapsBookingsTwoS() throws Throwable
    {
        Field f = RigBookings.class.getDeclaredField("slots");
        f.setAccessible(true);
        MBooking slots[] = (MBooking[]) f.get(this.bookings);
        
        MBooking m = new MBooking(10, 20, BType.CAPABILITY, this.dayKey);
        f = MBooking.class.getDeclaredField("reqCaps");
        f.setAccessible(true);
        f.set(m, new RequestCapabilities("foo,bar"));
        for (int i = 10; i <= 20; i++)
        {
            slots[i] = m;
        }
        
        MBooking mb = new MBooking(30, 40, BType.CAPABILITY, this.dayKey);
        f = MBooking.class.getDeclaredField("reqCaps");
        f.setAccessible(true);
        f.set(mb, new RequestCapabilities("foo,foo"));
        for (int i = 30; i <= 40; i++)
        {
            slots[i] = mb;
        }
        
        m = new MBooking(50, 70, BType.CAPABILITY, this.dayKey);
        f = MBooking.class.getDeclaredField("reqCaps");
        f.setAccessible(true);
        f.set(m, new RequestCapabilities("foo,foo"));
        for (int i = 50; i <= 70; i++)
        {
            slots[i] = m;
        }
        
        
        f = RigBookings.class.getDeclaredField("startSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 10);
        f = RigBookings.class.getDeclaredField("endSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 70);
        f = RigBookings.class.getDeclaredField("numBookings");
        f.setAccessible(true);
        f.setInt(this.bookings, 3);
        
        List<MBooking> tb = this.bookings.getCapabilitiesBookings("foo,foo");
        assertNotNull(tb);
        assertEquals(2, tb.size());
        assertEquals(tb.get(0).getType(), BType.CAPABILITY);
        assertEquals(tb.get(0).getStartSlot(), 30);
        assertEquals(tb.get(0).getEndSlot(), 40);
        assertEquals(tb.get(0), mb);
        assertEquals(tb.get(1).getType(), BType.CAPABILITY);
        assertEquals(tb.get(1).getStartSlot(), 50);
        assertEquals(tb.get(1).getEndSlot(), 70);
        assertEquals(tb.get(1), m);
    }
    
    public void testGetCapsBookings() throws Throwable
    {
        Field f = RigBookings.class.getDeclaredField("slots");
        f.setAccessible(true);
        MBooking slots[] = (MBooking[]) f.get(this.bookings);
        
        MBooking m = new MBooking(10, 20, BType.RIG, this.dayKey);
        for (int i = 10; i <= 20; i++)
        {
            slots[i] = m;
        }
        
        MBooking mb = new MBooking(30, 40, BType.TYPE, this.dayKey);
        for (int i = 30; i <= 40; i++)
        {
            slots[i] = mb;
        }
        
        m = new MBooking(50, 70, BType.CAPABILITY, this.dayKey);
        f = MBooking.class.getDeclaredField("reqCaps");
        f.setAccessible(true);
        f.set(m, new RequestCapabilities("foo,foo"));
        for (int i = 50; i <= 70; i++)
        {
            slots[i] = m;
        }
        
        
        f = RigBookings.class.getDeclaredField("startSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 10);
        f = RigBookings.class.getDeclaredField("endSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 70);
        f = RigBookings.class.getDeclaredField("numBookings");
        f.setAccessible(true);
        f.setInt(this.bookings, 3);
        
        List<MBooking> tb = this.bookings.getCapabilitiesBookings(new RequestCapabilities("foo,foo"));
        assertNotNull(tb);
        assertEquals(1, tb.size());
        assertEquals(tb.get(0).getType(), BType.CAPABILITY);
        assertEquals(tb.get(0).getStartSlot(), 50);
        assertEquals(tb.get(0).getEndSlot(), 70);
        assertEquals(tb.get(0), m);
    }
    
    public void testGetTypeCapsBookingsNoBookings() throws Throwable
    {
        List<MBooking> tb = this.bookings.getCapabilitiesBookings(new RequestCapabilities("foo,bar,baz"));
        assertNotNull(tb);
        assertEquals(0, tb.size());
    }
    
    public void testGetTypeCapsBookingsNoBookingsS() throws Throwable
    {
        List<MBooking> tb = this.bookings.getCapabilitiesBookings("foo,bar,baz");
        assertNotNull(tb);
        assertEquals(0, tb.size());
    }
    
    public void testGetSlotBooking() throws Throwable
    {
        Field f = RigBookings.class.getDeclaredField("slots");
        f.setAccessible(true);
        MBooking slots[] = (MBooking[]) f.get(this.bookings);
        
        MBooking m = new MBooking(76, 77, BType.RIG, this.dayKey);
        slots[76] = m;
        slots[77] = m;
        
        f = RigBookings.class.getDeclaredField("startSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 76);
        f = RigBookings.class.getDeclaredField("endSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 77);
        f = RigBookings.class.getDeclaredField("numBookings");
        f.setAccessible(true);
        f.setInt(this.bookings, 1);
        
        for (int i = 0; i < 76; i++)
        {
            assertNull(this.bookings.getSlotBooking(i));
        }
        
        assertEquals(m, this.bookings.getSlotBooking(76));
        assertEquals(m, this.bookings.getSlotBooking(77));
        
        for (int i = 78; i < 96; i++)
        {
            assertNull(this.bookings.getSlotBooking(i));
        }
    }
    
    public void testGetFreeSlots() throws Throwable
    {
        Field f = RigBookings.class.getDeclaredField("slots");
        f.setAccessible(true);
        MBooking slots[] = (MBooking[]) f.get(this.bookings);
        
        MBooking m = new MBooking(76, 77, BType.RIG, this.dayKey);
        slots[76] = m;
        slots[77] = m;
        
        f = RigBookings.class.getDeclaredField("startSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 76);
        f = RigBookings.class.getDeclaredField("endSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 77);
        f = RigBookings.class.getDeclaredField("numBookings");
        f.setAccessible(true);
        f.setInt(this.bookings, 1);
        
        List<MRange> range = this.bookings.getFreeSlots(72, 78, 1);
        assertNotNull(range);
        assertEquals(2, range.size());
        assertEquals(range.get(0).getStartSlot(), 72);
        assertEquals(range.get(0).getEndSlot(), 75);
        assertEquals(range.get(1).getStartSlot(), 78);
        assertEquals(range.get(1).getEndSlot(), 78);
    }
    
    public void testGetEarlyFit() throws Throwable
    {
        Field f = RigBookings.class.getDeclaredField("slots");
        f.setAccessible(true);
        MBooking slots[] = (MBooking[]) f.get(this.bookings);
        
        MBooking m = new MBooking(10, 20, BType.RIG, this.dayKey);
        for (int i = 10; i <= 20; i++)
        {
            slots[i] = m;
        }
        
        m = new MBooking(30, 40, BType.RIG, this.dayKey);
        for (int i = 30; i <= 40; i++)
        {
            slots[i] = m;
        }
        
        m = new MBooking(50, 70, BType.RIG, this.dayKey);
        for (int i = 50; i <= 70; i++)
        {
            slots[i] = m;
        }
        
        
        f = RigBookings.class.getDeclaredField("startSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 10);
        f = RigBookings.class.getDeclaredField("endSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 70);
        f = RigBookings.class.getDeclaredField("numBookings");
        f.setAccessible(true);
        f.setInt(this.bookings, 3);
        
        MBooking mb = new MBooking(10, 15, BType.RIG, this.dayKey);
        
        MRange es = this.bookings.getEarlyFit(mb);
        assertNotNull(es);
        assertEquals(4, es.getStartSlot());
        assertEquals(9, es.getEndSlot());
        assertEquals(6, es.getNumSlots());
    }
    
    public void testGetEarlyFit2() throws Throwable
    {
        Field f = RigBookings.class.getDeclaredField("slots");
        f.setAccessible(true);
        MBooking slots[] = (MBooking[]) f.get(this.bookings);
        
        MBooking m = new MBooking(10, 26, BType.RIG, this.dayKey);
        for (int i = 10; i <= 26; i++)
        {
            slots[i] = m;
        }
        
        m = new MBooking(30, 40, BType.RIG, this.dayKey);
        for (int i = 30; i <= 40; i++)
        {
            slots[i] = m;
        }
        
        m = new MBooking(50, 70, BType.RIG, this.dayKey);
        for (int i = 50; i <= 70; i++)
        {
            slots[i] = m;
        }
        
        
        f = RigBookings.class.getDeclaredField("startSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 10);
        f = RigBookings.class.getDeclaredField("endSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 70);
        f = RigBookings.class.getDeclaredField("numBookings");
        f.setAccessible(true);
        f.setInt(this.bookings, 3);
        
        MBooking mb = new MBooking(27, 30, BType.RIG, this.dayKey);
        
        MRange es = this.bookings.getEarlyFit(mb);
        assertNotNull(es);
        assertEquals(27, es.getStartSlot());
        assertEquals(29, es.getEndSlot());
        assertEquals(3, es.getNumSlots());
    }
    
    public void testGetEarlyFit3() throws Throwable
    {
        Field f = RigBookings.class.getDeclaredField("slots");
        f.setAccessible(true);
        MBooking slots[] = (MBooking[]) f.get(this.bookings);
        
        MBooking m = new MBooking(10, 29, BType.RIG, this.dayKey);
        for (int i = 10; i <= 29; i++)
        {
            slots[i] = m;
        }
        
        m = new MBooking(30, 49, BType.RIG, this.dayKey);
        for (int i = 30; i <= 49; i++)
        {
            slots[i] = m;
        }
        
        m = new MBooking(51, 70, BType.RIG, this.dayKey);
        for (int i = 51; i <= 70; i++)
        {
            slots[i] = m;
        }
        
        
        f = RigBookings.class.getDeclaredField("startSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 10);
        f = RigBookings.class.getDeclaredField("endSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 70);
        f = RigBookings.class.getDeclaredField("numBookings");
        f.setAccessible(true);
        f.setInt(this.bookings, 3);
        
        MBooking mb = new MBooking(50, 54, BType.RIG, this.dayKey);
        
        MRange es = this.bookings.getEarlyFit(mb);
        assertNotNull(es);
        assertEquals(5, es.getStartSlot());
        assertEquals(9, es.getEndSlot());
        assertEquals(mb.getNumSlots(), es.getNumSlots());
    }
    
    public void testGetLateFit() throws Throwable
    {
        Field f = RigBookings.class.getDeclaredField("slots");
        f.setAccessible(true);
        MBooking slots[] = (MBooking[]) f.get(this.bookings);
        
        MBooking m = new MBooking(10, 20, BType.RIG, this.dayKey);
        for (int i = 10; i <= 20; i++)
        {
            slots[i] = m;
        }
        
        m = new MBooking(30, 40, BType.RIG, this.dayKey);
        for (int i = 30; i <= 40; i++)
        {
            slots[i] = m;
        }
        
        m = new MBooking(50, 70, BType.RIG, this.dayKey);
        for (int i = 50; i <= 70; i++)
        {
            slots[i] = m;
        }
        
        
        f = RigBookings.class.getDeclaredField("startSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 10);
        f = RigBookings.class.getDeclaredField("endSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 70);
        f = RigBookings.class.getDeclaredField("numBookings");
        f.setAccessible(true);
        f.setInt(this.bookings, 3);
        
        MBooking mb = new MBooking(10, 15, BType.RIG, this.dayKey);
        
        MRange es = this.bookings.getLateFit(mb);
        assertNotNull(es);
        assertEquals(21, es.getStartSlot());
        assertEquals(26, es.getEndSlot());
        assertEquals(6, es.getNumSlots());
    }
    
    public void testGetLateFit2() throws Throwable
    {
        Field f = RigBookings.class.getDeclaredField("slots");
        f.setAccessible(true);
        MBooking slots[] = (MBooking[]) f.get(this.bookings);
        
        MBooking m = new MBooking(10, 26, BType.RIG, this.dayKey);
        for (int i = 10; i <= 26; i++)
        {
            slots[i] = m;
        }
        
        m = new MBooking(30, 40, BType.RIG, this.dayKey);
        for (int i = 30; i <= 40; i++)
        {
            slots[i] = m;
        }
        
        m = new MBooking(50, 70, BType.RIG, this.dayKey);
        for (int i = 50; i <= 70; i++)
        {
            slots[i] = m;
        }
        
        
        f = RigBookings.class.getDeclaredField("startSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 10);
        f = RigBookings.class.getDeclaredField("endSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 70);
        f = RigBookings.class.getDeclaredField("numBookings");
        f.setAccessible(true);
        f.setInt(this.bookings, 3);
        
        MBooking mb = new MBooking(26, 30, BType.RIG, this.dayKey);
        
        MRange es = this.bookings.getLateFit(mb);
        assertNotNull(es);
        assertEquals(27, es.getStartSlot());
        assertEquals(29, es.getEndSlot());
        assertEquals(3, es.getNumSlots());
    }
    
    public void testGetLateFit3() throws Throwable
    {
        Field f = RigBookings.class.getDeclaredField("slots");
        f.setAccessible(true);
        MBooking slots[] = (MBooking[]) f.get(this.bookings);
        
        MBooking m = new MBooking(10, 29, BType.RIG, this.dayKey);
        for (int i = 10; i <= 29; i++)
        {
            slots[i] = m;
        }
        
        m = new MBooking(30, 49, BType.RIG, this.dayKey);
        for (int i = 30; i <= 49; i++)
        {
            slots[i] = m;
        }
        
        m = new MBooking(51, 70, BType.RIG, this.dayKey);
        for (int i = 51; i <= 70; i++)
        {
            slots[i] = m;
        }
        
        
        f = RigBookings.class.getDeclaredField("startSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 10);
        f = RigBookings.class.getDeclaredField("endSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 70);
        f = RigBookings.class.getDeclaredField("numBookings");
        f.setAccessible(true);
        f.setInt(this.bookings, 3);
        
        MBooking mb = new MBooking(50, 53, BType.RIG, this.dayKey);
        
        MRange es = this.bookings.getLateFit(mb);
        assertNotNull(es);
        assertEquals(71, es.getStartSlot());
        assertEquals(74, es.getEndSlot());
        assertEquals(4, es.getNumSlots());
    }
    
    public void testGetLateFit4() throws Throwable
    {
        Field f = RigBookings.class.getDeclaredField("slots");
        f.setAccessible(true);
        MBooking slots[] = (MBooking[]) f.get(this.bookings);
        
        MBooking m = new MBooking(10, 27, BType.RIG, this.dayKey);
        for (int i = 10; i <= 27; i++)
        {
            slots[i] = m;
        }
        
        m = new MBooking(30, 49, BType.RIG, this.dayKey);
        for (int i = 30; i <= 49; i++)
        {
            slots[i] = m;
        }
        
        m = new MBooking(51, 70, BType.RIG, this.dayKey);
        for (int i = 51; i <= 70; i++)
        {
            slots[i] = m;
        }
        
        
        f = RigBookings.class.getDeclaredField("startSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 10);
        f = RigBookings.class.getDeclaredField("endSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 70);
        f = RigBookings.class.getDeclaredField("numBookings");
        f.setAccessible(true);
        f.setInt(this.bookings, 3);
        
        MBooking mb = new MBooking(27, 30, BType.RIG, this.dayKey);
        
        MRange es = this.bookings.getLateFit(mb);
        assertNotNull(es);
        assertEquals(28, es.getStartSlot());
        assertEquals(29, es.getEndSlot());
        assertEquals(2, es.getNumSlots());
    }
    
    public void testGetFreeSlotsSequential() throws Throwable
    {
        Field f = RigBookings.class.getDeclaredField("slots");
        f.setAccessible(true);
        MBooking slots[] = (MBooking[]) f.get(this.bookings);
        
        for (int j = 0; j <= 6; j++)
        {
            MBooking m = new MBooking(j * 10, j * 10 + 9, BType.RIG, this.dayKey);
            for (int i = m.getStartSlot(); i <= m.getEndSlot(); i++)
            {
                slots[i] = m;
            }
        }
        
        f = RigBookings.class.getDeclaredField("startSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 0);
        f = RigBookings.class.getDeclaredField("endSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 69);
        f = RigBookings.class.getDeclaredField("numBookings");
        f.setAccessible(true);
        f.setInt(this.bookings, 7);
        
        List<MRange> range = this.bookings.getFreeSlots();
        assertEquals(1, range.size());
        MRange r = range.get(0);
        assertEquals(70, r.getStartSlot());
        assertEquals(95, r.getEndSlot());
    }
    
    public void testAreSlotsFree() throws Exception
    {
        assertTrue(this.bookings.areSlotsFree(0, 96));
        
        Field f = RigBookings.class.getDeclaredField("slots");
        f.setAccessible(true);
        MBooking slots[] = (MBooking[])f.get(this.bookings);
        assertEquals(96, slots.length);
        assertNull(this.bookings.getNextBooking(10));
    }

    public void testAreSlotsFreeOneBookings() throws Throwable
    {
        Field f = RigBookings.class.getDeclaredField("slots");
        f.setAccessible(true);
        MBooking slots[] = (MBooking[]) f.get(this.bookings);
        
        MBooking m = new MBooking(0, 48, BType.RIG, this.dayKey);
        for (int i = 0; i <= 48; i++)
        {
           slots[i] = m;
        }
        
        f = RigBookings.class.getDeclaredField("startSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 0);
        f = RigBookings.class.getDeclaredField("endSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 48);
        f = RigBookings.class.getDeclaredField("numBookings");
        f.setAccessible(true);
        f.setInt(this.bookings, 1);
        
        assertFalse(this.bookings.areSlotsFree(0, 48));
        assertFalse(this.bookings.areSlotsFree(47, 47));
        assertFalse(this.bookings.areSlotsFree(0, 10));
        assertFalse(this.bookings.areSlotsFree(0, 96));
        assertTrue(this.bookings.areSlotsFree(49, 96));
    }
    
    public void testAreSlotsFreeTwoBookings() throws Throwable
    {
        Field f = RigBookings.class.getDeclaredField("slots");
        f.setAccessible(true);
        MBooking slots[] = (MBooking[]) f.get(this.bookings);
        
        MBooking m = new MBooking(5, 10, BType.RIG, this.dayKey);
        for (int i = 5; i <= 10; i++)
        {
           slots[i] = m;
        }
        
        m = new MBooking(15, 25, BType.RIG, this.dayKey);
        for (int i = 15; i <= 25; i++)
        {
            slots[i] = m;
        }
        
        f = RigBookings.class.getDeclaredField("startSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 5);
        f = RigBookings.class.getDeclaredField("endSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 25);
        f = RigBookings.class.getDeclaredField("numBookings");
        f.setAccessible(true);
        f.setInt(this.bookings, 2);
        
        assertTrue(this.bookings.areSlotsFree(0, 4));
        assertFalse(this.bookings.areSlotsFree(5, 10));
        assertTrue(this.bookings.areSlotsFree(11, 14));
        assertFalse(this.bookings.areSlotsFree(15, 25));
        assertFalse(this.bookings.areSlotsFree(0, 96));
        assertTrue(this.bookings.areSlotsFree(26, 96));
    }
    
    public void testCommit() throws Throwable
    {
        Field f = RigBookings.class.getDeclaredField("slots");
        f.setAccessible(true);
        MBooking slots[] = (MBooking[]) f.get(this.bookings);
        
        MBooking m = new MBooking(5, 10, BType.RIG, this.dayKey);
        assertTrue(this.bookings.commitBooking(m));
        
        f = RigBookings.class.getDeclaredField("startSlot");
        f.setAccessible(true);
        assertEquals(5, f.getInt(this.bookings));
        f = RigBookings.class.getDeclaredField("endSlot");
        f.setAccessible(true);
        assertEquals(10, f.getInt(this.bookings));
        f = RigBookings.class.getDeclaredField("numBookings");
        f.setAccessible(true);
        assertEquals(1, f.getInt(this.bookings));
        
        int i = 0;
        for ( ; i <= 4; i++)
        {
            assertNull(slots[i]);
        }
        
        for ( ; i <= 10 ; i++)
        {
            assertNotNull(slots[i]);
            assertTrue(m == slots[i]);
        }
        
        for ( ; i < slots.length ; i++)
        {
            assertNull(slots[i]);
        }
        
        assertEquals(1, this.bookings.getNumBookings());
        assertTrue(this.bookings.areSlotsFree(0, 4));
        assertFalse(this.bookings.areSlotsFree(5, 10));
        assertTrue(this.bookings.areSlotsFree(11, 96));
        
        m = new MBooking(10, 15, BType.RIG, this.dayKey);
        assertFalse(this.bookings.commitBooking(m));
        
        m = new MBooking(1, 3, BType.RIG, this.dayKey);
        assertTrue(this.bookings.commitBooking(m));
        assertEquals(2, this.bookings.getNumBookings());
        
        f = RigBookings.class.getDeclaredField("startSlot");
        f.setAccessible(true);
        assertEquals(1, f.getInt(this.bookings));
        f = RigBookings.class.getDeclaredField("endSlot");
        f.setAccessible(true);
        assertEquals(10, f.getInt(this.bookings));
        
        m = new MBooking(15, 20, BType.RIG, this.dayKey);
        assertTrue(this.bookings.commitBooking(m));
        assertEquals(3, this.bookings.getNumBookings());
        
        f = RigBookings.class.getDeclaredField("startSlot");
        f.setAccessible(true);
        assertEquals(1, f.getInt(this.bookings));
        f = RigBookings.class.getDeclaredField("endSlot");
        f.setAccessible(true);
        assertEquals(20, f.getInt(this.bookings));
    }
    
    public void testCommitTwo() throws Exception
    {
        Field f = RigBookings.class.getDeclaredField("slots");
        f.setAccessible(true);
        MBooking slots[] = (MBooking[]) f.get(this.bookings);
        
        for (int i = 0; i < 24; i++)
        {
            MBooking m = new MBooking(i * 4, i * 4 + 3, BType.RIG, this.dayKey);
            assertTrue(this.bookings.commitBooking(m));
        }
        
        for (int i = 0; i < slots.length; i++)
        {
            assertNotNull(slots[i]);
        }
    }

    public void testRemove() throws Exception
    {
        Field f = RigBookings.class.getDeclaredField("slots");
        f.setAccessible(true);
        MBooking slots[] = (MBooking[]) f.get(this.bookings);
        
        Bookings b = new Bookings();
        b.setId(1L);
        
        
        MBooking m = new MBooking(5, 10, BType.RIG, this.dayKey);
        m.setBooking(b);
        
        for (int i = 5; i <= 10; i++)
        {
            slots[i] = m;
        }
        
        f = RigBookings.class.getDeclaredField("startSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 5);
        f = RigBookings.class.getDeclaredField("endSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 10);
        f = RigBookings.class.getDeclaredField("numBookings");
        f.setAccessible(true);
        f.setInt(this.bookings, 1);
        
        assertTrue(this.bookings.removeBooking(m));
        assertEquals(0, this.bookings.getNumBookings());
        for (int i = 0; i < slots.length; i++)
        {
            assertNull(slots[i]);
        }
    }

    public void testRemoveTwo() throws Exception
    {
        Field f = RigBookings.class.getDeclaredField("slots");
        f.setAccessible(true);
        MBooking slots[] = (MBooking[]) f.get(this.bookings);
        
        Bookings b = new Bookings();
        b.setId(1L);
        MBooking m = new MBooking(5, 10, BType.RIG, this.dayKey);
        m.setBooking(b);
        for (int i = 5; i <= 10; i++)
        {
            slots[i] = m;
        }
        
        Bookings b2 = new Bookings();
        b2.setId(2L);
        MBooking m2 = new MBooking(20,30, BType.RIG, this.dayKey);
        m2.setBooking(b2);
        for (int i = 20; i <= 30; i++)
        {
            slots[i] = m2;
        }
        
        Field ss = RigBookings.class.getDeclaredField("startSlot");
        ss.setAccessible(true);
        ss.setInt(this.bookings, 5);
        Field es = RigBookings.class.getDeclaredField("endSlot");
        es.setAccessible(true);
        es.setInt(this.bookings, 30);
        f = RigBookings.class.getDeclaredField("numBookings");
        f.setAccessible(true);
        f.setInt(this.bookings, 2);
        
        assertTrue(this.bookings.hasBooking(m));
        assertTrue(this.bookings.removeBooking(m));
        assertFalse(this.bookings.hasBooking(m));
        assertEquals(1, this.bookings.getNumBookings());
        assertEquals(20, ss.getInt(this.bookings));
        assertEquals(30, es.getInt(this.bookings));
        for (int i = 0; i < 20; i++) assertNull(slots[i]);
        for (int i = 20; i <= 30; i++) assertNotNull(slots[i]);
        for (int i = 31; i < slots.length; i++) assertNull(slots[i]);
        
        assertTrue(this.bookings.hasBooking(m2));
        assertTrue(this.bookings.removeBooking(m2));
        assertFalse(this.bookings.hasBooking(m2));
        assertEquals(0, this.bookings.getNumBookings());
        for (int i = 0; i < slots.length; i++) assertNull(slots[i]);
    }
    
    public void testGetFreeAll()
    {
        List<MRange> range = this.bookings.getFreeSlots();
        assertEquals(1, range.size());
        
        MRange r = range.get(0);
        assertEquals(0, r.getStartSlot());
        assertEquals(95, r.getEndSlot());
        
        
        Calendar start = r.getStart();
        Calendar cal = Calendar.getInstance();
        assertEquals(cal.get(Calendar.YEAR), start.get(Calendar.YEAR));
        assertEquals(cal.get(Calendar.MONTH), start.get(Calendar.MONTH));
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), start.get(Calendar.DAY_OF_MONTH));
        assertEquals(0, start.get(Calendar.HOUR));
        assertEquals(0, start.get(Calendar.MINUTE));
        assertEquals(0, start.get(Calendar.SECOND));
        
        Calendar end = r.getEnd();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        assertEquals(cal.get(Calendar.YEAR), end.get(Calendar.YEAR));
        assertEquals(cal.get(Calendar.MONTH), end.get(Calendar.MONTH));
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), end.get(Calendar.DAY_OF_MONTH));
        assertEquals(0, end.get(Calendar.HOUR));
        assertEquals(0, end.get(Calendar.MINUTE));
        assertEquals(0, end.get(Calendar.SECOND));
    }
    
    public void testGetFreeNone() throws Exception
    {
        Field f = RigBookings.class.getDeclaredField("slots");
        f.setAccessible(true);
        MBooking slots[] = (MBooking[])f.get(this.bookings);
        MBooking m = new MBooking(0, 95, BType.RIG, this.dayKey);
        for (int i = 0; i < slots.length; i++)
        {
            slots[i] = m;
        }
        f = RigBookings.class.getDeclaredField("startSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 0);
        f = RigBookings.class.getDeclaredField("endSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 95);
        f = RigBookings.class.getDeclaredField("numBookings");
        f.setAccessible(true);
        f.setInt(this.bookings, 1);
        
        List<MRange> range = this.bookings.getFreeSlots();
        assertNotNull(range);
        assertEquals(0, range.size());
        assertNotNull(this.bookings.getNextBooking(95));
    }
    
    public void testGetFreeTwoBookings() throws Exception
    {
        Field f = RigBookings.class.getDeclaredField("slots");
        f.setAccessible(true);
        MBooking slots[] = (MBooking[])f.get(this.bookings);
        
        MBooking m = new MBooking(10, 20, BType.RIG, this.dayKey);
        for (int i = 10; i <= 20; i++)
        {
            slots[i] = m;
        }
        
        m = new MBooking(30, 40, BType.RIG, this.dayKey);
        for (int i = 30; i <= 40; i++)
        {
            slots[i] = m;
        }
        
        
        f = RigBookings.class.getDeclaredField("startSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 10);
        f = RigBookings.class.getDeclaredField("endSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 40);
        f = RigBookings.class.getDeclaredField("numBookings");
        f.setAccessible(true);
        f.setInt(this.bookings, 2);
        
        List<MRange> range = this.bookings.getFreeSlots();
        assertNotNull(range);
        assertEquals(3, range.size());
        
        MRange r = range.get(0);
        assertEquals(0, r.getStartSlot());
        assertEquals(9, r.getEndSlot());
        assertEquals(10, this.bookings.getNextBooking(0).getStartSlot());
        
        r = range.get(1);
        assertEquals(21, r.getStartSlot());
        assertEquals(29, r.getEndSlot());
        
        r = range.get(2);
        assertEquals(41, r.getStartSlot());
        assertEquals(95, r.getEndSlot());
    }
    
    public void testGetFreeTwoBookings2() throws Exception
    {
        Field f = RigBookings.class.getDeclaredField("slots");
        f.setAccessible(true);
        MBooking slots[] = (MBooking[])f.get(this.bookings);
        
        MBooking m = new MBooking(0, 20, BType.RIG, this.dayKey);
        for (int i = 0; i <= 20; i++)
        {
            slots[i] = m;
        }
        
        m = new MBooking(90, 95, BType.RIG, this.dayKey);
        for (int i = 90; i <= 95; i++)
        {
            slots[i] = m;
        }
        
        
        f = RigBookings.class.getDeclaredField("startSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 0);
        f = RigBookings.class.getDeclaredField("endSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 95);
        f = RigBookings.class.getDeclaredField("numBookings");
        f.setAccessible(true);
        f.setInt(this.bookings, 2);
        
        List<MRange> range = this.bookings.getFreeSlots();
        assertNotNull(range);
        assertEquals(1, range.size());
        
        MRange r = range.get(0);
        assertEquals(21, r.getStartSlot());
        assertEquals(89, r.getEndSlot());
    }
    
    public void testGetFreeRange() throws Exception
    {
        Field f = RigBookings.class.getDeclaredField("slots");
        f.setAccessible(true);
        MBooking slots[] = (MBooking[])f.get(this.bookings);
        
        MBooking m = new MBooking(0, 20, BType.RIG, this.dayKey);
        for (int i = 0; i <= 20; i++)
        {
            slots[i] = m;
        }
        
        m = new MBooking(90, 95, BType.RIG, this.dayKey);
        for (int i = 90; i <= 95; i++)
        {
            slots[i] = m;
        }
        
        
        f = RigBookings.class.getDeclaredField("startSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 0);
        f = RigBookings.class.getDeclaredField("endSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 95);
        f = RigBookings.class.getDeclaredField("numBookings");
        f.setAccessible(true);
        f.setInt(this.bookings, 2);
        
        List<MRange> range = this.bookings.getFreeSlots(21, 89, 1);
        assertNotNull(range);
        assertEquals(1, range.size());
        
        MRange r = range.get(0);
        assertEquals(21, r.getStartSlot());
        assertEquals(89, r.getEndSlot());
    }
    
    public void testGetFreeRange2() throws Exception
    {
        Field f = RigBookings.class.getDeclaredField("slots");
        f.setAccessible(true);
        MBooking slots[] = (MBooking[])f.get(this.bookings);
        
        MBooking m = new MBooking(0, 20, BType.RIG, this.dayKey);
        for (int i = 0; i <= 20; i++)
        {
            slots[i] = m;
        }
        
        m = new MBooking(30, 40, BType.RIG, this.dayKey);
        for (int i = 30; i <= 40; i++)
        {
            slots[i] = m;
        }
        
        m = new MBooking(50, 70, BType.RIG, this.dayKey);
        for (int i = 50; i <= 70; i++)
        {
            slots[i] = m;
        }
        
        
        f = RigBookings.class.getDeclaredField("startSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 0);
        f = RigBookings.class.getDeclaredField("endSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 70);
        f = RigBookings.class.getDeclaredField("numBookings");
        f.setAccessible(true);
        f.setInt(this.bookings, 3);
        
        List<MRange> range = this.bookings.getFreeSlots(10, 60, 1);
        assertNotNull(range);
        assertEquals(2, range.size());
        
        MRange r = range.get(0);
        assertEquals(21, r.getStartSlot());
        assertEquals(29, r.getEndSlot());
        
        r = range.get(1);
        assertEquals(41, r.getStartSlot());
        assertEquals(49, r.getEndSlot());
    }
    
    public void testGetFreeRange3() throws Exception
    {
        Field f = RigBookings.class.getDeclaredField("slots");
        f.setAccessible(true);
        MBooking slots[] = (MBooking[])f.get(this.bookings);
        
        MBooking m = new MBooking(0, 20, BType.RIG, this.dayKey);
        for (int i = 0; i <= 20; i++)
        {
            slots[i] = m;
        }
        
        m = new MBooking(30, 40, BType.RIG, this.dayKey);
        for (int i = 30; i <= 40; i++)
        {
            slots[i] = m;
        }
        
        m = new MBooking(50, 60, BType.RIG, this.dayKey);
        for (int i = 50; i <= 70; i++)
        {
            slots[i] = m;
        }
        
        
        f = RigBookings.class.getDeclaredField("startSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 0);
        f = RigBookings.class.getDeclaredField("endSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 60);
        f = RigBookings.class.getDeclaredField("numBookings");
        f.setAccessible(true);
        f.setInt(this.bookings, 3);
        
        List<MRange> range = this.bookings.getFreeSlots(10, 70, 1);
        assertNotNull(range);
        assertEquals(3, range.size());
        
        MRange r = range.get(0);
        assertEquals(21, r.getStartSlot());
        assertEquals(29, r.getEndSlot());
        
        r = range.get(1);
        assertEquals(41, r.getStartSlot());
        assertEquals(49, r.getEndSlot());
        
        r = range.get(2);
        assertEquals(61, r.getStartSlot());
        assertEquals(70, r.getEndSlot());
    }
    
    public void testLots() throws Exception
    {
        Field f = RigBookings.class.getDeclaredField("slots");
        f.setAccessible(true);
        MBooking slots[] = (MBooking[])f.get(this.bookings);
        
        int i;
        for (i = 0; i < slots.length; i += 2)
        {
            slots[i] = new MBooking(i, i, BType.RIG, this.dayKey);
        }
        
        f = RigBookings.class.getDeclaredField("startSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 0);
        f = RigBookings.class.getDeclaredField("endSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 94);
        f = RigBookings.class.getDeclaredField("numBookings");
        f.setAccessible(true);
        f.setInt(this.bookings, 48);
        
        List<MRange> range = this.bookings.getFreeSlots();
        assertNotNull(range);
        assertEquals(48, range.size());
        
       for (MRange r : range)
       {
           assertTrue(r.getStartSlot() % 2 == 1);
           assertTrue(r.getStartSlot() == r.getEndSlot());
           assertFalse(r.getStart().equals(r.getEnd()));
           assertEquals(1, r.getNumSlots());
       }
    }
    
    public void testLotsThreshold() throws Exception
    {
        Field f = RigBookings.class.getDeclaredField("slots");
        f.setAccessible(true);
        MBooking slots[] = (MBooking[])f.get(this.bookings);

        int i, j = 0;
        for (i = 0; i < 49; i += 2)
        {
            slots[i] = new MBooking(i, i, BType.RIG, this.dayKey);
            j++;
        }
        
        for ( ; i < slots.length; i += 3)
        {
            slots[i] = new MBooking(i, i, BType.RIG, this.dayKey);
            j++;
        }

        f = RigBookings.class.getDeclaredField("startSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 0);
        f = RigBookings.class.getDeclaredField("endSlot");
        f.setAccessible(true);
        f.setInt(this.bookings, 95);
        f = RigBookings.class.getDeclaredField("numBookings");
        f.setAccessible(true);
        f.setInt(this.bookings, j);

        List<MRange> range = this.bookings.getFreeSlots(2);
        assertNotNull(range);
        assertEquals(15, range.size());
    }
}
