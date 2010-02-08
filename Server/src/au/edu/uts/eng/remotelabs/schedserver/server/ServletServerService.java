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
 * bundle to host the servlet on the embedded Jetty Server. The parameters are:
 * <ul>
 *  <li><tt>servlet</tt> - The servlet that should be hosted.</li>
 *  <li><tt>pathSpec</tt> - The trailing URL suffix to specify whether this
 *  servlet is to handle a request.</li>
 *  <li><tt>isAxis</tt> - Whether the servlet is a Axis 2 servlet.</li>
 *  <li><tt>axisRepo<tt> - The Axis repository which is the path containing
 *  an Axis servlet descriptor.</li>
 * </ul>
 */
public class ServletServerService
{
    /** The HTTP servlet. */
    private final HttpServlet servlet;
    
    /** The path to specify this servlet. */
    private final String pathSpec;
    
    /** Whether the servlet is an Axis servlet. If this is true, the 
     *  <tt>axisRepository</tt> field must be set. */
    private final boolean isAxis;
    
    /** The path to the Axis repository. */
    private final String axisRepo;
    
    public ServletServerService(HttpServlet servlet, String pathSpec)
    {
        this(servlet, pathSpec, false, null);
    }
    
    public ServletServerService(final HttpServlet servlet, final String pathSpec, final boolean isAxis, final String axisRepo)
    {
        this.servlet = servlet;
        this.pathSpec = pathSpec;
        this.isAxis = isAxis;
        this.axisRepo = axisRepo;
    }

    /**
     * @return the servlet
     */
    public HttpServlet getServlet()
    {
        return this.servlet;
    }

    /**
     * @return the pathSpec
     */
    public String getPathSpec()
    {
        return this.pathSpec;
    }

    /**
     * @return the isAxis
     */
    public boolean isAxis()
    {
        return this.isAxis;
    }

    /**
     * @return the axisRepo
     */
    public String getAxisRepo()
    {
        return this.axisRepo;
    }
}
