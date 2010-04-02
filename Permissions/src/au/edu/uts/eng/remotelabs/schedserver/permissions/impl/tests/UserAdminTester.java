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
package au.edu.uts.eng.remotelabs.schedserver.permissions.impl.tests;

import java.lang.reflect.Field;
import java.util.Properties;

import junit.framework.TestCase;

import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.Session;
import org.junit.Test;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
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
import au.edu.uts.eng.remotelabs.schedserver.permissions.impl.UserAdmin;

/**
 * Tests the {@link UserAdmin} class.
 */
public class UserAdminTester extends TestCase
{
    /** Object of class under test. */
    private UserAdmin admin;
    
    /** User DAO. */
    private UserDao dao;
    
    public UserAdminTester(String name) throws Exception
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
    public void setUp()
    {
        this.admin = new UserAdmin(DataAccessActivator.getNewSession());
        this.dao = new UserDao();
        
    }
    
    @Test
    public void testAddUser()
    {
        String name = "tuser";
        String ns = "tuserns";
        
        assertTrue(this.admin.addUser(ns, name));
               
        User user = this.dao.findByName(ns, name);
        assertNotNull(user);
        assertEquals(name, user.getName());
        assertEquals(ns, user.getNamespace());
        assertEquals(User.USER, user.getPersona());
        
        this.dao.delete(user);
    }

    @Test
    public void testAddUserWithPersona()
    {
        String name = "tuser";
        String ns = "tuserns";
        String ps = User.ADMIN;
        
        assertTrue(this.admin.addUser(ns, name, ps));
               
        User user = this.dao.findByName(ns, name);
        assertNotNull(user);
        assertEquals(name, user.getName());
        assertEquals(ns, user.getNamespace());
        assertEquals(ps, user.getPersona());
        
        this.dao.delete(user);
    }
    
    @Test
    public void testAddUserExists()
    {
        String name = "tuser";
        String ns = "tuserns";
        
        User user = new User(name, ns, User.ADMIN);
        this.dao.persist(user);
        
        assertFalse(this.admin.addUser(ns, name, User.ADMIN));
        this.dao.delete(user);
    }
    
    @Test
    public void testAddUserWrongPersona()
    {
        String name = "tuser";
        String ns = "tuserns";
        
        assertFalse(this.admin.addUser(ns, name, "foobar"));
    }
    
    @Test
    public void testEditUserNewNameNSPersona()
    {
        String name = "tuser";
        String ns = "tuserns";
        
        User user = new User(name, ns, User.ADMIN);
        Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        ses.save(user);
        ses.getTransaction().commit();
        
        assertTrue(this.admin.editUser(user.getId(), "newName", "newNS", User.USER));
        
        User edittedUser = this.dao.findByName("newName", "newNS");
        ses.refresh(user);
        assertEquals(user.getName(), edittedUser.getName());
        assertEquals(user.getNamespace(), edittedUser.getNamespace());
        assertEquals(user.getId(), edittedUser.getId());
        assertEquals(user.getPersona(), edittedUser.getPersona());
        
        this.dao.delete(edittedUser);
    }
    
    @Test
    public void testEditUserNewPersona()
    {
        String name = "tuser";
        String ns = "tuserns";
        
        User user = new User(name, ns, User.ADMIN);
        Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        ses.save(user);
        ses.getTransaction().commit();
        
        assertTrue(this.admin.editUser(user.getId(), ns, name, User.USER));
       
        ses.refresh(user);
        assertEquals(name, user.getName());
        assertEquals(ns, user.getNamespace());
        assertEquals(User.USER, user.getPersona());
        
        ses.beginTransaction();
        ses.delete(user);
        ses.getTransaction().commit();
    }
    
    @Test
    public void testEditUserNoNameNoNSPersona()
    {
        String name = "tuser";
        String ns = "tuserns";
        
        User user = new User(name, ns, User.ADMIN);
        Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        ses.save(user);
        ses.getTransaction().commit();
        
        assertTrue(this.admin.editUser(user.getId(), null, null, User.USER));
       
        ses.refresh(user);
        assertEquals(name, user.getName());
        assertEquals(ns, user.getNamespace());
        assertEquals(User.USER, user.getPersona());
        
        ses.beginTransaction();
        ses.delete(user);
        ses.getTransaction().commit();
    }
    
    @Test
    public void testEditNoUser()
    {
        assertFalse(this.admin.editUser(1, "newName", "newNS", User.USER));
        assertNotNull(this.admin.getFailureReason());
    }
    
    @Test
    public void testEditUserPersona()
    {
        String name = "eUser";
        String ns = "nseUser";
        
        User user = new User(name, ns, User.ADMIN);
        this.dao.persist(user);
        
        assertTrue(this.admin.editUser(ns, name, User.ACADEMIC));
        assertNull(this.admin.getFailureReason());
        
        this.dao.refresh(user);
        assertEquals(name, user.getName());
        assertEquals(ns, user.getNamespace());
        assertEquals(User.ACADEMIC, user.getPersona());
        
        this.dao.delete(user);
    }
    
    @Test
    public void testEditUserInvalidPersona()
    {
        String name = "eUser";
        String ns = "nseUser";
        
        User user = new User(name, ns, User.ADMIN);
        this.dao.persist(user);
        
        assertFalse(this.admin.editUser(ns, name, "Not valid"));
        assertNotNull(this.admin.getFailureReason());
        
        this.dao.delete(user);
    }
    
    @Test
    public void testEditUserUserNotExist()
    {
        assertFalse(this.admin.editUser("not", "exist", User.ADMIN));
        assertNotNull(this.admin.getFailureReason());
    }
    
    @Test
    public void testDeleteUser()
    {
        Session ses = DataAccessActivator.getNewSession();
        
        ses.beginTransaction();
        UserClass uc = new UserClass("test1", (short) 10, true, true, true, true);
        ses.save(uc);
        User us = new User("del1", "ns1", "ACADEMIC");
        ses.save(us);
        ses.getTransaction().commit();
        
        ses.beginTransaction();
        UserAssociation assoc = new UserAssociation();
        assoc.setUser(us);
        assoc.setUserClass(uc);
        UserAssociationId id = new UserAssociationId();
        id.setUserClassId(uc.getId());
        id.setUsersId(us.getId());
        assoc.setId(id);
        ses.save(assoc);
        ses.getTransaction().commit();
        
        this.admin.deleteUser(us.getId());
        
        assertNull(this.dao.findByName("ns1", "test1"));
        
        ses.beginTransaction();
        ses.delete(uc);
        ses.getTransaction().commit();
    }
}
