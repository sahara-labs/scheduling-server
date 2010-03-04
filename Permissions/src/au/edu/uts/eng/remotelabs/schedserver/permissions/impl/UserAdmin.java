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

import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;

/**
 * Class to add, edit, get and delete users.
 */
public class UserAdmin
{
    /** Administrator user persona. */
    public static final String ADMIN = "ADMIN";
    
    /** Academic user persona. */
    public static final String ACADEMIC = "ACADEMIC";
    
    /** Regular user persona. */
    public static final String USER = "USER";
    
    /** Demonstration user. */
    public static final String DEMO = "DEMO";
    
    /** Failure reason. */
    private String failureReason;

    /** Logger. */
    private Logger logger;
    
    public UserAdmin()
    {
        this.logger = LoggerActivator.getLogger();
    }
    
    /**
     * Adds a regular user (persona type USER) with the specified name and 
     * name space.
     * 
     * @param name user's name
     * @param namespace user's name space 
     * @return true if successful, false otherwise
     */
    public boolean addUser(String name, String namespace)
    {
        return this.addUser(name, namespace, UserAdmin.USER);
    }
    
    /**
     * Adds a user with the specified user name, name space and persona type.
     * 
     * @param name users name
     * @param namespace users name space
     * @param persona users persona type
     * @return true if successful
     */
    public boolean addUser(String name, String namespace, String persona)
    {
        if ((persona = this.verfiyPersona(persona)) == null) return false;
        
        this.logger.debug("Adding user with name='" + name + "', name space='" + namespace + "' and persona='" 
                + persona + "'.");
        
        
        return false;
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
        if (UserAdmin.ACADEMIC.equalsIgnoreCase(persona)) return persona.toUpperCase();
        if (UserAdmin.ADMIN.equalsIgnoreCase(persona)) return persona.toUpperCase();
        if (UserAdmin.DEMO.equalsIgnoreCase(persona)) return persona.toUpperCase();
        if (UserAdmin.USER.equalsIgnoreCase(persona)) return persona.toUpperCase();
        
        this.failureReason = "Unknown persona type " + persona;
        this.logger.warn("Verifying persona " + persona + " failed. If must be either 'ADMIN', 'ACADEMIC', 'USER' or" +
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
    
    
}
