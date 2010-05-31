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
 * @date 3rd April 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.queuer.impl.tests;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RequestCapabilitiesDao;
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
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.tests.DataAccessTestSetup;
import au.edu.uts.eng.remotelabs.schedserver.queuer.impl.InnerQueue;
import au.edu.uts.eng.remotelabs.schedserver.queuer.impl.Queue;

/**
 * Tests the {@link Queue} class.
 */
public class QueueTester extends TestCase
{
    /** Object of class under test. */
    private Queue queue;
    
    @Override
    @Before
    public void setUp() throws Exception
    {
        DataAccessTestSetup.setup();
        this.queue = Queue.getInstance();
        Field f = Queue.class.getDeclaredField("rigQueues");
        f.setAccessible(true);
        f.set(this.queue, new HashMap<Long, InnerQueue>());
        f = Queue.class.getDeclaredField("typeQueues");
        f.setAccessible(true);
        f.set(this.queue, new HashMap<Long, InnerQueue>());
        f = Queue.class.getDeclaredField("capabilityQueues");
        f.setAccessible(true);
        f.set(this.queue, new HashMap<Long, InnerQueue>());
        
        f = Queue.class.getDeclaredField("notTest");
        f.setAccessible(true);
        f.set(this.queue, Boolean.FALSE);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testAddEntryRig() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 10000);
        Date after = new Date(System.currentTimeMillis() + 10000);
        Date now = new Date();
        
        db.beginTransaction();
        User user1 = new User("qperm1", "testns", "USER");
        db.persist(user1);
        User user2 = new User("qperm2", "testns", "USER");
        db.persist(user2);
        User user3 = new User("qperm3", "testns", "USER");
        db.persist(user3);
        
        UserClass uc1 = new UserClass();
        uc1.setName("uc1");
        uc1.setActive(true);
        db.persist(uc1);
        
        UserAssociation ass1 = new UserAssociation(new UserAssociationId(user1.getId(), uc1.getId()), uc1, user1);
        db.persist(ass1);
        UserAssociation ass2 = new UserAssociation(new UserAssociationId(user2.getId(), uc1.getId()), uc1, user2);
        db.persist(ass2);
        UserAssociation ass3 = new UserAssociation(new UserAssociationId(user3.getId(), uc1.getId()), uc1, user3);
        db.persist(ass3);
        
        RigType rt = new RigType();
        rt.setName("Perm_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("perm,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Perm_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
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
        Session ses2 = new Session();
        ses2.setActive(true);
        ses2.setReady(true);
        ses2.setActivityLastUpdated(now);
        ses2.setExtensions((short) 5);
        ses2.setPriority((short) 5);
        ses2.setRequestTime(new Date(System.currentTimeMillis() - 1000));
        ses2.setRequestedResourceId(r.getId());
        ses2.setRequestedResourceName(r.getName());
        ses2.setResourceType("RIG");
        ses2.setResourcePermission(p1);
        ses2.setUser(user2);
        ses2.setUserName(user2.getName());
        ses2.setUserNamespace(user2.getNamespace());
        db.persist(ses2);
        Session ses3 = new Session();
        ses3.setActive(true);
        ses3.setReady(true);
        ses3.setActivityLastUpdated(now);
        ses3.setExtensions((short) 5);
        ses3.setPriority((short) 5);
        ses3.setRequestTime(new Date(System.currentTimeMillis() - 2000));
        ses3.setRequestedResourceId(r.getId());
        ses3.setRequestedResourceName(r.getName());
        ses3.setResourceType("RIG");
        ses3.setResourcePermission(p1);
        ses3.setUser(user3);
        ses3.setUserName(user3.getName());
        ses3.setUserNamespace(user3.getNamespace());
        db.persist(ses3);
        db.getTransaction().commit();
        
        this.queue.addEntry(ses1, db);
        this.queue.addEntry(ses2, db);
        this.queue.addEntry(ses3, db);
        
        db.beginTransaction();
        db.delete(ses1);
        db.delete(ses2);
        db.delete(ses3);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass1);
        db.delete(ass2);
        db.delete(ass3);
        db.delete(uc1);
        db.delete(user1);
        db.delete(user2);
        db.delete(user3);
        db.getTransaction().commit();
        
        Rig ar = ses1.getRig();
        assertNotNull(ar);
        assertEquals(r.getId(), ar.getId());
        assertEquals(r.getName(), ses1.getAssignedRigName());
        assertNotNull(ses1.getAssignmentTime());
        
        assertNull(ses2.getRig());
        assertNull(ses2.getAssignedRigName());
        assertNull(ses2.getAssignmentTime());
        assertNull(ses3.getRig());
        assertNull(ses3.getAssignedRigName());
        assertNull(ses3.getAssignmentTime());
        
        Field f = Queue.class.getDeclaredField("rigQueues");
        f.setAccessible(true);
        Map<Long, InnerQueue> que = (Map<Long, InnerQueue>) f.get(this.queue);
        assertNotNull(que);
        assertEquals(1, que.size());
        
