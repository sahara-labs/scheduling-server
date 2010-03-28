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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Properties;

import junit.framework.TestCase;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.hibernate.Session;
import org.hibernate.cfg.AnnotationConfiguration;
import org.junit.Before;
import org.junit.Test;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserAssociationDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserClassDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.AcademicPermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Config;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.MatchingCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.MatchingCapabilitiesId;
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
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.logger.impl.SystemErrLogger;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddAcademicPermission;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddPermission;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUser;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserAssociation;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserAssociationResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserClass;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserClassResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserLock;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.BulkAddUserClassUsers;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteAcademicPermission;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeletePermission;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUser;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserAssociation;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserAssociationResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserClass;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserClassResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserLock;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditAcademicPermission;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditPermission;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUser;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUserClass;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUserClassResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUserResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermission;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForAcademic;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForUserClass;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermission;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUser;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserClass;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUser;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClass;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClasses;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassesForUser;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUsersInUserClass;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.OperationResponseType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.PermissionIDType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.PermissionType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.PermissionWithLockListType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.PermissionWithLockType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.PersonaType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.ResourceIDType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UnlockUserLock;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UnlockUserLockResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserAssociationType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserClassIDType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserClassType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserIDType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserLockIDUserPermSequence;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserLockType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserNameNamespaceSequence;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserType;

/**
 * Tests the {@link Permissions} class.
 */
public class PermissionsTester extends TestCase
{
    /** Object of class under test. */
    private Permissions permissions;
    
    public PermissionsTester(String name) throws Exception
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
    public void setUp() throws Exception
    {
        this.permissions = new Permissions();
    }

    /**
     * Test method for {@link Permissions#addAcademicPermission(AddAcademicPermission)}.
     */
    @Test
    public void testAddAcademicPermission()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link Permissions#addPermission(AddPermission)}.
     */
    @Test
    public void testAddPermission()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link Permissions#addUser(AddUser)}.
     */
    @Test
    public void testAddUser() throws Exception
    {
        AddUser req = new AddUser();
        UserType user = new UserType();
        req.setAddUser(user);
        user.setRequestorQName("TESTNS:mdiponio");
        UserNameNamespaceSequence nsname = new UserNameNamespaceSequence();
        nsname.setUserNamespace("TESTNS");
        nsname.setUserName("tmachet");
        user.setUserNameNamespaceSequence(nsname);
        user.setPersona(PersonaType.ADMIN);
        
        AddUserResponse resp = this.permissions.addUser(req);
        assertNotNull(resp);
        
        OperationResponseType op = resp.getAddUserResponse();
        assertNotNull(op);
        assertTrue(op.getSuccessful());
        
        UserDao dao = new UserDao();
        User us = dao.findByName("TESTNS", "tmachet");
        assertNotNull(us);
        dao.delete(us);
        dao.closeSession();
        
        OMElement ele = resp.getOMElement(AddUserResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("<successful>true</successful>"));
    }

    /**
     * Test method for {@link Permissions#editUser(EditUser)}.
     */
    @Test
    public void testEditUser() throws Exception
    {
        User user = new User();
        user.setNamespace("TESTNS");
        user.setName("tmachet");
        user.setPersona("USER");
        UserDao dao = new UserDao();
        dao.persist(user);
        
        EditUser req = new EditUser();
        UserType ty = new UserType();
        req.setEditUser(ty);
        ty.setRequestorQName("TESTNS:mdiponio");
        ty.setUserID(String.valueOf(user.getId()));
        UserNameNamespaceSequence seq = new UserNameNamespaceSequence();
        seq.setUserNamespace("TESTNS-ENG");
        seq.setUserName("tmachet");
        ty.setUserNameNamespaceSequence(seq);
        ty.setPersona(PersonaType.ADMIN);
        
        EditUserResponse resp = this.permissions.editUser(req);
        assertNotNull(resp);
        
        OperationResponseType op = resp.getEditUserResponse();
        assertNotNull(op);
        assertTrue(op.getSuccessful());
        assertNull(op.getFailureReason());
        assertEquals(0, op.getFailureCode());
        
        dao.refresh(user);
        assertEquals("TESTNS-ENG", user.getNamespace());
        assertEquals("tmachet", user.getName());
        assertEquals("ADMIN", user.getPersona());
        
        dao.delete(user);
        
        OMElement ele = resp.getOMElement(EditUserResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("<successful>true</successful>"));
    }
    
