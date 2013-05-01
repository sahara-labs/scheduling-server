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
 * @date 22nd March 2013
 */
package au.edu.uts.eng.remotelabs.schedserver.ands.impl;

import java.util.Date;
import java.util.HashSet;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.ConfigDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.ProjectDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Collection;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Config;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Project;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.SessionEventListener;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;

/**
 * Generates Redbox metadata ingest files for auto-publish sessions.
 */
public class RedboxIngestFilesGenerator implements SessionEventListener
{
    /** The name of the database configuration property that stores the location
     *  of the ANDS datastore. */
    public static final String STORAGE_PROP_KEY = "ANDS_Storage_Mount";
    
    /** Logger. */
    private Logger logger;
    
    public RedboxIngestFilesGenerator()
    {
        this.logger = LoggerActivator.getLogger();
    }
    
    @Override
    public void eventOccurred(SessionEvent event, Session session, org.hibernate.Session db)
    {
        /* We only care about sessions that are finished, */
        if (event != SessionEvent.FINISHED) return;
        
        /* and we only care about sessions that have a project 
         * that is published and auto-publishes metadata. */
        Project project = new ProjectDao(db).getProject(session);
        if (project == null || project.getPublishTime() == null || !project.isAutoPublishCollections()) return;
        
        /* We only need to publish a collection if the session
         * produced files. */
        if (session.getFiles().size() == 0) return;  
        
        this.logger.info("Auto publishing collection metadata for project '" + project.getActivity() + "'.");
        
        /* Add the collection. */
        Collection col = new Collection();
        col.setProject(project);
        col.setPublishTime(new Date());
        col.setUserManaged(false);
        col.setSessions(new HashSet<Session>(1));
        col.getSessions().add(session);
        
        db.beginTransaction();
        db.persist(col);
        db.getTransaction().commit();
        
        /* Get the storage location. */
        Config property = new ConfigDao(db).getConfig(STORAGE_PROP_KEY);
        if (property == null)
        {
            this.logger.warn("Unable to auto publish collection metadata for project '" + project.getActivity() + "'.");
            return;
        }
        
        /* Generate the metadata. */
        if (!new RedboxIngestFile().generateAndStore(col, property.getValue()))
        {
            this.logger.warn("Failed to generate metadata ingest data for project '" + project.getActivity() + "'. " +
            		"It will need to be manually created.");
        }
    }
}
