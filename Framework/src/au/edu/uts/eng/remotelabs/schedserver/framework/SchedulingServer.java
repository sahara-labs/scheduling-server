/**
 * SAHARA Scheduling Server
 *
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
 * @date 4th January 2010
 */

package au.edu.uts.eng.remotelabs.schedserver.framework;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarFile;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;
import org.osgi.service.packageadmin.PackageAdmin;

/**
 * Starts the Scheduling Server OSGi framework instance and shutdown it down 
 * on program termination request.
 */
public class SchedulingServer
{
    /** OSGi framework instance. */
    private Framework framework;
    
    /** Ordered list of bundles symbolic names  which must be started in the specified
     *  order. */
    private final static List<String> SS_Bundles = new ArrayList<String>();
    
    static
    {
        SS_Bundles.add("SchedulingServer-Configuration");
        SS_Bundles.add("SchedulingServer-Logger");
        SS_Bundles.add("SchedulingServer-DataAccess");
    }
    
    /**
     * Runs the scheduling server until the static <code>stop</code> method
     * is invoked.
     */
    public void runSchedulingServer()
    {
        System.out.println("Classpath:" + System.getProperty("java.class.path"));
        try
        {
            /* Load and start framework. */
            FrameworkFactory frmFactory = this.getFrameworkFactory();
            this.framework = frmFactory.newFramework(new FrameworkProperties().getProperties());
            this.framework.init();
            this.framework.start();
            
            /* Add a shutdown hook to shutdown the framework cleanly. */
            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run()
                {
                    if (SchedulingServer.this.framework != null)
                    {
                        try
                        {
                            System.err.println("Shutting down Scheduling Server...");
                            SchedulingServer.this.framework.stop();
                            SchedulingServer.this.framework.waitForStop(0);
                        }
                        catch (Exception e)
                        {
                            System.err.println("Unable to shutdown framework cleanly.");
                        }
                    }
                }
            });
            
            /* Install or update bundles. */
            BundleContext context = this.framework.getBundleContext();
            Map<String, File> jars = this.getBundleJars(FrameworkProperties.BUNDLE_DIR);
            
            /* Install mandatory scheduling server bundles in order. */
            for (int i = 0; i < SchedulingServer.SS_Bundles.size(); i++)
            {
                String symName = SchedulingServer.SS_Bundles.get(i);
                File bundleJar = jars.remove(symName);
                if (bundleJar == null)
                {
                    throw new IllegalStateException("Bundle " + symName + " not found");
                }
                System.out.println("Installing bundle " + symName + " from " + bundleJar.toURI().toString() + ".");
                this.installOrUpdateBundle(context, symName, bundleJar, true);
            }
            
            /* Install the rest of the detected bundles. */
            for (Entry<String, File> e : jars.entrySet())
            {
                System.out.println("Installing bundle " + e.getKey() + " from " + 
                        e.getValue().toURI().toString() + ".");
                this.installOrUpdateBundle(context, e.getKey(), e.getValue(), false);
            }
            
            /* Start any bundles that haven't already been started. */
            for (Bundle b : context.getBundles())
            {
                if (!(b.getState() == Bundle.ACTIVE || b.getState() == Bundle.STARTING))
                {
                    System.out.println("Starting bundle " + b.getSymbolicName() + " (id " + b.getBundleId() + ").");
                    b.start();
                }
            }
 
