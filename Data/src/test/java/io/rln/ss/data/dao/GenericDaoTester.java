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
 * @date 8th January 2010
 */
package io.rln.ss.data.dao;

import java.io.Serializable;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;

import io.rln.ss.data.DataActivator;
import io.rln.ss.data.dao.GenericDao;
import io.rln.ss.data.entities.Config;
import io.rln.ss.data.testsetup.DataAccessTestSetup;

/**
 * Tests the {@link GenericDao} class.
 */
public class GenericDaoTester extends TestCase
{
    /** Object of class under test. */
    private GenericDao<Config> dao;

    @Before
    @Override
    protected void setUp() throws Exception
    {
        DataAccessTestSetup.setup();
        this.dao = new GenericDao<Config>(Config.class);
        super.setUp();
    }

    @After
    @Override
    protected void tearDown() throws Exception
    {
        this.dao.closeSession();
        super.tearDown();
    }

    /**
     * Test method for {@link io.rln.ss.data.dao.GenericDao#persist(java.lang.Object)}.
     */
    public void testPersist()
    {
        String key = "persist_test_key";
        String val = "persist_test_val";
    
        Config conf = new Config(key, val);
        conf = this.dao.persist(conf);
        assertEquals(key, conf.getKey());
        assertEquals(val, conf.getValue());
        assertTrue(conf.getId() > 0);
        
        this.dao.closeSession();
        
        Session ses = DataActivator.getNewSession();
        Config loaded = (Config) ses.get(Config.class, conf.getId());
        assertNotNull(loaded);
        assertEquals(conf.getId(), loaded.getId());
        assertEquals(conf.getKey(), loaded.getKey());
        assertEquals(conf.getValue(), loaded.getValue());
        
        ses.beginTransaction();
        ses.delete(loaded);
        ses.getTransaction().commit();
    }

    /**
     * Test method for {@link io.rln.ss.data.dao.GenericDao#get(java.lang.Class, java.io.Serializable)}.
     */
    public void testGet()
    {
        String key = "get_test_key";
        String val = "get_test_val";
        Session ses = DataActivator.getNewSession();
        ses.beginTransaction();
        Config c = new Config(key, val);
        ses.save(c);
        ses.getTransaction().commit();
        ses.close();
        
        Config got = this.dao.get(c.getId());
        assertNotNull(got);
        assertEquals(c.getId(), got.getId());
        assertEquals(c.getKey(), got.getKey());
        assertEquals(c.getValue(), got.getValue());
        
        this.dao.delete(got);
    }

    /**
     * Test method for {@link io.rln.ss.data.dao.GenericDao#get(java.lang.Class, java.io.Serializable)}.
     */
    public void testGetNotFound()
    {
        assertNull(this.dao.get(134567890));
    }

    /**
     * Test method for {@link io.rln.ss.data.dao.GenericDao#merge(java.lang.Object)}.
     */
    public void testMerge()
    {
        Config conf = this.dao.persist(new Config("merge_conf_key", "merge_conf_val"));
        this.dao.closeSession();
        
        /* Conf is not transient. */
        GenericDao<Config> cDao = new GenericDao<Config>(Config.class);
        conf = cDao.merge(conf);
        assertNotNull(conf);
        
        conf.setValue("new_val");
        cDao.flush();
        cDao.closeSession();
        
        cDao = new GenericDao<Config>(Config.class);
        conf = cDao.get(conf.getId());
        assertEquals("new_val", conf.getValue());
        cDao.delete(conf);
    }

    /**
     * Test method for {@link io.rln.ss.data.dao.GenericDao#flush()}.
     */
    public void testFlush()
    {
        Config conf = this.dao.persist(new Config("flush_conf_key", "val"));
        assertNotNull(conf);
        
        /* Make dirty. */
        conf.setValue("new_val");
       
        Session ses = this.dao.getSession();
        assertNotNull(ses);
        assertTrue(ses.isDirty());
        
        /* Close which should flush. */
        this.dao.flush();
        assertFalse(ses.isDirty());
        
        ses = DataActivator.getNewSession();
        Config c = (Config)ses.load(Config.class, conf.getId());
        assertEquals("new_val", c.getValue());
        
        ses.getTransaction().begin();
        ses.delete(c);
        ses.getTransaction().commit();
    }

    /**
     * Test method for {@link io.rln.ss.data.dao.GenericDao#delete(java.lang.Object)}.
     */
    public void testDelete()
    {
        Session ses = DataActivator.getNewSession();
        ses.beginTransaction();
        Serializable id = ses.save(new Config("delete_conf_key", "delete_conf_val"));
        ses.getTransaction().commit();
        ses.close();
        
        ses = DataActivator.getNewSession();
        assertNotNull(ses.get(Config.class, id));
        ses.close();
        
        this.dao.delete(this.dao.get(id));
        
        ses = DataActivator.getNewSession();
        assertNull(ses.get(Config.class, id));
        ses.close();
    }
    
    /**
     * Test method for {@link io.rln.ss.data.dao.GenericDao#delete(java.lang.Object)}.
     */
    public void testDeleteId()
    {
        Session ses = DataActivator.getNewSession();
        ses.beginTransaction();
        Serializable id = ses.save(new Config("delete_conf_key", "delete_conf_val"));
        ses.getTransaction().commit();
        ses.close();
        
        ses = DataActivator.getNewSession();
        assertNotNull(ses.get(Config.class, id));
        ses.close();
        
        this.dao.delete(id);
        
        ses = DataActivator.getNewSession();
        assertNull(ses.get(Config.class, id));
        ses.close();
    }

    /**
     * Test method for {@link io.rln.ss.data.dao.GenericDao#closeSession()}.
     */
    public void testCloseSession()
    {
        Config conf = this.dao.persist(new Config("close_conf_key", "val"));
        assertNotNull(conf);
        
        /* Make dirty. */
        conf.setValue("new_val");
       
        Session ses = this.dao.getSession();
        assertNotNull(ses);
        assertTrue(ses.isOpen());
        assertTrue(ses.isDirty());
        
        /* Close which should flush. */
        this.dao.closeSession();
        assertFalse(ses.isOpen());
        
        ses = DataActivator.getNewSession();
        Config c = (Config)ses.load(Config.class, conf.getId());
        assertEquals("new_val", c.getValue());
        
        ses.beginTransaction();
        ses.delete(c);
        ses.getTransaction().commit();
    }

    /**
     * Test method for {@link io.rln.ss.data.dao.GenericDao#getSession()}.
     */
    public void testGetSession()
    {
        Session ses = DataActivator.getNewSession();
        GenericDao<Config> cDao = new GenericDao<Config>(ses, Config.class);
        assertEquals(ses, cDao.getSession());
    }

}
