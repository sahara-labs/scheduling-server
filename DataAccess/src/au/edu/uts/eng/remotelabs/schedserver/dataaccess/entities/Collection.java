/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2012, University of Technology, Sydney
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
 * @date 29th October 2012
 */
package au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Collection entity.
 */
@Entity
@Table(name = "collection")
public class Collection implements Serializable
{
    /** Serializable class. */
    private static final long serialVersionUID = -3351356067476803316L;

    /** Record primary key. */
    private Long id;
    
    /** The project this collection relates to. */
    private Project project;
    
    /** The time at which this collection was published. */
    private Date publishTime;
    
    /** Whether this collection is automatically published or user managed. */
    private boolean userManaged;
    
    /** Session which are part of this collection. */
    private Set<Session> sessions = new HashSet<Session>(0);
    
    public Collection()
    {
        /* Bean style constructor. */
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    public Long getId()
    {
        return this.id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id", nullable = false)
    public Project getProject()
    {
        return this.project;
    }

    public void setProject(Project project)
    {
        this.project = project;
    }

    @Column(name = "publish_name", nullable = true)
    public Date getPublishTime()
    {
        return this.publishTime;
    }

    public void setPublishTime(Date publishTime)
    {
        this.publishTime = publishTime;
    }

    @Column(name = "user_managed", nullable = false)
    public boolean isUserManaged()
    {
        return this.userManaged;
    }

    public void setUserManaged(boolean userManaged)
    {
        this.userManaged = userManaged;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "collection_sessions",
        joinColumns = @JoinColumn(name = "collection_id", referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "session_id", referencedColumnName = "id"))
    public Set<Session> getSessions()
    {
        return this.sessions;
    }

    public void setSessions(Set<Session> sessions)
    {
        this.sessions = sessions;
    }
}
