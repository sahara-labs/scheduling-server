/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2009 - 2011, University of Technology, Sydney
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
 * @date 6th February 2011
 */
package au.edu.uts.eng.remotelabs.schedserver.rigmanagement.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigLogDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigOfflineSchedule;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.rigoperations.RigMaintenance;
import au.edu.uts.eng.remotelabs.schedserver.rigoperations.RigReleaser;

/**
 * Notifies a rig to go into maintenance if a it is in a maintenance 
 * period or to leave maintenance mode if a maintenance period
 * is finishing.
 */
public class RigMaintenanceNotifier implements Runnable
{
    /** The period this is to run. */
    public static final int RUN_PERIOD = 60;
    
    /** Flag to stop SOAP calls going out in tests. */
    private boolean notTest = true;
    
    /** Logger. */
    private Logger logger;
    
    public RigMaintenanceNotifier()
    {
        this.logger = LoggerActivator.getLogger();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run()
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        if (db == null)
        {
            this.logger.error("Unable to run rig maintenance notification service because unable to obtain a database " +
            		"session.");
            return;
        }
        
        Date ps = new Date();
        Date pe = new Date(System.currentTimeMillis() + RUN_PERIOD * 1000);
        
        try
        {
            /* ----------------------------------------------------------------
             * -- For maintenance periods that are starting, terminate the   --
             * -- rig's session and set the rig to maintenance mode.         --
             * ---------------------------------------------------------------- */ 
            Criteria qu = db.createCriteria(RigOfflineSchedule.class)
                .add(Restrictions.eq("active", Boolean.TRUE))
                .add(Restrictions.ge("startTime", ps))
                .add(Restrictions.lt("startTime", pe))
                .createCriteria("rig")
                    .add(Restrictions.eq("active", Boolean.TRUE))
                    .add(Restrictions.eq("online", Boolean.TRUE));

            for (RigOfflineSchedule sched : (List<RigOfflineSchedule>)qu.list())
            {
                Rig rig = sched.getRig();
                this.logger.info("Going to notify rig " + rig.getName()  + " to go into maintenance state at time " +
                        ps + '.');
                
                /* Check the rig isn't in session, if it is terminate the session. */
                if (sched.getRig().isInSession())
                {
                    
                    Session ses = rig.getSession();
                    this.logger.info("Need to kick off user " + ses.getUser().qName() + " from rig " + rig.getName() + 
                            " because rig is going into maintenance.");
                    ses.setActive(false);
                    ses.setRemovalTime(pe);
                    ses.setRemovalReason("Rig going into maintenance.");

                    if (this.notTest) new RigReleaser().release(ses, db);
                }
                
                rig.setOnline(false);
                rig.setOfflineReason("Rig going into maintenance.");
                new RigLogDao(db).addOfflineLog(rig, "Maintenance period starting.");
                
                db.beginTransaction();
                db.flush();
                db.getTransaction().commit();
                
                if (this.notTest) new RigMaintenance().putMaintenance(rig, true, db);
            }
            
            /* ----------------------------------------------------------------
             * -- For maintenance periods that are ending, notify the rig to --
             * -- end maintenance mode.                                      --
             * ---------------------------------------------------------------- */
            qu = db.createCriteria(RigOfflineSchedule.class)
                .add(Restrictions.eq("active", Boolean.TRUE))
                .add(Restrictions.ge("endTime", ps))
                .add(Restrictions.lt("endTime", pe));
            
            List<RigOfflineSchedule> endOffline = qu.list();
            for (RigOfflineSchedule sched : endOffline)
            {
                sched.setActive(false);
                
                Rig rig = sched.getRig();
                if (rig.isActive())
                {
                    this.logger.info("Going to notify rig " + rig.getName()  + " to clear maintenance state at time " +
                            ps + '.');
                    if (this.notTest) new RigMaintenance().clearMaintenance(rig, db);
                }
                else
                {
                    this.logger.warn("Mainteance period for rig " + rig.getName() + " is ending but it is not " +
                    		"registered.");
                }
            }
            
            if (endOffline.size() > 0)
            {
                db.beginTransaction();
                db.flush();
                db.getTransaction().commit();
            }
        }
        catch (HibernateException ex)
        {
            this.logger.error("Caught database exception in rig maintenance notification service. Exception reason: " +
                    ex.getMessage() + '.');
            
            if (db.isDirty())
            {
                db.getTransaction().rollback();
            }
        }
        finally
        {
            db.close();
        }
    }

}
