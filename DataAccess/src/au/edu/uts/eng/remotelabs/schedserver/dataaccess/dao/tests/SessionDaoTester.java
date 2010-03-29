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
package au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.tests;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Properties;

import junit.framework.TestCase;

import org.hibernate.cfg.AnnotationConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.SessionDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.AcademicPermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Config;
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
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserLock;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.logger.impl.SystemErrLogger;

/**
 * Tests the {@link SessionDao} class.
 */
public class SessionDaoTester extends TestCase
{
    /** Object of class under test. */
    private SessionDao dao;
    
    public SessionDaoTester(String name) throws Exception
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
        cfg.addAnnotatedClass(Session.class);
        cfg.addAnnotatedClass(User.class);
        cfg.addAnnotatedClass(UserAssociation.class);
        cfg.addAnnotatedClass(UserAssociationId.class);
        cfg.addAnnotatedClass(UserClass.class);
        cfg.addAnnotatedClass(UserLock.class);
        
        f = DataAccessActivator.class.getDeclaredField("sessionFactory");
        f.setAccessible(true);
        f.set(null, cfg.buildSessionFactory());
    }

    @Before
    @Override
    public void setUp() throws Exception
    {
        this.dao = new SessionDao();
    }
    
    @After
    @Override
    public void tearDown() throws Exception
    {
        this.dao.closeSession();
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.SessionDao#findActiveSession(au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User)}.
     */
    @Test
    public void testFindActiveSession()
    {
        Date now = new Date();
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        db.beginTransaction();
        User user = new User("locktest", "ns", "USER");
        db.persist(user);
        UserClass userClass= new UserClass();
        userClass.setName("uc");
        db.persist(userClass);
        RigType type = new RigType();
        type.setName("rig_type");
        db.persist(type);
        ResourcePermission perm = new ResourcePermission();         
        perm.setStartTime(now);
        perm.setExpiryTime(now);
        perm.setUserClass(userClass);
        perm.setType("TYPE");
        perm.setRigType(type);
        db.persist(perm);
        Session ses = new Session();
        ses.setActive(true);
        ses.setActivityLastUpdated(now);
        ses.setPriority((short) 1);
        ses.setRequestTime(now);
        ses.setUser(user);
        ses.setUserName(user.getName());
        ses.setUserNamespace(user.getNamespace());
        ses.setRequestedResourceId(type.getId());
        ses.setRequestedResourceName(type.getName());
        ses.setResourcePermission(perm);
        ses.setResourceType("TYPE");
        db.persist(ses);
        db.getTransaction().commit();
        
        Session found = this.dao.findActiveSession(user);
        assertNotNull(found);
        assertEquals(ses.getId(), found.getId());
        
        db.beginTransaction();
        db.delete(ses);
        db.delete(perm);
        db.delete(type);
        db.delete(userClass);
        db.delete(user);
        db.getTransaction().commit();   
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.SessionDao#findActiveSession(au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User)}.
     */
    @Test
    public void testFindActiveSessionNotFound()
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        db.beginTransaction();
        User user = new User("locktest", "ns", "USER");
        db.persist(user);
        db.getTransaction().commit();
        
        Session found = this.dao.findActiveSession(user);
        assertNull(found);
        
        db.beginTransaction();
        db.delete(user);
        db.getTransaction().commit();   
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.SessionDao#findActiveSession(au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User)}.
     */
    @Test
    public void testFindActiveSessionTwoSessions()
    {
        Date now = new Date();
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        db.beginTransaction();
        User user = new User("locktest", "ns", "USER");
        db.persist(user);
        UserClass userClass= new UserClass();
        userClass.setName("uc");
        db.persist(userClass);
        RigType type = new RigType();
        type.setName("rig_type");
        db.persist(type);
        ResourcePermission perm = new ResourcePermission();         
        perm.setStartTime(now);
        perm.setExpiryTime(now);
        perm.setUserClass(userClass);
        perm.setType("TYPE");
        perm.setRigType(type);
        db.persist(perm);
        Session ses = new Session();
        ses.setActive(true);
        ses.setActivityLastUpdated(now);
        ses.setPriority((short) 1);
        ses.setRequestTime(now);
        ses.setUser(user);
        ses.setUserName(user.getName());
        ses.setUserNamespace(user.getNamespace());
        ses.setRequestedResourceId(type.getId());
        ses.setRequestedResourceName(type.getName());
        ses.setResourcePermission(perm);
        ses.setResourceType("TYPE");
        db.persist(ses);
        Session ses2 = new Session();
        ses2.setActive(false);
        ses2.setActivityLastUpdated(now);
        ses2.setPriority((short) 1);
        ses2.setRequestTime(new Date(System.currentTimeMillis() - 1000));
        ses2.setUser(user);
        ses2.setUserName(user.getName());
        ses2.setUserNamespace(user.getNamespace());
        ses2.setRequestedResourceId(type.getId());
        ses2.setRequestedResourceName(type.getName());
        ses2.setResourcePermission(perm);
        ses2.setResourceType("TYPE");
        db.persist(ses2);
        
        db.getTransaction().commit();
        
        Session found = this.dao.findActiveSession(user);
        assertNotNull(found);
        assertEquals(ses.getId(), found.getId());
        
        db.beginTransaction();
        db.delete(ses2);
        db.delete(ses);
        db.delete(perm);
        db.delete(type);
        db.delete(userClass);
        db.delete(user);
        db.getTransaction().commit();   
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.SessionDao#findActiveSession(au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User)}.
     */
    @Test
    public void testFindActiveSessionTwoActiveSessions()
    {
        Date now = new Date();
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        db.beginTransaction();
        User user = new User("locktest", "ns", "USER");
        db.persist(user);
        UserClass userClass= new UserClass();
        userClass.setName("uc");
        db.persist(userClass);
        RigType type = new RigType();
        type.setName("rig_type");
        db.persist(type);
        ResourcePermission perm = new ResourcePermission();         
        perm.setStartTime(now);
        perm.setExpiryTime(now);
        perm.setUserClass(userClass);
        perm.setType("TYPE");
        perm.setRigType(type);
        db.persist(perm);
        Session ses = new Session();
        ses.setActive(true);
        ses.setActivityLastUpdated(now);
        ses.setPriority((short) 1);
        ses.setRequestTime(now);
        ses.setUser(user);
        ses.setUserName(user.getName());
        ses.setUserNamespace(user.getNamespace());
        ses.setRequestedResourceId(type.getId());
        ses.setRequestedResourceName(type.getName());
        ses.setResourcePermission(perm);
        ses.setResourceType("TYPE");
        db.persist(ses);
        Session ses2 = new Session();
        ses2.setActive(true);
        ses2.setActivityLastUpdated(now);
        ses2.setPriority((short) 1);
        ses2.setRequestTime(new Date(System.currentTimeMillis() - 1000));
        ses2.setUser(user);
        ses2.setUserName(user.getName());
        ses2.setUserNamespace(user.getNamespace());
        ses2.setRequestedResourceId(type.getId());
        ses2.setRequestedResourceName(type.getName());
        ses2.setResourcePermission(perm);
        ses2.setResourceType("TYPE");
        db.persist(ses2);
        
        db.getTransaction().commit();
        
        Session found = this.dao.findActiveSession(user);
        assertNotNull(found);
        assertEquals(ses.getId(), found.getId());
        
        db.beginTransaction();
        db.delete(ses2);
        db.delete(ses);
        db.delete(perm);
        db.delete(type);
        db.delete(userClass);
        db.delete(user);
        db.getTransaction().commit();   
    }

}
