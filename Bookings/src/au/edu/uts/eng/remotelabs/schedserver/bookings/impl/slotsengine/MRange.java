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
package au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Slot range.
 */
public class MRange implements Comparable<MRange>
{
    /** Day key. */
    private String dayKey;
    
    /** The start slot of this range. */
    private int startSlot;
    
    /** The end slot of this range. */
    private int endSlot;
    
    /** The number of slots in this range. */
    private int numSlots;
    
    public MRange(int start, int end, String day)
    {
        this.startSlot = start;
        this.endSlot = end;
        this.numSlots = end - start + 1;
        this.dayKey = day;
    }
    
    /**
     * Collapse ranges by removing collapsing overlapping or directly
     * sequential ranges to a single range.
     * 
     * @param l1 list of time ranges
     * @param l2 list of time ranges
     * @return collapsed ranges
     */
    public static List<MRange> collapseRanges(List<MRange> l1, List<MRange> l2)
    {
        if (l1.size() == 0) return l2;
        if (l2.size() == 0) return l1;
        
        List<MRange> range = new ArrayList<MRange>(l1.size() + l2.size());
        range.addAll(l1);
        range.addAll(l2);
        
        return MRange.collapseRange(range);
    }
    
    /**
     * Collapse ranges by removing collapsing overlapping or directly
     * sequential ranges to a single range.
     * 
     * @param range list of ranges
     * @return collapsed ranges
     */
    public static List<MRange> collapseRange(List<MRange> range)
    {
        Collections.sort(range);
        
        /* Collapse all overlapping slots. */
        for (int i = 0; i < range.size() - 1; i++)
        {
            MRange cur = range.get(i);
            MRange next = range.get(i + 1);
              
            /* The +1 deals with sequential ranges. For example, if a range 
             * finishes at 5 and the next range starts at 6, they should be
             * collapsed since there isn't a filled slot between the ranges. */
            if (cur.getEndSlot() + 1 >= next.getStartSlot())
            {
                range.remove(next);
                MRange nm = new MRange(cur.getStartSlot(), 
                        next.getEndSlot() > cur.getEndSlot() ? next.getEndSlot() : cur.getEndSlot(), cur.getDayKey());
                range.set(i, nm);
                i--;
            }
        }
        
        return range;
    }

    public int getStartSlot()
    {
        return this.startSlot;
    }
    
    public Calendar getStart()
    {
        return TimeUtil.getCalendarFromSlot(this.dayKey, this.startSlot);
    }

    public int getEndSlot()
    {
        return this.endSlot;
    }
    
    public Calendar getEnd()
    {
        /* Bookings finish at the end of their slot. */
        return TimeUtil.getCalendarFromSlot(this.dayKey, this.endSlot + 1);
    }

    public int getNumSlots()
    {
        return this.numSlots;
    }
    
    public String getDayKey()
    {
        return this.dayKey;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        
        if (!(o instanceof MRange)) return false;
        
        MRange mr = (MRange)o;
        return mr.getStartSlot() == this.getStartSlot() && mr.getEndSlot() == this.endSlot && 
                this.dayKey.equals(this.dayKey);
    }
    
    @Override
    public int hashCode()
    {
        int hash = 23;
        hash = hash * 17 + this.startSlot;
        hash = hash * 17 + this.endSlot;
        hash = hash * 17 + this.dayKey.hashCode();
        return hash;
    }

    @Override
    public int compareTo(MRange o)
    {
        /* Checked days. */
        int cmp = TimeUtil.compareDays(this.dayKey, o.getDayKey());
        if (cmp != 0) return cmp;

        /* Check start slot. */
        if (this.startSlot < o.getStartSlot()) return -1;
        else if (this.startSlot != o.getStartSlot()) return 1;
        
        /* Check duration of slots. */
        if (this.numSlots < o.getNumSlots()) return -1;
        else if (this.numSlots != o.getNumSlots()) return 1;
        return 0;
    }
}
