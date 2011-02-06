/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2009 - 2011, University of Technology, Sydney
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
 * @date 6th February 2011
 */
package au.edu.uts.eng.remotelabs.schedserver.rigmanagement.impl.tests;


import java.lang.reflect.Field;
import java.util.Date;

import junit.framework.TestCase;

import org.junit.Before;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigLogDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigLog;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigOfflineSchedule;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClass;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.testsetup.DataAccessTestSetup;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.impl.RigMaintenanceNotifier;

/**
 * Tests the {@link RigMaintenanceNotifier} class.
 */
public class RigMaintenanceNotifierTester extends TestCase
{
    /** Object of class under test. */
    private RigMaintenanceNotifier notifier;


    @Override
    @Before
    public void setUp() throws Exception
    {
        DataAccessTestSetup.setup();
        
        this.notifier = new RigMaintenanceNotifier();
        
        Field f = RigMaintenanceNotifier.class.getDeclaredField("notTest");
        f.setAccessible(true);
        f.set(this.notifier, false);
    }

    public void testRun()
    {
        /* Nothing exciting should happen. */
        this.notifier.run();
    }
    
    public void testRunTwoActive()
    {
        Date now = new Date();
        Date soon = new Date(System.currentTimeMillis() + 30000);
        Date past = new Date(System.currentTimeMillis() - 30000);
        Date future = new Date(System.currentTimeMillis() + 120000);
        
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        db.beginTransaction();
        RigType ty1 = new RigType();
        ty1.setName("notiftest");
        db.save(ty1);
        RigCapabilities caps = new RigCapabilities("test,notif,time");
        db.save(caps);
        Rig r1 = new Rig();
        r1.setActive(true);
        r1.setOnline(true);
        r1.setName("r1notif");
        r1.setRigCapabilities(caps);
        r1.setRigType(ty1);
        r1.setLastUpdateTimestamp(now);
        db.save(r1);
        Rig r2 = new Rig();
        r2.setActive(true);
        r2.setOnline(true);
        r2.setName("r2notif");
        r2.setRigCapabilities(caps);
        r2.setRigType(ty1);
        r2.setLastUpdateTimestamp(past);
        db.save(r2);
        Rig r3 = new Rig();
        r3.setActive(false);
        r3.setOnline(false);
        r3.setName("r3notif");
        r3.setRigCapabilities(caps);
        r3.setRigType(ty1);
        r3.setLastUpdateTimestamp(now);
        db.save(r3);
        RigOfflineSchedule sch1 = new RigOfflineSchedule();
        sch1.setActive(true);
        sch1.setStartTime(soon);
        sch1.setEndTime(future);
        sch1.setReason("is offline");
        sch1.setRig(r1);
        db.save(sch1);
        RigOfflineSchedule sch2 = new RigOfflineSchedule();
        sch2.setActive(false);
        sch2.setStartTime(now);
        sch2.setEndTime(future);
        sch2.setReason("is offline");
        sch2.setRig(r2);
        db.save(sch2);
        RigOfflineSchedule sch3 = new RigOfflineSchedule();
        sch3.setActive(true);
        sch3.setStartTime(now);
        sch3.setEndTime(future);
        sch3.setReason("is offline");
        sch3.setRig(r3);
        db.save(sch3);
        db.getTransaction().commit();
        
        this.notifier.run();
        
        db.refresh(r1);
        db.refresh(r2);
        db.refresh(r3);
        
        RigLogDao rldao = new RigLogDao(db);
        db.beginTransaction();
        for (RigLog log : rldao.findLogs(r1, past, future)) db.delete(log);
        for (RigLog log : rldao.findLogs(r2, past, future)) db.delete(log);
        for (RigLog log : rldao.findLogs(r3, past, future)) db.delete(log);
        db.getTransaction().commit();
        
        db.beginTransaction();
        db.delete(sch3);
        db.delete(sch2);
        db.delete(sch1);
        db.delete(r1);
        db.delete(r2);
        db.delete(r3);
        db.delete(caps);
        db.delete(ty1);
        db.getTransaction().commit();
        
        assertTrue(r1.isActive());
        assertFalse(r1.isOnline());
        assertNotNull(r1.getOfflineReason());
        
        assertTrue(r2.isActive());
        assertTrue(r2.isOnline());
        assertNull(r2.getOfflineReason());
        
        assertFalse(r3.isActive());
        assertFalse(r3.isOnline());
        assertNull(r3.getOfflineReason());
    }
    
