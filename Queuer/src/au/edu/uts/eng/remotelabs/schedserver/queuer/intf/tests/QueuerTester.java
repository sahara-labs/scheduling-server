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
 * @date 28th March 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.queuer.intf.tests;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Properties;

import junit.framework.TestCase;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.hibernate.cfg.AnnotationConfiguration;
import org.junit.Before;
import org.junit.Test;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RequestCapabilitiesDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigCapabilitiesDao;
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
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckPermissionAvailability;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckPermissionAvailabilityResponse;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.InQueueType;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.IsUserInQueue;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.IsUserInQueueResponse;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.PermissionIDType;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.QueueTargetType;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.QueueType;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.ResourceIDType;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.UserIDType;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.UserNSNameSequence;

/**
 * Tests the {@link Queuer} class.
 */
public class QueuerTester extends TestCase
{
    /** Object of class under test. */ 
    private Queuer queuer;

    public QueuerTester(String name) throws Exception
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
        props.setProperty("hibernate.generate_statistics", "true");
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
    protected void setUp() throws Exception
    {
        super.setUp();
        this.queuer = new Queuer();
        
        
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#addUserToQueue(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.AddUserToQueue)}.
     */
    public void testAddUserToQueue()
    {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#removeUserFromQueue(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.RemoveUserFromQueue)}.
     */
    public void testRemoveUserFromQueue()
    {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#checkPermissionAvailability(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckPermissionAvailability)}.
     */
    public void testCheckPermissionAvailability() throws Exception
    {
        Date now = new Date();
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        db.beginTransaction();
        UserClass userClass = new UserClass();
        userClass.setName("uc");
        userClass.setQueuable(true);
        db.persist(userClass);
        RigType type = new RigType();
        type.setName("rig_type");
        db.persist(type);
        RigCapabilities caps = new RigCapabilities("a,b,c,d,e,f");
        db.persist(caps);
        Rig rig1 = new Rig();
        rig1.setName("rig1");
        rig1.setActive(true);
        rig1.setOnline(true);
        rig1.setInSession(false);
        rig1.setContactUrl("foo://bar");
        rig1.setLastUpdateTimestamp(now);
        rig1.setRigType(type);
        rig1.setRigCapabilities(caps);
        db.persist(rig1);
        Rig rig2 = new Rig();
        rig2.setName("rig2");
        rig2.setActive(true);
        rig2.setOnline(true);
        rig2.setInSession(false);
        rig2.setContactUrl("foo://bar");
        rig2.setLastUpdateTimestamp(now);
        rig2.setRigType(type);
        rig2.setRigCapabilities(caps);
        db.persist(rig2);
        Rig rig3 = new Rig();
        rig3.setName("rig3");
        rig3.setActive(true);
        rig3.setOnline(true);
        rig3.setInSession(true);
        rig3.setContactUrl("foo://bar");
        rig3.setLastUpdateTimestamp(now);
        rig3.setRigType(type);
        rig3.setRigCapabilities(caps);
        db.persist(rig3);
        ResourcePermission perm = new ResourcePermission();         
        perm.setStartTime(now);
        perm.setExpiryTime(now);
        perm.setUserClass(userClass);
        perm.setType("TYPE");
        perm.setRigType(type);
        db.persist(perm);
        
        db.getTransaction().commit();
        
        /* Request parameters. */
        CheckPermissionAvailability request = new CheckPermissionAvailability();
        PermissionIDType pId = new PermissionIDType();
        request.setCheckPermissionAvailability(pId);
        pId.setPermissionID(perm.getId().intValue());
        
        CheckPermissionAvailabilityResponse resp = this.queuer.checkPermissionAvailability(request);
        
        db.beginTransaction();
        db.delete(perm);
        db.delete(rig1);
        db.delete(rig2);
        db.delete(rig3);
        db.delete(caps);
        db.delete(type);
        db.delete(userClass);
        db.getTransaction().commit();  
        
        assertNotNull(resp);
        QueueType queue = resp.getCheckPermissionAvailabilityResponse();
        assertNotNull(queue);
        assertTrue(queue.getViable());
        assertTrue(queue.getHasFree());
        assertTrue(queue.getIsQueueable());
        assertFalse(queue.getIsCodeAssignable());
        
        ResourceIDType res = queue.getQueuedResource();
        assertNotNull(res);
        assertEquals("TYPE", res.getType());
        assertEquals(type.getId().intValue(), res.getResourceID());
        assertEquals(type.getName(), res.getResourceName());
        
        QueueTargetType targets[] = queue.getQueueTarget();
        assertNotNull(targets);
        assertEquals(3, targets.length);
        QueueTargetType targ = targets[0];
        assertNotNull(targ);
        assertTrue(targ.getIsFree());
        assertTrue(targ.getViable());
        ResourceIDType tres = targ.getResource();
        assertNotNull(targ);
        assertEquals("RIG", tres.getType());
        assertEquals(rig1.getId().intValue(), tres.getResourceID());
        assertEquals(rig1.getName(), tres.getResourceName());
        targ = targets[1];
        assertNotNull(targ);
        assertTrue(targ.getIsFree());
        assertTrue(targ.getViable());
        tres = targ.getResource();
        assertNotNull(targ);
        assertEquals("RIG", tres.getType());
        assertEquals(rig2.getId().intValue(), tres.getResourceID());
        assertEquals(rig2.getName(), tres.getResourceName());
        targ = targets[2];
        assertNotNull(targ);
        assertFalse(targ.getIsFree());
        assertTrue(targ.getViable());
        tres = targ.getResource();
        assertNotNull(targ);
        assertEquals("RIG", tres.getType());
        assertEquals(rig3.getId().intValue(), tres.getResourceID());
        assertEquals(rig3.getName(), tres.getResourceName());
        
        OMElement ele = resp.getOMElement(CheckPermissionAvailabilityResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#checkPermissionAvailability(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckPermissionAvailability)}.
     */
    public void testCheckPermissionAvailabilityAllOffline() throws Exception
    {
        Date now = new Date();
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        db.beginTransaction();
        UserClass userClass = new UserClass();
        userClass.setName("uc");
        userClass.setQueuable(true);
        db.persist(userClass);
        RigType type = new RigType();
        type.setName("rig_type");
        db.persist(type);
        RigCapabilities caps = new RigCapabilities("a,b,c,d,e,f");
        db.persist(caps);
        Rig rig1 = new Rig();
        rig1.setName("rig1");
        rig1.setActive(true);
        rig1.setOnline(false);
        rig1.setInSession(false);
        rig1.setContactUrl("foo://bar");
        rig1.setLastUpdateTimestamp(now);
        rig1.setRigType(type);
        rig1.setRigCapabilities(caps);
        db.persist(rig1);
        Rig rig2 = new Rig();
        rig2.setName("rig2");
        rig2.setActive(true);
        rig2.setOnline(false);
        rig2.setInSession(false);
        rig2.setContactUrl("foo://bar");
        rig2.setLastUpdateTimestamp(now);
        rig2.setRigType(type);
        rig2.setRigCapabilities(caps);
        db.persist(rig2);
        Rig rig3 = new Rig();
        rig3.setName("rig3");
        rig3.setActive(true);
        rig3.setOnline(false);
        rig3.setInSession(true);
        rig3.setContactUrl("foo://bar");
        rig3.setLastUpdateTimestamp(now);
        rig3.setRigType(type);
        rig3.setRigCapabilities(caps);
        db.persist(rig3);
        ResourcePermission perm = new ResourcePermission();         
        perm.setStartTime(now);
        perm.setExpiryTime(now);
        perm.setUserClass(userClass);
        perm.setType("TYPE");
        perm.setRigType(type);
        db.persist(perm);
        
        db.getTransaction().commit();
        
        /* Request parameters. */
        CheckPermissionAvailability request = new CheckPermissionAvailability();
        PermissionIDType pId = new PermissionIDType();
        request.setCheckPermissionAvailability(pId);
        pId.setPermissionID(perm.getId().intValue());
        
        CheckPermissionAvailabilityResponse resp = this.queuer.checkPermissionAvailability(request);
        
        db.beginTransaction();
        db.delete(perm);
        db.delete(rig1);
        db.delete(rig2);
        db.delete(rig3);
        db.delete(caps);
        db.delete(type);
        db.delete(userClass);
        db.getTransaction().commit();  
        
        assertNotNull(resp);
        QueueType queue = resp.getCheckPermissionAvailabilityResponse();
        assertNotNull(queue);
        assertFalse(queue.getViable());
        assertFalse(queue.getHasFree());
        assertTrue(queue.getIsQueueable());
        assertFalse(queue.getIsCodeAssignable());
        
        ResourceIDType res = queue.getQueuedResource();
        assertNotNull(res);
        assertEquals("TYPE", res.getType());
        assertEquals(type.getId().intValue(), res.getResourceID());
        assertEquals(type.getName(), res.getResourceName());
        
        QueueTargetType targets[] = queue.getQueueTarget();
        assertNotNull(targets);
        assertEquals(3, targets.length);
        QueueTargetType targ = targets[0];
        assertNotNull(targ);
        assertFalse(targ.getIsFree());
        assertFalse(targ.getViable());
        ResourceIDType tres = targ.getResource();
        assertNotNull(targ);
        assertEquals("RIG", tres.getType());
        assertEquals(rig1.getId().intValue(), tres.getResourceID());
        assertEquals(rig1.getName(), tres.getResourceName());
        targ = targets[1];
        assertNotNull(targ);
        assertFalse(targ.getIsFree());
        assertFalse(targ.getViable());
        tres = targ.getResource();
        assertNotNull(targ);
        assertEquals("RIG", tres.getType());
        assertEquals(rig2.getId().intValue(), tres.getResourceID());
        assertEquals(rig2.getName(), tres.getResourceName());
        targ = targets[2];
        assertNotNull(targ);
        assertFalse(targ.getIsFree());
        assertFalse(targ.getViable());
        tres = targ.getResource();
        assertNotNull(targ);
        assertEquals("RIG", tres.getType());
        assertEquals(rig3.getId().intValue(), tres.getResourceID());
        assertEquals(rig3.getName(), tres.getResourceName());
        
        OMElement ele = resp.getOMElement(CheckPermissionAvailabilityResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#checkPermissionAvailability(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckPermissionAvailability)}.
     */
    public void testCheckPermissionAvailabilityAllInSession() throws Exception
    {
        Date now = new Date();
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        db.beginTransaction();
        UserClass userClass = new UserClass();
        userClass.setName("uc");
        userClass.setQueuable(false);
        db.persist(userClass);
        RigType type = new RigType();
        type.setName("rig_type");
        type.setCodeAssignable(true);
        db.persist(type);
        RigCapabilities caps = new RigCapabilities("a,b,c,d,e,f");
        db.persist(caps);
        Rig rig1 = new Rig();
        rig1.setName("rig1");
        rig1.setActive(true);
        rig1.setOnline(true);
        rig1.setInSession(true);
        rig1.setContactUrl("foo://bar");
        rig1.setLastUpdateTimestamp(now);
        rig1.setRigType(type);
        rig1.setRigCapabilities(caps);
        db.persist(rig1);
        Rig rig2 = new Rig();
        rig2.setName("rig2");
        rig2.setActive(true);
        rig2.setOnline(true);
        rig2.setInSession(true);
        rig2.setContactUrl("foo://bar");
        rig2.setLastUpdateTimestamp(now);
        rig2.setRigType(type);
        rig2.setRigCapabilities(caps);
        db.persist(rig2);
        Rig rig3 = new Rig();
        rig3.setName("rig3");
        rig3.setActive(true);
        rig3.setOnline(true);
        rig3.setInSession(true);
        rig3.setContactUrl("foo://bar");
        rig3.setLastUpdateTimestamp(now);
        rig3.setRigType(type);
        rig3.setRigCapabilities(caps);
        db.persist(rig3);
        ResourcePermission perm = new ResourcePermission();         
        perm.setStartTime(now);
        perm.setExpiryTime(now);
        perm.setUserClass(userClass);
        perm.setType("TYPE");
        perm.setRigType(type);
        db.persist(perm);
        
        db.getTransaction().commit();
        
        /* Request parameters. */
        CheckPermissionAvailability request = new CheckPermissionAvailability();
        PermissionIDType pId = new PermissionIDType();
        request.setCheckPermissionAvailability(pId);
        pId.setPermissionID(perm.getId().intValue());
        
        CheckPermissionAvailabilityResponse resp = this.queuer.checkPermissionAvailability(request);
        
        db.beginTransaction();
        db.delete(perm);
        db.delete(rig1);
        db.delete(rig2);
        db.delete(rig3);
        db.delete(caps);
        db.delete(type);
        db.delete(userClass);
        db.getTransaction().commit();  
        
        assertNotNull(resp);
        QueueType queue = resp.getCheckPermissionAvailabilityResponse();
        assertNotNull(queue);
        assertTrue(queue.getViable());
        assertFalse(queue.getHasFree());
        assertFalse(queue.getIsQueueable());
        assertTrue(queue.getIsCodeAssignable());
        
        ResourceIDType res = queue.getQueuedResource();
        assertNotNull(res);
        assertEquals("TYPE", res.getType());
        assertEquals(type.getId().intValue(), res.getResourceID());
        assertEquals(type.getName(), res.getResourceName());
        
        QueueTargetType targets[] = queue.getQueueTarget();
        assertNotNull(targets);
        assertEquals(3, targets.length);
        QueueTargetType targ = targets[0];
        assertNotNull(targ);
        assertFalse(targ.getIsFree());
        assertTrue(targ.getViable());
        ResourceIDType tres = targ.getResource();
        assertNotNull(targ);
        assertEquals("RIG", tres.getType());
        assertEquals(rig1.getId().intValue(), tres.getResourceID());
        assertEquals(rig1.getName(), tres.getResourceName());
        targ = targets[1];
        assertNotNull(targ);
        assertFalse(targ.getIsFree());
        assertTrue(targ.getViable());
        tres = targ.getResource();
        assertNotNull(targ);
        assertEquals("RIG", tres.getType());
        assertEquals(rig2.getId().intValue(), tres.getResourceID());
        assertEquals(rig2.getName(), tres.getResourceName());
        targ = targets[2];
        assertNotNull(targ);
        assertFalse(targ.getIsFree());
        assertTrue(targ.getViable());
        tres = targ.getResource();
        assertNotNull(targ);
        assertEquals("RIG", tres.getType());
        assertEquals(rig3.getId().intValue(), tres.getResourceID());
        assertEquals(rig3.getName(), tres.getResourceName());
        
        OMElement ele = resp.getOMElement(CheckPermissionAvailabilityResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#checkPermissionAvailability(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckPermissionAvailability)}.
     */
    public void testCheckPermissionAvailabilityNoRigs() throws Exception
    {
        Date now = new Date();
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        db.beginTransaction();
        UserClass userClass = new UserClass();
        userClass.setName("uc");
        userClass.setQueuable(true);
        db.persist(userClass);
        RigType type = new RigType();
        type.setName("rig_type");
        db.persist(type);
        ResourcePermission perm = new ResourcePermission();         
        perm.setStartTime(now);
        perm.setExpiryTime(now);
        perm.setUserClass(userClass);
        perm.setType("TYPE");
        perm.setRigType(type);
        db.persist(perm);
        
        db.getTransaction().commit();
        
        /* Request parameters. */
        CheckPermissionAvailability request = new CheckPermissionAvailability();
        PermissionIDType pId = new PermissionIDType();
        request.setCheckPermissionAvailability(pId);
        pId.setPermissionID(perm.getId().intValue());
        
        CheckPermissionAvailabilityResponse resp = this.queuer.checkPermissionAvailability(request);
        
        db.beginTransaction();
        db.delete(perm);
        db.delete(type);
        db.delete(userClass);
        db.getTransaction().commit();  
        
        assertNotNull(resp);
        QueueType queue = resp.getCheckPermissionAvailabilityResponse();
        assertNotNull(queue);
        assertFalse(queue.getViable());
        assertFalse(queue.getHasFree());
        assertTrue(queue.getIsQueueable());
        assertFalse(queue.getIsCodeAssignable());
        
        ResourceIDType res = queue.getQueuedResource();
        assertNotNull(res);
        assertEquals("TYPE", res.getType());
        assertEquals(type.getId().intValue(), res.getResourceID());
        assertEquals(type.getName(), res.getResourceName());
        
        QueueTargetType targets[] = queue.getQueueTarget();
        assertNull(targets);
        
        OMElement ele = resp.getOMElement(CheckPermissionAvailabilityResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#checkPermissionAvailability(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckPermissionAvailability)}.
     */
    public void testCheckPermissionAvailabilityRig() throws Exception
    {
        Date now = new Date();
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        db.beginTransaction();
        UserClass userClass = new UserClass();
        userClass.setName("uc");
        userClass.setQueuable(true);
        db.persist(userClass);
        RigType type = new RigType();
        type.setName("rig_type");
        db.persist(type);
        RigCapabilities caps = new RigCapabilities("a,b,c,d,e,f");
        db.persist(caps);
        Rig rig1 = new Rig();
        rig1.setName("rig1");
        rig1.setActive(true);
        rig1.setOnline(true);
        rig1.setInSession(false);
        rig1.setContactUrl("foo://bar");
        rig1.setLastUpdateTimestamp(now);
        rig1.setRigType(type);
        rig1.setRigCapabilities(caps);
        db.persist(rig1);
        ResourcePermission perm = new ResourcePermission();         
        perm.setStartTime(now);
        perm.setExpiryTime(now);
        perm.setUserClass(userClass);
        perm.setType("RIG");
        perm.setRig(rig1);
        db.persist(perm);
        
        db.getTransaction().commit();
        
        /* Request parameters. */
        CheckPermissionAvailability request = new CheckPermissionAvailability();
        PermissionIDType pId = new PermissionIDType();
        request.setCheckPermissionAvailability(pId);
        pId.setPermissionID(perm.getId().intValue());
        
        CheckPermissionAvailabilityResponse resp = this.queuer.checkPermissionAvailability(request);
        
        db.beginTransaction();
        db.delete(perm);
        db.delete(rig1);
        db.delete(caps);
        db.delete(type);
        db.delete(userClass);
        db.getTransaction().commit();  
        
        assertNotNull(resp);
        QueueType queue = resp.getCheckPermissionAvailabilityResponse();
        assertNotNull(queue);
        assertTrue(queue.getViable());
        assertTrue(queue.getHasFree());
        assertTrue(queue.getIsQueueable());
        assertFalse(queue.getIsCodeAssignable());
        
        ResourceIDType res = queue.getQueuedResource();
        assertNotNull(res);
        assertEquals("RIG", res.getType());
        assertEquals(rig1.getId().intValue(), res.getResourceID());
        assertEquals(rig1.getName(), res.getResourceName());
        
        QueueTargetType targets[] = queue.getQueueTarget();
        assertNotNull(targets);
        assertEquals(1, targets.length);
        QueueTargetType targ = targets[0];
        assertNotNull(targ);
        assertTrue(targ.getIsFree());
        assertTrue(targ.getViable());
        ResourceIDType tres = targ.getResource();
        assertNotNull(targ);
        assertEquals("RIG", tres.getType());
        assertEquals(rig1.getId().intValue(), tres.getResourceID());
        assertEquals(rig1.getName(), tres.getResourceName());

        OMElement ele = resp.getOMElement(CheckPermissionAvailabilityResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#checkPermissionAvailability(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckPermissionAvailability)}.
     */
    public void testCheckPermissionAvailabilityRigOffline() throws Exception
    {
        Date now = new Date();
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        db.beginTransaction();
        UserClass userClass = new UserClass();
        userClass.setName("uc");
        userClass.setQueuable(true);
        db.persist(userClass);
        RigType type = new RigType();
        type.setName("rig_type");
        db.persist(type);
        RigCapabilities caps = new RigCapabilities("a,b,c,d,e,f");
        db.persist(caps);
        Rig rig1 = new Rig();
        rig1.setName("rig1");
        rig1.setActive(true);
        rig1.setOnline(false);
        rig1.setInSession(false);
        rig1.setContactUrl("foo://bar");
        rig1.setLastUpdateTimestamp(now);
        rig1.setRigType(type);
        rig1.setRigCapabilities(caps);
        db.persist(rig1);
        ResourcePermission perm = new ResourcePermission();         
        perm.setStartTime(now);
        perm.setExpiryTime(now);
        perm.setUserClass(userClass);
        perm.setType("RIG");
        perm.setRig(rig1);
        db.persist(perm);
        
        db.getTransaction().commit();
        
        /* Request parameters. */
        CheckPermissionAvailability request = new CheckPermissionAvailability();
        PermissionIDType pId = new PermissionIDType();
        request.setCheckPermissionAvailability(pId);
        pId.setPermissionID(perm.getId().intValue());
        
        CheckPermissionAvailabilityResponse resp = this.queuer.checkPermissionAvailability(request);
        
        db.beginTransaction();
        db.delete(perm);
        db.delete(rig1);
        db.delete(caps);
        db.delete(type);
        db.delete(userClass);
        db.getTransaction().commit();  
        
        assertNotNull(resp);
        QueueType queue = resp.getCheckPermissionAvailabilityResponse();
        assertNotNull(queue);
        assertFalse(queue.getViable());
        assertFalse(queue.getHasFree());
        assertTrue(queue.getIsQueueable());
        assertFalse(queue.getIsCodeAssignable());
        
        ResourceIDType res = queue.getQueuedResource();
        assertNotNull(res);
        assertEquals("RIG", res.getType());
        assertEquals(rig1.getId().intValue(), res.getResourceID());
        assertEquals(rig1.getName(), res.getResourceName());
        
        QueueTargetType targets[] = queue.getQueueTarget();
        assertNotNull(targets);
        assertEquals(1, targets.length);
        QueueTargetType targ = targets[0];
        assertNotNull(targ);
        assertFalse(targ.getIsFree());
        assertFalse(targ.getViable());
        ResourceIDType tres = targ.getResource();
        assertNotNull(targ);
        assertEquals("RIG", tres.getType());
        assertEquals(rig1.getId().intValue(), tres.getResourceID());
        assertEquals(rig1.getName(), tres.getResourceName());

        OMElement ele = resp.getOMElement(CheckPermissionAvailabilityResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#checkPermissionAvailability(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckPermissionAvailability)}.
     */
    public void testCheckPermissionAvailabilityRigInSession() throws Exception
    {
        Date now = new Date();
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        db.beginTransaction();
        UserClass userClass = new UserClass();
        userClass.setName("uc");
        userClass.setQueuable(true);
        db.persist(userClass);
        RigType type = new RigType();
        type.setName("rig_type");
        type.setCodeAssignable(true);
        db.persist(type);
        RigCapabilities caps = new RigCapabilities("a,b,c,d,e,f");
        db.persist(caps);
        Rig rig1 = new Rig();
        rig1.setName("rig1");
        rig1.setActive(true);
        rig1.setOnline(true);
        rig1.setInSession(true);
        rig1.setContactUrl("foo://bar");
        rig1.setLastUpdateTimestamp(now);
        rig1.setRigType(type);
        rig1.setRigCapabilities(caps);
        db.persist(rig1);
        ResourcePermission perm = new ResourcePermission();         
        perm.setStartTime(now);
        perm.setExpiryTime(now);
        perm.setUserClass(userClass);
        perm.setType("RIG");
        perm.setRig(rig1);
        db.persist(perm);
        
        db.getTransaction().commit();
        
        /* Request parameters. */
        CheckPermissionAvailability request = new CheckPermissionAvailability();
        PermissionIDType pId = new PermissionIDType();
        request.setCheckPermissionAvailability(pId);
        pId.setPermissionID(perm.getId().intValue());
        
        CheckPermissionAvailabilityResponse resp = this.queuer.checkPermissionAvailability(request);
        
        db.beginTransaction();
        db.delete(perm);
        db.delete(rig1);
        db.delete(caps);
        db.delete(type);
        db.delete(userClass);
        db.getTransaction().commit();  
        
        assertNotNull(resp);
        QueueType queue = resp.getCheckPermissionAvailabilityResponse();
        assertNotNull(queue);
        assertTrue(queue.getViable());
        assertFalse(queue.getHasFree());
        assertTrue(queue.getIsQueueable());
        assertTrue(queue.getIsCodeAssignable());
        
        ResourceIDType res = queue.getQueuedResource();
        assertNotNull(res);
        assertEquals("RIG", res.getType());
        assertEquals(rig1.getId().intValue(), res.getResourceID());
        assertEquals(rig1.getName(), res.getResourceName());
        
        QueueTargetType targets[] = queue.getQueueTarget();
        assertNotNull(targets);
        assertEquals(1, targets.length);
        QueueTargetType targ = targets[0];
        assertNotNull(targ);
        assertFalse(targ.getIsFree());
        assertTrue(targ.getViable());
        ResourceIDType tres = targ.getResource();
        assertNotNull(targ);
        assertEquals("RIG", tres.getType());
        assertEquals(rig1.getId().intValue(), tres.getResourceID());
        assertEquals(rig1.getName(), tres.getResourceName());

        OMElement ele = resp.getOMElement(CheckPermissionAvailabilityResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#checkPermissionAvailability(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckPermissionAvailability)}.
     */
    public void testCheckPermissionAvailabilityCaps() throws Exception
    {
        Date now = new Date();
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        RigCapabilitiesDao dao = new RigCapabilitiesDao(db);
        RigCapabilities a = dao.addCapabilities("a");
        RigCapabilities ab = dao.addCapabilities("a,b");
        RigCapabilities abc = dao.addCapabilities("a,b,c");
        RequestCapabilitiesDao reqCapsDao = new RequestCapabilitiesDao(db);
        RequestCapabilities ra = reqCapsDao.addCapabilities("a");
        
        db.beginTransaction();
        UserClass userClass = new UserClass();
        userClass.setName("uc");
        userClass.setQueuable(true);
        db.persist(userClass);
        RigType type = new RigType();
        type.setName("rig_type");
        db.persist(type);
        Rig rig1 = new Rig();
        rig1.setName("rig1");
        rig1.setActive(true);
        rig1.setOnline(true);
        rig1.setInSession(false);
        rig1.setContactUrl("foo://bar");
        rig1.setLastUpdateTimestamp(now);
        rig1.setRigType(type);
        rig1.setRigCapabilities(a);
        db.persist(rig1);
        Rig rig2 = new Rig();
        rig2.setName("rig2");
        rig2.setActive(true);
        rig2.setOnline(true);
        rig2.setInSession(false);
        rig2.setContactUrl("foo://bar");
        rig2.setLastUpdateTimestamp(now);
        rig2.setRigType(type);
        rig2.setRigCapabilities(ab);
        db.persist(rig2);
        Rig rig3 = new Rig();
        rig3.setName("rig3");
        rig3.setActive(true);
        rig3.setOnline(true);
        rig3.setInSession(false);
        rig3.setContactUrl("foo://bar");
        rig3.setLastUpdateTimestamp(now);
        rig3.setRigType(type);
        rig3.setRigCapabilities(abc);
        db.persist(rig3);
        Rig rig4 = new Rig();
        rig4.setName("rig4");
        rig4.setActive(true);
        rig4.setOnline(true);
        rig4.setInSession(false);
        rig4.setContactUrl("foo://bar");
        rig4.setLastUpdateTimestamp(now);
        rig4.setRigType(type);
        rig4.setRigCapabilities(abc);
        db.persist(rig4);
        ResourcePermission perm = new ResourcePermission();         
        perm.setStartTime(now);
        perm.setExpiryTime(now);
        perm.setUserClass(userClass);
        perm.setType("CAPABILITY");
        perm.setRequestCapabilities(ra);
        db.persist(perm);
        db.getTransaction().commit();
        
        /* Request parameters. */
        CheckPermissionAvailability request = new CheckPermissionAvailability();
        PermissionIDType pId = new PermissionIDType();
        request.setCheckPermissionAvailability(pId);
        pId.setPermissionID(perm.getId().intValue());
        
        CheckPermissionAvailabilityResponse resp = this.queuer.checkPermissionAvailability(request);
        
        db.beginTransaction();
        db.delete(perm);
        db.delete(rig1);
        db.delete(rig2);
        db.delete(rig3);
        db.delete(rig4);
        db.delete(a);
        db.delete(ab);
        db.delete(abc);
        db.delete(type);
        db.delete(userClass);
        db.getTransaction().commit();  
        
        assertNotNull(resp);
        QueueType queue = resp.getCheckPermissionAvailabilityResponse();
        assertNotNull(queue);
        assertTrue(queue.getViable());
        assertTrue(queue.getHasFree());
        assertTrue(queue.getIsQueueable());
        assertFalse(queue.getIsCodeAssignable());
        
        ResourceIDType res = queue.getQueuedResource();
        assertNotNull(res);
        assertEquals("CAPABILITY", res.getType());
        assertEquals(ra.getId().intValue(), res.getResourceID());
        assertEquals(ra.getCapabilities(), res.getResourceName());
        
        QueueTargetType targets[] = queue.getQueueTarget();
        assertNotNull(targets);
        assertEquals(3, targets.length);
        QueueTargetType targ = targets[0];
        assertNotNull(targ);
        assertTrue(targ.getIsFree());
        assertTrue(targ.getViable());
        ResourceIDType tres = targ.getResource();
        assertNotNull(targ);
        assertEquals("RIG", tres.getType());
        assertEquals(rig1.getId().intValue(), tres.getResourceID());
        assertEquals(rig1.getName(), tres.getResourceName());
        targ = targets[1];
        assertNotNull(targ);
        assertTrue(targ.getIsFree());
        assertTrue(targ.getViable());
        tres = targ.getResource();
        assertNotNull(targ);
        assertEquals("RIG", tres.getType());
        assertEquals(rig2.getId().intValue(), tres.getResourceID());
        assertEquals(rig2.getName(), tres.getResourceName());
        targ = targets[2];
        assertNotNull(targ);
        assertFalse(targ.getIsFree());
        assertTrue(targ.getViable());
        tres = targ.getResource();
        assertNotNull(targ);
        assertEquals("RIG", tres.getType());
        assertEquals(rig3.getId().intValue(), tres.getResourceID());
        assertEquals(rig3.getName(), tres.getResourceName());
        targ = targets[3];
        assertNotNull(targ);
        assertTrue(targ.getIsFree());
        assertTrue(targ.getViable());
        tres = targ.getResource();
        assertNotNull(targ);
        assertEquals("RIG", tres.getType());
        assertEquals(rig4.getId().intValue(), tres.getResourceID());
        assertEquals(rig4.getName(), tres.getResourceName());
        
        OMElement ele = resp.getOMElement(CheckPermissionAvailabilityResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }
    

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#getUserQueuePosition(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.GetUserQueuePosition)}.
     */
    public void testGetUserQueuePosition()
    {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#checkResourceAvailability(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckResourceAvailability)}.
     */
    public void testCheckResourceAvailability()
    {
        fail("Not yet implemented"); // TODO
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#isUserInQueue(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.IsUserInQueue)}.
     */
    @Test
    public void testIsUserInQueue() throws Exception
    {
        Date now = new Date();
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        db.beginTransaction();
        User user = new User("locktest", "ns", "USER");
        db.persist(user);
        UserClass userClass= new UserClass();
        userClass.setName("uc");
        db.persist(userClass);
        RigType type = new RigType();
        type.setName("rig_type");
        db.persist(type);
        ResourcePermission perm = new ResourcePermission();         
        perm.setStartTime(now);
        perm.setExpiryTime(now);
        perm.setUserClass(userClass);
        perm.setType("TYPE");
        perm.setRigType(type);
        db.persist(perm);
        Session ses = new Session();
        ses.setActive(true);
        ses.setActivityLastUpdated(now);
        ses.setPriority((short) 1);
        ses.setRequestTime(now);
        ses.setUser(user);
        ses.setUserName(user.getName());
        ses.setUserNamespace(user.getNamespace());
        ses.setRequestedResourceId(type.getId());
        ses.setRequestedResourceName(type.getName());
        ses.setResourcePermission(perm);
        ses.setResourceType("TYPE");
        db.persist(ses);
        db.getTransaction().commit();
        
        /* Request parameters. */
        IsUserInQueue request = new IsUserInQueue();
        UserIDType uid = new UserIDType();
        request.setIsUserInQueue(uid);
        uid.setUserID(String.valueOf(user.getId()));
        
        IsUserInQueueResponse resp = this.queuer.isUserInQueue(request);
        
        db.beginTransaction();
        db.delete(ses);
        db.delete(perm);
        db.delete(type);
        db.delete(userClass);
        db.delete(user);
        db.getTransaction().commit();  
        
        assertNotNull(resp);
        InQueueType queue = resp.getIsUserInQueueResponse();
        assertNotNull(queue);
        
        assertTrue(queue.getInQueue());
        assertFalse(queue.getInSession());
        assertNull(queue.getAssignedResource());
        
        ResourceIDType reqRes = queue.getQueuedResouce();
        assertNotNull(reqRes);
        assertEquals("TYPE", reqRes.getType());
        assertEquals(type.getId().intValue(), reqRes.getResourceID());
        assertEquals(type.getName(), reqRes.getResourceName());
        
        OMElement ele = resp.getOMElement(IsUserInQueueResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains(type.getName()));
        
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#isUserInQueue(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.IsUserInQueue)}.
     */
    @Test
    public void testIsUserInQueueNSName() throws Exception
    {
        Date now = new Date();
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        db.beginTransaction();
        User user = new User("locktest", "ns", "USER");
        db.persist(user);
        UserClass userClass= new UserClass();
        userClass.setName("uc");
        db.persist(userClass);
        RigType type = new RigType();
        type.setName("rig_type");
        db.persist(type);
        ResourcePermission perm = new ResourcePermission();         
        perm.setStartTime(now);
        perm.setExpiryTime(now);
        perm.setUserClass(userClass);
        perm.setType("TYPE");
        perm.setRigType(type);
        db.persist(perm);
        Session ses = new Session();
        ses.setActive(true);
        ses.setActivityLastUpdated(now);
        ses.setPriority((short) 1);
        ses.setRequestTime(now);
        ses.setUser(user);
        ses.setUserName(user.getName());
        ses.setUserNamespace(user.getNamespace());
        ses.setRequestedResourceId(type.getId());
        ses.setRequestedResourceName(type.getName());
        ses.setResourcePermission(perm);
        ses.setResourceType("TYPE");
        db.persist(ses);
        db.getTransaction().commit();
        
        /* Request parameters. */
        IsUserInQueue request = new IsUserInQueue();
        UserIDType uid = new UserIDType();
        request.setIsUserInQueue(uid);
        uid.setUserQName(user.getNamespace() + ":" + user.getName());
        
        IsUserInQueueResponse resp = this.queuer.isUserInQueue(request);
        
        db.beginTransaction();
        db.delete(ses);
        db.delete(perm);
        db.delete(type);
        db.delete(userClass);
        db.delete(user);
        db.getTransaction().commit();  
        
        assertNotNull(resp);
        InQueueType queue = resp.getIsUserInQueueResponse();
        assertNotNull(queue);
        
        assertTrue(queue.getInQueue());
        assertFalse(queue.getInSession());
        assertNull(queue.getAssignedResource());
        
        ResourceIDType reqRes = queue.getQueuedResouce();
        assertNotNull(reqRes);
        assertEquals("TYPE", reqRes.getType());
        assertEquals(type.getId().intValue(), reqRes.getResourceID());
        assertEquals(type.getName(), reqRes.getResourceName());
        
        OMElement ele = resp.getOMElement(IsUserInQueueResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains(type.getName()));   
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#isUserInQueue(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.IsUserInQueue)}.
     */
    @Test
    public void testIsUserInQueueActive() throws Exception
    {
        Date now = new Date();
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        db.beginTransaction();
        User user = new User("locktest", "ns", "USER");
        db.persist(user);
        UserClass userClass= new UserClass();
        userClass.setName("uc");
        db.persist(userClass);
        RigType type = new RigType();
        type.setName("rig_type");
        db.persist(type);
        ResourcePermission perm = new ResourcePermission();         
        perm.setStartTime(now);
        perm.setExpiryTime(now);
        perm.setUserClass(userClass);
        perm.setType("TYPE");
        perm.setRigType(type);
        db.persist(perm);
        RigCapabilities caps = new RigCapabilities("a,b,c,d,e");
        db.persist(caps);
        Rig rig = new Rig();
        rig.setName("in_ses_test");
        rig.setContactUrl("foo:///");
        rig.setInSession(true);
        rig.setLastUpdateTimestamp(new Date());
        rig.setRigCapabilities(caps);
        rig.setRigType(type);
        db.persist(rig);
        Session ses = new Session();
        ses.setActive(true);
        ses.setActivityLastUpdated(now);
        ses.setPriority((short) 1);
        ses.setRequestTime(now);
        ses.setUser(user);
        ses.setUserName(user.getName());
        ses.setUserNamespace(user.getNamespace());
        ses.setRequestedResourceId(type.getId());
        ses.setRequestedResourceName(type.getName());
        ses.setResourcePermission(perm);
        ses.setResourceType("TYPE");
        ses.setAssignmentTime(now);
        ses.setRig(rig);
        ses.setAssignedRigName(rig.getName());
        db.persist(ses);
        db.getTransaction().commit();
        
        /* Request parameters. */
        IsUserInQueue request = new IsUserInQueue();
        UserIDType uid = new UserIDType();
        request.setIsUserInQueue(uid);
        uid.setUserID(String.valueOf(user.getId()));
        
        IsUserInQueueResponse resp = this.queuer.isUserInQueue(request);
        
        db.beginTransaction();
        db.delete(ses);
        db.delete(perm);
        db.delete(rig);
        db.delete(caps);
        db.delete(type);
        db.delete(userClass);
        db.delete(user);
        db.getTransaction().commit();  
        
        assertNotNull(resp);
        InQueueType queue = resp.getIsUserInQueueResponse();
        assertNotNull(queue);
        
        assertFalse(queue.getInQueue());
        assertTrue(queue.getInSession());
        
        ResourceIDType reqRes = queue.getQueuedResouce();
        assertNotNull(reqRes);
        assertEquals("TYPE", reqRes.getType());
        assertEquals(type.getId().intValue(), reqRes.getResourceID());
        assertEquals(type.getName(), reqRes.getResourceName());
        
        ResourceIDType res = queue.getAssignedResource();
        assertNotNull(res);
        assertEquals("RIG", res.getType());
        assertEquals(rig.getName(), res.getResourceName());
        assertEquals(rig.getId().intValue(), res.getResourceID());
        
        OMElement ele = resp.getOMElement(IsUserInQueueResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains(type.getName()));
        
    }
    
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#isUserInQueue(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.IsUserInQueue)}.
     */
    @Test
    public void testIsUserInQueueTwoSes() throws Exception
    {
        Date now = new Date();
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        db.beginTransaction();
        User user = new User("locktest", "ns", "USER");
        db.persist(user);
        UserClass userClass= new UserClass();
        userClass.setName("uc");
        db.persist(userClass);
        RigType type = new RigType();
        type.setName("rig_type");
        db.persist(type);
        ResourcePermission perm = new ResourcePermission();         
        perm.setStartTime(now);
        perm.setExpiryTime(now);
        perm.setUserClass(userClass);
        perm.setType("TYPE");
        perm.setRigType(type);
        db.persist(perm);
        Session ses = new Session();
        ses.setActive(true);
        ses.setActivityLastUpdated(now);
        ses.setPriority((short) 1);
        ses.setRequestTime(now);
        ses.setUser(user);
        ses.setUserName(user.getName());
        ses.setUserNamespace(user.getNamespace());
        ses.setRequestedResourceId(type.getId());
        ses.setRequestedResourceName(type.getName());
        ses.setResourcePermission(perm);
        ses.setResourceType("TYPE");
        Session ses2 = new Session();
        ses2.setActive(false);
        ses2.setActivityLastUpdated(now);
        ses2.setPriority((short) 1);
        ses2.setRequestTime(new Date(System.currentTimeMillis() - 1000));
        ses2.setUser(user);
        ses2.setUserName(user.getName());
        ses2.setUserNamespace(user.getNamespace());
        ses2.setRequestedResourceId(type.getId());
        ses2.setRequestedResourceName(type.getName());
        ses2.setResourcePermission(perm);
        ses2.setResourceType("TYPE");
        db.persist(ses2);
        db.persist(ses);
        db.getTransaction().commit();
        
        /* Request parameters. */
        IsUserInQueue request = new IsUserInQueue();
        UserIDType uid = new UserIDType();
        request.setIsUserInQueue(uid);
        uid.setUserID(String.valueOf(user.getId()));
        
        IsUserInQueueResponse resp = this.queuer.isUserInQueue(request);
        
        db.beginTransaction();
        db.delete(ses2);
        db.delete(ses);
        db.delete(perm);
        db.delete(type);
        db.delete(userClass);
        db.delete(user);
        db.getTransaction().commit();  
        
        assertNotNull(resp);
        InQueueType queue = resp.getIsUserInQueueResponse();
        assertNotNull(queue);
        
        assertTrue(queue.getInQueue());
        assertFalse(queue.getInSession());
        assertNull(queue.getAssignedResource());
        
        ResourceIDType reqRes = queue.getQueuedResouce();
        assertNotNull(reqRes);
        assertEquals("TYPE", reqRes.getType());
        assertEquals(type.getId().intValue(), reqRes.getResourceID());
        assertEquals(type.getName(), reqRes.getResourceName());
        
        OMElement ele = resp.getOMElement(IsUserInQueueResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains(type.getName()));
        
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#isUserInQueue(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.IsUserInQueue)}.
     */
    @Test
    public void testIsUserInQueueNot() throws Exception
    {
        Date now = new Date();
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        db.beginTransaction();
        User user = new User("locktest", "ns", "USER");
        db.persist(user);
        UserClass userClass= new UserClass();
        userClass.setName("uc");
        db.persist(userClass);
        RigType type = new RigType();
        type.setName("rig_type");
        db.persist(type);
        ResourcePermission perm = new ResourcePermission();         
        perm.setStartTime(now);
        perm.setExpiryTime(now);
        perm.setUserClass(userClass);
        perm.setType("TYPE");
        perm.setRigType(type);
        db.persist(perm);
        Session ses = new Session();
        ses.setActive(false);
        ses.setActivityLastUpdated(now);
        ses.setPriority((short) 1);
        ses.setRequestTime(now);
        ses.setUser(user);
        ses.setUserName(user.getName());
        ses.setUserNamespace(user.getNamespace());
        ses.setRequestedResourceId(type.getId());
        ses.setRequestedResourceName(type.getName());
        ses.setResourcePermission(perm);
        ses.setResourceType("TYPE");
        db.persist(ses);
        db.getTransaction().commit();
        
        /* Request parameters. */
        IsUserInQueue request = new IsUserInQueue();
        UserIDType uid = new UserIDType();
        request.setIsUserInQueue(uid);
        uid.setUserID(String.valueOf(user.getId()));
        
        IsUserInQueueResponse resp = this.queuer.isUserInQueue(request);
        
        db.beginTransaction();
        db.delete(ses);
        db.delete(perm);
        db.delete(type);
        db.delete(userClass);
        db.delete(user);
        db.getTransaction().commit();  
        
        assertNotNull(resp);
        InQueueType queue = resp.getIsUserInQueueResponse();
        assertNotNull(queue);
        
        assertFalse(queue.getInQueue());
        assertFalse(queue.getInSession());
        assertNull(queue.getAssignedResource());
        assertNull(queue.getQueuedResouce());
        
        OMElement ele = resp.getOMElement(IsUserInQueueResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("false"));
        
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#isUserInQueue(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.IsUserInQueue)}.
     */
    @Test
    public void testIsUserInQueueNoSessions() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        db.beginTransaction();
        User user = new User("locktest", "ns", "USER");
        db.persist(user);
        db.getTransaction().commit();
        
        /* Request parameters. */
        IsUserInQueue request = new IsUserInQueue();
        UserIDType uid = new UserIDType();
        request.setIsUserInQueue(uid);
        uid.setUserID(String.valueOf(user.getId()));
        
        IsUserInQueueResponse resp = this.queuer.isUserInQueue(request);
        
        db.beginTransaction();
        db.delete(user);
        db.getTransaction().commit();  
        
        assertNotNull(resp);
        InQueueType queue = resp.getIsUserInQueueResponse();
        assertNotNull(queue);
        
        assertFalse(queue.getInQueue());
        assertFalse(queue.getInSession());
        assertNull(queue.getAssignedResource());
        assertNull(queue.getQueuedResouce());
        
        OMElement ele = resp.getOMElement(IsUserInQueueResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("false"));
        
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.intf.Queuer#isUserInQueue(au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.IsUserInQueue)}.
     */
    @Test
    public void testIsUserInQueueNoUser() throws Exception
    {   
        /* Request parameters. */
        IsUserInQueue request = new IsUserInQueue();
        UserIDType uid = new UserIDType();
        request.setIsUserInQueue(uid);
        uid.setUserQName("ns:does_not_exist");
        
        IsUserInQueueResponse resp = this.queuer.isUserInQueue(request);
        
        assertNotNull(resp);
        InQueueType queue = resp.getIsUserInQueueResponse();
        assertNotNull(queue);
        
        assertFalse(queue.getInQueue());
        assertFalse(queue.getInSession());
        assertNull(queue.getAssignedResource());
        assertNull(queue.getQueuedResouce());
        
        OMElement ele = resp.getOMElement(IsUserInQueueResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("false"));
        
    }
    
    @Test
    public void testGetUserFromUserID() throws Exception
    {
        org.hibernate.Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        User user = new User("UserIdTest", "Queuer", "USER");
        ses.persist(user);
        ses.getTransaction().commit();
        
        UserIDType uid = new UserIDType();
        uid.setUserID(String.valueOf(user.getId()));
        
        Method meth = Queuer.class.getDeclaredMethod("getUserFromUserID", UserIDType.class, org.hibernate.Session.class);
        meth.setAccessible(true);
        User loaded = (User)meth.invoke(this.queuer, uid, ses);
        assertNotNull(loaded);
        
        ses.beginTransaction();
        ses.delete(user);
        ses.getTransaction().commit();
        
        assertEquals(user.getId().longValue(), loaded.getId().longValue());
        assertEquals(user.getName(), loaded.getName());
        assertEquals(user.getNamespace(), loaded.getNamespace());
        assertEquals(user.getPersona(), loaded.getPersona());
        
        ses.close();
    }
    
    @Test
    public void testGetUserFromUserIDNmNsSeq() throws Exception
    {
        org.hibernate.Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        User user = new User("UserIdTest", "Queuer", "USER");
        ses.persist(user);
        ses.getTransaction().commit();
        
        UserIDType uid = new UserIDType();
        UserNSNameSequence seq = new UserNSNameSequence();
        seq.setUserNamespace(user.getNamespace());
        seq.setUserName(user.getName());
        uid.setUserNSNameSequence(seq);
        
        Method meth = Queuer.class.getDeclaredMethod("getUserFromUserID", UserIDType.class, org.hibernate.Session.class);
        meth.setAccessible(true);
        User loaded = (User)meth.invoke(this.queuer, uid, ses);
        assertNotNull(loaded);
        
        ses.beginTransaction();
        ses.delete(user);
        ses.getTransaction().commit();
        
        assertEquals(user.getId().longValue(), loaded.getId().longValue());
        assertEquals(user.getName(), loaded.getName());
        assertEquals(user.getNamespace(), loaded.getNamespace());
        assertEquals(user.getPersona(), loaded.getPersona());
        
        ses.close();
    }
    
    @Test
    public void testGetUserFromUserIDQName() throws Exception
    {
        org.hibernate.Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        User user = new User("UserIdTest", "Queuer", "USER");
        ses.persist(user);
        ses.getTransaction().commit();
        
        UserIDType uid = new UserIDType();
        uid.setUserQName(user.getNamespace() + ":" + user.getName());
        
        Method meth = Queuer.class.getDeclaredMethod("getUserFromUserID", UserIDType.class, org.hibernate.Session.class);
        meth.setAccessible(true);
        User loaded = (User)meth.invoke(this.queuer, uid, ses);
        assertNotNull(loaded);
        
        ses.beginTransaction();
        ses.delete(user);
        ses.getTransaction().commit();
        
        assertEquals(user.getId().longValue(), loaded.getId().longValue());
        assertEquals(user.getName(), loaded.getName());
        assertEquals(user.getNamespace(), loaded.getNamespace());
        assertEquals(user.getPersona(), loaded.getPersona());
        
        ses.close();
    }
    
    @Test
    public void testGetUserFromUserIDNotExist() throws Exception
    {
        UserIDType uid = new UserIDType();
        uid.setUserQName("QU_TEST:does_not_exist");
        org.hibernate.Session ses = DataAccessActivator.getNewSession();
        
        Method meth = Queuer.class.getDeclaredMethod("getUserFromUserID", UserIDType.class, org.hibernate.Session.class);
        meth.setAccessible(true);
        assertNull(meth.invoke(this.queuer, uid, ses));
        
        ses.close();
    }

}
