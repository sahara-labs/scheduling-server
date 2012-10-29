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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Session entity class which maps to the session table.
 */
@Entity
@Table(name = "session")
public class Session implements java.io.Serializable
{
    /** Serializable class. */
    private static final long serialVersionUID = -4636136114987299334L;
    
    /** Record primary key. */
    private Long id;
    
    /** User who is in session. */
    private User user;
    
    /** The resource permission which granting access to the resource in use. */
    private ResourcePermission resourcePermission;
    
    /** The rig this session is using. */
    private Rig rig;
    
    /** User name of the user in session. */
    private String userName;
    
    /** Name space of the user in session. */
    private String userNamespace;
    
    /** The time the request was made. */
    private Date requestTime;
    
    /** The resource type in use. */
    private String resourceType;
    
    /** Key to the requested resource which may a rig key, a rig type key or a 
     *  request capabilities key. */
    private Long requestedResourceId;
    
    /** Name of the requested resource which may be a rig name, rig type 
     *  or request capabilities string. */
    private String requestedResourceName;
    
    /** Request priority. */
    private short priority;
    
    /** Time the activity was last detected or notified. */
    private Date activityLastUpdated;
    
    /** The name of the assigned rig. */
    private String assignedRigName;
    
    /** The time the rig was assigned. */
    private Date assignmentTime;
    
    /** Whether the session is active (either in queue or on rig). */
    private boolean active;
    
    /** Whether the session is ready for use or still allocating. */
    private boolean ready;
    
    /** Whether the session has the grace period initiated (forced log-off grace). */
    private boolean inGrace;
    
    /** The time at which the rig session must terminate. */
    private Date removalTime;
    
    /** The reason for rig removal. */
    private String removalReason;
    
    /** The path to the uploaded code. */
    private String codeReference;
    
    /** Session duration. */
    private int duration;
    
    /** The number of session extensions. */
    private short extensions;


    public Session()
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    public User getUser()
    {
        return this.user;
    }

    public void setUser(final User user)
    {
        this.user = user;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "resource_permission_id")
    public ResourcePermission getResourcePermission()
    {
        return this.resourcePermission;
    }

    public void setResourcePermission(final ResourcePermission resourcePermission)
    {
        this.resourcePermission = resourcePermission;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "assigned_rig_id")
    public Rig getRig()
    {
        return this.rig;
    }

    public void setRig(final Rig rig)
    {
        this.rig = rig;
    }

    @Column(name = "user_name", nullable = false, length = 50)
    public String getUserName()
    {
        return this.userName;
    }

    public void setUserName(final String userName)
    {
        this.userName = userName;
    }

    @Column(name = "user_namespace", nullable = false, length = 50)
    public String getUserNamespace()
    {
        return this.userNamespace;
    }

    public void setUserNamespace(final String userNamespace)
    {
        this.userNamespace = userNamespace;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "request_time", nullable = false, length = 19)
    public Date getRequestTime()
    {
        return this.requestTime;
    }

    public void setRequestTime(final Date requestTime)
    {
        this.requestTime = requestTime;
    }

    @Column(name = "resource_type", nullable = false, length = 10)
    public String getResourceType()
    {
        return this.resourceType;
    }

    public void setResourceType(final String resourceType)
    {
        this.resourceType = resourceType;
    }

    @Column(name = "requested_resource_id")
    public Long getRequestedResourceId()
    {
        return this.requestedResourceId;
    }

    public void setRequestedResourceId(final Long requestedResourceId)
    {
        this.requestedResourceId = requestedResourceId;
    }

    @Column(name = "requested_resource_name", nullable = false, length = 1024)
    public String getRequestedResourceName()
    {
        return this.requestedResourceName;
    }

    public void setRequestedResourceName(final String requestedResourceName)
    {
        this.requestedResourceName = requestedResourceName;
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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "activity_last_updated", nullable = false, length = 19)
    public Date getActivityLastUpdated()
    {
        return this.activityLastUpdated;
    }

    public void setActivityLastUpdated(final Date activityLastUpdated)
    {
        this.activityLastUpdated = activityLastUpdated;
    }

    @Column(name = "assigned_rig_name", nullable = true, length = 50)
    public String getAssignedRigName()
    {
        return this.assignedRigName;
    }

    public void setAssignedRigName(final String assignedRigName)
    {
        this.assignedRigName = assignedRigName;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "assignment_time", nullable = true, length = 19)
    public Date getAssignmentTime()
    {
        return this.assignmentTime;
    }

    public void setAssignmentTime(final Date assignmentTime)
    {
        this.assignmentTime = assignmentTime;
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
    
    @Column(name = "ready", nullable = true)
    public boolean isReady()
    {
        return this.ready;
    }
    
    public void setReady(final boolean ready)
    {
        this.ready = ready;
    }
    
    @Column(name = "in_grace", nullable = true)
    public boolean isInGrace()
    {
        return this.inGrace;
    }
    
    public void setInGrace(final boolean inGrace)
    {
        this.inGrace = inGrace;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "removal_time", nullable = true, length = 19)
    public Date getRemovalTime()
    {
        return this.removalTime;
    }

    public void setRemovalTime(final Date removalTime)
    {
        this.removalTime = removalTime;
    }

    @Column(name = "removal_reason", nullable = true, length = 1024)
    public String getRemovalReason()
    {
        return this.removalReason;
    }

    public void setRemovalReason(final String removalReason)
    {
        this.removalReason = removalReason;
    }

    @Column(name = "code_reference", nullable = true, length = 1024)
    public String getCodeReference()
    {
        return this.codeReference;
    }

    public void setCodeReference(final String codeReference)
    {
        this.codeReference = codeReference;
    }

    @Column(name = "duration", nullable = false)
    public int getDuration()
    {
        return this.duration;
    }

    public void setDuration(final int duration)
    {
        this.duration = duration;
    }

    @Column(name = "extensions", nullable = false)
    public short getExtensions()
    {
        return this.extensions;
    }

    public void setExtensions(final short extensions)
    {
        this.extensions = extensions;
    }
}
