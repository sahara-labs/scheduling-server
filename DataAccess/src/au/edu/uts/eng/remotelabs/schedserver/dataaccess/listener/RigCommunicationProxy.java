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
 * @date 31st July 2016
 */

package au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener;


import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;

/**
 * Communicator service to call operations on rigs.
 */
public interface RigCommunicationProxy
{
    /**
     * Allocates the user in the session to the rig client by calling the rig 
     * client allocate (or equivalent) operation.
     * 
     * @param ses session information
     * @param db database session
     */
    public void allocate(Session ses, org.hibernate.Session db);
    
    /**
     * Releases the user in the session from the rig client by calling 
     * the rig release (or equivalent) operation.
     * 
     * @param ses session information
     * @param db database session
     */
    public void release(Session ses, org.hibernate.Session db);
    
    /**
     * Notify the rig of a message using the rig notification (or equivalent)
     * operation.
     * 
     * @param message message to send
     * @param ses session information
     * @param db database session
     */
    public void notify(String message, Session ses, org.hibernate.Session db);
    
    /**
     * Sets maintenance state on rig. 
     * 
     * @param rig rig to set
     * @param runTests whether exerciser tests should be run
     * @param db database session
     */
    public void putMaintenance(Rig rig, boolean runTests, org.hibernate.Session db);
    
    /**
     * Clears maintenance state on rig.
     * 
     * @param rig rig to clear
     * @param db database session
     */
    public void clearMaintenance(Rig rig, org.hibernate.Session db);
    
    /**
     * Detects whether activity exists for the session using the activity 
     * detection (or equivalent) operation. 
     * 
     * @param ses session information
     * @param db database session 
     * @return whether activity has been detected
     */
    public boolean hasActivity(Session ses, org.hibernate.Session db);
    
    /**
     * Detects whether activity exists for the session using the activity 
     * detection (or equivalent) operation. 
     * 
     * @param ses session information
     * @param db database session 
     * @return callback to receive response
     */
    public void hasActivity(Session ses, org.hibernate.Session db, ActivityAsyncCallback callbcak);
    
    /**
     * Callback for async operation.
     */
    public static class ActivityAsyncCallback
    {
        /**
         * Response received.
         * 
         * @param activity whether there is activity
         */
        public void response(boolean activity) { };
        
        /**
         * Error received.
         * 
         * @param Exception e
         */
        public void error(Exception e) { }
    }
}
