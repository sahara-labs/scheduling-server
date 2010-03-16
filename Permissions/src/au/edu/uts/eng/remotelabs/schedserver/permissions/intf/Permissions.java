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

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserAssociationDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserClassDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserAssociation;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserAssociationId;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClass;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.permissions.impl.UserAdmin;
import au.edu.uts.eng.remotelabs.schedserver.permissions.impl.UserClassAdmin;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddAcademicPermission;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddAcademicPermissionResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddPermission;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddPermissionResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUser;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserAssociation;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserAssociationResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserClass;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserClassResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserLock;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserLockResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.BulkAddUserClassUsers;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.BulkAddUserClassUsersResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteAcademicPermission;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteAcademicPermissionResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeletePermission;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeletePermissionResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUser;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserAssociation;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserAssociationResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserClass;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserClassResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserLock;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserLockResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditAcademicPermission;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditAcademicPermissionResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditPermission;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditPermissionResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUser;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUserClass;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUserClassResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUserResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermission;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForAcademic;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForAcademicResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForUserClass;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForUserClassResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermission;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUser;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserClass;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserClassResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUser;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClass;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClasses;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassesForUser;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassesForUserResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassesResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUsersInUserClass;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUsersInUserClassResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.OperationRequestType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.OperationResponseType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.PermissionWithLockListType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.PersonaType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UnlockUserLock;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UnlockUserLockResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserAssociationType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserClassIDType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserClassType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserIDType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserLockType;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserType;

/**
 * The Permissions SOAP interface implementation.
 */
public class Permissions implements PermissionsSkeletonInterface
{
    /** Logger. */
    private Logger logger;
    
    public Permissions()
    {
        this.logger = LoggerActivator.getLogger();
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
    public AddUserResponse addUser(AddUser addUser)
    {
        /* Request parameters. */
        UserType userReq = addUser.getAddUser();
        String nm = userReq.getUserName(), ns = userReq.getUserNamespace(), persona = userReq.getPersona().getValue();
        this.logger.debug("Received request to add user with name=" + nm + ", namespace=" + ns
                + " and persona=" + persona + '.');
        
        /* Response parameters. */
        AddUserResponse resp = new AddUserResponse();
        OperationResponseType op = new OperationResponseType();
        op.setSuccessful(false);
        resp.setAddUserResponse(op);
        
        /* Check if the requestor is authorised. */
        if (!this.checkPermission(userReq))
        {
            op.setFailureCode(1);
            op.setFailureReason("Permission denied.");
        }
        /* Check the request parameters. */
        else if (nm == null || ns == null || persona == null)
        {
            op.setFailureCode(2);
            op.setFailureReason("Mandatory parameter not provided.");
        }
        else
        {
            UserAdmin admin = new UserAdmin(DataAccessActivator.getNewSession());
            if (admin.addUser(ns, nm, persona))
            {
                op.setSuccessful(true);
            }
            else
            {
                op.setFailureCode(3);
                op.setFailureReason(admin.getFailureReason());
            }
            admin.closeSession();
        }

        return resp;
    }
    
    @Override
    public EditUserResponse editUser(EditUser request)
    {
        /* Request parameters. */
        UserType userReq = request.getEditUser();
        String ns = userReq.getUserNamespace(), name = userReq.getUserName(), persona = userReq.getPersona().getValue();
        long id = this.getIdentifier(userReq.getUserID());
        this.logger.debug("Received request to edit user with id=" + id + ", namespace=" + ns + ", name=" + name 
                + ", persona=" + persona + '.');
        
        /* Response parameters. */
        EditUserResponse resp = new EditUserResponse();
        OperationResponseType op = new OperationResponseType();
        resp.setEditUserResponse(op);
        op.setSuccessful(false);
        
        UserAdmin userAdmin = new UserAdmin(DataAccessActivator.getNewSession());
        
        /* Check if the user is authorised. */
        if (!this.checkPermission(userReq))
        {
            op.setFailureCode(1);
            op.setFailureReason("Permission denied.");            
        }
        else if (id > 0)
        {
            if (userAdmin.editUser(id, ns, name, persona))
            {
                op.setSuccessful(true);
            }
            else
            {
                op.setFailureCode(3);
                op.setFailureReason(userAdmin.getFailureReason());
            }
        }
        else if (ns != null && name != null)
        {
            if (userAdmin.editUser(ns, name, persona))
            {
                op.setSuccessful(true);
            }
            else
            {
                op.setFailureCode(3);
                op.setFailureReason(userAdmin.getFailureReason());
            }
        }
        else
        {
            op.setFailureCode(2);
            op.setFailureReason("Mandatory parameter not provided.");
        }
        
        userAdmin.closeSession();
        return resp;
    }
    
    @Override
    public DeleteUserResponse deleteUser(DeleteUser request)
    {
        /* Request parameters. */
        UserIDType userReq = request.getDeleteUser();
        String ns = userReq.getUserNamespace(), name = userReq.getUserName();
        long id = this.getIdentifier(userReq.getUserID());
        this.logger.debug("Received delete user with id=" + id + ", namespace=" + ns + ", name=" + name + '.');
        
        /* Response parameters. */
        DeleteUserResponse resp = new DeleteUserResponse();
        OperationResponseType op = new OperationResponseType();
        resp.setDeleteUserResponse(op);
        op.setSuccessful(false);
        
        UserAdmin admin = new UserAdmin(DataAccessActivator.getNewSession());
        if (!this.checkPermission(userReq))
        {
            op.setFailureCode(1);
            op.setFailureReason("Permission denied");
        }
        else if (id != 0)
        {
            if (admin.deleteUser(id))
            {
                op.setSuccessful(true);
            }
            else
            {
                op.setFailureCode(3);
                op.setFailureReason(admin.getFailureReason());
            }
        }
        else if (ns != null && name != null)
        {
            if (admin.deleteUser(ns, name))
            {
                op.setSuccessful(true);
            }
            else
            {
                op.setFailureCode(3);
                op.setFailureReason(admin.getFailureReason());
            }
        }
        else
        {
            op.setFailureCode(2);
            op.setFailureReason("Mandatory parameter not supplied");
        }
        
        admin.closeSession();
        return resp;
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
            uc.setIsKickable(cls.isKickable());
            uc.setIsQueuable(cls.isQueuable());
            uc.setIsUserLockable(cls.isUsersLockable());
            uc.setPriority(cls.getPriority());
        }
            
        dao.closeSession();
        return resp;
    }
    
