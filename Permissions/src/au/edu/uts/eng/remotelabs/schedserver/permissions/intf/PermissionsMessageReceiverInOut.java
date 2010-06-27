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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.databinding.ADBException;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.receivers.AbstractInOutMessageReceiver;
import org.apache.axis2.util.JavaUtils;

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
 * Message receiver for the permissions SOAP operations.
 */
public class PermissionsMessageReceiverInOut extends AbstractInOutMessageReceiver
{
    @Override
    public void invokeBusinessLogic(final MessageContext msgContext, final MessageContext newMsgContext)
            throws AxisFault
    {
        try
        {
            final Object obj = this.getTheImplementationObject(msgContext);
            final PermissionsSkeletonInterface skel = (PermissionsSkeletonInterface) obj;

            SOAPEnvelope envelope = null;
            final AxisOperation op = msgContext.getOperationContext().getAxisOperation();
            if (op == null)
            {
                throw new AxisFault("Operation is not located, if this is doclit style the SOAP-ACTION should " +
                		"specified via the SOAP Action to use the RawXMLProvider");
            }

            String methodName;
            if ((op.getName() != null) && ((methodName = JavaUtils.xmlNameToJava(op.getName().getLocalPart())) != null))
            {
                if ("addUserLock".equals(methodName))
                {
                    AddUserLockResponse addUserLockResponse = null;
                    final AddUserLock wrappedParam = (AddUserLock) this.fromOM(
                            msgContext.getEnvelope().getBody().getFirstElement(), AddUserLock.class, 
                            this.getEnvelopeNamespaces(msgContext.getEnvelope()));
                    addUserLockResponse = skel.addUserLock(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), addUserLockResponse, false);
                }
                else if ("addPermission".equals(methodName))
                {
                    AddPermissionResponse addPermissionResponse = null;
                    final AddPermission wrappedParam = (AddPermission) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), AddPermission.class, this.getEnvelopeNamespaces(msgContext
                            .getEnvelope()));
                    addPermissionResponse = skel.addPermission(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), addPermissionResponse, false);
                }
                else if ("addUser".equals(methodName))
                {
                    AddUserResponse addUserResponse = null;
                    final AddUser wrappedParam = (AddUser) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), AddUser.class, this.getEnvelopeNamespaces(msgContext.getEnvelope()));
                    addUserResponse = skel.addUser(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), addUserResponse, false);
                }
                else if ("deleteUser".equals(methodName))
                {
                    DeleteUserResponse deleteUserResponse = null;
                    final DeleteUser wrappedParam = (DeleteUser) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), DeleteUser.class, this.getEnvelopeNamespaces(msgContext.getEnvelope()));
                    deleteUserResponse = skel.deleteUser(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), deleteUserResponse, false);
                }
                else if ("getUserClassesForUser".equals(methodName))
                {
                    GetUserClassesForUserResponse getUserClassesForUserResponse = null;
                    final GetUserClassesForUser wrappedParam = (GetUserClassesForUser) this.fromOM(msgContext
                            .getEnvelope().getBody().getFirstElement(), GetUserClassesForUser.class, this
                            .getEnvelopeNamespaces(msgContext.getEnvelope()));
                    getUserClassesForUserResponse = skel.getUserClassesForUser(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), getUserClassesForUserResponse, false);
                }
                else if ("deleteAcademicPermission".equals(methodName))
                {
                    DeleteAcademicPermissionResponse deleteAcademicPermissionResponse = null;
                    final DeleteAcademicPermission wrappedParam = (DeleteAcademicPermission) this.fromOM(msgContext
                            .getEnvelope().getBody().getFirstElement(), DeleteAcademicPermission.class, this
                            .getEnvelopeNamespaces(msgContext.getEnvelope()));
                    deleteAcademicPermissionResponse = skel.deleteAcademicPermission(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), deleteAcademicPermissionResponse,
                            false);
                }
                else if ("getAcademicPermissionsForAcademic".equals(methodName))
                {
                    GetAcademicPermissionsForAcademicResponse getAcademicPermissionsForAcademicResponse = null;
                    final GetAcademicPermissionsForAcademic wrappedParam = (GetAcademicPermissionsForAcademic) this
                            .fromOM(msgContext.getEnvelope().getBody().getFirstElement(),
                            GetAcademicPermissionsForAcademic.class, this.getEnvelopeNamespaces(msgContext
                            .getEnvelope()));
                    getAcademicPermissionsForAcademicResponse = skel.getAcademicPermissionsForAcademic(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext),
                            getAcademicPermissionsForAcademicResponse, false);
                }
                else if ("deleteUserAssociation".equals(methodName))
                {
                    DeleteUserAssociationResponse deleteUserAssociationResponse = null;
                    final DeleteUserAssociation wrappedParam = (DeleteUserAssociation) this.fromOM(msgContext
                            .getEnvelope().getBody().getFirstElement(), DeleteUserAssociation.class, this
                            .getEnvelopeNamespaces(msgContext.getEnvelope()));

                    deleteUserAssociationResponse = skel.deleteUserAssociation(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), deleteUserAssociationResponse, false);
                }
                else if ("getPermissionsForUserClass".equals(methodName))
                {
                    GetPermissionsForUserClassResponse getPermissionsForUserClassResponse = null;
                    final GetPermissionsForUserClass wrappedParam = (GetPermissionsForUserClass) this.fromOM(msgContext
                            .getEnvelope().getBody().getFirstElement(), GetPermissionsForUserClass.class, this
                            .getEnvelopeNamespaces(msgContext.getEnvelope()));
                    getPermissionsForUserClassResponse = skel.getPermissionsForUserClass(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), getPermissionsForUserClassResponse,
                            false);
                }
                else if ("editPermission".equals(methodName))
                {
                    EditPermissionResponse editPermissionResponse = null;
                    final EditPermission wrappedParam = (EditPermission) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), EditPermission.class, this.getEnvelopeNamespaces(msgContext
                            .getEnvelope()));
                    editPermissionResponse = skel.editPermission(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), editPermissionResponse, false);
                }
                else if ("editUserClass".equals(methodName))
                {
                    EditUserClassResponse editUserClassResponse = null;
                    final EditUserClass wrappedParam = (EditUserClass) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), EditUserClass.class, this.getEnvelopeNamespaces(msgContext
                            .getEnvelope()));
                    editUserClassResponse = skel.editUserClass(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), editUserClassResponse, false);
                }
                else if ("deleteUserLock".equals(methodName))
                {
                    DeleteUserLockResponse deleteUserLockResponse = null;
                    final DeleteUserLock wrappedParam = (DeleteUserLock) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), DeleteUserLock.class, this.getEnvelopeNamespaces(msgContext
                            .getEnvelope()));
                    deleteUserLockResponse = skel.deleteUserLock(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), deleteUserLockResponse, false);
                }
                else

                if ("editAcademicPermission".equals(methodName))
                {

                    EditAcademicPermissionResponse editAcademicPermissionResponse83 = null;
                    final EditAcademicPermission wrappedParam = (EditAcademicPermission) this.fromOM(msgContext
                            .getEnvelope().getBody().getFirstElement(), EditAcademicPermission.class, this
                            .getEnvelopeNamespaces(msgContext.getEnvelope()));

                    editAcademicPermissionResponse83 =

                    skel.editAcademicPermission(wrappedParam);

                    envelope = this
                            .toEnvelope(this.getSOAPFactory(msgContext), editAcademicPermissionResponse83, false);
                }
                else

                if ("deleteUserClass".equals(methodName))
                {

                    DeleteUserClassResponse deleteUserClassResponse85 = null;
                    final DeleteUserClass wrappedParam = (DeleteUserClass) this.fromOM(msgContext.getEnvelope()
                            .getBody().getFirstElement(), DeleteUserClass.class, this.getEnvelopeNamespaces(msgContext
                            .getEnvelope()));

                    deleteUserClassResponse85 =

                    skel.deleteUserClass(wrappedParam);

                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), deleteUserClassResponse85, false);
                }
                else

                if ("addUserClass".equals(methodName))
                {

                    AddUserClassResponse addUserClassResponse87 = null;
                    final AddUserClass wrappedParam = (AddUserClass) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), AddUserClass.class, this
                            .getEnvelopeNamespaces(msgContext.getEnvelope()));

                    addUserClassResponse87 =

                    skel.addUserClass(wrappedParam);

                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), addUserClassResponse87, false);
                }
                else

                if ("deletePermission".equals(methodName))
                {

                    DeletePermissionResponse deletePermissionResponse89 = null;
                    final DeletePermission wrappedParam = (DeletePermission) this.fromOM(msgContext.getEnvelope()
                            .getBody().getFirstElement(), DeletePermission.class, this.getEnvelopeNamespaces(msgContext
                            .getEnvelope()));

                    deletePermissionResponse89 =

                    skel.deletePermission(wrappedParam);

                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), deletePermissionResponse89, false);
                }
                else if ("getPermissionsForUser".equals(methodName))
                {
                    GetPermissionsForUserResponse getPermissionsForUserResponse = null;
                    final GetPermissionsForUser wrappedParam = (GetPermissionsForUser) this.fromOM(msgContext
                            .getEnvelope().getBody().getFirstElement(), GetPermissionsForUser.class, this
                            .getEnvelopeNamespaces(msgContext.getEnvelope()));
                    getPermissionsForUserResponse = skel.getPermissionsForUser(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), getPermissionsForUserResponse, false);
                }
                else if ("getUsersInUserClass".equals(methodName))
                {
                    GetUsersInUserClassResponse getUsersInUserClassResponse = null;
                    final GetUsersInUserClass wrappedParam = (GetUsersInUserClass) this.fromOM(msgContext.getEnvelope()
                            .getBody().getFirstElement(), GetUsersInUserClass.class, this
                            .getEnvelopeNamespaces(msgContext.getEnvelope()));
                    getUsersInUserClassResponse = skel.getUsersInUserClass(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), getUsersInUserClassResponse, false);
                }
                else if ("addUserAssociation".equals(methodName))
                {
                    AddUserAssociationResponse addUserAssociationResponse = null;
                    final AddUserAssociation wrappedParam = (AddUserAssociation) this.fromOM(msgContext.getEnvelope()
                            .getBody().getFirstElement(), AddUserAssociation.class, this
                            .getEnvelopeNamespaces(msgContext.getEnvelope()));
                    addUserAssociationResponse = skel.addUserAssociation(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), addUserAssociationResponse, false);
                }
                else if ("getUser".equals(methodName))
                {
                    GetUserResponse getUserResponse = null;
                    final GetUser wrappedParam = (GetUser) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), GetUser.class, this.getEnvelopeNamespaces(msgContext.getEnvelope()));
                    getUserResponse = skel.getUser(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), getUserResponse, false);
                }
                else if ("getAcademicPermission".equals(methodName))
                {
                    GetAcademicPermissionResponse getAcademicPermissionResponse = null;
                    final GetAcademicPermission wrappedParam = (GetAcademicPermission) this.fromOM(msgContext
                            .getEnvelope().getBody().getFirstElement(), GetAcademicPermission.class, this
                            .getEnvelopeNamespaces(msgContext.getEnvelope()));
                    getAcademicPermissionResponse = skel.getAcademicPermission(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), getAcademicPermissionResponse, false);
                }
                else if ("addAcademicPermission".equals(methodName))
                {
                    AddAcademicPermissionResponse addAcademicPermissionResponse = null;
                    final AddAcademicPermission wrappedParam = (AddAcademicPermission) this.fromOM(msgContext
                            .getEnvelope().getBody().getFirstElement(), AddAcademicPermission.class, this
                            .getEnvelopeNamespaces(msgContext.getEnvelope()));
                    addAcademicPermissionResponse = skel.addAcademicPermission(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), addAcademicPermissionResponse, false);
                }
                else if ("unlockUserLock".equals(methodName))
                {
                    UnlockUserLockResponse unlockUserLockResponse = null;
                    final UnlockUserLock wrappedParam = (UnlockUserLock) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), UnlockUserLock.class, this.getEnvelopeNamespaces(msgContext
                            .getEnvelope()));
                    unlockUserLockResponse = skel.unlockUserLock(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), unlockUserLockResponse, false);
                }
                else if ("getUserClasses".equals(methodName))
                {
                    GetUserClassesResponse getUserClassesResponse = null;
                    final GetUserClasses wrappedParam = (GetUserClasses) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), GetUserClasses.class, this.getEnvelopeNamespaces(msgContext
                            .getEnvelope()));
                    getUserClassesResponse = skel.getUserClasses(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), getUserClassesResponse, false);
                }
                else if ("bulkAddUserClassUsers".equals(methodName))
                {
                    BulkAddUserClassUsersResponse bulkAddUserClassUsersResponse = null;
                    final BulkAddUserClassUsers wrappedParam = (BulkAddUserClassUsers) this.fromOM(msgContext
                            .getEnvelope().getBody().getFirstElement(), BulkAddUserClassUsers.class, this
                            .getEnvelopeNamespaces(msgContext.getEnvelope()));
                    bulkAddUserClassUsersResponse = skel.bulkAddUserClassUsers(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), bulkAddUserClassUsersResponse, false);
                }
                else if ("getAcademicPermissionsForUserClass".equals(methodName))
                {
                    GetAcademicPermissionsForUserClassResponse getAcademicPermissionsForUserClassResponse = null;
                    final GetAcademicPermissionsForUserClass wrappedParam = (GetAcademicPermissionsForUserClass) this
                            .fromOM(msgContext.getEnvelope().getBody().getFirstElement(),
                            GetAcademicPermissionsForUserClass.class, this.getEnvelopeNamespaces(msgContext
                            .getEnvelope()));
                    getAcademicPermissionsForUserClassResponse = skel.getAcademicPermissionsForUserClass(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), getAcademicPermissionsForUserClassResponse, false);
                }
                else if ("getPermission".equals(methodName))
                {
                    GetPermissionResponse getPermissionResponse = null;
                    final GetPermission wrappedParam = (GetPermission) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), GetPermission.class, this.getEnvelopeNamespaces(msgContext
                            .getEnvelope()));
                    getPermissionResponse = skel.getPermission(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), getPermissionResponse, false);
                }
                else if ("getUserClass".equals(methodName))
                {
                    GetUserClassResponse getUserClassResponse = null;
                    final GetUserClass wrappedParam = (GetUserClass) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), GetUserClass.class, this
                            .getEnvelopeNamespaces(msgContext.getEnvelope()));
                    getUserClassResponse = skel.getUserClass(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), getUserClassResponse, false);
                }
                else if ("editUser".equals(methodName))
                {
                    EditUserResponse editUserResponse = null;
                    final EditUser wrappedParam = (EditUser) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), EditUser.class, this.getEnvelopeNamespaces(msgContext.getEnvelope()));
                    editUserResponse = skel.editUser(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), editUserResponse, false);
                }
                else
                {
                    throw new RuntimeException("method not found");
                }

                newMsgContext.setEnvelope(envelope);
            }
        }
        catch (final Exception e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final AddUserLockResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(AddUserLockResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final AddPermissionResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(AddPermissionResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final AddUserResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(AddUserResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final DeleteUserResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(DeleteUserResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final GetUserClassesForUserResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(GetUserClassesForUserResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final DeleteAcademicPermissionResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(DeleteAcademicPermissionResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final GetAcademicPermissionsForAcademicResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(GetAcademicPermissionsForAcademicResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final DeleteUserAssociationResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(DeleteUserAssociationResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final GetPermissionsForUserClassResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(GetPermissionsForUserClassResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final EditPermissionResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(EditPermissionResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final EditUserClassResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(EditUserClassResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final DeleteUserLockResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(DeleteUserLockResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final EditAcademicPermissionResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(EditAcademicPermissionResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final DeleteUserClassResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(DeleteUserClassResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final AddUserClassResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(AddUserClassResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final DeletePermissionResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(DeletePermissionResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final GetPermissionsForUserResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(GetPermissionsForUserResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final GetUsersInUserClassResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(GetUsersInUserClassResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final AddUserAssociationResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(AddUserAssociationResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final GetUserResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(GetUserResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final GetAcademicPermissionResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(GetAcademicPermissionResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final AddAcademicPermissionResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(AddAcademicPermissionResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final UnlockUserLockResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(UnlockUserLockResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final GetUserClassesResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(GetUserClassesResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final BulkAddUserClassUsersResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(BulkAddUserClassUsersResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final GetAcademicPermissionsForUserClassResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(GetAcademicPermissionsForUserClassResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final GetPermissionResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(GetPermissionResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final GetUserClassResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(GetUserClassResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final EditUserResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(EditUserResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private Object fromOM(final OMElement param, final Class<?> type, final Map<String, String> extraNamespaces)
            throws AxisFault
    {
        try
        {
            if (AddUserLock.class.equals(type))
            {
                return AddUserLock.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (AddUserLockResponse.class.equals(type))
            {
                return AddUserLockResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (AddPermission.class.equals(type))
            {
                return AddPermission.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (AddPermissionResponse.class.equals(type))
            {
                return AddPermissionResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (AddUser.class.equals(type))
            {
                return AddUser.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (AddUserResponse.class.equals(type))
            {
                return AddUserResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (DeleteUser.class.equals(type))
            {
                return DeleteUser.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (DeleteUserResponse.class.equals(type))
            {
                return DeleteUserResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetUserClassesForUser.class.equals(type))
            {
                return GetUserClassesForUser.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetUserClassesForUserResponse.class.equals(type))
            {
                return GetUserClassesForUserResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (DeleteAcademicPermission.class.equals(type))
            {
                return DeleteAcademicPermission.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (DeleteAcademicPermissionResponse.class.equals(type))
            {
                return DeleteAcademicPermissionResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetAcademicPermissionsForAcademic.class.equals(type))
            {
                return GetAcademicPermissionsForAcademic.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetAcademicPermissionsForAcademicResponse.class.equals(type))
            {
                return GetAcademicPermissionsForAcademicResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (DeleteUserAssociation.class.equals(type))
            {
                return DeleteUserAssociation.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (DeleteUserAssociationResponse.class.equals(type))
            {
                return DeleteUserAssociationResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetPermissionsForUserClass.class.equals(type))
            {
                return GetPermissionsForUserClass.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetPermissionsForUserClassResponse.class.equals(type))
            {
                return GetPermissionsForUserClassResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (EditPermission.class.equals(type))
            {
                return EditPermission.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (EditPermissionResponse.class.equals(type))
            {
                return EditPermissionResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (EditUserClass.class.equals(type))
            {
                return EditUserClass.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (EditUserClassResponse.class.equals(type))
            {
                return EditUserClassResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (DeleteUserLock.class.equals(type))
            {
                return DeleteUserLock.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (DeleteUserLockResponse.class.equals(type))
            {
                return DeleteUserLockResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (EditAcademicPermission.class.equals(type))
            {
                return EditAcademicPermission.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (EditAcademicPermissionResponse.class.equals(type))
            {
                return EditAcademicPermissionResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (DeleteUserClass.class.equals(type))
            {
                return DeleteUserClass.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (DeleteUserClassResponse.class.equals(type))
            {
                return DeleteUserClassResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (AddUserClass.class.equals(type))
            {
                return AddUserClass.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (AddUserClassResponse.class.equals(type))
            {
                return AddUserClassResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (DeletePermission.class.equals(type))
            {
                return DeletePermission.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (DeletePermissionResponse.class.equals(type))
            {
                return DeletePermissionResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetPermissionsForUser.class.equals(type))
            {
                return GetPermissionsForUser.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetPermissionsForUserResponse.class.equals(type))
            {
                return GetPermissionsForUserResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetUsersInUserClass.class.equals(type))
            {
                return GetUsersInUserClass.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetUsersInUserClassResponse.class.equals(type))
            {
                return GetUsersInUserClassResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (AddUserAssociation.class.equals(type))
            {
                return AddUserAssociation.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (AddUserAssociationResponse.class.equals(type))
            {
                return AddUserAssociationResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetUser.class.equals(type))
            {
                return GetUser.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetUserResponse.class.equals(type))
            {
                return GetUserResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetAcademicPermission.class.equals(type))
            {
                return GetAcademicPermission.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetAcademicPermissionResponse.class.equals(type))
            {
                return GetAcademicPermissionResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (AddAcademicPermission.class.equals(type))
            {
                return AddAcademicPermission.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (AddAcademicPermissionResponse.class.equals(type))
            {
                return AddAcademicPermissionResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (UnlockUserLock.class.equals(type))
            {
                return UnlockUserLock.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (UnlockUserLockResponse.class.equals(type))
            {
                return UnlockUserLockResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetUserClasses.class.equals(type))
            {
                return GetUserClasses.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetUserClassesResponse.class.equals(type))
            {
                return GetUserClassesResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (BulkAddUserClassUsers.class.equals(type))
            {
                return BulkAddUserClassUsers.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (BulkAddUserClassUsersResponse.class.equals(type))
            {
                return BulkAddUserClassUsersResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetAcademicPermissionsForUserClass.class.equals(type))
            {
                return GetAcademicPermissionsForUserClass.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetAcademicPermissionsForUserClassResponse.class.equals(type))
            {
                return GetAcademicPermissionsForUserClassResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetPermission.class.equals(type))
            {
                return GetPermission.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetPermissionResponse.class.equals(type))
            {
                return GetPermissionResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetUserClass.class.equals(type))
            {
                return GetUserClass.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetUserClassResponse.class.equals(type))
            {
                return GetUserClassResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (EditUser.class.equals(type))
            {
                return EditUser.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (EditUserResponse.class.equals(type))
            {
                return EditUserResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

        }
        catch (final Exception e)
        {
            throw AxisFault.makeFault(e);
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
    private Map<String, String> getEnvelopeNamespaces(final SOAPEnvelope env)
    {
        final Map<String, String> returnMap = new HashMap<String, String>();
        final Iterator namespaceIterator = env.getAllDeclaredNamespaces();
        while (namespaceIterator.hasNext())
        {
            final OMNamespace ns = (OMNamespace) namespaceIterator.next();
            returnMap.put(ns.getPrefix(), ns.getNamespaceURI());
        }
        return returnMap;
    }
}

