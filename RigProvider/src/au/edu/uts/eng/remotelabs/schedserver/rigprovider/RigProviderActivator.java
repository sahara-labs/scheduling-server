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
 * @date 12th January 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.rigprovider;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.apache.axis2.transport.http.AxisServlet;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

import au.edu.uts.eng.remotelabs.schedserver.config.Config;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.EventServiceListener;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.RigEventListener;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.RigEventListener.RigStateChangeEvent;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.SessionEventListener;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.SessionEventListener.SessionEvent;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.identok.IdentityToken;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.identok.impl.IdentityTokenRegister;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.impl.StatusTimeoutChecker;
import au.edu.uts.eng.remotelabs.schedserver.server.ServletContainer;
import au.edu.uts.eng.remotelabs.schedserver.server.ServletContainerService;

/**
 * Activator for the local rig provider bundle.
 */
public class RigProviderActivator implements BundleActivator
{
    /** Servlet container service registration. */
    private ServiceRegistration<ServletContainerService> serverReg;

    /** Identity token service registration. */
    private ServiceRegistration<IdentityToken> idenTokReg;
    
    /** Rig status message timeout checker. */
    private StatusTimeoutChecker tmChecker;
    
    /** Runnable status timeout checker service registration. */
    private ServiceRegistration<Runnable> runnableReg;
    
    /** Rig event listeners list. */
    private static List<RigEventListener> rigListeners;
    
    /** The list of session event listeners. */
    private static List<SessionEventListener> sessionListeners;
    
    
    /** Configuration service tracker. */
    private static ServiceTracker<Config, Config> configTracker;
    
    /** Logger. */
    private Logger logger;
    
    @Override
    public void start(final BundleContext context) throws Exception
    {
        this.logger = LoggerActivator.getLogger();
        this.logger.info("Starting " + context.getBundle().getSymbolicName() + " bundle.");
        
        RigProviderActivator.configTracker = new ServiceTracker<Config, Config>(context, Config.class, null);
        RigProviderActivator.configTracker.open();
        
        /* Service to allow other bundles to obtain identity tokens for rigs. */
        Dictionary<String, String> props = new Hashtable<String, String>();
        props.put("provider", "local");
        this.idenTokReg = context.registerService(IdentityToken.class, IdentityTokenRegister.getInstance(), props);
        
        /* Service to run the status timeout checker every 30 seconds. */
        this.tmChecker = new StatusTimeoutChecker();
        props = new Hashtable<String, String>();
        props.put("period", "30");
        this.runnableReg = context.registerService(Runnable.class, this.tmChecker, props);
        
        /* Add service listener to add and remove registered rig event listeners. */
        RigProviderActivator.rigListeners = new ArrayList<RigEventListener>();
        EventServiceListener<RigEventListener> rigServListener = 
                new EventServiceListener<RigEventListener>(RigProviderActivator.rigListeners, context);
        context.addServiceListener(rigServListener, '(' + Constants.OBJECTCLASS + '=' + RigEventListener.class.getName() + ')');
        for (ServiceReference<RigEventListener> ref : context.getServiceReferences(RigEventListener.class, null))
        {
            rigServListener.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED, ref));
        }
        
        /* Add service listener to add and remove registered session event listeners. */
        RigProviderActivator.sessionListeners = new ArrayList<SessionEventListener>();
        EventServiceListener<SessionEventListener> sesServListener = 
                new EventServiceListener<SessionEventListener>(sessionListeners, context);
        context.addServiceListener(sesServListener, 
                '(' + Constants.OBJECTCLASS + '=' + SessionEventListener.class.getName() + ')');
        for (ServiceReference<SessionEventListener> ref : context.getServiceReferences(SessionEventListener.class, null))
        {
            sesServListener.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED, ref));
        }
        
        /* Hosts external SOAP service. */
        ServletContainerService service = new ServletContainerService();
        service.addServlet(new ServletContainer(new AxisServlet(), true));
        this.serverReg = context.registerService(ServletContainerService.class, service, null);
    }

    @Override
    public void stop(final BundleContext context) throws Exception
    {
        this.logger.info("Stopping " + context.getBundle().getSymbolicName() + " bundle.");
        this.serverReg.unregister();
        
        /* Clean up identity tokens. */
        this.idenTokReg.unregister();
        IdentityTokenRegister.getInstance().expunge();
        
        this.runnableReg.unregister();
        
        RigProviderActivator.sessionListeners = null;
        
        /* Take all rigs offline. */
        org.hibernate.Session ses = DataAccessActivator.getNewSession();
        if (ses != null)
        {
            Query qu = ses.createQuery("UPDATE Rig SET active=:false, in_session=:false, online=:false, " +
            		"session_id=:null, offline_reason=:offline");
            qu.setBoolean("false", false);
            qu.setParameter("null", null, Hibernate.BIG_INTEGER);
            qu.setString("offline", "Scheduling Server shutting down.");
            
            ses.beginTransaction();
            int num = qu.executeUpdate();
            ses.getTransaction().commit();
            this.logger.info("Took " + num + " rigs offline for shutdown.");
            ses.close();
        }
        
        /* Cleanup the configuration service tracker. */
        RigProviderActivator.configTracker.close();
        RigProviderActivator.configTracker = null;
    }
    
    /**
     * Returns the specified configuration property value or if this bundle is
     * unloaded or the configuration property does not exist, the specified
     * default is returned.
     *  
     * @param prop configuration property
     * @param def default value
     * @return configured value or default
     */
    public static String getConfigurationProperty(String prop, String def)
    {
        if (RigProviderActivator.configTracker == null)
        {
            return def;
        }
        
        Config config = RigProviderActivator.configTracker.getService();
        if (config == null)
        {
            return def;
        }
        
        return config.getProperty(prop, def);
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
        if (sessionListeners == null) return;
        
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
    
    
    public static int getAsyncTimeout()
    {
    	// TODO
    	return 0;
    }
}
