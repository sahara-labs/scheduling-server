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
package au.edu.uts.eng.remotelabs.schedserver.rigprovider.impl.tests;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Properties;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.hibernate.cfg.AnnotationConfiguration;
import org.junit.Before;
import org.junit.Test;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigCapabilitiesDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigTypeDao;
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
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.impl.RegisterLocalRig;

/**
 * Tests the {@link RegisterLocalRig} class.
 */
public class RegisterLocalRigTester extends TestCase
{
    /** Object of class under test. */
    private RegisterLocalRig register;

    public RegisterLocalRigTester(String name) throws Exception
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
        this.register = new RegisterLocalRig();
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.rigprovider.impl.RegisterLocalRig#registerRig(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testAddRigToSchedServerAllNew()
    {
        Session ses = DataAccessActivator.getNewSession();
        
        String name = "lb1";
        String type = "loadedbeam";
        String caps = "cantilever,horizbean,vertbeam";
        String contactUrl = "http://lbremote1.eng.uts.edu.au:7070/services/RigClientService";
        
        assertTrue(this.register.registerRig(name, type, caps, contactUrl));
        assertNull(this.register.getFailedReason());
        
        Rig rig = this.register.getRegisteredRig();
        assertNotNull(rig);
        
        assertEquals(name, rig.getName());
        assertEquals(type, rig.getRigType().getName());
        assertEquals(contactUrl, rig.getContactUrl());
        assertNotNull(rig.getRigCapabilities());
        
        /* Default values. */
        assertTrue(rig.isActive());
        assertFalse(rig.isInSession());
        assertFalse(rig.isOnline());
        assertTrue(rig.isManaged());
        assertNotNull(rig.getOfflineReason());
        assertNull(rig.getMeta());
        assertNull(rig.getSession());

        assertTrue(new Date(System.currentTimeMillis()).after(rig.getLastUpdateTimestamp()));
        assertTrue(new Date(System.currentTimeMillis() - 10000).before(rig.getLastUpdateTimestamp()));
        
        /* Test caps record was created and added. */
        RigCapabilities capsRec = new RigCapabilitiesDao(ses).findCapabilites(caps);
        assertNotNull(capsRec);
        assertEquals(caps, capsRec.getCapabilities());
        
        /* Test type record was created and added. */
        RigType typeRec = new RigTypeDao(ses).findByName(type);
        assertNotNull(typeRec);
        assertEquals(type, typeRec.getName());
        assertEquals(180, typeRec.getLogoffGraceDuration());
        assertFalse(typeRec.isCodeAssignable());
        
        /* Test rig record was created and added. */
        Rig rigRec = new RigDao(ses).findByName(name);
        assertNotNull(rigRec);
        assertNotNull(rigRec.getId());
        assertEquals(name, rigRec.getName());
        assertEquals(type, rigRec.getRigType().getName());
        assertTrue(rigRec.isActive());
        assertEquals(contactUrl, rigRec.getContactUrl());
        assertFalse(rigRec.isInSession());
        assertNotNull(rigRec.getLastUpdateTimestamp());
        assertTrue(rigRec.isManaged());
        assertNull(rigRec.getMeta());
        assertNotNull(rigRec.getOfflineReason());
        assertFalse(rigRec.isOnline());
        assertEquals(caps, rigRec.getRigCapabilities().getCapabilities());
        
        assertTrue(new Date(System.currentTimeMillis()).after(rig.getLastUpdateTimestamp()));
        assertTrue(new Date(System.currentTimeMillis() - 10000).before(rig.getLastUpdateTimestamp()));
        
        /* Cleanup. */
        ses.beginTransaction();
        ses.delete(rigRec);
        ses.delete(capsRec);
        ses.delete(typeRec);
        ses.getTransaction().commit(); 
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.rigprovider.impl.RegisterLocalRig#registerRig(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testAddRigToSchedServerNewRigExistingTypeNewCaps()
    {   
        Session ses = DataAccessActivator.getNewSession();
        
        String name = "lb1";
        String type = "loadedbeam";
        String caps = "cantilever,horizbean,vertbeam";
        String contactUrl = "http://lbremote1.eng.uts.edu.au:7070/services/RigClientService";
        
        RigType existType = new RigType(type, 180, false);
        ses.beginTransaction();
        ses.save(existType);
        ses.getTransaction().commit();
        ses.flush();
        ses.evict(existType);
        
        assertTrue(this.register.registerRig(name, type, caps, contactUrl));
        assertNull(this.register.getFailedReason());
        
        Rig rig = this.register.getRegisteredRig();
        assertNotNull(rig);
        
        assertEquals(name, rig.getName());
        assertEquals(type, rig.getRigType().getName());
        assertEquals(contactUrl, rig.getContactUrl());
        assertNotNull(rig.getRigCapabilities());
        
        /* Default values. */
        assertTrue(rig.isActive());
        assertFalse(rig.isInSession());
        assertFalse(rig.isOnline());
        assertTrue(rig.isManaged());
        assertNotNull(rig.getOfflineReason());
        assertNull(rig.getMeta());
        assertNull(rig.getSession());

        assertTrue(new Date(System.currentTimeMillis()).after(rig.getLastUpdateTimestamp()));
        assertTrue(new Date(System.currentTimeMillis() - 10000).before(rig.getLastUpdateTimestamp()));
        
        /* Test caps record was created and added. */
        RigCapabilities capsRec = new RigCapabilitiesDao(ses).findCapabilites(caps);
        assertNotNull(capsRec);
        assertEquals(caps, capsRec.getCapabilities());
        
        /* Test type record was created and added. */
        RigType typeRec = new RigTypeDao(ses).findByName(type);
        assertNotNull(typeRec);
        assertEquals(type, typeRec.getName());
        assertEquals(180, typeRec.getLogoffGraceDuration());
        assertFalse(typeRec.isCodeAssignable());
        
        /* Test rig record was created and added. */
        Rig rigRec = new RigDao(ses).findByName(name);
        assertNotNull(rigRec);
        assertNotNull(rigRec.getId());
        assertEquals(name, rigRec.getName());
        assertEquals(type, rigRec.getRigType().getName());
        assertTrue(rigRec.isActive());
        assertEquals(contactUrl, rigRec.getContactUrl());
        assertFalse(rigRec.isInSession());
        assertNotNull(rigRec.getLastUpdateTimestamp());
        assertTrue(rigRec.isManaged());
        assertNull(rigRec.getMeta());
        assertNotNull(rigRec.getOfflineReason());
        assertFalse(rigRec.isOnline());
        assertEquals(caps, rigRec.getRigCapabilities().getCapabilities());
        
        assertTrue(new Date(System.currentTimeMillis()).after(rigRec.getLastUpdateTimestamp()));
        assertTrue(new Date(System.currentTimeMillis() - 10000).before(rigRec.getLastUpdateTimestamp()));
        
        /* Cleanup. */
        ses.beginTransaction();
        ses.delete(rigRec);
        ses.delete(capsRec);
        ses.delete(typeRec);
        ses.getTransaction().commit(); 
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.rigprovider.impl.RegisterLocalRig#registerRig(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testAddRigToSchedServerNewRigExistingTypeExistingCaps()
    {   
        Session ses = DataAccessActivator.getNewSession();
        
        String name = "lb1";
        String type = "loadedbeam";
        String caps = "cantilever,horizbean,vertbeam";
        String contactUrl = "http://lbremote1.eng.uts.edu.au:7070/services/RigClientService";
        
        RigType existType = new RigType(type, 180, false);
        RigCapabilities existCaps = new RigCapabilities(caps);
        ses.beginTransaction();
        ses.save(existType);
        ses.save(existCaps);
        ses.getTransaction().commit();
        ses.flush();
        ses.evict(existType);
        
        assertTrue(this.register.registerRig(name, type, caps, contactUrl));
        assertNull(this.register.getFailedReason());
        
        
        Rig rig = this.register.getRegisteredRig();
        assertNotNull(rig);
        
        assertEquals(name, rig.getName());
        assertEquals(type, rig.getRigType().getName());
        assertEquals(contactUrl, rig.getContactUrl());
        assertNotNull(rig.getRigCapabilities());
        
        /* Default values. */
        assertTrue(rig.isActive());
        assertFalse(rig.isInSession());
        assertFalse(rig.isOnline());
        assertTrue(rig.isManaged());
        assertNotNull(rig.getOfflineReason());
        assertNull(rig.getMeta());
        assertNull(rig.getSession());

        assertTrue(new Date(System.currentTimeMillis()).after(rig.getLastUpdateTimestamp()));
        assertTrue(new Date(System.currentTimeMillis() - 10000).before(rig.getLastUpdateTimestamp()));
        
        /* Test caps record was created and added. */
        RigCapabilities capsRec = new RigCapabilitiesDao(ses).findCapabilites(caps);
        assertNotNull(capsRec);
        assertEquals(caps, capsRec.getCapabilities());
        
        /* Test type record was created and added. */
        RigType typeRec = new RigTypeDao(ses).findByName(type);
        assertNotNull(typeRec);
        assertEquals(type, typeRec.getName());
        assertEquals(180, typeRec.getLogoffGraceDuration());
        assertFalse(typeRec.isCodeAssignable());
        
        /* Test rig record was created and added. */
        Rig rigRec = new RigDao(ses).findByName(name);
        assertNotNull(rigRec);
        assertNotNull(rigRec.getId());
        assertEquals(name, rigRec.getName());
        assertEquals(type, rigRec.getRigType().getName());
        assertTrue(rigRec.isActive());
        assertEquals(contactUrl, rigRec.getContactUrl());
        assertFalse(rigRec.isInSession());
        assertNotNull(rigRec.getLastUpdateTimestamp());
        assertTrue(rigRec.isManaged());
        assertNull(rigRec.getMeta());
        assertNotNull(rigRec.getOfflineReason());
        assertFalse(rigRec.isOnline());
        assertEquals(caps, rigRec.getRigCapabilities().getCapabilities());
        
        assertTrue(new Date(System.currentTimeMillis()).after(rigRec.getLastUpdateTimestamp()));
        assertTrue(new Date(System.currentTimeMillis() - 10000).before(rigRec.getLastUpdateTimestamp()));
        
        /* Cleanup. */
        ses.beginTransaction();
        ses.delete(rigRec);
        ses.delete(capsRec);
        ses.delete(typeRec);
        ses.getTransaction().commit(); 
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.rigprovider.impl.RegisterLocalRig#registerRig(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testAddRigToSchedServerInactiveRig()
    {   
        Session ses = DataAccessActivator.getNewSession();
        
        String name = "lb1";
        String type = "loadedbeam";
        String caps = "cantilever,horizbean,vertbeam";
        String contactUrl = "http://lbremote1.eng.uts.edu.au:7070/services/RigClientService";
        
        RigType existType = new RigType(type, 180, false);
        RigCapabilities existCaps = new RigCapabilities(caps);
        ses.beginTransaction();
        ses.save(existType);
        ses.save(existCaps);
        ses.getTransaction().commit();
        ses.flush();
        ses.evict(existType);
        ses.evict(existCaps);
        
        Rig existRig = new Rig(existType, existCaps, name, contactUrl, 
                new Date(), false, "Broken.", false, true, false);
        ses.beginTransaction();
        ses.save(existRig);
        ses.getTransaction().commit();
        ses.flush();
        ses.evict(existRig);
        
        assertTrue(this.register.registerRig(name, type, caps, contactUrl));
        assertNull(this.register.getFailedReason());
        
        Rig rig = this.register.getRegisteredRig();
        assertNotNull(rig);
        
        assertEquals(name, rig.getName());
        assertEquals(type, rig.getRigType().getName());
        assertEquals(contactUrl, rig.getContactUrl());
        assertNotNull(rig.getRigCapabilities());
        
        /* Default values. */
        assertTrue(rig.isActive());
        assertFalse(rig.isInSession());
        assertFalse(rig.isOnline());
        assertTrue(rig.isManaged());
        assertNotNull(rig.getOfflineReason());
        assertNull(rig.getMeta());
        assertNull(rig.getSession());

        assertTrue(new Date(System.currentTimeMillis()).after(rig.getLastUpdateTimestamp()));
        assertTrue(new Date(System.currentTimeMillis() - 10000).before(rig.getLastUpdateTimestamp()));
        
        /* Test caps record was created and added. */
        RigCapabilities capsRec = new RigCapabilitiesDao(ses).findCapabilites(caps);
        assertNotNull(capsRec);
        assertEquals(caps, capsRec.getCapabilities());
        
        /* Test type record was created and added. */
        RigType typeRec = new RigTypeDao(ses).findByName(type);
        assertNotNull(typeRec);
        assertEquals(type, typeRec.getName());
        assertEquals(180, typeRec.getLogoffGraceDuration());
        assertFalse(typeRec.isCodeAssignable());
        
        /* Test rig record was created and added. */
        Rig rigRec = new RigDao(ses).findByName(name);
        assertNotNull(rigRec);
        assertNotNull(rigRec.getId());
        assertEquals(name, rigRec.getName());
        assertEquals(type, rigRec.getRigType().getName());
        assertTrue(rigRec.isActive());
        assertEquals(contactUrl, rigRec.getContactUrl());
        assertFalse(rigRec.isInSession());
        assertNotNull(rigRec.getLastUpdateTimestamp());
        assertTrue(rigRec.isManaged());
        assertNull(rigRec.getMeta());
        assertNotNull(rigRec.getOfflineReason());
        assertFalse(rigRec.isOnline());
        assertEquals(caps, rigRec.getRigCapabilities().getCapabilities());
        
        assertTrue(new Date(System.currentTimeMillis()).after(rigRec.getLastUpdateTimestamp()));
        assertTrue(new Date(System.currentTimeMillis() - 10000).before(rigRec.getLastUpdateTimestamp()));
        
        /* Cleanup. */
        ses.beginTransaction();
        ses.delete(rigRec);
        ses.delete(capsRec);
        ses.delete(typeRec);
        ses.getTransaction().commit(); 
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.rigprovider.impl.RegisterLocalRig#registerRig(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testAddRigToSchedServerActiveRig()
    {   
        Session ses = DataAccessActivator.getNewSession();
        
        String name = "lb1";
        String type = "loadedbeam";
        String caps = "cantilever,horizbean,vertbeam";
        String contactUrl = "http://lbremote1.eng.uts.edu.au:7070/services/RigClientService";
        
        RigType existType = new RigType(type, 180, false);
        RigCapabilities existCaps = new RigCapabilities(caps);
        ses.beginTransaction();
        ses.save(existType);
        ses.save(existCaps);
        ses.getTransaction().commit();
        ses.flush();
        ses.evict(existType);
        ses.evict(existCaps);
        
        Rig existRig = new Rig(existType, existCaps, name, contactUrl, 
                new Date(), false, "Broken.", false, true, true);
        ses.beginTransaction();
        ses.save(existRig);
        ses.getTransaction().commit();
        ses.flush();
        ses.evict(existRig);
 
        assertFalse(this.register.registerRig(name, type, caps, contactUrl));
        
        RigCapabilities capsRec = new RigCapabilitiesDao(ses).findCapabilites(caps);
        RigType typeRec = new RigTypeDao(ses).findByName(type);
        Rig rigRec = new RigDao(ses).findByName(name);
        ses.beginTransaction();
        ses.delete(rigRec);
        ses.delete(capsRec);
        ses.delete(typeRec);
        ses.getTransaction().commit();
        
        assertEquals("Exists", this.register.getFailedReason());
    }

}
