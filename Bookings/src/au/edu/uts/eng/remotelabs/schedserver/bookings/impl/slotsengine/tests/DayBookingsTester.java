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
 * @date 20th November 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine.tests;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.hibernate.Session;

import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine.DayBookings;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine.MBooking;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine.MRange;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine.RigBookings;
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
 * Tests the {@link DayBookings} class.
 */
public class DayBookingsTester extends TestCase
{
    /** Object of class under test. */
    private DayBookings day;
    
    /** The day string for today. */
    private String dayStr;

    @Override
    public void setUp() throws Exception
    {
        DataAccessTestSetup.setup();
        
        Field f = LoggerActivator.class.getDeclaredField("logger");
        f.setAccessible(true);
        f.set(null, new SystemErrLogger());
        
        this.dayStr = TimeUtil.getDateStr(Calendar.getInstance());
        this.day = new DayBookings(this.dayStr);
    }
    
    public void testGetFreeSlotsRigLoadBalance() throws Exception
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
        RigCapabilities caps = new RigCapabilities("book,test,tar,caps");
        ses.save(caps);
        RequestCapabilities rcaps = new RequestCapabilities("book,test");
        ses.save(rcaps);
        Rig r1 = new Rig();
        r1.setName("bkrig1");
        r1.setRigType(rigType1);
        r1.setLastUpdateTimestamp(new Date());
        r1.setRigCapabilities(caps);
        ses.save(r1);
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
        
        /* #### RIG BOOKINGS FOR R1 ########################################### */
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
        Bookings bk5 = new Bookings();
        bk5.setActive(true);
        bk5.setDuration(3600);
        /* Slots 20 - 24. */
        rt1tm.add(Calendar.MINUTE, 30);
        rt1tm.add(Calendar.HOUR, 4);
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
        
        /* #### Bookings for Request Caps 2. #################################*/
        Calendar rcap1tm = TimeUtil.getDayBegin(this.dayStr);
        Bookings bk6 = new Bookings();
        bk6.setActive(true);
        bk6.setDuration(3600);
        /* Slots 20 - 24. */
        rcap1tm.add(Calendar.HOUR, 8);
        bk6.setStartTime(rcap1tm.getTime());
        rcap1tm.add(Calendar.HOUR, 1);
        bk6.setEndTime(rcap1tm.getTime());
        bk6.setResourcePermission(perm1);
        bk6.setResourceType("CAPABILITY");
        bk6.setRequestCapabilities(rcaps);
        bk6.setUser(us1);
        bk6.setUserName(us1.getName());
        bk6.setUserNamespace(us1.getNamespace());
        ses.save(bk6);
        Bookings bk7 = new Bookings();
        bk7.setActive(true);
        bk7.setDuration(3600);
        /* Slots 56 - 59. */
        rcap1tm.add(Calendar.HOUR, 5);
        bk7.setStartTime(rcap1tm.getTime());
        rcap1tm.add(Calendar.HOUR, 1);
        bk7.setEndTime(rcap1tm.getTime());
        bk7.setResourcePermission(perm1);
        bk7.setResourceType("CAPABILITY");
        bk7.setRequestCapabilities(rcaps);
        bk7.setUser(us1);
        bk7.setUserName(us1.getName());
        bk7.setUserNamespace(us1.getNamespace());
        ses.save(bk7);
        
        ses.getTransaction().commit();
        
        ses.beginTransaction();
        MatchingCapabilities mat1 = new MatchingCapabilities(rcaps, caps);
        ses.save(mat1);
        ses.getTransaction().commit();
        
        ses.refresh(rcaps);
        ses.refresh(caps);
        ses.refresh(r1);
        ses.refresh(rigType1);
        
        List<MRange> range = this.day.getFreeSlots(r1, 1, ses);
        
        ses.beginTransaction();
        ses.delete(bk1);
        ses.delete(bk2);
        ses.delete(bk3);
        ses.delete(bk4);
        ses.delete(bk5);
        ses.delete(bk6);
        ses.delete(bk7);
        ses.delete(perm1);
        ses.delete(r1);
        ses.delete(mat1);
        ses.delete(caps);
        ses.delete(rcaps);
        ses.delete(rigType1);
        ses.delete(us1);
        ses.delete(uclass1);
        ses.getTransaction().commit();
        
        assertNotNull(range);
        assertEquals(5, range.size());
        
        MRange mr = range.get(0);
        assertEquals(6, mr.getStartSlot());
        assertEquals(7, mr.getEndSlot());
        assertEquals(2, mr.getNumSlots());
        assertEquals(this.day.getDay(), mr.getDayKey());
        
        mr = range.get(1);
        assertEquals(10, mr.getStartSlot());
        assertEquals(19, mr.getEndSlot());
        assertEquals(10, mr.getNumSlots());
        
        mr = range.get(2);
        assertEquals(24, mr.getStartSlot());
        assertEquals(31, mr.getEndSlot());
        assertEquals(8, mr.getNumSlots());
        
        mr = range.get(3);
        assertEquals(44, mr.getStartSlot());
        assertEquals(55, mr.getEndSlot());
        assertEquals(12, mr.getNumSlots());
        
