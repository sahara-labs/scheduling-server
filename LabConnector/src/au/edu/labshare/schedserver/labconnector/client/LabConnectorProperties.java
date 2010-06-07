/**
 * SAHARA Scheduling Server - LabConnector
 * Properties class to derive the external LabConnector web service location
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
 * @author Herbert Yeung
 * @date 27th May 2010
 */
package au.edu.labshare.schedserver.labconnector.client;

// Packages needed for reading properties file (without reinventing the wheel)
//import org.osgi.util.tracker.ServiceTracker;
//import au.edu.uts.eng.remotelabs.schedserver.config.Config;

public class LabConnectorProperties
{
    /** LabConnector properties configuration file location. */
    public static String LABCONNECTOR_PROPERTIES_PATH = "resources/labconnector.properties";
    public static String LABCONNECTOR_PORTNUM         = "labconnector_port";
    public static String LABCONNECTOR_HOSTNAME        = "labconnector_remotehost";
    public static String LABCONNECTOR_PATH            = "labconnector_wspath";

    /** LabConnector configuration. */
    //private IConfig      labConnectorConfig;
    private String       labConnectorURL              = null;

    public LabConnectorProperties()
    {
    }

    public String getProperties()
    {
        final int portNum;
        final String hostname;
        final String wsPath;

        try
        {
            // Read values from the configuration file
            //this.labConnectorConfig = new PropertiesConfig( LABCONNECTOR_PROPERTIES_PATH);
            //portNum = Integer.parseInt(this.labConnectorConfig.getProperty(LABCONNECTOR_PORTNUM));
            //hostname = this.labConnectorConfig.getProperty(LABCONNECTOR_HOSTNAME);
            //wsPath = this.labConnectorConfig.getProperty(LABCONNECTOR_PATH);

            //this.labConnectorURL = "http://" + hostname + ":" + Integer.toString(portNum) + wsPath;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return this.labConnectorURL;
    }
}
