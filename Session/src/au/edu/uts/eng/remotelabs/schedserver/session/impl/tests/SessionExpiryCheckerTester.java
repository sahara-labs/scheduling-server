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
package au.edu.uts.eng.remotelabs.schedserver.session.impl.tests;


import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserAssociation;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserAssociationId;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClass;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.tests.DataAccessTestSetup;
import au.edu.uts.eng.remotelabs.schedserver.queuer.impl.InnerQueue;
import au.edu.uts.eng.remotelabs.schedserver.queuer.impl.Queue;
import au.edu.uts.eng.remotelabs.schedserver.session.impl.SessionExpiryChecker;

/**
 * Tests the {@link SessionExpiryChecker} class. 
 */
public class SessionExpiryCheckerTester extends TestCase
{
    /** Object of class under test. */
    private SessionExpiryChecker checker;
    
    @Override
    @Before
    public void setUp() throws Exception
    {
        DataAccessTestSetup.setup();
        Queue queue = Queue.getInstance();
        Field f = Queue.class.getDeclaredField("rigQueues");
        f.setAccessible(true);
        f.set(queue, new HashMap<Long, InnerQueue>());
        f = Queue.class.getDeclaredField("typeQueues");
        f.setAccessible(true);
        f.set(queue, new HashMap<Long, InnerQueue>());
        f = Queue.class.getDeclaredField("capabilityQueues");
        f.setAccessible(true);
        f.set(queue, new HashMap<Long, InnerQueue>());

        f = Queue.class.getDeclaredField("notTest");
        f.setAccessible(true);
        f.set(queue, Boolean.FALSE);
        
        this.checker = new SessionExpiryChecker();
        f = SessionExpiryChecker.class.getDeclaredField("notTest");
        f.setAccessible(true);
        f.set(this.checker, Boolean.FALSE);
    }
    