        mr = range.get(4);
        assertEquals(60, mr.getStartSlot());
        assertEquals(95, mr.getEndSlot());
        assertEquals(36, mr.getNumSlots());
    }

    public void testGetFreeSlotsRig() throws Exception
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
        RigCapabilities caps = new RigCapabilities("book,test,tar,foo");
        ses.save(caps);
        Rig r1 = new Rig();
        r1.setName("bkrig1");
        r1.setRigType(rigType1);
        r1.setLastUpdateTimestamp(new Date());
        r1.setRigCapabilities(caps);
        ses.save(r1);
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
        ses.getTransaction().commit();
        
        ses.refresh(rigType1);
        
        List<MRange> range = this.day.getFreeSlots(r1, 0, 95, 1, ses);
        
        ses.beginTransaction();
        ses.delete(perm1);
        ses.delete(r1);
        ses.delete(caps);
        ses.delete(rigType1);
        ses.delete(us1);
        ses.delete(uclass1);
        ses.getTransaction().commit();
        
        assertNotNull(range);
        assertEquals(1, range.size());
        
        MRange mr = range.get(0);
        assertEquals(0, mr.getStartSlot());
        assertEquals(95, mr.getEndSlot());
        assertEquals(96, mr.getNumSlots());
        assertEquals(this.day.getDay(), mr.getDayKey());
    }
    
    public void testGetFreeSlotsRigWithRigRB() throws Exception
    {
        Calendar tm = TimeUtil.getDayBegin(this.dayStr);
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
        RigCapabilities caps = new RigCapabilities("book,test,tar,foo");
        ses.save(caps);
        Rig r1 = new Rig();
        r1.setName("bkrig1");
        r1.setRigType(rigType1);
        r1.setLastUpdateTimestamp(new Date());
        r1.setRigCapabilities(caps);
        ses.save(r1);
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
        
        /* #### RIG BOOKINGS FOR R1 ########################################### */
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
        Bookings bk2 = new Bookings();
        bk2.setActive(true);
        bk2.setCancelReason("Test cancel.");
        bk2.setDuration(1800);
        /* Slots 8 - 9. */
        tm.add(Calendar.MINUTE, 30);
        bk2.setStartTime(tm.getTime());
        tm.add(Calendar.MINUTE, 30);
        bk2.setEndTime(tm.getTime());
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
        tm.add(Calendar.MINUTE, 30);
        tm.add(Calendar.HOUR, 6);
        bk3.setStartTime(tm.getTime());
        tm.add(Calendar.HOUR, 2);
        bk3.setEndTime(tm.getTime());
        bk3.setResourcePermission(perm1);
        bk3.setResourceType("RIG");
        bk3.setRig(r1);
        bk3.setUser(us1);
        bk3.setUserName(us1.getName());
        bk3.setUserNamespace(us1.getNamespace());
        ses.save(bk3);
        ses.getTransaction().commit();
        
        ses.refresh(rigType1);
        
        List<MRange> range = this.day.getFreeSlots(r1, 1, ses);
        
        ses.beginTransaction();
        ses.delete(bk1);
        ses.delete(bk2);
        ses.delete(bk3);
        ses.delete(perm1);
        ses.delete(r1);
        ses.delete(caps);
        ses.delete(rigType1);
        ses.delete(us1);
        ses.delete(uclass1);
        ses.getTransaction().commit();
        
        assertNotNull(range);
        assertEquals(4, range.size());
        
        MRange mr = range.get(0);
        assertEquals(0, mr.getStartSlot());
        assertEquals(1, mr.getEndSlot());
        assertEquals(2, mr.getNumSlots());
        assertEquals(this.day.getDay(), mr.getDayKey());
        
        mr = range.get(1);
        assertEquals(6, mr.getStartSlot());
        assertEquals(7, mr.getEndSlot());
        assertEquals(2, mr.getNumSlots());
        
        mr = range.get(2);
        assertEquals(10, mr.getStartSlot());
        assertEquals(35, mr.getEndSlot());
        assertEquals(26, mr.getNumSlots());
        
        mr = range.get(3);
        assertEquals(44, mr.getStartSlot());
        assertEquals(95, mr.getEndSlot());
        assertEquals(52, mr.getNumSlots());
    }
    
    public void testGetFreeSlotsRigWithRigRBWithThres() throws Exception
    {
        Calendar tm = TimeUtil.getDayBegin(this.dayStr);
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
        RigCapabilities caps = new RigCapabilities("book,test,tar,foo");
        ses.save(caps);
        Rig r1 = new Rig();
        r1.setName("bkrig1");
        r1.setRigType(rigType1);
        r1.setLastUpdateTimestamp(new Date());
        r1.setRigCapabilities(caps);
        ses.save(r1);
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
        
        /* #### RIG BOOKINGS FOR R1 ########################################### */
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
        Bookings bk2 = new Bookings();
        bk2.setActive(true);
        bk2.setCancelReason("Test cancel.");
        bk2.setDuration(1800);
        /* Slots 8 - 9. */
        tm.add(Calendar.MINUTE, 30);
        bk2.setStartTime(tm.getTime());
        tm.add(Calendar.MINUTE, 30);
        bk2.setEndTime(tm.getTime());
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
        tm.add(Calendar.MINUTE, 30);
        tm.add(Calendar.HOUR, 6);
        bk3.setStartTime(tm.getTime());
        tm.add(Calendar.HOUR, 2);
        bk3.setEndTime(tm.getTime());
        bk3.setResourcePermission(perm1);
        bk3.setResourceType("RIG");
        bk3.setRig(r1);
        bk3.setUser(us1);
        bk3.setUserName(us1.getName());
        bk3.setUserNamespace(us1.getNamespace());
        ses.save(bk3);
        ses.getTransaction().commit();
        
        ses.refresh(rigType1);
        
        List<MRange> range = this.day.getFreeSlots(r1, 2, ses);
        
        ses.beginTransaction();
        ses.delete(bk1);
        ses.delete(bk2);
        ses.delete(bk3);
        ses.delete(perm1);
        ses.delete(r1);
        ses.delete(caps);
        ses.delete(rigType1);
        ses.delete(us1);
        ses.delete(uclass1);
        ses.getTransaction().commit();
        
        assertNotNull(range);
        assertEquals(4, range.size());
        
        MRange mr = range.get(0);
        assertEquals(0, mr.getStartSlot());
        assertEquals(1, mr.getEndSlot());
        assertEquals(2, mr.getNumSlots());
        assertEquals(this.day.getDay(), mr.getDayKey());
        
        mr = range.get(1);
        assertEquals(6, mr.getStartSlot());
        assertEquals(7, mr.getEndSlot());
        assertEquals(2, mr.getNumSlots());
        
        mr = range.get(2);
        assertEquals(10, mr.getStartSlot());
        assertEquals(35, mr.getEndSlot());
        assertEquals(26, mr.getNumSlots());
        
        mr = range.get(3);
        assertEquals(44, mr.getStartSlot());
        assertEquals(95, mr.getEndSlot());
        assertEquals(52, mr.getNumSlots());
    }
    
    public void testGetFreeSlotsRigWithRigRBWithThres4() throws Exception
    {
        Calendar tm = TimeUtil.getDayBegin(this.dayStr);
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
        RigCapabilities caps = new RigCapabilities("book,test,tar,foo");
        ses.save(caps);
        Rig r1 = new Rig();
        r1.setName("bkrig1");
        r1.setRigType(rigType1);
        r1.setLastUpdateTimestamp(new Date());
        r1.setRigCapabilities(caps);
        ses.save(r1);
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
        
        /* #### RIG BOOKINGS FOR R1 ########################################### */
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
        Bookings bk2 = new Bookings();
        bk2.setActive(true);
        bk2.setCancelReason("Test cancel.");
        bk2.setDuration(1800);
        /* Slots 8 - 9. */
        tm.add(Calendar.MINUTE, 30);
        bk2.setStartTime(tm.getTime());
        tm.add(Calendar.MINUTE, 30);
        bk2.setEndTime(tm.getTime());
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
        tm.add(Calendar.MINUTE, 30);
        tm.add(Calendar.HOUR, 6);
        bk3.setStartTime(tm.getTime());
        tm.add(Calendar.HOUR, 2);
        bk3.setEndTime(tm.getTime());
        bk3.setResourcePermission(perm1);
        bk3.setResourceType("RIG");
        bk3.setRig(r1);
        bk3.setUser(us1);
        bk3.setUserName(us1.getName());
        bk3.setUserNamespace(us1.getNamespace());
        ses.save(bk3);
        ses.getTransaction().commit();
        
        ses.refresh(rigType1);
        
        List<MRange> range = this.day.getFreeSlots(r1, 4, ses);
        
        ses.beginTransaction();
        ses.delete(bk1);
        ses.delete(bk2);
        ses.delete(bk3);
        ses.delete(perm1);
        ses.delete(r1);
        ses.delete(caps);
        ses.delete(rigType1);
        ses.delete(us1);
        ses.delete(uclass1);
        ses.getTransaction().commit();
        
        assertNotNull(range);
        assertEquals(2, range.size());
        
        MRange mr = range.get(0);
        assertEquals(10, mr.getStartSlot());
        assertEquals(35, mr.getEndSlot());
        assertEquals(26, mr.getNumSlots());
        
        mr = range.get(1);
        assertEquals(44, mr.getStartSlot());
        assertEquals(95, mr.getEndSlot());
        assertEquals(52, mr.getNumSlots());
    }
    
    public void testGetRigBookingsLoadCaps() throws Exception
    {
        Calendar begin = TimeUtil.getDayBegin(this.day.getDay());
        Calendar end = TimeUtil.getDayEnd(this.day.getDay());
        end.add(Calendar.SECOND, 1);
        
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
        ses.save(r2);Rig r3 = new Rig();
        r3.setName("bkrig3");
        r3.setRigType(rigType2);
        r3.setLastUpdateTimestamp(new Date());
        r3.setRigCapabilities(caps3);
        ses.save(r3);
       
        ResourcePermission perm1 = new ResourcePermission();
        perm1.setUserClass(uclass1);
        perm1.setType("CAPABIlITY");
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
        Bookings bk1 = new Bookings();
        bk1.setActive(true);
        bk1.setDuration(3600);
        bk1.setStartTime(begin.getTime());
        bk1.setEndTime(end.getTime());
        bk1.setResourcePermission(perm1);
        bk1.setResourceType("CAPABILITY");
        bk1.setRequestCapabilities(rcaps1);
        bk1.setUser(us1);
        bk1.setUserName(us1.getName());
        bk1.setUserNamespace(us1.getNamespace());
        ses.save(bk1);
        Bookings bk2 = new Bookings();
        bk2.setActive(true);
        bk2.setCancelReason("Test cancel.");
        bk2.setDuration(3600);
        bk2.setStartTime(begin.getTime());
        bk2.setEndTime(end.getTime());
        bk2.setResourcePermission(perm1);
        bk2.setResourceType("CAPABILITY");
        bk2.setRequestCapabilities(rcaps2);
        bk2.setUser(us1);
        bk2.setUserName(us1.getName());
        bk2.setUserNamespace(us1.getNamespace());
        ses.save(bk2);
        Bookings bk3 = new Bookings();
        bk3.setActive(true);
        bk3.setDuration(7200);
        bk3.setStartTime(begin.getTime());
        bk3.setEndTime(end.getTime());
        bk3.setResourcePermission(perm1);
        bk3.setResourceType("CAPABILITY");
        bk3.setRequestCapabilities(rcaps3);
        bk3.setUser(us1);
        bk3.setUserName(us1.getName());
        bk3.setUserNamespace(us1.getNamespace());
        ses.save(bk3);
        
        ses.getTransaction().commit();
        
        ses.beginTransaction();
        MatchingCapabilities mc1 = new MatchingCapabilities(rcaps1, caps1);
        ses.save(mc1);
        MatchingCapabilities mc2 = new MatchingCapabilities(rcaps1, caps2);
        ses.save(mc2);
        MatchingCapabilities mc3 = new MatchingCapabilities(rcaps1, caps3);
        ses.save(mc3);
        MatchingCapabilities mc4 = new MatchingCapabilities(rcaps2, caps1);
        ses.save(mc4);
        MatchingCapabilities mc5 = new MatchingCapabilities(rcaps2, caps3);
        ses.save(mc5);
        MatchingCapabilities mc6 = new MatchingCapabilities(rcaps3, caps2);
        ses.save(mc6);
        ses.getTransaction().commit();
        
        ses.refresh(rigType1);
        ses.refresh(rigType2);
        ses.refresh(caps1);
        ses.refresh(caps2);
        ses.refresh(caps3);
        ses.refresh(rcaps1);
        ses.refresh(rcaps2);
        ses.refresh(rcaps3);
        ses.refresh(r1);
        ses.refresh(r2);
        ses.refresh(r3);
        
        Method m = DayBookings.class.getDeclaredMethod("getRigBookings", Rig.class, Session.class);
        m.setAccessible(true);
        RigBookings rb = (RigBookings)m.invoke(this.day, r1, ses);
        
        ses.beginTransaction();
        ses.delete(bk3);
        ses.delete(bk2);
        ses.delete(bk1);
        ses.delete(perm1);
        ses.delete(r1);
        ses.delete(r2);
        ses.delete(r3);
        ses.delete(mc1);
        ses.delete(mc2);
        ses.delete(mc3);
        ses.delete(mc4);
        ses.delete(mc5);
        ses.delete(mc6);
        ses.delete(rcaps1);
        ses.delete(rcaps2);
        ses.delete(rcaps3);
        ses.delete(caps1);
        ses.delete(caps2);
        ses.delete(caps3);
        ses.delete(rigType1);
        ses.delete(rigType2);
        ses.delete(us1);
        ses.delete(uclass1);
        ses.getTransaction().commit();
        
        assertNotNull(rb);
        
        RigBookings rb1 = (RigBookings)m.invoke(this.day, r1, ses);
        assertNotNull(rb1);
        RigBookings rb2 = (RigBookings)m.invoke(this.day, r2, ses);
        assertNotNull(rb2);
        RigBookings rb3 = (RigBookings)m.invoke(this.day, r3, ses);
        assertNotNull(rb3);
        
        /* Check type loops. */
        assertTrue(rb1.getTypeLoopNext() == rb2);
        assertTrue(rb2.getTypeLoopNext() == rb1);
        assertTrue(rb3.getTypeLoopNext() == rb3);
        
        /* Check caps loaded. */
        Field f = DayBookings.class.getDeclaredField("loadedCapabilites");
        f.setAccessible(true);
        @SuppressWarnings("unchecked")
        Set<RequestCapabilities> reqList = (Set<RequestCapabilities>)f.get(this.day);
        assertNotNull(reqList);
        assertEquals(3, reqList.size());
        
        /* Check caps loops. */                
        assertTrue(rb2.getCapsLoopNext(rcaps3) == rb2);
        assertTrue(rb1.getCapsLoopNext(rcaps2) == rb3);
        assertTrue(rb3.getCapsLoopNext(rcaps2) == rb1);
        
        int i = 0;
        RigBookings next = rb;
        List<Rig> rigs = new ArrayList<Rig>(3);
        do
        {
            i++;
            rigs.add(next.getRig());
            next = next.getCapsLoopNext(rcaps1);
        }
        while (next != rb);
        
        assertEquals(3, i);
        assertTrue(rigs.contains(r1));
        assertTrue(rigs.contains(r2));
        assertTrue(rigs.contains(r3));
        
        /* Check the bookings are assigned to a rig. */
        assertTrue(bk1.isActive());
        assertTrue(bk2.isActive());
        assertTrue(bk3.isActive());
        
        assertTrue(rb2.hasBooking(new MBooking(bk3, this.dayStr)));
        assertTrue(rb1.hasBooking(new MBooking(bk2, this.dayStr)) || 
                   rb3.hasBooking(new MBooking(bk2, this.dayStr)));
        assertTrue(rb1.hasBooking(new MBooking(bk1, this.dayStr)) || 
                   rb2.hasBooking(new MBooking(bk1, this.dayStr)) || 
                   rb3.hasBooking(new MBooking(bk1, this.dayStr)));
    }
    
    public void testGetRigBookingsTestTypeLoopOne() throws Exception
    {
        Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        RigType rigType1 = new RigType("booktestrigtype", 300, false);
        ses.save(rigType1);
        RigCapabilities caps = new RigCapabilities("book,test,tar,foo");
        ses.save(caps);
        Rig r1 = new Rig();
        r1.setName("bkrig1");
        r1.setRigType(rigType1);
        r1.setLastUpdateTimestamp(new Date());
        r1.setRigCapabilities(caps);
        ses.save(r1);
        ses.getTransaction().commit();
        
        ses.refresh(rigType1);
        
        Method m = DayBookings.class.getDeclaredMethod("getRigBookings", Rig.class, Session.class);
        m.setAccessible(true);
        RigBookings rb = (RigBookings)m.invoke(this.day, r1, ses);
        
        ses.beginTransaction();
        ses.delete(r1);
        ses.delete(caps);
        ses.delete(rigType1);
        ses.getTransaction().commit();
        
        assertNotNull(rb);
        assertTrue(rb.getTypeLoopNext() == rb);
    }

    public void testGetRigBookingsTestTypeLoop() throws Exception
    {
        Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        RigType rigType1 = new RigType("booktestrigtype", 300, false);
        ses.save(rigType1);
        RigType rigType2 = new RigType("booktestrigtype2", 300, false);
        ses.save(rigType2);
        
        RigCapabilities caps = new RigCapabilities("book,test,tar,foo");
        ses.save(caps);
        Rig r1 = new Rig();
        r1.setName("bkrig1");
        r1.setRigType(rigType1);
        r1.setLastUpdateTimestamp(new Date());
        r1.setRigCapabilities(caps);
        ses.save(r1);
        Rig r2 = new Rig();
        r2.setName("bkrig2");
        r2.setRigType(rigType1);
        r2.setLastUpdateTimestamp(new Date());
        r2.setRigCapabilities(caps);
        ses.save(r2);
        Rig r3 = new Rig();
        r3.setName("bkrig3");
        r3.setRigType(rigType1);
        r3.setLastUpdateTimestamp(new Date());
        r3.setRigCapabilities(caps);
        ses.save(r3);
        Rig r4 = new Rig();
        r4.setName("bkrig4");
        r4.setRigType(rigType1);
        r4.setLastUpdateTimestamp(new Date());
        r4.setRigCapabilities(caps);
        ses.save(r4);
        Rig r5 = new Rig();
        r5.setName("bkrig5");
        r5.setRigType(rigType1);
        r5.setLastUpdateTimestamp(new Date());
        r5.setRigCapabilities(caps);
        ses.save(r5);
        Rig r6 = new Rig();
        r6.setName("bkrig6");
        r6.setRigType(rigType2);
        r6.setLastUpdateTimestamp(new Date());
        r6.setRigCapabilities(caps);
        ses.save(r6);
        ses.getTransaction().commit();
        
        ses.refresh(rigType1);
        ses.refresh(rigType2);
        
        Method m = DayBookings.class.getDeclaredMethod("getRigBookings", Rig.class, Session.class);
        m.setAccessible(true);
        RigBookings rb = (RigBookings) m.invoke(this.day, r1, ses);
        
        ses.beginTransaction();
        ses.delete(r1);
        ses.delete(r2);
        ses.delete(r3);
        ses.delete(r4);
        ses.delete(r5);
        ses.delete(r6);
        ses.delete(caps);
        ses.delete(rigType1);
        ses.delete(rigType2);
        ses.getTransaction().commit();
        
        assertNotNull(rb);
        
        int i = 0;
        List<Rig> rigs = new ArrayList<Rig>(5);
        rigs.add(r1);
        rigs.add(r2);
        rigs.add(r3);
        rigs.add(r4);
        rigs.add(r5);
        
        RigBookings next = rb;
        do
        {
            i++;
            assertTrue(rigs.remove(next.getRig()));
            next = next.getTypeLoopNext();
        }
        while (next != rb);
        assertEquals(5, i);
        assertEquals(0, rigs.size());
    }
    
    public void testGetRigBookingsLoadType() throws Exception
    {
        Calendar begin = TimeUtil.getDayBegin(this.day.getDay());
        Calendar end = TimeUtil.getDayEnd(this.day.getDay());
        end.add(Calendar.SECOND, 1);
        
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
        RigCapabilities caps = new RigCapabilities("book,test,tar,foo");
        ses.save(caps);
        Rig r1 = new Rig();
        r1.setName("bkrig1");
        r1.setRigType(rigType1);
        r1.setLastUpdateTimestamp(new Date());
        r1.setRigCapabilities(caps);
        ses.save(r1);
        Rig r2 = new Rig();
        r2.setName("bkrig2");
        r2.setRigType(rigType1);
        r2.setLastUpdateTimestamp(new Date());
        r2.setRigCapabilities(caps);
        ses.save(r2);Rig r3 = new Rig();
        r3.setName("bkrig3");
        r3.setRigType(rigType1);
        r3.setLastUpdateTimestamp(new Date());
        r3.setRigCapabilities(caps);
        ses.save(r3);
        Rig r4 = new Rig();
        r4.setName("bkrig4");
        r4.setRigType(rigType1);
        r4.setLastUpdateTimestamp(new Date());
        r4.setRigCapabilities(caps);
        ses.save(r4);
        Rig r5 = new Rig();
        r5.setName("bkrig5");
        r5.setRigType(rigType1);
        r5.setLastUpdateTimestamp(new Date());
        r5.setRigCapabilities(caps);
        ses.save(r5);
        Rig r6 = new Rig();
        r6.setName("bkrig6");
        r6.setRigType(rigType2);
        r6.setLastUpdateTimestamp(new Date());
        r6.setRigCapabilities(caps);
        ses.save(r6);
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
        bk1.setStartTime(begin.getTime());
        bk1.setEndTime(end.getTime());
        bk1.setResourcePermission(perm1);
        bk1.setResourceType("TYPE");
        bk1.setRigType(rigType1);
        bk1.setUser(us1);
        bk1.setUserName(us1.getName());
        bk1.setUserNamespace(us1.getNamespace());
        ses.save(bk1);
        Bookings bk2 = new Bookings();
        bk2.setActive(true);
        bk2.setCancelReason("Test cancel.");
        bk2.setDuration(3600);
        bk2.setStartTime(begin.getTime());
        bk2.setEndTime(end.getTime());
        bk2.setResourcePermission(perm1);
        bk2.setResourceType("TYPE");
        bk2.setRigType(rigType1);
        bk2.setUser(us1);
        bk2.setUserName(us1.getName());
        bk2.setUserNamespace(us1.getNamespace());
        ses.save(bk2);
        Bookings bk3 = new Bookings();
        bk3.setActive(true);
        bk3.setDuration(7200);
        bk3.setStartTime(begin.getTime());
        bk3.setEndTime(end.getTime());
        bk3.setResourcePermission(perm1);
        bk3.setResourceType("TYPE");
        bk3.setRigType(rigType1);
        bk3.setUser(us1);
        bk3.setUserName(us1.getName());
        bk3.setUserNamespace(us1.getNamespace());
        ses.save(bk3);
        Bookings bk4 = new Bookings();
        bk4.setActive(true);
        bk4.setDuration(3600);
        bk4.setStartTime(begin.getTime());
        bk4.setEndTime(end.getTime());
        bk4.setResourcePermission(perm1);
        bk4.setResourceType("TYPE");
        bk4.setRigType(rigType1);
        bk4.setUser(us1);
        bk4.setUserName(us1.getName());
        bk4.setUserNamespace(us1.getNamespace());
        ses.save(bk4);
        Bookings bk5 = new Bookings();
        bk5.setActive(true);
        bk5.setDuration(7200);
        bk5.setStartTime(begin.getTime());
        bk5.setEndTime(end.getTime());
        bk5.setResourcePermission(perm1);
        bk5.setResourceType("TYPE");
        bk5.setRigType(rigType1);
        bk5.setUser(us1);
        bk5.setUserName(us1.getName());
        bk5.setUserNamespace(us1.getNamespace());
        ses.save(bk5);
        /* Overbook. */
        Bookings bk6 = new Bookings();
        bk6.setActive(true);
        bk6.setDuration(1800);
        bk6.setStartTime(begin.getTime());
        bk6.setEndTime(end.getTime());
        bk6.setResourcePermission(perm1);
        bk6.setResourceType("TYPE");
        bk6.setRigType(rigType1);
        bk6.setUser(us1);
        bk6.setUserName(us1.getName());
        bk6.setUserNamespace(us1.getNamespace());
        ses.save(bk6);
        /* Different type. */
        Bookings bk7 = new Bookings();
        bk7.setActive(true);
        bk7.setDuration(1800);
        bk7.setStartTime(begin.getTime());
        bk7.setEndTime(end.getTime());
        bk7.setResourcePermission(perm1);
        bk7.setResourceType("TYPE");
        bk7.setRigType(rigType2);
        bk7.setUser(us1);
        bk7.setUserName(us1.getName());
        bk7.setUserNamespace(us1.getNamespace());
        ses.save(bk7);
        ses.getTransaction().commit();
        
        ses.refresh(rigType1);
        ses.refresh(rigType2);
        
        Method m = DayBookings.class.getDeclaredMethod("getRigBookings", Rig.class, Session.class);
        m.setAccessible(true);
        RigBookings rb = (RigBookings)m.invoke(this.day, r1, ses);
        
        ses.beginTransaction();
        ses.delete(bk7);
        ses.delete(bk6);
        ses.delete(bk5);
        ses.delete(bk4);
        ses.delete(bk3);
        ses.delete(bk2);
        ses.delete(bk1);
        ses.delete(perm1);
        ses.delete(r1);
        ses.delete(r2);
        ses.delete(r3);
        ses.delete(r4);
        ses.delete(r5);
        ses.delete(r6);
        ses.delete(caps);
        ses.delete(rigType1);
        ses.delete(rigType2);
        ses.delete(us1);
        ses.delete(uclass1);
        ses.getTransaction().commit();
        
        assertNotNull(rb);
        
        int i = 0;
        List<Rig> rigs = new ArrayList<Rig>(5);
        rigs.add(r1);
        rigs.add(r2);
        rigs.add(r3);
        rigs.add(r4);
        rigs.add(r5);
        
        RigBookings next = rb;
        do
        {
            i++;
            assertTrue(rigs.remove(next.getRig()));
            assertFalse(next.areSlotsFree(0, 95));
            assertEquals(0, next.getFreeSlots().size());
            next = next.getTypeLoopNext();
        }
        while (next != rb);
        
        assertEquals(5, i);
        assertEquals(0, rigs.size());
        
        assertTrue(bk1.isActive());
        assertTrue(bk2.isActive());
        assertTrue(bk3.isActive());
        assertTrue(bk4.isActive());
        assertTrue(bk5.isActive());
        assertFalse(bk6.isActive());
        assertTrue(bk7.isActive());
    }
    
    public void testLoadRigBookingsNoB() throws Exception
    {   
        Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        RigType rigType1 = new RigType("booktestrigtype", 300, false);
        ses.save(rigType1);
        RigCapabilities caps = new RigCapabilities("book,test,tar,foo");
        ses.save(caps);
        Rig r1 = new Rig();
        r1.setName("bkrig1");
        r1.setRigType(rigType1);
        r1.setLastUpdateTimestamp(new Date());
        r1.setRigCapabilities(caps);
        ses.save(r1);
        ses.getTransaction().commit();
        
        Method m = DayBookings.class.getDeclaredMethod("loadRig", RigBookings.class, Rig.class, Session.class);
        m.setAccessible(true);
        RigBookings bookings = new RigBookings(r1, this.day.getDay());
        m.invoke(this.day, bookings, r1, ses);
        
        ses.beginTransaction();
        ses.delete(r1);
        ses.delete(caps);
        ses.delete(rigType1);
        ses.getTransaction().commit();
        
        assertEquals(0, bookings.getNumBookings());
        assertTrue(bookings.areSlotsFree(0, 95));
    }

    public void testLoadRigBookings() throws Exception
    {
        Calendar begin = TimeUtil.getDayBegin(this.day.getDay());
        Calendar end = TimeUtil.getDayEnd(this.day.getDay());
        end.add(Calendar.SECOND, 1);
        
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
        RigCapabilities caps = new RigCapabilities("book,test,tar,foo");
        ses.save(caps);
        Rig r1 = new Rig();
        r1.setName("bkrig1");
        r1.setRigType(rigType1);
        r1.setLastUpdateTimestamp(new Date());
        r1.setRigCapabilities(caps);
        ses.save(r1);
        Rig r2 = new Rig();
        r2.setName("bkrig2");
        r2.setRigType(rigType1);
        r2.setLastUpdateTimestamp(new Date());
        r2.setRigCapabilities(caps);
        ses.save(r2);
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
        Bookings bk1 = new Bookings();
        bk1.setActive(true);
        bk1.setDuration(3600);
        begin.add(Calendar.HOUR_OF_DAY, -2);
        bk1.setStartTime(begin.getTime());
        begin.add(Calendar.HOUR_OF_DAY, 1);
        bk1.setEndTime(begin.getTime());
        bk1.setResourcePermission(perm1);
        bk1.setResourceType("RIG");
        bk1.setRig(r1);
        bk1.setUser(us1);
        bk1.setUserName(us1.getName());
        bk1.setUserNamespace(us1.getNamespace());
        ses.save(bk1);
        Bookings bk2 = new Bookings();
        bk2.setActive(false);
        bk2.setCancelReason("Test cancel.");
        bk2.setDuration(3600);
        Calendar st = Calendar.getInstance();
        st.setTime(begin.getTime());
        begin.add(Calendar.HOUR, 2);
        bk2.setStartTime(st.getTime());
        bk2.setEndTime(begin.getTime());
        bk2.setResourcePermission(perm1);
        bk2.setResourceType("RIG");
        bk2.setRig(r1);
        bk2.setUser(us1);
        bk2.setUserName(us1.getName());
        bk2.setUserNamespace(us1.getNamespace());
        ses.save(bk2);
        /* Booking 1 - 0, 3 */
        Bookings bk3 = new Bookings();
        bk3.setActive(true);
        bk3.setDuration(7200);
        bk3.setStartTime(st.getTime());
        bk3.setEndTime(begin.getTime());
        bk3.setResourcePermission(perm1);
        bk3.setResourceType("RIG");
        bk3.setRig(r1);
        bk3.setUser(us1);
        bk3.setUserName(us1.getName());
        bk3.setUserNamespace(us1.getNamespace());
        ses.save(bk3);
        Bookings bk4 = new Bookings();
        bk4.setActive(true);
        bk4.setDuration(3600);
        bk4.setStartTime(st.getTime());
        bk4.setEndTime(begin.getTime());
        bk4.setResourcePermission(perm1);
        bk4.setResourceType("TYPE");
        bk4.setRigType(rigType1);
        bk4.setUser(us2);
        bk4.setUserName(us2.getName());
        bk4.setUserNamespace(us2.getNamespace());
        ses.save(bk4);
        /* Book 2 - 4 - 8 */
        Bookings bk5 = new Bookings();
        bk5.setActive(true);
        bk5.setDuration(7200);
        bk5.setStartTime(begin.getTime());
        begin.add(Calendar.HOUR, 2);
        bk5.setEndTime(begin.getTime());
        bk5.setResourcePermission(perm1);
        bk5.setResourceType("RIG");
        bk5.setRig(r1);
        bk5.setUser(us2);
        bk5.setUserName(us2.getName());
        bk5.setUserNamespace(us2.getNamespace());
        ses.save(bk5);
        /* Book 3 - 13, 15 */
        Bookings bk6 = new Bookings();
        bk6.setActive(true);
        bk6.setDuration(1800);
        begin.add(Calendar.HOUR, 1);
        begin.add(Calendar.MINUTE, 30);
        bk6.setStartTime(begin.getTime());
        begin.add(Calendar.MINUTE, 30);
        bk6.setEndTime(begin.getTime());
        bk6.setResourcePermission(perm1);
        bk6.setResourceType("RIG");
        bk6.setRig(r1);
        bk6.setUser(us2);
        bk6.setUserName(us2.getName());
        bk6.setUserNamespace(us2.getNamespace());
        ses.save(bk6);
        /* Over committed */
        Bookings bk7 = new Bookings();
        bk7.setActive(true);
        bk7.setDuration(1800);
        begin.add(Calendar.MINUTE, -15);
        bk7.setStartTime(begin.getTime());
        begin.add(Calendar.MINUTE, 30);
        bk7.setEndTime(begin.getTime());
        bk7.setResourcePermission(perm1);
        bk7.setResourceType("RIG");
        bk7.setRig(r1);
        bk7.setUser(us2);
        bk7.setUserName(us2.getName());
        bk7.setUserNamespace(us2.getNamespace());
        ses.save(bk7);
        /* Non-aligned */
        Bookings bk8 = new Bookings();
        bk8.setActive(true);
        bk8.setDuration(1800);
        begin.add(Calendar.MINUTE, 10);
        bk8.setStartTime(begin.getTime());
        begin.add(Calendar.MINUTE, 40);
        bk8.setEndTime(begin.getTime());
        bk8.setResourcePermission(perm1);
        bk8.setResourceType("RIG");
        bk8.setRig(r1);
        bk8.setUser(us2);
        bk8.setUserName(us2.getName());
        bk8.setUserNamespace(us2.getNamespace());
        ses.save(bk8);

        /* Day end. */
        Bookings bk9 = new Bookings();
        bk9.setActive(true);
        bk9.setDuration(7200);
        end.add(Calendar.HOUR, -1);
        bk9.setStartTime(end.getTime());
        end.add(Calendar.HOUR, 2);
        bk9.setEndTime(end.getTime());
        bk9.setResourcePermission(perm1);
        bk9.setResourceType("RIG");
        bk9.setRig(r1);
        bk9.setUser(us2);
        bk9.setUserName(us2.getName());
        bk9.setUserNamespace(us2.getNamespace());
        ses.save(bk9);
        ses.getTransaction().commit();
        
        Method m = DayBookings.class.getDeclaredMethod("loadRig", RigBookings.class, Rig.class, Session.class);
        m.setAccessible(true);
        RigBookings bookings = new RigBookings(r1, this.day.getDay());
        m.invoke(this.day, bookings, r1, ses);
        
        ses.refresh(bk1);
        ses.refresh(bk2);
        ses.refresh(bk3);
        ses.refresh(bk4);
        ses.refresh(bk5);
        ses.refresh(bk6);
        ses.refresh(bk7);
        ses.refresh(bk8);
        ses.refresh(bk9);
        
        ses.beginTransaction();
        ses.delete(bk9);
        ses.delete(bk8);
        ses.delete(bk7);
        ses.delete(bk6);
        ses.delete(bk5);
        ses.delete(bk4);
        ses.delete(bk3);
        ses.delete(bk2);
        ses.delete(bk1);
        ses.delete(perm1);
        ses.delete(r1);
        ses.delete(r2);
        ses.delete(caps);
        ses.delete(rigType1);
        ses.delete(us1);
        ses.delete(us2);
        ses.delete(uclass1);
        ses.getTransaction().commit();
        
        assertTrue(bk1.isActive());
        assertFalse(bk2.isActive());
        assertTrue(bk3.isActive());
        assertTrue(bk4.isActive());
        assertTrue(bk5.isActive());
        assertTrue(bk6.isActive());
        assertFalse(bk7.isActive());
        assertTrue(bk8.isActive());
        assertTrue(bk9.isActive());
        
        assertFalse(bookings.hasBooking(new MBooking(bk1, this.dayStr)));
        assertFalse(bookings.hasBooking(new MBooking(bk2, this.dayStr)));
        assertTrue(bookings.hasBooking(new MBooking(bk3, this.dayStr)));
        assertFalse(bookings.hasBooking(new MBooking(bk4, this.dayStr)));
        assertTrue(bookings.hasBooking(new MBooking(bk5, this.dayStr)));
        assertTrue(bookings.hasBooking(new MBooking(bk6, this.dayStr)));
        assertFalse(bookings.hasBooking(new MBooking(bk7, this.dayStr)));
        assertTrue(bookings.hasBooking(new MBooking(bk8, this.dayStr)));
        assertTrue(bookings.hasBooking(new MBooking(bk9, this.dayStr)));
        
        assertEquals(5, bookings.getNumBookings());
        assertFalse(bookings.areSlotsFree(0, 8));
        assertTrue(bookings.areSlotsFree(12, 17));
        assertFalse(bookings.areSlotsFree(18, 19));
        assertTrue(bookings.areSlotsFree(20, 20));
        assertFalse(bookings.areSlotsFree(21, 24));
        assertTrue(bookings.areSlotsFree(25, 91));
        assertFalse(bookings.areSlotsFree(92, 95));
        
        assertNotNull(bk7.getCancelReason());
    }
}
