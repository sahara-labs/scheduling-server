/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2011, University of Technology, Sydney
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
 * @date 31st January 2011
 */

package au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types;

import javax.xml.stream.XMLStreamReader;

import org.apache.axis2.databinding.ADBException;

/**
 * Extension mapper.
 */
public class ExtensionMapper
{
    public static Object getTypeObject(final String namespaceURI, final String typeName, final XMLStreamReader reader)
            throws Exception
    {
        if ("http://remotelabs.eng.uts.edu.au/rigmanagement".equals(namespaceURI) && "VoidType".equals(typeName))
        {
            return VoidType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/rigmanagement".equals(namespaceURI)
                && "OperationRequestType".equals(typeName))
        {
            return OperationRequestType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/rigmanagement".equals(namespaceURI) && "RigStateType".equals(typeName))
        {
            return RigStateType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/rigmanagement".equals(namespaceURI)
                && "PutRigOfflineType".equals(typeName))
        {
            return PutRigOfflineType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/rigmanagement".equals(namespaceURI)
                && "OfflinePeriodType".equals(typeName))
        {
            return OfflinePeriodType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/rigmanagement".equals(namespaceURI) && "RigTypeIDType".equals(typeName))
        {
            return RigTypeIDType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/rigmanagement".equals(namespaceURI)
                && "PutRigOnlineType".equals(typeName))
        {
            return PutRigOnlineType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/rigmanagement".equals(namespaceURI) && "RigLogType".equals(typeName))
        {
            return RigLogType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/rigmanagement".equals(namespaceURI) && "RigType".equals(typeName))
        {
            return RigType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/rigmanagement".equals(namespaceURI)
                && "CancelRigOfflineType".equals(typeName))
        {
            return CancelRigOfflineType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/rigmanagement".equals(namespaceURI)
                && "OperationResponseType".equals(typeName))
        {
            return OperationResponseType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/rigmanagement".equals(namespaceURI) && "RigTypesType".equals(typeName))
        {
            return RigTypesType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/rigmanagement".equals(namespaceURI) && "KickRigType".equals(typeName))
        {
            return KickRigType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/rigmanagement".equals(namespaceURI) && "RigIDType".equals(typeName))
        {
            return RigIDType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/rigmanagement".equals(namespaceURI) && "RigTypeType".equals(typeName))
        {
            return RigTypeType.Factory.parse(reader);
        }

        throw new ADBException("Unsupported type " + namespaceURI + " " + typeName);
    }
}
