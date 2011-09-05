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

package au.edu.uts.eng.remotelabs.schedserver.multisite.client;


/**
 * Handler for MultiSite Callback async operation responses or faults.
 */
public abstract class MultiSiteCallbackClientHandler
{
    /**
     * Invoked when the response is from sending notification of booking 
     * cancellation is received.
     * 
     * @param successful the consumer success acknowledgment
     * @param reason error reason or null if none provided
     */
    public void receiveResultBookingCancelled(final boolean successful, final String reason)
    {
        /* Should be overridden. */
    }

    /**
     * Invoked when an error from sending notification of booking 
     * occurs.
     * 
     * @param e error exception
     */
    public void receiveErrorBookingCancelled(final Exception e)
    {
        /* Should be overridden. */
    }

    /**
     * Invoked when the response is from sending notification of started
     * session is received.
     * 
     * @param successful the consumer success acknowledgment
     * @param reason error reason or null if none provided
     */
    public void receiveResultSessionStarted(final boolean successful, final String error)
    {
        /* Should be overridden. */
    }

    /**
     * Invoked when an error from sending notification of a started session.
     * 
     * @param e error exception
     */
    public void receiveErrorSessionStarted(final Exception e)
    {
        /* Should be overridden. */
    }
    
    /**
     * Invoked when the response is from sending notification of a finished
     * session is received.
     * 
     * @param successful the consumer success acknowledgment
     * @param reason error reason or null if none provided
     */
    public void receiveResultSessionFinished(final boolean successful, final String reason)
    {
        /* Should be overridden. */
    }

    /**
     * Invoked when an error from sending notification of a finished session.
     * 
     * @param e error exception
     */
    public void receiveErrorSessionFinished(final Exception e)
    {
        /* Should be overridden. */
    }
    
    /**
     * Invoked when the response is from sending notification of a session
     * update is received.
     * 
     * @param successful the consumer success acknowledgment
     * @param reason error reason or null if none provided
     */
    public void receiveResultSessionUpdate(final boolean successful, final String reason)
    {
        /* Should be overridden. */
    }

    /**
     * Invoked when an error from sending notification of a session.
     * update.
     * 
     * @param e error exception
     */
    public void receiveErrorsessionUpdate(final Exception e)
    {
        /* Should be overridden. */
    }
}
