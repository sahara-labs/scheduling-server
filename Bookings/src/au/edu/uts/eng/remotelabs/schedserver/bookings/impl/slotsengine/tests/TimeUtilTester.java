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

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.YEAR;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.BookingEngine.TimePeriod;
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
        Calendar start = TimeUtil.getDayBegin(new Date());
        assertNotNull(start);
        
        Calendar now = Calendar.getInstance();
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
        Calendar end = TimeUtil.getDayEnd(new Date());
        assertNotNull(end);
        
        Calendar now = Calendar.getInstance();
        assertEquals(23, end.get(HOUR_OF_DAY));
        assertEquals(59, end.get(MINUTE));
        assertEquals(59, end.get(SECOND));
        assertEquals(999, end.get(MILLISECOND));
        assertEquals(now.get(YEAR), end.get(YEAR));
        assertEquals(now.get(MONTH), end.get(MONTH));
        assertEquals(now.get(DAY_OF_MONTH), end.get(DAY_OF_MONTH));
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
    
    public void testDaySlotEngine()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 1);
        cal.set(Calendar.MINUTE, 35);
        cal.set(Calendar.SECOND, 10);
        String day = TimeUtil.getDateStr(cal);
        
        assertEquals(6, TimeUtil.getDaySlotIndex(cal, day));
    }
    
    public void testDaySlotEngineAfter()
    {
        Calendar cal = Calendar.getInstance();
        String day = TimeUtil.getDateStr(cal);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        
        assertEquals(95, TimeUtil.getDaySlotIndex(cal, day));
    }
    
    public void testDaySlotEngineBefore()
    {
        Calendar cal = Calendar.getInstance();
        String day = TimeUtil.getDateStr(cal);
        cal.add(Calendar.DAY_OF_MONTH, -1);
        
        assertEquals(0, TimeUtil.getDaySlotIndex(cal, day));
    }
    
    public void testCompareDays()
    {
        String d1 = "2010-10-10";
        assertEquals(0, TimeUtil.compareDays(d1, d1));
    }
    
    public void testCompareDaysDiffYear()
    {
        String d1 = "2010-10-10";
        String d2 = "2011-10-10";
        assertEquals(-1, TimeUtil.compareDays(d1, d2));
    }
    
    public void testCompareDaysDiffYear2()
    {
        String d1 = "2012-10-10";
        String d2 = "2011-10-10";
        assertEquals(1, TimeUtil.compareDays(d1, d2));
    }
    
    public void testCompareDaysDiffMonth()
    {
        String d1 = "2010-9-10";
        String d2 = "2010-10-10";
        assertEquals(-1, TimeUtil.compareDays(d1, d2));
    }
    
    public void testCompareDaysDiffMonth2()
    {
        String d1 = "2010-11-10";
        String d2 = "2010-10-10";
        assertEquals(1, TimeUtil.compareDays(d1, d2));
    }
    
    public void testCompareDaysDiffDay()
    {
        String d1 = "2010-10-9";
        String d2 = "2010-10-10";
        assertEquals(-1, TimeUtil.compareDays(d1, d2));
    }
    
    public void testCompareDaysDiffDay2()
    {
        String d1 = "2010-10-11";
        String d2 = "2010-10-10";
        assertEquals(1, TimeUtil.compareDays(d1, d2));
    }
    
    public void testGetDayKeys() throws Exception
    {
        Calendar cal = Calendar.getInstance();
        TimePeriod period = new TimePeriod(cal, cal);
        
        List<String> keys = TimeUtil.getDayKeys(period);
        assertNotNull(keys);
        assertEquals(1, keys.size());
        assertEquals(TimeUtil.getDateStr(cal), keys.get(0));
    }
    
   
    public void testGetDayKeysTwoDays()
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
        
        List<String> keys = TimeUtil.getDayKeys(period);
        assertNotNull(keys);
        assertEquals(2, keys.size());
        assertEquals(TimeUtil.getDateStr(cal), keys.get(0));
        cal.add(Calendar.DAY_OF_MONTH, 1);
        assertEquals(TimeUtil.getDateStr(cal), keys.get(1));
    }
    
    public void testGetDayKeysFiveDays() throws Exception
    {
        Calendar cal = Calendar.getInstance();
        Calendar next = Calendar.getInstance();
        next.add(Calendar.DAY_OF_MONTH, 4);
        TimePeriod period = new TimePeriod(cal, next);

        List<String> keys = TimeUtil.getDayKeys(period);
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
    
    public void testGetDayKeysFiveDaysDate() throws Exception
    {
        Calendar cal = Calendar.getInstance();
        Calendar next = Calendar.getInstance();
        next.add(Calendar.DAY_OF_MONTH, 4);

        List<String> keys = TimeUtil.getDayKeys(cal.getTime(), next.getTime());
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
    
    public void testCoerceToNextSlotTime()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 10);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        
        Calendar co = TimeUtil.coerceToNextSlotTime(cal);
        assertEquals(10, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, cal.get(Calendar.MINUTE));
        assertEquals(0, cal.get(Calendar.SECOND));
        assertEquals(co, cal);
    }
    
    public void testCoerceToNextSlotTimeHourRoll()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 10);
        cal.set(Calendar.MINUTE, 55);
        cal.set(Calendar.SECOND, 49);
        
        Calendar co = TimeUtil.coerceToNextSlotTime(cal);
        assertEquals(11, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, cal.get(Calendar.MINUTE));
        assertEquals(0, cal.get(Calendar.SECOND));
        assertEquals(co, cal);
    }
    
    public void testCoerceToNextSlotTimeHourRoll2()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 10);
        cal.set(Calendar.MINUTE, 45);
        cal.set(Calendar.SECOND, 49);
        
        Calendar co = TimeUtil.coerceToNextSlotTime(cal);
        assertEquals(11, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, cal.get(Calendar.MINUTE));
        assertEquals(0, cal.get(Calendar.SECOND));
        assertEquals(co, cal);
    }
    
    public void testCoerceToNextSlotTimeMinRoll()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 10);
        cal.set(Calendar.MINUTE, 33);
        cal.set(Calendar.SECOND, 11);
        
        Calendar co = TimeUtil.coerceToNextSlotTime(cal);
        assertEquals(10, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(45, cal.get(Calendar.MINUTE));
        assertEquals(0, cal.get(Calendar.SECOND));
        assertEquals(co, cal);
    }
    
    public void testCoerceToNextSlotTimeMinRoll2()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 10);
        cal.set(Calendar.MINUTE, 2);
        cal.set(Calendar.SECOND, 0);
        
        Calendar co = TimeUtil.coerceToNextSlotTime(cal);
        assertEquals(10, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(15, cal.get(Calendar.MINUTE));
        assertEquals(0, cal.get(Calendar.SECOND));
        assertEquals(co, cal);
    }
    
    public void testCoerceToNextSlotTimeMinRoll3()
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 10);
        cal.set(Calendar.MINUTE, 15);
        cal.set(Calendar.SECOND, 1);
        
        Calendar co = TimeUtil.coerceToNextSlotTime(cal);
        assertEquals(10, cal.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, cal.get(Calendar.MINUTE));
        assertEquals(0, cal.get(Calendar.SECOND));
        assertEquals(co, cal);
    }
}
