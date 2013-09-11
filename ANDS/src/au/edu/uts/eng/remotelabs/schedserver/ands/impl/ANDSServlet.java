/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2013, University of Technology, Sydney
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
 * @date 28th May 2013
 */
package au.edu.uts.eng.remotelabs.schedserver.ands.impl;

import static au.edu.uts.eng.remotelabs.schedserver.ands.ANDSActivator.BUNDLE;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.ConfigDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.GenericDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Collection;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Config;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;

import com.eclipsesource.json.JsonObject;


/**
 * Interface to allow external triggering of metadata generation.
 */
public class ANDSServlet extends HttpServlet
{
    /** Serializable class. */
    private static final long serialVersionUID = 194451676306707157L;
    
    private final Logger logger;
    
    public ANDSServlet()
    {
        this.logger = LoggerActivator.getLogger();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        resp.getWriter().println("ANDS REST Interface.");
        resp.getWriter().println("URL: " + req.getRequestURI());
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        /* The response will always be JSON. */
        JsonObject json = new JsonObject();
        resp.setContentType("application/json");
        
        org.hibernate.Session db = DataAccessActivator.getNewSession();        
        try
        {
            /* Determine the action requested with follows the bundle route in 
             * the request URI. */
            String uri = req.getRequestURI();
            String action = uri.substring(uri.indexOf(BUNDLE) + BUNDLE.length());            
            if (action.indexOf('/', 1) > 0) action = action.substring(0, action.indexOf('/', 1));            
            
            if ("/publish".equals(action))
            {
                resp.setStatus(HttpServletResponse.SC_OK);
                this.serviceGenerate(req, json, db);
            }
            else
            {
                this.logger.warn("Unknown ANDS request action: " + action);
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                json.add("success", false);
                json.add("reason", "Unknown action");
            }
            
            /* Add the response. */
            json.writeTo(resp.getWriter());
        }
        finally
        {
            db.close();
        }
    }
    
    /**
     * Service the generate metadata request.
     * 
     * @param req request
     * @param resp response
     * @param db database session
     * @throws ServletException 
     * @throws IOException
     */
    private void serviceGenerate(HttpServletRequest req, JsonObject resp, org.hibernate.Session db) 
            throws ServletException, IOException
    {
        /* Check inputs. */
        long cID;
        try
        {
            cID = Long.parseLong(req.getParameter("collection"));
        }
        catch (NumberFormatException ex)
        {
            this.logger.warn("Collection key not supplied or was not a valid record key.");
            resp.set("success", false);
            resp.set("reason", "Param not provided.");
            return;
        }
        
        GenericDao<Collection> dao = new GenericDao<Collection>(db, Collection.class);
        Collection collection = dao.get(cID);
        if (collection == null)
        {
            this.logger.warn("Collection record not found from key " + cID + ".");
            resp.set("success", false);
            resp.set("reason", "Collection not found.");
            return;
        }
        
        /* A collection can only be published once. If it already been  
         * published, the publish will be non-null. */
        if (collection.getPublishTime() != null)
        {
            this.logger.warn("Cannot republish a collection that already has been published.");
            resp.set("success", false);
            resp.set("reason", "Collection already published.");
            return;
        }
        
        /* Looks like we can publish the collection so we will publish it. */
        Config property = new ConfigDao(db).getConfig(RedboxIngestFilesGenerator.STORAGE_PROP_KEY);
        if (property == null)
        {
            this.logger.warn("Unable to publish collection with key " + cID + " because the ingest file storage " +
            		"location was not configured."); 
            resp.set("success", false);
            resp.set("reason", "Ingest location not configured.");
            return;
        }
        
        
        this.logger.info("Manually publishing collection with key '" + cID + "'.");
        if (new RedboxIngestFile().generateAndStore(collection, property.getValue()))
        {
            collection.setPublishTime(new Date());
            dao.flush();
            
            resp.set("success", true);
        }
        else
        {
            resp.set("success", false);
            resp.set("reason", "Ingest generation failed.");
        }
    }
}



