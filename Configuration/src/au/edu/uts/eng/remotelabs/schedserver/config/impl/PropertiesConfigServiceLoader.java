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
package au.edu.uts.eng.remotelabs.schedserver.config.impl;

import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import au.edu.uts.eng.remotelabs.schedserver.config.Config;

/**
 * Loads {@link PropertiesConfig} configuration classes.
 * <br />
 * The default configuration is either specified by the system property 
 * <tt>config.file</tt> or if that is not specified, the directory
 * '<tt>conf/schedulingserver.properties</tt>' relative to the current working 
 * directory is used.
 */
public class PropertiesConfigServiceLoader implements ConfigServiceLoaderImpl
{
    /** Location of default properties file. */
    public static final String DEFAULT_CONFIG_FILE = "conf/schedulingserver.properties";
    
    /** Bundle context. */
    private  final BundleContext context;

    /** Loaded IConfig instances keyed with the configuration file location. */
    private final Map<String, Config> loadedConfig;
    
    /** Service registrations. */
    private final Map<String, ServiceRegistration> registrations;
    
    /**
     * Constructor. 
     * 
     * @param cont bundle context
     */
    public PropertiesConfigServiceLoader(final BundleContext cont)
    {
        this.context = cont;
        this.loadedConfig = Collections.synchronizedMap(new HashMap<String, Config>());
        this.registrations = Collections.synchronizedMap(new HashMap<String, ServiceRegistration>());
    }
    
    @Override
    public boolean isConfigLoaded(final String fileLoc)
    {
        return this.loadedConfig.containsKey(fileLoc);
    }

    @Override
    public Config loadService(final String fileLoc)
    {
        if (this.isConfigLoaded(fileLoc))
        {
            return this.loadedConfig.get(fileLoc);
        }
        
        final Config config = new PropertiesConfig(fileLoc);
        this.loadedConfig.put(fileLoc, config);
        
        /* Register service. */
        final Dictionary<String, String> props = new Hashtable<String, String>(1);
        props.put("config.loc", fileLoc);
        this.registrations.put(fileLoc, this.context.registerService(Config.class.getCanonicalName(), config, props));
        
        return config;
    }
    
    @Override
    public void loadDefault()
    {
        final Config config = new PropertiesConfig();
        
        this.loadedConfig.put(System.getProperty("config.file", PropertiesConfig.DEFAULT_CONFIG_FILE), config);
        System.err.println("Loaded " + config.getConfigurationInfomation() + ".");
        
        final Dictionary<String, String> props = new Hashtable<String, String>();
        props.put("config.loc", PropertiesConfig.DEFAULT_CONFIG_FILE);
        props.put("default", "true");
        props.put("common", "true");
        
        this.registrations.put(PropertiesConfig.DEFAULT_CONFIG_FILE, 
                this.context.registerService(Config.class.getName(), config, props));
    }

    @Override
    public void unloadService(final String fileLoc)
    {
        if (!this.isConfigLoaded(fileLoc)) return;
        
        this.registrations.get(fileLoc).unregister();
        this.registrations.remove(fileLoc);
        this.loadedConfig.remove(fileLoc);
    }

    @Override
    public void unloadAll()
    {
        for (Entry<String, Config> e : this.loadedConfig.entrySet())
        {
            this.registrations.get(e.getKey()).unregister();
        }
        this.loadedConfig.clear();
        this.registrations.clear();
    }  
}
