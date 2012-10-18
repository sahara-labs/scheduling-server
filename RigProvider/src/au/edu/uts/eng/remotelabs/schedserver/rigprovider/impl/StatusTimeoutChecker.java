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
 * @date 21st February 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.rigprovider.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.criterion.Restrictions;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigLogDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.RigEventListener.RigStateChangeEvent;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.RigProviderActivator;

/**
 * Puts rigs offline if they have not provided a status update with the
 * configured rig timeout period. For a rig to be considered timeout, the
 * following conditions must hold true:
 * <ul>
 *  <li>The rig must be managed.</li>
 *  <li>The rig must be active.</li>
 *  <li>The rig must be online.</li>
 *  <li>The rig must not be in session.</li>
 *  <li>The last time the rig provided a status update must be before the
 *  current time minus the timeout period.</li>
 * </ul>
 * If the above is true for a rig, the rig is set to be offline, with
 * the offline reason as 'Timed out' and set to inactive.
 * <br />
 * The time out period may be configured with the <tt>Rig_Timeout_Period</tt>
 * which specifies the time out period in seconds. The default timeout period
 * is 300 seconds.
 */
public class StatusTimeoutChecker implements Runnable
{
    /** The default timeout in minutes. */
    public static final int DEFAULT_TIMEOUT = 100;
    
    /** The period a rig must provide a status update, otherwise the rig
     *  is put offline. */
    private int timeout;
    
    /** Logger. */
    private Logger logger;
    
    /**
     * Constructor which loads the time out period from configuration.
     */
    public StatusTimeoutChecker()
    {
        this.logger = LoggerActivator.getLogger();

        String tmStr = RigProviderActivator.getConfigurationProperty("Rig_Timeout_Period", 
                String.valueOf(StatusTimeoutChecker.DEFAULT_TIMEOUT));
        try
        {
            this.timeout = Integer.parseInt(tmStr);
            this.logger.info("Rig time out for providing a status update is " + this.timeout + " seconds.");

        }
        catch (NumberFormatException nfe)
        {
            this.timeout = StatusTimeoutChecker.DEFAULT_TIMEOUT;
            this.logger.warn("Configured rig time out period '" + tmStr + "' is not valid, using the default value " +
                    " of " + this.timeout + " seconds.");
        }
    }
    
    /**
     * Constructor.
     * 
     * @param tm timeout period in seconds
     */
    public StatusTimeoutChecker(int tm)
    {
        this.logger = LoggerActivator.getLogger();
        
        this.timeout = tm;
        this.logger.info("Rig time out for providing a status update is " + this.timeout + " seconds.");
    }

    @Override
    @SuppressWarnings("unchecked")
    public void run()
    {
        org.hibernate.Session db = null;
        try
        {
            db = DataAccessActivator.getNewSession();
            if (db == null)
            {
                this.logger.error("Unable to obtain a database session for the rig status time out checker. " +
                        "Ensure the database is running and configured database details are correct.");
                return;
            }
            
            RigLogDao rigLogDao = new RigLogDao(db);
            
            /* Get all the rigs that have timed out. */
            List<Rig> timedOut = db.createCriteria(Rig.class)
                .add(Restrictions.eq("managed", true))    // Unmanaged rigs need not provide a status update
                .add(Restrictions.eq("active", true))
                .add(Restrictions.lt("lastUpdateTimestamp", new Date(System.currentTimeMillis() - this.timeout * 1000)))
                .list();
            
            for (Rig rig : timedOut)
            {
                Calendar cal = Calendar.getInstance();
                cal.setTime(rig.getLastUpdateTimestamp());
                
                this.logger.warn("Rig " + rig.getName() + " has timed out with last status update received at " +
                        cal.get(Calendar.DATE) + '/' + (cal.get(Calendar.MONTH)+ 1) + '/' + cal.get(Calendar.YEAR) +
                        " - " + cal.get(Calendar.HOUR_OF_DAY) + ':' + cal.get(Calendar.MINUTE) + ':' + cal.get(Calendar.SECOND) +
                        ". Making rig inactive.");
                
                if (rig.getSession() != null)
                {
                    this.logger.warn("Timed out rig " + rig.getName() + " is in session so the session is being " +
                    		"terminated.");
                    Session ses = rig.getSession();
                    
                    ses.setActive(false);
                    ses.setRemovalReason("Rig timed out");
                    ses.setRemovalTime(new Date());
                    
                    rig.setInSession(false);
                    rig.setSession(null);
                }
                
                rig.setActive(false);
                rig.setOnline(false);
                rig.setOfflineReason("Timed out.");
                
                rigLogDao.addUnRegisteredLog(rig, "Timed out.");
                
                db.beginTransaction();
                db.flush();
                db.getTransaction().commit();
                
                /* Fire a notification the rig has gone offline. */
                RigProviderActivator.notifyRigEvent(RigStateChangeEvent.OFFLINE, rig, db);
            }
        }
        catch (HibernateException hex)
        {   
            this.logger.error("Failed to query database to check rig status. Exception: " + 
                    hex.getClass().getName() + ", Message:" + hex.getMessage());
            
            if (db != null && db.getTransaction() != null)
            {
                try
                {
                    db.getTransaction().rollback();
                }
                catch (HibernateException ex)
                {
                    this.logger.error("Exception rolling back up status timeout transaction (Exception: " + 
                            ex.getClass().getName() + "," + " Message: " + ex.getMessage() + ").");
                }
            }
        }
        finally
        {
            try
            {
                if (db != null) db.close();
            }
            catch (HibernateException ex)
            {
                this.logger.error("Exception cleaning up database session (Exception: " + ex.getClass().getName() + "," +
                        " Message: " + ex.getMessage() + ").");
            } 
        }
    } 
}
