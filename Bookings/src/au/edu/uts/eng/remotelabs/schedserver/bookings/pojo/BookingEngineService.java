/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2011, University of Technology, Sydney
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
 * @date 12th Decemeber 2011
 */
package au.edu.uts.eng.remotelabs.schedserver.bookings.pojo;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigOfflineSchedule;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;

/**
 * Booking service to interface queueing operations with booking operations.
 */
public interface BookingEngineService
{
    /**
     * Checks whether the rig is free for the specified duration. The rig is 
     * free if there are no booking on the rig between now and now plus the
     * duration. 
     * 
     * @param rig rig to check for freeness
     * @param duration duration to check from now
     * @param db database connection
     * @return true if the rig is free
     */
    public boolean isFreeFor(Rig rig, int duration, org.hibernate.Session db);
    
    /**
     * Attempts to put a queued session to a rig. This returns <tt>true</tt> if
     * the rig is free for the duration and the session can be put to it. If it
     * returns <tt>false</tt> the rig cannot be allocated to the session because
     * the rig is already booked.
     * 
     * @param rig rig to put a booking to
     * @param ses session to put
     * @param db database connection
     * @return true if session is put to rig
     */
    public boolean putQueuedSession(Rig rig, Session ses, org.hibernate.Session db);
    
    /**
     * Attempts to extend an existing session on the rig.
     * 
     * @param rig rig to extend rig on
     * @param ses session to extend
     * @param duration length of extension
     * @param db database connection
     * @return true if session is extended
     */
    public boolean extendQueuedSession(Rig rig, Session ses, int duration, org.hibernate.Session db);
    
    /**
     * Puts a rig offline so it cannot accept bookings for the specified time 
     * period. If the rig already has bookings in that time period, the bookings
     * will be cancelled.
     * 
     * @param period offline period
     * @param db database connection
     */
    public void putRigOffline(RigOfflineSchedule period, org.hibernate.Session db);
    
    /**
     * Clears a rig offline period so bookings can be made during the offline period.
     * 
     * @param period offline period
     * @param db database connection
     */
    public void clearRigOffline(RigOfflineSchedule period, org.hibernate.Session db);
}
