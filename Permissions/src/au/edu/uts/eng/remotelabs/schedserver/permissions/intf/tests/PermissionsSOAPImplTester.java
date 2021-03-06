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
package au.edu.uts.eng.remotelabs.schedserver.permissions.intf.tests;

import java.lang.reflect.Method;
import java.util.Date;

import junit.framework.TestCase;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserClassDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RequestCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserAssociation;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserAssociationId;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClass;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserLock;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.testsetup.DataAccessTestSetup;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.PermissionsSOAPImpl;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermission;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForAcademic;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermission;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUser;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserClass;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUser;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClass;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassesForUser;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.OperationResponseType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.PermissionIDType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.PermissionType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.PermissionWithLockListType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.PermissionWithLockType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.ResourceClass;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.ResourceIDType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UnlockUserLock;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UnlockUserLockResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserClassIDType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserClassType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserIDType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserLockIDUserPermSequence;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserLockType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserNameNamespaceSequence;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserType;

/**
 * Tests the {@link PermissionsSOAPImpl} class.
 */
public class PermissionsSOAPImplTester extends TestCase
{
    /** Object of class under test. */
    private PermissionsSOAPImpl permissions;
    
    @Override
    @Before
    public void setUp() throws Exception
    {
        DataAccessTestSetup.setup();
        this.permissions = new PermissionsSOAPImpl();
    }

    /**
     * Test method for {@link PermissionsSOAPImpl#getAcademicPermission(GetAcademicPermission)}.
     */
    @Test
    public void testGetAcademicPermission()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link PermissionsSOAPImpl#getAcademicPermissionsForAcademic(GetAcademicPermissionsForAcademic)}.
     */
    @Test
    public void testGetAcademicPermissionsForAcademic()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link PermissionsSOAPImpl#getAcademicPermissionsForUserClass(GetAcademicPermissionsForUserClass)}.
     */
    @Test
    public void testGetAcademicPermissionsForUserClass()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link PermissionsSOAPImpl#getPermission(GetPermission)}.
     */
    @Test
    public void testGetPermission()
    {
        Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        UserClass uclass1 = new UserClass();
        uclass1.setName("permTestClass2");
        uclass1.setActive(true);
        uclass1.setQueuable(false);
        uclass1.setBookable(true);
        uclass1.setTimeHorizon(1000);
        ses.save(uclass1);
        RigType rigType1 = new RigType("interRigType1", 300, false);
        ses.save(rigType1);
        ResourcePermission perm1 = new ResourcePermission();
        perm1.setUserClass(uclass1);
        perm1.setType("TYPE");
        perm1.setSessionDuration(3600);
        perm1.setQueueActivityTimeout(300);
        perm1.setAllowedExtensions((short)10);
        perm1.setSessionActivityTimeout(300);
        perm1.setExtensionDuration(300);
        perm1.setMaximumBookings(10);
        perm1.setRigType(rigType1);
        perm1.setStartTime(new Date());
        perm1.setExpiryTime(new Date());
        perm1.setDisplayName("display_name1");
        ses.save(perm1);
        ses.getTransaction().commit();
        
        GetPermission request = new GetPermission();
        PermissionIDType pid = new PermissionIDType();
        pid.setPermissionID(perm1.getId().intValue());
        request.setGetPermission(pid);
        
        GetPermissionResponse response = this.permissions.getPermission(request);
        
        ses.beginTransaction();
        ses.delete(perm1);
        ses.delete(rigType1);
        ses.delete(uclass1);
        ses.getTransaction().commit();
        
        assertNotNull(response);
        PermissionType p = response.getGetPermissionResponse();
        assertNotNull(p);
        
        /* User class. */
        UserClassIDType ucid = p.getUserClass();
        assertNotNull(ucid);
        assertEquals(uclass1.getId().intValue(), ucid.getUserClassID());
        assertEquals(uclass1.getName(), ucid.getUserClassName());
        
        assertEquals(ResourceClass._TYPE, p.getResourceClass().toString());
        ResourceIDType rid = p.getResource();
        assertNotNull(rid);
        assertEquals(rigType1.getId().intValue(), rid.getResourceID());
        assertEquals(rigType1.getName(), rid.getResourceName());
        
        assertFalse(p.canQueue());
        assertTrue(p.canBook());
        
        assertEquals(uclass1.getTimeHorizon(), p.getTimeHorizon());
        assertEquals(perm1.getMaximumBookings(), p.getMaxBookings());
        assertEquals(perm1.getSessionDuration(), p.getSessionDuration());
        assertEquals(perm1.getExtensionDuration(), p.getExtensionDuration());
        assertEquals(perm1.getAllowedExtensions(), p.getAllowedExtensions());
        assertEquals(perm1.getQueueActivityTimeout(), p.getQueueActivityTmOut());
        assertEquals(perm1.getSessionActivityTimeout(), p.getSessionActivityTmOut());
        assertEquals(perm1.getDisplayName(), p.getDisplayName());
        
        assertEquals(perm1.getStartTime().getTime() / 1000, p.getStart().getTimeInMillis() / 1000);
        assertEquals(perm1.getExpiryTime().getTime() / 1000, p.getExpiry().getTimeInMillis() / 1000);
    }
    
