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
package au.edu.uts.eng.remotelabs.schedserver.permissions.impl.tests;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserClassDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClass;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.testsetup.DataAccessTestSetup;
import au.edu.uts.eng.remotelabs.schedserver.permissions.impl.UserClassAdmin;

/**
 * Tests the {@link UserClassAdmin} class.
 */
public class UserClassAdminTester extends TestCase
{
    /** Object of class under test. */
    private UserClassAdmin admin;
    
    /** User class dao. */
    private UserClassDao dao;
    
    @Override
    @Before
    public void setUp() throws Exception
    {
        DataAccessTestSetup.setup();
        this.admin = new UserClassAdmin(DataAccessActivator.getNewSession());
        this.dao = new UserClassDao();
    }
    
    @Override
    @After
    public void tearDown() throws Exception
    {
        this.admin.closeSession();
        this.dao.closeSession();
    }


    @Test
    public void testAddUser()
    {
        assertTrue(this.admin.addUserClass("className", 10, true, true, true, true));
        
        UserClass uc = this.dao.findByName("className");
        assertNotNull(uc);
        assertEquals("className", uc.getName());
        assertEquals(10, uc.getPriority());
        assertTrue(uc.isActive());
        assertTrue(uc.isKickable());
        assertTrue(uc.isQueuable());
        assertTrue(uc.isUsersLockable());
        
        this.dao.delete(uc);
    }
    
    @Test
    public void testAddUserFalse()
    {
        assertTrue(this.admin.addUserClass("className", 300, false, false, false, false));
        
        UserClass uc = this.dao.findByName("className");
        assertNotNull(uc);
        assertEquals("className", uc.getName());
        assertEquals(300, uc.getPriority());
        assertFalse(uc.isActive());
        assertFalse(uc.isKickable());
        assertFalse(uc.isQueuable());
        assertFalse(uc.isUsersLockable());
        
        this.dao.delete(uc);
    }
    
    @Test
    public void testAddUserExists()
    {
        UserClass uc = new UserClass();
        uc.setName("clazz");
        uc.setPriority((short)50);
        uc.setActive(true);
        uc.setQueuable(true);
        uc.setBookable(true);
        uc.setUsersLockable(false);
        Session ses = this.dao.getSession();
        ses.beginTransaction();
        ses.save(uc);
        ses.getTransaction().commit();
        
        assertFalse(this.admin.addUserClass("clazz", 10, false, false, false, false));
        assertNotNull(this.admin.getFailureReason());
        
        this.dao.delete(uc);
    }

    @Test
    public void testEditUser()
    {
        UserClass uc = new UserClass();
        uc.setName("clazz");
        uc.setPriority((short)50);
        uc.setActive(true);
        uc.setQueuable(true);
        uc.setBookable(true);
        uc.setUsersLockable(false);
        uc.setKickable(true);
        Session ses = this.dao.getSession();
        ses.beginTransaction();
        ses.save(uc);
        ses.getTransaction().commit();
        
        assertTrue(this.admin.editUserClass("clazz", 10, true, false, true, false));
        assertNull(this.admin.getFailureReason());
        this.dao.refresh(uc);
        
        assertEquals("clazz", uc.getName());
        assertEquals(10, uc.getPriority());
        assertTrue(uc.isActive());
        assertTrue(uc.isKickable());
        assertFalse(uc.isQueuable());
        assertFalse(uc.isUsersLockable());
        
        this.dao.delete(uc);
    }


    @Test
    public void testEditUserId()
    {
        UserClass uc = new UserClass();
        uc.setName("clazz");
        uc.setPriority((short)50);
        uc.setActive(true);
        uc.setQueuable(true);
        uc.setBookable(true);
        uc.setUsersLockable(false);
        uc.setKickable(true);
        Session ses = this.dao.getSession();
        ses.beginTransaction();
        ses.save(uc);
        ses.getTransaction().commit();
        
        assertTrue(this.admin.editUserClass(uc.getId(), "userc", 10, true, false, true, false));
        assertNull(this.admin.getFailureReason());
        this.dao.refresh(uc);
        
        assertEquals("userc", uc.getName());
        assertEquals(10, uc.getPriority());
        assertTrue(uc.isActive());
        assertTrue(uc.isKickable());
        assertFalse(uc.isQueuable());
        assertFalse(uc.isUsersLockable());
        
        this.dao.delete(uc);
    }
    
    @Test
    public void testEditUserNotFound()
    {
        assertFalse(this.admin.editUserClass("foo", 10, true, true, true, true));
        assertNotNull(this.admin.getFailureReason());
    }

    @Test
    public void testDeleteUserClass()
    {
        UserClass uc = new UserClass();
        uc.setName("clazz");
        uc.setPriority((short)50);
        uc.setActive(true);
        uc.setQueuable(true);
        uc.setBookable(true);
        uc.setUsersLockable(false);
        uc.setKickable(true);
        Session ses = this.dao.getSession();
        ses.beginTransaction();
        ses.save(uc);
        ses.getTransaction().commit();
        
        assertTrue(this.admin.deleteUserClass("clazz"));
        assertNull(this.admin.getFailureReason());
        
        assertNull(this.dao.findByName("clazz"));
        
    }

    @Test
    public void testDeleteUserClassId()
    {
        UserClass uc = new UserClass();
        uc.setName("clazz");
        uc.setPriority((short)50);
        uc.setActive(true);
        uc.setQueuable(true);
        uc.setBookable(true);
        uc.setUsersLockable(false);
        uc.setKickable(true);
        Session ses = this.dao.getSession();
        ses.beginTransaction();
        ses.save(uc);
        ses.getTransaction().commit();
        
        
        assertTrue(this.admin.deleteUserClass(uc.getId()));
        assertNull(this.admin.getFailureReason());
        
        assertNull(this.dao.findByName(uc.getName()));
    }
    
    @Test
    public void testDeleteUserClassNotFound()
    {
        assertFalse(this.admin.deleteUserClass("not exists"));
        assertNotNull(this.admin.getFailureReason());
    }

}
