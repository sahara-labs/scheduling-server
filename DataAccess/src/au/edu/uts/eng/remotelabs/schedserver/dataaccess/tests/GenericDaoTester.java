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
 * @date 8th January 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.dataaccess.tests;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Properties;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.hibernate.cfg.AnnotationConfiguration;
import org.junit.After;
import org.junit.Before;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.GenericDao;
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

/**
 * Tests the {@link GenericDao} class.
 */
public class GenericDaoTester extends TestCase
{
    /** Object of class under test. */
    private GenericDao<Config> dao;

    public GenericDaoTester(String name) throws Exception
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

    @Before
    @Override
    protected void setUp() throws Exception
    {
        this.dao = new GenericDao<Config>(Config.class);
        super.setUp();
    }

    @After
    @Override
    protected void tearDown() throws Exception
    {
        this.dao.closeSession();
        super.tearDown();
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.dataaccess.GenericDao#persist(java.lang.Object)}.
     */
    public void testPersist()
    {
        String key = "persist_test_key";
        String val = "persist_test_val";
    
        Config conf = new Config(key, val);
        conf = this.dao.persist(conf);
        assertEquals(key, conf.getKey());
        assertEquals(val, conf.getValue());
        assertTrue(conf.getId() > 0);
        
        this.dao.closeSession();
        
        Session ses = DataAccessActivator.getNewSession();
        Config loaded = (Config) ses.get(Config.class, conf.getId());
        assertNotNull(loaded);
        assertEquals(conf.getId(), loaded.getId());
        assertEquals(conf.getKey(), loaded.getKey());
        assertEquals(conf.getValue(), loaded.getValue());
        
        ses.beginTransaction();
        ses.delete(loaded);
        ses.getTransaction().commit();
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.dataaccess.GenericDao#get(java.lang.Class, java.io.Serializable)}.
     */
    public void testGet()
    {
        String key = "get_test_key";
        String val = "get_test_val";
        Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        Config c = new Config(key, val);
        ses.save(c);
        ses.getTransaction().commit();
        ses.close();
        
        Config got = this.dao.get(c.getId());
        assertNotNull(got);
        assertEquals(c.getId(), got.getId());
        assertEquals(c.getKey(), got.getKey());
        assertEquals(c.getValue(), got.getValue());
        
        this.dao.delete(got);
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.dataaccess.GenericDao#get(java.lang.Class, java.io.Serializable)}.
     */
    public void testGetNotFound()
    {
        assertNull(this.dao.get(134567890));
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.dataaccess.GenericDao#merge(java.lang.Object)}.
     */
    public void testMerge()
    {
        Config conf = this.dao.persist(new Config("merge_conf_key", "merge_conf_val"));
        this.dao.closeSession();
        
        /* Conf is not transient. */
        GenericDao<Config> cDao = new GenericDao<Config>(Config.class);
        conf = cDao.merge(conf);
        assertNotNull(conf);
        
        conf.setValue("new_val");
        cDao.flush();
        cDao.closeSession();
        
        cDao = new GenericDao<Config>(Config.class);
        conf = cDao.get(conf.getId());
        assertEquals("new_val", conf.getValue());
        cDao.delete(conf);
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.dataaccess.GenericDao#flush()}.
     */
    public void testFlush()
    {
        Config conf = this.dao.persist(new Config("flush_conf_key", "val"));
        assertNotNull(conf);
        
        /* Make dirty. */
        conf.setValue("new_val");
       
        Session ses = this.dao.getSession();
        assertNotNull(ses);
        assertTrue(ses.isDirty());
        
        /* Close which should flush. */
        this.dao.flush();
        assertFalse(ses.isDirty());
        
        ses = DataAccessActivator.getNewSession();
        Config c = (Config)ses.load(Config.class, conf.getId());
        assertEquals("new_val", c.getValue());
        
        ses.getTransaction().begin();
        ses.delete(c);
        ses.getTransaction().commit();
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.dataaccess.GenericDao#delete(java.lang.Object)}.
     */
    public void testDelete()
    {
        Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        Serializable id = ses.save(new Config("delete_conf_key", "delete_conf_val"));
        ses.getTransaction().commit();
        ses.close();
        
        ses = DataAccessActivator.getNewSession();
        assertNotNull(ses.get(Config.class, id));
        ses.close();
        
        this.dao.delete(this.dao.get(id));
        
        ses = DataAccessActivator.getNewSession();
        assertNull(ses.get(Config.class, id));
        ses.close();
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.dataaccess.GenericDao#delete(java.lang.Object)}.
     */
    public void testDeleteId()
    {
        Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        Serializable id = ses.save(new Config("delete_conf_key", "delete_conf_val"));
        ses.getTransaction().commit();
        ses.close();
        
        ses = DataAccessActivator.getNewSession();
        assertNotNull(ses.get(Config.class, id));
        ses.close();
        
        this.dao.delete(id);
        
        ses = DataAccessActivator.getNewSession();
        assertNull(ses.get(Config.class, id));
        ses.close();
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.dataaccess.GenericDao#closeSession()}.
     */
    public void testCloseSession()
    {
        Config conf = this.dao.persist(new Config("close_conf_key", "val"));
        assertNotNull(conf);
        
        /* Make dirty. */
        conf.setValue("new_val");
       
        Session ses = this.dao.getSession();
        assertNotNull(ses);
        assertTrue(ses.isOpen());
        assertTrue(ses.isDirty());
        
        /* Close which should flush. */
        this.dao.closeSession();
        assertFalse(ses.isOpen());
        
        ses = DataAccessActivator.getNewSession();
        Config c = (Config)ses.load(Config.class, conf.getId());
        assertEquals("new_val", c.getValue());
        
        ses.beginTransaction();
        ses.delete(c);
        ses.getTransaction().commit();
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.dataaccess.GenericDao#getSession()}.
     */
    public void testGetSession()
    {
        Session ses = DataAccessActivator.getNewSession();
        GenericDao<Config> cDao = new GenericDao<Config>(ses, Config.class);
        assertEquals(ses, cDao.getSession());
    }

}
