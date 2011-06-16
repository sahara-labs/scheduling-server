package au.edu.labshare.schedserver.labconnector.service;

import au.edu.labshare.schedserver.labconnector.client.LabConnectorServiceClient;
import au.edu.labshare.schedserver.labconnector.service.LabConnectorExperimentStorage;
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
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;

public class LabConnector implements LabConnectorSkeletonInterface
{
    /** Logger. */
    private Logger logger;

	LabConnectorExperimentStorage labconnectorExptStorage;
    
    public LabConnector()
    {
        this.logger = LoggerActivator.getLogger();
		labconnectorExptStorage = new LabConnectorExperimentStorage();
    }

    @Override
    public CancelBookingTimeResponse cancelBookingTime(CancelBookingTime cancelBookingTime)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CancelMaintenanceTimeResponse cancelMaintenanceTime(CancelMaintenanceTime cancelMaintenanceTime)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DeleteSavedUserExperimentInputResponse deleteSavedUserExperimentInput(DeleteSavedUserExperimentInput deleteSavedUserExperimentInput)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GetExperimentIDResponse getExperimentID(GetExperimentID getExperimentID)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GetExperimentResultsResponse getExperimentResults(GetExperimentResults getExperimentResults)
    {
        //Technically, this is what the notify() call should call once it has completed experiment submission
        /*GetExperimentResultsResponse experimentResultsResponse = new GetExperimentResultsResponse();
        
        //Get the userID and the experimentResults to write to disk
        experimentResultsResponse.setLabResultsXML(null);
        experimentResultsResponse.setComplete(true);
        experimentResultsResponse.setAvailability(true);*/
        
        return null;
    }

    @Override
    public GetExperimentSpecsResponse getExperimentSpecs(GetExperimentSpecs getExperimentSpecs)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GetExperimentStatusResponse getExperimentStatus(GetExperimentStatus getExperimentStatus)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GetExperimentTypeResponse getExperimentType(GetExperimentType getExperimentType)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GetInteractiveExperimentSessionResponse getInteractiveExperimentSession(GetInteractiveExperimentSession getInteractiveExperimentSession)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GetLabIDResponse getLabID(GetLabID getLabID)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GetLabInfoResponse getLabInfo(GetLabInfo getLabInfo)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GetLabStatusResponse getLabStatus(GetLabStatus getLabStatus)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GetMaintenanceTimeResponse getMaintenanceTime(GetMaintenanceTime getMaintenanceTime)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GetSavedUserExperimentInputResponse getSavedUserExperimentInput(GetSavedUserExperimentInput getSavedUserExperimentInput)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GetTokenResponse getToken(GetToken getToken)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GetUserPermissionsResponse getUserPermissions(GetUserPermissions getUserPermissions)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ReleaseExperimentResponse releaseExperiment(ReleaseExperiment releaseExperiment)
    {
        au.edu.labshare.schedserver.labconnector.client.LabConnectorStub.ReleaseExperimentResponse releaseExperimentResponse;
        ReleaseExperimentResponse labconnectorReleaseExptResponse = new ReleaseExperimentResponse();
        
        //Call the iLabs Service Broker Cancel() call
        LabConnectorServiceClient labConnectorServiceClient;
        labConnectorServiceClient = new LabConnectorServiceClient();
        releaseExperimentResponse = labConnectorServiceClient.releaseExperiment(releaseExperiment.getUserID(), releaseExperiment.getExperimentname(), releaseExperiment.getExperimentID());
        
        labconnectorReleaseExptResponse.setReleaseResponse(releaseExperimentResponse.getReleaseResponse());
        return labconnectorReleaseExptResponse; 
    }

    @Override
    public ReleaseSlaveResponse releaseSlave(ReleaseSlave releaseSlave)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SaveExperimentResultsResponse saveExperimentResults(SaveExperimentResults saveExperimentResults)
    {
        //Establish a response 
        SaveExperimentResultsResponse saveExperimentResultsResponse = new SaveExperimentResultsResponse();
        
        //Get the userID and the experimentResults to write to disk
        String userID = saveExperimentResults.getUserID();
        String exptResultsXML = saveExperimentResults.getExperimentResultsXML();
        
        //Write results to disk so that it can accessed by the user - by default it should be /home/saharausers/userID
        //TODO: START Another bit of hardcoding to get it working - it is mapped to one user ONLY iLabs:userID 17 matched with Sahara:username UTS_dlowe 
        if(userID.equals("17"))
          userID = new String("UTS_dlowe"); //TODO: END TO REPLACE
        
        if(this.labconnectorExptStorage.writeExperimentResults(userID, exptResultsXML))
        {
            saveExperimentResultsResponse.setStorageResponse(true);
            saveExperimentResultsResponse.setErrorMessage("None");
        }
        else
        {
            saveExperimentResultsResponse.setStorageResponse(false);
            saveExperimentResultsResponse.setErrorMessage("Experiment Storage could not be accessed");
        }
            
        return new SaveExperimentResultsResponse();
    }