    public void testRunSession()
    {
        Date now = new Date();
        Date soon = new Date(System.currentTimeMillis() + 30000);
        Date past = new Date(System.currentTimeMillis() - 30000);
        Date future = new Date(System.currentTimeMillis() + 120000);
        
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        db.beginTransaction();
        RigType ty1 = new RigType();
        ty1.setName("notiftest");
        db.save(ty1);
        RigCapabilities caps = new RigCapabilities("test,notif,time");
        db.save(caps);
        Rig r1 = new Rig();
        r1.setActive(true);
        r1.setOnline(true);
        r1.setName("r1notif");
        r1.setRigCapabilities(caps);
        r1.setRigType(ty1);
        r1.setLastUpdateTimestamp(now);
        db.save(r1);
        Rig r2 = new Rig();
        r2.setActive(true);
        r2.setOnline(true);
        r2.setName("r2notif");
        r2.setRigCapabilities(caps);
        r2.setRigType(ty1);
        r2.setLastUpdateTimestamp(past);
        db.save(r2);
        Rig r3 = new Rig();
        r3.setActive(false);
        r3.setOnline(false);
        r3.setName("r3notif");
        r3.setRigCapabilities(caps);
        r3.setRigType(ty1);
        r3.setLastUpdateTimestamp(now);
        db.save(r3);
        RigOfflineSchedule sch1 = new RigOfflineSchedule();
        sch1.setActive(true);
        sch1.setStartTime(soon);
        sch1.setEndTime(future);
        sch1.setReason("is offline");
        sch1.setRig(r1);
        db.save(sch1);
        RigOfflineSchedule sch2 = new RigOfflineSchedule();
        sch2.setActive(false);
        sch2.setStartTime(now);
        sch2.setEndTime(future);
        sch2.setReason("is offline");
        sch2.setRig(r2);
        db.save(sch2);
        RigOfflineSchedule sch3 = new RigOfflineSchedule();
        sch3.setActive(true);
        sch3.setStartTime(now);
        sch3.setEndTime(future);
        sch3.setReason("is offline");
        sch3.setRig(r3);
        db.save(sch3);
        UserClass uc = new UserClass();
        uc.setName("ucnotif");
        db.save(uc);
        ResourcePermission rp = new ResourcePermission();
        rp.setExpiryTime(now);
        rp.setStartTime(now);
        rp.setType("TYPE");
        rp.setRigType(ty1);
        rp.setUserClass(uc);
        db.save(rp);
        User us1 = new User();
        us1.setName("notifyu");
        us1.setNamespace("notifns");
        us1.setPersona("USER");
        db.save(us1);
        Session ses = new Session();
        ses.setActive(true);
        ses.setActivityLastUpdated(now);
        ses.setRequestedResourceName(r1.getName());
        ses.setResourceType("RIG");
        ses.setResourcePermission(rp);
        ses.setRequestTime(now);
        ses.setUser(us1);
        ses.setUserName(us1.getName());
        ses.setUserNamespace(us1.getNamespace());
        ses.setRig(r1);
        db.save(ses);
        db.getTransaction().commit();
        
        r1.setInSession(true);
        r1.setSession(ses);
        db.beginTransaction();
        db.flush();
        db.getTransaction().commit();
        
        this.notifier.run();
        
        db.refresh(r1);
        db.refresh(r2);
        db.refresh(r3);
        db.refresh(ses);
        
        RigLogDao rldao = new RigLogDao(db);
        db.beginTransaction();
        for (RigLog log : rldao.findLogs(r1, past, future)) db.delete(log);
        for (RigLog log : rldao.findLogs(r2, past, future)) db.delete(log);
        for (RigLog log : rldao.findLogs(r3, past, future)) db.delete(log);
        db.getTransaction().commit();
        
        db.beginTransaction();
        db.delete(ses);
        db.delete(rp);
        db.delete(us1);
        db.delete(uc);
        db.delete(sch3);
        db.delete(sch2);
        db.delete(sch1);
        db.delete(r1);
        db.delete(r2);
        db.delete(r3);
        db.delete(caps);
        db.delete(ty1);
        db.getTransaction().commit();
        
        assertTrue(r1.isActive());
        assertFalse(r1.isOnline());
        assertNotNull(r1.getOfflineReason());
        
        assertFalse(ses.isActive());
        assertNotNull(ses.getRemovalReason());
        assertNotNull(ses.getRemovalTime());
        
        assertTrue(r2.isActive());
        assertTrue(r2.isOnline());
        assertNull(r2.getOfflineReason());
        
        assertFalse(r3.isActive());
        assertFalse(r3.isOnline());
        assertNull(r3.getOfflineReason());
    }
    