    @Override
    public AddUserClassResponse addUserClass(AddUserClass request)
    {
        /* Request parameters. */
        UserClassType ucReq = request.getAddUserClass();
        String name = ucReq.getUserClassName();
        int pri = ucReq.getPriority();
        this.logger.debug("Received add user request with name=" + name + ", priority=" + pri + ", active=" + 
                ucReq.getIsActive() + ", kickable=" + ucReq.getIsKickable() + ", queueable=" + ucReq.getIsQueuable() +
                ", lockable=" + ucReq.getIsUserLockable() + '.');
        
        /* Response parameters. */
        AddUserClassResponse resp = new AddUserClassResponse();
        OperationResponseType op = new OperationResponseType();
        resp.setAddUserClassResponse(op);
        op.setSuccessful(false);
        
        if (!this.checkPermission(ucReq))
        {
            op.setFailureCode(1);
            op.setFailureReason("Permission denied.");
        }
        else if (name == null)
        {
            op.setFailureCode(2);
            op.setFailureReason("Mandatory parameter not supplied.");
        }
        else
        {
            UserClassAdmin admin = new UserClassAdmin(DataAccessActivator.getNewSession());
            if (admin.addUserClass(name, pri, ucReq.getIsActive(), ucReq.getIsQueuable(), ucReq.getIsKickable(), 
                    ucReq.getIsUserLockable()))
            {
                op.setSuccessful(true);
            }
            else
            {
                op.setFailureCode(3);
                op.setFailureReason(admin.getFailureReason());
            }
            admin.closeSession();
        }
        
        return resp;
    }
    
