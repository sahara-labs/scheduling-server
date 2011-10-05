/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2011, Michael Diponio
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
 * @date 30th January 2011
 */
package au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities;

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
 * Remote permission details.
 */
@Entity
@Table(name = "remote_permission")
public class RemotePermission
{
    /** Record primary key. */
    private Long id;
    
    /** Mapping key that is shared between provider and consumer. */
    private String guid;
    
    /** The permission that relates to this mapping. */
    private ResourcePermission permission;
    
    /** The remote site that is the provider of the permission. */
    private RemoteSite site;
    
    /** Whether this permission has been accepted by the provider. */
    private boolean inRequest;
    
    /** Whether this permission is enabled. */
    private boolean active;
    
    /** Whether this permission is has been rejected by the remote site. */
    private boolean rejected;
    
    /** When the permission was requested. */
    private Date requestTime;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId()
    {
        return this.id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    @Column(name = "guid", nullable = false, unique = true, length = 255)
    public String getGuid()
    {
        return this.guid;
    }

    public void setGuid(String guid)
    {
        this.guid = guid;
    }


    @OneToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "resource_permission_id", nullable = false)
    public ResourcePermission getPermission()
    {
        return this.permission;
    }

    public void setPermission(ResourcePermission permission)
    {
        this.permission = permission;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "remote_site_id", nullable = false)
    public RemoteSite getSite()
    {
        return this.site;
    }

    public void setSite(RemoteSite site)
    {
        this.site = site;
    }
    
    
    @Column(name = "in_request", nullable = false)
    public boolean isInRequest()
    {
        return this.inRequest;
    }

    public void setInRequest(boolean inRequest)
    {
        this.inRequest = inRequest;
    }
    
    @Column(name = "active", nullable = false)
    public boolean isActive()
    {
        return this.active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    @Column(name = "rejected", nullable = false)
    public boolean isRejected()
    {
        return this.rejected;
    }

    public void setRejected(boolean rejected)
    {
        this.rejected = rejected;
    }

    @Column(name = "request_time", nullable = false)
    public Date getRequestTime()
    {
        return this.requestTime;
    }

    public void setRequestTime(Date requestTime)
    {
        this.requestTime = requestTime;
    }
}
