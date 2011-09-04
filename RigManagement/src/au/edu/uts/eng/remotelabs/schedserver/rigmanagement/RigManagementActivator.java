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
 * @date 29th January 2010
 */

package au.edu.uts.eng.remotelabs.schedserver.rigmanagement;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.apache.axis2.transport.http.AxisServlet;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

import au.edu.uts.eng.remotelabs.schedserver.bookings.pojo.BookingEngineService;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.SessionEventListener;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.SessionEventListener.SessionEvent;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.SessionEventServiceListener;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.impl.RigMaintenanceNotifier;
import au.edu.uts.eng.remotelabs.schedserver.server.ServletContainer;
import au.edu.uts.eng.remotelabs.schedserver.server.ServletContainerService;

/**
 * Activator for the rig management bundle.
 */
public class RigManagementActivator implements BundleActivator 
{
    /** Tracker for the booking engine service. */
    private static ServiceTracker<BookingEngineService, BookingEngineService> bookingTracker;
    
    /** The list of session event listeners. */
    private static List<SessionEventListener> sessionListeners;
    
    /** SOAP servlet service registration. */
    private ServiceRegistration<ServletContainerService> soapService;
    
    /** Maintenance notifier. */
    private ServiceRegistration<Runnable> notifierReg;
    
    /** Logger. */
    private Logger logger;
    
    @Override
	public void start(BundleContext context) throws Exception 
	{
        this.logger = LoggerActivator.getLogger();
        this.logger.info("The rig management bundle is starting up.");
        
        /* Open a tracker to the booking engine. */
        bookingTracker = 
                new ServiceTracker<BookingEngineService, BookingEngineService>(context, BookingEngineService.class, null);
        bookingTracker.open();
        
        /* Add service listener to add and remove registered session event listeners. */
        sessionListeners = new ArrayList<SessionEventListener>();
        SessionEventServiceListener sesServListener = new SessionEventServiceListener(sessionListeners, context);
        context.addServiceListener(sesServListener, 
                '(' + Constants.OBJECTCLASS + '=' + SessionEventListener.class.getName() + ')');
        for (ServiceReference<SessionEventListener> ref : context.getServiceReferences(SessionEventListener.class, null))
        {
            sesServListener.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED, ref));
        }
        
        /* Register the maintenance notifier. */
        Dictionary<String, String> props = new Hashtable<String, String>();
        props.put("period", String.valueOf(RigMaintenanceNotifier.RUN_PERIOD));
        this.notifierReg = context.registerService(Runnable.class, new RigMaintenanceNotifier(), props);
        
        /* Register the Rig Management SOAP service. */
        this.logger.debug("Registering the Rig Management SOAP service.");
        ServletContainerService soapService = new ServletContainerService();
        soapService.addServlet(new ServletContainer(new AxisServlet(), true));
        this.soapService = context.registerService(ServletContainerService.class, soapService, null);
	}

	@Override
	public void stop(BundleContext context) throws Exception 
	{
		this.logger.info("The rig management bundle is shutting down.");
		
		this.soapService.unregister();
		this.notifierReg.unregister();
		
		RigManagementActivator.sessionListeners = null;
		
		bookingTracker.close();
		bookingTracker = null;
	}

	/**
     * Returns a booking service object or null if not booking service is running.
     * 
     * @return booking engine service or null
     */
    public static BookingEngineService getBookingService()
    {
        return bookingTracker == null ? null : bookingTracker.getService();
    }
    
    /**
     * Notifies the session event listeners of an event.
     * 
     * @param event type of event
     * @param session session change
     * @param db database
     */
    public static void notifySessionEvent(SessionEvent event, Session session, org.hibernate.Session db)
    {
        if (sessionListeners == null) return;
        
        for (SessionEventListener listener : sessionListeners)
        {
            listener.eventOccurred(event, session, db);
        }
    }
}
