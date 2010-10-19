/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
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
 * @date 5th April 2009
 */

package au.edu.uts.eng.remotelabs.schedserver.session;

import java.util.Date;
import java.util.Properties;

import org.apache.axis2.transport.http.AxisServlet;
import org.hibernate.Query;
import org.hibernate.Session;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.RigEventListener;
import au.edu.uts.eng.remotelabs.schedserver.server.ServletContainer;
import au.edu.uts.eng.remotelabs.schedserver.server.ServletContainerService;
import au.edu.uts.eng.remotelabs.schedserver.session.impl.RigShutdownSessonStopper;
import au.edu.uts.eng.remotelabs.schedserver.session.impl.SessionExpiryChecker;

/**
 * Activator for the Session bundle which handles running sessions.
 */
public class SessionActivator implements BundleActivator 
{
    /** Service registration for the Session SOAP interface. */
    private ServiceRegistration soapReg;
    
    /** Session expiry checked runnable task service. */
    private ServiceRegistration sessionCheckerReg;
    
    /** Shutdown event notifier session terminator service. */
    private ServiceRegistration terminatorService;
    
    /** Logger. */
    private Logger logger;
    
    @Override
	public void start(BundleContext context) throws Exception 
	{
        this.logger = LoggerActivator.getLogger();
        this.logger.info("Starting the Session bunde...");
        
        /* Register the session timeout checker service. */
        Properties props = new Properties();
        props.put("period", "10");
        SessionExpiryChecker task = new SessionExpiryChecker();
        task.run(); // Expire any old sessions
        this.sessionCheckerReg = context.registerService(Runnable.class.getName(), task, props);
        
        /* Register the rig event notifier. */
        this.terminatorService = context.registerService(RigEventListener.class.getName(), 
                new RigShutdownSessonStopper(), null);
        
        /* Register the queuer service. */
        this.logger.debug("Registering the Queuer SOAP interface service.");
        ServletContainerService soapService = new ServletContainerService();
        soapService.addServlet(new ServletContainer(new AxisServlet(), true));
        this.soapReg = context.registerService(ServletContainerService.class.getName(), soapService, null);
	}

	@Override
	public void stop(BundleContext context) throws Exception 
	{
	    this.logger.info("Stopping the Session bundle...");
	    this.soapReg.unregister();
	    this.terminatorService.unregister();
	    this.sessionCheckerReg.unregister();
	    
	    /* Terminate all in progress sessions. */
	    Session ses = DataAccessActivator.getNewSession();
        if (ses != null)
        {
            Query qu = ses.createQuery("UPDATE Session SET active=:false, removal_reason=:reason, removal_time=:time " +
                    " WHERE active=:true");
            qu.setBoolean("false", false);
            qu.setBoolean("true", true);
            qu.setString("reason", "Scheduling Server shutting down.");
            qu.setTimestamp("time", new Date());
            
            ses.beginTransaction();
            int num = qu.executeUpdate();
            ses.getTransaction().commit();
            this.logger.info("Terminated " + num + " sessions for shutdown.");
            ses.close();
        }
	}
}
