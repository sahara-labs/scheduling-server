/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2009, University of Technology, Sydney
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
 * @author Tania Machet (tmachet)
 * @date 22nd December 2010
 */

package au.edu.uts.eng.remotelabs.schedserver.reports.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;

/**
 * Generates statistics about user sessions.
 */
public class SessionStatistics
{
    /** List of queue durations. */
    public List<Integer> queueDuration;
    
    /** List of session durations. */
    public List<Integer> sessionDuration;
    
    /** Number of sessions added. */
    public int sessionCount;
    
    public SessionStatistics()
    {
        this.queueDuration = new ArrayList<Integer>();
        this.sessionDuration = new ArrayList<Integer>();
        this.sessionCount = 0;
    }

    /**
     * Adds a session to statistics set.
     * 
     * @param session session to add
     */
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
        this.sessionCount++;
    }
    
    /**
     * Gets the average queue duration of the sessions in this set in seconds.
     * 
     * @return average queue duration
     */
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

    /**
     * Gets the average session duration of the sessions in this set in seconds.
     * 
     * @return average session duration
     */
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

    /**
     * Gets the shortest queue duration in seconds.
     * 
     * @return minimum queue duration
     */
    public int getMinimumQueueDuration()
    {
        return Collections.min(this.queueDuration);
    }

    /**
     * Gets the shortest session duration in seconds.
     * 
     * @return minimum session duration
     */
    public int getMinimumSessionDuration()
    {
        return Collections.min(this.sessionDuration);
    }

    /**
     * Gets the maximum queue duration in seconds.
     * 
     * @return maximum queue duration
     */
    public int getMaximumQueueDuration()
    {
        return Collections.max(this.queueDuration);
    }

    /**
     * Gets the maximum session duration in seconds.
     * 
     * @return maximum session duration
     */
    public int getMaximumSessionDuration()
    {
        return Collections.max(this.sessionDuration);
    }
    
    /**
     * Gets the median queue duration in seconds.
     * 
     * @return median queue duration
     */
    public int getMedianQueueDuration()
    {
        int mid = this.queueDuration.size()/2;
        Collections.sort(this.queueDuration);
        
        if(this.queueDuration.size() % 2 == 1)   return this.queueDuration.get(mid);
        
        return (this.queueDuration.get(mid-1) + this.queueDuration.get(mid))/2;
    }
    
    /**
     * Gets the median session duration.
     * 
     * @return median session duration
     */
    public int getMedianSessionDuration()
    {
        int mid = this.sessionDuration.size()/2;
        Collections.sort(this.sessionDuration);
        
        if(this.sessionDuration.size() % 2 == 1)   return this.sessionDuration.get(mid);
        
        return (this.sessionDuration.get(mid-1) + this.sessionDuration.get(mid))/2;
    }

    /**
     * Gets the total queue duration in seconds.
     * 
     * @return total queue duration
     */
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

    /**
     * Gets the total session duration in seconds.
     * 
     * @return total session duration
     */
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
    
    /**
     * Returns the session count.
     * 
     * @return session count
     */
    public int getSessionCount()
    {
        return this.sessionCount;
    }
}
