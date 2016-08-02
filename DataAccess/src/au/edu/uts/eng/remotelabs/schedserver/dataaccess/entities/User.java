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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * User entity class which maps to the user table.
 * <p />
 * A user is a unique person in the system composed of their name
 * and a name space (e.g institution).
 */
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "namespace" }))
public class User implements java.io.Serializable
{
    /** Administrator user persona. */
    public static final String ADMIN = "ADMIN";
    
    /** Academic user persona. */
    public static final String ACADEMIC = "ACADEMIC";
    
    /** Regular user persona. */
    public static final String USER = "USER";
    
    /** Demonstration user persona. */
    public static final String DEMO = "DEMO";
    
    /** Serilizable class. */
    private static final long serialVersionUID = -4090859888631269654L;
    
    /** Record primary key. */
    private Long id;
    
    /** Name of the user. */
    private String name;
    
    /** Name space of the user. */
    private String namespace;
    
    /** Persona of the user which may be 'ADMIN', 'ACADEMIC', 'USER', 'DEMO'. */
    private String persona;
        
    /** The users first name. */
    private String firstName;
    
    /** The users last name. */
    private String lastName;
    
    /** The email address of the user. */
    private String email;
    
    /** The card ID associated with the user. */
    private String card;
    
    /** Locks applied to this class. */
    private Set<UserLock> userLocks = new HashSet<UserLock>(0);
    
    /** The academic permission this user has. */
    private Set<AcademicPermission> academicPermissions = new HashSet<AcademicPermission>(0);
    
    /** Associations this class has with user classes. */
    private Set<UserAssociation> userAssociations = new HashSet<UserAssociation>(0);
    
    /** The sessions this user has performed. */
    private Set<Session> sessions = new HashSet<Session>(0);

    public User()
    {
        /* Bean style constructor. */
    }

    public User(final String name, final String namespace, final String persona)
    {
        this.name = name;
        this.namespace = namespace;
        this.persona = persona;
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

    @Column(name = "name", nullable = false, length = 50)
    public String getName()
    {
        return this.name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    @Column(name = "namespace", nullable = false, length = 50)
    public String getNamespace()
    {
        return this.namespace;
    }

    public void setNamespace(final String namespace)
    {
        this.namespace = namespace;
    }

    @Column(name = "persona", nullable = false, length = 8)
    public String getPersona()
    {
        return this.persona;
    }

    public void setPersona(final String persona)
    {
        this.persona = persona;
    }

    @Column(name = "first_name", nullable = true, length = 50)
    public String getFirstName()
    {
        return this.firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    @Column(name = "last_name", nullable = true, length = 50)
    public String getLastName()
    {
        return this.lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    @Column(name = "email", nullable = true, length = 100)
    public String getEmail()
    {
        return this.email;
    }

    public void setEmail(final String email)
    {
        this.email = email;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    public Set<UserLock> getUserLocks()
    {
        return this.userLocks;
    }

    public void setUserLocks(final Set<UserLock> userLocks)
    {
        this.userLocks = userLocks;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.REMOVE)
    public Set<AcademicPermission> getAcademicPermissions()
    {
        return this.academicPermissions;
    }

    public void setAcademicPermissions(final Set<AcademicPermission> academicPermissions)
    {
        this.academicPermissions = academicPermissions;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    public Set<UserAssociation> getUserAssociations()
    {
        return this.userAssociations;
    }

    public void setUserAssociations(final Set<UserAssociation> userAssociations)
    {
        this.userAssociations = userAssociations;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    public Set<Session> getSessions()
    {
        return this.sessions;
    }

    public void setSessions(final Set<Session> sessions)
    {
        this.sessions = sessions;
    }
    
    @Column(name = "card", nullable = true, unique = true)
    public String getCard()
    {
        return card;
    }

    public void setCard(String card)
    {
        this.card = card;
    }

    /**
     * Utility method to provide the qualified name of this user in the form
     * &lt;namespace&gt;:&lt;name&gt;
     * 
     * @return qualified name
     */
    public String qName()
    {
        return this.namespace + ':' + this.name;
    }
}
