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
 * @date 29th November 2010
 */


package au.edu.uts.eng.remotelabs.schedserver.reports.intf;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.databinding.ADBException;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.receivers.AbstractInOutMessageReceiver;
import org.apache.axis2.util.JavaUtils;

import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryInfo;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryInfoResponse;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionAccess;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionAccessResponse;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionReport;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionReportResponse;

/**
 * Reports service message receiver.
 */
public class ReportsMessageReceiverInOut extends AbstractInOutMessageReceiver
{
    @Override
    public void invokeBusinessLogic(final MessageContext msgContext, final MessageContext newMsgContext)
            throws AxisFault
    {
        try
        {
            final ReportsSkeletonInterface skel = (ReportsSkeletonInterface)this.getTheImplementationObject(msgContext);
         
            SOAPEnvelope envelope = null;
         
            final AxisOperation op = msgContext.getOperationContext().getAxisOperation();
            if (op == null)
            {
                throw new AxisFault("Operation is not located, if this is doclit style the SOAP-ACTION should " +
                		"specified via the SOAP Action to use the RawXMLProvider");
            }

            String methodName;
            if ((op.getName() != null) && ((methodName = JavaUtils.xmlNameToJava(op.getName().getLocalPart())) != null))
            {
                if ("querySessionAccess".equals(methodName))
                {
                    final QuerySessionAccess wrappedParam = (QuerySessionAccess) this.fromOM(msgContext.getEnvelope()
                            .getBody().getFirstElement(), QuerySessionAccess.class,
                            this.getEnvelopeNamespaces(msgContext.getEnvelope()));

                    QuerySessionAccessResponse response = skel.querySessionAccess(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), response, false);
                }
                else if ("queryInfo".equals(methodName))
                {
                    final QueryInfo wrappedParam = (QueryInfo) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), QueryInfo.class, this.getEnvelopeNamespaces(msgContext.getEnvelope()));

                    QueryInfoResponse response = skel.queryInfo(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), response, false);
                }
                else if ("querySessionReport".equals(methodName))
                {
                    final QuerySessionReport wrappedParam = (QuerySessionReport) this.fromOM(msgContext.getEnvelope()
                            .getBody().getFirstElement(), QuerySessionReport.class,
                            this.getEnvelopeNamespaces(msgContext.getEnvelope()));

                    QuerySessionReportResponse response = skel.querySessionReport(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), response, false);
                }
                else
                {
                    throw new RuntimeException("SOAP operation not found.");
                }

                newMsgContext.setEnvelope(envelope);
            }
        }
        catch (final Exception e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final QuerySessionAccessResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(QuerySessionAccessResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final QueryInfoResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(QueryInfoResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final QuerySessionReportResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(QuerySessionReportResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private Object fromOM(final OMElement param, final Class<?> type, final Map<String, String> extraNamespaces) throws AxisFault
    {
        try
        {
            if (QuerySessionAccess.class.equals(type))
            {
                return QuerySessionAccess.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (QuerySessionAccessResponse.class.equals(type))
            {
                return QuerySessionAccessResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (QueryInfo.class.equals(type))
            {
                return QueryInfo.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (QueryInfoResponse.class.equals(type))
            {
                return QueryInfoResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (QuerySessionReport.class.equals(type))
            {
                return QuerySessionReport.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (QuerySessionReportResponse.class.equals(type))
            {
                return QuerySessionReportResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }
        }
        catch (final Exception e)
        {
            throw AxisFault.makeFault(e);
        }
        return null;
    }

    private Map<String, String> getEnvelopeNamespaces(final SOAPEnvelope env)
    {
        final Map<String, String> returnMap = new HashMap<String, String>();
        final Iterator<?> namespaceIterator = env.getAllDeclaredNamespaces();
        while (namespaceIterator.hasNext())
        {
            final OMNamespace ns = (OMNamespace) namespaceIterator.next();
            returnMap.put(ns.getPrefix(), ns.getNamespaceURI());
        }
        return returnMap;
    }
}
