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
 * @date 7th December 2011
 */
package au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.tests;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserAssociationDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserAssociation;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserAssociationId;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClass;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.testsetup.DataAccessTestSetup;

/**
 * Tests the {@link UserAssociationDao} class.
 */
public class UserAssociationDaoTester extends TestCase
{
    /** Object of class under test. */
    private UserAssociationDao dao;

    @Override
    public void setUp() throws Exception
    {
        DataAccessTestSetup.setup();
        this.dao = new UserAssociationDao();
    }
    
    public void testDeleteUserUserClass()
    {
        Session ses = this.dao.getSession();
        ses.beginTransaction();
        
        User us = new User("user", "userns", "ADMIN");
        ses.save(us);

        UserClass uc = new UserClass();
        uc.setName("clazz");
        uc.setPriority((short) 10);
        uc.setActive(true);
        uc.setKickable(true);
        uc.setQueuable(true);
        uc.setBookable(true);
        uc.setUsersLockable(true);
        ses.save(uc);
        
        UserAssociationId id = new UserAssociationId(us.getId(), uc.getId());
        UserAssociation assoc = new UserAssociation(id, uc, us);
        ses.save(assoc);
        
        ses.getTransaction().commit();
        
        this.dao.delete(us, uc);
        
        int num = (Integer) this.dao.getSession().createCriteria(UserAssociation.class)
                .add(Restrictions.eq("user", us))
                .add(Restrictions.eq("userClass", uc))
                .setProjection(Projections.rowCount())
                .uniqueResult();
        
        assertEquals(0, num);
        
        ses.beginTransaction();
        ses.delete(us);
        ses.delete(uc);
        ses.getTransaction().commit();
    }
    
    public void testDeleteUserUserClassNoDelete()
    {
        Session ses = this.dao.getSession();
        ses.beginTransaction();
        
        User us = new User("user", "userns", "ADMIN");
        ses.save(us);
        
        User us2 = new User("user2", "userns", "ADMIN");
        ses.save(us2);

        UserClass uc = new UserClass();
        uc.setName("clazz");
        uc.setPriority((short) 10);
        uc.setActive(true);
        uc.setKickable(true);
        uc.setQueuable(true);
        uc.setBookable(true);
        uc.setUsersLockable(true);
        ses.save(uc);
        
        UserAssociationId id = new UserAssociationId(us.getId(), uc.getId());
        UserAssociation assoc = new UserAssociation(id, uc, us);
        ses.save(assoc);
        
        ses.getTransaction().commit();
        
        this.dao.delete(us2, uc);
        
        int num = (Integer) this.dao.getSession().createCriteria(UserAssociation.class)
                .add(Restrictions.eq("user", us))
                .add(Restrictions.eq("userClass", uc))
                .setProjection(Projections.rowCount())
                .uniqueResult();
        
        assertEquals(1, num);
        
        ses.beginTransaction();
        ses.delete(assoc);
        ses.delete(us);
        ses.delete(us2);
        ses.delete(uc);
        ses.getTransaction().commit();
    }
}
