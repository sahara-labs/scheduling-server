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
 * @date 6th September 2011
 */
package au.edu.uts.eng.remotelabs.schedserver.multisite.impl;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Bookings;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.multisite.MultiSiteActivator;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.callback.client.MultiSiteCallbackClientHandler;

/**
 * Handles communication error by queuing for later transmission. 
 */
public class BookingsCancellationNotifierCallback extends MultiSiteCallbackClientHandler
{
    /** The cancelled booking. */
    private Bookings booking;
    
    /** Logger. */
    private final Logger logger = LoggerActivator.getLogger();
    
    public BookingsCancellationNotifierCallback(Bookings booking)
    {
        this.booking = booking;
    }
    
    @Override
    public void receiveResultBookingCancelled(final boolean successful, final String reason)
    {
        if (successful)
        {
            this.logger.debug("Successfully notified consumer site of booking cancellation.");
        }
        else
        {
            this.logger.warn("Consumer received booking cancellatio notification but did not successfully " +
            		"acknowledge it. Provided reason: " + reason);
        }
    }

    @Override
    public void receiveErrorBookingCancelled(final Exception e)
    {
        this.logger.warn("Failed sending booking cancellation notification for booking '" + this.booking.getId() + 
                    "'. Will queue booking for later sending. Exception " + e.getClass().getSimpleName() + ": " +
                    e.getMessage());
        MultiSiteActivator.queueNotification(this.booking);
    }
}
