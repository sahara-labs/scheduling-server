/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2012, University of Technology, Sydney
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
 * @date 2nd November 2012
 */
package au.edu.uts.eng.remotelabs.schedserver.rigmanagement.pojo;

import java.util.Date;

import org.hibernate.Session;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigOfflineSchedule;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;

/**
 * The interface for the Rig Management POJO service.
 */
public interface RigManagementService
{
    /**
     * Gets the status of a rig type. A rig type is online if all it's constituent 
     * rigs are online; offline if one or more rigs are offline with a scheduled 
     * offline period and there are no alarmed rigs; alarmed if at least one rig 
     * is alarmed. 
     * 
     * @param rigtype type to provide status of
     * @param db open database session 
     * @return rig type status
     */
    public ResourceStatus getRigTypeStatus(RigType rigtype, Session db);
    
    /**
     * Gets the status of a rig. 
     * 
     * @param rig rig to provide status of
     * @param db open database session
     * @return rig status
     */
    public ResourceStatus getRigStatus(Rig rig, Session db);
    
    /**
     * Put a rig offline so it cannot be queued or reserved during the specified
     * period between the start and end dates. The rig client is notified to be 
     * offline at either the start of the period if it is in the future or 
     * immediately if it is in the past.
     * 
     * @param rig rig that is to be put offline
     * @param start start of offline period
     * @param end end of offline period
     * @param reason reason for the rig to be offline in the period
     * @param db open database session 
     * @return true if rig put offline
     */
    public boolean putRigOffline(Rig rig, Date start, Date end, String reason, Session db);
    
    /**
     * Cancels an offline period. If the offline period is currently in progress,
     * the rig client is notified to be put back online.
     * 
     * @param schedule scheduled offline period
     * @param db open database session
     * @return true if rig put back online
     */
    public boolean cancelRigOffline(RigOfflineSchedule schedule, Session db);
    
    /**
     * Free the rig from an in progress session.
     * 
     * @param rig rig to free
     * @param db open database session
     * @return true if rig freed
     */
    public boolean freeRig(Rig rig, Session db);
    
    /**
     * Enumeration of rig states.
     */
    public enum ResourceStatus
    {
        /* The rig is online. */
        ONLINE,
        /* The rig is offline but marked to be offline. */
        OFFLINE, 
        /* The rig is offline and there is no offline period scheduled. */
        ALARMED 
    }
}
