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
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.permissions.impl.UserAdmin;
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
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UnlockUserLock;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UnlockUserLockResponse;
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
            if (admin.addUser(nm, ns, persona))
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

    /* (non-Javadoc)
     * @see au.edu.uts.eng.remotelabs.schedserver.permissions.intf.PermissionsSkeletonInterface#addUserAssociation(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserAssociation)
     */
    @Override
    public AddUserAssociationResponse addUserAssociation(AddUserAssociation request)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see au.edu.uts.eng.remotelabs.schedserver.permissions.intf.PermissionsSkeletonInterface#addUserClass(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.AddUserClass)
     */
    @Override
    public AddUserClassResponse addUserClass(AddUserClass request)
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
        // TODO Auto-generated method stub
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
     * @see au.edu.uts.eng.remotelabs.schedserver.permissions.intf.PermissionsSkeletonInterface#deleteUser(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUser)
     */
    @Override
    public DeleteUserResponse deleteUser(DeleteUser request)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see au.edu.uts.eng.remotelabs.schedserver.permissions.intf.PermissionsSkeletonInterface#deleteUserAssociation(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserAssociation)
     */
    @Override
    public DeleteUserAssociationResponse deleteUserAssociation(DeleteUserAssociation request)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see au.edu.uts.eng.remotelabs.schedserver.permissions.intf.PermissionsSkeletonInterface#deleteUserClass(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.DeleteUserClass)
     */
    @Override
    public DeleteUserClassResponse deleteUserClass(DeleteUserClass request)
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

    @Override
    public EditUserResponse editUser(EditUser request)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see au.edu.uts.eng.remotelabs.schedserver.permissions.intf.PermissionsSkeletonInterface#editUserClass(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.EditUserClass)
     */
    @Override
    public EditUserClassResponse editUserClass(EditUserClass request)
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

    /* (non-Javadoc)
     * @see au.edu.uts.eng.remotelabs.schedserver.permissions.intf.PermissionsSkeletonInterface#getPermissionsForUser(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUser)
     */
    @Override
    public GetPermissionsForUserResponse getPermissionsForUser(GetPermissionsForUser request)
    {
        // TODO Auto-generated method stub
        return null;
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
     * @see au.edu.uts.eng.remotelabs.schedserver.permissions.intf.PermissionsSkeletonInterface#getUser(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUser)
     */
    @Override
    public GetUserResponse getUser(GetUser request)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see au.edu.uts.eng.remotelabs.schedserver.permissions.intf.PermissionsSkeletonInterface#getUserClass(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClass)
     */
    @Override
    public GetUserClassResponse getUserClass(GetUserClass request)
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
}
