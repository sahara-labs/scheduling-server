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
 * @date 31st May 2010
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
import javax.persistence.Table;

/**
 * Logs the state changes off a rig. The states a rig may be in are:
 * <ul>
 *  <li>ONLINE - The rig is registered and online.</li>
 *  <li>OFFLINE - The rig is registered and offline.</li>
 *  <li>NOT_REGISTERED - Rig was previously registered but is no
 *  longer active.</li>
 * </ul>
 * <strong>NOTE:</strong> The free or in-use states are not logged
 * here, but are logged in the {@link Session} table.
 */
@Entity
@Table(name = "rig_log")
public class RigLog implements Serializable
{
    private static final long serialVersionUID = -3129829396037723954L;
    
    /** Registered online state (active, online). */
    public static final String ONLINE = "ONLINE";
    
    /** Registered offline state (active, offline). */
    public static final String OFFLINE = "OFFLINE";
    
    /** Not registered (inactive). */
    public static final String NOT_REGISTERED = "NOT_REGISTERED";

    /** Record primary key. */
    private Long id;
    
    /** Rig this log relates too. */
    private Rig rig;
    
    /** The rig state before this log event. */
    private String oldState;
    
    /** The rig state after this log event. */
    private String newState;
    
    /** Reason this event occured. */
    private String reason;
    
    /** Time stamp of this event. */
    private Date timeStamp;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rig_id", nullable = false)
    public Rig getRig()
    {
        return this.rig;
    }

    public void setRig(Rig rig)
    {
        this.rig = rig;
    }

    @Column(name = "old_state", nullable = false, length = 20)
    public String getOldState()
    {
        return this.oldState;
    }

    public void setOldState(String oldState)
    {
        this.oldState = oldState;
    }

    @Column(name = "new_state", nullable = false, length = 20)
    public String getNewState()
    {
        return this.newState;
    }

    public void setNewState(String newState)
    {
        this.newState = newState;
    }

    @Column(name = "reason", nullable = false, length = 255)
    public String getReason()
    {
        return this.reason;
    }

    public void setReason(String reason)
    {
        this.reason = reason;
    }

    @Column(name = "timestamp", nullable = false)
    public Date getTimeStamp()
    {
        return this.timeStamp;
    }

    public void setTimeStamp(Date timeStamp)
    {
        this.timeStamp = timeStamp;
    }
}
