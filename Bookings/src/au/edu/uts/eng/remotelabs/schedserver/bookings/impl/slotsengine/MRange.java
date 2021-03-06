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

import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.BookingEngine.TimePeriod;

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
    
    /**
     * Returns the complement of the list of ranges. That is, for a range of
     * times, this will return a list containing the times not in that range.
     * The range must 
     * 
     * @param range time range
     * @param day day key
     * @return complement of range
     */
    public static List<MRange> complement(List<MRange> range, String day)
    {
        List<MRange> complement = new ArrayList<MRange>();
        
        if (range.size() == 0)
        {
            complement.add(new MRange(0, SlotBookingEngine.NUM_SLOTS - 1, day));
            return complement;
        }
        
        MRange t = range.get(0);
        if (t.getStartSlot() != 0)
        {
            complement.add(new MRange(0, t.getStartSlot()  -1, day));
        }
        
        for (int i = 0; i < range.size() - 1; i++)
        {
            MRange a = range.get(i);
            MRange b = range.get(i + 1);
          
            
            complement.add(new MRange(a.getEndSlot() + 1, b.getStartSlot() - 1, day));
        }
        
        t = range.get(range.size() - 1);
        if (t.getEndSlot() != SlotBookingEngine.NUM_SLOTS - 1)
        {
            complement.add(new MRange(t.getEndSlot() + 1, SlotBookingEngine.NUM_SLOTS  - 1, day));
        }
        
        return complement;
    }
    
    /**
     * Converts a list of ranges into a list of time periods. Two time
     * periods with one at the end of a day and the next at the beginning of the
     * next day are collapsed to a single time period.
     * <br />
     * Ranges on the same day are expected to already be sorted sequentially and
     * collapsed.
     * 
     * @param range list of ranges
     * @return list of time periods
     */
    public static List<TimePeriod> rangeToTimePeriod(List<MRange> range)
    {
        if (range.size() == 0)
        {
            return Collections.emptyList();
        }
        
        List<TimePeriod> times = new ArrayList<TimePeriod>(range.size());
        
        TimePeriod tp = null;
        for (int i = 0; i < range.size(); i++)
        {
            MRange mr = range.get(i);
            Calendar start = mr.getStart();
            Calendar end = mr.getEnd();

            /* If the current range starts at the beginning of a day, the 
             * preceding range finishes at the end of the day and the two
             * ranges are on successive days. The two ranges should be merged. */
            if (i > 0 && Math.abs(tp.getEndTime().getTimeInMillis() - start.getTimeInMillis())  < 1000)
            {
                times.set(times.size() - 1, tp = new TimePeriod(tp.getStartTime(), end));
            }
            else
            {
                times.add(tp = new TimePeriod(start, end));
            }
        }
        
        return times;
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
                this.dayKey.equals(mr.getDayKey());
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
