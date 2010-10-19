/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2009, University of Technology, Sydney
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
 * @author Michael Diponio (mdiponio)
 * @date 5th April 2009
 */

package au.edu.uts.eng.remotelabs.schedserver.rigproxy;

import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.AbortBatchControlResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.AllocateResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.GetAttributeResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.GetBatchControlStatusResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.GetConfigResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.GetStatusResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.IsActivityDetectableResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.NotifyResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.PerformBatchControlResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.PerformPrimitiveControlResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.ReleaseResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.SetConfigResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.SetMaintenanceResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.SetTestIntervalResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.SlaveAllocateResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.SlaveReleaseResponse;

/**
 * RigClient async callback handler. This class should be overridden to
 * implement desired callback response behaviour.
 */
public abstract class RigClientAsyncServiceCallbackHandler
{
    /**
     * Method to override to receive the response from an allocate operation
     * call.
     * 
     * @param response response from call
     */
    public void allocateResponseCallback(final AllocateResponse response)
    { /* Does nothing by default. */ }

    /**
     * Method to override to receive error information from a failed 
     * allocate operation call.
     * 
     * @param e error exception
     */
    public void allocateErrorCallback(final Exception e)
    { /* Does nothing by default. */ }
    
    /**
     * Method to override to receive the response from an release operation
     * call.
     * 
     * @param response response from call
     */
    public void releaseResponseCallback(final ReleaseResponse response)
    { /* Does nothing by default. */ }

    /**
     * Method to override to receive error information from a failed 
     * release operation call.
     * 
     * @param e error exception
     */
    public void releaseErrorCallback(final Exception e)
    { /* Does nothing by default. */ }
    
    /**
     * Method to override to receive the response form a notify operation call.
     * 
     * @param response response from call
     */
    public void notifyResponseCallback(final NotifyResponse response)
    { /* Does nothing by default. */ }

    /**
     * Method to override to receive error information from a failed notify operation
     * call.
     * 
     * @param e error exception
     */
    public void notifyErrorCallback(final Exception e)
    { /* Does nothing by default. */ }
    
    /**
     * Method to override to receive the response from a activity detection call.
     * 
     * @param response response from call
     */
    public void activityDetectionResponseCallback(final IsActivityDetectableResponse response)
    { /* Does nothing by default. */ }

    /**
     * Method to override to receive error information from a failed activity
     * detection operation call.
     * 
     * @param e error exception
     */
    public void activityDetectionErrorCallback(final Exception e)
    { /* Does nothing by default. */ }
    
    /**
     * Method to override to receive the response from an slave allocate operation
     * call.
     * 
     * @param response response from call
     */
    public void slaveAllocateResponseCallback(final SlaveAllocateResponse result)
    { /* Does nothing by default. */ }

    /**
     * Method to override to receive error information from a failed 
     * slave allocate operation call.
     * 
     * @param e error exception
     */    
    public void slaveAllocateErrorCallback(final Exception e)
    { /* Does nothing by default. */ }
    
    /**
     * Method to override to receive the response from an slave release operation
     * call.
     * 
     * @param response response from call
     */    
    public void slaveReleaseResponseCallback(final SlaveReleaseResponse result)
    { /* Does nothing by default. */ }

    /**
     * Method to override to receive error information from a failed 
     * slave release operation call.
     * 
     * @param e error exception
     */
    public void slaveReleaseErrorCallback(final Exception e)
    { /* Does nothing by default. */ }
    
    /**
     * Method to override to receive the response from an primitive control operation
     * call.
     * 
     * @param response response from call
     */
    public void performPrimitiveControlResponseCallback(final PerformPrimitiveControlResponse result)
    { /* Does nothing by default. */ }

    /**
     * Method to override to receive error information from a failed 
     * primitive control operation call.
     * 
     * @param e error exception
     */
    public void performPrimitiveControlErrorCallback(final Exception e)
    { /* Does nothing by default. */ }
    
