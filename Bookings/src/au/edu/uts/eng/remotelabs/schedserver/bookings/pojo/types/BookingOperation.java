/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2011, Michael Diponio
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
 * @date 7th August 2011
 */
package au.edu.uts.eng.remotelabs.schedserver.bookings.pojo.types;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Bookings;

/**
 * Booking creation response.
 */
public class BookingOperation
{
    /** Whether the booking operation successful. */
    private boolean success;
    
    /** The booking. */
    private Bookings booking;
    
    /** Best fits. */
    private final List<BestFit> bestFits;
    
    /** Failure reason. */
    private String failureReason;
    
    public BookingOperation()
    {
        this.bestFits = new ArrayList<BookingOperation.BestFit>();
    }

    public boolean successful()
    {
        return this.success;
    }

    public Bookings getBooking()
    {
        return this.booking;
    }

    public void setBooking(Bookings booking)
    {
        this.booking = booking;
    }
    
    public List<BestFit> getBestFits()
    {
        return this.bestFits;
    }
    
    public void addBestFit(Calendar start, Calendar end)
    {
        BestFit bf = new BestFit();
        bf.start = start;
        bf.end = end;
        this.bestFits.add(bf);
    }
    
    public void setSuccess(boolean success)
    {
        this.success = success;
    }

    public String getFailureReason()
    {
        return this.failureReason;
    }

    public void setFailureReason(String errorReason)
    {
        this.failureReason = errorReason;
    }
    
    public class BestFit
    {
        private Calendar start;
        
        private Calendar end;

        public Calendar getStart()
        {
            return this.start;
        }
        
        public Calendar getEnd()
        {
            return this.end;
        }
    }
}
