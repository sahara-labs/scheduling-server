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
package au.edu.uts.eng.remotelabs.schedserver.server;


/**
 * Class that should be registered as a service to allow the SchedServer-Server
 * bundle to host a web page that is accessible from the root of the web server.
 * Each page has:
 * <ul>
 *  <li>Name - name of the web page.</li>
 *  <li>Page - implementation of the page that extends abstract page.</li>
 *  <li>Icon - page icon.</li>
 *  <li>Tooltip - a textual description of the page.</li>
 * </ul>
 */
public class HostedPage
{
    /** Name of page. */
    private String name;
    
    /** Page implementation. */
    private Class<? extends AbstractPage> page;
    
    /** Page link. */
    private String link;
    
    /** Page link icon. */
    private String icon;
    
    /** Page tool tip. */
    private String toolTip;
    
    /** Whether to put the page in the dash board. */
    private boolean inDashBoard;
    
    /** Whether to put the page in the navigation links. */
    private boolean inNavLinks;
    
    /**
     * Hosted page.
     * 
     * @param name name of page
     * @param page page implementation
     * @param icon page link icon
     * @param toolTip tool tip
     * @param inDash whether to put in dashboard
     * @param inNav whether to put in navigation links
     */
    public HostedPage(String name, Class<? extends AbstractPage> page, String icon, String toolTip, 
            boolean inDash, boolean inNav)
    {
        this.name = name;
        this.page = page;
        this.icon = icon;
        this.toolTip = toolTip;
        this.link = name.toLowerCase().replaceAll("\\s", "");
        
        this.inDashBoard = inDash;
        this.inNavLinks = inNav;
    }

    /**
     * Gets the page name
     * 
     * @return page name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Gets the page implementation instance.
     * 
     * @return page instance
     * @throws IllegalAccessException 
     * @throws InstantiationException 
     */
    public AbstractPage getPage() throws InstantiationException, IllegalAccessException
    {
        return this.page.newInstance();
    }
    
    /**
     * Gets the class loader of the page so resources may be looked up 
     * relative to it.
     * 
     * @return pages classloader
     */
    public ClassLoader getClassLoader()
    {
        return this.page.getClassLoader();
    }

    /**
     * Gets the page link.
     * 
     * @return page link
     */
    public String getLink()
    {
        return this.link;
    }

    /**
     * Gets the page icon.
     * 
     * @return icon
     */
    public String getIcon()
    {
        return this.icon;
    }

    /**
     * Gets the page tool tip.
     * 
     * @return tooltip
     */
    public String getToolTip()
    {
        return this.toolTip;
    }
    
    /**
     * Whether the link should be put in the dash board.
     *  
     * @return whether to put in dash board
     */
    public boolean putInDashBoard()
    {
        return this.inDashBoard;
    }
    
    /**
     * Whether the link should be put in the navigation links.
     * 
     * @return whether to put in navigation links
     */
    public boolean putInNavLinks()
    {
        return this.inNavLinks;
    }
}
