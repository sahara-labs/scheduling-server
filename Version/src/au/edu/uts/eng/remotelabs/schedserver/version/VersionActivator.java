/**
 * SAHARA Scheduling Server
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
 * @date 12th February 2010
 */

package au.edu.uts.eng.remotelabs.schedserver.version;

import org.apache.axis2.transport.http.AxisServlet;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.server.ServletContainerService;

/**
 * The version bundle is a simple SOAP service with one operation that lists 
 * out the name of all the bundles running.
 */
public class VersionActivator implements BundleActivator 
{
    /** Servlet container service registration. */
    private ServiceRegistration registration;
    
    /** Bundle context. */
    private static BundleContext bundleContext;
    
    /** Logger. */
    private Logger logger;
    
    @Override
	public void start(BundleContext context) throws Exception 
	{
		this.logger = LoggerActivator.getLogger();
		this.logger.info("Bundle " + context.getBundle().getSymbolicName() + " starting up...");
		
		VersionActivator.bundleContext = context;
		
		this.registration = context.registerService(ServletContainerService.class.getName(),
		        new ServletContainerService(new AxisServlet(), true), null);
	}

    @Override
	public void stop(BundleContext context) throws Exception 
	{
		this.logger.info("Bundle " + context.getBundle().getSymbolicName() + " shutting down...");
		this.registration.unregister();
		
		VersionActivator.bundleContext = null;
	}
    
    /**
     * Returns the list of installed bundles.
     * 
     * @return bundle list
     */
    public static Bundle[] getInstalledBundles()
    {
        if (VersionActivator.bundleContext == null)
        {
            return new Bundle[0];
        }
        
        return VersionActivator.bundleContext.getBundles();
    }
}
