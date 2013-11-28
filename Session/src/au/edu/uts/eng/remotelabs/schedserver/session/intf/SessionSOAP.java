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

package au.edu.uts.eng.remotelabs.schedserver.session.intf;

import au.edu.uts.eng.remotelabs.schedserver.session.intf.types.EnableCollaboration;
import au.edu.uts.eng.remotelabs.schedserver.session.intf.types.EnableCollaborationResponse;
import au.edu.uts.eng.remotelabs.schedserver.session.intf.types.FinishSession;
import au.edu.uts.eng.remotelabs.schedserver.session.intf.types.FinishSessionResponse;
import au.edu.uts.eng.remotelabs.schedserver.session.intf.types.GetSessionInformation;
import au.edu.uts.eng.remotelabs.schedserver.session.intf.types.GetSessionInformationResponse;

/**
 * SessionSkeletonInterface java skeleton interface for the axisService
 */
public interface SessionSOAP
{
    /**
     * Terminates the users rig session if they have one.
     * 
     * @param request
     * @return response
     */
    public FinishSessionResponse finishSession(FinishSession request);

    /**
     * Provides information about the users session. If the <tt>isInSession</tt> 
     * field is <tt>false</tt>, not other information is provide, otherwise if it
     * is <tt>true</tt>, all other fields are populated.
     * 
     * @param request
     * @return response
     */
    public GetSessionInformationResponse getSessionInformation(GetSessionInformation request);
    
    /**
     * Enables collaboration for the current rig session.
     * 
     * @param request
     * @return response
     */
    public EnableCollaborationResponse enableCollaboration(EnableCollaboration request);
}
