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
package au.edu.uts.eng.remotelabs.schedserver.bookings.impl;

import java.util.Calendar;
import java.util.Date;

import au.edu.uts.eng.remotelabs.schedserver.bookings.BookingActivator;

/**
 * Date / Time utility to convert dates between different formats.
 */
public class TimeUtil
{
    /**
     * Converts a date object to a date string without time portion in the form 
     * &lt;year&gt;-&lt;month&gt;-&lt;day&gt;.
     * 
     * @param date date to convert
     * @return string date
     */
    public static String getDateStr(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        
        return TimeUtil.getDateStr(cal);
    }
    
    /**
     * Converts a calendar object to a date string without time portion in the form 
     * &lt;year&gt;-&lt;month&gt;-&lt;day&gt;.
     * 
     * @param cal calendar to convert
     * @return string date
     */
    public static String getDateStr(Calendar cal)
    {        
        StringBuilder dt = new StringBuilder();
        dt.append(cal.get(Calendar.YEAR));
        dt.append('-');
        dt.append(cal.get(Calendar.MONTH));
        dt.append('-');
        dt.append(cal.get(Calendar.DAY_OF_MONTH));
        return dt.toString();
    }
    
    /**
     * Gets a date which is at the beginning of a day.
     * 
     * @param dateStr day key
     * @return start date
     */
    public static Date getDayBegin(String dateStr)
    {
        String parts[] = dateStr.split("-");
        
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(parts[0]));
        cal.set(Calendar.MONTH, Integer.parseInt(parts[1]));
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(parts[2]));
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        return cal.getTime();
    }
    
    /**
     * Gets a date which is at the beginning of a day.
     * 
     * @param dateStr day key
     * @return start date
     */
    public static Date getDayBegin(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        return cal.getTime();
    }
    
    /**
     * Gets a date which is at the beginning of a day.
     * 
     * @param dateStr day key
     * @return start date
     */
    public static Date getDayBegin(Calendar date)
    {   
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date.getTimeInMillis());
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        
        return cal.getTime();
    }
    
    /**
     * Gets a date which is at the end of a day.
     * 
     * @param dateStr day key
     * @return end date
     */
    public static Date getDayEnd(String dateStr)
    {
        String parts[] = dateStr.split("-");
        
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(parts[0]));
        cal.set(Calendar.MONTH, Integer.parseInt(parts[1]));
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(parts[2]));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        
        return cal.getTime();
    }
    
    /**
     * Gets a date which is at the end of a day.
     * 
     * @param dateStr day key
     * @return end date
     */
    public static Date getDayEnd(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        
        return cal.getTime();
    }
    
    /**
     * Gets a date which begins at the beginning of a day.
     * 
     * @param dateStr day key
     * @return end date
     */
    public static Date getDayEnd(Calendar date)
    {   
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(date.getTimeInMillis());
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        
        return cal.getTime();
    }
    
    /**
     * Gets the slot index for the specified date time.
     * 
     * @param date time of slot
     * @return slot index
     */
    public static int getSlotIndex(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return TimeUtil.getSlotIndex(cal);
    }
    
    /**
     * Gets the slot index for the specified date time.
     * 
     * @param cal time of slot
     * @return slot index
     */
    public static int getSlotIndex(Calendar cal)
    {
        return (int) (cal.get(Calendar.HOUR_OF_DAY) * (3600 / BookingActivator.TIME_QUANTUM) + 
            Math.ceil((cal.get(Calendar.MINUTE) * 60) + cal.get(Calendar.SECOND)) / BookingActivator.TIME_QUANTUM);
    }
}
