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
package au.edu.uts.eng.remotelabs.schedserver.multisite.impl;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RemoteSite;
import au.edu.uts.eng.remotelabs.schedserver.server.AbstractPage;

/**
 * Federation web interface page.
 */
public class FederationPage extends AbstractPage
{
    /** Database session. */
    private Session db;
    
    public FederationPage()
    {
        this.headCss.add("/css/federation.css");
        this.headCss.add("/js/federation.js");
    }
    
    @Override
    public void preService(HttpServletRequest req)
    {
        this.db = DataAccessActivator.getNewSession();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void contents(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        /* Add the list of existing sites. */
        this.println("<div id='fed' class='ui-corner-all'>");
        this.println("  <div id='fedtitle'>");
        this.println("     <span class='ui-icon ui-icon-transferthick-e-w' style='float:left;margin-right:10px'></span>");
        this.println("     Federation Sites");
        this.println("  </div>");
        this.println("  <div id='fedlist'>");
        this.println("      <ul>");
        
        for (RemoteSite site : ((List<RemoteSite>)this.db.createCriteria(RemoteSite.class).list()))
        {
            this.println("<li>");
            this.println("  <div class='fedsitetitle " + (site.isOnline() ? "siteonline" : "siteoffline") + "'>");
            this.println("      <span class='ui-icon ui-icon-circle-arrow-e'></span>");
            this.println(this.stringTransform(site.getName()));
            this.println("  </div>");
            this.println("  <div class='fedsite'>");
            this.println(site.getServiceAddress());
            this.println("   </div>");
            this.println("</li>");
        }
        
        this.println("      </ul>");
        this.println("  </div>");
        this.println("</div>");
        
    }
    
    @Override
    public void postService(HttpServletResponse resp)
    {
        this.db.close();
    }

    @Override
    protected String getPageType()
    {
        return "Federation";
    }
}
