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
 * @date 12th March 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.permissions.intf;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.hibernate.Session;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.ResourcePermissionDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserClassDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserLockDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RequestCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserAssociation;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClass;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserLock;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermission;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForAcademic;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForAcademicResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermission;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUser;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserClass;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserClassResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUser;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClass;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassesForUser;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassesForUserResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.OperationRequestType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.OperationResponseType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.PermissionType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.PermissionWithLockListType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.PermissionWithLockType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.PersonaType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.ResourceClass;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.ResourceIDType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UnlockUserLock;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UnlockUserLockResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserClassIDType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserClassType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserIDType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserLockIDUserPermSequence;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserLockType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserType;

/**
 * The Permissions SOAP interface implementation.
 */
public class PermissionsSOAPImpl implements PermissionsSOAP
{
    /** Logger. */
    private Logger logger;
    
    public PermissionsSOAPImpl()
    {
        this.logger = LoggerActivator.getLogger();
    }
    
    @Override
    public GetUserClassResponse getUserClass(GetUserClass request)
    {
        /* Request parameters. */
        UserClassIDType uCReq = request.getGetUserClass();
        String name = uCReq.getUserClassName();
        long id = uCReq.getUserClassID();
        this.logger.debug("Received get user class request with id=" + id + ", name=" + name + '.');
        
        /* Response parameters. */
        GetUserClassResponse resp = new GetUserClassResponse();
        UserClassType uc = new UserClassType();
        resp.setGetUserClassResponse(uc);
        
        UserClassDao dao = new UserClassDao();
        UserClass cls;
        if ((id > 0 && (cls = dao.get(id)) != null) || (name != null && (cls = dao.findByName(name)) != null))
        {
            uc.setUserClassID(cls.getId().intValue());
            uc.setUserClassName(cls.getName());
            uc.setIsActive(cls.isActive());
            uc.setIsBookable(cls.isBookable());
            uc.setIsKickable(cls.isKickable());
            uc.setIsQueuable(cls.isQueuable());
            uc.setIsUserLockable(cls.isUsersLockable());
            uc.setPriority(cls.getPriority());
            uc.setTimeHorizon(cls.getTimeHorizon());
        }
            
        dao.closeSession();
        return resp;
    }
    
    @Override
    public GetPermissionResponse getPermission(GetPermission request)
    {
        /* Request parameters. */
        int pid = request.getGetPermission().getPermissionID();
        this.logger.debug("Received get permission request for permission id=" + pid + '.');
        
        /* Response paramters. */
        GetPermissionResponse response = new GetPermissionResponse();
        PermissionType permission = new PermissionType();
        permission.setPermissionID(0);
        
        UserClassIDType userClass = new UserClassIDType();
        permission.setUserClass(userClass);
        
        permission.setResourceClass(ResourceClass.RIG);
        ResourceIDType resource = new ResourceIDType();
        permission.setResource(resource);
        
        permission.setCanBook(false);
        permission.setCanQueue(false);
        
        response.setGetPermissionResponse(permission);
        
        /* Load the permission. */
        ResourcePermissionDao dao = new ResourcePermissionDao();
        try
        {
            ResourcePermission rp = dao.get((long)pid);
            if (rp == null)
            {
                this.logger.info("Resource permission with identifer '" + pid + "' not found.");
                return response;
            }
            
            permission.setPermissionID(rp.getId().intValue());
            
            /* User class details . */
            UserClass uc = rp.getUserClass();
            userClass.setUserClassID(uc.getId().intValue());
            userClass.setUserClassName(uc.getName());
            permission.setCanBook(uc.isBookable());
            permission.setCanQueue(uc.isQueuable());
            permission.setTimeHorizon(uc.getTimeHorizon());
            
            /* Resource details. */
            if (ResourcePermission.RIG_PERMISSION.equals(rp.getType()))
            {
                Rig rig = rp.getRig();
                if (rig == null)
                {
                    this.logger.warn("Incorrect configuration of a rig resource permission with id " + rp.getId() + 
                            ", as no rig is set.");
                }
                else
                {
                    permission.setResourceClass(ResourceClass.RIG);
                    resource.setResourceID(rig.getId().intValue());
                    resource.setResourceName(rig.getName());
                }
            }
            else if (ResourcePermission.TYPE_PERMISSION.equals(rp.getType()))
            {
                RigType rigType = rp.getRigType();
                if (rigType == null)
                {
                    this.logger.warn("Incorrect configuration of a rig type resource permission with id " + 
                            rp.getId() + ", as no rig type is set.");                    
                }
                else
                {
                    permission.setResourceClass(ResourceClass.TYPE);
                    resource.setResourceID(rigType.getId().intValue());
                    resource.setResourceName(rigType.getName());
                }
            }
            else if (ResourcePermission.CAPS_PERMISSION.equals(rp.getType()))
            {
                RequestCapabilities caps = rp.getRequestCapabilities();
                if (caps == null)
                {
                    this.logger.warn("Incorrect configuration of a request capabilities resource permission with id " +
                            rp.getId() + ", as no request capabilities are set.");
                }
                else
                {
                    permission.setResourceClass(ResourceClass.CAPABILITY);
                    resource.setResourceID(caps.getId().intValue());
                    resource.setResourceName(caps.getCapabilities());
                }
            }
            else
            {
                this.logger.warn("Incorrect configuration of a resource permission with id " + rp.getId() + 
                        ". It has an unknown resource type " + rp.getType() + ". It should be one of " +
                        "'RIG', 'TYPE', 'CAPABILITY' or 'CONSUMER'.");
            }
            
            /* Other resource permission details. */
            permission.setMaxBookings(rp.getMaximumBookings());
            permission.setSessionDuration(rp.getSessionDuration());
            permission.setExtensionDuration(rp.getExtensionDuration());
            permission.setAllowedExtensions(rp.getAllowedExtensions());
            permission.setQueueActivityTmOut(rp.getQueueActivityTimeout());
            permission.setSessionActivityTmOut(rp.getSessionActivityTimeout());
            permission.setDisplayName(rp.getDisplayName());
            
            Calendar start = Calendar.getInstance();
            start.setTime(rp.getStartTime());
            permission.setStart(start);
            
            Calendar expiry = Calendar.getInstance();
            expiry.setTime(rp.getExpiryTime());
            permission.setExpiry(expiry);
            
            return response;
        }
        finally
        {
            dao.closeSession();
        }
    }

