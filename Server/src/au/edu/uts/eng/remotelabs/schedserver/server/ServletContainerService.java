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
package au.edu.uts.eng.remotelabs.schedserver.server;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that should be registered as a service to allow the SchedServer-Server
 * bundle to host the servlet on the embedded Jetty Server. The servlet will be
 * hosted using the bundle symbolic name as the servlet path by default or 
 * optionally as the specified path.The URL format is: <br />
 * <div align="center">
 * <tt>http(s)://&lt;hostname&gt;:&lt;port&gt'/&lt;Bundle-SymbolicName&gt;/</tt>
 * </div>
 * <br />For example, with an <strong>Axis</strong> service:</br />
 * <div align="center">
 * <tt>http://remotelabs.eng.uts.edu.au:8080/SchedulingServer-LocalRigProvider/services/LocalRigProvider?wsdl</tt>
 * </div>
 */
public class ServletContainerService
{
    /** List of servlets to host. */
    private final List<ServletContainer> servlets;
    
    /** The path spec to use. The default is to use the Bundle-SymbolicName
     *  and this is probably the more appropriate choice. */
    private final String overriddingPathSpec;
    
    public ServletContainerService()
    {
        this(null);
    }
    
    public ServletContainerService(final String pathSpecOverride)
    {
        this.overriddingPathSpec = pathSpecOverride;
        
        this.servlets = new ArrayList<ServletContainer>();
    }
    
    /**
     * Adds a servlet container.
     * 
     * @param container container to add
     */
    public void addServlet(ServletContainer container)
    {
        this.servlets.add(container);
    }
    
    /**
     * Returns array of all servlet containers.

     * @return servlet containers
     */
    public ServletContainer[] getServlets()
    {
        return this.servlets.toArray(new ServletContainer[this.servlets.size()]);
    }
    
    /**
     * @return the overriddingPathSpec
     */
    public String getOverriddingPathSpec()
    {
        return this.overriddingPathSpec;
    }
}
