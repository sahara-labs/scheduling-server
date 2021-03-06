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
 * @date 10th November 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao;

import java.util.Calendar;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Bookings;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;

/**
 * Data access object for the {@link Bookings} entity.
 */
public class BookingsDao extends GenericDao<Bookings>
{
    /**
     * Constructor that opens a new session.
     * 
     * @throws IllegalStateException if a session factory cannot be obtained
     */
    public BookingsDao()
    {
        super(Bookings.class);
    }
    
    /**
     * Constructor that uses the provided session. The session must be 
     * not-null and open.
     * 
     * @param ses open session
     * @throws IllegalStateException if the provided use session is null or
     *         closed
     */
    public BookingsDao(Session ses)
    {
        super(ses, Bookings.class);
    }
    
    /**
     * Gets a users booking that will start next.
     * 
     * @param user user to booking for
     * @return booking or null if not found
     */
    public Bookings getNextBooking(User user)
    {
        return this.getNextBookingWithin(user, 0);
    }
    
    /**
     * Gets a users booking that will start anyone between now and now 
     * plus the specified duration. 
     * 
     * @param user user to find the booking for
     * @param duration duration to find bookings within
     * @return booking or null if not found
     */
    public Bookings getNextBookingWithin(User user, int duration)
    {
        Calendar start = Calendar.getInstance();
        start.add(Calendar.SECOND, duration);
        
        return (Bookings) this.session.createCriteria(Bookings.class)
            .add(Restrictions.eq("active", Boolean.TRUE))
            .add(Restrictions.eq("user", user))
            .add(Restrictions.lt("startTime", start.getTime()))
            .setMaxResults(1)
            .addOrder(Order.asc("startTime"))
            .uniqueResult();
    }
}
