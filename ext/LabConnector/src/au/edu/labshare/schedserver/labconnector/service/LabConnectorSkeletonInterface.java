
/**
 * LabConnectorSkeletonInterface.java
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
     *  LabConnectorSkeletonInterface java skeleton interface for the axisService
     */
    public interface LabConnectorSkeletonInterface {
     
         
        /**
         * Auto generated method signature
         * 
                                    * @param getExperimentResults
         */

        
                public GetExperimentResultsResponse getExperimentResults
                (
                  GetExperimentResults getExperimentResults
                 )
            ;
        
         
        /**
         * Auto generated method signature
         * 
                                    * @param deleteSavedUserExperimentInput
         */

        
                public DeleteSavedUserExperimentInputResponse deleteSavedUserExperimentInput
                (
                  DeleteSavedUserExperimentInput deleteSavedUserExperimentInput
                 )
            ;
        
         
        /**
         * Auto generated method signature
         * 
                                    * @param getSavedUserExperimentInput
         */

        
                public GetSavedUserExperimentInputResponse getSavedUserExperimentInput
                (
                  GetSavedUserExperimentInput getSavedUserExperimentInput
                 )
            ;
        
         
        /**
         * Auto generated method signature
         * 
                                    * @param setUserPermissions
         */

        
                public SetUserPermissionsResponse setUserPermissions
                (
                  SetUserPermissions setUserPermissions
                 )
            ;
        
         
        /**
         * Auto generated method signature
         * 
                                    * @param getLabStatus
         */

        
                public GetLabStatusResponse getLabStatus
                (
                  GetLabStatus getLabStatus
                 )
            ;
        
         
        /**
         * Auto generated method signature
         * 
                                    * @param getExperimentID
         */

        
                public GetExperimentIDResponse getExperimentID
                (
                  GetExperimentID getExperimentID
                 )
            ;
        
         
        /**
         * Auto generated method signature
         * 
                                    * @param saveExperimentResults
         */

        
                public SaveExperimentResultsResponse saveExperimentResults
                (
                  SaveExperimentResults saveExperimentResults
                 )
            ;
        
         
        /**
         * Auto generated method signature
         * 
                                    * @param saveUserExperimentInput
         */

        
                public SaveUserExperimentInputResponse saveUserExperimentInput
                (
                  SaveUserExperimentInput saveUserExperimentInput
                 )
            ;
        
         
        /**
         * Auto generated method signature
         * 
                                    * @param getExperimentSpecs
         */

        
                public GetExperimentSpecsResponse getExperimentSpecs
                (
                  GetExperimentSpecs getExperimentSpecs
                 )
            ;
        
         
        /**
         * Auto generated method signature
         * 
                                    * @param getInteractiveExperimentSession
         */

        
                public GetInteractiveExperimentSessionResponse getInteractiveExperimentSession
                (
                  GetInteractiveExperimentSession getInteractiveExperimentSession
                 )
            ;
        
         
        /**
         * Auto generated method signature
         * 
                                    * @param getMaintenanceTime
         */

        
                public GetMaintenanceTimeResponse getMaintenanceTime
                (
                  GetMaintenanceTime getMaintenanceTime
                 )
            ;
        
         
        /**
         * Auto generated method signature
         * 
                                    * @param cancelBookingTime
         */

        
                public CancelBookingTimeResponse cancelBookingTime
                (
                  CancelBookingTime cancelBookingTime
                 )
            ;
        
         
        /**
         * Auto generated method signature
         * 
                                    * @param getLabInfo
         */

        
                public GetLabInfoResponse getLabInfo
                (
                  GetLabInfo getLabInfo
                 )
            ;
        
         
        /**
         * Auto generated method signature
         * 
                                    * @param setMaintenanceTime
         */

        
                public SetMaintenanceTimeResponse setMaintenanceTime
                (
                  SetMaintenanceTime setMaintenanceTime
                 )
            ;
        
         
        /**
         * Auto generated method signature
         * 
                                    * @param getLabID
         */

        
                public GetLabIDResponse getLabID
                (
                  GetLabID getLabID
                 )
            ;
        
         
        /**
         * Auto generated method signature
         * 
                                    * @param submitExperiment
         */

        
                public SubmitExperimentResponse submitExperiment
                (
                  SubmitExperiment submitExperiment
                 )
            ;
        
         
        /**
         * Auto generated method signature
         * 
                                    * @param scheduleBookingTime
         */

        
                public ScheduleBookingTimeResponse scheduleBookingTime
                (
                  ScheduleBookingTime scheduleBookingTime
                 )
            ;
        
         
        /**
         * Auto generated method signature
         * 
                                    * @param getToken
         */

        
                public GetTokenResponse getToken
                (
                  GetToken getToken
                 )
            ;
        
         
        /**
         * Auto generated method signature
         * 
                                    * @param cancelMaintenanceTime
         */

        
                public CancelMaintenanceTimeResponse cancelMaintenanceTime
                (
                  CancelMaintenanceTime cancelMaintenanceTime
                 )
            ;
        
         
        /**
         * Auto generated method signature
         * 
                                    * @param releaseSlave
         */

        
                public ReleaseSlaveResponse releaseSlave
                (
                  ReleaseSlave releaseSlave
                 )
            ;
        
         
        /**
         * Auto generated method signature
         * 
                                    * @param getExperimentType
         */

        
                public GetExperimentTypeResponse getExperimentType
                (
                  GetExperimentType getExperimentType
                 )
            ;
        
         
        /**
         * Auto generated method signature
         * 
                                    * @param getExperimentStatus
         */

        
                public GetExperimentStatusResponse getExperimentStatus
                (
                  GetExperimentStatus getExperimentStatus
                 )
            ;
        
         
        /**
         * Auto generated method signature
         * 
                                    * @param releaseExperiment
         */

        
                public ReleaseExperimentResponse releaseExperiment
                (
                  ReleaseExperiment releaseExperiment
                 )
            ;
        
         
        /**
         * Auto generated method signature
         * 
                                    * @param getUserPermissions
         */

        
                public GetUserPermissionsResponse getUserPermissions
                (
                  GetUserPermissions getUserPermissions
                 )
            ;
        
         }
    