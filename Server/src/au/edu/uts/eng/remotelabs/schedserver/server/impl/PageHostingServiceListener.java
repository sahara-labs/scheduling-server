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
 * @date 11th September 2011
 */
package au.edu.uts.eng.remotelabs.schedserver.server.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;

import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.server.AbstractPage;
import au.edu.uts.eng.remotelabs.schedserver.server.HostedPage;
import au.edu.uts.eng.remotelabs.schedserver.server.root.RootServlet;
import au.edu.uts.eng.remotelabs.schedserver.server.root.pages.IndexPage;

/**
 * Service listener for page hosting services.
 */
public class PageHostingServiceListener implements ServiceListener
{
    /** Class loaders of the bundles that have hosted pages. */
    private static final Map<String, ClassLoader> bundleClassLoaders = new HashMap<String, ClassLoader>();
    
    /** The number of hosted pages per bundle. */
    private final Map<String, Integer> numberPagesPerBundle = new HashMap<String, Integer>();
    
    /** This bundles context. */
    private BundleContext context;
    
    /** Logger. */
    private Logger logger;
    
    public PageHostingServiceListener(BundleContext context)
    {
        this.logger = LoggerActivator.getLogger();
        
        this.context = context;
    }

    @Override
    public void serviceChanged(ServiceEvent event)
    {
        HostedPage page = (HostedPage) this.context.getService(event.getServiceReference());
        String bundle = event.getServiceReference().getBundle().getSymbolicName();
        
        switch (event.getType())
        {
            case ServiceEvent.REGISTERED:
                this.logger.debug("Added page '" + page + "' from bundle '" + bundle + "' to be hosted on the " +
                		"Scheduling Server administrative interface.");
                
                RootServlet.addPage(page);
                if (page.putInDashBoard()) IndexPage
                        .addPageLink(page.getName(), page.getLink(), page.getIcon(), page.getToolTip());
                if (page.putInNavLinks()) AbstractPage.addNavLink(page.getName(), '/' + page.getLink());
                
                /* We only need the hosting pages bundle classloader once. Each 
                 * subsequent page from the same page will use the same classloader 
                 * to load its references. Minimising the number of classloaders 
                 * will quicken resource lookups. */
                if (this.numberPagesPerBundle.containsKey(bundle))
                {
                    this.numberPagesPerBundle.put(bundle, this.numberPagesPerBundle.get(bundle) + 1);
                }
                else
                {
                    this.numberPagesPerBundle.put(bundle, 1);
                    PageHostingServiceListener.bundleClassLoaders.put(bundle, page.getClassLoader());
                }
                break;
                
            case ServiceEvent.UNREGISTERING:
                this.logger.debug("Removng page '" + page.getName() + "' from bundle '" + bundle + 
                        "' from being hosted on the Scheduling Server administrative interface.");
                
                RootServlet.removePage(page);
                IndexPage.removePageLink(page.getName());
                AbstractPage.removeNavLink(page.getName());
                
                if (this.numberPagesPerBundle.get(bundle) == 1)
                {
                    this.numberPagesPerBundle.remove(bundle);
                    PageHostingServiceListener.bundleClassLoaders.remove(bundle);
                }
                else
                {
                    this.numberPagesPerBundle.put(bundle, this.numberPagesPerBundle.get(bundle) + 1);
                }
                
                break;
        }
    }

    /**
     * Gets the class loaders of bundles with hosted pages.
     * 
     * @return class loaders
     */
    public static Collection<ClassLoader> getClassLoaders()
    {
        return PageHostingServiceListener.bundleClassLoaders.values();
    }
}
