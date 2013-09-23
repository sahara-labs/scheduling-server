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

import javax.servlet.Servlet;

/**
 * Container for a servlet and a flag to indicate if it is a
 * Apache Axis servlet.
 * 
 * If the Axis flag is set to <code>true</code>, there must be a 
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
 * 
 * @see ServletContainerService
 */
public class ServletContainer
{
    /** The servlet to host. */
    private final Servlet servlet;
    
    /** Whether the servlet is an Axis servlet. */
    private final boolean isAxis;
    
    /** The path to host the servlet, relative to the bundles path. */
    private final String path;
    
    /**
     * Constructor for a servlet that is <em>NOT</em> a axis servlet and takes
     * the root path of the bundles path. 
     * 
     * @param _servlet servlet
     */
    public ServletContainer(final Servlet _servlet)
    {
        this(_servlet, false);
    }
    
    /**
     * Constructor for a servlet that takes the root path of the bundles
     * path.
     *  
     * @param _servlet servlet 
     * @param axis whether it is a Axis servlet
     */
    public ServletContainer(final Servlet _servlet, final boolean axis)
    {
        this(_servlet, axis, "/*");
    }
    
    public ServletContainer(final Servlet _servlet, final boolean axis, String path)
    {
        this.servlet = _servlet;
        this.isAxis = axis;
        
        if (path == null || path.length() < 2)
        {
            this.path = "/*";
        }
        else if (path.charAt(path.length() - 1) == '/')
        {
            this.path = path + "*";
        }
        else if (path.charAt(path.length() - 2) != '/')
        {
            this.path = path + "/*";
        }
        else
        {
            this.path = path;
        }
    }

    /**
     * @return the servlet
     */
    public Servlet getServlet()
    {
        return this.servlet;
    }

    /**
     * @return the isAxis
     */
    public boolean isAxis()
    {
        return this.isAxis;
    }

    /**
     * @return the path
     */
    public String getPath()
    {
        return this.path;
    }
    
}
