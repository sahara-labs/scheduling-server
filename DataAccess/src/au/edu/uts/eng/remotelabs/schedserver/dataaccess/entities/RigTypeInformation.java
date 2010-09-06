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
 * @date 6th September 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities;

import static javax.persistence.GenerationType.AUTO;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * RigType entity class which maps to the rig_type_information table.
 * <p />
 * The rig rigType class holds information about rig types.
 */
@Entity
@Table(name = "rig_type_information", uniqueConstraints = {@UniqueConstraint(columnNames = "rig_type_id")})
public class RigTypeInformation implements Serializable
{
    /** Serializable class. */
    private static final long serialVersionUID = -696887487976221175L;
    
    /** Record primary key. */
    private int id;
    
    /** The rig rigType this information is about. */
    private RigType rigType;
    
    /** The institution this rig rigType belongs to. */
    private String institution;
    
    /** A textual description of the rig rigType. */
    private String description;
    
    /** A shorter description of the rig rigType. */
    private String shortDescription;
    
    /** A description on how to use the rig rigType. */
    private String usageDescription;
    
    public RigTypeInformation()
    {
        /* Bean style constructor. */
    }

    @Id
    @GeneratedValue(strategy = AUTO)
    @Column(name = "id", unique = true, nullable = false)
    public int getId()
    {
        return this.id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rig_type_id")
    public RigType getRigType()
    {
        return this.rigType;
    }

    public void setRigType(RigType type)
    {
        this.rigType = type;
    }

    @Column(name = "institution", nullable = false)
    public String getInstitution()
    {
        return this.institution;
    }

    public void setInstitution(String institution)
    {
        this.institution = institution;
    }

    @Column(name = "description", nullable = false, length = 2055)
    public String getDescription()
    {
        return this.description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @Column(name = "short_description", nullable = true, length = 2055)
    public String getShortDescription()
    {
        return this.shortDescription;
    }

    public void setShortDescription(String shortDescription)
    {
        this.shortDescription = shortDescription;
    }

    @Column(name = "usage_description", nullable = true, length = 2055)
    public String getUsageDescription()
    {
        return this.usageDescription;
    }

    public void setUsageDescription(String usageDescription)
    {
        this.usageDescription = usageDescription;
    }
}
