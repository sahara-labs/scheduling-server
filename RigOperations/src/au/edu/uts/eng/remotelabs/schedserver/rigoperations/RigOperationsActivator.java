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
import java.util.List;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceReference;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.RigEventListener;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.RigEventListener.RigStateChangeEvent;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.SessionEventListener;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.SessionEventListener.SessionEvent;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.SessionEventServiceListener;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.rigoperations.impl.RigEventServiceListener;

/**
 * Activator for the Rig Operations bundle.
 */
public class RigOperationsActivator implements BundleActivator 
{
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
        this.logger.info("Started the Rig Operations bundle.");
        
        /* Add service listener to add and remove registered rig event listeners. */
        rigListeners = new ArrayList<RigEventListener>();
        RigEventServiceListener rigServListener = new RigEventServiceListener(rigListeners, context);
        context.addServiceListener(rigServListener, '(' + Constants.OBJECTCLASS + '=' + RigEventListener.class.getName() + ')');
        for (ServiceReference<RigEventListener> ref : context.getServiceReferences(RigEventListener.class, null))
        {
                rigServListener.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED, ref));
        }
        
        /* Add service listener to add and remove registered session event listeners. */
        sessionListeners = new ArrayList<SessionEventListener>();
        SessionEventServiceListener sesServListener = new SessionEventServiceListener(sessionListeners, context);
        context.addServiceListener(sesServListener, 
                '(' + Constants.OBJECTCLASS + '=' + SessionEventListener.class.getName() + ')');
        for (ServiceReference<SessionEventListener> ref : context.getServiceReferences(SessionEventListener.class, null))
        {
            sesServListener.serviceChanged(new ServiceEvent(ServiceEvent.REGISTERED, ref));
        }
	}

	@Override
	public void stop(BundleContext context) throws Exception 
	{
	    this.logger.info("Stopping the Rig Operations bundle.");
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
}
