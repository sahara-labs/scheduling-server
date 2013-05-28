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
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.ConfigDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.ProjectDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.SessionDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Collection;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Config;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Project;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.RigEventListener;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.SessionEventListener;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;

/**
 * Generates Redbox metadata ingest files for auto-publish sessions.
 */
public class RedboxIngestFilesGenerator implements SessionEventListener, RigEventListener, Runnable
{
    /** The name of the database configuration property that stores the location
     *  of the ANDS datastore. */
    public static final String STORAGE_PROP_KEY = "ANDS_Storage_Mount";
    
    /** List of pending sessions to generate metadata from. */
    private final Map<Rig, Session> pendingSessions;
    
    /** Logger. */
    private Logger logger;
    
    public RedboxIngestFilesGenerator()
    {
        this.logger = LoggerActivator.getLogger();
        this.pendingSessions = new ConcurrentHashMap<Rig, Session>();
    }
    
    @Override
    public void eventOccurred(SessionEvent event, Session session, org.hibernate.Session db)
    {
        /* Handles session events that are relevant to generating research
         * metadata. Finishing a session checks tracks whether metadata
         * should potentially be generated. Assigning a session is when 
         * metadata is actually generated as there will be no more pending
         * data file updates from the Rig Client. */
        switch (event)
        {
            case ASSIGNED:
                if (this.pendingSessions.containsKey(session.getRig()))
                {
                    /* A rig will no longer transfer data files for the previous 
                     * session if it starting a new session as the only transmits 
                     * data files during session. */
                    this.triggerGeneration(this.pendingSessions.remove(session.getRig()), db);
                }
                break;
                
            case FINISHED:
                /* and we only care about sessions that have a project 
                 * that is published and auto-publishes metadata. */
                Project project = new ProjectDao(db).getProject(session);
                if (project == null || project.getPublishTime() == null || !project.isAutoPublishCollections()) return; 
                
                this.pendingSessions.put(session.getRig(), session);
                break;
        }
    }
    
    @Override
    public void eventOccurred(RigStateChangeEvent event, Rig rig, org.hibernate.Session db)
    {
        /* Handles rig events that are relevant to generating rig metadata. If 
         * a rig gets unregistered then it will no longer send session data and
         * so we can safely generate metadata. */
        if (event == RigStateChangeEvent.REMOVED && this.pendingSessions.containsKey(rig))
        {
            /* We will not receive any more files from this rig so we are free to generate
             * metadata. */
            this.logger.debug("Generating metadata for rig " + rig.getName() + " because it is going offline or " +
            		"being unregistered.");
            this.triggerGeneration(this.pendingSessions.remove(rig), db);
        }
    }
    
    @Override
    public void run()
    {
        SessionDao dao = null;
        Date earliestFin = new Date(System.currentTimeMillis() - 600000);
        
        /* If a rig is infrequently used, there may not be events that allow 
         * metadata generation to proceed. This is periodically run and will
         * generate metadata after 1 hour of the session finishing. */
        try
        {
            Iterator<Entry<Rig, Session>> it = this.pendingSessions.entrySet().iterator();
            while (it.hasNext())
            {
                Entry<Rig, Session> e = it.next();
                if (e.getValue().getRemovalTime().before(earliestFin))
                {
                    /* Session finished greater than an hour ago, so we can generate 
                     * metadata. */
                    it.remove();
                    if (dao == null) dao = new SessionDao();
                    this.triggerGeneration(e.getValue(), dao.getSession());
                }
                
            }
        }
        finally
        {
            if (dao != null) dao.closeSession();
        }
    }

    /**
     * Trigger generation of data.
     * 
     * @param session session to generate metadata from
     * @param db database session
     */
    private void triggerGeneration(Session session, org.hibernate.Session db)
    {
        /* We need to get a persistent instance of session. */
        session = (Session)db.load(Session.class, session.getId());
        
        Project project = new ProjectDao(db).getProject(session);
        if (project == null) return;
        
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
