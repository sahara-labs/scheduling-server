/**
 * SAHARA Scheduling Server
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
 * @date 12th February 2010
 */

package au.edu.uts.eng.remotelabs.schedserver.version.inf;

import org.osgi.framework.Bundle;

import au.edu.uts.eng.remotelabs.schedserver.version.VersionActivator;
import au.edu.uts.eng.remotelabs.schedserver.version.inf.types.BundleType;
import au.edu.uts.eng.remotelabs.schedserver.version.inf.types.GetVersions;
import au.edu.uts.eng.remotelabs.schedserver.version.inf.types.GetVersionsResponse;
import au.edu.uts.eng.remotelabs.schedserver.version.inf.types.VersionResponseType;

/**
 * Version skeleton for the Scheduling Server Version bundle interface. 
 */
public class Version implements VersionSkeletonInterface
{
    @Override
    public GetVersionsResponse getVersions(GetVersions nullParam)
    {
        GetVersionsResponse response = new GetVersionsResponse();
        VersionResponseType type = new VersionResponseType();
        response.setGetVersionsResponse(type);
        
        for (Bundle b : VersionActivator.getInstalledBundles())
        {
            BundleType bundle = new BundleType();
            bundle.setSymbolicName(b.getSymbolicName());
            bundle.setId(b.getBundleId());
            bundle.setVersion(b.getVersion().toString());
            bundle.setState(this.stateToString(b.getState()));
            type.addBundle(bundle);
        }
        
        return response;
    }
    
    /**
     * Does a translation from an integer representation of a bundles state to
     * a string format.
     * 
     * @param state bundle state
     * @return representation
     */
    public String stateToString(int state)
    {
        switch (state)
        {
            case Bundle.ACTIVE: return "Active";
            case Bundle.INSTALLED: return "Installed";
            case Bundle.RESOLVED: return "Resolved";
            case Bundle.STARTING: return "Starting";
            case Bundle.STOPPING: return "Stopping";
            default: return "Unknown";
        }
    }
}
