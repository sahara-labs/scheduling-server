/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2011, Michael Diponio
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
 * @date 6th August 2011
 */
package au.edu.uts.eng.remotelabs.schedserver.session.pojo.impl;

import java.util.Date;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.SessionEventListener.SessionEvent;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.session.SessionActivator;
import au.edu.uts.eng.remotelabs.schedserver.session.pojo.SessionService;

/**
 * Implementation of the Session POJO service.
 */
public class SessionServiceImpl implements SessionService
{
    /** Logger. */
    private Logger logger;

    public SessionServiceImpl()
    {
        this.logger = LoggerActivator.getLogger();
    }
    
    @Override
    public boolean finishSession(Session ses, String reason, org.hibernate.Session db)
    {
        /* Only sessions that are active and in session will be finished. */
        if (!ses.isActive() || ses.getAssignmentTime() == null)
        {
            this.logger.warn("Session '" + ses.getId() + "' not assigned to rig so will not be finished.");
            return false;
        }
       
        /* Finish session. */
        this.logger.debug("Finishing session '" + ses.getId() + "' with reason: " + reason);
        db.beginTransaction();
        ses.setActive(false);
        ses.setRemovalReason(reason);
        ses.setRemovalTime(new Date());
        db.flush();
        db.getTransaction().commit();
        
        /* Fire session finished event. */
        SessionActivator.notifySessionEvent(SessionEvent.FINISHED, ses, db);

        /* Local session, release the rig. */
        SessionActivator.release(ses, db);        
        return true;
    }

}
