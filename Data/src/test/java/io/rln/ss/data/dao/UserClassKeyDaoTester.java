/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2012, University of Technology, Sydney
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
 * @date 10th January 2012
 */
package io.rln.ss.data.dao;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.junit.Before;
import org.junit.Test;

import io.rln.ss.data.DataActivator;
import io.rln.ss.data.dao.UserClassKeyDao;
import io.rln.ss.data.entities.User;
import io.rln.ss.data.entities.UserClass;
import io.rln.ss.data.entities.UserClassKey;
import io.rln.ss.data.entities.UserClassKeyConstraint;
import io.rln.ss.data.entities.UserClassKeyRedemption;
import io.rln.ss.data.testsetup.DataAccessTestSetup;

/**
 * Tests the {@link UserClassKeyDao} class
 */
public class UserClassKeyDaoTester extends TestCase
{
    /** Object of class under test. */
    public UserClassKeyDao dao;
    
    @Before
    @Override
    public void setUp() throws Exception
    {
        super.setUp();
        
        DataAccessTestSetup.setup();
        
        this.dao = new UserClassKeyDao();
    }
    
    @Test
    public void testFindKey()
    {
        Session db = DataActivator.getNewSession();
        
        db.beginTransaction();
        
        UserClass uc = new UserClass();
        uc.setName("UC_Test");
        uc.setPriority((short)1);
        uc.setTimeHorizon(0);
        db.persist(uc);
        
        UserClassKey key = new UserClassKey();
        key.setRemaining(1);
        key.setUserClass(uc);
        key.generateKey();
        db.persist(key);
        
        db.getTransaction().commit();
        
        UserClassKey loadedKey = this.dao.findKey(key.getRedeemKey());
        
        db.beginTransaction();
        db.delete(key);
        db.delete(uc);
        db.getTransaction().commit();
        
        assertNotNull(loadedKey);
        assertNotNull(loadedKey.getId());
        assertEquals(key.getRedeemKey(), loadedKey.getRedeemKey());
        assertEquals(key.getRemaining(), loadedKey.getRemaining());
        
        assertEquals(0, loadedKey.getConstraints().size());
        assertEquals(0, loadedKey.getRedemptions().size());
        assertNull(loadedKey.getExpiry());
    }
    
    @Test
    public void testFindKeyNotFound()
    {
        assertNull(this.dao.findKey("Does_Not_Exist"));
    }
    
    @Test
    public void testDelete()
    {
        Session db = DataActivator.getNewSession();
        
        db.beginTransaction();
        
        UserClass uc = new UserClass();
        uc.setName("UC_Test");
        uc.setPriority((short)1);
        uc.setTimeHorizon(0);
        db.persist(uc);
        
        UserClassKey key = new UserClassKey();
        key.setRemaining(1);
        key.setUserClass(uc);
        key.generateKey();
        db.persist(key);
        
        db.getTransaction().commit();
        
        int preDelete = (Integer) db.createCriteria(UserClassKey.class)
                .add(Restrictions.eq("redeemKey", key.getRedeemKey()))
                .setProjection(Projections.rowCount())
                .uniqueResult();
        
        this.dao.delete(this.dao.get(key.getId()));
        
        int postDelete = (Integer) db.createCriteria(UserClassKey.class)
                .add(Restrictions.eq("redeemKey", key.getRedeemKey()))
                .setProjection(Projections.rowCount())
                .uniqueResult();
        
        db.beginTransaction();
        db.delete(uc);
        db.getTransaction().commit();

        assertEquals(1, preDelete);
        assertEquals(0, postDelete);
    }

    @Test
    public void testDeleteId()
    {
        Session db = DataActivator.getNewSession();
        
        db.beginTransaction();
        
        UserClass uc = new UserClass();
        uc.setName("UC_Test");
        uc.setPriority((short)1);
        uc.setTimeHorizon(0);
        db.persist(uc);
        
        UserClassKey key = new UserClassKey();
        key.setRemaining(1);
        key.setUserClass(uc);
        key.generateKey();
        db.persist(key);
        
        db.getTransaction().commit();
        
        int preDelete = (Integer) db.createCriteria(UserClassKey.class)
                .add(Restrictions.eq("redeemKey", key.getRedeemKey()))
                .setProjection(Projections.rowCount())
                .uniqueResult();
        
        this.dao.delete(key.getId());
        
        int postDelete = (Integer) db.createCriteria(UserClassKey.class)
                .add(Restrictions.eq("redeemKey", key.getRedeemKey()))
                .setProjection(Projections.rowCount())
                .uniqueResult();
        
        db.beginTransaction();
        db.delete(uc);
        db.getTransaction().commit();

        assertEquals(1, preDelete);
        assertEquals(0, postDelete);
    }
    
