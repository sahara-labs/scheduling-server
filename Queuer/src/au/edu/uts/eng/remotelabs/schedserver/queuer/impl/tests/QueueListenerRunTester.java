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
 * @date 6th April 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.queuer.impl.tests;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import junit.framework.TestCase;

import org.hibernate.cfg.AnnotationConfiguration;
import org.junit.Before;
import org.junit.Test;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.AcademicPermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Config;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.MatchingCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.MatchingCapabilitiesId;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RequestCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserAssociation;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserAssociationId;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClass;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserLock;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.logger.impl.SystemErrLogger;
import au.edu.uts.eng.remotelabs.schedserver.queuer.impl.InnerQueue;
import au.edu.uts.eng.remotelabs.schedserver.queuer.impl.Queue;
import au.edu.uts.eng.remotelabs.schedserver.queuer.impl.QueueListenerRun;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.RigEventListener;

/**
 * Tests the {@link QueueListenerRun} class.
 */
public class QueueListenerRunTester extends TestCase
{
    /** Object of class under test. */
    private QueueListenerRun listener;
    
    public QueueListenerRunTester(String name) throws Exception
    {
        super(name);
        
        /* Set up the logger. */
        Field f = LoggerActivator.class.getDeclaredField("logger");
        f.setAccessible(true);
        f.set(null, new SystemErrLogger());
        
        /* Set up the SessionFactory. */
        AnnotationConfiguration cfg = new AnnotationConfiguration();
        Properties props = new Properties();
        props.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        props.setProperty("hibernate.connection.url", "jdbc:mysql://127.0.0.1:3306/sahara");
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLInnoDBDialect");
        props.setProperty("hibernate.connection.username", "sahara");
        props.setProperty("hibernate.connection.password", "saharapasswd");
        props.setProperty("hibernate.show_sql", "true");
        props.setProperty("hibernate.format_sql", "true");
        props.setProperty("hibernate.use_sql_comments", "true");
        props.setProperty("hibernate.generate_statistics", "false");
        cfg.setProperties(props);
        cfg.addAnnotatedClass(AcademicPermission.class);
        cfg.addAnnotatedClass(Config.class);
        cfg.addAnnotatedClass(MatchingCapabilities.class);
        cfg.addAnnotatedClass(MatchingCapabilitiesId.class);
        cfg.addAnnotatedClass(RequestCapabilities.class);
        cfg.addAnnotatedClass(ResourcePermission.class);
        cfg.addAnnotatedClass(Rig.class);
        cfg.addAnnotatedClass(RigCapabilities.class);
        cfg.addAnnotatedClass(RigType.class);
        cfg.addAnnotatedClass(au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session.class);
        cfg.addAnnotatedClass(User.class);
        cfg.addAnnotatedClass(UserAssociation.class);
        cfg.addAnnotatedClass(UserAssociationId.class);
        cfg.addAnnotatedClass(UserClass.class);
        cfg.addAnnotatedClass(UserLock.class);
        
        f = DataAccessActivator.class.getDeclaredField("sessionFactory");
        f.setAccessible(true);
        f.set(null, cfg.buildSessionFactory());
    }

    @Override
    @Before
    public void setUp() throws Exception
    {
        this.listener = new QueueListenerRun();
        
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
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testEventOccurredOnline() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 10000);
        Date after = new Date(System.currentTimeMillis() + 10000);
        Date now = new Date();
        
        db.beginTransaction();
        User user1 = new User("qperm1", "testns", "USER");
        db.persist(user1);
        
        UserClass uc1 = new UserClass();
        uc1.setName("uc1");
        uc1.setActive(true);
        db.persist(uc1);
        
        UserAssociation ass1 = new UserAssociation(new UserAssociationId(user1.getId(), uc1.getId()), uc1, user1);
        db.persist(ass1);
        
