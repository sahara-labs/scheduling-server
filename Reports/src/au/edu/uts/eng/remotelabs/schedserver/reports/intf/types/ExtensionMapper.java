
/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4  Built on : Apr 26, 2008 (06:25:17 EDT)
 */

            package au.edu.uts.eng.remotelabs.schedserver.reports.intf.types;
            /**
            *  ExtensionMapper class
            */
        
        public  class ExtensionMapper{

          public static java.lang.Object getTypeObject(java.lang.String namespaceURI,
                                                       java.lang.String typeName,
                                                       javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/reports".equals(namespaceURI) &&
                  "typeForQuery_type3".equals(typeName)){
                   
                            return  au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.TypeForQuery_type3.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/reports".equals(namespaceURI) &&
                  "QuerySessionAccessResponseType".equals(typeName)){
                   
                            return  au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionAccessResponseType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/reports".equals(namespaceURI) &&
                  "operator_type1".equals(typeName)){
                   
                            return  au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.Operator_type1.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/reports".equals(namespaceURI) &&
                  "QueryInfoType".equals(typeName)){
                   
                            return  au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryInfoType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/reports".equals(namespaceURI) &&
                  "QueryFilterType".equals(typeName)){
                   
                            return  au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryFilterType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/reports".equals(namespaceURI) &&
                  "PaginationType".equals(typeName)){
                   
                            return  au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.PaginationType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/reports".equals(namespaceURI) &&
                  "QuerySessionReportResponseComplexType".equals(typeName)){
                   
                            return  au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionReportResponseComplexType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/reports".equals(namespaceURI) &&
                  "QuerySessionAccessType".equals(typeName)){
                   
                            return  au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionAccessType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/reports".equals(namespaceURI) &&
                  "typeForQuery_type1".equals(typeName)){
                   
                            return  au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.TypeForQuery_type1.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/reports".equals(namespaceURI) &&
                  "typeForQuery_type3".equals(typeName)){
                   
                            return  au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.TypeForQuery_type3.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/reports".equals(namespaceURI) &&
                  "SessionReportType".equals(typeName)){
                   
                            return  au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.SessionReportType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/reports".equals(namespaceURI) &&
                  "AccessReportType".equals(typeName)){
                   
                            return  au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.AccessReportType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/reports".equals(namespaceURI) &&
                  "RequestorType".equals(typeName)){
                   
                            return  au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.RequestorType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/reports".equals(namespaceURI) &&
                  "QueryInfoResponseType".equals(typeName)){
                   
                            return  au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryInfoResponseType.Factory.parse(reader);
                        

                  }

              
                  if (
                  "http://remotelabs.eng.uts.edu.au/schedserver/reports".equals(namespaceURI) &&
                  "QuerySessionReportComplexType".equals(typeName)){
                   
                            return  au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionReportComplexType.Factory.parse(reader);
                        

                  }

              
             throw new org.apache.axis2.databinding.ADBException("Unsupported type " + namespaceURI + " " + typeName);
          }

        }
    