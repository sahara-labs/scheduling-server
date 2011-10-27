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
 * @date 26th October 2011
 */

package au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation;

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

import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.DiscoverResources;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.DiscoverResourcesResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.GetRequests;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.GetRequestsResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.InitiateSite;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.InitiateSiteResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.NotifyAccept;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.NotifyAcceptResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.NotifyCancel;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.NotifyCancelResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.NotifyModify;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.NotifyModifyResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.RequestResource;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.RequestResourceResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.SiteReconnect;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.SiteReconnectResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.SiteShutdown;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.SiteShutdownResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.SiteStatus;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types.SiteStatusResponse;

/**
 * MultiSite federation service message receiver.
 */
public class MultiSiteFederationMessageReceiverInOut extends AbstractInOutMessageReceiver
{
    @Override
    public void invokeBusinessLogic(final MessageContext msgContext, final MessageContext newMsgContext)
            throws AxisFault
    {
        try
        {
            final MultiSiteFederationSOAP impl = (MultiSiteFederationSOAP) this.getTheImplementationObject(msgContext);

            SOAPEnvelope envelope = null;
            final AxisOperation op = msgContext.getOperationContext().getAxisOperation();
            if (op == null)
            {
                throw new AxisFault("Operation is not located, if this is doclit style the SOAP-ACTION should " +
                		"specified via the SOAP Action to use the RawXMLProvider");
            }

            String methodName;
            if ((op.getName() != null)
                    && ((methodName = JavaUtils.xmlNameToJavaIdentifier(op.getName().getLocalPart())) != null))
            {
                if ("siteShutdown".equals(methodName))
                {
                    final SiteShutdown wrappedParam = (SiteShutdown) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), SiteShutdown.class,
                            this.getEnvelopeNamespaces(msgContext.getEnvelope()));

                    SiteShutdownResponse response = impl.siteShutdown(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), response, false,
                            new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/",
                                    "siteShutdown"));
                }
                else if ("siteReconnect".equals(methodName))
                {
                    final SiteReconnect wrappedParam = (SiteReconnect) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), SiteReconnect.class,
                            this.getEnvelopeNamespaces(msgContext.getEnvelope()));

                    SiteReconnectResponse response = impl.siteReconnect(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), response, false,
                            new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/",
                                    "siteReconnect"));
                }
                else if ("requestResource".equals(methodName))
                {
                    final RequestResource wrappedParam = (RequestResource) this.fromOM(msgContext.getEnvelope()
                            .getBody().getFirstElement(), RequestResource.class,
                            this.getEnvelopeNamespaces(msgContext.getEnvelope()));

                    RequestResourceResponse response = impl.requestResource(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), response, false,
                            new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/",
                                    "requestResource"));
                }
                else if ("notifyCancel".equals(methodName))
                {
                    final NotifyCancel wrappedParam = (NotifyCancel) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), NotifyCancel.class,
                            this.getEnvelopeNamespaces(msgContext.getEnvelope()));

                    NotifyCancelResponse response = impl.notifyCancel(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), response, false,
                            new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/",
                                    "notifyCancel"));
                }
                else if ("notifyAccept".equals(methodName))
                {
                    final NotifyAccept wrappedParam = (NotifyAccept) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), NotifyAccept.class,
                            this.getEnvelopeNamespaces(msgContext.getEnvelope()));

                    NotifyAcceptResponse response =impl.notifyAccept(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), response, false,
                            new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/",
                                    "notifyAccept"));
                }
                else if ("notifyModify".equals(methodName))
                {
                    final NotifyModify wrappedParam = (NotifyModify) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), NotifyModify.class,
                            this.getEnvelopeNamespaces(msgContext.getEnvelope()));

                    NotifyModifyResponse response = impl.notifyModify(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), response, false,
                            new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/",
                                    "notifyModify"));
                }
                else if ("initiateSite".equals(methodName))
                {
                    final InitiateSite wrappedParam = (InitiateSite) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), InitiateSite.class,
                            this.getEnvelopeNamespaces(msgContext.getEnvelope()));

                    InitiateSiteResponse response = impl.initiateSite(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), response, false,
                            new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/",
                                    "initiateSite"));
                }
                else if ("getRequests".equals(methodName))
                {
                    final GetRequests wrappedParam = (GetRequests) this
                            .fromOM(msgContext.getEnvelope().getBody().getFirstElement(), GetRequests.class,
                                    this.getEnvelopeNamespaces(msgContext.getEnvelope()));

                    GetRequestsResponse response = impl.getRequests(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), response, false,
                            new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/",
                                    "getRequests"));
                }
                else if ("siteStatus".equals(methodName))
                {
                    final SiteStatus wrappedParam = (SiteStatus) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), SiteStatus.class, this.getEnvelopeNamespaces(msgContext.getEnvelope()));

                    SiteStatusResponse response = impl.siteStatus(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), response, false,
                            new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/", "siteStatus"));
                }
                else if ("discoverResources".equals(methodName))
                {
                    final DiscoverResources wrappedParam = (DiscoverResources) this.fromOM(msgContext.getEnvelope()
                            .getBody().getFirstElement(), DiscoverResources.class,
                            this.getEnvelopeNamespaces(msgContext.getEnvelope()));

                    DiscoverResourcesResponse response = impl.discoverResources(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), response, false,
                            new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/",
                                    "discoverResources"));
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

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final SiteShutdownResponse param,
            final boolean optimizeContent, final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(SiteShutdownResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final SiteReconnectResponse param,
            final boolean optimizeContent, final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(SiteReconnectResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final RequestResourceResponse param,
            final boolean optimizeContent, final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(RequestResourceResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final NotifyCancelResponse param,
            final boolean optimizeContent, final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(NotifyCancelResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final NotifyAcceptResponse param,
            final boolean optimizeContent, final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(NotifyAcceptResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final NotifyModifyResponse param,
            final boolean optimizeContent, final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(NotifyModifyResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final InitiateSiteResponse param,
            final boolean optimizeContent, final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(InitiateSiteResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final GetRequestsResponse param,
            final boolean optimizeContent, final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(GetRequestsResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final SiteStatusResponse param,
            final boolean optimizeContent, final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(SiteStatusResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final DiscoverResourcesResponse param,
            final boolean optimizeContent, final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(DiscoverResourcesResponse.MY_QNAME, factory));
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
            if (SiteShutdown.class.equals(type))
            {
                return SiteShutdown.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (SiteShutdownResponse.class.equals(type))
            {
                return SiteShutdownResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (SiteReconnect.class.equals(type))
            {
                return SiteReconnect.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (SiteReconnectResponse.class.equals(type))
            {
                return SiteReconnectResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (RequestResource.class.equals(type))
            {
                return RequestResource.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (RequestResourceResponse.class.equals(type))
            {
                return RequestResourceResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (NotifyCancel.class.equals(type))
            {
                return NotifyCancel.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (NotifyCancelResponse.class.equals(type))
            {
                return NotifyCancelResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (NotifyAccept.class.equals(type))
            {
                return NotifyAccept.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (NotifyAcceptResponse.class.equals(type))
            {
                return NotifyAcceptResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (NotifyModify.class.equals(type))
            {
                return NotifyModify.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (NotifyModifyResponse.class.equals(type))
            {
                return NotifyModifyResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (InitiateSite.class.equals(type))
            {
                return InitiateSite.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (InitiateSiteResponse.class.equals(type))
            {
                return InitiateSiteResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetRequests.class.equals(type))
            {
                return GetRequests.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetRequestsResponse.class.equals(type))
            {
                return GetRequestsResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (SiteStatus.class.equals(type))
            {
                return SiteStatus.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (SiteStatusResponse.class.equals(type))
            {
                return SiteStatusResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (DiscoverResources.class.equals(type))
            {
                return DiscoverResources.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (DiscoverResourcesResponse.class.equals(type))
            {
                return DiscoverResourcesResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
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
