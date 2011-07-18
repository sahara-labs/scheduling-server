
/**
 * MultiSiteMessageReceiverInOut.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.0  Built on : May 17, 2011 (04:19:43 IST)
 */
        package au.edu.uts.eng.remotelabs.schedserver.multisite.intf;

        /**
        *  MultiSiteMessageReceiverInOut message receiver
        */

        public class MultiSiteMessageReceiverInOut extends AbstractInOutMessageReceiver{


        public void invokeBusinessLogic(MessageContext msgContext, MessageContext newMsgContext)
        throws AxisFault{

        try {

        // get the implementation class for the Web Service
        Object obj = getTheImplementationObject(msgContext);

        MultiSiteInterface skel = (MultiSiteInterface)obj;
        //Out Envelop
        org.apache.axiom.soap.SOAPEnvelope envelope = null;
        //Find the axisOperation that has been set by the Dispatch phase.
        description.AxisOperation op = msgContext.getOperationContext().getAxisOperation();
        if (op == null) {
        throw new AxisFault("Operation is not located, if this is doclit style the SOAP-ACTION should specified via the SOAP Action to use the RawXMLProvider");
        }

        String methodName;
        if((op.getName() != null) && ((methodName = JavaUtils.xmlNameToJavaIdentifier(op.getName().getLocalPart())) != null)){


        

            if("getUserStatus".equals(methodName)){
                
                GetUserStatusResponse getUserStatusResponse19 = null;
	                        GetUserStatus wrappedParam =
                                                             (GetUserStatus)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    GetUserStatus.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getUserStatusResponse19 =
                                                   
                                                   
                                                         skel.getUserStatus(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getUserStatusResponse19, false, new javax.xml.namespace.QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite",
                                                    "getUserStatus"));
                                    } else 

            if("cancelBooking".equals(methodName)){
                
                CancelBookingResponse cancelBookingResponse21 = null;
	                        CancelBooking wrappedParam =
                                                             (CancelBooking)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    CancelBooking.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               cancelBookingResponse21 =
                                                   
                                                   
                                                         skel.cancelBooking(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), cancelBookingResponse21, false, new javax.xml.namespace.QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite",
                                                    "cancelBooking"));
                                    } else 

            if("checkAvailability".equals(methodName)){
                
                CheckAvailabilityResponse checkAvailabilityResponse23 = null;
	                        CheckAvailability wrappedParam =
                                                             (CheckAvailability)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    CheckAvailability.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               checkAvailabilityResponse23 =
                                                   
                                                   
                                                         skel.checkAvailability(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), checkAvailabilityResponse23, false, new javax.xml.namespace.QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite",
                                                    "checkAvailability"));
                                    } else 

            if("findFreeBookings".equals(methodName)){
                
                FindFreeBookingsResponse findFreeBookingsResponse25 = null;
	                        FindFreeBookings wrappedParam =
                                                             (FindFreeBookings)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    FindFreeBookings.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               findFreeBookingsResponse25 =
                                                   
                                                   
                                                         skel.findFreeBookings(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), findFreeBookingsResponse25, false, new javax.xml.namespace.QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite",
                                                    "findFreeBookings"));
                                    } else 

            if("getSessionInformation".equals(methodName)){
                
                GetSessionInformationResponse getSessionInformationResponse27 = null;
	                        GetSessionInformation wrappedParam =
                                                             (GetSessionInformation)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    GetSessionInformation.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getSessionInformationResponse27 =
                                                   
                                                   
                                                         skel.getSessionInformation(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getSessionInformationResponse27, false, new javax.xml.namespace.QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite",
                                                    "getSessionInformation"));
                                    } else 

            if("getQueuePosition".equals(methodName)){
                
                GetQueuePositionResponse getQueuePositionResponse29 = null;
	                        GetQueuePosition wrappedParam =
                                                             (GetQueuePosition)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    GetQueuePosition.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getQueuePositionResponse29 =
                                                   
                                                   
                                                         skel.getQueuePosition(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getQueuePositionResponse29, false, new javax.xml.namespace.QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite",
                                                    "getQueuePosition"));
                                    } else 

            if("addToQueue".equals(methodName)){
                
                AddToQueueResponse addToQueueResponse31 = null;
	                        AddToQueue wrappedParam =
                                                             (AddToQueue)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    AddToQueue.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               addToQueueResponse31 =
                                                   
                                                   
                                                         skel.addToQueue(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), addToQueueResponse31, false, new javax.xml.namespace.QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite",
                                                    "addToQueue"));
                                    } else 

            if("createBooking".equals(methodName)){
                
                CreateBookingResponse createBookingResponse33 = null;
	                        CreateBooking wrappedParam =
                                                             (CreateBooking)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    CreateBooking.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               createBookingResponse33 =
                                                   
                                                   
                                                         skel.createBooking(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), createBookingResponse33, false, new javax.xml.namespace.QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite",
                                                    "createBooking"));
                                    } else 

            if("finishSession".equals(methodName)){
                
                FinishSessionResponse finishSessionResponse35 = null;
	                        FinishSession wrappedParam =
                                                             (FinishSession)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    FinishSession.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               finishSessionResponse35 =
                                                   
                                                   
                                                         skel.finishSession(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), finishSessionResponse35, false, new javax.xml.namespace.QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite",
                                                    "finishSession"));
                                    
            } else {
              throw new RuntimeException("method not found");
            }
        

        newMsgContext.setEnvelope(envelope);
        }
        }
        catch (Exception e) {
        throw AxisFault.makeFault(e);
        }
        }
        
        //
            private  OMElement  toOM(GetUserStatus param, boolean optimizeContent)
            throws AxisFault {

            
                        try{
                             return param.getOMElement(GetUserStatus.MY_QNAME,
                                          OMAbstractFactory.getOMFactory());
                        } catch(databinding.ADBException e){
                            throw AxisFault.makeFault(e);
                        }
                    

            }
        
            private  OMElement  toOM(GetUserStatusResponse param, boolean optimizeContent)
            throws AxisFault {

            
                        try{
                             return param.getOMElement(GetUserStatusResponse.MY_QNAME,
                                          OMAbstractFactory.getOMFactory());
                        } catch(databinding.ADBException e){
                            throw AxisFault.makeFault(e);
                        }
                    

            }
        
            private  OMElement  toOM(CancelBooking param, boolean optimizeContent)
            throws AxisFault {

            
                        try{
                             return param.getOMElement(CancelBooking.MY_QNAME,
                                          OMAbstractFactory.getOMFactory());
                        } catch(databinding.ADBException e){
                            throw AxisFault.makeFault(e);
                        }
                    

            }
        
            private  OMElement  toOM(CancelBookingResponse param, boolean optimizeContent)
            throws AxisFault {

            
                        try{
                             return param.getOMElement(CancelBookingResponse.MY_QNAME,
                                          OMAbstractFactory.getOMFactory());
                        } catch(databinding.ADBException e){
                            throw AxisFault.makeFault(e);
                        }
                    

            }
        
            private  OMElement  toOM(CheckAvailability param, boolean optimizeContent)
            throws AxisFault {

            
                        try{
                             return param.getOMElement(CheckAvailability.MY_QNAME,
                                          OMAbstractFactory.getOMFactory());
                        } catch(databinding.ADBException e){
                            throw AxisFault.makeFault(e);
                        }
                    

            }
        
            private  OMElement  toOM(CheckAvailabilityResponse param, boolean optimizeContent)
            throws AxisFault {

            
                        try{
                             return param.getOMElement(CheckAvailabilityResponse.MY_QNAME,
                                          OMAbstractFactory.getOMFactory());
                        } catch(databinding.ADBException e){
                            throw AxisFault.makeFault(e);
                        }
                    

            }
        
            private  OMElement  toOM(FindFreeBookings param, boolean optimizeContent)
            throws AxisFault {

            
                        try{
                             return param.getOMElement(FindFreeBookings.MY_QNAME,
                                          OMAbstractFactory.getOMFactory());
                        } catch(databinding.ADBException e){
                            throw AxisFault.makeFault(e);
                        }
                    

            }
        
            private  OMElement  toOM(FindFreeBookingsResponse param, boolean optimizeContent)
            throws AxisFault {

            
                        try{
                             return param.getOMElement(FindFreeBookingsResponse.MY_QNAME,
                                          OMAbstractFactory.getOMFactory());
                        } catch(databinding.ADBException e){
                            throw AxisFault.makeFault(e);
                        }
                    

            }
        
            private  OMElement  toOM(GetSessionInformation param, boolean optimizeContent)
            throws AxisFault {

            
                        try{
                             return param.getOMElement(GetSessionInformation.MY_QNAME,
                                          OMAbstractFactory.getOMFactory());
                        } catch(databinding.ADBException e){
                            throw AxisFault.makeFault(e);
                        }
                    

            }
        
            private  OMElement  toOM(GetSessionInformationResponse param, boolean optimizeContent)
            throws AxisFault {

            
                        try{
                             return param.getOMElement(GetSessionInformationResponse.MY_QNAME,
                                          OMAbstractFactory.getOMFactory());
                        } catch(databinding.ADBException e){
                            throw AxisFault.makeFault(e);
                        }
                    

            }
        
            private  OMElement  toOM(GetQueuePosition param, boolean optimizeContent)
            throws AxisFault {

            
                        try{
                             return param.getOMElement(GetQueuePosition.MY_QNAME,
                                          OMAbstractFactory.getOMFactory());
                        } catch(databinding.ADBException e){
                            throw AxisFault.makeFault(e);
                        }
                    

            }
        
            private  OMElement  toOM(GetQueuePositionResponse param, boolean optimizeContent)
            throws AxisFault {

            
                        try{
                             return param.getOMElement(GetQueuePositionResponse.MY_QNAME,
                                          OMAbstractFactory.getOMFactory());
                        } catch(databinding.ADBException e){
                            throw AxisFault.makeFault(e);
                        }
                    

            }
        
            private  OMElement  toOM(AddToQueue param, boolean optimizeContent)
            throws AxisFault {

            
                        try{
                             return param.getOMElement(AddToQueue.MY_QNAME,
                                          OMAbstractFactory.getOMFactory());
                        } catch(databinding.ADBException e){
                            throw AxisFault.makeFault(e);
                        }
                    

            }
        
            private  OMElement  toOM(AddToQueueResponse param, boolean optimizeContent)
            throws AxisFault {

            
                        try{
                             return param.getOMElement(AddToQueueResponse.MY_QNAME,
                                          OMAbstractFactory.getOMFactory());
                        } catch(databinding.ADBException e){
                            throw AxisFault.makeFault(e);
                        }
                    

            }
        
            private  OMElement  toOM(CreateBooking param, boolean optimizeContent)
            throws AxisFault {

            
                        try{
                             return param.getOMElement(CreateBooking.MY_QNAME,
                                          OMAbstractFactory.getOMFactory());
                        } catch(databinding.ADBException e){
                            throw AxisFault.makeFault(e);
                        }
                    

            }
        
            private  OMElement  toOM(CreateBookingResponse param, boolean optimizeContent)
            throws AxisFault {

            
                        try{
                             return param.getOMElement(CreateBookingResponse.MY_QNAME,
                                          OMAbstractFactory.getOMFactory());
                        } catch(databinding.ADBException e){
                            throw AxisFault.makeFault(e);
                        }
                    

            }
        
            private  OMElement  toOM(FinishSession param, boolean optimizeContent)
            throws AxisFault {

            
                        try{
                             return param.getOMElement(FinishSession.MY_QNAME,
                                          OMAbstractFactory.getOMFactory());
                        } catch(databinding.ADBException e){
                            throw AxisFault.makeFault(e);
                        }
                    

            }
        
            private  OMElement  toOM(FinishSessionResponse param, boolean optimizeContent)
            throws AxisFault {

            
                        try{
                             return param.getOMElement(FinishSessionResponse.MY_QNAME,
                                          OMAbstractFactory.getOMFactory());
                        } catch(databinding.ADBException e){
                            throw AxisFault.makeFault(e);
                        }
                    

            }
        
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, GetUserStatusResponse param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
                        throws AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(GetUserStatusResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(databinding.ADBException e){
                        throw AxisFault.makeFault(e);
                    }
                    }
                    
                         private GetUserStatusResponse wrapgetUserStatus(){
                                GetUserStatusResponse wrappedElement = new GetUserStatusResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, CancelBookingResponse param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
                        throws AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(CancelBookingResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(databinding.ADBException e){
                        throw AxisFault.makeFault(e);
                    }
                    }
                    
                         private CancelBookingResponse wrapcancelBooking(){
                                CancelBookingResponse wrappedElement = new CancelBookingResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, CheckAvailabilityResponse param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
                        throws AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(CheckAvailabilityResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(databinding.ADBException e){
                        throw AxisFault.makeFault(e);
                    }
                    }
                    
                         private CheckAvailabilityResponse wrapcheckAvailability(){
                                CheckAvailabilityResponse wrappedElement = new CheckAvailabilityResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, FindFreeBookingsResponse param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
                        throws AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(FindFreeBookingsResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(databinding.ADBException e){
                        throw AxisFault.makeFault(e);
                    }
                    }
                    
                         private FindFreeBookingsResponse wrapfindFreeBookings(){
                                FindFreeBookingsResponse wrappedElement = new FindFreeBookingsResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, GetSessionInformationResponse param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
                        throws AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(GetSessionInformationResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(databinding.ADBException e){
                        throw AxisFault.makeFault(e);
                    }
                    }
                    
                         private GetSessionInformationResponse wrapgetSessionInformation(){
                                GetSessionInformationResponse wrappedElement = new GetSessionInformationResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, GetQueuePositionResponse param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
                        throws AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(GetQueuePositionResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(databinding.ADBException e){
                        throw AxisFault.makeFault(e);
                    }
                    }
                    
                         private GetQueuePositionResponse wrapgetQueuePosition(){
                                GetQueuePositionResponse wrappedElement = new GetQueuePositionResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, AddToQueueResponse param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
                        throws AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(AddToQueueResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(databinding.ADBException e){
                        throw AxisFault.makeFault(e);
                    }
                    }
                    
                         private AddToQueueResponse wrapaddToQueue(){
                                AddToQueueResponse wrappedElement = new AddToQueueResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, CreateBookingResponse param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
                        throws AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(CreateBookingResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(databinding.ADBException e){
                        throw AxisFault.makeFault(e);
                    }
                    }
                    
                         private CreateBookingResponse wrapcreateBooking(){
                                CreateBookingResponse wrappedElement = new CreateBookingResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, FinishSessionResponse param, boolean optimizeContent, javax.xml.namespace.QName methodQName)
                        throws AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(FinishSessionResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(databinding.ADBException e){
                        throw AxisFault.makeFault(e);
                    }
                    }
                    
                         private FinishSessionResponse wrapfinishSession(){
                                FinishSessionResponse wrappedElement = new FinishSessionResponse();
                                return wrappedElement;
                         }
                    


        /**
        *  get the default envelope
        */
        private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory){
        return factory.getDefaultEnvelope();
        }


        private  Object fromOM(
        OMElement param,
        Class type,
        java.util.Map extraNamespaces) throws AxisFault{

        try {
        
                if (GetUserStatus.class.equals(type)){
                
                           return GetUserStatus.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (GetUserStatusResponse.class.equals(type)){
                
                           return GetUserStatusResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (CancelBooking.class.equals(type)){
                
                           return CancelBooking.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (CancelBookingResponse.class.equals(type)){
                
                           return CancelBookingResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (CheckAvailability.class.equals(type)){
                
                           return CheckAvailability.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (CheckAvailabilityResponse.class.equals(type)){
                
                           return CheckAvailabilityResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (FindFreeBookings.class.equals(type)){
                
                           return FindFreeBookings.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (FindFreeBookingsResponse.class.equals(type)){
                
                           return FindFreeBookingsResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (GetSessionInformation.class.equals(type)){
                
                           return GetSessionInformation.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (GetSessionInformationResponse.class.equals(type)){
                
                           return GetSessionInformationResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (GetQueuePosition.class.equals(type)){
                
                           return GetQueuePosition.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (GetQueuePositionResponse.class.equals(type)){
                
                           return GetQueuePositionResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (AddToQueue.class.equals(type)){
                
                           return AddToQueue.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (AddToQueueResponse.class.equals(type)){
                
                           return AddToQueueResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (CreateBooking.class.equals(type)){
                
                           return CreateBooking.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (CreateBookingResponse.class.equals(type)){
                
                           return CreateBookingResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (FinishSession.class.equals(type)){
                
                           return FinishSession.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (FinishSessionResponse.class.equals(type)){
                
                           return FinishSessionResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
        } catch (Exception e) {
        throw AxisFault.makeFault(e);
        }
           return null;
        }



    

        /**
        *  A utility method that copies the namepaces from the SOAPEnvelope
        */
        private java.util.Map getEnvelopeNamespaces(org.apache.axiom.soap.SOAPEnvelope env){
        java.util.Map returnMap = new java.util.HashMap();
        java.util.Iterator namespaceIterator = env.getAllDeclaredNamespaces();
        while (namespaceIterator.hasNext()) {
        OMNamespace ns = (OMNamespace) namespaceIterator.next();
        returnMap.put(ns.getPrefix(),ns.getNamespaceURI());
        }
        return returnMap;
        }

        private AxisFault createAxisFault(Exception e) {
        AxisFault f;
        Throwable cause = e.getCause();
        if (cause != null) {
            f = new AxisFault(e.getMessage(), cause);
        } else {
            f = new AxisFault(e.getMessage());
        }

        return f;
    }

        }//end of class
    