            this.framework.waitForStop(0);
        }
        catch (Exception ex)
        {
            System.out.println("Failed... Exception: " + ex.getClass().getCanonicalName() + ", Message: " + 
                    ex.getMessage() + ".");
            System.exit(-1);
        }
    }
    
    /**
     * Starts the program running.
     */
    public static void start()
    {
        final SchedulingServer ss = new SchedulingServer();
        ss.runSchedulingServer();
    }
    
    /**
     * Stops the program running.
     */
    public static void stop()
    {
        System.exit(0);
    }
    
    /**
     * Installs the specified bundle if it is not already installed. If it is, 
     * the bundle is updated with the provided bundle jar. 
     *     
     * @param context a bundle context
     * @param name symbolic name of a bundle
     * @param bundleJar bundle file 
     * @param doStart whether to start the bundle immediately
     * @throws Exception error installing or updating a bundle
     */
    private void installOrUpdateBundle(BundleContext context, String name, File bundleJar, boolean doStart) throws Exception
    {
        Bundle bundle = null;
        
        /* Search for the bundle. */
        for (Bundle b : context.getBundles())
        {
            if (b.getSymbolicName().equals(name))
            {
                bundle = b;
                break;
            }
        }
        
        if (bundle == null)
        {
            bundle = context.installBundle(bundleJar.toURI().toString());
        }
        else
        {
            /* Bundle previously installed so going to update it. Updating a bundle
             * is a two set process. 
             *   1) The bundle is 'updated', replacing the old bundle with the new
             *      bundle.
             *   2) The bundle is 'refreshed', ensuring all dependent bundles use
             *      the newly updated bundle.*/
            bundle.update(new FileInputStream(bundleJar));
            ServiceReference ref = context.getServiceReference(PackageAdmin.class.getCanonicalName());
            if (ref != null)
            {
                PackageAdmin adm = (PackageAdmin)context.getService(ref);
                adm.refreshPackages(new Bundle[]{bundle});
                context.ungetService(ref);
            }
        }
        
        if (doStart &&  !(bundle.getState() == Bundle.ACTIVE || bundle.getState() == Bundle.STARTING))
        {
            System.out.println("Starting bundle " + bundle.getSymbolicName() + " (id " + bundle.getBundleId() + ").");
            bundle.start();
        }
    }
    
    /**
     * Gets the JAR files in a specified directory. The JAR files which do not
     * have the <code>Bundle-SymbolicName</code> field in their manifest are
     * ignored as these are not OSGi bundles.
     * 
     * @param bundlePath bundle directory
     * @return map of jar files in directory keyed by their symbolic name
     * @throws IOException error opening jar file
     */
    private Map<String, File> getBundleJars(String bundlePath) throws IOException
    {
        Map<String, File> jars = new HashMap<String, File>();
        
        File path = new File(bundlePath);
        
        if (!path.exists()) return jars;
        
        for (File file : path.listFiles())
        {
            if (file.getName().endsWith(".jar"))
            {
                JarFile jar = new JarFile(file);
                String name = jar.getManifest().getMainAttributes().getValue("Bundle-SymbolicName");
                
                if (name != null) /* Ignore Jars that aren't OSGi bundles as they cannot be installed. */
                {
                    /* Remove any directives. */
                    if (name.indexOf(';') > 0)
                    {
                        name = name.split(";", 2)[0];
                    }
                    jars.put(name, file);
                }
            }
        }
        
        return jars;
    }
    
    /**
     * Instantiates a {@link org.osgi.framework.launch.FrameworkFactory} using the resource 
     * <code>META-INF/services/org.osgi.framework.launch.FrameworkFactory</code>
     * to find the qualified name of the FrameworkFactory implementation class.
     * 
     * @return {@link FrameworkFactory} instance
     * @throws Exception failure to find or create instance
     */
    private FrameworkFactory getFrameworkFactory() throws Exception
    {
        /* Create the Framework factory (this uses instructions from 
         * http://felix.apache.org/site/apache-felix-framework-launching-and-embedding.html. */
        
        /* The following resource file should be packaged inside the OSGi
         * framework Jar file and should contain the name of the 
         * FrameworkFactory implementation class on the first non-blank, 
         * non-comment line. */
        InputStream input = SchedulingServer.class.getClassLoader().getResourceAsStream(
                "META-INF/services/org.osgi.framework.launch.FrameworkFactory");
        if (input != null)
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            Object obj = null;
            try
            {
                for (String tmp = reader.readLine(); tmp != null; tmp = reader.readLine())
                {
                    tmp = tmp.trim();
                    try
                    {
                        if (tmp.length() > 0 && tmp.charAt(0) != '#' && 
                                (obj = Class.forName(tmp).newInstance()) instanceof FrameworkFactory)
                        {
                            return (FrameworkFactory)obj;
                        }
                    }
                    catch (Exception e)
                    {
                        /* Just swallowing the class loading exception, to continue 
                         * to search the resource for the FrameworkFactory
                         * implementation class. */ 
                    }
                }
            }
            finally
            {
                reader.close();
            }
        }
        
        throw new Exception("Unable to find FrameworkFactory class.");
    }
}
