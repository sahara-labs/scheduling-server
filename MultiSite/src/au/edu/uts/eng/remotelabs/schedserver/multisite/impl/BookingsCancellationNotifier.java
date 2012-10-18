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
 * @date 5th September 2011
 */
package au.edu.uts.eng.remotelabs.schedserver.multisite.impl;

import java.rmi.RemoteException;
import java.util.Calendar;

import org.apache.axis2.AxisFault;
import org.hibernate.Session;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Bookings;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RemoteSite;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.BookingsEventListener;
import au.edu.uts.eng.remotelabs.schedserver.multisite.MultiSiteActivator;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.callback.types.BookingCancelType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.callback.types.BookingCancelled;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.BookingType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.PermissionIDType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.UserIDType;

/**
 * Notifies a consumer site that a booking has been cancelled by the provider
 * site. 
 */
public class BookingsCancellationNotifier extends AbstractCallbackRequest implements BookingsEventListener
{
    @Override
    public void eventOccurred(BookingsEvent event, Bookings booking, Session db)
    {
        /* If the booking is not from a consumer site, we can ignored it. */
        if (!booking.getResourcePermission().isRemote()) return;
        
        if (booking.getResourcePermission().getRemotePermission() == null)
        {
            this.logger.error("Unable to provide consumer notification of the cancellation of booking '" + 
                    booking.getId() + "' because there is no remote permission mapping.");
            return;
        }
        
        switch (event)
        {
            case SYSTEM_CANCELLED:
                this.notifyCancellation(booking, db);
                break;
                
            case USER_CANCELLED:
                this.logger.debug("MultiSite booking cancelled by user '" + booking.getUser().qName() + 
                        "' so no need to notify consumer site.");
                break;
        }
    }
    
    /**
     * Notifies site of booking cancellation.
     * 
     * @param booking cancelled booking
     * @param db database
     */
    private void notifyCancellation(Bookings booking, Session db)
    {
        RemoteSite site = booking.getResourcePermission().getRemotePermission().getSite();
        
        if (!this.checkPreconditions(site, db))
        {
            MultiSiteActivator.queueNotification(booking);
            return;
        }
                
        BookingCancelled request = new BookingCancelled();
        BookingCancelType cancel = new BookingCancelType();
        this.addSiteID(cancel);
        request.setBookingCancelled(cancel);
        
        UserIDType user = new UserIDType();
        this.addSiteID(user);
        user.setUserID(booking.getUser().getName());
        cancel.setUser(user);
        
        BookingType bookingType = new BookingType();
        Calendar start = Calendar.getInstance();
        start.setTime(booking.getStartTime());
        bookingType.setStart(start);
        Calendar end = Calendar.getInstance();
        end.setTime(booking.getEndTime());
        bookingType.setEnd(end);
        cancel.setBooking(bookingType);
        
        PermissionIDType perm = new PermissionIDType();
        this.addSiteID(perm);
        perm.setPermissionID(booking.getResourcePermission().getRemotePermission().getGuid());
        bookingType.setPermission(perm);
        
        try
        {
            this.getStub(site).startBookingCancelled(request, new BookingsCancellationCallback(booking));
        }
        catch (AxisFault e)
        {
            this.logger.warn("Failed sending booking cancellation notification for booking '" + booking.getId() + 
                    "'. Will queue booking for later sending. Axis fault: " + e.getFaultMessageContext());
            MultiSiteActivator.queueNotification(booking);
        }
        catch (RemoteException e)
        {
            this.logger.warn("Failed sending booking cancellation notification for booking '" + booking.getId() + 
                    "'. Will queue booking for later sending. Remote exception: " + e.getClass().getSimpleName());
            MultiSiteActivator.queueNotification(booking);
        }
    }
}
