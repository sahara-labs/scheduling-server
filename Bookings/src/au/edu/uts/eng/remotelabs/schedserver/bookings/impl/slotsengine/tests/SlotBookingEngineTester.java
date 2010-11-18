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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.BookingEngine.TimePeriod;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine.SlotBookingEngine;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine.TimeUtil;
import au.edu.uts.eng.remotelabs.schedserver.logger.impl.SystemErrLogger;

/**
 * Tests the {@link SlotBookingEngine} class.
 */
public class SlotBookingEngineTester extends TestCase
{
    /** Object of class under test. */
    private SlotBookingEngine engine;

    @Override
    @Before
    public void setUp() throws Exception
    {
        this.engine = new SlotBookingEngine(); 
        Field f = SlotBookingEngine.class.getDeclaredField("logger");
        f.setAccessible(true);
        f.set(this.engine, new SystemErrLogger());
        this.engine.init();
    }

    @Test
    public void testGetFreeTimesRigTimePeriodInt()
    {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public void testGetFreeTimesRigTypeTimePeriodInt()
    {
        fail("Not yet implemented"); // TODO
    }

    @Test
    public void testGetFreeTimesRequestCapabilitiesTimePeriodInt()
    {
        fail("Not yet implemented"); // TODO
    }

    @SuppressWarnings("unchecked")
    public void testGetDayKeys() throws Exception
    {
        Calendar cal = Calendar.getInstance();
        TimePeriod period = new TimePeriod(cal, cal);
        
        Method m = SlotBookingEngine.class.getDeclaredMethod("getDayKeys", TimePeriod.class);
        m.setAccessible(true);
        List<String> keys = (List<String>) m.invoke(this.engine, period);
        assertNotNull(keys);
        assertEquals(1, keys.size());
        assertEquals(TimeUtil.getDateStr(cal), keys.get(0));
    }
    
    @SuppressWarnings("unchecked")
    public void testGetDayKeysTwoDays() throws Exception
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Calendar next = Calendar.getInstance();
        next.add(Calendar.DAY_OF_MONTH, 1);
        next.set(Calendar.HOUR_OF_DAY, 23);
        next.set(Calendar.MINUTE, 59);
        next.set(Calendar.SECOND, 59);
        TimePeriod period = new TimePeriod(cal, next);
        
        Method m = SlotBookingEngine.class.getDeclaredMethod("getDayKeys", TimePeriod.class);
        m.setAccessible(true);
        List<String> keys = (List<String>) m.invoke(this.engine, period);
        assertNotNull(keys);
        assertEquals(2, keys.size());
        assertEquals(TimeUtil.getDateStr(cal), keys.get(0));
        cal.add(Calendar.DAY_OF_MONTH, 1);
        assertEquals(TimeUtil.getDateStr(cal), keys.get(1));
    }
    
    @SuppressWarnings("unchecked")
    public void testGetDayKeysFiveDays() throws Exception
    {
        Calendar cal = Calendar.getInstance();
        Calendar next = Calendar.getInstance();
        next.add(Calendar.DAY_OF_MONTH, 4);
        TimePeriod period = new TimePeriod(cal, next);
        
        Method m = SlotBookingEngine.class.getDeclaredMethod("getDayKeys", TimePeriod.class);
        m.setAccessible(true);
        List<String> keys = (List<String>) m.invoke(this.engine, period);
        assertNotNull(keys);
        assertEquals(5, keys.size());
        assertEquals(TimeUtil.getDateStr(cal), keys.get(0));
        cal.add(Calendar.DAY_OF_MONTH, 1);
        assertEquals(TimeUtil.getDateStr(cal), keys.get(1));
                cal.add(Calendar.DAY_OF_MONTH, 1);
        assertEquals(TimeUtil.getDateStr(cal), keys.get(2));
                cal.add(Calendar.DAY_OF_MONTH, 1);
        assertEquals(TimeUtil.getDateStr(cal), keys.get(3));
        cal.add(Calendar.DAY_OF_MONTH, 1);
        assertEquals(TimeUtil.getDateStr(cal), keys.get(4));
    }
}
