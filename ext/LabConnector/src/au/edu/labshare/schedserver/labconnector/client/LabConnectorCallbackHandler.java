/**
 * SAHARA Scheduling Server - LabConnector
 * Callback handler for Axis2 client.
 * 
 * @license See LICENSE in the top level directory for complete license terms.
 * 
 * Copyright (c) 2010, University of Technology, Sydney
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *  * Neither the name of the University of Technology, Sydney nor the names
 *    of its contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *    
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * @author Herbert Yeung
 * @date 18th May 2010
 */
/**
 * LabConnectorServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.5.1  Built on : Oct 19, 2009 (10:59:00 EDT)
 */

// package au.edu.uts.eng.ilabs_test.labconnector;
package au.edu.labshare.schedserver.labconnector.client;

/**
 * LabConnectorCallbackHandler Callback class, Users can extend this class and
 * implement their own receiveResult and receiveError methods.
 */
public abstract class LabConnectorCallbackHandler
{

    protected Object clientData;

    /**
     * User can pass in any object that needs to be accessed once the
     * NonBlocking Web service call is finished and appropriate method of this
     * CallBack is called.
     * 
     * @param clientData
     *            Object mechanism by which the user can pass in user data that
     *            will be avilable at the time this callback is called.
     */
    public LabConnectorCallbackHandler(Object clientData)
    {
        this.clientData = clientData;
    }

    /**
     * Please use this constructor if you don't want to set any clientData
     */
    public LabConnectorCallbackHandler()
    {
        this.clientData = null;
    }

    /**
     * Get the client data
     */

    public Object getClientData()
    {
        return clientData;
    }

