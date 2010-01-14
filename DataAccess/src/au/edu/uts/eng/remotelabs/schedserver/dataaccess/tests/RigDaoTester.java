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
 * @date 11th January 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.dataaccess.tests;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.hibernate.cfg.AnnotationConfiguration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.RigDao;
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
 * Tests the {@link RigDao} class.
 */
public class RigDaoTester extends TestCase
{
    /** Object of class under test. */
    private RigDao dao;
    
    public RigDaoTester(String name) throws Exception
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
    public void setUp() throws Exception
    {
        this.dao = new RigDao();
        super.setUp();
    }

    @Override
    @After
    public void tearDown() throws Exception
    {
        this.dao.closeSession();
        super.tearDown();
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.dataaccess.RigDao#findByName(java.lang.String)}.
     */
    @Test
    public void testFindByName()
    {
        /* Add a rig. */
        Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        RigType type = new RigType();
        type.setName("rig_test");
        type.setLogoffGraceDuration(600);
        ses.save(type);
        
        RigCapabilities caps = new RigCapabilities("a,b,c,d,e,f");
        ses.save(caps);
        
        Rig rig = new Rig();
        rig.setName("rig_name_test");
        rig.setRigType(type);
        rig.setContactUrl("http://url");
        rig.setRigCapabilities(caps);
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        ts.setNanos(0); // Need to trunc time as nanoseconds aren't stored in the DB.
        rig.setLastUpdateTimestamp(ts);
        rig.setManaged(false);
        rig.setMeta("iLabs");
        ses.save(rig);
        ses.getTransaction().commit();
        ses.close();
        
        /* Find the rig. */
        Rig fRig = this.dao.findByName("rig_name_test");
        assertNotNull(fRig);
        assertEquals(rig.getId(), fRig.getId());
        assertEquals(rig.getContactUrl(), fRig.getContactUrl());
        assertEquals(fRig.getLastUpdateTimestamp(), rig.getLastUpdateTimestamp());
        
        assertEquals(rig.getRigType().getName(), fRig.getRigType().getName());
        assertEquals(rig.getRigCapabilities().getCapabilities(), fRig.getRigCapabilities().getCapabilities());
        assertEquals(rig.getManaged(), fRig.getManaged());
        assertEquals(rig.getMeta(), fRig.getMeta());
        
        /* Delete all. */
        this.dao.delete(fRig);
        ses = this.dao.getSession();
        ses.beginTransaction();
        ses.delete(fRig.getRigCapabilities());
        ses.delete(fRig.getRigType());
        ses.getTransaction().commit();
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.dataaccess.RigDao#findFreeinType(au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType)}.
     */
    @Test
    public void testFindFreeinType()
    {
        /* Add a rig. */
        long ids[] = new long[5];
        Session ses = this.dao.getSession();
        ses.beginTransaction();
        RigType type = new RigType();
        type.setName("rig_test");
        type.setLogoffGraceDuration(600);
        ses.save(type);
        
        RigCapabilities caps = new RigCapabilities("a,b,c,d,e,f");
        ses.save(caps);
        ses.getTransaction().commit();
        
        Rig rig = new Rig();
        rig.setName("rig_name_test_1");
        rig.setRigType(type);
        rig.setContactUrl("http://url");
        rig.setRigCapabilities(caps);
        Timestamp ts = new Timestamp(System.currentTimeMillis());
        ts.setNanos(0); // Need to trunc time as nanoseconds aren't stored in the DB.
        rig.setLastUpdateTimestamp(ts);
        rig.setOnline(true);
        rig.setActive(true);
        rig.setManaged(true);
        this.dao.persist(rig);
        ids[0] = rig.getId();

        rig = new Rig();
        rig.setName("rig_name_test_2");
        rig.setRigType(type);
        rig.setContactUrl("http://url");
        rig.setRigCapabilities(caps);
        rig.setLastUpdateTimestamp(ts);
        rig.setOnline(true);
        rig.setActive(true);
        rig.setManaged(true);
        this.dao.persist(rig);
        ids[1] = rig.getId();
        
        rig = new Rig();
        rig.setName("rig_name_test_3");
        rig.setRigType(type);
        rig.setContactUrl("http://url");
        rig.setRigCapabilities(caps);
        rig.setLastUpdateTimestamp(ts);
        rig.setActive(true);
        rig.setOnline(true);
        rig.setInSession(true);
        this.dao.persist(rig);
        ids[2] = rig.getId();
        
        rig = new Rig();
        rig.setName("rig_name_test_4");
        rig.setRigType(type);
        rig.setContactUrl("http://url");
        rig.setRigCapabilities(caps);
        rig.setLastUpdateTimestamp(ts);
        rig.setOnline(true);
        rig.setInSession(true);
        this.dao.persist(rig);
        ids[3] = rig.getId();
        
        rig = new Rig();
        rig.setName("rig_name_test_5");
        rig.setRigType(type);
        rig.setContactUrl("http://url");
        rig.setRigCapabilities(caps);
        rig.setLastUpdateTimestamp(ts);
        rig.setActive(true);
        rig.setInSession(true);
        this.dao.persist(rig);
        ids[4] = rig.getId();
        
        this.dao.closeSession();
        this.dao = new RigDao();

        
        List<Rig> free = this.dao.findFreeinType(type);
        assertNotNull(free);
        assertEquals(2, free.size());
        Rig fr = free.get(0);
        assertTrue(fr.isActive());
        assertTrue(fr.isOnline());
        assertFalse(fr.isInSession());
        assertTrue(fr.getManaged());
        assertNull(fr.getMeta());
        fr = free.get(1);
        assertTrue(fr.isActive());
        assertTrue(fr.isOnline());
        assertFalse(fr.isInSession());
        assertTrue(fr.getManaged());
        assertNull(fr.getMeta());
        
        List<String> names = new ArrayList<String>(2);
        for (Rig r : free)
        {
            names.add(r.getName());
        }
        assertTrue(names.contains("rig_name_test_1"));
        assertTrue(names.contains("rig_name_test_2"));
        
        ses = this.dao.getSession();
        ses.beginTransaction();
        for (long i : ids)
        {
            ses.createSQLQuery("DELETE FROM rig WHERE id=" + i).executeUpdate();
        }
        ses.createSQLQuery("DELETE FROM rig_capabilities WHERE id=" + caps.getId()).executeUpdate();
        ses.createSQLQuery("DELETE FROM rig_type WHERE id=" + type.getId()).executeUpdate();
        ses.getTransaction().commit();
        
        
    }

}