    @Override
    public SaveUserExperimentInputResponse saveUserExperimentInput(SaveUserExperimentInput saveUserExperimentInput)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ScheduleBookingTimeResponse scheduleBookingTime(ScheduleBookingTime scheduleBookingTime)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SetMaintenanceTimeResponse setMaintenanceTime(SetMaintenanceTime setMaintenanceTime)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SetUserPermissionsResponse setUserPermissions(SetUserPermissions setUserPermissions)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SubmitExperimentResponse submitExperiment(SubmitExperiment submitExperiment)
    {

        au.edu.labshare.schedserver.labconnector.client.LabConnectorStub.SubmitExperimentResponse submitExptResponse;
        
        LabConnectorServiceClient labConnectorServiceClient;
        labConnectorServiceClient = new LabConnectorServiceClient();
        SubmitExperimentResponse submitExptRespType = new SubmitExperimentResponse();

        /*
         * Invoke the LabConnector Proxy client to call the .NET LabConnector Proxy to iLabs
         */
        try
        {
            /*
             * 1. We should technically use a parameters file to:
             *    a) map the userID to one expected by iLabs
             *    (Limitation is that on ServiceBroker ilab_ISB::User DB table - UserID field is an integer type
             *    whereas UserID on Sahara2 is recognized as a string (e.g. heyeung))
             * 
             *    b) map the LabIDs (GUIDS) expected by iLabs (e.g. 54xsdfersdsfdfsdf) to 
             *    the Resource_Permissions::DisplayName which is used by Sahara2 to uniquely identify experiments
             *    
             * 2. Need to change how LabConnector.asmx - GetLabServer() reads from Web.config. Fortunately, the SbToLsPasskey is the same
             *    for this instance, but will become problematic. Need to be able to identify this. This is a limitation at the moment on iLab part.
             *    Appears almost 1:1. However, the ServiceBroker does a DB lookup from iLabs to determine this. Maybe best path!?!  
             */
            
            //TODO: START Only for testing purposes - we are hardcoding userID to be 17 and just doing an if statement to determine LabServer GUID
            String labIDVal = submitExperiment.getLabID();
            if(labIDVal.equals("Time Of Day - Batch"))
            {
                submitExptResponse =  labConnectorServiceClient.submitBatchExperiment(
                        submitExperiment.getExperimentSpecs(), "54FE7B0C423D41C1B06D8F189DFBED76", //Hardcoded AgentGUID of Labserver
                        submitExperiment.getPriority(), "17"); //User 17 is tstUser on iLabs_SB
            }
            else if(labIDVal.equals("UQ Radioactivity Expt1"))
            {
                submitExptResponse =  labConnectorServiceClient.submitBatchExperiment(
                        submitExperiment.getExperimentSpecs(), "9164ebd6edfd46f2b09dd7175af45ad8", //Hardcoded AgentGUID of Labserver
                        submitExperiment.getPriority(), "17"); //User 17 is tstUser on iLabs_SB
            }
            else //We default to UQ's Radioactivity Expt.
            {
                submitExptResponse =  labConnectorServiceClient.submitBatchExperiment(
                        submitExperiment.getExperimentSpecs(), "9164ebd6edfd46f2b09dd7175af45ad8", //Hardcoded AgentGUID of Labserver
                        submitExperiment.getPriority(), "17"); //User 17 is tstUser on iLabs_SB
                //submitExptResponse =  labConnectorServiceClient.submitBatchExperiment(
                //        submitExperiment.getExperimentSpecs(), "54FE7B0C423D41C1B06D8F189DFBED76", //Hardcoded AgentGUID of Labserver
                //        submitExperiment.getPriority(), "17"); //User 17 is tstUser on iLabs_SB
            }//TODO: END TO REPLACE

            
            //submitExptResponse =  labConnectorServiceClient.submitBatchExperiment(
            //                            submitExperiment.getExperimentSpecs(), submitExperiment.getLabID(), 
            //                            submitExperiment.getPriority(), submitExperiment.getUserID());
            
            if(submitExptResponse.getErrorMessage() != null)
                submitExptRespType.setErrorMessage(submitExptResponse.getErrorMessage());
            else
                submitExptRespType.setErrorMessage("Invalid Experiment Credentials");
            
            submitExptRespType.setExperimentID(submitExptResponse.getExperimentID());
        }
        catch(Exception e)
        {
            //Log any exception output
            this.logger.debug("Received " + this.getClass().getName() + e.toString());
        }
        
        return submitExptRespType;
    }

}
