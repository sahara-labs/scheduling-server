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
package io.rln.ss.data.dao;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.rln.ss.data.DataActivator;
import io.rln.ss.data.dao.ConfigDao;
import io.rln.ss.data.dao.GenericDao;
import io.rln.ss.data.entities.Config;
import io.rln.ss.data.testsetup.DataAccessTestSetup;

/**
 * Tests the {@link GenericDao} class.
 */
public class ConfigDaoTester extends TestCase
{
    /** Object of class under test. */
    private ConfigDao dao;
    
    @Before
    @Override
    public void setUp() throws Exception
    {
        DataAccessTestSetup.setup();
        this.dao = new ConfigDao();
    }
    
    @Test
    public void testCreate()
    {
        String key = "testkey";
        String val = "testval";
    
        Config conf = this.dao.create(key, val);
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
    
    @Test
    public void testGetConfig()
    {
        Config p1 = this.dao.persist(new Config("conf_test_key", "val1"));
      
        Config conf = this.dao.getConfig("conf_test_key");
        assertNotNull(conf);
        assertEquals(p1.getKey(), conf.getKey());
        assertEquals(p1.getValue(), conf.getValue());
        this.dao.delete(p1);
    }
    
    @Test
    public void testGetConfigNotFound()
    {
        Config conf = this.dao.getConfig("does_not_exist");
        assertNull(conf);
    }
    
    @Override
    @After
    public void tearDown()
    {
        this.dao.closeSession();
    }
}