        InnerQueue in = que.get(r.getId());
        assertNotNull(in);
        assertEquals(2, in.size());
        db.close();
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testAddEntryRigTwo() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 10000);
        Date after = new Date(System.currentTimeMillis() + 10000);
        Date now = new Date();
        
        db.beginTransaction();
        User user1 = new User("qperm1", "testns", "USER");
        db.persist(user1);
        User user2 = new User("qperm2", "testns", "USER");
        db.persist(user2);
        User user3 = new User("qperm3", "testns", "USER");
        db.persist(user3);
        
        UserClass uc1 = new UserClass();
        uc1.setName("uc1");
        uc1.setActive(true);
        db.persist(uc1);
        
        UserAssociation ass1 = new UserAssociation(new UserAssociationId(user1.getId(), uc1.getId()), uc1, user1);
        db.persist(ass1);
        UserAssociation ass2 = new UserAssociation(new UserAssociationId(user2.getId(), uc1.getId()), uc1, user2);
        db.persist(ass2);
        UserAssociation ass3 = new UserAssociation(new UserAssociationId(user3.getId(), uc1.getId()), uc1, user3);
        db.persist(ass3);
        
        RigType rt = new RigType();
        rt.setName("Perm_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("perm,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Perm_Rig_Test_Rig1");
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
        Session ses2 = new Session();
        ses2.setActive(true);
        ses2.setReady(true);
        ses2.setActivityLastUpdated(now);
        ses2.setExtensions((short) 5);
        ses2.setPriority((short) 5);
        ses2.setRequestTime(new Date(System.currentTimeMillis() - 1000));
        ses2.setRequestedResourceId(r.getId());
        ses2.setRequestedResourceName(r.getName());
        ses2.setResourceType("RIG");
        ses2.setResourcePermission(p1);
        ses2.setUser(user2);
        ses2.setUserName(user2.getName());
        ses2.setUserNamespace(user2.getNamespace());
        db.persist(ses2);
        Session ses3 = new Session();
        ses3.setActive(true);
        ses3.setReady(true);
        ses3.setActivityLastUpdated(now);
        ses3.setExtensions((short) 5);
        ses3.setPriority((short) 5);
        ses3.setRequestTime(new Date(System.currentTimeMillis() - 2000));
        ses3.setRequestedResourceId(r.getId());
        ses3.setRequestedResourceName(r.getName());
        ses3.setResourceType("RIG");
        ses3.setResourcePermission(p1);
        ses3.setUser(user3);
        ses3.setUserName(user3.getName());
        ses3.setUserNamespace(user3.getNamespace());
        db.persist(ses3);
        db.getTransaction().commit();
        
        this.queue.addEntry(ses1, db);
        this.queue.addEntry(ses2, db);
        this.queue.addEntry(ses3, db);
        
        Field f = Queue.class.getDeclaredField("rigQueues");
        f.setAccessible(true);
        Map<Long, InnerQueue> que = (Map<Long, InnerQueue>) f.get(this.queue);
        assertNotNull(que);
        assertEquals(1, que.size());
        
        InnerQueue in = que.get(r.getId());
        assertNotNull(in);
        assertEquals(3, in.size());
        
        r.setInSession(false);
        db.beginTransaction();
        db.flush();
        db.getTransaction().commit();
        
     
        this.queue.runRigAssignment(r.getId(), db);
        
        db.refresh(ses3);
        Rig ar = ses3.getRig();
        assertNotNull(ar);
        assertEquals(r.getId(), ar.getId());
        assertEquals(r.getName(), ses3.getAssignedRigName());
        assertNotNull(ses3.getAssignmentTime());
        
        assertNull(ses2.getRig());
        assertNull(ses2.getAssignedRigName());
        assertNull(ses2.getAssignmentTime());
        assertNull(ses1.getRig());
        assertNull(ses1.getAssignedRigName());
        assertNull(ses1.getAssignmentTime());

        que = (Map<Long, InnerQueue>) f.get(this.queue);
        assertNotNull(que);
        assertEquals(1, que.size());
        
        in = que.get(r.getId());
        assertNotNull(in);
        assertEquals(2, in.size());
        
        db.refresh(r);
        r.setInSession(false);
        db.beginTransaction();
        db.flush();
        db.getTransaction().commit();
        
        this.queue.runRigAssignment(r.getId(), db);
        
        db.refresh(ses2);
        ar = ses2.getRig();
        assertNotNull(ar);
        assertEquals(r.getId(), ar.getId());
        assertEquals(r.getName(), ses2.getAssignedRigName());
        assertNotNull(ses2.getAssignmentTime());

        assertNull(ses1.getRig());
        assertNull(ses1.getAssignedRigName());
        assertNull(ses1.getAssignmentTime());

        que = (Map<Long, InnerQueue>) f.get(this.queue);
        assertNotNull(que);
        assertEquals(1, que.size());
        
        in = que.get(r.getId());
        assertNotNull(in);
        assertEquals(1, in.size());
        
        db.refresh(r);
        r.setInSession(false);
        db.beginTransaction();
        db.flush();
        db.getTransaction().commit();
        this.queue.runRigAssignment(r.getId(), db);
        
        db.refresh(ses1);
        ar = ses1.getRig();
        assertNotNull(ar);
        assertEquals(r.getId(), ar.getId());
        assertEquals(r.getName(), ses1.getAssignedRigName());
        assertNotNull(ses1.getAssignmentTime());

        que = (Map<Long, InnerQueue>) f.get(this.queue);
        assertNotNull(que);
        assertEquals(0, que.size());
        
        in = que.get(r.getId());
        assertNull(in);

        db.beginTransaction();
        db.delete(ses1);
        db.delete(ses2);
        db.delete(ses3);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass1);
        db.delete(ass2);
        db.delete(ass3);
        db.delete(uc1);
        db.delete(user1);
        db.delete(user2);
        db.delete(user3);
        db.getTransaction().commit();
        db.close();
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testAddEntryRigType() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 10000);
        Date after = new Date(System.currentTimeMillis() + 10000);
        Date now = new Date();
        
        db.beginTransaction();
        User user1 = new User("qperm1", "testns", "USER");
        db.persist(user1);
        User user2 = new User("qperm2", "testns", "USER");
        db.persist(user2);

        UserClass uc1 = new UserClass();
        uc1.setName("uc1");
        uc1.setActive(true);
        db.persist(uc1);
        
        UserAssociation ass1 = new UserAssociation(new UserAssociationId(user1.getId(), uc1.getId()), uc1, user1);
        db.persist(ass1);
        UserAssociation ass2 = new UserAssociation(new UserAssociationId(user2.getId(), uc1.getId()), uc1, user2);
        db.persist(ass2);

        RigType rt = new RigType();
        rt.setName("Perm_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("perm,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Perm_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("TYPE");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRigType(rt);
        db.persist(p1);
        
        Session ses1 = new Session();
        ses1.setActive(true);
        ses1.setReady(true);
        ses1.setActivityLastUpdated(now);
        ses1.setExtensions((short) 5);
        ses1.setPriority((short) 5);
        ses1.setRequestTime(now);
        ses1.setRequestedResourceId(rt.getId());
        ses1.setRequestedResourceName(rt.getName());
        ses1.setResourceType("TYPE");
        ses1.setResourcePermission(p1);
        ses1.setUser(user1);
        ses1.setUserName(user1.getName());
        ses1.setUserNamespace(user1.getNamespace());
        db.persist(ses1);
        Session ses2 = new Session();
        ses2.setActive(true);
        ses2.setReady(true);
        ses2.setActivityLastUpdated(now);
        ses2.setExtensions((short) 5);
        ses2.setPriority((short) 5);
        ses2.setRequestTime(new Date(System.currentTimeMillis() - 1000));
        ses2.setRequestedResourceId(rt.getId());
        ses2.setRequestedResourceName(rt.getName());
        ses2.setResourceType("TYPE");
        ses2.setResourcePermission(p1);
        ses2.setUser(user2);
        ses2.setUserName(user2.getName());
        ses2.setUserNamespace(user2.getNamespace());
        db.persist(ses2);   
        db.getTransaction().commit();
        
        /*** Add the entries to the queue. ************************************/
        this.queue.addEntry(ses1, db);
        this.queue.addEntry(ses2, db);
        
        Field f = Queue.class.getDeclaredField("typeQueues");
        f.setAccessible(true);
        Map<Long, InnerQueue> que = (Map<Long, InnerQueue>) f.get(this.queue);
        assertNotNull(que);
        assertEquals(1, que.size());
        
        InnerQueue in = que.get(rt.getId());
        assertNotNull(in);
        assertEquals(2, in.size());
        
        r.setInSession(false);
        db.beginTransaction();
        db.flush();
        db.getTransaction().commit();
        /*** Run assignment. **************************************************/
        this.queue.runTypeAssignment(rt.getId(), db);

        assertEquals(1, que.size());
        in = que.get(rt.getId());
        assertNotNull(in);
        assertEquals(1, in.size());
        
        assertNotNull(ses2.getAssignmentTime());
        assertEquals(r.getName(), ses2.getAssignedRigName());
        assertEquals(r.getId(), ses2.getRig().getId());
        
        r.setInSession(false);
        db.beginTransaction();
        db.flush();
        db.getTransaction().commit();
        /*** Run assignment again.*********************************************/
        this.queue.runRigAssignment(r.getId(), db);

        assertEquals(0, que.size());
        in = que.get(rt.getId());
        assertNull(in);
        
        assertNotNull(ses1.getAssignmentTime());
        assertEquals(r.getName(), ses1.getAssignedRigName());
        assertEquals(r.getId(), ses1.getRig().getId());

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
        db.close();
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testAddEntryRigCaps() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 10000);
        Date after = new Date(System.currentTimeMillis() + 10000);
        Date now = new Date();
        
        db.beginTransaction();
        RigCapabilities caps = new RigCapabilities("perm,test,rig,type");
        db.persist(caps);
        db.getTransaction().commit();
        
        RequestCapabilitiesDao dao = new RequestCapabilitiesDao(db);
        RequestCapabilities reqCaps = dao.addCapabilities("perm,type");
        
        db.beginTransaction();
        User user1 = new User("qperm1", "testns", "USER");
        db.persist(user1);
        User user2 = new User("qperm2", "testns", "USER");
        db.persist(user2);
        User user3 = new User("qperm3", "testns", "USER");
        db.persist(user3);
        
        UserClass uc1 = new UserClass();
        uc1.setName("uc1");
        uc1.setActive(true);
        db.persist(uc1);
        
        UserAssociation ass1 = new UserAssociation(new UserAssociationId(user1.getId(), uc1.getId()), uc1, user1);
        db.persist(ass1);
        UserAssociation ass2 = new UserAssociation(new UserAssociationId(user2.getId(), uc1.getId()), uc1, user2);
        db.persist(ass2);
        UserAssociation ass3 = new UserAssociation(new UserAssociationId(user3.getId(), uc1.getId()), uc1, user3);
        db.persist(ass3);
        
        RigType rt = new RigType();
        rt.setName("Perm_Test_Rig_Type");
        db.persist(rt);
        
        Rig r = new Rig();
        r.setName("Perm_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(false);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("CAPABILITY");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRequestCapabilities(reqCaps);
        db.persist(p1);
        
        Session ses1 = new Session();
        ses1.setActive(true);
        ses1.setReady(true);
        ses1.setActivityLastUpdated(now);
        ses1.setExtensions((short) 5);
        ses1.setPriority((short) 5);
        ses1.setRequestTime(now);
        ses1.setRequestedResourceId(reqCaps.getId());
        ses1.setRequestedResourceName(reqCaps.getCapabilities());
        ses1.setResourceType("CAPABILITY");
        ses1.setResourcePermission(p1);
        ses1.setUser(user1);
        ses1.setUserName(user1.getName());
        ses1.setUserNamespace(user1.getNamespace());
        db.persist(ses1);
        Session ses2 = new Session();
        ses2.setActive(true);
        ses2.setReady(true);
        ses2.setActivityLastUpdated(now);
        ses2.setExtensions((short) 5);
        ses2.setPriority((short) 5);
        ses2.setRequestTime(new Date(System.currentTimeMillis() - 1000));
        ses2.setRequestedResourceId(reqCaps.getId());
        ses2.setRequestedResourceName(reqCaps.getCapabilities());
        ses2.setResourceType("CAPABILITY");
        ses2.setResourcePermission(p1);
        ses2.setUser(user2);
        ses2.setUserName(user2.getName());
        ses2.setUserNamespace(user2.getNamespace());
        db.persist(ses2);
        Session ses3 = new Session();
        ses3.setActive(true);
        ses3.setReady(true);
        ses3.setActivityLastUpdated(now);
        ses3.setExtensions((short) 5);
        ses3.setPriority((short) 5);
        ses3.setRequestTime(new Date(System.currentTimeMillis() - 2000));
        ses3.setRequestedResourceId(r.getId());
        ses3.setRequestedResourceName(r.getName());
        ses3.setResourceType("RIG");
        ses3.setResourcePermission(p1);
        ses3.setUser(user3);
        ses3.setUserName(user3.getName());
        ses3.setUserNamespace(user3.getNamespace());
        db.persist(ses3);
        db.getTransaction().commit();
        
        db.refresh(caps);
        db.refresh(reqCaps);
        
        this.queue.addEntry(ses1, db);
        this.queue.addEntry(ses2, db);
        this.queue.addEntry(ses3, db);
        
        db.beginTransaction();
        db.delete(ses1);
        db.delete(ses2);
        db.delete(ses3);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(ass1);
        db.delete(ass2);
        db.delete(ass3);
        db.delete(uc1);
        db.delete(user1);
        db.delete(user2);
        db.delete(user3);
        db.getTransaction().commit();
        
        dao.delete(reqCaps);
        db.beginTransaction();
        db.delete(caps);
        db.getTransaction().commit();
        
        Rig ar = ses1.getRig();
        assertNotNull(ar);
        assertEquals(r.getId(), ar.getId());
        assertEquals(r.getName(), ses1.getAssignedRigName());
        assertNotNull(ses1.getAssignmentTime());
        
        assertNull(ses2.getRig());
        assertNull(ses2.getAssignedRigName());
        assertNull(ses2.getAssignmentTime());
        assertNull(ses3.getRig());
        assertNull(ses3.getAssignedRigName());
        assertNull(ses3.getAssignmentTime());
        
        Field f = Queue.class.getDeclaredField("capabilityQueues");
        f.setAccessible(true);
        Map<Long, InnerQueue> que = (Map<Long, InnerQueue>) f.get(this.queue);
        assertNotNull(que);
        assertEquals(1, que.size());
        
        InnerQueue in = que.get(reqCaps.getId());
        assertNotNull(in);
        assertEquals(1, in.size());
        db.close();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testAddEntry() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 10000);
        Date after = new Date(System.currentTimeMillis() + 10000);
        Date now = new Date();
        
        db.beginTransaction();
        User user1 = new User("qperm1", "testns", "USER");
        db.persist(user1);
        User user2 = new User("qperm2", "testns", "USER");
        db.persist(user2);
        User user3 = new User("qperm3", "testns", "USER");
        db.persist(user3);
        User user4 = new User("qperm4", "testns", "USER");
        db.persist(user4);
        User user5 = new User("qperm5", "testns", "USER");
        db.persist(user5);
        
        UserClass uc1 = new UserClass();
        uc1.setName("uc1");
        uc1.setActive(true);
        db.persist(uc1);
        
        UserAssociation ass1 = new UserAssociation(new UserAssociationId(user1.getId(), uc1.getId()), uc1, user1);
        db.persist(ass1);
        UserAssociation ass2 = new UserAssociation(new UserAssociationId(user2.getId(), uc1.getId()), uc1, user2);
        db.persist(ass2);
        UserAssociation ass3 = new UserAssociation(new UserAssociationId(user3.getId(), uc1.getId()), uc1, user3);
        db.persist(ass3);
        UserAssociation ass4 = new UserAssociation(new UserAssociationId(user4.getId(), uc1.getId()), uc1, user4);
        db.persist(ass4);
        UserAssociation ass5 = new UserAssociation(new UserAssociationId(user5.getId(), uc1.getId()), uc1, user5);
        db.persist(ass5);
        
        RigType rt = new RigType();
        rt.setName("Perm_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("perm,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Perm_Rig_Test_Rig1");
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
        Session ses2 = new Session();
        ses2.setActive(true);
        ses2.setReady(true);
        ses2.setActivityLastUpdated(now);
        ses2.setExtensions((short) 5);
        ses2.setPriority((short) 5);
        ses2.setRequestTime(new Date(System.currentTimeMillis() - 1000));
        ses2.setRequestedResourceId(r.getId());
        ses2.setRequestedResourceName(r.getName());
        ses2.setResourceType("RIG");
        ses2.setResourcePermission(p1);
        ses2.setUser(user2);
        ses2.setUserName(user2.getName());
        ses2.setUserNamespace(user2.getNamespace());
        db.persist(ses2);
        Session ses3 = new Session();
        ses3.setActive(true);
        ses3.setReady(true);
        ses3.setActivityLastUpdated(now);
        ses3.setExtensions((short) 5);
        ses3.setPriority((short) 5);
        ses3.setRequestTime(new Date(System.currentTimeMillis() - 2000));
        ses3.setRequestedResourceId(r.getId());
        ses3.setRequestedResourceName(r.getName());
        ses3.setResourceType("RIG");
        ses3.setResourcePermission(p1);
        ses3.setUser(user3);
        ses3.setUserName(user3.getName());
        ses3.setUserNamespace(user3.getNamespace());
        db.persist(ses3);
        Session ses4 = new Session();
        ses4.setActive(true);
        ses4.setReady(true);
        ses4.setActivityLastUpdated(now);
        ses4.setExtensions((short) 5);
        ses4.setPriority((short) 5);
        ses4.setRequestTime(new Date(System.currentTimeMillis() - 3000));
        ses4.setRequestedResourceId(r.getId());
        ses4.setRequestedResourceName(r.getName());
        ses4.setResourceType("RIG");
        ses4.setResourcePermission(p1);
        ses4.setUser(user4);
        ses4.setUserName(user4.getName());
        ses4.setUserNamespace(user4.getNamespace());
        db.persist(ses4);
        Session ses5 = new Session();
        ses5.setActive(true);
        ses5.setReady(true);
        ses5.setActivityLastUpdated(now);
        ses5.setExtensions((short) 5);
        ses5.setPriority((short) 5);
        ses5.setRequestTime(new Date(System.currentTimeMillis() - 4000));
        ses5.setRequestedResourceId(r.getId());
        ses5.setRequestedResourceName(r.getName());
        ses5.setResourceType("RIG");
        ses5.setResourcePermission(p1);
        ses5.setUser(user5);
        ses5.setUserName(user5.getName());
        ses5.setUserNamespace(user5.getNamespace());
        db.persist(ses5);
        db.getTransaction().commit();
        
        this.queue.addEntry(ses1, db);
        this.queue.addEntry(ses2, db);
        this.queue.addEntry(ses3, db);
        this.queue.addEntry(ses4, db);
        this.queue.addEntry(ses5, db);
        
        db.beginTransaction();
        db.delete(ses1);
        db.delete(ses2);
        db.delete(ses3);
        db.delete(ses4);
        db.delete(ses5);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass1);
        db.delete(ass2);
        db.delete(ass3);
        db.delete(ass4);
        db.delete(ass5);
        db.delete(uc1);
        db.delete(user1);
        db.delete(user2);
        db.delete(user3);
        db.delete(user4);
        db.delete(user5);
        db.getTransaction().commit();
        
        Field f = Queue.class.getDeclaredField("rigQueues");
        f.setAccessible(true);
        Map<Long, InnerQueue> que = (Map<Long, InnerQueue>) f.get(this.queue);
        assertNotNull(que);
        assertEquals(1, que.size());
        
        InnerQueue in = que.get(r.getId());
        assertNotNull(in);
        assertEquals(5, in.size());
        
        Session s = in.getHead();
        assertNotNull(s);
        assertEquals(ses5.getId(), s.getId());
        assertEquals((long)ses5.getRequestedResourceId(), in.getResourceId());
        assertEquals(ses5.getRequestedResourceName(), in.getResourceName());
        assertEquals(ses5.getResourceType(), in.getResourceType());
        
        s = in.getHead();
        assertNotNull(s);
        assertEquals(ses4.getId(), s.getId());
        assertEquals((long)ses4.getRequestedResourceId(), in.getResourceId());
        assertEquals(ses4.getRequestedResourceName(), in.getResourceName());
        assertEquals(ses4.getResourceType(), in.getResourceType());
        
        s = in.getHead();
        assertNotNull(s);
        assertEquals(ses3.getId(), s.getId());
        assertEquals((long)ses3.getRequestedResourceId(), in.getResourceId());
        assertEquals(ses3.getRequestedResourceName(), in.getResourceName());
        assertEquals(ses3.getResourceType(), in.getResourceType());
        
        s = in.getHead();
        assertNotNull(s);
        assertEquals(ses2.getId(), s.getId());
        assertEquals((long)ses2.getRequestedResourceId(), in.getResourceId());
        assertEquals(ses2.getRequestedResourceName(), in.getResourceName());
        assertEquals(ses2.getResourceType(), in.getResourceType());
        
        s = in.getHead();
        assertNotNull(s);
        assertEquals(ses1.getId(), s.getId());
        assertEquals((long)ses1.getRequestedResourceId(), in.getResourceId());
        assertEquals(ses1.getRequestedResourceName(), in.getResourceName());
        assertEquals(ses1.getResourceType(), in.getResourceType());
        db.close();
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testAddEntryType() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 10000);
        Date after = new Date(System.currentTimeMillis() + 10000);
        Date now = new Date();
        
        db.beginTransaction();
        User user1 = new User("qperm11", "testns", "USER");
        db.persist(user1);
        User user2 = new User("qperm22", "testns", "USER");
        db.persist(user2);
        User user3 = new User("qperm32", "testns", "USER");
        db.persist(user3);
        User user4 = new User("qperm42", "testns", "USER");
        db.persist(user4);
        User user5 = new User("qperm52", "testns", "USER");
        db.persist(user5);
        
        UserClass uc1 = new UserClass();
        uc1.setName("uc1");
        uc1.setActive(true);
        db.persist(uc1);
        
        UserAssociation ass1 = new UserAssociation(new UserAssociationId(user1.getId(), uc1.getId()), uc1, user1);
        db.persist(ass1);
        UserAssociation ass2 = new UserAssociation(new UserAssociationId(user2.getId(), uc1.getId()), uc1, user2);
        db.persist(ass2);
        UserAssociation ass3 = new UserAssociation(new UserAssociationId(user3.getId(), uc1.getId()), uc1, user3);
        db.persist(ass3);
        UserAssociation ass4 = new UserAssociation(new UserAssociationId(user4.getId(), uc1.getId()), uc1, user4);
        db.persist(ass4);
        UserAssociation ass5 = new UserAssociation(new UserAssociationId(user5.getId(), uc1.getId()), uc1, user5);
        db.persist(ass5);
        
        RigType rt = new RigType();
        rt.setName("Perm_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("perm,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Perm_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("TYPE");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRigType(rt);
        db.persist(p1);
        
        Session ses1 = new Session();
        ses1.setActive(true);
        ses1.setReady(true);
        ses1.setActivityLastUpdated(now);
        ses1.setExtensions((short) 5);
        ses1.setPriority((short) 5);
        ses1.setRequestTime(now);
        ses1.setRequestedResourceId(rt.getId());
        ses1.setRequestedResourceName(rt.getName());
        ses1.setResourceType("TYPE");
        ses1.setResourcePermission(p1);
        ses1.setUser(user1);
        ses1.setUserName(user1.getName());
        ses1.setUserNamespace(user1.getNamespace());
        db.persist(ses1);
        Session ses2 = new Session();
        ses2.setActive(true);
        ses2.setReady(true);
        ses2.setActivityLastUpdated(now);
        ses2.setExtensions((short) 5);
        ses2.setPriority((short) 5);
        ses2.setRequestTime(new Date(System.currentTimeMillis() - 1000));
        ses2.setRequestedResourceId(rt.getId());
        ses2.setRequestedResourceName(rt.getName());
        ses2.setResourceType("TYPE");
        ses2.setResourcePermission(p1);
        ses2.setUser(user2);
        ses2.setUserName(user2.getName());
        ses2.setUserNamespace(user2.getNamespace());
        db.persist(ses2);
        Session ses3 = new Session();
        ses3.setActive(true);
        ses3.setReady(true);
        ses3.setActivityLastUpdated(now);
        ses3.setExtensions((short) 5);
        ses3.setPriority((short) 5);
        ses3.setRequestTime(new Date(System.currentTimeMillis() - 2000));
        ses3.setRequestedResourceId(rt.getId());
        ses3.setRequestedResourceName(rt.getName());
        ses3.setResourceType("TYPE");
        ses3.setResourcePermission(p1);
        ses3.setUser(user3);
        ses3.setUserName(user3.getName());
        ses3.setUserNamespace(user3.getNamespace());
        db.persist(ses3);
        Session ses4 = new Session();
        ses4.setActive(true);
        ses4.setReady(true);
        ses4.setActivityLastUpdated(now);
        ses4.setExtensions((short) 5);
        ses4.setPriority((short) 5);
        ses4.setRequestTime(new Date(System.currentTimeMillis() - 3000));
        ses4.setRequestedResourceId(rt.getId());
        ses4.setRequestedResourceName(rt.getName());
        ses4.setResourceType("TYPE");
        ses4.setResourcePermission(p1);
        ses4.setUser(user4);
        ses4.setUserName(user4.getName());
        ses4.setUserNamespace(user4.getNamespace());
        db.persist(ses4);
        Session ses5 = new Session();
        ses5.setActive(true);
        ses5.setReady(true);
        ses5.setActivityLastUpdated(now);
        ses5.setExtensions((short) 5);
        ses5.setPriority((short) 5);
        ses5.setRequestTime(new Date(System.currentTimeMillis() - 4000));
        ses5.setRequestedResourceId(rt.getId());
        ses5.setRequestedResourceName(rt.getName());
        ses5.setResourceType("TYPE");
        ses5.setResourcePermission(p1);
        ses5.setUser(user5);
        ses5.setUserName(user5.getName());
        ses5.setUserNamespace(user5.getNamespace());
        db.persist(ses5);
        db.getTransaction().commit();
        
        this.queue.addEntry(ses1, db);
        this.queue.addEntry(ses2, db);
        this.queue.addEntry(ses3, db);
        this.queue.addEntry(ses4, db);
        this.queue.addEntry(ses5, db);
        
        db.beginTransaction();
        db.delete(ses1);
        db.delete(ses2);
        db.delete(ses3);
        db.delete(ses4);
        db.delete(ses5);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass1);
        db.delete(ass2);
        db.delete(ass3);
        db.delete(ass4);
        db.delete(ass5);
        db.delete(uc1);
        db.delete(user1);
        db.delete(user2);
        db.delete(user3);
        db.delete(user4);
        db.delete(user5);
        db.getTransaction().commit();
        
        Field f = Queue.class.getDeclaredField("typeQueues");
        f.setAccessible(true);
        Map<Long, InnerQueue> que = (Map<Long, InnerQueue>) f.get(this.queue);
        assertNotNull(que);
        assertEquals(1, que.size());
        
        InnerQueue in = que.get(rt.getId());
        assertNotNull(in);
        assertEquals(5, in.size());
        
        Session s = in.getHead();
        assertNotNull(s);
        assertEquals(ses5.getId(), s.getId());
        assertEquals((long)ses5.getRequestedResourceId(), in.getResourceId());
        assertEquals(ses5.getRequestedResourceName(), in.getResourceName());
        assertEquals(ses5.getResourceType(), in.getResourceType());
        
        s = in.getHead();
        assertNotNull(s);
        assertEquals(ses4.getId(), s.getId());
        assertEquals((long)ses4.getRequestedResourceId(), in.getResourceId());
        assertEquals(ses4.getRequestedResourceName(), in.getResourceName());
        assertEquals(ses4.getResourceType(), in.getResourceType());
        
        s = in.getHead();
        assertNotNull(s);
        assertEquals(ses3.getId(), s.getId());
        assertEquals((long)ses3.getRequestedResourceId(), in.getResourceId());
        assertEquals(ses3.getRequestedResourceName(), in.getResourceName());
        assertEquals(ses3.getResourceType(), in.getResourceType());
        
        s = in.getHead();
        assertNotNull(s);
        assertEquals(ses2.getId(), s.getId());
        assertEquals((long)ses2.getRequestedResourceId(), in.getResourceId());
        assertEquals(ses2.getRequestedResourceName(), in.getResourceName());
        assertEquals(ses2.getResourceType(), in.getResourceType());
        
        s = in.getHead();
        assertNotNull(s);
        assertEquals(ses1.getId(), s.getId());
        assertEquals((long)ses1.getRequestedResourceId(), in.getResourceId());
        assertEquals(ses1.getRequestedResourceName(), in.getResourceName());
        assertEquals(ses1.getResourceType(), in.getResourceType());
        db.close();
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testAddEntryCaps() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 10000);
        Date after = new Date(System.currentTimeMillis() + 10000);
        Date now = new Date();
        
        db.beginTransaction();
        User user1 = new User("qperm1", "testns", "USER");
        db.persist(user1);
        User user2 = new User("qperm2", "testns", "USER");
        db.persist(user2);
        User user3 = new User("qperm3", "testns", "USER");
        db.persist(user3);
        User user4 = new User("qperm4", "testns", "USER");
        db.persist(user4);
        User user5 = new User("qperm5", "testns", "USER");
        db.persist(user5);
        
        UserClass uc1 = new UserClass();
        uc1.setName("uc1");
        uc1.setActive(true);
        db.persist(uc1);
        
        UserAssociation ass1 = new UserAssociation(new UserAssociationId(user1.getId(), uc1.getId()), uc1, user1);
        db.persist(ass1);
        UserAssociation ass2 = new UserAssociation(new UserAssociationId(user2.getId(), uc1.getId()), uc1, user2);
        db.persist(ass2);
        UserAssociation ass3 = new UserAssociation(new UserAssociationId(user3.getId(), uc1.getId()), uc1, user3);
        db.persist(ass3);
        UserAssociation ass4 = new UserAssociation(new UserAssociationId(user4.getId(), uc1.getId()), uc1, user4);
        db.persist(ass4);
        UserAssociation ass5 = new UserAssociation(new UserAssociationId(user5.getId(), uc1.getId()), uc1, user5);
        db.persist(ass5);
        
        RigType rt = new RigType();
        rt.setName("Perm_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("perm,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Perm_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(false);
        db.persist(r);
        
        RequestCapabilities reqCaps = new RequestCapabilities("perm,test");
        db.persist(reqCaps);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("CAPABILITY");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRequestCapabilities(reqCaps);
        db.persist(p1);
        
        Session ses1 = new Session();
        ses1.setActive(true);
        ses1.setReady(true);
        ses1.setActivityLastUpdated(now);
        ses1.setExtensions((short) 5);
        ses1.setPriority((short) 5);
        ses1.setRequestTime(now);
        ses1.setRequestedResourceId(reqCaps.getId());
        ses1.setRequestedResourceName(reqCaps.getCapabilities());
        ses1.setResourceType("CAPABILITY");
        ses1.setResourcePermission(p1);
        ses1.setUser(user1);
        ses1.setUserName(user1.getName());
        ses1.setUserNamespace(user1.getNamespace());
        db.persist(ses1);
        Session ses2 = new Session();
        ses2.setActive(true);
        ses2.setReady(true);
        ses2.setActivityLastUpdated(now);
        ses2.setExtensions((short) 5);
        ses2.setPriority((short) 5);
        ses2.setRequestTime(new Date(System.currentTimeMillis() - 1000));
        ses2.setRequestedResourceId(reqCaps.getId());
        ses2.setRequestedResourceName(reqCaps.getCapabilities());
        ses2.setResourceType("CAPABILITY");
        ses2.setResourcePermission(p1);
        ses2.setUser(user2);
        ses2.setUserName(user2.getName());
        ses2.setUserNamespace(user2.getNamespace());
        db.persist(ses2);
        Session ses3 = new Session();
        ses3.setActive(true);
        ses3.setReady(true);
        ses3.setActivityLastUpdated(now);
        ses3.setExtensions((short) 5);
        ses3.setPriority((short) 5);
        ses3.setRequestTime(new Date(System.currentTimeMillis() - 2000));
        ses3.setRequestedResourceId(reqCaps.getId());
        ses3.setRequestedResourceName(reqCaps.getCapabilities());
        ses3.setResourceType("CAPABILITY");
        ses3.setResourcePermission(p1);
        ses3.setUser(user3);
        ses3.setUserName(user3.getName());
        ses3.setUserNamespace(user3.getNamespace());
        db.persist(ses3);
        Session ses4 = new Session();
        ses4.setActive(true);
        ses4.setReady(true);
        ses4.setActivityLastUpdated(now);
        ses4.setExtensions((short) 5);
        ses4.setPriority((short) 5);
        ses4.setRequestTime(new Date(System.currentTimeMillis() - 3000));
        ses4.setRequestedResourceId(reqCaps.getId());
        ses4.setRequestedResourceName(reqCaps.getCapabilities());
        ses4.setResourceType("CAPABILITY");
        ses4.setResourcePermission(p1);
        ses4.setUser(user4);
        ses4.setUserName(user4.getName());
        ses4.setUserNamespace(user4.getNamespace());
        db.persist(ses4);
        Session ses5 = new Session();
        ses5.setActive(true);
        ses5.setReady(true);
        ses5.setActivityLastUpdated(now);
        ses5.setExtensions((short) 5);
        ses5.setPriority((short) 5);
        ses5.setRequestTime(new Date(System.currentTimeMillis() - 4000));
        ses5.setRequestedResourceId(reqCaps.getId());
        ses5.setRequestedResourceName(reqCaps.getCapabilities());
        ses5.setResourceType("CAPABILITY");
        ses5.setResourcePermission(p1);
        ses5.setUser(user5);
        ses5.setUserName(user5.getName());
        ses5.setUserNamespace(user5.getNamespace());
        db.persist(ses5);
        db.getTransaction().commit();
        
        this.queue.addEntry(ses1, db);
        this.queue.addEntry(ses2, db);
        this.queue.addEntry(ses3, db);
        this.queue.addEntry(ses4, db);
        this.queue.addEntry(ses5, db);
        
        db.beginTransaction();
        db.delete(ses1);
        db.delete(ses2);
        db.delete(ses3);
        db.delete(ses4);
        db.delete(ses5);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(reqCaps);
        db.delete(caps);
        db.delete(ass1);
        db.delete(ass2);
        db.delete(ass3);
        db.delete(ass4);
        db.delete(ass5);
        db.delete(uc1);
        db.delete(user1);
        db.delete(user2);
        db.delete(user3);
        db.delete(user4);
        db.delete(user5);
        db.getTransaction().commit();
        
        Field f = Queue.class.getDeclaredField("capabilityQueues");
        f.setAccessible(true);
        Map<Long, InnerQueue> que = (Map<Long, InnerQueue>) f.get(this.queue);
        assertNotNull(que);
        assertEquals(1, que.size());
        
        InnerQueue in = que.get(reqCaps.getId());
        assertNotNull(in);
        assertEquals(5, in.size());
        
        Session s = in.getHead();
        assertNotNull(s);
        assertEquals(ses5.getId(), s.getId());
        assertEquals((long)ses5.getRequestedResourceId(), in.getResourceId());
        assertEquals(ses5.getRequestedResourceName(), in.getResourceName());
        assertEquals(ses5.getResourceType(), in.getResourceType());
        
        s = in.getHead();
        assertNotNull(s);
        assertEquals(ses4.getId(), s.getId());
        assertEquals((long)ses4.getRequestedResourceId(), in.getResourceId());
        assertEquals(ses4.getRequestedResourceName(), in.getResourceName());
        assertEquals(ses4.getResourceType(), in.getResourceType());
        
        s = in.getHead();
        assertNotNull(s);
        assertEquals(ses3.getId(), s.getId());
        assertEquals((long)ses3.getRequestedResourceId(), in.getResourceId());
        assertEquals(ses3.getRequestedResourceName(), in.getResourceName());
        assertEquals(ses3.getResourceType(), in.getResourceType());
        
        s = in.getHead();
        assertNotNull(s);
        assertEquals(ses2.getId(), s.getId());
        assertEquals((long)ses2.getRequestedResourceId(), in.getResourceId());
        assertEquals(ses2.getRequestedResourceName(), in.getResourceName());
        assertEquals(ses2.getResourceType(), in.getResourceType());
        
        s = in.getHead();
        assertNotNull(s);
        assertEquals(ses1.getId(), s.getId());
        assertEquals((long)ses1.getRequestedResourceId(), in.getResourceId());
        assertEquals(ses1.getRequestedResourceName(), in.getResourceName());
        assertEquals(ses1.getResourceType(), in.getResourceType());
        db.close();
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testAddEntryTypeMultiple() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 10000);
        Date after = new Date(System.currentTimeMillis() + 10000);
        Date now = new Date();
        
        db.beginTransaction();
        User user1 = new User("qperm1", "testns", "USER");
        db.persist(user1);
        User user2 = new User("qperm2", "testns", "USER");
        db.persist(user2);
        User user3 = new User("qperm3", "testns", "USER");
        db.persist(user3);
        User user4 = new User("qperm4", "testns", "USER");
        db.persist(user4);
        User user5 = new User("qperm5", "testns", "USER");
        db.persist(user5);
        
        UserClass uc1 = new UserClass();
        uc1.setName("uc1");
        uc1.setActive(true);
        db.persist(uc1);
        
        UserAssociation ass1 = new UserAssociation(new UserAssociationId(user1.getId(), uc1.getId()), uc1, user1);
        db.persist(ass1);
        UserAssociation ass2 = new UserAssociation(new UserAssociationId(user2.getId(), uc1.getId()), uc1, user2);
        db.persist(ass2);
        UserAssociation ass3 = new UserAssociation(new UserAssociationId(user3.getId(), uc1.getId()), uc1, user3);
        db.persist(ass3);
        UserAssociation ass4 = new UserAssociation(new UserAssociationId(user4.getId(), uc1.getId()), uc1, user4);
        db.persist(ass4);
        UserAssociation ass5 = new UserAssociation(new UserAssociationId(user5.getId(), uc1.getId()), uc1, user5);
        db.persist(ass5);
        
        RigType rt = new RigType();
        rt.setName("Perm_Test_Rig_Type");
        db.persist(rt);
        
        RigType rt2 = new RigType();
        rt2.setName("Perm_Test_Rig_Type22");
        db.persist(rt2);
        
        RigCapabilities caps = new RigCapabilities("perm,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Perm_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("TYPE");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRigType(rt);
        db.persist(p1);
        
        Session ses1 = new Session();
        ses1.setActive(true);
        ses1.setReady(true);
        ses1.setActivityLastUpdated(now);
        ses1.setExtensions((short) 5);
        ses1.setPriority((short) 5);
        ses1.setRequestTime(now);
        ses1.setRequestedResourceId(rt.getId());
        ses1.setRequestedResourceName(rt.getName());
        ses1.setResourceType("TYPE");
        ses1.setResourcePermission(p1);
        ses1.setUser(user1);
        ses1.setUserName(user1.getName());
        ses1.setUserNamespace(user1.getNamespace());
        db.persist(ses1);
        Session ses2 = new Session();
        ses2.setActive(true);
        ses2.setReady(true);
        ses2.setActivityLastUpdated(now);
        ses2.setExtensions((short) 5);
        ses2.setPriority((short) 5);
        ses2.setRequestTime(new Date(System.currentTimeMillis() - 1000));
        ses2.setRequestedResourceId(rt.getId());
        ses2.setRequestedResourceName(rt.getName());
        ses2.setResourceType("TYPE");
        ses2.setResourcePermission(p1);
        ses2.setUser(user2);
        ses2.setUserName(user2.getName());
        ses2.setUserNamespace(user2.getNamespace());
        db.persist(ses2);
        Session ses3 = new Session();
        ses3.setActive(true);
        ses3.setReady(true);
        ses3.setActivityLastUpdated(now);
        ses3.setExtensions((short) 5);
        ses3.setPriority((short) 5);
        ses3.setRequestTime(new Date(System.currentTimeMillis() - 2000));
        ses3.setRequestedResourceId(rt.getId());
        ses3.setRequestedResourceName(rt.getName());
        ses3.setResourceType("TYPE");
        ses3.setResourcePermission(p1);
        ses3.setUser(user3);
        ses3.setUserName(user3.getName());
        ses3.setUserNamespace(user3.getNamespace());
        db.persist(ses3);
        Session ses4 = new Session();
        ses4.setActive(true);
        ses4.setReady(true);
        ses4.setActivityLastUpdated(now);
        ses4.setExtensions((short) 5);
        ses4.setPriority((short) 5);
        ses4.setRequestTime(new Date(System.currentTimeMillis() - 3000));
        ses4.setRequestedResourceId(rt2.getId());
        ses4.setRequestedResourceName(rt2.getName());
        ses4.setResourceType("TYPE");
        ses4.setResourcePermission(p1);
        ses4.setUser(user4);
        ses4.setUserName(user4.getName());
        ses4.setUserNamespace(user4.getNamespace());
        db.persist(ses4);
        Session ses5 = new Session();
        ses5.setActive(true);
        ses5.setReady(true);
        ses5.setActivityLastUpdated(now);
        ses5.setExtensions((short) 5);
        ses5.setPriority((short) 5);
        ses5.setRequestTime(new Date(System.currentTimeMillis() - 4000));
        ses5.setRequestedResourceId(rt2.getId());
        ses5.setRequestedResourceName(rt2.getName());
        ses5.setResourceType("TYPE");
        ses5.setResourcePermission(p1);
        ses5.setUser(user5);
        ses5.setUserName(user5.getName());
        ses5.setUserNamespace(user5.getNamespace());
        db.persist(ses5);
        db.getTransaction().commit();
        
        this.queue.addEntry(ses1, db);
        this.queue.addEntry(ses2, db);
        this.queue.addEntry(ses3, db);
        this.queue.addEntry(ses4, db);
        this.queue.addEntry(ses5, db);
        
        db.beginTransaction();
        db.delete(ses1);
        db.delete(ses2);
        db.delete(ses3);
        db.delete(ses4);
        db.delete(ses5);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(rt2);
        db.delete(caps);
        db.delete(ass1);
        db.delete(ass2);
        db.delete(ass3);
        db.delete(ass4);
        db.delete(ass5);
        db.delete(uc1);
        db.delete(user1);
        db.delete(user2);
        db.delete(user3);
        db.delete(user4);
        db.delete(user5);
        db.getTransaction().commit();
        
        Field f = Queue.class.getDeclaredField("typeQueues");
        f.setAccessible(true);
        Map<Long, InnerQueue> que = (Map<Long, InnerQueue>) f.get(this.queue);
        assertNotNull(que);
        assertEquals(2, que.size());
        
        InnerQueue in = que.get(rt.getId());
        assertNotNull(in);
        assertEquals(3, in.size());
        
        Session s = in.getHead();
        assertNotNull(s);
        assertEquals(ses3.getId(), s.getId());
        assertEquals((long)ses3.getRequestedResourceId(), in.getResourceId());
        assertEquals(ses3.getRequestedResourceName(), in.getResourceName());
        assertEquals(ses3.getResourceType(), in.getResourceType());
        
        s = in.getHead();
        assertNotNull(s);
        assertEquals(ses2.getId(), s.getId());
        assertEquals((long)ses2.getRequestedResourceId(), in.getResourceId());
        assertEquals(ses2.getRequestedResourceName(), in.getResourceName());
        assertEquals(ses2.getResourceType(), in.getResourceType());
        
        s = in.getHead();
        assertNotNull(s);
        assertEquals(ses1.getId(), s.getId());
        assertEquals((long)ses1.getRequestedResourceId(), in.getResourceId());
        assertEquals(ses1.getRequestedResourceName(), in.getResourceName());
        assertEquals(ses1.getResourceType(), in.getResourceType());
        
        in = que.get(rt2.getId());
        assertEquals(2, in.size());
        s = in.getHead();
        assertNotNull(s);
        assertEquals(ses5.getId(), s.getId());
        assertEquals((long)ses5.getRequestedResourceId(), in.getResourceId());
        assertEquals(ses5.getRequestedResourceName(), in.getResourceName());
        assertEquals(ses5.getResourceType(), in.getResourceType());
        
        s = in.getHead();
        assertNotNull(s);
        assertEquals(ses4.getId(), s.getId());
        assertEquals((long)ses4.getRequestedResourceId(), in.getResourceId());
        assertEquals(ses4.getRequestedResourceName(), in.getResourceName());
        assertEquals(ses4.getResourceType(), in.getResourceType());
        db.close();
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testRemoveEntry() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 10000);
        Date after = new Date(System.currentTimeMillis() + 10000);
        Date now = new Date();
        
        db.beginTransaction();
        User user1 = new User("qperm1", "testns", "USER");
        db.persist(user1);
        User user2 = new User("qperm2", "testns", "USER");
        db.persist(user2);
        User user3 = new User("qperm3", "testns", "USER");
        db.persist(user3);
        User user4 = new User("qperm4", "testns", "USER");
        db.persist(user4);
        User user5 = new User("qperm5", "testns", "USER");
        db.persist(user5);
        
        UserClass uc1 = new UserClass();
        uc1.setName("uc1");
        uc1.setActive(true);
        db.persist(uc1);
        
        UserAssociation ass1 = new UserAssociation(new UserAssociationId(user1.getId(), uc1.getId()), uc1, user1);
        db.persist(ass1);
        UserAssociation ass2 = new UserAssociation(new UserAssociationId(user2.getId(), uc1.getId()), uc1, user2);
        db.persist(ass2);
        UserAssociation ass3 = new UserAssociation(new UserAssociationId(user3.getId(), uc1.getId()), uc1, user3);
        db.persist(ass3);
        UserAssociation ass4 = new UserAssociation(new UserAssociationId(user4.getId(), uc1.getId()), uc1, user4);
        db.persist(ass4);
        UserAssociation ass5 = new UserAssociation(new UserAssociationId(user5.getId(), uc1.getId()), uc1, user5);
        db.persist(ass5);
        
        RigType rt = new RigType();
        rt.setName("Perm_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("perm,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Perm_Rig_Test_Rig1");
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
        Session ses2 = new Session();
        ses2.setActive(true);
        ses2.setReady(true);
        ses2.setActivityLastUpdated(now);
        ses2.setExtensions((short) 5);
        ses2.setPriority((short) 5);
        ses2.setRequestTime(new Date(System.currentTimeMillis() - 1000));
        ses2.setRequestedResourceId(r.getId());
        ses2.setRequestedResourceName(r.getName());
        ses2.setResourceType("RIG");
        ses2.setResourcePermission(p1);
        ses2.setUser(user2);
        ses2.setUserName(user2.getName());
        ses2.setUserNamespace(user2.getNamespace());
        db.persist(ses2);
        Session ses3 = new Session();
        ses3.setActive(true);
        ses3.setReady(true);
        ses3.setActivityLastUpdated(now);
        ses3.setExtensions((short) 5);
        ses3.setPriority((short) 5);
        ses3.setRequestTime(new Date(System.currentTimeMillis() - 2000));
        ses3.setRequestedResourceId(r.getId());
        ses3.setRequestedResourceName(r.getName());
        ses3.setResourceType("RIG");
        ses3.setResourcePermission(p1);
        ses3.setUser(user3);
        ses3.setUserName(user3.getName());
        ses3.setUserNamespace(user3.getNamespace());
        db.persist(ses3);
        Session ses4 = new Session();
        ses4.setActive(true);
        ses4.setReady(true);
        ses4.setActivityLastUpdated(now);
        ses4.setExtensions((short) 5);
        ses4.setPriority((short) 5);
        ses4.setRequestTime(new Date(System.currentTimeMillis() - 3000));
        ses4.setRequestedResourceId(r.getId());
        ses4.setRequestedResourceName(r.getName());
        ses4.setResourceType("RIG");
        ses4.setResourcePermission(p1);
        ses4.setUser(user4);
        ses4.setUserName(user4.getName());
        ses4.setUserNamespace(user4.getNamespace());
        db.persist(ses4);
        Session ses5 = new Session();
        ses5.setActive(true);
        ses5.setReady(true);
        ses5.setActivityLastUpdated(now);
        ses5.setExtensions((short) 5);
        ses5.setPriority((short) 5);
        ses5.setRequestTime(new Date(System.currentTimeMillis() - 4000));
        ses5.setRequestedResourceId(r.getId());
        ses5.setRequestedResourceName(r.getName());
        ses5.setResourceType("RIG");
        ses5.setResourcePermission(p1);
        ses5.setUser(user5);
        ses5.setUserName(user5.getName());
        ses5.setUserNamespace(user5.getNamespace());
        db.persist(ses5);
        db.getTransaction().commit();
        
        this.queue.addEntry(ses1, db);
        this.queue.addEntry(ses2, db);
        this.queue.addEntry(ses3, db);
        this.queue.addEntry(ses4, db);
        this.queue.addEntry(ses5, db);
        
        this.queue.removeEntry(ses4, db);
        
        db.beginTransaction();
        db.delete(ses1);
        db.delete(ses2);
        db.delete(ses3);
        db.delete(ses4);
        db.delete(ses5);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass1);
        db.delete(ass2);
        db.delete(ass3);
        db.delete(ass4);
        db.delete(ass5);
        db.delete(uc1);
        db.delete(user1);
        db.delete(user2);
        db.delete(user3);
        db.delete(user4);
        db.delete(user5);
        db.getTransaction().commit();
        
        Field f = Queue.class.getDeclaredField("rigQueues");
        f.setAccessible(true);
        Map<Long, InnerQueue> que = (Map<Long, InnerQueue>) f.get(this.queue);
        assertNotNull(que);
        assertEquals(1, que.size());
        
        InnerQueue in = que.get(r.getId());
        assertNotNull(in);
        assertEquals(4, in.size());
        
        Session s = in.getHead();
        assertNotNull(s);
        assertEquals(ses5.getId(), s.getId());
        assertEquals((long)ses5.getRequestedResourceId(), in.getResourceId());
        assertEquals(ses5.getRequestedResourceName(), in.getResourceName());
        assertEquals(ses5.getResourceType(), in.getResourceType());

        s = in.getHead();
        assertNotNull(s);
        assertEquals(ses3.getId(), s.getId());
        assertEquals((long)ses3.getRequestedResourceId(), in.getResourceId());
        assertEquals(ses3.getRequestedResourceName(), in.getResourceName());
        assertEquals(ses3.getResourceType(), in.getResourceType());
        
        s = in.getHead();
        assertNotNull(s);
        assertEquals(ses2.getId(), s.getId());
        assertEquals((long)ses2.getRequestedResourceId(), in.getResourceId());
        assertEquals(ses2.getRequestedResourceName(), in.getResourceName());
        assertEquals(ses2.getResourceType(), in.getResourceType());
        
        s = in.getHead();
        assertNotNull(s);
        assertEquals(ses1.getId(), s.getId());
        assertEquals((long)ses1.getRequestedResourceId(), in.getResourceId());
        assertEquals(ses1.getRequestedResourceName(), in.getResourceName());
        assertEquals(ses1.getResourceType(), in.getResourceType());
        db.close();
    }
    @SuppressWarnings("unchecked")
    @Test
    public void testRemoveEntryType() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 10000);
        Date after = new Date(System.currentTimeMillis() + 10000);
        Date now = new Date();
        
        db.beginTransaction();
        User user1 = new User("qperm1", "testns", "USER");
        db.persist(user1);
        User user2 = new User("qperm2", "testns", "USER");
        db.persist(user2);
        User user3 = new User("qperm3", "testns", "USER");
        db.persist(user3);
        User user4 = new User("qperm4", "testns", "USER");
        db.persist(user4);
        User user5 = new User("qperm5", "testns", "USER");
        db.persist(user5);
        
        UserClass uc1 = new UserClass();
        uc1.setName("uc1");
        uc1.setActive(true);
        db.persist(uc1);
        
        UserAssociation ass1 = new UserAssociation(new UserAssociationId(user1.getId(), uc1.getId()), uc1, user1);
        db.persist(ass1);
        UserAssociation ass2 = new UserAssociation(new UserAssociationId(user2.getId(), uc1.getId()), uc1, user2);
        db.persist(ass2);
        UserAssociation ass3 = new UserAssociation(new UserAssociationId(user3.getId(), uc1.getId()), uc1, user3);
        db.persist(ass3);
        UserAssociation ass4 = new UserAssociation(new UserAssociationId(user4.getId(), uc1.getId()), uc1, user4);
        db.persist(ass4);
        UserAssociation ass5 = new UserAssociation(new UserAssociationId(user5.getId(), uc1.getId()), uc1, user5);
        db.persist(ass5);
        
        RigType rt = new RigType();
        rt.setName("Perm_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("perm,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Perm_Rig_Test_Rig1");
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
        db.persist(p1);
        
        Session ses1 = new Session();
        ses1.setActive(true);
        ses1.setReady(true);
        ses1.setActivityLastUpdated(now);
        ses1.setExtensions((short) 5);
        ses1.setPriority((short) 5);
        ses1.setRequestTime(now);
        ses1.setRequestedResourceId(rt.getId());
        ses1.setRequestedResourceName(rt.getName());
        ses1.setResourceType("TYPE");
        ses1.setResourcePermission(p1);
        ses1.setUser(user1);
        ses1.setUserName(user1.getName());
        ses1.setUserNamespace(user1.getNamespace());
        db.persist(ses1);
        Session ses2 = new Session();
        ses2.setActive(true);
        ses2.setReady(true);
        ses2.setActivityLastUpdated(now);
        ses2.setExtensions((short) 5);
        ses2.setPriority((short) 5);
        ses2.setRequestTime(new Date(System.currentTimeMillis() - 1000));
        ses2.setRequestedResourceId(rt.getId());
        ses2.setRequestedResourceName(rt.getName());
        ses2.setResourceType("TYPE");
        ses2.setResourcePermission(p1);
        ses2.setUser(user2);
        ses2.setUserName(user2.getName());
        ses2.setUserNamespace(user2.getNamespace());
        db.persist(ses2);
        Session ses3 = new Session();
        ses3.setActive(true);
        ses3.setReady(true);
        ses3.setActivityLastUpdated(now);
        ses3.setExtensions((short) 5);
        ses3.setPriority((short) 5);
        ses3.setRequestTime(new Date(System.currentTimeMillis() - 2000));
        ses3.setRequestedResourceId(rt.getId());
        ses3.setRequestedResourceName(rt.getName());
        ses3.setResourceType("TYPE");
        ses3.setResourcePermission(p1);
        ses3.setUser(user3);
        ses3.setUserName(user3.getName());
        ses3.setUserNamespace(user3.getNamespace());
        db.persist(ses3);
        Session ses4 = new Session();
        ses4.setActive(true);
        ses4.setReady(true);
        ses4.setActivityLastUpdated(now);
        ses4.setExtensions((short) 5);
        ses4.setPriority((short) 5);
        ses4.setRequestTime(new Date(System.currentTimeMillis() - 3000));
        ses4.setRequestedResourceId(rt.getId());
        ses4.setRequestedResourceName(rt.getName());
        ses4.setResourceType("TYPE");
        ses4.setResourcePermission(p1);
        ses4.setUser(user4);
        ses4.setUserName(user4.getName());
        ses4.setUserNamespace(user4.getNamespace());
        db.persist(ses4);
        Session ses5 = new Session();
        ses5.setActive(true);
        ses5.setReady(true);
        ses5.setActivityLastUpdated(now);
        ses5.setExtensions((short) 5);
        ses5.setPriority((short) 5);
        ses5.setRequestTime(new Date(System.currentTimeMillis() - 4000));
        ses5.setRequestedResourceId(rt.getId());
        ses5.setRequestedResourceName(rt.getName());
        ses5.setResourceType("TYPE");
        ses5.setResourcePermission(p1);
        ses5.setUser(user5);
        ses5.setUserName(user5.getName());
        ses5.setUserNamespace(user5.getNamespace());
        db.persist(ses5);
        db.getTransaction().commit();
        
        this.queue.addEntry(ses1, db);
        this.queue.addEntry(ses2, db);
        this.queue.addEntry(ses3, db);
        this.queue.addEntry(ses4, db);
        this.queue.addEntry(ses5, db);
        
        this.queue.removeEntry(ses3, db);
        
        db.beginTransaction();
        db.delete(ses1);
        db.delete(ses2);
        db.delete(ses3);
        db.delete(ses4);
        db.delete(ses5);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass1);
        db.delete(ass2);
        db.delete(ass3);
        db.delete(ass4);
        db.delete(ass5);
        db.delete(uc1);
        db.delete(user1);
        db.delete(user2);
        db.delete(user3);
        db.delete(user4);
        db.delete(user5);
        db.getTransaction().commit();
        
        Field f = Queue.class.getDeclaredField("typeQueues");
        f.setAccessible(true);
        Map<Long, InnerQueue> que = (Map<Long, InnerQueue>) f.get(this.queue);
        assertNotNull(que);
        assertEquals(1, que.size());
        
        InnerQueue in = que.get(rt.getId());
        assertNotNull(in);
        assertEquals(4, in.size());
        
        Session s = in.getHead();
        assertNotNull(s);
        assertEquals(ses5.getId(), s.getId());
        assertEquals((long)ses5.getRequestedResourceId(), in.getResourceId());
        assertEquals(ses5.getRequestedResourceName(), in.getResourceName());
        assertEquals(ses5.getResourceType(), in.getResourceType());
        
        s = in.getHead();
        assertNotNull(s);
        assertEquals(ses4.getId(), s.getId());
        assertEquals((long)ses4.getRequestedResourceId(), in.getResourceId());
        assertEquals(ses4.getRequestedResourceName(), in.getResourceName());
        assertEquals(ses4.getResourceType(), in.getResourceType());
        
        s = in.getHead();
        assertNotNull(s);
        assertEquals(ses2.getId(), s.getId());
        assertEquals((long)ses2.getRequestedResourceId(), in.getResourceId());
        assertEquals(ses2.getRequestedResourceName(), in.getResourceName());
        assertEquals(ses2.getResourceType(), in.getResourceType());
        
        s = in.getHead();
        assertNotNull(s);
        assertEquals(ses1.getId(), s.getId());
        assertEquals((long)ses1.getRequestedResourceId(), in.getResourceId());
        assertEquals(ses1.getRequestedResourceName(), in.getResourceName());
        assertEquals(ses1.getResourceType(), in.getResourceType());
        db.close();
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testRemoveEntryCaps() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 10000);
        Date after = new Date(System.currentTimeMillis() + 10000);
        Date now = new Date();
        
        db.beginTransaction();
        User user1 = new User("qperm1", "testns", "USER");
        db.persist(user1);
        User user2 = new User("qperm2", "testns", "USER");
        db.persist(user2);
        User user3 = new User("qperm3", "testns", "USER");
        db.persist(user3);
        User user4 = new User("qperm4", "testns", "USER");
        db.persist(user4);
        User user5 = new User("qperm5", "testns", "USER");
        db.persist(user5);
        
        UserClass uc1 = new UserClass();
        uc1.setName("uc1");
        uc1.setActive(true);
        db.persist(uc1);
        
        UserAssociation ass1 = new UserAssociation(new UserAssociationId(user1.getId(), uc1.getId()), uc1, user1);
        db.persist(ass1);
        UserAssociation ass2 = new UserAssociation(new UserAssociationId(user2.getId(), uc1.getId()), uc1, user2);
        db.persist(ass2);
        UserAssociation ass3 = new UserAssociation(new UserAssociationId(user3.getId(), uc1.getId()), uc1, user3);
        db.persist(ass3);
        UserAssociation ass4 = new UserAssociation(new UserAssociationId(user4.getId(), uc1.getId()), uc1, user4);
        db.persist(ass4);
        UserAssociation ass5 = new UserAssociation(new UserAssociationId(user5.getId(), uc1.getId()), uc1, user5);
        db.persist(ass5);
        
        RigType rt = new RigType();
        rt.setName("Perm_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("perm,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Perm_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(false);
        db.persist(r);
        
        RequestCapabilities reqCaps = new RequestCapabilities("perm,test");
        db.persist(reqCaps);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("CAPABILITY");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRequestCapabilities(reqCaps);
        db.persist(p1);
        
        Session ses1 = new Session();
        ses1.setActive(true);
        ses1.setReady(true);
        ses1.setActivityLastUpdated(now);
        ses1.setExtensions((short) 5);
        ses1.setPriority((short) 5);
        ses1.setRequestTime(now);
        ses1.setRequestedResourceId(reqCaps.getId());
        ses1.setRequestedResourceName(reqCaps.getCapabilities());
        ses1.setResourceType("CAPABILITY");
        ses1.setResourcePermission(p1);
        ses1.setUser(user1);
        ses1.setUserName(user1.getName());
        ses1.setUserNamespace(user1.getNamespace());
        db.persist(ses1);
        Session ses2 = new Session();
        ses2.setActive(true);
        ses2.setReady(true);
        ses2.setActivityLastUpdated(now);
        ses2.setExtensions((short) 5);
        ses2.setPriority((short) 5);
        ses2.setRequestTime(new Date(System.currentTimeMillis() - 1000));
        ses2.setRequestedResourceId(reqCaps.getId());
        ses2.setRequestedResourceName(reqCaps.getCapabilities());
        ses2.setResourceType("CAPABILITY");
        ses2.setResourcePermission(p1);
        ses2.setUser(user2);
        ses2.setUserName(user2.getName());
        ses2.setUserNamespace(user2.getNamespace());
        db.persist(ses2);
        Session ses3 = new Session();
        ses3.setActive(true);
        ses3.setReady(true);
        ses3.setActivityLastUpdated(now);
        ses3.setExtensions((short) 5);
        ses3.setPriority((short) 5);
        ses3.setRequestTime(new Date(System.currentTimeMillis() - 2000));
        ses3.setRequestedResourceId(reqCaps.getId());
        ses3.setRequestedResourceName(reqCaps.getCapabilities());
        ses3.setResourceType("CAPABILITY");
        ses3.setResourcePermission(p1);
        ses3.setUser(user3);
        ses3.setUserName(user3.getName());
        ses3.setUserNamespace(user3.getNamespace());
        db.persist(ses3);
        Session ses4 = new Session();
        ses4.setActive(true);
        ses4.setReady(true);
        ses4.setActivityLastUpdated(now);
        ses4.setExtensions((short) 5);
        ses4.setPriority((short) 5);
        ses4.setRequestTime(new Date(System.currentTimeMillis() - 3000));
        ses4.setRequestedResourceId(reqCaps.getId());
        ses4.setRequestedResourceName(reqCaps.getCapabilities());
        ses4.setResourceType("CAPABILITY");
        ses4.setResourcePermission(p1);
        ses4.setUser(user4);
        ses4.setUserName(user4.getName());
        ses4.setUserNamespace(user4.getNamespace());
        db.persist(ses4);
        Session ses5 = new Session();
        ses5.setActive(true);
        ses5.setReady(true);
        ses5.setActivityLastUpdated(now);
        ses5.setExtensions((short) 5);
        ses5.setPriority((short) 5);
        ses5.setRequestTime(new Date(System.currentTimeMillis() - 4000));
        ses5.setRequestedResourceId(reqCaps.getId());
        ses5.setRequestedResourceName(reqCaps.getCapabilities());
        ses5.setResourceType("CAPABILITY");
        ses5.setResourcePermission(p1);
        ses5.setUser(user5);
        ses5.setUserName(user5.getName());
        ses5.setUserNamespace(user5.getNamespace());
        db.persist(ses5);
        db.getTransaction().commit();
        
        this.queue.addEntry(ses1, db);
        this.queue.addEntry(ses2, db);
        this.queue.addEntry(ses3, db);
        this.queue.addEntry(ses4, db);
        this.queue.addEntry(ses5, db);
        
        this.queue.removeEntry(ses5, db);
        
        db.beginTransaction();
        db.delete(ses1);
        db.delete(ses2);
        db.delete(ses3);
        db.delete(ses4);
        db.delete(ses5);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(reqCaps);
        db.delete(caps);
        db.delete(ass1);
        db.delete(ass2);
        db.delete(ass3);
        db.delete(ass4);
        db.delete(ass5);
        db.delete(uc1);
        db.delete(user1);
        db.delete(user2);
        db.delete(user3);
        db.delete(user4);
        db.delete(user5);
        db.getTransaction().commit();
        
        Field f = Queue.class.getDeclaredField("capabilityQueues");
        f.setAccessible(true);
        Map<Long, InnerQueue> que = (Map<Long, InnerQueue>) f.get(this.queue);
        assertNotNull(que);
        assertEquals(1, que.size());
        
        InnerQueue in = que.get(reqCaps.getId());
        assertNotNull(in);
        assertEquals(4, in.size());
        
        Session s = in.getHead();
        assertNotNull(s);
        assertEquals(ses4.getId(), s.getId());
        assertEquals((long)ses4.getRequestedResourceId(), in.getResourceId());
        assertEquals(ses4.getRequestedResourceName(), in.getResourceName());
        assertEquals(ses4.getResourceType(), in.getResourceType());
        
        s = in.getHead();
        assertNotNull(s);
        assertEquals(ses3.getId(), s.getId());
        assertEquals((long)ses3.getRequestedResourceId(), in.getResourceId());
        assertEquals(ses3.getRequestedResourceName(), in.getResourceName());
        assertEquals(ses3.getResourceType(), in.getResourceType());
        
        s = in.getHead();
        assertNotNull(s);
        assertEquals(ses2.getId(), s.getId());
        assertEquals((long)ses2.getRequestedResourceId(), in.getResourceId());
        assertEquals(ses2.getRequestedResourceName(), in.getResourceName());
        assertEquals(ses2.getResourceType(), in.getResourceType());
        
        s = in.getHead();
        assertNotNull(s);
        assertEquals(ses1.getId(), s.getId());
        assertEquals((long)ses1.getRequestedResourceId(), in.getResourceId());
        assertEquals(ses1.getRequestedResourceName(), in.getResourceName());
        assertEquals(ses1.getResourceType(), in.getResourceType());
        db.close();
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testGetEntryPosition() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 10000);
        Date after = new Date(System.currentTimeMillis() + 10000);
        Date now = new Date();
        
        db.beginTransaction();
        RigCapabilities caps = new RigCapabilities("perm,test,rig,type");
        db.persist(caps);
        db.getTransaction().commit();
        
        RequestCapabilitiesDao dao = new RequestCapabilitiesDao(db);
        RequestCapabilities rCaps1 = dao.addCapabilities("rig,test");
        RequestCapabilities rCaps2 = dao.addCapabilities("type");
        RequestCapabilities rCaps3 = dao.addCapabilities("perm,rig");
        
        db.beginTransaction();
        User user1 = new User("qperm1", "testns", "USER");
        db.persist(user1);
        User user2 = new User("qperm2", "testns", "USER");
        db.persist(user2);
        User user3 = new User("qperm3", "testns", "USER");
        db.persist(user3);
        User user4 = new User("qperm4", "testns", "USER");
        db.persist(user4);
        User user5 = new User("qperm5", "testns", "USER");
        db.persist(user5);
        User user6 = new User("qperm6", "testns", "USER");
        db.persist(user6);
        User user7 = new User("qperm7", "testns", "USER");
        db.persist(user7);
        User user8 = new User("qperm8", "testns", "USER");
        db.persist(user8);
        User user9 = new User("qperm9", "testns", "USER");
        db.persist(user9);
        User user10 = new User("qperm10", "testns", "USER");
        db.persist(user10);
        
        UserClass uc1 = new UserClass();
        uc1.setName("uc1");
        uc1.setActive(true);
        db.persist(uc1);
        
        UserAssociation ass1 = new UserAssociation(new UserAssociationId(user1.getId(), uc1.getId()), uc1, user1);
        db.persist(ass1);
        UserAssociation ass2 = new UserAssociation(new UserAssociationId(user2.getId(), uc1.getId()), uc1, user2);
        db.persist(ass2);
        UserAssociation ass3 = new UserAssociation(new UserAssociationId(user3.getId(), uc1.getId()), uc1, user3);
        db.persist(ass3);
        UserAssociation ass4 = new UserAssociation(new UserAssociationId(user4.getId(), uc1.getId()), uc1, user4);
        db.persist(ass4);
        UserAssociation ass5 = new UserAssociation(new UserAssociationId(user5.getId(), uc1.getId()), uc1, user5);
        db.persist(ass5);
        UserAssociation ass6 = new UserAssociation(new UserAssociationId(user6.getId(), uc1.getId()), uc1, user6);
        db.persist(ass6);
        UserAssociation ass7 = new UserAssociation(new UserAssociationId(user7.getId(), uc1.getId()), uc1, user7);
        db.persist(ass7);
        UserAssociation ass8 = new UserAssociation(new UserAssociationId(user8.getId(), uc1.getId()), uc1, user8);
        db.persist(ass8);
        UserAssociation ass9 = new UserAssociation(new UserAssociationId(user9.getId(), uc1.getId()), uc1, user9);
        db.persist(ass9);
        UserAssociation ass10 = new UserAssociation(new UserAssociationId(user10.getId(), uc1.getId()), uc1, user10);
        db.persist(ass10);
        
        RigType rt = new RigType();
        rt.setName("Perm_Test_Rig_Type");
        db.persist(rt);
        
        
        
        Rig r = new Rig();
        r.setName("Perm_Rig_Test_Rig1");
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
        Session ses2 = new Session();
        ses2.setActive(true);
        ses2.setReady(true);
        ses2.setActivityLastUpdated(now);
        ses2.setExtensions((short) 5);
        ses2.setPriority((short) 5);
        ses2.setRequestTime(new Date(System.currentTimeMillis() - 1000));
        ses2.setRequestedResourceId(rt.getId());
        ses2.setRequestedResourceName(rt.getName());
        ses2.setResourceType("TYPE");
        ses2.setResourcePermission(p1);
        ses2.setUser(user2);
        ses2.setUserName(user2.getName());
        ses2.setUserNamespace(user2.getNamespace());
        db.persist(ses2);
        Session ses3 = new Session();
        ses3.setActive(true);
        ses3.setReady(true);
        ses3.setActivityLastUpdated(now);
        ses3.setExtensions((short) 5);
        ses3.setPriority((short) 5);
        ses3.setRequestTime(new Date(System.currentTimeMillis() - 2000));
        ses3.setRequestedResourceId(rCaps1.getId());
        ses3.setRequestedResourceName(rCaps1.getCapabilities());
        ses3.setResourceType("CAPABILITY");
        ses3.setResourcePermission(p1);
        ses3.setUser(user3);
        ses3.setUserName(user3.getName());
        ses3.setUserNamespace(user3.getNamespace());
        db.persist(ses3);
        Session ses4 = new Session();
        ses4.setActive(true);
        ses4.setReady(true);
        ses4.setActivityLastUpdated(now);
        ses4.setExtensions((short) 5);
        ses4.setPriority((short) 5);
        ses4.setRequestTime(new Date(System.currentTimeMillis() - 3000));
        ses4.setRequestedResourceId(rt.getId());
        ses4.setRequestedResourceName(rt.getName());
        ses4.setResourceType("TYPE");
        ses4.setResourcePermission(p1);
        ses4.setUser(user4);
        ses4.setUserName(user4.getName());
        ses4.setUserNamespace(user4.getNamespace());
        db.persist(ses4);
        Session ses5 = new Session();
        ses5.setActive(true);
        ses5.setReady(true);
        ses5.setActivityLastUpdated(now);
        ses5.setExtensions((short) 5);
        ses5.setPriority((short) 5);
        ses5.setRequestTime(new Date(System.currentTimeMillis() - 4000));
        ses5.setRequestedResourceId(r.getId());
        ses5.setRequestedResourceName(r.getName());
        ses5.setResourceType("RIG");
        ses5.setResourcePermission(p1);
        ses5.setUser(user5);
        ses5.setUserName(user5.getName());
        ses5.setUserNamespace(user5.getNamespace());
        db.persist(ses5);
        Session ses6 = new Session();
        ses6.setActive(true);
        ses6.setReady(true);
        ses6.setActivityLastUpdated(now);
        ses6.setExtensions((short) 5);
        ses6.setPriority((short) 6);
        ses6.setRequestTime(now);
        ses6.setRequestedResourceId(r.getId());
        ses6.setRequestedResourceName(r.getName());
        ses6.setResourceType("RIG");
        ses6.setResourcePermission(p1);
        ses6.setUser(user6);
        ses6.setUserName(user6.getName());
        ses6.setUserNamespace(user6.getNamespace());
        db.persist(ses6);
        Session ses7 = new Session();
        ses7.setActive(true);
        ses7.setReady(true);
        ses7.setActivityLastUpdated(now);
        ses7.setExtensions((short) 5);
        ses7.setPriority((short) 6);
        ses7.setRequestTime(new Date(System.currentTimeMillis() - 1000));
        ses7.setRequestedResourceId(rCaps1.getId());
        ses7.setRequestedResourceName(rCaps1.getCapabilities());
        ses7.setResourceType("CAPABILITY");
        ses7.setResourcePermission(p1);
        ses7.setUser(user7);
        ses7.setUserName(user7.getName());
        ses7.setUserNamespace(user7.getNamespace());
        db.persist(ses7);
        Session ses8 = new Session();
        ses8.setActive(true);
        ses8.setReady(true);
        ses8.setActivityLastUpdated(now);
        ses8.setExtensions((short) 5);
        ses8.setPriority((short) 6);
        ses8.setRequestTime(new Date(System.currentTimeMillis() - 2000));
        ses8.setRequestedResourceId(rCaps2.getId());
        ses8.setRequestedResourceName(rCaps2.getCapabilities());
        ses8.setResourceType("CAPABILITY");
        ses8.setResourcePermission(p1);
        ses8.setUser(user8);
        ses8.setUserName(user8.getName());
        ses8.setUserNamespace(user8.getNamespace());
        db.persist(ses8);
        Session ses9 = new Session();
        ses9.setActive(true);
        ses9.setReady(true);
        ses9.setActivityLastUpdated(now);
        ses9.setExtensions((short) 5);
        ses9.setPriority((short) 6);
        ses9.setRequestTime(new Date(System.currentTimeMillis() - 3000));
        ses9.setRequestedResourceId(rt.getId());
        ses9.setRequestedResourceName(rt.getName());
        ses9.setResourceType("TYPE");
        ses9.setResourcePermission(p1);
        ses9.setUser(user9);
        ses9.setUserName(user9.getName());
        ses9.setUserNamespace(user9.getNamespace());
        db.persist(ses9);
        Session ses10 = new Session();
        ses10.setActive(true);
        ses10.setReady(true);
        ses10.setActivityLastUpdated(now);
        ses10.setExtensions((short) 5);
        ses10.setPriority((short) 6);
        ses10.setRequestTime(new Date(System.currentTimeMillis() - 4000));
        ses10.setRequestedResourceId(rCaps3.getId());
        ses10.setRequestedResourceName(rCaps3.getCapabilities());
        ses10.setResourceType("CAPABILITY");
        ses10.setResourcePermission(p1);
        ses10.setUser(user10);
        ses10.setUserName(user10.getName());
        ses10.setUserNamespace(user10.getNamespace());
        db.persist(ses10);
        db.getTransaction().commit();
        
        db.refresh(r);
        db.refresh(rt);
        db.refresh(caps);
        db.refresh(rCaps1);
        db.refresh(rCaps2);
        db.refresh(rCaps3);
        
        this.queue.addEntry(ses1, db);
        this.queue.addEntry(ses2, db);
        this.queue.addEntry(ses3, db);
        this.queue.addEntry(ses4, db);
        this.queue.addEntry(ses5, db);
        this.queue.addEntry(ses6, db);
        this.queue.addEntry(ses7, db);
        this.queue.addEntry(ses8, db);
        this.queue.addEntry(ses9, db);
        this.queue.addEntry(ses10, db);
        
        assertEquals(1, this.queue.getEntryPosition(ses5, db));
        assertEquals(2, this.queue.getEntryPosition(ses4, db));
        assertEquals(3, this.queue.getEntryPosition(ses3, db));
        assertEquals(4, this.queue.getEntryPosition(ses2, db));
        assertEquals(5, this.queue.getEntryPosition(ses1, db));
        assertEquals(6, this.queue.getEntryPosition(ses10, db));
        assertEquals(7, this.queue.getEntryPosition(ses9, db));
        assertEquals(8, this.queue.getEntryPosition(ses8, db));
        assertEquals(9, this.queue.getEntryPosition(ses7, db));
        assertEquals(10, this.queue.getEntryPosition(ses6, db));
        
        db.beginTransaction();
        db.delete(ses1);
        db.delete(ses2);
        db.delete(ses3);
        db.delete(ses4);
        db.delete(ses5);
        db.delete(ses6);
        db.delete(ses7);
        db.delete(ses8);
        db.delete(ses9);
        db.delete(ses10);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(ass1);
        db.delete(ass2);
        db.delete(ass3);
        db.delete(ass4);
        db.delete(ass5);
        db.delete(ass6);
        db.delete(ass7);
        db.delete(ass8);
        db.delete(ass9);
        db.delete(ass10);
        db.delete(uc1);
        db.delete(user1);
        db.delete(user2);
        db.delete(user3);
        db.delete(user4);
        db.delete(user5);
        db.delete(user6);
        db.delete(user7);
        db.delete(user8);
        db.delete(user9);
        db.delete(user10);
        db.getTransaction().commit();
        
        dao.delete(rCaps1);
        dao.delete(rCaps2);
        dao.delete(rCaps3);
        db.beginTransaction();
        db.delete(caps);
        db.getTransaction().commit();
        
        Field f = Queue.class.getDeclaredField("rigQueues");
        f.setAccessible(true);
        Map<Long, InnerQueue> que = (Map<Long, InnerQueue>) f.get(this.queue);
        assertNotNull(que);
        assertEquals(1, que.size());
        
        InnerQueue in = que.get(r.getId());
        assertEquals(3, in.size());
        
        f = Queue.class.getDeclaredField("typeQueues");
        f.setAccessible(true);
        que = (Map<Long, InnerQueue>) f.get(this.queue);
        assertNotNull(que);
        assertEquals(1, que.size());
        
        in = que.get(rt.getId());
        assertEquals(3, in.size());
        
        f = Queue.class.getDeclaredField("capabilityQueues");
        f.setAccessible(true);
        que = (Map<Long, InnerQueue>) f.get(this.queue);
        assertNotNull(que);
        assertEquals(3, que.size());
        
        in = que.get(rCaps1.getId());
        assertEquals(2, in.size());
        
        in = que.get(rCaps2.getId());
        assertEquals(1, in.size());
        
        in = que.get(rCaps3.getId());
        assertEquals(1, in.size());
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testGetEntryPositionAssignment() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 10000);
        Date after = new Date(System.currentTimeMillis() + 10000);
        Date now = new Date();
        
        db.beginTransaction();
        RigCapabilities caps = new RigCapabilities("perm,test,rig,type");
        db.persist(caps);
        db.getTransaction().commit();
        
        RequestCapabilitiesDao dao = new RequestCapabilitiesDao(db);
        RequestCapabilities rCaps1 = dao.addCapabilities("rig,test");
        RequestCapabilities rCaps2 = dao.addCapabilities("type");
        RequestCapabilities rCaps3 = dao.addCapabilities("perm,rig");
        
        db.beginTransaction();
        User user1 = new User("qperm1", "testns", "USER");
        db.persist(user1);
        User user2 = new User("qperm2", "testns", "USER");
        db.persist(user2);
        User user3 = new User("qperm3", "testns", "USER");
        db.persist(user3);
        User user4 = new User("qperm4", "testns", "USER");
        db.persist(user4);
        User user5 = new User("qperm5", "testns", "USER");
        db.persist(user5);
        User user6 = new User("qperm6", "testns", "USER");
        db.persist(user6);
        User user7 = new User("qperm7", "testns", "USER");
        db.persist(user7);
        User user8 = new User("qperm8", "testns", "USER");
        db.persist(user8);
        User user9 = new User("qperm9", "testns", "USER");
        db.persist(user9);
        User user10 = new User("qperm10", "testns", "USER");
        db.persist(user10);
        
        UserClass uc1 = new UserClass();
        uc1.setName("uc1");
        uc1.setActive(true);
        db.persist(uc1);
        
        UserAssociation ass1 = new UserAssociation(new UserAssociationId(user1.getId(), uc1.getId()), uc1, user1);
        db.persist(ass1);
        UserAssociation ass2 = new UserAssociation(new UserAssociationId(user2.getId(), uc1.getId()), uc1, user2);
        db.persist(ass2);
        UserAssociation ass3 = new UserAssociation(new UserAssociationId(user3.getId(), uc1.getId()), uc1, user3);
        db.persist(ass3);
        UserAssociation ass4 = new UserAssociation(new UserAssociationId(user4.getId(), uc1.getId()), uc1, user4);
        db.persist(ass4);
        UserAssociation ass5 = new UserAssociation(new UserAssociationId(user5.getId(), uc1.getId()), uc1, user5);
        db.persist(ass5);
        UserAssociation ass6 = new UserAssociation(new UserAssociationId(user6.getId(), uc1.getId()), uc1, user6);
        db.persist(ass6);
        UserAssociation ass7 = new UserAssociation(new UserAssociationId(user7.getId(), uc1.getId()), uc1, user7);
        db.persist(ass7);
        UserAssociation ass8 = new UserAssociation(new UserAssociationId(user8.getId(), uc1.getId()), uc1, user8);
        db.persist(ass8);
        UserAssociation ass9 = new UserAssociation(new UserAssociationId(user9.getId(), uc1.getId()), uc1, user9);
        db.persist(ass9);
        UserAssociation ass10 = new UserAssociation(new UserAssociationId(user10.getId(), uc1.getId()), uc1, user10);
        db.persist(ass10);
        
        RigType rt = new RigType();
        rt.setName("Perm_Test_Rig_Type");
        db.persist(rt);
        
        Rig r = new Rig();
        r.setName("Perm_Rig_Test_Rig1");
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
        Session ses2 = new Session();
        ses2.setActive(true);
        ses2.setReady(true);
        ses2.setActivityLastUpdated(now);
        ses2.setExtensions((short) 5);
        ses2.setPriority((short) 5);
        ses2.setRequestTime(new Date(System.currentTimeMillis() - 1000));
        ses2.setRequestedResourceId(rt.getId());
        ses2.setRequestedResourceName(rt.getName());
        ses2.setResourceType("TYPE");
        ses2.setResourcePermission(p1);
        ses2.setUser(user2);
        ses2.setUserName(user2.getName());
        ses2.setUserNamespace(user2.getNamespace());
        db.persist(ses2);
        Session ses3 = new Session();
        ses3.setActive(true);
        ses3.setReady(true);
        ses3.setActivityLastUpdated(now);
        ses3.setExtensions((short) 5);
        ses3.setPriority((short) 5);
        ses3.setRequestTime(new Date(System.currentTimeMillis() - 2000));
        ses3.setRequestedResourceId(rCaps1.getId());
        ses3.setRequestedResourceName(rCaps1.getCapabilities());
        ses3.setResourceType("CAPABILITY");
        ses3.setResourcePermission(p1);
        ses3.setUser(user3);
        ses3.setUserName(user3.getName());
        ses3.setUserNamespace(user3.getNamespace());
        db.persist(ses3);
        Session ses4 = new Session();
        ses4.setActive(true);
        ses4.setReady(true);
        ses4.setActivityLastUpdated(now);
        ses4.setExtensions((short) 5);
        ses4.setPriority((short) 5);
        ses4.setRequestTime(new Date(System.currentTimeMillis() - 3000));
        ses4.setRequestedResourceId(rt.getId());
        ses4.setRequestedResourceName(rt.getName());
        ses4.setResourceType("TYPE");
        ses4.setResourcePermission(p1);
        ses4.setUser(user4);
        ses4.setUserName(user4.getName());
        ses4.setUserNamespace(user4.getNamespace());
        db.persist(ses4);
        Session ses5 = new Session();
        ses5.setActive(true);
        ses5.setReady(true);
        ses5.setActivityLastUpdated(now);
        ses5.setExtensions((short) 5);
        ses5.setPriority((short) 5);
        ses5.setRequestTime(new Date(System.currentTimeMillis() - 4000));
        ses5.setRequestedResourceId(r.getId());
        ses5.setRequestedResourceName(r.getName());
        ses5.setResourceType("RIG");
        ses5.setResourcePermission(p1);
        ses5.setUser(user5);
        ses5.setUserName(user5.getName());
        ses5.setUserNamespace(user5.getNamespace());
        db.persist(ses5);
        Session ses6 = new Session();
        ses6.setActive(true);
        ses6.setReady(true);
        ses6.setActivityLastUpdated(now);
        ses6.setExtensions((short) 5);
        ses6.setPriority((short) 6);
        ses6.setRequestTime(now);
        ses6.setRequestedResourceId(r.getId());
        ses6.setRequestedResourceName(r.getName());
        ses6.setResourceType("RIG");
        ses6.setResourcePermission(p1);
        ses6.setUser(user6);
        ses6.setUserName(user6.getName());
        ses6.setUserNamespace(user6.getNamespace());
        db.persist(ses6);
        Session ses7 = new Session();
        ses7.setActive(true);
        ses7.setReady(true);
        ses7.setActivityLastUpdated(now);
        ses7.setExtensions((short) 5);
        ses7.setPriority((short) 6);
        ses7.setRequestTime(new Date(System.currentTimeMillis() - 1000));
        ses7.setRequestedResourceId(rCaps1.getId());
        ses7.setRequestedResourceName(rCaps1.getCapabilities());
        ses7.setResourceType("CAPABILITY");
        ses7.setResourcePermission(p1);
        ses7.setUser(user7);
        ses7.setUserName(user7.getName());
        ses7.setUserNamespace(user7.getNamespace());
        db.persist(ses7);
        Session ses8 = new Session();
        ses8.setActive(true);
        ses8.setReady(true);
        ses8.setActivityLastUpdated(now);
        ses8.setExtensions((short) 5);
        ses8.setPriority((short) 6);
        ses8.setRequestTime(new Date(System.currentTimeMillis() - 2000));
        ses8.setRequestedResourceId(rCaps2.getId());
        ses8.setRequestedResourceName(rCaps2.getCapabilities());
        ses8.setResourceType("CAPABILITY");
        ses8.setResourcePermission(p1);
        ses8.setUser(user8);
        ses8.setUserName(user8.getName());
        ses8.setUserNamespace(user8.getNamespace());
        db.persist(ses8);
        Session ses9 = new Session();
        ses9.setActive(true);
        ses9.setReady(true);
        ses9.setActivityLastUpdated(now);
        ses9.setExtensions((short) 5);
        ses9.setPriority((short) 6);
        ses9.setRequestTime(new Date(System.currentTimeMillis() - 3000));
        ses9.setRequestedResourceId(rt.getId());
        ses9.setRequestedResourceName(rt.getName());
        ses9.setResourceType("TYPE");
        ses9.setResourcePermission(p1);
        ses9.setUser(user9);
        ses9.setUserName(user9.getName());
        ses9.setUserNamespace(user9.getNamespace());
        db.persist(ses9);
        Session ses10 = new Session();
        ses10.setActive(true);
        ses10.setReady(true);
        ses10.setActivityLastUpdated(now);
        ses10.setExtensions((short) 5);
        ses10.setPriority((short) 6);
        ses10.setRequestTime(new Date(System.currentTimeMillis() - 4000));
        ses10.setRequestedResourceId(rCaps3.getId());
        ses10.setRequestedResourceName(rCaps3.getCapabilities());
        ses10.setResourceType("CAPABILITY");
        ses10.setResourcePermission(p1);
        ses10.setUser(user10);
        ses10.setUserName(user10.getName());
        ses10.setUserNamespace(user10.getNamespace());
        db.persist(ses10);
        db.getTransaction().commit();
        
        db.refresh(r);
        db.refresh(rt);
        db.refresh(caps);
        db.refresh(rCaps1);
        db.refresh(rCaps2);
        db.refresh(rCaps3);
        
        this.queue.addEntry(ses1, db);
        this.queue.addEntry(ses2, db);
        this.queue.addEntry(ses3, db);
        this.queue.addEntry(ses4, db);
        this.queue.addEntry(ses5, db);
        this.queue.addEntry(ses6, db);
        this.queue.addEntry(ses7, db);
        this.queue.addEntry(ses8, db);
        this.queue.addEntry(ses9, db);
        this.queue.addEntry(ses10, db);
        
        assertEquals(1, this.queue.getEntryPosition(ses5, db));
        assertEquals(2, this.queue.getEntryPosition(ses4, db));
        assertEquals(3, this.queue.getEntryPosition(ses3, db));
        assertEquals(4, this.queue.getEntryPosition(ses2, db));
        assertEquals(5, this.queue.getEntryPosition(ses1, db));
        assertEquals(6, this.queue.getEntryPosition(ses10, db));
        assertEquals(7, this.queue.getEntryPosition(ses9, db));
        assertEquals(8, this.queue.getEntryPosition(ses8, db));
        assertEquals(9, this.queue.getEntryPosition(ses7, db));
        assertEquals(10, this.queue.getEntryPosition(ses6, db));
        
        r.setInSession(false);
        db.beginTransaction();
        db.flush();
        db.getTransaction().commit();
        this.queue.runRigAssignment(r.getId(), db);
        
        assertEquals(r.getId(), ses5.getRig().getId());
        assertEquals(1, this.queue.getEntryPosition(ses4, db));
        assertEquals(2, this.queue.getEntryPosition(ses3, db));
        assertEquals(3, this.queue.getEntryPosition(ses2, db));
        assertEquals(4, this.queue.getEntryPosition(ses1, db));
        assertEquals(5, this.queue.getEntryPosition(ses10, db));
        assertEquals(6, this.queue.getEntryPosition(ses9, db));
        assertEquals(7, this.queue.getEntryPosition(ses8, db));
        assertEquals(8, this.queue.getEntryPosition(ses7, db));
        assertEquals(9, this.queue.getEntryPosition(ses6, db));
        
        r.setInSession(false);
        db.beginTransaction();
        db.flush();
        db.getTransaction().commit();
        this.queue.runRigAssignment(r.getId(), db);
        
        assertEquals(r.getId(), ses4.getRig().getId());
        assertEquals(1, this.queue.getEntryPosition(ses3, db));
        assertEquals(2, this.queue.getEntryPosition(ses2, db));
        assertEquals(3, this.queue.getEntryPosition(ses1, db));
        assertEquals(4, this.queue.getEntryPosition(ses10, db));
        assertEquals(5, this.queue.getEntryPosition(ses9, db));
        assertEquals(6, this.queue.getEntryPosition(ses8, db));
        assertEquals(7, this.queue.getEntryPosition(ses7, db));
        assertEquals(8, this.queue.getEntryPosition(ses6, db));
        
        this.queue.removeEntry(ses3, db);
        assertEquals(1, this.queue.getEntryPosition(ses2, db));
        assertEquals(2, this.queue.getEntryPosition(ses1, db));
        assertEquals(3, this.queue.getEntryPosition(ses10, db));
        assertEquals(4, this.queue.getEntryPosition(ses9, db));
        assertEquals(5, this.queue.getEntryPosition(ses8, db));
        assertEquals(6, this.queue.getEntryPosition(ses7, db));
        assertEquals(7, this.queue.getEntryPosition(ses6, db));
        
        r.setInSession(false);
        db.beginTransaction();
        db.flush();
        db.getTransaction().commit();
        this.queue.runRigAssignment(r.getId(), db);
        
        assertEquals(r.getId(), ses2.getRig().getId());
        assertEquals(1, this.queue.getEntryPosition(ses1, db));
        assertEquals(2, this.queue.getEntryPosition(ses10, db));
        assertEquals(3, this.queue.getEntryPosition(ses9, db));
        assertEquals(4, this.queue.getEntryPosition(ses8, db));
        assertEquals(5, this.queue.getEntryPosition(ses7, db));
        assertEquals(6, this.queue.getEntryPosition(ses6, db));
        
        r.setInSession(false);
        db.beginTransaction();
        db.flush();
        db.getTransaction().commit();
        this.queue.runRigAssignment(r.getId(), db);
        
        assertEquals(r.getId(), ses1.getRig().getId());
        assertEquals(1, this.queue.getEntryPosition(ses10, db));
        assertEquals(2, this.queue.getEntryPosition(ses9, db));
        assertEquals(3, this.queue.getEntryPosition(ses8, db));
        assertEquals(4, this.queue.getEntryPosition(ses7, db));
        assertEquals(5, this.queue.getEntryPosition(ses6, db));
        
        r.setInSession(false);
        db.beginTransaction();
        db.flush();
        db.getTransaction().commit();
        this.queue.runRigAssignment(r.getId(), db);
        
        assertEquals(r.getId(), ses10.getRig().getId());
        assertEquals(1, this.queue.getEntryPosition(ses9, db));
        assertEquals(2, this.queue.getEntryPosition(ses8, db));
        assertEquals(3, this.queue.getEntryPosition(ses7, db));
        assertEquals(4, this.queue.getEntryPosition(ses6, db));

        r.setInSession(false);
        db.beginTransaction();
        db.flush();
        db.getTransaction().commit();
        this.queue.runRigAssignment(r.getId(), db);
        
        assertEquals(r.getId(), ses9.getRig().getId());
        assertEquals(1, this.queue.getEntryPosition(ses8, db));
        assertEquals(2, this.queue.getEntryPosition(ses7, db));
        assertEquals(3, this.queue.getEntryPosition(ses6, db));

        r.setInSession(false);
        db.beginTransaction();
        db.flush();
        db.getTransaction().commit();
        this.queue.runRigAssignment(r.getId(), db);
        
        assertEquals(r.getId(), ses8.getRig().getId());
        assertEquals(1, this.queue.getEntryPosition(ses7, db));
        assertEquals(2, this.queue.getEntryPosition(ses6, db));

        r.setInSession(false);
        db.beginTransaction();
        db.flush();
        db.getTransaction().commit();
        this.queue.runRigAssignment(r.getId(), db);
        
        assertEquals(r.getId(), ses7.getRig().getId());
        assertEquals(1, this.queue.getEntryPosition(ses6, db));

        r.setInSession(false);
        db.beginTransaction();
        db.flush();
        db.getTransaction().commit();
        this.queue.runRigAssignment(r.getId(), db);
        
        assertEquals(r.getId(), ses6.getRig().getId());
        
        db.beginTransaction();
        db.delete(ses1);
        db.delete(ses2);
        db.delete(ses3);
        db.delete(ses4);
        db.delete(ses5);
        db.delete(ses6);
        db.delete(ses7);
        db.delete(ses8);
        db.delete(ses9);
        db.delete(ses10);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(ass1);
        db.delete(ass2);
        db.delete(ass3);
        db.delete(ass4);
        db.delete(ass5);
        db.delete(ass6);
        db.delete(ass7);
        db.delete(ass8);
        db.delete(ass9);
        db.delete(ass10);
        db.delete(uc1);
        db.delete(user1);
        db.delete(user2);
        db.delete(user3);
        db.delete(user4);
        db.delete(user5);
        db.delete(user6);
        db.delete(user7);
        db.delete(user8);
        db.delete(user9);
        db.delete(user10);
        db.getTransaction().commit();
        
        dao.delete(rCaps1);
        dao.delete(rCaps2);
        dao.delete(rCaps3);
        db.beginTransaction();
        db.delete(caps);
        db.getTransaction().commit();
        
        Field f = Queue.class.getDeclaredField("rigQueues");
        f.setAccessible(true);
        Map<Long, InnerQueue> que = (Map<Long, InnerQueue>) f.get(this.queue);
        assertNotNull(que);
        assertEquals(0, que.size());
        
        f = Queue.class.getDeclaredField("typeQueues");
        f.setAccessible(true);
        que = (Map<Long, InnerQueue>) f.get(this.queue);
        assertNotNull(que);
        assertEquals(0, que.size());
        
        f = Queue.class.getDeclaredField("capabilityQueues");
        f.setAccessible(true);
        que = (Map<Long, InnerQueue>) f.get(this.queue);
        assertNotNull(que);
        assertEquals(0, que.size());
    }
    
    @Test
    public void testIsRigQueued() throws Exception
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
        rt.setName("Perm_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("perm,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Perm_Rig_Test_Rig1");
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
        
        this.queue.addEntry(ses1, db);
        
        assertTrue(this.queue.isRigQueued(r.getId(), db));
        
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
        db.close();
    }
    
    @Test
    public void testIsRigQueuedType() throws Exception
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
        rt.setName("Perm_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("perm,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Perm_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("TYPE");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRigType(rt);
        db.persist(p1);
        
        Session ses1 = new Session();
        ses1.setActive(true);
        ses1.setReady(true);
        ses1.setActivityLastUpdated(now);
        ses1.setExtensions((short) 5);
        ses1.setPriority((short) 5);
        ses1.setRequestTime(now);
        ses1.setRequestedResourceId(rt.getId());
        ses1.setRequestedResourceName(rt.getName());
        ses1.setResourceType("TYPE");
        ses1.setResourcePermission(p1);
        ses1.setUser(user1);
        ses1.setUserName(user1.getName());
        ses1.setUserNamespace(user1.getNamespace());
        db.persist(ses1);
        db.getTransaction().commit();
        
        this.queue.addEntry(ses1, db);
        
        assertTrue(this.queue.isRigQueued(r.getId(), db));
        
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
        db.close();
    }
    
    @Test
    public void testIsRigQueuedCaps() throws Exception
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
        rt.setName("Perm_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("perm,test,rig,type");
        db.persist(caps);
        
        RequestCapabilities reqCaps = new RequestCapabilities("perm");
        db.persist(reqCaps);
        
        MatchingCapabilities match = new MatchingCapabilities(
                new MatchingCapabilitiesId(caps.getId(), reqCaps.getId()), reqCaps, caps);
        db.persist(match);
        
        Rig r = new Rig();
        r.setName("Perm_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("CAPABILITY");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRequestCapabilities(reqCaps);
        db.persist(p1);
        
        Session ses1 = new Session();
        ses1.setActive(true);
        ses1.setReady(true);
        ses1.setActivityLastUpdated(now);
        ses1.setExtensions((short) 5);
        ses1.setPriority((short) 5);
        ses1.setRequestTime(now);
        ses1.setRequestedResourceId(reqCaps.getId());
        ses1.setRequestedResourceName(reqCaps.getCapabilities());
        ses1.setResourceType("CAPABILITY");
        ses1.setResourcePermission(p1);
        ses1.setUser(user1);
        ses1.setUserName(user1.getName());
        ses1.setUserNamespace(user1.getNamespace());
        db.persist(ses1);
        db.getTransaction().commit();
        
        db.refresh(reqCaps);
        db.refresh(caps);
        
        this.queue.addEntry(ses1, db);
        
        assertTrue(this.queue.isRigQueued(r.getId(), db));
        
        db.beginTransaction();
        db.delete(ses1);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(match);
        db.delete(reqCaps);
        db.delete(caps);
        db.delete(ass1);
        db.delete(uc1);
        db.delete(user1);
        db.getTransaction().commit();
        db.close();
    }
    
    @Test
    public void testIsRigQueuedNotQueued() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 10000);
        
        db.beginTransaction();
        RigType rt = new RigType();
        rt.setName("Perm_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("perm,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Perm_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        db.getTransaction().commit();
        
        assertFalse(this.queue.isRigQueued(r.getId(), db));
        
        db.beginTransaction();
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.getTransaction().commit();
        db.close();
    }
}
