/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2011, Michael Diponio
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
 * @date 28th August 2011
 */

package au.edu.uts.eng.remotelabs.schedserver.multisite.intf.callback;

import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.callback.types.BookingCancelled;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.callback.types.BookingCancelledResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.callback.types.SessionFinished;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.callback.types.SessionFinishedResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.callback.types.SessionStarted;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.callback.types.SessionStartedResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.callback.types.SessionUpdate;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.callback.types.SessionUpdateResponse;

/**
 * Interface for the MultiSite callback service which notifies a consumer site
 * or events relevant to their site.
 */
public interface MultiSiteCallbackSOAP
{

    /**
     * Notifies a consumer site a booking has been cancelled.
     * 
     * @param bookingCancelled request
     * @return response
     */
    public BookingCancelledResponse bookingCancelled(BookingCancelled bookingCancelled);

    /**
     * Notifies a consumer site a session is starting.
     *  
     * @param sessionStarted request
     * @return response
     */
    public SessionStartedResponse sessionStarted(SessionStarted sessionStarted);


    /**
     * Notifies a consumer site a session has finished.
     * 
     * @param sessionFinished request
     * @return response
     */
    public SessionFinishedResponse sessionFinished(SessionFinished sessionFinished);

    /**
     * Notifies a consumer site a session parameter has changed.
     * 
     * @param sessionUpdate request
     * @return response
     */
    public SessionUpdateResponse sessionUpdate(SessionUpdate sessionUpdate);
}
