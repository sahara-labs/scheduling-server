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
 * @date 31st May 2010
 */
package io.rln.ss.data.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;

import io.rln.ss.data.DataActivator;
import io.rln.ss.data.dao.RigDao;
import io.rln.ss.data.dao.RigLogDao;
import io.rln.ss.data.entities.Rig;
import io.rln.ss.data.entities.RigCapabilities;
import io.rln.ss.data.entities.RigLog;
import io.rln.ss.data.entities.RigType;
import io.rln.ss.data.testsetup.DataAccessTestSetup;

/**
 * Tests the {@link RigDao} class.
 */
public class RigLogDaoTester extends TestCase
{
    /** Object of class under test. */
    private RigLogDao dao;

    @Override
    @Before
    public void setUp() throws Exception
    {
        DataAccessTestSetup.setup();
        this.dao = new RigLogDao();
    }
    
    /**
     * Test method for {@link io.rln.ss.data.dao.RigLogDao#addRegisteredLog()}
     */
    @Test
    public void testAddRegisteredLog()
    {
        Date now = new Date();

        Session ses = DataActivator.getNewSession();
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
        rig.setLastUpdateTimestamp(now);
        rig.setManaged(false);
        rig.setMeta("iLabs");
        rig.setOnline(true);
        ses.save(rig);
        ses.getTransaction().commit();
        
        RigLogDao dao = new RigLogDao(ses);
        RigLog log = dao.addRegisteredLog(rig, "Newly registered");
        assertNotNull(log);
        
        Session ses2 = DataActivator.getNewSession();
        RigLog loaded = (RigLog) ses2.load(RigLog.class, log.getId());
        assertNotNull(loaded);
        
        assertEquals(RigLog.NOT_REGISTERED, loaded.getOldState());
        assertEquals(RigLog.ONLINE, loaded.getNewState());
        assertEquals("Newly registered", loaded.getReason());
        assertEquals(Math.floor(now.getTime() / 1000), Math.floor(loaded.getTimeStamp().getTime() / 1000));
        assertEquals(rig.getId().longValue(), loaded.getRig().getId().longValue());
        
        ses.beginTransaction();
        ses.delete(log);
        ses.delete(rig);
        ses.delete(type);
        ses.delete(caps);
        ses.getTransaction().commit();
        ses.close();
        ses2.close();
    }
    
    /**
     * Test method for {@link io.rln.ss.data.dao.RigLogDao#addOnlineLog()}
     */
    @Test
    public void testAddOnlineLog()
    {
        Date now = new Date();

        Session ses = DataActivator.getNewSession();
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
        rig.setLastUpdateTimestamp(now);
        rig.setManaged(false);
        rig.setMeta("iLabs");
        rig.setOnline(true);
        ses.save(rig);
        ses.getTransaction().commit();
        
        RigLogDao dao = new RigLogDao(ses);
        RigLog log = dao.addOnlineLog(rig, "Fixed");
        assertNotNull(log);
        
        Session ses2 = DataActivator.getNewSession();
        RigLog loaded = (RigLog) ses2.load(RigLog.class, log.getId());
        assertNotNull(loaded);
        
        assertEquals(RigLog.OFFLINE, loaded.getOldState());
        assertEquals(RigLog.ONLINE, loaded.getNewState());
        assertEquals("Fixed", loaded.getReason());
        assertEquals(Math.floor(now.getTime() / 1000), Math.floor(loaded.getTimeStamp().getTime() / 1000));
        assertEquals(rig.getId().longValue(), loaded.getRig().getId().longValue());
        
        ses.beginTransaction();
        ses.delete(log);
        ses.delete(rig);
        ses.delete(type);
        ses.delete(caps);
        ses.getTransaction().commit();
        ses.close();
        ses2.close();
    }
    
