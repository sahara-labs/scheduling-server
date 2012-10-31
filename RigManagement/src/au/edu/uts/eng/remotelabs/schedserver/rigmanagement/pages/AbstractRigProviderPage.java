/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2012, University of Technology, Sydney
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
 * @date 19th October 2012
 */
package au.edu.uts.eng.remotelabs.schedserver.rigmanagement.pages;

import java.io.IOException;
import java.lang.reflect.Method;

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
 * Base class for Rig Provider administration pages which use Apache Velocity 
 * to render the view.
 */
public abstract class AbstractRigProviderPage extends AbstractPage
{
	/** Web page context which contains any dynamic variables to inject into
	 *  the template to be rendered. */
	protected final Context context;
	
	 /** Post methods. */
    private Method postMethod;
    
    /** Database session. */
    protected final Session db;
	
	public AbstractRigProviderPage()
	{
		this.context = new VelocityContext();
		
		this.db = DataAccessActivator.getNewSession();
		
		/* Added web page resources. */
		this.headCss.add("/css/rigmanagement.css");
        this.headJs.add("/js/rigmanagement.js");
	}
	
	@Override
    public void preService(HttpServletRequest req)
    {
        String requestSuf = req.getRequestURI().substring(req.getRequestURI().lastIndexOf('/'));
        if (requestSuf.length() > 1) requestSuf = requestSuf.substring(1); // Remove last '/'
  
        if ("POST".equals(req.getMethod()))
        {
            try
            {
                this.postMethod = this.getClass().getMethod(requestSuf, HttpServletRequest.class);
                this.framing = false;
            }
            catch (Exception e)
            {
                /* This is a valid case, it just means the request is not handled
                 * specially. Rather it is a page load. */
            }
        }
    }
	
	@Override
	public void contents(HttpServletRequest req, HttpServletResponse resp) throws IOException 
	{
		if (this.postMethod != null)
        {
            try
            {
                resp.setContentType("application/json");
                this.out.print(this.postMethod.invoke(this, req));
            }
            catch (Exception e)
            {
                this.logger.error("BUG: Exception invoking post method " + this.postMethod.getName() + ", message: " + 
                        e.getMessage());
            }
        }
        else
        {
            /* If it is a GET request, we are going to provide the view. */
            this.setupView(req);
            
            this.flushOut();
            
            Template template;
            try
            {
                template = Velocity.getTemplate("/META-INF/templates/" + this.getClass().getSimpleName() + ".vm");
                template.merge(this.context, this.out);
            }
            catch (Exception e)
            {
                this.logger.error("Failed to loaded view of rig provided page: " + e.getMessage());
                throw new IOException("Failed to loaded Velocity template.", e);
            }
        }
	}
	
	/**
	 * 
	 * 
	 * @param req
	 */
	public abstract void setupView(HttpServletRequest req);
    
    @Override
    public void postService(HttpServletResponse resp)
    {
        this.db.close();
    }
}
