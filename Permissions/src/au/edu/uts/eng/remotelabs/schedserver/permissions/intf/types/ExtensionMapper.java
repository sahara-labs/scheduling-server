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

package au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types;

import javax.xml.stream.XMLStreamReader;

import org.apache.axis2.databinding.ADBException;

/**
 * ExtensionMapper class.
 */
public class ExtensionMapper
{

    public static Object getTypeObject(final String namespaceURI, final String typeName, final XMLStreamReader reader)
            throws Exception
    {

        if ("http://remotelabs.eng.uts.edu.au/schedserver/permissions".equals(namespaceURI)
                && "resourceClass_type1".equals(typeName))
        {
            return ResourceClass.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/permissions".equals(namespaceURI)
                && "ResourceIDType".equals(typeName))
        {
            return ResourceIDType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/permissions".equals(namespaceURI)
                && "UserListType".equals(typeName))
        {
            return UserListType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/permissions".equals(namespaceURI)
                && "UserType".equals(typeName))
        {
            return UserType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/permissions".equals(namespaceURI)
                && "UserClassListType".equals(typeName))
        {
            return UserClassListType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/permissions".equals(namespaceURI)
                && "UserAssociationType".equals(typeName))
        {
            return UserAssociationType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/permissions".equals(namespaceURI)
                && "PermissionIDType".equals(typeName))
        {
            return PermissionIDType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/permissions".equals(namespaceURI)
                && "PermissionWithLockListType".equals(typeName))
        {
            return PermissionWithLockListType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/permissions".equals(namespaceURI)
                && "UserClassType".equals(typeName))
        {
            return UserClassType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/permissions".equals(namespaceURI)
                && "OperationRequestType".equals(typeName))
        {
            return OperationRequestType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/permissions".equals(namespaceURI)
                && "persona_type1".equals(typeName))
        {
            return PersonaType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/permissions".equals(namespaceURI)
                && "UserIDType".equals(typeName))
        {
            return UserIDType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/permissions".equals(namespaceURI)
                && "BulkAddUserClassUsersType".equals(typeName))
        {
            return BulkAddUserClassUsersType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/permissions".equals(namespaceURI)
                && "UserLockIDType".equals(typeName))
        {
            return UserLockIDType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/permissions".equals(namespaceURI)
                && "UserClassIDType".equals(typeName))
        {
            return UserClassIDType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/permissions".equals(namespaceURI)
                && "resourceClass_type1".equals(typeName))
        {
            return ResourceClass.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/permissions".equals(namespaceURI)
                && "OperationResponseType".equals(typeName))
        {
            return OperationResponseType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/permissions".equals(namespaceURI)
                && "AcademicPermissionListType".equals(typeName))
        {
            return AcademicPermissionListType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/permissions".equals(namespaceURI)
                && "UserLockResponseType".equals(typeName))
        {
            return UserLockResponseType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/permissions".equals(namespaceURI)
                && "AcademicPermissionIDType".equals(typeName))
        {
            return AcademicPermissionIDType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/permissions".equals(namespaceURI)
                && "AcademicPermissionType".equals(typeName))
        {
            return AcademicPermissionType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/permissions".equals(namespaceURI)
                && "PermissionType".equals(typeName))
        {
            return PermissionType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/permissions".equals(namespaceURI)
                && "PermissionListType".equals(typeName))
        {
            return PermissionListType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/permissions".equals(namespaceURI)
                && "UserClassesRequestType".equals(typeName))
        {
            return UserClassesRequestType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/permissions".equals(namespaceURI)
                && "UserLockType".equals(typeName))
        {
            return UserLockType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/permissions".equals(namespaceURI)
                && "PermissionWithLockType".equals(typeName))
        {
            return PermissionWithLockType.Factory.parse(reader);
        }

        throw new ADBException("Unsupported type " + namespaceURI + " " + typeName);
    }
}
