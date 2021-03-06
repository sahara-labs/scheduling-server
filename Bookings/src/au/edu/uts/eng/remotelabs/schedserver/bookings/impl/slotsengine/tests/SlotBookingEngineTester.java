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
 * @date 11th November 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine.tests;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;

import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.BookingEngine.BookingCreation;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.BookingEngine.TimePeriod;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine.DayBookings;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine.MBooking;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine.RigBookings;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine.SlotBookingEngine;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine.TimeUtil;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Bookings;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.MatchingCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RequestCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClass;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.testsetup.DataAccessTestSetup;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.logger.impl.SystemErrLogger;

/**
 * Tests the {@link SlotBookingEngine} class.
 */
public class SlotBookingEngineTester extends TestCase
{
    /** Object of class under test. */
    private SlotBookingEngine engine;
    
    /** The day string for tomorrow. */
    private String dayKey;
    
    @Override
    @Before
    public void setUp() throws Exception
    {
        DataAccessTestSetup.setup();
        
        Field f = LoggerActivator.class.getDeclaredField("logger");
        f.setAccessible(true);
        f.set(null, new SystemErrLogger());
        
        this.engine = new SlotBookingEngine(); 
        f = SlotBookingEngine.class.getDeclaredField("logger");
        f.setAccessible(true);
        f.set(this.engine, new SystemErrLogger());
        this.engine.init();
        
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        this.dayKey = TimeUtil.getDayKey(cal);
    }
    
    @Test
    @SuppressWarnings("unchecked")
    public void testCleanStaleDays() throws Exception
    {
        Field f = SlotBookingEngine.class.getDeclaredField("days");
        f.setAccessible(true);
        Map<String, DayBookings> days = (Map<String, DayBookings>)f.get(this.engine);
        
        f = SlotBookingEngine.class.getDeclaredField("dayHitCounts");
        f.setAccessible(true);
        Map<String, Integer> dayHits = (Map<String, Integer>)f.get(this.engine);
        
        Calendar cal = Calendar.getInstance();
        String day = TimeUtil.getDayKey(cal);
        days.put(day, new DayBookings(day));
        dayHits.put(day, 0);
        
        /* Past days. */
        for (int i = 1; i <= 5; i++)
        {
            cal.add(Calendar.DAY_OF_MONTH, -1);
            day = TimeUtil.getDayKey(cal);
            days.put(day, new DayBookings(day));
            dayHits.put(day, 0);
        }
        
        /* Future days. */
        cal.setTimeInMillis(System.currentTimeMillis());
        for (int i = 1; i <= 100; i++)
        {
            cal.add(Calendar.DAY_OF_MONTH, 1);
            day = TimeUtil.getDayKey(cal);
            days.put(day, new DayBookings(day));
            dayHits.put(day, 0);
        }
        
        this.engine.cleanStaleDays();
        
        cal.setTimeInMillis(System.currentTimeMillis());
        for (String dk : days.keySet())
        {
            Calendar dkend = TimeUtil.getDayEnd(dk);
            if (dkend.before(cal)) fail("Old day " + dk);
            else if (dkend.get(Calendar.DAY_OF_MONTH) - cal.get(Calendar.DAY_OF_MONTH) > SlotBookingEngine.HOT_DAYS)
            {
                fail("Non-hot day " + dk);
            }
        }
    }
    
    @Test
    @SuppressWarnings("unchecked")
    public void testCleanStaleDays2() throws Exception
    {
        Field f = SlotBookingEngine.class.getDeclaredField("days");
        f.setAccessible(true);
        Map<String, DayBookings> days = (Map<String, DayBookings>)f.get(this.engine);
        
        f = SlotBookingEngine.class.getDeclaredField("dayHitCounts");
        f.setAccessible(true);
        Map<String, Integer> dayHits = (Map<String, Integer>)f.get(this.engine);
        
        Calendar cal = Calendar.getInstance();
        String day = TimeUtil.getDayKey(cal);
        days.put(day, new DayBookings(day));
        dayHits.put(day, 5);
        
        /* Past days. */
        for (int i = 1; i <= 5; i++)
        {
            cal.add(Calendar.DAY_OF_MONTH, -1);
            day = TimeUtil.getDayKey(cal);
            days.put(day, new DayBookings(day));
            dayHits.put(day, 5);
        }
        
        /* Future days. */
        cal.setTimeInMillis(System.currentTimeMillis());
        for (int i = 1; i <= 100; i++)
        {
            cal.add(Calendar.DAY_OF_MONTH, 1);
            day = TimeUtil.getDayKey(cal);
            days.put(day, new DayBookings(day));
            dayHits.put(day, 5);
        }
        
        this.engine.cleanStaleDays();
        
        cal.setTimeInMillis(System.currentTimeMillis());
        for (String dk : days.keySet())
        {
            Calendar dkend = TimeUtil.getDayEnd(dk);
            if (dkend.before(cal))
            {
                fail("Old day " + dk);
            }
            else if (dkend.get(Calendar.DAY_OF_MONTH) - cal.get(Calendar.DAY_OF_MONTH) > SlotBookingEngine.MAX_DAYS)
            {
                fail("Non-hot day " + dk);
            }
            else if (dkend.get(Calendar.DAY_OF_MONTH) - cal.get(Calendar.DAY_OF_MONTH) > SlotBookingEngine.HOT_DAYS)
            {
                assertEquals(2, dayHits.get(dk).intValue());
            }
            
            dayHits.remove(dk);
        }
        
        assertEquals(0, dayHits.size());
    }
    
    @Test
    public void testIsFreeFor()
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
        ResourcePermission perm1 = new ResourcePermission();
        perm1.setUserClass(uclass1);
        perm1.setType("RIG");
        perm1.setSessionDuration(3600);
        perm1.setQueueActivityTimeout(300);
        perm1.setAllowedExtensions((short)10);
        perm1.setSessionActivityTimeout(300);
        perm1.setExtensionDuration(300);
        perm1.setMaximumBookings(10);
        perm1.setRig(r1);
        perm1.setStartTime(new Date());
        perm1.setExpiryTime(new Date());
        perm1.setDisplayName("bookperm");
        ses.save(perm1);

