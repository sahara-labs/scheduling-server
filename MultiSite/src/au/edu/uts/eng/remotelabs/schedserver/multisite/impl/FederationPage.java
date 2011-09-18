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
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RemoteSiteDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RemoteSite;
import au.edu.uts.eng.remotelabs.schedserver.server.AbstractPage;

/**
 * Federation web interface page.
 */
public class FederationPage extends AbstractPage
{
    public static final String SITES_TAB = "sites";
    public static final String MULTISITE_TIMES_TAB = "times";
    public static final String REQUESTS_TAB = "requests";
    
    /** Database session. */
    private Session db;
    
    /** This pages tabs. */
    private final Map<String, Method> tabs;
    
    /** The display name of each of the tabs. */
    private final Map<String, String> tabNames;
    
    /** Toolips for each of the tabs. */
    private final Map<String, String> tabToolTips;
    
    /** Request action. */
    private String action;
    
    public FederationPage()
    {
        this.tabs = new LinkedHashMap<String, Method>(3);
        this.tabNames = new HashMap<String, String>(this.tabs.size());
        this.tabToolTips = new HashMap<String, String>(this.tabs.size());
        
        try
        {
            /* Tab to show list of sites. */
            this.tabs.put(SITES_TAB, FederationPage.class.getDeclaredMethod("sitesTab"));
            this.tabNames.put(SITES_TAB, "Sites");
            this.tabToolTips.put(SITES_TAB, "Shows the list of sites and their details. In the consumer capacity of these " +
        		"sites, their authorisation can be set here. In the provider capacity of these sites, they " +
        		"may be interrogated for their provided resources.");
            
            /* Tab to show times that may be requested by consumers. */
            this.tabs.put(MULTISITE_TIMES_TAB, FederationPage.class.getDeclaredMethod("multisiteTimesTab"));
            this.tabNames.put(MULTISITE_TIMES_TAB, "Allowed Times");
            this.tabToolTips.put(MULTISITE_TIMES_TAB, "Allows date ranges to set for federation use of this providers " +
        		"resources. Other site will then be able to view these free times and reques their use.");
            
            this.tabs.put(REQUESTS_TAB, FederationPage.class.getDeclaredMethod("requestsTab"));
            this.tabNames.put(REQUESTS_TAB, "Requests");
            this.tabToolTips.put(REQUESTS_TAB, "Shows the list of requests other sites have made for use of this sites " +
        		"resources. The requests may be accepted or denied.");
        }
        catch (Exception e)
        {
            this.logger.error("Failed setting up federation page tabs. Exception: " + e.getClass().getSimpleName() +
                    ". This is a bug so please report it.");
        }
        
        this.headCss.add("/css/federation.css");
        this.headJs.add("/js/federation.js");
    }
    
    @Override
    public void preService(HttpServletRequest req)
    {
        this.db = DataAccessActivator.getNewSession();
        
        this.action = req.getRequestURI().substring(req.getRequestURI().lastIndexOf('/') + 1);
        if (this.action.contains("?")) this.action = this.action.substring(0, this.action.indexOf('&'));
        
        this.framing = !this.tabs.containsKey(this.action);
    }
    
    
    @Override
    public void contents(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        if ("saveauth".equals(this.action) && "POST".equals(req.getMethod()))
        {
            RemoteSite site = new RemoteSiteDao(this.db).findSite(req.getParameter("guid"));
            if (site == null)
            {
                this.logger.warn("Unable to change site authorisation for '" + req.getParameter("guid") + "' " +
                		"because the site was not found.");
                this.println("FAILED");
            }
            else
            {
                this.logger.info("Changing site authorisation for '" + site.getName() + "' to requestor: " +
                        req.getParameter("requestor") + ", viewer: " + req.getParameter("viewer") + ", authorizor: " +
                        req.getParameter("authorizor") + ".");
                
                site.setViewer("true".equalsIgnoreCase(req.getParameter("viewer")));
                site.setAuthorizer("true".equals(req.getParameter("authorizor")));
                site.setRequestor("true".equals(req.getParameter("requestor")));
                
                this.db.beginTransaction();
                this.db.flush();
                this.db.getTransaction().commit();
                
                this.println("SUCCESS");
            }
        }
        else if (this.tabNames.containsKey(this.action))
        {
            try
            {
                this.tabs.get(this.action).invoke(this);
            }
            catch (Exception e)
            {
                this.logger.error("Failed setting up federation page tabs. Exception: " + e.getClass().getSimpleName() +
                    ". This is a bug so please report it.");
            }
        }
        else this.indexPage();
    }
    
