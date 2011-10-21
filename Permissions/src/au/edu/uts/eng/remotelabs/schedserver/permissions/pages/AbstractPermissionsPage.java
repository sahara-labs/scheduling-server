/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2011, University of Technology, Sydney
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
 * @date 1st February 2011
 */
package au.edu.uts.eng.remotelabs.schedserver.permissions.pages;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;
import org.hibernate.Session;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.server.AbstractPage;

/**
 * Base classes for permission pages which uses Apache Velocity to render 
 * the views.
 */
public abstract class AbstractPermissionsPage extends AbstractPage
{  
    /** Page context which contains any dynamic variables. */
    protected final Context context = new VelocityContext();
    
    /** Database session. */
    protected final Session db;
    
    public AbstractPermissionsPage()
    {
        this.db = DataAccessActivator.getNewSession();
        
        /* Add page resources. */
        this.headCss.add("/css/permissions.css");
        this.headJs.add("/js/permissions.js");
    }
    
    @Override
    public void contents(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        if ("POST".equals(req.getMethod()))
        {
            // TODO Post handling.
        }
        else
        {
            /* If it is a GET request, we are going to provide the view. */
            this.flushOut();
            
            Template template = Velocity.getTemplate("/META-INF/templates/" + this.getClass().getSimpleName() + ".vm");
            template.merge(this.context, this.out);
        }
        
    }
    
    public abstract void setupView(HttpServletRequest req);
    
    @Override
    public void postService(HttpServletResponse resp)
    {
        this.db.close();
    }
    
}
