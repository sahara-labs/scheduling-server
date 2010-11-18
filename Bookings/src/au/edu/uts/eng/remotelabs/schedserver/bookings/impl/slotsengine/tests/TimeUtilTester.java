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
package au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine.tests;

import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import org.junit.Test;

import static java.util.Calendar.*;

import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine.TimeUtil;

/**
 * Tests the {@link TimeUtil} class.
 */
public class TimeUtilTester extends TestCase
{
    @Test
    public void testGetDateStr()
    {
        Date date = new Date();
        
        String dt = TimeUtil.getDateStr(date);
        assertNotNull(dt);
        String parts[] = dt.split("-");
        assertEquals(3, parts.length);
        
        Calendar cal = Calendar.getInstance();
        assertEquals(Integer.parseInt(parts[0]), cal.get(YEAR));
        assertEquals(Integer.parseInt(parts[1]), cal.get(MONTH));
        assertEquals(Integer.parseInt(parts[2]), cal.get(DAY_OF_MONTH));
    }
    
    @Test
    public void testGetDayBegin()
    {
        Date d = TimeUtil.getDayBegin(new Date());
        assertNotNull(d);
        
        Calendar now = Calendar.getInstance();
        Calendar start = Calendar.getInstance();
        start.setTime(d);
        assertEquals(0, start.get(HOUR_OF_DAY));
        assertEquals(0, start.get(MINUTE));
        assertEquals(0, start.get(SECOND));
        assertEquals(0, start.get(MILLISECOND));
        assertEquals(now.get(YEAR), start.get(YEAR));
        assertEquals(now.get(MONTH), start.get(MONTH));
        assertEquals(now.get(DAY_OF_MONTH), start.get(DAY_OF_MONTH));
    }

    @Test
    public void testGetDayEnd()
    {
        Date d = TimeUtil.getDayEnd(new Date());
        assertNotNull(d);
        
        Calendar now = Calendar.getInstance();
        Calendar start = Calendar.getInstance();
        start.setTime(d);
        assertEquals(23, start.get(HOUR_OF_DAY));
        assertEquals(59, start.get(MINUTE));
        assertEquals(59, start.get(SECOND));
        assertEquals(999, start.get(MILLISECOND));
        assertEquals(now.get(YEAR), start.get(YEAR));
        assertEquals(now.get(MONTH), start.get(MONTH));
        assertEquals(now.get(DAY_OF_MONTH), start.get(DAY_OF_MONTH));
    }
    
