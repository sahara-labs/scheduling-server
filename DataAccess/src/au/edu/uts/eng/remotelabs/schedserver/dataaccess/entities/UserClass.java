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
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * UserClass entity class which maps to the user_class table.
 * <p />
 * A user class is a role that defines the resource permissions and constraints
 * a group of users may have. It is analogous to a subject. The following 
 * attributes may be set for the user class:
 * <ul>
 *  <li>Resource permissions - the resources a user may be access.</li>
 *  <li>Priority - Queue priority.<li>
 *  <li>Queueable - Whether a user may queue for a resource.</li>
 *  <li>Bookable - Whether a user may book for a resource.</li>
 *  <li>Kickable - Whether a user may be forcibly removed for a resource before
 *  their guaranteed time has elapsed.</li>
 *  <li>Lockable - Whether a user may be locked from using a resource.</li>
 *  <li>Time Horizon - The offset from which a booking can be made. That is, 
 *  from now until this time is elapsed, no bookings can be made.</li>
 * </ul>
 */
@Entity
@Table(name = "user_class", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class UserClass implements java.io.Serializable
{
    /** Serializable class. */
    private static final long serialVersionUID = -6085739202856516308L;
    
    /** Record primary key. */
    private Long id;
    
    /** Name of the user class. */
    private String name;
    
    /** Priority a user gets when requesting a resource this class provides 
     *  permission for. */
    private short priority;
    
    /** Whether a user may queue for resources. If not, they must be assigned
     *  to a free resource or the request fails. */
    private boolean queuable;
    
    /** Whether a user may book for resources. If not, they must queue for the 
     * resources. */
    private boolean bookable;
    
    /** Whether a user may be kicked from a resource. */
    private boolean kickable;
    
    /** Whether the users of this class may be locked from assessing resources
     * which have permission granted by this class. */
    private boolean usersLockable;
    
    /** Whether the user class is active. */
    private boolean active;
    
    /** The time offset from a booking can be made. */
    private int timeHorizon;
    
    /** The user associations to the users in this class. */
    private Set<UserAssociation> userAssociations = new HashSet<UserAssociation>(0);
    
    /** The academic permissions relating to this class. */
    private Set<AcademicPermission> academicPermissions = new HashSet<AcademicPermission>(0);
    
    /** The resource permissions this class has. */
    private Set<ResourcePermission> resourcePermissions = new HashSet<ResourcePermission>(0);
    
    /** The user class keys to add users to this class. */
    private Set<UserClassKey> keys = new HashSet<UserClassKey>(0);

    public UserClass()
    {
        /* Bean style class. */
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

    @Column(name = "name", unique = true, nullable = false, length = 50)
    public String getName()
    {
        return this.name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    @Column(name = "priority", nullable = false)
    public short getPriority()
    {
        return this.priority;
    }

    public void setPriority(final short priority)
    {
        this.priority = priority;
    }

    @Column(name = "queuable", nullable = false)
    public boolean isQueuable()
    {
        return this.queuable;
    }

    public void setQueuable(final boolean queuable)
    {
        this.queuable = queuable;
    }
    
    @Column(name = "bookable", nullable = false)
    public boolean isBookable()
    {
        return this.bookable;
    }
    
    public void setBookable(final boolean bookable)
    {
        this.bookable = bookable;
    }

    @Column(name = "kickable", nullable = false)
    public boolean isKickable()
    {
        return this.kickable;
    }

    public void setKickable(final boolean kickable)
    {
        this.kickable = kickable;
    }

    @Column(name = "users_lockable", nullable = false)
    public boolean isUsersLockable()
    {
        return this.usersLockable;
    }

    public void setUsersLockable(final boolean usersLockable)
    {
        this.usersLockable = usersLockable;
    }

    @Column(name = "active", nullable = false)
    public boolean isActive()
    {
        return this.active;
    }

    public void setActive(final boolean active)
    {
        this.active = active;
    }
    
    @Column(name = "time_horizon", columnDefinition="int default '0' not null")
    public int getTimeHorizon()
    {
        return this.timeHorizon;
    }
    
    public void setTimeHorizon(int horizon)
    {
        this.timeHorizon = horizon;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userClass")
    public Set<UserAssociation> getUserAssociations()
    {
        return this.userAssociations;
    }

    public void setUserAssociations(final Set<UserAssociation> userAssociations)
    {
        this.userAssociations = userAssociations;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userClass")
    public Set<AcademicPermission> getAcademicPermissions()
    {
        return this.academicPermissions;
    }

    public void setAcademicPermissions(final Set<AcademicPermission> academicPermissions)
    {
        this.academicPermissions = academicPermissions;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userClass")
    public Set<ResourcePermission> getResourcePermissions()
    {
        return this.resourcePermissions;
    }

    public void setResourcePermissions(final Set<ResourcePermission> resourcePermissions)
    {
        this.resourcePermissions = resourcePermissions;
    }

    @OneToMany(mappedBy = "userClass", fetch = FetchType.LAZY)
    public Set<UserClassKey> getKeys()
    {
        return this.keys;
    }

    public void setKeys(Set<UserClassKey> keys)
    {
        this.keys = keys;
    }
}
