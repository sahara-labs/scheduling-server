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
 * @date 4th January 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.rigprovider.identok;

/**
 * Interface to allow the identity token of a rig to be queried. The identity
 * token is a string that is provided to a rig client so it can authenticate 
 * privileged operations.<br />
 * The life cycle of identity tokens is:
 * <ol>
 *  <li>A rig client registers itself and provided registration is successful,
 *  it is given an identity token by the scheduling server.</li>
 *  <li>The rig client continually updates it's status and may optionally
 *  receive a new identity token from the scheduling server.</li>
 *  <li>When the scheduling server performs a privileged operation on the
 *  scheduling server (for example allocate), the scheduling server provides
 *  the identity token to authenticate itself. If the identity token is 
 *  not the same as the last identity token provided from either registration
 *  or a status update, the rig client rejects performing the requested 
 *  operation.</li>
 *  <li>When the rig client removes itself, the identity token is destroyed for
 *  the rig client.</li>
 * </ol>
 * Notes about identity tokens:
 * <ul>
 *  <li>Identity tokens are not likely to be the same across different rigs,
 *  so should be obtained on a per rig based.</li>
 *  <li>Identity tokens are not likely to remain static across the life
 *  or a rig registration - status update - removal life cycle, so they
 *  should not be cached. The should be requested on a per use basis.</li>
 * </ul>
 */
public interface IdentityToken
{
    /**
     * Gets the identity token for a specified rig. 
     * 
     * @param rigName name of the rig to return the identity token of
     * @return identity token for rig or null if identity token of \
     *    rig does not exist
     */
    public String getIdentityToken(String rigName);
}
