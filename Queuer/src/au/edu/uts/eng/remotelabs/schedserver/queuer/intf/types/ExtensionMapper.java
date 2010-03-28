
/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 19, 2008 (10:13:44 LKT)
 */

            package au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types;
            /**
            *  ExtensionMapper class
            */
        
        public  class ExtensionMapper{

          public static java.lang.Object getTypeObject(java.lang.String namespaceURI,
                                                       java.lang.String typeName,
                                                       javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/queuer".equals(namespaceURI) &&
                  "UserQueueType".equals(typeName)){
                   
                            return  au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.UserQueueType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/queuer".equals(namespaceURI) &&
                  "PermissionIDType".equals(typeName)){
                   
                            return  au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.PermissionIDType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/queuer".equals(namespaceURI) &&
                  "QueueRequestType".equals(typeName)){
                   
                            return  au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.QueueRequestType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/queuer".equals(namespaceURI) &&
                  "InQueueType".equals(typeName)){
                   
                            return  au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.InQueueType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/queuer".equals(namespaceURI) &&
                  "QueueTargetType".equals(typeName)){
                   
                            return  au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.QueueTargetType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/queuer".equals(namespaceURI) &&
                  "QueueType".equals(typeName)){
                   
                            return  au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.QueueType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/queuer".equals(namespaceURI) &&
                  "ResourceIDType".equals(typeName)){
                   
                            return  au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.ResourceIDType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/queuer".equals(namespaceURI) &&
                  "UserIDType".equals(typeName)){
                   
                            return  au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.UserIDType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/queuer".equals(namespaceURI) &&
                  "OperationRequestType".equals(typeName)){
                   
                            return  au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.OperationRequestType.Factory.parse(reader);
                        

                  }

              
             throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
          }

        }
    