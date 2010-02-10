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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.thread.QueuedThreadPool;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import au.edu.uts.eng.remotelabs.schedserver.config.Config;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.server.ServletServerService;

/**
 * The server implementation. Listens for service events filter by the 
 * <tt>ServletServerService<tt> object classes to dynamically add and
 * remove request handling servlets.
 * <br />
 * Added or removing a servlet requires the serverContext to be reset, thus
 * requires the server to briefly stop listening.
 */
public class ServerImpl
{
     /** Address post-fix for the service URL. */
    public static final String URL_POSTFIX = "/services/SchedulingServer";
    
    /** Bundle serverContext of the Server bundle. */
    private final BundleContext bundleContext;
    
    /** Jetty server. */
    private Server server;
    
    /** Service services keyed by path spec. */
    private final Map<String, ServiceReference> services;
    
    /** Connector which receives requests. */
    private List<Connector> connectors;
    
    /** Context which handles a request using a servlet. */
    private Context serverContext;
    
    /** Request thread pool. */
    private QueuedThreadPool threadPool;

    /** Logger. */
    private final Logger logger;
    
    public ServerImpl(final BundleContext context)
    {
        this.logger = LoggerActivator.getLogger();
        this.bundleContext = context;
        
        this.services = new HashMap<String, ServiceReference>();
    }

    /**
     * Initialize the embedded Jetty server. This sets up the server and adds 
     * the connectors and thread pool. Currently only a HTTP connector is 
     * supported.
     * 
     * @throws Exception error occurs initializing server
     */
    public synchronized void init() throws Exception
	{
	    this.logger.debug("Starting the Scheduling Server server up.");

	    this.connectors = new ArrayList<Connector>();

	    /* Get the configuration service. */
	    final ServiceReference ref = this.bundleContext.getServiceReference(Config.class.getName());
	    Config config = null;
	    if (ref == null || (config = (Config)this.bundleContext.getService(ref)) == null)
	    {
	        this.logger.error("Unable to get configuration service reference so unable " +
	        "to load server configuration.");
	        throw new Exception("Unable to load server configuration.");
	    }

	    /* --------------------------------------------------------------------
	     * ---- 1. Create the server. -----------------------------------------
	     * ----------------------------------------------------------------- */
	    /* The server is the main class for the Jetty HTTP server. It uses a 
	     * connectors to receive requests, a serverContext to handle requests and 
	     * a thread pool to manage concurrent requests.
	     */
	    this.server = new Server();

	    /* --------------------------------------------------------------------
	     * ---- 2. Create and configure the connectors. ------------------------
	     * ----------------------------------------------------------------- */
	    /* The connectors receives requests and calls handle on handler object
	     * to handle a request. */
	    final Connector http = new SelectChannelConnector();
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
	    // TODO Add HTTPS connector for the Scheduling Server Server

	    this.server.setConnectors(this.connectors.toArray(new Connector[this.connectors.size()]));

	    /* --------------------------------------------------------------------
	     * ---- 3. Create and configure the request thread pool. -------------- 
	     * ----------------------------------------------------------------- */
	    int concurrentReqs = 100;
	    tmp = config.getProperty("Concurrent_Requests", "100");
	    try
	    {
	        concurrentReqs = Integer.parseInt(tmp);
	        this.logger.info("Allowable concurrent requests is " + concurrentReqs + ".");
	    }
	    catch (NumberFormatException nfe)
	    {
	        this.logger.warn(tmp + " is not a valid number of concurrent requests. Using the default of " +
	                concurrentReqs + '.');
	    }
	    this.threadPool = new QueuedThreadPool(concurrentReqs);
	    this.server.setThreadPool(this.threadPool);

	    /* --------------------------------------------------------------------
	     * --- 4. Create and configure the handler. ---------------------------
	     * ------------------------------------------------------------------*/
	    /* The handler routes the requests to the Apache Axis 2 servlet. This 
	     * registers all the HTTP servlets that are registered as OSGI services. */
//	    this.serverContext = new Context(this.server, "/", Context.SESSIONS);
	}
    
