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
 * @date 4th January 2010
 */
package io.rln.ss.data.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import io.rln.ss.data.entities.Session;
import io.rln.ss.data.entities.User;

/**
 * Data access object for the {@link Session} entity.
 */
public class SessionDao extends GenericDao<Session>
{
    public SessionDao()
    {
        super(Session.class);
    }
    
    public SessionDao(org.hibernate.Session ses)
    {
        super(ses, Session.class);
    }
    
    /**
     * Gets the users active session. If the user does not have an active 
     * session <code>null</code> is returned. If the user has multiple
     * active sessions, only the lastest (ordered by request time) is 
     * returned.
     * 
     * @param user user to find active session of
     * @return active session or null
     */
    @SuppressWarnings("unchecked")
    public Session findActiveSession(User user)
    {
        Criteria cri = this.session.createCriteria(Session.class);
        cri.add(Restrictions.eq("user", user));
        cri.add(Restrictions.eq("active", Boolean.TRUE));
        cri.addOrder(Order.desc("requestTime"));
        cri.setMaxResults(1);
        
        List<Session> sessions = cri.list();
        if (sessions.size() == 0)
        {
            return null;
        }
        return sessions.get(0);
    }
    
    /**
     * Returns all the currently active sessions. If no active sessions are
     * found, an empty list is returned.
     * 
     * @return active sessions
     */
    @SuppressWarnings("unchecked")
    public List<Session> findAllActiveSessions()
    {
        Criteria cri = this.session.createCriteria(Session.class);
        cri.add(Restrictions.eq("active", Boolean.TRUE));
        return cri.list();        
    }
}