    /**
     * Test method for {@link PermissionsSOAPImpl#getPermission(GetPermission)}.
     */
    @Test
    public void testGetPermissionRig()
    {
        Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        UserClass uclass1 = new UserClass();
        uclass1.setName("permTestClass2");
        uclass1.setActive(true);
        uclass1.setQueuable(false);
        uclass1.setBookable(true);
        uclass1.setTimeHorizon(1000);
        ses.save(uclass1);
        RigType rigType1 = new RigType("interRigType1", 300, false);
        ses.save(rigType1);
        RigCapabilities rigCaps = new RigCapabilities("not,important");
        ses.save(rigCaps);
        Rig rig1 = new Rig(rigType1, rigCaps, "interRig1", "http://contact", new Date(), true, null, false, true, true);
        ses.save(rig1);
        ResourcePermission perm1 = new ResourcePermission();
        perm1.setUserClass(uclass1);
        perm1.setType("RIG");
        perm1.setSessionDuration(3600);
        perm1.setQueueActivityTimeout(300);
        perm1.setAllowedExtensions((short)10);
        perm1.setSessionActivityTimeout(300);
        perm1.setExtensionDuration(300);
        perm1.setMaximumBookings(10);
        perm1.setRig(rig1);
        perm1.setStartTime(new Date());
        perm1.setExpiryTime(new Date());
        perm1.setDisplayName("display_name1");
        ses.save(perm1);
        ses.getTransaction().commit();
        
        GetPermission request = new GetPermission();
        PermissionIDType pid = new PermissionIDType();
        pid.setPermissionID(perm1.getId().intValue());
        request.setGetPermission(pid);
        
        GetPermissionResponse response = this.permissions.getPermission(request);
        
        ses.beginTransaction();
        ses.delete(perm1);
        ses.delete(rig1);
        ses.delete(rigCaps);
        ses.delete(rigType1);
        ses.delete(uclass1);
        ses.getTransaction().commit();
        
        assertNotNull(response);
        PermissionType p = response.getGetPermissionResponse();
        assertNotNull(p);
        
        /* User class. */
        UserClassIDType ucid = p.getUserClass();
        assertNotNull(ucid);
        assertEquals(uclass1.getId().intValue(), ucid.getUserClassID());
        assertEquals(uclass1.getName(), ucid.getUserClassName());
        
        assertEquals(ResourceClass._RIG, p.getResourceClass().toString());
        ResourceIDType rid = p.getResource();
        assertNotNull(rid);
        assertEquals(rig1.getId().intValue(), rid.getResourceID());
        assertEquals(rig1.getName(), rid.getResourceName());
        
        assertFalse(p.canQueue());
        assertTrue(p.canBook());
        
        assertEquals(uclass1.getTimeHorizon(), p.getTimeHorizon());
        assertEquals(perm1.getMaximumBookings(), p.getMaxBookings());
        assertEquals(perm1.getSessionDuration(), p.getSessionDuration());
        assertEquals(perm1.getExtensionDuration(), p.getExtensionDuration());
        assertEquals(perm1.getAllowedExtensions(), p.getAllowedExtensions());
        assertEquals(perm1.getQueueActivityTimeout(), p.getQueueActivityTmOut());
        assertEquals(perm1.getSessionActivityTimeout(), p.getSessionActivityTmOut());
        assertEquals(perm1.getDisplayName(), p.getDisplayName());
        
        assertEquals(perm1.getStartTime().getTime() / 1000, p.getStart().getTimeInMillis() / 1000);
        assertEquals(perm1.getExpiryTime().getTime() / 1000, p.getExpiry().getTimeInMillis() / 1000);
    }
    
    /**
     * Test method for {@link PermissionsSOAPImpl#getPermission(GetPermission)}.
     */
    @Test
    public void testGetPermissionCaps()
    {
        Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        UserClass uclass1 = new UserClass();
        uclass1.setName("permTestClass2");
        uclass1.setActive(true);
        uclass1.setQueuable(false);
        uclass1.setBookable(true);
        uclass1.setTimeHorizon(1000);
        ses.save(uclass1);
        RequestCapabilities reqCaps = new RequestCapabilities("not,important");
        ses.save(reqCaps);
        ResourcePermission perm1 = new ResourcePermission();
        perm1.setUserClass(uclass1);
        perm1.setType("CAPABILITY");
        perm1.setSessionDuration(3600);
        perm1.setQueueActivityTimeout(300);
        perm1.setAllowedExtensions((short)10);
        perm1.setSessionActivityTimeout(300);
        perm1.setExtensionDuration(300);
        perm1.setMaximumBookings(10);
        perm1.setRequestCapabilities(reqCaps);
        perm1.setStartTime(new Date());
        perm1.setExpiryTime(new Date());
        perm1.setDisplayName("display_name1");
        ses.save(perm1);
        ses.getTransaction().commit();
        
        GetPermission request = new GetPermission();
        PermissionIDType pid = new PermissionIDType();
        pid.setPermissionID(perm1.getId().intValue());
        request.setGetPermission(pid);
        
        GetPermissionResponse response = this.permissions.getPermission(request);
        
        ses.beginTransaction();
        ses.delete(perm1);
        ses.delete(reqCaps);
        ses.delete(uclass1);
        ses.getTransaction().commit();
        
        assertNotNull(response);
        PermissionType p = response.getGetPermissionResponse();
        assertNotNull(p);
        
        /* User class. */
        UserClassIDType ucid = p.getUserClass();
        assertNotNull(ucid);
        assertEquals(uclass1.getId().intValue(), ucid.getUserClassID());
        assertEquals(uclass1.getName(), ucid.getUserClassName());
        
        assertEquals(ResourceClass._CAPABILITY, p.getResourceClass().toString());
        ResourceIDType rid = p.getResource();
        assertNotNull(rid);
        assertEquals(reqCaps.getId().intValue(), rid.getResourceID());
        assertEquals(reqCaps.getCapabilities(), rid.getResourceName());
        
        assertFalse(p.canQueue());
        assertTrue(p.canBook());
        
        assertEquals(uclass1.getTimeHorizon(), p.getTimeHorizon());
        assertEquals(perm1.getMaximumBookings(), p.getMaxBookings());
        assertEquals(perm1.getSessionDuration(), p.getSessionDuration());
        assertEquals(perm1.getExtensionDuration(), p.getExtensionDuration());
        assertEquals(perm1.getAllowedExtensions(), p.getAllowedExtensions());
        assertEquals(perm1.getQueueActivityTimeout(), p.getQueueActivityTmOut());
        assertEquals(perm1.getSessionActivityTimeout(), p.getSessionActivityTmOut());
        assertEquals(perm1.getDisplayName(), p.getDisplayName());
        
        assertEquals(perm1.getStartTime().getTime() / 1000, p.getStart().getTimeInMillis() / 1000);
        assertEquals(perm1.getExpiryTime().getTime() / 1000, p.getExpiry().getTimeInMillis() / 1000);
    }

