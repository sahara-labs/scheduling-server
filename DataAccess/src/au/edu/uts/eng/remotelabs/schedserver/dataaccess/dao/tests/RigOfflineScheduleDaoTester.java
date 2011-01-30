/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2011, University of Technology, Sydney
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
 * @date 30th January 2011
 */
package au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.tests;

import java.util.Date;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigOfflineScheduleDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigOfflineSchedule;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.testsetup.DataAccessTestSetup;

/**
 * Tests the {@link RigOfflineScheduleDao} class.
 */
public class RigOfflineScheduleDaoTester extends TestCase
{
    /** Class under test. */
    private RigOfflineScheduleDao dao;


    @Override
    @Before
    public void setUp() throws Exception
    {
        DataAccessTestSetup.setup();
        this.dao = new RigOfflineScheduleDao();
        super.setUp();
    }

    @Test
    public void testIsOffline()
    {
        Session ses = this.dao.getSession();
        ses.beginTransaction();
        RigType rt = new RigType();
        rt.setName("offlineschedtype");
        ses.save(rt);
        RigCapabilities caps = new RigCapabilities("offline,schedul,test");
        ses.save(caps);
        Rig r = new Rig();
        r.setName("offlineschedtest");
        r.setLastUpdateTimestamp(new Date());
        r.setRigCapabilities(caps);
        r.setRigType(rt);
        ses.save(r);
        RigOfflineSchedule ro = new RigOfflineSchedule();
        ro.setStartTime(new Date(System.currentTimeMillis() - 60000));
        ro.setEndTime(new Date(System.currentTimeMillis() + 60000));
        ro.setRig(r);
        ro.setReason("testcase");
        ses.save(ro);
        ses.getTransaction().commit();
        
        boolean off = this.dao.isOffline(r);
        
        ses.beginTransaction();
        ses.delete(ro);
        ses.delete(r);
        ses.delete(rt);
        ses.delete(caps);
        ses.getTransaction().commit();
        
        assertTrue(off);
    }
    
    @Test
    public void testIsOfflineBefore()
    {
        Session ses = this.dao.getSession();
        ses.beginTransaction();
        RigType rt = new RigType();
        rt.setName("offlineschedtype");
        ses.save(rt);
        RigCapabilities caps = new RigCapabilities("offline,schedul,test");
        ses.save(caps);
        Rig r = new Rig();
        r.setName("offlineschedtest");
        r.setLastUpdateTimestamp(new Date());
        r.setRigCapabilities(caps);
        r.setRigType(rt);
        ses.save(r);
        RigOfflineSchedule ro = new RigOfflineSchedule();
        ro.setStartTime(new Date(System.currentTimeMillis() - 120000));
        ro.setEndTime(new Date(System.currentTimeMillis() - 60000));
        ro.setRig(r);
        ro.setReason("testcase");
        ses.save(ro);
        ses.getTransaction().commit();
        
        boolean off = this.dao.isOffline(r);
        
        ses.beginTransaction();
        ses.delete(ro);
        ses.delete(r);
        ses.delete(rt);
        ses.delete(caps);
        ses.getTransaction().commit();
        
        assertFalse(off);
    }
    
    @Test
    public void testIsOfflineAfter()
    {
        Session ses = this.dao.getSession();
        ses.beginTransaction();
        RigType rt = new RigType();
        rt.setName("offlineschedtype");
        ses.save(rt);
        RigCapabilities caps = new RigCapabilities("offline,schedul,test");
        ses.save(caps);
        Rig r = new Rig();
        r.setName("offlineschedtest");
        r.setLastUpdateTimestamp(new Date());
        r.setRigCapabilities(caps);
        r.setRigType(rt);
        ses.save(r);
        RigOfflineSchedule ro = new RigOfflineSchedule();
        ro.setStartTime(new Date(System.currentTimeMillis() + 60000));
        ro.setEndTime(new Date(System.currentTimeMillis() + 1200000));
        ro.setRig(r);
        ro.setReason("testcase");
        ses.save(ro);
        ses.getTransaction().commit();
        
        boolean off = this.dao.isOffline(r);
        
        ses.beginTransaction();
        ses.delete(ro);
        ses.delete(r);
        ses.delete(rt);
        ses.delete(caps);
        ses.getTransaction().commit();
        
        assertFalse(off);
    }

    @Test
    public void testIsOfflineNot()
    {
        Session ses = this.dao.getSession();
        ses.beginTransaction();
        RigType rt = new RigType();
        rt.setName("offlineschedtype");
        ses.save(rt);
        RigCapabilities caps = new RigCapabilities("offline,schedul,test");
        ses.save(caps);
        Rig r = new Rig();
        r.setName("offlineschedtest");
        r.setLastUpdateTimestamp(new Date());
        r.setRigCapabilities(caps);
        r.setRigType(rt);
        ses.save(r);
        ses.getTransaction().commit();
        
        boolean off = this.dao.isOffline(r);
        
        ses.beginTransaction();
        ses.delete(r);
        ses.delete(rt);
        ses.delete(caps);
        ses.getTransaction().commit();
        
        assertFalse(off);
    }

}
