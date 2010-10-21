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
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.impl.StatusTimeoutChecker;

/**
 * Tests the {@link StatusTimeoutChecker} class. 
 */
public class StatusTimeoutCheckerTester extends TestCase
{
    /** Object of class under test. */
    private StatusTimeoutChecker checker;
    
    @Override
    @Before
    public void setUp() throws Exception
    {
        DataAccessTestSetup.setup();
        this.checker = new StatusTimeoutChecker();
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.rigprovider.impl.StatusTimeoutChecker#run()}.
     */
    @Test
    public void testOneTimedOut()
    {
        Session ses = DataAccessActivator.getNewSession();
        RigType type = new RigType("tmType", 180, false);
        new RigTypeDao(ses).persist(type);
        RigCapabilities caps = new RigCapabilities("time,out");
        new RigCapabilitiesDao(ses).persist(caps);
        Rig tmdOut = new Rig(type, caps, "tm1", "http://tm", new Date(System.currentTimeMillis() - 86400001), // A day ago
                true, null, false, true, true);
        tmdOut.setActive(true);
        tmdOut.setOnline(true);
        tmdOut.setOfflineReason(null);
        RigDao rigDao = new RigDao(ses);
        rigDao.persist(tmdOut);
        
        this.checker.run();
        
        ses.refresh(tmdOut);
        assertFalse(tmdOut.isOnline());
        assertEquals("Timed out.", tmdOut.getOfflineReason());
        assertFalse(tmdOut.isActive());
        assertTrue(tmdOut.isManaged());
        
        ses.beginTransaction();
        ses.createQuery("DELETE RigLog WHERE rig='" + tmdOut.getId() + "'").executeUpdate();
        ses.getTransaction().commit();
        
        ses.beginTransaction();
        ses.delete(tmdOut);
        ses.delete(type);
        ses.delete(caps);
        ses.getTransaction().commit();
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.rigprovider.impl.StatusTimeoutChecker#run()}.
     */
    @Test
    public void testNoneTimedOut()
    {
        Session ses = DataAccessActivator.getNewSession();
        RigType type = new RigType("tmType", 180, false);
        new RigTypeDao(ses).persist(type);
        RigCapabilities caps = new RigCapabilities("time,out");
        new RigCapabilitiesDao(ses).persist(caps);
        Rig noTmdOut = new Rig(type, caps, "tm1", "http://tm", new Date(System.currentTimeMillis()), 
                true, null, false, true, true);
        noTmdOut.setActive(true);
        noTmdOut.setOnline(true);
        noTmdOut.setOfflineReason(null);
        RigDao rigDao = new RigDao(ses);
        rigDao.persist(noTmdOut);
        
        this.checker.run();
        
        ses.refresh(noTmdOut);
        assertTrue(noTmdOut.isOnline());
        assertNull(noTmdOut.getOfflineReason());
        assertTrue(noTmdOut.isActive());
        assertTrue(noTmdOut.isManaged());
        
        ses.beginTransaction();
        ses.delete(noTmdOut);
        ses.delete(type);
        ses.delete(caps);
        ses.getTransaction().commit();
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.rigprovider.impl.StatusTimeoutChecker#run()}.
     */
    @Test
    public void testTwoTimedOutOneNotTimedOut()
    {
        Session ses = DataAccessActivator.getNewSession();
        RigType type = new RigType("tmType", 180, false);
        new RigTypeDao(ses).persist(type);
        RigCapabilities caps = new RigCapabilities("time,out");
        new RigCapabilitiesDao(ses).persist(caps);
        
        RigDao rigDao = new RigDao(ses);
        Rig noTmdOut1 = new Rig(type, caps, "tm1", "http://tm", new Date(System.currentTimeMillis() - 295000), // Close  
                true, null, false, true, true);
        rigDao.persist(noTmdOut1);
        Rig tmdOut1 = new Rig(type, caps, "tm2", "http://tm", new Date(System.currentTimeMillis() - 301000),  // Just over
                true, null, false, true, true);
        rigDao.persist(noTmdOut1);
        Rig tmdOut2 = new Rig(type, caps, "tm3", "http://tm", new Date(System.currentTimeMillis() - 1000000), 
                true, null, false, true, true);
        rigDao.persist(noTmdOut1);
        rigDao.persist(tmdOut1);
        rigDao.persist(tmdOut2);
        
        this.checker.run();
        
        ses.refresh(noTmdOut1);
        assertTrue(noTmdOut1.isOnline());
        assertNull(noTmdOut1.getOfflineReason());
        assertTrue(noTmdOut1.isActive());
        assertTrue(noTmdOut1.isManaged());
        
        ses.refresh(tmdOut1);
        assertFalse(tmdOut1.isOnline());
        assertEquals("Timed out.", tmdOut1.getOfflineReason());
        assertFalse(tmdOut1.isActive());
        assertTrue(tmdOut1.isManaged());
        
        ses.refresh(tmdOut2);
        assertFalse(tmdOut2.isOnline());
        assertEquals("Timed out.", tmdOut2.getOfflineReason());
        assertFalse(tmdOut2.isActive());
        assertTrue(tmdOut2.isManaged());
        
        ses.beginTransaction();
        ses.createQuery("DELETE RigLog WHERE rig='" + tmdOut1.getId() + "'").executeUpdate();
        ses.createQuery("DELETE RigLog WHERE rig='" + tmdOut2.getId() + "'").executeUpdate();
        ses.getTransaction().commit();
        
        ses.beginTransaction();
        ses.delete(tmdOut1);
        ses.delete(tmdOut2);
        ses.delete(noTmdOut1);
        ses.delete(type);
        ses.delete(caps);
        ses.getTransaction().commit();
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.rigprovider.impl.StatusTimeoutChecker#run()}.
     */
    @Test
    public void testNoneNoneNoneTimedOut()
    {
        Session ses = DataAccessActivator.getNewSession();
        RigType type = new RigType("tmType", 180, false);
        new RigTypeDao(ses).persist(type);
        RigCapabilities caps = new RigCapabilities("time,out");
        new RigCapabilitiesDao(ses).persist(caps);
        
        RigDao rigDao = new RigDao(ses);
        Rig noTmdOut1 = new Rig(type, caps, "tm1", "http://tm", new Date(System.currentTimeMillis() - 295000), // Close  
                true, null, false, true, true);
        Rig tmdOut1 = new Rig(type, caps, "tm2", "http://tm", new Date(System.currentTimeMillis() - 296000),  // Closer
                true, null, false, true, true);
        Rig tmdOut2 = new Rig(type, caps, "tm3", "http://tm", new Date(System.currentTimeMillis() - 297000), // Closest 
                true, null, false, true, true);
        rigDao.persist(noTmdOut1);
        rigDao.persist(tmdOut1);
        rigDao.persist(tmdOut2);
        
        this.checker.run();
        
        ses.refresh(noTmdOut1);
        assertTrue(noTmdOut1.isOnline());
        assertNull(noTmdOut1.getOfflineReason());
        assertTrue(noTmdOut1.isActive());
        assertTrue(noTmdOut1.isManaged());
        
        ses.refresh(tmdOut1);
        assertTrue(tmdOut1.isOnline());
        assertNull(tmdOut1.getOfflineReason());
        assertTrue(tmdOut1.isActive());
        assertTrue(tmdOut1.isManaged());
        
        ses.refresh(tmdOut2);
        assertTrue(tmdOut2.isOnline());
        assertNull(tmdOut2.getOfflineReason());
        assertTrue(tmdOut2.isActive());
        assertTrue(tmdOut2.isManaged());
        
        ses.beginTransaction();
        ses.delete(tmdOut1);
        ses.delete(tmdOut2);
        ses.delete(noTmdOut1);
        ses.delete(type);
        ses.delete(caps);
        ses.getTransaction().commit();
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.rigprovider.impl.StatusTimeoutChecker#run()}.
     */
    @Test
    public void testOneTimedout()
    {
        Session ses = DataAccessActivator.getNewSession();
        RigType type = new RigType("tmType", 180, false);
        new RigTypeDao(ses).persist(type);
        RigCapabilities caps = new RigCapabilities("time,out");
        new RigCapabilitiesDao(ses).persist(caps);
        
        RigDao rigDao = new RigDao(ses);
        Rig noTmdOut1 = new Rig(type, caps, "tm1", "http://tm", new Date(System.currentTimeMillis() - 295000), // Close  
                true, null, false, true, true);
        Rig tmdOut1 = new Rig(type, caps, "tm2", "http://tm", new Date(System.currentTimeMillis() - 1000000),
                true, null, false, true, true);
        tmdOut1.setActive(false);
        Rig tmdOut2 = new Rig(type, caps, "tm3", "http://tm", new Date(System.currentTimeMillis() - 1000000), 
                true, null, false, true, true);
        rigDao.persist(noTmdOut1);
        rigDao.persist(tmdOut1);
        rigDao.persist(tmdOut2);
        
        this.checker.run();
        
        ses.refresh(noTmdOut1);
        assertTrue(noTmdOut1.isOnline());
        assertNull(noTmdOut1.getOfflineReason());
        assertTrue(noTmdOut1.isActive());
        assertTrue(noTmdOut1.isManaged());
        
        ses.refresh(tmdOut1);
        assertTrue(tmdOut1.isOnline());
        assertNull(tmdOut1.getOfflineReason());
        assertFalse(tmdOut1.isActive());
        assertTrue(tmdOut1.isManaged());
        
        ses.refresh(tmdOut2);
        assertFalse(tmdOut2.isOnline());
        assertEquals("Timed out.", tmdOut2.getOfflineReason());
        assertFalse(tmdOut2.isActive());
        assertTrue(tmdOut2.isManaged());
        
        ses.beginTransaction();
        ses.createQuery("DELETE RigLog WHERE rig='" + tmdOut2.getId() + "'").executeUpdate();
        ses.getTransaction().commit();
        
        ses.beginTransaction();
        ses.delete(tmdOut1);
        ses.delete(tmdOut2);
        ses.delete(noTmdOut1);
        ses.delete(type);
        ses.delete(caps);
        ses.getTransaction().commit();
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.rigprovider.impl.StatusTimeoutChecker#run()}.
     */
    @Test
    public void testDiffTm()
    {
        StatusTimeoutChecker tmChecker = new StatusTimeoutChecker(120);
        
        Session ses = DataAccessActivator.getNewSession();
        RigType type = new RigType("tmType", 180, false);
        new RigTypeDao(ses).persist(type);
        RigCapabilities caps = new RigCapabilities("time,out");
        new RigCapabilitiesDao(ses).persist(caps);
        
        RigDao rigDao = new RigDao(ses);
        Rig noTmdOut1 = new Rig(type, caps, "tm1", "http://tm", new Date(System.currentTimeMillis() - 119000), // Close  
                true, null, false, true, true);
        Rig tmdOut1 = new Rig(type, caps, "tm2", "http://tm", new Date(System.currentTimeMillis() - 10000000),
                true, null, false, true, true);
        tmdOut1.setManaged(false);
        Rig tmdOut2 = new Rig(type, caps, "tm3", "http://tm", new Date(System.currentTimeMillis() - 180000), 
                true, null, false, true, true);
        rigDao.persist(noTmdOut1);
        rigDao.persist(tmdOut1);
        rigDao.persist(tmdOut2);
        
        tmChecker.run();
        
        ses.refresh(noTmdOut1);
        assertTrue(noTmdOut1.isOnline());
        assertNull(noTmdOut1.getOfflineReason());
        assertTrue(noTmdOut1.isActive());
        assertTrue(noTmdOut1.isManaged());
        
        ses.refresh(tmdOut1);
        assertTrue(tmdOut1.isOnline());
        assertNull(tmdOut1.getOfflineReason());
        assertTrue(tmdOut1.isActive());
        assertFalse(tmdOut1.isManaged());
        
        ses.refresh(tmdOut2);
        assertFalse(tmdOut2.isOnline());
        assertEquals("Timed out.", tmdOut2.getOfflineReason());
        assertFalse(tmdOut2.isActive());
        assertTrue(tmdOut2.isManaged());
        
        ses.beginTransaction();
        ses.createQuery("DELETE RigLog WHERE rig='" + tmdOut2.getId() + "'").executeUpdate();
        ses.getTransaction().commit();
        
        ses.beginTransaction();
        ses.delete(tmdOut1);
        ses.delete(tmdOut2);
        ses.delete(noTmdOut1);
        ses.delete(type);
        ses.delete(caps);
        ses.getTransaction().commit();
    }
    
    @Test
    public void testNotRigs() throws Exception
    {
        /* Check to ensure not exceptions are thrown when no rigs are present. */
        this.checker.run();
    }

}
