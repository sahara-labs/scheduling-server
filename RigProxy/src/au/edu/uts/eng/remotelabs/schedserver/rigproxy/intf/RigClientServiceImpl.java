/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
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
 * @author Michael Diponio (mdiponio)
 * @date 5th April 2010
 */

package au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf;

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
import org.apache.axis2.client.Options;
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

import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.AbortBatchControl;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.AbortBatchControlResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.Allocate;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.AllocateResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.GetAttribute;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.GetAttributeResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.GetBatchControlStatus;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.GetBatchControlStatusResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.GetStatus;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.GetStatusResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.IsActivityDetectable;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.IsActivityDetectableResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.Notify;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.NotifyResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.PerformBatchControl;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.PerformBatchControlResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.PerformPrimitiveControl;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.PerformPrimitiveControlResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.Release;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.ReleaseResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.SetMaintenance;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.SetMaintenanceResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.SetTestInterval;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.SetTestIntervalResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.SlaveAllocate;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.SlaveAllocateResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.SlaveRelease;
import au.edu.uts.eng.remotelabs.schedserver.rigproxy.intf.types.SlaveReleaseResponse;

/**
 * RigClient service implementation.
 */
public class RigClientServiceImpl extends Stub
{
    protected AxisOperation[] _operations;

    private final HashMap<?, ?> faultExceptionNameMap = new HashMap<Object, Object>();
    private final HashMap<?, ?> faultExceptionClassNameMap = new HashMap<Object, Object>();
    private final HashMap<?, ?> faultMessageMap = new HashMap<Object, Object>();

    private static int counter = 0;
    
    private final QName[] opNameArray = null;

    public RigClientServiceImpl(final ConfigurationContext configurationContext, final String targetEndpoint)
            throws AxisFault
    {
        this(configurationContext, targetEndpoint, false);
    }

    public RigClientServiceImpl(ConfigurationContext configurationContext, final String targetEndpoint,
            final boolean useSeparateListener) throws AxisFault
    {
        this.populateAxisService();

        this._serviceClient = new ServiceClient(configurationContext, this._service);
        configurationContext = this._serviceClient.getServiceContext().getConfigurationContext();
        
        Options opts = this._serviceClient.getOptions();
        opts.setTo(new EndpointReference(targetEndpoint));
        opts.setUseSeparateListener(useSeparateListener);
    }

    public RigClientServiceImpl(final ConfigurationContext configurationContext) throws AxisFault
    {
        this(configurationContext, "http://remotelabs.eng.uts.edu.au:8080/rigclient");
    }

    public RigClientServiceImpl() throws AxisFault
    {
        this("http://remotelabs.eng.uts.edu.au:8080/rigclient");
    }

    public RigClientServiceImpl(final String targetEndpoint) throws AxisFault
    {
        this(null, targetEndpoint);
    }
    
    public RigClientServiceImpl(final String targetEndpoint, int tm) throws AxisFault
    {
        this.populateAxisService();

        this._serviceClient = new ServiceClient(null, this._service);
        this._serviceClient.getServiceContext().getConfigurationContext();
        
        Options opts = this._serviceClient.getOptions();
        opts.setTo(new EndpointReference(targetEndpoint));
        opts.setUseSeparateListener(false);
        opts.setTimeOutInMilliSeconds(tm );
    }

    private static synchronized String getUniqueSuffix()
    {
        if (RigClientServiceImpl.counter > 99999)
        {
            RigClientServiceImpl.counter = 0;
        }
        RigClientServiceImpl.counter = RigClientServiceImpl.counter + 1;
        return Long.toString(System.currentTimeMillis()) + "_" + RigClientServiceImpl.counter;
    }

    private void populateAxisService() throws AxisFault
    {
        this._service = new AxisService("RigClientService" + RigClientServiceImpl.getUniqueSuffix());
        this.addAnonymousOperations();

        AxisOperation __operation;
        this._operations = new AxisOperation[14];
        __operation = new OutInAxisOperation();

        __operation
                .setName(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "performPrimitiveControl"));
        this._service.addOperation(__operation);
        this._operations[0] = __operation;

