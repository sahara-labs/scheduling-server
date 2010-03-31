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
 * @date 31st March 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.queuer.impl;

import org.hibernate.Session;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigTypeDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserAssociation;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClass;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;

/**
 * Class which provides utility for entry to the queue.
 */
public class QueueEntry
{
    /** Resource permission. */
    private ResourcePermission resourcePerm;
    
    /** User to add to queue. */
    private User user;
    
    /** Database session. */
    private Session session;

    /** Logger. */
    private Logger logger;
    
    public QueueEntry(Session session)
    {
        this.session = session;
        this.logger = LoggerActivator.getLogger();
    }
    
    /**
     * Checks if a user has permission to use the resoruce permission.
     * Permission is having an association to the user class which 
     * owns the resource permission.
     * 
     * @param user user to check permission of 
     * @param perm resource permission 
     * @return true if the user has permission use the resource permission 
     */
    public boolean hasPermission(User user,  ResourcePermission perm)
    {
        UserClass permClass = perm.getUserClass();
        for (UserAssociation assoc : permClass.getUserAssociations())
        {
            if (assoc.getUserClass().getId().longValue() == permClass.getId().longValue())
            {
                this.resourcePerm = perm;
                this.user = user;
                return true;
            }
        }

        this.logger.warn("User " + user.getNamespace() + ':' + user.getName() + " does not have " +
                		"permission to queue for permission with identifier " + perm.getId() + '.');
        return false;
    }
    
    /**
     * Checks whether the user has permission to use the requested resource.
     * Permission is if the resource is contained in a resource permission of 
     * a class the user is a member of.
     * <br />
     * If the request is a rig, the resource permission must contain a matching
     * rig resource. A type permission or request capabilities permission
     * containing a type or request capabilities which may result in the requested
     * rig being assigned is not considered as having permission. To put this
     * succinctly, there must be a direct match between the requested resource
     * and a resource permission resource.
     * <br />
     * The requested resource may be identified by either resource identifier
     * (primary key) or resource name, with the resource identifier taking
     * precendence. To force using the resource name, provide 0 as the 
     * resource identifier.
     * 
     * @param user user requesting resource
     * @param type type of resource (either RIG, TYPE or CAPABILITY)
     * @param resId resource identifier (optional, may be 0)
     * @param resName resource name (optional, may be null) 
     * @return true if the user has permission
     */
    public boolean hasPermission(User user, String type, long resId, String resName)
    {
        this.user = user;
        
        Rig rig;
        RigDao rigDao = new RigDao(this.session);
        
        RigType rigType;
        RigTypeDao rigTypeDao = new RigTypeDao(this.session);
        
        if (type.equals(ResourcePermission.RIG_PERMISSION) && 
                (resId > 0 && (rig = rigDao.get(resId)) != null || 
                 resName != null && (rig = rigDao.findByName(resName)) != null))
        {
            for (UserAssociation assoc : user.getUserAssociations())
            {
                /* Look at the resource permissions of the classes the user is a member of
                 * and check if any provide the requested rig. */
                for (ResourcePermission rp : assoc.getUserClass().getResourcePermissions())
                {
                    /* If the resource class contains a rig resource, the user has 
                     * permission if the requested rig is the same rig. */
                    if (rp.getType().equals(ResourcePermission.RIG_PERMISSION) && rig.getId().equals(rp.getRig().getId()))
                    {
                        this.resourcePerm = rp;
                        return true;
                    }
                }
            }
            this.logger.warn("User " + user.getNamespace() + ':' + user.getName() + " does not have permission to use " +
            		"rig " + rig.getName());
        }
        else if (type.equals(ResourcePermission.TYPE_PERMISSION) && 
                (resId > 0 && (rigType = rigTypeDao.get(resId)) != null ||
                 resName != null && (rigType = rigTypeDao.findByName(resName)) != null))
        {
            for (UserAssociation assoc : user.getUserAssociations())
            {
                /* Look at the resource permissions of the classes  the user is a member of
                 * and check if any provide the requested rig. */
                
            }
        }
        else
        {
            /* Resource not found. */
            if (resId > 0) this.logger.warn("Resource with id " + resId + " not found.");
            else if (resName != null) this.logger.warn("Resource with name " + resName + " not found.");
            else this.logger.warn("Resource not found because no identity information was found.");
            return false;
        }
        
        return false;
    }
    
    public boolean isViable()
    {
        
        
        return false;
    }
}