    @Test
    public void testDeleteWithConstRef()
    {
        Session db = DataActivator.getNewSession();
        
        db.beginTransaction();
        
        UserClass uc = new UserClass();
        uc.setName("UC_Test");
        uc.setPriority((short)1);
        uc.setTimeHorizon(0);
        db.persist(uc);
        
        UserClassKey key = new UserClassKey();
        key.setRemaining(1);
        key.setUserClass(uc);
        key.generateKey();
        db.persist(key);
        
        UserClassKeyConstraint keyConst = new UserClassKeyConstraint();
        keyConst.setName("Const_1");
        keyConst.setValue("Value_1");
        keyConst.setClassKey(key);
        db.persist(keyConst);
        
        UserClassKeyConstraint keyConst2 = new UserClassKeyConstraint();
        keyConst2.setName("Const_2");
        keyConst2.setValue("Value_2");
        keyConst2.setClassKey(key);
        db.persist(keyConst2);
        
        db.getTransaction().commit();
        
        int preDelete = (Integer) db.createCriteria(UserClassKey.class)
                .add(Restrictions.eq("redeemKey", key.getRedeemKey()))
                .setProjection(Projections.rowCount())
                .uniqueResult();
        
        int preRefCount = (Integer) db.createCriteria(UserClassKeyConstraint.class)
                .add(Restrictions.eq("classKey.id", key.getId()))
                .setProjection(Projections.rowCount())
                .uniqueResult();

        
        this.dao.delete(this.dao.get(key.getId()));
        
        int postDelete = (Integer) db.createCriteria(UserClassKey.class)
                .add(Restrictions.eq("redeemKey", key.getRedeemKey()))
                .setProjection(Projections.rowCount())
                .uniqueResult();
        
        int postRefCount = (Integer) db.createCriteria(UserClassKeyConstraint.class)
                .add(Restrictions.eq("classKey.id", key.getId()))
                .setProjection(Projections.rowCount())
                .uniqueResult();
        
        db.beginTransaction();
        db.delete(uc);
        db.getTransaction().commit();

        assertEquals(1, preDelete);
        assertEquals(2, preRefCount);
        assertEquals(0, postDelete);
        assertEquals(0, postRefCount);
    }
        
    @Test
    public void testDeleteWithRedeemRef()
    {
        Session db = DataActivator.getNewSession();
        
        db.beginTransaction();
        
        UserClass uc = new UserClass();
        uc.setName("UC_Test");
        uc.setPriority((short)1);
        uc.setTimeHorizon(0);
        db.persist(uc);
        
        UserClassKey key = new UserClassKey();
        key.setRemaining(1);
        key.setUserClass(uc);
        key.generateKey();
        db.persist(key);
        
        User user = new User();
        user.setName("uck_test");
        user.setNamespace("TEST");
        user.setPersona("USER");
        db.persist(user);
        
        User user2 = new User();
        user2.setName("uck_test2");
        user2.setNamespace("TEST");
        user2.setPersona("USER");
        db.persist(user2);
        
        UserClassKeyRedemption redemp = new UserClassKeyRedemption();
        redemp.setClassKey(key);
        redemp.setUser(user);
        db.persist(redemp);
        
        UserClassKeyRedemption redemp2 = new UserClassKeyRedemption();
        redemp2.setClassKey(key);
        redemp2.setUser(user2);
        db.persist(redemp2);
        
        db.getTransaction().commit();
        
        int preDelete = (Integer) db.createCriteria(UserClassKey.class)
                .add(Restrictions.eq("redeemKey", key.getRedeemKey()))
                .setProjection(Projections.rowCount())
                .uniqueResult();
        
        int preRefCount = (Integer) db.createCriteria(UserClassKeyRedemption.class)
                .add(Restrictions.eq("classKey.id", key.getId()))
                .setProjection(Projections.rowCount())
                .uniqueResult();

        this.dao.delete(this.dao.get(key.getId()));
        
        int postDelete = (Integer) db.createCriteria(UserClassKey.class)
                .add(Restrictions.eq("redeemKey", key.getRedeemKey()))
                .setProjection(Projections.rowCount())
                .uniqueResult();
        
        int postRefCount = (Integer) db.createCriteria(UserClassKeyRedemption.class)
                .add(Restrictions.eq("classKey.id", key.getId()))
                .setProjection(Projections.rowCount())
                .uniqueResult();
        
        db.beginTransaction();
        db.delete(user);
        db.delete(user2);
        db.delete(uc);
        db.getTransaction().commit();

        assertEquals(1, preDelete);
        assertEquals(2, preRefCount);
        assertEquals(0, postDelete);
        assertEquals(0, postRefCount);
    }
    