    @Override
    public GetUserResponse getUser(GetUser request)
    {
        /** Request parameters. */
        UserIDType userReq = request.getGetUser();
        String ns = userReq.getUserNamespace(), nm = userReq.getUserName();
        long id = this.getIdentifier(userReq.getUserID());
        this.logger.debug("Received get user request with id=" + id + ", namespace=" + ns + " and name=" + nm + '.');
        
        /** Response parameters. */
        GetUserResponse resp = new GetUserResponse();
        UserType userResp = new UserType();
        resp.setGetUserResponse(userResp);
        userResp.setPersona(PersonaType.NOTFOUND);
        
        UserDao dao = new UserDao();
        User user;
        if (id > 0 && (user = dao.get(id)) != null)
        {
            userResp.setUserID(String.valueOf(user.getId()));
            userResp.setNameNamespace(user.getNamespace(), user.getName());
            userResp.setUserQName(user.getNamespace() + UserIDType.QNAME_DELIM + user.getName());
            userResp.setPersona(PersonaType.Factory.fromValue(user.getPersona()));
        }
        else if (ns != null && nm != null && (user = dao.findByName(ns, nm)) != null)
        {
            userResp.setUserID(String.valueOf(user.getId()));
            userResp.setNameNamespace(user.getNamespace(), user.getName());
            userResp.setUserQName(user.getNamespace() + UserIDType.QNAME_DELIM + user.getName());
            userResp.setPersona(PersonaType.Factory.fromValue(user.getPersona()));
        }
        
        dao.closeSession();
        return resp;
    }

    @Override
    public GetAcademicPermissionResponse getAcademicPermission(GetAcademicPermission request)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public GetPermissionsForUserClassResponse getPermissionsForUserClass(GetPermissionsForUserClass request)
    {
        // TODO Auto-generated method stub.
        return null;
    }
    