    /**
     * Test method for {@link Permissions#deleteUser(DeleteUser)}.
     */
    @Test
    public void testDeleteUser() throws Exception
    {
        UserDao dao = new UserDao();
        User user = new User();
        user.setPersona("USER");
        user.setName("tmachet");
        user.setNamespace("TESTNS");
        dao.persist(user);
        
        DeleteUser req = new DeleteUser();
        UserIDType usTy = new UserIDType();
        req.setDeleteUser(usTy);
        usTy.setRequestorQName("TESTNS:mdiponio");
        UserNameNamespaceSequence seq = new UserNameNamespaceSequence();
        seq.setUserNamespace("TESTNS");
        seq.setUserName("tmachet");
        usTy.setUserNameNamespaceSequence(seq);
        
        DeleteUserResponse resp = this.permissions.deleteUser(req);
        assertNotNull(resp);
        
        OperationResponseType op = resp.getDeleteUserResponse();
        assertNotNull(op);
        assertTrue(op.getSuccessful());
        
        assertNull(dao.findByName("TESTNS", "tmachet"));
        
        OMElement ele = resp.getOMElement(DeleteUserResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("<successful>true</successful>"));
    }
    
    /**
     * Test method for {@link Permissions#deleteUser(DeleteUser)}.
     */
    @Test
    public void testDeleteUserID() throws Exception
    {
        UserDao dao = new UserDao();
        User user = new User();
        user.setPersona("USER");
        user.setName("tmachet");
        user.setNamespace("TESTNS");
        dao.persist(user);
        
        DeleteUser req = new DeleteUser();
        UserIDType usTy = new UserIDType();
        req.setDeleteUser(usTy);
        usTy.setRequestorQName("TESTNS:mdiponio");
        usTy.setUserID(String.valueOf(user.getId()));
        
        DeleteUserResponse resp = this.permissions.deleteUser(req);
        assertNotNull(resp);
        
        OperationResponseType op = resp.getDeleteUserResponse();
        assertNotNull(op);
        assertTrue(op.getSuccessful());
        
        assertNull(dao.findByName("TESTNS", "tmachet"));
        
        OMElement ele = resp.getOMElement(DeleteUserResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("<successful>true</successful>"));
    }

    /**
     * Test method for {@link Permissions#addUserAssociation(AddUserAssociation)}.
     */
    @Test
    public void testAddUserAssociation() throws Exception
    {
        UserDao uDao = new UserDao();
        User user = new User("tuser", "ns1", "ADMIN");
        uDao.persist(user);
        UserClassDao cDao = new UserClassDao(uDao.getSession());
        UserClass ucls = new UserClass();
        ucls.setName("usClass");
        cDao.persist(ucls);
        UserAssociationDao aDao = new UserAssociationDao(uDao.getSession());
        
        AddUserAssociation req = new AddUserAssociation();
        UserAssociationType assocType = new UserAssociationType();
        req.setAddUserAssociation(assocType);
        assocType.setRequestorQName("TESTNS:mdiponio");
        UserIDType uid = new UserIDType();
        uid.setUserQName("ns1:tuser");
        assocType.setUser(uid);
        UserClassIDType cid = new UserClassIDType();
        cid.setUserClassName("usClass");
        assocType.setUserClass(cid);
        
        AddUserAssociationResponse resp = this.permissions.addUserAssociation(req);
        assertNotNull(resp);
        
        OperationResponseType op = resp.getAddUserAssociationResponse();
        assertNotNull(op);
        assertTrue(op.getSuccessful());
        assertEquals(0, op.getFailureCode());
        assertNull(op.getFailureReason());
        
        UserAssociation ua = aDao.get(new UserAssociationId(user.getId(), ucls.getId()));
        assertNotNull(ua);
        assertEquals(user.getId(), ua.getUser().getId());
        assertEquals(ucls.getId(), ua.getUserClass().getId());
        aDao.delete(ua);
        uDao.delete(user);
        cDao.delete(ucls);
        cDao.closeSession();
        
        OMElement ele = resp.getOMElement(AddUserAssociationResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("<successful>true</successful>"));
    }
    
