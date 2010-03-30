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
 * @date 11th January 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.dataaccess.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Performs some transformations to capabilities string.
 * <p />
 * A normalized capabilities string is one with no duplicate tokens, no 
 * whitespace around tokens, with all tokens lower case and sorted by the
 * natural order of the capabilities string tokens.
 */
public class Capabilities
{
    /** Normalized capabilities string. */
    private String normalizedCaps;
    
    /** The tokens in the capabilities string. */
    private final List<String> tokens;
    
    /**
     * Constructor. The provided capabilities must have a single comma
     * as a demarcation between capability tokens.
     * 
     * @param caps capabilities
     */
    public Capabilities(final String caps)
    {
        this.tokens = new ArrayList<String>();
        this.normalize(caps);
    }
    
    public Capabilities()
    {
        this.tokens = new ArrayList<String>();
    }
    
    /**
     * Returns a normalized version of the capabilities string.
     * 
     * @return normalized capabilities string
     */
    public String asCapabilitiesString()
    {
        return this.normalizedCaps;
    }
    
    /**
     * Returns a list of normalized tokens of the capabilities string.
     * 
     * @return capabilities token list
     */
    public List<String> asCapabilitiesList()
    {
        return Collections.unmodifiableList(this.tokens);
    }
    
    /**
     * Returns an array of normalized tokens of the capabilities string.
     * 
     * @return capabilities token array
     */
    public String[] asCapabilitiesArray()
    {
        return this.tokens.toArray(new String[this.tokens.size()]);
    }
    
    /**
     * Normalize the the capabilities string.
     * 
     * @param caps unnormalized capabilities string
     */
    private void normalize(final String caps)
    {
        final String toks[] = caps.split(",");
        for (String tok : toks)
        {
            tok = tok.trim();
            tok = tok.toLowerCase();
            if (tok.length() > 0 && !this.tokens.contains(tok))
            {
                this.tokens.add(tok);
            }
        }
        
        Collections.sort(this.tokens);
        
        final StringBuilder builder = new StringBuilder(caps.length());
        for (String tok : this.tokens)
        {
            if (builder.length() > 0)
            {
                builder.append(',');
            }
            builder.append(tok);
        }
        this.normalizedCaps = builder.toString();
    }
    
    /**
     * Resets this capabilities, adds the specified capabilities and
     * normalizes them.
     * 
     * @param caps new capabilities string
     */
    public void setCapabilitiesString(final String caps)
    {
        this.tokens.clear();
        this.normalize(caps);
    }
    
    /**
     * Returns the normalized capabilities string.
     */
    @Override
    public String toString()
    {
        return this.normalizedCaps;
    }
    
    
    /**
     * Returns true if the (normalized) capabilities string match.
     * 
     * @return true if the object matches, false otherwise
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == this) return true;
        if (obj == null) return false;
        if (!(obj instanceof Capabilities)) return false;
        
        Capabilities caps = (Capabilities)obj;
        if (this.normalizedCaps != null)
        {
            return this.normalizedCaps.equals(caps.asCapabilitiesString());
        }
        return false;
    }
    
    /**
     * Hash code based on the normalized capabilities string.
     * 
     * @return hash code
     */
    @Override
    public int hashCode()
    {
        int hashcode = 17;
        if (this.normalizedCaps != null)
        {
            hashcode = 31 * hashcode + this.normalizedCaps.hashCode();
        }
        return hashcode;
    }
    
}