    @Override
    public EditUserClassResponse editUserClass(EditUserClass request)
    {
        /* Request parameters. */
        UserClassType ucReq = request.getEditUserClass();
        String name = ucReq.getUserClassName();
        int id = ucReq.getUserClassID(), pri = ucReq.getPriority();
        this.logger.debug("Received edit user request with id=" + id + ", name=" + name + ", priority=" + pri + ", active=" + 
                ucReq.getIsActive() + ", kickable=" + ucReq.getIsKickable() + ", queueable=" + ucReq.getIsQueuable() +
                ", lockable=" + ucReq.getIsUserLockable() + '.');
        
        /* Response parameters. */
        EditUserClassResponse resp = new EditUserClassResponse();
        OperationResponseType op = new OperationResponseType();
        resp.setEditUserClassResponse(op);
        op.setSuccessful(false);
        
        UserClassAdmin admin = new UserClassAdmin(DataAccessActivator.getNewSession());
        if (!this.checkPermission(ucReq))
        {
            op.setFailureCode(1);
            op.setFailureReason("Permission denied.");
        }
        else if (id > 0)
        {
            if (admin.editUserClass(id, name, pri, ucReq.getIsActive(), ucReq.getIsQueuable(), ucReq.getIsKickable(), 
                    ucReq.getIsUserLockable()))
            {
                op.setSuccessful(true);
            }
            else
            {
                op.setFailureCode(3);
                op.setFailureReason(admin.getFailureReason());
            }
        }
        else if (name != null)
        {
            
            if (admin.editUserClass(name, pri, ucReq.getIsActive(), ucReq.getIsQueuable(), ucReq.getIsKickable(), 
                    ucReq.getIsUserLockable()))
            {
                op.setSuccessful(true);
            }
            else
            {
                op.setFailureCode(3);
                op.setFailureReason(admin.getFailureReason());
            }
        }
        else
        {
            op.setFailureCode(2);
            op.setFailureReason("Mandatory parameter not provided");
        }
        
        admin.closeSession();
        return resp;
    }
    
    @Override
    public DeleteUserClassResponse deleteUserClass(DeleteUserClass request)
    {
        /* Request parameters. */
        UserClassIDType usClRequest = request.getDeleteUserClass();
        String name = usClRequest.getUserClassName();
        int id = usClRequest.getUserClassID();
        this.logger.debug("Received delete user request with id=" + id + ", name=" + name + ".");
        
        /* Response parameters. */
        DeleteUserClassResponse resp = new DeleteUserClassResponse();
        OperationResponseType op = new OperationResponseType();
        resp.setDeleteUserClassResponse(op);
        op.setSuccessful(false);
        
        UserClassAdmin admin = new UserClassAdmin(DataAccessActivator.getNewSession());
        if (!this.checkPermission(usClRequest))
        {
            op.setFailureCode(1);
            op.setFailureReason("Permission denied");
        }
        else if (id > 0)
        {
            if (admin.deleteUserClass(id))
            {
                op.setSuccessful(true);
            }
            else
            {
                op.setFailureCode(3);
                op.setFailureReason(admin.getFailureReason());
            }
        }
        else if (name != null)
        {
            if (admin.deleteUserClass(name))
            {
                op.setSuccessful(true);
            }
            else
            {
                op.setFailureCode(3);
                op.setFailureReason(admin.getFailureReason());
            }
        }
        else
        {
            op.setFailureCode(2);
            op.setFailureReason("Mandatory parameter not provided");
        }
        
        admin.closeSession();
        return resp;
    }
    
    @Override
    public AddUserAssociationResponse addUserAssociation(AddUserAssociation request)
    {
        /* Request parameters. */
        UserAssociationType assocReq = request.getAddUserAssociation();
        UserIDType uId = assocReq.getUser();
        UserClassIDType uClsId = assocReq.getUserClass();
        String unm = uId.getUserName(), uns = uId.getUserNamespace(), cnm = uClsId.getUserClassName();
        long userId = 0, classId = uClsId.getUserClassID();
        try
        {
            userId = Integer.parseInt(uId.getUserID());
        }
        catch (NumberFormatException ex) { /* Not going to use ID them. */ }
        this.logger.debug("Received add user association request for user id=" + userId + ", user name=" + unm +
                ", user namespace" + uns + ", class id=" + classId + ", class name=" + cnm + ".");
        
        /* Response parameters. */
        AddUserAssociationResponse resp = new AddUserAssociationResponse();
        OperationResponseType op = new OperationResponseType();
        resp.setAddUserAssociationResponse(op);
        op.setSuccessful(false);
        
        UserClassDao classDao = new UserClassDao();
        UserDao userDao = new UserDao(classDao.getSession());
        UserAssociationDao assocDao = new UserAssociationDao(classDao.getSession());
        User user;
        UserClass userClass;
        if (!this.checkPermission(assocReq))
        {
            op.setFailureCode(1);
            op.setFailureReason("Permission denied");
        }
        else if (((userId > 0 && (user = userDao.get(userId)) != null) || // Load user from ID
                 (uns != null && unm != null && (user = userDao.findByName(uns, unm)) != null)) && // Load user from namespace & name
                ((classId > 0 && (userClass = classDao.get(classId)) != null) || // Load class from ID
                 (cnm != null && (userClass = classDao.findByName(cnm)) != null))) // Load class from name
        {
            UserAssociationId assocId = new UserAssociationId(user.getId(), userClass.getId());
            if (assocDao.get(assocId) == null)
            {
                UserAssociation assoc = new UserAssociation();
                assoc.setId(assocId);
                assoc.setUser(user);
                assoc.setUserClass(userClass);
                assocDao.persist(assoc);
                op.setSuccessful(true);
            }
            else
            {
                op.setFailureCode(3);
                op.setFailureReason("Association already exists.");
            }
        }
        else
        {
            op.setFailureCode(2);
            op.setFailureReason("A mandatory parameter was not provided");
        }
        
        classDao.closeSession();
        return resp;
    }
    
