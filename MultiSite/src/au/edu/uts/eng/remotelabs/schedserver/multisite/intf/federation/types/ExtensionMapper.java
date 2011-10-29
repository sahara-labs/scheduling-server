/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2011, Michael Diponio
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
 * @date 26th October 2011
 */

package au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types;

import javax.xml.stream.XMLStreamReader;

import org.apache.axis2.databinding.ADBException;

import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.AvailabilityResponseType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.BookingIDType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.BookingResponseType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.BookingSlotState;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.BookingSlotType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.BookingType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.CreateBookingType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.FindFreeBookingsResponseType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.FindFreeBookingsType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.OperationResponseType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.PermissionIDType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.QueueRequestType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.QueueTargetType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.QueueType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.ResourceType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.SessionType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.SiteIDType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.TimePeriodType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.UserIDType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.UserStatusType;

/**
 * ExtensionMapper class.
 */
public class ExtensionMapper
{
    public static Object getTypeObject(final String namespaceURI, final String typeName, final XMLStreamReader reader)
            throws Exception
    {
        if ("http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI)
                && "state_type1".equals(typeName))
        {
            return BookingSlotState.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/".equals(namespaceURI)
                && "RequestableResourceType".equals(typeName))
        {
            return RequestableResourceType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/".equals(namespaceURI)
                && "ResourceRequestType".equals(typeName))
        {
            return ResourceRequestType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI)
                && "TimePeriodType".equals(typeName))
        {
            return TimePeriodType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI)
                && "FindFreeBookingsResponseType".equals(typeName))
        {
            return FindFreeBookingsResponseType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI)
                && "ResourceType".equals(typeName))
        {
            return ResourceType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/".equals(namespaceURI)
                && "SiteType".equals(typeName))
        {
            return SiteType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI)
                && "BookingType".equals(typeName))
        {
            return BookingType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI)
                && "state_type1".equals(typeName))
        {
            return BookingSlotState.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/".equals(namespaceURI)
                && "RequestableResourceListType".equals(typeName))
        {
            return RequestableResourceListType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/".equals(namespaceURI)
                && "SiteShutdownType".equals(typeName))
        {
            return SiteShutdownType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI)
                && "UserIDType".equals(typeName))
        {
            return UserIDType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI)
                && "CreateBookingType".equals(typeName))
        {
            return CreateBookingType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI)
                && "BookingSlotType".equals(typeName))
        {
            return BookingSlotType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI)
                && "OperationResponseType".equals(typeName))
        {
            return OperationResponseType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI)
                && "BookingResponseType".equals(typeName))
        {
            return BookingResponseType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/".equals(namespaceURI)
                && "RequestResourceResponseType".equals(typeName))
        {
            return RequestResourceResponseType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI)
                && "QueueRequestType".equals(typeName))
        {
            return QueueRequestType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI)
                && "PermissionIDType".equals(typeName))
        {
            return PermissionIDType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI)
                && "SiteIDType".equals(typeName))
        {
            return SiteIDType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI)
                && "FindFreeBookingsType".equals(typeName))
        {
            return FindFreeBookingsType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/".equals(namespaceURI)
                && "ResourceRequestListType".equals(typeName))
        {
            return ResourceRequestListType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI)
                && "UserStatusType".equals(typeName))
        {
            return UserStatusType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI)
                && "SessionType".equals(typeName))
        {
            return SessionType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI)
                && "QueueTargetType".equals(typeName))
        {
            return QueueTargetType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI)
                && "QueueType".equals(typeName))
        {
            return QueueType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI)
                && "BookingIDType".equals(typeName))
        {
            return BookingIDType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI)
                && "AvailabilityResponseType".equals(typeName))
        {
            return AvailabilityResponseType.Factory.parse(reader);
        }

        throw new ADBException("Unsupported type " + namespaceURI + " " + typeName);
    }
}
