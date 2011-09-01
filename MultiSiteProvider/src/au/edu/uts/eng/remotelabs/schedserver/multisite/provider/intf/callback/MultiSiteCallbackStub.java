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

package au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.callback;

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
import org.apache.axis2.client.async.AxisCallback;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.databinding.ADBException;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.OutInAxisOperation;
import org.apache.axis2.description.WSDL2Constants;
import org.apache.axis2.util.CallbackReceiver;
import org.apache.axis2.util.Utils;
import org.apache.axis2.wsdl.WSDLConstants;

import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.callback.types.BookingCancelled;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.callback.types.BookingCancelledResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.callback.types.SessionFinished;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.callback.types.SessionFinishedResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.callback.types.SessionStarted;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.callback.types.SessionStartedResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.callback.types.SessionUpdate;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.callback.types.SessionUpdateResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.OperationResponseType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.requests.callback.MultiSiteCallbackHandler;

/**
 * MultiSite callback client.
 */
public class MultiSiteCallbackStub extends Stub implements MultiSiteCallback
{
    protected AxisOperation[] _operations;

    private final HashMap<?, ?> faultExceptionNameMap = new HashMap<Object, Object>();
    private final HashMap<?, ?> faultExceptionClassNameMap = new HashMap<Object, Object>();
    private final HashMap<?, ?> faultMessageMap = new HashMap<Object, Object>();

    private static int counter = 0;

    private static synchronized String getUniqueSuffix()
    {
        if (MultiSiteCallbackStub.counter > 99999)
        {
            MultiSiteCallbackStub.counter = 0;
        }
        MultiSiteCallbackStub.counter = MultiSiteCallbackStub.counter + 1;
        
        return Long.toString(System.currentTimeMillis()) + "_" + MultiSiteCallbackStub.counter;
    }

    private void populateAxisService() throws AxisFault
    {
        this._service = new AxisService("MultiSiteCallback" + MultiSiteCallbackStub.getUniqueSuffix());
        this.addAnonymousOperations();


        AxisOperation operation;
        this._operations = new AxisOperation[4];
        operation = new OutInAxisOperation();
        operation.setName(new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteCallback/",
                "bookingCancelled"));
        this._service.addOperation(operation);
        this._operations[0] = operation;

