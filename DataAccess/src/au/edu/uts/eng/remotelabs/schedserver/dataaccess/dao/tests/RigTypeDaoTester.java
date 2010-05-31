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
 * @date 11th January 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.tests;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigTypeDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.tests.DataAccessTestSetup;

/**
 * Tests the {@link RigTypeDao} class.
 */
public class RigTypeDaoTester extends TestCase
{
    /** Object of class under test. */
    private RigTypeDao dao;
    
    @Override
    @Before
    public void setUp() throws Exception
    {
        DataAccessTestSetup.setup();
        this.dao = new RigTypeDao();
        super.setUp();
    }

    @Override
    @After
    public void tearDown() throws Exception
    {
        this.dao.closeSession();
        super.tearDown();
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigTypeDao#findByName(java.lang.String)}.
     */
    @Test
    public void testFindByName()
    {
        Session ses = DataAccessActivator.getNewSession();
        RigType type = new RigType();
        type.setName("testType");
        type.setLogoffGraceDuration(600);
        ses.beginTransaction();
        ses.save(type);
        ses.getTransaction().commit();
        ses.close();
        
        RigType rigType = this.dao.findByName("testType");
        assertNotNull(rigType);
        assertEquals(type.getId(), rigType.getId());
        assertEquals(type.getLogoffGraceDuration(), rigType.getLogoffGraceDuration());
        
        this.dao.delete(rigType);
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigTypeDao#findByName(String)}.
     */
    @Test
    public void testFindByNameNotFound()
    {
        RigType type = this.dao.findByName("does_not_exist");
        assertNull(type);
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigTypeDao#loadOrCreate(String)}
     */
    @Test
    public void testLoadOrCreateExists()
    {
        Session ses = DataAccessActivator.getNewSession();
        RigType type = new RigType();
        type.setName("testType");
        type.setLogoffGraceDuration(600);
        type.setCodeAssignable(true);
        ses.beginTransaction();
        ses.save(type);
        ses.getTransaction().commit();
        ses.close();
        
     
        RigType same = this.dao.loadOrCreate("testType");
        assertNotNull(same);
        assertEquals("testType", same.getName());
        assertTrue(same.isCodeAssignable());
        assertEquals(600, same.getLogoffGraceDuration());
        
        this.dao.delete(same);
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigTypeDao#loadOrCreate(String)}
     */
    @Test
    public void testLoadOrCreateNotExist()
    {
        RigType type = this.dao.loadOrCreate("testType");
        assertNotNull(type);
        assertEquals("testType", type.getName());
        assertFalse(type.isCodeAssignable());
        assertEquals(180, type.getLogoffGraceDuration());
        
        Session ses = DataAccessActivator.getNewSession();
        RigType same = (RigType) ses.load(RigType.class, type.getId());
        
        assertEquals(type.getName(), same.getName());
        assertEquals(type.getLogoffGraceDuration(), same.getLogoffGraceDuration());
        assertEquals(type.isCodeAssignable(), same.isCodeAssignable());
        
        this.dao.delete(type);
    }

}
