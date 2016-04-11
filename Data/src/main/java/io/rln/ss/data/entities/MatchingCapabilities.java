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
 * @date 6th January 2010
 */

package io.rln.ss.data.entities;

// Generated 06/01/2010 5:09:20 PM by Hibernate Tools 3.2.5.Beta

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * MatchingCapabilities entity which maps to table matching_capabilities.
 * <p />
 * The matching_capabilities table holds all the computed matches between rig and
 * request capabilities.
 */
@Entity
@Table(name = "matching_capabilities", uniqueConstraints = @UniqueConstraint(columnNames = {
        "rig_capabilities", "request_capabilities" }))
public class MatchingCapabilities implements java.io.Serializable
{
    /** Serializable class. */
    private static final long serialVersionUID = -7581770025633403213L;
    
    /** Record primary key. */
    private MatchingCapabilitiesId id;
    
    /** Request capability that matches the rig capability. */
    private RequestCapabilities requestCapabilities;
    
    /** Rig capability that matches the request capability. */
    private RigCapabilities rigCapabilities;

    public MatchingCapabilities()
    {
        /* Bean style constructor. */
    }
    
    public MatchingCapabilities(final RequestCapabilities requestCapabilities, final RigCapabilities rigCapabilities)
    {
        this.id = new MatchingCapabilitiesId(rigCapabilities.getId(), requestCapabilities.getId());
        this.requestCapabilities = requestCapabilities;
        this.rigCapabilities = rigCapabilities;
    }

    public MatchingCapabilities(final MatchingCapabilitiesId id,
            final RequestCapabilities requestCapabilities,
            final RigCapabilities rigCapabilities)
    {
        this.id = id;
        this.requestCapabilities = requestCapabilities;
        this.rigCapabilities = rigCapabilities;
    }

    @EmbeddedId
    @AttributeOverrides( {
            @AttributeOverride(name = "rigCapabilities", column = @Column(name = "rig_capabilities", nullable = false)),
            @AttributeOverride(name = "requestCapabilities", column = @Column(name = "request_capabilities", nullable = false)) })
    public MatchingCapabilitiesId getId()
    {
        return this.id;
    }

    public void setId(final MatchingCapabilitiesId id)
    {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_capabilities", nullable = false, insertable = false, updatable = false)
    public RequestCapabilities getRequestCapabilities()
    {
        return this.requestCapabilities;
    }

    public void setRequestCapabilities(
            final RequestCapabilities requestCapabilities)
    {
        this.requestCapabilities = requestCapabilities;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rig_capabilities", nullable = false, insertable = false, updatable = false)
    public RigCapabilities getRigCapabilities()
    {
        return this.rigCapabilities;
    }

    public void setRigCapabilities(final RigCapabilities rigCapabilities)
    {
        this.rigCapabilities = rigCapabilities;
    }
}
