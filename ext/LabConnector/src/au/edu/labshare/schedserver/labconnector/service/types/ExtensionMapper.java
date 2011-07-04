/**
 * SAHARA Scheduling Server - LabConnector
 * Class type for the web service.
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
/**
 * ExtensionMapper.java
 * 
 * This file was auto-generated from WSDL by the Apache Axis2 version: 1.4 Built
 * on : Apr 26, 2008 (06:25:17 EDT)
 */

package au.edu.labshare.schedserver.labconnector.service.types;

/**
 * ExtensionMapper class
 */

public class ExtensionMapper
{

    public static java.lang.Object getTypeObject(java.lang.String namespaceURI,
            java.lang.String typeName, javax.xml.stream.XMLStreamReader reader)
            throws java.lang.Exception
    {

        if ("http://labshare.edu.au:8080/LabConnector/".equals(namespaceURI)
                && "Permissions".equals(typeName))
        {

            return au.edu.labshare.schedserver.labconnector.service.types.Permissions.Factory
                    .parse(reader);

        }

        if ("http://labshare.edu.au:8080/LabConnector/".equals(namespaceURI)
                && "ExperimentType".equals(typeName))
        {

            return au.edu.labshare.schedserver.labconnector.service.types.ExperimentType.Factory
                    .parse(reader);

        }

        if ("http://labshare.edu.au:8080/LabConnector/".equals(namespaceURI)
                && "BookingType".equals(typeName))
        {

            return au.edu.labshare.schedserver.labconnector.service.types.BookingType.Factory
                    .parse(reader);

        }

        if ("http://labshare.edu.au:8080/LabConnector/".equals(namespaceURI)
                && "ExperimentUserInput".equals(typeName))
        {

            return au.edu.labshare.schedserver.labconnector.service.types.ExperimentUserInput.Factory
                    .parse(reader);

        }

        if ("http://labshare.edu.au:8080/LabConnector/".equals(namespaceURI)
                && "ExperimentStatus".equals(typeName))
        {

            return au.edu.labshare.schedserver.labconnector.service.types.ExperimentStatus.Factory
                    .parse(reader);

        }

        if ("http://labshare.edu.au:8080/LabConnector/".equals(namespaceURI)
                && "InteractionType".equals(typeName))
        {

            return au.edu.labshare.schedserver.labconnector.service.types.InteractionType.Factory
                    .parse(reader);

        }

        throw new org.apache.axis2.databinding.ADBException("Unsupported type "
                + namespaceURI + " " + typeName);
    }

}