    /**
     * auto generated Axis2 call back method for getUserPermissions method
     * override this method for handling normal response from getUserPermissions
     * operation
     */
    public void receiveResultgetUserPermissions(
            au.edu.labshare.schedserver.labconnector.client.LabConnectorStub.GetUserPermissionsResponse result)
    {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling
     * error response from getUserPermissions operation
     */
    public void receiveErrorgetUserPermissions(java.lang.Exception e)
    {
    }

    /**
     * auto generated Axis2 call back method for saveUserExperimentInput method
     * override this method for handling normal response from
     * saveUserExperimentInput operation
     */
    public void receiveResultsaveUserExperimentInput(
            au.edu.labshare.schedserver.labconnector.client.LabConnectorStub.SaveUserExperimentInputResponse result)
    {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling
     * error response from saveUserExperimentInput operation
     */
    public void receiveErrorsaveUserExperimentInput(java.lang.Exception e)
    {
    }

    /**
     * auto generated Axis2 call back method for cancelBookingTime method
     * override this method for handling normal response from cancelBookingTime
     * operation
     */
    public void receiveResultcancelBookingTime(
            au.edu.labshare.schedserver.labconnector.client.LabConnectorStub.CancelBookingTimeResponse result)
    {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling
     * error response from cancelBookingTime operation
     */
    public void receiveErrorcancelBookingTime(java.lang.Exception e)
    {
    }

    /**
     * auto generated Axis2 call back method for getLabInfo method override this
     * method for handling normal response from getLabInfo operation
     */
    public void receiveResultgetLabInfo(
            au.edu.labshare.schedserver.labconnector.client.LabConnectorStub.GetLabInfoResponse result)
    {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling
     * error response from getLabInfo operation
     */
    public void receiveErrorgetLabInfo(java.lang.Exception e)
    {
    }

    /**
     * auto generated Axis2 call back method for getExperimentResults method
     * override this method for handling normal response from
     * getExperimentResults operation
     */
    public void receiveResultgetExperimentResults(
            au.edu.labshare.schedserver.labconnector.client.LabConnectorStub.GetExperimentResultsResponse result)
    {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling
     * error response from getExperimentResults operation
     */
    public void receiveErrorgetExperimentResults(java.lang.Exception e)
    {
    }

    /**
     * auto generated Axis2 call back method for getMaintenanceTime method
     * override this method for handling normal response from getMaintenanceTime
     * operation
     */
    public void receiveResultgetMaintenanceTime(
            au.edu.labshare.schedserver.labconnector.client.LabConnectorStub.GetMaintenanceTimeResponse result)
    {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling
     * error response from getMaintenanceTime operation
     */
    public void receiveErrorgetMaintenanceTime(java.lang.Exception e)
    {
    }

    /**
     * auto generated Axis2 call back method for deleteSavedUserExperimentInput
     * method override this method for handling normal response from
     * deleteSavedUserExperimentInput operation
     */
    public void receiveResultdeleteSavedUserExperimentInput(
            au.edu.labshare.schedserver.labconnector.client.LabConnectorStub.DeleteSavedUserExperimentInputResponse result)
    {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling
     * error response from deleteSavedUserExperimentInput operation
     */
    public void receiveErrordeleteSavedUserExperimentInput(java.lang.Exception e)
    {
    }

    /**
     * auto generated Axis2 call back method for cancelMaintenanceTime method
     * override this method for handling normal response from
     * cancelMaintenanceTime operation
     */
    public void receiveResultcancelMaintenanceTime(
            au.edu.labshare.schedserver.labconnector.client.LabConnectorStub.CancelMaintenanceTimeResponse result)
    {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling
     * error response from cancelMaintenanceTime operation
     */
    public void receiveErrorcancelMaintenanceTime(java.lang.Exception e)
    {
    }

    /**
     * auto generated Axis2 call back method for getSavedUserExperimentInput
     * method override this method for handling normal response from
     * getSavedUserExperimentInput operation
     */
    public void receiveResultgetSavedUserExperimentInput(
            au.edu.labshare.schedserver.labconnector.client.LabConnectorStub.GetSavedUserExperimentInputResponse result)
    {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling
     * error response from getSavedUserExperimentInput operation
     */
    public void receiveErrorgetSavedUserExperimentInput(java.lang.Exception e)
    {
    }

    /**
     * auto generated Axis2 call back method for getExperimentID method override
     * this method for handling normal response from getExperimentID operation
     */
    public void receiveResultgetExperimentID(
            au.edu.labshare.schedserver.labconnector.client.LabConnectorStub.GetExperimentIDResponse result)
    {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling
     * error response from getExperimentID operation
     */
    public void receiveErrorgetExperimentID(java.lang.Exception e)
    {
    }

    /**
     * auto generated Axis2 call back method for submitExperiment method
     * override this method for handling normal response from submitExperiment
     * operation
     */
    public void receiveResultsubmitExperiment(
            au.edu.labshare.schedserver.labconnector.client.LabConnectorStub.SubmitExperimentResponse result)
    {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling
     * error response from submitExperiment operation
     */
    public void receiveErrorsubmitExperiment(java.lang.Exception e)
    {
    }

    /**
     * auto generated Axis2 call back method for scheduleBookingTime method
     * override this method for handling normal response from
     * scheduleBookingTime operation
     */
    public void receiveResultscheduleBookingTime(
            au.edu.labshare.schedserver.labconnector.client.LabConnectorStub.ScheduleBookingTimeResponse result)
    {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling
     * error response from scheduleBookingTime operation
     */
    public void receiveErrorscheduleBookingTime(java.lang.Exception e)
    {
    }

    /**
     * auto generated Axis2 call back method for saveExperimentResults method
     * override this method for handling normal response from
     * saveExperimentResults operation
     */
    public void receiveResultsaveExperimentResults(
            au.edu.labshare.schedserver.labconnector.client.LabConnectorStub.SaveExperimentResultsResponse result)
    {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling
     * error response from saveExperimentResults operation
     */
    public void receiveErrorsaveExperimentResults(java.lang.Exception e)
    {
    }

    /**
     * auto generated Axis2 call back method for getToken method override this
     * method for handling normal response from getToken operation
     */
    public void receiveResultgetToken(
            au.edu.labshare.schedserver.labconnector.client.LabConnectorStub.GetTokenResponse result)
    {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling
     * error response from getToken operation
     */
    public void receiveErrorgetToken(java.lang.Exception e)
    {
    }

    /**
     * auto generated Axis2 call back method for getLabID method override this
     * method for handling normal response from getLabID operation
     */
    public void receiveResultgetLabID(
            au.edu.labshare.schedserver.labconnector.client.LabConnectorStub.GetLabIDResponse result)
    {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling
     * error response from getLabID operation
     */
    public void receiveErrorgetLabID(java.lang.Exception e)
    {
    }

    /**
     * auto generated Axis2 call back method for setUserPermissions method
     * override this method for handling normal response from setUserPermissions
     * operation
     */
    public void receiveResultsetUserPermissions(
            au.edu.labshare.schedserver.labconnector.client.LabConnectorStub.SetUserPermissionsResponse result)
    {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling
     * error response from setUserPermissions operation
     */
    public void receiveErrorsetUserPermissions(java.lang.Exception e)
    {
    }

    /**
     * auto generated Axis2 call back method for getExperimentType method
     * override this method for handling normal response from getExperimentType
     * operation
     */
    public void receiveResultgetExperimentType(
            au.edu.labshare.schedserver.labconnector.client.LabConnectorStub.GetExperimentTypeResponse result)
    {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling
     * error response from getExperimentType operation
     */
    public void receiveErrorgetExperimentType(java.lang.Exception e)
    {
    }

    /**
     * auto generated Axis2 call back method for releaseExperiment method
     * override this method for handling normal response from releaseExperiment
     * operation
     */
    public void receiveResultreleaseExperiment(
            au.edu.labshare.schedserver.labconnector.client.LabConnectorStub.ReleaseExperimentResponse result)
    {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling
     * error response from releaseExperiment operation
     */
    public void receiveErrorreleaseExperiment(java.lang.Exception e)
    {
    }

    /**
     * auto generated Axis2 call back method for getExperimentStatus method
     * override this method for handling normal response from
     * getExperimentStatus operation
     */
    public void receiveResultgetExperimentStatus(
            au.edu.labshare.schedserver.labconnector.client.LabConnectorStub.GetExperimentStatusResponse result)
    {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling
     * error response from getExperimentStatus operation
     */
    public void receiveErrorgetExperimentStatus(java.lang.Exception e)
    {
    }

    /**
     * auto generated Axis2 call back method for releaseSlave method override
     * this method for handling normal response from releaseSlave operation
     */
    public void receiveResultreleaseSlave(
            au.edu.labshare.schedserver.labconnector.client.LabConnectorStub.ReleaseSlaveResponse result)
    {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling
     * error response from releaseSlave operation
     */
    public void receiveErrorreleaseSlave(java.lang.Exception e)
    {
    }

    /**
     * auto generated Axis2 call back method for getInteractiveExperimentSession
     * method override this method for handling normal response from
     * getInteractiveExperimentSession operation
     */
    public void receiveResultgetInteractiveExperimentSession(
            au.edu.labshare.schedserver.labconnector.client.LabConnectorStub.GetInteractiveExperimentSessionResponse result)
    {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling
     * error response from getInteractiveExperimentSession operation
     */
    public void receiveErrorgetInteractiveExperimentSession(
            java.lang.Exception e)
    {
    }

    /**
     * auto generated Axis2 call back method for setMaintenanceTime method
     * override this method for handling normal response from setMaintenanceTime
     * operation
     */
    public void receiveResultsetMaintenanceTime(
            au.edu.labshare.schedserver.labconnector.client.LabConnectorStub.SetMaintenanceTimeResponse result)
    {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling
     * error response from setMaintenanceTime operation
     */
    public void receiveErrorsetMaintenanceTime(java.lang.Exception e)
    {
    }

    /**
     * auto generated Axis2 call back method for getExperimentSpecs method
     * override this method for handling normal response from getExperimentSpecs
     * operation
     */
    public void receiveResultgetExperimentSpecs(
            au.edu.labshare.schedserver.labconnector.client.LabConnectorStub.GetExperimentSpecsResponse result)
    {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling
     * error response from getExperimentSpecs operation
     */
    public void receiveErrorgetExperimentSpecs(java.lang.Exception e)
    {
    }

    /**
     * auto generated Axis2 call back method for getLabStatus method override
     * this method for handling normal response from getLabStatus operation
     */
    public void receiveResultgetLabStatus(
            au.edu.labshare.schedserver.labconnector.client.LabConnectorStub.GetLabStatusResponse result)
    {
    }

    /**
     * auto generated Axis2 Error handler override this method for handling
     * error response from getLabStatus operation
     */
    public void receiveErrorgetLabStatus(java.lang.Exception e)
    {
    }

}
