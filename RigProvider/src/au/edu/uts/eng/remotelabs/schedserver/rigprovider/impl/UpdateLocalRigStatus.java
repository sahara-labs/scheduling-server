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
 * @date 18th February 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.rigprovider.impl;

import java.util.Date;

import org.hibernate.Session;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigLogDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.RigEventListener;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.RigEventListener.RigStateChangeEvent;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.RigProviderActivator;

/**
 * Updates the status of an exist rig. The rig status is composed of a
 * status flag and optionally, if the status is bad (false), the reason
 * is it is bad.
 */
public class UpdateLocalRigStatus
{
    /** Rig data access object. */
    private final RigDao rigDao;
    
    /** Rig log data access object. */
    private final RigLogDao rigLogDao;
    
    /** Reason updating a rigs status failed. */
    private String failedReason;
    
    /** Logger. */
    private final Logger logger;
    
    
    public UpdateLocalRigStatus()
    {
        this.logger = LoggerActivator.getLogger();
        this.rigDao = new RigDao();
        this.rigLogDao = new RigLogDao(this.rigDao.getSession());
    }
    
    public UpdateLocalRigStatus(Session ses)
    {
        this.logger = LoggerActivator.getLogger();
        this.rigDao = new RigDao(ses);
        this.rigLogDao = new RigLogDao(ses);
    }
    
    /**
     * Updates the rig with the provided name status. If the rig is inactive,
     * it is reactivated, provided the contact URL is not null (removing a 
     * rig should nullify the contact URL). If it is null, the rig is not 
     * reactivated.
     * <br />
     * Activating an inactive rig, using a status update is questionable and 
     * may lead to active uncommunicable rigs, but is left to deal with
     * possible sloppy rig clients. 
     * 
     * @param name the rigs name
     * @param online true if the rig is online
     * @param offlineReason reason the rig is offline, null if the rig is \ 
     *         online
     * @return true if successfully updated the rigs status, false otherwise
     */
    public boolean updateStatus(String name, boolean online, String offlineReason)
    {        
        Rig rig = this.rigDao.findByName(name);
        if (rig == null)
        {
            this.failedReason = "Not registered";
            this.logger.warn("Unable to update the status of rig with name '" + name + "' as it does not exist.");
            return false;
        }
        
        if (rig.getContactUrl() == null)
        {
            this.failedReason = "Rig '" + name + "' is not registered, it is not active and does not have a " +
            		"contact URL.";
            this.logger.warn("Unable to update the status of rig with name '" + name + "' as it is not active and " +
            		"does not have a contact URL.");
            return false;
        }
        
        if (!rig.isActive())
        {
            rig.setActive(true);
            this.logger.info("Reactivating rig '" + name + "' because a status update was received.");
        }
        
        if (!rig.isOnline() && online)
        {
            /* Rig was offline but now came online. */
            this.logger.info("Rig '" + name + "' came online.");
            rig.setOnline(true);
            rig.setOfflineReason(null);
            rig.setLastUpdateTimestamp(new Date());
            this.rigDao.flush();
            
            /* Log rig came online. */
            this.rigLogDao.addOnlineLog(rig, "Rig came online.");
            
            /* Fire online rig event. */
            for (RigEventListener list : RigProviderActivator.getRigEventListeners())
            {
                list.eventOccurred(RigStateChangeEvent.ONLINE, rig, this.rigDao.getSession());
            }    
        }
        else if (rig.isOnline() && !online)
        {
            /* Rig was online but now has gone offline. */
            this.logger.info("Rig '" + name + "' has gone offline with reason " + offlineReason + ".");
            rig.setOnline(false);
            rig.setOfflineReason(offlineReason);
            rig.setLastUpdateTimestamp(new Date());
            this.rigDao.flush();
            
            /* Log offline reason. */
            if (offlineReason == null)
            {
                this.rigLogDao.addOfflineLog(rig, "Rig has gone offline.");
            }
            else
            {
                this.rigLogDao.addOfflineLog(rig, offlineReason);
            }
            
            /* Fire offline rig event. */
            for (RigEventListener list : RigProviderActivator.getRigEventListeners())
            {
                list.eventOccurred(RigStateChangeEvent.OFFLINE, rig, this.rigDao.getSession());
            }
        }
        else
        {
            /* Log the offline reason if it has changed. */
            if (offlineReason != null && !offlineReason.equalsIgnoreCase(rig.getOfflineReason()))
            {
                this.rigLogDao.addOfflineLog(rig, offlineReason);
            }
            
            /* Nothing interesting happened, just update the heart beat. */
            rig.setLastUpdateTimestamp(new Date());
            rig.setOfflineReason(offlineReason);
            this.rigDao.flush();
        }
        
        return true;
    }

    /**
     * @return the failedReason
     */
    public String getFailedReason()
    {
        return this.failedReason;
    }
    
    /**
     * Returns the in use database session.
     * 
     * @return database session
     */
    public Session getSession()
    {
        return this.rigDao.getSession();
    }
}
