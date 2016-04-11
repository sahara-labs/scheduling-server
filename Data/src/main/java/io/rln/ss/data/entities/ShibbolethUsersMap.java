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
package io.rln.ss.data.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Entity to store Shibboleth users map. The map is created by local users 
 * logging in through a Shibboleth single signon service.
 */
@Entity
@Table(name = "shib_users_map")
public class ShibbolethUsersMap implements Serializable 
{
    /** Serializable class. */
    private static final long serialVersionUID = -3424573001449621219L;
    
    /** Record primary key. */
    private String sid;
    
    /** User whose mapped with this record. */
    private User user;

    /** Name in the users record. */
    private String userName;
    
    /** The home organisation attribute of the user. */
    private String homeOrganisation;
    
    /** The affliation attribute of the user. */
    private String affliation;

    @Id
    @Column(name = "sid", nullable = false, length = 255)
    public String getSid()
    {
        return this.sid;
    }

    public void setSid(String sid)
    {
        this.sid = sid;
    }
    
    @OneToOne(optional = true)
    @JoinColumn(name = "users_id", nullable = true)
    public User getUser()
    {
        return this.user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    @Column(name = "user_name", nullable = false, length = 50)
    public String getUserName()
    {
        return this.userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    @Column(name = "home_org", nullable = true, length = 255)
    public String getHomeOrganisation()
    {
        return this.homeOrganisation;
    }

    public void setHomeOrganisation(String homeOrganisation)
    {
        this.homeOrganisation = homeOrganisation;
    }

    @Column(name = "affliation", nullable = true, length = 255)
    public String getAffliation()
    {
        return this.affliation;
    }

    public void setAffliation(String affliation)
    {
        this.affliation = affliation;
    }
}
