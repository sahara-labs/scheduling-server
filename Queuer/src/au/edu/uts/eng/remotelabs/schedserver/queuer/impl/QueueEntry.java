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

import static au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission.CAPS_PERMISSION;
import static au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission.RIG_PERMISSION;
import static au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission.TYPE_PERMISSION;

import java.util.Date;

import org.hibernate.Session;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RequestCapabilitiesDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigTypeDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RequestCapabilities;
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
        
        /* First check, whether the user class the permission is part of 
         * is active. */
        if (!permClass.isActive())
        {
            this.logger.warn("Failed permission check for resoruce permission " + perm.getId() + " as it's user class " +
            		"is not active.");
            return false;
        }
        
        /* Second check, whether the resource permission is active. */
        if (!this.isResourcePermissionActive(perm))
        {
            if (perm.getStartTime().after(new Date()))
            {
                this.logger.warn("Failed permission check for resource permission " + perm.getId() + " because the  " +
                		"permissions time window has not begun yet. (Starts on " + perm.getStartTime().toString() + ")");
            }
            else
            {
                this.logger.warn("Failed permission check for resource permission " + perm.getId() + " because the  " +
                        "permissions time window has expired. (Ended on " + perm.getExpiryTime().toString() + ")");
            }
            return false;
        }
        
        /* Third check, whether the user is a member of the user class which 
         * has the resource permission. */
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
        
        RequestCapabilities caps;
        RequestCapabilitiesDao capsDao = new RequestCapabilitiesDao(this.session);
        
        
        if (RIG_PERMISSION.equals(type) && (resId > 0 && (rig = rigDao.get(resId)) != null || 
                 resName != null && (rig = rigDao.findByName(resName)) != null))
        {
            for (UserAssociation assoc : user.getUserAssociations())
            {
                /* Discard the user class if it is not valid. */
                UserClass userClass = assoc.getUserClass();
                if (!assoc.getUserClass().isActive()) continue;
                
                /* Look at the resource permissions of the classes the user is a member of
                 * and check if any provide the requested rig. */
                for (ResourcePermission rp : userClass.getResourcePermissions())
                {
                    if (!this.isResourcePermissionActive(rp)) continue;
                    /* If the resource class contains a rig resource, the user has 
                     * permission if the requested rig is the same rig. */
                    if (RIG_PERMISSION.equals(rp.getType()) && rig.getId().equals(rp.getRig().getId()))
                    {
                        this.resourcePerm = rp;
                        
                        /* One last case to account, a valid permission with undesirable constraints:
                         *   - Queueable - Not having this prohibts access to the queue (only assignment
                         *                 to free resources is allowed.
                         *   - Kickable  - If a user who isn't kickable wants access, this user will
                         *                 be removed.
                         */
                        if (userClass.isQueuable() && !userClass.isKickable())
                        {
                            /* This is the best case for a permission, so finish the search and
                             * return true. */
                            return true;
                        }
                        else
                        {
                            /* As there is undesirable consequences of the found permission, 
                             * continue the search with a different user class so a 
                             * potentially better case may be found. */
                            break;
                        }
                    }
                }
            }
            
            /* This is the case where a permission is found in a user class that isn't queueable
             * or is kickable. The resource is still valid so permission succeeds. */
            if (this.resourcePerm != null) return true;
            
            /* No permission found so fail permission. */
            this.logger.warn("User " + user.getNamespace() + ':' + user.getName() + " does not have permission to use " +
                    "rig " + rig.getName() + '.');
        }
        else if (TYPE_PERMISSION.equals(type) && (resId > 0 && (rigType = rigTypeDao.get(resId)) != null ||
                 resName != null && (rigType = rigTypeDao.findByName(resName)) != null))
        {
            for (UserAssociation assoc : user.getUserAssociations())
            {
                /* Discard the user class if it is not valid. */
                UserClass userClass = assoc.getUserClass();
                if (!userClass.isActive()) continue;
                
                /* Look at the resource permissions of the classes the user is a member of
                 * and check if any provide the requested type. */
                for (ResourcePermission rp : userClass.getResourcePermissions())
                {
                    if (!this.isResourcePermissionActive(rp)) continue;
                    /* If the resource class contains a type resource, the user has 
                     * permission if the requested type is the same type. */
                    if (TYPE_PERMISSION.equals(rp.getType()) && rigType.getId().equals(rp.getRigType().getId()))
                    {
                         this.resourcePerm = rp;
                        
                        /* One last case to account, a valid permission with undesirable constraints:
                         *   - Queueable - Not having this prohibts access to the queue (only assignment
                         *                 to free resources is allowed.
                         *   - Kickable  - If a user who isn't kickable wants access, this user will
                         *                 be removed.
                         */
                        if (userClass.isQueuable() && !userClass.isKickable())
                        {
                            /* This is the best case for a permission, so finish the search and
                             * return true. */
                            return true;
                        }
                        else
                        {
                            /* As there is undesirable consequences of the found permission, 
                             * continue the search with a different user class so a 
                             * potentially better case may be found. */
                            break;
                        }
                    }
                }
            }
            
            /* This is the case where a permission is found in a user class that isn't queueable
             * or is kickable. The resource is still valid so permission succeeds. */
            if (this.resourcePerm != null) return true;
            
            /* No permission found so fail permission. */
            this.logger.warn("User " + user.getNamespace() + ':' + user.getName() + " does not have permission to use " +
                    "rig type " + rigType.getName() + '.');
        }
        else if (CAPS_PERMISSION.equals(type) && (resId > 0 && (caps = capsDao.get(resId)) != null ||
                resName != null && (caps = capsDao.findCapabilites(resName)) != null))
        {
            for (UserAssociation assoc : user.getUserAssociations())
            {
                UserClass userClass = assoc.getUserClass();
                /* Discard the user class if it is not valid. */
                if (!userClass.isActive()) continue;
                
                /* Look at the resource permissions of the classes the user is a member of
                 * and check if any provide the requested request capabilities. */
                for (ResourcePermission rp : userClass.getResourcePermissions())
                {
                    if (!this.isResourcePermissionActive(rp)) continue;
                    /* If the resource class contains a request capabilities resource, the user 
                     * has permission if the requested capabilites are the same. */
                    if (CAPS_PERMISSION.equals(rp.getType()) && caps.getId().equals(rp.getRequestCapabilities().getId()))
                    {
                        this.resourcePerm = rp;
                        
                        /* One last case to account, a valid permission with undesirable constraints:
                         *   - Queueable - Not having this prohibts access to the queue (only assignment
                         *                 to free resources is allowed.
                         *   - Kickable  - If a user who isn't kickable wants access, this user will
                         *                 be removed.
                         */
                        if (userClass.isQueuable() && !userClass.isKickable())
                        {
                            /* This is the best case for a permission, so finish the search and
                             * return true. */
                            return true;
                        }
                        else
                        {
                            /* As there is undesirable consequences of the found permission, 
                             * continue the search with a different user class so a 
                             * potentially better case may be found. */
                            break;
                        }
                    }
                }
            }
            
            /* This is the case where a permission is found in a user class that isn't queueable
             * or is kickable. The resource is still valid so permission succeeds. */
            if (this.resourcePerm != null) return true;
            
            /* No permission found so fail permission. */
            this.logger.warn("User " + user.getNamespace() + ':' + user.getName() + " does not have permission to use " +
                    "request capabilites " + caps.getCapabilities() + '.');
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
    
    /**
     * Returns true if the resource permission resource is viable and can be
     * queued for. A user can queue if:
     * <ul>
     * <li>The resource is free and online.</li>
     * <li>The resource is in use and online and the resource permission has 
     * the queue permission.</li>
     * </ul>
     *
     * @return true if the user can enter the queue
     */
    public boolean canUserQueue()
    {
        if (RIG_PERMISSION.equals(this.resourcePerm.getType()))
        {
            Rig rig = this.resourcePerm.getRig();
            
            if (rig.isOnline() && !rig.isInSession())
            {
                /* Direct assignment is always allowed. */
                return true;
            }
            else if (rig.isOnline() && rig.isInSession())
            {
                /* If the rig is in session, the ability to queue is based on
                 * whether the user class can queue. */
                return this.resourcePerm.getUserClass().isQueuable();
            }
        }
        else if (TYPE_PERMISSION.equals(this.resourcePerm.getType()))
        {
            RigType type = this.resourcePerm.getRigType();
            
            // TODO 
        }
        else if (CAPS_PERMISSION.equals(this.resourcePerm.getType()))
        {
            
        }
        else
        {
            this.logger.error("Unknown resource permission type " + this.resourcePerm.getType() + ". This must be " +
            		"either RIG for a rig permission, TYPE for a rig type permission or CAPABILITY for a request " +
            		"capabilities permission.");
        }
        
        
        return false;
    }
    
    /**
     * Returns true if the resource permission is valid. A resource permission
     * is valid if the current time is after the start time and after the end 
     * time.
     * 
     * @return true if the resource permission is valid
     */
    private boolean isResourcePermissionActive(ResourcePermission rp)
    {
        Date now = new Date();
        if (rp.getStartTime().before(now) && rp.getExpiryTime().after(now))
        {
            return true;
        }
        return false;
    }
    
    /**
     * Returns the resource permission that will be used when adding a queue 
     * entry.
     * 
     * @return resource permission
     */
    public ResourcePermission getResourcePermission()
    {
        return this.resourcePerm;
    }
    
    /**
     * Returns the user that will be used when queuing a user.
     * 
     * @return user who is to be queued
     */
    public User getUser()
    {
        return this.user;
    }

}
