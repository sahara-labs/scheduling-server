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
 * @date 16th March 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao;

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserAssociation;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserAssociationId;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClass;

/**
 * Data access object for the {@link UserAssociation} entity.
 */
public class UserAssociationDao extends GenericDao<UserAssociation>
{
    public UserAssociationDao()
    {        
        super(UserAssociation.class);
    }

    public UserAssociationDao(Session ses)
    {
        super(ses, UserAssociation.class);
    }
    
    /**
     * Adds a user association for the user and user class.
     * 
     * @param user user to whose association to add
     * @param userClass class to associate
     */
    public void add(User user, UserClass userClass)
    {
        UserAssociation ua = new UserAssociation();
        ua.setUser(user);
        ua.setUserClass(userClass);
        ua.setId(new UserAssociationId(user.getId(), userClass.getId()));
        
        this.persist(ua);
    }
    
    /**
     * Deletes the user association of the user and user class.
     * 
     * @param user user whose association to delete
     * @param userClass associated user class
     */
    public void delete(User user, UserClass userClass)
    {
        UserAssociation ua = (UserAssociation) this.session.createCriteria(UserAssociation.class)
                .add(Restrictions.eq("user", user))
                .add(Restrictions.eq("userClass", userClass))
                .uniqueResult();
        
        if (ua == null)
        {
            this.logger.debug("Unable to delete user association between user '" + user.qName() + "' and class '" + 
                    userClass.getName() + "'.");
        }
        else
        {
            this.delete(ua);
        }
    }
}
