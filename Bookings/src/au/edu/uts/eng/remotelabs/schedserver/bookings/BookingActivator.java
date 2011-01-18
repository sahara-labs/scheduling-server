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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.axis2.transport.http.AxisServlet;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.BookingEngine;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.BookingManagementTask;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.RigEventServiceListener;
import au.edu.uts.eng.remotelabs.schedserver.bookings.impl.slotsengine.SlotBookingEngine;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.messenger.MessengerService;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.RigEventListener;
import au.edu.uts.eng.remotelabs.schedserver.server.ServletContainer;
import au.edu.uts.eng.remotelabs.schedserver.server.ServletContainerService;

/**
 * Bookings bundle activator.
 */
public class BookingActivator implements BundleActivator 
{
    /** SOAP interface hosting server service registration. */
    private ServiceRegistration soapService;
    
    /** Engine management service registration tasks. */
    private Map<ServiceRegistration, BookingManagementTask> engineTasks;
    
    /** Rig event notification tasks. */
    private List<ServiceRegistration> notifServices;
    
    /** Booking engine implementation. */
    private static BookingEngine engine;
    
    /** Booking engine service registration. */
    private ServiceRegistration engineService;
    
    /** Rig event listeners list. */
    private static List<RigEventListener> listenerList;
    
    /** Service tracker for the messenger service. */
    private static ServiceTracker messengerService;
    
    /** Logger. */
    private Logger logger;

	@Override
	public void start(BundleContext context) throws Exception 
	{
		this.logger = LoggerActivator.getLogger();
		this.logger.info("Starting bookings bundle...");
		
		/* Start listening for the messenger service. */
		BookingActivator.messengerService = new ServiceTracker(context, MessengerService.class.getName(), null);
		BookingActivator.messengerService.open();
		
		/* Initialise the booking engine and register the engine management 
		 * tasks to periodically run. */
		BookingActivator.engine = new SlotBookingEngine();
		Properties props = new Properties();
		this.engineTasks = new HashMap<ServiceRegistration, BookingManagementTask>();
		this.notifServices = new ArrayList<ServiceRegistration>();
		for (BookingManagementTask task : BookingActivator.engine.init())
		{
		    props.put("period", String.valueOf(task.getPeriod()));
		    this.engineTasks.put(context.registerService(Runnable.class.getName(), task, props), task);
		    
		    /* If the tasks is a needs rig event notification, broadcast it. */
		    if (task instanceof RigEventListener)
		    {
		        RigEventListener listener = (RigEventListener)task;
		        this.notifServices.add(context.registerService(RigEventListener.class.getName(), listener, null));
		    }
		}
		
		listenerList = new ArrayList<RigEventListener>();
		RigEventServiceListener servListener = new RigEventServiceListener(listenerList, context);
        context.addServiceListener(servListener, '(' + Constants.OBJECTCLASS + '=' + RigEventListener.class.getName() + ')');
        
        /* Fire pseudo events for all registered services. */
        ServiceReference refs[] = context.getServiceReferences(RigEventListener.class.getName(), null);
        if (refs != null)
        {
            for (ServiceReference ref : refs)
            {
                servListener.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED, ref));
            }
        }
		
		/* Register the booking engine service. */
		this.engineService = context.registerService(BookingEngineService.class.getName(), BookingActivator.engine, null);
		
		/* Register the Bookings SOAP service. */
		this.logger.debug("Registering the Bookings SOAP service.");
		ServletContainerService soapService = new ServletContainerService();
	    soapService.addServlet(new ServletContainer(new AxisServlet(), true));
	    this.soapService = context.registerService(ServletContainerService.class.getName(), soapService, null);
	}
	
	@Override
	public void stop(BundleContext context) throws Exception 
	{
	    this.logger.info("Shutting down bookings bundle.");
		this.soapService.unregister();
		this.engineService.unregister();
		
		for (ServiceRegistration s : this.notifServices) s.unregister();
		
		for (Entry<ServiceRegistration, BookingManagementTask> s : this.engineTasks.entrySet())
		{
		    s.getKey().unregister();
		    s.getValue().cleanUp();
		}
		
		listenerList = null;
		
		BookingActivator.engine.cleanUp();
		
		BookingActivator.messengerService.close();
		BookingActivator.messengerService = null;
	}
	
	/**
	 * Returns the running booking engine.
	 * 
	 * @return booking engine.
	 */
	public static BookingEngine getBookingEngine()
	{
	    return BookingActivator.engine;
	}
	
	/**
     * Returns the list of registered rig state change event listeners.
     * 
     * @return list of event listeners
     */
    public static RigEventListener[] getRigEventListeners()
    {
        if (listenerList == null)
        {
            return new RigEventListener[0];
        }
        
        synchronized (listenerList)
        {
            return listenerList.toArray(new RigEventListener[listenerList.size()]);
        }
    }
    
    /**
     * Returns a messenger service object.
     * 
     * @return messenger service
     */
    public static MessengerService getMessengerService()
    {
        if (BookingActivator.messengerService == null) return null;
        
        return (MessengerService)BookingActivator.messengerService.getService();
    }
}