    @Test
    public void testDeleteWithRefs()
    {
        Session db = DataActivator.getNewSession();
        
        db.beginTransaction();
        
        UserClass uc = new UserClass();
        uc.setName("UC_Test");
        uc.setPriority((short)1);
        uc.setTimeHorizon(0);
        db.persist(uc);
        
        UserClassKey key = new UserClassKey();
        key.setRemaining(1);
        key.setUserClass(uc);
        key.generateKey();
        db.persist(key);
        
        UserClassKeyConstraint keyConst = new UserClassKeyConstraint();
        keyConst.setName("Const_1");
        keyConst.setValue("Value_1");
        keyConst.setClassKey(key);
        db.persist(keyConst);
        
        UserClassKeyConstraint keyConst2 = new UserClassKeyConstraint();
        keyConst2.setName("Const_2");
        keyConst2.setValue("Value_2");
        keyConst2.setClassKey(key);
        db.persist(keyConst2);
        
        User user = new User();
        user.setName("uck_test");
        user.setNamespace("TEST");
        user.setPersona("USER");
        db.persist(user);
        
        User user2 = new User();
        user2.setName("uck_test2");
        user2.setNamespace("TEST");
        user2.setPersona("USER");
        db.persist(user2);
        
        UserClassKeyRedemption redemp = new UserClassKeyRedemption();
        redemp.setClassKey(key);
        redemp.setUser(user);
        db.persist(redemp);
        
        UserClassKeyRedemption redemp2 = new UserClassKeyRedemption();
        redemp2.setClassKey(key);
        redemp2.setUser(user2);
        db.persist(redemp2);
        
        db.getTransaction().commit();
        
        int preDelete = (Integer) db.createCriteria(UserClassKey.class)
                .add(Restrictions.eq("redeemKey", key.getRedeemKey()))
                .setProjection(Projections.rowCount())
                .uniqueResult();
        
        int preConstRefCount = (Integer) db.createCriteria(UserClassKeyConstraint.class)
                .add(Restrictions.eq("classKey.id", key.getId()))
                .setProjection(Projections.rowCount())
                .uniqueResult();
        
        int preRedempRefCount = (Integer) db.createCriteria(UserClassKeyRedemption.class)
                .add(Restrictions.eq("classKey.id", key.getId()))
                .setProjection(Projections.rowCount())
                .uniqueResult();

        
        this.dao.delete(this.dao.get(key.getId()));
        
        int postDelete = (Integer) db.createCriteria(UserClassKey.class)
                .add(Restrictions.eq("redeemKey", key.getRedeemKey()))
                .setProjection(Projections.rowCount())
                .uniqueResult();
        
        int postConstRefCount = (Integer) db.createCriteria(UserClassKeyConstraint.class)
                .add(Restrictions.eq("classKey.id", key.getId()))
                .setProjection(Projections.rowCount())
                .uniqueResult();
        
        int postRedempRefCount = (Integer) db.createCriteria(UserClassKeyRedemption.class)
                .add(Restrictions.eq("classKey.id", key.getId()))
                .setProjection(Projections.rowCount())
                .uniqueResult();
        
        db.beginTransaction();
        db.delete(user);
        db.delete(user2);
        db.delete(uc);
        db.getTransaction().commit();

        assertEquals(1, preDelete);
        assertEquals(2, preConstRefCount);
        assertEquals(2, preRedempRefCount);
        assertEquals(0, postDelete);
        assertEquals(0, postConstRefCount);
        assertEquals(0, postRedempRefCount);
    }
}
