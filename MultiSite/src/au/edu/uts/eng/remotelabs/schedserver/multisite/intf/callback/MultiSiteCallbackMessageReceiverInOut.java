/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2011, Michael Diponio
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
 * @date 28th August 2011
 */

package au.edu.uts.eng.remotelabs.schedserver.multisite.intf.callback;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.QName;

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

import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.callback.types.BookingCancelled;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.callback.types.BookingCancelledResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.callback.types.SessionFinished;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.callback.types.SessionFinishedResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.callback.types.SessionStarted;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.callback.types.SessionStartedResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.callback.types.SessionUpdate;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.callback.types.SessionUpdateResponse;

/**
 * MultiSiteCallbackMessageReceiverInOut message receiver.
 */
public class MultiSiteCallbackMessageReceiverInOut extends AbstractInOutMessageReceiver
{
    @Override
    public void invokeBusinessLogic(final MessageContext msgContext, final MessageContext newMsgContext)
            throws AxisFault
    {
        try
        {
            final Object obj = this.getTheImplementationObject(msgContext);
            final MultiSiteCallbackSOAP skel = (MultiSiteCallbackSOAP) obj;

            SOAPEnvelope envelope = null;

            final AxisOperation op = msgContext.getOperationContext().getAxisOperation();
            if (op == null)
            {
                throw new AxisFault(
                        "Operation is not located, if this is doclit style the SOAP-ACTION should specified via the SOAP Action to use the RawXMLProvider");
            }

            String methodName;
            if ((op.getName() != null)
                    && ((methodName = JavaUtils.xmlNameToJavaIdentifier(op.getName().getLocalPart())) != null))
            {
                if ("bookingCancelled".equals(methodName))
                {
                    final BookingCancelled wrappedParam = (BookingCancelled) this.fromOM(msgContext.getEnvelope()
                            .getBody().getFirstElement(), BookingCancelled.class,
                            this.getEnvelopeNamespaces(msgContext.getEnvelope()));

                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), skel.bookingCancelled(wrappedParam),
                            false, new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteCallback/",
                                    "bookingCancelled"));
                }
                else if ("sessionStarted".equals(methodName))
                {
                    final SessionStarted wrappedParam = (SessionStarted) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), SessionStarted.class,
                            this.getEnvelopeNamespaces(msgContext.getEnvelope()));

                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), skel.sessionStarted(wrappedParam), false,
                            new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteCallback/",
                                    "sessionStarted"));
                }
                else if ("sessionFinished".equals(methodName))
                {
                    final SessionFinished wrappedParam = (SessionFinished) this.fromOM(msgContext.getEnvelope()
                            .getBody().getFirstElement(), SessionFinished.class,
                            this.getEnvelopeNamespaces(msgContext.getEnvelope()));

                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), skel.sessionFinished(wrappedParam), false,
                            new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteCallback/",
                                    "sessionFinished"));
                }
                else if ("sessionUpdate".equals(methodName))
                {
                    final SessionUpdate wrappedParam = (SessionUpdate) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), SessionUpdate.class,
                            this.getEnvelopeNamespaces(msgContext.getEnvelope()));

                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), skel.sessionUpdate(wrappedParam), false,
                            new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteCallback/",
                                    "sessionUpdate"));
                }
                else
                {
                    throw new RuntimeException("method not found");
                }

                newMsgContext.setEnvelope(envelope);
            }
        }
        catch (final Exception e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final BookingCancelledResponse param,
            final boolean optimizeContent, final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(BookingCancelledResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final SessionStartedResponse param,
            final boolean optimizeContent, final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(SessionStartedResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final SessionFinishedResponse param,
            final boolean optimizeContent, final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(SessionFinishedResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final SessionUpdateResponse param,
            final boolean optimizeContent, final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(SessionUpdateResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private Object fromOM(final OMElement param, final Class<?> type, final Map<String, String> extraNamespaces)
            throws AxisFault
    {
        try
        {
            if (BookingCancelled.class.equals(type))
            {
                return BookingCancelled.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (BookingCancelledResponse.class.equals(type))
            {
                return BookingCancelledResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (SessionStarted.class.equals(type))
            {
                return SessionStarted.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (SessionStartedResponse.class.equals(type))
            {
                return SessionStartedResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (SessionFinished.class.equals(type))
            {
                return SessionFinished.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (SessionFinishedResponse.class.equals(type))
            {
                return SessionFinishedResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (SessionUpdate.class.equals(type))
            {
                return SessionUpdate.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (SessionUpdateResponse.class.equals(type))
            {
                return SessionUpdateResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
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
