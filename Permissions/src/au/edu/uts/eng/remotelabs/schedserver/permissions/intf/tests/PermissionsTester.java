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
import java.util.Properties;

import junit.framework.TestCase;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.hibernate.cfg.AnnotationConfiguration;
import org.junit.Before;
import org.junit.Test;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
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
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUser;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserClass;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserClassResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUser;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserClass;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserClassResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUser;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUserClass;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUserClassResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUserResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUser;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClass;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.OperationResponseType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.PersonaType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserClassIDType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserClassType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserIDType;
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
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions#addAcademicPermission(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddAcademicPermission)}.
     */
    @Test
    public void testAddAcademicPermission()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions#addPermission(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddPermission)}.
     */
    @Test
    public void testAddPermission()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions#addUser(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUser)}.
     */
    @Test
    public void testAddUser() throws Exception
    {
        AddUser req = new AddUser();
        UserType user = new UserType();
        req.setAddUser(user);
        user.setRequestorQName("UTS:mdiponio");
        UserNameNamespaceSequence nsname = new UserNameNamespaceSequence();
        nsname.setUserNamespace("UTS");
        nsname.setUserName("tmachet");
        user.setUserNameNamespaceSequence(nsname);
        user.setPersona(PersonaType.ADMIN);
        
        AddUserResponse resp = this.permissions.addUser(req);
        assertNotNull(resp);
        
        OperationResponseType op = resp.getAddUserResponse();
        assertNotNull(op);
        assertTrue(op.getSuccessful());
        
        UserDao dao = new UserDao();
        User us = dao.findByName("UTS", "tmachet");
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
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions#editUser(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUser)}.
     */
    @Test
    public void testEditUser() throws Exception
    {
        User user = new User();
        user.setNamespace("UTS");
        user.setName("tmachet");
        user.setPersona("USER");
        UserDao dao = new UserDao();
        dao.persist(user);
        
        EditUser req = new EditUser();
        UserType ty = new UserType();
        req.setEditUser(ty);
        ty.setRequestorQName("UTS:mdiponio");
        ty.setUserID(String.valueOf(user.getId()));
        UserNameNamespaceSequence seq = new UserNameNamespaceSequence();
        seq.setUserNamespace("UTS-ENG");
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
        assertEquals("UTS-ENG", user.getNamespace());
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
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions#deleteUser(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUser)}.
     */
    @Test
    public void testDeleteUser() throws Exception
    {
        UserDao dao = new UserDao();
        User user = new User();
        user.setPersona("USER");
        user.setName("tmachet");
        user.setNamespace("UTS");
        dao.persist(user);
        
        DeleteUser req = new DeleteUser();
        UserIDType usTy = new UserIDType();
        req.setDeleteUser(usTy);
        usTy.setRequestorQName("UTS:mdiponio");
        UserNameNamespaceSequence seq = new UserNameNamespaceSequence();
        seq.setUserNamespace("UTS");
        seq.setUserName("tmachet");
        usTy.setUserNameNamespaceSequence(seq);
        
        DeleteUserResponse resp = this.permissions.deleteUser(req);
        assertNotNull(resp);
        
        OperationResponseType op = resp.getDeleteUserResponse();
        assertNotNull(op);
        assertTrue(op.getSuccessful());
        
        assertNull(dao.findByName("UTS", "tmachet"));
        
        OMElement ele = resp.getOMElement(DeleteUserResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("<successful>true</successful>"));
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions#deleteUser(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUser)}.
     */
    @Test
    public void testDeleteUserID() throws Exception
    {
        UserDao dao = new UserDao();
        User user = new User();
        user.setPersona("USER");
        user.setName("tmachet");
        user.setNamespace("UTS");
        dao.persist(user);
        
        DeleteUser req = new DeleteUser();
        UserIDType usTy = new UserIDType();
        req.setDeleteUser(usTy);
        usTy.setRequestorQName("UTS:mdiponio");
        usTy.setUserID(String.valueOf(user.getId()));
        
        DeleteUserResponse resp = this.permissions.deleteUser(req);
        assertNotNull(resp);
        
        OperationResponseType op = resp.getDeleteUserResponse();
        assertNotNull(op);
        assertTrue(op.getSuccessful());
        
        assertNull(dao.findByName("UTS", "tmachet"));
        
        OMElement ele = resp.getOMElement(DeleteUserResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("<successful>true</successful>"));
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions#addUserAssociation(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserAssociation)}.
     */
    @Test
    public void testAddUserAssociation()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions#addUserClass(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserClass)}.
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
        cls.setRequestorQName("UTS:mdiponio");
        
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
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions#addUserClass(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserClass)}.
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
        cls.setRequestorQName("UTS:mdiponio");
        
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
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions#addUserClass(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserClass)}.
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
        cls.setRequestorQName("UTS:mdiponio");
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
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions#addUserLock(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserLock)}.
     */
    @Test
    public void testAddUserLock()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions#bulkAddUserClassUsers(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.BulkAddUserClassUsers)}.
     */
    @Test
    public void testBulkAddUserClassUsers()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions#deleteAcademicPermission(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteAcademicPermission)}.
     */
    @Test
    public void testDeleteAcademicPermission()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions#deletePermission(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeletePermission)}.
     */
    @Test
    public void testDeletePermission()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions#deleteUserAssociation(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserAssociation)}.
     */
    @Test
    public void testDeleteUserAssociation()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions#deleteUserClass(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserClass)}.
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
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions#deleteUserClass(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserClass)}.
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
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions#deleteUserClass(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserClass)}.
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
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions#deleteUserClass(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserClass)}.
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
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions#deleteUserLock(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserLock)}.
     */
    @Test
    public void testDeleteUserLock()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions#editAcademicPermission(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditAcademicPermission)}.
     */
    @Test
    public void testEditAcademicPermission()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions#editPermission(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditPermission)}.
     */
    @Test
    public void testEditPermission()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions#editUserClass(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUserClass)}.
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
        cls.setRequestorQName("UTS:mdiponio");
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
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions#editUserClass(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUserClass)}.
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
        cls.setRequestorQName("UTS:mdiponio");
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
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions#editUserClass(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUserClass)}.
     */
    @Test
    public void testEditUserClassDoesExist() throws Exception
    {
        EditUserClass req = new EditUserClass();
        UserClassType cls = new UserClassType();
        req.setEditUserClass(cls);
        cls.setRequestorQName("UTS:mdiponio");
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
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions#getAcademicPermission(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermission)}.
     */
    @Test
    public void testGetAcademicPermission()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions#getAcademicPermissionsForAcademic(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForAcademic)}.
     */
    @Test
    public void testGetAcademicPermissionsForAcademic()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions#getAcademicPermissionsForUserClass(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForUserClass)}.
     */
    @Test
    public void testGetAcademicPermissionsForUserClass()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions#getPermission(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermission)}.
     */
    @Test
    public void testGetPermission()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions#getPermissionsForUser(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUser)}.
     */
    @Test
    public void testGetPermissionsForUser()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions#getPermissionsForUserClass(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserClass)}.
     */
    @Test
    public void testGetPermissionsForUserClass()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions#getUser(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUser)}.
     */
    @Test
    public void testGetUser() throws Exception
    {
        User user = new User();
        user.setNamespace("UTS");
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
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions#getUser(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUser)}.
     */
    @Test
    public void testGetUserQName() throws Exception
    {
        User user = new User();
        user.setNamespace("UTS");
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
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions#getUser(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUser)}.
     */
    @Test
    public void testGetUserSeq() throws Exception
    {
        User user = new User();
        user.setNamespace("UTS");
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
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions#getUserClass(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClass)}.
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
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions#getUserClass(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClass)}.
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
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions#getUserClasses(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClasses)}.
     */
    @Test
    public void testGetUserClasses()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions#getUserClassesForUser(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassesForUser)}.
     */
    @Test
    public void testGetUserClassesForUser()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions#getUsersInUserClass(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUsersInUserClass)}.
     */
    @Test
    public void testGetUsersInUserClass()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.permissions.intf.Permissions#unlockUserLock(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UnlockUserLock)}.
     */
    @Test
    public void testUnlockUserLock()
    {
        fail("Not yet implemented");
    }

}
