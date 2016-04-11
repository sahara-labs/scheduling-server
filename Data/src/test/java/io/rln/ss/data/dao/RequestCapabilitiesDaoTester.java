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
 * @date 29th March 2010
 */
package io.rln.ss.data.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.rln.ss.data.DataActivator;
import io.rln.ss.data.dao.RequestCapabilitiesDao;
import io.rln.ss.data.dao.RigCapabilitiesDao;
import io.rln.ss.data.entities.MatchingCapabilities;
import io.rln.ss.data.entities.MatchingCapabilitiesId;
import io.rln.ss.data.entities.RequestCapabilities;
import io.rln.ss.data.entities.ResourcePermission;
import io.rln.ss.data.entities.RigCapabilities;
import io.rln.ss.data.entities.UserClass;
import io.rln.ss.data.testsetup.DataAccessTestSetup;

/**
 * Tests the {@link RigCapabilitiesDao} class.
 */
public class RequestCapabilitiesDaoTester extends TestCase
{
    /** Object of class under test. */
    private RequestCapabilitiesDao dao;
  
    @Before
    @Override
    public void setUp() throws Exception
    {
        DataAccessTestSetup.setup();
        this.dao = new RequestCapabilitiesDao();
        super.setUp();
    }

    @After
    @Override
    public void tearDown() throws Exception
    {
        this.dao.closeSession();
        super.tearDown();
    }

    /**
     * Test method for {@link io.rln.ss.data.dao.RigCapabilitiesDao#findCapabilites(java.lang.String)}.
     */
    @Test
    public void testFindCapabilites()
    {
        Session ses = DataActivator.getNewSession();
        RequestCapabilities caps = new RequestCapabilities("a,b,c,d,e,f");
        ses.beginTransaction();
        ses.save(caps);
        ses.getTransaction().commit();
        ses.close();
        
        RequestCapabilities ld = this.dao.findCapabilites("a,b,c,d,e,f");
        assertNotNull(ld);
        assertEquals(caps.getId(), ld.getId());
        this.dao.delete(ld);
    }
    
    /**
     * Test method for {@link io.rln.ss.data.dao.RigCapabilitiesDao#findCapabilites(java.lang.String)}.
     */
    @Test
    public void testFindCapabilitiesNotFound()
    {
        RequestCapabilities cap = this.dao.findCapabilites("b,c,d");
        assertNull(cap);
    }

    /**
     * Test method for {@link io.rln.ss.data.dao.RigCapabilitiesDao#addCapabilities(java.lang.String)}.
     */
    @Test
    public void testAddCapabilities()
    {
        /* Add the request capabilities. */
        RigCapabilities req[] = new RigCapabilities[10];
        RigCapabilitiesDao rigDao = new RigCapabilitiesDao();
        req[0] = new RigCapabilities("a,b,c,d");
        req[1] = new RigCapabilities("a,b,c,d,e");
        req[2] = new RigCapabilities("a,d,e");
        req[3] = new RigCapabilities("a,b,d,f");
        req[4] = new RigCapabilities("b,d,f");
        req[5] = new RigCapabilities("f");
        req[6] = new RigCapabilities("a,c,f");
        req[7] = new RigCapabilities("x,y");
        req[8] = new RigCapabilities("xy,x");
        req[9] = new RigCapabilities("x,y,z");
        for (int i = 0; i < req.length; i++)
        {
            req[i] = rigDao.persist(req[i]);
        }
        
        String capsStr = "d, a,b";
        RequestCapabilities caps = this.dao.addCapabilities(capsStr);
        assertNotNull(caps);
        assertEquals("a,b,d", caps.getCapabilities());

        Set<MatchingCapabilities> matches = caps.getMatchingCapabilitieses();
        List<String> matchingReq = new ArrayList<String>();
        for (MatchingCapabilities m : matches)
        {
            matchingReq.add(m.getRigCapabilities().getCapabilities());
        }
        
        assertEquals(3, matchingReq.size());
        assertTrue(matchingReq.contains("a,b,c,d"));
        assertTrue(matchingReq.contains("a,b,c,d,e"));
        assertTrue(matchingReq.contains("a,b,d,f"));
        
        Session ses = this.dao.getSession();
        ses.beginTransaction();
        for (MatchingCapabilities m : matches)
        {
            ses.delete(m);
        }
        ses.getTransaction().commit();
        for (RigCapabilities r : req)
        {
            rigDao.delete(r);
        }
        this.dao.delete(caps);
    }

    @Test
    public void testDelete()
    {
        Session ses = DataActivator.getNewSession();
        ses.beginTransaction();
        RequestCapabilities req = new RequestCapabilities("a,b");
        ses.persist(req);
        RigCapabilities rig1 = new RigCapabilities("a,b,c,d");
        ses.persist(rig1);
        RigCapabilities rig2 = new RigCapabilities("a,b,c,d,e");
        ses.persist(rig2);
        MatchingCapabilities match1 = new MatchingCapabilities(new MatchingCapabilitiesId(rig1.getId(), req.getId()), req, rig1);
        ses.persist(match1);
        MatchingCapabilities match2 = new MatchingCapabilities(new MatchingCapabilitiesId(rig2.getId(), req.getId()), req, rig2);
        ses.persist(match2);
        ses.getTransaction().commit();
        
        this.dao.delete(req.getId());
        assertNull(this.dao.get(req.getId()));
        
        ses.refresh(rig1);
        assertEquals(0, rig1.getMatchingCapabilitieses().size());
        ses.refresh(rig2);
        assertEquals(0, rig2.getMatchingCapabilitieses().size());
        
        ses.beginTransaction();
        ses.delete(rig1);
        ses.delete(rig2);
        ses.getTransaction().commit();
    }
    
    public void testDeleteResourcePermission()
    {
        Session ses = DataActivator.getNewSession();
        ses.beginTransaction();
        RequestCapabilities req = new RequestCapabilities("a,b");
        ses.persist(req);
        UserClass uc = new UserClass();
        uc.setName("user_class");
        ses.persist(uc);
        RigCapabilities rig1 = new RigCapabilities("a,b,c,d");
        ses.persist(rig1);
        RigCapabilities rig2 = new RigCapabilities("a,b,c,d,e");
        ses.persist(rig2);
        ResourcePermission rc = new ResourcePermission();
        rc.setType("CAPABILITY");
        rc.setRequestCapabilities(req);
        rc.setExpiryTime(new Date());
        rc.setStartTime(new Date());
        rc.setUserClass(uc);
        ses.persist(rc);
        MatchingCapabilities match1 = new MatchingCapabilities(new MatchingCapabilitiesId(rig1.getId(), req.getId()), req, rig1);
        ses.persist(match1);
        MatchingCapabilities match2 = new MatchingCapabilities(new MatchingCapabilitiesId(rig2.getId(), req.getId()), req, rig2);
        ses.persist(match2);
        ses.getTransaction().commit();
        
        this.dao.delete(req.getId());
        assertNull(this.dao.get(req.getId()));
        
        ses.refresh(rig1);
        assertEquals(0, rig1.getMatchingCapabilitieses().size());
        ses.refresh(rig2);
        assertEquals(0, rig2.getMatchingCapabilitieses().size());
        
        ses.beginTransaction();
        ses.delete(rig1);
        ses.delete(rig2);
        ses.delete(rc);
        ses.delete(uc);
        ses.getTransaction().commit();
    }
}
