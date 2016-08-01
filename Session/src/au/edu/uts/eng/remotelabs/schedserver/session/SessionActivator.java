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

import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.apache.axis2.transport.http.AxisServlet;
import org.hibernate.Query;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

import au.edu.uts.eng.remotelabs.schedserver.bookings.pojo.BookingEngineService;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.EventServiceListener;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.RigCommunicationProxy;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.RigCommunicationProxy.ActivityAsyncCallback;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.RigEventListener;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.RigEventListener.RigStateChangeEvent;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.SessionEventListener;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.SessionEventListener.SessionEvent;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.server.ServletContainer;
import au.edu.uts.eng.remotelabs.schedserver.server.ServletContainerService;
import au.edu.uts.eng.remotelabs.schedserver.session.impl.RigShutdownSessonStopper;
import au.edu.uts.eng.remotelabs.schedserver.session.impl.SessionExpiryChecker;
import au.edu.uts.eng.remotelabs.schedserver.session.pojo.SessionService;
import au.edu.uts.eng.remotelabs.schedserver.session.pojo.impl.SessionServiceImpl;

/**
 * Activator for the Session bundle which handles running sessions.
 */
public class SessionActivator implements BundleActivator 
{
    /** Service registration for the Session SOAP interface. */
    private ServiceRegistration<ServletContainerService> soapReg;

    /** Session expiry checked runnable task service. */
    private ServiceRegistration<Runnable> checkerTask;

    /** Shutdown event notifier session terminator service. */
    private ServiceRegistration<RigEventListener> terminatorService;

    /** Session POJO service registration. */
    private ServiceRegistration<SessionService> pojoReg;

    /** Booking service tracker. */
    private static ServiceTracker<BookingEngineService, BookingEngineService> bookingsTracker;

    /** The list of registered communication proxies. */
    private static List<RigCommunicationProxy> commsProxies; 
    
    /** Rig event listeners list. */
    private static List<RigEventListener> rigListeners;

    /** The list of session event listeners. */
    private static List<SessionEventListener> sessionListeners;

    /** Logger. */
    private Logger logger;

    @Override
    public void start(BundleContext context) throws Exception 
    {
        this.logger = LoggerActivator.getLogger();
        this.logger.info("Starting the Session bunde...");

        /* Track the bookings service tracker. */
        SessionActivator.bookingsTracker = 
                new ServiceTracker<BookingEngineService, BookingEngineService>(context, BookingEngineService.class, null);
        SessionActivator.bookingsTracker.open();

        /* Register the session timeout checker service. */
        Dictionary<String, String> props = new Hashtable<String, String>(1);
        props.put("period", "10");
        SessionExpiryChecker task = new SessionExpiryChecker();
        task.run(); // Expire any old sessions
        this.checkerTask = context.registerService(Runnable.class, task, props);

        /* Register the rig event notifier. */
        this.terminatorService = context.registerService(RigEventListener.class, new RigShutdownSessonStopper(), null);
        
        /* Add service listener to add and remove registered rig event listeners. */
        SessionActivator.rigListeners = new ArrayList<RigEventListener>();
        EventServiceListener<RigEventListener> rigServListener = 
                new EventServiceListener<RigEventListener>(SessionActivator.rigListeners, context);
        context.addServiceListener(rigServListener, '(' + Constants.OBJECTCLASS + '=' + RigEventListener.class.getName() + ')');
        for (ServiceReference<RigEventListener> ref : context.getServiceReferences(RigEventListener.class, null))
        {
            rigServListener.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED, ref));
        }

        /* Add service listener to add and remove registered session event listeners. */
        SessionActivator.sessionListeners = new ArrayList<SessionEventListener>();
        EventServiceListener<SessionEventListener> sesServListener = 
                new EventServiceListener<SessionEventListener>(sessionListeners, context);
        context.addServiceListener(sesServListener, 
                '(' + Constants.OBJECTCLASS + '=' + SessionEventListener.class.getName() + ')');
        for (ServiceReference<SessionEventListener> ref : context.getServiceReferences(SessionEventListener.class, null))
        {
            sesServListener.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED, ref));
        }
        
        /* Service listener for communication proxies. */
        SessionActivator.commsProxies = new ArrayList<RigCommunicationProxy>();
        EventServiceListener<RigCommunicationProxy> commsServs = 
                new EventServiceListener<RigCommunicationProxy>(SessionActivator.commsProxies, context);
        context.addServiceListener(commsServs, '(' + Constants.OBJECTCLASS + '=' + RigCommunicationProxy.class.getName() + ')');
        for (ServiceReference<RigCommunicationProxy> ref : context.getServiceReferences(RigCommunicationProxy.class, null))
        {
            commsServs.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED, ref));
        }

        /* Register the Session POJO service. */
        this.pojoReg = context.registerService(SessionService.class, new SessionServiceImpl(), null);

        /* Register the Session SOAP service. */
        this.logger.debug("Registering the Queuer SOAP interface service.");
        ServletContainerService soapService = new ServletContainerService();
        soapService.addServlet(new ServletContainer(new AxisServlet(), true));
        this.soapReg = context.registerService(ServletContainerService.class, soapService, null);
    }

    @Override
    public void stop(BundleContext context) throws Exception 
    {
        this.logger.info("Stopping the Session bundle...");
        this.soapReg.unregister();
        this.pojoReg.unregister();
        this.terminatorService.unregister();
        this.checkerTask.unregister();
        SessionActivator.bookingsTracker.close();

        /* Terminate all in progress sessions. */
        org.hibernate.Session ses = DataAccessActivator.getNewSession();
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
        
        SessionActivator.commsProxies = null;
        SessionActivator.sessionListeners = null;
        SessionActivator.rigListeners = null;
    }

    /**
     * Returns a booking service object or null if not booking service is running.
     * 
     * @return booking engine service or null
     */
    public static BookingEngineService getBookingService()
    {
        if (SessionActivator.bookingsTracker == null) return null;

        return SessionActivator.bookingsTracker.getService();
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
    
    /**
     * Notifies the rig event listeners of an event.
     * 
     * @param event type of event
     * @param rig the rig that the event refers to
     * @param db database
     */
    public static void notifyRigEvent(RigStateChangeEvent event, Rig rig, org.hibernate.Session db)
    {
        if (rigListeners == null) return;
        
        for (RigEventListener listener : rigListeners)
        {
            listener.eventOccurred(event, rig, db);
        }
    }
    
    /**
     * Calls notify operation on all communication proxies.
     * 
     * @param message message to send
     * @param ses session information
     * @param db database session
     */
    public static void notify(String message, Session ses, org.hibernate.Session db)
    {
        if (commsProxies == null) return;
        for (RigCommunicationProxy proxy : commsProxies)
        {
            proxy.notify(message, ses, db);
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
    
    /**
     * Gets activity detection for a rig.
     * 
     * @param ses session information
     * @param db database session
     */
    public static boolean hasActivity(Session ses, org.hibernate.Session db)
    { 
        if (commsProxies == null) return false;
        for (RigCommunicationProxy proxy : commsProxies)
        {
            if (proxy.hasActivity(ses, db)) return true;
        }
        return false;
    }
    
    /**
     * Gets activity detection for a rig.
     * 
     * @param ses session information
     * @param db database session
     */
    public static void hasActivity(Session ses, org.hibernate.Session db, ActivityAsyncCallback callback)
    { 
        if (commsProxies == null) return;
        for (RigCommunicationProxy proxy : commsProxies)
        {
            proxy.hasActivity(ses, db, callback);
        }
    }
}
