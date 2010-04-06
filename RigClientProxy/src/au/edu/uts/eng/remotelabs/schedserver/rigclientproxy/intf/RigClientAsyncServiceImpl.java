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
 * @author Michael Diponio (mdiponio)
 * @date 5th April 2009
 */

package au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf;

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

import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.RigClientAsyncServiceCallbackHandler;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.AbortBatchControl;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.AbortBatchControlResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.Allocate;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.AllocateResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.GetAttribute;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.GetAttributeResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.GetBatchControlStatus;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.GetBatchControlStatusResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.GetStatus;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.GetStatusResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.IsActivityDetectable;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.IsActivityDetectableResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.Notify;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.NotifyResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.PerformBatchControl;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.PerformBatchControlResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.PerformPrimitiveControl;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.PerformPrimitiveControlResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.Release;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.ReleaseResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.SetMaintenance;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.SetMaintenanceResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.SetTestInterval;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.SetTestIntervalResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.SlaveAllocate;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.SlaveAllocateResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.SlaveRelease;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.SlaveReleaseResponse;

/**
 * RigClient client service implementation.
 */
@SuppressWarnings("unchecked")
public class RigClientAsyncServiceImpl extends Stub
{
    protected AxisOperation[] _operations;

    //hashmaps to keep the fault mapping
    private final HashMap faultExceptionNameMap = new HashMap();
    private final HashMap faultExceptionClassNameMap = new HashMap();
    private final HashMap faultMessageMap = new HashMap();

    private static int counter = 0;
    
    public RigClientAsyncServiceImpl() throws AxisFault
    {
        this("http://remotelabs.eng.uts.edu.au:8080/rigclient");
    }
    
    public RigClientAsyncServiceImpl(final ConfigurationContext configurationContext) throws AxisFault
    {
        this(configurationContext, "http://remotelabs.eng.uts.edu.au:8080/rigclient");
    }

    public RigClientAsyncServiceImpl(final ConfigurationContext configurationContext, final String targetEndpoint)
            throws AxisFault
    {
        this(configurationContext, targetEndpoint, false);
    }

    public RigClientAsyncServiceImpl(ConfigurationContext configurationContext, final String targetEndpoint,
            final boolean useSeparateListener) throws AxisFault
    {
        this.populateAxisService();
        
        this._serviceClient = new ServiceClient(configurationContext, this._service);
        configurationContext = this._serviceClient.getServiceContext().getConfigurationContext();
        this._serviceClient.getOptions().setTo(new EndpointReference(targetEndpoint));
        this._serviceClient.getOptions().setUseSeparateListener(useSeparateListener);
    }

    public RigClientAsyncServiceImpl(final String targetEndpoint) throws AxisFault
    {
        this(null, targetEndpoint);
    }
    
    private static synchronized String getUniqueSuffix()
    {
        if (RigClientAsyncServiceImpl.counter > 99999)
        {
            RigClientAsyncServiceImpl.counter = 0;
        }
        RigClientAsyncServiceImpl.counter = RigClientAsyncServiceImpl.counter + 1;
        return Long.toString(System.currentTimeMillis()) + "_" + RigClientAsyncServiceImpl.counter;
    }

    private void populateAxisService() throws AxisFault
    {
        this._service = new AxisService("RigClientService" + RigClientAsyncServiceImpl.getUniqueSuffix());
        this.addAnonymousOperations();

        AxisOperation op;
        this._operations = new AxisOperation[14];
        
        op = new OutInAxisOperation();
        op.setName(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "performPrimitiveControl"));
        this._service.addOperation(op);
        this._operations[0] = op;
        
