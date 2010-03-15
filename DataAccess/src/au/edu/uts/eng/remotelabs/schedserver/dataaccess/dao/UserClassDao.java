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
 * @date 14th March 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao;

import java.io.Serializable;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.AcademicPermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserAssociation;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClass;

/**
 * Data access object for user class entities.
 */
public class UserClassDao extends GenericDao<UserClass>
{
    public UserClassDao()
    {
        super(UserClass.class);
    }
    
    public UserClassDao(Session ses)
    {
        super(ses, UserClass.class);
    }
    
    /**
     * Gets the user with the specified name.
     * 
     * @param name name of user class
     * @return user class or null if not found
     */
    public UserClass findByName(String name)
    {
        Criteria cri = this.session.createCriteria(UserClass.class);
        cri.add(Restrictions.eq("name", name));
        return (UserClass)cri.uniqueResult();
    }
    
    @Override
    public void delete(Serializable id)
    {
        UserClass uc = this.get(id);
        if (uc != null) this.delete(uc);
    }
    
    @Override
    public void delete(UserClass uc)
    {
        this.session.beginTransaction();
        
        /* Delete all the user associations. */
        for (UserAssociation assoc : uc.getUserAssociations())
        {
            this.session.delete(assoc);
        }
        
        /* Delete all academic permissions. */
        for (AcademicPermission perm : uc.getAcademicPermissions())
        {
            this.session.delete(perm);
        }
        
        /* Delete all associated resource permissions. */
        for (ResourcePermission perm : uc.getResourcePermissions())
        {
            /* Null out any sessions which have this resource permission. */
            for (au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session ses : 
                    perm.getSessionsForResourcePermissionId())
            {
                ses.setResourcePermissionByResourcePermissionId(null);
            }
            this.session.delete(perm);
        }
        
        this.session.getTransaction().commit();
        
        super.delete(uc);
    }
}
