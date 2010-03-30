/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2009, University of Technology, Sydney
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
 * @date 28th March 2009
 */

package au.edu.uts.eng.remotelabs.schedserver.queuer.intf;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.ResourcePermissionDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.SessionDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.MatchingCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RequestCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.AddUserToQueue;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.AddUserToQueueResponse;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckPermissionAvailability;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckPermissionAvailabilityResponse;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckResourceAvailability;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckResourceAvailabilityResponse;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.GetUserQueuePosition;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.GetUserQueuePositionResponse;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.InQueueType;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.IsUserInQueue;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.IsUserInQueueResponse;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.OperationRequestType;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.PermissionIDType;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.QueueTargetType;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.QueueType;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.RemoveUserFromQueue;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.RemoveUserFromQueueResponse;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.ResourceIDType;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.UserIDType;

/**
 * Queuer SOAP interface implementation.
 */
public class Queuer implements QueuerSkeletonInterface
{
    /** Logger. */
    private Logger logger;
    
    public Queuer()
    {
        this.logger = LoggerActivator.getLogger();
    }
    
    @Override
    public AddUserToQueueResponse addUserToQueue(final AddUserToQueue request)
    {
        //TODO : fill this with the necessary business logic
        throw new java.lang.UnsupportedOperationException("Please implement " + this.getClass().getName()
                + "#addUserToQueue");
    }

    @Override
    public RemoveUserFromQueueResponse removeUserFromQueue(final RemoveUserFromQueue request)
    {
        //TODO : fill this with the necessary business logic
        throw new java.lang.UnsupportedOperationException("Please implement " + this.getClass().getName()
                + "#removeUserFromQueue");
    }
    
    public GetUserQueuePositionResponse getUserQueuePosition(final GetUserQueuePosition request)
    {
        //TODO : fill this with the necessary business logic
        throw new java.lang.UnsupportedOperationException("Please implement " + this.getClass().getName()
                + "#getUserQueuePosition");
    }
    
    @Override
    public IsUserInQueueResponse isUserInQueue(final IsUserInQueue request)
    {
        /* Request parameters. */
        UserIDType uid = request.getIsUserInQueue();
        this.logger.debug("Received is user in queue request with user id=" + uid.getUserID() + ", user namespace="
                + uid.getUserNamespace() + ", user name=" + uid.getUserName() + '.');
        
        /* Response parameters. */
        IsUserInQueueResponse resp = new IsUserInQueueResponse();
        InQueueType inQueue = new InQueueType();
        resp.setIsUserInQueueResponse(inQueue);
        inQueue.setInQueue(false);
        inQueue.setInSession(false);
        
        SessionDao dao = new SessionDao();
        Session ses;
        User user;
        if (!this.checkPermission(uid))
        {
            this.logger.warn("Unable to check if user is in queue because of invalid permission.");
        }
        else if ((user = this.getUserFromUserID(uid, dao.getSession())) != null &&
                (ses = dao.findActiveSession(user)) != null)
        {
            if (ses.getAssignmentTime() == null)
            {
                /* User is currently in queue. */
                inQueue.setInQueue(true);
            }
            else
            {
                /* User is currently in session. */
                inQueue.setInSession(true);
                Rig rig = ses.getRig();
                ResourceIDType res = new ResourceIDType();
                inQueue.setAssignedResource(res);
                res.setType("RIG");
                res.setResourceID(rig.getId().intValue());
                res.setResourceName(rig.getName());
            }
            
            /* Add requested resource. */
            ResourceIDType res = new ResourceIDType();
            inQueue.setQueuedResouce(res);
            res.setType(ses.getResourceType());
            res.setResourceID(ses.getRequestedResourceId().intValue());
            res.setResourceName(ses.getRequestedResourceName());
        }
        
        dao.closeSession();
        return resp;
    }