        RigType rt = new RigType();
        rt.setName("QR_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("qr,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("QR_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(false);
        r.setInSession(false);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("RIG");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRig(r);
        db.persist(p1);
        
        Session ses1 = new Session();
        ses1.setActive(true);
        ses1.setReady(true);
        ses1.setActivityLastUpdated(now);
        ses1.setExtensions((short) 5);
        ses1.setPriority((short) 5);
        ses1.setRequestTime(now);
        ses1.setRequestedResourceId(r.getId());
        ses1.setRequestedResourceName(r.getName());
        ses1.setResourceType("RIG");
        ses1.setResourcePermission(p1);
        ses1.setUser(user1);
        ses1.setUserName(user1.getName());
        ses1.setUserNamespace(user1.getNamespace());
        db.persist(ses1);
        
        db.getTransaction().commit();
        
        Queue.getInstance().addEntry(ses1, db);
        
        r.setOnline(true);
        r.setInSession(false);
        db.beginTransaction();
        db.flush();
        db.getTransaction().commit();
        
        this.listener.eventOccurred(RigEventListener.RigStateChangeEvent.ONLINE, r, db);
        
        db.refresh(ses1);
        db.refresh(r);

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
        
        Field f = Queue.class.getDeclaredField("rigQueues");
        f.setAccessible(true);
        Map<Long, InnerQueue> que = (Map<Long, InnerQueue>) f.get(Queue.getInstance());
        assertNotNull(que);
        assertEquals(0, que.size());
 
        assertTrue(r.isInSession());
        assertNotNull(ses1.getAssignmentTime());
        assertEquals(r.getId(), ses1.getRig().getId());
        assertEquals(r.getName(), ses1.getAssignedRigName());
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testEventOccurredRegistered() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 10000);
        Date after = new Date(System.currentTimeMillis() + 10000);
        Date now = new Date();
        
        db.beginTransaction();
        User user1 = new User("qperm1", "testns", "USER");
        db.persist(user1);
        
        UserClass uc1 = new UserClass();
        uc1.setName("uc1");
        uc1.setActive(true);
        db.persist(uc1);
        
        UserAssociation ass1 = new UserAssociation(new UserAssociationId(user1.getId(), uc1.getId()), uc1, user1);
        db.persist(ass1);
        
        RigType rt = new RigType();
        rt.setName("QR_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("qr,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("QR_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(false);
        r.setInSession(false);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("RIG");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRig(r);
        db.persist(p1);
        
        Session ses1 = new Session();
        ses1.setActive(true);
        ses1.setReady(true);
        ses1.setActivityLastUpdated(now);
        ses1.setExtensions((short) 5);
        ses1.setPriority((short) 5);
        ses1.setRequestTime(now);
        ses1.setRequestedResourceId(r.getId());
        ses1.setRequestedResourceName(r.getName());
        ses1.setResourceType("RIG");
        ses1.setResourcePermission(p1);
        ses1.setUser(user1);
        ses1.setUserName(user1.getName());
        ses1.setUserNamespace(user1.getNamespace());
        db.persist(ses1);
        
        db.getTransaction().commit();
        
        Queue.getInstance().addEntry(ses1, db);
        
        r.setOnline(true);
        r.setInSession(false);
        db.beginTransaction();
        db.flush();
        db.getTransaction().commit();
        
        this.listener.eventOccurred(RigEventListener.RigStateChangeEvent.REGISTERED, r, db);
        
        db.refresh(ses1);
        db.refresh(r);

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
        
        Field f = Queue.class.getDeclaredField("rigQueues");
        f.setAccessible(true);
        Map<Long, InnerQueue> que = (Map<Long, InnerQueue>) f.get(Queue.getInstance());
        assertNotNull(que);
        assertEquals(0, que.size());
 
        assertTrue(r.isInSession());
        assertNotNull(ses1.getAssignmentTime());
        assertEquals(r.getId(), ses1.getRig().getId());
        assertEquals(r.getName(), ses1.getAssignedRigName());
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testEventOccurredOffline() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 10000);
        Date after = new Date(System.currentTimeMillis() + 10000);
        Date now = new Date();
        
        db.beginTransaction();
        User user1 = new User("qperm1", "testns", "USER");
        db.persist(user1);
        
        UserClass uc1 = new UserClass();
        uc1.setName("uc1");
        uc1.setActive(true);
        db.persist(uc1);
        
        UserAssociation ass1 = new UserAssociation(new UserAssociationId(user1.getId(), uc1.getId()), uc1, user1);
        db.persist(ass1);
        
        RigType rt = new RigType();
        rt.setName("QR_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("qr,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("QR_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(false);
        r.setInSession(false);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("RIG");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRig(r);
        db.persist(p1);
        
        Session ses1 = new Session();
        ses1.setActive(true);
        ses1.setReady(true);
        ses1.setActivityLastUpdated(now);
        ses1.setExtensions((short) 5);
        ses1.setPriority((short) 5);
        ses1.setRequestTime(now);
        ses1.setRequestedResourceId(r.getId());
        ses1.setRequestedResourceName(r.getName());
        ses1.setResourceType("RIG");
        ses1.setResourcePermission(p1);
        ses1.setUser(user1);
        ses1.setUserName(user1.getName());
        ses1.setUserNamespace(user1.getNamespace());
        db.persist(ses1);
        
        db.getTransaction().commit();
        
        Queue.getInstance().addEntry(ses1, db);
        
        this.listener.eventOccurred(RigEventListener.RigStateChangeEvent.OFFLINE, r, db);
        
        db.refresh(ses1);
        db.refresh(r);

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
        
        Field f = Queue.class.getDeclaredField("rigQueues");
        f.setAccessible(true);
        Map<Long, InnerQueue> que = (Map<Long, InnerQueue>) f.get(Queue.getInstance());
        assertNotNull(que);
        assertEquals(1, que.size());
 
        assertFalse(r.isInSession());
        assertNull(ses1.getAssignmentTime());
        assertNull(ses1.getRig());
        assertNull(ses1.getAssignedRigName());
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testEventOccurredRemoved() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 10000);
        Date after = new Date(System.currentTimeMillis() + 10000);
        Date now = new Date();
        
        db.beginTransaction();
        User user1 = new User("qperm1", "testns", "USER");
        db.persist(user1);
        
        UserClass uc1 = new UserClass();
        uc1.setName("uc1");
        uc1.setActive(true);
        db.persist(uc1);
        
        UserAssociation ass1 = new UserAssociation(new UserAssociationId(user1.getId(), uc1.getId()), uc1, user1);
        db.persist(ass1);
        
        RigType rt = new RigType();
        rt.setName("QR_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("qr,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("QR_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(false);
        r.setInSession(false);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("RIG");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRig(r);
        db.persist(p1);
        
        Session ses1 = new Session();
        ses1.setActive(true);
        ses1.setReady(true);
        ses1.setActivityLastUpdated(now);
        ses1.setExtensions((short) 5);
        ses1.setPriority((short) 5);
        ses1.setRequestTime(now);
        ses1.setRequestedResourceId(r.getId());
        ses1.setRequestedResourceName(r.getName());
        ses1.setResourceType("RIG");
        ses1.setResourcePermission(p1);
        ses1.setUser(user1);
        ses1.setUserName(user1.getName());
        ses1.setUserNamespace(user1.getNamespace());
        db.persist(ses1);
        
        db.getTransaction().commit();
        
        Queue.getInstance().addEntry(ses1, db);
        
        this.listener.eventOccurred(RigEventListener.RigStateChangeEvent.REMOVED, r, db);
        
        db.refresh(ses1);
        db.refresh(r);

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
        
        Field f = Queue.class.getDeclaredField("rigQueues");
        f.setAccessible(true);
        Map<Long, InnerQueue> que = (Map<Long, InnerQueue>) f.get(Queue.getInstance());
        assertNotNull(que);
        assertEquals(1, que.size());
 
        assertFalse(r.isInSession());
        assertNull(ses1.getAssignmentTime());
        assertNull(ses1.getRig());
        assertNull(ses1.getAssignedRigName());
    }
}
