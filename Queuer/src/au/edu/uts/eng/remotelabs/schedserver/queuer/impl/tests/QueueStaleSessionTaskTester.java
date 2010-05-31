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
package au.edu.uts.eng.remotelabs.schedserver.queuer.impl.tests;


import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Before;

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
import au.edu.uts.eng.remotelabs.schedserver.queuer.impl.QueueStaleSessionTask;

/**
 * Tests the {@link QueueStaleSessionTask} class.
 */
public class QueueStaleSessionTaskTester extends TestCase
{
    /** Object of class under test. */
    public QueueStaleSessionTask task;
    
    @Override
    @Before
    public void setUp() throws Exception
    {
        DataAccessTestSetup.setup();
        this.task = new QueueStaleSessionTask();
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
    }
    
    @SuppressWarnings("unchecked")
    public void testRun() throws Exception
    {
        Queue queue= Queue.getInstance();
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 10000);
        Date after = new Date(System.currentTimeMillis() + 10000);
        Date now = new Date();
        Date expired = new Date(System.currentTimeMillis() - 305000);
        
        db.beginTransaction();
        User user1 = new User("stale1", "testns", "USER");
        db.persist(user1);
        User user2 = new User("stale2", "testns", "USER");
        db.persist(user2);
        User user3 = new User("stale3", "testns", "USER");
        db.persist(user3);
        User user4 = new User("stale4", "testns", "USER");
        db.persist(user4);
        User user5 = new User("stale5", "testns", "USER");
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
        rt.setName("Stale_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("perm,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Stale_Rig_Test_Rig1");
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
        p1.setQueueActivityTimeout(300);
        db.persist(p1);
        
        /* Stale. */
        Session ses1 = new Session();
        ses1.setActive(true);
        ses1.setReady(true);
        ses1.setActivityLastUpdated(expired);
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
        /* Batch not state. */
        Session ses2 = new Session();
        ses2.setActive(true);
        ses2.setReady(true);
        ses2.setActivityLastUpdated(expired);
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
        ses2.setCodeReference("/foo/bar/baz");
        db.persist(ses2);
        /* In session. */
        Session ses3 = new Session();
        ses3.setActive(true);
        ses3.setReady(true);
        ses3.setActivityLastUpdated(expired);
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
        ses3.setAssignmentTime(now);
        ses3.setRig(r);
        db.persist(ses3);
        /* Ok. */
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
        /* Stale. */
        Session ses5 = new Session();
        ses5.setActive(true);
        ses5.setReady(true);
        ses5.setActivityLastUpdated(expired);
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
        /* Not active. */
        Session ses6 = new Session();
        ses6.setActive(false);
        ses6.setReady(true);
        ses6.setActivityLastUpdated(expired);
        ses6.setExtensions((short) 5);
        ses6.setPriority((short) 5);
        ses6.setRequestTime(new Date(System.currentTimeMillis() - 4000));
        ses6.setRequestedResourceId(r.getId());
        ses6.setRequestedResourceName(r.getName());
        ses6.setResourceType("RIG");
        ses6.setResourcePermission(p1);
        ses6.setUser(user5);
        ses6.setUserName(user5.getName());
        ses6.setUserNamespace(user5.getNamespace());
        db.persist(ses6);
        db.getTransaction().commit();
        
        queue.addEntry(ses1, db);
        queue.addEntry(ses2, db);
        queue.addEntry(ses4, db);
        queue.addEntry(ses5, db);
        
        Field f = Queue.class.getDeclaredField("rigQueues");
        f.setAccessible(true);
        Map<Long, InnerQueue> que = (Map<Long, InnerQueue>) f.get(queue);
        assertNotNull(que);
        assertEquals(1, que.size());
        
        InnerQueue in = que.get(r.getId());
        assertNotNull(in);
        assertEquals(4, in.size());
        
        this.task.run();
        
        db.refresh(ses1);
        db.refresh(ses5);
        
        
        db.beginTransaction();
        db.delete(ses1);
        db.delete(ses2);
        db.delete(ses3);
        db.delete(ses4);
        db.delete(ses5);
        db.delete(ses6);
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
        
        f = Queue.class.getDeclaredField("rigQueues");
        f.setAccessible(true);
        que = (Map<Long, InnerQueue>) f.get(queue);
        assertNotNull(que);
        assertEquals(1, que.size());
        
        in = que.get(r.getId());
        assertNotNull(in);
        assertEquals(2, in.size());
        
        assertNotNull(ses1.getRemovalReason());
        assertNotNull(ses1.getRequestTime());
        assertFalse(ses1.isActive());
        
        assertNotNull(ses5.getRemovalReason());
        assertNotNull(ses5.getRequestTime());
        assertFalse(ses5.isActive());       
    }
}
