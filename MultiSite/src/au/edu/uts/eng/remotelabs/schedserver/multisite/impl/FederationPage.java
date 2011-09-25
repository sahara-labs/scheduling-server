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

import static au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission.CAPS_PERMISSION;
import static au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission.RIG_PERMISSION;
import static au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission.TYPE_PERMISSION;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RemoteSiteDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RequestablePermissionPeriodDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RemoteSite;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RequestablePermissionPeriod;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
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
    
    /** The actions that need to be handled. */
    private final Map<String, Method> postActions;
    
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
        this.postActions = new HashMap<String, Method>(3);
        
        this.tabs = new LinkedHashMap<String, Method>(3);
        this.tabNames = new HashMap<String, String>(this.tabs.size());
        this.tabToolTips = new HashMap<String, String>(this.tabs.size());
        
        try
        {
            /* Post actions that can be invoked through HTTP POST requests. */
            this.postActions.put("saveauth",
                    FederationPage.class.getDeclaredMethod("handleSaveAuth", HttpServletRequest.class));
            this.postActions.put("deleteperiod", 
                    FederationPage.class.getDeclaredMethod("handleDeletePeriod", HttpServletRequest.class));
            this.postActions.put("resources", 
                    FederationPage.class.getDeclaredMethod("resourcesList", HttpServletRequest.class));
            
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
        		"resources. Other sites will then be able to view these free times and request their use.");
            
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
        this.headJs.add("/js/jquery-ui-timepicker-addon.js");
        this.headJs.add("/js/federation.js");
    }
    
    @Override
    public void preService(HttpServletRequest req)
    {
        this.db = DataAccessActivator.getNewSession();
        
        this.action = req.getRequestURI().substring(req.getRequestURI().lastIndexOf('/') + 1);
        if (this.action.contains("?")) this.action = this.action.substring(0, this.action.indexOf('&'));
        
        this.framing = !(this.tabs.containsKey(this.action) || this.postActions.containsKey(this.action));
    }
    
    
    @Override
    public void contents(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        try 
        {
            System.out.println(this.action); // FIXME Remove
            if ("POST".equals(req.getMethod()) && this.postActions.containsKey(this.action))
            {
                resp.setContentType("application/json");
                this.postActions.get(this.action).invoke(this, req);
            }
            else if (this.tabNames.containsKey(this.action))
            {
                this.tabs.get(this.action).invoke(this);

            }
            else this.indexPage();
        }
        catch (Exception e)
        {
            this.logger.error("Failed setting up federation page tabs. Exception: " + e.getClass().getSimpleName() +
                    ". This is a bug so please report it.");
        }
    }

    /* ========================================================================
     * == Page display.                                                      ==
     * ======================================================================== */
    
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
        this.println("<a id='addsite' class='fedbutton fedtopbutton' onclick='addSite();'><span class='ui-icon ui-icon-circle-plus'></span>" +
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
            
            /* Provider request. */
            this.println("<div class='siterole'>");
            this.println("  <div class='siterolelabel'>As Provider:</div>");
            this.println("  <a class='fedbutton' onclick='loadProviderResources(\"" + site.getGuid() + "\")'>");
            this.println("      <img src='/img/request.png' alt='request' />");
            this.println("      Request Resources");
            this.println("  </a>");
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
            this.println("      <tr><td>Redirectee:</td><td><input type='checkbox' id='" + id + "redirectee' " +
                    (site.isRedirectee() ? "checked='checked'" : "") + " /></td></tr>");
            this.println("  </table>");
            this.println("  <button type='button' onclick='saveConsumerAuth(\"" + id + "\", \"" + site.getGuid() + 
                    "\")'>Save</button>");
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
        /* Add period button. */
        this.println("<a id='addperiod' class='fedbutton fedtopbutton' onclick='addPeriod();'>" +
        		"<span class='ui-icon ui-icon-circle-plus'></span>Add Period</a>");
        
        /* Existing sites. */
        this.println("<div id='fedperiods' class='ui-corner-all'>");
        this.println("  <div id='fedtitle'>");
        this.println("     <span class='ui-icon ui-icon-transferthick-e-w' style='float:left;margin-right:10px'></span>");
        this.println("     Allowed Time Periods");
        this.println("  </div>");
        this.println("  <div id='fedlist'>");
        
        List<RequestablePermissionPeriod> periods = new RequestablePermissionPeriodDao(this.db).getActivePeriods();
        if (periods.isEmpty())
        {
            this.println("<div class='ui-state ui-state-error ui-corner-all'>" +
            		"<span class='ui-icon ui-icon-alert'></span>No allowed time periods.</div>");
        }
        else
        {
            this.println("<ul id='timeperiodlist'>");
            
            String currentType = null;
            long currentId = 0;
            for (RequestablePermissionPeriod per : periods)
            {
                if (currentType == null || !currentType.equals(per.getType()) ||
                        TYPE_PERMISSION.equals(per.getType()) && currentId != per.getRigType().getId() ||
                        RIG_PERMISSION.equals(per.getType()) && currentId != per.getRig().getId() ||
                        CAPS_PERMISSION.equals(per.getType()) && currentId != per.getRequestCapabilities().getId())
                {
                    if (currentType != null)
                    {
                        /* Close off previous the resource section. */
                        this.println("</ul></li>");
                    }
                    
                    /* Start a new resource section. */
                    currentType = per.getType();
                    

                    this.println("<li>");
                    if (TYPE_PERMISSION.equals(per.getType()))
                    {
                        currentId = per.getRigType().getId();
                        this.println("<div class='resourceperiodheader'>Rig Type: " + 
                                this.stringTransform(per.getRigType().getName()) + "</div>");
                    }
                    else if (RIG_PERMISSION.equals(per.getType()))
                    {
                        currentId = per.getRig().getId();
                        this.println("<div class='resourceperiodheader'>Rig: " + 
                                this.stringTransform(per.getRig().getName()) + "</div>");
                    }
                    else if (CAPS_PERMISSION.equals(per.getType()))
                    {
                        currentId = per.getRequestCapabilities().getId();
                        this.println("<div class='resourceperiodheader'>Request Capabilities: " + 
                                per.getRequestCapabilities().getCapabilities() + "</div>");
                    }
                    else
                    {
                        this.logger.error("Unknown permission type '" + per.getType() + "' for request permission period '" +
                                per.getId() + "'.");
                    }
                    
                    this.println("<ul class='resourceperiodlist'>");
                }
                
                this.println("<li id='timeperiod" + per.getId() + "'>");
                
                /* Dates for periods. */
                this.println("<div class='timeperiod'>");
                this.println("<span>" + per.getStart() + "</span><span class='ui-icon ui-icon-arrowthick-1-e'></span>" + 
                        "<span>" + per.getEnd() + "</span>");
                this.println("</div>");
                
                /* Delete button. */
                this.println("<a class='timeperioddelete fedbutton' onclick='deletePeriod(" + per.getId() + ")'>" +
                        "<span class='ui-icon ui-icon-circle-close'></span></a>");
                
                this.println("  <div class='timeperiodclear'></div>");
                this.println("</li>");
            }
            
            if (currentType != null)
            {
                /* There was at least one resource section we are closing off
                 * the last resource section. */
                this.println("</ul></li>");
            }
            this.println("</ul>");
        }
        
        this.println("  </div>");
        this.println("</div>");
        
        
    }
    
    /**
     * Requests for permissions.
     */
    protected void requestsTab()
    {
        // TODO Requests tab
        this.println("Requests tab");
    }
    
    /* ========================================================================
     * == Page AJAX behaviour.                                               ==
     * ======================================================================== */
    
    /**
     * Handles saving site authorisation.
     * 
     * @param req HTTP request
     */
    protected void handleSaveAuth(HttpServletRequest req)
    {
        RemoteSite site = new RemoteSiteDao(this.db).findSite(req.getParameter("guid"));
        if (site == null)
        {
            this.logger.warn("Unable to change site authorisation for '" + req.getParameter("guid") + "' " +
            		"because the site was not found.");
            this.println("{\"success\":false}");
        }
        else
        {
            this.logger.info("Changing site authorisation for '" + site.getName() + "' to requestor: " +
                    req.getParameter("requestor") + ", viewer: " + req.getParameter("viewer") + ", authorizor: " +
                    req.getParameter("authorizor") + ", redirectee: " + req.getParameter("redirectee") + ".");
            
            site.setViewer("true".equalsIgnoreCase(req.getParameter("viewer")));
            site.setAuthorizer("true".equals(req.getParameter("authorizor")));
            site.setRequestor("true".equals(req.getParameter("requestor")));
            site.setRedirectee("true".equals(req.getParameter("redirectee")));
            
            this.db.beginTransaction();
            this.db.flush();
            this.db.getTransaction().commit();
            
            this.println("{\"success\":true}");
        }
    }
    
    /**
     * Delete the request period.
     * 
     * @param req HTTP request
     */
    protected void handleDeletePeriod(HttpServletRequest req)
    {
        RequestablePermissionPeriodDao dao = new RequestablePermissionPeriodDao(this.db);
        
        if (req.getParameter("pid") == null)
        {
            this.logger.warn("Unable to delete requestable permission period because the ID parameter (pid) was not " +
                    "provided.");
            this.println("{\"success\":false}");
            return;
        }
        
        try
        {
            RequestablePermissionPeriod period = dao.get(Long.parseLong(req.getParameter("pid")));
            period.setActive(false);
            dao.flush();
            
            this.logger.info("Request permission period '" + period.getId() + "' has been cancelled.");
            this.println("{\"success\":true}");
        }
        catch (NumberFormatException e)
        {
            this.logger.warn("Unable to delete requestable permission period because the ID parameter ('" + 
                    req.getParameter("pid") + "') was not a valid ID value.");
            this.println("{\"success\":false}");
        }
    }
    
    /**
     * Gets the list of resources of the specified type.
     * 
     * @param req HTTP request 
     */
    @SuppressWarnings("unchecked")
    protected void resourcesList(HttpServletRequest req)
    {
        String type = req.getParameter("type");
        this.buf.append("{\"type\":\"" + type + "\",\"list\":[");
        
        boolean pullBack = false;
        if ("rigtype".equals(type))
        {
            for (RigType rigType : (List<RigType>)this.db.createCriteria(RigType.class)
                    .add(Restrictions.eq("managed", Boolean.TRUE))
                    .add(Restrictions.isNull("site"))
                    .list())
            {
                this.buf.append('"' + rigType.getName() + "\",");
                pullBack = true;
            }
        }
        else if ("rig".equals(type))
        {
            for (Rig rig : (List<Rig>)this.db.createCriteria(Rig.class)
                    .add(Restrictions.eq("managed", Boolean.TRUE))
                    .add(Restrictions.isNull("site"))
                    .list())
            {
                this.buf.append('"' + rig.getName() + "\",");
                pullBack = true;
            }
        }
        else if ("caps".equals(type))
        {
            // TODO Implement caps
        }
        else
        {
            this.logger.warn("Cannot provide resoruce list because the type parameter '" + null + "' is not one of " +
            		"'rigtype', 'rig' or 'caps'.");
        }
        
        if (pullBack) this.buf.deleteCharAt(this.buf.length() - 1);
        this.println("]}");
        
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
