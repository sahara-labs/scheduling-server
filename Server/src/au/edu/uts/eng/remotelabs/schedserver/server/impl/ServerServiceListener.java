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
 * @date 8th February 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.server.impl;

import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.server.ServletContainerService;

/**
 * Listener for <tt>ServletContainerService</tt> service events. If the service
 * is registered, the servlet is added to the server context to handle 
 * requests. If the service is being unregistered, the servlet is
 * removed.
 */
public class ServerServiceListener implements ServiceListener
{
    /** Logger. */
    private final Logger logger;
    
    /** Server implementation. */
    private final ServerImpl server;
    
    public ServerServiceListener(final ServerImpl srv)
    {
        this.logger = LoggerActivator.getLogger();
        this.server = srv;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void serviceChanged(final ServiceEvent event)
    {
        ServiceReference<ServletContainerService> ref;
        switch (event.getType())
        {
            case ServiceEvent.REGISTERED:
                ref = (ServiceReference<ServletContainerService>) event.getServiceReference();
                this.logger.debug("Received a registered service event for a servlet service of bundle " + 
                        ref.getBundle().getSymbolicName() + '.');
                this.server.addService(ref);
                break;
                
            case ServiceEvent.UNREGISTERING:
                ref = (ServiceReference<ServletContainerService>) event.getServiceReference();
                this.logger.debug("Received a unregistering service event for a shutting down servlet service of " +
                		"bundle " + ref.getBundle().getSymbolicName() + '.');
                this.server.removeService(ref);
                break;
        }
    }
}
