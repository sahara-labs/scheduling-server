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
 * @date 17th October 2010
 */

package au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.service;

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

import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.service.types.AllocateCallback;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.service.types.AllocateCallbackResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.service.types.ReleaseCallback;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.service.types.ReleaseCallbackResponse;

/**
 * RigClient Proxy message receiver.
 */
public class RigClientProxyMessageReceiverInOut extends AbstractInOutMessageReceiver
{
    @Override
    public void invokeBusinessLogic(final MessageContext msgContext, final MessageContext newMsgContext)
            throws AxisFault
    {
        try
        {
            final Object obj = this.getTheImplementationObject(msgContext);
            final RigClientProxyInterface skel = (RigClientProxyInterface) obj;

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
                if ("allocateCallback".equals(methodName))
                {
                    final AllocateCallback wrappedParam = (AllocateCallback) this.fromOM(
                            msgContext.getEnvelope().getBody().getFirstElement(), AllocateCallback.class,
                            this.getEnvelopeNamespaces(msgContext.getEnvelope())
                    );

                    AllocateCallbackResponse response = skel.allocateCallback(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), response, false);
                }
                else if ("releaseCallback".equals(methodName))
                {
                    final ReleaseCallback wrappedParam = (ReleaseCallback) this.fromOM(
                            msgContext.getEnvelope().getBody().getFirstElement(), ReleaseCallback.class,
                            this.getEnvelopeNamespaces(msgContext.getEnvelope())
                    );
                    
                    ReleaseCallbackResponse response = skel.releaseCallback(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), response, false);
                }
                else
                {
                    throw new RuntimeException("Operation not found.");
                }

                newMsgContext.setEnvelope(envelope);
            }
        }
        catch (final Exception e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final AllocateCallbackResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(AllocateCallbackResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final ReleaseCallbackResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(ReleaseCallbackResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }
    
    @SuppressWarnings("rawtypes") 
    private Object fromOM(final OMElement param, final Class<?> type, final Map extraNamespaces)
            throws AxisFault
    {
        try
        {
            if (AllocateCallback.class.equals(type))
            {
                return AllocateCallback.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (AllocateCallbackResponse.class.equals(type))
            {
                return AllocateCallbackResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }
            
            if (ReleaseCallback.class.equals(type))
            {
                return ReleaseCallback.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }
            
            if (ReleaseCallbackResponse.class.equals(type))
            {
                return ReleaseCallbackResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }
        }
        catch (final Exception e)
        {
            throw AxisFault.makeFault(e);
        }
        return null;
    }

    
    @SuppressWarnings({"rawtypes", "unchecked"})
    private Map getEnvelopeNamespaces(final SOAPEnvelope env)
    {
        final Map returnMap = new HashMap();
        final Iterator namespaceIterator = env.getAllDeclaredNamespaces();
        while (namespaceIterator.hasNext())
        {
            final OMNamespace ns = (OMNamespace) namespaceIterator.next();
            returnMap.put(ns.getPrefix(), ns.getNamespaceURI());
        }
        return returnMap;
    }
}
