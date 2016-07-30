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
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * ResourcePermission entity class which maps to the resource_permission table.
 * <p />
 * The resource permission class specifies the resources a user class may 
 * access including the constraints that are placed on the sessions
 * that were granting with this resource permission.
 */
@Entity
@Table(name = "resource_permission")
public class ResourcePermission implements java.io.Serializable
{
    /** Rig type resource permission type. */
    public static final String TYPE_PERMISSION = "TYPE";
    
    /** Rig resource permission type. */
    public static final String RIG_PERMISSION = "RIG";
    
    /** Capability resource permission type. */
    public static final String CAPS_PERMISSION = "CAPABILITY";
    
    /** Serializable class. */
    private static final long serialVersionUID = -2292524825260205119L;
    
    /** Primary key. */
    private Long id;

    /** User class this resource permission refers to. */
    private UserClass userClass;
    
    /** Whether the resource permission is remote. */
    private boolean remote;
    
    /** Rig this resource permission refers to if it is a permission granting 
     *  access to a specific rig. */
    private Rig rig;
    
    /** Rig type this resource permission refers to if it is a permission
     *  granting access to a rig type. */
    private RigType rigType;
    
    /** Request capabilities this resource permission refers to if it is
     *  a permission granting access to a specific request capabilities. */
    private RequestCapabilities requestCapabilities;
    
    /** The resource permission resource type, either RIG, TYPE or CAP 
     *  for a rig permission, rig type permission or rig capabilities
     *  permission respectively. */
    private String type;
    
    /** The maximum guaranteed session duration in seconds. */
    private int sessionDuration;
    
    /** The extension duration if a session is extended. */
    private int extensionDuration;
    
    /** The number of times a session is allowed to be extended. */
    private short allowedExtensions;
    
    /** Timeout to terminate queue entrances if no presence is detected within
     *  this period */
    private int queueActivityTimeout;
    
    /** Timeout to terminate sessions if no presence is detected within this
     *  period. */
    private int sessionActivityTimeout;
    
    /** The time at which the resource permission comes in force. */
    private Date startTime;
    
    /** The time at which the resource permission is no longer valid. */
    private Date expiryTime;
    
    /** The maximum number of concurrent bookings that can be made. */
    private int maximumBookings;
    
    /** The name of the permission. */
    private String displayName;
    
    /** Whether to use activity detection for sessions. */
    private boolean useActivityDetection;
    
    private Set<UserLock> userLocks = new HashSet<UserLock>(0);
    private Set<Session> sessionsForResourcePermission = new HashSet<Session>(0);

    public ResourcePermission()
    {
        /* Bean style constructor. */
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_class_id", nullable = true)
    public UserClass getUserClass()
    {
        return this.userClass;
    }

    public void setUserClass(final UserClass userClass)
    {
        this.userClass = userClass;
    }
    
    @Column(name = "is_remote", nullable = false)
    public boolean isRemote()
    {
        return this.remote;
    }

    public void setRemote(boolean remote)
    {
        this.remote = remote;
    }
    
    @Column(name = "type", nullable = false, length = 10)
    public String getType()
    {
        return this.type;
    }

    public void setType(final String type)
    {
        this.type = type;
    }
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    public RigType getRigType()
    {
        return this.rigType;
    }

    public void setRigType(final RigType rigType)
    {
        this.rigType = rigType;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "name_id")
    public Rig getRig()
    {
        return this.rig;
    }

    public void setRig(final Rig rig)
    {
        this.rig = rig;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_caps_id")
    public RequestCapabilities getRequestCapabilities()
    {
        return this.requestCapabilities;
    }

    public void setRequestCapabilities(
            final RequestCapabilities requestCapabilities)
    {
        this.requestCapabilities = requestCapabilities;
    }

    @Column(name = "session_duration", nullable = false)
    public int getSessionDuration()
    {
        return this.sessionDuration;
    }

    public void setSessionDuration(final int sessionDuration)
    {
        this.sessionDuration = sessionDuration;
    }

    @Column(name = "extension_duration", nullable = false)
    public int getExtensionDuration()
    {
        return this.extensionDuration;
    }

    public void setExtensionDuration(final int extensionDuration)
    {
        this.extensionDuration = extensionDuration;
    }

    @Column(name = "allowed_extensions", nullable = false)
    public short getAllowedExtensions()
    {
        return this.allowedExtensions;
    }

    public void setAllowedExtensions(final short allowedExtensions)
    {
        this.allowedExtensions = allowedExtensions;
    }

    @Column(name = "queue_activity_timeout", nullable = false)
    public int getQueueActivityTimeout()
    {
        return this.queueActivityTimeout;
    }

    public void setQueueActivityTimeout(final int queueActivityTimeout)
    {
        this.queueActivityTimeout = queueActivityTimeout;
    }

    @Column(name = "session_activity_timeout", nullable = false)
    public int getSessionActivityTimeout()
    {
        return this.sessionActivityTimeout;
    }

    public void setSessionActivityTimeout(final int sessionActivityTimeout)
    {
        this.sessionActivityTimeout = sessionActivityTimeout;
    }
    
    @Column(name = "start_time", nullable = false)
    public Date getStartTime()
    {
        return this.startTime;
    }
    
    public void setStartTime(final Date startTime)
    {
        this.startTime = startTime;
    }
    
    @Column(name = "expiry_time", nullable = false)
    public Date getExpiryTime()
    {
        return this.expiryTime;
    }
    
    public void setExpiryTime(final Date expiryTime)
    {
        this.expiryTime = expiryTime;
    }
    
    @Column(name = "maximum_bookings", columnDefinition = "int default '0' not null")
    public int getMaximumBookings()
    {
        return this.maximumBookings;
    }

    public void setMaximumBookings(final int maximumBookings)
    {
        this.maximumBookings = maximumBookings;
    }
    
    @Column(name = "display_name", nullable = true, length = 255)
    public String getDisplayName()
    {
        return this.displayName;
    }
    
    public void setDisplayName(final String displayName)
    {
        this.displayName = displayName;
    }
    
    @Column(name="use_activity_detection", nullable = false)
    public boolean isActivityDetected()
    {
        return this.useActivityDetection;
    }
    
    public void setActivityDetected(final boolean detection)
    {
        this.useActivityDetection = detection;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "resourcePermission")
    public Set<UserLock> getUserLocks()
    {
        return this.userLocks;
    }

    public void setUserLocks(final Set<UserLock> userLocks)
    {
        this.userLocks = userLocks;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "resourcePermission")
    public Set<Session> getSessionsForResourcePermission()
    {
        return this.sessionsForResourcePermission;
    }

    public void setSessionsForResourcePermission(final Set<Session> sessionsForResourcePermission)
    {
        this.sessionsForResourcePermission = sessionsForResourcePermission;
    }

}