        op = new OutInAxisOperation();
        op.setName(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "getBatchControlStatus"));
        this._service.addOperation(op);
        this._operations[1] = op;

        op = new OutInAxisOperation();
        op.setName(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "isActivityDetectable"));
        this._service.addOperation(op);
        this._operations[2] = op;

        op = new OutInAxisOperation();
        op.setName(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "getAttribute"));
        this._service.addOperation(op);
        this._operations[3] = op;

        op = new OutInAxisOperation();
        op.setName(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "slaveRelease"));
        this._service.addOperation(op);
        this._operations[4] = op;

        op = new OutInAxisOperation();
        op.setName(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "release"));
        this._service.addOperation(op);
        this._operations[5] = op;

        op = new OutInAxisOperation();
        op.setName(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "notify"));
        this._service.addOperation(op);
        this._operations[6] = op;

        op = new OutInAxisOperation();
        op.setName(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "setTestInterval"));
        this._service.addOperation(op);
        this._operations[7] = op;

        op = new OutInAxisOperation();
        op.setName(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "setMaintenance"));
        this._service.addOperation(op);
        this._operations[8] = op;
        
        op = new OutInAxisOperation();
        op.setName(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "performBatchControl"));
        this._service.addOperation(op);
        this._operations[9] = op;
        
        op = new OutInAxisOperation();
        op.setName(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "getStatus"));
        this._service.addOperation(op);
        this._operations[10] = op;

        op = new OutInAxisOperation();
        op.setName(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "allocate"));
        this._service.addOperation(op);
        this._operations[11] = op;

        op = new OutInAxisOperation();
        op.setName(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "slaveAllocate"));
        this._service.addOperation(op);
        this._operations[12] = op;

        op = new OutInAxisOperation();
        op.setName(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "abortBatchControl"));
        this._service.addOperation(op);
        this._operations[13] = op;
    }

    public void startperformPrimitiveControl(final PerformPrimitiveControl request, final RigClientAsyncServiceCallbackHandler callback)
            throws RemoteException
    {

        final OperationClient _operationClient = this._serviceClient.createClient(this._operations[0].getName());
        _operationClient.getOptions().setAction("http://remotelabs.eng.uts.edu.au/rigclient/primitiveControl");
        _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

        this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

        final MessageContext _messageContext = new MessageContext();
        SOAPEnvelope env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()),
                request, this.optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "performPrimitiveControl")));
        this._serviceClient.addHeadersToEnvelope(env);
        _messageContext.setEnvelope(env);
        _operationClient.addMessageContext(_messageContext);

        _operationClient.setCallback(new AxisCallback()
        {
            public void onMessage(final MessageContext resultContext)
            {
                try
                {
                    final SOAPEnvelope resultEnv = resultContext.getEnvelope();
                    final Object object = RigClientAsyncServiceImpl.this.fromOM(resultEnv.getBody().getFirstElement(),
                            PerformPrimitiveControlResponse.class, RigClientAsyncServiceImpl.this.getEnvelopeNamespaces(resultEnv));
                    callback.receiveResultperformPrimitiveControl((PerformPrimitiveControlResponse) object);
                }
                catch (final AxisFault e)
                {
                    callback.receiveErrorperformPrimitiveControl(e);
                }
            }

            public void onError(final Exception error)
            {
                if (error instanceof AxisFault)
                {
                    final AxisFault f = (AxisFault) error;
                    final OMElement faultElt = f.getDetail();
                    if (faultElt != null)
                    {
                        if (RigClientAsyncServiceImpl.this.faultExceptionNameMap.containsKey(faultElt.getQName()))
                        {
                            try
                            {
                                final String exceptionClassName = (String) RigClientAsyncServiceImpl.this.faultExceptionClassNameMap
                                        .get(faultElt.getQName());
                                final Class exceptionClass = Class.forName(exceptionClassName);
                                final Exception ex = (Exception) exceptionClass.newInstance();
                                
                                final String messageClassName = (String) RigClientAsyncServiceImpl.this.faultMessageMap
                                        .get(faultElt.getQName());
                                final Class messageClass = Class.forName(messageClassName);
                                final Object messageObject = RigClientAsyncServiceImpl.this.fromOM(faultElt, messageClass,
                                        null);
                                final Method m = exceptionClass.getMethod("setFaultMessage",
                                        new Class[] { messageClass });
                                m.invoke(ex, new Object[] { messageObject });

                                callback.receiveErrorperformPrimitiveControl(new RemoteException(ex.getMessage(), ex));
                            }
                            catch (final ClassCastException e)
                            {                                
                                callback.receiveErrorperformPrimitiveControl(f);
                            }
                            catch (final ClassNotFoundException e)
                            {   
                                callback.receiveErrorperformPrimitiveControl(f);
                            }
                            catch (final NoSuchMethodException e)
                            {   
                                callback.receiveErrorperformPrimitiveControl(f);
                            }
                            catch (final InvocationTargetException e)
                            {   
                                callback.receiveErrorperformPrimitiveControl(f);
                            }
                            catch (final IllegalAccessException e)
                            {   
                                callback.receiveErrorperformPrimitiveControl(f);
                            }
                            catch (final InstantiationException e)
                            {   
                                callback.receiveErrorperformPrimitiveControl(f);
                            }
                            catch (final AxisFault e)
                            {   
                                callback.receiveErrorperformPrimitiveControl(f);
                            }
                        }
                        else
                        {
                            callback.receiveErrorperformPrimitiveControl(f);
                        }
                    }
                    else
                    {
                        callback.receiveErrorperformPrimitiveControl(f);
                    }
                }
                else
                {
                    callback.receiveErrorperformPrimitiveControl(error);
                }
            }

            public void onFault(final MessageContext faultContext)
            {
                final AxisFault fault = Utils.getInboundFaultFromMessageContext(faultContext);
                this.onError(fault);
            }

            public void onComplete()
            {
                try
                {
                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                }
                catch (final AxisFault axisFault)
                {
                    callback.receiveErrorperformPrimitiveControl(axisFault);
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

    public void startgetBatchControlStatus(final GetBatchControlStatus request, final RigClientAsyncServiceCallbackHandler callback)
            throws RemoteException
    {
        final OperationClient _operationClient = this._serviceClient.createClient(this._operations[1].getName());
        _operationClient.getOptions().setAction("http://remotelabs.eng.uts.edu.au/rigclient/batchStatus");
        _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);
        this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

        SOAPEnvelope env = null;
        final MessageContext _messageContext = new MessageContext();

        env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()),
                request, this.optimizeContent(new QName(
                        "http://remotelabs.eng.uts.edu.au/rigclient/protocol", "getBatchControlStatus")));
        
        this._serviceClient.addHeadersToEnvelope(env);
        _messageContext.setEnvelope(env);
        _operationClient.addMessageContext(_messageContext);
        
        _operationClient.setCallback(new AxisCallback()
        {
            public void onMessage(final MessageContext resultContext)
            {
                try
                {
                    final SOAPEnvelope resultEnv = resultContext.getEnvelope();
                    final Object object = RigClientAsyncServiceImpl.this.fromOM(resultEnv.getBody().getFirstElement(),
                            GetBatchControlStatusResponse.class, RigClientAsyncServiceImpl.this
                                    .getEnvelopeNamespaces(resultEnv));
                    callback.receiveResultgetBatchControlStatus((GetBatchControlStatusResponse) object);
                }
                catch (final AxisFault e)
                {
                    callback.receiveErrorgetBatchControlStatus(e);
                }
            }

            public void onError(final Exception error)
            {
                if (error instanceof AxisFault)
                {
                    final AxisFault f = (AxisFault) error;
                    final OMElement faultElt = f.getDetail();
                    if (faultElt != null)
                    {
                        if (RigClientAsyncServiceImpl.this.faultExceptionNameMap.containsKey(faultElt.getQName()))
                        {           
                            try
                            {
                                final String exceptionClassName = (String) RigClientAsyncServiceImpl.this.faultExceptionClassNameMap
                                        .get(faultElt.getQName());
                                final Class exceptionClass = Class.forName(exceptionClassName);
                                final Exception ex = (Exception) exceptionClass.newInstance();
                                
                                final String messageClassName = (String) RigClientAsyncServiceImpl.this.faultMessageMap
                                        .get(faultElt.getQName());
                                final Class messageClass = Class.forName(messageClassName);
                                final Object messageObject = RigClientAsyncServiceImpl.this.fromOM(faultElt, messageClass,
                                        null);
                                final Method m = exceptionClass.getMethod("setFaultMessage",
                                        new Class[] { messageClass });
                                m.invoke(ex, new Object[] { messageObject });
                                callback.receiveErrorgetBatchControlStatus(new RemoteException(ex.getMessage(), ex));
                            }
                            catch (final ClassCastException e)
                            {
                                callback.receiveErrorgetBatchControlStatus(f);
                            }
                            catch (final ClassNotFoundException e)
                            {
                                callback.receiveErrorgetBatchControlStatus(f);
                            }
                            catch (final NoSuchMethodException e)
                            {
                                callback.receiveErrorgetBatchControlStatus(f);
                            }
                            catch (final InvocationTargetException e)
                            {
                                callback.receiveErrorgetBatchControlStatus(f);
                            }
                            catch (final IllegalAccessException e)
                            {
                                callback.receiveErrorgetBatchControlStatus(f);
                            }
                            catch (final InstantiationException e)
                            {
                                callback.receiveErrorgetBatchControlStatus(f);
                            }
                            catch (final AxisFault e)
                            {
                                callback.receiveErrorgetBatchControlStatus(f);
                            }
                        }
                        else
                        {
                            callback.receiveErrorgetBatchControlStatus(f);
                        }
                    }
                    else
                    {
                        callback.receiveErrorgetBatchControlStatus(f);
                    }
                }
                else
                {
                    callback.receiveErrorgetBatchControlStatus(error);
                }
            }

            public void onFault(final MessageContext faultContext)
            {
                final AxisFault fault = Utils.getInboundFaultFromMessageContext(faultContext);
                this.onError(fault);
            }

            public void onComplete()
            {
                try
                {
                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                }
                catch (final AxisFault axisFault)
                {
                    callback.receiveErrorgetBatchControlStatus(axisFault);
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

    public void startisActivityDetectable(final IsActivityDetectable request, final RigClientAsyncServiceCallbackHandler callback)
            throws RemoteException
    {
        final OperationClient _operationClient = this._serviceClient.createClient(this._operations[2].getName());
        _operationClient.getOptions().setAction("http://remotelabs.eng.uts.edu.au/rigclient/isActivityDetectable");
        _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

        this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
        SOAPEnvelope env = null;
        final MessageContext _messageContext = new MessageContext();
        env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()),
                request, this.optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "isActivityDetectable")));

        this._serviceClient.addHeadersToEnvelope(env);
        _messageContext.setEnvelope(env);
        _operationClient.addMessageContext(_messageContext);

        _operationClient.setCallback(new AxisCallback()
        {
            public void onMessage(final MessageContext resultContext)
            {
                try
                {
                    final SOAPEnvelope resultEnv = resultContext.getEnvelope();

                    final Object object = RigClientAsyncServiceImpl.this.fromOM(resultEnv.getBody().getFirstElement(),
                            IsActivityDetectableResponse.class, RigClientAsyncServiceImpl.this.getEnvelopeNamespaces(resultEnv));
                    callback.receiveResultisActivityDetectable((IsActivityDetectableResponse) object);

                }
                catch (final AxisFault e)
                {
                    callback.receiveErrorisActivityDetectable(e);
                }
            }

            public void onError(final Exception error)
            {
                if (error instanceof AxisFault)
                {
                    final AxisFault f = (AxisFault) error;
                    final OMElement faultElt = f.getDetail();
                    if (faultElt != null)
                    {
                        if (RigClientAsyncServiceImpl.this.faultExceptionNameMap.containsKey(faultElt.getQName()))
                        {
                            try
                            {
                                final String exceptionClassName = (String) RigClientAsyncServiceImpl.this.faultExceptionClassNameMap
                                        .get(faultElt.getQName());
                                final Class exceptionClass = Class.forName(exceptionClassName);
                                final Exception ex = (Exception) exceptionClass.newInstance();
                                
                                final String messageClassName = (String) RigClientAsyncServiceImpl.this.faultMessageMap
                                        .get(faultElt.getQName());
                                final Class messageClass = Class.forName(messageClassName);
                                final Object messageObject = RigClientAsyncServiceImpl.this.fromOM(faultElt, messageClass,
                                        null);
                                final Method m = exceptionClass.getMethod("setFaultMessage",
                                        new Class[] { messageClass });
                                m.invoke(ex, new Object[] { messageObject });

                                callback.receiveErrorisActivityDetectable(new RemoteException(ex.getMessage(), ex));
                            }
                            catch (final ClassCastException e)
                            {
                                callback.receiveErrorisActivityDetectable(f);
                            }
                            catch (final ClassNotFoundException e)
                            {
                                callback.receiveErrorisActivityDetectable(f);
                            }
                            catch (final NoSuchMethodException e)
                            {
                                callback.receiveErrorisActivityDetectable(f);
                            }
                            catch (final InvocationTargetException e)
                            {
                                callback.receiveErrorisActivityDetectable(f);
                            }
                            catch (final IllegalAccessException e)
                            {
                                callback.receiveErrorisActivityDetectable(f);
                            }
                            catch (final InstantiationException e)
                            {
                                callback.receiveErrorisActivityDetectable(f);
                            }
                            catch (final AxisFault e)
                            {
                                callback.receiveErrorisActivityDetectable(f);
                            }
                        }
                        else
                        {
                            callback.receiveErrorisActivityDetectable(f);
                        }
                    }
                    else
                    {
                        callback.receiveErrorisActivityDetectable(f);
                    }
                }
                else
                {
                    callback.receiveErrorisActivityDetectable(error);
                }
            }

            public void onFault(final MessageContext faultContext)
            {
                final AxisFault fault = Utils.getInboundFaultFromMessageContext(faultContext);
                this.onError(fault);
            }

            public void onComplete()
            {
                try
                {
                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                }
                catch (final AxisFault axisFault)
                {
                    callback.receiveErrorisActivityDetectable(axisFault);
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

    public void startgetAttribute(final GetAttribute request, final RigClientAsyncServiceCallbackHandler callback)
            throws RemoteException
    {
        final OperationClient _operationClient = this._serviceClient.createClient(this._operations[3].getName());
        _operationClient.getOptions().setAction("http://remotelabs.eng.uts.edu.au/rigclient/getAttribute");
        _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

        this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
        SOAPEnvelope env = null;
        final MessageContext _messageContext = new MessageContext();
        env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), request, this
                .optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "getAttribute")));

        this._serviceClient.addHeadersToEnvelope(env);
        _messageContext.setEnvelope(env);
        _operationClient.addMessageContext(_messageContext);

        _operationClient.setCallback(new AxisCallback()
        {
            public void onMessage(final MessageContext resultContext)
            {
                try
                {
                    final SOAPEnvelope resultEnv = resultContext.getEnvelope();

                    final Object object = RigClientAsyncServiceImpl.this.fromOM(resultEnv.getBody().getFirstElement(),
                            GetAttributeResponse.class, RigClientAsyncServiceImpl.this.getEnvelopeNamespaces(resultEnv));
                    callback.receiveResultgetAttribute((GetAttributeResponse) object);
                }
                catch (final AxisFault e)
                {
                    callback.receiveErrorgetAttribute(e);
                }
            }

            public void onError(final Exception error)
            {
                if (error instanceof AxisFault)
                {
                    final AxisFault f = (AxisFault) error;
                    final OMElement faultElt = f.getDetail();
                    if (faultElt != null)
                    {
                        if (RigClientAsyncServiceImpl.this.faultExceptionNameMap.containsKey(faultElt.getQName()))
                        {
                            try
                            {
                                final String exceptionClassName = (String) RigClientAsyncServiceImpl.this.faultExceptionClassNameMap
                                        .get(faultElt.getQName());
                                final Class exceptionClass = Class.forName(exceptionClassName);
                                final Exception ex = (Exception) exceptionClass.newInstance();
                                final String messageClassName = (String) RigClientAsyncServiceImpl.this.faultMessageMap
                                        .get(faultElt.getQName());
                                final Class messageClass = Class.forName(messageClassName);
                                final Object messageObject = RigClientAsyncServiceImpl.this.fromOM(faultElt, messageClass,
                                        null);
                                final Method m = exceptionClass.getMethod("setFaultMessage",
                                        new Class[] { messageClass });
                                m.invoke(ex, new Object[] { messageObject });

                                callback.receiveErrorgetAttribute(new RemoteException(ex.getMessage(), ex));
                            }
                            catch (final ClassCastException e)
                            {
                                
                                callback.receiveErrorgetAttribute(f);
                            }
                            catch (final ClassNotFoundException e)
                            {
                                
                                callback.receiveErrorgetAttribute(f);
                            }
                            catch (final NoSuchMethodException e)
                            {
                                
                                callback.receiveErrorgetAttribute(f);
                            }
                            catch (final InvocationTargetException e)
                            {
                                
                                callback.receiveErrorgetAttribute(f);
                            }
                            catch (final IllegalAccessException e)
                            {
                                
                                callback.receiveErrorgetAttribute(f);
                            }
                            catch (final InstantiationException e)
                            {
                                
                                callback.receiveErrorgetAttribute(f);
                            }
                            catch (final AxisFault e)
                            {
                                
                                callback.receiveErrorgetAttribute(f);
                            }
                        }
                        else
                        {
                            callback.receiveErrorgetAttribute(f);
                        }
                    }
                    else
                    {
                        callback.receiveErrorgetAttribute(f);
                    }
                }
                else
                {
                    callback.receiveErrorgetAttribute(error);
                }
            }

            public void onFault(final MessageContext faultContext)
            {
                final AxisFault fault = Utils.getInboundFaultFromMessageContext(faultContext);
                this.onError(fault);
            }

            public void onComplete()
            {
                try
                {
                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                }
                catch (final AxisFault axisFault)
                {
                    callback.receiveErrorgetAttribute(axisFault);
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

    public void startslaveRelease(final SlaveRelease request, final RigClientAsyncServiceCallbackHandler callback)
            throws RemoteException
    {
        final OperationClient _operationClient = this._serviceClient.createClient(this._operations[4].getName());
        _operationClient.getOptions().setAction("http://remotelabs.eng.uts.edu.au/rigclient/slaveRelease");
        _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

        this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
        
        SOAPEnvelope env = null;
        final MessageContext _messageContext = new MessageContext();

        env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), request, this
                .optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "slaveRelease")));

        
        this._serviceClient.addHeadersToEnvelope(env);
        _messageContext.setEnvelope(env);
        _operationClient.addMessageContext(_messageContext);

        _operationClient.setCallback(new AxisCallback()
        {
            public void onMessage(final MessageContext resultContext)
            {
                try
                {
                    final SOAPEnvelope resultEnv = resultContext.getEnvelope();

                    final Object object = RigClientAsyncServiceImpl.this.fromOM(resultEnv.getBody().getFirstElement(),
                            SlaveReleaseResponse.class, RigClientAsyncServiceImpl.this.getEnvelopeNamespaces(resultEnv));
                    callback.receiveResultslaveRelease((SlaveReleaseResponse) object);

                }
                catch (final AxisFault e)
                {
                    callback.receiveErrorslaveRelease(e);
                }
            }

            public void onError(final Exception error)
            {
                if (error instanceof AxisFault)
                {
                    final AxisFault f = (AxisFault) error;
                    final OMElement faultElt = f.getDetail();
                    if (faultElt != null)
                    {
                        if (RigClientAsyncServiceImpl.this.faultExceptionNameMap.containsKey(faultElt.getQName()))
                        {                            
                            try
                            {
                                final String exceptionClassName = (String) RigClientAsyncServiceImpl.this.faultExceptionClassNameMap
                                        .get(faultElt.getQName());
                                final Class exceptionClass = Class.forName(exceptionClassName);
                                final Exception ex = (Exception) exceptionClass.newInstance();
                                
                                final String messageClassName = (String) RigClientAsyncServiceImpl.this.faultMessageMap
                                        .get(faultElt.getQName());
                                final Class messageClass = Class.forName(messageClassName);
                                final Object messageObject = RigClientAsyncServiceImpl.this.fromOM(faultElt, messageClass,
                                        null);
                                final Method m = exceptionClass.getMethod("setFaultMessage",
                                        new Class[] { messageClass });
                                m.invoke(ex, new Object[] { messageObject });

                                callback.receiveErrorslaveRelease(new RemoteException(ex.getMessage(), ex));
                            }
                            catch (final ClassCastException e)
                            {       
                                callback.receiveErrorslaveRelease(f);
                            }
                            catch (final ClassNotFoundException e)
                            {   
                                callback.receiveErrorslaveRelease(f);
                            }
                            catch (final NoSuchMethodException e)
                            {   
                                callback.receiveErrorslaveRelease(f);
                            }
                            catch (final InvocationTargetException e)
                            {   
                                callback.receiveErrorslaveRelease(f);
                            }
                            catch (final IllegalAccessException e)
                            {   
                                callback.receiveErrorslaveRelease(f);
                            }
                            catch (final InstantiationException e)
                            {   
                                callback.receiveErrorslaveRelease(f);
                            }
                            catch (final AxisFault e)
                            {   
                                callback.receiveErrorslaveRelease(f);
                            }
                        }
                        else
                        {
                            callback.receiveErrorslaveRelease(f);
                        }
                    }
                    else
                    {
                        callback.receiveErrorslaveRelease(f);
                    }
                }
                else
                {
                    callback.receiveErrorslaveRelease(error);
                }
            }

            public void onFault(final MessageContext faultContext)
            {
                final AxisFault fault = Utils.getInboundFaultFromMessageContext(faultContext);
                this.onError(fault);
            }

            public void onComplete()
            {
                try
                {
                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                }
                catch (final AxisFault axisFault)
                {
                    callback.receiveErrorslaveRelease(axisFault);
                }
            }
        });

        CallbackReceiver _callbackReceiver = null;
        if (this._operations[4].getMessageReceiver() == null && _operationClient.getOptions().isUseSeparateListener())
        {
            _callbackReceiver = new CallbackReceiver();
            this._operations[4].setMessageReceiver(_callbackReceiver);
        }

        _operationClient.execute(false);
    }

    public void callRelease(final Release request, final RigClientAsyncServiceCallbackHandler callback)
            throws RemoteException
    {
        final OperationClient _operationClient = this._serviceClient.createClient(this._operations[5].getName());
        _operationClient.getOptions().setAction("http://remotelabs.eng.uts.edu.au/rigclient/release");
        _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

        this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
        
        SOAPEnvelope env = null;
        final MessageContext _messageContext = new MessageContext();

        env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), request, this
                .optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "release")));
        
        this._serviceClient.addHeadersToEnvelope(env);
        _messageContext.setEnvelope(env);
        _operationClient.addMessageContext(_messageContext);

        _operationClient.setCallback(new AxisCallback()
        {
            public void onMessage(final MessageContext resultContext)
            {
                try
                {
                    final SOAPEnvelope resultEnv = resultContext.getEnvelope();

                    final Object object = RigClientAsyncServiceImpl.this.fromOM(resultEnv.getBody().getFirstElement(),
                            ReleaseResponse.class, RigClientAsyncServiceImpl.this.getEnvelopeNamespaces(resultEnv));
                    callback.releaseResponseCallback((ReleaseResponse) object);

                }
                catch (final AxisFault e)
                {
                    callback.releaseErrorCallback(e);
                }
            }

            public void onError(final Exception error)
            {
                if (error instanceof AxisFault)
                {
                    final AxisFault f = (AxisFault) error;
                    final OMElement faultElt = f.getDetail();
                    if (faultElt != null)
                    {
                        if (RigClientAsyncServiceImpl.this.faultExceptionNameMap.containsKey(faultElt.getQName()))
                        {                   
                            try
                            {
                                final String exceptionClassName = (String) RigClientAsyncServiceImpl.this.faultExceptionClassNameMap
                                        .get(faultElt.getQName());
                                final Class exceptionClass = Class.forName(exceptionClassName);
                                final Exception ex = (Exception) exceptionClass.newInstance();
                                
                                final String messageClassName = (String) RigClientAsyncServiceImpl.this.faultMessageMap
                                        .get(faultElt.getQName());
                                final Class messageClass = Class.forName(messageClassName);
                                final Object messageObject = RigClientAsyncServiceImpl.this.fromOM(faultElt, messageClass,
                                        null);
                                final Method m = exceptionClass.getMethod("setFaultMessage",
                                        new Class[] { messageClass });
                                m.invoke(ex, new Object[] { messageObject });

                                callback.releaseErrorCallback(new RemoteException(ex.getMessage(), ex));
                            }
                            catch (final ClassCastException e)
                            {       
                                callback.releaseErrorCallback(f);
                            }
                            catch (final ClassNotFoundException e)
                            {   
                                callback.releaseErrorCallback(f);
                            }
                            catch (final NoSuchMethodException e)
                            {   
                                callback.releaseErrorCallback(f);
                            }
                            catch (final InvocationTargetException e)
                            {   
                                callback.releaseErrorCallback(f);
                            }
                            catch (final IllegalAccessException e)
                            {   
                                callback.releaseErrorCallback(f);
                            }
                            catch (final InstantiationException e)
                            {   
                                callback.releaseErrorCallback(f);
                            }
                            catch (final AxisFault e)
                            {   
                                callback.releaseErrorCallback(f);
                            }
                        }
                        else
                        {
                            callback.releaseErrorCallback(f);
                        }
                    }
                    else
                    {
                        callback.releaseErrorCallback(f);
                    }
                }
                else
                {
                    callback.releaseErrorCallback(error);
                }
            }

            public void onFault(final MessageContext faultContext)
            {
                final AxisFault fault = Utils.getInboundFaultFromMessageContext(faultContext);
                this.onError(fault);
            }

            public void onComplete()
            {
                try
                {
                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                }
                catch (final AxisFault axisFault)
                {
                    callback.releaseErrorCallback(axisFault);
                }
            }
        });

        CallbackReceiver _callbackReceiver = null;
        if (this._operations[5].getMessageReceiver() == null && _operationClient.getOptions().isUseSeparateListener())
        {
            _callbackReceiver = new CallbackReceiver();
            this._operations[5].setMessageReceiver(_callbackReceiver);
        }

        _operationClient.execute(false);
    }

    public void startnotify(final Notify request, final RigClientAsyncServiceCallbackHandler callback)
            throws RemoteException
    {
        final OperationClient _operationClient = this._serviceClient.createClient(this._operations[6].getName());
        _operationClient.getOptions().setAction("http://remotelabs.eng.uts.edu.au/rigclient/notify");
        _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

        this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
        SOAPEnvelope env = null;
        final MessageContext _messageContext = new MessageContext();
        env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), request, this
                .optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "notify")));

        this._serviceClient.addHeadersToEnvelope(env);        
        _messageContext.setEnvelope(env);
        _operationClient.addMessageContext(_messageContext);

        _operationClient.setCallback(new AxisCallback()
        {
            public void onMessage(final MessageContext resultContext)
            {
                try
                {
                    final SOAPEnvelope resultEnv = resultContext.getEnvelope();
                    final Object object = RigClientAsyncServiceImpl.this.fromOM(resultEnv.getBody().getFirstElement(),
                            NotifyResponse.class, RigClientAsyncServiceImpl.this.getEnvelopeNamespaces(resultEnv));
                    callback.receiveResultnotify((NotifyResponse) object);

                }
                catch (final AxisFault e)
                {
                    callback.receiveErrornotify(e);
                }
            }

            public void onError(final Exception error)
            {
                if (error instanceof AxisFault)
                {
                    final AxisFault f = (AxisFault) error;
                    final OMElement faultElt = f.getDetail();
                    if (faultElt != null)
                    {
                        if (RigClientAsyncServiceImpl.this.faultExceptionNameMap.containsKey(faultElt.getQName()))
                        {
                            try
                            {
                                final String exceptionClassName = (String) RigClientAsyncServiceImpl.this.faultExceptionClassNameMap
                                        .get(faultElt.getQName());
                                final Class exceptionClass = Class.forName(exceptionClassName);
                                final Exception ex = (Exception) exceptionClass.newInstance();
                                
                                final String messageClassName = (String) RigClientAsyncServiceImpl.this.faultMessageMap
                                        .get(faultElt.getQName());
                                final Class messageClass = Class.forName(messageClassName);
                                final Object messageObject = RigClientAsyncServiceImpl.this.fromOM(faultElt, messageClass,
                                        null);
                                final Method m = exceptionClass.getMethod("setFaultMessage",
                                        new Class[] { messageClass });
                                m.invoke(ex, new Object[] { messageObject });

                                callback.receiveErrornotify(new RemoteException(ex.getMessage(), ex));
                            }
                            catch (final ClassCastException e)
                            {       
                                callback.receiveErrornotify(f);
                            }
                            catch (final ClassNotFoundException e)
                            {
                                callback.receiveErrornotify(f);
                            }
                            catch (final NoSuchMethodException e)
                            {   
                                callback.receiveErrornotify(f);
                            }
                            catch (final InvocationTargetException e)
                            {                                
                                callback.receiveErrornotify(f);
                            }
                            catch (final IllegalAccessException e)
                            {   
                                callback.receiveErrornotify(f);
                            }
                            catch (final InstantiationException e)
                            {   
                                callback.receiveErrornotify(f);
                            }
                            catch (final AxisFault e)
                            {   
                                callback.receiveErrornotify(f);
                            }
                        }
                        else
                        {
                            callback.receiveErrornotify(f);
                        }
                    }
                    else
                    {
                        callback.receiveErrornotify(f);
                    }
                }
                else
                {
                    callback.receiveErrornotify(error);
                }
            }

            public void onFault(final MessageContext faultContext)
            {
                final AxisFault fault = Utils.getInboundFaultFromMessageContext(faultContext);
                this.onError(fault);
            }

            public void onComplete()
            {
                try
                {
                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                }
                catch (final AxisFault axisFault)
                {
                    callback.receiveErrornotify(axisFault);
                }
            }
        });

        CallbackReceiver _callbackReceiver = null;
        if (this._operations[6].getMessageReceiver() == null && _operationClient.getOptions().isUseSeparateListener())
        {
            _callbackReceiver = new CallbackReceiver();
            this._operations[6].setMessageReceiver(_callbackReceiver);
        }

        _operationClient.execute(false);
    }

    public void startsetTestInterval(final SetTestInterval request, final RigClientAsyncServiceCallbackHandler callback)
            throws RemoteException
    {
        final OperationClient _operationClient = this._serviceClient.createClient(this._operations[7].getName());
        _operationClient.getOptions().setAction("http://remotelabs.eng.uts.edu.au/rigclient/testInterval");
        _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

        this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
        SOAPEnvelope env = null;
        final MessageContext _messageContext = new MessageContext();        

        env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), request,
                this.optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "setTestInterval")));
        this._serviceClient.addHeadersToEnvelope(env);
        _messageContext.setEnvelope(env);
        _operationClient.addMessageContext(_messageContext);

        _operationClient.setCallback(new AxisCallback()
        {
            public void onMessage(final MessageContext resultContext)
            {
                try
                {
                    final SOAPEnvelope resultEnv = resultContext.getEnvelope();

                    final Object object = RigClientAsyncServiceImpl.this.fromOM(resultEnv.getBody().getFirstElement(),
                            SetTestIntervalResponse.class, RigClientAsyncServiceImpl.this.getEnvelopeNamespaces(resultEnv));
                    callback.receiveResultsetTestInterval((SetTestIntervalResponse) object);

                }
                catch (final AxisFault e)
                {
                    callback.receiveErrorsetTestInterval(e);
                }
            }

            public void onError(final Exception error)
            {
                if (error instanceof AxisFault)
                {
                    final AxisFault f = (AxisFault) error;
                    final OMElement faultElt = f.getDetail();
                    if (faultElt != null)
                    {
                        if (RigClientAsyncServiceImpl.this.faultExceptionNameMap.containsKey(faultElt.getQName()))
                        {
                            
                            try
                            {
                                final String exceptionClassName = (String) RigClientAsyncServiceImpl.this.faultExceptionClassNameMap
                                        .get(faultElt.getQName());
                                final Class exceptionClass = Class.forName(exceptionClassName);
                                final Exception ex = (Exception) exceptionClass.newInstance();
                                
                                final String messageClassName = (String) RigClientAsyncServiceImpl.this.faultMessageMap
                                        .get(faultElt.getQName());
                                final Class messageClass = Class.forName(messageClassName);
                                final Object messageObject = RigClientAsyncServiceImpl.this.fromOM(faultElt, messageClass,
                                        null);
                                final Method m = exceptionClass.getMethod("setFaultMessage",
                                        new Class[] { messageClass });
                                m.invoke(ex, new Object[] { messageObject });

                                callback.receiveErrorsetTestInterval(new RemoteException(ex.getMessage(), ex));
                            }
                            catch (final ClassCastException e)
                            {
                                callback.receiveErrorsetTestInterval(f);
                            }
                            catch (final ClassNotFoundException e)
                            {   
                                callback.receiveErrorsetTestInterval(f);
                            }
                            catch (final NoSuchMethodException e)
                            {   
                                callback.receiveErrorsetTestInterval(f);
                            }
                            catch (final InvocationTargetException e)
                            {   
                                callback.receiveErrorsetTestInterval(f);
                            }
                            catch (final IllegalAccessException e)
                            {   
                                callback.receiveErrorsetTestInterval(f);
                            }
                            catch (final InstantiationException e)
                            {   
                                callback.receiveErrorsetTestInterval(f);
                            }
                            catch (final AxisFault e)
                            {   
                                callback.receiveErrorsetTestInterval(f);
                            }
                        }
                        else
                        {
                            callback.receiveErrorsetTestInterval(f);
                        }
                    }
                    else
                    {
                        callback.receiveErrorsetTestInterval(f);
                    }
                }
                else
                {
                    callback.receiveErrorsetTestInterval(error);
                }
            }

            public void onFault(final MessageContext faultContext)
            {
                final AxisFault fault = Utils.getInboundFaultFromMessageContext(faultContext);
                this.onError(fault);
            }

            public void onComplete()
            {
                try
                {
                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                }
                catch (final AxisFault axisFault)
                {
                    callback.receiveErrorsetTestInterval(axisFault);
                }
            }
        });

        CallbackReceiver _callbackReceiver = null;
        if (this._operations[7].getMessageReceiver() == null && _operationClient.getOptions().isUseSeparateListener())
        {
            _callbackReceiver = new CallbackReceiver();
            this._operations[7].setMessageReceiver(_callbackReceiver);
        }
        
        _operationClient.execute(false);
    }

    public void startsetMaintenance(final SetMaintenance request, final RigClientAsyncServiceCallbackHandler callback)
            throws RemoteException
    {
        final OperationClient _operationClient = this._serviceClient.createClient(this._operations[8].getName());
        _operationClient.getOptions().setAction("http://remotelabs.eng.uts.edu.au/rigclient/maintenance");
        _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

        this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
        
        SOAPEnvelope env = null;
        final MessageContext _messageContext = new MessageContext();
        env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), request,
                this.optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol",
                                "setMaintenance")));
        this._serviceClient.addHeadersToEnvelope(env);
        _messageContext.setEnvelope(env);
        _operationClient.addMessageContext(_messageContext);

        _operationClient.setCallback(new AxisCallback()
        {
            public void onMessage(final MessageContext resultContext)
            {
                try
                {
                    final SOAPEnvelope resultEnv = resultContext.getEnvelope();

                    final Object object = RigClientAsyncServiceImpl.this.fromOM(resultEnv.getBody().getFirstElement(),
                            SetMaintenanceResponse.class, RigClientAsyncServiceImpl.this.getEnvelopeNamespaces(resultEnv));
                    callback.receiveResultsetMaintenance((SetMaintenanceResponse) object);

                }
                catch (final AxisFault e)
                {
                    callback.receiveErrorsetMaintenance(e);
                }
            }

            public void onError(final Exception error)
            {
                if (error instanceof AxisFault)
                {
                    final AxisFault f = (AxisFault) error;
                    final OMElement faultElt = f.getDetail();
                    if (faultElt != null)
                    {
                        if (RigClientAsyncServiceImpl.this.faultExceptionNameMap.containsKey(faultElt.getQName()))
                        {                            
                            try
                            {
                                final String exceptionClassName = (String) RigClientAsyncServiceImpl.this.faultExceptionClassNameMap
                                        .get(faultElt.getQName());
                                final Class exceptionClass = Class.forName(exceptionClassName);
                                final Exception ex = (Exception) exceptionClass.newInstance();
                                
                                final String messageClassName = (String) RigClientAsyncServiceImpl.this.faultMessageMap
                                        .get(faultElt.getQName());
                                final Class messageClass = Class.forName(messageClassName);
                                final Object messageObject = RigClientAsyncServiceImpl.this.fromOM(faultElt, messageClass,
                                        null);
                                final Method m = exceptionClass.getMethod("setFaultMessage",
                                        new Class[] { messageClass });
                                m.invoke(ex, new Object[] { messageObject });

                                callback.receiveErrorsetMaintenance(new RemoteException(ex.getMessage(), ex));
                            }
                            catch (final ClassCastException e)
                            {       
                                callback.receiveErrorsetMaintenance(f);
                            }
                            catch (final ClassNotFoundException e)
                            {   
                                callback.receiveErrorsetMaintenance(f);
                            }
                            catch (final NoSuchMethodException e)
                            {   
                                callback.receiveErrorsetMaintenance(f);
                            }
                            catch (final InvocationTargetException e)
                            {   
                                callback.receiveErrorsetMaintenance(f);
                            }
                            catch (final IllegalAccessException e)
                            {   
                                callback.receiveErrorsetMaintenance(f);
                            }
                            catch (final InstantiationException e)
                            {   
                                callback.receiveErrorsetMaintenance(f);
                            }
                            catch (final AxisFault e)
                            {   
                                callback.receiveErrorsetMaintenance(f);
                            }
                        }
                        else
                        {
                            callback.receiveErrorsetMaintenance(f);
                        }
                    }
                    else
                    {
                        callback.receiveErrorsetMaintenance(f);
                    }
                }
                else
                {
                    callback.receiveErrorsetMaintenance(error);
                }
            }

            public void onFault(final MessageContext faultContext)
            {
                final AxisFault fault = Utils.getInboundFaultFromMessageContext(faultContext);
                this.onError(fault);
            }

            public void onComplete()
            {
                try
                {
                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                }
                catch (final AxisFault axisFault)
                {
                    callback.receiveErrorsetMaintenance(axisFault);
                }
            }
        });

        CallbackReceiver _callbackReceiver = null;
        if (this._operations[8].getMessageReceiver() == null && _operationClient.getOptions().isUseSeparateListener())
        {
            _callbackReceiver = new CallbackReceiver();
            this._operations[8].setMessageReceiver(_callbackReceiver);
        }

        _operationClient.execute(false);
    }


    public void startperformBatchControl(final PerformBatchControl request, final RigClientAsyncServiceCallbackHandler callback)
            throws RemoteException
    {
        final OperationClient _operationClient = this._serviceClient.createClient(this._operations[9].getName());
        _operationClient.getOptions().setAction("http://remotelabs.eng.uts.edu.au/rigclient/batch");
        _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

        this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
        
        SOAPEnvelope env = null;
        final MessageContext _messageContext = new MessageContext();

        env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()),
                request, this.optimizeContent(new QName(
                        "http://remotelabs.eng.uts.edu.au/rigclient/protocol", "performBatchControl")));
        
        this._serviceClient.addHeadersToEnvelope(env);        
        _messageContext.setEnvelope(env);
        
        _operationClient.addMessageContext(_messageContext);

        _operationClient.setCallback(new AxisCallback()
        {
            public void onMessage(final MessageContext resultContext)
            {
                try
                {
                    final SOAPEnvelope resultEnv = resultContext.getEnvelope();

                    final Object object = RigClientAsyncServiceImpl.this.fromOM(resultEnv.getBody().getFirstElement(),
                            PerformBatchControlResponse.class, RigClientAsyncServiceImpl.this
                                    .getEnvelopeNamespaces(resultEnv));
                    callback.receiveResultperformBatchControl((PerformBatchControlResponse) object);
                }
                catch (final AxisFault e)
                {
                    callback.receiveErrorperformBatchControl(e);
                }
            }

            public void onError(final Exception error)
            {
                if (error instanceof AxisFault)
                {
                    final AxisFault f = (AxisFault) error;
                    final OMElement faultElt = f.getDetail();
                    if (faultElt != null)
                    {
                        if (RigClientAsyncServiceImpl.this.faultExceptionNameMap.containsKey(faultElt.getQName()))
                        {
                            try
                            {
                                final String exceptionClassName = (String) RigClientAsyncServiceImpl.this.faultExceptionClassNameMap
                                        .get(faultElt.getQName());
                                final Class exceptionClass = Class.forName(exceptionClassName);
                                final Exception ex = (Exception) exceptionClass.newInstance();
                                
                                final String messageClassName = (String) RigClientAsyncServiceImpl.this.faultMessageMap
                                        .get(faultElt.getQName());
                                final Class messageClass = Class.forName(messageClassName);
                                final Object messageObject = RigClientAsyncServiceImpl.this.fromOM(faultElt, messageClass,
                                        null);
                                final Method m = exceptionClass.getMethod("setFaultMessage",
                                        new Class[] { messageClass });
                                m.invoke(ex, new Object[] { messageObject });

                                callback.receiveErrorperformBatchControl(new RemoteException(ex.getMessage(), ex));
                            }
                            catch (final ClassCastException e)
                            {       
                                callback.receiveErrorperformBatchControl(f);
                            }
                            catch (final ClassNotFoundException e)
                            {   
                                callback.receiveErrorperformBatchControl(f);
                            }
                            catch (final NoSuchMethodException e)
                            {   
                                callback.receiveErrorperformBatchControl(f);
                            }
                            catch (final InvocationTargetException e)
                            {   
                                callback.receiveErrorperformBatchControl(f);
                            }
                            catch (final IllegalAccessException e)
                            {   
                                callback.receiveErrorperformBatchControl(f);
                            }
                            catch (final InstantiationException e)
                            {   
                                callback.receiveErrorperformBatchControl(f);
                            }
                            catch (final AxisFault e)
                            {   
                                callback.receiveErrorperformBatchControl(f);
                            }
                        }
                        else
                        {
                            callback.receiveErrorperformBatchControl(f);
                        }
                    }
                    else
                    {
                        callback.receiveErrorperformBatchControl(f);
                    }
                }
                else
                {
                    callback.receiveErrorperformBatchControl(error);
                }
            }

            public void onFault(final MessageContext faultContext)
            {
                final AxisFault fault = Utils.getInboundFaultFromMessageContext(faultContext);
                this.onError(fault);
            }

            public void onComplete()
            {
                try
                {
                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                }
                catch (final AxisFault axisFault)
                {
                    callback.receiveErrorperformBatchControl(axisFault);
                }
            }
        });

        CallbackReceiver _callbackReceiver = null;
        if (this._operations[9].getMessageReceiver() == null && _operationClient.getOptions().isUseSeparateListener())
        {
            _callbackReceiver = new CallbackReceiver();
            this._operations[9].setMessageReceiver(_callbackReceiver);
        }

        _operationClient.execute(false);
    }

    public void startgetStatus(final GetStatus request, final RigClientAsyncServiceCallbackHandler callback)
            throws RemoteException
    {
        final OperationClient _operationClient = this._serviceClient.createClient(this._operations[10].getName());
        _operationClient.getOptions().setAction("http://remotelabs.eng.uts.edu.au/rigclient/getStatus");
        _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

        this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
        
        SOAPEnvelope env = null;
        final MessageContext _messageContext = new MessageContext();

        env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), request, this
                .optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "getStatus")));

        
        this._serviceClient.addHeadersToEnvelope(env);
        _messageContext.setEnvelope(env);

        _operationClient.addMessageContext(_messageContext);
        
        _operationClient.setCallback(new AxisCallback()
        {
            public void onMessage(final MessageContext resultContext)
            {
                try
                {
                    final SOAPEnvelope resultEnv = resultContext.getEnvelope();

                    final Object object = RigClientAsyncServiceImpl.this.fromOM(resultEnv.getBody().getFirstElement(),
                            GetStatusResponse.class, RigClientAsyncServiceImpl.this.getEnvelopeNamespaces(resultEnv));
                    callback.receiveResultgetStatus((GetStatusResponse) object);

                }
                catch (final AxisFault e)
                {
                    callback.receiveErrorgetStatus(e);
                }
            }

            public void onError(final Exception error)
            {
                if (error instanceof AxisFault)
                {
                    final AxisFault f = (AxisFault) error;
                    final OMElement faultElt = f.getDetail();
                    if (faultElt != null)
                    {
                        if (RigClientAsyncServiceImpl.this.faultExceptionNameMap.containsKey(faultElt.getQName()))
                        {                    
                            try
                            {
                                final String exceptionClassName = (String) RigClientAsyncServiceImpl.this.faultExceptionClassNameMap
                                        .get(faultElt.getQName());
                                final Class exceptionClass = Class.forName(exceptionClassName);
                                final Exception ex = (Exception) exceptionClass.newInstance();
                                
                                final String messageClassName = (String) RigClientAsyncServiceImpl.this.faultMessageMap
                                        .get(faultElt.getQName());
                                final Class messageClass = Class.forName(messageClassName);
                                final Object messageObject = RigClientAsyncServiceImpl.this.fromOM(faultElt, messageClass,
                                        null);
                                final Method m = exceptionClass.getMethod("setFaultMessage",
                                        new Class[] { messageClass });
                                m.invoke(ex, new Object[] { messageObject });

                                callback.receiveErrorgetStatus(new RemoteException(ex.getMessage(), ex));
                            }
                            catch (final ClassCastException e)
                            {                                
                                callback.receiveErrorgetStatus(f);
                            }
                            catch (final ClassNotFoundException e)
                            {   
                                callback.receiveErrorgetStatus(f);
                            }
                            catch (final NoSuchMethodException e)
                            {   
                                callback.receiveErrorgetStatus(f);
                            }
                            catch (final InvocationTargetException e)
                            {   
                                callback.receiveErrorgetStatus(f);
                            }
                            catch (final IllegalAccessException e)
                            {   
                                callback.receiveErrorgetStatus(f);
                            }
                            catch (final InstantiationException e)
                            {   
                                callback.receiveErrorgetStatus(f);
                            }
                            catch (final AxisFault e)
                            {   
                                callback.receiveErrorgetStatus(f);
                            }
                        }
                        else
                        {
                            callback.receiveErrorgetStatus(f);
                        }
                    }
                    else
                    {
                        callback.receiveErrorgetStatus(f);
                    }
                }
                else
                {
                    callback.receiveErrorgetStatus(error);
                }
            }

            public void onFault(final MessageContext faultContext)
            {
                final AxisFault fault = Utils.getInboundFaultFromMessageContext(faultContext);
                this.onError(fault);
            }

            public void onComplete()
            {
                try
                {
                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                }
                catch (final AxisFault axisFault)
                {
                    callback.receiveErrorgetStatus(axisFault);
                }
            }
        });

        CallbackReceiver _callbackReceiver = null;
        if (this._operations[10].getMessageReceiver() == null && _operationClient.getOptions().isUseSeparateListener())
        {
            _callbackReceiver = new CallbackReceiver();
            this._operations[10].setMessageReceiver(_callbackReceiver);
        }
        
        _operationClient.execute(false);
    }

    public void callAllocate(final Allocate request, final RigClientAsyncServiceCallbackHandler callback)
            throws RemoteException
    {
        final OperationClient _operationClient = this._serviceClient.createClient(this._operations[11].getName());
        _operationClient.getOptions().setAction("http://remotelabs.eng.uts.edu.au/rigclient/allocate");
        _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

        this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
   
        SOAPEnvelope env = null;
        final MessageContext _messageContext = new MessageContext();        

        env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), request, this
                .optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "allocate")));
        
        this._serviceClient.addHeadersToEnvelope(env);        
        _messageContext.setEnvelope(env);

        _operationClient.addMessageContext(_messageContext);

        _operationClient.setCallback(new AxisCallback()
        {
            public void onMessage(final MessageContext resultContext)
            {
                try
                {
                    final SOAPEnvelope resultEnv = resultContext.getEnvelope();

                    final Object object = RigClientAsyncServiceImpl.this.fromOM(resultEnv.getBody().getFirstElement(),
                            AllocateResponse.class, RigClientAsyncServiceImpl.this.getEnvelopeNamespaces(resultEnv));
                    callback.allocateResponseCallback((AllocateResponse) object);
                }
                catch (final AxisFault e)
                {
                    callback.allocateErrorCallback(e);
                }
            }

            public void onError(final Exception error)
            {
                if (error instanceof AxisFault)
                {
                    final AxisFault f = (AxisFault) error;
                    final OMElement faultElt = f.getDetail();
                    if (faultElt != null)
                    {
                        if (RigClientAsyncServiceImpl.this.faultExceptionNameMap.containsKey(faultElt.getQName()))
                        {           
                            try
                            {
                                final String exceptionClassName = (String) RigClientAsyncServiceImpl.this.faultExceptionClassNameMap
                                        .get(faultElt.getQName());
                                final Class exceptionClass = Class.forName(exceptionClassName);
                                final Exception ex = (Exception) exceptionClass.newInstance();
                                
                                final String messageClassName = (String) RigClientAsyncServiceImpl.this.faultMessageMap
                                        .get(faultElt.getQName());
                                final Class messageClass = Class.forName(messageClassName);
                                final Object messageObject = RigClientAsyncServiceImpl.this.fromOM(faultElt, messageClass,
                                        null);
                                final Method m = exceptionClass.getMethod("setFaultMessage",
                                        new Class[] { messageClass });
                                m.invoke(ex, new Object[] { messageObject });

                                callback.allocateErrorCallback(new RemoteException(ex.getMessage(), ex));
                            }
                            catch (final ClassCastException e)
                            {       
                                callback.allocateErrorCallback(f);
                            }
                            catch (final ClassNotFoundException e)
                            {   
                                callback.allocateErrorCallback(f);
                            }
                            catch (final NoSuchMethodException e)
                            {   
                                callback.allocateErrorCallback(f);
                            }
                            catch (final InvocationTargetException e)
                            {   
                                callback.allocateErrorCallback(f);
                            }
                            catch (final IllegalAccessException e)
                            {   
                                callback.allocateErrorCallback(f);
                            }
                            catch (final InstantiationException e)
                            {   
                                callback.allocateErrorCallback(f);
                            }
                            catch (final AxisFault e)
                            {   
                                callback.allocateErrorCallback(f);
                            }
                        }
                        else
                        {
                            callback.allocateErrorCallback(f);
                        }
                    }
                    else
                    {
                        callback.allocateErrorCallback(f);
                    }
                }
                else
                {
                    callback.allocateErrorCallback(error);
                }
            }

            public void onFault(final MessageContext faultContext)
            {
                final AxisFault fault = Utils.getInboundFaultFromMessageContext(faultContext);
                this.onError(fault);
            }

            public void onComplete()
            {
                try
                {
                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                }
                catch (final AxisFault axisFault)
                {
                    callback.allocateErrorCallback(axisFault);
                }
            }
        });

        CallbackReceiver _callbackReceiver = null;
        if (this._operations[11].getMessageReceiver() == null && _operationClient.getOptions().isUseSeparateListener())
        {
            _callbackReceiver = new CallbackReceiver();
            this._operations[11].setMessageReceiver(_callbackReceiver);
        }
        
        _operationClient.execute(false);
    }

    public void startslaveAllocate(final SlaveAllocate request, final RigClientAsyncServiceCallbackHandler callback)
            throws RemoteException
    {
        final OperationClient _operationClient = this._serviceClient.createClient(this._operations[12].getName());
        _operationClient.getOptions().setAction("http://remotelabs.eng.uts.edu.au/rigclient/slaveAllocate");
        _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

        this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
        
        SOAPEnvelope env = null;
        final MessageContext _messageContext = new MessageContext();

        env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), request, this
                .optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "slaveAllocate")));

        this._serviceClient.addHeadersToEnvelope(env);
        
        _messageContext.setEnvelope(env);
        
        _operationClient.addMessageContext(_messageContext);

        _operationClient.setCallback(new AxisCallback()
        {
            public void onMessage(final MessageContext resultContext)
            {
                try
                {
                    final SOAPEnvelope resultEnv = resultContext.getEnvelope();
                    final Object object = RigClientAsyncServiceImpl.this.fromOM(resultEnv.getBody().getFirstElement(),
                            SlaveAllocateResponse.class, RigClientAsyncServiceImpl.this.getEnvelopeNamespaces(resultEnv));
                    callback.receiveResultslaveAllocate((SlaveAllocateResponse) object);
                }
                catch (final AxisFault e)
                {
                    callback.receiveErrorslaveAllocate(e);
                }
            }

            public void onError(final Exception error)
            {
                if (error instanceof AxisFault)
                {
                    final AxisFault f = (AxisFault) error;
                    final OMElement faultElt = f.getDetail();
                    if (faultElt != null)
                    {
                        if (RigClientAsyncServiceImpl.this.faultExceptionNameMap.containsKey(faultElt.getQName()))
                        {
                            try
                            {
                                final String exceptionClassName = (String) RigClientAsyncServiceImpl.this.faultExceptionClassNameMap
                                        .get(faultElt.getQName());
                                final Class exceptionClass = Class.forName(exceptionClassName);
                                final Exception ex = (Exception) exceptionClass.newInstance();
                                
                                final String messageClassName = (String) RigClientAsyncServiceImpl.this.faultMessageMap
                                        .get(faultElt.getQName());
                                final Class messageClass = Class.forName(messageClassName);
                                final Object messageObject = RigClientAsyncServiceImpl.this.fromOM(faultElt, messageClass,
                                        null);
                                final Method m = exceptionClass.getMethod("setFaultMessage",
                                        new Class[] { messageClass });
                                m.invoke(ex, new Object[] { messageObject });

                                callback.receiveErrorslaveAllocate(new RemoteException(ex.getMessage(), ex));
                            }
                            catch (final ClassCastException e)
                            {       
                                callback.receiveErrorslaveAllocate(f);
                            }
                            catch (final ClassNotFoundException e)
                            {   
                                callback.receiveErrorslaveAllocate(f);
                            }
                            catch (final NoSuchMethodException e)
                            {   
                                callback.receiveErrorslaveAllocate(f);
                            }
                            catch (final InvocationTargetException e)
                            {   
                                callback.receiveErrorslaveAllocate(f);
                            }
                            catch (final IllegalAccessException e)
                            {   
                                callback.receiveErrorslaveAllocate(f);
                            }
                            catch (final InstantiationException e)
                            {   
                                callback.receiveErrorslaveAllocate(f);
                            }
                            catch (final AxisFault e)
                            {   
                                callback.receiveErrorslaveAllocate(f);
                            }
                        }
                        else
                        {
                            callback.receiveErrorslaveAllocate(f);
                        }
                    }
                    else
                    {
                        callback.receiveErrorslaveAllocate(f);
                    }
                }
                else
                {
                    callback.receiveErrorslaveAllocate(error);
                }
            }

            public void onFault(final MessageContext faultContext)
            {
                final AxisFault fault = Utils.getInboundFaultFromMessageContext(faultContext);
                this.onError(fault);
            }

            public void onComplete()
            {
                try
                {
                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                }
                catch (final AxisFault axisFault)
                {
                    callback.receiveErrorslaveAllocate(axisFault);
                }
            }
        });

        CallbackReceiver _callbackReceiver = null;
        if (this._operations[12].getMessageReceiver() == null && _operationClient.getOptions().isUseSeparateListener())
        {
            _callbackReceiver = new CallbackReceiver();
            this._operations[12].setMessageReceiver(_callbackReceiver);
        }

        _operationClient.execute(false);
    }

    public void startabortBatchControl(final AbortBatchControl request, final RigClientAsyncServiceCallbackHandler callback)
            throws RemoteException
    {
        final OperationClient _operationClient = this._serviceClient.createClient(this._operations[13].getName());
        _operationClient.getOptions().setAction("http://remotelabs.eng.uts.edu.au/rigclient/batchAbort");
        _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

        this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");
        
        SOAPEnvelope env = null;
        final MessageContext _messageContext = new MessageContext();

        env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), request,
                this.optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol",
                        "abortBatchControl")));
        
        this._serviceClient.addHeadersToEnvelope(env);
        _messageContext.setEnvelope(env);

        _operationClient.addMessageContext(_messageContext);

        _operationClient.setCallback(new AxisCallback()
        {
            public void onMessage(final MessageContext resultContext)
            {
                try
                {
                    final SOAPEnvelope resultEnv = resultContext.getEnvelope();

                    final Object object = RigClientAsyncServiceImpl.this
                            .fromOM(resultEnv.getBody().getFirstElement(), AbortBatchControlResponse.class,
                                    RigClientAsyncServiceImpl.this.getEnvelopeNamespaces(resultEnv));
                    callback.receiveResultabortBatchControl((AbortBatchControlResponse) object);

                }
                catch (final AxisFault e)
                {
                    callback.receiveErrorabortBatchControl(e);
                }
            }

            public void onError(final Exception error)
            {
                if (error instanceof AxisFault)
                {
                    final AxisFault f = (AxisFault) error;
                    final OMElement faultElt = f.getDetail();
                    if (faultElt != null)
                    {
                        if (RigClientAsyncServiceImpl.this.faultExceptionNameMap.containsKey(faultElt.getQName()))
                        {
                            try
                            {
                                final String exceptionClassName = (String) RigClientAsyncServiceImpl.this.faultExceptionClassNameMap
                                        .get(faultElt.getQName());
                                final Class exceptionClass = Class.forName(exceptionClassName);
                                final Exception ex = (Exception) exceptionClass.newInstance();
                                
                                final String messageClassName = (String) RigClientAsyncServiceImpl.this.faultMessageMap
                                        .get(faultElt.getQName());
                                final Class messageClass = Class.forName(messageClassName);
                                final Object messageObject = RigClientAsyncServiceImpl.this.fromOM(faultElt, messageClass,
                                        null);
                                final Method m = exceptionClass.getMethod("setFaultMessage",
                                        new Class[] { messageClass });
                                m.invoke(ex, new Object[] { messageObject });

                                callback.receiveErrorabortBatchControl(new RemoteException(ex.getMessage(), ex));
                            }
                            catch (final ClassCastException e)
                            {
                                callback.receiveErrorabortBatchControl(f);
                            }
                            catch (final ClassNotFoundException e)
                            {   
                                callback.receiveErrorabortBatchControl(f);
                            }
                            catch (final NoSuchMethodException e)
                            {   
                                callback.receiveErrorabortBatchControl(f);
                            }
                            catch (final InvocationTargetException e)
                            {   
                                callback.receiveErrorabortBatchControl(f);
                            }
                            catch (final IllegalAccessException e)
                            {   
                                callback.receiveErrorabortBatchControl(f);
                            }
                            catch (final InstantiationException e)
                            {   
                                callback.receiveErrorabortBatchControl(f);
                            }
                            catch (final AxisFault e)
                            {   
                                callback.receiveErrorabortBatchControl(f);
                            }
                        }
                        else
                        {
                            callback.receiveErrorabortBatchControl(f);
                        }
                    }
                    else
                    {
                        callback.receiveErrorabortBatchControl(f);
                    }
                }
                else
                {
                    callback.receiveErrorabortBatchControl(error);
                }
            }

            public void onFault(final MessageContext faultContext)
            {
                final AxisFault fault = Utils.getInboundFaultFromMessageContext(faultContext);
                this.onError(fault);
            }

            public void onComplete()
            {
                try
                {
                    _messageContext.getTransportOut().getSender().cleanup(_messageContext);
                }
                catch (final AxisFault axisFault)
                {
                    callback.receiveErrorabortBatchControl(axisFault);
                }
            }
        });

        CallbackReceiver _callbackReceiver = null;
        if (this._operations[13].getMessageReceiver() == null && _operationClient.getOptions().isUseSeparateListener())
        {
            _callbackReceiver = new CallbackReceiver();
            this._operations[13].setMessageReceiver(_callbackReceiver);
        }

        _operationClient.execute(false);
    }

    /**
     * A utility method that copies the namepaces from the SOAPEnvelope
     */
    private Map<String, String> getEnvelopeNamespaces(final SOAPEnvelope env)
    {
        final Map<String, String> returnMap = new HashMap<String, String>();
        final Iterator namespaceIterator = env.getAllDeclaredNamespaces();
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

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final PerformPrimitiveControl param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(PerformPrimitiveControl.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final GetBatchControlStatus param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(GetBatchControlStatus.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }

    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final IsActivityDetectable param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(IsActivityDetectable.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }

    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final GetAttribute param, final boolean optimizeContent)
            throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(GetAttribute.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final SlaveRelease param, final boolean optimizeContent)
            throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(SlaveRelease.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final Release param, final boolean optimizeContent)
            throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(Release.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final Notify param, final boolean optimizeContent)
            throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(Notify.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final SetTestInterval param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(SetTestInterval.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final SetMaintenance param, final boolean optimizeContent)
            throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(SetMaintenance.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final PerformBatchControl param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(PerformBatchControl.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final GetStatus param, final boolean optimizeContent)
            throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(GetStatus.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final Allocate param, final boolean optimizeContent)
            throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(Allocate.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final SlaveAllocate param, final boolean optimizeContent)
            throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(SlaveAllocate.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final AbortBatchControl param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(AbortBatchControl.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private Object fromOM(final OMElement param, final Class type, final Map<String, String> extraNamespaces)
            throws AxisFault
    {
        try
        {
            if (PerformPrimitiveControl.class.equals(type))
            {
                return PerformPrimitiveControl.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (PerformPrimitiveControlResponse.class.equals(type))
            {
                return PerformPrimitiveControlResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetBatchControlStatus.class.equals(type))
            {
                return GetBatchControlStatus.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetBatchControlStatusResponse.class.equals(type))
            {
                return GetBatchControlStatusResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (IsActivityDetectable.class.equals(type))
            {
                return IsActivityDetectable.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (IsActivityDetectableResponse.class.equals(type))
            {
                return IsActivityDetectableResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetAttribute.class.equals(type))
            {
                return GetAttribute.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetAttributeResponse.class.equals(type))
            {
                return GetAttributeResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (SlaveRelease.class.equals(type))
            {
                return SlaveRelease.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (SlaveReleaseResponse.class.equals(type))
            {
                return SlaveReleaseResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (Release.class.equals(type))
            {
                return Release.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (ReleaseResponse.class.equals(type))
            {
                return ReleaseResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (Notify.class.equals(type))
            {
                return Notify.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (NotifyResponse.class.equals(type))
            {
                return NotifyResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (SetTestInterval.class.equals(type))
            {
                return SetTestInterval.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (SetTestIntervalResponse.class.equals(type))
            {
                return SetTestIntervalResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (SetMaintenance.class.equals(type))
            {
                return SetMaintenance.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (SetMaintenanceResponse.class.equals(type))
            {
                return SetMaintenanceResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (PerformBatchControl.class.equals(type))
            {
                return PerformBatchControl.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (PerformBatchControlResponse.class.equals(type))
            {
                return PerformBatchControlResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetStatus.class.equals(type))
            {
                return GetStatus.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetStatusResponse.class.equals(type))
            {
                return GetStatusResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (Allocate.class.equals(type))
            {
                return Allocate.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (AllocateResponse.class.equals(type))
            {
                return AllocateResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (SlaveAllocate.class.equals(type))
            {
                return SlaveAllocate.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (SlaveAllocateResponse.class.equals(type))
            {
                return SlaveAllocateResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (AbortBatchControl.class.equals(type))
            {
                return AbortBatchControl.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (AbortBatchControlResponse.class.equals(type))
            {
                return AbortBatchControlResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }
        }
        catch (final Exception e)
        {
            throw AxisFault.makeFault(e);
        }
        return null;
    }
}
