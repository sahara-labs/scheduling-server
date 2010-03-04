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
 * @date 3rd March 2009
 */

package au.edu.uts.eng.remotelabs.schedserver.permissions.intf;

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
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UnlockUserLock;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UnlockUserLockResponse;

/**
 * PermissionsSkeleton, skeleton class for the Permissions service.
 */
public class PermissionsSkeleton implements PermissionsSkeletonInterface
{
    public AddUserLockResponse addUserLock(final AddUserLock request)
    {
        throw new UnsupportedOperationException("Called " + this.getClass().getName() + "#addUserLock");
    }

    public AddPermissionResponse addPermission(final AddPermission request)
    {
        throw new UnsupportedOperationException("Called " + this.getClass().getName() + "#addPermission");
    }

    public AddUserResponse addUser(final AddUser request)
    {
        throw new UnsupportedOperationException("Called " + this.getClass().getName() + "#addUser");
    }

    public DeleteUserResponse deleteUser(final DeleteUser request)
    {
        throw new UnsupportedOperationException("Called " + this.getClass().getName() + "#deleteUser");
    }
    public GetUserClassesForUserResponse getUserClassesForUser(final GetUserClassesForUser request)
    {
        throw new UnsupportedOperationException("Called " + this.getClass().getName() + "#getUserClassesForUser");
    }

    public DeleteAcademicPermissionResponse deleteAcademicPermission(
            final DeleteAcademicPermission request)
    {
        throw new UnsupportedOperationException("Called " + this.getClass().getName() + "#deleteAcademicPermission");
    }

    public GetAcademicPermissionsForAcademicResponse getAcademicPermissionsForAcademic(
            final GetAcademicPermissionsForAcademic request)
    {
        throw new UnsupportedOperationException("Called " + this.getClass().getName() 
                + "#getAcademicPermissionsForAcademic");
    }

    public DeleteUserAssociationResponse deleteUserAssociation(final DeleteUserAssociation request)
    {
        throw new UnsupportedOperationException("Called " + this.getClass().getName() + "#deleteUserAssociation");
    }

    public GetPermissionsForUserClassResponse getPermissionsForUserClass(
            final GetPermissionsForUserClass request)
    {
        throw new UnsupportedOperationException("Called " + this.getClass().getName() + "#getPermissionsForUserClass");
    }

    public EditPermissionResponse editPermission(final EditPermission request)
    {
        throw new UnsupportedOperationException("Called " + this.getClass().getName() + "#editPermission");
    }

    public EditUserClassResponse editUserClass(final EditUserClass request)
    {
        throw new UnsupportedOperationException("Called " + this.getClass().getName() + "#editUserClass");
    }

    public DeleteUserLockResponse deleteUserLock(final DeleteUserLock request)
    {
        throw new UnsupportedOperationException("Called " + this.getClass().getName() + "#deleteUserLock");
    }

    public EditAcademicPermissionResponse editAcademicPermission(final EditAcademicPermission request)
    {
        throw new UnsupportedOperationException("Called " + this.getClass().getName() + "#editAcademicPermission");
    }

    public DeleteUserClassResponse deleteUserClass(final DeleteUserClass request)
    {
        throw new UnsupportedOperationException("Called " + this.getClass().getName() + "#deleteUserClass");
    }

    public AddUserClassResponse addUserClass(final AddUserClass request)
    {
        throw new UnsupportedOperationException("Called " + this.getClass().getName() + "#addUserClass");
    }

    public DeletePermissionResponse deletePermission(final DeletePermission request)
    {
        throw new UnsupportedOperationException("Called " + this.getClass().getName() + "#deletePermission");
    }

    public GetPermissionsForUserResponse getPermissionsForUser(final GetPermissionsForUser request)
    {
        throw new UnsupportedOperationException("Called " + this.getClass().getName() + "#getPermissionsForUser");
    }


    public GetUsersInUserClassResponse getUsersInUserClass(final GetUsersInUserClass request)
    {
        throw new UnsupportedOperationException("Called " + this.getClass().getName() + "#getUsersInUserClass");
    }

    public AddUserAssociationResponse addUserAssociation(final AddUserAssociation request)
    {
        throw new UnsupportedOperationException("Called " + this.getClass().getName() + "#addUserAssociation");
    }

    public GetUserResponse getUser(final GetUser request)
    {
        throw new UnsupportedOperationException("Called " + this.getClass().getName() + "#getUser");
    }

    public GetAcademicPermissionResponse getAcademicPermission(final GetAcademicPermission request)
    {
        throw new UnsupportedOperationException("Called " + this.getClass().getName() + "#getAcademicPermission");
    }

    public AddAcademicPermissionResponse addAcademicPermission(final AddAcademicPermission request)
    {
        throw new UnsupportedOperationException("Called " + this.getClass().getName() + "#addAcademicPermission");
    }

    public UnlockUserLockResponse unlockUserLock(final UnlockUserLock request)
    {
        throw new UnsupportedOperationException("Called " + this.getClass().getName() + "#unlockUserLock");
    }

    public GetUserClassesResponse getUserClasses(final GetUserClasses request)
    {
        throw new UnsupportedOperationException("Called " + this.getClass().getName() + "#getUserClasses");
    }

    public BulkAddUserClassUsersResponse bulkAddUserClassUsers(final BulkAddUserClassUsers request)
    {
        throw new UnsupportedOperationException("Called " + this.getClass().getName() + "#bulkAddUserClassUsers");
    }

    public GetAcademicPermissionsForUserClassResponse getAcademicPermissionsForUserClass(
            final GetAcademicPermissionsForUserClass request)
    {
        throw new UnsupportedOperationException("Called " + this.getClass().getName()
                + "#getAcademicPermissionsForUserClass");
    }

    public GetPermissionResponse getPermission(final GetPermission getPermission52)
    {
        throw new UnsupportedOperationException("Called " + this.getClass().getName() + "#getPermission");
    }

    public GetUserClassResponse getUserClass(final GetUserClass request)
    {
        throw new UnsupportedOperationException("Called " + this.getClass().getName() + "#getUserClass");
    }

    public EditUserResponse editUser(final EditUser request)
    {
        throw new UnsupportedOperationException("Called " + this.getClass().getName() + "#editUser");
    }
}
