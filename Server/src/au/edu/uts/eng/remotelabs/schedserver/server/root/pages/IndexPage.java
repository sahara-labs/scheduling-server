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
 * @date 19th August 2011
 */

package au.edu.uts.eng.remotelabs.schedserver.server.root.pages;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import au.edu.uts.eng.remotelabs.schedserver.server.AbstractPage;

/**
 * The default page.
 */
public class IndexPage extends AbstractPage
{
    /** Page links. */
    private static final Map<String, String> links = Collections.synchronizedMap(new LinkedHashMap<String, String>());
    static
    {
//        IndexPage.links.put("Diagnostics", "/info");
//        IndexPage.links.put("Documentation", "/doc");
        IndexPage.links.put("About", "/about");
    }
    
    /** Icons. */
    private static final Map<String, String> icons = Collections.synchronizedMap(new HashMap<String, String>());
    static
    {
        IndexPage.icons.put("Documentation", "doc");
        IndexPage.icons.put("Internals", "framework");
        IndexPage.icons.put("About", "about");
        IndexPage.icons.put("Diagnostics", "runtime");        
    }
    
    /** Tooltips. */
    private static final Map<String, String> toolTips = Collections.synchronizedMap(new HashMap<String, String>());
    static 
    {
        IndexPage.toolTips.put("About", "About Scheduling Server.");
        IndexPage.toolTips.put("Internals", "Information about the internals of the Scheduling Server.");
        IndexPage.toolTips.put("Documentation", "Documentation about the Scheduling Server.");
        IndexPage.toolTips.put("Diagnostics", "Runtime information.");
    }

    @Override
    public void contents(HttpServletRequest req, HttpServletResponse resp) throws IOException
    {
        this.println("<div id='alllinks'>");
        
        /* Link pages. */
        this.println("<div id='linklist'>");
        this.println("  <ul class='ullinklist'>");

        int i = 0;
        for (Entry<String, String> e : IndexPage.links.entrySet())
        {
            String name = e.getKey();
            String classes = "linkbut indexlinkbut plaina";
            if (i == 0) classes += " ui-corner-top";
            else if (i == IndexPage.links.size() - 1) classes += " ui-corner-bottom";

            this.println("       <li><a id='" + name + "link' class='" + classes + "' href='" + e.getValue() + "'>");
            this.println("           <div class='linkbutcont'>");
            this.println("               <div class='linkbutconticon'>");
            this.println("                   <img src='/img/" + IndexPage.icons.get(name) + "_small.png' alt='" + name + "' />");
            this.println("               </div>");
            this.println("               <div class='linkbutcontlabel'>" + this.stringTransform(name) + "</div>");
            this.println("               <div id='" + name + "hover' class='tooltiphov ui-corner-all'>");
            this.println("                  <div class='tooltipimg'><img src='/img/" + IndexPage.icons.get(name) + ".png' alt='"+ name + "' /></div>");
            this.println("                  <div class='tooltipdesc'>" + IndexPage.toolTips.get(name) + "</div>");
            this.println("               </div>");
            this.println("           </div>");
            this.println("      </a></li>");

            i++;
        }
       
       this.println("   </ul>"); // ullinklist
       this.println("</div>"); // linklist
             
      this.println("</div>");
      
      this.println("<div style='clear:both; margin-bottom:20px'> </div>");
      
      /* Tooltip hover events. */
      this.println("<script type='text/javascript'>");
      
      this.println("var ttStates = new Object();");
      
      this.println( 
      		"function loadIndexToolTip(name)\n" + 
      		"{\n" + 
      		"    if (ttStates[name])\n" + 
      		"    {\n" + 
      		"        $('#' + name + 'hover').fadeIn();\n" + 
      		"        $('#' + name + 'link').css('font-weight', 'bold');\n" + 
      		"    }\n" + 
      		"}\n");
      
      this.println("$(document).ready(function() {");
      for (String name : IndexPage.toolTips.keySet())
      {
          this.println("    ttStates['" + name + "'] = false;");
          this.println("    $('#" + name + "link').hover(");
          this.println("        function() {");
          this.println("            ttStates['" + name + "'] = true;");
          this.println("            setTimeout('loadIndexToolTip(\"" + name + "\")', 1200);");
          this.println("        },");
          this.println("        function() {");
          this.println("            if (ttStates['" + name + "'])");
          this.println("            {");
          this.println("                $('#" + name + "hover').fadeOut();");
          this.println("                $('#" + name + "link').css('font-weight', 'normal');");
          this.println("                ttStates['" + name + "'] = false;");
          this.println("            }");
          this.println("        }");
          this.println("     )");
          
      }
      this.println("})");
      this.println("</script>");
    }

    @Override
    protected String getPageHeader()
    {
        return "Welcome to SAHARA Labs r3.2";
    }
    
    @Override
    protected String getPageType()
    {
        return "Main";
    }
    
    /**
     * Adds a link to the dash board.
     * 
     * @param name page name
     * @param link page link
     * @param icon page icon
     * @param toolTip page tool tip
     */
    public static void addPageLink(String name, String link, String icon, String toolTip)
    {
//        IndexPage.links.remove("Diagnostics");
//        IndexPage.links.remove("Documentation");
        IndexPage.links.remove("About");
        
        IndexPage.links.put(name, "/" + link);
        IndexPage.icons.put(name, icon);
        IndexPage.toolTips.put(name, toolTip);
        
//        IndexPage.links.put("Diagnostics", "/info");
//        IndexPage.links.put("Documentation", "/doc");
        IndexPage.links.put("About", "/about");
    }
    
    /**
     * Removes a link from the dash board.
     * 
     * @param name page name
     * @param link page link
     * @param icon page icon
     * @param toolTip page tool tip
     */
    public static void removePageLink(String name)
    {
        IndexPage.links.remove(name);
        IndexPage.icons.remove(name);
        IndexPage.toolTips.remove(name);
    }
}
