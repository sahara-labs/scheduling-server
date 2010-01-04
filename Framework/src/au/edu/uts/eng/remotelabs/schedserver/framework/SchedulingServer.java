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
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.launch.Framework;
import org.osgi.framework.launch.FrameworkFactory;

/**
 * Starts the Scheduling Server OSGi framework instance and shutdown it down 
 * on program termination request.
 */
public class SchedulingServer
{
    /** Whether this Scheduling Server is to shutdown. */
    private static boolean shutdown = false;
    
    /** OSGi framework instance. */
    private Framework framework;
    
    /**
     * Runs the scheduling server until the static <code>stop</code> method
     * is invoked.
     */
    public void runSchedulingServer()
    {
        
        try
        {
            /* Load framework properties. */
            Map<String, String> props = new HashMap<String, String>();
            
            /* Load and start framework. */
            FrameworkFactory frmFactory = this.getFrameworkFactory();
            this.framework = frmFactory.newFramework(props);
            this.framework.init();
            this.framework.start();
            
            System.out.println(this.framework.getSymbolicName());
        }
        catch (Exception ex)
        {
            System.out.println("Failed because of exception " + ex.getClass().getCanonicalName() + ", with message " + 
                    ex.getMessage() + ".");
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
        SchedulingServer.shutdown = true;
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
        URL url = SchedulingServer.class.getClassLoader().getResource(
                "META-INF/services/org.osgi.framework.launch.FrameworkFactory");
        if (url != null)
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
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
