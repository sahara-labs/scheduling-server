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
 * @date 2nd September 2011
 */
package io.rln.ss.data.listener;

import io.rln.ss.data.entities.Session;

/**
 * Session event listener that can be implemented and registered to receive 
 * notification of session change events. This listener provides notifications
 * of the following events:
 * <ul>
 *  <li>Queued - Session queued.</li>
 *  <li>Assigned - Session assigned to a rig.</li>
 *  <li>Ready - Session ready for use (assignment complete).</li>
 *  <li>Extended - Session time extended.</li>
 *  <li>Grace - Session in grace period (marked for termination).</li>
 *  <li>Finished - Session is finished.</li>
 * </ul>
 * <strong>NOTE:</strong>Not all sessions events may come in sequence.
 */
public interface SessionEventListener
{
    enum SessionEvent
    {
        /** Session is being queued. */
        QUEUED, /** Session is assigned to a rig. */
        ASSIGNED, /** Session is ready for use (assignment complete). */
        READY,  /** Session time has been extended. */
        EXTENDED, /** Session is in grace period (marked for termination). */
        GRACE, /** Session is finished. */
        FINISHED
    }
    
    /**
     * Receives the event caused by the session state change.
     * 
     * @param event type of event
     * @param the session that
     * @param db the database session the rig is attached to
     */
    public void eventOccurred(SessionEvent event, Session session, org.hibernate.Session db);
}
