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
 * @date 8th April 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.queuer.tests;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
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
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.testsetup.DataAccessTestSetup;
import au.edu.uts.eng.remotelabs.schedserver.queuer.QueueInfo;
import au.edu.uts.eng.remotelabs.schedserver.queuer.impl.InnerQueue;
import au.edu.uts.eng.remotelabs.schedserver.queuer.impl.Queue;

/**
 * Tests the {@link QueueInfo} class.
 */
public class QueueInfoTester extends TestCase
{
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
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.QueueInfo#isQueued(au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig, org.hibernate.Session)}.
     */
    @Test
    public void testIsQueuedRigSession()
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 10000);
        Date after = new Date(System.currentTimeMillis() + 10000);
        Date now = new Date();
        
        db.beginTransaction();
        User user1 = new User("queueperm1", "testns", "USER");
        db.persist(user1);
        
        UserClass uc1 = new UserClass();
        uc1.setName("uc1");
        uc1.setActive(true);
        db.persist(uc1);
        
        UserAssociation ass1 = new UserAssociation(new UserAssociationId(user1.getId(), uc1.getId()), uc1, user1);
        db.persist(ass1);
        
        RigType rt = new RigType();
        rt.setName("Queue_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("perm,test,rig,type");
        db.persist(caps);
        
        RequestCapabilities reqCaps = new RequestCapabilities("perm");
        db.persist(reqCaps);
        
        MatchingCapabilities match = new MatchingCapabilities(
                new MatchingCapabilitiesId(caps.getId(), reqCaps.getId()), reqCaps, caps);
        db.persist(match);
        
        Rig r = new Rig();
        r.setName("Queue_Rig_Test_Rig1");
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
        
        Queue.getInstance().addEntry(ses1, db);
        
        assertTrue(QueueInfo.isQueued(r.getId(), db));
        
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

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.queuer.QueueInfo#isQueued(long, org.hibernate.Session)}.
     */
    @Test
    public void testIsQueuedLongSession()
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 10000);
        
        db.beginTransaction();
        RigType rt = new RigType();
        rt.setName("Queue_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("perm,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Queue_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        db.getTransaction().commit();
        
        assertFalse(QueueInfo.isQueued(r.getId(), db));
        
        db.beginTransaction();
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.getTransaction().commit();
        db.close();
    }

}
