/**
 * SAHARA Rig Client
 * 
 * Software abstraction of physical rig to provide rig session control
 * and rig device control. Automatically tests rig hardware and reports
 * the rig status to ensure rig goodness.
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
 * @author <First> <Last> (tmachet)
 * @date <Day> <Month> 2010
 *
 * Changelog:
 * - 22/12/2010 - tmachet - Initial file creation.
 */
package au.edu.uts.eng.remotelabs.schedserver.reports.intf.tests;

import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.reports.impl.UserRecord;

/**
 * @author tmachet
 *
 */
public class UserRecordTester extends TestCase
{

    private UserRecord record;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        this.record = new UserRecord();
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.reports.impl.UserRecord#addRecord(au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session)}.
     */
    public void testAddRecord()
    {
        Date request = new Date(System.currentTimeMillis() - 30000);
        Date assign = new Date(System.currentTimeMillis() + 20000);
        Date remove = new Date(System.currentTimeMillis() + 70000);
        Date after = new Date(System.currentTimeMillis() + 100000);
        Date request2 = new Date(System.currentTimeMillis() - 40000);
        Date assign2 = new Date(System.currentTimeMillis() + 30000);
        Date remove2 = new Date(System.currentTimeMillis() + 80000);
        Date after2 = new Date(System.currentTimeMillis() + 110000);

        short sh = 1;
        
        Session ses1 = new Session("test1", "REPS", request, "RIG", "Rig1", sh, after, "Rig1", assign, true, remove,
                "ended", "none", sh);
        Session ses2 = new Session("test1", "REPS", request2, "RIG", "Rig1", sh, after2, "Rig1", assign2, true, remove2,
                "ended", "none", sh);
        
        this.record.addRecord(ses1);
        
        assertEquals(this.record.queueDuration.size(),1);
        assertEquals(this.record.sessionDuration.size(),1);

        this.record.addRecord(ses2);
        assertEquals(this.record.queueDuration.size(),2);
        assertEquals(this.record.sessionDuration.size(),2);
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.reports.impl.UserRecord#getAverageQueueDuration()}.
     */
    public void testGetMethods()
    {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, 1);

        // Queue time 60 min
        // Session time 120 min
        Date request = cal.getTime();
        cal.add(Calendar.HOUR, 1);
        Date assign = cal.getTime();
        cal.add(Calendar.HOUR, 2);
        Date remove = cal.getTime();
        cal.add(Calendar.HOUR, 1);
        Date after = cal.getTime();
        
        // Queue time 30min
        // Session time 60 min
        cal.add(Calendar.MINUTE, 30);
        Date request2 = cal.getTime();
        cal.add(Calendar.MINUTE, 30);
        Date assign2 = cal.getTime();
        cal.add(Calendar.MINUTE, 60);
        Date remove2 = cal.getTime();
        cal.add(Calendar.MINUTE, 30);
        Date after2 = cal.getTime();

        // Queue time 0 hr
        // Session time 40 min
        cal.add(Calendar.MINUTE, 30);
        Date request3 = cal.getTime();
        Date assign3 = cal.getTime();
        cal.add(Calendar.MINUTE, 40);
        Date remove3 = cal.getTime();
        cal.add(Calendar.MINUTE, 30);
        Date after3 = cal.getTime();

        // Queue time 20 min
        // Session time 0 min
        cal.add(Calendar.MINUTE, 30);
        Date request4 = cal.getTime();
        Date assign4 = null;
        cal.add(Calendar.MINUTE, 20);
        Date remove4 = cal.getTime();
        cal.add(Calendar.MINUTE, 30);
        Date after4 = cal.getTime();

        short sh = 1;
        
        Session ses1 = new Session("test1", "REPS", request, "RIG", "Rig1", sh, after, "Rig1", assign, true, remove,
                "ended", "none", sh);
        Session ses2 = new Session("test1", "REPS", request2, "RIG", "Rig1", sh, after2, "Rig1", assign2, true, remove2,
                "ended", "none", sh);
        Session ses3 = new Session("test1", "REPS", request3, "RIG", "Rig1", sh, after3, "Rig1", assign3, true, remove3,
                "ended", "none", sh);
        Session ses4 = new Session("test1", "REPS", request4, "RIG", "Rig1", sh, after4, "Rig1", assign4, true, remove4,
                "ended", "none", sh);
        
        this.record.addRecord(ses1);
        this.record.addRecord(ses2);
        this.record.addRecord(ses3);
        this.record.addRecord(ses4);
        
        int aveQ = this.record.getAverageQueueDuration();
        int aveS = this.record.getAverageSessionDuration();
        int minQ = this.record .getMinimumQueueDuration();
        int minS = this.record.getMinimumSessionDuration();
        int maxQ = this.record .getMaximumQueueDuration();
        int maxS = this.record.getMaximumSessionDuration();
        int medQ = this.record .getMedianQueueDuration();
        int medS = this.record.getMedianSessionDuration();
        assertEquals(aveQ,275*6);
        assertEquals(aveS,55*60);
        assertEquals(minQ,0);
        assertEquals(minS,0);
        assertEquals(maxQ,60*60);
        assertEquals(maxS,120*60);
        assertEquals(medQ,25*60);
        assertEquals(medS,50*60);
    }


}
