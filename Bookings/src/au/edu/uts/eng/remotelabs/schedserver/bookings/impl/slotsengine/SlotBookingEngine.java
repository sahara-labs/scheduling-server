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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.BookingEngine;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RequestCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;


/**
 * The slot booking engine. This is an in-memory booking engine with uses 
 * aligned booking time.
 */
public class SlotBookingEngine implements BookingEngine
{
    /** The length of each booking slot in seconds. */
    public static final int TIME_QUANTUM = 15 * 60;
    
    public static final int NUM_SLOTS = 24 * 60 * 60 / SlotBookingEngine.TIME_QUANTUM;
    
    /** The loaded of day bookings. */
    private Map<String, DayBookings> days;
    
    /** Logger. */
    private Logger logger;
    
    public SlotBookingEngine()
    {
        this.logger = LoggerActivator.getLogger();
        this.days = new HashMap<String, DayBookings>();
    }
    
    @Override
    public void init()
    {
        this.logger.debug("Initalising the slot booking engine.");
    }

    @Override
    public List<TimePeriod> getFreeTimes(Rig rig, TimePeriod period, int minDuration)
    {
        /* Work out the slots that the minimum duration requires. */
        int slots = (int)Math.ceil(minDuration / TIME_QUANTUM);
        for (String day : this.getDayKeys(period))
        {
            DayBookings dayBookings = this.getDayBookings(day);
            int dayStart = TimeUtil.getDaySlotIndex(period.getStartTime(), day);
            int dayEnd = TimeUtil.getDaySlotIndex(period.getEndTime(), day);
            
        }
        
        return null;
    }

    @Override
    public List<TimePeriod> getFreeTimes(RigType rigType, TimePeriod period, int minDuration)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<TimePeriod> getFreeTimes(RequestCapabilities caps, TimePeriod period, int minDuration)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    /**
     * Returns the bookings for that day. If that day isn't loaded, it is 
     * loaded.
     * 
     * @param dayKey day key
     * @return bookings
     */
    private DayBookings getDayBookings(String dayKey)
    {
        if (!this.days.containsKey(dayKey))
        {
            synchronized (this.days)
            {
                this.days.put(dayKey, new DayBookings(dayKey));
            }
        }
        
        return this.days.get(dayKey);
    }
    
    /**
     * Returns the days in the specified day period.
     * 
     * @param period time period
     * @return list of day keys
     */
    private List<String> getDayKeys(TimePeriod period)
    {
        List<String> days = new ArrayList<String>();
        
        Calendar start = Calendar.getInstance();
        start.setTimeInMillis(period.getStartTime().getTimeInMillis());
        Calendar end = period.getEndTime();
        
        days.add(TimeUtil.getDateStr(start));
        while (end.compareTo(start) < 86.4e6 && start.get(Calendar.DAY_OF_YEAR) != end.get(Calendar.DAY_OF_YEAR))
        {
            start.add(Calendar.DAY_OF_MONTH, 1);
            days.add(TimeUtil.getDateStr(start));
        }
        
        return days;
    }
}