    /**
     * Test method for {@link PermissionsSOAPImpl#getPermissionsForUser(GetPermissionsForUser)}.
     */
    @Test
    public void testGetPermissionsForUser() throws Exception
    {
        /**********************************************************************
         ** Test setup.                                                      **
         *********************************************************************/
        Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        /* User. */
        User user = new User("test", "permUser", "USER");
        ses.save(user);
        
        /* Three classes. */
        UserClass uclass1 = new UserClass();
        uclass1.setName("permTestClass1");
        uclass1.setActive(true);
        ses.save(uclass1);
        UserClass uclass2 = new UserClass();
        uclass2.setName("permTestClass2");
        uclass2.setActive(true);
        ses.save(uclass2);
        UserClass uclass3 = new UserClass();
        uclass3.setName("permTestClass3");
        uclass3.setActive(true);
        uclass3.setQueuable(true);
        uclass3.setBookable(true);
        ses.save(uclass3);
        
        /* User is a member of two. */
        UserAssociation assoc1 = new UserAssociation(new UserAssociationId(user.getId(), uclass1.getId()), uclass1, user);
        ses.save(assoc1);
        UserAssociation assoc2 = new UserAssociation(new UserAssociationId(user.getId(), uclass2.getId()), uclass2, user);
        ses.save(assoc2);
        
        /* Resources for the permissions. */
        /* -- Three rig types. */
        RigType rigType1 = new RigType("interRigType1", 300, false);
        ses.save(rigType1);
        RigType rigType2 = new RigType("interRigType2", 300, false);
        ses.save(rigType2);
        RigType rigType3 = new RigType("batchRigType3", 300, true);
        ses.save(rigType3);
        
        /* -- Five rigs all using the same rig caps (not important). */
        RigCapabilities rigCaps = new RigCapabilities("not,important");
        ses.save(rigCaps);
        Rig rig1 = new Rig(rigType1, rigCaps, "interRig1", "http://contact", new Date(), true, null, false, true, true);
        ses.save(rig1);
        Rig rig2 = new Rig(rigType1, rigCaps, "interRig2", "http://contact", new Date(), true, null, false, true, true);
        ses.save(rig2);
        Rig rig3 = new Rig(rigType2, rigCaps, "interRig3", "http://contact", new Date(), true, null, false, true, true);
        ses.save(rig3);
        Rig rig4 = new Rig(rigType2, rigCaps, "interRig4", "http://contact", new Date(), true, null, false, true, true);
        ses.save(rig4);
        Rig rig5 = new Rig(rigType2, rigCaps, "interRig5", "http://contact", new Date(), true, null, false, true, true);
        ses.save(rig5);
        
        /* -- One request capabilties. */
        RequestCapabilities caps = new RequestCapabilities("a, b, c, d");
        ses.save(caps);
        
        /* Resource permissions. */
        ResourcePermission perm1 = new ResourcePermission();
        perm1.setUserClass(uclass1);
        perm1.setType("TYPE");
        perm1.setSessionDuration(3600);
        perm1.setQueueActivityTimeout(300);
        perm1.setAllowedExtensions((short)10);
        perm1.setSessionActivityTimeout(300);
        perm1.setExtensionDuration(300);
        perm1.setRigType(rigType1);
        perm1.setStartTime(new Date());
        perm1.setExpiryTime(new Date());
        perm1.setDisplayName("display_name1");
        ses.save(perm1);
        ResourcePermission perm2 = new ResourcePermission();
        perm2.setUserClass(uclass1);
        perm2.setType("RIG");
        perm2.setSessionDuration(3600);
        perm2.setQueueActivityTimeout(300);
        perm2.setAllowedExtensions((short)10);
        perm2.setSessionActivityTimeout(300);
        perm2.setExtensionDuration(300);
        perm2.setRig(rig1);
        perm2.setStartTime(new Date());
        perm2.setExpiryTime(new Date());
        perm2.setDisplayName("display_name2");
        ses.save(perm2);
        ResourcePermission perm3 = new ResourcePermission();
        perm3.setUserClass(uclass1);
        perm3.setType("RIG");
        perm3.setSessionDuration(3600);
        perm3.setQueueActivityTimeout(300);
        perm3.setAllowedExtensions((short)10);
        perm3.setSessionActivityTimeout(300);
        perm3.setExtensionDuration(300);
        perm3.setRig(rig2);
        perm3.setStartTime(new Date());
        perm3.setExpiryTime(new Date());
        ses.save(perm3);
        ResourcePermission perm4 = new ResourcePermission();
        perm4.setUserClass(uclass2);
        perm4.setType("TYPE");
        perm4.setSessionDuration(3600);
        perm4.setQueueActivityTimeout(300);
        perm4.setAllowedExtensions((short)10);
        perm4.setSessionActivityTimeout(300);
        perm4.setExtensionDuration(300);
        perm4.setRigType(rigType2);
        perm4.setStartTime(new Date());
        perm4.setExpiryTime(new Date());
        ses.save(perm4);
        ResourcePermission perm5 = new ResourcePermission();
        perm5.setUserClass(uclass2);
        perm5.setType("RIG");
        perm5.setSessionDuration(3600);
        perm5.setQueueActivityTimeout(300);
        perm5.setAllowedExtensions((short)10);
        perm5.setSessionActivityTimeout(300);
        perm5.setExtensionDuration(300);
        perm5.setRig(rig3);
        perm5.setStartTime(new Date());
        perm5.setExpiryTime(new Date());
        ses.save(perm5);
        ResourcePermission perm6 = new ResourcePermission();
        perm6.setUserClass(uclass2);
        perm6.setType("RIG");
        perm6.setSessionDuration(3600);
        perm6.setQueueActivityTimeout(300);
        perm6.setAllowedExtensions((short)10);
        perm6.setSessionActivityTimeout(300);
        perm6.setExtensionDuration(300);
        perm6.setRig(rig4);
        perm6.setStartTime(new Date());
        perm6.setExpiryTime(new Date());
        ses.save(perm6);
        ResourcePermission perm7 = new ResourcePermission();
        perm7.setUserClass(uclass2);
        perm7.setType("CAPABILITY");
        perm7.setSessionDuration(3600);
        perm7.setQueueActivityTimeout(300);
        perm7.setAllowedExtensions((short)10);
        perm7.setSessionActivityTimeout(300);
        perm7.setExtensionDuration(300);
        perm7.setRequestCapabilities(caps);
        perm7.setStartTime(new Date());
        perm7.setExpiryTime(new Date());
        ses.save(perm7);
        ResourcePermission perm8 = new ResourcePermission();
        perm8.setUserClass(uclass3);
        perm8.setType("RIG");
        perm8.setSessionDuration(3600);
        perm8.setQueueActivityTimeout(300);
        perm8.setAllowedExtensions((short)10);
        perm8.setSessionActivityTimeout(300);
        perm8.setExtensionDuration(300);
        perm8.setRig(rig5);
        perm8.setStartTime(new Date());
        perm8.setExpiryTime(new Date());
        ses.save(perm8);
        ResourcePermission perm9 = new ResourcePermission();
        perm9.setUserClass(uclass3);
        perm9.setType("CAPABILITY");
        perm9.setSessionDuration(3600);
        perm9.setQueueActivityTimeout(300);
        perm9.setAllowedExtensions((short)10);
        perm9.setSessionActivityTimeout(300);
        perm9.setExtensionDuration(300);
        perm9.setRequestCapabilities(caps);
        perm9.setStartTime(new Date());
        perm9.setExpiryTime(new Date());
        ses.save(perm9);
        
        /* User locks. */
        UserLock lock1 = new UserLock(user, perm1, true, "abc123");
        ses.save(lock1);
        UserLock lock2 = new UserLock(user, perm2, false, "abc123");
        ses.save(lock2);
        ses.getTransaction().commit();
        
        /**********************************************************************
         ** Actual test.                                                     **
         *********************************************************************/
        GetPermissionsForUser request = new GetPermissionsForUser();
        UserIDType uid = new UserIDType();
        request.setGetPermissionsForUser(uid);
        uid.setNameNamespace(user.getNamespace(), user.getName());
        
        GetPermissionsForUserResponse resp = this.permissions.getPermissionsForUser(request);
        assertNotNull(resp);

        /**********************************************************************
         ** Test cleanup.                                                    **
         *********************************************************************/
        ses.beginTransaction();
        ses.delete(lock2);
        ses.delete(lock1);
        ses.delete(perm1);
        ses.delete(perm2);
        ses.delete(perm3);
        ses.delete(perm4);
        ses.delete(perm5);
        ses.delete(perm6);
        ses.delete(perm7);
        ses.delete(perm8);
        ses.delete(perm9);
        ses.delete(caps);
        ses.delete(rig5);
        ses.delete(rig4);
        ses.delete(rig3);
        ses.delete(rig2);
        ses.delete(rig1);
        ses.delete(rigCaps);
        ses.delete(rigType3);
        ses.delete(rigType2);
        ses.delete(rigType1);
        ses.delete(assoc2);
        ses.delete(assoc1);
        ses.delete(uclass3);
        ses.delete(uclass2);
        ses.delete(uclass1);
        ses.delete(user);
        ses.getTransaction().commit();
        
        PermissionWithLockListType permListType = resp.getGetPermissionsForUserResponse();
        assertNotNull(permListType);
        
        PermissionWithLockType permList[] = permListType.getPermission();
        assertNotNull(permList);
        assertEquals(7, permList.length);
        
        for (PermissionWithLockType lock : permList)
        {
            assertNotNull(lock);
            PermissionType pt = lock.getPermission();
            assertNotNull(pt);
            
            assertEquals(3600, pt.getSessionDuration());
            assertEquals(300, pt.getExtensionDuration());
            assertEquals(10, pt.getAllowedExtensions());
            assertEquals(300, pt.getQueueActivityTmOut());
            assertEquals(300, pt.getSessionActivityTmOut());
            assertNotNull(pt.getStart());
            assertNotNull(pt.getExpiry());
            
            ResourceIDType res = pt.getResource();
            assertNotNull(res);
            assertTrue(res.getResourceID() > 0);
            assertNotNull(res.getResourceName());
        }
        
        OMElement ele = resp.getOMElement(GetPermissionsForUser.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }
    
    /**
     * Test method for {@link PermissionsSOAPImpl#getPermissionsForUser(GetPermissionsForUser)}.
     */
    @Test
    public void testGetPermissionsForUserLocked()
    {
        /**********************************************************************
         ** Test setup.                                                      **
         *********************************************************************/
        Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        User user = new User("test", "permUser", "USER");
        ses.save(user);
        UserClass uclass1 = new UserClass();
        uclass1.setName("permTestClass1");
        uclass1.setActive(true);
        uclass1.setQueuable(false);
        uclass1.setBookable(true);
        uclass1.setTimeHorizon(1000);
        ses.save(uclass1);
        UserAssociation assoc1 = new UserAssociation(new UserAssociationId(user.getId(), uclass1.getId()), uclass1, user);
        ses.save(assoc1);
        RigType rigType1 = new RigType("interRigType1", 300, false);
        ses.save(rigType1);
        RigCapabilities rigCaps = new RigCapabilities("not,important");
        ses.save(rigCaps);
        Rig rig1 = new Rig(rigType1, rigCaps, "interRig1", "http://contact", new Date(), true, null, false, true, true);
        ses.save(rig1);
        ResourcePermission perm1 = new ResourcePermission();
        perm1.setUserClass(uclass1);
        perm1.setType("TYPE");
        perm1.setSessionDuration(3600);
        perm1.setQueueActivityTimeout(300);
        perm1.setAllowedExtensions((short)10);
        perm1.setSessionActivityTimeout(300);
        perm1.setExtensionDuration(300);
        perm1.setMaximumBookings(10);
        perm1.setRigType(rigType1);
        perm1.setStartTime(new Date());
        perm1.setExpiryTime(new Date());
        perm1.setDisplayName("display_name1");
        ses.save(perm1);
        UserLock lock1 = new UserLock(user, perm1, true, "abc123");
        ses.save(lock1);
        ses.getTransaction().commit();
        
        /**********************************************************************
         ** Actual test.                                                     **
         *********************************************************************/
        GetPermissionsForUser request = new GetPermissionsForUser();
        UserIDType uid = new UserIDType();
        request.setGetPermissionsForUser(uid);
        uid.setNameNamespace(user.getNamespace(), user.getName());
        
        GetPermissionsForUserResponse resp = this.permissions.getPermissionsForUser(request);
        assertNotNull(resp);

        /**********************************************************************
         ** Test cleanup.                                                    **
         *********************************************************************/
        ses.beginTransaction();
        ses.delete(lock1);
        ses.delete(perm1);
        ses.delete(rig1);
        ses.delete(rigCaps);
        ses.delete(rigType1);
        ses.delete(assoc1);
        ses.delete(uclass1);
        ses.delete(user);
        ses.getTransaction().commit();
        
        PermissionWithLockListType permListType = resp.getGetPermissionsForUserResponse();
        assertNotNull(permListType);
        
        PermissionWithLockType permList[] = permListType.getPermission();
        assertNotNull(permList);
        assertEquals(1, permList.length);
        
        PermissionWithLockType perm = permList[0];
        assertNotNull(perm);
        assertTrue(perm.getIsLocked());
        
        PermissionType p = perm.getPermission();
        assertNotNull(p);
        assertTrue(p.canBook());
        assertFalse(p.canQueue());
        assertEquals(perm1.getAllowedExtensions(), p.getAllowedExtensions());
        assertEquals(perm1.getExtensionDuration(), p.getExtensionDuration());
        assertEquals(perm1.getQueueActivityTimeout(), p.getQueueActivityTmOut());
        assertEquals(perm1.getSessionActivityTimeout(), p.getSessionActivityTmOut());
        assertEquals(perm1.getSessionDuration(), p.getSessionDuration());
        assertEquals(perm1.getMaximumBookings(), p.getMaxBookings());
        assertEquals(uclass1.getTimeHorizon(), p.getTimeHorizon());
        assertEquals("TYPE", p.getResourceClass().getValue());
        assertEquals(perm1.getDisplayName(), p.getDisplayName());
        
        ResourceIDType res = p.getResource();
        assertNotNull(res);
        assertEquals(res.getResourceName(), "interRigType1");
    }
    
    /**
     * Test method for {@link PermissionsSOAPImpl#getPermissionsForUser(GetPermissionsForUser)}.
     */
    @Test
    public void testGetPermissionsForUserNotLocked()
    {
        /**********************************************************************
         ** Test setup.                                                      **
         *********************************************************************/
        Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        User user = new User("test", "permUser", "USER");
        ses.save(user);
        UserClass uclass1 = new UserClass();
        uclass1.setName("permTestClass1");
        uclass1.setActive(true);
        uclass1.setBookable(false);
        uclass1.setQueuable(true);
        ses.save(uclass1);
        UserAssociation assoc1 = new UserAssociation(new UserAssociationId(user.getId(), uclass1.getId()), uclass1, user);
        ses.save(assoc1);
        RigType rigType1 = new RigType("interRigType1", 300, false);
        ses.save(rigType1);
        RigCapabilities rigCaps = new RigCapabilities("not,important");
        ses.save(rigCaps);
        Rig rig1 = new Rig(rigType1, rigCaps, "interRig1", "http://contact", new Date(), true, null, false, true, true);
        ses.save(rig1);
        ResourcePermission perm1 = new ResourcePermission();
        perm1.setUserClass(uclass1);
        perm1.setType("RIG");
        perm1.setSessionDuration(3600);
        perm1.setQueueActivityTimeout(300);
        perm1.setAllowedExtensions((short)10);
        perm1.setSessionActivityTimeout(300);
        perm1.setExtensionDuration(300);
        perm1.setRig(rig1);
        perm1.setStartTime(new Date());
        perm1.setExpiryTime(new Date());
        ses.save(perm1);
        UserLock lock1 = new UserLock(user, perm1, false, "abc123");
        ses.save(lock1);
        ses.getTransaction().commit();
        
        /**********************************************************************
         ** Actual test.                                                     **
         *********************************************************************/
        GetPermissionsForUser request = new GetPermissionsForUser();
        UserIDType uid = new UserIDType();
        request.setGetPermissionsForUser(uid);
        uid.setNameNamespace(user.getNamespace(), user.getName());
        
        GetPermissionsForUserResponse resp = this.permissions.getPermissionsForUser(request);
        assertNotNull(resp);

        /**********************************************************************
         ** Test cleanup.                                                    **
         *********************************************************************/
        ses.beginTransaction();
        ses.delete(lock1);
        ses.delete(perm1);
        ses.delete(rig1);
        ses.delete(rigCaps);
        ses.delete(rigType1);
        ses.delete(assoc1);
        ses.delete(uclass1);
        ses.delete(user);
        ses.getTransaction().commit();
        
        PermissionWithLockListType permListType = resp.getGetPermissionsForUserResponse();
        assertNotNull(permListType);
        
        PermissionWithLockType permList[] = permListType.getPermission();
        assertNotNull(permList);
        assertEquals(1, permList.length);
        
        PermissionWithLockType perm = permList[0];
        assertNotNull(perm);
        assertFalse(perm.getIsLocked());
        
        PermissionType p = perm.getPermission();
        assertNotNull(p);
        assertFalse(p.canBook());
        assertTrue(p.canQueue());
        assertEquals(perm1.getAllowedExtensions(), p.getAllowedExtensions());
        assertEquals(perm1.getExtensionDuration(), p.getExtensionDuration());
        assertEquals(perm1.getQueueActivityTimeout(), p.getQueueActivityTmOut());
        assertEquals(perm1.getSessionActivityTimeout(), p.getSessionActivityTmOut());
        assertEquals(perm1.getSessionDuration(), p.getSessionDuration());
        assertEquals("RIG", p.getResourceClass().getValue());
        
        ResourceIDType res = p.getResource();
        assertNotNull(res);
        assertEquals(res.getResourceName(), "interRig1");
    }
    
    /**
     * Test method for {@link PermissionsSOAPImpl#getPermissionsForUser(GetPermissionsForUser)}.
     */
    @Test
    public void testGetPermissionsForUserNoPerms() throws Exception
    {
        User user = new User("test", "TESTNS", "USER");
        UserDao dao = new UserDao();
        dao.persist(user);
        
        GetPermissionsForUser request = new GetPermissionsForUser();
        UserIDType uid = new UserIDType();
        request.setGetPermissionsForUser(uid);
        uid.setUserID(String.valueOf(user.getId()));
        
        GetPermissionsForUserResponse resp = this.permissions.getPermissionsForUser(request);
        assertNotNull(resp);
        
        dao.delete(user);
        dao.closeSession();
        
        PermissionWithLockListType lockList = resp.getGetPermissionsForUserResponse();
        assertNotNull(lockList);
        assertNull(lockList.getPermission());
        
        OMElement ele = resp.getOMElement(GetPermissionsForUserResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }
    
    /**
     * Test method for {@link PermissionsSOAPImpl#getPermissionsForUser(GetPermissionsForUser)}.
     */
    @Test
    public void testGetPermissionsForUserNoUser() throws Exception
    {      
        GetPermissionsForUser request = new GetPermissionsForUser();
        UserIDType uid = new UserIDType();
        request.setGetPermissionsForUser(uid);
        uid.setUserQName("FOO:BAR");
        
        GetPermissionsForUserResponse resp = this.permissions.getPermissionsForUser(request);
        assertNotNull(resp);

        PermissionWithLockListType lockList = resp.getGetPermissionsForUserResponse();
        assertNotNull(lockList);
        assertNull(lockList.getPermission());
        
        OMElement ele = resp.getOMElement(GetPermissionsForUserResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }


    /**
     * Test method for {@link PermissionsSOAPImpl#getPermissionsForUserClass(GetPermissionsForUserClass)}.
     */
    @Test
    public void testGetPermissionsForUserClass()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link PermissionsSOAPImpl#getUser(GetUser)}.
     */
    @Test
    public void testGetUser() throws Exception
    {
        User user = new User();
        user.setNamespace("TESTNS");
        user.setName("mdiponio");
        user.setPersona("ADMIN");
        UserDao dao = new UserDao();
        dao.persist(user);
        
        GetUser req = new GetUser();
        UserIDType uid = new UserIDType();
        req.setGetUser(uid);
        uid.setUserID(String.valueOf(user.getId()));
        
        GetUserResponse resp = this.permissions.getUser(req);
        assertNotNull(resp);
        
        UserType type = resp.getGetUserResponse();
        assertNotNull(type);
        
        assertEquals((long)user.getId(), Long.parseLong(type.getUserID()));
        assertEquals(user.getName(), type.getUserName());
        assertEquals(user.getNamespace(), type.getUserNamespace());
        
        
        String qName[] = type.getUserQName().split(UserIDType.QNAME_DELIM);
        assertEquals(user.getNamespace(), qName[0]);
        assertEquals(user.getName(), qName[1]);
        
        UserNameNamespaceSequence seq = type.getUserNameNamespaceSequence();
        assertEquals(user.getName(), seq.getUserName());
        assertEquals(user.getNamespace(), seq.getUserNamespace());
        
        assertEquals(user.getPersona(), type.getPersona().getValue());        
        
        dao.delete(user);
        dao.closeSession();
        
        OMElement ele = resp.getOMElement(GetUserResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("<persona>ADMIN</persona>"));
    }
    
    /**
     * Test method for {@link PermissionsSOAPImpl#getUser(GetUser)}.
     */
    @Test
    public void testGetUserQName() throws Exception
    {
        User user = new User();
        user.setNamespace("TESTNS");
        user.setName("mdiponio");
        user.setPersona("ADMIN");
        UserDao dao = new UserDao();
        dao.persist(user);
        
        GetUser req = new GetUser();
        UserIDType uid = new UserIDType();
        req.setGetUser(uid);
        uid.setUserQName(user.getNamespace() + UserIDType.QNAME_DELIM + user.getName());
        
        GetUserResponse resp = this.permissions.getUser(req);
        assertNotNull(resp);
        
        UserType type = resp.getGetUserResponse();
        assertNotNull(type);
        
        assertEquals((long)user.getId(), Long.parseLong(type.getUserID()));
        assertEquals(user.getName(), type.getUserName());
        assertEquals(user.getNamespace(), type.getUserNamespace());
        
        
        String qName[] = type.getUserQName().split(UserIDType.QNAME_DELIM);
        assertEquals(user.getNamespace(), qName[0]);
        assertEquals(user.getName(), qName[1]);
        
        UserNameNamespaceSequence seq = type.getUserNameNamespaceSequence();
        assertEquals(user.getName(), seq.getUserName());
        assertEquals(user.getNamespace(), seq.getUserNamespace());
        
        assertEquals(user.getPersona(), type.getPersona().getValue());        
        
        dao.delete(user);
        dao.closeSession();
        
        OMElement ele = resp.getOMElement(GetUserResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("<persona>ADMIN</persona>"));
    }
    
    /**
     * Test method for {@link PermissionsSOAPImpl#getUser(GetUser)}.
     */
    @Test
    public void testGetUserSeq() throws Exception
    {
        User user = new User();
        user.setNamespace("TESTNS");
        user.setName("mdiponio");
        user.setPersona("ADMIN");
        UserDao dao = new UserDao();
        dao.persist(user);
        
        GetUser req = new GetUser();
        UserIDType uid = new UserIDType();
        req.setGetUser(uid);
        uid.setNameNamespace(user.getNamespace(), user.getName());
        
        GetUserResponse resp = this.permissions.getUser(req);
        assertNotNull(resp);
        
        UserType type = resp.getGetUserResponse();
        assertNotNull(type);
        
        assertEquals((long)user.getId(), Long.parseLong(type.getUserID()));
        assertEquals(user.getName(), type.getUserName());
        assertEquals(user.getNamespace(), type.getUserNamespace());
        
        
        String qName[] = type.getUserQName().split(UserIDType.QNAME_DELIM);
        assertEquals(user.getNamespace(), qName[0]);
        assertEquals(user.getName(), qName[1]);
        
        UserNameNamespaceSequence seq = type.getUserNameNamespaceSequence();
        assertEquals(user.getName(), seq.getUserName());
        assertEquals(user.getNamespace(), seq.getUserNamespace());
        
        assertEquals(user.getPersona(), type.getPersona().getValue());        
        
        dao.delete(user);
        dao.closeSession();
        
        OMElement ele = resp.getOMElement(GetUserResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("<persona>ADMIN</persona>"));
    }

    /**
     * Test method for {@link PermissionsSOAPImpl#getUserClass(GetUserClass)}.
     */
    @Test
    public void testGetUserClass() throws Exception
    {
        UserClassDao dao = new UserClassDao();
        UserClass cls = new UserClass();
        cls.setName("ucTest");
        cls.setPriority((short)50);
        cls.setTimeHorizon(10000);
        cls.setActive(true);
        cls.setQueuable(true);
        cls.setBookable(true);
        cls.setUsersLockable(false);
        cls.setKickable(true);
        dao.persist(cls);
        
        GetUserClass req = new GetUserClass();
        UserClassIDType ucId = new UserClassIDType();
        req.setGetUserClass(ucId);
        ucId.setUserClassID(cls.getId().intValue());
        
        GetUserClassResponse resp = this.permissions.getUserClass(req);
        assertNotNull(resp);
        
        UserClassType uc = resp.getGetUserClassResponse();
        assertNotNull(uc);
        
        assertEquals(cls.getId().intValue(), uc.getUserClassID());
        assertEquals(cls.getName(), uc.getUserClassName());
        assertEquals(cls.isActive(), uc.getIsActive());
        assertEquals(cls.isKickable(), uc.getIsKickable());
        assertEquals(cls.isQueuable(), uc.getIsQueuable());
        assertEquals(cls.isBookable(), uc.getIsBookable());
        assertEquals(cls.isUsersLockable(), uc.getIsUserLockable());
        assertEquals(cls.getTimeHorizon(), uc.getTimeHorizon());
        
        dao.delete(cls);
        dao.closeSession();
        
        OMElement ele = resp.getOMElement(GetUserClassResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }
    
    /**
     * Test method for {@link PermissionsSOAPImpl#getUserClass(GetUserClass)}.
     */
    @Test
    public void testGetUserClassName() throws Exception
    {
        UserClassDao dao = new UserClassDao();
        UserClass cls = new UserClass();
        cls.setName("ucTest");
        cls.setPriority((short)50);
        cls.setTimeHorizon(100);
        cls.setActive(true);
        cls.setQueuable(false);
        cls.setBookable(true);
        cls.setUsersLockable(false);
        cls.setKickable(true);
        dao.persist(cls);
        dao.persist(cls);
        
        GetUserClass req = new GetUserClass();
        UserClassIDType ucId = new UserClassIDType();
        req.setGetUserClass(ucId);
        ucId.setUserClassName("ucTest");
        
        GetUserClassResponse resp = this.permissions.getUserClass(req);
        assertNotNull(resp);
        
        UserClassType uc = resp.getGetUserClassResponse();
        assertNotNull(uc);
        
        assertEquals(cls.getId().intValue(), uc.getUserClassID());
        assertEquals(cls.getName(), uc.getUserClassName());
        assertEquals(cls.isBookable(), uc.getIsBookable());
        assertEquals(cls.isActive(), uc.getIsActive());
        assertEquals(cls.isKickable(), uc.getIsKickable());
        assertEquals(cls.isQueuable(), uc.getIsQueuable());
        assertEquals(cls.isUsersLockable(), uc.getIsUserLockable());
        assertEquals(cls.getTimeHorizon(), uc.getTimeHorizon());
        
        dao.delete(cls);
        dao.closeSession();
        
        OMElement ele = resp.getOMElement(GetUserClassResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }

    /**
     * Test method for {@link PermissionsSOAPImpl#getUserClasses(GetUserClasses)}.
     */
    @Test
    public void testGetUserClasses()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link PermissionsSOAPImpl#getUserClassesForUser(GetUserClassesForUser)}.
     */
    @Test
    public void testGetUserClassesForUser()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link PermissionsSOAPImpl#getUsersInUserClass(GetUsersInUserClass)}.
     */
    @Test
    public void testGetUsersInUserClass()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link PermissionsSOAPImpl#unlockUserLock(UnlockUserLock)}.
     */
    @Test
    public void testUnlockUserLockID()
    {
        Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        User user = new User("locktest", "ns", "USER");
        ses.persist(user);
        UserClass userClass= new UserClass();
        userClass.setName("uc");
        ses.persist(userClass);
        ResourcePermission perm = new ResourcePermission();         
        perm.setStartTime(new Date());
        perm.setExpiryTime(new Date());
        perm.setUserClass(userClass);
        perm.setType("RIG");
        ses.persist(perm);
        UserLock lock = new UserLock(user, perm, true, "abc123");
        ses.persist(lock);
        ses.getTransaction().commit();
        
        /* Request. */
        UnlockUserLock request = new UnlockUserLock();
        UserLockType lockReq = new UserLockType();
        request.setUnlockUserLock(lockReq);
        lockReq.setRequestorQName("TESTNS:mdiponio");
        lockReq.setLockKey("abc123");
        lockReq.setUserLockID(lock.getId().intValue());
        
        UnlockUserLockResponse resp = this.permissions.unlockUserLock(request);
        assertNotNull(resp);
        OperationResponseType op = resp.getUnlockUserLockResponse();
        assertNotNull(op);
        assertTrue(op.getSuccessful());
        assertEquals(0, op.getFailureCode());
        assertNull(op.getFailureReason());
        
        ses.refresh(lock);
        assertFalse(lock.isIsLocked());
        
        ses.beginTransaction();
        ses.delete(lock);
        ses.delete(perm);
        ses.delete(userClass);
        ses.delete(user);
        ses.getTransaction().commit();
        ses.close();
    }
    
    /**
     * Test method for {@link PermissionsSOAPImpl#unlockUserLock(UnlockUserLock)}.
     */
    @Test
    public void testUnlockUserLockIDWrongKey()
    {
        Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        User user = new User("locktest", "ns", "USER");
        ses.persist(user);
        UserClass userClass= new UserClass();
        userClass.setName("uc");
        ses.persist(userClass);
        ResourcePermission perm = new ResourcePermission();         
        perm.setStartTime(new Date());
        perm.setExpiryTime(new Date());
        perm.setUserClass(userClass);
        perm.setType("RIG");
        ses.persist(perm);
        UserLock lock = new UserLock(user, perm, true, "abc123");
        ses.persist(lock);
        ses.getTransaction().commit();
        
        /* Request. */
        UnlockUserLock request = new UnlockUserLock();
        UserLockType lockReq = new UserLockType();
        request.setUnlockUserLock(lockReq);
        lockReq.setRequestorQName("TESTNS:mdiponio");
        lockReq.setLockKey("wrongkey");
        lockReq.setUserLockID(lock.getId().intValue());
        
        UnlockUserLockResponse resp = this.permissions.unlockUserLock(request);
        assertNotNull(resp);
        OperationResponseType op = resp.getUnlockUserLockResponse();
        assertNotNull(op);
        assertFalse(op.getSuccessful());
        assertEquals(3, op.getFailureCode());
        assertNotNull(op.getFailureReason());
        
        ses.refresh(lock);
        assertTrue(lock.isIsLocked());
        
        ses.beginTransaction();
        ses.delete(lock);
        ses.delete(perm);
        ses.delete(userClass);
        ses.delete(user);
        ses.getTransaction().commit();
        ses.close();
    }
    
    /**
     * Test method for {@link PermissionsSOAPImpl#unlockUserLock(UnlockUserLock)}.
     */
    @Test
    public void testUnlockUserLockPermUser()
    {
        Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        User user = new User("locktest", "ns", "USER");
        ses.persist(user);
        UserClass userClass= new UserClass();
        userClass.setName("uc");
        ses.persist(userClass);
        ResourcePermission perm = new ResourcePermission();         
        perm.setStartTime(new Date());
        perm.setExpiryTime(new Date());
        perm.setUserClass(userClass);
        perm.setType("RIG");
        ses.persist(perm);
        UserLock lock = new UserLock(user, perm, true, "abc123");
        ses.persist(lock);
        ses.getTransaction().commit();
        
        /* Request. */
        UnlockUserLock request = new UnlockUserLock();
        UserLockType lockReq = new UserLockType();
        request.setUnlockUserLock(lockReq);
        lockReq.setRequestorQName("TESTNS:mdiponio");
        lockReq.setLockKey("abc123");
        UserLockIDUserPermSequence seq = new UserLockIDUserPermSequence();
        PermissionIDType pId = new PermissionIDType();
        pId.setPermissionID(perm.getId().intValue());
        seq.setPermissionID(pId);
        UserIDType uId = new UserIDType();
        uId.setUserQName(user.getNamespace() + ":" + user.getName());
        seq.setUserID(uId);
        lockReq.setUserIDPermissionsTracker(seq);
        
        UnlockUserLockResponse resp = this.permissions.unlockUserLock(request);
        assertNotNull(resp);
        OperationResponseType op = resp.getUnlockUserLockResponse();
        assertNotNull(op);
        assertTrue(op.getSuccessful());
        assertEquals(0, op.getFailureCode());
        assertNull(op.getFailureReason());
        
        ses.refresh(lock);
        assertFalse(lock.isIsLocked());
        
        ses.beginTransaction();
        ses.delete(lock);
        ses.delete(perm);
        ses.delete(userClass);
        ses.delete(user);
        ses.getTransaction().commit();
        ses.close();
    }
    
    /**
     * Test method for {@link PermissionsSOAPImpl#unlockUserLock(UnlockUserLock)}.
     */
    @Test
    public void testUnlockUserLockPermUserWrongKey()
    {
        Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        User user = new User("locktest", "ns", "USER");
        ses.persist(user);
        UserClass userClass= new UserClass();
        userClass.setName("uc");
        ses.persist(userClass);
        ResourcePermission perm = new ResourcePermission();         
        perm.setStartTime(new Date());
        perm.setExpiryTime(new Date());
        perm.setUserClass(userClass);
        perm.setType("RIG");
        ses.persist(perm);
        UserLock lock = new UserLock(user, perm, true, "abc123");
        ses.persist(lock);
        ses.getTransaction().commit();
        
        /* Request. */
        UnlockUserLock request = new UnlockUserLock();
        UserLockType lockReq = new UserLockType();
        request.setUnlockUserLock(lockReq);
        lockReq.setRequestorQName("TESTNS:mdiponio");
        lockReq.setLockKey("wrongkey");
        UserLockIDUserPermSequence seq = new UserLockIDUserPermSequence();
        PermissionIDType pId = new PermissionIDType();
        pId.setPermissionID(perm.getId().intValue());
        seq.setPermissionID(pId);
        UserIDType uId = new UserIDType();
        uId.setUserQName(user.getNamespace() + ":" + user.getName());
        seq.setUserID(uId);
        lockReq.setUserIDPermissionsTracker(seq);
        
        UnlockUserLockResponse resp = this.permissions.unlockUserLock(request);
        assertNotNull(resp);
        OperationResponseType op = resp.getUnlockUserLockResponse();
        assertNotNull(op);
        assertFalse(op.getSuccessful());
        assertEquals(3, op.getFailureCode());
        assertNotNull(op.getFailureReason());
        
        ses.refresh(lock);
        assertTrue(lock.isIsLocked());
        
        ses.beginTransaction();
        ses.delete(lock);
        ses.delete(perm);
        ses.delete(userClass);
        ses.delete(user);
        ses.getTransaction().commit();
        ses.close();
    }
    
    /**
     * Test method for {@link PermissionsSOAPImpl#unlockUserLock(UnlockUserLock)}.
     */
    @Test
    public void testUnlockUserLockNoKey()
    {
        /* Request. */
        UnlockUserLock request = new UnlockUserLock();
        UserLockType lockReq = new UserLockType();
        request.setUnlockUserLock(lockReq);
        lockReq.setRequestorQName("TESTNS:mdiponio");
        lockReq.setUserLockID(10);
        
        
        UnlockUserLockResponse resp = this.permissions.unlockUserLock(request);
        assertNotNull(resp);
        OperationResponseType op = resp.getUnlockUserLockResponse();
        assertNotNull(op);
        assertFalse(op.getSuccessful());
        assertEquals(2, op.getFailureCode());
        assertNotNull(op.getFailureReason());
    }
    
    @Test
    public void testGetUserFromUserID() throws Exception
    {
        Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        User user = new User("UserIdTest", "Permission", "USER");
        ses.persist(user);
        ses.getTransaction().commit();
        
        UserIDType uid = new UserIDType();
        uid.setUserID(String.valueOf(user.getId()));
        
        Method meth = PermissionsSOAPImpl.class.getDeclaredMethod("getUserFromUserID", UserIDType.class, Session.class);
        meth.setAccessible(true);
        User loaded = (User)meth.invoke(this.permissions, uid, ses);
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
        Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        User user = new User("UserIdTest", "Permission", "USER");
        ses.persist(user);
        ses.getTransaction().commit();
        
        UserIDType uid = new UserIDType();
        UserNameNamespaceSequence seq = new UserNameNamespaceSequence();
        seq.setUserNamespace(user.getNamespace());
        seq.setUserName(user.getName());
        uid.setUserNameNamespaceSequence(seq);
        
        Method meth = PermissionsSOAPImpl.class.getDeclaredMethod("getUserFromUserID", UserIDType.class, Session.class);
        meth.setAccessible(true);
        User loaded = (User)meth.invoke(this.permissions, uid, ses);
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
        Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        User user = new User("UserIdTest", "Permission", "USER");
        ses.persist(user);
        ses.getTransaction().commit();
        
        UserIDType uid = new UserIDType();
        uid.setUserQName(user.getNamespace() + ":" + user.getName());
        
        Method meth = PermissionsSOAPImpl.class.getDeclaredMethod("getUserFromUserID", UserIDType.class, Session.class);
        meth.setAccessible(true);
        User loaded = (User)meth.invoke(this.permissions, uid, ses);
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
        uid.setUserQName("PERM_TEST:does_not_exist");
        Session ses = DataAccessActivator.getNewSession();
        
        Method meth = PermissionsSOAPImpl.class.getDeclaredMethod("getUserFromUserID", UserIDType.class, Session.class);
        meth.setAccessible(true);
        assertNull(meth.invoke(this.permissions, uid, ses));
        
        ses.close();
    }
}
