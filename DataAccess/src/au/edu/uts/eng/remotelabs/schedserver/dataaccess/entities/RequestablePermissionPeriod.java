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
 * @date 19th September 2011
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
 * Entity which stores the times resources may be requested by consumers.
 */
@Entity
@Table(name = "requestable_permission_period")
public class RequestablePermissionPeriod implements Serializable
{
    /** Serializable class. */
    private static final long serialVersionUID = 8440890444119276512L;

    /** Record primary key. */
    private Long id;
    
    /** Whether the period is active. */
    private boolean active;
    
    /** The type of resource. */
    private String type;
    
    /** The rig type this refers to if it is a rig type resource. */
    private RigType rigType;
    
    /** The rig this refers to if it is a rig resource. */
    private Rig rig;
    
    /** The request capabilities this refers to if it is a request 
     * capabilities resource. */
    private RequestCapabilities requestCapabilities;
    
    /** The requestable period start. */
    private Date start;
    
    /** The requestable period end. */
    private Date end;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public Long getId()
    {
        return this.id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }
    
    @Column(name = "active")
    public boolean isActive()
    {
        return this.active;
    }
    
    public void setActive(boolean active)
    {
        this.active = active;
    }

    @Column(name = "type")
    public String getType()
    {
        return this.type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    @ManyToOne
    @JoinColumn(name = "rig_type_id", nullable = true)
    public RigType getRigType()
    {
        return this.rigType;
    }

    public void setRigType(RigType rigType)
    {
        this.rigType = rigType;
    }

    @ManyToOne
    @JoinColumn(name = "rig_id", nullable = true)
    public Rig getRig()
    {
        return this.rig;
    }

    public void setRig(Rig rig)
    {
        this.rig = rig;
    }

    @ManyToOne
    @JoinColumn(name = "request_capabilities_id", nullable = true)
    public RequestCapabilities getRequestCapabilities()
    {
        return this.requestCapabilities;
    }

    public void setRequestCapabilities(RequestCapabilities caps)
    {
        this.requestCapabilities = caps;
    }

    @Column(name = "start")
    public Date getStart()
    {
        return this.start;
    }

    public void setStart(Date start)
    {
        this.start = start;
    }
    
    @Column(name = "end")
    public Date getEnd()
    {
        return this.end;
    }

    public void setEnd(Date end)
    {
        this.end = end;
    }
}
