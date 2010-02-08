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
 * @date 4th January 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.server.impl;

import java.util.ArrayList;
import java.util.List;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.thread.QueuedThreadPool;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import au.edu.uts.eng.remotelabs.schedserver.config.Config;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.server.ServletServerService;

/**
 * The server implementation. 
 */
public class ServerImpl
{
     /** Address post-fix for the service URL. */
    public static final String URL_POSTFIX = "/services/SchedulingServer";
    
    /** Jetty server. */
    private Server server;
    
    /** Connector which receives requests. */
    private List<Connector> connectors;
    
    /** Context which handles a request using a servlet. */
    private Context context;
    
    /** Request thread pool. */
    private QueuedThreadPool threadPool;

    /** Logger. */
    private Logger logger;

    public void start(BundleContext context) throws Exception 
	{
	    this.logger = LoggerActivator.getLogger();
	    this.logger.debug("Starting the Scheduling Server server up.");

	    synchronized (this)
	    {
	        this.connectors = new ArrayList<Connector>();
	        
	        /* Get the configuration service. */
	        ServiceReference ref = context.getServiceReference(Config.class.getName());
	        Config config = null;
	        if (ref == null || (config = (Config)context.getService(ref)) == null)
	        {
	            this.logger.error("Unable to get configuration service reference so unable " +
	            		"to load server configuration.");
	            throw new Exception("Unable to load server configuration.");
	        }
	        
	        /* --------------------------------------------------------------------
	         * ---- 1. Create the server. -----------------------------------------
	         * ----------------------------------------------------------------- */
	        /* The server is the main class for the Jetty HTTP server. It uses a 
	         * connectors to receive requests, a context to handle requests and 
	         * a thread pool to manage concurrent requests.
	         */
	        this.server = new Server();

	        /* --------------------------------------------------------------------
	         * ---- 2. Create and configure the connectors. ------------------------
	         * ----------------------------------------------------------------- */
	        /* The connectors receives requests and calls handle on handler object
	         * to handle a request. */
	        Connector http = new SelectChannelConnector();
	        String tmp = config.getProperty("Listening_Port", "8080");
	        try
	        {
	            http.setPort(Integer.parseInt(tmp));
	            this.logger.info("Listening on port " + tmp + '.');
	        }
	        catch  (NumberFormatException nfe)
	        {
	            http.setPort(8080);
	            this.logger.info("Invalid configuration for the Scheduling Server listening port. " + tmp + " is " +
	            		"not a valid port number. Using the default of " + 8080 + '.');
	        }
	        this.connectors.add(http);
	        
	        /* HTTPS connector. */
	        // TODO HTTP connector
	        
	        this.server.setConnectors(this.connectors.toArray(new Connector[this.connectors.size()]));

	        /* --------------------------------------------------------------------
	         * ---- 3. Create and configure the request thread pool. -------------- 
	         * ----------------------------------------------------------------- */
	        int concurrentRequests = 100;
	        tmp = config.getProperty("Concurrent_Requests", "100");
	        try
	        {
	            concurrentRequests = Integer.parseInt(tmp);
	            this.logger.info("Allowable concurrent requests is " + concurrentRequests + ".");
	        }
	        catch (NumberFormatException nfe)
	        {
	            this.logger.warn(tmp + " is not a valid number of concurrent requests. Using the default of " +
	                    concurrentRequests + '.');
	        }
	        this.threadPool = new QueuedThreadPool(concurrentRequests);
	        this.server.setThreadPool(this.threadPool);

	        /* --------------------------------------------------------------------
	         * --- 4. Create and configure the handler. ---------------------------
	         * ------------------------------------------------------------------*/
	        /* The handler routes the requests to the Apache Axis 2 servlet. This 
	         * registers all the HTTP servlets that are registered as OSGI services. */
	        this.context = new Context(this.server, "/", Context.SESSIONS);
	        
	        ServiceReference httpServletRefs[] = context.getServiceReferences(ServletServerService.class.getName(), null);
	        for (ServiceReference httpServletRef : httpServletRefs)
	        {
	            ServletServerService servlet = (ServletServerService) context.getService(httpServletRef);

///C	            this.context.addServlet(servlet, pathSpec)
	        }
	        
//	        ServletHolder holder = new ServletHolder(new AxisServlet());
//
//	        File repoPath = new File("./interface");
//	        this.logger.debug("Axis repository " + repoPath.getCanonicalPath() + ".");
//	        holder.setInitParameter("axis2.repository.path", repoPath.getCanonicalPath());
//	        this.context.addServlet(holder, "/");
	        
	        this.server.start();
	    }
	}

    /**
     * Stops the server listening.
     * 
     * @throws Exception
     */
	public void stop() throws Exception 
	{
	    synchronized (this)
	    {
	        if (this.server != null)
	        {
	            this.server.stop();
	        }
	    }
	}
}
