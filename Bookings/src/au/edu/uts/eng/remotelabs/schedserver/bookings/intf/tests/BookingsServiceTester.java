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
 * @date 4th January 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.bookings.intf.tests;

import java.util.Date;

import junit.framework.TestCase;

import org.hibernate.Session;

import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.BookingsService;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Bookings;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClass;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.testsetup.DataAccessTestSetup;

/**
 * Tests the {@link BookingsService} class.
 */
public class BookingsServiceTester extends TestCase
{
    /** Object of class under test. */
    private BookingsService service;

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        
        DataAccessTestSetup.setup();
        this.service = new BookingsService();
    }

    /**
     * Test method for {@link BookingsService#createBooking(types.CreateBooking)}.
     */
    public void testCreateBooking()
    {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link BookingsService#deleteBookings(types.DeleteBookings)}.
     */
    public void testDeleteBookings()
    {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link BookingsService#findFreeBookings(types.FindBookingSlots)}.
     */
    public void testFindFreeBookings()
    {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link BookingsService#getBooking(types.GetBooking)}.
     */
    public void testGetBooking()
    {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link BookingsService#getBookings(types.GetBookings)}.
     */
    public void testGetBookings()
    {
        Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        UserClass uclass1 = new UserClass();
        uclass1.setName("booktestclass");
        uclass1.setActive(true);
        uclass1.setQueuable(false);
        uclass1.setBookable(true);
        uclass1.setTimeHorizon(1000);
        ses.save(uclass1);
        RigType rigType1 = new RigType("booktestrigtype", 300, false);
        ses.save(rigType1);
        ResourcePermission perm1 = new ResourcePermission();
        perm1.setUserClass(uclass1);
        perm1.setType("TYPE");
        perm1.setSessionDuration(3600);
        perm1.setQueueActivityTimeout(300);
        perm1.setAllowedExtensions((short)10);
        perm1.setSessionActivityTimeout(300);
        perm1.setExtensionDuration(300);
        perm1.setMaximumBookings(10);
        perm1.setRigType(rigType1);
        perm1.setStartTime(new Date());
        perm1.setExpiryTime(new Date());
        perm1.setDisplayName("bookperm");
        ses.save(perm1);
        Bookings bk1 = new Bookings();
        bk1.setActive(true);
        bk1.setDuration(3600);
        Date start = new Date();
        bk1.setStartTime(start);
        Date end = new Date(System.currentTimeMillis() + 3600 * 1000);
        bk1.setEndTime(end);
        bk1.setResourcePermission(perm1);
        bk1.setResourceType("TYPE");

        ses.getTransaction().commit();
    }

}