    /**
     * Test method for {@link io.rln.ss.data.dao.RigLogDao#addOfflineLog()}
     */
    @Test
    public void testAddOfflineLog()
    {
        Date now = new Date();

        Session ses = DataActivator.getNewSession();
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
        rig.setLastUpdateTimestamp(now);
        rig.setManaged(false);
        rig.setMeta("iLabs");
        rig.setOnline(true);
        ses.save(rig);
        ses.getTransaction().commit();
        
        RigLogDao dao = new RigLogDao(ses);
        RigLog log = dao.addOfflineLog(rig, "Broken");
        assertNotNull(log);
        
        Session ses2 = DataActivator.getNewSession();
        RigLog loaded = (RigLog) ses2.load(RigLog.class, log.getId());
        assertNotNull(loaded);
        
        assertEquals(RigLog.ONLINE, loaded.getOldState());
        assertEquals(RigLog.OFFLINE, loaded.getNewState());
        assertEquals("Broken", loaded.getReason());
        assertEquals(Math.floor(now.getTime() / 1000), Math.floor(loaded.getTimeStamp().getTime() / 1000));
        assertEquals(rig.getId().longValue(), loaded.getRig().getId().longValue());
        
        ses.beginTransaction();
        ses.delete(log);
        ses.delete(rig);
        ses.delete(type);
        ses.delete(caps);
        ses.getTransaction().commit();
        ses.close();
        ses2.close();
    }
    
    /**
     * Test method for {@link io.rln.ss.data.dao.RigLogDao#addUnRegisteredLog()}
     */
    @Test
    public void testAddUnRegisteredLog()
    {
        Date now = new Date();

        Session ses = DataActivator.getNewSession();
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
        rig.setLastUpdateTimestamp(now);
        rig.setManaged(false);
        rig.setMeta("iLabs");
        rig.setOnline(false);
        ses.save(rig);
        ses.getTransaction().commit();
        
        RigLogDao dao = new RigLogDao(ses);
        RigLog log = dao.addUnRegisteredLog(rig, "Un registered");
        assertNotNull(log);
        
        Session ses2 = DataActivator.getNewSession();
        RigLog loaded = (RigLog) ses2.load(RigLog.class, log.getId());
        assertNotNull(loaded);
        
        assertEquals(RigLog.OFFLINE, loaded.getOldState());
        assertEquals(RigLog.NOT_REGISTERED, loaded.getNewState());
        assertEquals("Un registered", loaded.getReason());
        assertEquals(Math.floor(now.getTime() / 1000), Math.floor(loaded.getTimeStamp().getTime() / 1000));
        assertEquals(rig.getId().longValue(), loaded.getRig().getId().longValue());
        
        ses.beginTransaction();
        ses.delete(log);
        ses.delete(rig);
        ses.delete(type);
        ses.delete(caps);
        ses.getTransaction().commit();
        ses.close();
        ses2.close();
    }

    /**
     * Test method for {@link io.rln.ss.data.dao.RigLogDao#findLogs(io.rln.ss.data.entities.Rig, java.util.Date, java.util.Date)}.
     */
    @Test
    public void testFindLogsRigDateDate()
    {
        Date before = new Date(System.currentTimeMillis() - 1000000);
        Date now = new Date();
        Date after = new Date(System.currentTimeMillis() + 1000000);

        Session ses = DataActivator.getNewSession();
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
        rig.setLastUpdateTimestamp(after);
        rig.setManaged(false);
        rig.setMeta("iLabs");
        rig.setOfflineReason("Tomorrows problem");
        ses.save(rig);

        RigLog online = new RigLog();
        online.setOldState(RigLog.NOT_REGISTERED);
        online.setNewState(RigLog.ONLINE);
        online.setReason("First registration");
        online.setTimeStamp(before);
        online.setRig(rig);
        ses.save(online);

        RigLog offline = new RigLog();
        offline.setOldState(RigLog.OFFLINE);
        offline.setNewState(RigLog.ONLINE);
        offline.setReason("Offline");
        offline.setTimeStamp(now);
        offline.setRig(rig);
        ses.save(offline);
        
        RigLog notreg = new RigLog();
        notreg.setOldState(RigLog.OFFLINE);
        notreg.setNewState(RigLog.NOT_REGISTERED);
        notreg.setReason("Not reg");
        notreg.setTimeStamp(after);
        notreg.setRig(rig);
        ses.save(notreg);
        ses.getTransaction().commit();
        
        List<RigLog> logs = this.dao.findLogs(rig, new Date(now.getTime() - 1000), new Date(after.getTime() + 1000));
        
        ses.beginTransaction();
        ses.delete(notreg);
        ses.delete(offline);
        ses.delete(online);
        ses.delete(rig);
        ses.delete(type);
        ses.delete(caps);
        ses.getTransaction().commit();
        ses.close();
        
        assertEquals(2, logs.size());
        assertEquals(offline.getId().longValue(), logs.get(0).getId().longValue());
        assertEquals(notreg.getId().longValue(), logs.get(1).getId().longValue());
    }

