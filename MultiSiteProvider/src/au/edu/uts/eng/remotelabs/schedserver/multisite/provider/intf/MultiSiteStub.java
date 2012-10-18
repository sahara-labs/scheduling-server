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
 * @date 17th July 2011
 */

package au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf;

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

import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.AddToQueue;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.AddToQueueResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.CancelBooking;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.CancelBookingResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.CheckAvailability;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.CheckAvailabilityResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.CreateBooking;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.CreateBookingResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.FindFreeBookings;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.FindFreeBookingsResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.FinishSession;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.FinishSessionResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.GetQueuePosition;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.GetQueuePositionResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.GetSessionInformation;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.GetSessionInformationResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.GetUserStatus;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.GetUserStatusResponse;

/**
 * MultiSite client implementation.
 */
public class MultiSiteStub extends Stub implements MultiSite
{
    protected AxisOperation[] _operations;

    private final HashMap<?, ?> faultExceptionNameMap = new HashMap<Object, Object>();
    private final HashMap<?, ?> faultExceptionClassNameMap = new HashMap<Object, Object>();
    private final HashMap<?, ?> faultMessageMap = new HashMap<Object, Object>();

    private static int counter = 0;

    private static synchronized String getUniqueSuffix()
    {
        if (MultiSiteStub.counter > 99999)
        {
            MultiSiteStub.counter = 0;
        }
        MultiSiteStub.counter = MultiSiteStub.counter + 1;
        return Long.toString(System.currentTimeMillis()) + "_" + MultiSiteStub.counter;
    }

