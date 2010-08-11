
/**
 * ScormPackagerMessageReceiverInOut.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4  Built on : Apr 26, 2008 (06:24:30 EDT)
 */
        package au.edu.labshare.schedserver.scormpackager.service;

        /**
        *  ScormPackagerMessageReceiverInOut message receiver
        */

        public class ScormPackagerMessageReceiverInOut extends org.apache.axis2.receivers.AbstractInOutMessageReceiver{


        public void invokeBusinessLogic(org.apache.axis2.context.MessageContext msgContext, org.apache.axis2.context.MessageContext newMsgContext)
        throws org.apache.axis2.AxisFault{

        try {

        // get the implementation class for the Web Service
        Object obj = getTheImplementationObject(msgContext);

        ScormPackagerSkeletonInterface skel = (ScormPackagerSkeletonInterface)obj;
        //Out Envelop
        org.apache.axiom.soap.SOAPEnvelope envelope = null;
        //Find the axisOperation that has been set by the Dispatch phase.
        org.apache.axis2.description.AxisOperation op = msgContext.getOperationContext().getAxisOperation();
        if (op == null) {
        throw new org.apache.axis2.AxisFault("Operation is not located, if this is doclit style the SOAP-ACTION should specified via the SOAP Action to use the RawXMLProvider");
        }

        java.lang.String methodName;
        if((op.getName() != null) && ((methodName = org.apache.axis2.util.JavaUtils.xmlNameToJava(op.getName().getLocalPart())) != null)){

        

            if("deleteSCO".equals(methodName)){
                
                au.edu.labshare.schedserver.scormpackager.types.DeleteSCOResponse deleteSCOResponse1 = null;
	                        au.edu.labshare.schedserver.scormpackager.types.DeleteSCO wrappedParam =
                                                             (au.edu.labshare.schedserver.scormpackager.types.DeleteSCO)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    au.edu.labshare.schedserver.scormpackager.types.DeleteSCO.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               deleteSCOResponse1 =
                                                   
                                                   
                                                         skel.deleteSCO(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), deleteSCOResponse1, false);
                                    } else 

            if("validateSCO".equals(methodName)){
                
                au.edu.labshare.schedserver.scormpackager.types.ValidateSCOResponse validateSCOResponse3 = null;
	                        au.edu.labshare.schedserver.scormpackager.types.ValidateSCO wrappedParam =
                                                             (au.edu.labshare.schedserver.scormpackager.types.ValidateSCO)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    au.edu.labshare.schedserver.scormpackager.types.ValidateSCO.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               validateSCOResponse3 =
                                                   
                                                   
                                                         skel.validateSCO(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), validateSCOResponse3, false);
                                    } else 

            if("createPIF".equals(methodName)){
                
                au.edu.labshare.schedserver.scormpackager.types.CreatePIFResponse createPIFResponse5 = null;
	                        au.edu.labshare.schedserver.scormpackager.types.CreatePIF wrappedParam =
                                                             (au.edu.labshare.schedserver.scormpackager.types.CreatePIF)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    au.edu.labshare.schedserver.scormpackager.types.CreatePIF.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               createPIFResponse5 =
                                                   
                                                   
                                                         skel.createPIF(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), createPIFResponse5, false);
                                    } else 

            if("createSCO".equals(methodName)){
                
                au.edu.labshare.schedserver.scormpackager.types.CreateSCOResponse createSCOResponse7 = null;
	                        au.edu.labshare.schedserver.scormpackager.types.CreateSCO wrappedParam =
                                                             (au.edu.labshare.schedserver.scormpackager.types.CreateSCO)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    au.edu.labshare.schedserver.scormpackager.types.CreateSCO.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               createSCOResponse7 =
                                                   
                                                   
                                                         skel.createSCO(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), createSCOResponse7, false);
                                    } else 

            if("validatePIF".equals(methodName)){
                
                au.edu.labshare.schedserver.scormpackager.types.ValidatePIFResponse validatePIFResponse9 = null;
	                        au.edu.labshare.schedserver.scormpackager.types.ValidatePIF wrappedParam =
                                                             (au.edu.labshare.schedserver.scormpackager.types.ValidatePIF)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    au.edu.labshare.schedserver.scormpackager.types.ValidatePIF.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               validatePIFResponse9 =
                                                   
                                                   
                                                         skel.validatePIF(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), validatePIFResponse9, false);
                                    } else 

            if("deletePIF".equals(methodName)){
                
                au.edu.labshare.schedserver.scormpackager.types.DeletePIFResponse deletePIFResponse11 = null;
	                        au.edu.labshare.schedserver.scormpackager.types.DeletePIF wrappedParam =
                                                             (au.edu.labshare.schedserver.scormpackager.types.DeletePIF)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    au.edu.labshare.schedserver.scormpackager.types.DeletePIF.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               deletePIFResponse11 =
                                                   
                                                   
                                                         skel.deletePIF(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), deletePIFResponse11, false);
                                    } else 

            if("validateManifest".equals(methodName)){
                
                au.edu.labshare.schedserver.scormpackager.types.ValidateManifestResponse validateManifestResponse13 = null;
	                        au.edu.labshare.schedserver.scormpackager.types.ValidateManifest wrappedParam =
                                                             (au.edu.labshare.schedserver.scormpackager.types.ValidateManifest)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    au.edu.labshare.schedserver.scormpackager.types.ValidateManifest.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               validateManifestResponse13 =
                                                   
                                                   
                                                         skel.validateManifest(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), validateManifestResponse13, false);
                                    
            } else {
              throw new java.lang.RuntimeException("method not found");
            }
        

        newMsgContext.setEnvelope(envelope);
        }
        }
        catch (java.lang.Exception e) {
        throw org.apache.axis2.AxisFault.makeFault(e);
        }
        }
        
     
        
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, au.edu.labshare.schedserver.scormpackager.types.DeleteSCOResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(au.edu.labshare.schedserver.scormpackager.types.DeleteSCOResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }

                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, au.edu.labshare.schedserver.scormpackager.types.ValidateSCOResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(au.edu.labshare.schedserver.scormpackager.types.ValidateSCOResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }

                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, au.edu.labshare.schedserver.scormpackager.types.CreatePIFResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(au.edu.labshare.schedserver.scormpackager.types.CreatePIFResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }

                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, au.edu.labshare.schedserver.scormpackager.types.CreateSCOResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(au.edu.labshare.schedserver.scormpackager.types.CreateSCOResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, au.edu.labshare.schedserver.scormpackager.types.ValidatePIFResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(au.edu.labshare.schedserver.scormpackager.types.ValidatePIFResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, au.edu.labshare.schedserver.scormpackager.types.DeletePIFResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(au.edu.labshare.schedserver.scormpackager.types.DeletePIFResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, au.edu.labshare.schedserver.scormpackager.types.ValidateManifestResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(au.edu.labshare.schedserver.scormpackager.types.ValidateManifestResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }

        @SuppressWarnings("unchecked")
		private  java.lang.Object fromOM(
        org.apache.axiom.om.OMElement param,
        java.lang.Class type,
        java.util.Map extraNamespaces) throws org.apache.axis2.AxisFault{

        try {
        
                if (au.edu.labshare.schedserver.scormpackager.types.DeleteSCO.class.equals(type)){
                
                           return au.edu.labshare.schedserver.scormpackager.types.DeleteSCO.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.labshare.schedserver.scormpackager.types.DeleteSCOResponse.class.equals(type)){
                
                           return au.edu.labshare.schedserver.scormpackager.types.DeleteSCOResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.labshare.schedserver.scormpackager.types.ValidateSCO.class.equals(type)){
                
                           return au.edu.labshare.schedserver.scormpackager.types.ValidateSCO.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.labshare.schedserver.scormpackager.types.ValidateSCOResponse.class.equals(type)){
                
                           return au.edu.labshare.schedserver.scormpackager.types.ValidateSCOResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.labshare.schedserver.scormpackager.types.CreatePIF.class.equals(type)){
                
                           return au.edu.labshare.schedserver.scormpackager.types.CreatePIF.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.labshare.schedserver.scormpackager.types.CreatePIFResponse.class.equals(type)){
                
                           return au.edu.labshare.schedserver.scormpackager.types.CreatePIFResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.labshare.schedserver.scormpackager.types.CreateSCO.class.equals(type)){
                
                           return au.edu.labshare.schedserver.scormpackager.types.CreateSCO.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.labshare.schedserver.scormpackager.types.CreateSCOResponse.class.equals(type)){
                
                           return au.edu.labshare.schedserver.scormpackager.types.CreateSCOResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.labshare.schedserver.scormpackager.types.ValidatePIF.class.equals(type)){
                
                           return au.edu.labshare.schedserver.scormpackager.types.ValidatePIF.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.labshare.schedserver.scormpackager.types.ValidatePIFResponse.class.equals(type)){
                
                           return au.edu.labshare.schedserver.scormpackager.types.ValidatePIFResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.labshare.schedserver.scormpackager.types.DeletePIF.class.equals(type)){
                
                           return au.edu.labshare.schedserver.scormpackager.types.DeletePIF.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.labshare.schedserver.scormpackager.types.DeletePIFResponse.class.equals(type)){
                
                           return au.edu.labshare.schedserver.scormpackager.types.DeletePIFResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.labshare.schedserver.scormpackager.types.ValidateManifest.class.equals(type)){
                
                           return au.edu.labshare.schedserver.scormpackager.types.ValidateManifest.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (au.edu.labshare.schedserver.scormpackager.types.ValidateManifestResponse.class.equals(type)){
                
                           return au.edu.labshare.schedserver.scormpackager.types.ValidateManifestResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
        } catch (java.lang.Exception e) {
        throw org.apache.axis2.AxisFault.makeFault(e);
        }
           return null;
        }



    

        /**
        *  A utility method that copies the namepaces from the SOAPEnvelope
        */
        @SuppressWarnings("unchecked")
		private java.util.Map getEnvelopeNamespaces(org.apache.axiom.soap.SOAPEnvelope env){
        java.util.Map returnMap = new java.util.HashMap();
        java.util.Iterator namespaceIterator = env.getAllDeclaredNamespaces();
        while (namespaceIterator.hasNext()) {
        org.apache.axiom.om.OMNamespace ns = (org.apache.axiom.om.OMNamespace) namespaceIterator.next();
        returnMap.put(ns.getPrefix(),ns.getNamespaceURI());
        }
        return returnMap;
        }

        }//end of class
    