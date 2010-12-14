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

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.hibernate.Session;
import org.junit.Test;

import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine.SlotBookingEngine;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine.TimeUtil;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.BookingsService;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.BookingIDType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.BookingListType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.BookingRequestType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.BookingSlotListType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.BookingSlotType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.BookingType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.BookingsRequestType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.FindBookingSlotType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.FindFreeBookings;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.FindFreeBookingsResponse;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.GetBooking;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.GetBookingResponse;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.GetBookings;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.GetBookingsResponse;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.PermissionIDType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.ResourceIDType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.SlotState;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.TimePeriodType;
import au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types.UserIDType;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Bookings;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.MatchingCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RequestCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserAssociation;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserAssociationId;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClass;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.testsetup.DataAccessTestSetup;

/**
 * Tests the {@link BookingsService} class.
 */
public class BookingsServiceTester extends TestCase
{
    /** Object of class under test. */
    private BookingsService service;
    
    /** A day string. */
    private String dayStr;

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        
        DataAccessTestSetup.setup();
        this.service = new BookingsService();
        
        this.dayStr = TimeUtil.getDateStr(Calendar.getInstance());
        
        Field f = BookingsService.class.getDeclaredField("engine");
        f.setAccessible(true);
        
        SlotBookingEngine sbe = new SlotBookingEngine();
        sbe.init();
        f.set(this.service, sbe);
        
        sbe.cleanUp();
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
    
    @Test
    public void testFindFreeTimesBeforePerm()
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
        UserAssociation assoc = new UserAssociation();
        assoc.setId(new UserAssociationId(us1.getId(), uclass1.getId()));
        assoc.setUser(us1);
        assoc.setUserClass(uclass1);
        ses.save(assoc);
        RigType rigType1 = new RigType("booktestrigtype", 300, false);
        ses.save(rigType1);
        RigCapabilities caps1 = new RigCapabilities("book,test,foo");
        ses.save(caps1);
        Rig r1 = new Rig();
        r1.setName("bkrig1");
        r1.setRigType(rigType1);
        r1.setLastUpdateTimestamp(new Date());
        r1.setRigCapabilities(caps1);
        ses.save(r1);
        
        Calendar start = TimeUtil.getDayBegin(this.dayStr);
        Calendar end = TimeUtil.getDayBegin(this.dayStr);
        end.add(Calendar.DAY_OF_MONTH, 1);
        ResourcePermission perm1 = new ResourcePermission();
        perm1.setUserClass(uclass1);
        perm1.setType("TYPE");
        perm1.setSessionDuration(1800);
        perm1.setQueueActivityTimeout(300);
        perm1.setAllowedExtensions((short)10);
        perm1.setSessionActivityTimeout(300);
        perm1.setExtensionDuration(300);
        perm1.setMaximumBookings(10);
        perm1.setRigType(rigType1);
        perm1.setStartTime(start.getTime());
        perm1.setExpiryTime(end.getTime());
        perm1.setDisplayName("bookperm");
        ses.save(perm1);

        ses.getTransaction().commit();
        
        ses.refresh(caps1);
        ses.refresh(r1);
        ses.refresh(rigType1);
        ses.refresh(uclass1);
        ses.refresh(us1);
        
        FindFreeBookings request = new FindFreeBookings();
        FindBookingSlotType slotReq = new FindBookingSlotType();
        request.setFindBookingSlots(slotReq);
        UserIDType uid = new UserIDType();
        uid.setUserQName(us1.getNamespace() + ':' + us1.getName());
        slotReq.setUserID(uid);
        PermissionIDType pid = new PermissionIDType();
        pid.setPermissionID(perm1.getId().intValue());
        slotReq.setPermissionID(pid);
        
        TimePeriodType tp = new TimePeriodType();
        start.add(Calendar.DAY_OF_MONTH, -5);
        end.add(Calendar.DAY_OF_MONTH, -5);
        tp.setStartTime(start);
        tp.setEndTime(end);
        slotReq.setPeriod(tp);
        
        FindFreeBookingsResponse response = this.service.findFreeBookings(request);
        
        ses.beginTransaction();
        ses.delete(perm1);
        ses.delete(r1);
        ses.delete(caps1);
        ses.delete(rigType1);
        ses.delete(assoc);
        ses.delete(us1);
        ses.delete(uclass1);
        ses.getTransaction().commit();
        
        assertNotNull(response);
        BookingSlotListType slots = response.getFindFreeBookingsResponse();
        assertNotNull(slots);
        
        ResourceIDType res = slots.getResourceID();
        assertNotNull(res);
        assertEquals("TYPE", res.getType());
        assertEquals(rigType1.getId().intValue(), res.getResourceID());
        assertEquals(rigType1.getName(), res.getResourceName());
        
        pid = slots.getPermissionID();
        assertNotNull(pid);
        assertEquals(perm1.getId().intValue(), pid.getPermissionID());
        
        BookingSlotType slotsList[] = slots.getBookingSlot();
        assertNotNull(slotsList);
        
        for (BookingSlotType s : slotsList)
        {
            assertNotNull(s.getState());
            
            TimePeriodType t = s.getSlot();
            assertNotNull(t);
            assertNotNull(t.getStartTime());
            assertNotNull(t.getEndTime());
        }
        
        assertEquals(1, slotsList.length);
        
