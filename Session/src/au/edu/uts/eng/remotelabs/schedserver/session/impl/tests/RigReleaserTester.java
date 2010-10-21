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
package au.edu.uts.eng.remotelabs.schedserver.session.impl.tests;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;

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
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.testsetup.DataAccessTestSetup;
import au.edu.uts.eng.remotelabs.schedserver.queuer.impl.InnerQueue;
import au.edu.uts.eng.remotelabs.schedserver.queuer.impl.Queue;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.ErrorType;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.OperationResponseType;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.ReleaseResponse;
import au.edu.uts.eng.remotelabs.schedserver.session.impl.RigReleaser;

/**
 * Tests the {@link RigReleaser} class.
 */
public class RigReleaserTester extends TestCase
{
    /** Object of class under test. */
    private RigReleaser releaser;
    
    @Override
    @Before
    protected void setUp() throws Exception
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
        
        this.releaser = new RigReleaser();
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.session.impl.RigReleaser#releaseResponseCallback(au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.ReleaseResponse)}.
     */
    public void testReleaseResponseCallback() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 10000);
        Date after = new Date(System.currentTimeMillis() + 10000);
        Date now = new Date();
        
        db.beginTransaction();
        
        User user = new User("qperm1", "testns", "USER");
        db.persist(user);
        
        UserClass uc1 = new UserClass();
        uc1.setName("uc1");
        uc1.setActive(true);
        uc1.setQueuable(true);
        uc1.setPriority((short)4);
        db.persist(uc1);
        
        UserAssociation ass = new UserAssociation(new UserAssociationId(user.getId(), uc1.getId()), uc1, user);
        db.persist(ass);
        
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
        p1.setAllowedExtensions((short)10);
        db.persist(p1);
        
        Session ses = new Session();
        ses.setActive(true);
        ses.setReady(true);
        ses.setActivityLastUpdated(now);
        ses.setExtensions((short) 5);
        ses.setPriority((short) 5);
        ses.setRequestTime(now);
        ses.setRequestedResourceId(r.getId());
        ses.setRequestedResourceName(r.getName());
        ses.setResourceType("RIG");
        ses.setResourcePermission(p1);
        ses.setUser(user);
        ses.setUserName(user.getName());
        ses.setUserNamespace(user.getNamespace());
        db.persist(ses);

        db.getTransaction().commit();
        
        db.refresh(uc1);
        db.refresh(user);
        db.refresh(p1);
        db.refresh(r);
        db.refresh(rt);
        db.refresh(ses);
                
        Queue queue = Queue.getInstance();
        queue.addEntry(ses, db);
        
        assertNull(ses.getAssignedRigName());
        assertNull(ses.getAssignmentTime());
        assertNull(ses.getRig());
        
        Field f = RigReleaser.class.getDeclaredField("rig");
        f.setAccessible(true);
        f.set(this.releaser, r);
        
        ReleaseResponse resp = new ReleaseResponse();
        OperationResponseType op = new OperationResponseType();
        resp.setReleaseResponse(op);
        op.setSuccess(true);
        
        this.releaser.releaseResponseCallback(resp);
        
        db.refresh(r);
        db.refresh(ses);
        
        db.beginTransaction();
        db.createQuery("DELETE RigLog WHERE rig='" + r.getId() + "'").executeUpdate();
        db.getTransaction().commit();
        
        db.beginTransaction();
        db.delete(ses);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass);
        db.delete(uc1);
        db.delete(user);
        db.getTransaction().commit();
        
        assertTrue(r.isInSession());
        
        assertNotNull(ses.getAssignedRigName());
        assertNotNull(ses.getAssignmentTime());
        assertNotNull(ses.getRig());
        assertEquals(r.getId(), ses.getRig().getId());
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.session.impl.RigReleaser#releaseResponseCallback(au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.ReleaseResponse)}.
     */
    public void testReleaseResponseCallbackAsync() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 10000);
        Date after = new Date(System.currentTimeMillis() + 10000);
        Date now = new Date();
        
        db.beginTransaction();
        
        User user = new User("qperm1", "testns", "USER");
        db.persist(user);
        
        UserClass uc1 = new UserClass();
        uc1.setName("uc1");
        uc1.setActive(true);
        uc1.setQueuable(true);
        uc1.setPriority((short)4);
        db.persist(uc1);
        
        UserAssociation ass = new UserAssociation(new UserAssociationId(user.getId(), uc1.getId()), uc1, user);
        db.persist(ass);
        
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
        p1.setAllowedExtensions((short)10);
        db.persist(p1);
        
        Session ses = new Session();
        ses.setActive(true);
        ses.setReady(true);
        ses.setActivityLastUpdated(now);
        ses.setExtensions((short) 5);
        ses.setPriority((short) 5);
        ses.setRequestTime(now);
        ses.setRequestedResourceId(r.getId());
        ses.setRequestedResourceName(r.getName());
        ses.setResourceType("RIG");
        ses.setResourcePermission(p1);
        ses.setUser(user);
        ses.setUserName(user.getName());
        ses.setUserNamespace(user.getNamespace());
        db.persist(ses);
        
        r.setSession(ses);
        
        db.getTransaction().commit();
        
        db.refresh(uc1);
        db.refresh(user);
        db.refresh(p1);
        db.refresh(r);
        db.refresh(rt);
        db.refresh(ses);
                
        Field f = RigReleaser.class.getDeclaredField("rig");
        f.setAccessible(true);
        f.set(this.releaser, r);
        
        ReleaseResponse resp = new ReleaseResponse();
        OperationResponseType op = new OperationResponseType();
        resp.setReleaseResponse(op);
        op.setSuccess(true);
        op.setWillCallback(true);
        
        this.releaser.releaseResponseCallback(resp);
        
        db.refresh(r);
        db.refresh(ses);
        
        db.beginTransaction();
        db.createQuery("DELETE RigLog WHERE rig='" + r.getId() + "'").executeUpdate();
        db.getTransaction().commit();
        
        db.beginTransaction();
        db.delete(ses);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass);
        db.delete(uc1);
        db.delete(user);
        db.getTransaction().commit();
        
        assertTrue(r.isInSession());
        assertNotNull(r.getSession());
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.session.impl.RigReleaser#releaseResponseCallback(au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.ReleaseResponse)}.
     */
    public void testReleaseResponseCallbackError() throws Exception
    {
        Date before = new Date(System.currentTimeMillis() - 10000);
        
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
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
        r.setInSession(false);
        db.persist(r);
        
        db.getTransaction().commit();

        ReleaseResponse resp = new ReleaseResponse();
        OperationResponseType op = new OperationResponseType();
        resp.setReleaseResponse(op);
        op.setSuccess(false);
        ErrorType err = new ErrorType();
        op.setError(err);
        err.setCode(1);
        err.setOperation("Alloc");
        err.setReason("Failure test.");
        
        Field f = RigReleaser.class.getDeclaredField("rig");
        f.setAccessible(true);
        f.set(this.releaser, r);
        
        this.releaser.releaseResponseCallback(resp);
        
        db.refresh(r);
        
        db.beginTransaction();
        db.createQuery("DELETE RigLog WHERE rig='" + r.getId() + "'").executeUpdate();
        db.getTransaction().commit();
        
        db.beginTransaction();
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.getTransaction().commit();

        assertFalse(r.isInSession());
        assertFalse(r.isOnline());
        assertNotNull(r.getOfflineReason());
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.session.impl.RigReleaser#releaseErrorCallback(java.lang.Exception)}.
     */
    public void testReleaseErrorCallback() throws Exception
    {
        Date before = new Date(System.currentTimeMillis() - 10000);
        
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
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
        r.setInSession(false);
        db.persist(r);
        
        db.getTransaction().commit();
        
        Field f = RigReleaser.class.getDeclaredField("rig");
        f.setAccessible(true);
        f.set(this.releaser, r);
        
        this.releaser.releaseErrorCallback(new Exception("Error"));
        
        db.refresh(r);
        
        db.beginTransaction();
        db.createQuery("DELETE RigLog WHERE rig='" + r.getId() + "'").executeUpdate();
        db.getTransaction().commit();
        
        db.beginTransaction();
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.getTransaction().commit();

        assertFalse(r.isInSession());
        assertFalse(r.isOnline());
        assertNotNull(r.getOfflineReason());
    }

}
