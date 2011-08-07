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
package au.edu.uts.eng.remotelabs.schedserver.multisite.provider.requests;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.axis2.AxisFault;
import org.hibernate.Session;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RemotePermission;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.BookingSlotType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.FindFreeBookings;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.FindFreeBookingsResponseType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.FindFreeBookingsType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.PermissionIDType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.TimePeriodType;

/**
 * Request to find free times.
 */
public class BookingsTimes extends AbstractRequest
{
    /** Response. */
    private FindFreeBookingsResponseType response;
    
    /**
     * Makes the MultiSite findFreeBookings call.
     * 
     * @param perm remote permission
     * @param start period start
     * @param end period end
     * @param db database session
     * @return true if call succeeded
     */
    public boolean findFreeTimes(RemotePermission perm, Calendar start, Calendar end, Session db)
    {
        this.site = perm.getSite();
        this.session = db;
        
        if (!this.checkPreconditions()) return false;
        
        /* Request parameters. */
        FindFreeBookings request = new FindFreeBookings();
        FindFreeBookingsType reqType = new FindFreeBookingsType();
        request.setFindFreeBookings(reqType);
        this.addSiteID(reqType);
        
        PermissionIDType permID = new PermissionIDType();
        this.addSiteID(permID);
        permID.setPermissionID(perm.getGuid());
        reqType.setPermission(permID);
        
        TimePeriodType time = new TimePeriodType();
        time.setStart(start);
        time.setEnd(end);
        reqType.setPeriod(time);
        
        try
        {
            this.response = this.getStub().findFreeBookings(request).getFindFreeBookingsResponse();
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
    
    public String getPermissionID()
    {
        return this.response.getPermission().getPermissionID();
    }
    
    public String getResourceName()
    {
        return this.response.getResource().getName();
    }
    
    public String getResourceType()
    {
        return this.response.getResource().getType();
    }
    
    public List<BookingTime> getTimes()
    {
        List<BookingTime> times = new ArrayList<BookingTime>();
        
        if (this.response.getSlot() == null) return times;
        
        for (BookingSlotType slot : this.response.getSlot())
        {
            BookingTime t = new BookingTime();
            t.start = slot.getStart();
            t.end = slot.getEnd();
            t.state = slot.getState().getValue();
            times.add(t);
        }
        
        return times;
    }
    
    /**
     * Booking slot.
     */
    public class BookingTime
    {
        private Calendar start;
        private Calendar end;
        private String state;

        public Calendar getStart()
        {
            return this.start;
        }

        public Calendar getEnd()
        {
            return this.end;
        }

        public String getState()
        {
            return this.state;
        }
    }
}
