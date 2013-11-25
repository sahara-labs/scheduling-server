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
 * @date 4th April 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.queuer.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;

/**
 * Task that periodically removes stale rig queue sessions. Stale sessions are
 * those that have the current time minus the last activity time greater the 
 * resource permission time-out interval.
 * <br />
 * Code assigned queue sessions (batch sessions) are not ever considered 
 * stale. 
 */
public class QueueStaleSessionTask implements Runnable
{
    /** Logger. */
    private Logger logger;
    
    public QueueStaleSessionTask()
    {
        this.logger = LoggerActivator.getLogger();
        this.logger.debug("Starting the queue stale session task, which removes stale queue sessions (those that " +
        		" exceed the permission queue activity timeout.");
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void run()
    {   
        org.hibernate.Session db = null;
        try
        {
            db = DataAccessActivator.getNewSession();
            Date now = new Date();
            
            Criteria query = db.createCriteria(Session.class);
            query.add(Restrictions.eq("active", Boolean.TRUE)) // Session must be active.
                 .add(Restrictions.isNull("assignmentTime"))   // Only sessions in the queue
                 .add(Restrictions.isNull("codeReference"));   // Only interactive sessions
            List<Session> sessions = query.list();
            
            /* For each of the sessions, if any of the sessions are stale, remove
             * them from queue. */
            for (Session s : sessions)
            {
                if ((System.currentTimeMillis() - s.getActivityLastUpdated().getTime()) / 1000 > 
                        s.getResourcePermission().getQueueActivityTimeout())
                {
                    this.logger.warn("Removing stale queue session with id=" + s.getId() + ". Last activity at" +
                            s.getActivityLastUpdated().toString() + ", current time is " + now.toString() + ".");
                    
                    Queue.getInstance().removeEntry(s, db);
                    s.setActive(false);
                    s.setRemovalReason("Queue activity timeout.");
                    s.setRemovalTime(now);
                    db.beginTransaction();
                    db.flush();
                    db.getTransaction().commit();
                }
            }
        }
        catch (HibernateException hex)
        {
            this.logger.error("Failed to query database to check stale sessions (Exception: " + 
                    hex.getClass().getName() + ", Message:" + hex.getMessage() + ").");
            
            if (db != null && db.getTransaction() != null)
            {
                try
                {
                    db.getTransaction().rollback();
                }
                catch (HibernateException ex)
                {
                    this.logger.error("Exception rolling back up stale session transaction (Exception: " + 
                            ex.getClass().getName() + "," + " Message: " + ex.getMessage() + ").");
                }
            }
        }
        finally
        {
            try
            {
                if (db != null) db.close();
            }
            catch (HibernateException ex)
            {
                this.logger.error("Exception cleaning up database session (Exception: " + ex.getClass().getName() + "," +
                		" Message: " + ex.getMessage() + ").");
            }
        }
    }

}