        __operation = new OutInAxisOperation();
        __operation.setName(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "getBatchControlStatus"));
        this._service.addOperation(__operation);
        this._operations[1] = __operation;

        __operation = new OutInAxisOperation();
        __operation.setName(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "isActivityDetectable"));
        this._service.addOperation(__operation);
        this._operations[2] = __operation;

        __operation = new OutInAxisOperation();
        __operation.setName(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "getAttribute"));
        this._service.addOperation(__operation);
        this._operations[3] = __operation;

        __operation = new OutInAxisOperation();
        __operation.setName(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "slaveRelease"));
        this._service.addOperation(__operation);
        this._operations[4] = __operation;

        __operation = new OutInAxisOperation();
        __operation.setName(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "release"));
        this._service.addOperation(__operation);
        this._operations[5] = __operation;

        __operation = new OutInAxisOperation();
        __operation.setName(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "notify"));
        this._service.addOperation(__operation);
        this._operations[6] = __operation;

        __operation = new OutInAxisOperation();
        __operation.setName(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "setTestInterval"));
        this._service.addOperation(__operation);
        this._operations[7] = __operation;

        __operation = new OutInAxisOperation();
        __operation.setName(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "setMaintenance"));
        this._service.addOperation(__operation);
        this._operations[8] = __operation;

        __operation = new OutInAxisOperation();
        __operation.setName(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "performBatchControl"));
        this._service.addOperation(__operation);
        this._operations[9] = __operation;

        __operation = new OutInAxisOperation();
        __operation.setName(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "getStatus"));
        this._service.addOperation(__operation);
        this._operations[10] = __operation;

        __operation = new OutInAxisOperation();
        __operation.setName(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "allocate"));
        this._service.addOperation(__operation);
        this._operations[11] = __operation;

        __operation = new OutInAxisOperation();
        __operation.setName(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "slaveAllocate"));
        this._service.addOperation(__operation);
        this._operations[12] = __operation;

        __operation = new OutInAxisOperation();
        __operation.setName(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "abortBatchControl"));
        this._service.addOperation(__operation);
        this._operations[13] = __operation;
    }

    public PerformPrimitiveControlResponse performPrimitiveControl(final PerformPrimitiveControl request)
            throws RemoteException
    {
        MessageContext _messageContext = null;
        try
        {
            final OperationClient _operationClient = this._serviceClient.createClient(this._operations[0].getName());
            _operationClient.getOptions().setAction("http://remotelabs.eng.uts.edu.au/rigclient/primitiveControl");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            _messageContext = new MessageContext();

            final SOAPEnvelope env = this.toEnvelope(
                    Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), request, this
                            .optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol",
                                    "performPrimitiveControl")));
            this._serviceClient.addHeadersToEnvelope(env);

            _messageContext.setEnvelope(env);
            _operationClient.addMessageContext(_messageContext);

            _operationClient.execute(true);

            final MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            final SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            final Object object = this.fromOM(_returnEnv.getBody().getFirstElement(),
                    PerformPrimitiveControlResponse.class, this.getEnvelopeNamespaces(_returnEnv));
            return (PerformPrimitiveControlResponse) object;
        }
        catch (final AxisFault f)
        {
            final OMElement faultElt = f.getDetail();
            if (faultElt != null)
            {
                if (this.faultExceptionNameMap.containsKey(faultElt.getQName()))
                {
                    try
                    {
                        final String exceptionClassName = (String) this.faultExceptionClassNameMap.get(faultElt
                                .getQName());
                        final Class<?> exceptionClass = Class.forName(exceptionClassName);
                        final Exception ex = (Exception) exceptionClass.newInstance();
                        final String messageClassName = (String) this.faultMessageMap.get(faultElt.getQName());
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
            _messageContext.getTransportOut().getSender().cleanup(_messageContext);
        }
    }

    public GetBatchControlStatusResponse getBatchControlStatus(final GetBatchControlStatus request)
            throws RemoteException
    {
        MessageContext _messageContext = null;
        try
        {
            final OperationClient _operationClient = this._serviceClient.createClient(this._operations[1].getName());
            _operationClient.getOptions().setAction("http://remotelabs.eng.uts.edu.au/rigclient/batchStatus");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            _messageContext = new MessageContext();

            final SOAPEnvelope env = this.toEnvelope(
                    Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), request, this
                            .optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol",
                                    "getBatchControlStatus")));
            this._serviceClient.addHeadersToEnvelope(env);
            _messageContext.setEnvelope(env);
            _operationClient.addMessageContext(_messageContext);
            _operationClient.execute(true);

            final MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            final SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
            final Object object = this.fromOM(_returnEnv.getBody().getFirstElement(),
                    GetBatchControlStatusResponse.class, this.getEnvelopeNamespaces(_returnEnv));

            return (GetBatchControlStatusResponse) object;
        }
        catch (final AxisFault f)
        {
            final OMElement faultElt = f.getDetail();
            if (faultElt != null)
            {
                if (this.faultExceptionNameMap.containsKey(faultElt.getQName()))
                {
                    try
                    {
                        final String exceptionClassName = (String) this.faultExceptionClassNameMap.get(faultElt
                                .getQName());
                        final Class<?> exceptionClass = Class.forName(exceptionClassName);
                        final Exception ex = (Exception) exceptionClass.newInstance();

                        final String messageClassName = (String) this.faultMessageMap.get(faultElt.getQName());
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
            _messageContext.getTransportOut().getSender().cleanup(_messageContext);
        }
    }

    public IsActivityDetectableResponse isActivityDetectable(final IsActivityDetectable request)
            throws RemoteException
    {
        MessageContext _messageContext = null;
        try
        {
            final OperationClient _operationClient = this._serviceClient.createClient(this._operations[2].getName());
            _operationClient.getOptions().setAction("http://remotelabs.eng.uts.edu.au/rigclient/isActivityDetectable");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);
            this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            _messageContext = new MessageContext();
            SOAPEnvelope env = null;
            env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()),
                    request, this.optimizeContent(new QName(
                            "http://remotelabs.eng.uts.edu.au/rigclient/protocol", "isActivityDetectable")));

            this._serviceClient.addHeadersToEnvelope(env);
            _messageContext.setEnvelope(env);
            _operationClient.addMessageContext(_messageContext);
            _operationClient.execute(true);

            final MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            final SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            final Object object = this.fromOM(_returnEnv.getBody().getFirstElement(),
                    IsActivityDetectableResponse.class, this.getEnvelopeNamespaces(_returnEnv));
            return (IsActivityDetectableResponse) object;
        }
        catch (final AxisFault f)
        {
            final OMElement faultElt = f.getDetail();
            if (faultElt != null)
            {
                if (this.faultExceptionNameMap.containsKey(faultElt.getQName()))
                {
                    try
                    {
                        final String exceptionClassName = (String) this.faultExceptionClassNameMap.get(faultElt
                                .getQName());
                        final Class<?> exceptionClass = Class.forName(exceptionClassName);
                        final Exception ex = (Exception) exceptionClass.newInstance();
                        final String messageClassName = (String) this.faultMessageMap.get(faultElt.getQName());
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
            _messageContext.getTransportOut().getSender().cleanup(_messageContext);
        }
    }

    public GetAttributeResponse getAttribute(final GetAttribute request) throws RemoteException
    {
        MessageContext _messageContext = null;
        try
        {
            final OperationClient _operationClient = this._serviceClient.createClient(this._operations[3].getName());
            _operationClient.getOptions().setAction("http://remotelabs.eng.uts.edu.au/rigclient/getAttribute");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            _messageContext = new MessageContext();
            SOAPEnvelope env = null;
            env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), request,
                    this.optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol",
                            "getAttribute")));

            this._serviceClient.addHeadersToEnvelope(env);
            _messageContext.setEnvelope(env);
            _operationClient.addMessageContext(_messageContext);
            _operationClient.execute(true);

            final MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            final SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            final Object object = this.fromOM(_returnEnv.getBody().getFirstElement(), GetAttributeResponse.class, this
                    .getEnvelopeNamespaces(_returnEnv));

            return (GetAttributeResponse) object;
        }
        catch (final AxisFault f)
        {
            final OMElement faultElt = f.getDetail();
            if (faultElt != null)
            {
                if (this.faultExceptionNameMap.containsKey(faultElt.getQName()))
                {
                    try
                    {
                        final String exceptionClassName = (String) this.faultExceptionClassNameMap.get(faultElt
                                .getQName());
                        final Class<?> exceptionClass = Class.forName(exceptionClassName);
                        final Exception ex = (Exception) exceptionClass.newInstance();

                        final String messageClassName = (String) this.faultMessageMap.get(faultElt.getQName());
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
            _messageContext.getTransportOut().getSender().cleanup(_messageContext);
        }
    }

    public SlaveReleaseResponse slaveRelease(final SlaveRelease request) throws RemoteException
    {
        MessageContext _messageContext = null;
        try
        {
            final OperationClient _operationClient = this._serviceClient.createClient(this._operations[4].getName());
            _operationClient.getOptions().setAction("http://remotelabs.eng.uts.edu.au/rigclient/slaveRelease");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            _messageContext = new MessageContext();
            SOAPEnvelope env = null;
            env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), request,
                    this.optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol",
                            "slaveRelease")));

            this._serviceClient.addHeadersToEnvelope(env);

            _messageContext.setEnvelope(env);
            _operationClient.addMessageContext(_messageContext);
            _operationClient.execute(true);

            final MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            final SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            final Object object = this.fromOM(_returnEnv.getBody().getFirstElement(), SlaveReleaseResponse.class, this
                    .getEnvelopeNamespaces(_returnEnv));

            return (SlaveReleaseResponse) object;
        }
        catch (final AxisFault f)
        {
            final OMElement faultElt = f.getDetail();
            if (faultElt != null)
            {
                if (this.faultExceptionNameMap.containsKey(faultElt.getQName()))
                {
                    try
                    {
                        final String exceptionClassName = (String) this.faultExceptionClassNameMap.get(faultElt
                                .getQName());
                        final Class<?> exceptionClass = Class.forName(exceptionClassName);
                        final Exception ex = (Exception) exceptionClass.newInstance();

                        final String messageClassName = (String) this.faultMessageMap.get(faultElt.getQName());
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
            _messageContext.getTransportOut().getSender().cleanup(_messageContext);
        }
    }


    public ReleaseResponse release(final Release request) throws RemoteException
    {
        MessageContext _messageContext = null;
        try
        {
            final OperationClient _operationClient = this._serviceClient.createClient(this._operations[5].getName());
            _operationClient.getOptions().setAction("http://remotelabs.eng.uts.edu.au/rigclient/release");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            _messageContext = new MessageContext();
            SOAPEnvelope env = null;

            env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), request, this
                    .optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "release")));

            this._serviceClient.addHeadersToEnvelope(env);
            _messageContext.setEnvelope(env);
            _operationClient.addMessageContext(_messageContext);
            _operationClient.execute(true);

            final MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            final SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            final Object object = this.fromOM(_returnEnv.getBody().getFirstElement(), ReleaseResponse.class, this
                    .getEnvelopeNamespaces(_returnEnv));

            return (ReleaseResponse) object;

        }
        catch (final AxisFault f)
        {
            final OMElement faultElt = f.getDetail();
            if (faultElt != null)
            {
                if (this.faultExceptionNameMap.containsKey(faultElt.getQName()))
                {
                    try
                    {
                        final String exceptionClassName = (String) this.faultExceptionClassNameMap.get(faultElt
                                .getQName());
                        final Class<?> exceptionClass = Class.forName(exceptionClassName);
                        final Exception ex = (Exception) exceptionClass.newInstance();

                        final String messageClassName = (String) this.faultMessageMap.get(faultElt.getQName());
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
            _messageContext.getTransportOut().getSender().cleanup(_messageContext);
        }
    }

    public NotifyResponse notify(final Notify request) throws RemoteException
    {
        MessageContext _messageContext = null;
        try
        {
            final OperationClient _operationClient = this._serviceClient.createClient(this._operations[6].getName());
            _operationClient.getOptions().setAction("http://remotelabs.eng.uts.edu.au/rigclient/notify");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            _messageContext = new MessageContext();
            SOAPEnvelope env = null;
            env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), request, this
                    .optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "notify")));

            this._serviceClient.addHeadersToEnvelope(env);

            _messageContext.setEnvelope(env);
            _operationClient.addMessageContext(_messageContext);
            _operationClient.execute(true);

            final MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            final SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            final Object object = this.fromOM(_returnEnv.getBody().getFirstElement(), NotifyResponse.class, this
                    .getEnvelopeNamespaces(_returnEnv));

            return (NotifyResponse) object;
        }
        catch (final AxisFault f)
        {
            final OMElement faultElt = f.getDetail();
            if (faultElt != null)
            {
                if (this.faultExceptionNameMap.containsKey(faultElt.getQName()))
                {
                    try
                    {
                        final String exceptionClassName = (String) this.faultExceptionClassNameMap.get(faultElt
                                .getQName());
                        final Class<?> exceptionClass = Class.forName(exceptionClassName);
                        final Exception ex = (Exception) exceptionClass.newInstance();

                        final String messageClassName = (String) this.faultMessageMap.get(faultElt.getQName());
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
            _messageContext.getTransportOut().getSender().cleanup(_messageContext);
        }
    }

    public SetTestIntervalResponse setTestInterval(final SetTestInterval request) throws RemoteException
    {
        MessageContext _messageContext = null;
        try
        {
            final OperationClient _operationClient = this._serviceClient.createClient(this._operations[7].getName());
            _operationClient.getOptions().setAction("http://remotelabs.eng.uts.edu.au/rigclient/testInterval");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            _messageContext = new MessageContext();
            
            SOAPEnvelope env = null;
            env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), request,
                    this.optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol",
                            "setTestInterval")));

            this._serviceClient.addHeadersToEnvelope(env);

            _messageContext.setEnvelope(env);
            _operationClient.addMessageContext(_messageContext);
            _operationClient.execute(true);

            final MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            final SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();
            final Object object = this.fromOM(_returnEnv.getBody().getFirstElement(), SetTestIntervalResponse.class,
                    this.getEnvelopeNamespaces(_returnEnv));

            return (SetTestIntervalResponse) object;
        }
        catch (final AxisFault f)
        {
            final OMElement faultElt = f.getDetail();
            if (faultElt != null)
            {
                if (this.faultExceptionNameMap.containsKey(faultElt.getQName()))
                {
                    try
                    {
                        final String exceptionClassName = (String) this.faultExceptionClassNameMap.get(faultElt
                                .getQName());
                        final Class<?> exceptionClass = Class.forName(exceptionClassName);
                        final Exception ex = (Exception) exceptionClass.newInstance();

                        final String messageClassName = (String) this.faultMessageMap.get(faultElt.getQName());
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
            _messageContext.getTransportOut().getSender().cleanup(_messageContext);
        }
    }

    public SetMaintenanceResponse setMaintenance(final SetMaintenance request) throws RemoteException
    {
        MessageContext _messageContext = null;
        try
        {
            final OperationClient _operationClient = this._serviceClient.createClient(this._operations[8].getName());
            _operationClient.getOptions().setAction("http://remotelabs.eng.uts.edu.au/rigclient/maintenance");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            _messageContext = new MessageContext();
            
            SOAPEnvelope env = null;
            env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), request,
                    this.optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol",
                            "setMaintenance")));

            this._serviceClient.addHeadersToEnvelope(env);

            _messageContext.setEnvelope(env);
            _operationClient.addMessageContext(_messageContext);
            _operationClient.execute(true);

            final MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            final SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            final Object object = this.fromOM(_returnEnv.getBody().getFirstElement(), SetMaintenanceResponse.class,
                    this.getEnvelopeNamespaces(_returnEnv));

            return (SetMaintenanceResponse) object;
        }
        catch (final AxisFault f)
        {
            final OMElement faultElt = f.getDetail();
            if (faultElt != null)
            {
                if (this.faultExceptionNameMap.containsKey(faultElt.getQName()))
                {
                    try
                    {
                        final String exceptionClassName = (String) this.faultExceptionClassNameMap.get(faultElt
                                .getQName());
                        final Class<?> exceptionClass = Class.forName(exceptionClassName);
                        final Exception ex = (Exception) exceptionClass.newInstance();

                        final String messageClassName = (String) this.faultMessageMap.get(faultElt.getQName());
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
            _messageContext.getTransportOut().getSender().cleanup(_messageContext);
        }
    }

    public PerformBatchControlResponse performBatchControl(final PerformBatchControl performBatchControl) throws RemoteException
    {
        MessageContext _messageContext = null;
        try
        {
            final OperationClient _operationClient = this._serviceClient.createClient(this._operations[9].getName());
            _operationClient.getOptions().setAction("http://remotelabs.eng.uts.edu.au/rigclient/batch");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            _messageContext = new MessageContext();
            SOAPEnvelope env = null;
            env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()),
                    performBatchControl, this.optimizeContent(new QName(
                            "http://remotelabs.eng.uts.edu.au/rigclient/protocol", "performBatchControl")));

            this._serviceClient.addHeadersToEnvelope(env);

            _messageContext.setEnvelope(env);
            _operationClient.addMessageContext(_messageContext);
            _operationClient.execute(true);

            final MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            final SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            final Object object = this.fromOM(_returnEnv.getBody().getFirstElement(),
                    PerformBatchControlResponse.class, this.getEnvelopeNamespaces(_returnEnv));

            return (PerformBatchControlResponse) object;
        }
        catch (final AxisFault f)
        {
            final OMElement faultElt = f.getDetail();
            if (faultElt != null)
            {
                if (this.faultExceptionNameMap.containsKey(faultElt.getQName()))
                {
                    try
                    {
                        final String exceptionClassName = (String) this.faultExceptionClassNameMap.get(faultElt
                                .getQName());
                        final Class<?> exceptionClass = Class.forName(exceptionClassName);
                        final Exception ex = (Exception) exceptionClass.newInstance();

                        final String messageClassName = (String) this.faultMessageMap.get(faultElt.getQName());
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
            _messageContext.getTransportOut().getSender().cleanup(_messageContext);
        }
    }

    public GetStatusResponse getStatus(final GetStatus request) throws RemoteException
    {
        MessageContext _messageContext = null;
        try
        {
            final OperationClient _operationClient = this._serviceClient.createClient(this._operations[10].getName());
            _operationClient.getOptions().setAction("http://remotelabs.eng.uts.edu.au/rigclient/getStatus");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            _messageContext = new MessageContext();
            SOAPEnvelope env = null;
            env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), request, this
                    .optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "getStatus")));

            this._serviceClient.addHeadersToEnvelope(env);

            _messageContext.setEnvelope(env);
            _operationClient.addMessageContext(_messageContext);
            _operationClient.execute(true);

            final MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            final SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            final Object object = this.fromOM(_returnEnv.getBody().getFirstElement(), GetStatusResponse.class, this
                    .getEnvelopeNamespaces(_returnEnv));

            return (GetStatusResponse) object;
        }
        catch (final AxisFault f)
        {
            final OMElement faultElt = f.getDetail();
            if (faultElt != null)
            {
                if (this.faultExceptionNameMap.containsKey(faultElt.getQName()))
                {
                    try
                    {
                        final String exceptionClassName = (String) this.faultExceptionClassNameMap.get(faultElt
                                .getQName());
                        final Class<?> exceptionClass = Class.forName(exceptionClassName);
                        final Exception ex = (Exception) exceptionClass.newInstance();

                        final String messageClassName = (String) this.faultMessageMap.get(faultElt.getQName());
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
            _messageContext.getTransportOut().getSender().cleanup(_messageContext);
        }
    }

    public AllocateResponse allocate(final Allocate request) throws RemoteException
    {
        MessageContext _messageContext = null;
        try
        {
            final OperationClient _operationClient = this._serviceClient.createClient(this._operations[11].getName());
            _operationClient.getOptions().setAction("http://remotelabs.eng.uts.edu.au/rigclient/allocate");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            _messageContext = new MessageContext();
            
            SOAPEnvelope env = null;
            env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), request, this
                    .optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol", "allocate")));

            this._serviceClient.addHeadersToEnvelope(env);

            _messageContext.setEnvelope(env);
            _operationClient.addMessageContext(_messageContext);
            _operationClient.execute(true);

            final MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            final SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            final Object object = this.fromOM(_returnEnv.getBody().getFirstElement(), AllocateResponse.class, this
                    .getEnvelopeNamespaces(_returnEnv));

            return (AllocateResponse) object;
        }
        catch (final AxisFault f)
        {
            final OMElement faultElt = f.getDetail();
            if (faultElt != null)
            {
                if (this.faultExceptionNameMap.containsKey(faultElt.getQName()))
                {
                    try
                    {
                        final String exceptionClassName = (String) this.faultExceptionClassNameMap.get(faultElt
                                .getQName());
                        final Class<?> exceptionClass = Class.forName(exceptionClassName);
                        final Exception ex = (Exception) exceptionClass.newInstance();

                        final String messageClassName = (String) this.faultMessageMap.get(faultElt.getQName());
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
            _messageContext.getTransportOut().getSender().cleanup(_messageContext);
        }
    }

    public SlaveAllocateResponse slaveAllocate(final SlaveAllocate request) throws RemoteException
    {
        MessageContext _messageContext = null;
        try
        {
            final OperationClient _operationClient = this._serviceClient.createClient(this._operations[12].getName());
            _operationClient.getOptions().setAction("http://remotelabs.eng.uts.edu.au/rigclient/slaveAllocate");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            _messageContext = new MessageContext();
            SOAPEnvelope env = null;
            env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()), request,
                    this.optimizeContent(new QName("http://remotelabs.eng.uts.edu.au/rigclient/protocol",
                            "slaveAllocate")));

            this._serviceClient.addHeadersToEnvelope(env);
            _messageContext.setEnvelope(env);
            _operationClient.addMessageContext(_messageContext);
            _operationClient.execute(true);

            final MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            final SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            final Object object = this.fromOM(_returnEnv.getBody().getFirstElement(), SlaveAllocateResponse.class, this
                    .getEnvelopeNamespaces(_returnEnv));

            return (SlaveAllocateResponse) object;
        }
        catch (final AxisFault f)
        {
            final OMElement faultElt = f.getDetail();
            if (faultElt != null)
            {
                if (this.faultExceptionNameMap.containsKey(faultElt.getQName()))
                {
                    try
                    {
                        final String exceptionClassName = (String) this.faultExceptionClassNameMap.get(faultElt
                                .getQName());
                        final Class<?> exceptionClass = Class.forName(exceptionClassName);
                        final Exception ex = (Exception) exceptionClass.newInstance();

                        final String messageClassName = (String) this.faultMessageMap.get(faultElt.getQName());
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
            _messageContext.getTransportOut().getSender().cleanup(_messageContext);
        }
    }

    public AbortBatchControlResponse abortBatchControl(final AbortBatchControl request) throws RemoteException
    {
        MessageContext _messageContext = null;
        try
        {
            final OperationClient _operationClient = this._serviceClient.createClient(this._operations[13].getName());
            _operationClient.getOptions().setAction("http://remotelabs.eng.uts.edu.au/rigclient/batchAbort");
            _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

            this.addPropertyToOperationClient(_operationClient, WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR,
                    "&");

            _messageContext = new MessageContext();

            SOAPEnvelope env = null;
            env = this.toEnvelope(Stub.getFactory(_operationClient.getOptions().getSoapVersionURI()),
                    request, this.optimizeContent(new QName(
                            "http://remotelabs.eng.uts.edu.au/rigclient/protocol", "abortBatchControl")));

            this._serviceClient.addHeadersToEnvelope(env);

            _messageContext.setEnvelope(env);
            _operationClient.addMessageContext(_messageContext);
            _operationClient.execute(true);

            final MessageContext _returnMessageContext = _operationClient
                    .getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
            final SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

            final Object object = this.fromOM(_returnEnv.getBody().getFirstElement(), AbortBatchControlResponse.class,
                    this.getEnvelopeNamespaces(_returnEnv));

            return (AbortBatchControlResponse) object;
        }
        catch (final AxisFault f)
        {
            final OMElement faultElt = f.getDetail();
            if (faultElt != null)
            {
                if (this.faultExceptionNameMap.containsKey(faultElt.getQName()))
                {
                    try
                    {
                        final String exceptionClassName = (String) this.faultExceptionClassNameMap.get(faultElt
                                .getQName());
                        final Class<?> exceptionClass = Class.forName(exceptionClassName);
                        final Exception ex = (Exception) exceptionClass.newInstance();

                        final String messageClassName = (String) this.faultMessageMap.get(faultElt.getQName());
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
            _messageContext.getTransportOut().getSender().cleanup(_messageContext);
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

    private Object fromOM(final OMElement param, final Class<?> type, final Map<String, String> extraNamespaces)
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
