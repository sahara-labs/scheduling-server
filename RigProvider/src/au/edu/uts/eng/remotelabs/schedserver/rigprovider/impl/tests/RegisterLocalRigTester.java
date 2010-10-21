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
 * @date 18th February 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.rigprovider.impl.tests;

import java.util.Date;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigCapabilitiesDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigTypeDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.testsetup.DataAccessTestSetup;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.impl.RegisterLocalRig;

/**
 * Tests the {@link RegisterLocalRig} class.
 */
public class RegisterLocalRigTester extends TestCase
{
    /** Object of class under test. */
    private RegisterLocalRig register;
    
    @Override
    @Before
    public void setUp() throws Exception
    {
        DataAccessTestSetup.setup();
        this.register = new RegisterLocalRig();
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.rigprovider.impl.RegisterLocalRig#registerRig(java.lang.String, java.lang.String, java.lang.String, java.lang.String)}.
     */
    @Test
    public void testAddRigToSchedServerAllNew()
    {
        Session ses = DataAccessActivator.getNewSession();
        
        String name = "register1";
        String type = "registertest";
        String caps = "a,b,c,d,e,f";
        String contactUrl = "http://lbremote1.eng.uts.edu.au:7070/services/RigClientService";
        
        assertTrue(this.register.registerRig(name, type, caps, contactUrl));
        assertNull(this.register.getFailedReason());
        
        Rig rig = this.register.getRegisteredRig();
        assertNotNull(rig);
        this.register.getSession().close();
        
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
        
        ses.beginTransaction();
        ses.createQuery("DELETE FROM RigLog WHERE rig='" + rig.getId() + "'").executeUpdate();
        ses.getTransaction().commit();
        
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
        
        String name = "register1";
        String type = "registertest";
        String caps = "a,b,c,d,e,f";
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
        ses.createQuery("DELETE FROM RigLog WHERE rig='" + rig.getId() + "'").executeUpdate();
        ses.getTransaction().commit();
        
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
        
        String name = "register1";
        String type = "registertest";
        String caps = "a,b,c,d,e,f";
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
        ses.createQuery("DELETE FROM RigLog WHERE rig='" + rig.getId() + "'").executeUpdate();
        ses.getTransaction().commit();
        
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
        
        String name = "register1";
        String type = "registertest";
        String caps = "a,b,c,d,e,f";
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
        ses.createQuery("DELETE FROM RigLog WHERE rig='" + rig.getId() + "'").executeUpdate();
        ses.getTransaction().commit();
        
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
        
        String name = "register1";
        String type = "registertest";
        String caps = "a,b,c,d,e,f";
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

        /* Cleanup. */
        ses.beginTransaction();
        ses.createQuery("DELETE FROM RigLog WHERE rig='" + rigRec.getId() + "'").executeUpdate();
        ses.getTransaction().commit();
        
        ses.beginTransaction();        
        ses.delete(rigRec);
        ses.delete(capsRec);
        ses.delete(typeRec);
        ses.getTransaction().commit();
        
        assertEquals("Exists", this.register.getFailedReason());
    }

}
