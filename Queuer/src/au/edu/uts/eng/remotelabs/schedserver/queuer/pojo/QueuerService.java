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
 * @date 22nd July 2011
 */
package au.edu.uts.eng.remotelabs.schedserver.queuer.pojo;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.queuer.pojo.types.QueueAvailability;
import au.edu.uts.eng.remotelabs.schedserver.queuer.pojo.types.QueueSession;

/**
 * The interface for the Queuer POJO service.
 */
public interface QueuerService
{
    /**
     * Checks the availability of a permission to be used. A permission is
     * available if one or more of its resources is online.
     * 
     * @param perm permission to check
     * @param ses database session
     * @return availability type
     */
    public QueueAvailability checkAvailability(ResourcePermission perm, org.hibernate.Session ses);
    
    /**
     * Adds the user to the queue denoted by the permission.
     * 
     * @param user user to add
     * @param perm permission specifying resources
     * @param code batch code reference
     * @param db database session
     * @return queue session information
     */
    public QueueSession addToQueue(User user, ResourcePermission perm, String code, org.hibernate.Session db);
    
    /**
     * Gets the sessions position in the queue. If the user is not in the queue,
     * <tt>-1</tt> is returned.
     *  
     * @param ses session whose position to get
     * @param db database session
     * @return position or -1 if not in queue
     */
    public int getQueuePosition(Session ses, org.hibernate.Session db);
    
    /**
     * Remove the session from the queue.
     * 
     * @param ses session to remove
     * @param reason reason for removal
     * @param db database
     * @return true if user removed from queue
     */
    public boolean removeFromQueue(Session ses, String reason, org.hibernate.Session db);
}