    private void populateAxisService() throws AxisFault
    {
        this._service = new AxisService("MultiSite" + MultiSiteStub.getUniqueSuffix());
        this.addAnonymousOperations();

        this._operations = new AxisOperation[9];

        AxisOperation __operation = new OutInAxisOperation();
        __operation.setName(new QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite", "getUserStatus"));
        this._service.addOperation(__operation);
        this._operations[0] = __operation;

        __operation = new OutInAxisOperation();
        __operation.setName(new QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite", "cancelBooking"));
        this._service.addOperation(__operation);
        this._operations[1] = __operation;

        __operation = new OutInAxisOperation();
        __operation.setName(new QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite", "checkAvailability"));
        this._service.addOperation(__operation);
        this._operations[2] = __operation;

        __operation = new OutInAxisOperation();
        __operation.setName(new QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite", "findFreeBookings"));
        this._service.addOperation(__operation);
        this._operations[3] = __operation;

        __operation = new OutInAxisOperation();
        __operation
                .setName(new QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite", "getSessionInformation"));
        this._service.addOperation(__operation);
        this._operations[4] = __operation;

        __operation = new OutInAxisOperation();
        __operation.setName(new QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite", "getQueuePosition"));
        this._service.addOperation(__operation);
        this._operations[5] = __operation;

        __operation = new OutInAxisOperation();
        __operation.setName(new QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite", "addToQueue"));
        this._service.addOperation(__operation);
        this._operations[6] = __operation;

        __operation = new OutInAxisOperation();
        __operation.setName(new QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite", "createBooking"));
        this._service.addOperation(__operation);
        this._operations[7] = __operation;

        __operation = new OutInAxisOperation();
        __operation.setName(new QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite", "finishSession"));
        this._service.addOperation(__operation);
        this._operations[8] = __operation;
    }

    public MultiSiteStub(final ConfigurationContext configurationContext, final String targetEndpoint) throws AxisFault
    {
        this(configurationContext, targetEndpoint, false);
    }

    public MultiSiteStub(final ConfigurationContext configurationContext, final String targetEndpoint,
            final boolean useSeparateListener) throws AxisFault
    {
        this.populateAxisService();

        this._serviceClient = new ServiceClient(configurationContext, this._service);
        this._serviceClient.getOptions().setTo(new EndpointReference(targetEndpoint));
        this._serviceClient.getOptions().setUseSeparateListener(useSeparateListener);
    }

    public MultiSiteStub(final ConfigurationContext configurationContext) throws AxisFault
    {
        this(configurationContext,
                "http://remotelabs.eng.uts.edu.au:8080/SchedulingServer-MultiSite/services/MultiSite");
    }

    public MultiSiteStub() throws AxisFault
    {
        this("http://remotelabs.eng.uts.edu.au:8080/SchedulingServer-MultiSite/services/MultiSite");
    }

    public MultiSiteStub(final String targetEndpoint) throws AxisFault
    {
        this(null, targetEndpoint);
    }

    @Override
    public GetUserStatusResponse getUserStatus(final GetUserStatus getUserStatus0) throws RemoteException

    {
        MessageContext _messageContext = null;
        try
        {
            final OperationClient _operationClient = this._serviceClient.createClient(this._operations[0].getName());
            _operationClient.getOptions().setAction(
                    "http://remotelabs.eng.uts.edu.au/schedserver/multisite/getUserStatus");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");
            _messageContext = new MessageContext();

            SOAPEnvelope env = null;
            env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), getUserStatus0,
                    this.optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite",
                            "getUserStatus")), new QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite",
                            "getUserStatus"));

            this._serviceClient.addHeadersToEnvelope(env);

            _messageContext.setEnvelope(env);
            _operationClient.addMessageContext(_messageContext);
            _operationClient.execute(true);

            final MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            final SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            final Object object = this.fromOM(_returnEnv.getBody().getFirstElement(), GetUserStatusResponse.class,
                    this.getEnvelopeNamespaces(_returnEnv));

            return (GetUserStatusResponse) object;

        }
        catch (final AxisFault f)
        {

            final OMElement faultElt = f.getDetail();
            if (faultElt != null)
            {
                if (this.faultExceptionNameMap.containsKey(new FaultMapKey(faultElt.getQName(), "getUserStatus")))
                {
                    try
                    {
                        final String exceptionClassName = (String) this.faultExceptionClassNameMap.get(new FaultMapKey(
                                faultElt.getQName(), "getUserStatus"));
                        final Class<?> exceptionClass = Class.forName(exceptionClassName);
                        final Exception ex = (Exception) exceptionClass.newInstance();
                        //message class
                        final String messageClassName = (String) this.faultMessageMap.get(new FaultMapKey(faultElt
                                .getQName(), "getUserStatus"));
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
    public CancelBookingResponse cancelBooking(final CancelBooking cancelBooking) throws RemoteException
    {
        MessageContext _messageContext = null;
        try
        {
            final OperationClient _operationClient = this._serviceClient.createClient(this._operations[1].getName());
            _operationClient.getOptions().setAction(
                    "http://remotelabs.eng.uts.edu.au/schedserver/multisite/cancelBooking");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            _messageContext = new MessageContext();
            SOAPEnvelope env = null;

            env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), cancelBooking,
                    this.optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite",
                            "cancelBooking")), new QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite",
                            "cancelBooking"));

            this._serviceClient.addHeadersToEnvelope(env);
            _messageContext.setEnvelope(env);
            _operationClient.addMessageContext(_messageContext);
            _operationClient.execute(true);

            final MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            final SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            final Object object = this.fromOM(_returnEnv.getBody().getFirstElement(), CancelBookingResponse.class,
                    this.getEnvelopeNamespaces(_returnEnv));

            return (CancelBookingResponse) object;
        }
        catch (final AxisFault f)
        {
            final OMElement faultElt = f.getDetail();
            if (faultElt != null)
            {
                if (this.faultExceptionNameMap.containsKey(new FaultMapKey(faultElt.getQName(), "cancelBooking")))
                {
                    try
                    {
                        final String exceptionClassName = (String) this.faultExceptionClassNameMap.get(new FaultMapKey(
                                faultElt.getQName(), "cancelBooking"));
                        final Class<?> exceptionClass = Class.forName(exceptionClassName);
                        final Exception ex = (Exception) exceptionClass.newInstance();

                        final String messageClassName = (String) this.faultMessageMap.get(new FaultMapKey(faultElt
                                .getQName(), "cancelBooking"));
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
    public CheckAvailabilityResponse checkAvailability(final CheckAvailability checkAvailability) throws RemoteException
    {
        MessageContext _messageContext = null;
        try
        {
            final OperationClient _operationClient = this._serviceClient.createClient(this._operations[2].getName());
            _operationClient.getOptions().setAction(
                    "http://remotelabs.eng.uts.edu.au/schedserver/multisite/NewOperation");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            _messageContext = new MessageContext();

            SOAPEnvelope env = null;
            env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()),
                    checkAvailability, this.optimizeContent(new QName(
                            "http://remotelabs.eng.uts.edu.au/schedserver/multisite", "checkAvailability")), new QName(
                            "http://remotelabs.eng.uts.edu.au/schedserver/multisite", "checkAvailability"));

            this._serviceClient.addHeadersToEnvelope(env);

            _messageContext.setEnvelope(env);
            _operationClient.addMessageContext(_messageContext);
            _operationClient.execute(true);

            final MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            final SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            final Object object = this.fromOM(_returnEnv.getBody().getFirstElement(), CheckAvailabilityResponse.class,
                    this.getEnvelopeNamespaces(_returnEnv));

            return (CheckAvailabilityResponse) object;
        }
        catch (final AxisFault f)
        {
            final OMElement faultElt = f.getDetail();
            if (faultElt != null)
            {
                if (this.faultExceptionNameMap.containsKey(new FaultMapKey(faultElt.getQName(), "checkAvailability")))
                {
                    try
                    {
                        final String exceptionClassName = (String) this.faultExceptionClassNameMap.get(new FaultMapKey(
                                faultElt.getQName(), "checkAvailability"));
                        final Class<?> exceptionClass = Class.forName(exceptionClassName);
                        final Exception ex = (Exception) exceptionClass.newInstance();

                        final String messageClassName = (String) this.faultMessageMap.get(new FaultMapKey(faultElt
                                .getQName(), "checkAvailability"));
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
    public FindFreeBookingsResponse findFreeBookings(final FindFreeBookings findFreeBookings) throws RemoteException
    {
        MessageContext _messageContext = null;
        try
        {
            final OperationClient _operationClient = this._serviceClient.createClient(this._operations[3].getName());
            _operationClient.getOptions().setAction(
                    "http://remotelabs.eng.uts.edu.au/schedserver/multisite/findFreeBookings");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            _messageContext = new MessageContext();

            SOAPEnvelope env = null;
            env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()),
                    findFreeBookings, this.optimizeContent(new QName(
                            "http://remotelabs.eng.uts.edu.au/schedserver/multisite", "findFreeBookings")), new QName(
                            "http://remotelabs.eng.uts.edu.au/schedserver/multisite", "findFreeBookings"));

            this._serviceClient.addHeadersToEnvelope(env);
            _messageContext.setEnvelope(env);
            _operationClient.addMessageContext(_messageContext);
            _operationClient.execute(true);

            final MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            final SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            final Object object = this.fromOM(_returnEnv.getBody().getFirstElement(), FindFreeBookingsResponse.class,
                    this.getEnvelopeNamespaces(_returnEnv));

            return (FindFreeBookingsResponse) object;
        }
        catch (final AxisFault f)
        {
            final OMElement faultElt = f.getDetail();
            if (faultElt != null)
            {
                if (this.faultExceptionNameMap.containsKey(new FaultMapKey(faultElt.getQName(), "findFreeBookings")))
                {
                    try
                    {
                        final String exceptionClassName = (String) this.faultExceptionClassNameMap.get(new FaultMapKey(
                                faultElt.getQName(), "findFreeBookings"));
                        final Class<?> exceptionClass = Class.forName(exceptionClassName);
                        final Exception ex = (Exception) exceptionClass.newInstance();
                        final String messageClassName = (String) this.faultMessageMap.get(new FaultMapKey(faultElt
                                .getQName(), "findFreeBookings"));
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
    public GetSessionInformationResponse getSessionInformation(final GetSessionInformation getSessionInformation)
            throws RemoteException
    {
        MessageContext _messageContext = null;
        try
        {
            final OperationClient _operationClient = this._serviceClient.createClient(this._operations[4].getName());
            _operationClient.getOptions().setAction(
                    "http://remotelabs.eng.uts.edu.au/schedserver/multisite/getSessionInformation");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            _messageContext = new MessageContext();
            SOAPEnvelope env = null;
            env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()),
                    getSessionInformation, this.optimizeContent(new QName(
                            "http://remotelabs.eng.uts.edu.au/schedserver/multisite", "getSessionInformation")),
                    new QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite", "getSessionInformation"));

            this._serviceClient.addHeadersToEnvelope(env);
            _messageContext.setEnvelope(env);
            _operationClient.addMessageContext(_messageContext);
            _operationClient.execute(true);

            final MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            final SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            final Object object = this.fromOM(_returnEnv.getBody().getFirstElement(),
                    GetSessionInformationResponse.class, this.getEnvelopeNamespaces(_returnEnv));

            return (GetSessionInformationResponse) object;
        }
        catch (final AxisFault f)
        {
            final OMElement faultElt = f.getDetail();
            if (faultElt != null)
            {
                if (this.faultExceptionNameMap
                        .containsKey(new FaultMapKey(faultElt.getQName(), "getSessionInformation")))
                {
                    try
                    {
                        final String exceptionClassName = (String) this.faultExceptionClassNameMap.get(new FaultMapKey(
                                faultElt.getQName(), "getSessionInformation"));
                        final Class<?> exceptionClass = Class.forName(exceptionClassName);
                        final Exception ex = (Exception) exceptionClass.newInstance();
                    
                        final String messageClassName = (String) this.faultMessageMap.get(new FaultMapKey(faultElt
                                .getQName(), "getSessionInformation"));
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
    public GetQueuePositionResponse getQueuePosition(final GetQueuePosition getQueuePosition) throws RemoteException
    {
        MessageContext _messageContext = null;
        try
        {
            final OperationClient _operationClient = this._serviceClient.createClient(this._operations[5].getName());
            _operationClient.getOptions().setAction(
                    "http://remotelabs.eng.uts.edu.au/schedserver/multisite/getQueuePosition");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

            _messageContext = new MessageContext();
            SOAPEnvelope env = null;
            env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()),
                    getQueuePosition, this.optimizeContent(new QName(
                            "http://remotelabs.eng.uts.edu.au/schedserver/multisite", "getQueuePosition")), new QName(
                            "http://remotelabs.eng.uts.edu.au/schedserver/multisite", "getQueuePosition"));

            this._serviceClient.addHeadersToEnvelope(env);
            _messageContext.setEnvelope(env);
            _operationClient.addMessageContext(_messageContext);
            _operationClient.execute(true);

            final MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            final SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
            final Object object = this.fromOM(_returnEnv.getBody().getFirstElement(), GetQueuePositionResponse.class,
                    this.getEnvelopeNamespaces(_returnEnv));

            return (GetQueuePositionResponse) object;
        }
        catch (final AxisFault f)
        {
            final OMElement faultElt = f.getDetail();
            if (faultElt != null)
            {
                if (this.faultExceptionNameMap.containsKey(new FaultMapKey(faultElt.getQName(), "getQueuePosition")))
                {
                    try
                    {
                        final String exceptionClassName = (String) this.faultExceptionClassNameMap.get(new FaultMapKey(
                                faultElt.getQName(), "getQueuePosition"));
                        final Class<?> exceptionClass = Class.forName(exceptionClassName);
                        final Exception ex = (Exception) exceptionClass.newInstance();
                 
                        final String messageClassName = (String) this.faultMessageMap.get(new FaultMapKey(faultElt
                                .getQName(), "getQueuePosition"));
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
    public AddToQueueResponse addToQueue(final AddToQueue addToQueue) throws RemoteException
    {
        MessageContext _messageContext = null;
        try
        {
            final OperationClient _operationClient = this._serviceClient.createClient(this._operations[6].getName());
            _operationClient.getOptions()
                    .setAction("http://remotelabs.eng.uts.edu.au/schedserver/multisite/addToQueue");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            _messageContext = new MessageContext();
            SOAPEnvelope env = null;
            env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), addToQueue,
                    this.optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite",
                            "addToQueue")), new QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite",
                            "addToQueue"));

            this._serviceClient.addHeadersToEnvelope(env);
            _messageContext.setEnvelope(env);
            _operationClient.addMessageContext(_messageContext);
            _operationClient.execute(true);

            final MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            final SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            final Object object = this.fromOM(_returnEnv.getBody().getFirstElement(), AddToQueueResponse.class,
                    this.getEnvelopeNamespaces(_returnEnv));

            return (AddToQueueResponse) object;
        }
        catch (final AxisFault f)
        {
            final OMElement faultElt = f.getDetail();
            if (faultElt != null)
            {
                if (this.faultExceptionNameMap.containsKey(new FaultMapKey(faultElt.getQName(), "addToQueue")))
                {
                    try
                    {
                        final String exceptionClassName = (String) this.faultExceptionClassNameMap.get(new FaultMapKey(
                                faultElt.getQName(), "addToQueue"));
                        final Class<?> exceptionClass = Class.forName(exceptionClassName);
                        final Exception ex = (Exception) exceptionClass.newInstance();

                        final String messageClassName = (String) this.faultMessageMap.get(new FaultMapKey(faultElt
                                .getQName(), "addToQueue"));
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
    public CreateBookingResponse createBooking(final CreateBooking createBooking) throws RemoteException
    {
        MessageContext _messageContext = null;
        try
        {
            final OperationClient _operationClient = this._serviceClient.createClient(this._operations[7].getName());
            _operationClient.getOptions().setAction(
                    "http://remotelabs.eng.uts.edu.au/schedserver/multisite/createBooking");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            _messageContext = new MessageContext();
            SOAPEnvelope env = null;
            env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), createBooking,
                    this.optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite",
                            "createBooking")), new QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite",
                            "createBooking"));

            this._serviceClient.addHeadersToEnvelope(env);
            _messageContext.setEnvelope(env);

            _operationClient.addMessageContext(_messageContext);
            _operationClient.execute(true);

            final MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            final SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            final Object object = this.fromOM(_returnEnv.getBody().getFirstElement(), CreateBookingResponse.class,
                    this.getEnvelopeNamespaces(_returnEnv));

            return (CreateBookingResponse) object;
        }
        catch (final AxisFault f)
        {
            final OMElement faultElt = f.getDetail();
            if (faultElt != null)
            {
                if (this.faultExceptionNameMap.containsKey(new FaultMapKey(faultElt.getQName(), "createBooking")))
                {
                    try
                    {
                        final String exceptionClassName = (String) this.faultExceptionClassNameMap.get(new FaultMapKey(
                                faultElt.getQName(), "createBooking"));
                        final Class<?> exceptionClass = Class.forName(exceptionClassName);
                        final Exception ex = (Exception) exceptionClass.newInstance();

                        final String messageClassName = (String) this.faultMessageMap.get(new FaultMapKey(faultElt
                                .getQName(), "createBooking"));
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
    public FinishSessionResponse finishSession(final FinishSession finishSession) throws RemoteException
    {
        MessageContext _messageContext = null;
        try
        {
            final OperationClient _operationClient = this._serviceClient.createClient(this._operations[8].getName());
            _operationClient.getOptions().setAction(
                    "http://remotelabs.eng.uts.edu.au/schedserver/multisite/finishSession");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            _messageContext = new MessageContext();

            SOAPEnvelope env = null;
            env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), finishSession,
                    this.optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite",
                            "finishSession")), new QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite",
                            "finishSession"));

            this._serviceClient.addHeadersToEnvelope(env);
            _messageContext.setEnvelope(env);
            _operationClient.addMessageContext(_messageContext);
            _operationClient.execute(true);

            final MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            final SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            final Object object = this.fromOM(_returnEnv.getBody().getFirstElement(), FinishSessionResponse.class,
                    this.getEnvelopeNamespaces(_returnEnv));

            return (FinishSessionResponse) object;
        }
        catch (final AxisFault f)
        {
            final OMElement faultElt = f.getDetail();
            if (faultElt != null)
            {
                if (this.faultExceptionNameMap.containsKey(new FaultMapKey(faultElt.getQName(), "finishSession")))
                {
                    try
                    {
                        final String exceptionClassName = (String) this.faultExceptionClassNameMap.get(new FaultMapKey(
                                faultElt.getQName(), "finishSession"));
                        final Class<?> exceptionClass = Class.forName(exceptionClassName);
                        final Exception ex = (Exception) exceptionClass.newInstance();

                        final String messageClassName = (String) this.faultMessageMap.get(new FaultMapKey(faultElt
                                .getQName(), "finishSession"));
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

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final GetUserStatus param,
            final boolean optimizeContent, final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(GetUserStatus.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final CancelBooking param,
            final boolean optimizeContent, final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(CancelBooking.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final CheckAvailability param,
            final boolean optimizeContent, final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(CheckAvailability.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final FindFreeBookings param,
            final boolean optimizeContent, final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(FindFreeBookings.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final GetSessionInformation param,
            final boolean optimizeContent, final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(GetSessionInformation.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final GetQueuePosition param,
            final boolean optimizeContent, final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(GetQueuePosition.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final AddToQueue param, final boolean optimizeContent,
            final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(AddToQueue.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final CreateBooking param,
            final boolean optimizeContent, final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(CreateBooking.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final FinishSession param,
            final boolean optimizeContent, final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(FinishSession.MY_QNAME, factory));
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
            if (GetUserStatus.class.equals(type))
            {
                return GetUserStatus.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetUserStatusResponse.class.equals(type))
            {
                return GetUserStatusResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (CancelBooking.class.equals(type))
            {
                return CancelBooking.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (CancelBookingResponse.class.equals(type))
            {
                return CancelBookingResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (CheckAvailability.class.equals(type))
            {
                return CheckAvailability.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (CheckAvailabilityResponse.class.equals(type))
            {
                return CheckAvailabilityResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (FindFreeBookings.class.equals(type))
            {
                return FindFreeBookings.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (FindFreeBookingsResponse.class.equals(type))
            {
                return FindFreeBookingsResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetSessionInformation.class.equals(type))
            {
                return GetSessionInformation.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetSessionInformationResponse.class.equals(type))
            {
                return GetSessionInformationResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetQueuePosition.class.equals(type))
            {
                return GetQueuePosition.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetQueuePositionResponse.class.equals(type))
            {
                return GetQueuePositionResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (AddToQueue.class.equals(type))
            {
                return AddToQueue.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (AddToQueueResponse.class.equals(type))
            {
                return AddToQueueResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (CreateBooking.class.equals(type))
            {
                return CreateBooking.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (CreateBookingResponse.class.equals(type))
            {
                return CreateBookingResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (FinishSession.class.equals(type))
            {
                return FinishSession.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (FinishSessionResponse.class.equals(type))
            {
                return FinishSessionResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }
        }
        catch (final Exception e)
        {
            throw AxisFault.makeFault(e);
        }
        return null;
    }
}
