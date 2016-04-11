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
 * @date 11th January 2010
 */
package io.rln.ss.data.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import io.rln.ss.data.entities.MatchingCapabilities;
import io.rln.ss.data.entities.MatchingCapabilitiesId;
import io.rln.ss.data.entities.RequestCapabilities;
import io.rln.ss.data.entities.RigCapabilities;
import io.rln.ss.data.impl.Capabilities;

/**
 * Data access object for the {@link RigCapabilities} class.
 */
public class RigCapabilitiesDao extends GenericDao<RigCapabilities>
{
    /**
     * Constructor that opens a new session.
     * 
     * @throws IllegalStateException if a session factory cannot be obtained
     */
    public RigCapabilitiesDao()
    {
        super(RigCapabilities.class);
    }
    
    /**
     * Constructor that uses the provided session. The session must be 
     * not-null and open.
     * 
     * @param ses open session
     * @throws IllegalStateException if the provided use session is null or
     *         closed
     */
    public RigCapabilitiesDao(Session ses)
    {
        super(ses, RigCapabilities.class);
    }
    
    /**
     * Find the rig capabilities entity which match the specified capabilities.
     * <code>null</code> is returned if none is found.
     *  
     * @param capabilities capabilities to find 
     * @return capabilities or null
     */
    public RigCapabilities findCapabilites(String capabilities)
    {
        Criteria cri = this.session.createCriteria(RigCapabilities.class);
        cri.add(Restrictions.eq("capabilities", new Capabilities(capabilities).asCapabilitiesString()));
        return (RigCapabilities) cri.uniqueResult();
    }
    
    /** 
     * Adds a new rig capabilities string.  Adding a new rig capabilities string 
     * involves creating the rig capabilities record and computing all
     * the matches to the currently stored request capabilities.
     * <p />
     * A match is defined as having each request capability token present
     * as a token of the rig capabilities string. That is, all matching
     * request capabilities strings are subsets of the rig capabililities
     * string.
     * <p />
     * To denote tokens in a capabilities string, the comma ('&#44;') symbol
     * is used.
     * 
     * @param capabilities rig capabilities string
     * @return rig capabilities persistent object
     */
    @SuppressWarnings("unchecked")
    public RigCapabilities addCapabilities(String capabilities)
    {
        Capabilities caps = new Capabilities(capabilities);
        List<String> rigCapsList = caps.asCapabilitiesList();
        GenericDao<MatchingCapabilities> matchCapsDao = 
                new GenericDao<MatchingCapabilities>(this.session, MatchingCapabilities.class);
        
        /* Store the rig capabilities. */
        RigCapabilities rigCaps = this.persist(new RigCapabilities(caps.asCapabilitiesString()));
        
        /* Iterate through all the request capabilities are store matches. */
        List<RequestCapabilities> results = this.session.createCriteria(RequestCapabilities.class).list();
        for (RequestCapabilities reqCaps : results)
        {
            if (rigCapsList.containsAll(new Capabilities(reqCaps.getCapabilities()).asCapabilitiesList()))
            {
                /* Rig - Request capabilities match. */
                MatchingCapabilities match = new MatchingCapabilities();
                match.setId(new MatchingCapabilitiesId(rigCaps.getId(), reqCaps.getId()));
                match.setRequestCapabilities(reqCaps);
                match.setRigCapabilities(rigCaps);
                matchCapsDao.persist(match);
            }
        }

        
        this.session.refresh(rigCaps);
        return rigCaps;
    }
    
    /**
     * Deletes the rig capabilities identified by its primary key, including
     * all matches with request capabilities.
     * 
     * @param id primary key of rig capabilities to delete.
     */
    @Override
    public void delete(Serializable id)
    {
        this.delete(this.get(id));
    }
    
    /**
     * Deletes the rig capabilities, including all matches with request 
     * capabilities.
     * 
     * @param caps rig capabilities to delete.
     */
    @Override
    public void delete(RigCapabilities caps)
    {
        this.session.beginTransaction();
        for (MatchingCapabilities matches : caps.getMatchingCapabilitieses())
        {
            this.session.delete(matches);
        }
        
        this.session.delete(caps);
        this.session.getTransaction().commit();
    }
}
