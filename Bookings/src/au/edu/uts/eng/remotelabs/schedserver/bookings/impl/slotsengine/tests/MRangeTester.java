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
 * @date 23th November 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine.tests;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.BookingEngine.TimePeriod;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine.MRange;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine.TimeUtil;

/**
 * Tests the {@link MRange} class.
 */
public class MRangeTester extends TestCase
{
    /** MRange day. */
    private String day;
    
    @Override
    public void setUp() throws Exception
    {
        this.day = TimeUtil.getDayKey(Calendar.getInstance());
    }
    
    public void testComplement()
    {
        List<MRange> r1 = new ArrayList<MRange>();
        r1.add(new MRange(6, 21, this.day));
        r1.add(new MRange(24, 31, this.day));
        r1.add(new MRange(33, 35, this.day));
        r1.add(new MRange(38, 95, this.day));
        
        List<MRange> co = MRange.complement(r1, this.day);
       assertEquals(4, co.size());
        
        MRange m = co.get(0);
        assertEquals(0, m.getStartSlot());
        assertEquals(5, m.getEndSlot());
        
        m = co.get(1);
        assertEquals(22, m.getStartSlot());
        assertEquals(23, m.getEndSlot());
        
        m = co.get(2);
        assertEquals(32, m.getStartSlot());
        assertEquals(32, m.getEndSlot());
        
        m = co.get(3);
        assertEquals(36, m.getStartSlot());
        assertEquals(37, m.getEndSlot());
    }
    
    public void testComplement2()
    {
        List<MRange> r1 = new ArrayList<MRange>();
        r1.add(new MRange(0, 21, this.day));
        r1.add(new MRange(24, 31, this.day));
        r1.add(new MRange(33, 35, this.day));
        r1.add(new MRange(38, 95, this.day));
        
        List<MRange> co = MRange.complement(r1, this.day);
       assertEquals(3, co.size());
        
        MRange m = co.get(0);
        
        m = co.get(0);
        assertEquals(22, m.getStartSlot());
        assertEquals(23, m.getEndSlot());
        
        m = co.get(1);
        assertEquals(32, m.getStartSlot());
        assertEquals(32, m.getEndSlot());
        
        m = co.get(2);
        assertEquals(36, m.getStartSlot());
        assertEquals(37, m.getEndSlot());
    }
    
    public void testComplement3()
    {
        List<MRange> r1 = new ArrayList<MRange>();
        r1.add(new MRange(0, 21, this.day));
        r1.add(new MRange(24, 31, this.day));
        r1.add(new MRange(33, 35, this.day));
        r1.add(new MRange(38, 55, this.day));
        
        List<MRange> co = MRange.complement(r1, this.day);
       assertEquals(4, co.size());
        
        MRange m = co.get(0);
        
        m = co.get(0);
        assertEquals(22, m.getStartSlot());
        assertEquals(23, m.getEndSlot());
        
        m = co.get(1);
        assertEquals(32, m.getStartSlot());
        assertEquals(32, m.getEndSlot());
        
        m = co.get(2);
        assertEquals(36, m.getStartSlot());
        assertEquals(37, m.getEndSlot());
        
        m = co.get(3);
        assertEquals(56, m.getStartSlot());
        assertEquals(95, m.getEndSlot());
    }
    
    public void testComplementEmpty()
    {
        List<MRange> range = MRange.complement(Collections.<MRange>emptyList(), this.day);
        assertEquals(1, range.size());
        assertEquals(0, range.get(0).getStartSlot());
        assertEquals(95, range.get(0).getEndSlot());
    }
    
    public void testComplementFull()
    {
        List<MRange> range = new ArrayList<MRange>();
        range.add(new MRange(0, 95, this.day));
        assertEquals(0, MRange.complement(range, this.day).size());
    }