        /* #### BOOKINGS FOR R1 ########################################### */
        Calendar tm = Calendar.getInstance();
        Bookings bk1 = new Bookings();
        bk1.setActive(true);
        bk1.setDuration(3600);
        /* Slots 2 - 5. */
        tm.add(Calendar.MINUTE, 30);
        bk1.setStartTime(tm.getTime());
        tm.add(Calendar.HOUR, 1);
        bk1.setEndTime(tm.getTime());
        bk1.setResourcePermission(perm1);
        bk1.setResourceType("RIG");
        bk1.setRig(r1);
        bk1.setUser(us1);
        bk1.setUserName(us1.getName());
        bk1.setUserNamespace(us1.getNamespace());
        ses.save(bk1);
       
        ses.getTransaction().commit();

        ses.refresh(caps1);
        ses.refresh(r1);
        ses.refresh(rigType1);

        boolean free = this.engine.isFreeFor(r1, 3600, ses);

        ses.beginTransaction();
        ses.delete(bk1);
        ses.delete(perm1);
        ses.delete(r1);
        ses.delete(caps1);
        ses.delete(rigType1);
        ses.delete(us1);
        ses.delete(uclass1);
        ses.getTransaction().commit();

        assertTrue(bk1.isActive());

        assertFalse(free);
    }
    
    @Test
    public void testIsFreeFor2()
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
        ResourcePermission perm1 = new ResourcePermission();
        perm1.setUserClass(uclass1);
        perm1.setType("RIG");
        perm1.setSessionDuration(3600);
        perm1.setQueueActivityTimeout(300);
        perm1.setAllowedExtensions((short)10);
        perm1.setSessionActivityTimeout(300);
        perm1.setExtensionDuration(300);
        perm1.setMaximumBookings(10);
        perm1.setRig(r1);
        perm1.setStartTime(new Date());
        perm1.setExpiryTime(new Date());
        perm1.setDisplayName("bookperm");
        ses.save(perm1);

        /* #### BOOKINGS FOR R1 ########################################### */
        Calendar tm = Calendar.getInstance();
        Bookings bk1 = new Bookings();
        bk1.setActive(true);
        bk1.setDuration(3600);
        /* Slots 2 - 5. */
        tm.add(Calendar.HOUR, 2);
        bk1.setStartTime(tm.getTime());
        tm.add(Calendar.HOUR, 1);
        bk1.setEndTime(tm.getTime());
        bk1.setResourcePermission(perm1);
        bk1.setResourceType("RIG");
        bk1.setRig(r1);
        bk1.setUser(us1);
        bk1.setUserName(us1.getName());
        bk1.setUserNamespace(us1.getNamespace());
        ses.save(bk1);
       
        ses.getTransaction().commit();

        ses.refresh(caps1);
        ses.refresh(r1);
        ses.refresh(rigType1);

        boolean free = this.engine.isFreeFor(r1, 3600, ses);

        ses.beginTransaction();
        ses.delete(bk1);
        ses.delete(perm1);
        ses.delete(r1);
        ses.delete(caps1);
        ses.delete(rigType1);
        ses.delete(us1);
        ses.delete(uclass1);
        ses.getTransaction().commit();

        assertTrue(bk1.isActive());

        assertTrue(free);
    }
    
    @Test
    public void testInitCancelOldBookings()
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
        ResourcePermission perm1 = new ResourcePermission();
        perm1.setUserClass(uclass1);
        perm1.setType("RIG");
        perm1.setSessionDuration(3600);
        perm1.setQueueActivityTimeout(300);
        perm1.setAllowedExtensions((short)10);
        perm1.setSessionActivityTimeout(300);
        perm1.setExtensionDuration(300);
        perm1.setMaximumBookings(10);
        perm1.setRig(r1);
        perm1.setStartTime(new Date());
        perm1.setExpiryTime(new Date());
        perm1.setDisplayName("bookperm");
        ses.save(perm1);

        /* Expired today. */
        Calendar now = Calendar.getInstance();
        Bookings bk1 = new Bookings();
        bk1.setActive(true);
        bk1.setDuration(3600);
        now.add(Calendar.HOUR, -2);
        bk1.setStartTime(now.getTime());
        now.add(Calendar.HOUR, 1);
        bk1.setEndTime(now.getTime());
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
        now.add(Calendar.DAY_OF_MONTH, -3);
        bk2.setStartTime(now.getTime());
        now.add(Calendar.MINUTE, 30);
        bk2.setEndTime(now.getTime());
        bk2.setResourcePermission(perm1);
        bk2.setResourceType("RIG");
        bk2.setRig(r1);
        bk2.setUser(us1);
        bk2.setUserName(us1.getName());
        bk2.setUserNamespace(us1.getNamespace());
        ses.save(bk2);
        Calendar ok = Calendar.getInstance();
        Bookings bk3 = new Bookings();
        bk3.setActive(true);
        bk3.setDuration(7200);
        ok.add(Calendar.HOUR, -2);
        bk3.setStartTime(ok.getTime());
        ok.add(Calendar.HOUR, 2);
        bk3.setEndTime(ok.getTime());
        bk3.setResourcePermission(perm1);
        bk3.setResourceType("RIG");
        bk3.setRig(r1);
        bk3.setUser(us1);
        bk3.setUserName(us1.getName());
        bk3.setUserNamespace(us1.getNamespace());
        ses.save(bk3);
        Bookings bk4 = new Bookings();
        bk4.setActive(true);
        bk4.setDuration(7200);
        ok.add(Calendar.DAY_OF_MONTH, 3);
        bk4.setStartTime(ok.getTime());
        ok.add(Calendar.HOUR, 2);
        bk4.setEndTime(ok.getTime());
        bk4.setResourcePermission(perm1);
        bk4.setResourceType("RIG");
        bk4.setRig(r1);
        bk4.setUser(us1);
        bk4.setUserName(us1.getName());
        bk4.setUserNamespace(us1.getNamespace());
        ses.save(bk4);
        ses.getTransaction().commit();
        
        this.engine.init();
        ses.refresh(bk1);
        ses.refresh(bk2);
        ses.refresh(bk3);
        ses.refresh(bk4);
        
        ses.beginTransaction();
        ses.delete(bk4);
        ses.delete(bk3);
        ses.delete(bk2);
        ses.delete(bk1);
        ses.delete(perm1);
        ses.delete(r1);
        ses.delete(caps1);
        ses.delete(rigType1);
        ses.delete(us1);
        ses.delete(uclass1);
        ses.getTransaction().commit();
        
        assertFalse(bk1.isActive());
        assertNotNull(bk1.getCancelReason());
        assertFalse(bk2.isActive());
        assertNotNull(bk2.getCancelReason());
        assertTrue(bk3.isActive());
        assertNull(bk3.getCancelReason());
        assertTrue(bk4.isActive());
        assertNull(bk4.getCancelReason());
    }
    
    @Test
    public void testCreateBookingBFR1()
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
        ResourcePermission perm1 = new ResourcePermission();
        perm1.setUserClass(uclass1);
        perm1.setType("RIG");
        perm1.setSessionDuration(3600);
        perm1.setQueueActivityTimeout(300);
        perm1.setAllowedExtensions((short)10);
        perm1.setSessionActivityTimeout(300);
        perm1.setExtensionDuration(300);
        perm1.setMaximumBookings(10);
        perm1.setRig(r1);
        Calendar permTime = Calendar.getInstance();
        permTime.add(Calendar.DAY_OF_MONTH, -10);
        perm1.setStartTime(permTime.getTime());
        permTime.add(Calendar.DAY_OF_MONTH, 20);
        perm1.setExpiryTime(permTime.getTime());
        perm1.setDisplayName("bookperm");
        ses.save(perm1);
        
        /* #### BOOKINGS FOR R1 ########################################### */
        Calendar r1tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar r2tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar r3tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rt1tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rt2tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rcap1tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rcap2tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rcap3tm = TimeUtil.getDayBegin(this.dayKey);
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
        
        /* Force a load. */
        this.engine.getFreeTimes(rigType1, new TimePeriod(TimeUtil.getDayBegin(this.dayKey), 
                TimeUtil.getDayEnd(this.dayKey)), 3600, ses);
        
        Calendar bkSt = TimeUtil.getDayBegin(this.dayKey);
        bkSt.add(Calendar.HOUR, 2);
        Calendar bkEd = TimeUtil.getDayBegin(this.dayKey);
        bkEd.add(Calendar.HOUR, 3);
        TimePeriod tp = new TimePeriod(bkSt, bkEd);
        BookingCreation cre = this.engine.createBooking(us1, perm1, tp, ses);
        
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

        assertNotNull(cre);
        assertFalse(cre.wasCreated());
        
        Bookings b = cre.getBooking();
        assertNull(b);
        
        List<TimePeriod> ltp = cre.getBestFits();
        assertNotNull(ltp);
        assertEquals(2, ltp.size());
        
        TimePeriod ef = ltp.get(0);
        Calendar s = ef.getStartTime();
        assertEquals(1, s.get(Calendar.HOUR));
        assertEquals(30, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        assertEquals(bkSt.get(Calendar.DAY_OF_MONTH), s.get(Calendar.DAY_OF_MONTH));
        assertEquals(bkSt.get(Calendar.MONTH), s.get(Calendar.MONTH));
        assertEquals(bkSt.get(Calendar.YEAR), s.get(Calendar.YEAR));
        Calendar e = ef.getEndTime();
        assertEquals(2, e.get(Calendar.HOUR));
        assertEquals(0, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        assertEquals(bkSt.get(Calendar.DAY_OF_MONTH), e.get(Calendar.DAY_OF_MONTH));
        assertEquals(bkSt.get(Calendar.MONTH), e.get(Calendar.MONTH));
        assertEquals(bkSt.get(Calendar.YEAR), e.get(Calendar.YEAR));
        
        TimePeriod lf = ltp.get(1);
        s = lf.getStartTime();
        assertEquals(2, s.get(Calendar.HOUR));
        assertEquals(30, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        assertEquals(bkSt.get(Calendar.DAY_OF_MONTH), s.get(Calendar.DAY_OF_MONTH));
        assertEquals(bkSt.get(Calendar.MONTH), s.get(Calendar.MONTH));
        assertEquals(bkSt.get(Calendar.YEAR), s.get(Calendar.YEAR));
        e = lf.getEndTime();
        assertEquals(3, e.get(Calendar.HOUR));
        assertEquals(30, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        assertEquals(bkSt.get(Calendar.DAY_OF_MONTH), s.get(Calendar.DAY_OF_MONTH));
        assertEquals(bkSt.get(Calendar.MONTH), s.get(Calendar.MONTH));
        assertEquals(bkSt.get(Calendar.YEAR), s.get(Calendar.YEAR));
    }
    
    @Test
    public void testCreateBookingBFT2()
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
        ResourcePermission perm1 = new ResourcePermission();
        perm1.setUserClass(uclass1);
        perm1.setType("TYPE");
        perm1.setSessionDuration(3600);
        perm1.setQueueActivityTimeout(300);
        perm1.setAllowedExtensions((short)10);
        perm1.setSessionActivityTimeout(300);
        perm1.setExtensionDuration(300);
        perm1.setMaximumBookings(10);
        perm1.setRigType(rigType2);
        Calendar permTime = Calendar.getInstance();
        permTime.add(Calendar.DAY_OF_MONTH, -10);
        perm1.setStartTime(permTime.getTime());
        permTime.add(Calendar.DAY_OF_MONTH, 20);
        perm1.setExpiryTime(permTime.getTime());
        perm1.setDisplayName("bookperm");
        ses.save(perm1);
        
        /* #### BOOKINGS FOR R1 ########################################### */
        Calendar r1tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar r2tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar r3tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rt1tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rt2tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rcap1tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rcap2tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rcap3tm = TimeUtil.getDayBegin(this.dayKey);
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
        
        /* Force a load. */
        this.engine.getFreeTimes(rigType1, new TimePeriod(TimeUtil.getDayBegin(this.dayKey), 
                TimeUtil.getDayEnd(this.dayKey)), 3600, ses);
        
        Calendar bkSt = TimeUtil.getDayBegin(this.dayKey);
        bkSt.add(Calendar.MINUTE, 30);
        Calendar bkEd = TimeUtil.getDayBegin(this.dayKey);
        bkEd.add(Calendar.HOUR, 1);
        bkEd.add(Calendar.MINUTE, 30);
        TimePeriod tp = new TimePeriod(bkSt, bkEd);
        BookingCreation cre = this.engine.createBooking(us1, perm1, tp, ses);
        
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
        
        assertNotNull(cre);
        assertFalse(cre.wasCreated());
        
        Bookings b = cre.getBooking();
        assertNull(b);
        
        List<TimePeriod> ltp = cre.getBestFits();
        assertNotNull(ltp);
        assertEquals(2, ltp.size());
        
        TimePeriod ef = ltp.get(0);
        Calendar s = ef.getStartTime();
        assertEquals(0, s.get(Calendar.HOUR));
        assertEquals(0, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        assertEquals(bkSt.get(Calendar.DAY_OF_MONTH), s.get(Calendar.DAY_OF_MONTH));
        assertEquals(bkSt.get(Calendar.MONTH), s.get(Calendar.MONTH));
        assertEquals(bkSt.get(Calendar.YEAR), s.get(Calendar.YEAR));
        Calendar e = ef.getEndTime();
        assertEquals(0, e.get(Calendar.HOUR));
        assertEquals(30, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        assertEquals(bkSt.get(Calendar.DAY_OF_MONTH), e.get(Calendar.DAY_OF_MONTH));
        assertEquals(bkSt.get(Calendar.MONTH), e.get(Calendar.MONTH));
        assertEquals(bkSt.get(Calendar.YEAR), e.get(Calendar.YEAR));
        
        TimePeriod lf = ltp.get(1);
        s = lf.getStartTime();
        assertEquals(1, s.get(Calendar.HOUR));
        assertEquals(0, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        assertEquals(bkSt.get(Calendar.DAY_OF_MONTH), s.get(Calendar.DAY_OF_MONTH));
        assertEquals(bkSt.get(Calendar.MONTH), s.get(Calendar.MONTH));
        assertEquals(bkSt.get(Calendar.YEAR), s.get(Calendar.YEAR));
        e = lf.getEndTime();
        assertEquals(2, e.get(Calendar.HOUR));
        assertEquals(0, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        assertEquals(bkSt.get(Calendar.DAY_OF_MONTH), s.get(Calendar.DAY_OF_MONTH));
        assertEquals(bkSt.get(Calendar.MONTH), s.get(Calendar.MONTH));
        assertEquals(bkSt.get(Calendar.YEAR), s.get(Calendar.YEAR));
    }
    
    @Test
    public void testCreateBookingC1()
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
        ResourcePermission perm1 = new ResourcePermission();
        perm1.setUserClass(uclass1);
        perm1.setType("CAPABILITY");
        perm1.setSessionDuration(3600);
        perm1.setQueueActivityTimeout(300);
        perm1.setAllowedExtensions((short)10);
        perm1.setSessionActivityTimeout(300);
        perm1.setExtensionDuration(300);
        perm1.setMaximumBookings(10);
        perm1.setRequestCapabilities(rcaps1);
        perm1.setStartTime(new Date());
        perm1.setExpiryTime(new Date());
        perm1.setDisplayName("bookperm");
        ses.save(perm1);
        
        /* #### BOOKINGS FOR R1 ########################################### */
        Calendar r1tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar r2tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar r3tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rt1tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rt2tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rcap1tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rcap2tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rcap3tm = TimeUtil.getDayBegin(this.dayKey);
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
        
        /* Force a load. */
        this.engine.getFreeTimes(rigType1, new TimePeriod(TimeUtil.getDayBegin(this.dayKey), 
                TimeUtil.getDayEnd(this.dayKey)), 3600, ses);
        
        Calendar bkSt = TimeUtil.getDayBegin(this.dayKey);
        bkSt.add(Calendar.HOUR, 5);
        Calendar bkEd = TimeUtil.getDayBegin(this.dayKey);
        bkEd.add(Calendar.HOUR, 6);
        TimePeriod tp = new TimePeriod(bkSt, bkEd);
        BookingCreation cre = this.engine.createBooking(us1, perm1, tp, ses);
        
        ses.beginTransaction();
        ses.delete(cre.getBooking());
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

        assertNotNull(cre);
        assertTrue(cre.wasCreated());
        
        Bookings b = cre.getBooking();
        assertNotNull(b);
        assertTrue(b.isActive());
        assertEquals(3600, b.getDuration());
    }
    
    @Test
    public void testCreateBookingR2()
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
        ResourcePermission perm1 = new ResourcePermission();
        perm1.setUserClass(uclass1);
        perm1.setType("RIG");
        perm1.setSessionDuration(3600);
        perm1.setQueueActivityTimeout(300);
        perm1.setAllowedExtensions((short)10);
        perm1.setSessionActivityTimeout(300);
        perm1.setExtensionDuration(300);
        perm1.setMaximumBookings(10);
        perm1.setRig(r2);
        perm1.setStartTime(new Date());
        perm1.setExpiryTime(new Date());
        perm1.setDisplayName("bookperm");
        ses.save(perm1);
        
        /* #### BOOKINGS FOR R1 ########################################### */
        Calendar r1tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar r2tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar r3tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rt1tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rt2tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rcap1tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rcap2tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rcap3tm = TimeUtil.getDayBegin(this.dayKey);
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
        
        /* Force a load. */
        this.engine.getFreeTimes(rigType1, new TimePeriod(TimeUtil.getDayBegin(this.dayKey), 
                TimeUtil.getDayEnd(this.dayKey)), 3600, ses);
        
        Calendar bkSt = TimeUtil.getDayBegin(this.dayKey);
        bkSt.add(Calendar.HOUR, 5);
        Calendar bkEd = TimeUtil.getDayBegin(this.dayKey);
        bkEd.add(Calendar.HOUR, 7);
        TimePeriod tp = new TimePeriod(bkSt, bkEd);
        BookingCreation cre = this.engine.createBooking(us1, perm1, tp, ses);
        
        ses.beginTransaction();
        ses.delete(cre.getBooking());
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

        assertNotNull(cre);
        assertTrue(cre.wasCreated());
        
        Bookings b = cre.getBooking();
        assertNotNull(b);
        assertTrue(b.isActive());
        assertEquals(7200, b.getDuration());
    }
    
    @Test
    public void testCreateBooking() throws Exception
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
        RigType rigType1 = new RigType("booktestrigtype", 300, false);
        ses.save(rigType1);
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
        
        /* #### BOOKINGS FOR R1 ########################################### */
        Calendar r1tm = TimeUtil.getDayBegin(this.dayKey);
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

        /* #### BOOKINGS FOR R2 ########################################### */
        Calendar r2tm = TimeUtil.getDayBegin(this.dayKey);
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

        /* #### Type bookings for RigType1 ####################################*/
        Calendar rt1tm = TimeUtil.getDayBegin(this.dayKey);
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
        
        /* #### Bookings for Request Caps 1. #################################*/
        Calendar rcap1tm = TimeUtil.getDayBegin(this.dayKey);
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
        
        /* #### Bookings for Request Caps 2. #################################*/
        Calendar rcap2tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rcap3tm = TimeUtil.getDayBegin(this.dayKey);
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
        ses.refresh(rigType1);
        
        /* Force a load. */
        this.engine.getFreeTimes(rigType1, new TimePeriod(TimeUtil.getDayBegin(this.dayKey), 
                TimeUtil.getDayEnd(this.dayKey)), 3600, ses);
        
        Calendar bkSt = TimeUtil.getDayBegin(this.dayKey);
        bkSt.add(Calendar.MINUTE, 90);
        Calendar bkEd = TimeUtil.getDayBegin(this.dayKey);
        bkEd.add(Calendar.HOUR, 2);
        TimePeriod tp = new TimePeriod(bkSt, bkEd);
        BookingCreation cre = this.engine.createBooking(us1, perm1, tp, ses);
        
        ses.beginTransaction();
        ses.delete(cre.getBooking());
        ses.delete(bk1);
        ses.delete(bk2);
        ses.delete(bk4);
        ses.delete(bk6);
        ses.delete(bk7);
        ses.delete(bk8);
        ses.delete(bk13);
        ses.delete(bk18);
        ses.delete(bk19);
        ses.delete(perm1);
        ses.delete(r1);
        ses.delete(r2);
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
        ses.delete(us1);
        ses.delete(uclass1);
        ses.getTransaction().commit();
        
        assertTrue(bk1.isActive());
        assertTrue(bk2.isActive());
        assertTrue(bk4.isActive());
        assertTrue(bk6.isActive());
        assertTrue(bk7.isActive());
        assertTrue(bk8.isActive());
        assertTrue(bk13.isActive());
        assertTrue(bk18.isActive());
        assertTrue(bk19.isActive());

        assertNotNull(cre);
        assertTrue(cre.wasCreated());
        assertNotNull(cre.getBooking());
        
        Bookings b = cre.getBooking();
        assertNotNull(b.getId());
        assertEquals(1800, b.getDuration());
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testCancelBooking() throws Exception
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
        RigType rigType1 = new RigType("booktestrigtype", 300, false);
        ses.save(rigType1);
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
        
        /* #### BOOKINGS FOR R1 ########################################### */
        Calendar r1tm = TimeUtil.getDayBegin(this.dayKey);
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

        /* #### BOOKINGS FOR R2 ########################################### */
        Calendar r2tm = TimeUtil.getDayBegin(this.dayKey);
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

        /* #### Type bookings for RigType1 ####################################*/
        Calendar rt1tm = TimeUtil.getDayBegin(this.dayKey);
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
        
        /* #### Bookings for Request Caps 1. #################################*/
        Calendar rcap1tm = TimeUtil.getDayBegin(this.dayKey);
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
        
        /* #### Bookings for Request Caps 2. #################################*/
        Calendar rcap2tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rcap3tm = TimeUtil.getDayBegin(this.dayKey);
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
        ses.refresh(rigType1);
        
        /* Force a load. */
        this.engine.getFreeTimes(rigType1, new TimePeriod(TimeUtil.getDayBegin(this.dayKey), 
                TimeUtil.getDayEnd(this.dayKey)), 3600, ses);
        
        boolean status = this.engine.cancelBooking(bk1, "Foo", ses);
        
        ses.beginTransaction();
        ses.delete(bk1);
        ses.delete(bk2);
        ses.delete(bk4);
        ses.delete(bk6);
        ses.delete(bk7);
        ses.delete(bk8);
        ses.delete(bk13);
        ses.delete(bk18);
        ses.delete(bk19);
        ses.delete(perm1);
        ses.delete(r1);
        ses.delete(r2);
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
        ses.delete(us1);
        ses.delete(uclass1);
        ses.getTransaction().commit();
        
        assertFalse(bk1.isActive());
        assertTrue(bk2.isActive());
        assertTrue(bk4.isActive());
        assertTrue(bk6.isActive());
        assertTrue(bk7.isActive());
        assertTrue(bk8.isActive());
        assertTrue(bk13.isActive());
        assertTrue(bk18.isActive());
        assertTrue(bk19.isActive());

        assertTrue(status);
        
        Field f = SlotBookingEngine.class.getDeclaredField("days");
        f.setAccessible(true);
        Map<String, DayBookings> days = (Map<String, DayBookings>) f.get(this.engine);
        assertNotNull(days);
        
        DayBookings day = days.get(this.dayKey);
        assertNotNull(day);
        f = DayBookings.class.getDeclaredField("rigBookings");
        f.setAccessible(true);
        
        Map<String, RigBookings> bklist = (Map<String, RigBookings>)f.get(day);
        assertNotNull(bklist);
        
        RigBookings r1b = bklist.get(r1.getName());
        assertNotNull(r1b);
        assertFalse(r1b.hasBooking(new MBooking(bk1, this.dayKey)));
        assertTrue(r1b.areSlotsFree(2, 5));
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testCancelBookingMultiDay() throws Exception
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
        RigType rigType1 = new RigType("booktestrigtype", 300, false);
        ses.save(rigType1);
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
        
        /* #### BOOKINGS FOR R1 ########################################### */
        Calendar r1tm = TimeUtil.getDayBegin(this.dayKey);
        Bookings bk1 = new Bookings();
        bk1.setActive(true);
        bk1.setDuration(3600);
        r1tm.add(Calendar.MINUTE, 30);
        bk1.setStartTime(r1tm.getTime());
        r1tm.add(Calendar.DAY_OF_MONTH, 5);
        r1tm.add(Calendar.HOUR, 1);
        bk1.setEndTime(r1tm.getTime());
        bk1.setResourcePermission(perm1);
        bk1.setResourceType("RIG");
        bk1.setRig(r1);
        bk1.setUser(us1);
        bk1.setUserName(us1.getName());
        bk1.setUserNamespace(us1.getNamespace());
        ses.save(bk1);

        /* #### BOOKINGS FOR R2 ########################################### */
        Calendar r2tm = TimeUtil.getDayBegin(this.dayKey);
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

        /* #### Type bookings for RigType1 ####################################*/
        Calendar rt1tm = TimeUtil.getDayBegin(this.dayKey);
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
        
        /* #### Bookings for Request Caps 1. #################################*/
        Calendar rcap1tm = TimeUtil.getDayBegin(this.dayKey);
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
        
        /* #### Bookings for Request Caps 3. #################################*/
        Calendar rcap3tm = TimeUtil.getDayBegin(this.dayKey);
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
        ses.refresh(rigType1);
        
        /* Force a load. */
        Calendar start = TimeUtil.getDayBegin(this.dayKey);
        Calendar end = TimeUtil.getDayBegin(this.dayKey);
        end.add(Calendar.DAY_OF_WEEK, 6);
        this.engine.getFreeTimes(rigType1, new TimePeriod(start, end), 3600, ses);
        
        boolean status = this.engine.cancelBooking(bk1, "Foo", ses);
        
        ses.beginTransaction();
        ses.delete(bk1);
        ses.delete(bk4);
        ses.delete(bk6);
        ses.delete(bk7);
        ses.delete(bk8);
        ses.delete(bk13);
        ses.delete(bk19);
        ses.delete(perm1);
        ses.delete(r1);
        ses.delete(r2);
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
        ses.delete(us1);
        ses.delete(uclass1);
        ses.getTransaction().commit();
        
        assertFalse(bk1.isActive());
        assertTrue(bk4.isActive());
        assertTrue(bk6.isActive());
        assertTrue(bk7.isActive());
        assertTrue(bk8.isActive());
        assertTrue(bk13.isActive());
        assertTrue(bk19.isActive());

        assertTrue(status);
        
        Field f = SlotBookingEngine.class.getDeclaredField("days");
        f.setAccessible(true);
        Map<String, DayBookings> days = (Map<String, DayBookings>) f.get(this.engine);
        assertNotNull(days);
        
        DayBookings day = days.get(this.dayKey);
        assertNotNull(day);
        f = DayBookings.class.getDeclaredField("rigBookings");
        f.setAccessible(true);
        
        Map<String, RigBookings> bklist = (Map<String, RigBookings>)f.get(day);
        assertNotNull(bklist);
        
        RigBookings r1b = bklist.get(r1.getName());
        assertNotNull(r1b);
        assertFalse(r1b.hasBooking(new MBooking(bk1, this.dayKey)));
        assertTrue(r1b.areSlotsFree(2, 5));
    }
    
    @Test
    public void testGetFreeMultiDays()
    {
        Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
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
        ses.getTransaction().commit();
        
        ses.refresh(r1);
        ses.refresh(rigType1);

        Calendar s = TimeUtil.getDayBegin(this.dayKey);
        Calendar e = TimeUtil.getDayEnd(this.dayKey);
        e.add(Calendar.DAY_OF_MONTH, 2);
        TimePeriod t = new TimePeriod(s, e);
        
        List<TimePeriod> times = this.engine.getFreeTimes(rigType1, t, 3600, ses);
        
        ses.beginTransaction();
        ses.delete(r1);
        ses.delete(caps1);
        ses.delete(rigType1);
        ses.getTransaction().commit();
        
        assertEquals(1, times.size());
        TimePeriod tf = times.get(0);
        
        Calendar ts = tf.getStartTime();
        Calendar te = tf.getEndTime();
        
        assertEquals(s.get(Calendar.DAY_OF_MONTH), ts.get(Calendar.DAY_OF_MONTH));
        assertEquals(s.get(Calendar.MONTH), ts.get(Calendar.MONTH));
        assertEquals(s.get(Calendar.YEAR), ts.get(Calendar.YEAR));
        assertEquals(0, ts.get(Calendar.HOUR));
        assertEquals(0, ts.get(Calendar.MINUTE));
        assertEquals(0, ts.get(Calendar.SECOND));
        
        e.add(Calendar.SECOND, 1);
        assertEquals(e.get(Calendar.DAY_OF_MONTH), te.get(Calendar.DAY_OF_MONTH));
        assertEquals(e.get(Calendar.MONTH), te.get(Calendar.MONTH));
        assertEquals(e.get(Calendar.YEAR), te.get(Calendar.YEAR));
        assertEquals(0, te.get(Calendar.HOUR));
        assertEquals(0, te.get(Calendar.MINUTE));
        assertEquals(0, te.get(Calendar.SECOND));
        
        assertTrue(times.get(0).getStartTime().compareTo(s) >= 0);
        assertTrue(times.get(times.size() - 1).getEndTime().compareTo(e) <= 0);
    }

    @Test
    public void testGetFreeTimesRig1()
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
        
        /* #### BOOKINGS FOR R1 ########################################### */
        Calendar r1tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar r2tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar r3tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rt1tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rt2tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rcap1tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rcap2tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rcap3tm = TimeUtil.getDayBegin(this.dayKey);
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
        
        Calendar start = TimeUtil.getDayBegin(this.dayKey);
        Calendar end = TimeUtil.getDayBegin(this.dayKey);
        end.add(Calendar.DAY_OF_MONTH, 1);
        
        TimePeriod tp = new TimePeriod(start, end);
        List<TimePeriod> range = this.engine.getFreeTimes(r1, tp, 600, ses);
        
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

        assertNotNull(range);
        assertEquals(3, range.size());
        
        TimePeriod t = range.get(0);
        Calendar s = t.getStartTime();
        Calendar e = t.getEndTime();
        assertEquals(1, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        assertEquals(2, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        t = range.get(1);
        s = t.getStartTime();
        e = t.getEndTime();
        assertEquals(2, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        assertEquals(8, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        t = range.get(2);
        s = t.getStartTime();
        e = t.getEndTime();
        assertEquals(11, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        
        assertEquals(0, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        
        Calendar cal = TimeUtil.getDayBegin(this.dayKey);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), e.get(Calendar.DAY_OF_MONTH));
        assertEquals(cal.get(Calendar.MONTH), e.get(Calendar.MONTH));
        assertEquals(cal.get(Calendar.YEAR), e.get(Calendar.YEAR));
        
        assertTrue(range.get(0).getStartTime().compareTo(start) >= 0);
        assertTrue(range.get(range.size() - 1).getEndTime().compareTo(end) <= 0);
    }
    
    @Test
    public void testGetFreeTimesRig3()
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
        
        /* #### BOOKINGS FOR R1 ########################################### */
        Calendar r1tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar r2tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar r3tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rt1tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rt2tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rcap1tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rcap2tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rcap3tm = TimeUtil.getDayBegin(this.dayKey);
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
        
        Calendar start = TimeUtil.getDayBegin(this.dayKey);
        Calendar end = TimeUtil.getDayBegin(this.dayKey);
        end.add(Calendar.DAY_OF_MONTH, 1);
        TimePeriod tp = new TimePeriod(start, end);
        List<TimePeriod> range = this.engine.getFreeTimes(r3, tp, 600, ses);
        
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

        assertNotNull(range);
        assertEquals(4, range.size());
        
        TimePeriod t = range.get(0);
        Calendar s = t.getStartTime();
        Calendar e = t.getEndTime();
        assertEquals(0, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        assertEquals(0, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        t = range.get(1);
        s = t.getStartTime();
        e = t.getEndTime();
        assertEquals(1, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        assertEquals(3, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        t = range.get(2);
        s = t.getStartTime();
        e = t.getEndTime();
        assertEquals(4, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        
        assertEquals(6, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        t = range.get(3);
        s = t.getStartTime();
        e = t.getEndTime();
        assertEquals(8, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(15, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        
        assertEquals(0, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        
        Calendar cal = TimeUtil.getDayBegin(this.dayKey);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), e.get(Calendar.DAY_OF_MONTH));
        assertEquals(cal.get(Calendar.MONTH), e.get(Calendar.MONTH));
        assertEquals(cal.get(Calendar.YEAR), e.get(Calendar.YEAR));
        
        assertTrue(range.get(0).getStartTime().compareTo(start) >= 0);
        assertTrue(range.get(range.size() -1 ).getEndTime().compareTo(end) <= 0);
    }
    
    @Test
    public void testGetFreeTimesRig2()
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
        
        /* #### BOOKINGS FOR R1 ########################################### */
        Calendar r1tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar r2tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar r3tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rt1tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rt2tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rcap1tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rcap2tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rcap3tm = TimeUtil.getDayBegin(this.dayKey);
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
        
        Calendar start = TimeUtil.getDayBegin(this.dayKey);
        Calendar end = TimeUtil.getDayBegin(this.dayKey);
        end.add(Calendar.DAY_OF_MONTH, 1);
        TimePeriod tp = new TimePeriod(start, end);
        List<TimePeriod> range = this.engine.getFreeTimes(r2, tp, 600, ses);
        
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

        assertNotNull(range);
        assertEquals(4, range.size());
        
        TimePeriod t = range.get(0);
        Calendar s = t.getStartTime();
        Calendar e = t.getEndTime();
        assertEquals(2, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        assertEquals(3, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        t = range.get(1);
        s = t.getStartTime();
        e = t.getEndTime();
        assertEquals(4, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        assertEquals(7, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        t = range.get(2);
        s = t.getStartTime();
        e = t.getEndTime();
        assertEquals(8, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(15, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        assertEquals(9, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        t = range.get(3);
        s = t.getStartTime();
        e = t.getEndTime();
        assertEquals(9, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        assertEquals(0, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        
        Calendar cal = TimeUtil.getDayBegin(this.dayKey);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), e.get(Calendar.DAY_OF_MONTH));
        assertEquals(cal.get(Calendar.MONTH), e.get(Calendar.MONTH));
        assertEquals(cal.get(Calendar.YEAR), e.get(Calendar.YEAR));
        
        assertTrue(range.get(0).getStartTime().compareTo(start) >= 0);
        assertTrue(range.get(range.size() - 1).getEndTime().compareTo(end) <= 0);
    }
    
    @Test
    public void testGetFreeTimesRigType1()
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
        
        /* #### BOOKINGS FOR R1 ########################################### */
        Calendar r1tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar r2tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar r3tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rt1tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rt2tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rcap1tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rcap2tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rcap3tm = TimeUtil.getDayBegin(this.dayKey);
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
        
        TimePeriod tp = new TimePeriod(TimeUtil.getDayBegin(this.dayKey), TimeUtil.getDayEnd(this.dayKey));
        List<TimePeriod> range = this.engine.getFreeTimes(rigType1, tp, 600, ses);
        
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

        assertNotNull(range);
        assertEquals(3, range.size());
        
        TimePeriod t = range.get(0);
        Calendar s = t.getStartTime();
        Calendar e = t.getEndTime();
        assertEquals(1, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        assertEquals(8, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));

        t = range.get(1);
        s = t.getStartTime();
        e = t.getEndTime();
        assertEquals(8, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(15, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        assertEquals(9, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        t = range.get(2);
        s = t.getStartTime();
        e = t.getEndTime();
        assertEquals(9, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        assertEquals(23, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(59, e.get(Calendar.MINUTE));
        assertEquals(59, e.get(Calendar.SECOND));
    }
    
    @Test
    public void testGetFreeTimesRigType2()
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
        
        /* #### BOOKINGS FOR R1 ########################################### */
        Calendar r1tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar r2tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar r3tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rt1tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rt2tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rcap1tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rcap2tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rcap3tm = TimeUtil.getDayBegin(this.dayKey);
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
        
        Calendar start = TimeUtil.getDayBegin(this.dayKey);
        start.add(Calendar.MINUTE, 10);
        Calendar end = TimeUtil.getDayBegin(this.dayKey);
        end.add(Calendar.DAY_OF_MONTH, 1);
        
        TimePeriod tp = new TimePeriod(start, end);
        List<TimePeriod> range = this.engine.getFreeTimes(rigType2, tp, 600, ses);
        
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

        assertNotNull(range);
        assertEquals(5, range.size());
        
        TimePeriod t = range.get(0);
        
        Calendar s = t.getStartTime();
        Calendar e = t.getEndTime();
        assertEquals(0, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(10, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        assertEquals(0, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        t = range.get(1);
        s = t.getStartTime();
        e = t.getEndTime();
        assertEquals(1, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        assertEquals(3, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        t = range.get(2);
        s = t.getStartTime();
        e = t.getEndTime();
        assertEquals(4, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        assertEquals(6, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        t = range.get(3);
        s = t.getStartTime();
        e = t.getEndTime();
        assertEquals(8, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(15, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        assertEquals(14, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        /* DODGY: Balance problem. */
        t = range.get(4);
        s = t.getStartTime();
        e = t.getEndTime();
        assertEquals(15, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        assertEquals(0, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        Calendar cal = TimeUtil.getDayBegin(this.dayKey);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), e.get(Calendar.DAY_OF_MONTH));
        assertEquals(cal.get(Calendar.MONTH), e.get(Calendar.MONTH));
        assertEquals(cal.get(Calendar.YEAR), e.get(Calendar.YEAR));
        
        assertTrue(range.get(0).getStartTime().compareTo(start) >= 0);
        assertTrue(range.get(range.size() - 1).getEndTime().compareTo(end) <= 0);
    }
    
    @Test
    public void testGetFreeTimesReqCaps1()
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
        
        /* #### BOOKINGS FOR R1 ########################################### */
        Calendar r1tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar r2tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar r3tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rt1tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rt2tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rcap1tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rcap2tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rcap3tm = TimeUtil.getDayBegin(this.dayKey);
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
        
        Calendar start = TimeUtil.getDayBegin(this.dayKey);
        start.add(Calendar.MINUTE, 16);
        Calendar end = TimeUtil.getDayBegin(this.dayKey);
        end.add(Calendar.DAY_OF_MONTH, 1);
        TimePeriod tp = new TimePeriod(start, end);
        List<TimePeriod> range = this.engine.getFreeTimes(rcaps1, tp, 600, ses);
        
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

        assertNotNull(range);
        assertEquals(3, range.size());
        
        TimePeriod t = range.get(0);
        Calendar s = t.getStartTime();
        Calendar e = t.getEndTime();
        assertEquals(0, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(16, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        assertEquals(0, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        t = range.get(1);
        s = t.getStartTime();
        e = t.getEndTime();
        assertEquals(1, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        assertEquals(8, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        t = range.get(2);
        s = t.getStartTime();
        e = t.getEndTime();
        assertEquals(8, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(15, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        assertEquals(0, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        Calendar cal = TimeUtil.getDayBegin(this.dayKey);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), e.get(Calendar.DAY_OF_MONTH));
        assertEquals(cal.get(Calendar.MONTH), e.get(Calendar.MONTH));
        assertEquals(cal.get(Calendar.YEAR), e.get(Calendar.YEAR));
        
        assertTrue(range.get(0).getStartTime().compareTo(start) >= 0);
        assertTrue(range.get(range.size() - 1).getEndTime().compareTo(end) <= 0);
    }
    
    @Test
    public void testGetFreeTimesReqCaps2()
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
        
        /* #### BOOKINGS FOR R1 ########################################### */
        Calendar r1tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar r2tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar r3tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rt1tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rt2tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rcap1tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rcap2tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rcap3tm = TimeUtil.getDayBegin(this.dayKey);
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
        
        Calendar start = TimeUtil.getDayBegin(this.dayKey);
        Calendar end = TimeUtil.getDayBegin(this.dayKey);
        end.add(Calendar.DAY_OF_MONTH, 1);
        TimePeriod tp = new TimePeriod(start, end);
        List<TimePeriod> range = this.engine.getFreeTimes(rcaps2, tp, 600, ses);
        
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

        assertNotNull(range);
        assertEquals(3, range.size());
        
        TimePeriod t = range.get(0);
        Calendar s = t.getStartTime();
        Calendar e = t.getEndTime();
        assertEquals(0, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        assertEquals(0, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        t = range.get(1);
        s = t.getStartTime();
        e = t.getEndTime();
        assertEquals(1, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        assertEquals(8, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        t = range.get(2);
        s = t.getStartTime();
        e = t.getEndTime();
        assertEquals(8, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(15, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        assertEquals(0, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        Calendar cal = TimeUtil.getDayBegin(this.dayKey);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), e.get(Calendar.DAY_OF_MONTH));
        assertEquals(cal.get(Calendar.MONTH), e.get(Calendar.MONTH));
        assertEquals(cal.get(Calendar.YEAR), e.get(Calendar.YEAR));
        
        assertTrue(range.get(0).getStartTime().compareTo(start) >= 0);
        assertTrue(range.get(range.size() - 1).getEndTime().compareTo(end) <= 0);
                start.add(Calendar.MINUTE, 16);
    }
    
    @Test
    public void testGetFreeTimesReqCaps3()
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
        
        /* #### BOOKINGS FOR R1 ########################################### */
        Calendar r1tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar r2tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar r3tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rt1tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rt2tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rcap1tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rcap2tm = TimeUtil.getDayBegin(this.dayKey);
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
        Calendar rcap3tm = TimeUtil.getDayBegin(this.dayKey);
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
        
        Calendar start = TimeUtil.getDayBegin(this.dayKey);
        start.add(Calendar.MINUTE, 3);
        Calendar end = TimeUtil.getDayBegin(this.dayKey);
        end.add(Calendar.DAY_OF_MONTH, 1);
        TimePeriod tp = new TimePeriod(start, end);
        List<TimePeriod> range = this.engine.getFreeTimes(rcaps3, tp, 600, ses);
        
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

        assertNotNull(range);
        assertEquals(5, range.size());
        
        TimePeriod t = range.get(0);
        Calendar s = t.getStartTime();
        Calendar e = t.getEndTime();
        assertEquals(2, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        assertEquals(3, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        t = range.get(1);
        s = t.getStartTime();
        e = t.getEndTime();
        assertEquals(4, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        assertEquals(5, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        t = range.get(2);
        s = t.getStartTime();
        e = t.getEndTime();
        assertEquals(6, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        assertEquals(7, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        t = range.get(3);
        s = t.getStartTime();
        e = t.getEndTime();
        assertEquals(8, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(15, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        assertEquals(9, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        t = range.get(4);
        s = t.getStartTime();
        e = t.getEndTime();
        assertEquals(9, s.get(Calendar.HOUR_OF_DAY));
        assertEquals(30, s.get(Calendar.MINUTE));
        assertEquals(0, s.get(Calendar.SECOND));
        assertEquals(0, e.get(Calendar.HOUR_OF_DAY));
        assertEquals(0, e.get(Calendar.MINUTE));
        assertEquals(0, e.get(Calendar.SECOND));
        
        Calendar cal = TimeUtil.getDayBegin(this.dayKey);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        assertEquals(cal.get(Calendar.DAY_OF_MONTH), e.get(Calendar.DAY_OF_MONTH));
        assertEquals(cal.get(Calendar.MONTH), e.get(Calendar.MONTH));
        assertEquals(cal.get(Calendar.YEAR), e.get(Calendar.YEAR));
        
        assertTrue(range.get(0).getStartTime().compareTo(start) >= 0);
        assertTrue(range.get(range.size() - 1).getEndTime().compareTo(end) <= 0);
    }
}
