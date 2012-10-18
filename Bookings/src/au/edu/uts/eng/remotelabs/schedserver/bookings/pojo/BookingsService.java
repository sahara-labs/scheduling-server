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
package au.edu.uts.eng.remotelabs.schedserver.bookings.pojo;

import java.util.Calendar;

import org.hibernate.Session;

import au.edu.uts.eng.remotelabs.schedserver.bookings.pojo.types.BookingOperation;
import au.edu.uts.eng.remotelabs.schedserver.bookings.pojo.types.BookingsPeriod;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Bookings;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;

/**
 * POJO Bookings service.
 */
public interface BookingsService
{
    /**
     * Finds the times where bookings can be made within a time period.
     * 
     * @param start range start
     * @param end range end
     * @param permission permission to find bookings
     * @param db database
     * @return free bookings times
     */
    public BookingsPeriod getFreeBookings(Calendar start, Calendar end, ResourcePermission permission, Session db);
    
    /**
     * Creates a booking.
     * 
     * @param start booking start
     * @param end booking end 
     * @param user user to creation booking for
     * @param permission permission to create booking
     * @param db database
     * @return booking creation
     */
    public BookingOperation createBooking(Calendar start, Calendar end, User user, ResourcePermission permission, Session db);
    
    /**
     * Cancels a booking.
     * 
     * @param booking booking that is to be canceled
     * @param reason reason for canceling booking
     * @param user whether the cancellation is user requested
     * @param db database
     * @return booking operation details 
     */
    public BookingOperation cancelBooking(Bookings booking, String reason, boolean user, Session db);
}
