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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.SessionDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClass;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.tests.DataAccessTestSetup;

/**
 * Tests the {@link SessionDao} class.
 */
public class SessionDaoTester extends TestCase
{
    /** Object of class under test. */
    private SessionDao dao;

    @Before
    @Override
    public void setUp() throws Exception
    {
        DataAccessTestSetup.setup();
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
        db.close();
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.SessionDao#findActiveSession(au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User)}.
     */
    @Test
    public void testFindAllActiveSessions()
    {
        Date now = new Date();
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        db.beginTransaction();
        User user = new User("sestest", "ns", "USER");
        db.persist(user);
        User user2 = new User("sestest2", "ns", "USER");
        db.persist(user2);
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
        ses.setUser(user2);
        ses.setUserName(user2.getName());
        ses.setUserNamespace(user2.getNamespace());
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
        Session ses3 = new Session();
        ses3.setActive(false);
        ses3.setActivityLastUpdated(now);
        ses3.setPriority((short) 1);
        ses3.setRequestTime(new Date(System.currentTimeMillis() - 1000));
        ses3.setUser(user);
        ses3.setUserName(user.getName());
        ses3.setUserNamespace(user.getNamespace());
        ses3.setRequestedResourceId(type.getId());
        ses3.setRequestedResourceName(type.getName());
        ses3.setResourcePermission(perm);
        ses3.setResourceType("TYPE");
        db.persist(ses3);
        
        db.getTransaction().commit();
        
        List<Session> sessions = this.dao.findAllActiveSessions();
        
        db.beginTransaction();
        db.delete(ses);
        db.delete(ses2);
        db.delete(ses3);
        db.delete(ses);
        db.delete(perm);
        db.delete(type);
        db.delete(userClass);
        db.delete(user);
        db.delete(user2);
        db.getTransaction().commit();
        
        assertNotNull(sessions);
        assertEquals(2, sessions.size());
        
        List<Long> ids = new ArrayList<Long>(2);
        for (Session s : sessions)
        {
            ids.add(s.getId());
        }
        
        assertTrue(ids.contains(ses.getId()));
        assertTrue(ids.contains(ses2.getId()));
    }

}