    public void testRunEnd()
    {
        Date now = new Date();
        Date past = new Date(System.currentTimeMillis() - 30000);
        Date future = new Date(System.currentTimeMillis() + 120000);
        
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        db.beginTransaction();
        RigType ty1 = new RigType();
        ty1.setName("notiftest");
        db.save(ty1);
        RigCapabilities caps = new RigCapabilities("test,notif,time");
        db.save(caps);
        Rig r1 = new Rig();
        r1.setActive(true);
        r1.setOnline(true);
        r1.setName("r1notif");
        r1.setRigCapabilities(caps);
        r1.setRigType(ty1);
        r1.setLastUpdateTimestamp(now);
        db.save(r1);
        Rig r2 = new Rig();
        r2.setActive(true);
        r2.setOnline(true);
        r2.setName("r2notif");
        r2.setRigCapabilities(caps);
        r2.setRigType(ty1);
        r2.setLastUpdateTimestamp(past);
        db.save(r2);
        Rig r3 = new Rig();
        r3.setActive(false);
        r3.setOnline(false);
        r3.setName("r3notif");
        r3.setRigCapabilities(caps);
        r3.setRigType(ty1);
        r3.setLastUpdateTimestamp(now);
        db.save(r3);
        RigOfflineSchedule sch1 = new RigOfflineSchedule();
        sch1.setActive(true);
        sch1.setStartTime(past);
        sch1.setEndTime(now);
        sch1.setReason("is offline");
        sch1.setRig(r1);
        db.save(sch1);
        RigOfflineSchedule sch2 = new RigOfflineSchedule();
        sch2.setActive(false);
        sch2.setStartTime(past);
        sch2.setEndTime(now);
        sch2.setReason("is offline");
        sch2.setRig(r2);
        db.save(sch2);
        RigOfflineSchedule sch3 = new RigOfflineSchedule();
        sch3.setActive(true);
        sch3.setStartTime(past);
        sch3.setEndTime(now);
        sch3.setReason("is offline");
        sch3.setRig(r3);
        db.save(sch3);
        db.getTransaction().commit();
        
        this.notifier.run();
        
        db.refresh(r1);
        db.refresh(r2);
        db.refresh(r3);
        db.refresh(sch1);
        db.refresh(sch2);
        db.refresh(sch3);
        
        RigLogDao rldao = new RigLogDao(db);
        db.beginTransaction();
        for (RigLog log : rldao.findLogs(r1, past, future)) db.delete(log);
        for (RigLog log : rldao.findLogs(r2, past, future)) db.delete(log);
        for (RigLog log : rldao.findLogs(r3, past, future)) db.delete(log);
        db.getTransaction().commit();
        
        db.beginTransaction();
        db.delete(sch3);
        db.delete(sch2);
        db.delete(sch1);
        db.delete(r1);
        db.delete(r2);
        db.delete(r3);
        db.delete(caps);
        db.delete(ty1);
        db.getTransaction().commit();
        
        assertFalse(sch1.isActive());
        assertFalse(sch2.isActive());
        assertFalse(sch3.isActive());
    }
}
