
/**
 * LabConnectorMessageReceiverInOut.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4  Built on : Apr 26, 2008 (06:24:30 EDT)
 */
        package au.edu.labshare.schedserver.labconnector.service;
        
        import au.edu.labshare.schedserver.labconnector.service.types.CancelBookingTime;
        import au.edu.labshare.schedserver.labconnector.service.types.CancelBookingTimeResponse;
        import au.edu.labshare.schedserver.labconnector.service.types.CancelMaintenanceTime;
        import au.edu.labshare.schedserver.labconnector.service.types.CancelMaintenanceTimeResponse;
        import au.edu.labshare.schedserver.labconnector.service.types.DeleteSavedUserExperimentInput;
        import au.edu.labshare.schedserver.labconnector.service.types.DeleteSavedUserExperimentInputResponse;
        import au.edu.labshare.schedserver.labconnector.service.types.GetExperimentID;
        import au.edu.labshare.schedserver.labconnector.service.types.GetExperimentIDResponse;
        import au.edu.labshare.schedserver.labconnector.service.types.GetExperimentResults;
        import au.edu.labshare.schedserver.labconnector.service.types.GetExperimentResultsResponse;
        import au.edu.labshare.schedserver.labconnector.service.types.GetExperimentSpecs;
        import au.edu.labshare.schedserver.labconnector.service.types.GetExperimentSpecsResponse;
        import au.edu.labshare.schedserver.labconnector.service.types.GetExperimentStatus;
        import au.edu.labshare.schedserver.labconnector.service.types.GetExperimentStatusResponse;
        import au.edu.labshare.schedserver.labconnector.service.types.GetExperimentType;
        import au.edu.labshare.schedserver.labconnector.service.types.GetExperimentTypeResponse;
        import au.edu.labshare.schedserver.labconnector.service.types.GetInteractiveExperimentSession;
        import au.edu.labshare.schedserver.labconnector.service.types.GetInteractiveExperimentSessionResponse;
        import au.edu.labshare.schedserver.labconnector.service.types.GetLabID;
        import au.edu.labshare.schedserver.labconnector.service.types.GetLabIDResponse;
        import au.edu.labshare.schedserver.labconnector.service.types.GetLabInfo;
        import au.edu.labshare.schedserver.labconnector.service.types.GetLabInfoResponse;
        import au.edu.labshare.schedserver.labconnector.service.types.GetLabStatus;
        import au.edu.labshare.schedserver.labconnector.service.types.GetLabStatusResponse;
        import au.edu.labshare.schedserver.labconnector.service.types.GetMaintenanceTime;
        import au.edu.labshare.schedserver.labconnector.service.types.GetMaintenanceTimeResponse;
        import au.edu.labshare.schedserver.labconnector.service.types.GetSavedUserExperimentInput;
        import au.edu.labshare.schedserver.labconnector.service.types.GetSavedUserExperimentInputResponse;
        import au.edu.labshare.schedserver.labconnector.service.types.GetToken;
        import au.edu.labshare.schedserver.labconnector.service.types.GetTokenResponse;
        import au.edu.labshare.schedserver.labconnector.service.types.GetUserPermissions;
        import au.edu.labshare.schedserver.labconnector.service.types.GetUserPermissionsResponse;
        import au.edu.labshare.schedserver.labconnector.service.types.ReleaseExperiment;
        import au.edu.labshare.schedserver.labconnector.service.types.ReleaseExperimentResponse;
        import au.edu.labshare.schedserver.labconnector.service.types.ReleaseSlave;
        import au.edu.labshare.schedserver.labconnector.service.types.ReleaseSlaveResponse;
        import au.edu.labshare.schedserver.labconnector.service.types.SaveExperimentResults;
        import au.edu.labshare.schedserver.labconnector.service.types.SaveExperimentResultsResponse;
        import au.edu.labshare.schedserver.labconnector.service.types.SaveUserExperimentInput;
        import au.edu.labshare.schedserver.labconnector.service.types.SaveUserExperimentInputResponse;
        import au.edu.labshare.schedserver.labconnector.service.types.ScheduleBookingTime;
        import au.edu.labshare.schedserver.labconnector.service.types.ScheduleBookingTimeResponse;
        import au.edu.labshare.schedserver.labconnector.service.types.SetMaintenanceTime;
        import au.edu.labshare.schedserver.labconnector.service.types.SetMaintenanceTimeResponse;
        import au.edu.labshare.schedserver.labconnector.service.types.SetUserPermissions;
        import au.edu.labshare.schedserver.labconnector.service.types.SetUserPermissionsResponse;
        import au.edu.labshare.schedserver.labconnector.service.types.SubmitExperiment;
        import au.edu.labshare.schedserver.labconnector.service.types.SubmitExperimentResponse;

        /**
        *  LabConnectorMessageReceiverInOut message receiver
        */

        public class LabConnectorMessageReceiverInOut extends org.apache.axis2.receivers.AbstractInOutMessageReceiver{


        public void invokeBusinessLogic(org.apache.axis2.context.MessageContext msgContext, org.apache.axis2.context.MessageContext newMsgContext)
        throws org.apache.axis2.AxisFault{

        try {

        // get the implementation class for the Web Service
        Object obj = getTheImplementationObject(msgContext);

        LabConnectorSkeletonInterface skel = (LabConnectorSkeletonInterface)obj;
        //Out Envelop
        org.apache.axiom.soap.SOAPEnvelope envelope = null;
        //Find the axisOperation that has been set by the Dispatch phase.
        org.apache.axis2.description.AxisOperation op = msgContext.getOperationContext().getAxisOperation();
        if (op == null) {
        throw new org.apache.axis2.AxisFault("Operation is not located, if this is doclit style the SOAP-ACTION should specified via the SOAP Action to use the RawXMLProvider");
        }

        java.lang.String methodName;
        if((op.getName() != null) && ((methodName = org.apache.axis2.util.JavaUtils.xmlNameToJava(op.getName().getLocalPart())) != null)){

        

            if("getExperimentResults".equals(methodName)){
                
                GetExperimentResultsResponse getExperimentResultsResponse49 = null;
	                        GetExperimentResults wrappedParam =
                                                             (GetExperimentResults)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    GetExperimentResults.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getExperimentResultsResponse49 =
                                                   
                                                   
                                                         skel.getExperimentResults(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getExperimentResultsResponse49, false);
                                    } else 

            if("deleteSavedUserExperimentInput".equals(methodName)){
                
                DeleteSavedUserExperimentInputResponse deleteSavedUserExperimentInputResponse51 = null;
	                        DeleteSavedUserExperimentInput wrappedParam =
                                                             (DeleteSavedUserExperimentInput)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    DeleteSavedUserExperimentInput.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               deleteSavedUserExperimentInputResponse51 =
                                                   
                                                   
                                                         skel.deleteSavedUserExperimentInput(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), deleteSavedUserExperimentInputResponse51, false);
                                    } else 

            if("getSavedUserExperimentInput".equals(methodName)){
                
                GetSavedUserExperimentInputResponse getSavedUserExperimentInputResponse53 = null;
	                        GetSavedUserExperimentInput wrappedParam =
                                                             (GetSavedUserExperimentInput)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    GetSavedUserExperimentInput.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getSavedUserExperimentInputResponse53 =
                                                   
                                                   
                                                         skel.getSavedUserExperimentInput(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getSavedUserExperimentInputResponse53, false);
                                    } else 

            if("setUserPermissions".equals(methodName)){
                
                SetUserPermissionsResponse setUserPermissionsResponse55 = null;
	                        SetUserPermissions wrappedParam =
                                                             (SetUserPermissions)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    SetUserPermissions.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               setUserPermissionsResponse55 =
                                                   
                                                   
                                                         skel.setUserPermissions(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), setUserPermissionsResponse55, false);
                                    } else 

            if("getLabStatus".equals(methodName)){
                
                GetLabStatusResponse getLabStatusResponse57 = null;
	                        GetLabStatus wrappedParam =
                                                             (GetLabStatus)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    GetLabStatus.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getLabStatusResponse57 =
                                                   
                                                   
                                                         skel.getLabStatus(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getLabStatusResponse57, false);
                                    } else 

            if("getExperimentID".equals(methodName)){
                
                GetExperimentIDResponse getExperimentIDResponse59 = null;
	                        GetExperimentID wrappedParam =
                                                             (GetExperimentID)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    GetExperimentID.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getExperimentIDResponse59 =
                                                   
                                                   
                                                         skel.getExperimentID(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getExperimentIDResponse59, false);
                                    } else 

            if("saveExperimentResults".equals(methodName)){
                
                SaveExperimentResultsResponse saveExperimentResultsResponse61 = null;
	                        SaveExperimentResults wrappedParam =
                                                             (SaveExperimentResults)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    SaveExperimentResults.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               saveExperimentResultsResponse61 =
                                                   
                                                   
                                                         skel.saveExperimentResults(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), saveExperimentResultsResponse61, false);
                                    } else 

            if("saveUserExperimentInput".equals(methodName)){
                
                SaveUserExperimentInputResponse saveUserExperimentInputResponse63 = null;
	                        SaveUserExperimentInput wrappedParam =
                                                             (SaveUserExperimentInput)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    SaveUserExperimentInput.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               saveUserExperimentInputResponse63 =
                                                   
                                                   
                                                         skel.saveUserExperimentInput(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), saveUserExperimentInputResponse63, false);
                                    } else 

            if("getExperimentSpecs".equals(methodName)){
                
                GetExperimentSpecsResponse getExperimentSpecsResponse65 = null;
	                        GetExperimentSpecs wrappedParam =
                                                             (GetExperimentSpecs)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    GetExperimentSpecs.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getExperimentSpecsResponse65 =
                                                   
                                                   
                                                         skel.getExperimentSpecs(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getExperimentSpecsResponse65, false);
                                    } else 

            if("getInteractiveExperimentSession".equals(methodName)){
                
                GetInteractiveExperimentSessionResponse getInteractiveExperimentSessionResponse67 = null;
	                        GetInteractiveExperimentSession wrappedParam =
                                                             (GetInteractiveExperimentSession)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    GetInteractiveExperimentSession.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getInteractiveExperimentSessionResponse67 =
                                                   
                                                   
                                                         skel.getInteractiveExperimentSession(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getInteractiveExperimentSessionResponse67, false);
                                    } else 

            if("getMaintenanceTime".equals(methodName)){
                
                GetMaintenanceTimeResponse getMaintenanceTimeResponse69 = null;
	                        GetMaintenanceTime wrappedParam =
                                                             (GetMaintenanceTime)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    GetMaintenanceTime.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getMaintenanceTimeResponse69 =
                                                   
                                                   
                                                         skel.getMaintenanceTime(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getMaintenanceTimeResponse69, false);
                                    } else 

            if("cancelBookingTime".equals(methodName)){
                
                CancelBookingTimeResponse cancelBookingTimeResponse71 = null;
	                        CancelBookingTime wrappedParam =
                                                             (CancelBookingTime)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    CancelBookingTime.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               cancelBookingTimeResponse71 =
                                                   
                                                   
                                                         skel.cancelBookingTime(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), cancelBookingTimeResponse71, false);
                                    } else 

            if("getLabInfo".equals(methodName)){
                
                GetLabInfoResponse getLabInfoResponse73 = null;
	                        GetLabInfo wrappedParam =
                                                             (GetLabInfo)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    GetLabInfo.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getLabInfoResponse73 =
                                                   
                                                   
                                                         skel.getLabInfo(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getLabInfoResponse73, false);
                                    } else 

            if("setMaintenanceTime".equals(methodName)){
                
                SetMaintenanceTimeResponse setMaintenanceTimeResponse75 = null;
	                        SetMaintenanceTime wrappedParam =
                                                             (SetMaintenanceTime)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    SetMaintenanceTime.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               setMaintenanceTimeResponse75 =
                                                   
                                                   
                                                         skel.setMaintenanceTime(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), setMaintenanceTimeResponse75, false);
                                    } else 

            if("getLabID".equals(methodName)){
                
                GetLabIDResponse getLabIDResponse77 = null;
	                        GetLabID wrappedParam =
                                                             (GetLabID)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    GetLabID.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getLabIDResponse77 =
                                                   
                                                   
                                                         skel.getLabID(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getLabIDResponse77, false);
                                    } else 

            if("submitExperiment".equals(methodName)){
                
                SubmitExperimentResponse submitExperimentResponse79 = null;
	                        SubmitExperiment wrappedParam =
                                                             (SubmitExperiment)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    SubmitExperiment.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               submitExperimentResponse79 =
                                                   
                                                   
                                                         skel.submitExperiment(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), submitExperimentResponse79, false);
                                    } else 

            if("scheduleBookingTime".equals(methodName)){
                
                ScheduleBookingTimeResponse scheduleBookingTimeResponse81 = null;
	                        ScheduleBookingTime wrappedParam =
                                                             (ScheduleBookingTime)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    ScheduleBookingTime.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               scheduleBookingTimeResponse81 =
                                                   
                                                   
                                                         skel.scheduleBookingTime(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), scheduleBookingTimeResponse81, false);
                                    } else 

            if("getToken".equals(methodName)){
                
                GetTokenResponse getTokenResponse83 = null;
	                        GetToken wrappedParam =
                                                             (GetToken)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    GetToken.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getTokenResponse83 =
                                                   
                                                   
                                                         skel.getToken(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getTokenResponse83, false);
                                    } else 

            if("cancelMaintenanceTime".equals(methodName)){
                
                CancelMaintenanceTimeResponse cancelMaintenanceTimeResponse85 = null;
	                        CancelMaintenanceTime wrappedParam =
                                                             (CancelMaintenanceTime)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    CancelMaintenanceTime.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               cancelMaintenanceTimeResponse85 =
                                                   
                                                   
                                                         skel.cancelMaintenanceTime(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), cancelMaintenanceTimeResponse85, false);
                                    } else 

            if("releaseSlave".equals(methodName)){
                
                ReleaseSlaveResponse releaseSlaveResponse87 = null;
	                        ReleaseSlave wrappedParam =
                                                             (ReleaseSlave)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    ReleaseSlave.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               releaseSlaveResponse87 =
                                                   
                                                   
                                                         skel.releaseSlave(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), releaseSlaveResponse87, false);
                                    } else 

            if("getExperimentType".equals(methodName)){
                
                GetExperimentTypeResponse getExperimentTypeResponse89 = null;
	                        GetExperimentType wrappedParam =
                                                             (GetExperimentType)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    GetExperimentType.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getExperimentTypeResponse89 =
                                                   
                                                   
                                                         skel.getExperimentType(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getExperimentTypeResponse89, false);
                                    } else 

            if("getExperimentStatus".equals(methodName)){
                
                GetExperimentStatusResponse getExperimentStatusResponse91 = null;
	                        GetExperimentStatus wrappedParam =
                                                             (GetExperimentStatus)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    GetExperimentStatus.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getExperimentStatusResponse91 =
                                                   
                                                   
                                                         skel.getExperimentStatus(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getExperimentStatusResponse91, false);
                                    } else 

            if("releaseExperiment".equals(methodName)){
                
                ReleaseExperimentResponse releaseExperimentResponse93 = null;
	                        ReleaseExperiment wrappedParam =
                                                             (ReleaseExperiment)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    ReleaseExperiment.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               releaseExperimentResponse93 =
                                                   
                                                   
                                                         skel.releaseExperiment(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), releaseExperimentResponse93, false);
                                    } else 

            if("getUserPermissions".equals(methodName)){
                
                GetUserPermissionsResponse getUserPermissionsResponse95 = null;
	                        GetUserPermissions wrappedParam =
                                                             (GetUserPermissions)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    GetUserPermissions.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               getUserPermissionsResponse95 =
                                                   
                                                   
                                                         skel.getUserPermissions(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), getUserPermissionsResponse95, false);
                                    
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
        
        //
            private  org.apache.axiom.om.OMElement  toOM(GetExperimentResults param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(GetExperimentResults.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(GetExperimentResultsResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(GetExperimentResultsResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(DeleteSavedUserExperimentInput param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(DeleteSavedUserExperimentInput.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(DeleteSavedUserExperimentInputResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(DeleteSavedUserExperimentInputResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(GetSavedUserExperimentInput param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(GetSavedUserExperimentInput.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(GetSavedUserExperimentInputResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(GetSavedUserExperimentInputResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(SetUserPermissions param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(SetUserPermissions.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(SetUserPermissionsResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(SetUserPermissionsResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(GetLabStatus param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(GetLabStatus.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(GetLabStatusResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(GetLabStatusResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(GetExperimentID param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(GetExperimentID.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(GetExperimentIDResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(GetExperimentIDResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(SaveExperimentResults param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(SaveExperimentResults.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(SaveExperimentResultsResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(SaveExperimentResultsResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(SaveUserExperimentInput param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(SaveUserExperimentInput.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(SaveUserExperimentInputResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(SaveUserExperimentInputResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(GetExperimentSpecs param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(GetExperimentSpecs.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(GetExperimentSpecsResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(GetExperimentSpecsResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(GetInteractiveExperimentSession param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(GetInteractiveExperimentSession.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(GetInteractiveExperimentSessionResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(GetInteractiveExperimentSessionResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(GetMaintenanceTime param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(GetMaintenanceTime.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(GetMaintenanceTimeResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(GetMaintenanceTimeResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(CancelBookingTime param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(CancelBookingTime.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(CancelBookingTimeResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(CancelBookingTimeResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(GetLabInfo param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(GetLabInfo.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(GetLabInfoResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(GetLabInfoResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(SetMaintenanceTime param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(SetMaintenanceTime.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(SetMaintenanceTimeResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(SetMaintenanceTimeResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(GetLabID param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(GetLabID.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(GetLabIDResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(GetLabIDResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(SubmitExperiment param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(SubmitExperiment.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(SubmitExperimentResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(SubmitExperimentResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(ScheduleBookingTime param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(ScheduleBookingTime.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(ScheduleBookingTimeResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(ScheduleBookingTimeResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(GetToken param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(GetToken.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(GetTokenResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(GetTokenResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(CancelMaintenanceTime param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(CancelMaintenanceTime.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(CancelMaintenanceTimeResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(CancelMaintenanceTimeResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(ReleaseSlave param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(ReleaseSlave.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(ReleaseSlaveResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(ReleaseSlaveResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(GetExperimentType param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(GetExperimentType.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(GetExperimentTypeResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(GetExperimentTypeResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(GetExperimentStatus param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(GetExperimentStatus.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(GetExperimentStatusResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(GetExperimentStatusResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(ReleaseExperiment param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(ReleaseExperiment.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(ReleaseExperimentResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(ReleaseExperimentResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(GetUserPermissions param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(GetUserPermissions.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
            private  org.apache.axiom.om.OMElement  toOM(GetUserPermissionsResponse param, boolean optimizeContent)
            throws org.apache.axis2.AxisFault {

            
                        try{
                             return param.getOMElement(GetUserPermissionsResponse.MY_QNAME,
                                          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
                        } catch(org.apache.axis2.databinding.ADBException e){
                            throw org.apache.axis2.AxisFault.makeFault(e);
                        }
                    

            }
        
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, GetExperimentResultsResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(GetExperimentResultsResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private GetExperimentResultsResponse wrapgetExperimentResults(){
                                GetExperimentResultsResponse wrappedElement = new GetExperimentResultsResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, DeleteSavedUserExperimentInputResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(DeleteSavedUserExperimentInputResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private DeleteSavedUserExperimentInputResponse wrapdeleteSavedUserExperimentInput(){
                                DeleteSavedUserExperimentInputResponse wrappedElement = new DeleteSavedUserExperimentInputResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, GetSavedUserExperimentInputResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(GetSavedUserExperimentInputResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private GetSavedUserExperimentInputResponse wrapgetSavedUserExperimentInput(){
                                GetSavedUserExperimentInputResponse wrappedElement = new GetSavedUserExperimentInputResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, SetUserPermissionsResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(SetUserPermissionsResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private SetUserPermissionsResponse wrapsetUserPermissions(){
                                SetUserPermissionsResponse wrappedElement = new SetUserPermissionsResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, GetLabStatusResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(GetLabStatusResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private GetLabStatusResponse wrapgetLabStatus(){
                                GetLabStatusResponse wrappedElement = new GetLabStatusResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, GetExperimentIDResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(GetExperimentIDResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private GetExperimentIDResponse wrapgetExperimentID(){
                                GetExperimentIDResponse wrappedElement = new GetExperimentIDResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, SaveExperimentResultsResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(SaveExperimentResultsResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private SaveExperimentResultsResponse wrapsaveExperimentResults(){
                                SaveExperimentResultsResponse wrappedElement = new SaveExperimentResultsResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, SaveUserExperimentInputResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(SaveUserExperimentInputResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private SaveUserExperimentInputResponse wrapsaveUserExperimentInput(){
                                SaveUserExperimentInputResponse wrappedElement = new SaveUserExperimentInputResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, GetExperimentSpecsResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(GetExperimentSpecsResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private GetExperimentSpecsResponse wrapgetExperimentSpecs(){
                                GetExperimentSpecsResponse wrappedElement = new GetExperimentSpecsResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, GetInteractiveExperimentSessionResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(GetInteractiveExperimentSessionResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private GetInteractiveExperimentSessionResponse wrapgetInteractiveExperimentSession(){
                                GetInteractiveExperimentSessionResponse wrappedElement = new GetInteractiveExperimentSessionResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, GetMaintenanceTimeResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(GetMaintenanceTimeResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private GetMaintenanceTimeResponse wrapgetMaintenanceTime(){
                                GetMaintenanceTimeResponse wrappedElement = new GetMaintenanceTimeResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, CancelBookingTimeResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(CancelBookingTimeResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private CancelBookingTimeResponse wrapcancelBookingTime(){
                                CancelBookingTimeResponse wrappedElement = new CancelBookingTimeResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, GetLabInfoResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(GetLabInfoResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private GetLabInfoResponse wrapgetLabInfo(){
                                GetLabInfoResponse wrappedElement = new GetLabInfoResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, SetMaintenanceTimeResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(SetMaintenanceTimeResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private SetMaintenanceTimeResponse wrapsetMaintenanceTime(){
                                SetMaintenanceTimeResponse wrappedElement = new SetMaintenanceTimeResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, GetLabIDResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(GetLabIDResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private GetLabIDResponse wrapgetLabID(){
                                GetLabIDResponse wrappedElement = new GetLabIDResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, SubmitExperimentResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(SubmitExperimentResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private SubmitExperimentResponse wrapsubmitExperiment(){
                                SubmitExperimentResponse wrappedElement = new SubmitExperimentResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, ScheduleBookingTimeResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(ScheduleBookingTimeResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private ScheduleBookingTimeResponse wrapscheduleBookingTime(){
                                ScheduleBookingTimeResponse wrappedElement = new ScheduleBookingTimeResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, GetTokenResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(GetTokenResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private GetTokenResponse wrapgetToken(){
                                GetTokenResponse wrappedElement = new GetTokenResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, CancelMaintenanceTimeResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(CancelMaintenanceTimeResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private CancelMaintenanceTimeResponse wrapcancelMaintenanceTime(){
                                CancelMaintenanceTimeResponse wrappedElement = new CancelMaintenanceTimeResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, ReleaseSlaveResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(ReleaseSlaveResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private ReleaseSlaveResponse wrapreleaseSlave(){
                                ReleaseSlaveResponse wrappedElement = new ReleaseSlaveResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, GetExperimentTypeResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(GetExperimentTypeResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private GetExperimentTypeResponse wrapgetExperimentType(){
                                GetExperimentTypeResponse wrappedElement = new GetExperimentTypeResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, GetExperimentStatusResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(GetExperimentStatusResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private GetExperimentStatusResponse wrapgetExperimentStatus(){
                                GetExperimentStatusResponse wrappedElement = new GetExperimentStatusResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, ReleaseExperimentResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(ReleaseExperimentResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private ReleaseExperimentResponse wrapreleaseExperiment(){
                                ReleaseExperimentResponse wrappedElement = new ReleaseExperimentResponse();
                                return wrappedElement;
                         }
                    
                    private  org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, GetUserPermissionsResponse param, boolean optimizeContent)
                        throws org.apache.axis2.AxisFault{
                      try{
                          org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
                           
                                    emptyEnvelope.getBody().addChild(param.getOMElement(GetUserPermissionsResponse.MY_QNAME,factory));
                                

                         return emptyEnvelope;
                    } catch(org.apache.axis2.databinding.ADBException e){
                        throw org.apache.axis2.AxisFault.makeFault(e);
                    }
                    }
                    
                         private GetUserPermissionsResponse wrapgetUserPermissions(){
                                GetUserPermissionsResponse wrappedElement = new GetUserPermissionsResponse();
                                return wrappedElement;
                         }
                    


        /**
        *  get the default envelope
        */
        private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory){
        return factory.getDefaultEnvelope();
        }


        private  java.lang.Object fromOM(
        org.apache.axiom.om.OMElement param,
        java.lang.Class type,
        java.util.Map extraNamespaces) throws org.apache.axis2.AxisFault{

        try {
        
                if (GetExperimentResults.class.equals(type)){
                
                           return GetExperimentResults.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (GetExperimentResultsResponse.class.equals(type)){
                
                           return GetExperimentResultsResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (DeleteSavedUserExperimentInput.class.equals(type)){
                
                           return DeleteSavedUserExperimentInput.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (DeleteSavedUserExperimentInputResponse.class.equals(type)){
                
                           return DeleteSavedUserExperimentInputResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (GetSavedUserExperimentInput.class.equals(type)){
                
                           return GetSavedUserExperimentInput.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (GetSavedUserExperimentInputResponse.class.equals(type)){
                
                           return GetSavedUserExperimentInputResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (SetUserPermissions.class.equals(type)){
                
                           return SetUserPermissions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (SetUserPermissionsResponse.class.equals(type)){
                
                           return SetUserPermissionsResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (GetLabStatus.class.equals(type)){
                
                           return GetLabStatus.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (GetLabStatusResponse.class.equals(type)){
                
                           return GetLabStatusResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (GetExperimentID.class.equals(type)){
                
                           return GetExperimentID.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (GetExperimentIDResponse.class.equals(type)){
                
                           return GetExperimentIDResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (SaveExperimentResults.class.equals(type)){
                
                           return SaveExperimentResults.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (SaveExperimentResultsResponse.class.equals(type)){
                
                           return SaveExperimentResultsResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (SaveUserExperimentInput.class.equals(type)){
                
                           return SaveUserExperimentInput.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (SaveUserExperimentInputResponse.class.equals(type)){
                
                           return SaveUserExperimentInputResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (GetExperimentSpecs.class.equals(type)){
                
                           return GetExperimentSpecs.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (GetExperimentSpecsResponse.class.equals(type)){
                
                           return GetExperimentSpecsResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (GetInteractiveExperimentSession.class.equals(type)){
                
                           return GetInteractiveExperimentSession.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (GetInteractiveExperimentSessionResponse.class.equals(type)){
                
                           return GetInteractiveExperimentSessionResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (GetMaintenanceTime.class.equals(type)){
                
                           return GetMaintenanceTime.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (GetMaintenanceTimeResponse.class.equals(type)){
                
                           return GetMaintenanceTimeResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (CancelBookingTime.class.equals(type)){
                
                           return CancelBookingTime.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (CancelBookingTimeResponse.class.equals(type)){
                
                           return CancelBookingTimeResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (GetLabInfo.class.equals(type)){
                
                           return GetLabInfo.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (GetLabInfoResponse.class.equals(type)){
                
                           return GetLabInfoResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (SetMaintenanceTime.class.equals(type)){
                
                           return SetMaintenanceTime.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (SetMaintenanceTimeResponse.class.equals(type)){
                
                           return SetMaintenanceTimeResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (GetLabID.class.equals(type)){
                
                           return GetLabID.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (GetLabIDResponse.class.equals(type)){
                
                           return GetLabIDResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (SubmitExperiment.class.equals(type)){
                
                           return SubmitExperiment.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (SubmitExperimentResponse.class.equals(type)){
                
                           return SubmitExperimentResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (ScheduleBookingTime.class.equals(type)){
                
                           return ScheduleBookingTime.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (ScheduleBookingTimeResponse.class.equals(type)){
                
                           return ScheduleBookingTimeResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (GetToken.class.equals(type)){
                
                           return GetToken.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (GetTokenResponse.class.equals(type)){
                
                           return GetTokenResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (CancelMaintenanceTime.class.equals(type)){
                
                           return CancelMaintenanceTime.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (CancelMaintenanceTimeResponse.class.equals(type)){
                
                           return CancelMaintenanceTimeResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (ReleaseSlave.class.equals(type)){
                
                           return ReleaseSlave.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (ReleaseSlaveResponse.class.equals(type)){
                
                           return ReleaseSlaveResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (GetExperimentType.class.equals(type)){
                
                           return GetExperimentType.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (GetExperimentTypeResponse.class.equals(type)){
                
                           return GetExperimentTypeResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (GetExperimentStatus.class.equals(type)){
                
                           return GetExperimentStatus.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (GetExperimentStatusResponse.class.equals(type)){
                
                           return GetExperimentStatusResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (ReleaseExperiment.class.equals(type)){
                
                           return ReleaseExperiment.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (ReleaseExperimentResponse.class.equals(type)){
                
                           return ReleaseExperimentResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (GetUserPermissions.class.equals(type)){
                
                           return GetUserPermissions.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
                if (GetUserPermissionsResponse.class.equals(type)){
                
                           return GetUserPermissionsResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
                    

                }
           
        } catch (java.lang.Exception e) {
        throw org.apache.axis2.AxisFault.makeFault(e);
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
        org.apache.axiom.om.OMNamespace ns = (org.apache.axiom.om.OMNamespace) namespaceIterator.next();
        returnMap.put(ns.getPrefix(),ns.getNamespaceURI());
        }
        return returnMap;
        }

        private org.apache.axis2.AxisFault createAxisFault(java.lang.Exception e) {
        org.apache.axis2.AxisFault f;
        Throwable cause = e.getCause();
        if (cause != null) {
            f = new org.apache.axis2.AxisFault(e.getMessage(), cause);
        } else {
            f = new org.apache.axis2.AxisFault(e.getMessage());
        }

        return f;
    }

        }//end of class
    