    public void testCollapseRangesNoOverlap()
    {
        List<MRange> l1 = new ArrayList<MRange>();
        List<MRange> l2 = new ArrayList<MRange>();
        l1.add(new MRange(10, 15, this.day));
        l2.add(new MRange(20, 30, this.day));
        l1.add(new MRange(32, 35, this.day));
        l2.add(new MRange(40, 50, this.day));
        l1.add(new MRange(52, 60, this.day));
        l2.add(new MRange(65, 75, this.day));
        l1.add(new MRange(80, 85, this.day));
        l2.add(new MRange(87, 94, this.day));
        
        List<MRange> co = MRange.collapseRanges(l1, l2);
        assertNotNull(co);
        assertEquals(8, co.size());
        assertEquals(l1.get(0), co.get(0));
        assertEquals(l1.get(1), co.get(2));
        assertEquals(l1.get(2), co.get(4));
        assertEquals(l1.get(3), co.get(6));
        assertEquals(l2.get(0), co.get(1));
        assertEquals(l2.get(1), co.get(3));
        assertEquals(l2.get(2), co.get(5));
        assertEquals(l2.get(3), co.get(7));
    }
    
    public void testCollapseRangesOverlap()
    {
        List<MRange> l1 = new ArrayList<MRange>();
        List<MRange> l2 = new ArrayList<MRange>();
        l1.add(new MRange(0, 15, this.day));
        l2.add(new MRange(12, 35, this.day));
        l1.add(new MRange(32, 60, this.day));
        l2.add(new MRange(40, 50, this.day));
        l1.add(new MRange(62, 70, this.day));
        l2.add(new MRange(55, 75, this.day));
        l1.add(new MRange(75, 85, this.day));
        l2.add(new MRange(84, 95, this.day));
        
        List<MRange> co = MRange.collapseRanges(l1, l2);
        assertNotNull(co);
        assertEquals(1, co.size());
        assertEquals(new MRange(0, 95, this.day), co.get(0));
    }
    
    public void testCollapseRangeOverlap()
    {
        List<MRange> l1 = new ArrayList<MRange>();        
        l1.add(new MRange(0, 15, this.day));
        l1.add(new MRange(12, 35, this.day));
        l1.add(new MRange(32, 60, this.day));
        l1.add(new MRange(40, 50, this.day));
        l1.add(new MRange(62, 70, this.day));
        l1.add(new MRange(55, 75, this.day));
        l1.add(new MRange(75, 85, this.day));
        l1.add(new MRange(84, 95, this.day));
        
        List<MRange> co = MRange.collapseRange(l1);
        assertNotNull(co);
        assertEquals(1, co.size());
        assertEquals(new MRange(0, 95, this.day), co.get(0));
    }
    
    public void testCollapseRangesSequential()
    {
        List<MRange> l1 = new ArrayList<MRange>();
        List<MRange> l2 = new ArrayList<MRange>();
        l1.add(new MRange(0, 15, this.day));
        l2.add(new MRange(16, 35, this.day));
        l1.add(new MRange(32, 60, this.day));
        l2.add(new MRange(40, 50, this.day));
        l1.add(new MRange(62, 70, this.day));
        l2.add(new MRange(55, 75, this.day));
        l1.add(new MRange(75, 85, this.day));
        l2.add(new MRange(84, 95, this.day));
        
        List<MRange> co = MRange.collapseRanges(l1, l2);
        assertNotNull(co);
        assertEquals(1, co.size());
        assertEquals(new MRange(0, 95, this.day), co.get(0));
    }
    
    public void testCollapseRangeSequential()
    {
        List<MRange> l1 = new ArrayList<MRange>();
        l1.add(new MRange(0, 15, this.day));
        l1.add(new MRange(16, 35, this.day));
        l1.add(new MRange(32, 60, this.day));
        l1.add(new MRange(40, 50, this.day));
        l1.add(new MRange(62, 70, this.day));
        l1.add(new MRange(55, 75, this.day));
        l1.add(new MRange(75, 85, this.day));
        l1.add(new MRange(84, 95, this.day));
        
        List<MRange> co = MRange.collapseRange(l1);
        assertNotNull(co);
        assertEquals(1, co.size());
        assertEquals(new MRange(0, 95, this.day), co.get(0));
    }
    
    public void testCollapseRangesOverlapHoles()
    {
        List<MRange> l1 = new ArrayList<MRange>();
        List<MRange> l2 = new ArrayList<MRange>();
        l1.add(new MRange(0, 12, this.day));
        l2.add(new MRange(12, 20, this.day));
        l1.add(new MRange(22, 30, this.day));
        l2.add(new MRange(22, 30, this.day));
        l1.add(new MRange(62, 70, this.day));
        l2.add(new MRange(55, 75, this.day));
        l1.add(new MRange(80, 85, this.day));
        l2.add(new MRange(84, 95, this.day));
        
        List<MRange> co = MRange.collapseRanges(l1, l2);
        assertNotNull(co);
        assertEquals(4, co.size());
        assertEquals(new MRange(0, 20, this.day), co.get(0));
        assertEquals(new MRange(22, 30, this.day), co.get(1));
        assertEquals(new MRange(55, 75, this.day), co.get(2));
        assertEquals(new MRange(80, 95, this.day), co.get(3));
    }

