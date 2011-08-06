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

import static javax.persistence.GenerationType.AUTO;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * RigType entity class which maps to the rig_type table.
 * <p />
 * The rig type class holds all the defined rig types.
 */
@Entity
@Table(name = "rig_type", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class RigType implements java.io.Serializable
{
    /** Serializable class. */
    private static final long serialVersionUID = -3717782764245485528L;
    
    /** Record primary key. */
    private Long id;
    
    /** Name of the rig type. */
    private String name;
    
    /** Whether this rig type is managed by this rig type. */
    private boolean managed;
    
    /** Meta information about the rig type. */
    private String meta;
    
    /** The amount of grace time given during a system initiated session 
     *  termination.  */
    private int logoffGraceDuration;
    
    /** Whether code may be assigned to the rig during rig assignment 
     *  requests. */
    private boolean codeAssignable;
    
    /** The duration in seconds that allocation is expected to take. */
    private int setUpTime;
    
    /** The duration in seconds that release is expected to take. */
    private int tearDownTime;
    
    /** The site at which this rig type resides if not local. */
    private RemoteSite site;
    
    /** Foreign key relations. */
    private Set<ResourcePermission> resourcePermissions = new HashSet<ResourcePermission>(0);
    private Set<Rig> rigs = new HashSet<Rig>(0);
    private Set<RigTypeMedia> media = new HashSet<RigTypeMedia>(0); 
    private Set<Bookings> bookings = new HashSet<Bookings>(0);
    
    public RigType()
    {
        /* Bean style constructor. */
    }

    public RigType(final String name, final int logoffGraceDuration,
            final boolean codeAssignable)
    {
        this.name = name;
        this.logoffGraceDuration = logoffGraceDuration;
        this.codeAssignable = codeAssignable;
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

    @Column(name = "name", nullable = false, length = 50)
    public String getName()
    {
        return this.name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    @Column(name = "managed", nullable = false)
    public boolean isManaged()
    {
        return this.managed;
    }
    
    public void setManaged(final boolean managed)
    {
        this.managed = managed;
    }

    @Column(name = "meta", nullable = true, length = 255)
    public String getMeta()
    {
        return this.meta;
    }

    public void setMeta(final String meta)
    {
        this.meta = meta;
    }

    @Column(name = "logoff_grace_duration", nullable = false)
    public int getLogoffGraceDuration()
    {
        return this.logoffGraceDuration;
    }

    public void setLogoffGraceDuration(final int logoffGraceDuration)
    {
        this.logoffGraceDuration = logoffGraceDuration;
    }

    @Column(name = "code_assignable", nullable = false)
    public boolean isCodeAssignable()
    {
        return this.codeAssignable;
    }

    public void setCodeAssignable(final boolean codeAssignable)
    {
        this.codeAssignable = codeAssignable;
    }
    
    @Column(name = "set_up_time", columnDefinition="int default '0' not null")
    public int getSetUpTime()
    {
        return this.setUpTime;
    }
    
    public void setSetUpTime(final int setUpTime)
    {
        this.setUpTime = setUpTime;
    }
    
    @Column(name = "tear_down_time", columnDefinition="int default '0' not null")
    public int getTearDownTime()
    {
        return this.tearDownTime;
    }
    
    public void setTearDownTime(final int tearDownTime)
    {
        this.tearDownTime = tearDownTime;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", nullable = true)
    public RemoteSite getSite()
    {
        return this.site;
    }

    public void setSite(RemoteSite site)
    {
        this.site = site;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "rigType")
    public Set<ResourcePermission> getResourcePermissions()
    {
        return this.resourcePermissions;
    }

    public void setResourcePermissions(
            final Set<ResourcePermission> resourcePermissions)
    {
        this.resourcePermissions = resourcePermissions;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "rigType")
    @OrderBy("name ASC")
    public Set<Rig> getRigs()
    {
        return this.rigs;
    }

    public void setRigs(final Set<Rig> rigs)
    {
        this.rigs = rigs;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "rigType")
    public Set<RigTypeMedia> getMedia()
    {
        return this.media;
    }

    public void setMedia(Set<RigTypeMedia> media)
    {
        this.media = media;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "rigType")
    public Set<Bookings> getBookings()
    {
        return this.bookings;
    }

    public void setBookings(Set<Bookings> bookings)
    {
        this.bookings = bookings;
    }
    
    @Override
    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (!(o instanceof RigType)) return false;
        
        if (this.name == null)
        {
            return false;
        }
        else
        {
            RigType t = (RigType)o;
            return this.name.equals(t);
        }
    }
    
    @Override
    public int hashCode()
    {
        if (this.name == null)
        {
            return super.hashCode();
        }
        else
        {
            return this.name.hashCode();
        }
    }
}
