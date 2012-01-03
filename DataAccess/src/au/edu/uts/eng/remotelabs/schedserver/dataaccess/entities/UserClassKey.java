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
 * @date 2st January 2012
 */
package au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Key that allow redemption of users to user classes,
 */
@Entity
@Table(name = "user_class_key")
public class UserClassKey implements Serializable
{
    private static final long serialVersionUID = 7946691146385159049L;
    
    /** Record primary key. */
    private Long id;
    
    /** Redemption key. */
    private String redeemKey;
    
    /** The user class this key redeems. */
    private UserClass userClass;
    
    private boolean active;
    
    /** The number of remaining uses of the key. */
    private int remaining;
    
    /** The constraints list that must hold for a key to be redeemed. */
    private Set<UserClassKeyConstraint> constraints = new HashSet<UserClassKeyConstraint>(0);
    
    /** The list of users that have redeemed this association. */
    private Set<UserClassKeyRedemption> redemptions = new HashSet<UserClassKeyRedemption>(0);

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

    @Column(name = "redeem_key", unique = true, length = 255)
    public String getRedeemKey()
    {
        return this.redeemKey;
    }

    public void setRedeemKey(String redeemKey)
    {
        this.redeemKey = redeemKey;
    }

    @JoinColumn(name = "user_class_id", nullable = false)
    @ManyToOne(optional = false)
    public UserClass getUserClass()
    {
        return this.userClass;
    }

    public void setUserClass(UserClass userClass)
    {
        this.userClass = userClass;
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

    @Column(name = "remaining")
    public int getRemaining()
    {
        return this.remaining;
    }

    public void setRemaining(int remaining)
    {
        this.remaining = remaining;
    }

    @OneToMany(mappedBy = "classKey", fetch = FetchType.EAGER)
    public Set<UserClassKeyConstraint> getConstraints()
    {
        return this.constraints;
    }

    public void setConstraints(Set<UserClassKeyConstraint> constraints)
    {
        this.constraints = constraints;
    }

    @OneToMany(mappedBy = "classKey", fetch = FetchType.LAZY)
    public Set<UserClassKeyRedemption> getRedemptions()
    {
        return this.redemptions;
    }

    public void setRedemptions(Set<UserClassKeyRedemption> redemptions)
    {
        this.redemptions = redemptions;
    }
}
