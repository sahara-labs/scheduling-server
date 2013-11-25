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
 * @date 14th April 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.session.impl;

import java.util.Date;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.SessionDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.rigoperations.RigReleaser;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.RigClientAsyncService;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.RigClientAsyncServiceCallbackHandler;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.ActivityDetectableType;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.IsActivityDetectableResponse;

/**
 * Kicks off users who have time expired.
 */
public class SessionIdleKicker extends RigClientAsyncServiceCallbackHandler
{
    /** Session. */
    private Session session;
    
    /** Logger. */
    private Logger logger;
    
    private boolean notTest = true;
    
    public SessionIdleKicker()
    {
        this.logger = LoggerActivator.getLogger();
    }
    
    public void kickIfIdle(Session ses, org.hibernate.Session db)
    {
        this.session = ses;
        Rig rig = ses.getRig();
        
        try
        {
            RigClientAsyncService service = new RigClientAsyncService(rig.getName(), db);
            service.isActivityDetectable(this);
        }
        catch (Exception e)
        {
            this.logger.error("Failed calling rig client actiivty detection from " + rig.getName() + " at " + 
                    rig.getContactUrl() + " because of error " + e.getMessage() + ".");

            /* Put the rig offline. */
            rig.setInSession(false);
            rig.setOnline(false);
            rig.setOfflineReason("Activity detection failed for session " + ses.getId() + ".");
            db.beginTransaction();
            db.flush();
            db.getTransaction().commit();
        }
    }
    
    @Override
    public void activityDetectionResponseCallback(final IsActivityDetectableResponse response)
    {
        SessionDao dao = new SessionDao();
        Session ses = dao.merge(this.session);
        
        ActivityDetectableType act = response.getIsActivityDetectableResponse();
        if (act.getActivity())
        {
            /* If there is activty, update the activity time stamp. */
            ses.setActivityLastUpdated(new Date());
            dao.flush();
        }
        else
        {
            /* If not terminate the session. */
            ses.setActive(false);
            ses.setRemovalReason("Idle timeout.");
            ses.setRemovalTime(new Date());
            dao.flush();
            
            if (this.notTest) new RigReleaser().release(ses, dao.getSession());
        }
        
        dao.closeSession();
    }
    
    @Override
    public void activityDetectionErrorCallback(final Exception e)
    {
        RigDao dao = new RigDao();
        Rig rig = dao.merge(this.session.getRig());
        
        this.logger.error("Received error response from is activity detection of rig " + rig.getName() + " at " 
                + rig.getContactUrl() + ". Error message" + " is " + e.getMessage() + '.');
        
        /* Activity detection failed so take the rig offline. */
        rig.setOnline(false);
        rig.setOfflineReason("Activity detection failed with reason " + e.getMessage() + '.');
        dao.flush();
        
        dao.closeSession();
    }
}
