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

package au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.client;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.FaultMapKey;
import org.apache.axis2.client.OperationClient;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.client.Stub;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.databinding.ADBException;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.OutInAxisOperation;
import org.apache.axis2.description.WSDL2Constants;
import org.apache.axis2.wsdl.WSDLConstants;

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
 * MultiSiteFederation client implementation.
 */
public class MultiSiteFederationClientStub extends Stub implements MultiSiteFederationClient
{
    protected AxisOperation[] _operations;

    private final HashMap<?, ?> faultExceptionNameMap = new HashMap<Object, Object>();
    private final HashMap<?, ?> faultExceptionClassNameMap = new HashMap<Object, Object>();
    private final HashMap<?, ?> faultMessageMap = new HashMap<Object, Object>();

    private static int counter = 0;

    private static synchronized String getUniqueSuffix()
    {
        if (MultiSiteFederationClientStub.counter > 99999)
        {
            MultiSiteFederationClientStub.counter = 0;
        }
        MultiSiteFederationClientStub.counter = MultiSiteFederationClientStub.counter + 1;
        return Long.toString(System.currentTimeMillis()) + "_" + MultiSiteFederationClientStub.counter;
    }

    private void populateAxisService() throws AxisFault
    {
        this._service = new AxisService("MultiSiteFederation" + MultiSiteFederationClientStub.getUniqueSuffix());
        this.addAnonymousOperations();

        AxisOperation __operation;

        this._operations = new AxisOperation[10];
        __operation = new OutInAxisOperation();
        __operation.setName(new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/",
                "siteShutdown"));
        this._service.addOperation(__operation);
        this._operations[0] = __operation;

