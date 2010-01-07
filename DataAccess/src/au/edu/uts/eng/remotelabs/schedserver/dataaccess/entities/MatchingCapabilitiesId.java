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
 * @date 6th January 2010
 */

package au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities;

// Generated 06/01/2010 5:09:20 PM by Hibernate Tools 3.2.5.Beta

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * Primary key for the matching_capabilities table composed of a 
 * rig_capabilities primary key and request_capabilities primary key.
 */
@Embeddable
public class MatchingCapabilitiesId implements java.io.Serializable
{
    /** Serilizable class. */
    private static final long serialVersionUID = 8865428330884162127L;
    
    /** Rig capabilities key. */
    private long rigCapabilities;
    
    /** Request capabilities key. */
    private long requestCapabilities;

    public MatchingCapabilitiesId()
    {
        /* Bean style constructor. */
    }

    public MatchingCapabilitiesId(final long rigCapabilities,
            final long requestCapabilities)
    {
        this.rigCapabilities = rigCapabilities;
        this.requestCapabilities = requestCapabilities;
    }

    @Column(name = "rig_capabilities", nullable = false)
    public long getRigCapabilities()
    {
        return this.rigCapabilities;
    }

    public void setRigCapabilities(final long rigCapabilities)
    {
        this.rigCapabilities = rigCapabilities;
    }

    @Column(name = "request_capabilities", nullable = false)
    public long getRequestCapabilities()
    {
        return this.requestCapabilities;
    }

    public void setRequestCapabilities(final long requestCapabilities)
    {
        this.requestCapabilities = requestCapabilities;
    }

    @Override
    public boolean equals(final Object other)
    {
        if ((this == other))
        {
            return true;
        }
        if ((other == null))
        {
            return false;
        }
        if (!(other instanceof MatchingCapabilitiesId))
        {
            return false;
        }
        final MatchingCapabilitiesId castOther = (MatchingCapabilitiesId) other;

        return (this.getRigCapabilities() == castOther.getRigCapabilities())
                && (this.getRequestCapabilities() == castOther.getRequestCapabilities());
    }

    @Override
    public int hashCode()
    {
        int result = 17;

        result = 37 * result + (int) this.getRigCapabilities();
        result = 37 * result + (int) this.getRequestCapabilities();
        return result;
    }

}