    @Override
    public GetPermissionsForUserResponse getPermissionsForUser(GetPermissionsForUser request)
    {
        /* Request parameters. */
        UserIDType uid = request.getGetPermissionsForUser();
        String ns = uid.getUserNamespace(), nm = uid.getUserName();
        long id = this.getIdentifier(uid.getUserID());
        this.logger.debug("Received get permissions request for user with id=" + id + ", namespace=" + ns + ", name=" + nm + '.');
        
        /* Response parameters. */
        GetPermissionsForUserResponse resp = new GetPermissionsForUserResponse();
        PermissionWithLockListType permList = new PermissionWithLockListType();
        resp.setGetPermissionsForUserResponse(permList);
        
        /* 1) Load user. */
        UserDao userDao = new UserDao();
        User user = null;
        if (id > 0) user = userDao.get(id);
        else if (ns != null && nm != null) user = userDao.findByName(ns, nm);

        if (user == null)
        {
            this.logger.debug("User not found for getting permissions, id is " + id + ", namespce is " + ns + 
                    " and name is " + nm + '.');
            userDao.closeSession();
            return resp;
        }
        
        /* Get a list of resources that are locked. */
        List<Long> lockedResources = new ArrayList<Long>();
        for (UserLock lock : user.getUserLocks())
        {
            if (lock.isIsLocked()) lockedResources.add(lock.getResourcePermission().getId());
        }
        
        /* For each of the user classes the user is a member of, add all its resource permissions. */
        for (UserAssociation assoc : user.getUserAssociations())
        {
            UserClass userClass = assoc.getUserClass();
            if (!userClass.isActive()) continue;
            for (ResourcePermission resPerm : userClass.getResourcePermissions())
            {
                PermissionWithLockType permWithLock = new PermissionWithLockType();
                PermissionType perm = new PermissionType();
                perm.setPermissionID(resPerm.getId().intValue());
                permWithLock.setPermission(perm);

                /* Add user class. */
                UserClassIDType userClassIdType = new UserClassIDType();
                userClassIdType.setUserClassID(userClass.getId().intValue());
                userClassIdType.setUserClassName(userClass.getName());
                perm.setUserClass(userClassIdType);
                
                /* Add resource. */
                ResourceIDType resourceIdType = new ResourceIDType();
                perm.setResource(resourceIdType);
                if (ResourcePermission.RIG_PERMISSION.equals(resPerm.getType()))
                {
                    Rig rig = resPerm.getRig();
                    if (rig == null)
                    {
                        this.logger.warn("Incorrect configuration of a rig resource permission with " +
                                "id " + resPerm.getId() + ", as no rig is set.");
                        continue;
                    }
                    
                    perm.setResourceClass(ResourceClass.RIG);
                    resourceIdType.setResourceID(rig.getId().intValue());
                    resourceIdType.setResourceName(rig.getName());
                }
                else if (ResourcePermission.TYPE_PERMISSION.equals(resPerm.getType()))
                {
                    RigType rigType = resPerm.getRigType();
                    if (rigType == null)
                    {
                        this.logger.warn("Incorrect configuration of a rig type resource permission with " +
                                "id " + resPerm.getId() + ", as no rig type is set.");
                        continue;
                    }
                    
                    perm.setResourceClass(ResourceClass.TYPE);
                    resourceIdType.setResourceID(rigType.getId().intValue());
                    resourceIdType.setResourceName(rigType.getName());
                }
                else if (ResourcePermission.CAPS_PERMISSION.equals(resPerm.getType()))
                {
                    RequestCapabilities caps = resPerm.getRequestCapabilities();
                    if (caps == null)
                    {
                        this.logger.warn("Incorrect configuration of a request capabilities resource permission with " +
                                "id " + resPerm.getId() + ", as no request capabilities are set.");
                        continue;
                    }
                    perm.setResourceClass(ResourceClass.CAPABILITY);
                    resourceIdType.setResourceID(caps.getId().intValue());
                    resourceIdType.setResourceName(caps.getCapabilities());
                }
                else
                {
                    this.logger.warn("Incorrect configuration of a resource permission with id " + resPerm.getId() + 
                            ". It has an unknown resource type " + resPerm.getType() + ". It should be one of " +
                            "'RIG', 'TYPE', 'CONSUMER' or 'CAPABILITY'.");
                }

                /* Add information about permission. */
                perm.setCanBook(userClass.isBookable());
                perm.setCanQueue(userClass.isQueuable());
                perm.setTimeHorizon(userClass.getTimeHorizon());
                perm.setMaxBookings(resPerm.getMaximumBookings());
                perm.setSessionDuration(resPerm.getSessionDuration());
                perm.setExtensionDuration(resPerm.getExtensionDuration());
                perm.setAllowedExtensions(resPerm.getAllowedExtensions());
                perm.setQueueActivityTmOut(resPerm.getQueueActivityTimeout());
                perm.setSessionActivityTmOut(resPerm.getSessionActivityTimeout());
                
                Calendar start = Calendar.getInstance();
                start.setTime(resPerm.getStartTime());
                perm.setStart(start);
                Calendar expiry = Calendar.getInstance();
                expiry.setTime(resPerm.getExpiryTime());
                perm.setExpiry(expiry);
                perm.setDisplayName(resPerm.getDisplayName());
                
                /* Add if the resource permission is locked. */
                permWithLock.setIsLocked(lockedResources.contains(resPerm.getId()));
                permList.addPermission(permWithLock);
            }
        }
        
        userDao.closeSession();
        return resp;
    }

