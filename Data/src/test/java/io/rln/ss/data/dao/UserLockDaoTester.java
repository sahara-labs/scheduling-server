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
 * @date 27th March 2010
 */
package io.rln.ss.data.dao;


import java.util.Date;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.rln.ss.data.DataActivator;
import io.rln.ss.data.dao.UserLockDao;
import io.rln.ss.data.entities.ResourcePermission;
import io.rln.ss.data.entities.User;
import io.rln.ss.data.entities.UserClass;
import io.rln.ss.data.entities.UserLock;
import io.rln.ss.data.testsetup.DataAccessTestSetup;

/**
 * Tests the {@link UserLockDao} class.
 */
public class UserLockDaoTester extends TestCase
{
    /** Object of class under test. */
    private UserLockDao dao;

    @Override
    @Before
    public void setUp() throws Exception
    {
        DataAccessTestSetup.setup();
        this.dao = new UserLockDao();
    }
    
    @Override
    @After
    public void tearDown() throws Exception
    {
        this.dao.closeSession();
    }
    
    @Test
    public void testFindLock()
    {
         Session ses = DataActivator.getNewSession();
         ses.beginTransaction();
         User user = new User("locktest", "ns", "USER");
         ses.persist(user);
         UserClass userClass= new UserClass();
         userClass.setName("uc");
         ses.persist(userClass);
         ResourcePermission perm = new ResourcePermission();         
         perm.setStartTime(new Date());
         perm.setExpiryTime(new Date());
         perm.setUserClass(userClass);
         perm.setType("RIG");
         ses.persist(perm);
         UserLock lock = new UserLock(user, perm, true, "abc123");
         ses.persist(lock);
         ses.getTransaction().commit();
         
         UserLock ld = this.dao.findLock(user, perm);
         assertNotNull(ld);
         assertEquals(lock.getId(), ld.getId());
         assertEquals(lock.getLockKey(), ld.getLockKey());
         assertEquals(lock.getUser().getName(), ld.getUser().getName());
         assertEquals(lock.getResourcePermission().getId(), ld.getResourcePermission().getId());
         
         ses.beginTransaction();
         ses.delete(lock);
         ses.delete(perm);
         ses.delete(userClass);
         ses.delete(user);
         ses.getTransaction().commit();
    }
}