    /**
     * Method to override to receive the response from an get attribute operation
     * call.
     * 
     * @param response response from call
     */
    public void getAttributeResponseCallback(final GetAttributeResponse result)
    { /* Does nothing by default. */ }

    /**
     * Method to override to receive error information from a failed 
     * get attribute operation call.
     * 
     * @param e error exception
     */
    public void getAttributeErrorCallback(final Exception e)
    { /* Does nothing by default. */ }
    
    /**
     * Method to override to receive the response from an set test interval operation
     * call.
     * 
     * @param response response from call
     */
    public void setTestIntervalResponseCallback(final SetTestIntervalResponse result)
    { /* Does nothing by default. */ }

    /**
     * Method to override to receive error information from a failed 
     * set test interval operation call.
     * 
     * @param e error exception
     */
    public void setTestIntervalErrorCallback(final Exception e)
    { /* Does nothing by default. */ }
    
    /**
     * Method to override to receive the response from an set maintenance operation
     * call.
     * 
     * @param response response from call
     */
    public void setMaintenanceResponseCallback(final SetMaintenanceResponse result)
    { /* Does nothing by default. */ }

    /**
     * Method to override to receive error information from a failed 
     * set maintenance operation call.
     * 
     * @param e error exception
     */
    public void setMaintenanceErrorCallback(final Exception e)
    { /* Does nothing by default. */ }
    
    /**
     * Method to override to receive the response from an batch control operation
     * call.
     * 
     * @param response response from call
     */
    public void performBatchControlResponseCallback(final PerformBatchControlResponse result)
    { /* Does nothing by default. */ }

    /**
     * Method to override to receive error information from a failed 
     * batch control operation call.
     * 
     * @param e error exception
     */
    public void performBatchControlErrorCallback(final Exception e)
    { /* Does nothing by default. */ }
    
    /**
     * Method to override to receive the response from an abort batch operation
     * call.
     * 
     * @param response response from call
     */
    public void abortBatchControlResponseCallback(final AbortBatchControlResponse result)
    { /* Does nothing by default. */ }

    /**
     * Method to override to receive error information from a failed 
     * abort batch operation call.
     * 
     * @param e error exception
     */
    public void abortBatchControlErrorCallback(final Exception e)
    { /* Does nothing by default. */ }
    
     /**
     * Method to override to receive the response from an batch control status operation
     * call.
     * 
     * @param response response from call
     */
    public void getBatchControlStatusResponseCallback(final GetBatchControlStatusResponse result)
    { /* Does nothing by default. */ }

    /**
     * Method to override to receive error information from a failed 
     * batch control status operation call.
     * 
     * @param e error exception
     */
    public void getBatchControlStatusErrorCallback(final Exception e)
    { /* Does nothing by default. */ }
    
    /**
     * Method to override to receive the response from an get status operation
     * call.
     * 
     * @param response response from call
     */
    public void getStatusResponseCallback(final GetStatusResponse result)
    { /* Does nothing by default. */ }

    /**
     * Method to override to receive error information from a failed 
     * get status operation call.
     * 
     * @param e error exception
     */
    public void getStatusErrorCallback(final Exception e)
    { /* Does nothing by default. */ }
    
    /**
     * Method to override to receive the response from an get config operation
     * call.
     * 
     * @param response response from call
     */
    public void getConfigResponseCallback(final GetConfigResponse result)
    { /* Does nothing by default. */ }

    /**
     * Method to override to receive error information from a failed 
     * get config operation call.
     * 
     * @param e error exception
     */
    public void getConfigErrorCallback(final Exception e)
    { /* Does nothing by default. */ }
    
    /**
     * Method to override to receive the response from an set config operation
     * call.
     * 
     * @param response response from call
     */
    public void setConfigResponseCallback(final SetConfigResponse result)
    { /* Does nothing by default. */ }

    /**
     * Method to override to receive error information from a failed 
     * set config operation call.
     * 
     * @param e error exception
     */
    public void setConfigErrorCallback(final Exception e)
    { /* Does nothing by default. */ }
}
