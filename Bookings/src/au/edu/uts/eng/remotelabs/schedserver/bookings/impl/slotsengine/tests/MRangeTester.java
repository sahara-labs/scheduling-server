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
import java.util.List;

import junit.framework.TestCase;
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
        this.day = TimeUtil.getDateStr(Calendar.getInstance());
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
}
