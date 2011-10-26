
/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.0  Built on : May 17, 2011 (04:21:18 IST)
 */

        
            package au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types;

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
            *  ExtensionMapper class
            */
            @SuppressWarnings({"unchecked","unused"})
        
        public  class ExtensionMapper{

          public static java.lang.Object getTypeObject(java.lang.String namespaceURI,
                                                       java.lang.String typeName,
                                                       javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI) &&
                  "state_type1".equals(typeName)){
                   
                            return  BookingSlotState.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/".equals(namespaceURI) &&
                  "RequestableResourceType".equals(typeName)){
                   
                            return RequestableResourceType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/".equals(namespaceURI) &&
                  "ResourceRequestType".equals(typeName)){
                   
                            return ResourceRequestType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI) &&
                  "TimePeriodType".equals(typeName)){
                   
                            return  TimePeriodType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI) &&
                  "FindFreeBookingsResponseType".equals(typeName)){
                   
                            return  FindFreeBookingsResponseType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI) &&
                  "ResourceType".equals(typeName)){
                   
                            return  ResourceType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/".equals(namespaceURI) &&
                  "SiteType".equals(typeName)){
                   
                            return SiteType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI) &&
                  "BookingType".equals(typeName)){
                   
                            return  BookingType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI) &&
                  "state_type1".equals(typeName)){
                   
                            return  BookingSlotState.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/".equals(namespaceURI) &&
                  "RequestableResourceListType".equals(typeName)){
                   
                            return RequestableResourceListType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/".equals(namespaceURI) &&
                  "SiteShutdownType".equals(typeName)){
                   
                            return SiteShutdownType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI) &&
                  "UserIDType".equals(typeName)){
                   
                            return  UserIDType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI) &&
                  "CreateBookingType".equals(typeName)){
                   
                            return  CreateBookingType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI) &&
                  "BookingSlotType".equals(typeName)){
                   
                            return  BookingSlotType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI) &&
                  "OperationResponseType".equals(typeName)){
                   
                            return  OperationResponseType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI) &&
                  "BookingResponseType".equals(typeName)){
                   
                            return  BookingResponseType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/".equals(namespaceURI) &&
                  "RequestResourceResponseType".equals(typeName)){
                   
                            return RequestResourceResponseType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI) &&
                  "QueueRequestType".equals(typeName)){
                   
                            return  QueueRequestType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI) &&
                  "PermissionIDType".equals(typeName)){
                   
                            return  PermissionIDType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI) &&
                  "SiteIDType".equals(typeName)){
                   
                            return  SiteIDType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI) &&
                  "FindFreeBookingsType".equals(typeName)){
                   
                            return  FindFreeBookingsType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/".equals(namespaceURI) &&
                  "ResourceRequestListType".equals(typeName)){
                   
                            return ResourceRequestListType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI) &&
                  "UserStatusType".equals(typeName)){
                   
                            return  UserStatusType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI) &&
                  "SessionType".equals(typeName)){
                   
                            return  SessionType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI) &&
                  "QueueTargetType".equals(typeName)){
                   
                            return  QueueTargetType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI) &&
                  "QueueType".equals(typeName)){
                   
                            return  QueueType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI) &&
                  "BookingIDType".equals(typeName)){
                   
                            return  BookingIDType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/multisite".equals(namespaceURI) &&
                  "AvailabilityResponseType".equals(typeName)){
                   
                            return  AvailabilityResponseType.Factory.parse(reader);
                        

                  }

              
             throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
          }

        }
    