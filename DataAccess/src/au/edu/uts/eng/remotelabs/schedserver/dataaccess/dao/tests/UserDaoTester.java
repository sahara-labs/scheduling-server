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


import junit.framework.TestCase;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserAssociation;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserAssociationId;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClass;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.testsetup.DataAccessTestSetup;

/**
 * Tests the {@link UserDao} class. 
 */
public class UserDaoTester extends TestCase
{
    /** Object of class under test. */
    private UserDao dao;


    @Override
    @Before
    public void setUp() throws Exception
    {
        DataAccessTestSetup.setup();
        super.setUp();
        this.dao = new UserDao();
    }
    
    @Override
    @After
    public void tearDown() throws Exception
    {
        super.tearDown();
        this.dao.closeSession();
    }
    
    @Test
    public void testGetUser()
    {
        User user = new User("tuser", "test", "USER");
        Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        ses.save(user);
        ses.getTransaction().commit();
        ses.close();
        
        User ldUser = this.dao.findByName("test", "tuser");
        assertNotNull(ldUser);
        
        assertEquals("test", ldUser.getNamespace());
        assertEquals("tuser", ldUser.getName());
        assertEquals("USER", ldUser.getPersona());
        
        this.dao.delete(ldUser);
    }
    
    @Test
    public void testDeleteUser()
    {
        Session ses = DataAccessActivator.getNewSession();
        
        ses.beginTransaction();
        UserClass uc = new UserClass();
        uc.setName("test1");
        uc.setPriority((short)10);
        uc.setActive(true);
        uc.setBookable(true);
        uc.setQueuable(true);
        uc.setUsersLockable(true);
        ses.save(uc);
        User us = new User("del1", "ns1", "ACADEMIC");
        ses.save(us);
        ses.getTransaction().commit();
        
        ses.beginTransaction();
        UserAssociation assoc = new UserAssociation();
        assoc.setUser(us);
        assoc.setUserClass(uc);
        UserAssociationId id = new UserAssociationId();
        id.setUserClassId(uc.getId());
        id.setUsersId(us.getId());
        assoc.setId(id);
        ses.save(assoc);
        ses.getTransaction().commit();
        
        this.dao.delete(us.getId());
        
        assertNull(this.dao.findByName("ns1", "test1"));
        
        ses.beginTransaction();
        ses.delete(uc);
        ses.getTransaction().commit();
    }
}
