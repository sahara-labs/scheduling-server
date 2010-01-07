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

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

/**
 * Rig entity which maps to the rig table.
 * <p />
 * The rig class contains list of laboratory rigs.
 */
@Entity
@Table(name = "rig", uniqueConstraints = {@UniqueConstraint(columnNames = "name") })
public class Rig implements java.io.Serializable
{
    /** Serializable class. */
    private static final long serialVersionUID = -2028175400491809280L;
    
    /** Record primary key. */
    private Long id;
    
    /** The rigs type. */
    private RigType rigType;
    
    /** A session using the rig. */
    private Session session;
    
    /** The capabilities the rig provides. */
    private RigCapabilities rigCapabilities;
    
    /** The unique name of rig. */
    private String name;
    
    /** The URL of the rig's rig client interface. */
    private String contactUrl;
    
    /** The timestamp of the last rig client status update. */
    private Date lastUpdateTimestamp;
    
    /** Whether the rig is online (true if online). */
    private boolean online;
    
    /** Whether the rig is in session. */
    private boolean inSession;
    
    /** Whether the rig is active. An active rig is said to registered and
     * actively providing it's status. */
    private boolean active;
    
    private Set<ResourcePermission> resourcePermissions = new HashSet<ResourcePermission>(0);
    private Set<Session> sessions = new HashSet<Session>(0);

    public Rig()
    {
        /* Bean style constructor. */
    }

    public Rig(final RigType rigType, final RigCapabilities rigCapabilities,
            final String name, final String contactUrl,
            final Date lastUpdateTimestamp, final boolean online,
            final boolean inSession, final boolean active)
    {
        this.rigType = rigType;
        this.rigCapabilities = rigCapabilities;
        this.name = name;
        this.contactUrl = contactUrl;
        this.lastUpdateTimestamp = lastUpdateTimestamp;
        this.online = online;
        this.inSession = inSession;
        this.active = active;
    }

    public Rig(final RigType rigType, final Session session,
            final RigCapabilities rigCapabilities, final String name,
            final String contactUrl, final Date lastUpdateTimestamp,
            final boolean online, final boolean inSession,
            final boolean active,
            final Set<ResourcePermission> resourcePermissions,
            final Set<Session> sessions)
    {
        this.rigType = rigType;
        this.session = session;
        this.rigCapabilities = rigCapabilities;
        this.name = name;
        this.contactUrl = contactUrl;
        this.lastUpdateTimestamp = lastUpdateTimestamp;
        this.online = online;
        this.inSession = inSession;
        this.active = active;
        this.resourcePermissions = resourcePermissions;
        this.sessions = sessions;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId()
    {
        return this.id;
    }

    public void setId(final Long id)
    {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", nullable = false)
    public RigType getRigType()
    {
        return this.rigType;
    }

    public void setRigType(final RigType rigType)
    {
        this.rigType = rigType;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    public Session getSession()
    {
        return this.session;
    }

    public void setSession(final Session session)
    {
        this.session = session;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "caps_id", nullable = false)
    public RigCapabilities getRigCapabilities()
    {
        return this.rigCapabilities;
    }

    public void setRigCapabilities(final RigCapabilities rigCapabilities)
    {
        this.rigCapabilities = rigCapabilities;
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

    @Column(name = "contact_url", nullable = false, length = 65535)
    public String getContactUrl()
    {
        return this.contactUrl;
    }

    public void setContactUrl(final String contactUrl)
    {
        this.contactUrl = contactUrl;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_update_timestamp", nullable = false, length = 19)
    public Date getLastUpdateTimestamp()
    {
        return this.lastUpdateTimestamp;
    }

    public void setLastUpdateTimestamp(final Date lastUpdateTimestamp)
    {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
    }

    @Column(name = "online", nullable = false)
    public boolean isOnline()
    {
        return this.online;
    }

    public void setOnline(final boolean online)
    {
        this.online = online;
    }

    @Column(name = "in_session", nullable = false)
    public boolean isInSession()
    {
        return this.inSession;
    }

    public void setInSession(final boolean inSession)
    {
        this.inSession = inSession;
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "rig")
    public Set<ResourcePermission> getResourcePermissions()
    {
        return this.resourcePermissions;
    }

    public void setResourcePermissions(final Set<ResourcePermission> resourcePermissions)
    {
        this.resourcePermissions = resourcePermissions;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "rig")
    public Set<Session> getSessions()
    {
        return this.sessions;
    }

    public void setSessions(final Set<Session> sessions)
    {
        this.sessions = sessions;
    }
}
