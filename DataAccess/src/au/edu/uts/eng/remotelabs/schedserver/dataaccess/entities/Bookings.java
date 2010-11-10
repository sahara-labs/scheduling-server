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
 *    this software without specific prior written resourcePermission.
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
 * @date 13th September 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Bookings entity which maps to the bookings table.  
 */
@Entity
@Table(name = "bookings")
public class Bookings implements Serializable
{
    /** Serializable class. */
    private static final long serialVersionUID = -3936408090560586846L;
    
    /** Record primary key. */
    private Long id;
    
    /** Whether the booking is still valid. It is not valid if it has been 
     *  completed or canceled. */
    private boolean active;
    
    /** The resoruce resourcePermission this booking was created from. */
    private ResourcePermission resourcePermission;
    
    /** The start time of the booking. */
    private Date startTime;
    
    /** The end time of the booking. */
    private Date endTime;
    
    /** The booking duration. */
    private int duration;
    
    /** The user this booking relates to. */
    private User user;
    
    /** The name of the user. */
    private String userName;
    
    /** The namespace of the user. */
    private String userNamespace;
    
    /** The type of resource that has been booked, either 'RIG', 'TYPE' or
     *  'CAPABILITY'. */
    private String resourceType;
    
    /** The booked rig, if it is a rig booking. */
    private Rig rig;
    
    /** The booked rig type, if it is a rig type booking. */
    private RigType rigType;
    
    /** The booking request capabilities, if it is a rig capability booking. */
    private RequestCapabilities requestCapabilities;
    
    /** The session that was started from this booking. */
    private Session session;
    
    /** The reason this booking was canceled. */
    private String cancelReason;
    
    /** A file system reference to batch code to run on assignment. */
    private String codeReference;

    /**
     * @return the id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId()
    {
        return this.id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id)
    {
        this.id = id;
    }

    /**
     * @return the active
     */
    @Column(name = "active", nullable = false)
    public boolean isActive()
    {
        return this.active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(boolean active)
    {
        this.active = active;
    }

    /**
     * @return the resourcePermission
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "resource_permission_id", nullable = false)
    public ResourcePermission getResourcePermission()
    {
        return this.resourcePermission;
    }

    /**
     * @param resourcePermission the resourcePermission to set
     */
    public void setResourcePermission(ResourcePermission permission)
    {
        this.resourcePermission = permission;
    }

    /**
     * @return the startTime
     */
    @Column(name = "start_time", nullable = false)
    public Date getStartTime()
    {
        return this.startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    /**
     * @return the end time
     */
    @Column(name = "end_time", nullable = false)
    public Date getEndTime()
    {
        return this.endTime;
    }

    /**
     * @param the end time
     */
    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    /**
     * @return the duration
     */
    @Column(name = "duration", nullable = false)
    public int getDuration()
    {
        return this.duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(int duration)
    {
        this.duration = duration;
    }

    /**
     * @return the user
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser()
    {
        return this.user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user)
    {
        this.user = user;
    }

    /**
     * @return the userName
     */
    @Column(name = "user_name", nullable = false, length = 50)
    public String getUserName()
    {
        return this.userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    /** 
     * @return the userNamespace
     */
    @Column(name = "user_namespace", nullable = false, length = 50)
    public String getUserNamespace()
    {
        return this.userNamespace;
    }

    /**
     * @param userNamespace the userNamespace to set
     */
    public void setUserNamespace(String userNamespace)
    {
        this.userNamespace = userNamespace;
    }

    /**
     * @return the resourceType
     */
    @Column(name = "resource_type", nullable = false, length = 10) 
    public String getResourceType()
    {
        return this.resourceType;
    }

    /**
     * @param resourceType the resourceType to set
     */
    public void setResourceType(String resourceType)
    {
        this.resourceType = resourceType;
    }
    
    /**
     * @return the rig
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rig_id", nullable = true)
    public Rig getRig()
    {
        return this.rig;
    }

    /**
     * @param rig the rig to set
     */
    public void setRig(Rig rig)
    {
        this.rig = rig;
    }

    /**
     * @return the rigType
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rig_type_id", nullable = true)
    public RigType getRigType()
    {
        return this.rigType;
    }

    /**
     * @param rigType the rigType to set
     */
    public void setRigType(RigType rigType)
    {
        this.rigType = rigType;
    }

    /**
     * @return the requestCapabilities
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "request_caps_id", nullable = true)
    public RequestCapabilities getRequestCapabilities()
    {
        return this.requestCapabilities;
    }

    /**
     * @param requestCapabilities the requestCapabilities to set
     */
    public void setRequestCapabilities(RequestCapabilities requestCapability)
    {
        this.requestCapabilities = requestCapability;
    }

    /**
     * @return the session
     */
    @OneToOne(optional = true)
    @JoinColumn(name = "session_id", nullable = true)
    public Session getSession()
    {
        return this.session;
    }

    /**
     * @param session the session to set
     */
    public void setSession(Session session)
    {
        this.session = session;
    }

    /**
     * @return the cancelReason
     */
    @Column(name = "cancel_reason", nullable = true, length = 1024)
    public String getCancelReason()
    {
        return this.cancelReason;
    }

    /**
     * @param cancelReason the cancelReason to set
     */
    public void setCancelReason(String cancelReason)
    {
        this.cancelReason = cancelReason;
    }

    /**
     * @return the codeReference
     */
    @Column(name = "code_reference", nullable = true, length = 1024)
    public String getCodeReference()
    {
        return this.codeReference;
    }

    /**
     * @param codeReference the codeReference to set
     */
    public void setCodeReference(String codeReference)
    {
        this.codeReference = codeReference;
    }

}
