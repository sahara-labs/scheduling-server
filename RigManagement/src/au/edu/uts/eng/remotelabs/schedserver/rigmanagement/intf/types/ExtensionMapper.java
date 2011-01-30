
/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 19, 2008 (10:13:44 LKT)
 */

            package au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types;
            /**
            *  ExtensionMapper class
            */
        
        public  class ExtensionMapper{

          public static java.lang.Object getTypeObject(java.lang.String namespaceURI,
                                                       java.lang.String typeName,
                                                       javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/rigmanagement".equals(namespaceURI) &&
                  "VoidType".equals(typeName)){
                   
                            return  au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.VoidType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/rigmanagement".equals(namespaceURI) &&
                  "OperationRequestType".equals(typeName)){
                   
                            return  au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.OperationRequestType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/rigmanagement".equals(namespaceURI) &&
                  "RigStateType".equals(typeName)){
                   
                            return  au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.RigStateType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/rigmanagement".equals(namespaceURI) &&
                  "PutRigOfflineType".equals(typeName)){
                   
                            return  au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.PutRigOfflineType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/rigmanagement".equals(namespaceURI) &&
                  "OfflinePeriodType".equals(typeName)){
                   
                            return  au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.OfflinePeriodType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/rigmanagement".equals(namespaceURI) &&
                  "RigTypeIDType".equals(typeName)){
                   
                            return  au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.RigTypeIDType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/rigmanagement".equals(namespaceURI) &&
                  "PutRigOnlineType".equals(typeName)){
                   
                            return  au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.PutRigOnlineType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/rigmanagement".equals(namespaceURI) &&
                  "RigLogType".equals(typeName)){
                   
                            return  au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.RigLogType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/rigmanagement".equals(namespaceURI) &&
                  "RigType".equals(typeName)){
                   
                            return  au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.RigType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/rigmanagement".equals(namespaceURI) &&
                  "CancelRigOfflineType".equals(typeName)){
                   
                            return  au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.CancelRigOfflineType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/rigmanagement".equals(namespaceURI) &&
                  "OperationResponseType".equals(typeName)){
                   
                            return  au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.OperationResponseType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/rigmanagement".equals(namespaceURI) &&
                  "RigTypesType".equals(typeName)){
                   
                            return  au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.RigTypesType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/rigmanagement".equals(namespaceURI) &&
                  "KickRigType".equals(typeName)){
                   
                            return  au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.KickRigType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/rigmanagement".equals(namespaceURI) &&
                  "RigIDType".equals(typeName)){
                   
                            return  au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.RigIDType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/rigmanagement".equals(namespaceURI) &&
                  "RigTypeType".equals(typeName)){
                   
                            return  au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.RigTypeType.Factory.parse(reader);
                        

                  }

              
             throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
          }

        }
    