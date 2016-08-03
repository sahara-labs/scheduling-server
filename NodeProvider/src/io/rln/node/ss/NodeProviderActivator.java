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
import au.edu.uts.eng.remotelabs.schedserver.server.ServletContainer;
import au.edu.uts.eng.remotelabs.schedserver.server.ServletContainerService;
import io.rln.node.ss.client.NodeCommunicationsProxy;
import io.rln.node.ss.client.NodeSSLFactory;
import io.rln.node.ss.service.NodeRegistrationApi;

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
    
    /**
     * Returns the loaded node SSL factory.
     * 
     * @return node SSL factory
     */
    public static NodeSSLFactory getSocketFactory()
    {
        return nodeSSLFactory;
    }
}
