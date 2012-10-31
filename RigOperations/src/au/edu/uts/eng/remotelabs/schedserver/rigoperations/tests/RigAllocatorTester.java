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
package au.edu.uts.eng.remotelabs.schedserver.rigoperations.tests;


import java.lang.reflect.Field;
import java.util.Date;

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
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.testsetup.DataAccessTestSetup;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.proxy.intf.types.AllocateResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.proxy.intf.types.ErrorType;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.proxy.intf.types.OperationResponseType;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.requests.RigAllocator;

/**
 * Tests the {@link RigAllocator} class.
 */
public class RigAllocatorTester extends TestCase
{
    /** Object of class under test. */
    private RigAllocator alloc;
    
    @Override
    @Before
    public void setUp() throws Exception
    {
        DataAccessTestSetup.setup();
        this.alloc = new RigAllocator();
    }
    
    @Test
    public void testAllocateResponseCallback() throws Exception
    {
        Date before = new Date(System.currentTimeMillis() - 10000);
        Date after = new Date(System.currentTimeMillis() + 10000);
        Date now = new Date();
        
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
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
        ses1.setReady(false);
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
        ses1.setAssignmentTime(now);
        ses1.setRig(r);
        db.persist(ses1);        
        db.getTransaction().commit();

        AllocateResponse resp = new AllocateResponse();
        OperationResponseType op = new OperationResponseType();
        resp.setAllocateResponse(op);
        op.setSuccess(true);
        
        Field f = RigAllocator.class.getDeclaredField("session");
        f.setAccessible(true);
        f.set(this.alloc, ses1);
        
        this.alloc.allocateResponseCallback(resp);
        
        db.refresh(ses1);
        db.refresh(r);
        
        db.beginTransaction();
        db.createQuery("DELETE RigLog WHERE rig='" + r.getId() + "'").executeUpdate();
        db.getTransaction().commit();
        
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
        
        assertTrue(ses1.isActive());
        assertTrue(ses1.isReady());
        assertNull(ses1.getRemovalReason());
        assertNull(ses1.getRemovalTime());
        
        assertTrue(r.isInSession());
        assertTrue(r.isOnline());
        assertNull(r.getOfflineReason());
    }
    
    @Test
    public void testAllocateResponseCallbackAsync() throws Exception
    {
        Date before = new Date(System.currentTimeMillis() - 10000);
        Date after = new Date(System.currentTimeMillis() + 10000);
        Date now = new Date();
        
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
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
        ses1.setReady(false);
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
        ses1.setAssignmentTime(now);
        ses1.setRig(r);
        db.persist(ses1);        
        db.getTransaction().commit();

        AllocateResponse resp = new AllocateResponse();
        OperationResponseType op = new OperationResponseType();
        resp.setAllocateResponse(op);
        op.setSuccess(true);
        op.setWillCallback(true);
        
        Field f = RigAllocator.class.getDeclaredField("session");
        f.setAccessible(true);
        f.set(this.alloc, ses1);
        
        this.alloc.allocateResponseCallback(resp);
        
        db.refresh(ses1);
        db.refresh(r);
        
        db.beginTransaction();
        db.createQuery("DELETE RigLog WHERE rig='" + r.getId() + "'").executeUpdate();
        db.getTransaction().commit();
        
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
        
        assertTrue(ses1.isActive());
        assertFalse(ses1.isReady());
        assertNull(ses1.getRemovalReason());
        assertNull(ses1.getRemovalTime());
        
        assertTrue(r.isInSession());
        assertTrue(r.isOnline());
        assertNull(r.getOfflineReason());
    }
    
    @Test
    public void testAllocateResponseCallbackError() throws Exception
    {
        Date before = new Date(System.currentTimeMillis() - 10000);
        Date after = new Date(System.currentTimeMillis() + 10000);
        Date now = new Date();
        
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
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
        ses1.setReady(false);
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
        ses1.setAssignmentTime(now);
        ses1.setRig(r);
        db.persist(ses1);       
        
        r.setSession(ses1);
        
        db.getTransaction().commit();

        AllocateResponse resp = new AllocateResponse();
        OperationResponseType op = new OperationResponseType();
        resp.setAllocateResponse(op);
        op.setSuccess(false);
        ErrorType err = new ErrorType();
        op.setError(err);
        err.setCode(1);
        err.setOperation("Alloc");
        err.setReason("Failure test.");
        
        Field f = RigAllocator.class.getDeclaredField("session");
        f.setAccessible(true);
        f.set(this.alloc, ses1);
        
        this.alloc.allocateResponseCallback(resp);
        
        db.refresh(ses1);
        db.refresh(r);
        
        db.beginTransaction();
        db.createQuery("DELETE RigLog WHERE rig='" + r.getId() + "'").executeUpdate();
        db.getTransaction().commit();
        
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
        
        assertFalse(ses1.isActive());
        assertFalse(ses1.isReady());
        assertNotNull(ses1.getRemovalReason());
        assertNotNull(ses1.getRemovalTime());
        
        assertFalse(r.isInSession());
        assertFalse(r.isOnline());
        assertNotNull(r.getOfflineReason());
        assertNull(r.getSession());
    }
    
    @Test
    public void testAllocateErrorCallback() throws Exception
    {
        Date before = new Date(System.currentTimeMillis() - 10000);
        Date after = new Date(System.currentTimeMillis() + 10000);
        Date now = new Date();
        
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
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
        ses1.setReady(false);
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
        ses1.setAssignmentTime(now);
        ses1.setRig(r);
        db.persist(ses1);        
        
        r.setSession(ses1);
        
        db.getTransaction().commit();
        
        Field f = RigAllocator.class.getDeclaredField("session");
        f.setAccessible(true);
        f.set(this.alloc, ses1);
        
        this.alloc.allocateErrorCallback(new Exception("Fail!"));
        
        db.refresh(ses1);
        db.refresh(r);
        
        db.beginTransaction();
        db.createQuery("DELETE RigLog WHERE rig='" + r.getId() + "'").executeUpdate();
        db.getTransaction().commit();
        
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
        
        assertFalse(ses1.isActive());
        assertFalse(ses1.isReady());
        assertNotNull(ses1.getRemovalReason());
        assertNotNull(ses1.getRemovalTime());
        
        assertFalse(r.isInSession());
        assertFalse(r.isOnline());
        assertNotNull(r.getOfflineReason());
        assertNull(r.getSession());
    }
}
