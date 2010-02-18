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
package au.edu.uts.eng.remotelabs.schedserver.rigprovider.identok.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.identok.IdentityToken;

/**
 * Holds the registry containing all the identity tokens for the registered 
 * rigs.
 */
public class IdentityTokenRegister implements IdentityToken
{
    /** The number of characters comprising a identity token. */
    public static final int IDENTITY_TOKEN_LENGTH = 20;
    
    /** Average number times the <code>getOrGenerateIdentityToken</code> 
     *  method returns the same identity token. */
    public static final int AVERAGE_IDENTITY_TOKEN_LIFE = 50;
    
    /** The registry of identity tokens keyed by rig name. */
    private Map<String, String> registry;
    
    /** Random number generator. */
    private final Random randomGen;
    
    /** Singleton instance. */
    private static IdentityTokenRegister instance = new IdentityTokenRegister();
    
    /** Logger. */
    private final Logger logger;
    
    private IdentityTokenRegister()
    {
        this.logger = LoggerActivator.getLogger();
        
        this.randomGen = new Random();
        this.registry = Collections.synchronizedMap(new HashMap<String, String>());
    }
    
    /**
     * Generates a random identity token for the specified rig and adds it to 
     * the registry. The identity token can be any ASCII visible printing
     * character excluding '\' (backslash) and ''' (apostrophe) characters.
     * 
     * @param rigName name of the rig to generate an identity token for
     * @return newly generated identity token
     */
    public String generateIdentityToken(String rigName)
    {
        StringBuilder builder = new StringBuilder(IdentityTokenRegister.IDENTITY_TOKEN_LENGTH);
        
        int lookup;
        for (int i = 0; i < IDENTITY_TOKEN_LENGTH; i++)
            
        {
            if ((lookup = this.randomGen.nextInt(94) + 33) != 39 && lookup != 92) // Exclude the ''' & '\' characters
            {
                builder.append((char)lookup);
            }
            else
            {
                i--;
            }
        }
        
        this.logger.debug("Generating identity token for rig '" + rigName + "' is " + builder.toString());
        this.registry.put(rigName, builder.toString());
        
        return builder.toString();
    }
    
    /**
     * May either return the existing token for the rig or may generate and 
     * return a new identity token for the rig. If the rig does not have 
     * a identity token, it is always generated.
     * 
     * @param rigName name of the rig
     * @return existing or generated identity token
     */
    public String getOrGenerateIdentityToken(final String rigName)
    {
        synchronized (this.registry)
        {
            if (!this.registry.containsKey(rigName) || 
                    this.randomGen.nextInt(IdentityTokenRegister.AVERAGE_IDENTITY_TOKEN_LIFE + 1) == 0)
            {
                return this.generateIdentityToken(rigName);
            }
            else
            {
                return this.getIdentityToken(rigName);
            }
        }
    }
    
    @Override
    public String getIdentityToken(final String rigName)
    {
        return this.registry.get(rigName);
    }
    
    /**
     * Removes the identity token of the specified rig from the registry.
     * 
     * @param rigName name of rig to remove from registry
     */
    public void removeIdentityToken(final String rigName)
    {
        this.registry.remove(rigName);
    }
    
    /**
     * Expunges all stored identity tokens.
     */
    public void expunge()
    {
        this.registry.clear();
    }
    
    /**
     * Gets an instance of this class.
     * 
     * @return instance
     */
    public static IdentityTokenRegister getInstance()
    {
        return IdentityTokenRegister.instance;
    }
}