    @Test
    public void testRunGraceNotFinished()
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 10000);
        Date after = new Date(System.currentTimeMillis() + 10000);
        Date now = new Date();
        
        db.beginTransaction();
        User user1 = new User("sesperm1", "testns", "USER");
        db.persist(user1);
        
        UserClass uc1 = new UserClass();
        uc1.setName("sesclass1");
        uc1.setActive(true);
        uc1.setKickable(false);
        db.persist(uc1);
        
        UserAssociation ass1 = new UserAssociation(new UserAssociationId(user1.getId(), uc1.getId()), uc1, user1);
        db.persist(ass1);
        
        RigType rt = new RigType();
        rt.setName("Session_Test_Rig_Type");
        rt.setLogoffGraceDuration(180);
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("session,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Session_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("RIG");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRig(r);
        p1.setSessionDuration(1800);
        p1.setAllowedExtensions((short)3);
        p1.setExtensionDuration(300);
        p1.setSessionActivityTimeout(200);
        p1.setSessionActivityTimeout(200);
        db.persist(p1);
        
        Session ses1 = new Session();
        ses1.setActive(true);
        ses1.setReady(true);
        ses1.setInGrace(true);
        ses1.setActivityLastUpdated(now);
        ses1.setExtensions((short) 3);
        ses1.setPriority((short) 5);
        ses1.setRequestTime(now);
        ses1.setRequestedResourceId(rt.getId());
        ses1.setRequestedResourceName(rt.getName());
        ses1.setResourceType("TYPE");
        ses1.setResourcePermission(p1);
        ses1.setUser(user1);
        ses1.setUserName(user1.getName());
        ses1.setUserNamespace(user1.getNamespace());
        ses1.setAssignedRigName(r.getName());
        ses1.setAssignmentTime(new Date(System.currentTimeMillis() - 1798000));
        ses1.setRig(r);
        ses1.setRemovalReason("Test removal reason.");
        db.persist(ses1);
        db.getTransaction().commit();
        
        short oldExt = ses1.getExtensions();
        this.checker.run();
        
        db.refresh(ses1);
        assertTrue(ses1.isActive());
        assertNull(ses1.getRemovalTime());
        assertEquals(oldExt, ses1.getExtensions());
        
        db.beginTransaction();
        db.delete(ses1);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass1);
        db.delete(uc1);
        db.delete(user1);
        db.getTransaction().commit();
    }
    
    @Test
    public void testRunGraceFinished()
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 10000);
        Date after = new Date(System.currentTimeMillis() + 10000);
        Date now = new Date();
        
        db.beginTransaction();
        User user1 = new User("sesperm1", "testns", "USER");
        db.persist(user1);
        
        UserClass uc1 = new UserClass();
        uc1.setName("sesclass1");
        uc1.setActive(true);
        uc1.setKickable(false);
        db.persist(uc1);
        
        UserAssociation ass1 = new UserAssociation(new UserAssociationId(user1.getId(), uc1.getId()), uc1, user1);
        db.persist(ass1);
        
        RigType rt = new RigType();
        rt.setName("Session_Test_Rig_Type");
        rt.setLogoffGraceDuration(180);
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("session,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Session_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("RIG");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRig(r);
        p1.setSessionDuration(1800);
        p1.setAllowedExtensions((short)3);
        p1.setExtensionDuration(300);
        p1.setSessionActivityTimeout(200);
        db.persist(p1);
        
        Session ses1 = new Session();
        ses1.setActive(true);
        ses1.setReady(true);
        ses1.setInGrace(true);
        ses1.setActivityLastUpdated(now);
        ses1.setExtensions((short) 3);
        ses1.setPriority((short) 5);
        ses1.setRequestTime(now);
        ses1.setRequestedResourceId(rt.getId());
        ses1.setRequestedResourceName(rt.getName());
        ses1.setResourceType("TYPE");
        ses1.setResourcePermission(p1);
        ses1.setUser(user1);
        ses1.setUserName(user1.getName());
        ses1.setUserNamespace(user1.getNamespace());
        ses1.setAssignedRigName(r.getName());
        ses1.setAssignmentTime(new Date(System.currentTimeMillis() - 1801000));
        ses1.setRig(r);
        ses1.setRemovalReason("Test removal reason.");
        db.persist(ses1);
        db.getTransaction().commit();
        
        this.checker.run();
        short oldExt = ses1.getExtensions();
        
        db.refresh(ses1);
        assertFalse(ses1.isActive());
        assertNotNull(ses1.getRemovalTime());
        assertEquals(oldExt, ses1.getExtensions());
        
        db.beginTransaction();
        db.delete(ses1);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass1);
        db.delete(uc1);
        db.delete(user1);
        db.getTransaction().commit(); 
    }
    
    @Test
    public void testRunExtension()
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 10000);
        Date after = new Date(System.currentTimeMillis() + 10000);
        Date now = new Date();
        
        db.beginTransaction();
        User user1 = new User("sesperm1", "testns", "USER");
        db.persist(user1);
        
        UserClass uc1 = new UserClass();
        uc1.setName("sesclass1");
        uc1.setActive(true);
        uc1.setKickable(false);
        db.persist(uc1);
        
        UserAssociation ass1 = new UserAssociation(new UserAssociationId(user1.getId(), uc1.getId()), uc1, user1);
        db.persist(ass1);
        
        RigType rt = new RigType();
        rt.setName("Session_Test_Rig_Type");
        rt.setLogoffGraceDuration(180);
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("session,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Session_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("RIG");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRig(r);
        p1.setSessionDuration(1800);
        p1.setAllowedExtensions((short)3);
        p1.setExtensionDuration(300);
        p1.setSessionActivityTimeout(200);
        db.persist(p1);
        
        Session ses1 = new Session();
        ses1.setActive(true);
        ses1.setReady(true);
        ses1.setInGrace(false);
        ses1.setActivityLastUpdated(now);
        ses1.setExtensions((short) 3);
        ses1.setPriority((short) 5);
        ses1.setRequestTime(now);
        ses1.setRequestedResourceId(rt.getId());
        ses1.setRequestedResourceName(rt.getName());
        ses1.setResourceType("TYPE");
        ses1.setResourcePermission(p1);
        ses1.setUser(user1);
        ses1.setUserName(user1.getName());
        ses1.setUserNamespace(user1.getNamespace());
        ses1.setAssignedRigName(r.getName());
        ses1.setAssignmentTime(new Date(System.currentTimeMillis() - 1799000));
        ses1.setRig(r);
        db.persist(ses1);
        db.getTransaction().commit();
        
        this.checker.run();
        
        db.refresh(ses1);
        assertTrue(ses1.isActive());
        assertFalse(ses1.isInGrace());
        assertNull(ses1.getRemovalTime());
        assertEquals(2, ses1.getExtensions());
        assertNull(ses1.getRemovalReason());
        
        db.beginTransaction();
        db.delete(ses1);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass1);
        db.delete(uc1);
        db.delete(user1);
        db.getTransaction().commit(); 
    }
    
    @Test
    public void testRunExtension2()
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 10000);
        Date after = new Date(System.currentTimeMillis() + 10000);
        Date now = new Date();
        
        db.beginTransaction();
        User user1 = new User("sesperm1", "testns", "USER");
        db.persist(user1);
        
        UserClass uc1 = new UserClass();
        uc1.setName("sesclass1");
        uc1.setActive(true);
        uc1.setKickable(false);
        db.persist(uc1);
        
        UserAssociation ass1 = new UserAssociation(new UserAssociationId(user1.getId(), uc1.getId()), uc1, user1);
        db.persist(ass1);
        
        RigType rt = new RigType();
        rt.setName("Session_Test_Rig_Type");
        rt.setLogoffGraceDuration(180);
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("session,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Session_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("RIG");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRig(r);
        p1.setSessionDuration(1800);
        p1.setAllowedExtensions((short)3);
        p1.setExtensionDuration(300);
        p1.setSessionActivityTimeout(200);
        db.persist(p1);
        
        Session ses1 = new Session();
        ses1.setActive(true);
        ses1.setReady(true);
        ses1.setInGrace(false);
        ses1.setActivityLastUpdated(now);
        ses1.setExtensions((short) 2);
        ses1.setPriority((short) 5);
        ses1.setRequestTime(now);
        ses1.setRequestedResourceId(rt.getId());
        ses1.setRequestedResourceName(rt.getName());
        ses1.setResourceType("TYPE");
        ses1.setResourcePermission(p1);
        ses1.setUser(user1);
        ses1.setUserName(user1.getName());
        ses1.setUserNamespace(user1.getNamespace());
        ses1.setAssignedRigName(r.getName());
        ses1.setAssignmentTime(new Date(System.currentTimeMillis() - 1921000));
        ses1.setRig(r);
        db.persist(ses1);
        db.getTransaction().commit();
        
        this.checker.run();
        
        db.refresh(ses1);
        assertTrue(ses1.isActive());
        assertFalse(ses1.isInGrace());
        assertNull(ses1.getRemovalTime());
        assertEquals(1, ses1.getExtensions());
        assertNull(ses1.getRemovalReason());
        
        db.beginTransaction();
        db.delete(ses1);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass1);
        db.delete(uc1);
        db.delete(user1);
        db.getTransaction().commit(); 
    }
    
    @Test
    public void testRunExtensionNoExt()
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 10000);
        Date after = new Date(System.currentTimeMillis() + 10000);
        Date now = new Date();
        
        db.beginTransaction();
        User user1 = new User("sesperm1", "testns", "USER");
        db.persist(user1);
        
        UserClass uc1 = new UserClass();
        uc1.setName("sesclass1");
        uc1.setActive(true);
        uc1.setKickable(false);
        db.persist(uc1);
        
        UserAssociation ass1 = new UserAssociation(new UserAssociationId(user1.getId(), uc1.getId()), uc1, user1);
        db.persist(ass1);
        
        RigType rt = new RigType();
        rt.setName("Session_Test_Rig_Type");
        rt.setLogoffGraceDuration(180);
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("session,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Session_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("RIG");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRig(r);
        p1.setSessionDuration(1800);
        p1.setAllowedExtensions((short)3);
        p1.setExtensionDuration(300);
        p1.setSessionActivityTimeout(200);
        db.persist(p1);
        
        Session ses1 = new Session();
        ses1.setActive(true);
        ses1.setReady(true);
        ses1.setInGrace(false);
        ses1.setActivityLastUpdated(now);
        ses1.setExtensions((short) 2);
        ses1.setPriority((short) 5);
        ses1.setRequestTime(now);
        ses1.setRequestedResourceId(rt.getId());
        ses1.setRequestedResourceName(rt.getName());
        ses1.setResourceType("TYPE");
        ses1.setResourcePermission(p1);
        ses1.setUser(user1);
        ses1.setUserName(user1.getName());
        ses1.setUserNamespace(user1.getNamespace());
        ses1.setAssignedRigName(r.getName());
        ses1.setAssignmentTime(new Date(System.currentTimeMillis() - 1878000));
        ses1.setRig(r);
        db.persist(ses1);
        db.getTransaction().commit();
        
        this.checker.run();
        
        db.refresh(ses1);
        assertTrue(ses1.isActive());
        assertFalse(ses1.isInGrace());
        assertNull(ses1.getRemovalTime());
        assertEquals(2, ses1.getExtensions());
        assertNull(ses1.getRemovalReason());
        
        db.beginTransaction();
        db.delete(ses1);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass1);
        db.delete(uc1);
        db.delete(user1);
        db.getTransaction().commit(); 
    }
    
    @Test
    public void testRunExtension3()
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 10000);
        Date after = new Date(System.currentTimeMillis() + 10000);
        Date now = new Date();
        
        db.beginTransaction();
        User user1 = new User("sesperm1", "testns", "USER");
        db.persist(user1);
        
        UserClass uc1 = new UserClass();
        uc1.setName("sesclass1");
        uc1.setActive(true);
        uc1.setKickable(false);
        db.persist(uc1);
        
        UserAssociation ass1 = new UserAssociation(new UserAssociationId(user1.getId(), uc1.getId()), uc1, user1);
        db.persist(ass1);
        
        RigType rt = new RigType();
        rt.setName("Session_Test_Rig_Type");
        rt.setLogoffGraceDuration(180);
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("session,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Session_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("RIG");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRig(r);
        p1.setSessionDuration(1800);
        p1.setAllowedExtensions((short)3);
        p1.setExtensionDuration(300);
        p1.setSessionActivityTimeout(200);
        db.persist(p1);
        
        Session ses1 = new Session();
        ses1.setActive(true);
        ses1.setReady(true);
        ses1.setInGrace(false);
        ses1.setActivityLastUpdated(now);
        ses1.setExtensions((short) 1);
        ses1.setPriority((short) 5);
        ses1.setRequestTime(now);
        ses1.setRequestedResourceId(rt.getId());
        ses1.setRequestedResourceName(rt.getName());
        ses1.setResourceType("TYPE");
        ses1.setResourcePermission(p1);
        ses1.setUser(user1);
        ses1.setUserName(user1.getName());
        ses1.setUserNamespace(user1.getNamespace());
        ses1.setAssignedRigName(r.getName());
        ses1.setAssignmentTime(new Date(System.currentTimeMillis() - 2221000));
        ses1.setRig(r);
        db.persist(ses1);
        db.getTransaction().commit();
        
        this.checker.run();
        
        db.refresh(ses1);
        assertTrue(ses1.isActive());
        assertFalse(ses1.isInGrace());
        assertNull(ses1.getRemovalTime());
        assertNull(ses1.getRemovalReason());
        assertEquals(0, ses1.getExtensions());
        
        db.beginTransaction();
        db.delete(ses1);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass1);
        db.delete(uc1);
        db.delete(user1);
        db.getTransaction().commit(); 
    }
    
    @Test
    public void testRunNoExt()
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 10000);
        Date after = new Date(System.currentTimeMillis() + 10000);
        Date now = new Date();
        
        db.beginTransaction();
        User user1 = new User("sesperm1", "testns", "USER");
        db.persist(user1);
        
        UserClass uc1 = new UserClass();
        uc1.setName("sesclass1");
        uc1.setActive(true);
        uc1.setKickable(false);
        db.persist(uc1);
        
        UserAssociation ass1 = new UserAssociation(new UserAssociationId(user1.getId(), uc1.getId()), uc1, user1);
        db.persist(ass1);
        
        RigType rt = new RigType();
        rt.setName("Session_Test_Rig_Type");
        rt.setLogoffGraceDuration(180);
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("session,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Session_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("RIG");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRig(r);
        p1.setSessionDuration(1800);
        p1.setAllowedExtensions((short)3);
        p1.setExtensionDuration(300);
        p1.setSessionActivityTimeout(200);
        db.persist(p1);
        
        Session ses1 = new Session();
        ses1.setActive(true);
        ses1.setReady(true);
        ses1.setInGrace(false);
        ses1.setActivityLastUpdated(now);
        ses1.setExtensions((short) 0);
        ses1.setPriority((short) 5);
        ses1.setRequestTime(now);
        ses1.setRequestedResourceId(rt.getId());
        ses1.setRequestedResourceName(rt.getName());
        ses1.setResourceType("TYPE");
        ses1.setResourcePermission(p1);
        ses1.setUser(user1);
        ses1.setUserName(user1.getName());
        ses1.setUserNamespace(user1.getNamespace());
        ses1.setAssignedRigName(r.getName());
        ses1.setAssignmentTime(new Date(System.currentTimeMillis() - 2521000));
        ses1.setRig(r);
        db.persist(ses1);
        db.getTransaction().commit();
        
        this.checker.run();
        
        db.refresh(ses1);
        assertTrue(ses1.isActive());
        assertTrue(ses1.isInGrace());
        assertNotNull(ses1.getRemovalReason());
        assertNull(ses1.getRemovalTime());
        assertEquals(0, ses1.getExtensions());
        
        db.beginTransaction();
        db.delete(ses1);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass1);
        db.delete(uc1);
        db.delete(user1);
        db.getTransaction().commit(); 
    }
    
    @Test
    public void testRunNoExtQueued()
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 10000);
        Date after = new Date(System.currentTimeMillis() + 10000);
        Date now = new Date();
        
        db.beginTransaction();
        User user1 = new User("sesperm1", "testns", "USER");
        db.persist(user1);
        User user2 = new User("sesperm2", "testns", "USER");
        db.persist(user2);
        
        UserClass uc1 = new UserClass();
        uc1.setName("sesclass1");
        uc1.setActive(true);
        uc1.setKickable(false);
        db.persist(uc1);
        
        UserAssociation ass1 = new UserAssociation(new UserAssociationId(user1.getId(), uc1.getId()), uc1, user1);
        db.persist(ass1);
        UserAssociation ass2 = new UserAssociation(new UserAssociationId(user2.getId(), uc1.getId()), uc1, user2);
        db.persist(ass2);
        
        RigType rt = new RigType();
        rt.setName("Session_Test_Rig_Type");
        rt.setLogoffGraceDuration(180);
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("session,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Session_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("RIG");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRig(r);
        p1.setSessionDuration(1800);
        p1.setAllowedExtensions((short)3);
        p1.setExtensionDuration(300);
        p1.setSessionActivityTimeout(200);
        db.persist(p1);
        
        Session ses1 = new Session();
        ses1.setActive(true);
        ses1.setReady(true);
        ses1.setInGrace(false);
        ses1.setActivityLastUpdated(now);
        ses1.setExtensions((short) 2);
        ses1.setPriority((short) 5);
        ses1.setRequestTime(now);
        ses1.setRequestedResourceId(rt.getId());
        ses1.setRequestedResourceName(rt.getName());
        ses1.setResourceType("TYPE");
        ses1.setResourcePermission(p1);
        ses1.setUser(user1);
        ses1.setUserName(user1.getName());
        ses1.setUserNamespace(user1.getNamespace());
        ses1.setAssignedRigName(r.getName());
        ses1.setAssignmentTime(new Date(System.currentTimeMillis() - 2521000));
        ses1.setRig(r);
        db.persist(ses1);
        
        Session ses2 = new Session();
        ses2.setActive(true);
        ses2.setReady(false);
        ses2.setInGrace(false);
        ses2.setActivityLastUpdated(now);
        ses2.setExtensions((short) 3);
        ses2.setPriority((short) 5);
        ses2.setRequestTime(now);
        ses2.setRequestedResourceId(rt.getId());
        ses2.setRequestedResourceName(rt.getName());
        ses2.setResourceType("TYPE");
        ses2.setResourcePermission(p1);
        ses2.setUser(user2);
        ses2.setUserName(user2.getName());
        ses2.setUserNamespace(user2.getNamespace());
        db.persist(ses2);
        
        db.getTransaction().commit();
        
        Queue.getInstance().addEntry(ses2, db);
        
        this.checker.run();
        
        db.refresh(ses1);
        assertTrue(ses1.isActive());
        assertTrue(ses1.isInGrace());
        assertNotNull(ses1.getRemovalReason());
        assertNull(ses1.getRemovalTime());
        assertEquals(2, ses1.getExtensions());
        
        db.beginTransaction();
        db.delete(ses1);
        db.delete(ses2);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass1);
        db.delete(ass2);
        db.delete(uc1);
        db.delete(user1);
        db.delete(user2);
        db.getTransaction().commit(); 
    }
    
    @Test
    public void testRunKickable()
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 10000);
        Date after = new Date(System.currentTimeMillis() + 10000);
        Date now = new Date();
        
        db.beginTransaction();
        User user1 = new User("sesperm1", "testns", "USER");
        db.persist(user1);
        User user2 = new User("sesperm2", "testns", "USER");
        db.persist(user2);
        
        UserClass uc1 = new UserClass();
        uc1.setName("sesclass1");
        uc1.setActive(true);
        uc1.setKickable(true);
        db.persist(uc1);
        
        UserAssociation ass1 = new UserAssociation(new UserAssociationId(user1.getId(), uc1.getId()), uc1, user1);
        db.persist(ass1);
        UserAssociation ass2 = new UserAssociation(new UserAssociationId(user2.getId(), uc1.getId()), uc1, user2);
        db.persist(ass2);
        
        RigType rt = new RigType();
        rt.setName("Session_Test_Rig_Type");
        rt.setLogoffGraceDuration(180);
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("session,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Session_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("RIG");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRig(r);
        p1.setSessionDuration(1800);
        p1.setAllowedExtensions((short)3);
        p1.setExtensionDuration(300);
        p1.setSessionActivityTimeout(200);
        db.persist(p1);
        
        Session ses1 = new Session();
        ses1.setActive(true);
        ses1.setReady(true);
        ses1.setInGrace(false);
        ses1.setActivityLastUpdated(now);
        ses1.setExtensions((short) 3);
        ses1.setPriority((short) 5);
        ses1.setRequestTime(now);
        ses1.setRequestedResourceId(rt.getId());
        ses1.setRequestedResourceName(rt.getName());
        ses1.setResourceType("TYPE");
        ses1.setResourcePermission(p1);
        ses1.setUser(user1);
        ses1.setUserName(user1.getName());
        ses1.setUserNamespace(user1.getNamespace());
        ses1.setAssignedRigName(r.getName());
        ses1.setAssignmentTime(new Date(System.currentTimeMillis() - 3000));
        ses1.setRig(r);
        db.persist(ses1);
        
        Session ses2 = new Session();
        ses2.setActive(true);
        ses2.setReady(false);
        ses2.setInGrace(false);
        ses2.setActivityLastUpdated(now);
        ses2.setExtensions((short) 3);
        ses2.setPriority((short) 5);
        ses2.setRequestTime(now);
        ses2.setRequestedResourceId(rt.getId());
        ses2.setRequestedResourceName(rt.getName());
        ses2.setResourceType("TYPE");
        ses2.setResourcePermission(p1);
        ses2.setUser(user2);
        ses2.setUserName(user2.getName());
        ses2.setUserNamespace(user2.getNamespace());
        db.persist(ses2);
        
        db.getTransaction().commit();
        
        Queue.getInstance().addEntry(ses2, db);
        
        this.checker.run();
        
        db.refresh(ses1);
        assertFalse(ses1.isActive());
        assertFalse(ses1.isInGrace());
        assertNotNull(ses1.getRemovalReason());
        assertNotNull(ses1.getRemovalTime());
        assertEquals(3, ses1.getExtensions());
        
        db.beginTransaction();
        db.delete(ses1);
        db.delete(ses2);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass1);
        db.delete(ass2);
        db.delete(uc1);
        db.delete(user1);
        db.delete(user2);
        db.getTransaction().commit(); 
    }
    
    @Test
    public void testRunKickableNoKick()
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 10000);
        Date after = new Date(System.currentTimeMillis() + 10000);
        Date now = new Date();
        
        db.beginTransaction();
        User user1 = new User("sesperm1", "testns", "USER");
        db.persist(user1);
        
        UserClass uc1 = new UserClass();
        uc1.setName("sesclass1");
        uc1.setActive(true);
        uc1.setKickable(true);
        db.persist(uc1);
        
        UserAssociation ass1 = new UserAssociation(new UserAssociationId(user1.getId(), uc1.getId()), uc1, user1);
        db.persist(ass1);

        RigType rt = new RigType();
        rt.setName("Session_Test_Rig_Type");
        rt.setLogoffGraceDuration(180);
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("session,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Session_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("RIG");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRig(r);
        p1.setSessionDuration(1800);
        p1.setAllowedExtensions((short)3);
        p1.setExtensionDuration(300);
        p1.setSessionActivityTimeout(200);
        db.persist(p1);
        
        Session ses1 = new Session();
        ses1.setActive(true);
        ses1.setReady(true);
        ses1.setInGrace(false);
        ses1.setActivityLastUpdated(now);
        ses1.setExtensions((short) 3);
        ses1.setPriority((short) 5);
        ses1.setRequestTime(now);
        ses1.setRequestedResourceId(rt.getId());
        ses1.setRequestedResourceName(rt.getName());
        ses1.setResourceType("TYPE");
        ses1.setResourcePermission(p1);
        ses1.setUser(user1);
        ses1.setUserName(user1.getName());
        ses1.setUserNamespace(user1.getNamespace());
        ses1.setAssignedRigName(r.getName());
        ses1.setAssignmentTime(new Date(System.currentTimeMillis() - 3000));
        ses1.setRig(r);
        db.persist(ses1);
        
        db.getTransaction().commit();
        
        
        this.checker.run();
        
        db.refresh(ses1);
        assertTrue(ses1.isActive());
        assertFalse(ses1.isInGrace());
        assertNull(ses1.getRemovalReason());
        assertNull(ses1.getRemovalTime());
        assertEquals(3, ses1.getExtensions());
        
        db.beginTransaction();
        db.delete(ses1);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass1);
        db.delete(uc1);
        db.delete(user1);
        db.getTransaction().commit(); 
    }
}
