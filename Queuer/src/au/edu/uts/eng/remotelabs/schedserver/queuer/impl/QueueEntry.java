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
import static au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission.CONSUMER_PERMISSION;
import static au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission.RIG_PERMISSION;
import static au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission.TYPE_PERMISSION;

import java.util.Date;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RequestCapabilitiesDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.ResourcePermissionDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigTypeDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.SessionDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserLockDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.MatchingCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RemotePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RequestCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserAssociation;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClass;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserLock;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.SessionEventListener.SessionEvent;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.requests.QueueRequest;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.requests.SessionInformationRequest;
import au.edu.uts.eng.remotelabs.schedserver.queuer.QueueActivator;

/**
 * Class which provides utility for entry to the queue. To add a user to the
 * queue the following methods must be called in sequence:
 * <ol>
 *  <li><tt>isInQueue</tt> - Checks whether the user is already in the queue
 *  or if they have an in progress rig session. If the response is 
 *  <code>false</code> queue entry can proceed.</li>
 *  <li><tt>hasPermission</tt> - Checks whether the user (set up in the call
 *  to <tt>isInQueue</tt>) has access to the requested permission OR resource,
 *  depending on which overloaded method is called. If the response is 
 *  <code>true</code> queue entry can proceed.</li>
 *  <li><tt>canUserQueue</tt> - Checks whether can queue. The ability can 
 *  queue is based on rig viability and rig use.If the response is 
 *  <code>true</code> queue entry can proceed.</li>
 *  <li><tt>addToQueue</tt> - Actually add the user to the queue for the 
 *  requested resource (set up in the call to one of <tt>hasPermission</tt>
 *  overloaded methods). If the response is <code>true</code> the user
 *  may either be in the queue for a resource or directly assigned to a 
 *  resource.</li>
 * </ol>
 */
public class QueueEntry
{
    /** Resource permission. */
    private ResourcePermission resourcePerm;
    
    /** User to add to queue. */
    private User user;
    
    /** Active queue or rig session. */
    private Session activeSession;
    
    /** Failure reason. */
    private String errorMessage;
    
    /** Database session. */
    private org.hibernate.Session db;

    /** Logger. */
    private Logger logger;
    
    public QueueEntry(org.hibernate.Session session)
    {
        this.db = session;
        this.logger = LoggerActivator.getLogger();
    }
    
    /**
     * Checks whether the user is already in the queue.
     * 
     * @param user user record
     * @return true if in queue
     */
    public boolean isInQueue(User user)
    {
        SessionDao dao = new SessionDao(this.db);

        this.user = user;
        this.activeSession = dao.findActiveSession(user);
        return this.activeSession != null;
    }
    
    /**
     * Checks if a user has permission to use the resoruce permission.
     * Permission is having an association to the user class which 
     * owns the resource permission.
     * 
     * @param perm resource permission 
     * @return true if the user has permission use the resource permission 
     */
    public boolean hasPermission(ResourcePermission perm)
    {
        UserClass permClass = perm.getUserClass();
        
        /* First check, whether the user class the permission is part of 
         * is active. */
        if (!permClass.isActive())
        {
            this.logger.warn("Failed permission check for resoruce permission " + perm.getId() + " as it's user class " +
                    "is not active.");
            this.errorMessage = "Permission class not active";
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
            this.errorMessage = "Permission not active";
            return false;
        }
        
        /* Third check, whether the user is a member of the user class which 
         * has the resource permission. */
        for (UserAssociation assoc : this.user.getUserAssociations())
        {   
            if (assoc.getUserClass().getId().longValue() == permClass.getId().longValue())
            {
                this.resourcePerm = perm;
                return true;
            }
        }

        this.logger.warn("User " + this.user.getNamespace() + ':' + this.user.getName() + " does not have " +
                        "permission to queue for permission with identifier " + perm.getId() + '.');
        this.errorMessage = "User does not have permission";
        return false;
    }
    
