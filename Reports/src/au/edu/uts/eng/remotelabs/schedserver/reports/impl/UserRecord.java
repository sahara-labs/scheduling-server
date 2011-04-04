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
package au.edu.uts.eng.remotelabs.schedserver.reports.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;

/**
 * @author tmachet
 *
 */
public class UserRecord
{
    public List<Integer> queueDuration;
    public List<Integer> sessionDuration;
    public int userRecordCount;
    
    public UserRecord()
    {
        this.queueDuration = new ArrayList<Integer>();
        this.sessionDuration = new ArrayList<Integer>();
        this.userRecordCount = 0;
    }

    public void addRecord(Session session)
    {
        int queueD;
        int sessionD;
        
        if(session.getAssignmentTime() != null)
        {
            queueD = (int) ((session.getAssignmentTime().getTime() - session.getRequestTime().getTime())/1000);
            sessionD = (int) ((session.getRemovalTime().getTime() - session.getAssignmentTime().getTime())/1000);
        }
        else
        {
            queueD = (int) ((session.getRemovalTime().getTime() - session.getRequestTime().getTime())/1000);
            sessionD = 0;
        }

        this.queueDuration.add(queueD);
        this.sessionDuration.add(sessionD);
        this.userRecordCount++;
    }
    
    public int getAverageQueueDuration()
    {
        int tot = 0;
        
        Iterator<Integer> queueIt = this.queueDuration.iterator();
        while (queueIt.hasNext())
        {
            Integer queue = queueIt.next();
            tot += queue;
        }
        
        if (this.queueDuration.size() == 0)  return 0;
        return tot/this.queueDuration.size();
    }

    public int getAverageSessionDuration()
    {
        int tot = 0;
        
        Iterator<Integer> sessionIt = this.sessionDuration.iterator();
        while (sessionIt.hasNext())
        {
            Integer session = sessionIt.next();
            tot += session;
        }
        
        if (this.sessionDuration.size() == 0)  return 0;
        return tot/this.sessionDuration.size();
    }

    public int getMinimumQueueDuration()
    {
        return Collections.min(this.queueDuration);
    }

    public int getMinimumSessionDuration()
    {
        return Collections.min(this.sessionDuration);
    }

    public int getMaximumQueueDuration()
    {
        return Collections.max(this.queueDuration);
    }

    public int getMaximumSessionDuration()
    {
        return Collections.max(this.sessionDuration);
    }
    
    public int getMedianQueueDuration()
    {
        int mid = this.queueDuration.size()/2;
        Collections.sort(this.queueDuration);
        
        if(this.queueDuration.size() % 2 == 1)   return this.queueDuration.get(mid);
        
        return (this.queueDuration.get(mid-1) + this.queueDuration.get(mid))/2;
    }
    
    public int getMedianSessionDuration()
    {
        int mid = this.sessionDuration.size()/2;
        Collections.sort(this.sessionDuration);
        
        if(this.sessionDuration.size() % 2 == 1)   return this.sessionDuration.get(mid);
        
        return (this.sessionDuration.get(mid-1) + this.sessionDuration.get(mid))/2;
        
    }

    public int getTotalQueueDuration()
    {
        int tot = 0;
        
        Iterator<Integer> queueIt = this.queueDuration.iterator();
        while (queueIt.hasNext())
        {
            Integer session = queueIt.next();
            tot += session;
        }
        
        return tot;
    }

    public int getTotalSessionDuration()
    {
        int tot = 0;
        
        Iterator<Integer> sessionIt = this.sessionDuration.iterator();
        while (sessionIt.hasNext())
        {
            Integer session = sessionIt.next();
            tot += session;
        }
        
        return tot;
    }

}