    /**
     * Test method for {@link Permissions#addUserAssociation(AddUserAssociation)}.
     */
    @Test
    public void testAddUserAssociationID() throws Exception
    {
        UserDao uDao = new UserDao();
        User user = new User("tuser", "ns1", "ADMIN");
        uDao.persist(user);
        UserClassDao cDao = new UserClassDao(uDao.getSession());
        UserClass ucls = new UserClass();
        ucls.setName("usClass");
        cDao.persist(ucls);
        UserAssociationDao aDao = new UserAssociationDao(uDao.getSession());
        
        AddUserAssociation req = new AddUserAssociation();
        UserAssociationType assocType = new UserAssociationType();
        req.setAddUserAssociation(assocType);
        assocType.setRequestorQName("TESTNS:mdiponio");
        UserIDType uid = new UserIDType();
        uid.setUserID(String.valueOf(user.getId()));
        assocType.setUser(uid);
        UserClassIDType cid = new UserClassIDType();
        cid.setUserClassID((ucls.getId().intValue()));
        assocType.setUserClass(cid);
        
        AddUserAssociationResponse resp = this.permissions.addUserAssociation(req);
        assertNotNull(resp);
        
        OperationResponseType op = resp.getAddUserAssociationResponse();
        assertNotNull(op);
        assertTrue(op.getSuccessful());
        assertEquals(0, op.getFailureCode());
        assertNull(op.getFailureReason());
        
        UserAssociation ua = aDao.get(new UserAssociationId(user.getId(), ucls.getId()));
        assertNotNull(ua);
        assertEquals(user.getId(), ua.getUser().getId());
        assertEquals(ucls.getId(), ua.getUserClass().getId());
        aDao.delete(ua);
        uDao.delete(user);
        cDao.delete(ucls);
        cDao.closeSession();
        
        OMElement ele = resp.getOMElement(AddUserAssociationResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("<successful>true</successful>"));
    }
    
    /**
     * Test method for {@link Permissions#addUserAssociation(AddUserAssociation)}.
     */
    @Test
    public void testAddUserAssociationNoIdName() throws Exception
    {
        AddUserAssociation req = new AddUserAssociation();
        UserAssociationType assocType = new UserAssociationType();
        req.setAddUserAssociation(assocType);
        assocType.setRequestorQName("TESTNS:mdiponio");
        UserIDType uid = new UserIDType();
        assocType.setUser(uid);
        UserClassIDType cid = new UserClassIDType();
        assocType.setUserClass(cid);
        
        AddUserAssociationResponse resp = this.permissions.addUserAssociation(req);
        assertNotNull(resp);
        
        OperationResponseType op = resp.getAddUserAssociationResponse();
        assertNotNull(op);
        assertFalse(op.getSuccessful());
        assertEquals(2, op.getFailureCode());
        assertNotNull(op.getFailureReason());
        
        OMElement ele = resp.getOMElement(AddUserAssociationResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("<successful>false</successful>"));
    }

    /**
     * Test method for {@link Permissions#addUserClass(AddUserClass)}.
     */
    @Test
    public void testAddUserClass() throws Exception
    {
        AddUserClass req = new AddUserClass();
        UserClassType cls = new UserClassType();
        req.setAddUserClass(cls);
        
        cls.setUserClassName("newClass");
        cls.setPriority(10);
        cls.setIsActive(true);
        cls.setIsKickable(true);
        cls.setIsQueuable(true);
        cls.setIsUserLockable(true);
        cls.setRequestorQName("TESTNS:mdiponio");
        
        AddUserClassResponse resp = this.permissions.addUserClass(req);
        assertNotNull(resp);
        
        OperationResponseType op = resp.getAddUserClassResponse();
        assertNotNull(op);
        assertTrue(op.getSuccessful());
        assertEquals(0, op.getFailureCode());
        assertNull(op.getFailureReason());
        
        UserClassDao dao = new UserClassDao();
        UserClass uc = dao.findByName("newClass");
        assertNotNull(uc);
        
        assertEquals("newClass", uc.getName());
        assertEquals(10, uc.getPriority());
        assertTrue(uc.isActive());
        assertTrue(uc.isKickable());
        assertTrue(uc.isQueuable());
        assertTrue(uc.isUsersLockable());
        
        dao.delete(uc);
        dao.closeSession();
        
        OMElement ele = resp.getOMElement(AddUserClassResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("<successful>true</successful>"));
    }
    
    /**
     * Test method for {@link Permissions#addUserClass(AddUserClass)}.
     */
    @Test
    public void testAddUserClassFalse() throws Exception
    {
        AddUserClass req = new AddUserClass();
        UserClassType cls = new UserClassType();
        req.setAddUserClass(cls);
        
        cls.setUserClassName("newClass");
        cls.setPriority(100);
        cls.setIsActive(false);
        cls.setIsKickable(false);
        cls.setIsQueuable(false);
        cls.setIsUserLockable(false);
        cls.setRequestorQName("TESTNS:mdiponio");
        
        AddUserClassResponse resp = this.permissions.addUserClass(req);
        assertNotNull(resp);
        
        OperationResponseType op = resp.getAddUserClassResponse();
        assertNotNull(op);
        assertTrue(op.getSuccessful());
        assertEquals(0, op.getFailureCode());
        assertNull(op.getFailureReason());
        
        UserClassDao dao = new UserClassDao();
        UserClass uc = dao.findByName("newClass");
        assertNotNull(uc);
        
        assertEquals("newClass", uc.getName());
        assertEquals(100, uc.getPriority());
        assertFalse(uc.isActive());
        assertFalse(uc.isKickable());
        assertFalse(uc.isQueuable());
        assertFalse(uc.isUsersLockable());
        
        dao.delete(uc);
        dao.closeSession();
        
        OMElement ele = resp.getOMElement(AddUserClassResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("<successful>true</successful>"));
    }
    
