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
 * @date 6th September 2011
 */
package au.edu.uts.eng.remotelabs.schedserver.multisite.impl;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.callback.client.MultiSiteCallbackClientHandler;

/**
 * Receives response or error from provided session finished notification. 
 */
public class SessionFinishedCallback extends MultiSiteCallbackClientHandler
{
    /** Session notification was about. */
    private final Session session;
    
    /** Logger. */
    private final Logger logger;
    
    public SessionFinishedCallback(Session session)
    {
        this.logger = LoggerActivator.getLogger();
        this.session = session;
    }
    
    @Override
    public void receiveResponseSessionFinished(final boolean successful, final String reason)
    {
        if (successful)
        {
            this.logger.debug("Consumer site successfully acknowledged '" + this.session.getId() + "' is finished.");
        }
        else
        {
            this.logger.warn("Unable to provide consumer notification of session termination of '" + 
                    this.session.getId() + "'. Provided reason: " + reason);
        }
    }
    
    @Override
    public void receiveErrorSessionFinished(final Exception e)
    {
        this.logger.warn("Unable to provide consumer notification of session termination of '" + 
                    this.session.getId() + "'. Exception " + e.getClass().getSimpleName() + ": " + e.getMessage());
    }
}