    public void testGetSlotIndex()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 14);
        cal.set(Calendar.SECOND, 59);
        
        assertEquals(0, TimeUtil.getSlotIndex(cal.getTime()));
    }
    
    public void testGetSlotIndex1()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 15);
        cal.set(Calendar.SECOND, 0);
        assertEquals(1, TimeUtil.getSlotIndex(cal.getTime()));
    }
    
    public void testGetSlotIndex3()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 46);
        cal.set(Calendar.SECOND, 0);
        assertEquals(3, TimeUtil.getSlotIndex(cal.getTime()));
    }
    
    public void testGetSlotIndex5()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 1);
        cal.set(Calendar.MINUTE, 29);
        cal.set(Calendar.SECOND, 59);
        assertEquals(5, TimeUtil.getSlotIndex(cal.getTime()));
    }
    
    public void testGetSlotIndex12()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 3);
        cal.set(Calendar.MINUTE, 01);
        cal.set(Calendar.SECOND, 59);
        assertEquals(12, TimeUtil.getSlotIndex(cal.getTime()));
    }
    
    public void testGetSlotIndex18()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 4);
        cal.set(Calendar.MINUTE, 41);
        cal.set(Calendar.SECOND, 59);
        assertEquals(18, TimeUtil.getSlotIndex(cal.getTime()));
    }
    
    public void testGetSlotIndex46()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 11);
        cal.set(Calendar.MINUTE, 41);
        cal.set(Calendar.SECOND, 59);
        assertEquals(46, TimeUtil.getSlotIndex(cal.getTime()));
    }
    
    public void testGetSlotIndex95()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 41);
        cal.set(Calendar.SECOND, 59);
        assertEquals(94, TimeUtil.getSlotIndex(cal.getTime()));
    }
    
    public void testGetSlotIndex96()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 49);
        cal.set(Calendar.SECOND, 59);
        assertEquals(95, TimeUtil.getSlotIndex(cal.getTime()));
    }
    
    public void testGetCalendarFromSlot()
    {
        Calendar cal = Calendar.getInstance();
        String ds = cal.get(Calendar.YEAR) + "-" + cal.get(Calendar.MONTH) + '-' + cal.get(Calendar.DAY_OF_MONTH);
        
        Calendar tm = TimeUtil.getCalendarFromSlot(ds, 0);
        assertEquals(cal.get(Calendar.YEAR), tm.get(Calendar.YEAR));
        assertEquals(cal.get(Calendar.MONTH), tm.get(Calendar.MONTH));
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), tm.get(Calendar.DAY_OF_MONTH));
        assertEquals(0, tm.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, tm.get(Calendar.MINUTE));
        assertEquals(0, tm.get(Calendar.SECOND));
        
        tm = TimeUtil.getCalendarFromSlot(ds, 1);
        assertEquals(cal.get(Calendar.YEAR), tm.get(Calendar.YEAR));
        assertEquals(cal.get(Calendar.MONTH), tm.get(Calendar.MONTH));
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), tm.get(Calendar.DAY_OF_MONTH));
        assertEquals(0, tm.get(Calendar.HOUR_OF_DAY));
        assertEquals(15, tm.get(Calendar.MINUTE));
        assertEquals(0, tm.get(Calendar.SECOND));
        
        tm = TimeUtil.getCalendarFromSlot(ds, 2);
        assertEquals(cal.get(Calendar.YEAR), tm.get(Calendar.YEAR));
        assertEquals(cal.get(Calendar.MONTH), tm.get(Calendar.MONTH));
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), tm.get(Calendar.DAY_OF_MONTH));
        assertEquals(0, tm.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, tm.get(Calendar.MINUTE));
        assertEquals(0, tm.get(Calendar.SECOND));
        
        
        tm = TimeUtil.getCalendarFromSlot(ds, 4);
        assertEquals(cal.get(Calendar.YEAR), tm.get(Calendar.YEAR));
        assertEquals(cal.get(Calendar.MONTH), tm.get(Calendar.MONTH));
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), tm.get(Calendar.DAY_OF_MONTH));
        assertEquals(1, tm.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, tm.get(Calendar.MINUTE));
        assertEquals(0, tm.get(Calendar.SECOND));
        
        tm = TimeUtil.getCalendarFromSlot(ds, 10);
        assertEquals(cal.get(Calendar.YEAR), tm.get(Calendar.YEAR));
        assertEquals(cal.get(Calendar.MONTH), tm.get(Calendar.MONTH));
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), tm.get(Calendar.DAY_OF_MONTH));
        assertEquals(2, tm.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, tm.get(Calendar.MINUTE));
        assertEquals(0, tm.get(Calendar.SECOND));
        
        tm = TimeUtil.getCalendarFromSlot(ds, 20);
        assertEquals(cal.get(Calendar.YEAR), tm.get(Calendar.YEAR));
        assertEquals(cal.get(Calendar.MONTH), tm.get(Calendar.MONTH));
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), tm.get(Calendar.DAY_OF_MONTH));
        assertEquals(5, tm.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, tm.get(Calendar.MINUTE));
        assertEquals(0, tm.get(Calendar.SECOND));
        
        tm = TimeUtil.getCalendarFromSlot(ds, 49);
        assertEquals(cal.get(Calendar.YEAR), tm.get(Calendar.YEAR));
        assertEquals(cal.get(Calendar.MONTH), tm.get(Calendar.MONTH));
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), tm.get(Calendar.DAY_OF_MONTH));
        assertEquals(12, tm.get(Calendar.HOUR_OF_DAY));
        assertEquals(15, tm.get(Calendar.MINUTE));
        assertEquals(0, tm.get(Calendar.SECOND));
        
        tm = TimeUtil.getCalendarFromSlot(ds, 90);
        assertEquals(cal.get(Calendar.YEAR), tm.get(Calendar.YEAR));
        assertEquals(cal.get(Calendar.MONTH), tm.get(Calendar.MONTH));
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), tm.get(Calendar.DAY_OF_MONTH));
        assertEquals(22, tm.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, tm.get(Calendar.MINUTE));
        assertEquals(0, tm.get(Calendar.SECOND));
        
        tm = TimeUtil.getCalendarFromSlot(ds, 92);
        assertEquals(cal.get(Calendar.YEAR), tm.get(Calendar.YEAR));
        assertEquals(cal.get(Calendar.MONTH), tm.get(Calendar.MONTH));
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), tm.get(Calendar.DAY_OF_MONTH));
        assertEquals(23, tm.get(Calendar.HOUR_OF_DAY));
        assertEquals(00, tm.get(Calendar.MINUTE));
        assertEquals(0, tm.get(Calendar.SECOND));
        
        tm = TimeUtil.getCalendarFromSlot(ds, 96);
        cal.add(Calendar.HOUR, 24);
        assertEquals(cal.get(Calendar.YEAR), tm.get(Calendar.YEAR));
        assertEquals(cal.get(Calendar.MONTH), tm.get(Calendar.MONTH));
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), tm.get(Calendar.DAY_OF_MONTH));
        assertEquals(0, tm.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, tm.get(Calendar.MINUTE));
        assertEquals(0, tm.get(Calendar.SECOND));
        
    }
    
}