    public void testCompareToSame()
    {
        String d1 = "2010-10-10";
        MRange r1 = new MRange(10, 11, d1);
        assertEquals(0, r1.compareTo(r1));
    }
    
    public void testCompareToDays()
    {
        String d1 = "2010-10-10";
        String d2 = "2010-10-11";
        MRange r1 = new MRange(10, 11, d1);
        MRange r2 = new MRange(10, 11, d2);
        assertEquals(-1, r1.compareTo(r2));
    }
    
    public void testCompareToDaysLater()
    {
        String d1 = "2010-10-11";
        String d2 = "2010-10-10";
        MRange r1 = new MRange(10, 11, d1);
        MRange r2 = new MRange(10, 11, d2);
        assertEquals(1, r1.compareTo(r2));
    }
    
    public void testCompareSlots()
    {
        String d1 = "2010-10-10";
        MRange r1 = new MRange(10, 11, d1);
        MRange r2 = new MRange(11, 11, d1);
        assertEquals(-1, r1.compareTo(r2));
    }
    
    public void testCompareSlotsLater()
    {
        String d1 = "2010-10-10";
        MRange r1 = new MRange(15, 11, d1);
        MRange r2 = new MRange(11, 11, d1);
        assertEquals(1, r1.compareTo(r2));
    }
    
    public void testCompareLen()
    {
        String d1 = "2010-10-10";
        MRange r1 = new MRange(15, 11, d1);
        MRange r2 = new MRange(15, 15, d1);
        assertEquals(-1, r1.compareTo(r2));
    }
    
    public void testCompareLenLater()
    {
        String d1 = "2010-10-10";
        MRange r1 = new MRange(15, 15, d1);
        MRange r2 = new MRange(15, 11, d1);
        assertEquals(1, r1.compareTo(r2));
    }
    
    public void testCompareLenEqual()
    {
        String d1 = "2010-10-10";
        MRange r1 = new MRange(11, 11, d1);
        MRange r2 = new MRange(11, 11, d1);
        assertEquals(0, r1.compareTo(r2));
    }
    
    public void testRangeToTimePeriod()
    {
        String dk = TimeUtil.getDayKey(Calendar.getInstance());
        List<MRange> range = new ArrayList<MRange>();
        range.add(new MRange(1, 4, dk));
        range.add(new MRange(11, 14, dk));
        range.add(new MRange(21, 24, dk));
        range.add(new MRange(31, 34, dk));
        range.add(new MRange(41, 44, dk));
        
        List<TimePeriod> times = MRange.rangeToTimePeriod(range);
        assertEquals(times.size(), range.size());
        
        for (int i = 0; i < times.size(); i++)
        {
            MRange m = range.get(i);
            TimePeriod t = times.get(i);
            
            assertEquals(m.getDayKey(), TimeUtil.getDayKey(t.getStartTime()));
            assertEquals(m.getDayKey(), TimeUtil.getDayKey(t.getEndTime()));
            assertEquals(m.getStartSlot(), TimeUtil.getSlotIndex(t.getStartTime()));
            
            /* Duration. */
            assertEquals(3600, (t.getEndTime().getTimeInMillis() - t.getStartTime().getTimeInMillis()) / 1000); 
        }
    }
    
    public void testRangeToTimeCollapse()
    {
        Calendar cal = Calendar.getInstance();
        List<MRange> range = new ArrayList<MRange>();
        range.add(new MRange(0, 95, TimeUtil.getDayKey(cal)));
        cal.add(Calendar.DAY_OF_MONTH, 1);
        range.add(new MRange(0, 95, TimeUtil.getDayKey(cal)));
        cal.add(Calendar.DAY_OF_MONTH, 2);
        range.add(new MRange(0, 95, TimeUtil.getDayKey(cal)));
        
        
        List<TimePeriod> times = MRange.rangeToTimePeriod(range);
        assertEquals(2, times.size());
        
        TimePeriod t = times.get(0);
        Calendar s = t.getStartTime();
        assertEquals(0, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
    }
}
