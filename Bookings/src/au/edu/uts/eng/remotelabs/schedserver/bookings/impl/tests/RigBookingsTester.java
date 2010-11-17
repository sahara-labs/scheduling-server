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
package au.edu.uts.eng.remotelabs.schedserver.bookings.impl.tests;


import java.lang.reflect.Field;

import junit.framework.TestCase;

import org.junit.Before;

import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.MBooking;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.MBooking.BType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.RigBookings;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.logger.impl.SystemErrLogger;

/**
 * Tests the {@link RigBookings} class.
 */
public class RigBookingsTester extends TestCase
{
    /** Object of class under test. */
    private RigBookings bookings;

    @Override
    @Before
    public void setUp() throws Exception
    {
        this.bookings = new RigBookings(new Rig(), "11-12-2010");
        Field f = RigBookings.class.getDeclaredField("logger");
        f.setAccessible(true);
        f.set(this.bookings, new SystemErrLogger());
    }
    
    public void testAreSlotsFree() throws Exception
    {
        assertTrue(this.bookings.areSlotsFree(0, 96));
        
        Field f = RigBookings.class.getDeclaredField("slots");
        f.setAccessible(true);
        MBooking slots[] = (MBooking[])f.get(this.bookings);
        assertEquals(96, slots.length);
    }

    public void testAreSlotsFreeOneBookings() throws Throwable
    {
        Field f = RigBookings.class.getDeclaredField("slots");
        f.setAccessible(true);
        MBooking slots[] = (MBooking[]) f.get(this.bookings);
        
        MBooking m = new MBooking(0, 48, BType.RIG);
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
        
        MBooking m = new MBooking(5, 10, BType.RIG);
        for (int i = 5; i <= 10; i++)
        {
           slots[i] = m;
        }
        
        m = new MBooking(15, 25, BType.RIG);
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
        
        MBooking m = new MBooking(5, 10, BType.RIG);
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
        
        m = new MBooking(10, 15, BType.RIG);
        assertFalse(this.bookings.commitBooking(m));
        
        m = new MBooking(1, 3, BType.RIG);
        assertTrue(this.bookings.commitBooking(m));
        assertEquals(2, this.bookings.getNumBookings());
        
        f = RigBookings.class.getDeclaredField("startSlot");
        f.setAccessible(true);
        assertEquals(1, f.getInt(this.bookings));
        f = RigBookings.class.getDeclaredField("endSlot");
        f.setAccessible(true);
        assertEquals(10, f.getInt(this.bookings));
        
        m = new MBooking(15, 20, BType.RIG);
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
            MBooking m = new MBooking(i * 4, i * 4 + 3, BType.RIG);
            assertTrue(this.bookings.commitBooking(m));
        }
        
        for (int i = 0; i < slots.length; i++)
        {
            assertNotNull(slots[i]);
        }
    }
}
