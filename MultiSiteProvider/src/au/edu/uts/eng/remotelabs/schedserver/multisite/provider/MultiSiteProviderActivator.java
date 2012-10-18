/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2011, Michael Diponio
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
 * @date 17th July 2011
 */

package au.edu.uts.eng.remotelabs.schedserver.multisite.provider;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import au.edu.uts.eng.remotelabs.schedserver.config.Config;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;

/**
 * Activator for the MultiSite provider.
 */
public class MultiSiteProviderActivator implements BundleActivator 
{
    /** Configuration service tracker. */
    private static ServiceTracker<Config, Config> configTracker;
    
    /** Logger. */
    private Logger logger;
    
    @Override
    public void start(BundleContext bundleContext) throws Exception 
    {
        this.logger = LoggerActivator.getLogger();
        this.logger.info("Starting the MultiSite Provider bundle...");
        
        MultiSiteProviderActivator.configTracker = new ServiceTracker<Config, Config>(bundleContext, Config.class, null);
        MultiSiteProviderActivator.configTracker.open();
    }

    @Override
	public void stop(BundleContext bundleContext) throws Exception 
	{
        this.logger.info("Shutting down the MultiSite Provider bundle...");
        
        MultiSiteProviderActivator.configTracker.close();
        MultiSiteProviderActivator.configTracker = null;
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
        if (MultiSiteProviderActivator.configTracker == null)
        {
            return def;
        }
        
        Config config = MultiSiteProviderActivator.configTracker.getService();
        if (config == null)
        {
            return def;
        }
        
        return config.getProperty(prop, def);
    }
}
