
/**
 * SessionReportType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4  Built on : Apr 26, 2008 (06:25:17 EDT)
 */
            
                package au.edu.uts.eng.remotelabs.schedserver.reports.intf.types;
            

            /**
            *  SessionReportType bean class
            */
        
        public  class SessionReportType
        implements org.apache.axis2.databinding.ADBBean{
        /* This type was generated from the piece of schema that had
                name = SessionReportType
                Namespace URI = http://remotelabs.eng.uts.edu.au/schedserver/reports
                Namespace Prefix = ns1
                */
            

        private static java.lang.String generatePrefix(java.lang.String namespace) {
            if(namespace.equals("http://remotelabs.eng.uts.edu.au/schedserver/reports")){
                return "ns1";
            }
            return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
        }

        

                        /**
                        * field for SessionReportTypeChoice_type0
                        * This was an Array!
                        */

                        
                                    protected au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.SessionReportTypeChoice_type0[] localSessionReportTypeChoice_type0 ;
                                

                           /**
                           * Auto generated getter method
                           * @return au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.SessionReportTypeChoice_type0[]
                           */
                           public  au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.SessionReportTypeChoice_type0[] getSessionReportTypeChoice_type0(){
                               return localSessionReportTypeChoice_type0;
                           }

                           
                        


                               
                              /**
                               * validate the array for SessionReportTypeChoice_type0
                               */
                              protected void validateSessionReportTypeChoice_type0(au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.SessionReportTypeChoice_type0[] param){
                             
                              if ((param != null) && (param.length < 1)){
                                throw new java.lang.RuntimeException();
                              }
                              
                              }


                             /**
                              * Auto generated setter method
                              * @param param SessionReportTypeChoice_type0
                              */
                              public void setSessionReportTypeChoice_type0(au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.SessionReportTypeChoice_type0[] param){
                              
                                   validateSessionReportTypeChoice_type0(param);

                               
                                      this.localSessionReportTypeChoice_type0=param;
                              }

                               
                             
                             /**
                             * Auto generated add method for the array for convenience
                             * @param param au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.SessionReportTypeChoice_type0
                             */
                             public void addSessionReportTypeChoice_type0(au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.SessionReportTypeChoice_type0 param){
                                   if (localSessionReportTypeChoice_type0 == null){
                                   localSessionReportTypeChoice_type0 = new au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.SessionReportTypeChoice_type0[]{};
                                   }

                            

                               java.util.List list =
                            org.apache.axis2.databinding.utils.ConverterUtil.toList(localSessionReportTypeChoice_type0);
                               list.add(param);
                               this.localSessionReportTypeChoice_type0 =
                             (au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.SessionReportTypeChoice_type0[])list.toArray(
                            new au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.SessionReportTypeChoice_type0[list.size()]);

                             }
                             

                        /**
                        * field for AveQueueDuration
                        */

                        
                                    protected float localAveQueueDuration ;
                                

                           /**
                           * Auto generated getter method
                           * @return float
                           */
                           public  float getAveQueueDuration(){
                               return localAveQueueDuration;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param AveQueueDuration
                               */
                               public void setAveQueueDuration(float param){
                            
                                            this.localAveQueueDuration=param;
                                    

                               }
                            

                        /**
                        * field for MedQueueDuration
                        */

                        
                                    protected float localMedQueueDuration ;
                                

                           /**
                           * Auto generated getter method
                           * @return float
                           */
                           public  float getMedQueueDuration(){
                               return localMedQueueDuration;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param MedQueueDuration
                               */
                               public void setMedQueueDuration(float param){
                            
                                            this.localMedQueueDuration=param;
                                    

                               }
                            

                        /**
                        * field for MinQueueDuration
                        */

                        
                                    protected float localMinQueueDuration ;
                                

                           /**
                           * Auto generated getter method
                           * @return float
                           */
                           public  float getMinQueueDuration(){
                               return localMinQueueDuration;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param MinQueueDuration
                               */
                               public void setMinQueueDuration(float param){
                            
                                            this.localMinQueueDuration=param;
                                    

                               }
                            

                        /**
                        * field for TotalQueueDuration
                        */

                        
                                    protected float localTotalQueueDuration ;
                                

                           /**
                           * Auto generated getter method
                           * @return float
                           */
                           public  float getTotalQueueDuration(){
                               return localTotalQueueDuration;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param TotalQueueDuration
                               */
                               public void setTotalQueueDuration(float param){
                            
                                            this.localTotalQueueDuration=param;
                                    

                               }
                            

                        /**
                        * field for SessionCount
                        */

                        
                                    protected int localSessionCount ;
                                

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getSessionCount(){
                               return localSessionCount;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param SessionCount
                               */
                               public void setSessionCount(int param){
                            
                                            this.localSessionCount=param;
                                    

                               }
                            

                        /**
                        * field for UserCount
                        */

                        
                                    protected int localUserCount ;
                                
                           /*  This tracker boolean wil be used to detect whether the user called the set method
                          *   for this attribute. It will be used to determine whether to include this field
                           *   in the serialized XML
                           */
                           protected boolean localUserCountTracker = false ;
                           

                           /**
                           * Auto generated getter method
                           * @return int
                           */
                           public  int getUserCount(){
                               return localUserCount;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param UserCount
                               */
                               public void setUserCount(int param){
                            
                                       // setting primitive attribute tracker to true
                                       
                                               if (param==java.lang.Integer.MIN_VALUE) {
                                           localUserCountTracker = false;
                                              
                                       } else {
                                          localUserCountTracker = true;
                                       }
                                   
                                            this.localUserCount=param;
                                    

                               }
                            

                        /**
                        * field for AveSessionDuration
                        */

                        
                                    protected float localAveSessionDuration ;
                                

                           /**
                           * Auto generated getter method
                           * @return float
                           */
                           public  float getAveSessionDuration(){
                               return localAveSessionDuration;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param AveSessionDuration
                               */
                               public void setAveSessionDuration(float param){
                            
                                            this.localAveSessionDuration=param;
                                    

                               }
                            

                        /**
                        * field for MedSessionDuration
                        */

                        
                                    protected float localMedSessionDuration ;
                                

                           /**
                           * Auto generated getter method
                           * @return float
                           */
                           public  float getMedSessionDuration(){
                               return localMedSessionDuration;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param MedSessionDuration
                               */
                               public void setMedSessionDuration(float param){
                            
                                            this.localMedSessionDuration=param;
                                    

                               }
                            

                        /**
                        * field for MaxSessionDuration
                        */

                        
                                    protected float localMaxSessionDuration ;
                                

                           /**
                           * Auto generated getter method
                           * @return float
                           */
                           public  float getMaxSessionDuration(){
                               return localMaxSessionDuration;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param MaxSessionDuration
                               */
                               public void setMaxSessionDuration(float param){
                            
                                            this.localMaxSessionDuration=param;
                                    

                               }
                            

                        /**
                        * field for MinSessionDuration
                        */

                        
                                    protected float localMinSessionDuration ;
                                

                           /**
                           * Auto generated getter method
                           * @return float
                           */
                           public  float getMinSessionDuration(){
                               return localMinSessionDuration;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param MinSessionDuration
                               */
                               public void setMinSessionDuration(float param){
                            
                                            this.localMinSessionDuration=param;
                                    

                               }
                            

                        /**
                        * field for TotalSessionDuration
                        */

                        
                                    protected float localTotalSessionDuration ;
                                

                           /**
                           * Auto generated getter method
                           * @return float
                           */
                           public  float getTotalSessionDuration(){
                               return localTotalSessionDuration;
                           }

                           
                        
                            /**
                               * Auto generated setter method
                               * @param param TotalSessionDuration
                               */
                               public void setTotalSessionDuration(float param){
                            
                                            this.localTotalSessionDuration=param;
                                    

                               }
                            

     /**
     * isReaderMTOMAware
     * @return true if the reader supports MTOM
     */
   public static boolean isReaderMTOMAware(javax.xml.stream.XMLStreamReader reader) {
        boolean isReaderMTOMAware = false;
        
        try{
          isReaderMTOMAware = java.lang.Boolean.TRUE.equals(reader.getProperty(org.apache.axiom.om.OMConstants.IS_DATA_HANDLERS_AWARE));
        }catch(java.lang.IllegalArgumentException e){
          isReaderMTOMAware = false;
        }
        return isReaderMTOMAware;
   }
     
     
        /**
        *
        * @param parentQName
        * @param factory
        * @return org.apache.axiom.om.OMElement
        */
       public org.apache.axiom.om.OMElement getOMElement (
               final javax.xml.namespace.QName parentQName,
               final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException{


        
               org.apache.axiom.om.OMDataSource dataSource =
                       new org.apache.axis2.databinding.ADBDataSource(this,parentQName){

                 public void serialize(org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
                       SessionReportType.this.serialize(parentQName,factory,xmlWriter);
                 }
               };
               return new org.apache.axiom.om.impl.llom.OMSourcedElementImpl(
               parentQName,factory,dataSource);
            
       }

         public void serialize(final javax.xml.namespace.QName parentQName,
                                       final org.apache.axiom.om.OMFactory factory,
                                       org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter)
                                throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
                           serialize(parentQName,factory,xmlWriter,false);
         }

         public void serialize(final javax.xml.namespace.QName parentQName,
                               final org.apache.axiom.om.OMFactory factory,
                               org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter,
                               boolean serializeType)
            throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException{
            
                


                java.lang.String prefix = null;
                java.lang.String namespace = null;
                

                    prefix = parentQName.getPrefix();
                    namespace = parentQName.getNamespaceURI();

                    if ((namespace != null) && (namespace.trim().length() > 0)) {
                        java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
                        if (writerPrefix != null) {
                            xmlWriter.writeStartElement(namespace, parentQName.getLocalPart());
                        } else {
                            if (prefix == null) {
                                prefix = generatePrefix(namespace);
                            }

                            xmlWriter.writeStartElement(prefix, parentQName.getLocalPart(), namespace);
                            xmlWriter.writeNamespace(prefix, namespace);
                            xmlWriter.setPrefix(prefix, namespace);
                        }
                    } else {
                        xmlWriter.writeStartElement(parentQName.getLocalPart());
                    }
                
                  if (serializeType){
               

                   java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://remotelabs.eng.uts.edu.au/schedserver/reports");
                   if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0)){
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           namespacePrefix+":SessionReportType",
                           xmlWriter);
                   } else {
                       writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type",
                           "SessionReportType",
                           xmlWriter);
                   }

               
                   }
               
                                     
                                      if (localSessionReportTypeChoice_type0!=null){
                                            for (int i = 0;i < localSessionReportTypeChoice_type0.length;i++){
                                                if (localSessionReportTypeChoice_type0[i] != null){
                                                 localSessionReportTypeChoice_type0[i].serialize(null,factory,xmlWriter);
                                                } else {
                                                   
                                                           throw new org.apache.axis2.databinding.ADBException("SessionReportTypeChoice_type0 cannot be null!!");
                                                    
                                                }

                                            }
                                     } else {
                                        throw new org.apache.axis2.databinding.ADBException("SessionReportTypeChoice_type0 cannot be null!!");
                                     }
                                 
                                    namespace = "";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"aveQueueDuration", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"aveQueueDuration");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("aveQueueDuration");
                                    }
                                
                                               if (java.lang.Float.isNaN(localAveQueueDuration)) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("aveQueueDuration cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAveQueueDuration));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"medQueueDuration", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"medQueueDuration");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("medQueueDuration");
                                    }
                                
                                               if (java.lang.Float.isNaN(localMedQueueDuration)) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("medQueueDuration cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMedQueueDuration));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"minQueueDuration", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"minQueueDuration");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("minQueueDuration");
                                    }
                                
                                               if (java.lang.Float.isNaN(localMinQueueDuration)) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("minQueueDuration cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMinQueueDuration));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"totalQueueDuration", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"totalQueueDuration");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("totalQueueDuration");
                                    }
                                
                                               if (java.lang.Float.isNaN(localTotalQueueDuration)) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("totalQueueDuration cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTotalQueueDuration));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"sessionCount", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"sessionCount");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("sessionCount");
                                    }
                                
                                               if (localSessionCount==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("sessionCount cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localSessionCount));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                              if (localUserCountTracker){
                                    namespace = "";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"userCount", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"userCount");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("userCount");
                                    }
                                
                                               if (localUserCount==java.lang.Integer.MIN_VALUE) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("userCount cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localUserCount));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             }
                                    namespace = "";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"aveSessionDuration", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"aveSessionDuration");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("aveSessionDuration");
                                    }
                                
                                               if (java.lang.Float.isNaN(localAveSessionDuration)) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("aveSessionDuration cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAveSessionDuration));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"medSessionDuration", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"medSessionDuration");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("medSessionDuration");
                                    }
                                
                                               if (java.lang.Float.isNaN(localMedSessionDuration)) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("medSessionDuration cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMedSessionDuration));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"maxSessionDuration", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"maxSessionDuration");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("maxSessionDuration");
                                    }
                                
                                               if (java.lang.Float.isNaN(localMaxSessionDuration)) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("maxSessionDuration cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMaxSessionDuration));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"minSessionDuration", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"minSessionDuration");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("minSessionDuration");
                                    }
                                
                                               if (java.lang.Float.isNaN(localMinSessionDuration)) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("minSessionDuration cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMinSessionDuration));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                                    namespace = "";
                                    if (! namespace.equals("")) {
                                        prefix = xmlWriter.getPrefix(namespace);

                                        if (prefix == null) {
                                            prefix = generatePrefix(namespace);

                                            xmlWriter.writeStartElement(prefix,"totalSessionDuration", namespace);
                                            xmlWriter.writeNamespace(prefix, namespace);
                                            xmlWriter.setPrefix(prefix, namespace);

                                        } else {
                                            xmlWriter.writeStartElement(namespace,"totalSessionDuration");
                                        }

                                    } else {
                                        xmlWriter.writeStartElement("totalSessionDuration");
                                    }
                                
                                               if (java.lang.Float.isNaN(localTotalSessionDuration)) {
                                           
                                                         throw new org.apache.axis2.databinding.ADBException("totalSessionDuration cannot be null!!");
                                                      
                                               } else {
                                                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTotalSessionDuration));
                                               }
                                    
                                   xmlWriter.writeEndElement();
                             
                    xmlWriter.writeEndElement();
               

        }

         /**
          * Util method to write an attribute with the ns prefix
          */
          private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
                                      java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
              if (xmlWriter.getPrefix(namespace) == null) {
                       xmlWriter.writeNamespace(prefix, namespace);
                       xmlWriter.setPrefix(prefix, namespace);

              }

              xmlWriter.writeAttribute(namespace,attName,attValue);

         }

        /**
          * Util method to write an attribute without the ns prefix
          */
          private void writeAttribute(java.lang.String namespace,java.lang.String attName,
                                      java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException{
                if (namespace.equals(""))
              {
                  xmlWriter.writeAttribute(attName,attValue);
              }
              else
              {
                  registerPrefix(xmlWriter, namespace);
                  xmlWriter.writeAttribute(namespace,attName,attValue);
              }
          }


           /**
             * Util method to write an attribute without the ns prefix
             */
            private void writeQNameAttribute(java.lang.String namespace, java.lang.String attName,
                                             javax.xml.namespace.QName qname, javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

                java.lang.String attributeNamespace = qname.getNamespaceURI();
                java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
                if (attributePrefix == null) {
                    attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
                }
                java.lang.String attributeValue;
                if (attributePrefix.trim().length() > 0) {
                    attributeValue = attributePrefix + ":" + qname.getLocalPart();
                } else {
                    attributeValue = qname.getLocalPart();
                }

                if (namespace.equals("")) {
                    xmlWriter.writeAttribute(attName, attributeValue);
                } else {
                    registerPrefix(xmlWriter, namespace);
                    xmlWriter.writeAttribute(namespace, attName, attributeValue);
                }
            }
        /**
         *  method to handle Qnames
         */

        private void writeQName(javax.xml.namespace.QName qname,
                                javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
            java.lang.String namespaceURI = qname.getNamespaceURI();
            if (namespaceURI != null) {
                java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
                if (prefix == null) {
                    prefix = generatePrefix(namespaceURI);
                    xmlWriter.writeNamespace(prefix, namespaceURI);
                    xmlWriter.setPrefix(prefix,namespaceURI);
                }

                if (prefix.trim().length() > 0){
                    xmlWriter.writeCharacters(prefix + ":" + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                } else {
                    // i.e this is the default namespace
                    xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
                }

            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
        }

        private void writeQNames(javax.xml.namespace.QName[] qnames,
                                 javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {

            if (qnames != null) {
                // we have to store this data until last moment since it is not possible to write any
                // namespace data after writing the charactor data
                java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
                java.lang.String namespaceURI = null;
                java.lang.String prefix = null;

                for (int i = 0; i < qnames.length; i++) {
                    if (i > 0) {
                        stringToWrite.append(" ");
                    }
                    namespaceURI = qnames[i].getNamespaceURI();
                    if (namespaceURI != null) {
                        prefix = xmlWriter.getPrefix(namespaceURI);
                        if ((prefix == null) || (prefix.length() == 0)) {
                            prefix = generatePrefix(namespaceURI);
                            xmlWriter.writeNamespace(prefix, namespaceURI);
                            xmlWriter.setPrefix(prefix,namespaceURI);
                        }

                        if (prefix.trim().length() > 0){
                            stringToWrite.append(prefix).append(":").append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        } else {
                            stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                        }
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                }
                xmlWriter.writeCharacters(stringToWrite.toString());
            }

        }


         /**
         * Register a namespace prefix
         */
         private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException {
                java.lang.String prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null) {
                    prefix = generatePrefix(namespace);

                    while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null) {
                        prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
                    }

                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }

                return prefix;
            }


  
        /**
        * databinding method to get an XML representation of this object
        *
        */
        public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName)
                    throws org.apache.axis2.databinding.ADBException{


        
                 java.util.ArrayList elementList = new java.util.ArrayList();
                 java.util.ArrayList attribList = new java.util.ArrayList();

                
                             if (localSessionReportTypeChoice_type0!=null) {
                                 for (int i = 0;i < localSessionReportTypeChoice_type0.length;i++){

                                    if (localSessionReportTypeChoice_type0[i] != null){
                                         elementList.add(new javax.xml.namespace.QName("http://remotelabs.eng.uts.edu.au/schedserver/reports",
                                                                          "SessionReportTypeChoice_type0"));
                                         elementList.add(localSessionReportTypeChoice_type0[i]);
                                    } else {
                                        
                                               throw new org.apache.axis2.databinding.ADBException("SessionReportTypeChoice_type0 cannot be null !!");
                                            
                                    }

                                 }
                             } else {
                                 
                                        throw new org.apache.axis2.databinding.ADBException("SessionReportTypeChoice_type0 cannot be null!!");
                                    
                             }

                        
                                      elementList.add(new javax.xml.namespace.QName("",
                                                                      "aveQueueDuration"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAveQueueDuration));
                            
                                      elementList.add(new javax.xml.namespace.QName("",
                                                                      "medQueueDuration"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMedQueueDuration));
                            
                                      elementList.add(new javax.xml.namespace.QName("",
                                                                      "minQueueDuration"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMinQueueDuration));
                            
                                      elementList.add(new javax.xml.namespace.QName("",
                                                                      "totalQueueDuration"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTotalQueueDuration));
                            
                                      elementList.add(new javax.xml.namespace.QName("",
                                                                      "sessionCount"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localSessionCount));
                             if (localUserCountTracker){
                                      elementList.add(new javax.xml.namespace.QName("",
                                                                      "userCount"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localUserCount));
                            }
                                      elementList.add(new javax.xml.namespace.QName("",
                                                                      "aveSessionDuration"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localAveSessionDuration));
                            
                                      elementList.add(new javax.xml.namespace.QName("",
                                                                      "medSessionDuration"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMedSessionDuration));
                            
                                      elementList.add(new javax.xml.namespace.QName("",
                                                                      "maxSessionDuration"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMaxSessionDuration));
                            
                                      elementList.add(new javax.xml.namespace.QName("",
                                                                      "minSessionDuration"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localMinSessionDuration));
                            
                                      elementList.add(new javax.xml.namespace.QName("",
                                                                      "totalSessionDuration"));
                                 
                                elementList.add(
                                   org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localTotalSessionDuration));
                            

                return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());
            
            

        }

  

     /**
      *  Factory class that keeps the parse method
      */
    public static class Factory{

        
        

        /**
        * static method to create the object
        * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
        *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
        * Postcondition: If this object is an element, the reader is positioned at its end element
        *                If this object is a complex type, the reader is positioned at the end element of its outer element
        */
        public static SessionReportType parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception{
            SessionReportType object =
                new SessionReportType();

            int event;
            java.lang.String nillableValue = null;
            java.lang.String prefix ="";
            java.lang.String namespaceuri ="";
            try {
                
                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                
                if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!=null){
                  java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                        "type");
                  if (fullTypeName!=null){
                    java.lang.String nsPrefix = null;
                    if (fullTypeName.indexOf(":") > -1){
                        nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                    }
                    nsPrefix = nsPrefix==null?"":nsPrefix;

                    java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);
                    
                            if (!"SessionReportType".equals(type)){
                                //find namespace for the prefix
                                java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                                return (SessionReportType)au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.ExtensionMapper.getTypeObject(
                                     nsUri,type,reader);
                              }
                        

                  }
                

                }

                

                
                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();
                

                 
                    
                    reader.next();
                
                        java.util.ArrayList list1 = new java.util.ArrayList();
                    
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() ){
                                
                                    
                                    
                                    // Process the array and step past its final element's end.
                                    list1.add(au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.SessionReportTypeChoice_type0.Factory.parse(reader));
                                                        //loop until we find a start element that is not part of this array
                                                        boolean loopDone1 = false;
                                                        while(!loopDone1){

                                                            // Step to next element event.
                                                            while (!reader.isStartElement() && !reader.isEndElement())
                                                                reader.next();
                                                            if (reader.isEndElement()){
                                                                //two continuous end elements means we are exiting the xml structure
                                                                loopDone1 = true;
                                                            } else {
                                                                list1.add(au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.SessionReportTypeChoice_type0.Factory.parse(reader));
                                                            }
                                                        }
                                                        // call the converter utility  to convert and set the array
                                                        object.setSessionReportTypeChoice_type0((au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.SessionReportTypeChoice_type0[])
                                                            org.apache.axis2.databinding.utils.ConverterUtil.convertToArray(
                                                                au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.SessionReportTypeChoice_type0.class,
                                                                list1));

                                                 
                              }  // End of if for expected property start element
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","aveQueueDuration").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setAveQueueDuration(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToFloat(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","medQueueDuration").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setMedQueueDuration(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToFloat(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","minQueueDuration").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setMinQueueDuration(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToFloat(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","totalQueueDuration").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setTotalQueueDuration(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToFloat(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","sessionCount").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setSessionCount(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","userCount").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setUserCount(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                    else {
                                        
                                               object.setUserCount(java.lang.Integer.MIN_VALUE);
                                           
                                    }
                                
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","aveSessionDuration").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setAveSessionDuration(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToFloat(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","medSessionDuration").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setMedSessionDuration(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToFloat(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","maxSessionDuration").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setMaxSessionDuration(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToFloat(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","minSessionDuration").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setMinSessionDuration(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToFloat(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                            
                                    
                                    while (!reader.isStartElement() && !reader.isEndElement()) reader.next();
                                
                                    if (reader.isStartElement() && new javax.xml.namespace.QName("","totalSessionDuration").equals(reader.getName())){
                                
                                    java.lang.String content = reader.getElementText();
                                    
                                              object.setTotalSessionDuration(
                                                    org.apache.axis2.databinding.utils.ConverterUtil.convertToFloat(content));
                                              
                                        reader.next();
                                    
                              }  // End of if for expected property start element
                                
                                else{
                                    // A start element we are not expecting indicates an invalid parameter was passed
                                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                                }
                              
                            while (!reader.isStartElement() && !reader.isEndElement())
                                reader.next();
                            
                                if (reader.isStartElement())
                                // A start element we are not expecting indicates a trailing invalid property
                                throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                            



            } catch (javax.xml.stream.XMLStreamException e) {
                throw new java.lang.Exception(e);
            }

            return object;
        }

        }//end of factory class

        

        }
           
          