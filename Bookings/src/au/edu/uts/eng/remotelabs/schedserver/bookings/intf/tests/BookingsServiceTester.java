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

import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import org.hibernate.Session;

import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.BookingsService;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.BookingIDType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.BookingListType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.BookingRequestType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.BookingType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.BookingsRequestType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.GetBooking;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.GetBookingResponse;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.GetBookings;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.GetBookingsResponse;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.PermissionIDType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.ResourceIDType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.UserIDType;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Bookings;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RequestCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
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
        Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        UserClass uclass1 = new UserClass();
        uclass1.setName("booktestclass");
        uclass1.setActive(true);
        uclass1.setQueuable(false);
        uclass1.setBookable(true);
        uclass1.setTimeHorizon(1000);
        ses.save(uclass1);
        User us1 = new User();
        us1.setName("bktestuser");
        us1.setNamespace("BKNS");
        us1.setPersona("USER");
        ses.save(us1);
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
        bk1.setRigType(rigType1);
        bk1.setUser(us1);
        bk1.setUserName(us1.getName());
        bk1.setUserNamespace(us1.getNamespace());
        ses.save(bk1);
        ses.getTransaction().commit();
        
        GetBooking request = new GetBooking();
        BookingRequestType reqTy = new BookingRequestType();
        BookingIDType id = new BookingIDType();
        reqTy.setBookingID(id);
        request.setGetBooking(reqTy);
        id.setBookingID(bk1.getId().intValue());
        
        GetBookingResponse response = this.service.getBooking(request);
        
        ses.beginTransaction();
        ses.delete(bk1);
        ses.delete(perm1);
        ses.delete(rigType1);
        ses.delete(us1);
        ses.delete(uclass1);
        ses.getTransaction().commit();
        
        assertNotNull(response);
        BookingType b = response.getGetBookingResponse();
        assertNotNull(b);
        
        assertEquals(bk1.getId().intValue(), b.getBookingID());
        ResourceIDType res = b.getBookedResource();
        assertNotNull(res);
        assertEquals(perm1.getType(), res.getType()); 
        assertEquals(rigType1.getName(), res.getResourceName());
        assertEquals(rigType1.getId().intValue(), res.getResourceID());
        
        PermissionIDType pid = b.getPermissionID();
        assertNotNull(pid);
        assertEquals(perm1.getId().intValue(), pid.getPermissionID());
        
        Calendar startCal = b.getStartTime();
        assertNotNull(startCal);
        assertEquals(start.getTime() / 1000, startCal.getTimeInMillis() / 1000);
        
        Calendar endCal = b.getEndTime();
        assertNotNull(endCal);
        assertEquals(end.getTime() / 1000, endCal.getTimeInMillis() / 1000);
        
        assertEquals(bk1.getDuration(), b.getDuration());
        
        assertNull(b.getCodeReference());
        assertNull(b.getCancelReason());
        assertFalse(b.getIsCancelled());
        assertFalse(b.getIsFinished());   
    }
    
    /**
     * Test method for {@link BookingsService#getBooking(types.GetBooking)}.
     */
    public void testGetBookingCancelled()
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
        User us1 = new User();
        us1.setName("bktestuser");
        us1.setNamespace("BKNS");
        us1.setPersona("USER");
        ses.save(us1);
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
        bk1.setActive(false);
        bk1.setCancelReason("Test cancel.");
        bk1.setDuration(3600);
        Date start = new Date();
        bk1.setStartTime(start);
        Date end = new Date(System.currentTimeMillis() + 3600 * 1000);
        bk1.setEndTime(end);
        bk1.setResourcePermission(perm1);
        bk1.setResourceType("TYPE");
        bk1.setRigType(rigType1);
        bk1.setUser(us1);
        bk1.setUserName(us1.getName());
        bk1.setUserNamespace(us1.getNamespace());
        bk1.setCodeReference("/foo/bar");
        ses.save(bk1);
        ses.getTransaction().commit();
        
        GetBooking request = new GetBooking();
        BookingRequestType reqTy = new BookingRequestType();
        BookingIDType id = new BookingIDType();
        reqTy.setBookingID(id);
        request.setGetBooking(reqTy);
        id.setBookingID(bk1.getId().intValue());
        
        GetBookingResponse response = this.service.getBooking(request);
        
        ses.beginTransaction();
        ses.delete(bk1);
        ses.delete(perm1);
        ses.delete(rigType1);
        ses.delete(us1);
        ses.delete(uclass1);
        ses.getTransaction().commit();
        
        assertNotNull(response);
        BookingType b = response.getGetBookingResponse();
        assertNotNull(b);
        
        assertEquals(bk1.getId().intValue(), b.getBookingID());
        ResourceIDType res = b.getBookedResource();
        assertNotNull(res);
        assertEquals(perm1.getType(), res.getType()); 
        assertEquals(rigType1.getName(), res.getResourceName());
        assertEquals(rigType1.getId().intValue(), res.getResourceID());
        
        PermissionIDType pid = b.getPermissionID();
        assertNotNull(pid);
        assertEquals(perm1.getId().intValue(), pid.getPermissionID());
        
        Calendar startCal = b.getStartTime();
        assertNotNull(startCal);
        assertEquals(start.getTime() / 1000, startCal.getTimeInMillis() / 1000);
        
        Calendar endCal = b.getEndTime();
        assertNotNull(endCal);
        assertEquals(end.getTime() / 1000, endCal.getTimeInMillis() / 1000);
        
        assertEquals(bk1.getCodeReference(), b.getCodeReference());
        assertEquals(bk1.getCancelReason(), b.getCancelReason());
        assertTrue(b.getIsCancelled());
        assertTrue(b.getIsFinished());   
    }
    
    /**
     * Test method for {@link BookingsService#getBooking(types.GetBooking)}.
     */
    public void testGetBookingNotFound()
    {
        
        GetBooking request = new GetBooking();
        BookingRequestType reqTy = new BookingRequestType();
        BookingIDType id = new BookingIDType();
        reqTy.setBookingID(id);
        request.setGetBooking(reqTy);
        id.setBookingID(0);
        
        GetBookingResponse response = this.service.getBooking(request);

        
        assertNotNull(response);
        BookingType b = response.getGetBookingResponse();
        assertNotNull(b);
        
        assertEquals(-1, b.getBookingID());
        ResourceIDType res = b.getBookedResource();
        assertNotNull(res);
        assertNotNull(res.getType());
        
        PermissionIDType pid = b.getPermissionID();
        assertNotNull(pid);
        
        Calendar startCal = b.getStartTime();
        assertNotNull(startCal);
        
        Calendar endCal = b.getEndTime();
        assertNotNull(endCal);
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
        User us1 = new User();
        us1.setName("bktestuser1");
        us1.setNamespace("BKNS");
        us1.setPersona("USER");
        ses.save(us1);
        User us2 = new User();
        us2.setName("bktestuser2");
        us2.setNamespace("BKNS");
        us2.setPersona("USER");
        ses.save(us2);
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
        ResourcePermission perm2 = new ResourcePermission();
        perm2.setUserClass(uclass1);
        perm2.setType("TYPE");
        perm2.setSessionDuration(3600);
        perm2.setQueueActivityTimeout(300);
        perm2.setAllowedExtensions((short)10);
        perm2.setSessionActivityTimeout(300);
        perm2.setExtensionDuration(300);
        perm2.setMaximumBookings(10);
        perm2.setRigType(rigType1);
        perm2.setStartTime(new Date());
        perm2.setExpiryTime(new Date());
        perm2.setDisplayName("bookperm");
        ses.save(perm2);
        Bookings bk1 = new Bookings();
        bk1.setActive(true);
        bk1.setDuration(3600);
        Date start = new Date();
        bk1.setStartTime(start);
        Date end = new Date(System.currentTimeMillis() + 3600 * 1000);
        bk1.setEndTime(end);
        bk1.setResourcePermission(perm1);
        bk1.setResourceType("TYPE");
        bk1.setRigType(rigType1);
        bk1.setUser(us1);
        bk1.setUserName(us1.getName());
        bk1.setUserNamespace(us1.getNamespace());
        bk1.setCodeReference("/foo/bar");
        ses.save(bk1);
        Bookings bk2 = new Bookings();
        bk2.setActive(false);
        bk2.setCancelReason("Test cancel.");
        bk2.setDuration(3600);
        bk2.setStartTime(start);
        bk2.setEndTime(end);
        bk2.setResourcePermission(perm1);
        bk2.setResourceType("TYPE");
        bk2.setRigType(rigType1);
        bk2.setUser(us1);
        bk2.setUserName(us1.getName());
        bk2.setUserNamespace(us1.getNamespace());
        bk2.setCodeReference("/foo/bar");
        ses.save(bk2);
        Bookings bk3 = new Bookings();
        bk3.setActive(true);
        bk3.setDuration(3600);
        bk3.setStartTime(start);
        bk3.setEndTime(end);
        bk3.setResourcePermission(perm2);
        bk3.setResourceType("TYPE");
        bk3.setRigType(rigType1);
        bk3.setUser(us1);
        bk3.setUserName(us1.getName());
        bk3.setUserNamespace(us1.getNamespace());
        bk3.setCodeReference("/foo/bar");
        ses.save(bk3);
        Bookings bk4 = new Bookings();
        bk4.setActive(true);
        bk4.setDuration(3600);
        bk4.setStartTime(start);
        bk4.setEndTime(end);
        bk4.setResourcePermission(perm1);
        bk4.setResourceType("TYPE");
        bk4.setRigType(rigType1);
        bk4.setUser(us2);
        bk4.setUserName(us2.getName());
        bk4.setUserNamespace(us2.getNamespace());
        bk4.setCodeReference("/foo/bar");
        ses.save(bk4);
        Bookings bk5 = new Bookings();
        bk5.setActive(false);
        bk5.setDuration(3600);
        bk5.setStartTime(start);
        bk5.setEndTime(end);
        bk5.setResourcePermission(perm1);
        bk5.setResourceType("TYPE");
        bk5.setRigType(rigType1);
        bk5.setUser(us2);
        bk5.setUserName(us2.getName());
        bk5.setUserNamespace(us2.getNamespace());
        bk5.setCodeReference("/foo/bar");
        ses.save(bk5);
        Bookings bk6 = new Bookings();
        bk6.setActive(true);
        bk6.setDuration(3600);
        bk6.setStartTime(start);
        bk6.setEndTime(end);
        bk6.setResourcePermission(perm2);
        bk6.setResourceType("TYPE");
        bk6.setRigType(rigType1);
        bk6.setUser(us2);
        bk6.setUserName(us2.getName());
        bk6.setUserNamespace(us2.getNamespace());
        bk6.setCodeReference("/foo/bar");
        ses.save(bk6);
        ses.getTransaction().commit();
        
        GetBookings request = new GetBookings();
        BookingsRequestType reqTy = new BookingsRequestType();
        request.setGetBookings(reqTy);
        
        UserIDType uid = new UserIDType();
        uid.setUserQName(us1.getNamespace() + ':' + us1.getName());
        reqTy.setUserID(uid);
        
        PermissionIDType pid = new PermissionIDType();
        pid.setPermissionID(perm1.getId().intValue());
        reqTy.setPermissionID(pid);
        
        reqTy.setShowCancelled(false);
        reqTy.setShowFinished(false);
        
        GetBookingsResponse response = this.service.getBookings(request);
        
        ses.beginTransaction();
        ses.delete(bk6);
        ses.delete(bk5);
        ses.delete(bk4);
        ses.delete(bk3);
        ses.delete(bk2);
        ses.delete(bk1);
        ses.delete(perm1);
        ses.delete(perm2);
        ses.delete(rigType1);
        ses.delete(us2);
        ses.delete(us1);
        ses.delete(uclass1);
        ses.getTransaction().commit();
        
        assertNotNull(response);
        BookingListType bklist = response.getGetBookingsResponse();
        assertNotNull(bklist);
        
        BookingType[] bks = bklist.getBookings();
        assertEquals(1, bks.length);
        
        BookingType b = bks[0];
        assertNotNull(b);
        
        assertEquals(bk1.getId().intValue(), b.getBookingID());
        ResourceIDType res = b.getBookedResource();
        assertNotNull(res);
        assertEquals(perm1.getType(), res.getType()); 
        assertEquals(rigType1.getName(), res.getResourceName());
        assertEquals(rigType1.getId().intValue(), res.getResourceID());
        
        PermissionIDType pidty = b.getPermissionID();
        assertNotNull(pidty);
        assertEquals(perm1.getId().intValue(), pidty.getPermissionID());
        
        Calendar startCal = b.getStartTime();
        assertNotNull(startCal);
        assertEquals(start.getTime() / 1000, startCal.getTimeInMillis() / 1000);
        
        Calendar endCal = b.getEndTime();
        assertNotNull(endCal);
        assertEquals(end.getTime() / 1000, endCal.getTimeInMillis() / 1000);
        
        assertEquals(bk1.getCodeReference(), b.getCodeReference());
        assertNull(bk1.getCancelReason());
        assertFalse(b.getIsCancelled());
        assertFalse(b.getIsFinished());
    }
    
    /**
     * Test method for {@link BookingsService#getBookings(types.GetBookings)}.
     */
    public void testGetBookingsCancelled()
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
        User us1 = new User();
        us1.setName("bktestuser1");
        us1.setNamespace("BKNS");
        us1.setPersona("USER");
        ses.save(us1);
        User us2 = new User();
        us2.setName("bktestuser2");
        us2.setNamespace("BKNS");
        us2.setPersona("USER");
        ses.save(us2);
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
        ResourcePermission perm2 = new ResourcePermission();
        perm2.setUserClass(uclass1);
        perm2.setType("TYPE");
        perm2.setSessionDuration(3600);
        perm2.setQueueActivityTimeout(300);
        perm2.setAllowedExtensions((short)10);
        perm2.setSessionActivityTimeout(300);
        perm2.setExtensionDuration(300);
        perm2.setMaximumBookings(10);
        perm2.setRigType(rigType1);
        perm2.setStartTime(new Date());
        perm2.setExpiryTime(new Date());
        perm2.setDisplayName("bookperm");
        ses.save(perm2);
        Bookings bk1 = new Bookings();
        bk1.setActive(true);
        bk1.setDuration(3600);
        Date start = new Date();
        bk1.setStartTime(start);
        Date end = new Date(System.currentTimeMillis() + 3600 * 1000);
        bk1.setEndTime(end);
        bk1.setResourcePermission(perm1);
        bk1.setResourceType("TYPE");
        bk1.setRigType(rigType1);
        bk1.setUser(us1);
        bk1.setUserName(us1.getName());
        bk1.setUserNamespace(us1.getNamespace());
        bk1.setCodeReference("/foo/bar");
        ses.save(bk1);
        Bookings bk2 = new Bookings();
        bk2.setActive(false);
        bk2.setCancelReason("Test cancel.");
        bk2.setDuration(3600);
        Date startLt = new Date(start.getTime() + 100000);
        bk2.setStartTime(startLt);
        bk2.setEndTime(end);
        bk2.setResourcePermission(perm1);
        bk2.setResourceType("TYPE");
        bk2.setRigType(rigType1);
        bk2.setUser(us1);
        bk2.setUserName(us1.getName());
        bk2.setUserNamespace(us1.getNamespace());
        bk2.setCodeReference("/foo/bar");
        ses.save(bk2);
        Bookings bk3 = new Bookings();
        bk3.setActive(true);
        bk3.setDuration(3600);
        bk3.setStartTime(start);
        bk3.setEndTime(end);
        bk3.setResourcePermission(perm2);
        bk3.setResourceType("TYPE");
        bk3.setRigType(rigType1);
        bk3.setUser(us1);
        bk3.setUserName(us1.getName());
        bk3.setUserNamespace(us1.getNamespace());
        bk3.setCodeReference("/foo/bar");
        ses.save(bk3);
        Bookings bk4 = new Bookings();
        bk4.setActive(true);
        bk4.setDuration(3600);
        bk4.setStartTime(start);
        bk4.setEndTime(end);
        bk4.setResourcePermission(perm1);
        bk4.setResourceType("TYPE");
        bk4.setRigType(rigType1);
        bk4.setUser(us2);
        bk4.setUserName(us2.getName());
        bk4.setUserNamespace(us2.getNamespace());
        bk4.setCodeReference("/foo/bar");
        ses.save(bk4);
        Bookings bk5 = new Bookings();
        bk5.setActive(false);
        bk5.setDuration(3600);
        bk5.setStartTime(start);
        bk5.setEndTime(end);
        bk5.setResourcePermission(perm1);
        bk5.setResourceType("TYPE");
        bk5.setRigType(rigType1);
        bk5.setUser(us2);
        bk5.setUserName(us2.getName());
        bk5.setUserNamespace(us2.getNamespace());
        bk5.setCodeReference("/foo/bar");
        ses.save(bk5);
        Bookings bk6 = new Bookings();
        bk6.setActive(true);
        bk6.setDuration(3600);
        bk6.setStartTime(start);
        bk6.setEndTime(end);
        bk6.setResourcePermission(perm2);
        bk6.setResourceType("TYPE");
        bk6.setRigType(rigType1);
        bk6.setUser(us2);
        bk6.setUserName(us2.getName());
        bk6.setUserNamespace(us2.getNamespace());
        bk6.setCodeReference("/foo/bar");
        ses.save(bk6);
        ses.getTransaction().commit();
        
        GetBookings request = new GetBookings();
        BookingsRequestType reqTy = new BookingsRequestType();
        request.setGetBookings(reqTy);
        
        UserIDType uid = new UserIDType();
        uid.setUserQName(us1.getNamespace() + ':' + us1.getName());
        reqTy.setUserID(uid);
        
        PermissionIDType pid = new PermissionIDType();
        pid.setPermissionID(perm1.getId().intValue());
        reqTy.setPermissionID(pid);
        
        reqTy.setShowCancelled(true);
        reqTy.setShowFinished(true);
        
        GetBookingsResponse response = this.service.getBookings(request);
        
        ses.beginTransaction();
        ses.delete(bk6);
        ses.delete(bk5);
        ses.delete(bk4);
        ses.delete(bk3);
        ses.delete(bk2);
        ses.delete(bk1);
        ses.delete(perm1);
        ses.delete(perm2);
        ses.delete(rigType1);
        ses.delete(us2);
        ses.delete(us1);
        ses.delete(uclass1);
        ses.getTransaction().commit();
        
        assertNotNull(response);
        BookingListType bklist = response.getGetBookingsResponse();
        assertNotNull(bklist);
        
        BookingType[] bks = bklist.getBookings();
        assertEquals(2, bks.length);
        
        BookingType b = bks[0];
        assertNotNull(b);
        
        assertEquals(bk1.getId().intValue(), b.getBookingID());
        ResourceIDType res = b.getBookedResource();
        assertNotNull(res);
        assertEquals(perm1.getType(), res.getType()); 
        assertEquals(rigType1.getName(), res.getResourceName());
        assertEquals(rigType1.getId().intValue(), res.getResourceID());
        
        PermissionIDType pidty = b.getPermissionID();
        assertNotNull(pidty);
        assertEquals(perm1.getId().intValue(), pidty.getPermissionID());
        
        Calendar startCal = b.getStartTime();
        assertNotNull(startCal);
        assertEquals(start.getTime() / 1000, startCal.getTimeInMillis() / 1000);
        
        Calendar endCal = b.getEndTime();
        assertNotNull(endCal);
        assertEquals(end.getTime() / 1000, endCal.getTimeInMillis() / 1000);
        
        assertEquals(bk1.getCodeReference(), b.getCodeReference());
        assertNull(bk1.getCancelReason());
        assertFalse(b.getIsCancelled());
        assertFalse(b.getIsFinished());
        
        BookingType b2 = bks[1];
        assertNotNull(b2);
        
        assertEquals(bk2.getId().intValue(), b2.getBookingID());
        ResourceIDType res2 = b.getBookedResource();
        assertNotNull(res2);
        assertEquals(perm1.getType(), res2.getType()); 
        assertEquals(rigType1.getName(), res2.getResourceName());
        assertEquals(rigType1.getId().intValue(), res2.getResourceID());
        
        PermissionIDType pidty2 = b2.getPermissionID();
        assertNotNull(pidty2);
        assertEquals(perm1.getId().intValue(), pidty2.getPermissionID());
    
        assertEquals(bk2.getCancelReason(), b2.getCancelReason());
        assertTrue(b2.getIsCancelled());
        assertTrue(b2.getIsFinished()); 
    }
    
    /**
     * Test method for {@link BookingsService#getBookings(types.GetBookings)}.
     */
    public void testGetBookingsPermission()
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
        User us1 = new User();
        us1.setName("bktestuser1");
        us1.setNamespace("BKNS");
        us1.setPersona("USER");
        ses.save(us1);
        User us2 = new User();
        us2.setName("bktestuser2");
        us2.setNamespace("BKNS");
        us2.setPersona("USER");
        ses.save(us2);
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
        ResourcePermission perm2 = new ResourcePermission();
        perm2.setUserClass(uclass1);
        perm2.setType("TYPE");
        perm2.setSessionDuration(3600);
        perm2.setQueueActivityTimeout(300);
        perm2.setAllowedExtensions((short)10);
        perm2.setSessionActivityTimeout(300);
        perm2.setExtensionDuration(300);
        perm2.setMaximumBookings(10);
        perm2.setRigType(rigType1);
        perm2.setStartTime(new Date());
        perm2.setExpiryTime(new Date());
        perm2.setDisplayName("bookperm");
        ses.save(perm2);
        Bookings bk1 = new Bookings();
        bk1.setActive(true);
        bk1.setDuration(3600);
        Date start = new Date();
        bk1.setStartTime(start);
        Date end = new Date(System.currentTimeMillis() + 3600 * 1000);
        bk1.setEndTime(end);
        bk1.setResourcePermission(perm1);
        bk1.setResourceType("TYPE");
        bk1.setRigType(rigType1);
        bk1.setUser(us1);
        bk1.setUserName(us1.getName());
        bk1.setUserNamespace(us1.getNamespace());
        bk1.setCodeReference("/foo/bar");
        ses.save(bk1);
        Bookings bk2 = new Bookings();
        bk2.setActive(false);
        bk2.setCancelReason("Test cancel.");
        bk2.setDuration(3600);
        Date startLt = new Date(start.getTime() + 100000);
        bk2.setStartTime(startLt);
        bk2.setEndTime(end);
        bk2.setResourcePermission(perm1);
        bk2.setResourceType("TYPE");
        bk2.setRigType(rigType1);
        bk2.setUser(us1);
        bk2.setUserName(us1.getName());
        bk2.setUserNamespace(us1.getNamespace());
        bk2.setCodeReference("/foo/bar");
        ses.save(bk2);
        Bookings bk3 = new Bookings();
        bk3.setActive(true);
        bk3.setDuration(3600);
        bk3.setStartTime(start);
        bk3.setEndTime(end);
        bk3.setResourcePermission(perm2);
        bk3.setResourceType("TYPE");
        bk3.setRigType(rigType1);
        bk3.setUser(us1);
        bk3.setUserName(us1.getName());
        bk3.setUserNamespace(us1.getNamespace());
        bk3.setCodeReference("/foo/bar");
        ses.save(bk3);
        Bookings bk4 = new Bookings();
        bk4.setActive(true);
        bk4.setDuration(3600);
        bk4.setStartTime(start);
        bk4.setEndTime(end);
        bk4.setResourcePermission(perm1);
        bk4.setResourceType("TYPE");
        bk4.setRigType(rigType1);
        bk4.setUser(us2);
        bk4.setUserName(us2.getName());
        bk4.setUserNamespace(us2.getNamespace());
        bk4.setCodeReference("/foo/bar");
        ses.save(bk4);
        Bookings bk5 = new Bookings();
        bk5.setActive(false);
        bk5.setDuration(3600);
        bk5.setStartTime(start);
        bk5.setEndTime(end);
        bk5.setResourcePermission(perm1);
        bk5.setResourceType("TYPE");
        bk5.setRigType(rigType1);
        bk5.setUser(us2);
        bk5.setUserName(us2.getName());
        bk5.setUserNamespace(us2.getNamespace());
        bk5.setCodeReference("/foo/bar");
        ses.save(bk5);
        Bookings bk6 = new Bookings();
        bk6.setActive(true);
        bk6.setDuration(3600);
        bk6.setStartTime(start);
        bk6.setEndTime(end);
        bk6.setResourcePermission(perm2);
        bk6.setResourceType("TYPE");
        bk6.setRigType(rigType1);
        bk6.setUser(us2);
        bk6.setUserName(us2.getName());
        bk6.setUserNamespace(us2.getNamespace());
        bk6.setCodeReference("/foo/bar");
        ses.save(bk6);
        ses.getTransaction().commit();
        
        GetBookings request = new GetBookings();
        BookingsRequestType reqTy = new BookingsRequestType();
        request.setGetBookings(reqTy);
        
        PermissionIDType pid = new PermissionIDType();
        pid.setPermissionID(perm1.getId().intValue());
        reqTy.setPermissionID(pid);
        
        reqTy.setShowCancelled(true);
        reqTy.setShowFinished(true);
        
        GetBookingsResponse response = this.service.getBookings(request);
        
        ses.beginTransaction();
        ses.delete(bk6);
        ses.delete(bk5);
        ses.delete(bk4);
        ses.delete(bk3);
        ses.delete(bk2);
        ses.delete(bk1);
        ses.delete(perm1);
        ses.delete(perm2);
        ses.delete(rigType1);
        ses.delete(us2);
        ses.delete(us1);
        ses.delete(uclass1);
        ses.getTransaction().commit();
        
        assertNotNull(response);
        BookingListType bklist = response.getGetBookingsResponse();
        assertNotNull(bklist);
        
        BookingType[] bks = bklist.getBookings();
        assertEquals(4, bks.length);
    }
    
    /**
     * Test method for {@link BookingsService#getBookings(types.GetBookings)}.
     */
    public void testGetBookingsResourceID()
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
        User us1 = new User();
        us1.setName("bktestuser1");
        us1.setNamespace("BKNS");
        us1.setPersona("USER");
        ses.save(us1);
        User us2 = new User();
        us2.setName("bktestuser2");
        us2.setNamespace("BKNS");
        us2.setPersona("USER");
        ses.save(us2);
        RigType rigType1 = new RigType("booktestrigtype", 300, false);
        ses.save(rigType1);
        RigType rigType2 = new RigType("booktestrigtype2", 300, false);
        ses.save(rigType2);
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
        ResourcePermission perm2 = new ResourcePermission();
        perm2.setUserClass(uclass1);
        perm2.setType("TYPE");
        perm2.setSessionDuration(3600);
        perm2.setQueueActivityTimeout(300);
        perm2.setAllowedExtensions((short)10);
        perm2.setSessionActivityTimeout(300);
        perm2.setExtensionDuration(300);
        perm2.setMaximumBookings(10);
        perm2.setRigType(rigType2);
        perm2.setStartTime(new Date());
        perm2.setExpiryTime(new Date());
        perm2.setDisplayName("bookperm");
        ses.save(perm2);
        Bookings bk1 = new Bookings();
        bk1.setActive(true);
        bk1.setDuration(3600);
        Date start = new Date();
        bk1.setStartTime(start);
        Date end = new Date(System.currentTimeMillis() + 3600 * 1000);
        bk1.setEndTime(end);
        bk1.setResourcePermission(perm1);
        bk1.setResourceType("TYPE");
        bk1.setRigType(rigType1);
        bk1.setUser(us1);
        bk1.setUserName(us1.getName());
        bk1.setUserNamespace(us1.getNamespace());
        bk1.setCodeReference("/foo/bar");
        ses.save(bk1);
        Bookings bk2 = new Bookings();
        bk2.setActive(false);
        bk2.setCancelReason("Test cancel.");
        bk2.setDuration(3600);
        Date startLt = new Date(start.getTime() + 100000);
        bk2.setStartTime(startLt);
        bk2.setEndTime(end);
        bk2.setResourcePermission(perm1);
        bk2.setResourceType("TYPE");
        bk2.setRigType(rigType1);
        bk2.setUser(us1);
        bk2.setUserName(us1.getName());
        bk2.setUserNamespace(us1.getNamespace());
        bk2.setCodeReference("/foo/bar");
        ses.save(bk2);
        Bookings bk3 = new Bookings();
        bk3.setActive(true);
        bk3.setDuration(3600);
        bk3.setStartTime(start);
        bk3.setEndTime(end);
        bk3.setResourcePermission(perm1);
        bk3.setResourceType("TYPE");
        bk3.setRigType(rigType1);
        bk3.setUser(us1);
        bk3.setUserName(us1.getName());
        bk3.setUserNamespace(us1.getNamespace());
        bk3.setCodeReference("/foo/bar");
        ses.save(bk3);
        Bookings bk4 = new Bookings();
        bk4.setActive(true);
        bk4.setDuration(3600);
        bk4.setStartTime(start);
        bk4.setEndTime(end);
        bk4.setResourcePermission(perm1);
        bk4.setResourceType("TYPE");
        bk4.setRigType(rigType1);
        bk4.setUser(us2);
        bk4.setUserName(us2.getName());
        bk4.setUserNamespace(us2.getNamespace());
        bk4.setCodeReference("/foo/bar");
        ses.save(bk4);
        Bookings bk5 = new Bookings();
        bk5.setActive(false);
        bk5.setDuration(3600);
        bk5.setStartTime(start);
        bk5.setEndTime(end);
        bk5.setResourcePermission(perm2);
        bk5.setResourceType("TYPE");
        bk5.setRigType(rigType2);
        bk5.setUser(us2);
        bk5.setUserName(us2.getName());
        bk5.setUserNamespace(us2.getNamespace());
        bk5.setCodeReference("/foo/bar");
        ses.save(bk5);
        Bookings bk6 = new Bookings();
        bk6.setActive(true);
        bk6.setDuration(3600);
        bk6.setStartTime(start);
        bk6.setEndTime(end);
        bk6.setResourcePermission(perm2);
        bk6.setResourceType("TYPE");
        bk6.setRigType(rigType2);
        bk6.setUser(us2);
        bk6.setUserName(us2.getName());
        bk6.setUserNamespace(us2.getNamespace());
        bk6.setCodeReference("/foo/bar");
        ses.save(bk6);
        ses.getTransaction().commit();
        
        GetBookings request = new GetBookings();
        BookingsRequestType reqTy = new BookingsRequestType();
        request.setGetBookings(reqTy);
        
        ResourceIDType rid = new ResourceIDType();
        rid.setType("TYPE");
        rid.setResourceID(rigType1.getId().intValue());
        reqTy.setResourceID(rid);
        
        reqTy.setShowCancelled(true);
        reqTy.setShowFinished(true);
        
        GetBookingsResponse response = this.service.getBookings(request);
        
        ses.beginTransaction();
        ses.delete(bk6);
        ses.delete(bk5);
        ses.delete(bk4);
        ses.delete(bk3);
        ses.delete(bk2);
        ses.delete(bk1);
        ses.delete(perm1);
        ses.delete(perm2);
        ses.delete(rigType1);
        ses.delete(rigType2);
        ses.delete(us2);
        ses.delete(us1);
        ses.delete(uclass1);
        ses.getTransaction().commit();
        
        assertNotNull(response);
        BookingListType bklist = response.getGetBookingsResponse();
        assertNotNull(bklist);
        
        BookingType[] bks = bklist.getBookings();
        assertEquals(4, bks.length);
    }
    
    /**
     * Test method for {@link BookingsService#getBookings(types.GetBookings)}.
     */
    public void testGetBookingsResourceName()
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
        User us1 = new User();
        us1.setName("bktestuser1");
        us1.setNamespace("BKNS");
        us1.setPersona("USER");
        ses.save(us1);
        User us2 = new User();
        us2.setName("bktestuser2");
        us2.setNamespace("BKNS");
        us2.setPersona("USER");
        ses.save(us2);
        RigType rigType1 = new RigType("booktestrigtype", 300, false);
        ses.save(rigType1);
        RigType rigType2 = new RigType("booktestrigtype2", 300, false);
        ses.save(rigType2);
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
        ResourcePermission perm2 = new ResourcePermission();
        perm2.setUserClass(uclass1);
        perm2.setType("TYPE");
        perm2.setSessionDuration(3600);
        perm2.setQueueActivityTimeout(300);
        perm2.setAllowedExtensions((short)10);
        perm2.setSessionActivityTimeout(300);
        perm2.setExtensionDuration(300);
        perm2.setMaximumBookings(10);
        perm2.setRigType(rigType2);
        perm2.setStartTime(new Date());
        perm2.setExpiryTime(new Date());
        perm2.setDisplayName("bookperm");
        ses.save(perm2);
        Bookings bk1 = new Bookings();
        bk1.setActive(true);
        bk1.setDuration(3600);
        Date start = new Date();
        bk1.setStartTime(start);
        Date end = new Date(System.currentTimeMillis() + 3600 * 1000);
        bk1.setEndTime(end);
        bk1.setResourcePermission(perm1);
        bk1.setResourceType("TYPE");
        bk1.setRigType(rigType1);
        bk1.setUser(us1);
        bk1.setUserName(us1.getName());
        bk1.setUserNamespace(us1.getNamespace());
        bk1.setCodeReference("/foo/bar");
        ses.save(bk1);
        Bookings bk2 = new Bookings();
        bk2.setActive(false);
        bk2.setCancelReason("Test cancel.");
        bk2.setDuration(3600);
        Date startLt = new Date(start.getTime() + 100000);
        bk2.setStartTime(startLt);
        bk2.setEndTime(end);
        bk2.setResourcePermission(perm1);
        bk2.setResourceType("TYPE");
        bk2.setRigType(rigType1);
        bk2.setUser(us1);
        bk2.setUserName(us1.getName());
        bk2.setUserNamespace(us1.getNamespace());
        bk2.setCodeReference("/foo/bar");
        ses.save(bk2);
        Bookings bk3 = new Bookings();
        bk3.setActive(true);
        bk3.setDuration(3600);
        bk3.setStartTime(start);
        bk3.setEndTime(end);
        bk3.setResourcePermission(perm2);
        bk3.setResourceType("TYPE");
        bk3.setRigType(rigType1);
        bk3.setUser(us1);
        bk3.setUserName(us1.getName());
        bk3.setUserNamespace(us1.getNamespace());
        bk3.setCodeReference("/foo/bar");
        ses.save(bk3);
        Bookings bk4 = new Bookings();
        bk4.setActive(true);
        bk4.setDuration(3600);
        bk4.setStartTime(start);
        bk4.setEndTime(end);
        bk4.setResourcePermission(perm1);
        bk4.setResourceType("TYPE");
        bk4.setRigType(rigType1);
        bk4.setUser(us2);
        bk4.setUserName(us2.getName());
        bk4.setUserNamespace(us2.getNamespace());
        bk4.setCodeReference("/foo/bar");
        ses.save(bk4);
        Bookings bk5 = new Bookings();
        bk5.setActive(false);
        bk5.setDuration(3600);
        bk5.setStartTime(start);
        bk5.setEndTime(end);
        bk5.setResourcePermission(perm2);
        bk5.setResourceType("TYPE");
        bk5.setRigType(rigType2);
        bk5.setUser(us2);
        bk5.setUserName(us2.getName());
        bk5.setUserNamespace(us2.getNamespace());
        bk5.setCodeReference("/foo/bar");
        ses.save(bk5);
        Bookings bk6 = new Bookings();
        bk6.setActive(true);
        bk6.setDuration(3600);
        bk6.setStartTime(new Date(start.getTime() + 100));
        bk6.setEndTime(end);
        bk6.setResourcePermission(perm2);
        bk6.setResourceType("TYPE");
        bk6.setRigType(rigType2);
        bk6.setUser(us2);
        bk6.setUserName(us2.getName());
        bk6.setUserNamespace(us2.getNamespace());
        bk6.setCodeReference("/foo/bar");
        ses.save(bk6);
        ses.getTransaction().commit();
        
        GetBookings request = new GetBookings();
        BookingsRequestType reqTy = new BookingsRequestType();
        request.setGetBookings(reqTy);
        
        ResourceIDType rid = new ResourceIDType();
        rid.setType("TYPE");
        rid.setResourceName(rigType2.getName());
        reqTy.setResourceID(rid);
        
        reqTy.setShowCancelled(true);
        reqTy.setShowFinished(true);
        
        GetBookingsResponse response = this.service.getBookings(request);
        
        ses.beginTransaction();
        ses.delete(bk6);
        ses.delete(bk5);
        ses.delete(bk4);
        ses.delete(bk3);
        ses.delete(bk2);
        ses.delete(bk1);
        ses.delete(perm1);
        ses.delete(perm2);
        ses.delete(rigType1);
        ses.delete(rigType2);
        ses.delete(us2);
        ses.delete(us1);
        ses.delete(uclass1);
        ses.getTransaction().commit();
        
        assertNotNull(response);
        BookingListType bklist = response.getGetBookingsResponse();
        assertNotNull(bklist);
        
        BookingType[] bks = bklist.getBookings();
        assertEquals(2, bks.length);
        BookingType bt = bks[0];
        assertNotNull(bt);
        assertEquals(bt.getBookingID(), bk5.getId().intValue());
        bt = bks[1];
        assertNotNull(bt);
        assertEquals(bt.getBookingID(), bk6.getId().intValue());
    }
    
    /**
     * Test method for {@link BookingsService#getBookings(types.GetBookings)}.
     */
    public void testGetBookingsResourceRig()
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
        User us1 = new User();
        us1.setName("bktestuser1");
        us1.setNamespace("BKNS");
        us1.setPersona("USER");
        ses.save(us1);
        User us2 = new User();
        us2.setName("bktestuser2");
        us2.setNamespace("BKNS");
        us2.setPersona("USER");
        ses.save(us2);
        RigType rigType1 = new RigType("booktestrigtype", 300, false);
        ses.save(rigType1);
        RigType rigType2 = new RigType("booktestrigtype2", 300, false);
        ses.save(rigType2);
        RigCapabilities caps = new RigCapabilities("a,b,c,d");
        ses.save(caps);
        Rig rig = new Rig();
        rig.setName("booktestrig");
        rig.setLastUpdateTimestamp(new Date());
        rig.setRigCapabilities(caps);
        rig.setRigType(rigType1);
        ses.save(rig);
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
        ResourcePermission perm2 = new ResourcePermission();
        perm2.setUserClass(uclass1);
        perm2.setType("RIG");
        perm2.setSessionDuration(3600);
        perm2.setQueueActivityTimeout(300);
        perm2.setAllowedExtensions((short)10);
        perm2.setSessionActivityTimeout(300);
        perm2.setExtensionDuration(300);
        perm2.setMaximumBookings(10);
        perm2.setRig(rig);
        perm2.setStartTime(new Date());
        perm2.setExpiryTime(new Date());
        perm2.setDisplayName("bookperm");
        ses.save(perm2);
        Bookings bk1 = new Bookings();
        bk1.setActive(true);
        bk1.setDuration(3600);
        Date start = new Date();
        bk1.setStartTime(start);
        Date end = new Date(System.currentTimeMillis() + 3600 * 1000);
        bk1.setEndTime(end);
        bk1.setResourcePermission(perm1);
        bk1.setResourceType("TYPE");
        bk1.setRigType(rigType1);
        bk1.setUser(us1);
        bk1.setUserName(us1.getName());
        bk1.setUserNamespace(us1.getNamespace());
        bk1.setCodeReference("/foo/bar");
        ses.save(bk1);
        Bookings bk2 = new Bookings();
        bk2.setActive(false);
        bk2.setCancelReason("Test cancel.");
        bk2.setDuration(3600);
        Date startLt = new Date(start.getTime() + 100000);
        bk2.setStartTime(startLt);
        bk2.setEndTime(end);
        bk2.setResourcePermission(perm1);
        bk2.setResourceType("TYPE");
        bk2.setRigType(rigType1);
        bk2.setUser(us1);
        bk2.setUserName(us1.getName());
        bk2.setUserNamespace(us1.getNamespace());
        bk2.setCodeReference("/foo/bar");
        ses.save(bk2);
        Bookings bk3 = new Bookings();
        bk3.setActive(true);
        bk3.setDuration(3600);
        bk3.setStartTime(start);
        bk3.setEndTime(end);
        bk3.setResourcePermission(perm2);
        bk3.setResourceType("TYPE");
        bk3.setRigType(rigType1);
        bk3.setUser(us1);
        bk3.setUserName(us1.getName());
        bk3.setUserNamespace(us1.getNamespace());
        bk3.setCodeReference("/foo/bar");
        ses.save(bk3);
        Bookings bk4 = new Bookings();
        bk4.setActive(true);
        bk4.setDuration(3600);
        bk4.setStartTime(start);
        bk4.setEndTime(end);
        bk4.setResourcePermission(perm1);
        bk4.setResourceType("TYPE");
        bk4.setRigType(rigType1);
        bk4.setUser(us2);
        bk4.setUserName(us2.getName());
        bk4.setUserNamespace(us2.getNamespace());
        bk4.setCodeReference("/foo/bar");
        ses.save(bk4);
        Bookings bk5 = new Bookings();
        bk5.setActive(false);
        bk5.setDuration(3600);
        bk5.setStartTime(start);
        bk5.setEndTime(end);
        bk5.setResourcePermission(perm1);
        bk5.setResourceType("TYPE");
        bk5.setRigType(rigType1);
        bk5.setUser(us2);
        bk5.setUserName(us2.getName());
        bk5.setUserNamespace(us2.getNamespace());
        bk5.setCodeReference("/foo/bar");
        ses.save(bk5);
        Bookings bk6 = new Bookings();
        bk6.setActive(true);
        bk6.setDuration(3600);
        bk6.setStartTime(new Date(start.getTime() + 100));
        bk6.setEndTime(end);
        bk6.setResourcePermission(perm2);
        bk6.setResourceType("RIG");
        bk6.setRig(rig);
        bk6.setUser(us2);
        bk6.setUserName(us2.getName());
        bk6.setUserNamespace(us2.getNamespace());
        bk6.setCodeReference("/foo/bar");
        ses.save(bk6);
        ses.getTransaction().commit();
        
        GetBookings request = new GetBookings();
        BookingsRequestType reqTy = new BookingsRequestType();
        request.setGetBookings(reqTy);
        
        ResourceIDType rid = new ResourceIDType();
        rid.setType("RIG");
        rid.setResourceName(rig.getName());
        reqTy.setResourceID(rid);
        
        reqTy.setShowCancelled(true);
        reqTy.setShowFinished(true);
        
        GetBookingsResponse response = this.service.getBookings(request);
        
        ses.beginTransaction();
        ses.delete(bk6);
        ses.delete(bk5);
        ses.delete(bk4);
        ses.delete(bk3);
        ses.delete(bk2);
        ses.delete(bk1);
        ses.delete(perm1);
        ses.delete(perm2);
        ses.delete(rig);
        ses.delete(caps);
        ses.delete(rigType1);
        ses.delete(rigType2);
        ses.delete(us2);
        ses.delete(us1);
        ses.delete(uclass1);
        ses.getTransaction().commit();
        
        assertNotNull(response);
        BookingListType bklist = response.getGetBookingsResponse();
        assertNotNull(bklist);
        
        BookingType[] bks = bklist.getBookings();
        assertEquals(1, bks.length);
        BookingType bt = bks[0];
        assertNotNull(bt);
        assertEquals(bt.getBookingID(), bk6.getId().intValue());
    }
    
    /**
     * Test method for {@link BookingsService#getBookings(types.GetBookings)}.
     */
    public void testGetBookingsResourceCaps()
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
        User us1 = new User();
        us1.setName("bktestuser1");
        us1.setNamespace("BKNS");
        us1.setPersona("USER");
        ses.save(us1);
        User us2 = new User();
        us2.setName("bktestuser2");
        us2.setNamespace("BKNS");
        us2.setPersona("USER");
        ses.save(us2);
        RigType rigType1 = new RigType("booktestrigtype", 300, false);
        ses.save(rigType1);
        RequestCapabilities rcaps = new RequestCapabilities("a,b,c,d");
        ses.save(rcaps);
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
        ResourcePermission perm2 = new ResourcePermission();
        perm2.setUserClass(uclass1);
        perm2.setType("CAPABILITY");
        perm2.setSessionDuration(3600);
        perm2.setQueueActivityTimeout(300);
        perm2.setAllowedExtensions((short)10);
        perm2.setSessionActivityTimeout(300);
        perm2.setExtensionDuration(300);
        perm2.setMaximumBookings(10);
        perm2.setRequestCapabilities(rcaps);
        perm2.setStartTime(new Date());
        perm2.setExpiryTime(new Date());
        perm2.setDisplayName("bookperm");
        ses.save(perm2);
        Bookings bk1 = new Bookings();
        bk1.setActive(true);
        bk1.setDuration(3600);
        Date start = new Date();
        bk1.setStartTime(start);
        Date end = new Date(System.currentTimeMillis() + 3600 * 1000);
        bk1.setEndTime(end);
        bk1.setResourcePermission(perm1);
        bk1.setResourceType("TYPE");
        bk1.setRigType(rigType1);
        bk1.setUser(us1);
        bk1.setUserName(us1.getName());
        bk1.setUserNamespace(us1.getNamespace());
        bk1.setCodeReference("/foo/bar");
        ses.save(bk1);
        Bookings bk2 = new Bookings();
        bk2.setActive(false);
        bk2.setCancelReason("Test cancel.");
        bk2.setDuration(3600);
        Date startLt = new Date(start.getTime() + 100000);
        bk2.setStartTime(startLt);
        bk2.setEndTime(end);
        bk2.setResourcePermission(perm1);
        bk2.setResourceType("TYPE");
        bk2.setRigType(rigType1);
        bk2.setUser(us1);
        bk2.setUserName(us1.getName());
        bk2.setUserNamespace(us1.getNamespace());
        bk2.setCodeReference("/foo/bar");
        ses.save(bk2);
        Bookings bk3 = new Bookings();
        bk3.setActive(true);
        bk3.setDuration(3600);
        bk3.setStartTime(start);
        bk3.setEndTime(end);
        bk3.setResourcePermission(perm2);
        bk3.setResourceType("TYPE");
        bk3.setRigType(rigType1);
        bk3.setUser(us1);
        bk3.setUserName(us1.getName());
        bk3.setUserNamespace(us1.getNamespace());
        bk3.setCodeReference("/foo/bar");
        ses.save(bk3);
        Bookings bk4 = new Bookings();
        bk4.setActive(true);
        bk4.setDuration(3600);
        bk4.setStartTime(start);
        bk4.setEndTime(end);
        bk4.setResourcePermission(perm1);
        bk4.setResourceType("TYPE");
        bk4.setRigType(rigType1);
        bk4.setUser(us2);
        bk4.setUserName(us2.getName());
        bk4.setUserNamespace(us2.getNamespace());
        bk4.setCodeReference("/foo/bar");
        ses.save(bk4);
        Bookings bk5 = new Bookings();
        bk5.setActive(false);
        bk5.setDuration(3600);
        bk5.setStartTime(start);
        bk5.setEndTime(end);
        bk5.setResourcePermission(perm2);
        bk5.setResourceType("CAPABILITY");
        bk5.setRequestCapabilities(rcaps);
        bk5.setUser(us2);
        bk5.setUserName(us2.getName());
        bk5.setUserNamespace(us2.getNamespace());
        bk5.setCodeReference("/foo/bar");
        ses.save(bk5);
        Bookings bk6 = new Bookings();
        bk6.setActive(true);
        bk6.setDuration(3600);
        bk6.setStartTime(new Date(start.getTime() + 100));
        bk6.setEndTime(end);
        bk6.setResourcePermission(perm1);
        bk6.setResourceType("TYPE");
        bk6.setRigType(rigType1);
        bk6.setUser(us2);
        bk6.setUserName(us2.getName());
        bk6.setUserNamespace(us2.getNamespace());
        bk6.setCodeReference("/foo/bar");
        ses.save(bk6);
        ses.getTransaction().commit();
        
        GetBookings request = new GetBookings();
        BookingsRequestType reqTy = new BookingsRequestType();
        request.setGetBookings(reqTy);
        
        ResourceIDType rid = new ResourceIDType();
        rid.setType("CAPABILITY");
        rid.setResourceName(rcaps.getCapabilities());
        reqTy.setResourceID(rid);
        
        reqTy.setShowCancelled(true);
        reqTy.setShowFinished(true);
        
        GetBookingsResponse response = this.service.getBookings(request);
        
        ses.beginTransaction();
        ses.delete(bk6);
        ses.delete(bk5);
        ses.delete(bk4);
        ses.delete(bk3);
        ses.delete(bk2);
        ses.delete(bk1);
        ses.delete(perm1);
        ses.delete(perm2);
        ses.delete(rigType1);
        ses.delete(rcaps);
        ses.delete(us2);
        ses.delete(us1);
        ses.delete(uclass1);
        ses.getTransaction().commit();
        
        assertNotNull(response);
        BookingListType bklist = response.getGetBookingsResponse();
        assertNotNull(bklist);
        
        BookingType[] bks = bklist.getBookings();
        assertEquals(1, bks.length);
        BookingType bt = bks[0];
        assertNotNull(bt);
        assertEquals(bt.getBookingID(), bk5.getId().intValue());
    }
}
