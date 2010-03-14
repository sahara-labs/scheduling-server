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
package au.edu.uts.eng.remotelabs.schedserver.permissions.impl;

import org.hibernate.Session;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;

/**
 * Class to add, edit and delete users.
 */
public class UserAdmin
{   
    /** Failure reason. */
    private String failureReason;
    
    /** User data access object. */
    private UserDao dao;

    /** Logger. */
    private Logger logger;
    
    public UserAdmin(Session ses)
    {
        this.logger = LoggerActivator.getLogger();
        this.dao = new UserDao(ses);
    }
    
    /**
     * Adds a regular user (persona type USER) with the specified name and 
     * name space.
     * 
     * @param bs users name space 
     * @param name users name
     * @return true if successful, false otherwise
     */
    public boolean addUser(String ns, String name)
    {
        return this.addUser(ns, name, User.USER);
    }
    
    /**
     * Adds a user with the specified user name, name space and persona type.
     * 
     * @param ns users name space
     * @param name users name
     * @param persona users persona type
     * @return true if successful
     */
    public boolean addUser(String ns, String name, String persona)
    {
        this.logger.debug("Adding user with name='" + name + "', name space='" + ns + "' and persona='" 
                + persona + "'.");
        
        if ((persona = this.verfiyPersona(persona)) == null)
        {
            this.failureReason = "Unknown persona type " + persona;
            this.logger.warn("Failed adding user with name '" + name + "' and namespace '" + ns + "' because " +
                    " the persona type is not one of ADMIN, ACADEMIC, USER or DEMO.");
            return false;
        }
        
        /* Check doesn't already exist. */        
        User user = this.dao.findByName(ns, name);
        if (user != null)
        {
            this.logger.warn("Failed adding new user because a user with name '" + name + "' and namespace '" +
                    ns + "' already exists.");
            this.failureReason = "Name, namespace combination not unique";
            return false;
        }
        
        /* OK to save user. */
        user = new User(name, ns, persona);
        this.dao.persist(user);
        return true;
    }
    
    /**
     * Edits the user's details. The users name, name space and/or persona
     * may be changed, with the specified identifier used to determine 
     * the users record.
     * 
     * @param id users record identifier
     * @param ns users name space
     * @param name users name
     * @param persona
     * @return true if successful, false if record with id is not found
     */
    public boolean editUser(long id, String ns, String name, String persona)
    {
        User user = this.dao.get(id);
        if (user == null)
        {
            this.logger.warn("Unable to load user with record id='" + id + "', so unable to edit their details.");
            this.failureReason = "User not found";
            return false;
        }
        
        if (name != null && ns != null && (!user.getNamespace().equals(ns) || !user.getName().equals(name)))
        {
            /* Check the new name - name space is unique. */
            if (this.dao.findByName(ns, name) != null)
            {
               this.logger.warn("Unable to modify the user name and namespace of user with record " + user.getId() +
                       " as the new name, namespace combination will not be unique.");
               this.failureReason = "Name, namespace combination not unique";
               return false;
            }
            user.setName(name);
            user.setNamespace(ns);
        }
        
        if ((persona = this.verfiyPersona(persona)) == null)
        {
            this.failureReason = "Unknown persona type " + persona;
            this.logger.warn("Failed editting user with name '" + name + "' and namespace '" + ns + "' because " +
                    " the persona type is not one of ADMIN, ACADEMIC, USER or DEMO.");
            return false;
        }
        user.setPersona(persona);
        this.dao.flush();
        
        return true;
    }
    
    
    /**
     * Edits the users details. Loads the user with the specified name and 
     * name space and modifies the user's persona.
     * 
     * @param name users name
     * @param ns users namespace
     * @param persona the new persona
     * @return true if successful
     */
    public boolean editUser(String ns, String name, String persona)
    {
        User user = this.dao.findByName(ns, name);
        if (user == null)
        {
            this.failureReason = "User does not exist";
            this.logger.warn("Unable to edit the user with the name " + name + " and namespace " + ns + 
                    " because they do not exist.");
            return false;
        }
        
        if ((persona = this.verfiyPersona(persona)) == null)
        {
            this.failureReason = "Persona not valid";
            this.logger.warn("Unable to edit the user with the name " + name + " and namespace " + ns + 
                    " because the persona is not valid.");
            return false;
        }
        
        user.setPersona(persona);
        this.dao.flush();
        
        return true;
    }
    
    /**
     * Deletes the user with the specified identifer.
     * 
     * @param id user identifer
     * @return true if successful
     */
    public boolean deleteUser(long id)
    {
        User user = this.dao.get(id);
        if (user == null)
        {
            this.logger.warn("Unable to delete user with id " + id + " as it does not exist.");
            this.failureReason = "User does not exist";
            return false;
        }
        
        this.dao.delete(user);
        return true;
    }
    
    /**
     * Deletes the user with specified name and namespace.
     * 
     * @param ns namespace of user
     * @param name name of user
     * @return true if successful
     */
    public boolean deleteUser(String ns, String name)
    {
        User user = this.dao.findByName(ns, name);
        if (user == null)
        {
            this.logger.warn("Unable to delete user with name " + name + ", namespace " + ns + '.');
            this.failureReason = "User does not exist";
            return false;
        }
        
        this.dao.delete(user);
        return true;
    }
    
    /**
     * Verifies if the supplied persona is a correct persona type. Valid 
     * persona types are:
     * <ul>
     *  <li><tt>ADMIN</tt> - Administration god user.</li>
     *  <li><tt>ACADEMIC</tt> - Academic user.</li>
     *  <li><tt>USER</tt> - Regular user.</li>
     *  <li><tt>DEMO</tt> - Demonstration user.</li>
     * </ul>
     * 
     * @param persona persona to verify
     * @return upper case version of persona or null if not valid
     */
    private String verfiyPersona(String persona)
    {
        if (User.ACADEMIC.equalsIgnoreCase(persona)) return persona.toUpperCase();
        if (User.ADMIN.equalsIgnoreCase(persona)) return persona.toUpperCase();
        if (User.DEMO.equalsIgnoreCase(persona)) return persona.toUpperCase();
        if (User.USER.equalsIgnoreCase(persona)) return persona.toUpperCase();
        
        this.logger.debug("Verifying persona " + persona + " failed. If must be either 'ADMIN', 'ACADEMIC', 'USER' or" +
        		" 'DEMO'.");
        return null;
    }

    /**
     * @return the failureReason
     */
    public String getFailureReason()
    {
        return this.failureReason;
    }

    /**
     * Close the in use session.
     */
    public void closeSession()
    {
        this.dao.closeSession();
    }
}
