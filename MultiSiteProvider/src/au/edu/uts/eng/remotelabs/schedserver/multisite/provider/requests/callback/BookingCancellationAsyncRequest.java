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
 * @date 31st August 2011
 */
package au.edu.uts.eng.remotelabs.schedserver.multisite.provider.requests.callback;

import java.rmi.RemoteException;
import java.util.Calendar;

import org.apache.axis2.AxisFault;
import org.hibernate.Session;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Bookings;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RemotePermission;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.callback.types.BookingCancelType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.callback.types.BookingCancelled;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.BookingType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.PermissionIDType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.UserIDType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.requests.AbstractRequest;

/**
 * Request to notify consumer site of booking cancellation.
 */
public class BookingCancellationAsyncRequest extends AbstractRequest
{
    public boolean bookingCancelled(Bookings booking, Session db, MultiSiteCallbackHandler callback)
    {
        this.session = db;
        
        RemotePermission remotePerm = booking.getResourcePermission().getRemotePermission();
        if (remotePerm == null)
        {
            this.logger.warn("Cannot sent booking notification cancellation to consumer because the remote mapping " +
            		"permission was not found.");
            return false;
        }
        this.site = remotePerm.getSite();
        
        if (!this.checkPreconditions()) return false;
        
        BookingCancelled request = new BookingCancelled();
        BookingCancelType cancel = new BookingCancelType();
        this.addSiteID(cancel);
        request.setBookingCancelled(cancel);
        
        UserIDType user = new UserIDType();
        this.addSiteID(user);
        user.setUserID(booking.getUser().getName());
        cancel.setUser(user);
        
        BookingType bt = new BookingType();
        Calendar start = Calendar.getInstance();
        start.setTime(booking.getStartTime());
        bt.setStart(start);
        Calendar end = Calendar.getInstance();
        end.setTime(booking.getEndTime());
        bt.setEnd(end);
        cancel.setBooking(bt);
        
        PermissionIDType permId = new PermissionIDType();
        this.addSiteID(permId);
        permId.setPermissionID(remotePerm.getGuid());
        bt.setPermission(permId);
        
        try
        {
            this.getCallbackStub().startbookingCancelled(request, callback);
        }
        catch (AxisFault e)
        {
            this.failed = true;
            this.failureReason = "Fault (" + e.getReason() + ")";
            this.logger.warn("SOAP fault initiating async SOAP request, error reason '" + e.getReason() +
                    "', error message is '" + e.getMessage() + "'.");
            this.offlineSite(e);
            return false;
        }
        catch (RemoteException e)
        {
            this.failed = true;
            this.failureReason = "Remote error (" + e.getMessage() + ")";
            this.logger.warn("Remote error initiating async request, error  message is '" + e.getMessage() + "'.");
            this.offlineSite(e);
            return false;
        }
        
        
        return true;
    }
}