    @Override
    public DeleteUserAssociationResponse deleteUserAssociation(DeleteUserAssociation request)
    {
        /* Request parameters. */
        UserAssociationType assocReq = request.getDeleteUserAssociation();
        UserIDType uId = assocReq.getUser();
        UserClassIDType uClsId = assocReq.getUserClass();
        String unm = uId.getUserName(), uns = uId.getUserNamespace(), cnm = uClsId.getUserClassName();
        long userId = 0, classId = uClsId.getUserClassID();
        try
        {
            userId = Integer.parseInt(uId.getUserID());
        }
        catch (NumberFormatException ex) { /* Not going to use ID them. */ }
        this.logger.debug("Received delete user association request for user id=" + userId + ", user name=" + unm +
                ", user namespace" + uns + ", class id=" + classId + ", class name=" + cnm + ".");
        
        /* Response parameters. */
        DeleteUserAssociationResponse resp = new DeleteUserAssociationResponse();
        OperationResponseType op = new OperationResponseType();
        resp.setDeleteUserAssociationResponse(op);
        op.setSuccessful(false);
        
        UserClassDao classDao = new UserClassDao();
        UserDao userDao = new UserDao(classDao.getSession());
        UserAssociationDao assocDao = new UserAssociationDao(classDao.getSession());
        User user;
        UserClass userClass;
        if (!this.checkPermission(assocReq))
        {
            op.setFailureCode(1);
            op.setFailureReason("Permission denied");
        }
        else if (((userId > 0 && (user = userDao.get(userId)) != null) || // Load user from ID
                  (uns != null && unm != null && (user = userDao.findByName(uns, unm)) != null)) && // Load user from namespace & name
                 ((classId > 0 && (userClass = classDao.get(classId)) != null) || // Load class from ID
                  (cnm != null && (userClass = classDao.findByName(cnm)) != null))) // Load class from name
        {
            assocDao.delete(new UserAssociationId(user.getId(), userClass.getId()));
            op.setSuccessful(true);
        }
        else
        {
            op.setFailureCode(2);
            op.setFailureReason("A mandatory parameter was not provided");
        }
        
        classDao.closeSession();
        return resp;
    }

    
    /* (non-Javadoc)
     * @see au.edu.uts.eng.remotelabs.schedserver.permissions.intf.PermissionsSkeletonInterface#addAcademicPermission(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddAcademicPermission)
     */
    @Override
    public AddAcademicPermissionResponse addAcademicPermission(AddAcademicPermission request)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see au.edu.uts.eng.remotelabs.schedserver.permissions.intf.PermissionsSkeletonInterface#addPermission(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddPermission)
     */
    @Override
    public AddPermissionResponse addPermission(AddPermission request)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see au.edu.uts.eng.remotelabs.schedserver.permissions.intf.PermissionsSkeletonInterface#addUserLock(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserLock)
     */
    @Override
    public AddUserLockResponse addUserLock(AddUserLock request)
    {
        /* Request parameters. */
        UserLockType lock = request.getAddUserLock();
        
        /* Response parameters. */
        return null;
    }

    /* (non-Javadoc)
     * @see au.edu.uts.eng.remotelabs.schedserver.permissions.intf.PermissionsSkeletonInterface#bulkAddUserClassUsers(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.BulkAddUserClassUsers)
     */
    @Override
    public BulkAddUserClassUsersResponse bulkAddUserClassUsers(BulkAddUserClassUsers request)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see au.edu.uts.eng.remotelabs.schedserver.permissions.intf.PermissionsSkeletonInterface#deleteAcademicPermission(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteAcademicPermission)
     */
    @Override
    public DeleteAcademicPermissionResponse deleteAcademicPermission(DeleteAcademicPermission request)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see au.edu.uts.eng.remotelabs.schedserver.permissions.intf.PermissionsSkeletonInterface#deletePermission(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeletePermission)
     */
    @Override
    public DeletePermissionResponse deletePermission(DeletePermission request)
    {
        // TODO Auto-generated method stub
        return null;
    }
    