    @Override
    public GetUserClassesForUserResponse getUserClassesForUser(GetUserClassesForUser request)
    {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public GetAcademicPermissionsForAcademicResponse getAcademicPermissionsForAcademic(GetAcademicPermissionsForAcademic request)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UnlockUserLockResponse unlockUserLock(UnlockUserLock request)
    {
        /* Request parameters. */
        UserLockType lockReq = request.getUnlockUserLock();
        String key = lockReq.getLockKey();
        int lockId = lockReq.getUserLockID();
        UserLockIDUserPermSequence seq = lockReq.getUserIDPermissionsSequence();
        
        /* Response parameters. */
        UnlockUserLockResponse resp = new UnlockUserLockResponse();
        OperationResponseType op = new OperationResponseType();
        resp.setUnlockUserLockResponse(op);
        op.setSuccessful(false);
        
        UserLockDao dao = new UserLockDao();
        if (!this.checkPermission(lockReq))
        {
            this.logger.warn("Failed unlocking user permission because the requestor does not have permission.");
            op.setFailureCode(1);
            op.setFailureReason("Permission denied.");
        }
        else if (key == null)
        {
            this.logger.warn("Failed unlocking user lock because the no lock key was supplied.");
            op.setFailureCode(2);
            op.setFailureReason("Mandatory parameter not provided.");
        }
        else if (lockId > 0)
        {
            this.logger.debug("Received unlock user lock request with lock identifier=" + lockId + ", lock key=" + key + '.');
            UserLock lock = dao.get(Long.valueOf(lockId));
            if (lock == null)
            {
                this.logger.warn("Fail unlocking user lock (id=" + lockId + ") because the lock was not found.");
                op.setFailureCode(3);
                op.setFailureReason("User lock not found.");
            }
            else if (lock.getLockKey().equals(key))
            {
                lock.setIsLocked(false);
                dao.flush();
                op.setSuccessful(true);
            }
            else
            {
                this.logger.warn("Fail unlocking user lock because the supplied lock key was incorrect " +
                		"(supplied=" + key + ", actual=" + lock.getLockKey() + ").");
                op.setFailureCode(3);
                op.setFailureReason("Provided key does not match.");
            }
        }
        else if (seq != null)
        {
            UserIDType uid = seq.getUserID();
            long pId = seq.getPermissionID().getPermissionID();
            this.logger.debug("Received unlock user lock request with permission id=" + seq.getPermissionID().getPermissionID() +
                        ", user id=" + uid.getUserID() + ", user namespace= " + uid.getUserNamespace() + 
                        ", user name=" + uid.getUserName()  + ", lock key=" + key + '.');

            User user = this.getUserFromUserID(seq.getUserID(), dao.getSession());
            ResourcePermission perm = new ResourcePermissionDao(dao.getSession()).get(pId);
            UserLock lock;
            
            if (user == null || perm == null || (lock = dao.findLock(user, perm)) == null)
            {                
                this.logger.warn("Fail unlocking user lock (permission id=" + seq.getPermissionID().getPermissionID() +
                        ", user id= " + uid.getUserID() + ", user namespace= " + uid.getUserNamespace() + 
                        ", user name=" + uid.getUserName()  + ") because the lock was not found.");
                op.setFailureCode(3);
                op.setFailureReason("User lock not found.");
            }
            else if (lock.getLockKey().equals(key))
            {
                lock.setIsLocked(false);
                dao.flush();
                op.setSuccessful(true);
            }
            else
            {
                this.logger.warn("Fail unlocking user lock because the supplied lock key was incorrect " +
                		"(supplied=" + key + ", actual=" + lock.getLockKey() + ").");
                op.setFailureCode(3);
                op.setFailureReason("Provided key does not match.");
            }    
        }
        else
        {
            this.logger.warn("Failed unlocking user lock because the no lock key was supplied.");
            op.setFailureCode(2);
            op.setFailureReason("Mandatory parameter not provided.");
        }
        
        dao.closeSession();
        return resp;
    }

    /**
     * Checks whether the request has the specified permission.
     */
    private boolean checkPermission(OperationRequestType req)
    {
        // TODO check request permissions.
        return true;
    }
    
    /**
     * Gets the user identified by the user id type. 
     * 
     * @param uid user identity 
     * @param ses database session
     * @return user or null if not found
     */
    private User getUserFromUserID(UserIDType uid, Session ses)
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
}