    /**
     * Test method for {@link Permissions#addUserClass(AddUserClass)}.
     */
    @Test
    public void testAddUserClassExists() throws Exception
    {
        UserClassDao dao = new UserClassDao();
        UserClass uc = new UserClass();
        uc.setName("exists");
        uc.setPriority(190);
        dao.persist(uc);
        
        AddUserClass req = new AddUserClass();
        UserClassType cls = new UserClassType();
        req.setAddUserClass(cls);
        cls.setRequestorQName("TESTNS:mdiponio");
        cls.setUserClassName("exists");
        cls.setPriority(10);
        cls.setIsActive(true);
        cls.setIsKickable(true);
        cls.setIsQueuable(true);
        cls.setIsUserLockable(true);
        
        AddUserClassResponse resp = this.permissions.addUserClass(req);
        assertNotNull(resp);
        
        OperationResponseType op = resp.getAddUserClassResponse();
        assertNotNull(op);
        assertFalse(op.getSuccessful());
        assertEquals(3, op.getFailureCode());
        assertNotNull(op.getFailureReason());
        
        dao.delete(uc);
        dao.closeSession();
        
        OMElement ele = resp.getOMElement(AddUserResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("<successful>false</successful>"));
    }

    /**
     * Test method for {@link Permissions#addUserLock(AddUserLock)}.
     */
    @Test
    public void testAddUserLock()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link Permissions#bulkAddUserClassUsers(BulkAddUserClassUsers)}.
     */
    @Test
    public void testBulkAddUserClassUsers()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link Permissions#deleteAcademicPermission(DeleteAcademicPermission)}.
     */
    @Test
    public void testDeleteAcademicPermission()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link Permissions#deletePermission(DeletePermission)}.
     */
    @Test
    public void testDeletePermission()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link Permissions#deleteUserAssociation(DeleteUserAssociation)}.
     */
    @Test
    public void testDeleteUserAssociation() throws Exception
    {
        Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        User user = new User("tuser", "ns", "USER");
        ses.save(user);
        UserClass cls = new UserClass();
        cls.setName("tclass");
        ses.save(cls);
        UserAssociation ass = new UserAssociation();
        ass.setId(new UserAssociationId(user.getId(), cls.getId()));
        ass.setUser(user);
        ass.setUserClass(cls);
        ses.save(ass);
        ses.getTransaction().commit();
        
        DeleteUserAssociation req = new DeleteUserAssociation();
        UserAssociationType assocType = new UserAssociationType();
        req.setDeleteUserAssociation(assocType);
        assocType.setRequestorQName("TESTNS:mdiponio");
        UserIDType uid = new UserIDType();
        uid.setUserQName("ns:tuser");
        assocType.setUser(uid);
        UserClassIDType cid = new UserClassIDType();
        cid.setUserClassName("tclass");
        assocType.setUserClass(cid);
        
        DeleteUserAssociationResponse resp = this.permissions.deleteUserAssociation(req);
        assertNotNull(resp);
        
        OperationResponseType op = resp.getDeleteUserAssociationResponse();
        assertNotNull(op);
        assertTrue(op.getSuccessful());
        assertEquals(0, op.getFailureCode());
        assertNull(op.getFailureReason());
        
        ses.beginTransaction();
        ses.delete(user);
        ses.delete(cls);
        ses.getTransaction().commit();
        
        OMElement ele = resp.getOMElement(DeleteUserAssociationResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("<successful>true</successful>"));
    }
    