        BookingSlotType bs = slotsList[0];
        assertEquals(SlotState.NOPERMISSION, bs.getState());
        Calendar s = bs.getSlot().getStartTime();
        assertEquals(0, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        Calendar e = bs.getSlot().getEndTime();
        assertEquals(0, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        assertTrue(e.getTimeInMillis() - s.getTimeInMillis() > 80e6);
    }
    
    @Test
    public void testFindFreeTimesAfterPerm()
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
        UserAssociation assoc = new UserAssociation();
        assoc.setId(new UserAssociationId(us1.getId(), uclass1.getId()));
        assoc.setUser(us1);
        assoc.setUserClass(uclass1);
        ses.save(assoc);
        RigType rigType1 = new RigType("booktestrigtype", 300, false);
        ses.save(rigType1);
        RigCapabilities caps1 = new RigCapabilities("book,test,foo");
        ses.save(caps1);
        Rig r1 = new Rig();
        r1.setName("bkrig1");
        r1.setRigType(rigType1);
        r1.setLastUpdateTimestamp(new Date());
        r1.setRigCapabilities(caps1);
        ses.save(r1);
        
        Calendar start = TimeUtil.getDayBegin(this.dayStr);
        Calendar end = TimeUtil.getDayBegin(this.dayStr);
        end.add(Calendar.DAY_OF_MONTH, 1);
        ResourcePermission perm1 = new ResourcePermission();
        perm1.setUserClass(uclass1);
        perm1.setType("TYPE");
        perm1.setSessionDuration(1800);
        perm1.setQueueActivityTimeout(300);
        perm1.setAllowedExtensions((short)10);
        perm1.setSessionActivityTimeout(300);
        perm1.setExtensionDuration(300);
        perm1.setMaximumBookings(10);
        perm1.setRigType(rigType1);
        perm1.setStartTime(start.getTime());
        perm1.setExpiryTime(end.getTime());
        perm1.setDisplayName("bookperm");
        ses.save(perm1);

        ses.getTransaction().commit();
        
        ses.refresh(caps1);
        ses.refresh(r1);
        ses.refresh(rigType1);
        ses.refresh(uclass1);
        ses.refresh(us1);
        
        FindFreeBookings request = new FindFreeBookings();
        FindBookingSlotType slotReq = new FindBookingSlotType();
        request.setFindBookingSlots(slotReq);
        UserIDType uid = new UserIDType();
        uid.setUserQName(us1.getNamespace() + ':' + us1.getName());
        slotReq.setUserID(uid);
        PermissionIDType pid = new PermissionIDType();
        pid.setPermissionID(perm1.getId().intValue());
        slotReq.setPermissionID(pid);
        
        TimePeriodType tp = new TimePeriodType();
        start.add(Calendar.DAY_OF_MONTH, 5);
        end.add(Calendar.DAY_OF_MONTH, 5);
        tp.setStartTime(start);
        tp.setEndTime(end);
        slotReq.setPeriod(tp);
        
        FindFreeBookingsResponse response = this.service.findFreeBookings(request);
        
        ses.beginTransaction();
        ses.delete(perm1);
        ses.delete(r1);
        ses.delete(caps1);
        ses.delete(rigType1);
        ses.delete(assoc);
        ses.delete(us1);
        ses.delete(uclass1);
        ses.getTransaction().commit();
        
        assertNotNull(response);
        BookingSlotListType slots = response.getFindFreeBookingsResponse();
        assertNotNull(slots);
        
        ResourceIDType res = slots.getResourceID();
        assertNotNull(res);
        assertEquals("TYPE", res.getType());
        assertEquals(rigType1.getId().intValue(), res.getResourceID());
        assertEquals(rigType1.getName(), res.getResourceName());
        
        pid = slots.getPermissionID();
        assertNotNull(pid);
        assertEquals(perm1.getId().intValue(), pid.getPermissionID());
        
        BookingSlotType slotsList[] = slots.getBookingSlot();
        assertNotNull(slotsList);
        
        for (BookingSlotType s : slotsList)
        {
            assertNotNull(s.getState());
            
            TimePeriodType t = s.getSlot();
            assertNotNull(t);
            assertNotNull(t.getStartTime());
            assertNotNull(t.getEndTime());
        }
        
        assertEquals(1, slotsList.length);
        
        BookingSlotType bs = slotsList[0];
        assertEquals(SlotState.NOPERMISSION, bs.getState());
        Calendar s = bs.getSlot().getStartTime();
        assertEquals(0, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        Calendar e = bs.getSlot().getEndTime();
        assertEquals(0, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        assertTrue(e.getTimeInMillis() - s.getTimeInMillis() > 80e6);
    }
    
    @Test
    public void testFindFreeTimesWrongPermConf()
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
        UserAssociation assoc = new UserAssociation();
        assoc.setId(new UserAssociationId(us1.getId(), uclass1.getId()));
        assoc.setUser(us1);
        assoc.setUserClass(uclass1);
        ses.save(assoc);
        RigType rigType1 = new RigType("booktestrigtype", 300, false);
        ses.save(rigType1);
        RigCapabilities caps1 = new RigCapabilities("book,test,foo");
        ses.save(caps1);
        Rig r1 = new Rig();
        r1.setName("bkrig1");
        r1.setRigType(rigType1);
        r1.setLastUpdateTimestamp(new Date());
        r1.setRigCapabilities(caps1);
        ses.save(r1);
        
        Calendar start = TimeUtil.getDayBegin(this.dayStr);
        Calendar end = TimeUtil.getDayBegin(this.dayStr);
        end.add(Calendar.DAY_OF_MONTH, 1);
        ResourcePermission perm1 = new ResourcePermission();
        perm1.setUserClass(uclass1);
        perm1.setType("FOO");
        perm1.setSessionDuration(1800);
        perm1.setQueueActivityTimeout(300);
        perm1.setAllowedExtensions((short)10);
        perm1.setSessionActivityTimeout(300);
        perm1.setExtensionDuration(300);
        perm1.setMaximumBookings(10);
        perm1.setRigType(rigType1);
        perm1.setStartTime(start.getTime());
        perm1.setExpiryTime(end.getTime());
        perm1.setDisplayName("bookperm");
        ses.save(perm1);

        ses.getTransaction().commit();
        
        ses.refresh(caps1);
        ses.refresh(r1);
        ses.refresh(rigType1);
        ses.refresh(uclass1);
        ses.refresh(us1);
        
        FindFreeBookings request = new FindFreeBookings();
        FindBookingSlotType slotReq = new FindBookingSlotType();
        request.setFindBookingSlots(slotReq);
        UserIDType uid = new UserIDType();
        uid.setUserQName(us1.getNamespace() + ':' + us1.getName());
        slotReq.setUserID(uid);
        PermissionIDType pid = new PermissionIDType();
        pid.setPermissionID(perm1.getId().intValue());
        slotReq.setPermissionID(pid);
        
        TimePeriodType tp = new TimePeriodType();
        tp.setStartTime(start);
        tp.setEndTime(end);
        slotReq.setPeriod(tp);
        
        FindFreeBookingsResponse response = this.service.findFreeBookings(request);
        
        ses.beginTransaction();
        ses.delete(perm1);
        ses.delete(r1);
        ses.delete(caps1);
        ses.delete(rigType1);
        ses.delete(assoc);
        ses.delete(us1);
        ses.delete(uclass1);
        ses.getTransaction().commit();
        
        assertNotNull(response);
        
        /* Make sure it can be serialised. */
        try
        {
            OMElement ele = response.getOMElement(FindFreeBookingsResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
            assertNotNull(ele);
            
            String xml = ele.toStringWithConsume();
            assertNotNull(xml);
        }
        catch (Exception e)
        {
            fail("Failed serialising wrong perm conf.");
        }
    }
    
    
    @Test
    public void testFindFreeTimesRigType2()
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
        UserAssociation assoc = new UserAssociation();
        assoc.setId(new UserAssociationId(us1.getId(), uclass1.getId()));
        assoc.setUser(us1);
        assoc.setUserClass(uclass1);
        ses.save(assoc);
        RigType rigType1 = new RigType("booktestrigtype", 300, false);
        ses.save(rigType1);
        RigType rigType2 = new RigType("booktestrigtype2", 300, false);
        ses.save(rigType2);
        RigCapabilities caps1 = new RigCapabilities("book,test,foo");
        ses.save(caps1);
        RigCapabilities caps2 = new RigCapabilities("book,test,bar");
        ses.save(caps2);
        RigCapabilities caps3 = new RigCapabilities("book,baz,foo");
        ses.save(caps3);
        RequestCapabilities rcaps1 = new RequestCapabilities("book");
        ses.save(rcaps1);
        RequestCapabilities rcaps2 = new RequestCapabilities("foo");
        ses.save(rcaps2);
        RequestCapabilities rcaps3 = new RequestCapabilities("bar");
        ses.save(rcaps3);
        Rig r1 = new Rig();
        r1.setName("bkrig1");
        r1.setRigType(rigType1);
        r1.setLastUpdateTimestamp(new Date());
        r1.setRigCapabilities(caps1);
        ses.save(r1);
        Rig r2 = new Rig();
        r2.setName("bkrig2");
        r2.setRigType(rigType1);
        r2.setLastUpdateTimestamp(new Date());
        r2.setRigCapabilities(caps2);
        ses.save(r2);
        Rig r3 = new Rig();
        r3.setName("bkrig3");
        r3.setRigType(rigType2);
        r3.setLastUpdateTimestamp(new Date());
        r3.setRigCapabilities(caps3);
        ses.save(r3);
        
        Calendar start = TimeUtil.getDayBegin(this.dayStr);
        Calendar end = TimeUtil.getDayBegin(this.dayStr);
        end.add(Calendar.DAY_OF_MONTH, 1);
        ResourcePermission perm1 = new ResourcePermission();
        perm1.setUserClass(uclass1);
        perm1.setType("TYPE");
        perm1.setSessionDuration(1800);
        perm1.setQueueActivityTimeout(300);
        perm1.setAllowedExtensions((short)10);
        perm1.setSessionActivityTimeout(300);
        perm1.setExtensionDuration(300);
        perm1.setMaximumBookings(10);
        perm1.setRigType(rigType1);
        perm1.setStartTime(start.getTime());
        perm1.setExpiryTime(end.getTime());
        perm1.setDisplayName("bookperm");
        ses.save(perm1);
        
        /* #### BOOKINGS FOR R1 ########################################### */
        Calendar r1tm = TimeUtil.getDayBegin(this.dayStr);
        Bookings bk1 = new Bookings();
        bk1.setActive(true);
        bk1.setDuration(3600);
        /* Slots 2 - 5. */
        r1tm.add(Calendar.MINUTE, 30);
        bk1.setStartTime(r1tm.getTime());
        r1tm.add(Calendar.HOUR, 1);
        bk1.setEndTime(r1tm.getTime());
        bk1.setResourcePermission(perm1);
        bk1.setResourceType("RIG");
        bk1.setRig(r1);
        bk1.setUser(us1);
        bk1.setUserName(us1.getName());
        bk1.setUserNamespace(us1.getNamespace());
        ses.save(bk1);
        Bookings bk2 = new Bookings();
        bk2.setActive(true);
        bk2.setCancelReason("Test cancel.");
        bk2.setDuration(1800);
        /* Slots 8 - 9. */
        r1tm.add(Calendar.MINUTE, 30);
        bk2.setStartTime(r1tm.getTime());
        r1tm.add(Calendar.MINUTE, 30);
        bk2.setEndTime(r1tm.getTime());
        bk2.setResourcePermission(perm1);
        bk2.setResourceType("RIG");
        bk2.setRig(r1);
        bk2.setUser(us1);
        bk2.setUserName(us1.getName());
        bk2.setUserNamespace(us1.getNamespace());
        ses.save(bk2);
        Bookings bk3 = new Bookings();
        bk3.setActive(true);
        bk3.setDuration(7200);
        /* Slots 36 - 43. */
        r1tm.add(Calendar.MINUTE, 30);
        r1tm.add(Calendar.HOUR, 6);
        bk3.setStartTime(r1tm.getTime());
        r1tm.add(Calendar.HOUR, 2);
        bk3.setEndTime(r1tm.getTime());
        bk3.setResourcePermission(perm1);
        bk3.setResourceType("RIG");
        bk3.setRig(r1);
        bk3.setUser(us1);
        bk3.setUserName(us1.getName());
        bk3.setUserNamespace(us1.getNamespace());
        ses.save(bk3);
        
        /* #### BOOKINGS FOR R2 ########################################### */
        Calendar r2tm = TimeUtil.getDayBegin(this.dayStr);
        Bookings bk8 = new Bookings();
        bk8.setActive(true);
        bk8.setDuration(3600);
        /* Slots 13 - 16. */
        r2tm.add(Calendar.HOUR, 3);
        bk8.setStartTime(r2tm.getTime());
        r2tm.add(Calendar.HOUR, 1);
        bk8.setEndTime(r2tm.getTime());
        bk8.setResourcePermission(perm1);
        bk8.setResourceType("RIG");
        bk8.setRig(r2);
        bk8.setUser(us1);
        bk8.setUserName(us1.getName());
        bk8.setUserNamespace(us1.getNamespace());
        ses.save(bk8);
        
        Bookings bk9 = new Bookings();
        bk9.setActive(true);
        bk9.setDuration(1800);
        /* Slots 36 - 37. */
        r2tm.add(Calendar.HOUR, 5);
        bk9.setStartTime(r2tm.getTime());
        r2tm.add(Calendar.MINUTE, 30);
        bk9.setEndTime(r2tm.getTime());
        bk9.setResourcePermission(perm1);
        bk9.setResourceType("RIG");
        bk9.setRig(r2);
        bk9.setUser(us1);
        bk9.setUserName(us1.getName());
        bk9.setUserNamespace(us1.getNamespace());
        ses.save(bk9);
        
        /* #### BOOKINGS FOR R3 ########################################### */
        Calendar r3tm = TimeUtil.getDayBegin(this.dayStr);
        Bookings bk10 = new Bookings();
        bk10.setActive(true);
        bk10.setDuration(1800);
        /* Slots 2 - 3. */
        r3tm.add(Calendar.MINUTE, 30);
        bk10.setStartTime(r3tm.getTime());
        r3tm.add(Calendar.MINUTE, 30);
        bk10.setEndTime(r3tm.getTime());
        bk10.setResourcePermission(perm1);
        bk10.setResourceType("RIG");
        bk10.setRig(r3);
        bk10.setUser(us1);
        bk10.setUserName(us1.getName());
        bk10.setUserNamespace(us1.getNamespace());
        ses.save(bk10);
        
        Bookings bk11 = new Bookings();
        bk11.setActive(true);
        bk11.setDuration(3600);
        /* Slots 12 - 15. */
        r3tm.add(Calendar.HOUR, 2);
        bk11.setStartTime(r3tm.getTime());
        r3tm.add(Calendar.HOUR, 1);
        bk11.setEndTime(r3tm.getTime());
        bk11.setResourcePermission(perm1);
        bk11.setResourceType("RIG");
        bk11.setRig(r3);
        bk11.setUser(us1);
        bk11.setUserName(us1.getName());
        bk11.setUserNamespace(us1.getNamespace());
        ses.save(bk11);
        
        Bookings bk12 = new Bookings();
        bk12.setActive(true);
        bk12.setDuration(3600);
        /* Slots 24 - 27. */
        r3tm.add(Calendar.HOUR, 2);
        bk12.setStartTime(r3tm.getTime());
        r3tm.add(Calendar.HOUR, 1);
        bk12.setEndTime(r3tm.getTime());
        bk12.setResourcePermission(perm1);
        bk12.setResourceType("RIG");
        bk12.setRig(r3);
        bk12.setUser(us1);
        bk12.setUserName(us1.getName());
        bk12.setUserNamespace(us1.getNamespace());
        ses.save(bk12);
        
        /* #### Type bookings for RigType1 ####################################*/
        Calendar rt1tm = TimeUtil.getDayBegin(this.dayStr);
        Bookings bk4 = new Bookings();
        bk4.setActive(true);
        bk4.setDuration(1800);
        /* Slots 0 - 1. */
        bk4.setStartTime(rt1tm.getTime());
        rt1tm.add(Calendar.MINUTE, 30);
        bk4.setEndTime(rt1tm.getTime());
        bk4.setResourcePermission(perm1);
        bk4.setResourceType("TYPE");
        bk4.setRigType(rigType1);
        bk4.setUser(us1);
        bk4.setUserName(us1.getName());
        bk4.setUserNamespace(us1.getNamespace());
        ses.save(bk4);
        
        Bookings bk13 = new Bookings();
        bk13.setActive(true);
        bk13.setDuration(7200);
        /* Slots 0 - 7. */
        rt1tm.add(Calendar.MINUTE, -30);
        bk13.setStartTime(rt1tm.getTime());
        rt1tm.add(Calendar.HOUR, 2);
        bk13.setEndTime(rt1tm.getTime());
        bk13.setResourcePermission(perm1);
        bk13.setResourceType("TYPE");
        bk13.setRigType(rigType1);
        bk13.setUser(us1);
        bk13.setUserName(us1.getName());
        bk13.setUserNamespace(us1.getNamespace());
        ses.save(bk13);
        
        Bookings bk5 = new Bookings();
        bk5.setActive(true);
        bk5.setDuration(3600);
        /* Slots 20 - 23. */
        rt1tm.add(Calendar.HOUR, 3);
        bk5.setStartTime(rt1tm.getTime());
        rt1tm.add(Calendar.HOUR, 1);
        bk5.setEndTime(rt1tm.getTime());
        bk5.setResourcePermission(perm1);
        bk5.setResourceType("TYPE");
        bk5.setRigType(rigType1);
        bk5.setUser(us1);
        bk5.setUserName(us1.getName());
        bk5.setUserNamespace(us1.getNamespace());
        ses.save(bk5);
        
        Bookings bk14 = new Bookings();
        bk14.setActive(true);
        bk14.setDuration(3600);
        /* Slots 24 - 27. */
        bk14.setStartTime(rt1tm.getTime());
        rt1tm.add(Calendar.HOUR, 1);
        bk14.setEndTime(rt1tm.getTime());
        bk14.setResourcePermission(perm1);
        bk14.setResourceType("TYPE");
        bk14.setRigType(rigType1);
        bk14.setUser(us1);
        bk14.setUserName(us1.getName());
        bk14.setUserNamespace(us1.getNamespace());
        ses.save(bk14);
        
        Bookings bk15 = new Bookings();
        bk15.setActive(true);
        bk15.setDuration(5400);
        /* Slots 52 - 58. */
        rt1tm.add(Calendar.HOUR, 6);
        bk15.setStartTime(rt1tm.getTime());
        rt1tm.add(Calendar.HOUR, 1);
        rt1tm.add(Calendar.MINUTE, 30);
        bk15.setEndTime(rt1tm.getTime());
        bk15.setResourcePermission(perm1);
        bk15.setResourceType("TYPE");
        bk15.setRigType(rigType1);
        bk15.setUser(us1);
        bk15.setUserName(us1.getName());
        bk15.setUserNamespace(us1.getNamespace());
        ses.save(bk15);
        
        /* #### Type bookings for RigType2 ####################################*/
        Calendar rt2tm = TimeUtil.getDayBegin(this.dayStr);
        Bookings bk16 = new Bookings();
        bk16.setActive(true);
        bk16.setDuration(3600);
        /* Slots 52 - 59. */
        rt2tm.add(Calendar.HOUR, 7);
        bk16.setStartTime(rt2tm.getTime());
        rt2tm.add(Calendar.HOUR, 1);
        rt2tm.add(Calendar.MINUTE, 15);
        bk16.setEndTime(rt2tm.getTime());
        bk16.setResourcePermission(perm1);
        bk16.setResourceType("TYPE");
        bk16.setRigType(rigType2);
        bk16.setUser(us1);
        bk16.setUserName(us1.getName());
        bk16.setUserNamespace(us1.getNamespace());
        ses.save(bk16);
        
        /* #### Bookings for Request Caps 1. #################################*/
        Calendar rcap1tm = TimeUtil.getDayBegin(this.dayStr);
        Bookings bk6 = new Bookings();
        bk6.setActive(true);
        bk6.setDuration(1800);
        /* Slots 20 - 21. */
        rcap1tm.add(Calendar.HOUR, 5);
        bk6.setStartTime(rcap1tm.getTime());
        rcap1tm.add(Calendar.MINUTE, 30);
        bk6.setEndTime(rcap1tm.getTime());
        bk6.setResourcePermission(perm1);
        bk6.setResourceType("CAPABILITY");
        bk6.setRequestCapabilities(rcaps1);
        bk6.setUser(us1);
        bk6.setUserName(us1.getName());
        bk6.setUserNamespace(us1.getNamespace());
        ses.save(bk6);
        
        Bookings bk7 = new Bookings();
        bk7.setActive(true);
        bk7.setDuration(1800);
        /* Slots 22 - 23. */
        bk7.setStartTime(rcap1tm.getTime());
        rcap1tm.add(Calendar.MINUTE, 30);
        bk7.setEndTime(rcap1tm.getTime());
        bk7.setResourcePermission(perm1);
        bk7.setResourceType("CAPABILITY");
        bk7.setRequestCapabilities(rcaps1);
        bk7.setUser(us1);
        bk7.setUserName(us1.getName());
        bk7.setUserNamespace(us1.getNamespace());
        ses.save(bk7);
        
        Bookings bk17 = new Bookings();
        bk17.setActive(true);
        bk17.setDuration(1800);
        /* Slots 32 - 35. */
        rcap1tm.add(Calendar.HOUR, 2);
        bk17.setStartTime(rcap1tm.getTime());
        rcap1tm.add(Calendar.HOUR, 1);
        bk17.setEndTime(rcap1tm.getTime());
        bk17.setResourcePermission(perm1);
        bk17.setResourceType("CAPABILITY");
        bk17.setRequestCapabilities(rcaps1);
        bk17.setUser(us1);
        bk17.setUserName(us1.getName());
        bk17.setUserNamespace(us1.getNamespace());
        ses.save(bk17);
        
        /* #### Bookings for Request Caps 2. #################################*/
        Calendar rcap2tm = TimeUtil.getDayBegin(this.dayStr);
        Bookings bk18 = new Bookings();
        bk18.setActive(true);
        bk18.setDuration(1800);
        /* Slots 56 - 59. */
        rcap2tm.add(Calendar.HOUR, 14);
        bk18.setStartTime(rcap2tm.getTime());
        rcap2tm.add(Calendar.HOUR, 1);
        bk18.setEndTime(rcap2tm.getTime());
        bk18.setResourcePermission(perm1);
        bk18.setResourceType("CAPABILITY");
        bk18.setRequestCapabilities(rcaps2);
        bk18.setUser(us1);
        bk18.setUserName(us1.getName());
        bk18.setUserNamespace(us1.getNamespace());
        ses.save(bk18);
        
        /* #### Bookings for Request Caps 3. #################################*/
        Calendar rcap3tm = TimeUtil.getDayBegin(this.dayStr);
        Bookings bk19 = new Bookings();
        bk19.setActive(true);
        bk19.setDuration(4500);
        /* Slots 56 - 59. */
        rcap3tm.add(Calendar.HOUR, 7);
        bk19.setStartTime(rcap3tm.getTime());
        rcap3tm.add(Calendar.HOUR, 1);
        rcap3tm.add(Calendar.MINUTE, 15);
        bk19.setEndTime(rcap3tm.getTime());
        bk19.setResourcePermission(perm1);
        bk19.setResourceType("CAPABILITY");
        bk19.setRequestCapabilities(rcaps3);
        bk19.setUser(us1);
        bk19.setUserName(us1.getName());
        bk19.setUserNamespace(us1.getNamespace());
        ses.save(bk19);
        
        ses.getTransaction().commit();
        
        ses.beginTransaction();
        MatchingCapabilities mat1 = new MatchingCapabilities(rcaps1, caps1);
        ses.save(mat1);
        MatchingCapabilities mat2 = new MatchingCapabilities(rcaps1, caps2);
        ses.save(mat2);
        MatchingCapabilities mat3 = new MatchingCapabilities(rcaps1, caps3);
        ses.save(mat3);
        MatchingCapabilities mat4 = new MatchingCapabilities(rcaps2, caps1);
        ses.save(mat4);
        MatchingCapabilities mat5 = new MatchingCapabilities(rcaps2, caps3);
        ses.save(mat5);
        MatchingCapabilities mat6 = new MatchingCapabilities(rcaps3, caps2);
        ses.save(mat6);
        ses.getTransaction().commit();
        
        ses.refresh(rcaps1);
        ses.refresh(rcaps2);
        ses.refresh(rcaps3);
        ses.refresh(caps1);
        ses.refresh(caps2);
        ses.refresh(caps3);
        ses.refresh(r1);
        ses.refresh(r2);
        ses.refresh(r3);
        ses.refresh(rigType1);
        ses.refresh(rigType2);
        ses.refresh(uclass1);
        ses.refresh(us1);
        
        FindFreeBookings request = new FindFreeBookings();
        FindBookingSlotType slotReq = new FindBookingSlotType();
        request.setFindBookingSlots(slotReq);
        UserIDType uid = new UserIDType();
        uid.setUserQName(us1.getNamespace() + ':' + us1.getName());
        slotReq.setUserID(uid);
        PermissionIDType pid = new PermissionIDType();
        pid.setPermissionID(perm1.getId().intValue());
        slotReq.setPermissionID(pid);
        
        TimePeriodType tp = new TimePeriodType();
        tp.setStartTime(start);
        tp.setEndTime(end);
        slotReq.setPeriod(tp);
        
        FindFreeBookingsResponse response = this.service.findFreeBookings(request);
        
        ses.beginTransaction();
        ses.delete(bk1);
        ses.delete(bk2);
        ses.delete(bk3);
        ses.delete(bk4);
        ses.delete(bk5);
        ses.delete(bk6);
        ses.delete(bk7);
        ses.delete(bk8);
        ses.delete(bk9);
        ses.delete(bk10);
        ses.delete(bk11);
        ses.delete(bk12);
        ses.delete(bk13);
        ses.delete(bk14);
        ses.delete(bk15);
        ses.delete(bk16);
        ses.delete(bk17);
        ses.delete(bk18);
        ses.delete(bk19);
        ses.delete(perm1);
        ses.delete(r1);
        ses.delete(r2);
        ses.delete(r3);
        ses.delete(mat1);
        ses.delete(mat2);
        ses.delete(mat3);
        ses.delete(mat4);
        ses.delete(mat5);
        ses.delete(mat6);
        ses.delete(caps1);
        ses.delete(caps2);
        ses.delete(caps3);
        ses.delete(rcaps1);
        ses.delete(rcaps2);
        ses.delete(rcaps3);
        ses.delete(rigType1);
        ses.delete(rigType2);
        ses.delete(assoc);
        ses.delete(us1);
        ses.delete(uclass1);
        ses.getTransaction().commit();
        
        assertTrue(bk1.isActive());
        assertTrue(bk2.isActive());
        assertTrue(bk3.isActive());
        assertTrue(bk4.isActive());
        assertTrue(bk5.isActive());
        assertTrue(bk6.isActive());
        assertTrue(bk7.isActive());
        assertTrue(bk8.isActive());
        assertTrue(bk9.isActive());
        assertTrue(bk10.isActive());
        assertTrue(bk11.isActive());
        assertTrue(bk12.isActive());
        assertTrue(bk13.isActive());
        assertTrue(bk14.isActive());
        assertTrue(bk15.isActive());
        assertTrue(bk16.isActive());
        assertTrue(bk17.isActive());
        assertTrue(bk18.isActive());
        assertTrue(bk19.isActive());
        
        assertNotNull(response);
        BookingSlotListType slots = response.getFindFreeBookingsResponse();
        assertNotNull(slots);
        
        ResourceIDType res = slots.getResourceID();
        assertNotNull(res);
        assertEquals("TYPE", res.getType());
        assertEquals(rigType1.getId().intValue(), res.getResourceID());
        assertEquals(rigType1.getName(), res.getResourceName());
        
        pid = slots.getPermissionID();
        assertNotNull(pid);
        assertEquals(perm1.getId().intValue(), pid.getPermissionID());
        
        BookingSlotType slotsList[] = slots.getBookingSlot();
        assertNotNull(slotsList);
        
        for (BookingSlotType s : slotsList)
        {
            assertNotNull(s.getState());
            
            TimePeriodType t = s.getSlot();
            assertNotNull(t);
            assertNotNull(t.getStartTime());
            assertNotNull(t.getEndTime());
        }
        
        assertEquals(6, slotsList.length);
        
        BookingSlotType bs = slotsList[0];
        assertEquals(SlotState.BOOKED, bs.getState());
        Calendar s = bs.getSlot().getStartTime();
        assertEquals(0, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        Calendar e = bs.getSlot().getEndTime();
        assertEquals(1, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        bs = slotsList[1];
        assertEquals(SlotState.FREE, bs.getState());
        s = bs.getSlot().getStartTime();
        assertEquals(1, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        e = bs.getSlot().getEndTime();
        assertEquals(8, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        bs = slotsList[2];
        assertEquals(SlotState.BOOKED, bs.getState());
        s = bs.getSlot().getStartTime();
        assertEquals(8, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        e = bs.getSlot().getEndTime();
        assertEquals(8, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(15, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        
        bs = slotsList[3];
        assertEquals(SlotState.FREE, bs.getState());
        s = bs.getSlot().getStartTime();
        assertEquals(8, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(15, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        e = bs.getSlot().getEndTime();
        assertEquals(9, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        bs = slotsList[4];
        assertEquals(SlotState.BOOKED, bs.getState());
        s = bs.getSlot().getStartTime();
        assertEquals(9, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        e = bs.getSlot().getEndTime();
        assertEquals(9, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        bs = slotsList[5];
        assertEquals(SlotState.FREE, bs.getState());
        s = bs.getSlot().getStartTime();
        assertEquals(9, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        e = bs.getSlot().getEndTime();
        assertEquals(0, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
    }
    
    @Test
    public void testFindFreeTimesRig1()
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
        UserAssociation assoc = new UserAssociation();
        assoc.setId(new UserAssociationId(us1.getId(), uclass1.getId()));
        assoc.setUser(us1);
        assoc.setUserClass(uclass1);
        ses.save(assoc);
        RigType rigType1 = new RigType("booktestrigtype", 300, false);
        ses.save(rigType1);
        RigType rigType2 = new RigType("booktestrigtype2", 300, false);
        ses.save(rigType2);
        RigCapabilities caps1 = new RigCapabilities("book,test,foo");
        ses.save(caps1);
        RigCapabilities caps2 = new RigCapabilities("book,test,bar");
        ses.save(caps2);
        RigCapabilities caps3 = new RigCapabilities("book,baz,foo");
        ses.save(caps3);
        RequestCapabilities rcaps1 = new RequestCapabilities("book");
        ses.save(rcaps1);
        RequestCapabilities rcaps2 = new RequestCapabilities("foo");
        ses.save(rcaps2);
        RequestCapabilities rcaps3 = new RequestCapabilities("bar");
        ses.save(rcaps3);
        Rig r1 = new Rig();
        r1.setName("bkrig1");
        r1.setRigType(rigType1);
        r1.setLastUpdateTimestamp(new Date());
        r1.setRigCapabilities(caps1);
        ses.save(r1);
        Rig r2 = new Rig();
        r2.setName("bkrig2");
        r2.setRigType(rigType1);
        r2.setLastUpdateTimestamp(new Date());
        r2.setRigCapabilities(caps2);
        ses.save(r2);
        Rig r3 = new Rig();
        r3.setName("bkrig3");
        r3.setRigType(rigType2);
        r3.setLastUpdateTimestamp(new Date());
        r3.setRigCapabilities(caps3);
        ses.save(r3);
        
        Calendar start = TimeUtil.getDayBegin(this.dayStr);
        Calendar end = TimeUtil.getDayBegin(this.dayStr);
        end.add(Calendar.DAY_OF_MONTH, 1);
        ResourcePermission perm1 = new ResourcePermission();
        perm1.setUserClass(uclass1);
        perm1.setType("RIG");
        perm1.setSessionDuration(1800);
        perm1.setQueueActivityTimeout(300);
        perm1.setAllowedExtensions((short)10);
        perm1.setSessionActivityTimeout(300);
        perm1.setExtensionDuration(300);
        perm1.setMaximumBookings(10);
        perm1.setRig(r1);
        perm1.setStartTime(start.getTime());
        perm1.setExpiryTime(end.getTime());
        perm1.setDisplayName("bookperm");
        ses.save(perm1);
        
        /* #### BOOKINGS FOR R1 ########################################### */
        Calendar r1tm = TimeUtil.getDayBegin(this.dayStr);
        Bookings bk1 = new Bookings();
        bk1.setActive(true);
        bk1.setDuration(3600);
        /* Slots 2 - 5. */
        r1tm.add(Calendar.MINUTE, 30);
        bk1.setStartTime(r1tm.getTime());
        r1tm.add(Calendar.HOUR, 1);
        bk1.setEndTime(r1tm.getTime());
        bk1.setResourcePermission(perm1);
        bk1.setResourceType("RIG");
        bk1.setRig(r1);
        bk1.setUser(us1);
        bk1.setUserName(us1.getName());
        bk1.setUserNamespace(us1.getNamespace());
        ses.save(bk1);
        Bookings bk2 = new Bookings();
        bk2.setActive(true);
        bk2.setCancelReason("Test cancel.");
        bk2.setDuration(1800);
        /* Slots 8 - 9. */
        r1tm.add(Calendar.MINUTE, 30);
        bk2.setStartTime(r1tm.getTime());
        r1tm.add(Calendar.MINUTE, 30);
        bk2.setEndTime(r1tm.getTime());
        bk2.setResourcePermission(perm1);
        bk2.setResourceType("RIG");
        bk2.setRig(r1);
        bk2.setUser(us1);
        bk2.setUserName(us1.getName());
        bk2.setUserNamespace(us1.getNamespace());
        ses.save(bk2);
        Bookings bk3 = new Bookings();
        bk3.setActive(true);
        bk3.setDuration(7200);
        /* Slots 36 - 43. */
        r1tm.add(Calendar.MINUTE, 30);
        r1tm.add(Calendar.HOUR, 6);
        bk3.setStartTime(r1tm.getTime());
        r1tm.add(Calendar.HOUR, 2);
        bk3.setEndTime(r1tm.getTime());
        bk3.setResourcePermission(perm1);
        bk3.setResourceType("RIG");
        bk3.setRig(r1);
        bk3.setUser(us1);
        bk3.setUserName(us1.getName());
        bk3.setUserNamespace(us1.getNamespace());
        ses.save(bk3);
        
        /* #### BOOKINGS FOR R2 ########################################### */
        Calendar r2tm = TimeUtil.getDayBegin(this.dayStr);
        Bookings bk8 = new Bookings();
        bk8.setActive(true);
        bk8.setDuration(3600);
        /* Slots 13 - 16. */
        r2tm.add(Calendar.HOUR, 3);
        bk8.setStartTime(r2tm.getTime());
        r2tm.add(Calendar.HOUR, 1);
        bk8.setEndTime(r2tm.getTime());
        bk8.setResourcePermission(perm1);
        bk8.setResourceType("RIG");
        bk8.setRig(r2);
        bk8.setUser(us1);
        bk8.setUserName(us1.getName());
        bk8.setUserNamespace(us1.getNamespace());
        ses.save(bk8);
        
        Bookings bk9 = new Bookings();
        bk9.setActive(true);
        bk9.setDuration(1800);
        /* Slots 36 - 37. */
        r2tm.add(Calendar.HOUR, 5);
        bk9.setStartTime(r2tm.getTime());
        r2tm.add(Calendar.MINUTE, 30);
        bk9.setEndTime(r2tm.getTime());
        bk9.setResourcePermission(perm1);
        bk9.setResourceType("RIG");
        bk9.setRig(r2);
        bk9.setUser(us1);
        bk9.setUserName(us1.getName());
        bk9.setUserNamespace(us1.getNamespace());
        ses.save(bk9);
        
        /* #### BOOKINGS FOR R3 ########################################### */
        Calendar r3tm = TimeUtil.getDayBegin(this.dayStr);
        Bookings bk10 = new Bookings();
        bk10.setActive(true);
        bk10.setDuration(1800);
        /* Slots 2 - 3. */
        r3tm.add(Calendar.MINUTE, 30);
        bk10.setStartTime(r3tm.getTime());
        r3tm.add(Calendar.MINUTE, 30);
        bk10.setEndTime(r3tm.getTime());
        bk10.setResourcePermission(perm1);
        bk10.setResourceType("RIG");
        bk10.setRig(r3);
        bk10.setUser(us1);
        bk10.setUserName(us1.getName());
        bk10.setUserNamespace(us1.getNamespace());
        ses.save(bk10);
        
        Bookings bk11 = new Bookings();
        bk11.setActive(true);
        bk11.setDuration(3600);
        /* Slots 12 - 15. */
        r3tm.add(Calendar.HOUR, 2);
        bk11.setStartTime(r3tm.getTime());
        r3tm.add(Calendar.HOUR, 1);
        bk11.setEndTime(r3tm.getTime());
        bk11.setResourcePermission(perm1);
        bk11.setResourceType("RIG");
        bk11.setRig(r3);
        bk11.setUser(us1);
        bk11.setUserName(us1.getName());
        bk11.setUserNamespace(us1.getNamespace());
        ses.save(bk11);
        
        Bookings bk12 = new Bookings();
        bk12.setActive(true);
        bk12.setDuration(3600);
        /* Slots 24 - 27. */
        r3tm.add(Calendar.HOUR, 2);
        bk12.setStartTime(r3tm.getTime());
        r3tm.add(Calendar.HOUR, 1);
        bk12.setEndTime(r3tm.getTime());
        bk12.setResourcePermission(perm1);
        bk12.setResourceType("RIG");
        bk12.setRig(r3);
        bk12.setUser(us1);
        bk12.setUserName(us1.getName());
        bk12.setUserNamespace(us1.getNamespace());
        ses.save(bk12);
        
        /* #### Type bookings for RigType1 ####################################*/
        Calendar rt1tm = TimeUtil.getDayBegin(this.dayStr);
        Bookings bk4 = new Bookings();
        bk4.setActive(true);
        bk4.setDuration(1800);
        /* Slots 0 - 1. */
        bk4.setStartTime(rt1tm.getTime());
        rt1tm.add(Calendar.MINUTE, 30);
        bk4.setEndTime(rt1tm.getTime());
        bk4.setResourcePermission(perm1);
        bk4.setResourceType("TYPE");
        bk4.setRigType(rigType1);
        bk4.setUser(us1);
        bk4.setUserName(us1.getName());
        bk4.setUserNamespace(us1.getNamespace());
        ses.save(bk4);
        
        Bookings bk13 = new Bookings();
        bk13.setActive(true);
        bk13.setDuration(7200);
        /* Slots 0 - 7. */
        rt1tm.add(Calendar.MINUTE, -30);
        bk13.setStartTime(rt1tm.getTime());
        rt1tm.add(Calendar.HOUR, 2);
        bk13.setEndTime(rt1tm.getTime());
        bk13.setResourcePermission(perm1);
        bk13.setResourceType("TYPE");
        bk13.setRigType(rigType1);
        bk13.setUser(us1);
        bk13.setUserName(us1.getName());
        bk13.setUserNamespace(us1.getNamespace());
        ses.save(bk13);
        
        Bookings bk5 = new Bookings();
        bk5.setActive(true);
        bk5.setDuration(3600);
        /* Slots 20 - 23. */
        rt1tm.add(Calendar.HOUR, 3);
        bk5.setStartTime(rt1tm.getTime());
        rt1tm.add(Calendar.HOUR, 1);
        bk5.setEndTime(rt1tm.getTime());
        bk5.setResourcePermission(perm1);
        bk5.setResourceType("TYPE");
        bk5.setRigType(rigType1);
        bk5.setUser(us1);
        bk5.setUserName(us1.getName());
        bk5.setUserNamespace(us1.getNamespace());
        ses.save(bk5);
        
        Bookings bk14 = new Bookings();
        bk14.setActive(true);
        bk14.setDuration(3600);
        /* Slots 24 - 27. */
        bk14.setStartTime(rt1tm.getTime());
        rt1tm.add(Calendar.HOUR, 1);
        bk14.setEndTime(rt1tm.getTime());
        bk14.setResourcePermission(perm1);
        bk14.setResourceType("TYPE");
        bk14.setRigType(rigType1);
        bk14.setUser(us1);
        bk14.setUserName(us1.getName());
        bk14.setUserNamespace(us1.getNamespace());
        ses.save(bk14);
        
        Bookings bk15 = new Bookings();
        bk15.setActive(true);
        bk15.setDuration(5400);
        /* Slots 52 - 58. */
        rt1tm.add(Calendar.HOUR, 6);
        bk15.setStartTime(rt1tm.getTime());
        rt1tm.add(Calendar.HOUR, 1);
        rt1tm.add(Calendar.MINUTE, 30);
        bk15.setEndTime(rt1tm.getTime());
        bk15.setResourcePermission(perm1);
        bk15.setResourceType("TYPE");
        bk15.setRigType(rigType1);
        bk15.setUser(us1);
        bk15.setUserName(us1.getName());
        bk15.setUserNamespace(us1.getNamespace());
        ses.save(bk15);
        
        /* #### Type bookings for RigType2 ####################################*/
        Calendar rt2tm = TimeUtil.getDayBegin(this.dayStr);
        Bookings bk16 = new Bookings();
        bk16.setActive(true);
        bk16.setDuration(3600);
        /* Slots 52 - 59. */
        rt2tm.add(Calendar.HOUR, 7);
        bk16.setStartTime(rt2tm.getTime());
        rt2tm.add(Calendar.HOUR, 1);
        rt2tm.add(Calendar.MINUTE, 15);
        bk16.setEndTime(rt2tm.getTime());
        bk16.setResourcePermission(perm1);
        bk16.setResourceType("TYPE");
        bk16.setRigType(rigType2);
        bk16.setUser(us1);
        bk16.setUserName(us1.getName());
        bk16.setUserNamespace(us1.getNamespace());
        ses.save(bk16);
        
        /* #### Bookings for Request Caps 1. #################################*/
        Calendar rcap1tm = TimeUtil.getDayBegin(this.dayStr);
        Bookings bk6 = new Bookings();
        bk6.setActive(true);
        bk6.setDuration(1800);
        /* Slots 20 - 21. */
        rcap1tm.add(Calendar.HOUR, 5);
        bk6.setStartTime(rcap1tm.getTime());
        rcap1tm.add(Calendar.MINUTE, 30);
        bk6.setEndTime(rcap1tm.getTime());
        bk6.setResourcePermission(perm1);
        bk6.setResourceType("CAPABILITY");
        bk6.setRequestCapabilities(rcaps1);
        bk6.setUser(us1);
        bk6.setUserName(us1.getName());
        bk6.setUserNamespace(us1.getNamespace());
        ses.save(bk6);
        
        Bookings bk7 = new Bookings();
        bk7.setActive(true);
        bk7.setDuration(1800);
        /* Slots 22 - 23. */
        bk7.setStartTime(rcap1tm.getTime());
        rcap1tm.add(Calendar.MINUTE, 30);
        bk7.setEndTime(rcap1tm.getTime());
        bk7.setResourcePermission(perm1);
        bk7.setResourceType("CAPABILITY");
        bk7.setRequestCapabilities(rcaps1);
        bk7.setUser(us1);
        bk7.setUserName(us1.getName());
        bk7.setUserNamespace(us1.getNamespace());
        ses.save(bk7);
        
        Bookings bk17 = new Bookings();
        bk17.setActive(true);
        bk17.setDuration(1800);
        /* Slots 32 - 35. */
        rcap1tm.add(Calendar.HOUR, 2);
        bk17.setStartTime(rcap1tm.getTime());
        rcap1tm.add(Calendar.HOUR, 1);
        bk17.setEndTime(rcap1tm.getTime());
        bk17.setResourcePermission(perm1);
        bk17.setResourceType("CAPABILITY");
        bk17.setRequestCapabilities(rcaps1);
        bk17.setUser(us1);
        bk17.setUserName(us1.getName());
        bk17.setUserNamespace(us1.getNamespace());
        ses.save(bk17);
        
        /* #### Bookings for Request Caps 2. #################################*/
        Calendar rcap2tm = TimeUtil.getDayBegin(this.dayStr);
        Bookings bk18 = new Bookings();
        bk18.setActive(true);
        bk18.setDuration(1800);
        /* Slots 56 - 59. */
        rcap2tm.add(Calendar.HOUR, 14);
        bk18.setStartTime(rcap2tm.getTime());
        rcap2tm.add(Calendar.HOUR, 1);
        bk18.setEndTime(rcap2tm.getTime());
        bk18.setResourcePermission(perm1);
        bk18.setResourceType("CAPABILITY");
        bk18.setRequestCapabilities(rcaps2);
        bk18.setUser(us1);
        bk18.setUserName(us1.getName());
        bk18.setUserNamespace(us1.getNamespace());
        ses.save(bk18);
        
        /* #### Bookings for Request Caps 3. #################################*/
        Calendar rcap3tm = TimeUtil.getDayBegin(this.dayStr);
        Bookings bk19 = new Bookings();
        bk19.setActive(true);
        bk19.setDuration(4500);
        /* Slots 56 - 59. */
        rcap3tm.add(Calendar.HOUR, 7);
        bk19.setStartTime(rcap3tm.getTime());
        rcap3tm.add(Calendar.HOUR, 1);
        rcap3tm.add(Calendar.MINUTE, 15);
        bk19.setEndTime(rcap3tm.getTime());
        bk19.setResourcePermission(perm1);
        bk19.setResourceType("CAPABILITY");
        bk19.setRequestCapabilities(rcaps3);
        bk19.setUser(us1);
        bk19.setUserName(us1.getName());
        bk19.setUserNamespace(us1.getNamespace());
        ses.save(bk19);
        
        ses.getTransaction().commit();
        
        ses.beginTransaction();
        MatchingCapabilities mat1 = new MatchingCapabilities(rcaps1, caps1);
        ses.save(mat1);
        MatchingCapabilities mat2 = new MatchingCapabilities(rcaps1, caps2);
        ses.save(mat2);
        MatchingCapabilities mat3 = new MatchingCapabilities(rcaps1, caps3);
        ses.save(mat3);
        MatchingCapabilities mat4 = new MatchingCapabilities(rcaps2, caps1);
        ses.save(mat4);
        MatchingCapabilities mat5 = new MatchingCapabilities(rcaps2, caps3);
        ses.save(mat5);
        MatchingCapabilities mat6 = new MatchingCapabilities(rcaps3, caps2);
        ses.save(mat6);
        ses.getTransaction().commit();
        
        ses.refresh(rcaps1);
        ses.refresh(rcaps2);
        ses.refresh(rcaps3);
        ses.refresh(caps1);
        ses.refresh(caps2);
        ses.refresh(caps3);
        ses.refresh(r1);
        ses.refresh(r2);
        ses.refresh(r3);
        ses.refresh(rigType1);
        ses.refresh(rigType2);
        ses.refresh(uclass1);
        ses.refresh(us1);
        
        FindFreeBookings request = new FindFreeBookings();
        FindBookingSlotType slotReq = new FindBookingSlotType();
        request.setFindBookingSlots(slotReq);
        UserIDType uid = new UserIDType();
        uid.setUserQName(us1.getNamespace() + ':' + us1.getName());
        slotReq.setUserID(uid);
        PermissionIDType pid = new PermissionIDType();
        pid.setPermissionID(perm1.getId().intValue());
        slotReq.setPermissionID(pid);
        
        TimePeriodType tp = new TimePeriodType();
        tp.setStartTime(start);
        tp.setEndTime(end);
        slotReq.setPeriod(tp);
        
        FindFreeBookingsResponse response = this.service.findFreeBookings(request);
        
        ses.beginTransaction();
        ses.delete(bk1);
        ses.delete(bk2);
        ses.delete(bk3);
        ses.delete(bk4);
        ses.delete(bk5);
        ses.delete(bk6);
        ses.delete(bk7);
        ses.delete(bk8);
        ses.delete(bk9);
        ses.delete(bk10);
        ses.delete(bk11);
        ses.delete(bk12);
        ses.delete(bk13);
        ses.delete(bk14);
        ses.delete(bk15);
        ses.delete(bk16);
        ses.delete(bk17);
        ses.delete(bk18);
        ses.delete(bk19);
        ses.delete(perm1);
        ses.delete(r1);
        ses.delete(r2);
        ses.delete(r3);
        ses.delete(mat1);
        ses.delete(mat2);
        ses.delete(mat3);
        ses.delete(mat4);
        ses.delete(mat5);
        ses.delete(mat6);
        ses.delete(caps1);
        ses.delete(caps2);
        ses.delete(caps3);
        ses.delete(rcaps1);
        ses.delete(rcaps2);
        ses.delete(rcaps3);
        ses.delete(rigType1);
        ses.delete(rigType2);
        ses.delete(assoc);
        ses.delete(us1);
        ses.delete(uclass1);
        ses.getTransaction().commit();
        
        assertTrue(bk1.isActive());
        assertTrue(bk2.isActive());
        assertTrue(bk3.isActive());
        assertTrue(bk4.isActive());
        assertTrue(bk5.isActive());
        assertTrue(bk6.isActive());
        assertTrue(bk7.isActive());
        assertTrue(bk8.isActive());
        assertTrue(bk9.isActive());
        assertTrue(bk10.isActive());
        assertTrue(bk11.isActive());
        assertTrue(bk12.isActive());
        assertTrue(bk13.isActive());
        assertTrue(bk14.isActive());
        assertTrue(bk15.isActive());
        assertTrue(bk16.isActive());
        assertTrue(bk17.isActive());
        assertTrue(bk18.isActive());
        assertTrue(bk19.isActive());
        
        assertNotNull(response);
        BookingSlotListType slots = response.getFindFreeBookingsResponse();
        assertNotNull(slots);
        
        ResourceIDType res = slots.getResourceID();
        assertNotNull(res);
        assertEquals("RIG", res.getType());
        assertEquals(r1.getId().intValue(), res.getResourceID());
        assertEquals(r1.getName(), res.getResourceName());
        
        pid = slots.getPermissionID();
        assertNotNull(pid);
        assertEquals(perm1.getId().intValue(), pid.getPermissionID());
        
        BookingSlotType slotsList[] = slots.getBookingSlot();
        assertNotNull(slotsList);
        
        for (BookingSlotType s : slotsList)
        {
            assertNotNull(s.getState());
            
            TimePeriodType t = s.getSlot();
            assertNotNull(t);
            assertNotNull(t.getStartTime());
            assertNotNull(t.getEndTime());
        }
        
        assertEquals(6, slotsList.length);
        
        BookingSlotType bs = slotsList[0];
        assertEquals(SlotState.BOOKED, bs.getState());
        Calendar s = bs.getSlot().getStartTime();
        assertEquals(0, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        Calendar e = bs.getSlot().getEndTime();
        assertEquals(1, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        bs = slotsList[1];
        assertEquals(SlotState.FREE, bs.getState());
        s = bs.getSlot().getStartTime();
        assertEquals(1, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        e = bs.getSlot().getEndTime();
        assertEquals(2, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        bs = slotsList[2];
        assertEquals(SlotState.BOOKED, bs.getState());
        s = bs.getSlot().getStartTime();
        assertEquals(2, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        e = bs.getSlot().getEndTime();
        assertEquals(2, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        
        bs = slotsList[3];
        assertEquals(SlotState.FREE, bs.getState());
        s = bs.getSlot().getStartTime();
        assertEquals(2, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        e = bs.getSlot().getEndTime();
        assertEquals(8, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        bs = slotsList[4];
        assertEquals(SlotState.BOOKED, bs.getState());
        s = bs.getSlot().getStartTime();
        assertEquals(8, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        e = bs.getSlot().getEndTime();
        assertEquals(11, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        bs = slotsList[5];
        assertEquals(SlotState.FREE, bs.getState());
        s = bs.getSlot().getStartTime();
        assertEquals(11, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        e = bs.getSlot().getEndTime();
        assertEquals(0, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
    }
    
    @Test
    public void testFindFreeTimesReqCaps2()
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
        UserAssociation assoc = new UserAssociation();
        assoc.setId(new UserAssociationId(us1.getId(), uclass1.getId()));
        assoc.setUser(us1);
        assoc.setUserClass(uclass1);
        ses.save(assoc);
        RigType rigType1 = new RigType("booktestrigtype", 300, false);
        ses.save(rigType1);
        RigType rigType2 = new RigType("booktestrigtype2", 300, false);
        ses.save(rigType2);
        RigCapabilities caps1 = new RigCapabilities("book,test,foo");
        ses.save(caps1);
        RigCapabilities caps2 = new RigCapabilities("book,test,bar");
        ses.save(caps2);
        RigCapabilities caps3 = new RigCapabilities("book,baz,foo");
        ses.save(caps3);
        RequestCapabilities rcaps1 = new RequestCapabilities("book");
        ses.save(rcaps1);
        RequestCapabilities rcaps2 = new RequestCapabilities("foo");
        ses.save(rcaps2);
        RequestCapabilities rcaps3 = new RequestCapabilities("bar");
        ses.save(rcaps3);
        Rig r1 = new Rig();
        r1.setName("bkrig1");
        r1.setRigType(rigType1);
        r1.setLastUpdateTimestamp(new Date());
        r1.setRigCapabilities(caps1);
        ses.save(r1);
        Rig r2 = new Rig();
        r2.setName("bkrig2");
        r2.setRigType(rigType1);
        r2.setLastUpdateTimestamp(new Date());
        r2.setRigCapabilities(caps2);
        ses.save(r2);
        Rig r3 = new Rig();
        r3.setName("bkrig3");
        r3.setRigType(rigType2);
        r3.setLastUpdateTimestamp(new Date());
        r3.setRigCapabilities(caps3);
        ses.save(r3);
        
        Calendar start = TimeUtil.getDayBegin(this.dayStr);
        Calendar end = TimeUtil.getDayBegin(this.dayStr);
        end.add(Calendar.DAY_OF_MONTH, 1);
        ResourcePermission perm1 = new ResourcePermission();
        perm1.setUserClass(uclass1);
        perm1.setType("CAPABILITY");
        perm1.setSessionDuration(1800);
        perm1.setQueueActivityTimeout(300);
        perm1.setAllowedExtensions((short)10);
        perm1.setSessionActivityTimeout(300);
        perm1.setExtensionDuration(300);
        perm1.setMaximumBookings(10);
        perm1.setRequestCapabilities(rcaps2);
        perm1.setStartTime(start.getTime());
        perm1.setExpiryTime(end.getTime());
        perm1.setDisplayName("bookperm");
        ses.save(perm1);
        
        /* #### BOOKINGS FOR R1 ########################################### */
        Calendar r1tm = TimeUtil.getDayBegin(this.dayStr);
        Bookings bk1 = new Bookings();
        bk1.setActive(true);
        bk1.setDuration(3600);
        /* Slots 2 - 5. */
        r1tm.add(Calendar.MINUTE, 30);
        bk1.setStartTime(r1tm.getTime());
        r1tm.add(Calendar.HOUR, 1);
        bk1.setEndTime(r1tm.getTime());
        bk1.setResourcePermission(perm1);
        bk1.setResourceType("RIG");
        bk1.setRig(r1);
        bk1.setUser(us1);
        bk1.setUserName(us1.getName());
        bk1.setUserNamespace(us1.getNamespace());
        ses.save(bk1);
        Bookings bk2 = new Bookings();
        bk2.setActive(true);
        bk2.setCancelReason("Test cancel.");
        bk2.setDuration(1800);
        /* Slots 8 - 9. */
        r1tm.add(Calendar.MINUTE, 30);
        bk2.setStartTime(r1tm.getTime());
        r1tm.add(Calendar.MINUTE, 30);
        bk2.setEndTime(r1tm.getTime());
        bk2.setResourcePermission(perm1);
        bk2.setResourceType("RIG");
        bk2.setRig(r1);
        bk2.setUser(us1);
        bk2.setUserName(us1.getName());
        bk2.setUserNamespace(us1.getNamespace());
        ses.save(bk2);
        Bookings bk3 = new Bookings();
        bk3.setActive(true);
        bk3.setDuration(7200);
        /* Slots 36 - 43. */
        r1tm.add(Calendar.MINUTE, 30);
        r1tm.add(Calendar.HOUR, 6);
        bk3.setStartTime(r1tm.getTime());
        r1tm.add(Calendar.HOUR, 2);
        bk3.setEndTime(r1tm.getTime());
        bk3.setResourcePermission(perm1);
        bk3.setResourceType("RIG");
        bk3.setRig(r1);
        bk3.setUser(us1);
        bk3.setUserName(us1.getName());
        bk3.setUserNamespace(us1.getNamespace());
        ses.save(bk3);
        
        /* #### BOOKINGS FOR R2 ########################################### */
        Calendar r2tm = TimeUtil.getDayBegin(this.dayStr);
        Bookings bk8 = new Bookings();
        bk8.setActive(true);
        bk8.setDuration(3600);
        /* Slots 13 - 16. */
        r2tm.add(Calendar.HOUR, 3);
        bk8.setStartTime(r2tm.getTime());
        r2tm.add(Calendar.HOUR, 1);
        bk8.setEndTime(r2tm.getTime());
        bk8.setResourcePermission(perm1);
        bk8.setResourceType("RIG");
        bk8.setRig(r2);
        bk8.setUser(us1);
        bk8.setUserName(us1.getName());
        bk8.setUserNamespace(us1.getNamespace());
        ses.save(bk8);
        
        Bookings bk9 = new Bookings();
        bk9.setActive(true);
        bk9.setDuration(1800);
        /* Slots 36 - 37. */
        r2tm.add(Calendar.HOUR, 5);
        bk9.setStartTime(r2tm.getTime());
        r2tm.add(Calendar.MINUTE, 30);
        bk9.setEndTime(r2tm.getTime());
        bk9.setResourcePermission(perm1);
        bk9.setResourceType("RIG");
        bk9.setRig(r2);
        bk9.setUser(us1);
        bk9.setUserName(us1.getName());
        bk9.setUserNamespace(us1.getNamespace());
        ses.save(bk9);
        
        /* #### BOOKINGS FOR R3 ########################################### */
        Calendar r3tm = TimeUtil.getDayBegin(this.dayStr);
        Bookings bk10 = new Bookings();
        bk10.setActive(true);
        bk10.setDuration(1800);
        /* Slots 2 - 3. */
        r3tm.add(Calendar.MINUTE, 30);
        bk10.setStartTime(r3tm.getTime());
        r3tm.add(Calendar.MINUTE, 30);
        bk10.setEndTime(r3tm.getTime());
        bk10.setResourcePermission(perm1);
        bk10.setResourceType("RIG");
        bk10.setRig(r3);
        bk10.setUser(us1);
        bk10.setUserName(us1.getName());
        bk10.setUserNamespace(us1.getNamespace());
        ses.save(bk10);
        
        Bookings bk11 = new Bookings();
        bk11.setActive(true);
        bk11.setDuration(3600);
        /* Slots 12 - 15. */
        r3tm.add(Calendar.HOUR, 2);
        bk11.setStartTime(r3tm.getTime());
        r3tm.add(Calendar.HOUR, 1);
        bk11.setEndTime(r3tm.getTime());
        bk11.setResourcePermission(perm1);
        bk11.setResourceType("RIG");
        bk11.setRig(r3);
        bk11.setUser(us1);
        bk11.setUserName(us1.getName());
        bk11.setUserNamespace(us1.getNamespace());
        ses.save(bk11);
        
        Bookings bk12 = new Bookings();
        bk12.setActive(true);
        bk12.setDuration(3600);
        /* Slots 24 - 27. */
        r3tm.add(Calendar.HOUR, 2);
        bk12.setStartTime(r3tm.getTime());
        r3tm.add(Calendar.HOUR, 1);
        bk12.setEndTime(r3tm.getTime());
        bk12.setResourcePermission(perm1);
        bk12.setResourceType("RIG");
        bk12.setRig(r3);
        bk12.setUser(us1);
        bk12.setUserName(us1.getName());
        bk12.setUserNamespace(us1.getNamespace());
        ses.save(bk12);
        
        /* #### Type bookings for RigType1 ####################################*/
        Calendar rt1tm = TimeUtil.getDayBegin(this.dayStr);
        Bookings bk4 = new Bookings();
        bk4.setActive(true);
        bk4.setDuration(1800);
        /* Slots 0 - 1. */
        bk4.setStartTime(rt1tm.getTime());
        rt1tm.add(Calendar.MINUTE, 30);
        bk4.setEndTime(rt1tm.getTime());
        bk4.setResourcePermission(perm1);
        bk4.setResourceType("TYPE");
        bk4.setRigType(rigType1);
        bk4.setUser(us1);
        bk4.setUserName(us1.getName());
        bk4.setUserNamespace(us1.getNamespace());
        ses.save(bk4);
        
        Bookings bk13 = new Bookings();
        bk13.setActive(true);
        bk13.setDuration(7200);
        /* Slots 0 - 7. */
        rt1tm.add(Calendar.MINUTE, -30);
        bk13.setStartTime(rt1tm.getTime());
        rt1tm.add(Calendar.HOUR, 2);
        bk13.setEndTime(rt1tm.getTime());
        bk13.setResourcePermission(perm1);
        bk13.setResourceType("TYPE");
        bk13.setRigType(rigType1);
        bk13.setUser(us1);
        bk13.setUserName(us1.getName());
        bk13.setUserNamespace(us1.getNamespace());
        ses.save(bk13);
        
        Bookings bk5 = new Bookings();
        bk5.setActive(true);
        bk5.setDuration(3600);
        /* Slots 20 - 23. */
        rt1tm.add(Calendar.HOUR, 3);
        bk5.setStartTime(rt1tm.getTime());
        rt1tm.add(Calendar.HOUR, 1);
        bk5.setEndTime(rt1tm.getTime());
        bk5.setResourcePermission(perm1);
        bk5.setResourceType("TYPE");
        bk5.setRigType(rigType1);
        bk5.setUser(us1);
        bk5.setUserName(us1.getName());
        bk5.setUserNamespace(us1.getNamespace());
        ses.save(bk5);
        
        Bookings bk14 = new Bookings();
        bk14.setActive(true);
        bk14.setDuration(3600);
        /* Slots 24 - 27. */
        bk14.setStartTime(rt1tm.getTime());
        rt1tm.add(Calendar.HOUR, 1);
        bk14.setEndTime(rt1tm.getTime());
        bk14.setResourcePermission(perm1);
        bk14.setResourceType("TYPE");
        bk14.setRigType(rigType1);
        bk14.setUser(us1);
        bk14.setUserName(us1.getName());
        bk14.setUserNamespace(us1.getNamespace());
        ses.save(bk14);
        
        Bookings bk15 = new Bookings();
        bk15.setActive(true);
        bk15.setDuration(5400);
        /* Slots 52 - 58. */
        rt1tm.add(Calendar.HOUR, 6);
        bk15.setStartTime(rt1tm.getTime());
        rt1tm.add(Calendar.HOUR, 1);
        rt1tm.add(Calendar.MINUTE, 30);
        bk15.setEndTime(rt1tm.getTime());
        bk15.setResourcePermission(perm1);
        bk15.setResourceType("TYPE");
        bk15.setRigType(rigType1);
        bk15.setUser(us1);
        bk15.setUserName(us1.getName());
        bk15.setUserNamespace(us1.getNamespace());
        ses.save(bk15);
        
        /* #### Type bookings for RigType2 ####################################*/
        Calendar rt2tm = TimeUtil.getDayBegin(this.dayStr);
        Bookings bk16 = new Bookings();
        bk16.setActive(true);
        bk16.setDuration(3600);
        /* Slots 52 - 59. */
        rt2tm.add(Calendar.HOUR, 7);
        bk16.setStartTime(rt2tm.getTime());
        rt2tm.add(Calendar.HOUR, 1);
        rt2tm.add(Calendar.MINUTE, 15);
        bk16.setEndTime(rt2tm.getTime());
        bk16.setResourcePermission(perm1);
        bk16.setResourceType("TYPE");
        bk16.setRigType(rigType2);
        bk16.setUser(us1);
        bk16.setUserName(us1.getName());
        bk16.setUserNamespace(us1.getNamespace());
        ses.save(bk16);
        
        /* #### Bookings for Request Caps 1. #################################*/
        Calendar rcap1tm = TimeUtil.getDayBegin(this.dayStr);
        Bookings bk6 = new Bookings();
        bk6.setActive(true);
        bk6.setDuration(1800);
        /* Slots 20 - 21. */
        rcap1tm.add(Calendar.HOUR, 5);
        bk6.setStartTime(rcap1tm.getTime());
        rcap1tm.add(Calendar.MINUTE, 30);
        bk6.setEndTime(rcap1tm.getTime());
        bk6.setResourcePermission(perm1);
        bk6.setResourceType("CAPABILITY");
        bk6.setRequestCapabilities(rcaps1);
        bk6.setUser(us1);
        bk6.setUserName(us1.getName());
        bk6.setUserNamespace(us1.getNamespace());
        ses.save(bk6);
        
        Bookings bk7 = new Bookings();
        bk7.setActive(true);
        bk7.setDuration(1800);
        /* Slots 22 - 23. */
        bk7.setStartTime(rcap1tm.getTime());
        rcap1tm.add(Calendar.MINUTE, 30);
        bk7.setEndTime(rcap1tm.getTime());
        bk7.setResourcePermission(perm1);
        bk7.setResourceType("CAPABILITY");
        bk7.setRequestCapabilities(rcaps1);
        bk7.setUser(us1);
        bk7.setUserName(us1.getName());
        bk7.setUserNamespace(us1.getNamespace());
        ses.save(bk7);
        
        Bookings bk17 = new Bookings();
        bk17.setActive(true);
        bk17.setDuration(1800);
        /* Slots 32 - 35. */
        rcap1tm.add(Calendar.HOUR, 2);
        bk17.setStartTime(rcap1tm.getTime());
        rcap1tm.add(Calendar.HOUR, 1);
        bk17.setEndTime(rcap1tm.getTime());
        bk17.setResourcePermission(perm1);
        bk17.setResourceType("CAPABILITY");
        bk17.setRequestCapabilities(rcaps1);
        bk17.setUser(us1);
        bk17.setUserName(us1.getName());
        bk17.setUserNamespace(us1.getNamespace());
        ses.save(bk17);
        
        /* #### Bookings for Request Caps 2. #################################*/
        Calendar rcap2tm = TimeUtil.getDayBegin(this.dayStr);
        Bookings bk18 = new Bookings();
        bk18.setActive(true);
        bk18.setDuration(1800);
        /* Slots 56 - 59. */
        rcap2tm.add(Calendar.HOUR, 14);
        bk18.setStartTime(rcap2tm.getTime());
        rcap2tm.add(Calendar.HOUR, 1);
        bk18.setEndTime(rcap2tm.getTime());
        bk18.setResourcePermission(perm1);
        bk18.setResourceType("CAPABILITY");
        bk18.setRequestCapabilities(rcaps2);
        bk18.setUser(us1);
        bk18.setUserName(us1.getName());
        bk18.setUserNamespace(us1.getNamespace());
        ses.save(bk18);
        
        /* #### Bookings for Request Caps 3. #################################*/
        Calendar rcap3tm = TimeUtil.getDayBegin(this.dayStr);
        Bookings bk19 = new Bookings();
        bk19.setActive(true);
        bk19.setDuration(4500);
        /* Slots 56 - 59. */
        rcap3tm.add(Calendar.HOUR, 7);
        bk19.setStartTime(rcap3tm.getTime());
        rcap3tm.add(Calendar.HOUR, 1);
        rcap3tm.add(Calendar.MINUTE, 15);
        bk19.setEndTime(rcap3tm.getTime());
        bk19.setResourcePermission(perm1);
        bk19.setResourceType("CAPABILITY");
        bk19.setRequestCapabilities(rcaps3);
        bk19.setUser(us1);
        bk19.setUserName(us1.getName());
        bk19.setUserNamespace(us1.getNamespace());
        ses.save(bk19);
        
        ses.getTransaction().commit();
        
        ses.beginTransaction();
        MatchingCapabilities mat1 = new MatchingCapabilities(rcaps1, caps1);
        ses.save(mat1);
        MatchingCapabilities mat2 = new MatchingCapabilities(rcaps1, caps2);
        ses.save(mat2);
        MatchingCapabilities mat3 = new MatchingCapabilities(rcaps1, caps3);
        ses.save(mat3);
        MatchingCapabilities mat4 = new MatchingCapabilities(rcaps2, caps1);
        ses.save(mat4);
        MatchingCapabilities mat5 = new MatchingCapabilities(rcaps2, caps3);
        ses.save(mat5);
        MatchingCapabilities mat6 = new MatchingCapabilities(rcaps3, caps2);
        ses.save(mat6);
        ses.getTransaction().commit();
        
        ses.refresh(rcaps1);
        ses.refresh(rcaps2);
        ses.refresh(rcaps3);
        ses.refresh(caps1);
        ses.refresh(caps2);
        ses.refresh(caps3);
        ses.refresh(r1);
        ses.refresh(r2);
        ses.refresh(r3);
        ses.refresh(rigType1);
        ses.refresh(rigType2);
        ses.refresh(uclass1);
        ses.refresh(us1);
        
        FindFreeBookings request = new FindFreeBookings();
        FindBookingSlotType slotReq = new FindBookingSlotType();
        request.setFindBookingSlots(slotReq);
        UserIDType uid = new UserIDType();
        uid.setUserQName(us1.getNamespace() + ':' + us1.getName());
        slotReq.setUserID(uid);
        PermissionIDType pid = new PermissionIDType();
        pid.setPermissionID(perm1.getId().intValue());
        slotReq.setPermissionID(pid);
        
        TimePeriodType tp = new TimePeriodType();
        tp.setStartTime(start);
        tp.setEndTime(end);
        slotReq.setPeriod(tp);
        
        FindFreeBookingsResponse response = this.service.findFreeBookings(request);
        
        ses.beginTransaction();
        ses.delete(bk1);
        ses.delete(bk2);
        ses.delete(bk3);
        ses.delete(bk4);
        ses.delete(bk5);
        ses.delete(bk6);
        ses.delete(bk7);
        ses.delete(bk8);
        ses.delete(bk9);
        ses.delete(bk10);
        ses.delete(bk11);
        ses.delete(bk12);
        ses.delete(bk13);
        ses.delete(bk14);
        ses.delete(bk15);
        ses.delete(bk16);
        ses.delete(bk17);
        ses.delete(bk18);
        ses.delete(bk19);
        ses.delete(perm1);
        ses.delete(r1);
        ses.delete(r2);
        ses.delete(r3);
        ses.delete(mat1);
        ses.delete(mat2);
        ses.delete(mat3);
        ses.delete(mat4);
        ses.delete(mat5);
        ses.delete(mat6);
        ses.delete(caps1);
        ses.delete(caps2);
        ses.delete(caps3);
        ses.delete(rcaps1);
        ses.delete(rcaps2);
        ses.delete(rcaps3);
        ses.delete(rigType1);
        ses.delete(rigType2);
        ses.delete(assoc);
        ses.delete(us1);
        ses.delete(uclass1);
        ses.getTransaction().commit();
        
        assertTrue(bk1.isActive());
        assertTrue(bk2.isActive());
        assertTrue(bk3.isActive());
        assertTrue(bk4.isActive());
        assertTrue(bk5.isActive());
        assertTrue(bk6.isActive());
        assertTrue(bk7.isActive());
        assertTrue(bk8.isActive());
        assertTrue(bk9.isActive());
        assertTrue(bk10.isActive());
        assertTrue(bk11.isActive());
        assertTrue(bk12.isActive());
        assertTrue(bk13.isActive());
        assertTrue(bk14.isActive());
        assertTrue(bk15.isActive());
        assertTrue(bk16.isActive());
        assertTrue(bk17.isActive());
        assertTrue(bk18.isActive());
        assertTrue(bk19.isActive());
        
        assertNotNull(response);
        BookingSlotListType slots = response.getFindFreeBookingsResponse();
        assertNotNull(slots);
        
        ResourceIDType res = slots.getResourceID();
        assertNotNull(res);
        assertEquals("CAPABILITY", res.getType());
        assertEquals(rcaps2.getId().intValue(), res.getResourceID());
        assertEquals(rcaps2.getCapabilities(), res.getResourceName());
        
        pid = slots.getPermissionID();
        assertNotNull(pid);
        assertEquals(perm1.getId().intValue(), pid.getPermissionID());
        
        BookingSlotType slotsList[] = slots.getBookingSlot();
        assertNotNull(slotsList);
        
        for (BookingSlotType s : slotsList)
        {
            assertNotNull(s.getState());
            
            TimePeriodType t = s.getSlot();
            assertNotNull(t);
            assertNotNull(t.getStartTime());
            assertNotNull(t.getEndTime());
        }

        assertEquals(5, slotsList.length);
        
        BookingSlotType bs = slotsList[0];
        assertEquals(SlotState.FREE, bs.getState());
        Calendar s = bs.getSlot().getStartTime();
        assertEquals(0, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        Calendar e = bs.getSlot().getEndTime();
        assertEquals(0, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        bs = slotsList[1];
        assertEquals(SlotState.BOOKED, bs.getState());
        s = bs.getSlot().getStartTime();
        assertEquals(0, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        e = bs.getSlot().getEndTime();
        assertEquals(1, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        bs = slotsList[2];
        assertEquals(SlotState.FREE, bs.getState());
        s = bs.getSlot().getStartTime();
        assertEquals(1, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        e = bs.getSlot().getEndTime();
        assertEquals(8, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        
        bs = slotsList[3];
        assertEquals(SlotState.BOOKED, bs.getState());
        s = bs.getSlot().getStartTime();
        assertEquals(8, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        e = bs.getSlot().getEndTime();
        assertEquals(8, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(15, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        bs = slotsList[4];
        assertEquals(SlotState.FREE, bs.getState());
        s = bs.getSlot().getStartTime();
        assertEquals(8, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(15, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        e = bs.getSlot().getEndTime();
        assertEquals(0, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
    }
    
    /**
     * Test method for {@link BookingsService#findFreeBookings(types.FindFreeBookings)}.
     */
    public void testFindFreeBookingsNoPermission()
    {
        Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        User user = new User();
        user.setName("user");
        user.setNamespace("TEST");
        user.setPersona("USER");
        ses.save(user);
        ses.getTransaction().commit();
        
        FindFreeBookings request = new FindFreeBookings();
        FindBookingSlotType wrp = new FindBookingSlotType();
        request.setFindBookingSlots(wrp);
        UserIDType uid = new UserIDType();
        uid.setUserQName("TEST:user");
        wrp.setUserID(uid);
        PermissionIDType perm = new PermissionIDType();
        perm.setPermissionID(1023023);
        wrp.setPermissionID(perm);
        TimePeriodType tp = new TimePeriodType();
        tp.setEndTime(Calendar.getInstance());
        tp.setStartTime(Calendar.getInstance());
        wrp.setPeriod(tp);
        
        FindFreeBookingsResponse resp = this.service.findFreeBookings(request);
        assertNotNull(resp);
        
        ses.beginTransaction();
        ses.delete(user);
        ses.getTransaction().commit();
        
        
        /* Make sure it can be serialised. */
        try
        {
            OMElement ele = resp.getOMElement(FindFreeBookingsResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
            assertNotNull(ele);
            
            String xml = ele.toStringWithConsume();
            assertNotNull(xml);
        }
        catch (Exception e)
        {
            fail("Failed serialising wrong permission response.");
        }
    }
    
    /**
     * Test method for {@link BookingsService#findFreeBookings(types.FindFreeBookings)}.
     */
    public void testFindFreeBookingsNoUser()
    {
        FindFreeBookings request = new FindFreeBookings();
        FindBookingSlotType wrp = new FindBookingSlotType();
        request.setFindBookingSlots(wrp);
        UserIDType uid = new UserIDType();
        uid.setUserQName("WRONG:user");
        wrp.setUserID(uid);
        PermissionIDType perm = new PermissionIDType();
        perm.setPermissionID(10);
        wrp.setPermissionID(perm);
        TimePeriodType tp = new TimePeriodType();
        tp.setEndTime(Calendar.getInstance());
        tp.setStartTime(Calendar.getInstance());
        wrp.setPeriod(tp);
        
        FindFreeBookingsResponse resp = this.service.findFreeBookings(request);
        assertNotNull(resp);
        
        /* Make sure it can be serialised. */
        try
        {
            OMElement ele = resp.getOMElement(FindFreeBookingsResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
            assertNotNull(ele);
            
            String xml = ele.toStringWithConsume();
            assertNotNull(xml);
        }
        catch (Exception e)
        {
            fail("Failed serialising wrong user response.");
        }
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
