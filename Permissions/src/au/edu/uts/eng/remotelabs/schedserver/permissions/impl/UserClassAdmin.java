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
 * @date 15th March 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.permissions.impl;

import org.hibernate.Session;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserClassDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClass;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;

/**
 * Administration class to add, edit and delete user classes.
 */
public class UserClassAdmin
{
    /** User class data access object. */
    private UserClassDao dao;
    
    /** Reason an operation failed. */
    private String failureReason;
    
    /** Logger. */
    private final Logger logger;
    
    public UserClassAdmin(Session ses)
    {
        this.dao = new UserClassDao(ses);
        this.logger = LoggerActivator.getLogger();
    }
    
    /**
     * Adds a new user class.
     * 
     * @param name name of user class
     * @param priority user class queue priority
     * @param active whether the user class is active
     * @param queuable whether the user class is queuable
     * @param kickable whether the user class is kickable
     * @param lockable whether the members of the user class are lockable
     * @return true if successful
     */
    public boolean addUserClass(String name, int priority, boolean active, boolean queuable, boolean kickable, 
            boolean lockable)
    {
        UserClass userClass = new UserClass();
        userClass.setName(name);
        userClass.setPriority((short) priority);
        userClass.setActive(active);
        userClass.setQueuable(queuable);
        userClass.setKickable(kickable);
        userClass.setUsersLockable(lockable);
        
        this.logger.debug("Adding new user class with name '" + name + "' and priority '" + priority + "'.");
        
        /* Check if there is an existing user class with the same name. */
        if (this.dao.findByName(name) != null)
        {
            this.logger.warn("Unable to add user class with name '" + name + "' because a user class with that name " +
            		"already exists.");
            this.failureReason = "User class with name exists";
            return false;
        }
        
        this.dao.persist(userClass);
        return true;
    }
    
    /**
     * Edits and existing user class.
     * 
     * @param name name of user class
     * @param priority user class queue priority
     * @param active whether the user class is active
     * @param queuable whether the user class is queuable
     * @param kickable whether the user class is kickable
     * @param lockable whether the members of the user class are lockable
     * @return true if successful
     */
    public boolean editUserClass(String name, int priority, boolean active, boolean queuable, boolean kickable, 
            boolean lockable)
    {
        UserClass userClass = this.dao.findByName(name);
        if (userClass == null)
        {
            this.logger.warn("Unable to edit user class with name '" + name + "' as it does not exist.");
            this.failureReason = "User class not found";
            return false;
        }
        
        userClass.setPriority((short) priority);
        userClass.setActive(active);
        userClass.setQueuable(queuable);
        userClass.setKickable(kickable);
        userClass.setUsersLockable(lockable);
        
        this.dao.flush();
        return true;
    }
    
    /**
     * Edits an existing user class.
     * 
     * @param id user class record identifier
     * @param name name of user class (optional)
     * @param priority user class queue priority
     * @param active whether the user class is active
     * @param queuable whether the user class is queuable
     * @param kickable whether the user class is kickable
     * @param lockable whether the members of the user class are lockable
     * @return true if successful
     */
    public boolean editUserClass(long id, String name, int priority, boolean active, boolean queuable, boolean kickable, 
            boolean lockable)
    {
        UserClass userClass = this.dao.get(id);
        if (userClass == null)
        {
            this.logger.warn("Unable to edit user class with record identifier '" + id + "' as it does not exist.");
            this.failureReason = "User class not found";
            return false;
        }
        
        /* If name is being changed, check if it is unique. */
        if (name != null && !userClass.getName().equals(name))
        {
            if (this.dao.findByName(name) != null)
            {
                this.logger.warn("Unable to change name of user class record identifier '" + id + "' because the new " +
                		"name already exists.");
                this.failureReason = "User class with name exists";
                return false;
            }
            userClass.setName(name);
        }
        
        userClass.setPriority((short) priority);
        userClass.setActive(active);
        userClass.setQueuable(queuable);
        userClass.setKickable(kickable);
        userClass.setUsersLockable(lockable);
        
        this.dao.flush();
        return true;
    }
    
    /**
     * Deletes the user class with the specified record identifier.
     * 
     * @param id record identifier
     * @return true if successful
     */
    public boolean deleteUserClass(long id)
    {
        this.dao.delete(id);
        return true;
    }
    
    /**
     * Delete the user class with the specified name.
     * 
     * @param name user class name
     * @return true if successful
     */
    public boolean deleteUserClass(String name)
    {
        UserClass userClass = this.dao.findByName(name);
        if (userClass == null)
        {
            this.logger.warn("Unable to delete user class with name '" + name + "' as it does not exist.");
            this.failureReason = "Class does not exist.";
            return false;
        }
        
        this.dao.delete(userClass);
        return true;
    }
    
    /**
     * Closes the in use database session.
     */
    public void closeSession()
    {
        this.dao.closeSession();
    }

    /**
     * @return the failureReason
     */
    public String getFailureReason()
    {
        return this.failureReason;
    }
}