    /**
     * Test method for {@link Permissions#deleteUserAssociation(DeleteUserAssociation)}.
     */
    @Test
    public void testDeleteUserAssociationID() throws Exception
    {
        Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        User user = new User("tuser", "ns", "USER");
        ses.save(user);
        UserClass cls = new UserClass();
        cls.setName("tclass");
        ses.save(cls);
        UserAssociation ass = new UserAssociation();
        ass.setId(new UserAssociationId(user.getId(), cls.getId()));
        ass.setUser(user);
        ass.setUserClass(cls);
        ses.save(ass);
        ses.getTransaction().commit();
        
        DeleteUserAssociation req = new DeleteUserAssociation();
        UserAssociationType assocType = new UserAssociationType();
        req.setDeleteUserAssociation(assocType);
        assocType.setRequestorQName("TESTNS:mdiponio");
        UserIDType uid = new UserIDType();
        uid.setUserID(String.valueOf(user.getId()));
        assocType.setUser(uid);
        UserClassIDType cid = new UserClassIDType();
        cid.setUserClassID(cls.getId().intValue());
        assocType.setUserClass(cid);
        
        DeleteUserAssociationResponse resp = this.permissions.deleteUserAssociation(req);
        assertNotNull(resp);
        
        OperationResponseType op = resp.getDeleteUserAssociationResponse();
        assertNotNull(op);
        assertTrue(op.getSuccessful());
        assertEquals(0, op.getFailureCode());
        assertNull(op.getFailureReason());
        
        ses.beginTransaction();
        ses.delete(user);
        ses.delete(cls);
        ses.getTransaction().commit();
        
        OMElement ele = resp.getOMElement(DeleteUserAssociationResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("<successful>true</successful>"));
    }
    
    /**
     * Test method for {@link Permissions#deleteUserAssociation(DeleteUserAssociation)}.
     */
    @Test
    public void testDeleteUserAssociationNoID() throws Exception
    {   
        DeleteUserAssociation req = new DeleteUserAssociation();
        UserAssociationType assocType = new UserAssociationType();
        req.setDeleteUserAssociation(assocType);
        assocType.setRequestorQName("TESTNS:mdiponio");
        UserIDType uid = new UserIDType();
        assocType.setUser(uid);
        UserClassIDType cid = new UserClassIDType();
        assocType.setUserClass(cid);
        
        DeleteUserAssociationResponse resp = this.permissions.deleteUserAssociation(req);
        assertNotNull(resp);
        
        OperationResponseType op = resp.getDeleteUserAssociationResponse();
        assertNotNull(op);
        assertFalse(op.getSuccessful());
        assertEquals(2, op.getFailureCode());
        assertNotNull(op.getFailureReason());
        
        OMElement ele = resp.getOMElement(DeleteUserAssociationResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("<successful>false</successful>"));
    }

    /**
     * Test method for {@link Permissions#deleteUserClass(DeleteUserClass)}.
     */
    @Test
    public void testDeleteUserClass() throws Exception
    {
        UserClassDao dao = new UserClassDao();
        UserClass uc = new UserClass();
        uc.setName("todelete");
        dao.persist(uc);
        
        DeleteUserClass req = new DeleteUserClass();
        UserClassIDType cid = new UserClassIDType();
        req.setDeleteUserClass(cid);
        cid.setUserClassName("todelete");
        
        DeleteUserClassResponse resp = this.permissions.deleteUserClass(req);
        assertNotNull(resp);
        OperationResponseType op = resp.getDeleteUserClassResponse();
        assertNotNull(op);
        
        assertTrue(op.getSuccessful());
        assertEquals(0, op.getFailureCode());
        assertNull(op.getFailureReason());
        
        assertNull(dao.findByName("todelete"));
        
        OMElement ele = resp.getOMElement(DeleteUserClassResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("<successful>true</successful>"));
    }
    
    /**
     * Test method for {@link Permissions#deleteUserClass(DeleteUserClass)}.
     */
    @Test
    public void testDeleteUserClassID() throws Exception
    {
        UserClassDao dao = new UserClassDao();
        UserClass uc = new UserClass();
        uc.setName("todelete");
        dao.persist(uc);
        
        DeleteUserClass req = new DeleteUserClass();
        UserClassIDType cid = new UserClassIDType();
        req.setDeleteUserClass(cid);
        cid.setUserClassID(uc.getId().intValue());
        
        DeleteUserClassResponse resp = this.permissions.deleteUserClass(req);
        assertNotNull(resp);
        OperationResponseType op = resp.getDeleteUserClassResponse();
        assertNotNull(op);
        
        assertTrue(op.getSuccessful());
        assertEquals(0, op.getFailureCode());
        assertNull(op.getFailureReason());
        
        assertNull(dao.findByName("todelete"));
        
        OMElement ele = resp.getOMElement(DeleteUserClassResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("<successful>true</successful>"));
    }
    
    /**
     * Test method for {@link Permissions#deleteUserClass(DeleteUserClass)}.
     */
    @Test
    public void testDeleteUserClassNotExist() throws Exception
    {
        DeleteUserClass req = new DeleteUserClass();
        UserClassIDType cid = new UserClassIDType();
        req.setDeleteUserClass(cid);
        cid.setUserClassName("todelete");
        
        DeleteUserClassResponse resp = this.permissions.deleteUserClass(req);
        assertNotNull(resp);
        OperationResponseType op = resp.getDeleteUserClassResponse();
        assertNotNull(op);
        
        assertFalse(op.getSuccessful());
        assertEquals(3, op.getFailureCode());
        assertNotNull(op.getFailureReason());
        OMElement ele = resp.getOMElement(DeleteUserClassResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("<successful>false</successful>"));
    }
    