    /**
     * Adds a servlet to hosted on server based on the provided service reference.
     * If the server is started, it is briefly stopped to add the servlet and is 
     * then restarted.
     * 
     * @param ref service reference pointing to a ServletServerService service
     */
    public synchronized void addService(final ServiceReference ref)
    {
        boolean wasRunning = false;
        try
        {
            final ServletServerService serv = (ServletServerService)this.bundleContext.getService(ref);
            if (serv.getServlet() == null)
            {
                this.logger.error("Server registration from bundle " + ref.getBundle().getSymbolicName() + 
                " does not contain a servlet so it cannot be hosted. This is a bug.");
                throw new IllegalArgumentException("Servlet is empty.");
            }
            else if (this.services.containsKey(serv.getPathSpec()))
            {
                this.logger.error("Server registration from bundle " + ref.getBundle().getSymbolicName() +
                        " uses the same path as a previous registration from bundle " + 
                        this.services.get(serv.getPathSpec()).getBundle().getSymbolicName() + ". These must be " +
                "unique as they resolve to the unique URL postfix for the registered servlet.");
                throw new IllegalStateException("Path spec already in use.");
            }
            this.services.put(serv.getPathSpec(), ref);


            /* If running, stop the server. */
            wasRunning = this.server.isStarted() || this.server.isStarting();
            if (wasRunning) this.server.stop();

            this.server.removeHandler(this.serverContext);

            /* Populate the servlet with all the servlets to run. */
            this.serverContext = new Context(this.server, "/", Context.SESSIONS);
            for (ServiceReference exRef : this.services.values())
            {
                final ServletServerService exSer = (ServletServerService)this.bundleContext.getService(exRef);
                final ServletHolder holder = new ServletHolder(exSer.getServlet());

                if (exSer.isAxis())
                {
                    holder.setInitParameter("axis2.repository.path", exSer.getAxisRepo());
                }
                this.serverContext.addServlet(holder, exSer.getPathSpec());
            }
        }
        catch (Exception ex)
        {
            this.logger.error("Failed adding server service from bundle " + ref.getBundle().getSymbolicName() +
                    " because of exception with message: " + ex.getMessage() + '.');
        }
        finally
        {
            /* Restore the server state. */
            if (wasRunning && this.server.isStopped())
            {
                try
                {
                    this.server.start();
                }
                catch (Exception e)
                { 
                    this.logger.error("Failed starting Jetty server because of exception with message: " 
                            + e.getMessage() + '.');
                }
            }
        }

    }
    
    /**
     * Removes a servlet from being hosted on the server based on the provided 
     * service reference which provides a ServletServerService object. 
     * If the server is started, it is briefly stopped to remove the servlet and is 
     * then restarted.
     * 
     * @param ref service reference pointing to a ServletServerService service
     */
    public synchronized void removeService(final ServiceReference ref)
    {
        boolean wasRunning = false;
        try
        {
            final ServletServerService serv = (ServletServerService)this.bundleContext.getService(ref);
            if (serv.getPathSpec() == null)
            {
                this.logger.warn("Unable to remove the server servlet for bundle " + ref.getBundle().getSymbolicName() +
                        '.');
                return;
            }
            else if (this.services.containsKey(serv.getPathSpec()))
            {
                this.logger.warn("The server servlet for bundle " + ref.getBundle().getSymbolicName() + " is not " + 
                        " currently registered, so nothing to remove.");
                return;
            }
            this.services.remove(serv.getPathSpec());

            /* If running, stop the server. */
            wasRunning = this.server.isStarted() || this.server.isStarting();
            if (wasRunning) this.server.stop();

            this.server.removeHandler(this.serverContext);

            /* Populate the servlet with all the servlets to run. */
            this.serverContext = new Context(this.server, "/", Context.SESSIONS);
            for (ServiceReference exRef : this.services.values())
            {
                final ServletServerService exSer = (ServletServerService)this.bundleContext.getService(exRef);
                final ServletHolder holder = new ServletHolder(exSer.getServlet());
                if (exSer.isAxis())
                {
                    holder.setInitParameter("axis2.repository.path", exSer.getAxisRepo());
                }
                this.serverContext.addServlet(holder, exSer.getPathSpec());
            }
        }
        catch (Exception ex)
        {
            this.logger.error("Failed removing server service from bundle " + ref.getBundle().getSymbolicName() +
                " because of exception with message: " + ex.getMessage() + '.');
        }
        finally
        {
            /* Restore the server state. */
            if (wasRunning && this.server.isStopped())
            {
                try
                {
                    this.server.start();
                }
                catch (Exception ex)
                {
                    this.logger.error("Failed starting Jetty server because of exception with message: " 
                            + ex.getMessage() + '.');
                }
            }
        }
    }
    
    /**
     * Starts the listening server.
     * 
     * @throws Exception
     */
    public synchronized void start() throws Exception
    {
        try
        {
            this.server.start();
        }
        catch (Exception e)
        {
            this.logger.error("Exception thrown when starting the listening server with message: " +
                    e.getMessage() + '.');
            throw e;
        }
    }
    

    /**
     * Stops the server listening.
     * 
     * @throws Exception
     */
	public synchronized void stop() throws Exception 
	{
	    if (this.server != null)
	    {
	        try
	        {
	            this.server.stop();
	        }
	        catch (Exception e)
	        {
	            this.logger.error("Exception thrown when stopping the listening server with message: " + 
	                    e.getMessage() + '.');
	            throw e;
	        }
	    }
	}
}
