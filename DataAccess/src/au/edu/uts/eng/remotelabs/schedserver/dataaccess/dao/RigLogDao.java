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
package au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigLog;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;

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
     * Finds all the stored logs for the specified period for a specific
     * rig.
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
        cri.add(Restrictions.lt("timeStamp", begin));
        cri.add(Restrictions.gt("timeStamp", end));
        
        return cri.list();
    }
    
    /**
     * Finds all the stored logs for the specified period for a specific
     * rig, filtered for a specific state.
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
        cri.add(Restrictions.lt("timeStamp", begin));
        cri.add(Restrictions.gt("timeStamp", end));
        cri.add(Restrictions.eq("newState", state));
        
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
