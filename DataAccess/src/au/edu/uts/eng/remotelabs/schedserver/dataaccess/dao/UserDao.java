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
package au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao;

import java.io.Serializable;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.AcademicPermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserAssociation;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserLock;

/**
 * Data access object for {@link User} entity.
 */
public class UserDao extends GenericDao<User>
{   
    /**
     * Constructor that opens a new session.
     * 
     * @throws IllegalStateException if a session factory cannot be obtained
     */
    public UserDao()
    {
        super(User.class);
    }
    
    /**
     * Constructor that uses the provided session. The session must be 
     * not-null and open.
     * 
     * @param ses open session
     * @throws IllegalStateException if the provided use session is null or
     *         closed
     */
    public UserDao(Session ses)
    {
        super(ses, User.class);
    }
    
    /**
     * Finds the user with the specified qualified name. A qualified name has
     * the format &lt;namespace&gt;:&lt;name&gt;. If the user isn't found or
     * the specified qualified name is incorrect <code>null</code> is returned.
     * 
     * @param qname user qualified name
     * @return user or null if not found
     */
    public User findByQName(String qName)
    {
        String parts[] = qName.split(":", 2);
        
        /* Sanity check for incorrect format. */
        if (parts.length < 2) return null;
        
        return (User) this.session.createCriteria(User.class)
                .add(Restrictions.eq("namespace", parts[0]))
                .add(Restrictions.eq("name", parts[1]))
                .uniqueResult();        
    }
    
    /**
     * Returns the user with the specified name space and name or 
     * <code>null</code> if not found.
     * 
     * @param ns user namespace
     * @param name user name
     * @return user or null if not found
     */
    public User findByName(String ns, String name)
    {
        return (User) this.session.createCriteria(User.class)
            .add(Restrictions.eq("namespace", ns))
            .add(Restrictions.eq("name", name))
            .uniqueResult();
    }
    
    public User findByName(String name)
    {
        return (User) this.session.createCriteria(User.class)
            .add(Restrictions.eq("name", name))
            .uniqueResult();
    }
    
    @Override
    public void delete(Serializable id)
    {
        User user = this.get(id);
        this.delete(user);
    }
    
    @Override
    public void delete(User user)
    {
        this.session.beginTransaction();
        
        /** Deletes all the users academic permissions. */
        for (AcademicPermission perm : user.getAcademicPermissions())
        {
            this.session.delete(perm);
        }
        
        /** Deletes all the user associations. */
        for (UserAssociation assoc : user.getUserAssociations())
        {
            this.session.delete(assoc);
        }
        
        /** Deletes all the user locks. */
        for (UserLock lock : user.getUserLocks())
        {
            this.session.delete(lock);
        }
        
        /** NULL the session information. */
        for (au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session ses : user.getSessions())
        {
            ses.setUser(null);
        }
        
        this.session.getTransaction().commit();
        super.delete(user);
    }
}
