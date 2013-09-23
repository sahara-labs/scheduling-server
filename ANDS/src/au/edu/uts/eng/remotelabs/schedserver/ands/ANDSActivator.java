/**
 * SAHARA Scheduling Server
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
 * @author Michael Diponio (mdiponio)
 * @dat 18th March 2013
 */

package au.edu.uts.eng.remotelabs.schedserver.ands;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import au.edu.uts.eng.remotelabs.schedserver.ands.impl.ANDSServlet;
import au.edu.uts.eng.remotelabs.schedserver.ands.impl.RedboxIngestFilesGenerator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.RigEventListener;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.SessionEventListener;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.server.ServletContainer;
import au.edu.uts.eng.remotelabs.schedserver.server.ServletContainerService;

/**
 * Activator for the ANDS module.
 */
public class ANDSActivator implements BundleActivator 
{
    /** Bundle name. */
    public static final String BUNDLE = "SchedulingServer-ANDS";
    
    /** Service registration for the session event listener to generate
     *  metadata ingest files. */
    private ServiceRegistration<SessionEventListener> mdSessionListenerReg;
    
    /** Rig event listener to generate metadata ingest files. */
    private ServiceRegistration<RigEventListener> mdRigListenerReg;
    
    /** Runnable service to periodically generate metadata. */
    private ServiceRegistration<Runnable> mdRunnableReg;
    
    /** REST Interface. */
    private ServiceRegistration<ServletContainerService> restReg;
    
    /** Logger. */
    private Logger logger;
    
    @Override
    public void start(BundleContext context) throws Exception 
    {
        this.logger = LoggerActivator.getLogger();
        this.logger.debug("ANDS module starting up...");

        /* Register a notifiers and tasks to auto-generate metadata. */
        RedboxIngestFilesGenerator generator = new RedboxIngestFilesGenerator();
        this.mdSessionListenerReg = context.registerService(SessionEventListener.class, generator, null);
        this.mdRigListenerReg = context.registerService(RigEventListener.class, generator, null);
        
        Dictionary<String, String> props = new Hashtable<String, String>();
        props.put("period", "600");
        this.mdRunnableReg = context.registerService(Runnable.class, generator, props);
        
        ServletContainerService hosting = new ServletContainerService();
        hosting.addServlet(new ServletContainer(new ANDSServlet()));
        this.restReg = context.registerService(ServletContainerService.class, hosting, null);
	}

    @Override
	public void stop(BundleContext context) throws Exception 
	{
        this.logger.debug("ANDS module shutting down...");
        
        /* Unregister bundle services. */
        this.restReg.unregister();
        this.mdRigListenerReg.unregister();
        this.mdSessionListenerReg.unregister();
        this.mdRunnableReg.unregister();
	}
}