        __operation = new OutInAxisOperation();
        __operation.setName(new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/",
                "siteReconnect"));
        this._service.addOperation(__operation);
        this._operations[1] = __operation;
        __operation = new OutInAxisOperation();

        __operation.setName(new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/",
                "requestResource"));
        this._service.addOperation(__operation);
        this._operations[2] = __operation;
        
        __operation = new OutInAxisOperation();
        __operation.setName(new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/",
                "notifyCancel"));
        this._service.addOperation(__operation);
        this._operations[3] = __operation;

        __operation = new OutInAxisOperation();
        __operation.setName(new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/",
                "notifyAccept"));
        this._service.addOperation(__operation);
        this._operations[4] = __operation;

        __operation = new OutInAxisOperation();
        __operation.setName(new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/",
                "notifyModify"));
        this._service.addOperation(__operation);
        this._operations[5] = __operation;

        __operation = new OutInAxisOperation();
        __operation.setName(new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/",
                "initiateSite"));
        this._service.addOperation(__operation);
        this._operations[6] = __operation;

        __operation = new OutInAxisOperation();
        __operation.setName(new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/",
                "getRequests"));
        this._service.addOperation(__operation);
        this._operations[7] = __operation;

        __operation = new OutInAxisOperation();
        __operation
                .setName(new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/", "siteStatus"));
        this._service.addOperation(__operation);
        this._operations[8] = __operation;

        __operation = new OutInAxisOperation();
        __operation.setName(new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/",
                "discoverResources"));
        this._service.addOperation(__operation);
        this._operations[9] = __operation;
    }

    public MultiSiteFederationClientStub(final ConfigurationContext configurationContext, final String targetEndpoint)
            throws AxisFault
    {
        this(configurationContext, targetEndpoint, false);
    }

    public MultiSiteFederationClientStub(final ConfigurationContext configurationContext, final String targetEndpoint,
            final boolean useSeparateListener) throws AxisFault
    {
        this.populateAxisService();

        this._serviceClient = new ServiceClient(configurationContext, this._service);
        this._serviceClient.getOptions().setTo(new EndpointReference(targetEndpoint));
        this._serviceClient.getOptions().setUseSeparateListener(useSeparateListener);
    }

    public MultiSiteFederationClientStub(final String targetEndpoint) throws AxisFault
    {
        this(null, targetEndpoint);
    }

    @Override
    public SiteShutdownResponse siteShutdown(final SiteShutdown siteShutdown) throws RemoteException
    {
        MessageContext _messageContext = null;
        try
        {
            final OperationClient _operationClient = this._serviceClient.createClient(this._operations[0].getName());
            _operationClient.getOptions().setAction(
                    "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/siteShutdown");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            _messageContext = new MessageContext();

            SOAPEnvelope env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), siteShutdown,
                    this.optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/",
                            "siteShutdown")), new QName(
                            "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/", "siteShutdown"));

            this._serviceClient.addHeadersToEnvelope(env);
            _messageContext.setEnvelope(env);
            _operationClient.addMessageContext(_messageContext);
            _operationClient.execute(true);

            final MessageContext _returnMessageContext = _operationClient.getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            final SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            return (SiteShutdownResponse) this.fromOM(_returnEnv.getBody().getFirstElement(), SiteShutdownResponse.class,
                    this.getEnvelopeNamespaces(_returnEnv));
        }
        catch (final AxisFault f)
        {
            final OMElement faultElt = f.getDetail();
            if (faultElt != null)
            {
                if (this.faultExceptionNameMap.containsKey(new FaultMapKey(faultElt.getQName(), "siteShutdown")))
                {
                    try
                    {
                        final String exceptionClassName = (String) this.faultExceptionClassNameMap.get(new FaultMapKey(
                                faultElt.getQName(), "siteShutdown"));
                        final Class<?> exceptionClass = Class.forName(exceptionClassName);
                        final Exception ex = (Exception) exceptionClass.newInstance();
                 
                        final String messageClassName = (String) this.faultMessageMap.get(new FaultMapKey(faultElt
                                .getQName(), "siteShutdown"));
                        final Class<?> messageClass = Class.forName(messageClassName);
                        final Object messageObject = this.fromOM(faultElt, messageClass, null);
                        final Method m = exceptionClass.getMethod("setFaultMessage", new Class[] { messageClass });
                        m.invoke(ex, new Object[] { messageObject });

                        throw new RemoteException(ex.getMessage(), ex);
                    }
                    catch (final ClassCastException e)
                    {
                        throw f;
                    }
                    catch (final ClassNotFoundException e)
                    {
                        throw f;
                    }
                    catch (final NoSuchMethodException e)
                    {
                        throw f;
                    }
                    catch (final InvocationTargetException e)
                    {
                        throw f;
                    }
                    catch (final IllegalAccessException e)
                    {
                        throw f;
                    }
                    catch (final InstantiationException e)
                    {
                        throw f;
                    }
                }
                else
                {
                    throw f;
                }
            }
            else
            {
                throw f;
            }
        }
        finally
        {
            if (_messageContext.getTransportOut() != null)
            {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
    }

    @Override
    public SiteReconnectResponse siteReconnect(final SiteReconnect siteReconnect) throws RemoteException
    {
        MessageContext _messageContext = null;
        
        try
        {
            final OperationClient _operationClient = this._serviceClient.createClient(this._operations[1].getName());
            _operationClient.getOptions().setAction(
                    "http://remotelabs.eng.uts.edu.au/scheduling/MultiSiteFederation/NewOperation");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");
            
            _messageContext = new MessageContext();

            SOAPEnvelope env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), siteReconnect,
                    this.optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/",
                            "siteReconnect")), new QName(
                            "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/", "siteReconnect"));

            this._serviceClient.addHeadersToEnvelope(env);
            _messageContext.setEnvelope(env);
            _operationClient.addMessageContext(_messageContext);

            _operationClient.execute(true);

            final MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            
            final SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
            return (SiteReconnectResponse) this.fromOM(_returnEnv.getBody().getFirstElement(), SiteReconnectResponse.class,
                    this.getEnvelopeNamespaces(_returnEnv));
        }
        catch (final AxisFault f)
        {
            final OMElement faultElt = f.getDetail();
            if (faultElt != null)
            {
                if (this.faultExceptionNameMap.containsKey(new FaultMapKey(faultElt.getQName(), "siteReconnect")))
                {
                    try
                    {
                        final String exceptionClassName = (String) this.faultExceptionClassNameMap.get(new FaultMapKey(
                                faultElt.getQName(), "siteReconnect"));
                        final Class<?> exceptionClass = Class.forName(exceptionClassName);
                        final Exception ex = (Exception) exceptionClass.newInstance();

                        final String messageClassName = (String) this.faultMessageMap.get(new FaultMapKey(faultElt
                                .getQName(), "siteReconnect"));
                        final Class<?> messageClass = Class.forName(messageClassName);
                        final Object messageObject = this.fromOM(faultElt, messageClass, null);
                        final Method m = exceptionClass.getMethod("setFaultMessage", new Class[] { messageClass });
                        m.invoke(ex, new Object[] { messageObject });

                        throw new RemoteException(ex.getMessage(), ex);
                    }
                    catch (final ClassCastException e)
                    {
                        throw f;
                    }
                    catch (final ClassNotFoundException e)
                    {
                        throw f;
                    }
                    catch (final NoSuchMethodException e)
                    {
                        throw f;
                    }
                    catch (final InvocationTargetException e)
                    {
                        throw f;
                    }
                    catch (final IllegalAccessException e)
                    {
                        throw f;
                    }
                    catch (final InstantiationException e)
                    {
                        throw f;
                    }
                }
                else
                {
                    throw f;
                }
            }
            else
            {
                throw f;
            }
        }
        finally
        {
            if (_messageContext.getTransportOut() != null)
            {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
    }

    @Override
    public RequestResourceResponse requestResource(final RequestResource requestResource) throws RemoteException
    {
        MessageContext _messageContext = null;
        try
        {
            final OperationClient _operationClient = this._serviceClient.createClient(this._operations[2].getName());
            _operationClient.getOptions().setAction(
                    "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/requestResource");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            _messageContext = new MessageContext();

            SOAPEnvelope env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), requestResource,
                    this.optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/",
                            "requestResource")), new QName(
                            "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/", "requestResource"));
            this._serviceClient.addHeadersToEnvelope(env);
            _messageContext.setEnvelope(env);
            _operationClient.addMessageContext(_messageContext);
            _operationClient.execute(true);

            final MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            final SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            return (RequestResourceResponse) this.fromOM(_returnEnv.getBody().getFirstElement(), RequestResourceResponse.class,
                    this.getEnvelopeNamespaces(_returnEnv));
        }
        catch (final AxisFault f)
        {
            final OMElement faultElt = f.getDetail();
            if (faultElt != null)
            {
                if (this.faultExceptionNameMap.containsKey(new FaultMapKey(faultElt.getQName(), "requestResource")))
                {
                    try
                    {
                        final String exceptionClassName = (String) this.faultExceptionClassNameMap.get(new FaultMapKey(
                                faultElt.getQName(), "requestResource"));
                        final Class<?> exceptionClass = Class.forName(exceptionClassName);
                        final Exception ex = (Exception) exceptionClass.newInstance();

                        final String messageClassName = (String) this.faultMessageMap.get(new FaultMapKey(faultElt
                                .getQName(), "requestResource"));
                        final Class<?> messageClass = Class.forName(messageClassName);
                        final Object messageObject = this.fromOM(faultElt, messageClass, null);
                        final Method m = exceptionClass.getMethod("setFaultMessage", new Class[] { messageClass });
                        m.invoke(ex, new Object[] { messageObject });

                        throw new RemoteException(ex.getMessage(), ex);
                    }
                    catch (final ClassCastException e)
                    {
                        throw f;
                    }
                    catch (final ClassNotFoundException e)
                    {
                        throw f;
                    }
                    catch (final NoSuchMethodException e)
                    {
                        throw f;
                    }
                    catch (final InvocationTargetException e)
                    {
                        throw f;
                    }
                    catch (final IllegalAccessException e)
                    {
                        throw f;
                    }
                    catch (final InstantiationException e)
                    {
                        throw f;
                    }
                }
                else
                {
                    throw f;
                }
            }
            else
            {
                throw f;
            }
        }
        finally
        {
            if (_messageContext.getTransportOut() != null)
            {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
    }

    @Override
    public NotifyCancelResponse notifyCancel(final NotifyCancel notifyCancel) throws RemoteException
    {
        MessageContext _messageContext = null;
        try
        {
            final OperationClient _operationClient = this._serviceClient.createClient(this._operations[3].getName());
            _operationClient.getOptions().setAction(
                    "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/notifyCancel");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            _messageContext = new MessageContext();

            SOAPEnvelope env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), notifyCancel,
                    this.optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/",
                            "notifyCancel")), new QName(
                            "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/", "notifyCancel"));
            this._serviceClient.addHeadersToEnvelope(env);
            _messageContext.setEnvelope(env);
            _operationClient.addMessageContext(_messageContext);

            _operationClient.execute(true);

            final MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            final SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            return (NotifyCancelResponse) this.fromOM(_returnEnv.getBody().getFirstElement(), NotifyCancelResponse.class,
                    this.getEnvelopeNamespaces(_returnEnv));
        }
        catch (final AxisFault f)
        {
            final OMElement faultElt = f.getDetail();
            if (faultElt != null)
            {
                if (this.faultExceptionNameMap.containsKey(new FaultMapKey(faultElt.getQName(), "notifyCancel")))
                {
                    try
                    {
                        final String exceptionClassName = (String) this.faultExceptionClassNameMap.get(new FaultMapKey(
                                faultElt.getQName(), "notifyCancel"));
                        final Class<?> exceptionClass = Class.forName(exceptionClassName);
                        final Exception ex = (Exception) exceptionClass.newInstance();
                        final String messageClassName = (String) this.faultMessageMap.get(new FaultMapKey(faultElt
                                .getQName(), "notifyCancel"));
                        final Class<?> messageClass = Class.forName(messageClassName);
                        final Object messageObject = this.fromOM(faultElt, messageClass, null);
                        final Method m = exceptionClass.getMethod("setFaultMessage", new Class[] { messageClass });
                        m.invoke(ex, new Object[] { messageObject });

                        throw new RemoteException(ex.getMessage(), ex);
                    }
                    catch (final ClassCastException e)
                    {
                        throw f;
                    }
                    catch (final ClassNotFoundException e)
                    {
                        throw f;
                    }
                    catch (final NoSuchMethodException e)
                    {
                        throw f;
                    }
                    catch (final InvocationTargetException e)
                    {
                        throw f;
                    }
                    catch (final IllegalAccessException e)
                    {
                        throw f;
                    }
                    catch (final InstantiationException e)
                    {                     
                        throw f;
                    }
                }
                else
                {
                    throw f;
                }
            }
            else
            {
                throw f;
            }
        }
        finally
        {
            if (_messageContext.getTransportOut() != null)
            {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
    }

    @Override
    public NotifyAcceptResponse notifyAccept(final NotifyAccept notifyAccept) throws RemoteException
    {
        MessageContext _messageContext = null;
        try
        {
            final OperationClient _operationClient = this._serviceClient.createClient(this._operations[4].getName());
            _operationClient.getOptions().setAction(
                    "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/notifyAccept");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            _messageContext = new MessageContext();

            SOAPEnvelope env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), notifyAccept,
                    this.optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/",
                            "notifyAccept")), new QName(
                            "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/", "notifyAccept"));

            this._serviceClient.addHeadersToEnvelope(env);
            _messageContext.setEnvelope(env);
            _operationClient.addMessageContext(_messageContext);

            _operationClient.execute(true);

            final MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            final SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            return (NotifyAcceptResponse) this.fromOM(_returnEnv.getBody().getFirstElement(), NotifyAcceptResponse.class,
                    this.getEnvelopeNamespaces(_returnEnv));
        }
        catch (final AxisFault f)
        {
            final OMElement faultElt = f.getDetail();
            if (faultElt != null)
            {
                if (this.faultExceptionNameMap.containsKey(new FaultMapKey(faultElt.getQName(), "notifyAccept")))
                {
                    try
                    {
                        final String exceptionClassName = (String) this.faultExceptionClassNameMap.get(new FaultMapKey(
                                faultElt.getQName(), "notifyAccept"));
                        final Class<?> exceptionClass = Class.forName(exceptionClassName);
                        final Exception ex = (Exception) exceptionClass.newInstance();
                 
                        final String messageClassName = (String) this.faultMessageMap.get(new FaultMapKey(faultElt
                                .getQName(), "notifyAccept"));
                        final Class<?> messageClass = Class.forName(messageClassName);
                        final Object messageObject = this.fromOM(faultElt, messageClass, null);
                        final Method m = exceptionClass.getMethod("setFaultMessage", new Class[] { messageClass });
                        m.invoke(ex, new Object[] { messageObject });

                        throw new RemoteException(ex.getMessage(), ex);
                    }
                    catch (final ClassCastException e)
                    {
                        throw f;
                    }
                    catch (final ClassNotFoundException e)
                    {
                        throw f;
                    }
                    catch (final NoSuchMethodException e)
                    {
                        throw f;
                    }
                    catch (final InvocationTargetException e)
                    {
                        throw f;
                    }
                    catch (final IllegalAccessException e)
                    {
                        throw f;
                    }
                    catch (final InstantiationException e)
                    {
                        throw f;
                    }
                }
                else
                {
                    throw f;
                }
            }
            else
            {
                throw f;
            }
        }
        finally
        {
            if (_messageContext.getTransportOut() != null)
            {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
    }

    @Override
    public NotifyModifyResponse notifyModify(final NotifyModify notifyModify) throws RemoteException
    {
        MessageContext _messageContext = null;
        try
        {
            final OperationClient _operationClient = this._serviceClient.createClient(this._operations[5].getName());
            _operationClient.getOptions().setAction(
                    "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/notifyModify");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            _messageContext = new MessageContext();

            SOAPEnvelope env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), notifyModify,
                    this.optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/",
                            "notifyModify")), new QName(
                            "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/", "notifyModify"));

            this._serviceClient.addHeadersToEnvelope(env);
            _messageContext.setEnvelope(env);
            _operationClient.addMessageContext(_messageContext);
            _operationClient.execute(true);

            final MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            final SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            return (NotifyModifyResponse) this.fromOM(_returnEnv.getBody().getFirstElement(), NotifyModifyResponse.class,
                    this.getEnvelopeNamespaces(_returnEnv));
        }
        catch (final AxisFault f)
        {
            final OMElement faultElt = f.getDetail();
            if (faultElt != null)
            {
                if (this.faultExceptionNameMap.containsKey(new FaultMapKey(faultElt.getQName(), "notifyModify")))
                {
                    try
                    {
                        final String exceptionClassName = (String) this.faultExceptionClassNameMap.get(new FaultMapKey(
                                faultElt.getQName(), "notifyModify"));
                        final Class<?> exceptionClass = Class.forName(exceptionClassName);
                        final Exception ex = (Exception) exceptionClass.newInstance();

                        final String messageClassName = (String) this.faultMessageMap.get(new FaultMapKey(faultElt
                                .getQName(), "notifyModify"));
                        final Class<?> messageClass = Class.forName(messageClassName);
                        final Object messageObject = this.fromOM(faultElt, messageClass, null);
                        final Method m = exceptionClass.getMethod("setFaultMessage", new Class[] { messageClass });
                        m.invoke(ex, new Object[] { messageObject });

                        throw new RemoteException(ex.getMessage(), ex);
                    }
                    catch (final ClassCastException e)
                    {
                        throw f;
                    }
                    catch (final ClassNotFoundException e)
                    {
                        throw f;
                    }
                    catch (final NoSuchMethodException e)
                    {
                        throw f;
                    }
                    catch (final InvocationTargetException e)
                    {
                        throw f;
                    }
                    catch (final IllegalAccessException e)
                    {
                        throw f;
                    }
                    catch (final InstantiationException e)
                    {
                        throw f;
                    }
                }
                else
                {
                    throw f;
                }
            }
            else
            {
                throw f;
            }
        }
        finally
        {
            if (_messageContext.getTransportOut() != null)
            {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
    }

    @Override
    public InitiateSiteResponse initiateSite(final InitiateSite initiateSite) throws RemoteException
    {
        MessageContext _messageContext = null;
        try
        {
            final OperationClient _operationClient = this._serviceClient.createClient(this._operations[6].getName());
            _operationClient.getOptions().setAction(
                    "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/initiateSite");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            _messageContext = new MessageContext();

            SOAPEnvelope env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), initiateSite,
                    this.optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/",
                            "initiateSite")), new QName(
                            "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/", "initiateSite"));

            this._serviceClient.addHeadersToEnvelope(env);
            _messageContext.setEnvelope(env);
            _operationClient.addMessageContext(_messageContext);
            _operationClient.execute(true);

            final MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            final SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            return (InitiateSiteResponse) this.fromOM(_returnEnv.getBody().getFirstElement(), InitiateSiteResponse.class,
                    this.getEnvelopeNamespaces(_returnEnv));
        }
        catch (final AxisFault f)
        {
            final OMElement faultElt = f.getDetail();
            if (faultElt != null)
            {
                if (this.faultExceptionNameMap.containsKey(new FaultMapKey(faultElt.getQName(), "initiateSite")))
                {
                    try
                    {
                        final String exceptionClassName = (String) this.faultExceptionClassNameMap.get(new FaultMapKey(
                                faultElt.getQName(), "initiateSite"));
                        final Class<?> exceptionClass = Class.forName(exceptionClassName);
                        final Exception ex = (Exception) exceptionClass.newInstance();

                        final String messageClassName = (String) this.faultMessageMap.get(new FaultMapKey(faultElt
                                .getQName(), "initiateSite"));
                        final Class<?> messageClass = Class.forName(messageClassName);
                        final Object messageObject = this.fromOM(faultElt, messageClass, null);
                        final Method m = exceptionClass.getMethod("setFaultMessage", new Class[] { messageClass });
                        m.invoke(ex, new Object[] { messageObject });

                        throw new RemoteException(ex.getMessage(), ex);
                    }
                    catch (final ClassCastException e)
                    {
                        throw f;
                    }
                    catch (final ClassNotFoundException e)
                    {
                        throw f;
                    }
                    catch (final NoSuchMethodException e)
                    {
                        throw f;
                    }
                    catch (final InvocationTargetException e)
                    {
                        throw f;
                    }
                    catch (final IllegalAccessException e)
                    {
                        throw f;
                    }
                    catch (final InstantiationException e)
                    {
                        throw f;
                    }
                }
                else
                {
                    throw f;
                }
            }
            else
            {
                throw f;
            }
        }
        finally
        {
            if (_messageContext.getTransportOut() != null)
            {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
    }


    @Override
    public GetRequestsResponse getRequests(final GetRequests getRequests) throws RemoteException
    {
        MessageContext _messageContext = null;
        try
        {
            final OperationClient _operationClient = this._serviceClient.createClient(this._operations[7].getName());
            _operationClient.getOptions().setAction(
                    "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/getRequests");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            _messageContext = new MessageContext();
            
            SOAPEnvelope env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), getRequests,
                    this.optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/",
                            "getRequests")), new QName(
                            "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/", "getRequests"));

            this._serviceClient.addHeadersToEnvelope(env);
            _messageContext.setEnvelope(env);
            _operationClient.addMessageContext(_messageContext);
            _operationClient.execute(true);

            final MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            final SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            return (GetRequestsResponse) this.fromOM(_returnEnv.getBody().getFirstElement(), GetRequestsResponse.class,
                    this.getEnvelopeNamespaces(_returnEnv));
        }
        catch (final AxisFault f)
        {
            final OMElement faultElt = f.getDetail();
            if (faultElt != null)
            {
                if (this.faultExceptionNameMap.containsKey(new FaultMapKey(faultElt.getQName(), "getRequests")))
                {
                    try
                    {
                        final String exceptionClassName = (String) this.faultExceptionClassNameMap.get(new FaultMapKey(
                                faultElt.getQName(), "getRequests"));
                        final Class<?> exceptionClass = Class.forName(exceptionClassName);
                        final Exception ex = (Exception) exceptionClass.newInstance();
                        final String messageClassName = (String) this.faultMessageMap.get(new FaultMapKey(faultElt
                                .getQName(), "getRequests"));
                        final Class<?> messageClass = Class.forName(messageClassName);
                        final Object messageObject = this.fromOM(faultElt, messageClass, null);
                        final Method m = exceptionClass.getMethod("setFaultMessage", new Class[] { messageClass });
                        m.invoke(ex, new Object[] { messageObject });

                        throw new RemoteException(ex.getMessage(), ex);
                    }
                    catch (final ClassCastException e)
                    {
                        throw f;
                    }
                    catch (final ClassNotFoundException e)
                    {
                        throw f;
                    }
                    catch (final NoSuchMethodException e)
                    {
                        throw f;
                    }
                    catch (final InvocationTargetException e)
                    {
                        throw f;
                    }
                    catch (final IllegalAccessException e)
                    {
                        throw f;
                    }
                    catch (final InstantiationException e)
                    {
                        throw f;
                    }
                }
                else
                {
                    throw f;
                }
            }
            else
            {
                throw f;
            }
        }
        finally
        {
            if (_messageContext.getTransportOut() != null)
            {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
    }

    @Override
    public SiteStatusResponse siteStatus(final SiteStatus siteStatus) throws RemoteException
    {
        MessageContext _messageContext = null;
        try
        {
            final OperationClient _operationClient = this._serviceClient.createClient(this._operations[8].getName());
            _operationClient.getOptions().setAction(
                    "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/siteStatus");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            _messageContext = new MessageContext();

            SOAPEnvelope env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), siteStatus,
                    this.optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/",
                            "siteStatus")), new QName(
                            "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/", "siteStatus"));

            this._serviceClient.addHeadersToEnvelope(env);
            _messageContext.setEnvelope(env);
            _operationClient.addMessageContext(_messageContext);
            _operationClient.execute(true);

            final MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            final SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            return (SiteStatusResponse) this.fromOM(_returnEnv.getBody().getFirstElement(), SiteStatusResponse.class,
                    this.getEnvelopeNamespaces(_returnEnv));
        }
        catch (final AxisFault f)
        {
            final OMElement faultElt = f.getDetail();
            if (faultElt != null)
            {
                if (this.faultExceptionNameMap.containsKey(new FaultMapKey(faultElt.getQName(), "siteStatus")))
                {
                    try
                    {
                        final String exceptionClassName = (String) this.faultExceptionClassNameMap.get(new FaultMapKey(
                                faultElt.getQName(), "siteStatus"));
                        final Class<?> exceptionClass = Class.forName(exceptionClassName);
                        final Exception ex = (Exception) exceptionClass.newInstance();

                        final String messageClassName = (String) this.faultMessageMap.get(new FaultMapKey(faultElt
                                .getQName(), "siteStatus"));
                        final Class<?> messageClass = Class.forName(messageClassName);
                        final Object messageObject = this.fromOM(faultElt, messageClass, null);
                        final Method m = exceptionClass.getMethod("setFaultMessage", new Class[] { messageClass });
                        m.invoke(ex, new Object[] { messageObject });

                        throw new RemoteException(ex.getMessage(), ex);
                    }
                    catch (final ClassCastException e)
                    {
                        throw f;
                    }
                    catch (final ClassNotFoundException e)
                    {
                        throw f;
                    }
                    catch (final NoSuchMethodException e)
                    {
                        throw f;
                    }
                    catch (final InvocationTargetException e)
                    {
                        throw f;
                    }
                    catch (final IllegalAccessException e)
                    {
                        throw f;
                    }
                    catch (final InstantiationException e)
                    {
                        throw f;
                    }
                }
                else
                {
                    throw f;
                }
            }
            else
            {
                throw f;
            }
        }
        finally
        {
            if (_messageContext.getTransportOut() != null)
            {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
    }

    @Override
    public DiscoverResourcesResponse discoverResources(final DiscoverResources discoverResources) throws RemoteException
    {
        MessageContext _messageContext = null;
        try
        {
            final OperationClient _operationClient = this._serviceClient.createClient(this._operations[9].getName());
            _operationClient.getOptions().setAction(
                    "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/discoverResources");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            _messageContext = new MessageContext();

            SOAPEnvelope env = this
                    .toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()),
                            discoverResources, this.optimizeContent(new QName(
                                    "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/",
                                    "discoverResources")), new QName(
                                    "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/",
                                    "discoverResources"));

            this._serviceClient.addHeadersToEnvelope(env);
            _messageContext.setEnvelope(env);
            _operationClient.addMessageContext(_messageContext);
            _operationClient.execute(true);

            final MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            final SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            return (DiscoverResourcesResponse) this.fromOM(_returnEnv.getBody().getFirstElement(), DiscoverResourcesResponse.class,
                    this.getEnvelopeNamespaces(_returnEnv));
        }
        catch (final AxisFault f)
        {
            final OMElement faultElt = f.getDetail();
            if (faultElt != null)
            {
                if (this.faultExceptionNameMap.containsKey(new FaultMapKey(faultElt.getQName(), "discoverResources")))
                {
                    try
                    {
                        final String exceptionClassName = (String) this.faultExceptionClassNameMap.get(new FaultMapKey(
                                faultElt.getQName(), "discoverResources"));
                        final Class<?> exceptionClass = Class.forName(exceptionClassName);
                        final Exception ex = (Exception) exceptionClass.newInstance();

                        final String messageClassName = (String) this.faultMessageMap.get(new FaultMapKey(faultElt
                                .getQName(), "discoverResources"));
                        final Class<?> messageClass = Class.forName(messageClassName);
                        final Object messageObject = this.fromOM(faultElt, messageClass, null);
                        final Method m = exceptionClass.getMethod("setFaultMessage", new Class[] { messageClass });
                        m.invoke(ex, new Object[] { messageObject });

                        throw new RemoteException(ex.getMessage(), ex);
                    }
                    catch (final ClassCastException e)
                    {
                        throw f;
                    }
                    catch (final ClassNotFoundException e)
                    {
                        throw f;
                    }
                    catch (final NoSuchMethodException e)
                    {
                        throw f;
                    }
                    catch (final InvocationTargetException e)
                    {
                        throw f;
                    }
                    catch (final IllegalAccessException e)
                    {
                        throw f;
                    }
                    catch (final InstantiationException e)
                    {
                        throw f;
                    }
                }
                else
                {
                    throw f;
                }
            }
            else
            {
                throw f;
            }
        }
        finally
        {
            if (_messageContext.getTransportOut() != null)
            {
                _messageContext.getTransportOut().getSender().cleanup(_messageContext);
            }
        }
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

    private final QName[] opNameArray = null;
    private boolean optimizeContent(final QName opName)
    {
        if (this.opNameArray == null)
        {
            return false;
        }
        for (final QName element : this.opNameArray)
        {
            if (opName.equals(element))
            {
                return true;
            }
        }
        return false;
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final SiteShutdown param, final boolean optimizeContent,
            final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(SiteShutdown.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }

    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final SiteReconnect param,
            final boolean optimizeContent, final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(SiteReconnect.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }

    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final RequestResource param,
            final boolean optimizeContent, final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(RequestResource.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final NotifyCancel param, final boolean optimizeContent,
            final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(NotifyCancel.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final NotifyAccept param, final boolean optimizeContent,
            final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(NotifyAccept.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final NotifyModify param, final boolean optimizeContent,
            final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(NotifyModify.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final InitiateSite param, final boolean optimizeContent,
            final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(InitiateSite.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final GetRequests param, final boolean optimizeContent,
            final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(GetRequests.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final SiteStatus param, final boolean optimizeContent,
            final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(SiteStatus.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }

    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final DiscoverResources param,
            final boolean optimizeContent, final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(DiscoverResources.MY_QNAME, factory));
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
}
