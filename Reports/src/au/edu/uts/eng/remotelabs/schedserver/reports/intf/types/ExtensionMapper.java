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
 * @author Tania Machet (tmachet)
 * @date 13 December 2010
 */

package au.edu.uts.eng.remotelabs.schedserver.reports.intf.types;

import javax.xml.stream.XMLStreamReader;

import org.apache.axis2.databinding.ADBException;

/**
 * Extension mapper class.
 */
public class ExtensionMapper
{
    public static Object getTypeObject(final String namespaceURI, final String typeName, final XMLStreamReader reader)
            throws Exception
    {
        if ("http://remotelabs.eng.uts.edu.au/schedserver/reports".equals(namespaceURI)
                && "typeForQuery".equals(typeName))
        {
            return TypeForQuery.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/reports".equals(namespaceURI)
                && "QuerySessionAccessResponseType".equals(typeName))
        {
            return QuerySessionAccessResponseType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/reports".equals(namespaceURI) && "operator".equals(typeName))
        {
            return OperatorType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/reports".equals(namespaceURI)
                && "QueryInfoType".equals(typeName))
        {
            return QueryInfoType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/reports".equals(namespaceURI)
                && "QueryFilterType".equals(typeName))
        {
            return QueryFilterType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/reports".equals(namespaceURI)
                && "PaginationType".equals(typeName))
        {
            return PaginationType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/reports".equals(namespaceURI)
                && "QuerySessionReportResponseType".equals(typeName))
        {
            return QuerySessionReportResponseType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/reports".equals(namespaceURI)
                && "QuerySessionAccessType".equals(typeName))
        {
            return QuerySessionAccessType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/reports".equals(namespaceURI)
                && "typeForQuery_type1".equals(typeName))
        {
            return TypeForQuery.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/reports".equals(namespaceURI)
                && "typeForQuery_type3".equals(typeName))
        {
            return TypeForQuery.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/reports".equals(namespaceURI)
                && "SessionReportType".equals(typeName))
        {
            return SessionReportType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/reports".equals(namespaceURI)
                && "AccessReportType".equals(typeName))
        {
            return AccessReportType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/reports".equals(namespaceURI)
                && "RequestorType".equals(typeName))
        {
            return RequestorType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/reports".equals(namespaceURI)
                && "QueryInfoResponseType".equals(typeName))
        {
            return QueryInfoResponseType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/reports".equals(namespaceURI)
                && "QuerySessionReportType".equals(typeName))
        {
            return QuerySessionReportType.Factory.parse(reader);
        }

        throw new ADBException("Unsupported type " + namespaceURI + " " + typeName);
    }
}
