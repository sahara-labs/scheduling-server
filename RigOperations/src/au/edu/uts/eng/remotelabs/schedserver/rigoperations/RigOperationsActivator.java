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
 * @date 26th October 2010
 */

package au.edu.uts.eng.remotelabs.schedserver.rigoperations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.RigEventListener;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.rigoperations.impl.RigEventServiceListener;

/**
 * Activator for the Rig Operations bundle.
 */
public class RigOperationsActivator implements BundleActivator 
{
    /** Rig event listeners list. */
    private static List<RigEventListener> listenerList;
    
    /** Logger. */
    private Logger logger;
    
    @Override
	public void start(BundleContext context) throws Exception 
	{
        this.logger = LoggerActivator.getLogger();
        this.logger.info("Started the Rig Operations bundle.");
        
        /* Add service listener to add and remove registered rig event listeners. */
        listenerList = new ArrayList<RigEventListener>();
        RigEventServiceListener listener = new RigEventServiceListener(listenerList, context);
        context.addServiceListener(listener, '(' + Constants.OBJECTCLASS + '=' + RigEventListener.class.getName() + ')');
        
        /* Fire pseudo events for all registered services. */
        Collection<ServiceReference<RigEventListener>> refs = context.getServiceReferences(RigEventListener.class, null);
        
        for (ServiceReference<RigEventListener> ref : refs)
        {
                listener.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED, ref));
        }
	}

	@Override
	public void stop(BundleContext context) throws Exception 
	{
	    this.logger.info("Stopping the Rig Operations bundle.");
		listenerList = null;
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
}
