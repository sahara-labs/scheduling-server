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
 * @date 29th January 2010
 */

package au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf;

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

import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.FreeRig;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.FreeRigResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.GetRig;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.GetRigResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.GetTypeStatus;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.GetTypeStatusResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.GetTypes;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.GetTypesResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.PutRigOffline;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.PutRigOfflineResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.PutRigOnline;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.PutRigOnlineResponse;

/**
 * RigManagementMessageReceiverInOut message receiver.
 */
public class RigManagementMessageReceiverInOut extends AbstractInOutMessageReceiver
{
    @Override
    public void invokeBusinessLogic(final MessageContext msgContext, final MessageContext newMsgContext)
            throws AxisFault
    {
        try
        {
            final RigManagementInterface skel = (RigManagementInterface) this.getTheImplementationObject(msgContext);

            SOAPEnvelope envelope = null;
            final AxisOperation op = msgContext.getOperationContext().getAxisOperation();
            if (op == null)
            {
                throw new AxisFault("Operation is not located.");
            }

            String methodName;
            if ((op.getName() != null) && ((methodName = JavaUtils.xmlNameToJava(op.getName().getLocalPart())) != null))
            {
                if ("putRigOffline".equals(methodName))
                {
                    final PutRigOffline wrappedParam = (PutRigOffline) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), PutRigOffline.class,
                            this.getEnvelopeNamespaces(msgContext.getEnvelope()));

                    PutRigOfflineResponse resoponse = skel.putRigOffline(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), resoponse, false);
                }
                else if ("freeRig".equals(methodName))
                {
                    final FreeRig wrappedParam = (FreeRig) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), FreeRig.class, this.getEnvelopeNamespaces(msgContext.getEnvelope()));

                    FreeRigResponse response = skel.freeRig(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), response, false);
                }
                else if ("getRig".equals(methodName))
                {
                    final GetRig wrappedParam = (GetRig) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), GetRig.class, this.getEnvelopeNamespaces(msgContext.getEnvelope()));

                    GetRigResponse response = skel.getRig(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), response, false);
                } 
                else if ("getTypeStatus".equals(methodName))
                {
                    final GetTypeStatus wrappedParam = (GetTypeStatus) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), GetTypeStatus.class,
                            this.getEnvelopeNamespaces(msgContext.getEnvelope()));
                    
                    GetTypeStatusResponse response = skel.getTypeStatus(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), response, false);
                }
                else if ("getTypes".equals(methodName))
                {
                    final GetTypes wrappedParam = (GetTypes) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), GetTypes.class, this.getEnvelopeNamespaces(msgContext.getEnvelope()));

                    GetTypesResponse response = skel.getTypes(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), response, false);
                }
                else if ("putRigOnline".equals(methodName))
                {
                    final PutRigOnline wrappedParam = (PutRigOnline) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), PutRigOnline.class,
                            this.getEnvelopeNamespaces(msgContext.getEnvelope()));
                    
                    PutRigOnlineResponse response = skel.putRigOnline(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), response, false);
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

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final PutRigOfflineResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(PutRigOfflineResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final FreeRigResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(FreeRigResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final GetRigResponse param, final boolean optimizeContent)
            throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(GetRigResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final GetTypeStatusResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(GetTypeStatusResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final GetTypesResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(GetTypesResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final PutRigOnlineResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(PutRigOnlineResponse.MY_QNAME, factory));
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
            if (PutRigOffline.class.equals(type))
            {
                return PutRigOffline.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (PutRigOfflineResponse.class.equals(type))
            {
                return PutRigOfflineResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (FreeRig.class.equals(type))
            {
                return FreeRig.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (FreeRigResponse.class.equals(type))
            {
                return FreeRigResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetRig.class.equals(type))
            {
                return GetRig.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetRigResponse.class.equals(type))
            {
                return GetRigResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetTypeStatus.class.equals(type))
            {
                return GetTypeStatus.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetTypeStatusResponse.class.equals(type))
            {
                return GetTypeStatusResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetTypes.class.equals(type))
            {
                return GetTypes.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetTypesResponse.class.equals(type))
            {
                return GetTypesResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (PutRigOnline.class.equals(type))
            {
                return PutRigOnline.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (PutRigOnlineResponse.class.equals(type))
            {
                return PutRigOnlineResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
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
