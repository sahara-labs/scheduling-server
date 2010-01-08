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
 * @date 4th January 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.dataaccess;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.AcademicPermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClass;

/**
 * Data access object for the {@link AcademicPermission} class.
 */
public class AcademicPermissionDao extends GenericDao<AcademicPermission>
{
    /**
     * Constructor that opens a new session.
     * 
     * @throws IllegalStateException if a session factory cannot be obtained
     */
    public AcademicPermissionDao()
    {
        super(AcademicPermission.class);
    }
    
    /**
     * Constructor that uses the provided session. The session must be 
     * not-null and open.
     * 
     * @param ses open session
     * @throws IllegalStateException if the provided use session is null or
     *         closed
     */
    public AcademicPermissionDao(Session ses)
    {
        super(ses, AcademicPermission.class);
    }
    
    /**
     * Gets all the academic permissions for the specified user. 
     * 
     * @param user user to find permissions for 
     * @return list of academic permissions 
     */
    @SuppressWarnings("unchecked")
    public List<AcademicPermission> getByUser(User user)
    {
        Criteria cri = this.session.createCriteria(AcademicPermission.class);
        cri.add(Restrictions.eq("user", user));
        return cri.list();
    }
    
    /**
     * Gets all the academic permissions for the specified user class.
     * 
     * @param uClass user class to find permissions for
     * @return list of academic permissions 
     */
    @SuppressWarnings("unchecked")
    public List<AcademicPermission> getByUserClass(UserClass uClass)
    {
        Criteria cri = this.session.createCriteria(AcademicPermission.class);
        cri.add(Restrictions.eq("userClass", uClass));
        return cri.list();
    }
    
    /**
     * Gets the academic permission for the specified user and user class. If it is
     * not found <code>null</code>. If more than one permission is found, the first
     * is used.
     * 
     * @param user user constraint
     * @param uClass user class constraint
     * @return permission or <code>null</code>
     */
    @SuppressWarnings("unchecked")
    public AcademicPermission getForUserAndUserClass(User user, UserClass uClass)
    {
        Criteria cri = this.session.createCriteria(AcademicPermission.class);
        cri.add(Restrictions.eq("user", user))
           .add(Restrictions.eq("userClass", uClass));
        
        List<AcademicPermission> perms = cri.list();
        if (perms.size() == 0)
        {
            return null;
        }
        else if (perms.size() > 1)
        {
            this.logger.warn("Found more than one academic permission for the user " + user.getName() + " and " +
            		"user class " + uClass.getName() + ". Using the first found permission.");
        }
        return perms.get(0);
    }
}