    /**
     * Test method for {@link Permissions#deleteUserClass(DeleteUserClass)}.
     */
    @Test
    public void testDeleteUserClassNoIdName() throws Exception
    {

        DeleteUserClass req = new DeleteUserClass();
        UserClassIDType cid = new UserClassIDType();
        req.setDeleteUserClass(cid);
        
        DeleteUserClassResponse resp = this.permissions.deleteUserClass(req);
        assertNotNull(resp);
        OperationResponseType op = resp.getDeleteUserClassResponse();
        assertNotNull(op);
        
        assertFalse(op.getSuccessful());
        assertEquals(2, op.getFailureCode());
        assertNotNull(op.getFailureReason());
        
        OMElement ele = resp.getOMElement(DeleteUserClassResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("<successful>false</successful>"));
    }

    /**
     * Test method for {@link Permissions#deleteUserLock(DeleteUserLock)}.
     */
    @Test
    public void testDeleteUserLock()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link Permissions#editAcademicPermission(EditAcademicPermission)}.
     */
    @Test
    public void testEditAcademicPermission()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link Permissions#editPermission(EditPermission)}.
     */
    @Test
    public void testEditPermission()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link Permissions#editUserClass(EditUserClass)}.
     */
    @Test
    public void testEditUserClass() throws Exception
    {
        UserClassDao dao = new UserClassDao();
        UserClass uc = new UserClass();
        uc.setName("uclass");
        uc.setPriority(190);
        dao.persist(uc);
        
        EditUserClass req = new EditUserClass();
        UserClassType cls = new UserClassType();
        req.setEditUserClass(cls);
        cls.setRequestorQName("TESTNS:mdiponio");
        cls.setUserClassName("uclass");
        cls.setPriority(10);
        cls.setIsActive(true);
        cls.setIsKickable(true);
        cls.setIsQueuable(true);
        cls.setIsUserLockable(true);
        
        EditUserClassResponse resp = this.permissions.editUserClass(req);
        assertNotNull(resp);
        
        OperationResponseType op = resp.getEditUserClassResponse();
        assertNotNull(op);
        assertTrue(op.getSuccessful());
        assertEquals(0, op.getFailureCode());
        assertNull(op.getFailureReason());
        
        dao.refresh(uc);
        assertEquals("uclass", uc.getName());
        assertEquals(10, uc.getPriority());
        assertTrue(uc.isActive());
        assertTrue(uc.isKickable());
        assertTrue(uc.isQueuable());
        assertTrue(uc.isUsersLockable());
        
        dao.delete(uc);
        dao.closeSession();
        
        OMElement ele = resp.getOMElement(EditUserClassResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("<successful>true</successful>"));
    }
    
    /**
     * Test method for {@link Permissions#editUserClass(EditUserClass)}.
     */
    @Test
    public void testEditUserClassID() throws Exception
    {
        UserClassDao dao = new UserClassDao();
        UserClass uc = new UserClass();
        uc.setName("uclass");
        uc.setPriority(190);
        dao.persist(uc);
        
        EditUserClass req = new EditUserClass();
        UserClassType cls = new UserClassType();
        req.setEditUserClass(cls);
        cls.setUserClassID(uc.getId().intValue());
        cls.setRequestorQName("TESTNS:mdiponio");
        cls.setUserClassName("newname");
        cls.setPriority(10);
        cls.setIsActive(true);
        cls.setIsKickable(false);
        cls.setIsQueuable(true);
        cls.setIsUserLockable(false);
        
        EditUserClassResponse resp = this.permissions.editUserClass(req);
        assertNotNull(resp);
        
        OperationResponseType op = resp.getEditUserClassResponse();
        assertNotNull(op);
        assertTrue(op.getSuccessful());
        assertEquals(0, op.getFailureCode());
        assertNull(op.getFailureReason());
        
        dao.refresh(uc);
        assertEquals("newname", uc.getName());
        assertEquals(10, uc.getPriority());
        assertTrue(uc.isActive());
        assertFalse(uc.isKickable());
        assertTrue(uc.isQueuable());
        assertFalse(uc.isUsersLockable());
        
        dao.delete(uc);
        dao.closeSession();
        
        OMElement ele = resp.getOMElement(EditUserClassResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("<successful>true</successful>"));
    }
    
