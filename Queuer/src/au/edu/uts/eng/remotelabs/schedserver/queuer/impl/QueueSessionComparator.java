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
 * @date 2nd April 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.queuer.impl;

import java.util.Comparator;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;

/**
 * Comparator which returns which queueing session should have precendence 
 * over the other session. The compare function returns a negative number 
 * if the first argument should have precendence over the second number;
 * zero if they should have the same precendence; a positive number if the
 * second argument should have precendence over the first argument.
 * <br />
 * The actual comparison is based on:
 * <ol>
 *  <li>The stored priority. The session with the lowest priority has 
 *  precedence.</li>
 *  <li>If the priority is the same, the request time. The session with
 *  the earliest request time has precedence.</li>
 * </ol>
 */
public class QueueSessionComparator implements Comparator<Session>
{
    @Override
    public int compare(Session arg0, Session arg1)
    {
        if (arg0.getPriority() < arg1.getPriority())
        {
            return -1;
        }
        else if (arg0.getPriority() > arg1.getPriority())
        {
            return 1;
        }
        else
        {
            /* Both sessions have the same priority, so the comparasion is the
             * request time, with the earliest request time winning. */
            return arg0.getRequestTime().compareTo(arg1.getRequestTime());
        }
    }
}
