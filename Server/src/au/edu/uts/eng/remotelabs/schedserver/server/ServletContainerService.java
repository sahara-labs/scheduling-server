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

import javax.servlet.http.HttpServlet;

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
 * If the <code>isAxis</code>property is set to <code>true</code>, there must be a 
 * directory in the bundle Jar <tt>'META-INF'</tt> called <tt>'repo'</tt> 
 * containing the following files:
 * <ul>
 *  <li>services.list - File containing the name of the Axis archive file.<li>
 *  <li>Axis archive file (*.aar) - The Axis archive which is a zipped up 
 *  directory containing a folder called <tt>'META-INF'</tt> and within this,
 *  two files - the service WSDL and the service descriptor (services.xml).
 *  For example:
 *  <br /><br />
 *  <ul style="list-style:none">
 *    <li>| -META-INF/
 *    <li>| ---LocalRigProvider.wsdl</li>
 *    <li>| ---services.xml</li>
 *  </ul>
 * </ul>
 */
public class ServletContainerService
{
    /** The HTTP servlet. */
    private final HttpServlet servlet;
    
    /** Whether the servlet is an Axis servlet. If this is true, the 
     *  <tt>axisRepository</tt> field must be set. */
    private final boolean isAxisServlet;
    
    /** The path spec to use. The default is to use the Bundle-SymbolicName
     *  and this is probably the more appropriate choice. */
    private final String overriddingPathSpec;
    
    public ServletContainerService(final HttpServlet servlet)
    {
        this(servlet, false);
    }
    
    public ServletContainerService(final HttpServlet servlet, final boolean isAxis)
    {
        this(servlet, isAxis, null);
    }

    public ServletContainerService(final HttpServlet servlet, final boolean isAxis, final String pathSpecOverride)
    {
        this.servlet = servlet;
        this.isAxisServlet = isAxis;
        this.overriddingPathSpec = pathSpecOverride;
    }

    /**
     * @return the servlet
     */
    public HttpServlet getServlet()
    {
        return this.servlet;
    }

    /**
     * @return the isAxisServlet
     */
    public boolean isAxis()
    {
        return this.isAxisServlet;
    }
    
    /**
     * @return the overriddingPathSpec
     */
    public String getOverriddingPathSpec()
    {
        return this.overriddingPathSpec;
    }
}