    /* (non-Javadoc)
     * @see au.edu.uts.eng.remotelabs.schedserver.permissions.intf.PermissionsSkeletonInterface#deleteUserLock(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserLock)
     */
    @Override
    public DeleteUserLockResponse deleteUserLock(DeleteUserLock request)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see au.edu.uts.eng.remotelabs.schedserver.permissions.intf.PermissionsSkeletonInterface#editAcademicPermission(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditAcademicPermission)
     */
    @Override
    public EditAcademicPermissionResponse editAcademicPermission(EditAcademicPermission request)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see au.edu.uts.eng.remotelabs.schedserver.permissions.intf.PermissionsSkeletonInterface#editPermission(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditPermission)
     */
    @Override
    public EditPermissionResponse editPermission(EditPermission request)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see au.edu.uts.eng.remotelabs.schedserver.permissions.intf.PermissionsSkeletonInterface#getAcademicPermission(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermission)
     */
    @Override
    public GetAcademicPermissionResponse getAcademicPermission(GetAcademicPermission request)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see au.edu.uts.eng.remotelabs.schedserver.permissions.intf.PermissionsSkeletonInterface#getAcademicPermissionsForAcademic(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForAcademic)
     */
    @Override
    public GetAcademicPermissionsForAcademicResponse getAcademicPermissionsForAcademic(
            GetAcademicPermissionsForAcademic request)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see au.edu.uts.eng.remotelabs.schedserver.permissions.intf.PermissionsSkeletonInterface#getAcademicPermissionsForUserClass(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForUserClass)
     */
    @Override
    public GetAcademicPermissionsForUserClassResponse getAcademicPermissionsForUserClass(
            GetAcademicPermissionsForUserClass request)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see au.edu.uts.eng.remotelabs.schedserver.permissions.intf.PermissionsSkeletonInterface#getPermission(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermission)
     */
    @Override
    public GetPermissionResponse getPermission(GetPermission request)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GetPermissionsForUserResponse getPermissionsForUser(GetPermissionsForUser request)
    {
        /* Request parameters. */
        UserIDType uid = request.getGetPermissionsForUser();
        String ns = uid.getUserNamespace(), nm = uid.getUserName();
        long id = 0;
        try
        {
            id = Long.parseLong(uid.getUserID());
        }
        catch (NumberFormatException ex) { /* Not using id then. */ }
        this.logger.debug("Received get permissions for user with id=" + id + ", namespace=" + ns + ", name=" + nm + '.');
        
        /* Response parameters. */
        GetPermissionsForUserResponse resp = new GetPermissionsForUserResponse();
        PermissionWithLockListType permList = new PermissionWithLockListType();
        resp.setGetPermissionsForUserResponse(permList);
        
        // TODO
        
        return resp;
    }

    /* (non-Javadoc)
     * @see au.edu.uts.eng.remotelabs.schedserver.permissions.intf.PermissionsSkeletonInterface#getPermissionsForUserClass(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserClass)
     */
    @Override
    public GetPermissionsForUserClassResponse getPermissionsForUserClass(GetPermissionsForUserClass request)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see au.edu.uts.eng.remotelabs.schedserver.permissions.intf.PermissionsSkeletonInterface#getUserClasses(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClasses)
     */
    @Override
    public GetUserClassesResponse getUserClasses(GetUserClasses request)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see au.edu.uts.eng.remotelabs.schedserver.permissions.intf.PermissionsSkeletonInterface#getUserClassesForUser(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassesForUser)
     */
    @Override
    public GetUserClassesForUserResponse getUserClassesForUser(GetUserClassesForUser request)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see au.edu.uts.eng.remotelabs.schedserver.permissions.intf.PermissionsSkeletonInterface#getUsersInUserClass(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUsersInUserClass)
     */
    @Override
    public GetUsersInUserClassResponse getUsersInUserClass(GetUsersInUserClass request)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see au.edu.uts.eng.remotelabs.schedserver.permissions.intf.PermissionsSkeletonInterface#unlockUserLock(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UnlockUserLock)
     */
    @Override
    public UnlockUserLockResponse unlockUserLock(UnlockUserLock request)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Checks whether the request has the specified permission.
     */
    private boolean checkPermission(OperationRequestType req)
    {
        // TODO check request permissions. */
        return true;
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
