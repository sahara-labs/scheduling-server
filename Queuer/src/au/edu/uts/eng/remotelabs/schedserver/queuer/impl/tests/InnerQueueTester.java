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
 * @date 2nd April 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.queuer.impl.tests;


import java.util.Date;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.queuer.impl.InnerQueue;

/**
 * Tests the {@link InnerQueue} class.
 */
public class InnerQueueTester extends TestCase
{ 
    /** Object of class under test. */
    private InnerQueue queue;

    @Override
    @Before
    public void setUp() throws Exception
    {
        this.queue = new InnerQueue("RIG", 10, "rig1");
    }
    
    @Test
    public void testQueue()
    {
        Session ses1 = new Session();
        ses1.setPriority((short)10);
        ses1.setRequestTime(new Date(System.currentTimeMillis() - 1000));
        this.queue.add(ses1);
        
        Session ses2 = new Session();
        ses2.setPriority((short)1);
        ses2.setRequestTime(new Date(System.currentTimeMillis() - 1000));
        this.queue.add(ses2);
        
        Session ses3 = new Session();
        ses3.setPriority((short)10);
        ses3.setRequestTime(new Date(System.currentTimeMillis() - 10000));
        this.queue.add(ses3);
        
        Session ses4 = new Session();
        ses4.setPriority((short)5);
        ses4.setRequestTime(new Date(System.currentTimeMillis() - 1000));
        this.queue.add(ses4);
        
        Session ses5 = new Session();
        ses5.setPriority((short)10);
        ses5.setRequestTime(new Date(System.currentTimeMillis() - 5000));
        this.queue.add(ses5);
        
        Session ses6 = new Session();
        ses6.setPriority((short)5);
        ses6.setRequestTime(new Date(System.currentTimeMillis() - 10000));
        this.queue.add(ses6);
        
        assertEquals(6, this.queue.size());
        assertEquals(ses2, this.queue.getHead());
        assertEquals(ses6, this.queue.getHead());
        assertEquals(ses4, this.queue.getHead());
        assertEquals(ses3, this.queue.getHead());
        assertEquals(ses5, this.queue.getHead());
        assertEquals(ses1, this.queue.getHead());
        assertEquals(0, this.queue.size());
    }
    
    @Test
    public void testQueueRemoval()
    {
        Session ses1 = new Session();
        ses1.setId(1L);
        ses1.setPriority((short)10);
        ses1.setRequestTime(new Date(System.currentTimeMillis() - 1000));
        this.queue.add(ses1);
        
        Session ses2 = new Session();
        ses2.setId(2L);
        ses2.setPriority((short)1);
        ses2.setRequestTime(new Date(System.currentTimeMillis() - 1000));
        this.queue.add(ses2);
        
        Session ses3 = new Session();
        ses3.setId(3L);
        ses3.setPriority((short)10);
        ses3.setRequestTime(new Date(System.currentTimeMillis() - 10000));
        this.queue.add(ses3);
        
        Session ses4 = new Session();
        ses4.setId(4L);
        ses4.setPriority((short)5);
        ses4.setRequestTime(new Date(System.currentTimeMillis() - 1000));
        this.queue.add(ses4);
        
        Session ses5 = new Session();
        ses5.setId(5L);
        ses5.setPriority((short)10);
        ses5.setRequestTime(new Date(System.currentTimeMillis() - 5000));
        this.queue.add(ses5);
        
        Session ses6 = new Session();
        ses6.setId(6L);
        ses6.setPriority((short)5);
        ses6.setRequestTime(new Date(System.currentTimeMillis() - 10000));
        this.queue.add(ses6);
        
        assertEquals(6, this.queue.size());
        this.queue.remove(ses6);
        assertEquals(5, this.queue.size());
        assertEquals(ses2, this.queue.getHead());
        assertEquals(ses4, this.queue.getHead());
        this.queue.add(ses6);
        assertEquals(ses6, this.queue.getHead());
        assertEquals(ses3, this.queue.getHead());
        assertEquals(ses5, this.queue.getHead());
        assertEquals(ses1, this.queue.getHead());
        assertEquals(0, this.queue.size());
    }
    
