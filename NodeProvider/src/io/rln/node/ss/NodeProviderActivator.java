/*
 * VAS / RLN project.
 *
 * @author Michael Diponio <michael.diponio@uts.edu.au>
 * @date  30th July 2016
 */

package io.rln.node.ss;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

import au.edu.uts.eng.remotelabs.schedserver.bookings.pojo.BookingEngineService;
import au.edu.uts.eng.remotelabs.schedserver.bookings.pojo.BookingsService;
import au.edu.uts.eng.remotelabs.schedserver.config.Config;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.EventServiceListener;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.RigCommunicationProxy;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.RigEventListener;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.RigEventListener.RigStateChangeEvent;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.SessionEventListener;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.SessionEventListener.SessionEvent;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.queuer.pojo.QueuerService;
import au.edu.uts.eng.remotelabs.schedserver.server.ServletContainer;
import au.edu.uts.eng.remotelabs.schedserver.server.ServletContainerService;
import au.edu.uts.eng.remotelabs.schedserver.session.pojo.SessionService;
import io.rln.node.ss.client.NodeCommunicationsProxy;
import io.rln.node.ss.client.NodeSSLFactory;
import io.rln.node.ss.service.AccessApi;
import io.rln.node.ss.service.BookingsApi;
import io.rln.node.ss.service.NodeRegistrationApi;
import io.rln.node.ss.service.SchedulingApi;
import io.rln.node.ss.service.SessionApi;

/**
 * Activator for the node provider bundle.
 */
public class NodeProviderActivator implements BundleActivator 
{
    /** Node communications service. */
    private NodeCommunicationsProxy comms;
    
    /** Communication proxy to send messages to nodes. */
    private ServiceRegistration<RigCommunicationProxy> commsReg;
    
    /** Registration of hosted REST API. */
    private ServiceRegistration<ServletContainerService> restReg;
    
    /** Queuer service to obtain queue instances. */
    private static ServiceTracker<QueuerService, QueuerService> queuerTracker; 

    /** Bookings service tracker to perform booking operations. */
    public static ServiceTracker<BookingsService, BookingsService> bookingsTracker;
    
    /** Bookings engine service to check whether a node is free. */
    public static ServiceTracker<BookingEngineService, BookingEngineService> bookingServiceTracker;
    
    /** Session service to finish sessions. */
    private static ServiceTracker<SessionService, SessionService> sessionTracker;
    
    /** Rig event listeners. */
    private static List<RigEventListener> rigListeners;
    
    /** List of registered event listeners. */
    private static List<SessionEventListener> sessionListeners;
    
    /** Factory to set up SSL factories that authenticate node communication. */
    private static NodeSSLFactory nodeSSLFactory;
    
    /** Logger. */
    private Logger logger;

    public void start(BundleContext context) throws Exception 
    {
        this.logger = LoggerActivator.getLogger();
        this.logger.info("Starting " + context.getBundle().getSymbolicName() + " bundle.");
        
        ServiceReference<Config> configService = context.getServiceReference(Config.class);
        if (configService == null)
        {
            this.logger.error("Cannot start " + context.getBundle().getSymbolicName() + ", the configuration " +
                    "service was not found.");
            throw new Exception("Configuration not found.");
        }
        Config config = context.getService(configService);
        
        nodeSSLFactory = new NodeSSLFactory();
        nodeSSLFactory.init(config);
        
        queuerTracker = new ServiceTracker<QueuerService, QueuerService>(context, QueuerService.class, null);
        queuerTracker.open();
        bookingsTracker = new ServiceTracker<BookingsService, BookingsService>(context, BookingsService.class, null);
        bookingsTracker.open();
        bookingServiceTracker = new ServiceTracker<BookingEngineService, BookingEngineService>(context, BookingEngineService.class, null);
        bookingServiceTracker.open();
        sessionTracker = new ServiceTracker<SessionService, SessionService>(context, SessionService.class, null);
        sessionTracker.open();
        
        rigListeners = new ArrayList<RigEventListener>();
        EventServiceListener<RigEventListener> rigServices = 
                new EventServiceListener<RigEventListener>(rigListeners, context);
        context.addServiceListener(rigServices, '(' + Constants.OBJECTCLASS + '=' + RigEventListener.class.getName() + ')');
        for (ServiceReference<RigEventListener> ref : context.getServiceReferences(RigEventListener.class, null))
        {
            rigServices.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED, ref));
        }
        
        sessionListeners = new ArrayList<SessionEventListener>();
        EventServiceListener<SessionEventListener> sessionServices = 
                new EventServiceListener<SessionEventListener>(sessionListeners, context);
        context.addServiceListener(sessionServices, '(' + Constants.OBJECTCLASS + '=' + SessionEventListener.class + ')');
        for (ServiceReference<SessionEventListener> ref : context.getServiceReferences(SessionEventListener.class, null))
        {
            sessionServices.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED, ref));
        }
        
        /* Register communication proxy class. */
        this.comms = new NodeCommunicationsProxy();
        this.commsReg = context.registerService(RigCommunicationProxy.class, this.comms, null);
        
        /* Register hosting for API. */
        List<String> allowedHosts = new ArrayList<String>();
        String configuredHosts = config.getProperty("API_Consumers");
        if (configuredHosts != null)
        {
            allowedHosts.addAll(Arrays.asList(configuredHosts.split(",")));
        }
        
        ServletContainerService service = new ServletContainerService();
        service.addServlet(new ServletContainer(new NodeRegistrationApi(allowedHosts), false, NodeRegistrationApi.PATH));
        service.addServlet(new ServletContainer(new AccessApi(allowedHosts), false, AccessApi.PATH));
        service.addServlet(new ServletContainer(new SessionApi(allowedHosts), false, SessionApi.PATH));
        service.addServlet(new ServletContainer(new BookingsApi(allowedHosts), false, BookingsApi.PATH));
        service.addServlet(new ServletContainer(new SchedulingApi(allowedHosts), false, SchedulingApi.PATH));
        this.restReg = context.registerService(ServletContainerService.class, service, null);
        
        context.ungetService(configService);
    }

    public void stop(BundleContext context) throws Exception 
    {
        this.logger.info("Stopping " + context.getBundle().getSymbolicName() + " bundle.");
        
        this.restReg.unregister();
        
        this.commsReg.unregister();
        this.comms.shutdown();
        
        nodeSSLFactory.shutdown();
        nodeSSLFactory = null;
        
        sessionListeners = null;
        rigListeners = null;
        
        queuerTracker.close();
        queuerTracker = null;
        bookingServiceTracker.close();
        bookingServiceTracker = null;
        bookingsTracker.close();
        bookingsTracker = null;
        sessionTracker.close();
        sessionTracker = null;
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

    public static NodeSSLFactory getSocketFactory()
    {
        return nodeSSLFactory;
    }
    
    public static BookingsService getBookingService()
    {
        return bookingsTracker.getService();
    }
    
    public static BookingEngineService getBookingEngine()
    {
        return bookingServiceTracker.getService();
    }
    
    public static QueuerService getQueuer()
    {
        return queuerTracker.getService();
    }
    
    public static SessionService getSession()
    {
        return sessionTracker.getService();
    }
}
