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
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import io.rln.ss.data.entities.Rig;
import io.rln.ss.data.entities.RigType;

/**
 * Data access object for the {@link Rig} class.
 */
public class RigDao extends GenericDao<Rig>
{
    /**
     * Constructor that opens a new session.
     * 
     * @throws IllegalStateException if a session factory cannot be obtained
     */
    public RigDao()
    {
        super(Rig.class);
    }
    
    /**
     * Constructor that uses the provided session. The session must be 
     * not-null and open.
     * 
     * @param ses open session
     * @throws IllegalStateException if the provided use session is null or
     *         closed
     */
    public RigDao(Session ses)
    {
        super(ses, Rig.class);
    }
    
    /**
     * Finds the rig with the specified name or <code>null</code> if not
     * found.
     * 
     * @param name rig name
     * @return rig or null if not found
     */
    public Rig findByName(String name)
    {
        return (Rig) this.session.createCriteria(Rig.class)
                .add(Restrictions.eq("name", name))
                .uniqueResult();
    }
    
    /**
     * Returns the list of free rigs in a specified rig type. Free rigs are 
     * denoted by the following flags:
     * <ol>
     *  <li>active - True<li>
     *  <li>online - True<li>
     *  <li>in_session - False<li>
     * <ol>
     * 
     * @param type rig type
     * @return list of free rigs
     */
    @SuppressWarnings("unchecked")
    public List<Rig> findFreeinType(RigType type)
    {
        Criteria cri = this.session.createCriteria(Rig.class);
        cri.add(Restrictions.eq("rigType", type));
        cri.add(Restrictions.eq("active", true));
        cri.add(Restrictions.eq("online", true));
        cri.add(Restrictions.eq("inSession", false));
        return cri.list();
    }

    /**
     * Finds a <em>meta</em> rig which has the provided name and meta data. The
     * metadata is wildcarded as it need not be the only meta data the rig has.
     * <br /> 
     * Examples of meta rigs are rigs which belong to other sites but are
     * imitated here for multisite use.
     * 
     * @param name the name of the rig
     * @param meta the meta the rig must have
     * @return rig or null if not found
     */
    public Rig findMetaRig(String name, String meta)
    {
        return (Rig) this.session.createCriteria(Rig.class)
                .add(Restrictions.eq("name", name))
                .add(Restrictions.like("meta", "%" + meta + "%"))
                .uniqueResult();
    }
}