    /**
     * Test method for {@link io.rln.ss.data.dao.RigLogDao#findLogsOfState(io.rln.ss.data.entities.Rig, java.lang.String, java.util.Date, java.util.Date)}.
     */
    @Test
    public void testFindLogsOfStateRigStringDateDate()
    {
        Date before = new Date(System.currentTimeMillis() - 1000000);
        Date now = new Date();
        Date after = new Date(System.currentTimeMillis() + 1000000);

        Session ses = DataActivator.getNewSession();
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
        rig.setLastUpdateTimestamp(after);
        rig.setManaged(false);
        rig.setMeta("iLabs");
        rig.setOfflineReason("Tomorrows problem");
        ses.save(rig);

        RigLog online = new RigLog();
        online.setOldState(RigLog.NOT_REGISTERED);
        online.setNewState(RigLog.ONLINE);
        online.setReason("First registration");
        online.setTimeStamp(before);
        online.setRig(rig);
        ses.save(online);

        RigLog offline = new RigLog();
        offline.setOldState(RigLog.ONLINE);
        offline.setNewState(RigLog.OFFLINE);
        offline.setReason("Offline");
        offline.setTimeStamp(now);
        offline.setRig(rig);
        ses.save(offline);
        
        RigLog notreg = new RigLog();
        notreg.setOldState(RigLog.OFFLINE);
        notreg.setNewState(RigLog.NOT_REGISTERED);
        notreg.setReason("Not reg");
        notreg.setTimeStamp(after);
        notreg.setRig(rig);
        ses.save(notreg);
        ses.getTransaction().commit();
        
        List<RigLog> logs = this.dao.findLogsOfState(rig, RigLog.OFFLINE, new Date(now.getTime() - 1000), 
                new Date(after.getTime() + 1000));
        
        ses.beginTransaction();
        ses.delete(notreg);
        ses.delete(offline);
        ses.delete(online);
        ses.delete(rig);
        ses.delete(type);
        ses.delete(caps);
        ses.getTransaction().commit();
        ses.close();
        
        assertEquals(1, logs.size());
        assertEquals(offline.getId().longValue(), logs.get(0).getId().longValue());
    }

