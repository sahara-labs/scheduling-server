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
import au.edu.uts.eng.remotelabs.schedserver.queuer.impl.QueueSessionComparator;

/**
 * Tests the {@link QueueSessionComparator} class.
 */
public class QueueSessionComparatorTester extends TestCase
{
    /** Object of class under test. */
    private QueueSessionComparator comparator;

    @Override
    @Before
    public void setUp() throws Exception
    {
        this.comparator = new QueueSessionComparator();
    }

    @Test
    public void testCompare()
    {
        Session ses1 = new Session();
        ses1.setPriority((short)3);
        ses1.setRequestTime(new Date(System.currentTimeMillis() + 1000));
        
        Session ses2 = new Session();
        ses2.setPriority((short)5);
        ses2.setRequestTime(new Date(System.currentTimeMillis() - 1000));
        
        assertTrue(this.comparator.compare(ses1, ses2) < 0);
    }
    
    @Test
    public void testCompareHigherPri()
    {
        Session ses1 = new Session();
        ses1.setPriority((short)6);
        ses1.setRequestTime(new Date(System.currentTimeMillis() + 1000));
        
        Session ses2 = new Session();
        ses2.setPriority((short)5);
        ses2.setRequestTime(new Date(System.currentTimeMillis() - 1000));
        
        assertTrue(this.comparator.compare(ses1, ses2) > 0);
    }

    @Test
    public void testCompareEqualPri()
    {
        Session ses1 = new Session();
        ses1.setPriority((short)5);
        ses1.setRequestTime(new Date(System.currentTimeMillis() - 1000));
        
        Session ses2 = new Session();
        ses2.setPriority((short)5);
        ses2.setRequestTime(new Date(System.currentTimeMillis() + 1000));
        
        assertTrue(this.comparator.compare(ses1, ses2) < 0);
    }
    
    @Test
    public void testCompareEqualPriOlder()
    {
        Session ses1 = new Session();
        ses1.setPriority((short)5);
        ses1.setRequestTime(new Date(System.currentTimeMillis() + 1000));
        
        Session ses2 = new Session();
        ses2.setPriority((short)5);
        ses2.setRequestTime(new Date(System.currentTimeMillis() - 1000));
        
        assertTrue(this.comparator.compare(ses1, ses2) > 0);
    }
}
