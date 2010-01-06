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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * AcademicPermission entity which maps to the academic_permission table.
 * <p />
 * The academic_permission holds the permission of a 'academic' persona
 * user for a specific user class. The permissions include:
 * <ul>
 *  <li>View - Whether in progress sessions from the user class
 *  may be viewed by the user.</li>
 *  <li>Control - Whether in progress sessions from the user class 
 *  may be controlled by the user.</li>
 *  <li>Kick - Whether in progress sessions from the user  </li>
 * <ul>
 */
@Entity
@Table(name = "academic_permission")
public class AcademicPermission implements java.io.Serializable
{
    /** Serializable class. */
    private static final long serialVersionUID = -1352419248253487531L;
    
    /** Record primary key. */
    private Integer id;
    
    /** User whom the permission refers to. */
    private User user;
    
    /** User class whom the permission refers to. */
    private UserClass userClass;
    
    /** Whether the user may view user class sessions. */
    private boolean canView;
    
    /** Whether the user may control user class sessions. */
    private boolean canControl;
    
    /** Whether the user may kick users from user class sessions. */
    private boolean canKick;
    
    /** Whether the user may other academic permissions for the user class. */
    private boolean canModify;
    
    /** Whether the user may generate reports about user class sessions. */
    private boolean canGenerateReports;

    public AcademicPermission()
    {
        /* Java bean style constructor. */
    }

    public AcademicPermission(User user, UserClass userClass, boolean canView,
            boolean canControl, boolean canKick, boolean canModify,
            boolean canGenerateReports)
    {
        this.user = user;
        this.userClass = userClass;
        this.canView = canView;
        this.canControl = canControl;
        this.canKick = canKick;
        this.canModify = canModify;
        this.canGenerateReports = canGenerateReports;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId()
    {
        return this.id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    public User getUser()
    {
        return this.user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_class_id", nullable = false)
    public UserClass getUserClass()
    {
        return this.userClass;
    }

    public void setUserClass(UserClass userClass)
    {
        this.userClass = userClass;
    }

    @Column(name = "can_view", nullable = false)
    public boolean isCanView()
    {
        return this.canView;
    }

    public void setCanView(boolean canView)
    {
        this.canView = canView;
    }

    @Column(name = "can_control", nullable = false)
    public boolean isCanControl()
    {
        return this.canControl;
    }

    public void setCanControl(boolean canControl)
    {
        this.canControl = canControl;
    }

    @Column(name = "can_kick", nullable = false)
    public boolean isCanKick()
    {
        return this.canKick;
    }

    public void setCanKick(boolean canKick)
    {
        this.canKick = canKick;
    }

    @Column(name = "can_modify", nullable = false)
    public boolean isCanModify()
    {
        return this.canModify;
    }

    public void setCanModify(boolean canModify)
    {
        this.canModify = canModify;
    }

    @Column(name = "can_generate_reports", nullable = false)
    public boolean isCanGenerateReports()
    {
        return this.canGenerateReports;
    }

    public void setCanGenerateReports(boolean canGenerateReports)
    {
        this.canGenerateReports = canGenerateReports;
    }
}