    /**
     * Test method for {@link Permissions#editUserClass(EditUserClass)}.
     */
    @Test
    public void testEditUserClassDoesExist() throws Exception
    {
        EditUserClass req = new EditUserClass();
        UserClassType cls = new UserClassType();
        req.setEditUserClass(cls);
        cls.setRequestorQName("TESTNS:mdiponio");
        cls.setUserClassName("notexists");
        cls.setPriority(10);
        
        EditUserClassResponse resp = this.permissions.editUserClass(req);
        assertNotNull(resp);
        
        OperationResponseType op = resp.getEditUserClassResponse();
        assertNotNull(op);
        assertFalse(op.getSuccessful());
        assertEquals(3, op.getFailureCode());
        assertNotNull(op.getFailureReason());
        
        OMElement ele = resp.getOMElement(EditUserClassResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("<successful>false</successful>"));
    }


    /**
     * Test method for {@link Permissions#getAcademicPermission(GetAcademicPermission)}.
     */
    @Test
    public void testGetAcademicPermission()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link Permissions#getAcademicPermissionsForAcademic(GetAcademicPermissionsForAcademic)}.
     */
    @Test
    public void testGetAcademicPermissionsForAcademic()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link Permissions#getAcademicPermissionsForUserClass(GetAcademicPermissionsForUserClass)}.
     */
    @Test
    public void testGetAcademicPermissionsForUserClass()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link Permissions#getPermission(GetPermission)}.
     */
    @Test
    public void testGetPermission()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link Permissions#getPermissionsForUser(GetPermissionsForUser)}.
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
        ResourcePermission perm1 = new ResourcePermission(uclass1, "TYPE", 3600, 300, (short) 10, 300, 300);
        perm1.setRigType(rigType1);
        perm1.setStartTime(new Date());
        perm1.setExpiryTime(new Date());
        ses.save(perm1);
        ResourcePermission perm2 = new ResourcePermission(uclass1, "RIG", 3600, 300, (short) 10, 300, 300);
        perm2.setRig(rig1);
        perm2.setStartTime(new Date());
        perm2.setExpiryTime(new Date());
        ses.save(perm2);
        ResourcePermission perm3 = new ResourcePermission(uclass1, "RIG", 3600, 300, (short) 10, 300, 300);
        perm3.setRig(rig2);
        perm3.setStartTime(new Date());
        perm3.setExpiryTime(new Date());
        ses.save(perm3);
        ResourcePermission perm4 = new ResourcePermission(uclass2, "TYPE", 3600, 300, (short) 10, 300, 300);
        perm4.setRigType(rigType2);
        perm4.setStartTime(new Date());
        perm4.setExpiryTime(new Date());
        ses.save(perm4);
        ResourcePermission perm5 = new ResourcePermission(uclass2, "RIG", 3600, 300, (short) 10, 300, 300);
        perm5.setRig(rig3);
        perm5.setStartTime(new Date());
        perm5.setExpiryTime(new Date());
        ses.save(perm5);
        ResourcePermission perm6 = new ResourcePermission(uclass2, "RIG", 3600, 300, (short) 10, 300, 300);
        perm6.setRig(rig4);
        perm6.setStartTime(new Date());
        perm6.setExpiryTime(new Date());
        ses.save(perm6);
        ResourcePermission perm7 = new ResourcePermission(uclass2, "CAPABILITY", 3600, 300, (short) 10, 300, 300);
        perm7.setRequestCapabilities(caps);
        perm7.setStartTime(new Date());
        perm7.setExpiryTime(new Date());
        ses.save(perm7);
        ResourcePermission perm8 = new ResourcePermission(uclass3, "RIG", 3600, 300, (short) 10, 300, 300);
        perm8.setRig(rig5);
        perm8.setStartTime(new Date());
        perm8.setExpiryTime(new Date());
        ses.save(perm8);
        ResourcePermission perm9 = new ResourcePermission(uclass3, "CAPABILITY", 3600, 300, (short) 10, 300, 300);
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
     * Test method for {@link Permissions#getPermissionsForUser(GetPermissionsForUser)}.
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
        ses.save(uclass1);
        UserAssociation assoc1 = new UserAssociation(new UserAssociationId(user.getId(), uclass1.getId()), uclass1, user);
        ses.save(assoc1);
        RigType rigType1 = new RigType("interRigType1", 300, false);
        ses.save(rigType1);
        RigCapabilities rigCaps = new RigCapabilities("not,important");
        ses.save(rigCaps);
        Rig rig1 = new Rig(rigType1, rigCaps, "interRig1", "http://contact", new Date(), true, null, false, true, true);
        ses.save(rig1);
        ResourcePermission perm1 = new ResourcePermission(uclass1, "TYPE", 3600, 300, (short) 10, 300, 300);
        perm1.setRigType(rigType1);
        perm1.setStartTime(new Date());
        perm1.setExpiryTime(new Date());
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
        assertEquals(perm1.getAllowedExtensions(), p.getAllowedExtensions());
        assertEquals(perm1.getExtensionDuration(), p.getExtensionDuration());
        assertEquals(perm1.getQueueActivityTimeout(), p.getQueueActivityTmOut());
        assertEquals(perm1.getSessionActivityTimeout(), p.getSessionActivityTmOut());
        assertEquals(perm1.getSessionDuration(), p.getSessionDuration());
        assertEquals("TYPE", p.getResourceClass().getValue());
        
        ResourceIDType res = p.getResource();
        assertNotNull(res);
        assertEquals(res.getResourceName(), "interRigType1");
    }
    
