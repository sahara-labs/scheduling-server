/**
 * SAHARA Scheduling Server
 *
 * ScormPackagerProperties class
 * Declaration of properties needed by the scorm packager.
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
 * @author Herber Yeung
 * @date 4th November 2010
 */
package au.edu.labshare.schedserver.scormpackager;


//Packages needed for reading properties file
import java.util.Properties;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import au.edu.uts.eng.remotelabs.schedserver.config.Config;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;

public class ScormPackagerProperties 
{
    /** ScormPackager properties configuration file location. */
    public static String SCORMPACKAGER_SCO_PATH           = "scormpackager_scopath";
    public static String SCORMPACKAGER_EXPERIMENT_TITLE   = "scormpackager_scotitle";

    /** ScormPackager configuration. */
    private Config       scormPackagerConfig;
    private Logger       logger;
    private final Properties   scormPackagerProperties;

    public ScormPackagerProperties(BundleContext context)
    {        
        /* 
         * Load the default properties
         */
        this();
        
        /*
         * Log anything that goes wrong        
         */
        this.logger = LoggerActivator.getLogger();
        
        ServiceReference ref = context.getServiceReference(Config.class.getName());
        this.scormPackagerConfig = (Config)context.getService(ref);
        
        /*
         * Get the properties from either SchedServer/trunk/ScormPackager/scormpackager.properties file
         * or from default values
         */
        loadConfiguration();
    }
    

    public ScormPackagerProperties()
    {
        this.scormPackagerProperties = new Properties();
        populateDefaults();
    }
    
    /**
     * Loads the configuration properties. The following list contains
     * the configuration keys and expected data types:
     * <ul>
     *  <li>scormpackager_scopath - String - The storage path for the SCOs.</li>
     *  <li>scormpackager_scotitle - String - The title used if none is provided for the SCO.</li>
     * </ul>
     */
    private void loadConfiguration()
    { 
        String titleProperty = this.scormPackagerConfig.getProperty(ScormPackagerProperties.SCORMPACKAGER_EXPERIMENT_TITLE);
        if (titleProperty == null)
        {
            this.logger.warn("ScormPackager Title configuration property not set(scormpackager_scotitle) not found," +
                    " using the default (" + this.scormPackagerProperties.getProperty(ScormPackagerProperties.SCORMPACKAGER_EXPERIMENT_TITLE) + ").");
        }
        else
        {
            this.logger.info("ScormPackager SCO Title configuration property as " + titleProperty + ".");
        }
        
        String pathProperty = this.scormPackagerConfig.getProperty(ScormPackagerProperties.SCORMPACKAGER_SCO_PATH);
        if (titleProperty == null)
        {
            this.logger.warn("ScormPackager SCO Storage Path configuration property not set(scormpackager_scopath) not found," +
                    " using the default (" + this.scormPackagerProperties.getProperty(ScormPackagerProperties.SCORMPACKAGER_SCO_PATH) + ").");
        }
        else
        {
            this.logger.info("ScormPackager SCO Storage Path configuration property as " + pathProperty + ".");
        }
        
    }
    
    private void populateDefaults()
    {        
        /* ScormPackager specific defaults. */
        this.scormPackagerProperties.setProperty(ScormPackagerProperties.SCORMPACKAGER_SCO_PATH, "/SCOs/");
        this.scormPackagerProperties.setProperty(ScormPackagerProperties.SCORMPACKAGER_EXPERIMENT_TITLE, "Generic Title");
    }
	
}
