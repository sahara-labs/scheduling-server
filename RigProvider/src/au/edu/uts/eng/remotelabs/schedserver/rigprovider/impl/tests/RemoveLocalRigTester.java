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
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.impl.RemoveLocalRig;

/**
 * Tests the {@link RemoveLocalRig} class.
 */
public class RemoveLocalRigTester extends TestCase
{
    /** Object of class under test. */
    private RemoveLocalRig remove;
    
    @Override
    @Before
    public void setUp() throws Exception
    {
        DataAccessTestSetup.setup();
        this.remove = new RemoveLocalRig();
    }
    
    @Test
    public void testRemoveRig()
    {
        Session ses = DataAccessActivator.getNewSession();
        RigType type = new RigType("rlrType", 180, false);
        new RigTypeDao(ses).persist(type);
        RigCapabilities caps = new RigCapabilities("rlrA,rlrB");
        new RigCapabilitiesDao(ses).persist(caps);
        Rig rig = new Rig(type, caps, "rlr", "http://rlr", new Date(System.currentTimeMillis() - 86400001), // A day ago
                true, null, false, false, true);
        rig.setActive(true);
        rig.setOnline(true);
        rig.setOfflineReason(null);
        new RigDao(ses).persist(rig);
        
        assertTrue(this.remove.removeRig("rlr", "Shutting down."));
        
        ses.refresh(rig);
        assertFalse(rig.isActive());
        assertFalse(rig.isOnline());
        assertNull(rig.getContactUrl());
        assertNotNull(rig.getOfflineReason());
        assertEquals("Shutting down.", rig.getOfflineReason());
        
        Date ts = rig.getLastUpdateTimestamp();
        assertNotNull(ts);
        assertTrue(new Date().after(ts));
        assertTrue(new Date(System.currentTimeMillis() - 5000).before(ts));
        
        ses.beginTransaction();
        ses.createQuery("DELETE FROM RigLog WHERE rig='" + rig.getId() + "'").executeUpdate();
        ses.getTransaction().commit();
        
        ses.beginTransaction();
        ses.delete(rig);
        ses.delete(type);
        ses.delete(caps);
        ses.getTransaction().commit();
    }
    
    @Test
    public void testRemoveRigAlreadyInActive()
    {
        Session ses = DataAccessActivator.getNewSession();
        RigType type = new RigType("rlrType", 180, false);
        new RigTypeDao(ses).persist(type);
        RigCapabilities caps = new RigCapabilities("rlrA,rlrB");
        new RigCapabilitiesDao(ses).persist(caps);
        Rig rig = new Rig(type, caps, "rlr", "http://rlr", new Date(System.currentTimeMillis() - 86400001), // A day ago
                true, null, false, false, true);
        rig.setActive(false);
        rig.setOnline(true);
        rig.setOfflineReason("foo bar");
        new RigDao(ses).persist(rig);
        
        assertTrue(this.remove.removeRig("rlr", "Shutting down."));
        
        ses.refresh(rig);
        assertFalse(rig.isActive());
        assertFalse(rig.isOnline());
        assertNull(rig.getContactUrl());
        assertNotNull(rig.getOfflineReason());
        assertEquals("Shutting down.", rig.getOfflineReason());
        
        Date ts = rig.getLastUpdateTimestamp();
        assertNotNull(ts);
        assertTrue(new Date().after(ts));
        assertTrue(new Date(System.currentTimeMillis() - 5000).before(ts));
        
        ses.beginTransaction();
        ses.createQuery("DELETE FROM RigLog WHERE rig='" + rig.getId() + "'").executeUpdate();
        ses.getTransaction().commit();
        
        ses.beginTransaction();
        ses.delete(rig);
        ses.delete(type);
        ses.delete(caps);
        ses.getTransaction().commit();
    }
    
    @Test
    public void testRemoveRigNoRig()
    {
        assertFalse(this.remove.removeRig("removedRig", "Because I said so."));
        assertNotNull(this.remove.getFailedReason());
        assertEquals("Not registered", this.remove.getFailedReason());
    }

}
