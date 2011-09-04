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
 * @date 3rd September 2011
 */
package au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener;

import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.SessionEventListener;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;

/**
 * Event listener for SessinEventListener services.
 */
public class SessionEventServiceListener implements ServiceListener
{
    /** List of services. */
    private final List<SessionEventListener> services;
    
    /** Bundle context. */
    private final BundleContext context;
    
    
    /** Logger. */
    private final Logger logger;
    
    public SessionEventServiceListener(List<SessionEventListener> services, BundleContext context)
    {
        this.logger = LoggerActivator.getLogger();
        this.logger.debug("Creating a session event listener for bundle '" + context.getBundle().getSymbolicName() +
                "'.");
        
        this.context = context;
        this.services = services;
    }
    
    @Override
    public void serviceChanged(ServiceEvent evt)
    {
        @SuppressWarnings("unchecked")
        ServiceReference<SessionEventListener> ref = (ServiceReference<SessionEventListener>) evt.getServiceReference();
        switch (evt.getType())
        {
            case ServiceEvent.REGISTERED:
                this.logger.debug("Registering a session event listener from bundle '" + 
                        ref.getBundle().getSymbolicName() + "'.");
                synchronized (this.services)
                {
                    this.services.add(this.context.getService(ref));
                }
                break;
            case ServiceEvent.UNREGISTERING:
                this.logger.debug("Removing a session event listener from bundle '" + 
                        ref.getBundle().getSymbolicName() + "'.");
                synchronized (this.services)
                {
                    this.services.remove(this.context.getService(ref));
                }
                break;
        }
    }
}
