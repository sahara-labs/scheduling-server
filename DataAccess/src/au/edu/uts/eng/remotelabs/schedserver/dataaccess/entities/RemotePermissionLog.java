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
 * @date 5th October 2011
 */
package au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Holds logs of log events of remote permission negotiation.
 */
@Entity
@Table(name = "remote_permission_log")
public class RemotePermissionLog implements Serializable
{
    /** Serializable class. */
    private static final long serialVersionUID = -591180906918612661L;
    
    /** Record identifier. */
    private Long id;

    /** The permission that is being negotiated. */
    private RemotePermission remotePermission;
    
    /** The time the log was generated. */
    private Date time;
    
    /** The event that occurred. */
    private String event;
 
    /** The reason for the event (if any). */
    private String reason;
    
    /** The site which the log originated. */
    private RemoteSite site;

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
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "remote_permission_id", nullable = false)
    public RemotePermission getRemotePermission()
    {
        return this.remotePermission;
    }

    public void setRemotePermission(RemotePermission remotePermission)
    {
        this.remotePermission = remotePermission;
    }

    @Column(name = "time", nullable = false)
    public Date getTime()
    {
        return this.time;
    }

    public void setTime(Date time)
    {
        this.time = time;
    }

    @Column(name = "event", nullable = false)
    public String getEvent()
    {
        return this.event;
    }

    public void setEvent(String event)
    {
        this.event = event;
    }

    @Column(name = "reason", nullable = true)
    public String getReason()
    {
        return this.reason;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }

    @JoinColumn(name = "remote_site_id", nullable = true)
    public RemoteSite getSite()
    {
        return this.site;
    }

    public void setSite(RemoteSite site)
    {
        this.site = site;
    }
}
