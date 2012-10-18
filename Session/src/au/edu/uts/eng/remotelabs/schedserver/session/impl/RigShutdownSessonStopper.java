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
 * @date 27th August 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.session.impl;

import java.util.Date;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.RigEventListener;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.SessionEventListener.SessionEvent;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.session.SessionActivator;

/**
 * Receives notifications when a rig is being shutdown and if that is assigned
 * to a session, the session is terminated.
 */
public class RigShutdownSessonStopper implements RigEventListener
{
    /** Logger. */
    private Logger logger;
    
    public RigShutdownSessonStopper()
    {
        this.logger = LoggerActivator.getLogger();
    }

    @Override
    public void eventOccurred(RigStateChangeEvent event, Rig rig, org.hibernate.Session db)
    {
        try
        {
            if (event == RigStateChangeEvent.REMOVED)
            {
                /* Rig has shutdown, so will need to terminate its assigned
                 * session, if it has one. */
                Criteria qu = db.createCriteria(Session.class);
                qu.add(Restrictions.eq("rig", rig))
                  .add(Restrictions.eq("active", true));
                
                /* Whilst the above criteria are not enforced by a underlying 
                 * schema, concecptually there should only ever the one session
                 * the rig is assigned to. */
                Session ses = (Session)qu.uniqueResult();
                if (ses == null)
                {
                    this.logger.debug("A session is not active for rig " + rig.getName() + " so there is no need to " +
                    		"terminate a session.");
                    return;
                }
                
                this.logger.info("Session for " + ses.getUserNamespace() + ':' + ses.getUserName() + " on " +
                        "rig " + ses.getAssignedRigName() + " is being terminated because the rig has shutdown.");
                
                /* Session exists so terminate it. */
                ses.setActive(false);
                ses.setRemovalTime(new Date());
                ses.setRemovalReason("Rig " + rig.getName() + " has shutdown.");
                db.beginTransaction();
                db.flush();
                db.getTransaction().commit();
                
                SessionActivator.notifySessionEvent(SessionEvent.FINISHED, ses, db);
            }
        }
        catch (HibernateException ex)
        {
            db.getTransaction().rollback();
            this.logger.error("Failed to commit change to terminate session of rig " + rig.getName() + ", messsage: " + 
                    ex.getMessage() + '.');
        }
    }

}
