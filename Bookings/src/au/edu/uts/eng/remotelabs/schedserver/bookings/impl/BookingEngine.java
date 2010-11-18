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
 * @date 18th November 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.bookings.impl;

import java.util.Calendar;
import java.util.List;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RequestCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;


/**
 * Booking engine interface. 
 */
public interface BookingEngine
{
    /** 
     * Initialise the booking engine.
     */
    void init();
    
    /**
     * Gets free time periods for the rig in the requested time period. Each 
     * of the returned time periods needs to be at least as long as the 
     * specified duration.
     * 
     * @param rig rig to find free durations of
     * @param period the time period to find the free periods in
     * @param minDuration minimum time of period in seconds
     * @return list of time periods
     */
    public List<TimePeriod> getFreeTimes(Rig rig, TimePeriod period, int minDuration);
    
    /**
     * Gets free time periods for the rig type in the requested time period.
     * Each of the returned time periods needs to be at least as long as 
     * the specified duration.
     * 
     * @param rigType rig type to find free durations of
     * @param period the time period to find the free periods in
     * @param minDuration minimum time of period in seconds
     * @return list of time periods
     */
    public List<TimePeriod> getFreeTimes(RigType rigType, TimePeriod period, int minDuration);
    
    /**
     * Gets free time periods for the request capabilities in the requested 
     * time period. Each of the returned time periods needs to be at least as 
     * long as the specified duration.
     * 
     * @param caps request capabilities to find free durations of
     * @param period the time period to find the free periods in
     * @param minDuration minimum time of period in seconds
     * @return list of time periods
     */
    public List<TimePeriod> getFreeTimes(RequestCapabilities caps, TimePeriod period, int minDuration);
    
    public class TimePeriod
    {
        /** Period start. */
        private Calendar startTime;
        
        /** Period end. */
        private Calendar endTime;
        
        public TimePeriod(Calendar start, Calendar end)
        {
            this.startTime = start;
            this.endTime = end;
        }

        public Calendar getStartTime()
        {
            return this.startTime;
        }

        public Calendar getEndTime()
        {
            return this.endTime;
        }
    }
}
