/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
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
 * @author Michael Diponio (mdiponio)
 * @date 6th April 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.rigprovider;

import org.hibernate.Session;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;

/**
 * Rig event call listener which can be implemented and registered 
 * as a service to receive notification of rig state changes. The service 
 * must be registered under the qualified name of this interface. The following
 * events provide notification:
 * <ul>
 *  <li>Registered - The rig has been registered.</li>
 *  <li>Online - An off line rig has come online.</li>
 *  <li>Off line - An online rig has gone off line.</li>
 *  <li>Removal - A registered rig is being removed.</li>
 * </ul>
 */
public interface RigEventListener
{
    enum RigStateChangeEvent
    {
        /** The rig being registered. */
        REGISTERED, /** The rig has come online. */
        ONLINE, /** The rig has gone off line. */
        OFFLINE, /** The rig is being removed. */
        REMOVED
    }
    
    /**
     * Receives the event caused by the rig state change.
     * 
     * @param event type of event
     * @param rig the rig which caused the state change
     * @param db the database session the rig is attached to
     */
    public void receiveEvent(RigStateChangeEvent event, Rig rig, Session db);
}
