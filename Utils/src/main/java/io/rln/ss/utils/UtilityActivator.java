/**
 * Remote Labs Next project.
 * 
 * @license See LICENSE in the top level directory for complete license terms.
 * 
 * @author Michael Diponio <michael.diponio@uts.edu.au>
 * @date 4th April 2016
 */

package io.rln.ss.utils;

import java.util.Collection;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import io.rln.ss.utils.impl.ConfigServiceLoaderImpl;
import io.rln.ss.utils.impl.PropertiesConfigServiceLoader;
import io.rln.ss.utils.impl.SchedulingServiceListener;
import io.rln.ss.utils.impl.TaskScheduler;

/**
 * Activator for the Utility bundle which has configuration, logging and threading
 * utility services.
 */
public class UtilityActivator implements BundleActivator
{
	/** Configuration service loader. */
    private ConfigServiceLoaderImpl loader;
    
    /** Loader service registration. */
    private ServiceRegistration<ConfigServiceLoader> registration;

    /** The task scheduler. */
    private TaskScheduler scheduler;
    
    /** Runnable task service listener. */
    private SchedulingServiceListener listener;
    
    /** Logger activator. */
    private LoggerActivator loggerActivator;
    
    /** Logger. */
    private Logger logger;

	@Override
	public void start(BundleContext context) throws Exception 
	{		
		
		this.loader = new PropertiesConfigServiceLoader(context);
	    this.loader.loadDefault();
	    
	    this.registration = context.registerService(ConfigServiceLoader.class, this.loader, null);
	    
	    this.loggerActivator = new LoggerActivator();
	    this.loggerActivator.start(context);
	    
	    this.logger = LoggerActivator.getLogger();
	    
		
		/* Set up task scheduler service listener. */
		this.scheduler = new TaskScheduler();
		this.listener = new SchedulingServiceListener(this.scheduler, context);
		
		/* Set up the service listener for runnable tasks with a period. */
		String filter = "(&(" + Constants.OBJECTCLASS + "=" + Runnable.class.getName() + ")(period>=1))";
		this.logger.debug("Adding a service listener with filter " + filter + '.');
		context.addServiceListener(this.listener, filter);
		
		/* Fire pseudo events for each of the previously registered tasks. */
		Collection<ServiceReference<Runnable>> refs = context.getServiceReferences(Runnable.class, filter);
		for (ServiceReference<Runnable> ref : refs)
		{
		    this.listener.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED, ref));
		}		
	}

	@Override
	public void stop(BundleContext context) throws Exception 
	{
		/* Clean up scheduler. */
		context.removeServiceListener(this.listener);
		this.scheduler.shutdown();
		
		/* Clean up configuration. */
		this.registration.unregister();
        this.loader.unloadAll();
	}

}