    /**
     * Test method for {@link io.rln.ss.data.dao.RigLogDao#findLogs(io.rln.ss.data.entities.RigType, java.util.Date, java.util.Date)}.
     */
    @Test
    public void testFindLogsRigTypeDateDate()
    {
        Date before = new Date(System.currentTimeMillis() - 1000000);
        Date now = new Date();
        Date after = new Date(System.currentTimeMillis() + 1000000);

        Session ses = DataActivator.getNewSession();
        ses.beginTransaction();
        
        RigType type = new RigType();
        type.setName("log_test");
        type.setLogoffGraceDuration(600);
        ses.save(type);

        RigCapabilities caps = new RigCapabilities("a,b,c,d,e,f");
        ses.save(caps);
        
        Rig rig = new Rig();
        rig.setName("log_name_test1");
        rig.setRigType(type);
        rig.setContactUrl("http://url");
        rig.setRigCapabilities(caps);
        rig.setLastUpdateTimestamp(after);
        rig.setManaged(false);
        rig.setMeta("iLabs");
        rig.setOfflineReason("Tomorrows problem");
        ses.save(rig);

        Rig rig2 = new Rig();
        rig2.setName("log_name_test2");
        rig2.setRigType(type);
        rig2.setContactUrl("http://url");
        rig2.setRigCapabilities(caps);
        rig2.setLastUpdateTimestamp(after);
        rig2.setManaged(false);
        rig2.setMeta("iLabs");
        rig2.setOfflineReason("Tomorrows problem");
        ses.save(rig2);

        RigLog online = new RigLog();
        online.setOldState(RigLog.NOT_REGISTERED);
        online.setNewState(RigLog.ONLINE);
        online.setReason("First registration");
        online.setTimeStamp(before);
        online.setRig(rig);
        ses.save(online);

        RigLog offline = new RigLog();
        offline.setOldState(RigLog.ONLINE);
        offline.setNewState(RigLog.OFFLINE);
        offline.setReason("Offline");
        offline.setTimeStamp(now);
        offline.setRig(rig2);
        ses.save(offline);
        
        RigLog off2 = new RigLog();
        off2.setOldState(RigLog.ONLINE);
        off2.setNewState(RigLog.OFFLINE);
        off2.setReason("One Offline");
        off2.setTimeStamp(now);
        off2.setRig(rig);
        ses.save(off2);
        
        
        RigLog notreg = new RigLog();
        notreg.setOldState(RigLog.OFFLINE);
        notreg.setNewState(RigLog.NOT_REGISTERED);
        notreg.setReason("Not reg");
        notreg.setTimeStamp(after);
        notreg.setRig(rig2);
        ses.save(notreg);
        ses.getTransaction().commit();
        
        ses.refresh(type);
        System.out.println(type.getRigs().size());
        Map<Rig, List<RigLog>> logs = this.dao.findLogs(type, new Date(before.getTime() - 1000), 
                 new Date(after.getTime() + 1000));
        
        ses.beginTransaction();
        ses.delete(notreg);
        ses.delete(offline);
        ses.delete(off2);
        ses.delete(online);
        ses.delete(rig);
        ses.delete(rig2);
        ses.delete(type);
        ses.delete(caps);
        ses.getTransaction().commit();
        ses.close();
        
        assertEquals(2, logs.size());
        for (Entry<Rig, List<RigLog>> rlogs : logs.entrySet())
        {
            assertEquals(2, rlogs.getValue().size());
            if (rig.getId().longValue() == rlogs.getKey().getId().longValue())
            {
                assertEquals(online.getId().longValue(), rlogs.getValue().get(0).getId().longValue());
                assertEquals(off2.getId().longValue(), rlogs.getValue().get(1).getId().longValue());
                assertEquals("First registration", rlogs.getValue().get(0).getReason());
                assertEquals("One Offline", rlogs.getValue().get(1).getReason());
            }
            else if (rig2.getId().longValue() == rlogs.getKey().getId().longValue())
            {
                assertEquals(offline.getId().longValue(), rlogs.getValue().get(0).getId().longValue());
                assertEquals(notreg.getId().longValue(), rlogs.getValue().get(1).getId().longValue());
                assertEquals("Offline", rlogs.getValue().get(0).getReason());
                assertEquals("Not reg", rlogs.getValue().get(1).getReason());
            }
            else
            {
                fail("Impossible rig.");
            }
        }
    }

