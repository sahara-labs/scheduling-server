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
 * @date 18th October 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.service.tests;

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
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.service.RigClientProxy;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.service.types.AllocateCallback;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.service.types.AllocateCallbackResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.service.types.CallbackRequestType;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.service.types.CallbackResponseType;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.service.types.ErrorType;

/**
 * Tests the {@link RigClientProxy} class.
 */
public class RigClientProxyTester extends TestCase
{
    /** Object of class under test. */
    private RigClientProxy service;

    @Override
    @Before
    public void setUp() throws Exception
    {
        super.setUp();
        DataAccessTestSetup.setup();
        
        this.service = new RigClientProxy();
    }

    
    @Test
    public void testAllocateCallback()
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        Date now = new Date();
        db.beginTransaction();
        
        User user = new User("rcpperm1", "testns", "USER");
        db.persist(user);
        
        UserClass uc = new UserClass();
        uc.setName("uc1");
        uc.setActive(true);
        uc.setQueuable(true);
        uc.setPriority((short)4);
        db.persist(uc);
        
        UserAssociation ass = new UserAssociation(new UserAssociationId(user.getId(), uc.getId()), uc, user);
        db.persist(ass);
        
        RigType rt = new RigType();
        rt.setName("RCP_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("rcp,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("RCP_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(now);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission rp = new ResourcePermission();
        rp.setType("RIG");
        rp.setUserClass(uc);
        rp.setStartTime(now);
        rp.setExpiryTime(now);
        rp.setRig(r);
        rp.setAllowedExtensions((short)10);
        db.persist(rp);
        
        Session ses = new Session();
        ses.setActive(true);
        ses.setReady(false);
        ses.setActivityLastUpdated(now);
        ses.setExtensions((short) 5);
        ses.setPriority((short) 5);
        ses.setRequestTime(now);
        ses.setRequestedResourceId(r.getId());
        ses.setRequestedResourceName(r.getName());
        ses.setResourceType("RIG");
        ses.setResourcePermission(rp);
        ses.setAssignedRigName(r.getName());
        ses.setAssignmentTime(now);
        ses.setRig(r);
        ses.setUser(user);
        ses.setUserName(user.getName());
        ses.setUserNamespace(user.getNamespace());
        db.persist(ses);
        
        r.setSession(ses);
        db.getTransaction().commit();
        
        AllocateCallback alloc = new AllocateCallback();
        CallbackRequestType call = new CallbackRequestType();
        alloc.setAllocateCallback(call);
        call.setSuccess(true);
        call.setRigname(r.getName());
        
        AllocateCallbackResponse resp = this.service.allocateCallback(alloc);
        assertNotNull(resp);
        
        CallbackResponseType status = resp.getAllocateCallbackResponse();
        assertNotNull(status);
        assertTrue(status.getSuccess());
        
        db.refresh(ses);
        assertTrue(ses.isReady());
        assertTrue(ses.isActive());
        assertNull(ses.getRemovalReason());
        
        db.beginTransaction();
        r.setSession(null);
        db.delete(ses);
        db.delete(rp);
        db.delete(r);
        db.delete(caps);
        db.delete(rt);
        db.delete(ass);
        db.delete(uc);
        db.delete(user);
        db.getTransaction().commit();
    }
    
    @Test
    public void testAllocateCallbackFailAlloc()
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        Date now = new Date();
        db.beginTransaction();
        
        User user = new User("rcpperm1", "testns", "USER");
        db.persist(user);
        
        UserClass uc = new UserClass();
        uc.setName("uc1");
        uc.setActive(true);
        uc.setQueuable(true);
        uc.setPriority((short)4);
        db.persist(uc);
        
        UserAssociation ass = new UserAssociation(new UserAssociationId(user.getId(), uc.getId()), uc, user);
        db.persist(ass);
        
        RigType rt = new RigType();
        rt.setName("RCP_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("rcp,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("RCP_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(now);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission rp = new ResourcePermission();
        rp.setType("RIG");
        rp.setUserClass(uc);
        rp.setStartTime(now);
        rp.setExpiryTime(now);
        rp.setRig(r);
        rp.setAllowedExtensions((short)10);
        db.persist(rp);
        
        Session ses = new Session();
        ses.setActive(true);
        ses.setReady(false);
        ses.setActivityLastUpdated(now);
        ses.setExtensions((short) 5);
        ses.setPriority((short) 5);
        ses.setRequestTime(now);
        ses.setRequestedResourceId(r.getId());
        ses.setRequestedResourceName(r.getName());
        ses.setResourceType("RIG");
        ses.setResourcePermission(rp);
        ses.setAssignedRigName(r.getName());
        ses.setAssignmentTime(now);
        ses.setRig(r);
        ses.setUser(user);
        ses.setUserName(user.getName());
        ses.setUserNamespace(user.getNamespace());
        db.persist(ses);
        
        r.setSession(ses);
        db.getTransaction().commit();
        
        AllocateCallback alloc = new AllocateCallback();
        CallbackRequestType call = new CallbackRequestType();
        alloc.setAllocateCallback(call);
        call.setSuccess(false);
        call.setRigname(r.getName());
        
        ErrorType err = new ErrorType();
        err.setCode(1);
        err.setOperation("Allocation callback");
        err.setReason("Allocation failed for some unknown reason");
        call.setError(err);
        
        AllocateCallbackResponse resp = this.service.allocateCallback(alloc);
        assertNotNull(resp);
        
        CallbackResponseType status = resp.getAllocateCallbackResponse();
        assertNotNull(status);
        assertTrue(status.getSuccess());
        
        db.refresh(ses);
        db.refresh(r);
        
        assertFalse(ses.isActive());
        assertFalse(ses.isReady());
        assertNotNull(ses.getRemovalReason());
        assertNotNull(ses.getRemovalTime());
        
        assertFalse(r.isInSession());
        assertFalse(r.isOnline());
        assertNotNull(r.getOfflineReason());
        assertNull(r.getSession());
        
        db.beginTransaction();
        r.setSession(null);
        db.delete(ses);
        db.delete(rp);
        db.delete(r);
        db.delete(caps);
        db.delete(rt);
        db.delete(ass);
        db.delete(uc);
        db.delete(user);
        db.getTransaction().commit();
    }
    
    @Test
    public void testAllocateCallbackNoSession()
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        Date now = new Date();
        db.beginTransaction();
        
        User user = new User("rcpperm1", "testns", "USER");
        db.persist(user);
        
        UserClass uc = new UserClass();
        uc.setName("uc1");
        uc.setActive(true);
        uc.setQueuable(true);
        uc.setPriority((short)4);
        db.persist(uc);
        
        UserAssociation ass = new UserAssociation(new UserAssociationId(user.getId(), uc.getId()), uc, user);
        db.persist(ass);
        
        RigType rt = new RigType();
        rt.setName("RCP_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("rcp,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("RCP_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(now);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission rp = new ResourcePermission();
        rp.setType("RIG");
        rp.setUserClass(uc);
        rp.setStartTime(now);
        rp.setExpiryTime(now);
        rp.setRig(r);
        rp.setAllowedExtensions((short)10);
        db.persist(rp);

        db.getTransaction().commit();
        
        AllocateCallback alloc = new AllocateCallback();
        CallbackRequestType call = new CallbackRequestType();
        alloc.setAllocateCallback(call);
        call.setSuccess(true);
        call.setRigname(r.getName());
        
        AllocateCallbackResponse resp = this.service.allocateCallback(alloc);
        assertNotNull(resp);
        
        CallbackResponseType status = resp.getAllocateCallbackResponse();
        assertNotNull(status);
        assertFalse(status.getSuccess());
        
        db.refresh(r);
        
        assertFalse(r.isInSession());
        assertTrue(r.isOnline());
        assertNull(r.getSession());
        
        db.beginTransaction();
        r.setSession(null);
        db.delete(rp);
        db.delete(r);
        db.delete(caps);
        db.delete(rt);
        db.delete(ass);
        db.delete(uc);
        db.delete(user);
        db.getTransaction().commit();
    }
    
//    @Test
//    public void testAllocateCallbackNoRig()
//    {
//        org.hibernate.Session db = DataAccessActivator.getNewSession();
//        Date now = new Date();
//        db.beginTransaction();
//        
//        User user = new User("rcpperm1", "testns", "USER");
//        db.persist(user);
//        
//        UserClass uc = new UserClass();
//        uc.setName("uc1");
//        uc.setActive(true);
//        uc.setQueuable(true);
//        uc.setPriority((short)4);
//        db.persist(uc);
//        
//        UserAssociation ass = new UserAssociation(new UserAssociationId(user.getId(), uc.getId()), uc, user);
//        db.persist(ass);
//        
//        RigType rt = new RigType();
//        rt.setName("RCP_Test_Rig_Type");
//        db.persist(rt);
//        
//        RigCapabilities caps = new RigCapabilities("rcp,test,rig,type");
//        db.persist(caps);
//        
//        Rig r = new Rig();
//        r.setName("RCP_Rig_Test_Rig1");
//        r.setRigType(rt);
//        r.setRigCapabilities(caps);
//        r.setLastUpdateTimestamp(now);
//        r.setActive(true);
//        r.setOnline(true);
//        r.setInSession(true);
//        db.persist(r);
//        
//        ResourcePermission rp = new ResourcePermission();
//        rp.setType("RIG");
//        rp.setUserClass(uc);
//        rp.setStartTime(now);
//        rp.setExpiryTime(now);
//        rp.setRig(r);
//        rp.setAllowedExtensions((short)10);
//        db.persist(rp);
//        
//        Session ses = new Session();
//        ses.setActive(true);
//        ses.setReady(false);
//        ses.setActivityLastUpdated(now);
//        ses.setExtensions((short) 5);
//        ses.setPriority((short) 5);
//        ses.setRequestTime(now);
//        ses.setRequestedResourceId(r.getId());
//        ses.setRequestedResourceName(r.getName());
//        ses.setResourceType("RIG");
//        ses.setResourcePermission(rp);
//        ses.setAssignedRigName(r.getName());
//        ses.setAssignmentTime(now);
//        ses.setRig(r);
//        ses.setUser(user);
//        ses.setUserName(user.getName());
//        ses.setUserNamespace(user.getNamespace());
//        db.persist(ses);
//        
//        r.setSession(ses);
//        db.getTransaction().commit();
//        
//        db.beginTransaction();
//        r.setSession(null);
//        db.delete(ses);
//        db.delete(rp);
//        db.delete(r);
//        db.delete(caps);
//        db.delete(rt);
//        db.delete(ass);
//        db.delete(uc);
//        db.delete(user);
//        db.getTransaction().commit();
//    }
//
//    /**
//     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.service.RigClientProxy#releaseCallback(au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.service.types.ReleaseCallback)}.
//     */
//    @Test
//    public void testReleaseCallback()
//    {
//        fail("Not yet implemented"); // TODO
//    }

}
