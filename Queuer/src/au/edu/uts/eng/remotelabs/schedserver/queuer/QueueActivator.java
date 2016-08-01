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
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.SessionDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.EventServiceListener;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.RigCommunicationProxy;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.RigEventListener;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.SessionEventListener;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.SessionEventListener.SessionEvent;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.queuer.impl.Queue;
import au.edu.uts.eng.remotelabs.schedserver.queuer.impl.QueueListenerRun;
import au.edu.uts.eng.remotelabs.schedserver.queuer.impl.QueueStaleSessionTask;
import au.edu.uts.eng.remotelabs.schedserver.queuer.pojo.QueuerService;
import au.edu.uts.eng.remotelabs.schedserver.queuer.pojo.impl.QueuerServiceImpl;
import au.edu.uts.eng.remotelabs.schedserver.server.ServletContainer;
import au.edu.uts.eng.remotelabs.schedserver.server.ServletContainerService;

/**
 * Activator for the queuer bundle.
 */
public class QueueActivator implements BundleActivator 
{
    /** Service registration for the Permission SOAP interface. */
    private ServiceRegistration<ServletContainerService> soapReg;

    /** Queuer service this bundle exports. */
    private ServiceRegistration<QueuerService> queuerReg;

    /** Service registration for the queue stale session timeout task. */
    private ServiceRegistration<Runnable> staleTimeoutTaskReg;

    /** Service registration for a rig event listener. */
    private ServiceRegistration<RigEventListener> rigListenerReg;

    /** The list of session event listeners. */
    private static List<SessionEventListener> sessionListeners;

    /** The list of registered communication proxies. */
    private static List<RigCommunicationProxy> commsProxies; 

    /** Bookings service. */
    public static ServiceTracker<BookingEngineService, BookingEngineService> bookingTracker;

    /** Logger. */
    private Logger logger;

    @Override
    public void start(BundleContext context) throws Exception 
    {
        this.logger = LoggerActivator.getLogger();
        this.logger.info("Starting Queuer bundle...");

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
        Dictionary<String, String> props = new Hashtable<String, String>();
        props.put("period", "60");
        this.staleTimeoutTaskReg = context.registerService(Runnable.class, task, props);

        /* Obtain a service tracker for the booking service. */
        QueueActivator.bookingTracker = new ServiceTracker<BookingEngineService, BookingEngineService>(context, 
                BookingEngineService.class, null);
        QueueActivator.bookingTracker.open();

        /* Register the rig event listener service. */
        this.rigListenerReg = context.registerService(RigEventListener.class, new QueueListenerRun(), null);

        /* Set up the session event service listener to add and remove event listeners. */
        QueueActivator.sessionListeners = new ArrayList<SessionEventListener>();
        EventServiceListener<SessionEventListener> sesServListener = 
                new EventServiceListener<SessionEventListener>(QueueActivator.sessionListeners, context);
        context.addServiceListener(sesServListener, 
                '(' + Constants.OBJECTCLASS + '=' + SessionEventListener.class.getName() + ')');
        for (ServiceReference<SessionEventListener> ref : context.getServiceReferences(SessionEventListener.class, null))
        {
            sesServListener.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED, ref));
        }
        
        /* Service listener for communication proxies. */
        QueueActivator.commsProxies = new ArrayList<RigCommunicationProxy>();
        EventServiceListener<RigCommunicationProxy> commsServs = 
                new EventServiceListener<RigCommunicationProxy>(QueueActivator.commsProxies, context);
        context.addServiceListener(commsServs, '(' + Constants.OBJECTCLASS + '=' + RigCommunicationProxy.class.getName() + ')');
        for (ServiceReference<RigCommunicationProxy> ref : context.getServiceReferences(RigCommunicationProxy.class, null))
        {
            commsServs.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED, ref));
        }

        /* Register the Queuer POJO service. */
        this.logger.debug("Registering the Queuer POJO service.");
        this.queuerReg = context.registerService(QueuerService.class, new QueuerServiceImpl(), null);

        /* Register the Queuer SOAP service. */
        this.logger.debug("Registering the Queuer SOAP service.");
        ServletContainerService soapService = new ServletContainerService();
        soapService.addServlet(new ServletContainer(new AxisServlet(), true));
        this.soapReg = context.registerService(ServletContainerService.class, soapService, null);
    }

    @Override
    public void stop(BundleContext context) throws Exception 
    {
        this.logger.info("Shutting down the Queuer bundle...");
        this.soapReg.unregister();
        this.queuerReg.unregister();
        QueueActivator.sessionListeners = null;
        QueueActivator.bookingTracker.close();
        QueueActivator.bookingTracker = null;
        QueueActivator.commsProxies = null;
        this.staleTimeoutTaskReg.unregister();
        this.rigListenerReg.unregister();
    }

    /**
     * Returns a booking service object or null if not booking service is running.
     * 
     * @return booking engine service or null
     */
    public static BookingEngineService getBookingService()
    {
        if (QueueActivator.bookingTracker == null) return null;

        return QueueActivator.bookingTracker.getService();
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
        if (QueueActivator.sessionListeners == null) return;

        for (SessionEventListener listener : QueueActivator.sessionListeners)
        {
            listener.eventOccurred(event, session, db);
        }
    }

    /**
     * Calls allocate operation on all communication proxies.
     * 
     * @param ses session information
     * @param db database session
     */
    public static void allocate(Session ses, org.hibernate.Session db)
    {
        if (commsProxies == null) return;
        for (RigCommunicationProxy proxy : commsProxies)
        {
            proxy.allocate(ses, db);
        }
    }
    
    /**
     * Calls release operation on all communication proxies.
     * 
     * @param ses session information
     * @param db database session
     */
    public static void release(Session ses, org.hibernate.Session db)
    {
        if (commsProxies == null) return;
        for (RigCommunicationProxy proxy : commsProxies)
        {
            proxy.release(ses, db);
        }
    }
}