    /**
     * Test method for {@link io.rln.ss.data.dao.RigLogDao#findLogsOfState(io.rln.ss.data.entities.RigType, java.lang.String, java.util.Date, java.util.Date)}.
     */
    @Test
    public void testFindLogsOfStateRigTypeStringDateDate()
    {
        Date before = new Date(System.currentTimeMillis() - 1000000);
        Date now = new Date();
        Date after = new Date(System.currentTimeMillis() + 1000000);

        Session ses = DataActivator.getNewSession();
        ses.beginTransaction();
        
        RigType type = new RigType();
        type.setName("log_test");
        type.setLogoffGraceDuration(600);
        ses.save(type);

        RigCapabilities caps = new RigCapabilities("a,b,c,d,e,f");
        ses.save(caps);
        
        Rig rig = new Rig();
        rig.setName("log_name_test1");
        rig.setRigType(type);
        rig.setContactUrl("http://url");
        rig.setRigCapabilities(caps);
        rig.setLastUpdateTimestamp(after);
        rig.setManaged(false);
        rig.setMeta("iLabs");
        rig.setOfflineReason("Tomorrows problem");
        ses.save(rig);

        Rig rig2 = new Rig();
        rig2.setName("log_name_test2");
        rig2.setRigType(type);
        rig2.setContactUrl("http://url");
        rig2.setRigCapabilities(caps);
        rig2.setLastUpdateTimestamp(after);
        rig2.setManaged(false);
        rig2.setMeta("iLabs");
        rig2.setOfflineReason("Tomorrows problem");
        ses.save(rig2);

        RigLog online = new RigLog();
        online.setOldState(RigLog.NOT_REGISTERED);
        online.setNewState(RigLog.ONLINE);
        online.setReason("First registration");
        online.setTimeStamp(before);
        online.setRig(rig);
        ses.save(online);

        RigLog offline = new RigLog();
        offline.setOldState(RigLog.ONLINE);
        offline.setNewState(RigLog.OFFLINE);
        offline.setReason("Offline");
        offline.setTimeStamp(now);
        offline.setRig(rig2);
        ses.save(offline);
        
        RigLog off2 = new RigLog();
        off2.setOldState(RigLog.ONLINE);
        off2.setNewState(RigLog.OFFLINE);
        off2.setReason("One Offline");
        off2.setTimeStamp(now);
        off2.setRig(rig);
        ses.save(off2);
        
        
        RigLog notreg = new RigLog();
        notreg.setOldState(RigLog.OFFLINE);
        notreg.setNewState(RigLog.NOT_REGISTERED);
        notreg.setReason("Not reg");
        notreg.setTimeStamp(after);
        notreg.setRig(rig2);
        ses.save(notreg);
        ses.getTransaction().commit();
        
        ses.refresh(type);
        System.out.println(type.getRigs().size());
        Map<Rig, List<RigLog>> logs = this.dao.findLogsOfState(type, RigLog.OFFLINE, new Date(before.getTime() - 1000), 
                 new Date(after.getTime() + 1000));
        
        ses.beginTransaction();
        ses.delete(notreg);
        ses.delete(offline);
        ses.delete(off2);
        ses.delete(online);
        ses.delete(rig);
        ses.delete(rig2);
        ses.delete(type);
        ses.delete(caps);
        ses.getTransaction().commit();
        ses.close();
        
        assertEquals(2, logs.size());
        for (Entry<Rig, List<RigLog>> rlogs : logs.entrySet())
        {
            assertEquals(1, rlogs.getValue().size());
            if (rig.getId().longValue() == rlogs.getKey().getId().longValue())
            {
                assertEquals(off2.getId().longValue(), rlogs.getValue().get(0).getId().longValue());
                assertEquals("One Offline", rlogs.getValue().get(0).getReason());
            }
            else if (rig2.getId().longValue() == rlogs.getKey().getId().longValue())
            {
                assertEquals(offline.getId().longValue(), rlogs.getValue().get(0).getId().longValue());
                assertEquals("Offline", rlogs.getValue().get(0).getReason());
            }
            else
            {
                fail("Impossible rig.");
            }
        }
    }

}
