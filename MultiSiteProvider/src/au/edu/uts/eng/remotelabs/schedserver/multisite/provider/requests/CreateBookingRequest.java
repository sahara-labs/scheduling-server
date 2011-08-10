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
 * @date 8th August 2011
 */
package au.edu.uts.eng.remotelabs.schedserver.multisite.provider.requests;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.hibernate.Session;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RemotePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.BookingResponseType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.BookingSlotState;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.BookingType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.CreateBooking;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.CreateBookingType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.PermissionIDType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.TimePeriodType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.UserIDType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.requests.BookingsTimesRequest.BookingTime;

/**
 * Creates a booking request.
 */
public class CreateBookingRequest extends AbstractRequest
{
    /** Response. */
    private BookingResponseType response;
    
    /**
     * Creates a booking request.
     * 
     * @param user the user to create the booking for
     * @param perm permission identifier
     * @param start calendar start
     * @param end calendar end
     * @param db database
     * @return true if call successful
     */
    public boolean createBooking(User user, RemotePermission perm, Calendar start, Calendar end, Session db)
    {
        this.session = db;
        this.site  = perm.getSite();
        
        if (!this.checkPreconditions()) return false;
        
        CreateBooking request = new CreateBooking();
        CreateBookingType reqType = new CreateBookingType();
        this.addSiteID(reqType);
        request.setCreateBooking(reqType);
        
        UserIDType userId = new UserIDType();
        this.addSiteID(userId);
        userId.setUserID(user.getName());
        reqType.setUser(userId);
        
        BookingType booking = new BookingType();
        booking.setStart(start);
        booking.setEnd(end);
        reqType.setBooking(booking);
        
        PermissionIDType permId = new PermissionIDType();
        permId.setPermissionID(perm.getGuid());
        this.addSiteID(permId);
        booking.setPermission(permId);
        
        try
        {
            this.response = this.getStub().createBooking(request).getCreateBookingResponse();
        }
        catch (AxisFault e)
        {
            this.failed = true;
            this.failureReason = "Fault (" + e.getReason() + ")";
            this.logger.warn("SOAP fault making request, error reason '" + e.getReason() +
                    "', error message is '" + e.getMessage() + "'.");
            this.offlineSite(e);
            return false;
        }
        catch (RemoteException e)
        {
            this.failed = true;
            this.failureReason = "Remote error (" + e.getMessage() + ")";
            this.logger.warn("Remote error making request, error  message is '" + e.getMessage() + "'.");
            this.offlineSite(e);
            return false;
        }

        return true;
    }
    
    public boolean wasSuccessful()
    {
        return this.response.getWasSuccessful();
    }
    
    public String getReason()
    {
        return this.response.getReason();
    }

    public int getBookingID()
    {
        return this.response.getBookingID() == null ? 0 : this.response.getBookingID().getId();
    }
    
    public List<BookingTime> getBestFits()
    {
        List<BookingTime> bestFits = new ArrayList<BookingTime>();
        
        if (this.response.getBestFits() != null)
        {
            for (TimePeriodType tp : this.response.getBestFits())
            {
                BookingTime bt = new BookingTime();
                bt.start = tp.getStart();
                bt.end = tp.getEnd();
                bt.state = BookingSlotState._FREE;
                bestFits.add(bt);
            }
        }
        
        return bestFits;
    }
}