    @Test
    public void testQueueContains()
    {
        Session ses1 = new Session();
        ses1.setId(1L);
        ses1.setPriority((short)10);
        ses1.setRequestTime(new Date(System.currentTimeMillis() - 1000));
        this.queue.add(ses1);
        
        Session ses2 = new Session();
        ses2.setId(2L);
        ses2.setPriority((short)1);
        ses2.setRequestTime(new Date(System.currentTimeMillis() - 1000));
        this.queue.add(ses2);
        
        Session ses3 = new Session();
        ses3.setId(3L);
        ses3.setPriority((short)10);
        ses3.setRequestTime(new Date(System.currentTimeMillis() - 10000));
        this.queue.add(ses3);
        
        Session ses4 = new Session();
        ses4.setId(4L);
        ses4.setPriority((short)5);
        ses4.setRequestTime(new Date(System.currentTimeMillis() - 1000));
        this.queue.add(ses4);
        
        Session ses5 = new Session();
        ses5.setId(5L);
        ses5.setPriority((short)10);
        ses5.setRequestTime(new Date(System.currentTimeMillis() - 5000));
        this.queue.add(ses5);
        
        Session ses6 = new Session();
        ses6.setId(6L);
        ses6.setPriority((short)5);
        ses6.setRequestTime(new Date(System.currentTimeMillis() - 10000));
        this.queue.add(ses6);
        
        
        assertTrue(this.queue.contains(ses3));
        this.queue.remove(ses3);
        assertFalse(this.queue.contains(ses3));
    }
    
    @Test
    public void testQueueDifferentPriorities()
    {
        Session ses[] = new Session[100];
        for (int i = 0; i < ses.length; i++)
        {
            ses[i] = new Session();
            ses[i].setPriority((short)(100 - i));
            ses[i].setRequestTime(new Date(System.currentTimeMillis() + i * 1000));
            this.queue.add(ses[i]);
        }
        
        for (int i = 0; i < ses.length; i++)
        {
            assertEquals(this.queue.getHead(), ses[99 - i]);
        }
    }
    
    @Test
    public void testQueuePosition()
    {
        Session ses[] = new Session[100];
        for (int i = 0; i < ses.length; i++)
        {
            ses[i] = new Session();
            ses[i].setId(Long.valueOf(i));
            ses[i].setPriority((short)(100 - i));
            ses[i].setRequestTime(new Date(System.currentTimeMillis() + i * 1000));
            this.queue.add(ses[i]);
        }
        
        assertEquals(100, this.queue.position(ses[0]));
        assertEquals(1, this.queue.position(ses[99]));
        assertEquals(5, this.queue.position(ses[95]));
        assertEquals(90, this.queue.position(ses[10]));
        
        Session s = new Session();
        s.setId(1001L);
        s.setPriority((short)4);
        s.setRequestTime(new Date());
        assertEquals(-1, this.queue.position(s));
    }
    
    @Test
    public void testQueueNumberBefore()
    {
        Session ses[] = new Session[100];
        for (int i = 0; i < ses.length; i++)
        {
            ses[i] = new Session();
            ses[i].setId(Long.valueOf(i));
            ses[i].setPriority((short)(100 - i));
            ses[i].setRequestTime(new Date(System.currentTimeMillis() + i * 1000));
            this.queue.add(ses[i]);
        }
        
        Session s = new Session();
        s.setId(101L);
        s.setPriority((short)10);
        s.setRequestTime(new Date());
        assertEquals(9, this.queue.numberBefore(s));
        
        s.setPriority((short)1);
        s.setRequestTime(new Date());
        assertEquals(0, this.queue.numberBefore(s));
        
        s.setPriority((short)1);
        s.setRequestTime(new Date(System.currentTimeMillis() + 1000000));
        assertEquals(1, this.queue.numberBefore(s));
    }
    
    @Test
    public void testQueuePeek()
    {
        Session ses[] = new Session[100];
        for (int i = 0; i < ses.length; i++)
        {
            ses[i] = new Session();
            ses[i].setPriority((short)(100 - i));
            ses[i].setRequestTime(new Date(System.currentTimeMillis() + i * 1000));
            this.queue.add(ses[i]);
        }
        
        for (int i = 0; i < ses.length; i++)
        {
            assertEquals(this.queue.peekHead(), ses[99]);
        }
    }
    
    public void testQueueDifferentRequestTimes()
    {
        Session ses[] = new Session[1000];
        for (int i = 0; i < ses.length; i++)
        {
            ses[i] = new Session();
            ses[i].setPriority((short)(10));
            ses[i].setRequestTime(new Date(System.currentTimeMillis() - i * 1000));
            this.queue.add(ses[i]);
        }
        
        for (int i = 0; i < ses.length; i++)
        {
            assertEquals(this.queue.getHead(), ses[999 - i]);
        }
    }
}
