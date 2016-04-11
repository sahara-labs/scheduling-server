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
 * @date 31st May 2010
 */
package io.rln.ss.data.dao;

import static io.rln.ss.data.entities.RigLog.NOT_REGISTERED;
import static io.rln.ss.data.entities.RigLog.OFFLINE;
import static io.rln.ss.data.entities.RigLog.ONLINE;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import io.rln.ss.data.entities.Rig;
import io.rln.ss.data.entities.RigLog;
import io.rln.ss.data.entities.RigType;

/**
 * Data access object for the {@link RigLog} class.
 */
public class RigLogDao extends GenericDao<RigLog>
{
    /**
     * Constructor that opens a new session.
     * 
     * @throws IllegalStateException if a session factory cannot be obtained
     */
    public  RigLogDao()
    {
        super(RigLog.class);
    }
    
    /**
     * Constructor that uses the provided session. The session must be 
     * not-null and open.
     * 
     * @param ses open session
     * @throws IllegalStateException if the provided use session is null or
     *         closed
     */
    public RigLogDao(Session ses)
    {
        super(ses, RigLog.class);
    }
    
    /**
     * Adds a log message that specifies a rig has been registered.
     * 
     * @param rig the rig that has been registered
     * @param reason registration reason
     * @return log added
     */
    public RigLog addRegisteredLog(Rig rig, String reason)
    {
        RigLog log = new RigLog();
        log.setOldState(NOT_REGISTERED);
        
        if (rig.isOnline())
        {
            log.setNewState(ONLINE);
        }
        else
        {
            log.setNewState(OFFLINE);
        }
        
        log.setRig(rig);
        log.setReason(reason);
        log.setTimeStamp(new Date());
        
        return this.persist(log);
    }
    
    /**
     * Adds a log message that specifies a rig has been put online.
     * 
     * @param rig the rig that is going online
     * @param reason online reason
     * @return log added
     */
    public RigLog addOnlineLog(Rig rig, String reason)
    {
        RigLog log = new RigLog();
        log.setOldState(OFFLINE);
        log.setNewState(ONLINE);
        log.setRig(rig);
        log.setReason(reason);
        log.setTimeStamp(new Date());
        
        return this.persist(log);
    }
    
    /**
     * Adds a log message that specifies a rig has been put offline.
     * 
     * @param rig the rig that is going offline
     * @param reason offline reason
     * @return log added
     */
    public RigLog addOfflineLog(Rig rig, String reason)
    {
        RigLog log = new RigLog();
        log.setOldState(ONLINE);
        log.setNewState(OFFLINE);
        log.setRig(rig);
        log.setReason(reason);
        log.setTimeStamp(new Date());
        
        return this.persist(log);
    }
    
    /**
     * Adds a log message that specifies a rig has been unregistered.
     * 
     * @param rig the rig that is going online
     * @param reason unregister reason
     * @return log added
     */
    public RigLog addUnRegisteredLog(Rig rig, String reason)
    {
        RigLog log = new RigLog();
        
        if (rig.isOnline())
        {
            log.setOldState(ONLINE);
        }
        else
        {
            log.setOldState(OFFLINE);
        }
        
        log.setNewState(NOT_REGISTERED);
        log.setRig(rig);
        log.setReason(reason);
        log.setTimeStamp(new Date());
        
        return this.persist(log);
    }
    
    /**
     * Finds all the stored logs for the specified period for a specific
     * rig. The logs are ordered by their time stamp with the earliest log 
     * first.
     * 
     * @param rig rig to find the logs for
     * @param begin beginning of a period
     * @param end end of a period
     * @return list of logs
     */
    @SuppressWarnings("unchecked")
    public List<RigLog> findLogs(Rig rig, Date begin, Date end)
    {
        Criteria cri = this.session.createCriteria(RigLog.class);
        cri.add(Restrictions.eq("rig", rig));
        cri.add(Restrictions.gt("timeStamp", begin));
        cri.add(Restrictions.lt("timeStamp", end));
        cri.addOrder(Order.asc("timeStamp"));
        return cri.list();
    }
    
    /**
     * Finds all the stored logs for the specified period for a specific
     * rig, filtered for a specific state. The logs are ordered by their
     * time stamp with the earliest log first.
     * 
     * @param rig rig to find the logs for
     * @param begin beginning of a period
     * @param end end of a period
     * @return list of logs
     */
    @SuppressWarnings("unchecked")
    public  List<RigLog> findLogsOfState(Rig rig, String state, Date begin, Date end)
    {
        Criteria cri = this.session.createCriteria(RigLog.class);
        cri.add(Restrictions.eq("rig", rig));
        cri.add(Restrictions.gt("timeStamp", begin));
        cri.add(Restrictions.lt("timeStamp", end));
        cri.add(Restrictions.eq("newState", state));
        cri.addOrder(Order.asc("timeStamp"));
        return cri.list();
    }
    
    /**
     * Finds all the logs for a specified period for all the rigs in a specific
     * type.
     * 
     * @param type rig type of all the rigs to find the logs off
     * @param begin beginning of a period
     * @param end end of a period
     * @return list of logs for each rig
     */
    public Map<Rig, List<RigLog>> findLogs(RigType type, Date begin, Date end)
    {
        Map<Rig, List<RigLog>> logs = new HashMap<Rig, List<RigLog>>();
        
        for (Rig rig : type.getRigs())
        {
            logs.put(rig, this.findLogs(rig, begin, end));
        }
        
        return logs;
    }
    
    /**
     * Finds all the logs for a specified period for all the rigs in a specific
     * type filtered for a speific state
     * 
     * @param type rig type of all the rigs to find the logs off
     * @param state state to filter logs on
     * @param begin beginning of a period
     * @param end end of a period
     * @return list of logs for each rig
     */
    public Map<Rig, List<RigLog>> findLogsOfState(RigType type, String state, Date begin, Date end)
    {
        Map<Rig, List<RigLog>> logs = new HashMap<Rig, List<RigLog>>();
        
        for (Rig rig : type.getRigs())
        {
            logs.put(rig, this.findLogsOfState(rig, state, begin, end));
        }
        
        return logs;
    }
}
