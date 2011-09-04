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
 * @date 22nd October 2010
 */

package au.edu.uts.eng.remotelabs.schedserver.bookings;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.axis2.transport.http.AxisServlet;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.BookingEngine;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.BookingEngine.BookingInit;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.BookingManagementTask;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine.SlotBookingEngine;
import au.edu.uts.eng.remotelabs.schedserver.bookings.pojo.BookingEngineService;
import au.edu.uts.eng.remotelabs.schedserver.bookings.pojo.BookingsService;
import au.edu.uts.eng.remotelabs.schedserver.bookings.pojo.impl.BookingsServiceImpl;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.EventServiceListener;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.RigEventListener;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.SessionEventListener;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.SessionEventListener.SessionEvent;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.messenger.MessengerService;
import au.edu.uts.eng.remotelabs.schedserver.server.ServletContainer;
import au.edu.uts.eng.remotelabs.schedserver.server.ServletContainerService;

/**
 * Bookings bundle activator.
 */
public class BookingsActivator implements BundleActivator 
{
    /** Stand-off time for bookings. */
    public static final int BOOKING_STANDOFF = 1800;
    
    /** SOAP interface hosting server service registration. */
    private ServiceRegistration<ServletContainerService> soapService;
    
    /** POJO service service. */
    private ServiceRegistration<BookingsService> pojoService;
    
    /** Engine management service registration tasks. */
    private Map<ServiceRegistration<Runnable>, BookingManagementTask> engineTasks;
    
    /** Rig event notification tasks. */
    private List<ServiceRegistration<RigEventListener>> notifServices;
    
    /** Booking engine implementation. */
    private static BookingEngine engine;
    
    /** Booking engine service. */
    private static BookingEngineService service;
    
    /** Booking engine service registration. */
    private ServiceRegistration<BookingEngineService> engineService;
    
    /** Rig event listeners list. */
    private static List<RigEventListener> rigListeners;
    
    /** The list of session event listeners. */
    private static List<SessionEventListener> sessionListeners;
    
    /** Service tracker for the messenger service. */
    private static ServiceTracker<MessengerService, MessengerService> messengerService;
    
    /** Logger. */
    private Logger logger;

	@Override
	public void start(BundleContext context) throws Exception 
	{
		this.logger = LoggerActivator.getLogger();
		this.logger.info("Starting bookings bundle...");
		
		/* Start listening for the messenger service. */
		BookingsActivator.messengerService = 
		        new ServiceTracker<MessengerService, MessengerService>(context, MessengerService.class, null);
		BookingsActivator.messengerService.open();
		
		/* Initialise the booking engine and register the engine management 
		 * tasks to periodically run. */
		SlotBookingEngine slots = new SlotBookingEngine();
		BookingInit init = slots.init();
		BookingsActivator.engine = slots;
		BookingsActivator.service = slots;
		
		this.engineTasks = new HashMap<ServiceRegistration<Runnable>, BookingManagementTask>();
		Dictionary<String, String> props = new Hashtable<String, String>(1);
		for (BookingManagementTask task : init.getTasks())
		{
		    props.put("period", String.valueOf(task.getPeriod()));
		    this.engineTasks.put(context.registerService(Runnable.class, task, props), task);
		}
		
		this.notifServices = new ArrayList<ServiceRegistration<RigEventListener>>();
		for (RigEventListener listener : init.getListeners())
		{
		    this.notifServices.add(context.registerService(RigEventListener.class, listener, null));		    
		}
		
		/* Add service listener to add and remove registered rig event listeners. */
		BookingsActivator.rigListeners = new ArrayList<RigEventListener>();
		EventServiceListener<RigEventListener> servListener = 
		        new EventServiceListener<RigEventListener>(rigListeners, context);
        context.addServiceListener(servListener, '(' + Constants.OBJECTCLASS + '=' + RigEventListener.class.getName() + ')');
        for (ServiceReference<RigEventListener> ref : context.getServiceReferences(RigEventListener.class, null))
        {
            servListener.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED, ref));
        }
        
        /* Add service listener to add and remove registered session event listeners. */
        BookingsActivator.sessionListeners = new ArrayList<SessionEventListener>();
        EventServiceListener<SessionEventListener> sesServListener = 
                new EventServiceListener<SessionEventListener>(sessionListeners, context);
        context.addServiceListener(sesServListener, 
                '(' + Constants.OBJECTCLASS + '=' + SessionEventListener.class.getName() + ')');
        for (ServiceReference<SessionEventListener> ref : context.getServiceReferences(SessionEventListener.class, null))
        {
            sesServListener.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED, ref));
        }
		
		/* Register the booking engine service. */
        this.engineService = context.registerService(BookingEngineService.class, BookingsActivator.service, null);
		
		/* Register the Bookings SOAP service. */
		this.logger.debug("Registering the Bookings services.");
		
		this.pojoService = context.registerService(BookingsService.class, new BookingsServiceImpl(), null);
		
		ServletContainerService soapService = new ServletContainerService();
	    soapService.addServlet(new ServletContainer(new AxisServlet(), true));
	    this.soapService = context.registerService(ServletContainerService.class, soapService, null);
	}
	
	@Override
	public void stop(BundleContext context) throws Exception 
	{
	    this.logger.info("Shutting down bookings bundle.");
		this.soapService.unregister();
		this.pojoService.unregister();
		this.engineService.unregister();
		
		for (ServiceRegistration<RigEventListener> s : this.notifServices) s.unregister();
		
		for (Entry<ServiceRegistration<Runnable>, BookingManagementTask> s : this.engineTasks.entrySet())
		{
		    s.getKey().unregister();
		    s.getValue().cleanUp();
		}
		
		BookingsActivator.rigListeners = null;
		BookingsActivator.sessionListeners = null;
		
		BookingsActivator.engine.cleanUp();
		
		BookingsActivator.messengerService.close();
		BookingsActivator.messengerService = null;
	}
	
	/**
	 * Returns the running booking engine.
	 * 
	 * @return booking engine.
	 */
	public static BookingEngine getBookingEngine()
	{
	    return BookingsActivator.engine;
	}
	
	/**
     * Returns the list of registered rig state change event listeners.
     * 
     * @return list of event listeners
     */
    public static RigEventListener[] getRigEventListeners()
    {
        if (rigListeners == null)
        {
            return new RigEventListener[0];
        }
        
        synchronized (rigListeners)
        {
            return rigListeners.toArray(new RigEventListener[rigListeners.size()]);
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
     * Returns a messenger service object.
     * 
     * @return messenger service
     */
    public static MessengerService getMessengerService()
    {
        if (BookingsActivator.messengerService == null) return null;
        
        return BookingsActivator.messengerService.getService();
    }
}
