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

package au.edu.uts.eng.remotelabs.schedserver.rigclientproxy;

import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.AbortBatchControlResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.AllocateResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.GetAttributeResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.GetBatchControlStatusResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.GetStatusResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.IsActivityDetectableResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.NotifyResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.PerformBatchControlResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.PerformPrimitiveControlResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.ReleaseResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.SetMaintenanceResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.SetTestIntervalResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.SlaveAllocateResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.SlaveReleaseResponse;

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

    public void receiveResultslaveAllocate(final SlaveAllocateResponse result)
    { /* Does nothing by default. */ }

    public void receiveResultperformPrimitiveControl(final PerformPrimitiveControlResponse result)
    { /* Does nothing by default. */ }

    public void receiveErrorperformPrimitiveControl(final Exception e)
    { /* Does nothing by default. */ }

    public void receiveResultgetBatchControlStatus(final GetBatchControlStatusResponse result)
    { /* Does nothing by default. */ }

    public void receiveErrorgetBatchControlStatus(final Exception e)
    { /* Does nothing by default. */ }

    public void receiveResultgetAttribute(final GetAttributeResponse result)
    { /* Does nothing by default. */ }

    public void receiveErrorgetAttribute(final Exception e)
    { /* Does nothing by default. */ }

    public void receiveResultslaveRelease(final SlaveReleaseResponse result)
    { /* Does nothing by default. */ }

    public void receiveErrorslaveRelease(final Exception e)
    { /* Does nothing by default. */ }

    public void receiveResultsetTestInterval(final SetTestIntervalResponse result)
    { /* Does nothing by default. */ }

    public void receiveErrorsetTestInterval(final Exception e)
    { /* Does nothing by default. */ }

    public void receiveResultsetMaintenance(final SetMaintenanceResponse result)
    { /* Does nothing by default. */ }

    public void receiveErrorsetMaintenance(final Exception e)
    { /* Does nothing by default. */ }

    public void receiveResultperformBatchControl(final PerformBatchControlResponse result)
    { /* Does nothing by default. */ }

    public void receiveErrorperformBatchControl(final Exception e)
    { /* Does nothing by default. */ }

    public void receiveResultgetStatus(final GetStatusResponse result)
    { /* Does nothing by default. */ }

    public void receiveErrorgetStatus(final Exception e)
    { /* Does nothing by default. */ }

    public void receiveErrorslaveAllocate(final Exception e)
    { /* Does nothing by default. */ }

    public void receiveResultabortBatchControl(final AbortBatchControlResponse result)
    { /* Does nothing by default. */ }

    public void receiveErrorabortBatchControl(final Exception e)
    { /* Does nothing by default. */ }
}
