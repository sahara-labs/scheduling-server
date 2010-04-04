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
 * @date 28th March 2009
 */

package au.edu.uts.eng.remotelabs.schedserver.queuer;

import java.util.List;
import java.util.Properties;

import org.apache.axis2.transport.http.AxisServlet;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.SessionDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.queuer.impl.Queue;
import au.edu.uts.eng.remotelabs.schedserver.queuer.impl.QueueStaleSessionTask;
import au.edu.uts.eng.remotelabs.schedserver.server.ServletContainer;
import au.edu.uts.eng.remotelabs.schedserver.server.ServletContainerService;

/**
 * Activator for the queuer bundle.
 */
public class QueueActivator implements BundleActivator 
{
    /** Service registration for the Permission SOAP interface. */
    private ServiceRegistration soapRegistration;
    
    /** Service registration for the queue stale session timeout task. */
    private ServiceRegistration staleTimeoutTaskReg;
    
    /** Logger. */
    private Logger logger;

    @Override
    public void start(BundleContext context) throws Exception 
    {
        this.logger = LoggerActivator.getLogger();
        this.logger.info("Starting queuer bundle...");
        
        /* Reload all the active queue sessions. */
        Queue queue = Queue.getInstance();
        queue.expunge();
        SessionDao dao = new SessionDao();
        List<Session> sessions = dao.findAllActiveSessions();
        for (Session ses : sessions)
        {
            if (ses.getAssignmentTime() == null)
            {
                queue.addEntry(ses, dao.getSession());
            }
        }
        dao.closeSession();
        
        /* Register the queue stale session timeout task. */
        QueueStaleSessionTask task = new QueueStaleSessionTask();
        task.run(); // Clean up any old stale sessions
        Properties props = new Properties();
        props.put("period", "60");
        this.staleTimeoutTaskReg = context.registerService(Runnable.class.getName(), task, props);
        
        /* Register the queuer service. */
        this.logger.debug("Registering the Queuer SOAP interface service.");
        ServletContainerService soapService = new ServletContainerService();
	    soapService.addServlet(new ServletContainer(new AxisServlet(), true));
	    this.soapRegistration = context.registerService(ServletContainerService.class.getName(), soapService, null);
	}

	@Override
	public void stop(BundleContext context) throws Exception 
	{
	    this.logger.info("Shutting down the queuer bundle.");
	    this.soapRegistration.unregister();
	    this.staleTimeoutTaskReg.unregister();
	}
}
