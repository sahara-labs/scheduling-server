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
 * @date 11th November 2011
 */
package au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine.tests;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.hibernate.Session;

import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine.DayBookings;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine.MBooking;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine.MBooking.BType;
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
 * More tests of the {@link DayBookings} class. 
 */
public class DayBookingsTwoTester extends TestCase
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
    
    @SuppressWarnings("unchecked")
    public void testRigRegistered() throws Exception
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
        RigType rigType3 = new RigType("unrelatedrigtype", 300, false);
        ses.save(rigType3);
        RigType rigType4 = new RigType("noloadrigtype", 300, false);
        ses.save(rigType4);
        RigCapabilities caps1 = new RigCapabilities("book,test,foo");
        ses.save(caps1);
        RigCapabilities caps2 = new RigCapabilities("book,test,bar");
        ses.save(caps2);
        RigCapabilities caps3 = new RigCapabilities("book,baz,foo");
        ses.save(caps3);
        RigCapabilities caps4 = new RigCapabilities("something,different");
        ses.save(caps4);
        RigCapabilities caps5 = new RigCapabilities("diff,again");
        ses.save(caps5);
        RequestCapabilities rcaps1 = new RequestCapabilities("book");
        ses.save(rcaps1);
        RequestCapabilities rcaps2 = new RequestCapabilities("foo");
        ses.save(rcaps2);
        RequestCapabilities rcaps3 = new RequestCapabilities("bar");
        ses.save(rcaps3);
        RequestCapabilities rcaps4 = new RequestCapabilities("orphaned");
        ses.save(rcaps4);
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
        Rig r4 = new Rig();
        r4.setName("bkrig4");
        r4.setRigType(rigType3);
        r4.setLastUpdateTimestamp(new Date());
        r4.setRigCapabilities(caps4);
        ses.save(r4);
        Rig r5 = new Rig();
        r5.setName("bkrig5");
        r5.setRigType(rigType4);
        r5.setLastUpdateTimestamp(new Date());
        r5.setRigCapabilities(caps5);
        ses.save(r5);
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
        
        Bookings bk20 = new Bookings();
        bk20.setActive(true);
        bk20.setDuration(4500);
        rcap3tm.add(Calendar.HOUR, 7);
        bk20.setStartTime(rcap3tm.getTime());
        rcap3tm.add(Calendar.HOUR, 1);
        rcap3tm.add(Calendar.MINUTE, 15);
        bk20.setEndTime(rcap3tm.getTime());
        bk20.setResourcePermission(perm1);
        bk20.setResourceType("TYPE");
        bk20.setRigType(rigType3);
        bk20.setUser(us1);
        bk20.setUserName(us1.getName());
        bk20.setUserNamespace(us1.getNamespace());
        ses.save(bk20);
        
        /* Tomorrow. */
        Bookings bk21 = new Bookings();
        bk21.setActive(true);
        bk21.setDuration(4500);
        rcap3tm.add(Calendar.DAY_OF_MONTH, 1);
        bk21.setStartTime(rcap3tm.getTime());
        rcap3tm.add(Calendar.HOUR, 1);
        rcap3tm.add(Calendar.MINUTE, 15);
        bk21.setEndTime(rcap3tm.getTime());
        bk21.setResourcePermission(perm1);
        bk21.setResourceType("TYPE");
        bk21.setRigType(rigType4);
        bk21.setUser(us1);
        bk21.setUserName(us1.getName());
        bk21.setUserNamespace(us1.getNamespace());
        ses.save(bk21);
        
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
        ses.refresh(r4);
        ses.refresh(r5);
        ses.refresh(rigType1);
        ses.refresh(rigType2);
        ses.refresh(rigType3);
        ses.refresh(rigType4);

        this.day.fullLoad(ses);
        
        this.day.rigRegistered(r1, ses);
        
        ses.beginTransaction();
        ses.delete(bk21);
        ses.delete(bk20);
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
        ses.delete(r4);
        ses.delete(r5);
        ses.delete(mat1);
        ses.delete(mat2);
        ses.delete(mat3);
        ses.delete(mat4);
        ses.delete(mat5);
        ses.delete(mat6);
        ses.delete(caps1);
        ses.delete(caps2);
        ses.delete(caps3);
        ses.delete(caps4);
        ses.delete(caps5);
        ses.delete(rcaps1);
        ses.delete(rcaps2);
        ses.delete(rcaps3);
        ses.delete(rcaps4);
        ses.delete(rigType1);
        ses.delete(rigType2);
        ses.delete(rigType3);
        ses.delete(rigType4);
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
        assertTrue(bk20.isActive());
        assertTrue(bk21.isActive());
        
        Field f = DayBookings.class.getDeclaredField("rigBookings");
        f.setAccessible(true);
        Map<String, RigBookings> rbMap = (Map<String, RigBookings>)f.get(this.day);
        
        assertTrue(rbMap.containsKey(r1.getName()));
        assertTrue(rbMap.containsKey(r2.getName()));
        assertTrue(rbMap.containsKey(r3.getName()));
        assertTrue(rbMap.containsKey(r4.getName()));
        assertFalse(rbMap.containsKey(r5.getName()));
        
        RigBookings rb = rbMap.get(r1.getName());
        assertNotNull(rb);
        assertNotNull(rb.getTypeLoopNext());
        
        List<RigBookings> loop = new ArrayList<RigBookings>();
        RigBookings next = rb;
        for (int i = 0; i < 2; i++)
        {
            assertFalse(loop.contains(next));
            assertNotNull(next);
            assertNotNull(next.getTypeLoopNext());
            loop.add(next);
            next = next.getTypeLoopNext();
        }
        
        assertTrue(loop.contains(rb));
        assertTrue(loop.contains(rbMap.get(r1.getName())));
        assertTrue(loop.contains(rbMap.get(r2.getName())));
        
        f = DayBookings.class.getDeclaredField("typeTargets");
        f.setAccessible(true);
        Map<String, RigBookings> ttMap = (Map<String, RigBookings>)f.get(this.day);
        assertTrue(ttMap.containsKey(rigType1.getName()));
        assertTrue(ttMap.containsKey(rigType2.getName()));
        assertTrue(ttMap.containsKey(rigType3.getName()));
        assertFalse(ttMap.containsKey(rigType4.getName()));
        
        f = DayBookings.class.getDeclaredField("capsTargets");
        f.setAccessible(true);
        Map<String, RigBookings> cpMap = (Map<String, RigBookings>)f.get(this.day);
        
        assertTrue(cpMap.containsKey(rcaps1.getCapabilities()));
        assertTrue(cpMap.containsKey(rcaps2.getCapabilities()));
        assertTrue(cpMap.containsKey(rcaps3.getCapabilities()));
        assertFalse(cpMap.containsKey(rcaps4.getCapabilities()));
        
        assertNotNull(rb.getCapsLoopNext(rcaps1));
        assertNotNull(rb.getCapsLoopNext(rcaps2));
        assertNull(rb.getCapsLoopNext(rcaps3));
    }
    
    @SuppressWarnings("unchecked")
    public void testRigRegisteredChangedType() throws Exception
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
        RigType rigType3 = new RigType("unrelatedrigtype", 300, false);
        ses.save(rigType3);
        RigType rigType4 = new RigType("noloadrigtype", 300, false);
        ses.save(rigType4);
        RigCapabilities caps1 = new RigCapabilities("book,test,foo");
        ses.save(caps1);
        RigCapabilities caps2 = new RigCapabilities("book,test,bar");
        ses.save(caps2);
        RigCapabilities caps3 = new RigCapabilities("book,baz,foo");
        ses.save(caps3);
        RigCapabilities caps4 = new RigCapabilities("something,different");
        ses.save(caps4);
        RigCapabilities caps5 = new RigCapabilities("diff,again");
        ses.save(caps5);
        RequestCapabilities rcaps1 = new RequestCapabilities("book");
        ses.save(rcaps1);
        RequestCapabilities rcaps2 = new RequestCapabilities("foo");
        ses.save(rcaps2);
        RequestCapabilities rcaps3 = new RequestCapabilities("bar");
        ses.save(rcaps3);
        RequestCapabilities rcaps4 = new RequestCapabilities("orphaned");
        ses.save(rcaps4);
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
        Rig r4 = new Rig();
        r4.setName("bkrig4");
        r4.setRigType(rigType3);
        r4.setLastUpdateTimestamp(new Date());
        r4.setRigCapabilities(caps4);
        ses.save(r4);
        Rig r5 = new Rig();
        r5.setName("bkrig5");
        r5.setRigType(rigType4);
        r5.setLastUpdateTimestamp(new Date());
        r5.setRigCapabilities(caps5);
        ses.save(r5);
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
        
        Bookings bk20 = new Bookings();
        bk20.setActive(true);
        bk20.setDuration(4500);
        rcap3tm.add(Calendar.HOUR, 7);
        bk20.setStartTime(rcap3tm.getTime());
        rcap3tm.add(Calendar.HOUR, 1);
        rcap3tm.add(Calendar.MINUTE, 15);
        bk20.setEndTime(rcap3tm.getTime());
        bk20.setResourcePermission(perm1);
        bk20.setResourceType("TYPE");
        bk20.setRigType(rigType3);
        bk20.setUser(us1);
        bk20.setUserName(us1.getName());
        bk20.setUserNamespace(us1.getNamespace());
        ses.save(bk20);
        
        /* Tomorrow. */
        Bookings bk21 = new Bookings();
        bk21.setActive(true);
        bk21.setDuration(4500);
        rcap3tm.add(Calendar.DAY_OF_MONTH, 1);
        bk21.setStartTime(rcap3tm.getTime());
        rcap3tm.add(Calendar.HOUR, 1);
        rcap3tm.add(Calendar.MINUTE, 15);
        bk21.setEndTime(rcap3tm.getTime());
        bk21.setResourcePermission(perm1);
        bk21.setResourceType("TYPE");
        bk21.setRigType(rigType4);
        bk21.setUser(us1);
        bk21.setUserName(us1.getName());
        bk21.setUserNamespace(us1.getNamespace());
        ses.save(bk21);
        
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
        ses.refresh(r4);
        ses.refresh(r5);
        ses.refresh(rigType1);
        ses.refresh(rigType2);
        ses.refresh(rigType3);
        ses.refresh(rigType4);

        this.day.fullLoad(ses);
        
        r3.setRigType(rigType1);
        ses.beginTransaction();
        ses.flush();
        ses.getTransaction().commit();
        
        this.day.rigRegistered(r3, ses);
        
        ses.beginTransaction();
        ses.delete(bk21);
        ses.delete(bk20);
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
        ses.delete(r4);
        ses.delete(r5);
        ses.delete(mat1);
        ses.delete(mat2);
        ses.delete(mat3);
        ses.delete(mat4);
        ses.delete(mat5);
        ses.delete(mat6);
        ses.delete(caps1);
        ses.delete(caps2);
        ses.delete(caps3);
        ses.delete(caps4);
        ses.delete(caps5);
        ses.delete(rcaps1);
        ses.delete(rcaps2);
        ses.delete(rcaps3);
        ses.delete(rcaps4);
        ses.delete(rigType1);
        ses.delete(rigType2);
        ses.delete(rigType3);
        ses.delete(rigType4);
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
        assertFalse(bk16.isActive());
        assertTrue(bk17.isActive());
        assertTrue(bk18.isActive());
        assertTrue(bk19.isActive());
        assertTrue(bk20.isActive());
        assertTrue(bk21.isActive());
        
        Field f = DayBookings.class.getDeclaredField("rigBookings");
        f.setAccessible(true);
        Map<String, RigBookings> rbMap = (Map<String, RigBookings>)f.get(this.day);
        
        assertTrue(rbMap.containsKey(r1.getName()));
        assertTrue(rbMap.containsKey(r2.getName()));
        assertTrue(rbMap.containsKey(r3.getName()));
        assertTrue(rbMap.containsKey(r4.getName()));
        assertFalse(rbMap.containsKey(r5.getName()));
        
        RigBookings rb = rbMap.get(r3.getName());
        assertNotNull(rb);
        assertNotNull(rb.getTypeLoopNext());
        
        List<RigBookings> loop = new ArrayList<RigBookings>();
        RigBookings next = rb;
        for (int i = 0; i < 3; i++)
        {
            assertFalse(loop.contains(next));
            assertNotNull(next);
            assertNotNull(next.getTypeLoopNext());
            loop.add(next);
            next = next.getTypeLoopNext();
        }
        
        assertTrue(loop.contains(rb));
        assertTrue(loop.contains(rbMap.get(r1.getName())));
        assertTrue(loop.contains(rbMap.get(r2.getName())));
        assertTrue(loop.contains(rbMap.get(r3.getName())));
        
        f = DayBookings.class.getDeclaredField("typeTargets");
        f.setAccessible(true);
        Map<String, RigBookings> ttMap = (Map<String, RigBookings>)f.get(this.day);
        assertTrue(ttMap.containsKey(rigType1.getName()));
        assertFalse(ttMap.containsKey(rigType2.getName()));
        assertTrue(ttMap.containsKey(rigType3.getName()));
        assertFalse(ttMap.containsKey(rigType4.getName()));
        
        f = DayBookings.class.getDeclaredField("capsTargets");
        f.setAccessible(true);
        Map<String, RigBookings> cpMap = (Map<String, RigBookings>)f.get(this.day);
        
        assertTrue(cpMap.containsKey(rcaps1.getCapabilities()));
        assertTrue(cpMap.containsKey(rcaps2.getCapabilities()));
        assertTrue(cpMap.containsKey(rcaps3.getCapabilities()));
        assertFalse(cpMap.containsKey(rcaps4.getCapabilities()));
        
        assertNotNull(rb.getCapsLoopNext(rcaps1));
        assertNotNull(rb.getCapsLoopNext(rcaps2));
        assertNull(rb.getCapsLoopNext(rcaps3));
    }
    
    @SuppressWarnings("unchecked")
    public void testRigRegisteredChangedType2() throws Exception
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
        RigType rigType3 = new RigType("unrelatedrigtype", 300, false);
        ses.save(rigType3);
        RigType rigType4 = new RigType("noloadrigtype", 300, false);
        ses.save(rigType4);
        RigCapabilities caps1 = new RigCapabilities("book,test,foo");
        ses.save(caps1);
        RigCapabilities caps2 = new RigCapabilities("book,test,bar");
        ses.save(caps2);
        RigCapabilities caps3 = new RigCapabilities("book,baz,foo");
        ses.save(caps3);
        RigCapabilities caps4 = new RigCapabilities("something,different");
        ses.save(caps4);
        RigCapabilities caps5 = new RigCapabilities("diff,again");
        ses.save(caps5);
        RequestCapabilities rcaps1 = new RequestCapabilities("book");
        ses.save(rcaps1);
        RequestCapabilities rcaps2 = new RequestCapabilities("foo");
        ses.save(rcaps2);
        RequestCapabilities rcaps3 = new RequestCapabilities("bar");
        ses.save(rcaps3);
        RequestCapabilities rcaps4 = new RequestCapabilities("orphaned");
        ses.save(rcaps4);
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
        Rig r4 = new Rig();
        r4.setName("bkrig4");
        r4.setRigType(rigType3);
        r4.setLastUpdateTimestamp(new Date());
        r4.setRigCapabilities(caps4);
        ses.save(r4);
        Rig r5 = new Rig();
        r5.setName("bkrig5");
        r5.setRigType(rigType4);
        r5.setLastUpdateTimestamp(new Date());
        r5.setRigCapabilities(caps5);
        ses.save(r5);
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
        
        Bookings bk20 = new Bookings();
        bk20.setActive(true);
        bk20.setDuration(4500);
        rcap3tm.add(Calendar.HOUR, 7);
        bk20.setStartTime(rcap3tm.getTime());
        rcap3tm.add(Calendar.HOUR, 1);
        rcap3tm.add(Calendar.MINUTE, 15);
        bk20.setEndTime(rcap3tm.getTime());
        bk20.setResourcePermission(perm1);
        bk20.setResourceType("TYPE");
        bk20.setRigType(rigType3);
        bk20.setUser(us1);
        bk20.setUserName(us1.getName());
        bk20.setUserNamespace(us1.getNamespace());
        ses.save(bk20);
        
        /* Tomorrow. */
        Bookings bk21 = new Bookings();
        bk21.setActive(true);
        bk21.setDuration(4500);
        rcap3tm.add(Calendar.DAY_OF_MONTH, 1);
        bk21.setStartTime(rcap3tm.getTime());
        rcap3tm.add(Calendar.HOUR, 1);
        rcap3tm.add(Calendar.MINUTE, 15);
        bk21.setEndTime(rcap3tm.getTime());
        bk21.setResourcePermission(perm1);
        bk21.setResourceType("TYPE");
        bk21.setRigType(rigType4);
        bk21.setUser(us1);
        bk21.setUserName(us1.getName());
        bk21.setUserNamespace(us1.getNamespace());
        ses.save(bk21);
        
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
        ses.refresh(r4);
        ses.refresh(r5);
        ses.refresh(rigType1);
        ses.refresh(rigType2);
        ses.refresh(rigType3);
        ses.refresh(rigType4);

        this.day.fullLoad(ses);
        
        r1.setRigType(rigType2);
        ses.beginTransaction();
        ses.flush();
        ses.getTransaction().commit();
        
        this.day.rigRegistered(r1, ses);
        ses.refresh(bk2);
        
        ses.beginTransaction();
        ses.delete(bk21);
        ses.delete(bk20);
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
        ses.delete(r4);
        ses.delete(r5);
        ses.delete(mat1);
        ses.delete(mat2);
        ses.delete(mat3);
        ses.delete(mat4);
        ses.delete(mat5);
        ses.delete(mat6);
        ses.delete(caps1);
        ses.delete(caps2);
        ses.delete(caps3);
        ses.delete(caps4);
        ses.delete(caps5);
        ses.delete(rcaps1);
        ses.delete(rcaps2);
        ses.delete(rcaps3);
        ses.delete(rcaps4);
        ses.delete(rigType1);
        ses.delete(rigType2);
        ses.delete(rigType3);
        ses.delete(rigType4);
        ses.delete(us1);
        ses.delete(uclass1);
        ses.getTransaction().commit();
        
        assertTrue(bk1.isActive());
        assertTrue(bk2.isActive());
        assertTrue(bk3.isActive());
        assertFalse(bk4.isActive());
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
        assertTrue(bk20.isActive());
        assertTrue(bk21.isActive());
        
        Field f = DayBookings.class.getDeclaredField("rigBookings");
        f.setAccessible(true);
        Map<String, RigBookings> rbMap = (Map<String, RigBookings>)f.get(this.day);
        
        assertTrue(rbMap.containsKey(r1.getName()));
        assertTrue(rbMap.containsKey(r2.getName()));
        assertTrue(rbMap.containsKey(r3.getName()));
        assertTrue(rbMap.containsKey(r4.getName()));
        assertFalse(rbMap.containsKey(r5.getName()));
        
        RigBookings rb = rbMap.get(r1.getName());
        assertNotNull(rb);
        assertNotNull(rb.getTypeLoopNext());
        
        List<RigBookings> loop = new ArrayList<RigBookings>();
        RigBookings next = rb;
        for (int i = 0; i < 2; i++)
        {
            assertFalse(loop.contains(next));
            assertNotNull(next);
            assertNotNull(next.getTypeLoopNext());
            loop.add(next);
            next = next.getTypeLoopNext();
        }
        
        assertTrue(loop.contains(rb));
        assertTrue(loop.contains(rbMap.get(r1.getName())));
        assertTrue(loop.contains(rbMap.get(r3.getName())));
        
        RigBookings rb2 = rbMap.get(r2.getName());
        assertTrue(rb2 == rb2.getTypeLoopNext());
       
        f = DayBookings.class.getDeclaredField("typeTargets");
        f.setAccessible(true);
        Map<String, RigBookings> ttMap = (Map<String, RigBookings>)f.get(this.day);
        assertTrue(ttMap.containsKey(rigType1.getName()));
        assertTrue(ttMap.containsKey(rigType2.getName()));
        assertTrue(ttMap.containsKey(rigType3.getName()));
        assertFalse(ttMap.containsKey(rigType4.getName()));
        assertTrue(ttMap.get(rigType1.getName()) == rb2);
        
        f = DayBookings.class.getDeclaredField("capsTargets");
        f.setAccessible(true);
        Map<String, RigBookings> cpMap = (Map<String, RigBookings>)f.get(this.day);
        
        assertTrue(cpMap.containsKey(rcaps1.getCapabilities()));
        assertTrue(cpMap.containsKey(rcaps2.getCapabilities()));
        assertTrue(cpMap.containsKey(rcaps3.getCapabilities()));
        assertFalse(cpMap.containsKey(rcaps4.getCapabilities()));
        
        assertNotNull(rb.getCapsLoopNext(rcaps1));
        assertNotNull(rb.getCapsLoopNext(rcaps2));
        assertNull(rb.getCapsLoopNext(rcaps3));
    }
    
    @SuppressWarnings("unchecked")
    public void testRigRegisteredNewMatch() throws Exception
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
        RigType rigType3 = new RigType("unrelatedrigtype", 300, false);
        ses.save(rigType3);
        RigType rigType4 = new RigType("noloadrigtype", 300, false);
        ses.save(rigType4);
        RigCapabilities caps1 = new RigCapabilities("book,test,foo");
        ses.save(caps1);
        RigCapabilities caps2 = new RigCapabilities("book,test,bar");
        ses.save(caps2);
        RigCapabilities caps3 = new RigCapabilities("book,baz,foo");
        ses.save(caps3);
        RigCapabilities caps4 = new RigCapabilities("something,different");
        ses.save(caps4);
        RigCapabilities caps5 = new RigCapabilities("diff,again");
        ses.save(caps5);
        RequestCapabilities rcaps1 = new RequestCapabilities("book");
        ses.save(rcaps1);
        RequestCapabilities rcaps2 = new RequestCapabilities("foo");
        ses.save(rcaps2);
        RequestCapabilities rcaps3 = new RequestCapabilities("bar");
        ses.save(rcaps3);
        RequestCapabilities rcaps4 = new RequestCapabilities("orphaned");
        ses.save(rcaps4);
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
        Rig r4 = new Rig();
        r4.setName("bkrig4");
        r4.setRigType(rigType3);
        r4.setLastUpdateTimestamp(new Date());
        r4.setRigCapabilities(caps4);
        ses.save(r4);
        Rig r5 = new Rig();
        r5.setName("bkrig5");
        r5.setRigType(rigType4);
        r5.setLastUpdateTimestamp(new Date());
        r5.setRigCapabilities(caps5);
        ses.save(r5);
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
        
        Bookings bk20 = new Bookings();
        bk20.setActive(true);
        bk20.setDuration(4500);
        rcap3tm.add(Calendar.HOUR, 7);
        bk20.setStartTime(rcap3tm.getTime());
        rcap3tm.add(Calendar.HOUR, 1);
        rcap3tm.add(Calendar.MINUTE, 15);
        bk20.setEndTime(rcap3tm.getTime());
        bk20.setResourcePermission(perm1);
        bk20.setResourceType("TYPE");
        bk20.setRigType(rigType3);
        bk20.setUser(us1);
        bk20.setUserName(us1.getName());
        bk20.setUserNamespace(us1.getNamespace());
        ses.save(bk20);
        
        /* Tomorrow. */
        Bookings bk21 = new Bookings();
        bk21.setActive(true);
        bk21.setDuration(4500);
        rcap3tm.add(Calendar.DAY_OF_MONTH, 1);
        bk21.setStartTime(rcap3tm.getTime());
        rcap3tm.add(Calendar.HOUR, 1);
        rcap3tm.add(Calendar.MINUTE, 15);
        bk21.setEndTime(rcap3tm.getTime());
        bk21.setResourcePermission(perm1);
        bk21.setResourceType("TYPE");
        bk21.setRigType(rigType4);
        bk21.setUser(us1);
        bk21.setUserName(us1.getName());
        bk21.setUserNamespace(us1.getNamespace());
        ses.save(bk21);
        
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
        ses.refresh(r4);
        ses.refresh(r5);
        ses.refresh(rigType1);
        ses.refresh(rigType2);
        ses.refresh(rigType3);
        ses.refresh(rigType4);

        this.day.fullLoad(ses);
        
        ses.beginTransaction();
        Rig r6 = new Rig();
        r6.setName("bkrig6");
        r6.setRigType(rigType1);
        r6.setLastUpdateTimestamp(new Date());
        r6.setRigCapabilities(caps2);
        ses.save(r6);
        ses.getTransaction().commit();
        
        this.day.rigRegistered(r6, ses);
        
        ses.beginTransaction();
        ses.delete(bk21);
        ses.delete(bk20);
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
        ses.delete(r4);
        ses.delete(r5);
        ses.delete(r6);
        ses.delete(mat1);
        ses.delete(mat2);
        ses.delete(mat3);
        ses.delete(mat4);
        ses.delete(mat5);
        ses.delete(mat6);
        ses.delete(caps1);
        ses.delete(caps2);
        ses.delete(caps3);
        ses.delete(caps4);
        ses.delete(caps5);
        ses.delete(rcaps1);
        ses.delete(rcaps2);
        ses.delete(rcaps3);
        ses.delete(rcaps4);
        ses.delete(rigType1);
        ses.delete(rigType2);
        ses.delete(rigType3);
        ses.delete(rigType4);
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
        assertTrue(bk20.isActive());
        assertTrue(bk21.isActive());
        
        Field f = DayBookings.class.getDeclaredField("rigBookings");
        f.setAccessible(true);
        Map<String, RigBookings> rbMap = (Map<String, RigBookings>)f.get(this.day);
        
        assertTrue(rbMap.containsKey(r1.getName()));
        assertTrue(rbMap.containsKey(r2.getName()));
        assertTrue(rbMap.containsKey(r3.getName()));
        assertTrue(rbMap.containsKey(r4.getName()));
        assertFalse(rbMap.containsKey(r5.getName()));
        assertTrue(rbMap.containsKey(r6.getName()));
        
        RigBookings rb = rbMap.get(r6.getName());
        assertNotNull(rb);
        assertNotNull(rb.getTypeLoopNext());
        
        List<RigBookings> loop = new ArrayList<RigBookings>();
        RigBookings next = rb;
        for (int i = 0; i < 3; i++)
        {
            assertFalse(loop.contains(next));
            assertNotNull(next);
            assertNotNull(next.getTypeLoopNext());
            loop.add(next);
            next = next.getTypeLoopNext();
        }
        
        assertTrue(loop.contains(rb));
        assertTrue(loop.contains(rbMap.get(r1.getName())));
        assertTrue(loop.contains(rbMap.get(r2.getName())));
        
        f = DayBookings.class.getDeclaredField("typeTargets");
        f.setAccessible(true);
        Map<String, RigBookings> ttMap = (Map<String, RigBookings>)f.get(this.day);
        assertTrue(ttMap.containsKey(rigType1.getName()));
        assertTrue(ttMap.containsKey(rigType2.getName()));
        assertTrue(ttMap.containsKey(rigType3.getName()));
        assertFalse(ttMap.containsKey(rigType4.getName()));
        
        f = DayBookings.class.getDeclaredField("capsTargets");
        f.setAccessible(true);
        Map<String, RigBookings> cpMap = (Map<String, RigBookings>)f.get(this.day);
        
        assertTrue(cpMap.containsKey(rcaps1.getCapabilities()));
        assertTrue(cpMap.containsKey(rcaps2.getCapabilities()));
        assertTrue(cpMap.containsKey(rcaps3.getCapabilities()));
        assertFalse(cpMap.containsKey(rcaps4.getCapabilities()));
        
        assertNotNull(rb.getCapsLoopNext(rcaps3));
        
        RigBookings rb2 = rbMap.get(r2.getName());
        assertEquals(rb.getCapsLoopNext(rcaps3), rb2);
        assertEquals(rb, rb2.getCapsLoopNext(rcaps3));
        
        assertNotNull(rb.getCapsLoopNext(rcaps1));
        assertNull(rb.getCapsLoopNext(rcaps2));
    }
    
    @SuppressWarnings("unchecked")
    public void testRigRegisteredNewNoMatch() throws Exception
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
        RigType rigType3 = new RigType("unrelatedrigtype", 300, false);
        ses.save(rigType3);
        RigType rigType4 = new RigType("noloadrigtype", 300, false);
        ses.save(rigType4);
        RigCapabilities caps1 = new RigCapabilities("book,test,foo");
        ses.save(caps1);
        RigCapabilities caps2 = new RigCapabilities("book,test,bar");
        ses.save(caps2);
        RigCapabilities caps3 = new RigCapabilities("book,baz,foo");
        ses.save(caps3);
        RigCapabilities caps4 = new RigCapabilities("something,different");
        ses.save(caps4);
        RigCapabilities caps5 = new RigCapabilities("diff,again");
        ses.save(caps5);
        RequestCapabilities rcaps1 = new RequestCapabilities("book");
        ses.save(rcaps1);
        RequestCapabilities rcaps2 = new RequestCapabilities("foo");
        ses.save(rcaps2);
        RequestCapabilities rcaps3 = new RequestCapabilities("bar");
        ses.save(rcaps3);
        RequestCapabilities rcaps4 = new RequestCapabilities("orphaned");
        ses.save(rcaps4);
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
        Rig r4 = new Rig();
        r4.setName("bkrig4");
        r4.setRigType(rigType3);
        r4.setLastUpdateTimestamp(new Date());
        r4.setRigCapabilities(caps4);
        ses.save(r4);
        Rig r5 = new Rig();
        r5.setName("bkrig5");
        r5.setRigType(rigType4);
        r5.setLastUpdateTimestamp(new Date());
        r5.setRigCapabilities(caps5);
        ses.save(r5);
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
        
        Bookings bk20 = new Bookings();
        bk20.setActive(true);
        bk20.setDuration(4500);
        rcap3tm.add(Calendar.HOUR, 7);
        bk20.setStartTime(rcap3tm.getTime());
        rcap3tm.add(Calendar.HOUR, 1);
        rcap3tm.add(Calendar.MINUTE, 15);
        bk20.setEndTime(rcap3tm.getTime());
        bk20.setResourcePermission(perm1);
        bk20.setResourceType("TYPE");
        bk20.setRigType(rigType3);
        bk20.setUser(us1);
        bk20.setUserName(us1.getName());
        bk20.setUserNamespace(us1.getNamespace());
        ses.save(bk20);
        
        /* Tomorrow. */
        Bookings bk21 = new Bookings();
        bk21.setActive(true);
        bk21.setDuration(4500);
        rcap3tm.add(Calendar.DAY_OF_MONTH, 1);
        bk21.setStartTime(rcap3tm.getTime());
        rcap3tm.add(Calendar.HOUR, 1);
        rcap3tm.add(Calendar.MINUTE, 15);
        bk21.setEndTime(rcap3tm.getTime());
        bk21.setResourcePermission(perm1);
        bk21.setResourceType("TYPE");
        bk21.setRigType(rigType4);
        bk21.setUser(us1);
        bk21.setUserName(us1.getName());
        bk21.setUserNamespace(us1.getNamespace());
        ses.save(bk21);
        
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
        ses.refresh(r4);
        ses.refresh(r5);
        ses.refresh(rigType1);
        ses.refresh(rigType2);
        ses.refresh(rigType3);
        ses.refresh(rigType4);

        this.day.fullLoad(ses);
        
        ses.beginTransaction();
        Rig r6 = new Rig();
        r6.setName("bkrig6");
        r6.setRigType(rigType4);
        r6.setLastUpdateTimestamp(new Date());
        r6.setRigCapabilities(caps5);
        ses.save(r6);
        ses.getTransaction().commit();
        
        this.day.rigRegistered(r6, ses);
        
        ses.beginTransaction();
        ses.delete(bk21);
        ses.delete(bk20);
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
        ses.delete(r4);
        ses.delete(r5);
        ses.delete(r6);
        ses.delete(mat1);
        ses.delete(mat2);
        ses.delete(mat3);
        ses.delete(mat4);
        ses.delete(mat5);
        ses.delete(mat6);
        ses.delete(caps1);
        ses.delete(caps2);
        ses.delete(caps3);
        ses.delete(caps4);
        ses.delete(caps5);
        ses.delete(rcaps1);
        ses.delete(rcaps2);
        ses.delete(rcaps3);
        ses.delete(rcaps4);
        ses.delete(rigType1);
        ses.delete(rigType2);
        ses.delete(rigType3);
        ses.delete(rigType4);
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
        assertTrue(bk20.isActive());
        assertTrue(bk21.isActive());
        
        Field f = DayBookings.class.getDeclaredField("rigBookings");
        f.setAccessible(true);
        Map<String, RigBookings> rbMap = (Map<String, RigBookings>)f.get(this.day);
        
        assertTrue(rbMap.containsKey(r1.getName()));
        assertTrue(rbMap.containsKey(r2.getName()));
        assertTrue(rbMap.containsKey(r3.getName()));
        assertTrue(rbMap.containsKey(r4.getName()));
        assertFalse(rbMap.containsKey(r5.getName()));
        assertFalse(rbMap.containsKey(r6.getName()));
        
        f = DayBookings.class.getDeclaredField("typeTargets");
        f.setAccessible(true);
        Map<String, RigBookings> ttMap = (Map<String, RigBookings>)f.get(this.day);
        assertTrue(ttMap.containsKey(rigType1.getName()));
        assertTrue(ttMap.containsKey(rigType2.getName()));
        assertTrue(ttMap.containsKey(rigType3.getName()));
        assertFalse(ttMap.containsKey(rigType4.getName()));
        
        f = DayBookings.class.getDeclaredField("capsTargets");
        f.setAccessible(true);
        Map<String, RigBookings> cpMap = (Map<String, RigBookings>)f.get(this.day);
        
        assertTrue(cpMap.containsKey(rcaps1.getCapabilities()));
        assertTrue(cpMap.containsKey(rcaps2.getCapabilities()));
        assertTrue(cpMap.containsKey(rcaps3.getCapabilities()));
        assertFalse(cpMap.containsKey(rcaps4.getCapabilities()));
    }
    
    @SuppressWarnings("unchecked")
    public void testRigRegisteredExistingNoLoad() throws Exception
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
        RigType rigType3 = new RigType("unrelatedrigtype", 300, false);
        ses.save(rigType3);
        RigType rigType4 = new RigType("noloadrigtype", 300, false);
        ses.save(rigType4);
        RigCapabilities caps1 = new RigCapabilities("book,test,foo");
        ses.save(caps1);
        RigCapabilities caps2 = new RigCapabilities("book,test,bar");
        ses.save(caps2);
        RigCapabilities caps3 = new RigCapabilities("book,baz,foo");
        ses.save(caps3);
        RigCapabilities caps4 = new RigCapabilities("something,different");
        ses.save(caps4);
        RigCapabilities caps5 = new RigCapabilities("diff,again");
        ses.save(caps5);
        RequestCapabilities rcaps1 = new RequestCapabilities("book");
        ses.save(rcaps1);
        RequestCapabilities rcaps2 = new RequestCapabilities("foo");
        ses.save(rcaps2);
        RequestCapabilities rcaps3 = new RequestCapabilities("bar");
        ses.save(rcaps3);
        RequestCapabilities rcaps4 = new RequestCapabilities("orphaned");
        ses.save(rcaps4);
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
        Rig r4 = new Rig();
        r4.setName("bkrig4");
        r4.setRigType(rigType3);
        r4.setLastUpdateTimestamp(new Date());
        r4.setRigCapabilities(caps4);
        ses.save(r4);
        Rig r5 = new Rig();
        r5.setName("bkrig5");
        r5.setRigType(rigType4);
        r5.setLastUpdateTimestamp(new Date());
        r5.setRigCapabilities(caps5);
        ses.save(r5);
        Rig r6 = new Rig();
        r6.setName("bkrig6");
        r6.setRigType(rigType4);
        r6.setLastUpdateTimestamp(new Date());
        r6.setRigCapabilities(caps5);
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
        
        Bookings bk20 = new Bookings();
        bk20.setActive(true);
        bk20.setDuration(4500);
        rcap3tm.add(Calendar.HOUR, 7);
        bk20.setStartTime(rcap3tm.getTime());
        rcap3tm.add(Calendar.HOUR, 1);
        rcap3tm.add(Calendar.MINUTE, 15);
        bk20.setEndTime(rcap3tm.getTime());
        bk20.setResourcePermission(perm1);
        bk20.setResourceType("TYPE");
        bk20.setRigType(rigType3);
        bk20.setUser(us1);
        bk20.setUserName(us1.getName());
        bk20.setUserNamespace(us1.getNamespace());
        ses.save(bk20);
        
        /* Tomorrow. */
        Bookings bk21 = new Bookings();
        bk21.setActive(true);
        bk21.setDuration(4500);
        rcap3tm.add(Calendar.DAY_OF_MONTH, 1);
        bk21.setStartTime(rcap3tm.getTime());
        rcap3tm.add(Calendar.HOUR, 1);
        rcap3tm.add(Calendar.MINUTE, 15);
        bk21.setEndTime(rcap3tm.getTime());
        bk21.setResourcePermission(perm1);
        bk21.setResourceType("TYPE");
        bk21.setRigType(rigType4);
        bk21.setUser(us1);
        bk21.setUserName(us1.getName());
        bk21.setUserNamespace(us1.getNamespace());
        ses.save(bk21);
        
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
        ses.refresh(r4);
        ses.refresh(r5);
        ses.refresh(rigType1);
        ses.refresh(rigType2);
        ses.refresh(rigType3);
        ses.refresh(rigType4);

        this.day.fullLoad(ses);
       
        ses.beginTransaction();
        r6.setRigCapabilities(caps2);
        ses.getTransaction().commit();
        
        this.day.rigRegistered(r6, ses);
        
        ses.beginTransaction();
        ses.delete(bk21);
        ses.delete(bk20);
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
        ses.delete(r4);
        ses.delete(r5);
        ses.delete(r6);
        ses.delete(mat1);
        ses.delete(mat2);
        ses.delete(mat3);
        ses.delete(mat4);
        ses.delete(mat5);
        ses.delete(mat6);
        ses.delete(caps1);
        ses.delete(caps2);
        ses.delete(caps3);
        ses.delete(caps4);
        ses.delete(caps5);
        ses.delete(rcaps1);
        ses.delete(rcaps2);
        ses.delete(rcaps3);
        ses.delete(rcaps4);
        ses.delete(rigType1);
        ses.delete(rigType2);
        ses.delete(rigType3);
        ses.delete(rigType4);
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
        assertTrue(bk20.isActive());
        assertTrue(bk21.isActive());
        
        Field f = DayBookings.class.getDeclaredField("rigBookings");
        f.setAccessible(true);
        Map<String, RigBookings> rbMap = (Map<String, RigBookings>)f.get(this.day);
        
        assertTrue(rbMap.containsKey(r1.getName()));
        assertTrue(rbMap.containsKey(r2.getName()));
        assertTrue(rbMap.containsKey(r3.getName()));
        assertTrue(rbMap.containsKey(r4.getName()));
        assertTrue(rbMap.containsKey(r5.getName()));
        assertTrue(rbMap.containsKey(r6.getName()));
        
        RigBookings rb = rbMap.get(r6.getName());
        assertNotNull(rb);
        assertNotNull(rb.getTypeLoopNext());
        
        f = DayBookings.class.getDeclaredField("typeTargets");
        f.setAccessible(true);
        Map<String, RigBookings> ttMap = (Map<String, RigBookings>)f.get(this.day);
        assertTrue(ttMap.containsKey(rigType1.getName()));
        assertTrue(ttMap.containsKey(rigType2.getName()));
        assertTrue(ttMap.containsKey(rigType3.getName()));
        assertTrue(ttMap.containsKey(rigType4.getName()));
        
        f = DayBookings.class.getDeclaredField("capsTargets");
        f.setAccessible(true);
        Map<String, RigBookings> cpMap = (Map<String, RigBookings>)f.get(this.day);
        
        assertTrue(cpMap.containsKey(rcaps1.getCapabilities()));
        assertTrue(cpMap.containsKey(rcaps2.getCapabilities()));
        assertTrue(cpMap.containsKey(rcaps3.getCapabilities()));
        assertFalse(cpMap.containsKey(rcaps4.getCapabilities()));
        
        assertNotNull(rb.getCapsLoopNext(rcaps3));
        
        RigBookings rb2 = rbMap.get(r2.getName());
        assertEquals(rb.getCapsLoopNext(rcaps3), rb2);
        assertEquals(rb, rb2.getCapsLoopNext(rcaps3));
        
        assertNotNull(rb.getCapsLoopNext(rcaps1));
        assertNull(rb.getCapsLoopNext(rcaps2));
    }
    
    public void testFullLoadTypeNoRig() throws Exception
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
        
        ses.getTransaction().commit();

        ses.refresh(rigType1);

        this.day.fullLoad(ses);
        
        ses.beginTransaction();
        ses.delete(bk4);
        ses.delete(bk5);
        ses.delete(bk13);
        ses.delete(perm1);
        ses.delete(rigType1);
        ses.delete(us1);
        ses.delete(uclass1);
        ses.getTransaction().commit();
        
        assertFalse(bk4.isActive());
        assertNotNull(bk4.getCancelReason());
        assertFalse(bk5.isActive());
        assertNotNull(bk5.getCancelReason());
        assertFalse(bk13.isActive());
        assertNotNull(bk13.getCancelReason());
    }
    

    @SuppressWarnings("unchecked")
    public void testFullLoad() throws Exception
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
        RigType rigType3 = new RigType("unrelatedrigtype", 300, false);
        ses.save(rigType3);
        RigType rigType4 = new RigType("noloadrigtype", 300, false);
        ses.save(rigType4);
        RigCapabilities caps1 = new RigCapabilities("book,test,foo");
        ses.save(caps1);
        RigCapabilities caps2 = new RigCapabilities("book,test,bar");
        ses.save(caps2);
        RigCapabilities caps3 = new RigCapabilities("book,baz,foo");
        ses.save(caps3);
        RigCapabilities caps4 = new RigCapabilities("something,different");
        ses.save(caps4);
        RigCapabilities caps5 = new RigCapabilities("diff,again");
        ses.save(caps5);
        RequestCapabilities rcaps1 = new RequestCapabilities("book");
        ses.save(rcaps1);
        RequestCapabilities rcaps2 = new RequestCapabilities("foo");
        ses.save(rcaps2);
        RequestCapabilities rcaps3 = new RequestCapabilities("bar");
        ses.save(rcaps3);
        RequestCapabilities rcaps4 = new RequestCapabilities("orphaned");
        ses.save(rcaps4);
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
        Rig r4 = new Rig();
        r4.setName("bkrig4");
        r4.setRigType(rigType3);
        r4.setLastUpdateTimestamp(new Date());
        r4.setRigCapabilities(caps4);
        ses.save(r4);
        Rig r5 = new Rig();
        r5.setName("bkrig5");
        r5.setRigType(rigType4);
        r5.setLastUpdateTimestamp(new Date());
        r5.setRigCapabilities(caps5);
        ses.save(r5);
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
        
        Bookings bk20 = new Bookings();
        bk20.setActive(true);
        bk20.setDuration(4500);
        rcap3tm.add(Calendar.HOUR, 7);
        bk20.setStartTime(rcap3tm.getTime());
        rcap3tm.add(Calendar.HOUR, 1);
        rcap3tm.add(Calendar.MINUTE, 15);
        bk20.setEndTime(rcap3tm.getTime());
        bk20.setResourcePermission(perm1);
        bk20.setResourceType("TYPE");
        bk20.setRigType(rigType3);
        bk20.setUser(us1);
        bk20.setUserName(us1.getName());
        bk20.setUserNamespace(us1.getNamespace());
        ses.save(bk20);
        
        /* Tomorrow. */
        Bookings bk21 = new Bookings();
        bk21.setActive(true);
        bk21.setDuration(4500);
        rcap3tm.add(Calendar.DAY_OF_MONTH, 1);
        bk21.setStartTime(rcap3tm.getTime());
        rcap3tm.add(Calendar.HOUR, 1);
        rcap3tm.add(Calendar.MINUTE, 15);
        bk21.setEndTime(rcap3tm.getTime());
        bk21.setResourcePermission(perm1);
        bk21.setResourceType("TYPE");
        bk21.setRigType(rigType4);
        bk21.setUser(us1);
        bk21.setUserName(us1.getName());
        bk21.setUserNamespace(us1.getNamespace());
        ses.save(bk21);
        
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
        ses.refresh(r4);
        ses.refresh(r5);
        ses.refresh(rigType1);
        ses.refresh(rigType2);
        ses.refresh(rigType3);
        ses.refresh(rigType4);

        this.day.fullLoad(ses);
        
        ses.beginTransaction();
        ses.delete(bk21);
        ses.delete(bk20);
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
        ses.delete(r4);
        ses.delete(r5);
        ses.delete(mat1);
        ses.delete(mat2);
        ses.delete(mat3);
        ses.delete(mat4);
        ses.delete(mat5);
        ses.delete(mat6);
        ses.delete(caps1);
        ses.delete(caps2);
        ses.delete(caps3);
        ses.delete(caps4);
        ses.delete(caps5);
        ses.delete(rcaps1);
        ses.delete(rcaps2);
        ses.delete(rcaps3);
        ses.delete(rcaps4);
        ses.delete(rigType1);
        ses.delete(rigType2);
        ses.delete(rigType3);
        ses.delete(rigType4);
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
        assertTrue(bk20.isActive());
        assertTrue(bk21.isActive());
        
        Field f = DayBookings.class.getDeclaredField("rigBookings");
        f.setAccessible(true);
        Map<String, RigBookings> rbMap = (Map<String, RigBookings>)f.get(this.day);
        
        assertTrue(rbMap.containsKey(r1.getName()));
        assertTrue(rbMap.containsKey(r2.getName()));
        assertTrue(rbMap.containsKey(r3.getName()));
        assertTrue(rbMap.containsKey(r4.getName()));
        assertFalse(rbMap.containsKey(r5.getName()));
        
        f = DayBookings.class.getDeclaredField("typeTargets");
        f.setAccessible(true);
        Map<String, RigBookings> ttMap = (Map<String, RigBookings>)f.get(this.day);
        assertTrue(ttMap.containsKey(rigType1.getName()));
        assertTrue(ttMap.containsKey(rigType2.getName()));
        assertTrue(ttMap.containsKey(rigType3.getName()));
        assertFalse(ttMap.containsKey(rigType4.getName()));
        
        f = DayBookings.class.getDeclaredField("capsTargets");
        f.setAccessible(true);
        Map<String, RigBookings> cpMap = (Map<String, RigBookings>)f.get(this.day);
        
        assertTrue(cpMap.containsKey(rcaps1.getCapabilities()));
        assertTrue(cpMap.containsKey(rcaps2.getCapabilities()));
        assertTrue(cpMap.containsKey(rcaps3.getCapabilities()));
        assertFalse(cpMap.containsKey(rcaps4.getCapabilities()));
    }
    
    public void testFullLoadCapsNoRig() throws Exception
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
        RequestCapabilities reqCaps = new RequestCapabilities("hasnot,rigs");
        ses.save(reqCaps);
        ResourcePermission perm1 = new ResourcePermission();
        perm1.setUserClass(uclass1);
        perm1.setType("CAPABILITY");
        perm1.setSessionDuration(3600);
        perm1.setQueueActivityTimeout(300);
        perm1.setAllowedExtensions((short)10);
        perm1.setSessionActivityTimeout(300);
        perm1.setExtensionDuration(300);
        perm1.setMaximumBookings(10);
        perm1.setRequestCapabilities(reqCaps);
        perm1.setStartTime(new Date());
        perm1.setExpiryTime(new Date());
        perm1.setDisplayName("bookperm");
        ses.save(perm1);

        Calendar rt1tm = TimeUtil.getDayBegin(this.dayStr);
        Bookings bk4 = new Bookings();
        bk4.setActive(true);
        bk4.setDuration(1800);
        /* Slots 0 - 1. */
        bk4.setStartTime(rt1tm.getTime());
        rt1tm.add(Calendar.MINUTE, 30);
        bk4.setEndTime(rt1tm.getTime());
        bk4.setResourcePermission(perm1);
        bk4.setResourceType("CAPABILITY");
        bk4.setRequestCapabilities(reqCaps);
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
        bk13.setResourceType("CAPABILITY");
        bk13.setRequestCapabilities(reqCaps);
        bk13.setUser(us1);
        bk13.setUserName(us1.getName());
        bk13.setUserNamespace(us1.getNamespace());
        ses.save(bk13);
        
        ses.getTransaction().commit();


        this.day.fullLoad(ses);
        
        ses.beginTransaction();
        ses.delete(bk4);
        ses.delete(bk13);
        ses.delete(perm1);
        ses.delete(reqCaps);
        ses.delete(us1);
        ses.delete(uclass1);
        ses.getTransaction().commit();
        
        assertFalse(bk4.isActive());
        assertNotNull(bk4.getCancelReason());
        assertFalse(bk13.isActive());
        assertNotNull(bk13.getCancelReason());
    }
    
    public void testGetSlotStartingBookings() throws Exception
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
        perm1.setStartTime(new Date());
        perm1.setExpiryTime(new Date());
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

        this.day.fullLoad(ses);
        
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
        
        Map<String, MBooking> st = this.day.getSlotStartingBookings(0);
        assertNotNull(st);
        assertEquals(2, st.size());
        
        assertTrue(st.containsKey(r1.getName()));
        assertTrue(st.containsKey(r2.getName()));
        assertFalse(st.containsKey(r3.getName()));
        
        for (MBooking m : st.values())
        {
            assertEquals(0, m.getStartSlot());
            assertEquals(BType.TYPE, m.getType());
        }
        
        st = this.day.getSlotStartingBookings(1);
        assertNotNull(st);
        assertEquals(0, st.size());
        
        st = this.day.getSlotStartingBookings(2);
        assertNotNull(st);
        assertEquals(2, st.size());
        assertTrue(st.containsKey(r1.getName()));
        assertFalse(st.containsKey(r2.getName()));
        assertTrue(st.containsKey(r3.getName()));
        
        for (MBooking m : st.values())
        {
            assertEquals(2, m.getStartSlot());
            assertEquals(BType.RIG, m.getType());
        }
        
        st = this.day.getSlotStartingBookings(21);
        assertNotNull(st);
        assertEquals(0, st.size());
        
        st = this.day.getSlotStartingBookings(22);
        assertEquals(1, st.size());
        MBooking m = st.values().iterator().next();
        assertEquals(22, m.getStartSlot());
        assertEquals(BType.CAPABILITY, m.getType());
    }
}