        operation = new OutInAxisOperation();
        operation.setName(new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteCallback/",
                "sessionStarted"));
        this._service.addOperation(operation);
        this._operations[1] = operation;

        operation = new OutInAxisOperation();
        operation.setName(new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteCallback/",
                "sessionFinished"));
        this._service.addOperation(operation);
        this._operations[2] = operation;

        operation = new OutInAxisOperation();
        operation.setName(new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteCallback/",
                "sessionUpdate"));
        this._service.addOperation(operation);
        this._operations[3] = operation;
    }

    public MultiSiteCallbackStub(final ConfigurationContext configurationContext, final String targetEndpoint)
            throws AxisFault
    {
        this(configurationContext, targetEndpoint, false);
    }

    public MultiSiteCallbackStub(final ConfigurationContext configurationContext, final String targetEndpoint,
            final boolean useSeparateListener) throws AxisFault
    {
        this.populateAxisService();

        this._serviceClient = new ServiceClient(configurationContext, this._service);
        this._serviceClient.getOptions().setTo(new EndpointReference(targetEndpoint));
        this._serviceClient.getOptions().setUseSeparateListener(useSeparateListener);
    }

    public MultiSiteCallbackStub(final ConfigurationContext configurationContext) throws AxisFault
    {
        this(configurationContext,
                "http://remotelabs.eng.uts.edu.au:8080/SchedulingServer-MultiSite/services/MultiSiteCallback");

    }

    public MultiSiteCallbackStub() throws AxisFault
    {
        this("http://remotelabs.eng.uts.edu.au:8080/SchedulingServer-MultiSite/services/MultiSiteCallback");
    }

    public MultiSiteCallbackStub(final String targetEndpoint) throws AxisFault
    {
        this(null, targetEndpoint);
    }

    @Override
    public BookingCancelledResponse bookingCancelled(final BookingCancelled request) throws RemoteException
    {
        MessageContext _messageContext = null;
        try
        {
            final OperationClient _operationClient = this._serviceClient.createClient(this._operations[0].getName());
            _operationClient.getOptions().setAction(
                    "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteCallback/bookingCancelled");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            _messageContext = new MessageContext();
            
            SOAPEnvelope env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()),
                    request, this.optimizeContent(new QName(
                            "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteCallback/", "bookingCancelled")),
                    new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteCallback/", "bookingCancelled"));

            this._serviceClient.addHeadersToEnvelope(env);
            _messageContext.setEnvelope(env);

            _operationClient.addMessageContext(_messageContext);

            _operationClient.execute(true);

            final MessageContext _returnMessageContext = _operationClient.getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            final SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            final Object object = this.fromOM(_returnEnv.getBody().getFirstElement(), BookingCancelledResponse.class,
                    this.getEnvelopeNamespaces(_returnEnv));

            return (BookingCancelledResponse) object;
        }
        catch (final AxisFault f)
        {
            final OMElement faultElt = f.getDetail();
            if (faultElt != null)
            {
                if (this.faultExceptionNameMap.containsKey(new FaultMapKey(faultElt.getQName(), "bookingCancelled")))
                {
                    try
                    {
                        final String exceptionClassName = (String) this.faultExceptionClassNameMap.get(new FaultMapKey(
                                faultElt.getQName(), "bookingCancelled"));
                        final Class<?> exceptionClass = Class.forName(exceptionClassName);
                        final Exception ex = (Exception) exceptionClass.newInstance();
                 
                        final String messageClassName = (String) this.faultMessageMap.get(new FaultMapKey(faultElt
                                .getQName(), "bookingCancelled"));
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
    public void startbookingCancelled(final BookingCancelled request, final MultiSiteCallbackHandler callback)
            throws RemoteException
    {
        final OperationClient _operationClient = this._serviceClient.createClient(this._operations[0].getName());
        _operationClient.getOptions().setAction(
                "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteCallback/bookingCancelled");
        _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

        this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

        SOAPEnvelope env = null;
        final MessageContext _messageContext = new MessageContext();

        env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), request,
                this.optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteCallback/",
                        "bookingCancelled")), new QName(
                        "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteCallback/", "bookingCancelled"));

        this._serviceClient.addHeadersToEnvelope(env);
        
        _messageContext.setEnvelope(env);

        _operationClient.addMessageContext(_messageContext);
        _operationClient.setCallback(new AxisCallback()
        {
            @Override
            public void onMessage(final MessageContext resultContext)
            {
                try
                {
                    final SOAPEnvelope resultEnv = resultContext.getEnvelope();
                    final Object object = MultiSiteCallbackStub.this.fromOM(resultEnv.getBody().getFirstElement(),
                            BookingCancelledResponse.class, MultiSiteCallbackStub.this.getEnvelopeNamespaces(resultEnv));
                    
                    OperationResponseType response = ((BookingCancelledResponse) object).getBookingCancelledResponse();
                    callback.receiveResultBookingCancelled(response.getWasSuccessful(), response.getReason());
                }
                catch (final AxisFault e)
                {
                    callback.receiveErrorBookingCancelled(e);
                }
            }

            @Override
            public void onError(final Exception error)
            {
                if (error instanceof AxisFault)
                {
                    final AxisFault f = (AxisFault) error;
                    final OMElement faultElt = f.getDetail();
                    if (faultElt != null)
                    {
                        if (MultiSiteCallbackStub.this.faultExceptionNameMap.containsKey(new FaultMapKey(faultElt
                                .getQName(), "bookingCancelled")))
                        {
                            try
                            {
                                final String exceptionClassName = (String) MultiSiteCallbackStub.this.faultExceptionClassNameMap
                                        .get(new FaultMapKey(faultElt.getQName(), "bookingCancelled"));
                                final Class<?> exceptionClass = Class.forName(exceptionClassName);
                                final Exception ex = (Exception) exceptionClass.newInstance();

                                final String messageClassName = (String) MultiSiteCallbackStub.this.faultMessageMap
                                        .get(new FaultMapKey(faultElt.getQName(), "bookingCancelled"));
                                final Class<?> messageClass = Class.forName(messageClassName);
                                final Object messageObject = MultiSiteCallbackStub.this.fromOM(faultElt, messageClass,
                                        null);
                                final Method m = exceptionClass.getMethod("setFaultMessage",
                                        new Class[] { messageClass });
                                m.invoke(ex, new Object[] { messageObject });

                                callback.receiveErrorBookingCancelled(new RemoteException(ex.getMessage(), ex));
                            }
                            catch (final ClassCastException e)
                            {
                                callback.receiveErrorBookingCancelled(f);
                            }
                            catch (final ClassNotFoundException e)
                            {
                                callback.receiveErrorBookingCancelled(f);
                            }
                            catch (final NoSuchMethodException e)
                            {
                                callback.receiveErrorBookingCancelled(f);
                            }
                            catch (final InvocationTargetException e)
                            {
                                callback.receiveErrorBookingCancelled(f);
                            }
                            catch (final IllegalAccessException e)
                            {
                                callback.receiveErrorBookingCancelled(f);
                            }
                            catch (final InstantiationException e)
                            {
                                callback.receiveErrorBookingCancelled(f);
                            }
                            catch (final AxisFault e)
                            {
                                callback.receiveErrorBookingCancelled(f);
                            }
                        }
                        else
                        {
                            callback.receiveErrorBookingCancelled(f);
                        }
                    }
                    else
                    {
                        callback.receiveErrorBookingCancelled(f);
                    }
                }
                else
                {
                    callback.receiveErrorBookingCancelled(error);
                }
            }

            @Override
            public void onFault(final MessageContext faultContext)
            {
                final AxisFault fault = Utils.getInboundFaultFromMessageContext(faultContext);
                this.onError(fault);
            }

            @Override
            public void onComplete()
            {
                try
                {
                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                }
                catch (final AxisFault axisFault)
                {
                    callback.receiveErrorBookingCancelled(axisFault);
                }
            }
        });

        CallbackReceiver _callbackReceiver = null;
        if (this._operations[0].getMessageReceiver() == null && _operationClient.getOptions().isUseSeparateListener())
        {
            _callbackReceiver = new CallbackReceiver();
            this._operations[0].setMessageReceiver(_callbackReceiver);
        }

        _operationClient.execute(false);
    }

    @Override
    public SessionStartedResponse sessionStarted(final SessionStarted request) throws RemoteException
    {
        MessageContext _messageContext = null;
        try
        {
            final OperationClient _operationClient = this._serviceClient.createClient(this._operations[1].getName());
            _operationClient.getOptions().setAction(
                    "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteCallback/sessionStarted");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            _messageContext = new MessageContext();

            SOAPEnvelope env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), request,
                    this.optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteCallback/",
                            "sessionStarted")), new QName(
                            "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteCallback/", "sessionStarted"));

            this._serviceClient.addHeadersToEnvelope(env);
            _messageContext.setEnvelope(env);

            _operationClient.addMessageContext(_messageContext);
            _operationClient.execute(true);

            final MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            final SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            final Object object = this.fromOM(_returnEnv.getBody().getFirstElement(), SessionStartedResponse.class,
                    this.getEnvelopeNamespaces(_returnEnv));

            return (SessionStartedResponse) object;
        }
        catch (final AxisFault f)
        {
            final OMElement faultElt = f.getDetail();
            if (faultElt != null)
            {
                if (this.faultExceptionNameMap.containsKey(new FaultMapKey(faultElt.getQName(), "sessionStarted")))
                {
                    try
                    {
                        final String exceptionClassName = (String) this.faultExceptionClassNameMap.get(new FaultMapKey(
                                faultElt.getQName(), "sessionStarted"));
                        final Class<?> exceptionClass = Class.forName(exceptionClassName);
                        final Exception ex = (Exception) exceptionClass.newInstance();
                        final String messageClassName = (String) this.faultMessageMap.get(new FaultMapKey(faultElt
                                .getQName(), "sessionStarted"));
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
    public void startsessionStarted(final SessionStarted request, final MultiSiteCallbackHandler callback)
            throws RemoteException
    {
        final OperationClient _operationClient = this._serviceClient.createClient(this._operations[1].getName());
        _operationClient.getOptions().setAction(
                "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteCallback/sessionStarted");
        _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

        this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");


        final MessageContext _messageContext = new MessageContext();

        SOAPEnvelope env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), request,
                this.optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteCallback/",
                        "sessionStarted")), new QName(
                        "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteCallback/", "sessionStarted"));

        this._serviceClient.addHeadersToEnvelope(env);
        _messageContext.setEnvelope(env);

        _operationClient.addMessageContext(_messageContext);
        _operationClient.setCallback(new AxisCallback()
        {
            @Override
            public void onMessage(final MessageContext resultContext)
            {
                try
                {
                    final SOAPEnvelope resultEnv = resultContext.getEnvelope();

                    final Object object = MultiSiteCallbackStub.this.fromOM(resultEnv.getBody().getFirstElement(),
                            SessionStartedResponse.class, MultiSiteCallbackStub.this.getEnvelopeNamespaces(resultEnv));
                    callback.receiveResultsessionStarted((SessionStartedResponse) object);

                }
                catch (final AxisFault e)
                {
                    callback.receiveErrorsessionStarted(e);
                }
            }

            @Override
            public void onError(final Exception error)
            {
                if (error instanceof AxisFault)
                {
                    final AxisFault f = (AxisFault) error;
                    final OMElement faultElt = f.getDetail();
                    if (faultElt != null)
                    {
                        if (MultiSiteCallbackStub.this.faultExceptionNameMap.containsKey(new FaultMapKey(faultElt
                                .getQName(), "sessionStarted")))
                        {
                            try
                            {
                                final String exceptionClassName = (String) MultiSiteCallbackStub.this.faultExceptionClassNameMap
                                        .get(new FaultMapKey(faultElt.getQName(), "sessionStarted"));
                                final Class<?> exceptionClass = Class.forName(exceptionClassName);
                                final Exception ex = (Exception) exceptionClass.newInstance();

                                final String messageClassName = (String) MultiSiteCallbackStub.this.faultMessageMap
                                        .get(new FaultMapKey(faultElt.getQName(), "sessionStarted"));
                                final Class<?> messageClass = Class.forName(messageClassName);
                                final Object messageObject = MultiSiteCallbackStub.this.fromOM(faultElt, messageClass,
                                        null);
                                final Method m = exceptionClass.getMethod("setFaultMessage",
                                        new Class[] { messageClass });
                                m.invoke(ex, new Object[] { messageObject });

                                callback.receiveErrorsessionStarted(new RemoteException(ex.getMessage(), ex));
                            }
                            catch (final ClassCastException e)
                            {
                                callback.receiveErrorsessionStarted(f);
                            }
                            catch (final ClassNotFoundException e)
                            {
                                callback.receiveErrorsessionStarted(f);
                            }
                            catch (final NoSuchMethodException e)
                            {
                                callback.receiveErrorsessionStarted(f);
                            }
                            catch (final InvocationTargetException e)
                            {
                                callback.receiveErrorsessionStarted(f);
                            }
                            catch (final IllegalAccessException e)
                            {
                                callback.receiveErrorsessionStarted(f);
                            }
                            catch (final InstantiationException e)
                            {
                                callback.receiveErrorsessionStarted(f);
                            }
                            catch (final AxisFault e)
                            {
                                callback.receiveErrorsessionStarted(f);
                            }
                        }
                        else
                        {
                            callback.receiveErrorsessionStarted(f);
                        }
                    }
                    else
                    {
                        callback.receiveErrorsessionStarted(f);
                    }
                }
                else
                {
                    callback.receiveErrorsessionStarted(error);
                }
            }

            @Override
            public void onFault(final MessageContext faultContext)
            {
                final AxisFault fault = Utils.getInboundFaultFromMessageContext(faultContext);
                this.onError(fault);
            }

            @Override
            public void onComplete()
            {
                try
                {
                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                }
                catch (final AxisFault axisFault)
                {
                    callback.receiveErrorsessionStarted(axisFault);
                }
            }
        });

        CallbackReceiver _callbackReceiver = null;
        if (this._operations[1].getMessageReceiver() == null && _operationClient.getOptions().isUseSeparateListener())
        {
            _callbackReceiver = new CallbackReceiver();
            this._operations[1].setMessageReceiver(_callbackReceiver);
        }

        _operationClient.execute(false);

    }

    @Override
    public SessionFinishedResponse sessionFinished(final SessionFinished request) throws RemoteException
    {
        MessageContext _messageContext = null;
        try
        {
            final OperationClient _operationClient = this._serviceClient.createClient(this._operations[2].getName());
            _operationClient.getOptions().setAction(
                    "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteCallback/sessionFinished");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            _messageContext = new MessageContext();

            SOAPEnvelope env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()),
                    request, this.optimizeContent(new QName(
                            "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteCallback/", "sessionFinished")),
                    new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteCallback/", "sessionFinished"));

            this._serviceClient.addHeadersToEnvelope(env);

            _messageContext.setEnvelope(env);
            _operationClient.addMessageContext(_messageContext);

            _operationClient.execute(true);

            final MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            final SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            final Object object = this.fromOM(_returnEnv.getBody().getFirstElement(), SessionFinishedResponse.class,
                    this.getEnvelopeNamespaces(_returnEnv));

            return (SessionFinishedResponse) object;
        }
        catch (final AxisFault f)
        {
            final OMElement faultElt = f.getDetail();
            if (faultElt != null)
            {
                if (this.faultExceptionNameMap.containsKey(new FaultMapKey(faultElt.getQName(), "sessionFinished")))
                {
                    try
                    {
                        final String exceptionClassName = (String) this.faultExceptionClassNameMap.get(new FaultMapKey(
                                faultElt.getQName(), "sessionFinished"));
                        final Class<?> exceptionClass = Class.forName(exceptionClassName);
                        final Exception ex = (Exception) exceptionClass.newInstance();
                 
                        final String messageClassName = (String) this.faultMessageMap.get(new FaultMapKey(faultElt
                                .getQName(), "sessionFinished"));
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
    public void startsessionFinished(final SessionFinished request, final MultiSiteCallbackHandler callback)
            throws RemoteException
    {
        final OperationClient _operationClient = this._serviceClient.createClient(this._operations[2].getName());
        _operationClient.getOptions().setAction(
                "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteCallback/sessionFinished");
        _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

        this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

        final MessageContext _messageContext = new MessageContext();
        SOAPEnvelope env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), request,
                this.optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteCallback/",
                        "sessionFinished")), new QName(
                        "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteCallback/", "sessionFinished"));

        this._serviceClient.addHeadersToEnvelope(env);

        _messageContext.setEnvelope(env);

        _operationClient.addMessageContext(_messageContext);
        _operationClient.setCallback(new AxisCallback()
        {
            @Override
            public void onMessage(final MessageContext resultContext)
            {
                try
                {
                    final SOAPEnvelope resultEnv = resultContext.getEnvelope();

                    final Object object = MultiSiteCallbackStub.this.fromOM(resultEnv.getBody().getFirstElement(),
                            SessionFinishedResponse.class, MultiSiteCallbackStub.this.getEnvelopeNamespaces(resultEnv));
                    callback.receiveResultsessionFinished((SessionFinishedResponse) object);

                }
                catch (final AxisFault e)
                {
                    callback.receiveErrorsessionFinished(e);
                }
            }

            @Override
            public void onError(final Exception error)
            {
                if (error instanceof AxisFault)
                {
                    final AxisFault f = (AxisFault) error;
                    final OMElement faultElt = f.getDetail();
                    if (faultElt != null)
                    {
                        if (MultiSiteCallbackStub.this.faultExceptionNameMap.containsKey(new FaultMapKey(faultElt
                                .getQName(), "sessionFinished")))
                        {
                            try
                            {
                                final String exceptionClassName = (String) MultiSiteCallbackStub.this.faultExceptionClassNameMap
                                        .get(new FaultMapKey(faultElt.getQName(), "sessionFinished"));
                                final Class<?> exceptionClass = Class.forName(exceptionClassName);
                                final Exception ex = (Exception) exceptionClass.newInstance();

                                final String messageClassName = (String) MultiSiteCallbackStub.this.faultMessageMap
                                        .get(new FaultMapKey(faultElt.getQName(), "sessionFinished"));
                                final Class<?> messageClass = Class.forName(messageClassName);
                                final Object messageObject = MultiSiteCallbackStub.this.fromOM(faultElt, messageClass,
                                        null);
                                final Method m = exceptionClass.getMethod("setFaultMessage",
                                        new Class[] { messageClass });
                                m.invoke(ex, new Object[] { messageObject });

                                callback.receiveErrorsessionFinished(new RemoteException(ex.getMessage(), ex));
                            }
                            catch (final ClassCastException e)
                            {
                                callback.receiveErrorsessionFinished(f);
                            }
                            catch (final ClassNotFoundException e)
                            {
                                callback.receiveErrorsessionFinished(f);
                            }
                            catch (final NoSuchMethodException e)
                            {
                                callback.receiveErrorsessionFinished(f);
                            }
                            catch (final InvocationTargetException e)
                            {
                                callback.receiveErrorsessionFinished(f);
                            }
                            catch (final IllegalAccessException e)
                            {
                                callback.receiveErrorsessionFinished(f);
                            }
                            catch (final InstantiationException e)
                            {
                                callback.receiveErrorsessionFinished(f);
                            }
                            catch (final AxisFault e)
                            {
                                callback.receiveErrorsessionFinished(f);
                            }
                        }
                        else
                        {
                            callback.receiveErrorsessionFinished(f);
                        }
                    }
                    else
                    {
                        callback.receiveErrorsessionFinished(f);
                    }
                }
                else
                {
                    callback.receiveErrorsessionFinished(error);
                }
            }

            @Override
            public void onFault(final MessageContext faultContext)
            {
                final AxisFault fault = Utils.getInboundFaultFromMessageContext(faultContext);
                this.onError(fault);
            }

            @Override
            public void onComplete()
            {
                try
                {
                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                }
                catch (final AxisFault axisFault)
                {
                    callback.receiveErrorsessionFinished(axisFault);
                }
            }
        });

        CallbackReceiver _callbackReceiver = null;
        if (this._operations[2].getMessageReceiver() == null && _operationClient.getOptions().isUseSeparateListener())
        {
            _callbackReceiver = new CallbackReceiver();
            this._operations[2].setMessageReceiver(_callbackReceiver);
        }

        _operationClient.execute(false);
    }
 
     @Override
    public SessionUpdateResponse sessionUpdate(final SessionUpdate response) throws RemoteException
    {
        MessageContext _messageContext = null;
        try
        {
            final OperationClient _operationClient = this._serviceClient.createClient(this._operations[3].getName());
            _operationClient.getOptions().setAction(
                    "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteCallback/sessionUpdate");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            _messageContext = new MessageContext();

            SOAPEnvelope env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), response,
                    this.optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteCallback/",
                            "sessionUpdate")), new QName(
                            "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteCallback/", "sessionUpdate"));

            this._serviceClient.addHeadersToEnvelope(env);
            _messageContext.setEnvelope(env);

            _operationClient.addMessageContext(_messageContext);
            _operationClient.execute(true);

            final MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            final SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            final Object object = this.fromOM(_returnEnv.getBody().getFirstElement(), SessionUpdateResponse.class,
                    this.getEnvelopeNamespaces(_returnEnv));

            return (SessionUpdateResponse) object;
        }
        catch (final AxisFault f)
        {
            final OMElement faultElt = f.getDetail();
            if (faultElt != null)
            {
                if (this.faultExceptionNameMap.containsKey(new FaultMapKey(faultElt.getQName(), "sessionUpdate")))
                {
                    try
                    {
                        final String exceptionClassName = (String) this.faultExceptionClassNameMap.get(new FaultMapKey(
                                faultElt.getQName(), "sessionUpdate"));
                        final Class<?> exceptionClass = Class.forName(exceptionClassName);
                        final Exception ex = (Exception) exceptionClass.newInstance();

                        final String messageClassName = (String) this.faultMessageMap.get(new FaultMapKey(faultElt
                                .getQName(), "sessionUpdate"));
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
    public void startsessionUpdate(final SessionUpdate response, final MultiSiteCallbackHandler callback)
            throws RemoteException
    {
        final OperationClient _operationClient = this._serviceClient.createClient(this._operations[3].getName());
        _operationClient.getOptions().setAction(
                "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteCallback/sessionUpdate");
        _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

        this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

        final MessageContext _messageContext = new MessageContext();

        SOAPEnvelope env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), response, this
                .optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteCallback/",
                        "sessionUpdate")), new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteCallback/",
                "sessionUpdate"));

        this._serviceClient.addHeadersToEnvelope(env);

        _messageContext.setEnvelope(env);

        _operationClient.addMessageContext(_messageContext);
        _operationClient.setCallback(new AxisCallback()
        {
            @Override
            public void onMessage(final MessageContext resultContext)
            {
                try
                {
                    final SOAPEnvelope resultEnv = resultContext.getEnvelope();

                    final Object object = MultiSiteCallbackStub.this.fromOM(resultEnv.getBody().getFirstElement(),
                            SessionUpdateResponse.class, MultiSiteCallbackStub.this.getEnvelopeNamespaces(resultEnv));
                    callback.receiveResultsessionUpdate((SessionUpdateResponse) object);
                }
                catch (final AxisFault e)
                {
                    callback.receiveErrorsessionUpdate(e);
                }
            }

            @Override
            public void onError(final Exception error)
            {
                if (error instanceof AxisFault)
                {
                    final AxisFault f = (AxisFault) error;
                    final OMElement faultElt = f.getDetail();
                    if (faultElt != null)
                    {
                        if (MultiSiteCallbackStub.this.faultExceptionNameMap.containsKey(new FaultMapKey(faultElt
                                .getQName(), "sessionUpdate")))
                        {
                            try
                            {
                                final String exceptionClassName = (String) MultiSiteCallbackStub.this.faultExceptionClassNameMap
                                        .get(new FaultMapKey(faultElt.getQName(), "sessionUpdate"));
                                final Class<?> exceptionClass = Class.forName(exceptionClassName);
                                final Exception ex = (Exception) exceptionClass.newInstance();
                         
                                final String messageClassName = (String) MultiSiteCallbackStub.this.faultMessageMap
                                        .get(new FaultMapKey(faultElt.getQName(), "sessionUpdate"));
                                final Class<?> messageClass = Class.forName(messageClassName);
                                final Object messageObject = MultiSiteCallbackStub.this.fromOM(faultElt, messageClass,
                                        null);
                                final Method m = exceptionClass.getMethod("setFaultMessage",
                                        new Class[] { messageClass });
                                m.invoke(ex, new Object[] { messageObject });

                                callback.receiveErrorsessionUpdate(new RemoteException(ex.getMessage(), ex));
                            }
                            catch (final ClassCastException e)
                            {
                                callback.receiveErrorsessionUpdate(f);
                            }
                            catch (final ClassNotFoundException e)
                            {
                                callback.receiveErrorsessionUpdate(f);
                            }
                            catch (final NoSuchMethodException e)
                            {
                                callback.receiveErrorsessionUpdate(f);
                            }
                            catch (final InvocationTargetException e)
                            {
                                callback.receiveErrorsessionUpdate(f);
                            }
                            catch (final IllegalAccessException e)
                            {
                                callback.receiveErrorsessionUpdate(f);
                            }
                            catch (final InstantiationException e)
                            {
                                callback.receiveErrorsessionUpdate(f);
                            }
                            catch (final AxisFault e)
                            {
                                callback.receiveErrorsessionUpdate(f);
                            }
                        }
                        else
                        {
                            callback.receiveErrorsessionUpdate(f);
                        }
                    }
                    else
                    {
                        callback.receiveErrorsessionUpdate(f);
                    }
                }
                else
                {
                    callback.receiveErrorsessionUpdate(error);
                }
            }

            @Override
            public void onFault(final MessageContext faultContext)
            {
                final AxisFault fault = Utils.getInboundFaultFromMessageContext(faultContext);
                this.onError(fault);
            }

            @Override
            public void onComplete()
            {
                try
                {
                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                }
                catch (final AxisFault axisFault)
                {
                    callback.receiveErrorsessionUpdate(axisFault);
                }
            }
        });

        CallbackReceiver _callbackReceiver = null;
        if (this._operations[3].getMessageReceiver() == null && _operationClient.getOptions().isUseSeparateListener())
        {
            _callbackReceiver = new CallbackReceiver();
            this._operations[3].setMessageReceiver(_callbackReceiver);
        }

        _operationClient.execute(false);
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

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final BookingCancelled param,
            final boolean optimizeContent, final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(BookingCancelled.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final SessionStarted param,
            final boolean optimizeContent, final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(SessionStarted.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }

    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final SessionFinished param,
            final boolean optimizeContent, final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(SessionFinished.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final SessionUpdate param,
            final boolean optimizeContent, final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(SessionUpdate.MY_QNAME, factory));
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
}