    @Override
    public CheckPermissionAvailabilityResponse checkPermissionAvailability(final CheckPermissionAvailability request)
    {
        /* Request parameters. */
        PermissionIDType permResp = request.getCheckPermissionAvailability();
        long pId = permResp.getPermissionID();
        this.logger.debug("Received permission availability request with permission identifier=" + pId + '.');
        
        /* Response parameters. */
        CheckPermissionAvailabilityResponse resp = new CheckPermissionAvailabilityResponse();
        QueueType queue = new QueueType();
        resp.setCheckPermissionAvailabilityResponse(queue);
        queue.setViable(false);
        queue.setHasFree(false);
        queue.setIsQueuable(false);
        queue.setIsCodeAssignable(false);
        ResourceIDType resource = new ResourceIDType();
        queue.setQueuedResource(resource);
        resource.setType("NOTFOUND");
        
        if (!this.checkPermission(permResp))
        {
            this.logger.warn("Unable to check the resource permission because of invalid permission.");
            return resp;
        }
        
        ResourcePermissionDao dao = new ResourcePermissionDao();
        ResourcePermission perm = dao.get(pId);
        if (perm != null)
        {
            /* Queuable is based on the resource class. */
            queue.setIsQueuable(perm.getUserClass().isQueuable());
            
            String type = perm.getType();
            resource.setType(type);
            if (ResourcePermission.RIG_PERMISSION.equals(type))
            {
                /* Rig resource. */
                Rig rig = perm.getRig();
                resource.setResourceID(rig.getId().intValue());
                resource.setResourceName(rig.getName());

                queue.setHasFree(rig.isOnline() && !rig.isInSession());
                queue.setViable(rig.isOnline());
                
                /* Code assignable is defined by the rig type of the rig. */
                queue.setIsCodeAssignable(rig.getRigType().isCodeAssignable());
                
                /* Only one resource, the actual rig. */
                QueueTargetType target = new QueueTargetType();
                target.setViable(rig.isOnline());
                target.setIsFree(rig.isOnline() && !rig.isInSession());
                target.setResource(resource);
                queue.addQueueTarget(target);
                
            }
            else if (ResourcePermission.TYPE_PERMISSION.equals(type))
            {
                /* Rig type resource. */
                RigType rigType = perm.getRigType();
                resource.setResourceID(rigType.getId().intValue());
                resource.setResourceName(rigType.getName());
                queue.setIsCodeAssignable(rigType.isCodeAssignable());
                
                /* The targets are the rigs in the rig type. */
                for (Rig rig : rigType.getRigs())
                {
                    if (rig.isOnline()) queue.setViable(true);
                    if (rig.isOnline() && !rig.isInSession()) queue.setHasFree(true);
                    
                    QueueTargetType target = new QueueTargetType();
                    target.setViable(rig.isOnline());
                    target.setIsFree(rig.isOnline() && !rig.isInSession());
                    ResourceIDType resourceRig = new ResourceIDType();
                    resourceRig.setType(ResourcePermission.RIG_PERMISSION);
                    resourceRig.setResourceID(rig.getId().intValue());
                    resourceRig.setResourceName(rig.getName());
                    target.setResource(resourceRig);
                    queue.addQueueTarget(target);
                }
            }
            else if (ResourcePermission.CAPS_PERMISSION.equals(type))
            {
                /* Capabilities resource. */
                RequestCapabilities requestCaps = perm.getRequestCapabilities();
                resource.setResourceID(requestCaps.getId().intValue());
                resource.setResourceName(requestCaps.getCapabilities());
                
                /* For code assignable to be true, all rigs who match the
                 * request capabilities, must be code assignable. */
                queue.setIsCodeAssignable(true);
                
                /* Are all the rigs who have match rig capabilities to the
                 * request capabilities. */
                for (MatchingCapabilities match : requestCaps.getMatchingCapabilitieses())
                {
                    for (Rig capRig : match.getRigCapabilities().getRigs())
                    {
                        if (!capRig.getRigType().isCodeAssignable()) queue.setIsCodeAssignable(false);
                        
                        /* To be viable, only one rig needs to be online. */
                        if (capRig.isOnline()) queue.setViable(true);
                        
                        /* To be 'has free', only one rig needs to be free. */
                        if (capRig.isOnline() && !capRig.isInSession()) queue.setHasFree(true);
                        
                        /* Add target. */
                        QueueTargetType target = new QueueTargetType();
                        target.setViable(capRig.isOnline());
                        target.setIsFree(capRig.isOnline() && !capRig.isInSession());
                        queue.addQueueTarget(target);
                        ResourceIDType resTarget = new ResourceIDType();
                        resTarget.setType(ResourcePermission.RIG_PERMISSION);
                        resTarget.setResourceID(capRig.getId().intValue());
                        resTarget.setResourceName(capRig.getName());
                        target.setResource(resTarget);
                    }
                }
            }
        }
        else
        {
            this.logger.debug("Resource permission with ID " + pId + " not found.");
        }
        
        dao.closeSession();
        return resp;
    }

    

    public CheckResourceAvailabilityResponse checkResourceAvailability(final CheckResourceAvailability request)
    {
        //TODO : fill this with the necessary business logic
        throw new java.lang.UnsupportedOperationException("Please implement " + this.getClass().getName()
                + "#checkResourceAvailability");
    }

    
    
    /**
     * Gets the user identified by the user id type. 
     * 
     * @param uid user identity 
     * @param ses database session
     * @return user or null if not found
     */
    private User getUserFromUserID(UserIDType uid, org.hibernate.Session ses)
    {
        UserDao dao = new UserDao(ses);
        User user;
        long recordId = this.getIdentifier(uid.getUserID());
        String ns = uid.getUserNamespace(), nm = uid.getUserName();
        
        if (recordId > 0 && (user = dao.get(recordId)) != null)
        {
            return user;
        }
        else if (ns != null && nm != null && (user = dao.findByName(ns, nm)) != null)
        {
            return user;
        }
        
        return null;
    }
    
    /**
     * Converts string identifiers to a long.
     * 
     * @param idStr string containing a long  
     * @return long or 0 if identifier not valid
     */
    private long getIdentifier(String idStr)
    {
        if (idStr == null) return 0;
        
        try
        {
            return Long.parseLong(idStr);
        }
        catch (NumberFormatException nfe)
        {
            return 0;
        }
    }    
    
    /**
     * Checks whether the request has the specified permission. Currently this
     * is a stub and always return, irrespective of the provided user.
     * 
     * @return true if the request has the appropriate permission
     */
    private boolean checkPermission(OperationRequestType req)
    {
        // TODO Check request permissions for queuer
        return true;
    }
}
