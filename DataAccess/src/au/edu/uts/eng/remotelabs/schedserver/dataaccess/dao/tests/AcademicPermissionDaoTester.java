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
import java.util.List;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.AcademicPermissionDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.GenericDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.AcademicPermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClass;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.tests.DataAccessTestSetup;

/**
 * Tests the {@link AcademicPermissionDao} class.
 */
public class AcademicPermissionDaoTester extends TestCase
{
    /** Object of class under test. */
    private AcademicPermissionDao dao;
    
    /** Users. */
    private GenericDao<User> userDao;
    private User mUser;
    private User tUser;
    
    /** User classes. */
    private GenericDao<UserClass> classDao;
    private UserClass mClass;
    private UserClass tClass;
    
    /** Permissions. */
    private List<AcademicPermission> permissions;
    
    @Override
    @Before
    public void setUp() throws Exception
    {
        DataAccessTestSetup.setup();
        this.dao = new AcademicPermissionDao();
        
        this.userDao = new GenericDao<User>(User.class);
        this.mUser = new User("mdiponio", "UTS", "ADMIN");
        this.tUser = new User("tmachet", "UTS", "STUDENT");
        this.mUser = this.userDao.persist(this.mUser);
        this.tUser = this.userDao.persist(this.tUser);
        
        this.classDao = new GenericDao<UserClass>(UserClass.class);
        UserClass uc = new UserClass();
        uc.setName("mdClass");
        this.mClass = this.classDao.persist(uc);
        uc = new UserClass();
        uc.setName("tmClass");
        this.tClass = this.classDao.persist(uc);
        
        this.permissions = new ArrayList<AcademicPermission>(4);
        AcademicPermission perm = new AcademicPermission();
        perm.setUser(this.mUser);
        perm.setUserClass(this.mClass);
        this.permissions.add(this.dao.persist(perm));
        perm = new AcademicPermission();
        perm.setUser(this.tUser);
        perm.setUserClass(this.mClass);
        this.permissions.add(this.dao.persist(perm));
        perm = new AcademicPermission();
        perm.setUser(this.tUser);
        perm.setUserClass(this.tClass);
        this.permissions.add(this.dao.persist(perm));
        perm = new AcademicPermission();
        perm.setUser(this.mUser);
        perm.setUserClass(this.tClass);
        this.permissions.add(this.dao.persist(perm));
    }
    
    @After
    @Override
    public void tearDown() throws Exception
    {
        for (AcademicPermission p : this.permissions)
        {
            this.dao.delete(p);
        }
        
        this.userDao.delete(this.mUser);
        this.userDao.delete(this.tUser);
        this.userDao.closeSession();
        
        this.classDao.delete(this.mClass);
        this.classDao.delete(this.tClass);
        this.classDao.closeSession();
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.AcademicPermissionDao#getByUser(au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User)}.
     */
    @Test
    public void testGetByUser()
    { 
        List<AcademicPermission> per = this.dao.getByUser(this.mUser);
        assertNotNull(per);
        assertEquals(2, per.size());
        
        assertEquals(this.mUser.getName(), per.get(0).getUser().getName());
        List<String> names = new ArrayList<String>();
        names.add(per.get(0).getUserClass().getName());
        names.add(per.get(1).getUserClass().getName());
        assertTrue(names.contains(this.mClass.getName()));
        assertTrue(names.contains(this.tClass.getName()));
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.AcademicPermissionDao#getByUserClass(au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClass)}.
     */
    @Test
    public void testGetByUserClass()
    {
        List<AcademicPermission> per = this.dao.getByUserClass(this.mClass);
        assertNotNull(per);
        assertEquals(2, per.size());
        
        assertEquals(this.mClass.getName(), per.get(0).getUserClass().getName());
        List<String> names = new ArrayList<String>();
        names.add(per.get(0).getUser().getName());
        names.add(per.get(1).getUser().getName());
        assertTrue(names.contains(this.mUser.getName()));
        assertTrue(names.contains(this.tUser.getName()));
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.AcademicPermissionDao#getForUserAndUserClass(au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User, au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClass)}.
     */
    @Test
    public void testGetForUserAndUserClass()
    {
        AcademicPermission perm = this.dao.getForUserAndUserClass(this.mUser, this.mClass);
        assertNotNull(perm);
        assertEquals(this.mUser.getName(), perm.getUser().getName());
        assertEquals(this.mClass.getName(), perm.getUserClass().getName());
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.AcademicPermissionDao#getForUserAndUserClass(au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User, au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClass)}.
     */
    @Test
    public void testGetForUserAndUserClassT()
    {
        AcademicPermission perm = this.dao.getForUserAndUserClass(this.tUser, this.tClass);
        assertNotNull(perm);
        assertEquals(this.tUser.getName(), perm.getUser().getName());
        assertEquals(this.tClass.getName(), perm.getUserClass().getName());
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.AcademicPermissionDao#getForUserAndUserClass(au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User, au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClass)}.
     */
    @Test
    public void testGetForUserAndUserClassTM()
    {
        AcademicPermission perm = this.dao.getForUserAndUserClass(this.tUser, this.mClass);
        assertNotNull(perm);
        assertEquals(this.tUser.getName(), perm.getUser().getName());
        assertEquals(this.mClass.getName(), perm.getUserClass().getName());
    }

}
