/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2009, University of Technology, Sydney
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
 * @date 28th December 2009
 */
package io.rln.ss.utils.config;

/**
 * Configuration service loader. 
 */
public interface ConfigServiceLoader
{
    /**
     * Loads a configuration ({@link Config} instance which uses the provided
     * configuration file (<code>fileLoc</code> property). If the configuration
     * file has previously been loaded, the same instance is returned.
     * <p>
     * The loaded configuration instance is registered as a bundle service with
     * the <code>config.loc</code> set to the configuration file location.
     * 
     * @param fileLoc configuration file location
     * @return IConfig instance.
     */
    public Config loadService(final String fileLoc);
    
    /**
     * Unloads the configuration file service with has the config.loc
     * property set to <code>fileLoc</code>.
     * 
     * @param fileLoc configuration file to unload
     */
    public void unloadService(final String fileLoc);
    
    /**
     * Returns <code>true</code> if the configuration file <code>fileLoc</code>
     * is loaded.
     * 
     * @param fileLoc configuration file location
     * @return true if loaded
     */
    public boolean isConfigLoaded(final String fileLoc);
}