    /**
     * Index page to display the tab page.
     */
    public void indexPage()
    {
        this.println("<div id='federationpage'>");
        
        /* Tabs. */
        this.println("<div id='lefttabbar'>");
        this.println("  <ul id='lefttablist'>");
        
        int i = 0;
        for (String tab : this.tabs.keySet())
        {
            String classes = "notselectedtab";
            if (i == 0) classes = "ui-corner-tl selectedtab";
            else if (i == this.tabs.size() - 1) classes += " ui-corner-bl";

            this.println("<li><a id='" + tab + "tab' class='fedtab " + classes + "' " +
            		"onclick='loadFederationTab(\"" + tab + "\")'>");
            this.println("  <div class='linkbutcont'>");
            this.println("    <div class='linkbutcontlabel'>" + this.tabNames.get(tab) + "</div>");
            this.println("  </div>");
            this.println("  <div id='" + tab + "hover' class='tooltiphov ui-corner-all'>");
            this.println("    <div class='tooltipdesc'>" + this.tabToolTips.get(tab) + "</div>");
            this.println("  </div>");
            this.println("</a></li>");
            
            i++;
        }
        
        this.println("  </ul>");
        this.println("</div>");
        
        /* Content pane. */
        this.println("<div id='tabcontents'>");
        this.sitesTab();
        this.println("</div>");
        
        this.println("</div>"); // Federation page.
    }

    /**
     * Displays the sites this site is connected to and allows the authorisation
     * for these sites to be set.
     */
    @SuppressWarnings("unchecked")
    protected void sitesTab()
    {
        /* Add sites button. */
        this.println("<a id='addsite' class='fedbutton' onclick='addSite();'><span class='ui-icon ui-icon-circle-plus'></span>" +
        		"Add Site</a>");
        
        /* Existing sites. */
        this.println("<div id='fedsites' class='ui-corner-all'>");
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
            
            /* Site details. */
            this.println("<div class='sitedetails'>");
            this.println("<table>");
            this.println("  <tr>");
            this.println("      <td>Name:</td>");
            this.println("      <td>" + this.stringTransform(site.getName())+ "</td>");
            this.println("  </tr>");
            this.println("  <tr>");
            this.println("      <td>Address:</td>");
            this.println("      <td>" + site.getServiceAddress() + "</td>");
            this.println("  </tr>");
            this.println("  <tr>");
            this.println("      <td>Identifier:</td>");
            this.println("      <td>" + site.getGuid() + "</td>");
            this.println("  </tr>");
            this.println("  <tr>");
            this.println("      <td>Last Update:</td>");
            this.println("      <td>" + site.getLastPush() + "</td>");
            this.println("  </tr>");
            this.println("  <tr>");
            this.println("      <td>Namespace:</td>");
            this.println("      <td>" + site.getUserNamespace() + "</td>");
            this.println("  </tr>");
            this.println("</table>");
            this.println("</div>");
            
            /* Consumer authorisation. */
            String id = site.getName().toLowerCase().replaceAll("\\s", "");
            this.println("<div class='siterole'>");
            this.println("  <div class='siterolelabel'>As Consumer:</div>");
            this.println("  <table>");
            this.println("      <tr><td>Viewer:</td><td><input type='checkbox' id='" + id + "viewer' " + 
                    (site.isViewer() ? "checked='checked'" : "") + " /></td></tr>");
            this.println("      <tr><td>Requestor:</td><td><input type='checkbox' id='" + id + "requestor' " +
                    (site.isRequestor() ? "checked='checked'" : "") + " /></td></tr>");
            this.println("      <tr><td>Authorizor:</td><td><input type='checkbox' id='" + id + "authorizor' " +
            		(site.isAuthorizer() ? "checked='checked'" : "") + " /></td></tr>");
            this.println("  </table>");
            this.println("  <button type='button' onclick='saveConsumerAuth(\"" + id + "\", \"" + site.getGuid() + 
                    "\")'>Save</button>");
            this.println("</div>");
            
            /* Provider request. */
            this.println("<div class='siterole'>");
            this.println("  <div class='siterolelabel'>As Provider:</div>");
            this.println("  <a class='fedbutton' onclick='loadProviderResources(\"" + site.getGuid() + "\")'>");
            this.println("      <img src='/img/request.png' alt='request' />");
            this.println("      Request Resources");
            this.println("  </a>");
            this.println("</div>");
            
            this.println("<div class='siteclear'></div>");
            
            this.println("   </div>");
            this.println("</li>");
        }
        
        this.println("      </ul>");
        this.println("  </div>");
        this.println("</div>");
    }
    
    /**
     * Free times tab to allow the provide to declare free times.
     */
    protected void multisiteTimesTab()
    {
        this.println("Free times");
    }
    
    /**
     * Requests for permissions.
     */
    protected void requestsTab()
    {
        this.println("Requests tab");
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
