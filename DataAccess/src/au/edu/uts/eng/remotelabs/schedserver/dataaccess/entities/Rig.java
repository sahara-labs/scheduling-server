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

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType.Context;

/**
 * Rig entity which maps to the rig table.
 * <p />
 * The rig class contains list of laboratory rigs.
 */
@Entity
@Table(name = "rig", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
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
    
    /** The reason is offline. This is null if the rig is currently online. */
    private String offlineReason;
    
    /** Whether the rig is in session. */
    private boolean inSession;
    
    /** Whether the rig is active. An active rig is said to registered and
     * actively providing it's status. */
    private boolean active;
    
    /** Whether the rig is managed, that is whether the rig has its status
     *  periodically updated, so should have its status put to off-line 
     *  because of a lack of such message. */
    private boolean managed;
    
    /** Meta-information to find the providing rig service. */
    private String meta;
    
    /** The context this rig belongs to. */
    private Context context;
    
    /** The certificate that authenticates node. */
    private String cert;
    
    private Set<ResourcePermission> resourcePermissions = new HashSet<ResourcePermission>(0);
    private Set<Session> sessions = new HashSet<Session>(0);
    private Set<RigLog> rigLogs = new HashSet<RigLog>(0);
    private Set<Bookings> bookings = new HashSet<Bookings>(0);

    public Rig()
    {
        /* Bean style constructor. */
    }

    public Rig(final RigType rigType, final RigCapabilities rigCapabilities,
            final String name, final String contactUrl,
            final Date lastUpdateTimestamp, final boolean online, 
            final String offlineReason, final boolean inSession, 
            final boolean managed, final boolean active)
    {
        this.rigType = rigType;
        this.rigCapabilities = rigCapabilities;
        this.name = name;
        this.contactUrl = contactUrl;
        this.lastUpdateTimestamp = lastUpdateTimestamp;
        this.online = online;
        this.offlineReason = offlineReason;
        this.inSession = inSession;
        this.active = active;
        this.managed = managed;
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

    @ManyToOne(fetch = FetchType.EAGER)
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
    @JoinColumn(name = "caps_id", nullable = true)
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

    @Column(name = "contact_url", nullable = true, length = 1024)
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
    
    @Column(name = "offline_reason", nullable = true)
    public String getOfflineReason()
    {
        return this.offlineReason;
    }
    
    public void setOfflineReason(final String offlineReason)
    {
        this.offlineReason = offlineReason;
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
    
    @Column(name="managed", nullable = false)
    public boolean isManaged()
    {
        return this.managed;
    }
    
    public void setManaged(final boolean managed)
    {
        this.managed = managed;
    }
    
    @Column(name="meta", length=255)
    public String getMeta()
    {
        return this.meta;
    }
    
    public void setMeta(final String meta)
    {
        this.meta = meta;
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
    
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "rig")
    public Set<RigLog> getRigLogs()
    {
        return this.rigLogs;
    }
    
    public void  setRigLogs(final Set<RigLog> rigLogs)
    {
        this.rigLogs = rigLogs;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "rig")
    public Set<Bookings> getBookings()
    {
        return this.bookings;
    }

    public void setBookings(Set<Bookings> bookings)
    {
        this.bookings = bookings;
    }
    
    @Enumerated(EnumType.STRING)
    @Column(name = "context", nullable = true)
    public Context getContext()
    {
        return context;
    }

    public void setContext(Context context)
    {
        this.context = context;
    }
    
    @Column(name = "cert", columnDefinition = "text")
    public String getCert()
    {
        return cert;
    }

    public void setCert(String cert)
    {
        this.cert = cert;
    }

    @Override
    public boolean equals(Object o)
    {
        if (o == this) return true;
        if (!(o instanceof Rig)) return false;
        
        Rig r = (Rig)o;
        if (this.name != null)
        {
            return this.name.equals(r.getName());       
        }
        
        return false;
    }
    
    @Override
    public int hashCode()
    {   
        if (this.name != null)
        {
            int result = 23;
            result = result * 17 + this.name.hashCode();
            return result;
        }
        else
        {
            return super.hashCode();
        }
    }
}
