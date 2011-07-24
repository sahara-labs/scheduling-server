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
 * @date 30th January 2011
 */
package au.edu.uts.eng.remotelabs.schedserver.queuer.impl;

import au.edu.uts.eng.remotelabs.schedserver.bookings.BookingEngineService;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.queuer.QueueActivator;

/**
 * Utility methods for Queuer service implementations.
 */
public class QueuerUtil
{
    /**
     * Returns true if the rig is not booked for the specified duration. A free 
     * rig is one which:
     * <ul>
     *  <li>Is active and alive.</li>
     *  <li>Is online.</li>
     *  <li>Not in session.</li>
     *  <li>Not booked for the duration of the guaranteed permission time</li>
     * 
     * @param rig rig to check
     * @param perm permission to obtain duration for
     * @param ses database session
     * @return true if not booked
     */
    public static boolean isRigFree(Rig rig, ResourcePermission perm, org.hibernate.Session ses)
    {
        if (!rig.isActive() || !rig.isOnline() || rig.isInSession()) return false;
        
        BookingEngineService bs = QueueActivator.getBookingService();
        if (bs == null)
        {
            LoggerActivator.getLogger().debug("Returning rig " + rig.getName() + " is not booked because the bookings " +
            		"service does not appear to be running.");
            return true;
        }
        
        return bs.isFreeFor(rig, perm.getSessionDuration(), ses);
    }
}