    /**
     * Test method for {@link Permissions#getPermissionsForUser(GetPermissionsForUser)}.
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
        ses.save(uclass1);
        UserAssociation assoc1 = new UserAssociation(new UserAssociationId(user.getId(), uclass1.getId()), uclass1, user);
        ses.save(assoc1);
        RigType rigType1 = new RigType("interRigType1", 300, false);
        ses.save(rigType1);
        RigCapabilities rigCaps = new RigCapabilities("not,important");
        ses.save(rigCaps);
        Rig rig1 = new Rig(rigType1, rigCaps, "interRig1", "http://contact", new Date(), true, null, false, true, true);
        ses.save(rig1);
        ResourcePermission perm1 = new ResourcePermission(uclass1, "RIG", 3600, 300, (short) 10, 300, 300);
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
     * Test method for {@link Permissions#getPermissionsForUser(GetPermissionsForUser)}.
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
     * Test method for {@link Permissions#getPermissionsForUser(GetPermissionsForUser)}.
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
     * Test method for {@link Permissions#getPermissionsForUserClass(GetPermissionsForUserClass)}.
     */
    @Test
    public void testGetPermissionsForUserClass()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link Permissions#getUser(GetUser)}.
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
     * Test method for {@link Permissions#getUser(GetUser)}.
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
     * Test method for {@link Permissions#getUser(GetUser)}.
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
     * Test method for {@link Permissions#getUserClass(GetUserClass)}.
     */
    @Test
    public void testGetUserClass() throws Exception
    {
        UserClassDao dao = new UserClassDao();
        UserClass cls = new UserClass("ucTest", 50, true, false, true, false);
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
        assertEquals(cls.isUsersLockable(), uc.getIsUserLockable());
        
        dao.delete(cls);
        dao.closeSession();
        
        OMElement ele = resp.getOMElement(GetUserClassResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }
    
    /**
     * Test method for {@link Permissions#getUserClass(GetUserClass)}.
     */
    @Test
    public void testGetUserClassName() throws Exception
    {
        UserClassDao dao = new UserClassDao();
        UserClass cls = new UserClass("ucTest", 50, true, false, true, false);
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
        assertEquals(cls.isActive(), uc.getIsActive());
        assertEquals(cls.isKickable(), uc.getIsKickable());
        assertEquals(cls.isQueuable(), uc.getIsQueuable());
        assertEquals(cls.isUsersLockable(), uc.getIsUserLockable());
        
        dao.delete(cls);
        dao.closeSession();
        
        OMElement ele = resp.getOMElement(GetUserClassResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }

    /**
     * Test method for {@link Permissions#getUserClasses(GetUserClasses)}.
     */
    @Test
    public void testGetUserClasses()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link Permissions#getUserClassesForUser(GetUserClassesForUser)}.
     */
    @Test
    public void testGetUserClassesForUser()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link Permissions#getUsersInUserClass(GetUsersInUserClass)}.
     */
    @Test
    public void testGetUsersInUserClass()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link Permissions#unlockUserLock(UnlockUserLock)}.
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
     * Test method for {@link Permissions#unlockUserLock(UnlockUserLock)}.
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
     * Test method for {@link Permissions#unlockUserLock(UnlockUserLock)}.
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
     * Test method for {@link Permissions#unlockUserLock(UnlockUserLock)}.
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
     * Test method for {@link Permissions#unlockUserLock(UnlockUserLock)}.
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
        
        Method meth = Permissions.class.getDeclaredMethod("getUserFromUserID", UserIDType.class, Session.class);
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
        
        Method meth = Permissions.class.getDeclaredMethod("getUserFromUserID", UserIDType.class, Session.class);
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
        
        Method meth = Permissions.class.getDeclaredMethod("getUserFromUserID", UserIDType.class, Session.class);
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
        
        Method meth = Permissions.class.getDeclaredMethod("getUserFromUserID", UserIDType.class, Session.class);
        meth.setAccessible(true);
        assertNull(meth.invoke(this.permissions, uid, ses));
        
        ses.close();
    }
}
