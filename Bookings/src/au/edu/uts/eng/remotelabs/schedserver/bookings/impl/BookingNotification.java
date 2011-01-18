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
 * @date 18th January 2011
 */
package au.edu.uts.eng.remotelabs.schedserver.bookings.impl;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import au.edu.uts.eng.remotelabs.schedserver.bookings.BookingActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Bookings;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.messenger.MessengerService;

/**
 * Sends booking notification emails.
 */
public class BookingNotification
{
    /** Booking that was cancelled. */
    private Bookings booking;
    
    /** Logger. */
    private Logger logger;
    
    public BookingNotification(Bookings booking)
    {
        this.logger = LoggerActivator.getLogger();
        
        this.booking = booking;
        
    }
    
    /**
     * Sends booking cancellation email.
     */
    public void notifyCancel()
    {
        MessengerService service = BookingActivator.getMessengerService();
        if (service == null)
        {
            this.logger.warn("Unable send booking cancellation email to user " + this.booking.getUser().qName() + 
                    "because the messenger service wasn't loaded.");
        }
        else
        {
            Map<String, String> macros = new HashMap<String, String>();
            
            User user = this.booking.getUser(); 
            macros.put("firstname", user.getFirstName());
            macros.put("lastname", user.getLastName());
            macros.put("resource", this.getResourceName());
            macros.put("starttime", this.getPrettyDate(this.booking.getStartTime()));
            macros.put("cancelreason", this.booking.getCancelReason());
            
            service.sendTemplatedMessage(user, "BOOKING_CANCELLATION", macros);
        }
    }
    
    /**
     * Sends booking cancellation email.
     */
    public void notifyUserCancel()
    {
        MessengerService service = BookingActivator.getMessengerService();
        if (service == null)
        {
            this.logger.warn("Unable send booking cancellation email to user " + this.booking.getUser().qName() + 
                    "because the messenger service wasn't loaded.");
        }
        else
        {
            Map<String, String> macros = new HashMap<String, String>();
            
            User user = this.booking.getUser(); 
            macros.put("firstname", user.getFirstName());
            macros.put("lastname", user.getLastName());
            macros.put("resource", this.getResourceName());
            macros.put("starttime", this.getPrettyDate(this.booking.getStartTime()));
            
            service.sendTemplatedMessage(user, "BOOKING_CANCELLATION_CONFIRMATION", macros);
        }
    }
    
    /**
     * Sends booking creation email.
     */
    public void notifyCreation()
    {
        MessengerService service = BookingActivator.getMessengerService();
        if (service == null)
        {
            this.logger.warn("Unable send booking confirmation email to user " + this.booking.getUser().qName() + 
                    "because the messenger service wasn't loaded.");
        }
        else
        {
            Map<String, String> macros = new HashMap<String, String>();
            
            User user = this.booking.getUser(); 
            macros.put("firstname", user.getFirstName());
            macros.put("lastname", user.getLastName());
            macros.put("resource", this.getResourceName());
            macros.put("starttime", this.getPrettyDate(this.booking.getStartTime()));
            macros.put("endtime", this.getPrettyDate(this.booking.getEndTime()));
            
            service.sendTemplatedMessage(user, "BOOKING_CONFIRMATION", macros);
        }
    }
    
    /**
     * Sends booking creation email.
     */
    public void notifyStarting()
    {
        MessengerService service = BookingActivator.getMessengerService();
        if (service == null)
        {
            this.logger.warn("Unable send booking starting email to user " + this.booking.getUser().qName() + 
                    "because the messenger service wasn't loaded.");
        }
        else
        {
            Map<String, String> macros = new HashMap<String, String>();
            
            User user = this.booking.getUser(); 
            macros.put("firstname", user.getFirstName());
            macros.put("lastname", user.getLastName());
            macros.put("resource", this.getResourceName());
            macros.put("starttime", this.getPrettyDate(this.booking.getStartTime()));
            
            service.sendTemplatedMessage(user, "BOOKING_NOTIFICATION", macros);
        }
    }
    
    /**
     * Gets the resource name to provide in emails.
     *
     * @return resource name
     */
    private String getResourceName()
    {
        if (this.booking.getResourcePermission().getDisplayName() != null)
        {
            return this.booking.getResourcePermission().getDisplayName();
        }
        
        if (ResourcePermission.TYPE_PERMISSION.equals(this.booking.getResourceType()))
        {
            return this.booking.getRigType().getName();
        }
        else if (ResourcePermission.CAPS_PERMISSION.equals(this.booking.getResourceType()))
        {
            return this.booking.getRequestCapabilities().getCapabilities();
        }
        else return this.booking.getRig().getName();       
    }
    
    /**
     * Returns a displable date.
     * 
     * @param date date
     * @return date
     */
    private String getPrettyDate(Date date)
    {
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL);
        return df.format(date);
    }
}
