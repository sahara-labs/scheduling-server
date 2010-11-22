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

package au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities;

// Generated 06/01/2010 5:09:20 PM by Hibernate Tools 3.2.5.Beta

import static javax.persistence.GenerationType.AUTO;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * RigCapabilities entity class which maps to the rig_capabilities table.
 * <p />
 * The rig capabilities table holds the list of rig capabilities that have
 * been provided.
 */
@Entity
@Table(name = "rig_capabilities", uniqueConstraints = {@UniqueConstraint(columnNames = "capabilities")})
public class RigCapabilities implements java.io.Serializable
{
    /** Serializable class. */
    private static final long serialVersionUID = 7099369236095416135L;
    
    /** Record primary key. */
    private Long id;
    
    /** Rig capabilities string. */
    private String capabilities;
    
    private Set<MatchingCapabilities> matchingCapabilitieses = new HashSet<MatchingCapabilities>(0);
    private Set<Rig> rigs = new HashSet<Rig>(0);
    
    public RigCapabilities()
    {
        /* Bean style constructor. */
    }

    public RigCapabilities(final String capabilities)
    {
        this.capabilities = capabilities;
    }

    public RigCapabilities(final String capabilities,
            final Set<MatchingCapabilities> matchingCapabilitieses,
            final Set<Rig> rigs)
    {
        this.capabilities = capabilities;
        this.matchingCapabilitieses = matchingCapabilitieses;
        this.rigs = rigs;
    }

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId()
    {
        return this.id;
    }

    public void setId(final Long id)
    {
        this.id = id;
    }

    @Column(name = "capabilities", nullable = false, length = 255)
    public String getCapabilities()
    {
        return this.capabilities;
    }

    public void setCapabilities(final String capabilities)
    {
        this.capabilities = capabilities;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "rigCapabilities")
    public Set<MatchingCapabilities> getMatchingCapabilitieses()
    {
        return this.matchingCapabilitieses;
    }

    public void setMatchingCapabilitieses(
            final Set<MatchingCapabilities> matchingCapabilitieses)
    {
        this.matchingCapabilitieses = matchingCapabilitieses;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "rigCapabilities")
    @OrderBy("name ASC")
    public Set<Rig> getRigs()
    {
        return this.rigs;
    }

    public void setRigs(final Set<Rig> rigs)
    {
        this.rigs = rigs;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (!(o instanceof RigCapabilities)) return false;
        
        RigCapabilities r = (RigCapabilities)o;
        if (this.capabilities == null)
        {
            return false;
        }
        else
        {
            return this.capabilities.equals(r.getCapabilities());
        }
    }
   
    @Override
    public int hashCode()
    {
        if (this.capabilities == null)
        {
            return super.hashCode();
        }
        else
        {
            return this.capabilities.hashCode();
        }
    }
    
    @Override
    public String toString()
    {
        return this.capabilities;
    }
}
