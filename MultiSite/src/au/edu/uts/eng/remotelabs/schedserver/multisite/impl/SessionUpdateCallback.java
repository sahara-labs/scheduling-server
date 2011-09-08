/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2011, University of Technology, Sydney
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
 * @date 30th January 2011
 */
package au.edu.uts.eng.remotelabs.schedserver.multisite.impl;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.multisite.MultiSiteActivator;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.callback.client.MultiSiteCallbackClientHandler;
import au.edu.uts.eng.remotelabs.schedserver.session.pojo.SessionService;

/**
 * 
 */
public class SessionUpdateCallback extends MultiSiteCallbackClientHandler
{
    /** Session that is being updated. */
    private final Session session;
    
    /** Logger. */
    private final Logger logger;

    public SessionUpdateCallback(Session session)
    {
        this.logger = LoggerActivator.getLogger();
        this.session = session;
    }
    
    @Override
    public void receiveResponseSessionUpdate(boolean successful, String reason)
    {
        if (successful)
        {
            this.logger.debug("Consumer site successful acknowledged session update for session '" + 
                    this.session.getId() + "'.");
        }
        else
        {
            this.logger.warn("Consumer site did not successfully acknowledge session update for session '" + 
                    this.session.getId() + "' with reason: " + reason + ". Session will be terminated.");
            this.terminateSession();
        }
    }
    
    @Override
    public void receiveErrorSessionUpdate(Exception e)
    {
        this.logger.warn("Unable to provide consumer notification of session termination of '" + 
                    this.session.getId() + "'. Exception " + e.getClass().getSimpleName() + ": " + e.getMessage());
        this.terminateSession();
    }
    
    /** 
     * Terminate the session.
     */
    private void terminateSession()
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        try
        {
            this.terminateSession(db);
        }
        finally 
        {
            db.close();
        }
    }
    
    /**
     * Terminates the database session.
     * 
     * @param db database session
     */
    public void terminateSession(org.hibernate.Session db)
    {
        SessionService sesServ = MultiSiteActivator.getSessionService();
        if (sesServ == null)
        {
            this.logger.error("Unable to terminate session because the 'Session' service was not found.");
        }
        else
        {
            sesServ.finishSession((Session)db.merge(this.session), "Unable to update consumer.", db);
        }
    }
}