    /**
     * Checks if a user has permission to use the resoruce permission.
     * Permission is having an association to the user class which 
     * owns the resource permission.
     * 
     * @param permId resource permission identifier
     * @return true if the user has permission use the resource permission 
     */
    public boolean hasPermission(long permId)
    {
        ResourcePermission perm = new ResourcePermissionDao(this.db).get(permId);
        if (perm == null)
        {
            this.logger.warn("Resource permission with identifier " + permId + " not found.");
            this.errorMessage = "Permission not found";
            return false;
        }
        
        return this.hasPermission(perm);
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
     * @param type type of resource (either RIG, TYPE or CAPABILITY)
     * @param resId resource identifier (optional, may be 0)
     * @param resName resource name (optional, may be null) 
     * @return true if the user has permission
     */
    public boolean hasPermission(String type, long resId, String resName)
    {   
        Rig rig;
        RigDao rigDao = new RigDao(this.db);
        
        RigType rigType;
        RigTypeDao rigTypeDao = new RigTypeDao(this.db);
        
        RequestCapabilities caps;
        RequestCapabilitiesDao capsDao = new RequestCapabilitiesDao(this.db);
        
        if (RIG_PERMISSION.equals(type) && (resId > 0 && (rig = rigDao.get(resId)) != null || 
                 resName != null && (rig = rigDao.findByName(resName)) != null))
        {
            for (UserAssociation assoc : this.user.getUserAssociations())
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
            this.logger.warn("User " + this.user.getNamespace() + ':' + this.user.getName() + " does not have permission to use " +
                    "rig " + rig.getName() + '.');
        }
        else if (TYPE_PERMISSION.equals(type) && (resId > 0 && (rigType = rigTypeDao.get(resId)) != null ||
                 resName != null && (rigType = rigTypeDao.findByName(resName)) != null))
        {
            for (UserAssociation assoc : this.user.getUserAssociations())
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
            this.logger.warn("User " + this.user.getNamespace() + ':' + this.user.getName() + " does not have permission to use " +
                    "rig type " + rigType.getName() + '.');
        }
        else if (CAPS_PERMISSION.equals(type) && (resId > 0 && (caps = capsDao.get(resId)) != null ||
                resName != null && (caps = capsDao.findCapabilites(resName)) != null))
        {
            for (UserAssociation assoc : this.user.getUserAssociations())
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
            this.logger.warn("User " + this.user.getNamespace() + ':' + this.user.getName() + " does not have permission to use " +
                    "request capabilites " + caps.getCapabilities() + '.');
        }
        else
        {
            /* Resource not found. */
            if (resId > 0) this.logger.warn("Resource with id " + resId + " not found.");
            else if (resName != null) this.logger.warn("Resource with name " + resName + " not found.");
            else this.logger.warn("Resource not found because no identity information was found.");
            this.errorMessage = "Resource not found";
            return false;
        }
        
        this.errorMessage = "User does not have permission";
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
        /* Check the resource permission isn't locked. */
        UserLock lock = new UserLockDao(this.db).findLock(this.user, this.resourcePerm);
        if (lock != null && lock.isIsLocked())
        {
            this.logger.warn("Cannot queue for resource permission because it is locked for the user.");
            this.errorMessage = "Permission locked";
            return false;
        }
        
        if (RIG_PERMISSION.equals(this.resourcePerm.getType()))
        {
            Rig rig = this.resourcePerm.getRig();
            if (rig == null)
            {
                this.logger.error("Incorrect configuration of a rig type permission. The rig for it was not set.");
                this.errorMessage = "Permission incorrectly configured";
                return false;
            }
            
            if (rig.isOnline() && !rig.isInSession())
            {
                /* Direct assignment is always allowed. */
                return true;
            }
            else if (rig.isOnline())
            {
                /* If the rig is in session, the ability to queue is based on
                 * whether the user class can queue. */
                if (this.resourcePerm.getUserClass().isQueuable())
                {
                    return true;
                }
                this.logger.info("Cannot queue for rig " + rig.getName() + " because it is in use and the user doesn't " +
                		"have permission to queue for the rig.");
                this.errorMessage = "User cannot queue";
                return false;
            }
            else
            {
                this.logger.info("Cannot queue for rig " + rig.getName() + " because it is offline.");
                this.errorMessage = "Rig is offline";
            }
        }
        else if (TYPE_PERMISSION.equals(this.resourcePerm.getType()))
        {
            boolean hasOnline = false;
            RigType type = this.resourcePerm.getRigType();
            if (type == null)
            {
                this.logger.error("Incorrect configuration of a rig type permission. The rig type for it was not set.");
                this.errorMessage = "Permisson incorrectly configured";
                return false;
            }
            
            for (Rig rig : type.getRigs())
            {
                if (rig.isOnline() && !rig.isInSession())
                {
                    /* Direct assignment is always allowed. */
                    return true;
                }
                else if (rig.isOnline())
                {
                    hasOnline = true;
                }
            }
            
            if (hasOnline)
            {
                if (this.resourcePerm.getUserClass().isQueuable())
                {
                    return true;
                }
                this.logger.info("Cannot queue for rig type " + type.getName() + " because none of its rigs are " +
                		"free and the user doesn't have permission to queue for it.");
                this.errorMessage = "User cannot queue";
                return false;
            }
            
            this.logger.info("Cannot queue for rig type " + type.getName() + " because none of its rigs are " +
            		"online.");
            this.errorMessage = "No rigs in type are online";
        }
        else if (CAPS_PERMISSION.equals(this.resourcePerm.getType()))
        {
            boolean hasOnline = false;
            RequestCapabilities caps = this.resourcePerm.getRequestCapabilities();
            if (caps == null)
            {
                this.logger.error("Incorrect configuration of a request capabilities permission. The request " +
                		"capabilities for it was not set.");
                this.errorMessage = "Permission incorrectly configured";
                return false;
            }
            
            /* Check all the matching rigs to the request capabililites. */
            for (MatchingCapabilities match : caps.getMatchingCapabilitieses())
            {
                for (Rig rig : match.getRigCapabilities().getRigs())
                {
                    if (rig.isOnline() && !rig.isInSession())
                    {
                        /* Direct assignment is always allowed to a rig. */
                        return true;
                    }
                    else if (rig.isOnline())
                    {
                        /* If there is at least one online, but in use, the
                         * queue access will be based on the user class
                         * queueable flag. */
                        hasOnline = true;
                    }
                }
            }
            
            if (hasOnline)
            {
                if (this.resourcePerm.getUserClass().isQueuable())
                {
                    return true;
                }
                this.logger.info("Unable to queue for request capabilities " + caps.getCapabilities() + " as no matching " +
                		"rigs are free and the user doesn't have permission to queue for it.");
                this.errorMessage = "No rigs have request capabilities are online";
                return false;
            }
            this.logger.info("Unable to queue for request capabilities " + caps.getCapabilities() + " as no " +
            		"matching rigs are online.");
        }
        else if (CONSUMER_PERMISSION.equals(this.resourcePerm.getType()))
        { 
            return this.resourcePerm.getUserClass().isQueuable();
        }
        else
        {
            this.logger.error("Unknown resource permission type " + this.resourcePerm.getType() + ". This must be " +
            		"either RIG for a rig permission, TYPE for a rig type permission, CAPABILITY for a request " +
            		"capabilities permission or CONSUMER for a consumer permission.");
            this.errorMessage = "Unknown permission type";
        }
        
        return false;
    }

    /**
     * Creates a session and adds it to the queue with the user and resource
     * permission loaded from a call to <code>hasPermission</code>. The code
     * parameter is a file system reference to batch code, or <code>null</code>
     * if interactive session.
     * 
     * @return whether entering the queue was successful
     */
    public boolean addToQueue(String code)
    {
        Date now = new Date();
        
        Session ses = new Session();
        ses.setResourcePermission(this.resourcePerm);
        
        /* Start the session. */
        ses.setActive(true);
        ses.setActivityLastUpdated(now);
        ses.setRequestTime(now);
        
        /* Information about the user. */
        ses.setUser(this.user);
        ses.setUserNamespace(this.user.getNamespace());
        ses.setUserName(this.user.getName());
        
        /* Information about the queued resource. */
        String type = this.resourcePerm.getType();
        ses.setResourceType(type);
        if (RIG_PERMISSION.equals(type))
        {
            Rig rig = this.resourcePerm.getRig();
            ses.setRequestedResourceId(rig.getId());
            ses.setRequestedResourceName(rig.getName());
        }
        else if (TYPE_PERMISSION.equals(type))
        {
            RigType rigType = this.resourcePerm.getRigType();
            ses.setRequestedResourceId(rigType.getId());
            ses.setRequestedResourceName(rigType.getName());
        }
        else if (CAPS_PERMISSION.equals(type))
        {
            RequestCapabilities caps = this.resourcePerm.getRequestCapabilities();
            ses.setRequestedResourceId(caps.getId());
            ses.setRequestedResourceName(caps.getCapabilities());
        }
        else if (CONSUMER_PERMISSION.equals(type))
        {
            if (!this.handleConsumerQueue(ses)) return false;
        }

        ses.setCodeReference(code);
        ses.setExtensions(this.resourcePerm.getAllowedExtensions());
        ses.setDuration(this.resourcePerm.getSessionDuration());
        ses.setPriority(this.resourcePerm.getUserClass().getPriority());
        
        SessionDao sessionDao = new SessionDao(this.db);
        sessionDao.persist(ses);
        
        /* Add the session to the queue. */
        this.activeSession = Queue.getInstance().addEntry(ses, this.db);
        
        if (this.activeSession != null) QueueActivator.notifySessionEvent(
                    this.activeSession.getRig() == null ? SessionEvent.QUEUED : SessionEvent.ASSIGNED,
                    this.activeSession, this.db);
        
        return this.activeSession != null;
    }

    /**
     * Handles a consumer queue.
     * 
     * @param ses queue session
     * @return true if successful
     */
    private boolean handleConsumerQueue(Session ses)
    {
        RemotePermission remotePerm = this.resourcePerm.getRemotePermission();
        if (remotePerm == null)
        {
            this.logger.warn("Cannot queue for a consumer permission (id=" + this.resourcePerm.getId() + 
                    ") because there is no remote permission.");
            this.errorMessage = "No remote permission mapped";
            return false;
        }
        
        String remoteSite = remotePerm.getSite().getName();
        
        QueueRequest providerCall = new QueueRequest();
        if (!providerCall.addToQueue(remotePerm, this.user, this.db))
        {
            this.logger.warn("Remote queue failed with reason '" + providerCall.getFailureReason() + "'.");
            this.errorMessage = "COMMUNICATION ERROR: '" + remoteSite + "' failed (" + providerCall.getFailureReason() + ")";
            return false;
        }
        
        if (!providerCall.wasOperationSuccessful() || providerCall.isInBooking())
        {
            /* Provider failed to queue. The in-booking condition signifies 
             * a queue error because if a user in that state no queue attempts
             * will succeed. */
            this.logger.info("Provider '" + remoteSite + "' failed to queue for permission (id=" + 
                    this.resourcePerm.getId() + ") with reason: " + providerCall.getOperationReason() + ".");
            this.errorMessage = "PROVIDER ERROR:" + providerCall.getOperationReason();
            return false;
        }
        
        ses.setRequestedResourceId(-1L);
        if (providerCall.isInQueue())
        {
            this.logger.debug("Provider queuing succeeded, user '" + this.user.getName() + "' now in queue state.");
            ses.setRequestedResourceName(providerCall.getResourceName());
        }
        else if (providerCall.isInSession())
        {
            this.logger.debug("Provider queuing succeeded, user '" + this.user.getName() + "' now in session state.");
            
            /* Since in session state, we now need to unpack session 
             * information. */
            SessionInformationRequest provSes = providerCall.getSession();
            if (provSes == null)
            {
                this.logger.error("PROVIDER BUG: Queue response specifies the user is in session, but no session " +
                        "has been specified."); 
                this.errorMessage = "No session information provided";
                return false;
            }
            
            ses.setAssignmentTime(new Date());
            ses.setRequestedResourceName(provSes.getResourceName());
             
            /* Session state conditions. */
            ses.setReady(provSes.isReady());
            ses.setInGrace(provSes.isInGrace());

            /* Remote rig type may not exist either, if not will need to be added. */
            String siteNs = remotePerm.getSite().getUserNamespace();
            RigType remoteType = new RigTypeDao(this.db).
                    loadOrCreate(siteNs + ':' + provSes.getRigType(), false, "provider=" + remoteSite, remotePerm.getSite());
            
            RigDao rigDao = new RigDao(this.db);
            Rig remoteRig = rigDao.findByName(siteNs + ':' + provSes.getRigName());
            if (remoteRig == null)
            {
                /* Provider rig has not previously been used at site so it 
                 * must be created. */
                remoteRig = new Rig();
                /* Rig name format is <Provider>:<Specificed rig name>. */
                remoteRig.setName(siteNs + ':' + provSes.getRigName());
                /* Meta information about source. */
                remoteRig.setMeta("provider=" + remoteSite);
                remoteRig.setActive(false);  // The rig cannot take local sessions so cannot be online
                remoteRig.setOnline(false);  
                remoteRig.setManaged(false); // Is not managed because normal watch dogging does not apply
                remoteRig.setContactUrl(provSes.getContactURL());
                remoteRig.setLastUpdateTimestamp(new Date());
                remoteRig.setRigType(remoteType);
                remoteRig.setSite(remotePerm.getSite());
                
                rigDao.persist(remoteRig);
            }
            else
            {
                /* Rig details may have changed since last used session. */
                boolean requiresFlush = false;
                if (remoteRig.getContactUrl() == null || !remoteRig.getContactUrl().equals(provSes.getContactURL()))
                {
                    remoteRig.setContactUrl(provSes.getContactURL());
                    remoteRig.setLastUpdateTimestamp(new Date());
                    requiresFlush = true;
                }
                
                if (!remoteRig.getRigType().getId().equals(remoteType.getId()))
                {
                    remoteRig.setRigType(remoteType);
                    requiresFlush = true;
                }
                      
                if (requiresFlush) rigDao.flush();
            }
            
            ses.setRig(remoteRig);
            
            ses.setExtensions((short)provSes.getExtensions());
            ses.setDuration(provSes.getDuration());
        }
        else
        {
            /* Some unexpected state, something wrong so queuing failed. */
            this.logger.error("Some unknown state returned from user so assuming queuing failed.");
            this.errorMessage = "Unknown state";
            return false;
        }
        
        return true;
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
    
    /**
     * Returns the users active session.
     * 
     * @return active session or null if none exists
     */
    public Session getActiveSession()
    {
        return this.activeSession;
    }
    
    /**
     * Gets an error message if a method called returns false.
     *  
     * @return error message or null if no error
     */
    public String getErrorMessage()
    {
        return this.errorMessage;
    }
}
