/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2011 Michael Diponio
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
 * @date 17th May 2011
 */

package au.edu.uts.eng.remotelabs.schedserver.multisite;

import org.apache.axis2.transport.http.AxisServlet;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

import au.edu.uts.eng.remotelabs.schedserver.bookings.pojo.BookingsService;
import au.edu.uts.eng.remotelabs.schedserver.config.Config;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Bookings;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.BookingsEventListener;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.SessionEventListener;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.messenger.MessengerService;
import au.edu.uts.eng.remotelabs.schedserver.multisite.impl.BookingsCancellationNotifier;
import au.edu.uts.eng.remotelabs.schedserver.multisite.impl.FederationPage;
import au.edu.uts.eng.remotelabs.schedserver.multisite.impl.SessionEventNotifier;
import au.edu.uts.eng.remotelabs.schedserver.queuer.pojo.QueuerService;
import au.edu.uts.eng.remotelabs.schedserver.server.HostedPage;
import au.edu.uts.eng.remotelabs.schedserver.server.ServletContainer;
import au.edu.uts.eng.remotelabs.schedserver.server.ServletContainerService;
import au.edu.uts.eng.remotelabs.schedserver.session.pojo.SessionService;

/**
 * Activator for the MultiSite project.
 */
public class MultiSiteActivator implements BundleActivator 
{
    /* --- Hosted services ---------------------------------------------------- */
    /** SOAP service hosting. */
    private ServiceRegistration<ServletContainerService> soapRegistration;
    
    /** Bookings event listener. */
    private ServiceRegistration<BookingsEventListener> bookingsListenerRegistration;
    
    /** Session event listener service. */
    private ServiceRegistration<SessionEventListener> sessionListenerRegistration;
    
    /** Federation web interface page. */
    private ServiceRegistration<HostedPage> pageRegistration;
    
    /* --- Tracked services that are being used. ------------------------------ */
    /** Queuer service tracker. */
    private static ServiceTracker<QueuerService, QueuerService> queuerService;
    
    /** Session service tracker. */
    private static ServiceTracker<SessionService, SessionService> sessionService;
    
    /** Bookings service tracker. */
    private static ServiceTracker<BookingsService, BookingsService> bookingsService;
    
    /** Configuration service tracker. */
    private static ServiceTracker<Config, Config> configService;
    
    /** Messenger service tracker. */
    private static ServiceTracker<MessengerService, MessengerService> messengerTracker;
    
    /** Logger. */
    private Logger logger;
    
    @Override
	public void start(BundleContext bundleContext) throws Exception 
	{
		this.logger = LoggerActivator.getLogger();
		this.logger.info("Multisite bundle starting up.");
		
		/* Track the required services. */
		MultiSiteActivator.configService = new ServiceTracker<Config, Config>(bundleContext, Config.class, null);
		MultiSiteActivator.configService.open();
		
		MultiSiteActivator.messengerTracker = 
		        new ServiceTracker<MessengerService, MessengerService>(bundleContext, MessengerService.class, null);
		
		MultiSiteActivator.queuerService = new 
		        ServiceTracker<QueuerService, QueuerService>(bundleContext, QueuerService.class, null);
		MultiSiteActivator.queuerService.open();
		
		MultiSiteActivator.sessionService = new 
		        ServiceTracker<SessionService, SessionService>(bundleContext, SessionService.class, null);
		MultiSiteActivator.sessionService.open();
		
		MultiSiteActivator.bookingsService = new
		        ServiceTracker<BookingsService, BookingsService>(bundleContext, BookingsService.class, null);
		MultiSiteActivator.bookingsService.open();
		
		this.bookingsListenerRegistration = bundleContext.registerService(BookingsEventListener.class, 
		        new BookingsCancellationNotifier(), null);
		
		this.sessionListenerRegistration = bundleContext.registerService(SessionEventListener.class, 
		        new SessionEventNotifier(), null);
		
		this.pageRegistration = bundleContext.registerService(HostedPage.class, new HostedPage("Federation", 
		        FederationPage.class, "fed", "Details about the federation.", true, true), null);

		ServletContainerService serv = new ServletContainerService();
		serv.addServlet(new ServletContainer(new AxisServlet(), true));
		this.soapRegistration = bundleContext.registerService(ServletContainerService.class, serv, null);
	}

    @Override
	public void stop(BundleContext bundleContext) throws Exception 
	{
		this.logger.info("Multisie bundle shutting down.");
		
		this.soapRegistration.unregister();
		this.bookingsListenerRegistration.unregister();
		this.sessionListenerRegistration.unregister();
		this.pageRegistration.unregister();
		
		MultiSiteActivator.bookingsService.close();
		MultiSiteActivator.bookingsService = null;
		MultiSiteActivator.sessionService.close();
		MultiSiteActivator.sessionService = null;
		MultiSiteActivator.queuerService.close();
		MultiSiteActivator.queuerService = null;
		MultiSiteActivator.messengerTracker.close();
		MultiSiteActivator.messengerTracker = null;
		MultiSiteActivator.configService.close();
		MultiSiteActivator.configService = null;
	}

    /**
     * Gets a Queuer service instance.
     * 
     * @return queuer service or null if none found
     */
    public static QueuerService getQueuerService()
    {
        if (MultiSiteActivator.queuerService == null) return null;
        
        return MultiSiteActivator.queuerService.getService();
    }
    
    /**
     * Gets a Session service instance.
     * 
     * @return session service or null if none found
     */
    public static SessionService getSessionService()
    {
        if (MultiSiteActivator.sessionService == null) return null;
        
        return MultiSiteActivator.sessionService.getService();
    }
    
    /**
     * Gets a bookings service instance.
     * 
     * @return bookings server or null if none found
     */
    public static BookingsService getBookingsService()
    {
        if (MultiSiteActivator.bookingsService == null) return null;
        
        return MultiSiteActivator.bookingsService.getService();
    }
    
    /**
     * Gets a configuration value.
     * 
     * @param property the configuration property name
     * @param defValue value to return if the config service isn't running or \
     *          property not found
     * @return configuration value or default if not found
     */
    public static String getConfigValue(String property, String defValue)
    {
        if (MultiSiteActivator.configService == null) return defValue;
        
        Config config = MultiSiteActivator.configService.getService();
        return config == null ? defValue : config.getProperty(property, defValue);
    }
    
    /**
     * Queues a cancel booking notification to be sent later.
     * 
     * @param booking booking to notify
     */
    public static void queueNotification(Bookings booking)
    {
        // TODO Implement queued notifications of booking cancellation. 
    }
}
