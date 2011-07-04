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
 * @date 17th January 2010
 */

package au.edu.uts.eng.remotelabs.schedserver.messenger;



import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

import au.edu.uts.eng.remotelabs.schedserver.config.Config;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.messenger.impl.Messenger;

/**
 * Activator for the Messenger bundle.
 * <br />
 * The messenger bundle sends messengers out to users using mediums
 * such as email.
 */
public class MessengerActivator implements BundleActivator 
{
    /** Messenger. */
    private Messenger messenger;
    
    /** Service registration for the messenger service. */
    private ServiceRegistration<MessengerService> messengerReg;
    
    /** Configuration service tracker. */
    private static ServiceTracker<Config, Config> configTracker;
    
    /** Logger. */
	private Logger logger;

	@Override
	public void start(BundleContext context) throws Exception 
	{
	    this.logger = LoggerActivator.getLogger();
	    this.logger.debug("Starting the Messenger bundle.");

	    /* Set up the messenger. */
	    this.messenger = new Messenger();
	    
	    MessengerActivator.configTracker = new ServiceTracker<Config, Config>(context, Config.class.getName(), null);
	    MessengerActivator.configTracker.open();
	    Config config = MessengerActivator.configTracker.waitForService(5000);
	    if (config == null)
	    {
	        this.logger.error("Configuration service not loaded, unable to correctly configure the messenger service. " +
	        		"Notification messages will NOT be sent out.");
	    }
	    else
	    {
	        this.messenger.init(config);
	    }

	    /* Broadcast it as a service for others to use. */
	    this.messengerReg = context.registerService(MessengerService.class, this.messenger, null);
	}

	
	@Override
	public void stop(BundleContext context) throws Exception 
	{
	    this.logger.debug("Stopping the Messenger bundle.");
	    
	    this.messengerReg.unregister();
	    this.messenger.cleanUp();
	    MessengerActivator.configTracker.close();
	    MessengerActivator.configTracker = null;
	}
	
	/**
	 * Returns a configuration map.
	 * 
	 * @return configuration map
	 */
	public static Config getConfiguration()
	{
	    if (MessengerActivator.configTracker == null)
	    {
	        return null;
	    }
	    

	    return MessengerActivator.configTracker.getService();
	}
}
