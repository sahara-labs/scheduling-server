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
 * @date 28th March 2009
 */

package au.edu.uts.eng.remotelabs.schedserver.queuer.intf;

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

import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.AddUserToQueue;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.AddUserToQueueResponse;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckPermissionAvailability;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckPermissionAvailabilityResponse;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckResourceAvailability;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.CheckResourceAvailabilityResponse;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.GetUserQueuePosition;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.GetUserQueuePositionResponse;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.IsUserInQueue;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.IsUserInQueueResponse;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.RemoveUserFromQueue;
import au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types.RemoveUserFromQueueResponse;

/**
 * Queuer SOAP interface message receiver.
 */
public class QueuerMessageReceiverInOut extends AbstractInOutMessageReceiver
{
    @Override
    public void invokeBusinessLogic(final MessageContext msgContext, final MessageContext newMsgContext)
            throws AxisFault
    {
        try
        {
            final Object obj = this.getTheImplementationObject(msgContext);
            final QueuerSOAP skel = (QueuerSOAP) obj;

            SOAPEnvelope envelope = null;
            final AxisOperation op = msgContext.getOperationContext().getAxisOperation();
            if (op == null)
            {
                throw new AxisFault("Operation is not located, if this is doclit style the SOAP-ACTION should" +
                		" specified via the SOAP Action to use the RawXMLProvider");
            }

            String methodName;
            if ((op.getName() != null) && ((methodName = JavaUtils.xmlNameToJava(op.getName().getLocalPart())) != null))
            {
                if ("addUserToQueue".equals(methodName))
                {
                    AddUserToQueueResponse response = null;
                    final AddUserToQueue wrappedParam = (AddUserToQueue) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), AddUserToQueue.class, this.getEnvelopeNamespaces(msgContext
                            .getEnvelope()));

                    response = skel.addUserToQueue(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), response, false);
                }
                else if ("removeUserFromQueue".equals(methodName))
                {
                    RemoveUserFromQueueResponse response = null;
                    final RemoveUserFromQueue wrappedParam = (RemoveUserFromQueue) this.fromOM(msgContext.getEnvelope()
                            .getBody().getFirstElement(), RemoveUserFromQueue.class, this
                            .getEnvelopeNamespaces(msgContext.getEnvelope()));

                    response = skel.removeUserFromQueue(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), response, false);
                }
                else if ("checkPermissionAvailability".equals(methodName))
                {
                    CheckPermissionAvailabilityResponse response = null;
                    final CheckPermissionAvailability wrappedParam = (CheckPermissionAvailability) this.fromOM(
                            msgContext.getEnvelope().getBody().getFirstElement(), CheckPermissionAvailability.class,
                            this.getEnvelopeNamespaces(msgContext.getEnvelope()));
                    
                    response = skel.checkPermissionAvailability(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), response, false);
                }
                else if ("getUserQueuePosition".equals(methodName))
                {
                    GetUserQueuePositionResponse response = null;
                    final GetUserQueuePosition wrappedParam = (GetUserQueuePosition) this.fromOM(msgContext
                            .getEnvelope().getBody().getFirstElement(), GetUserQueuePosition.class, this
                            .getEnvelopeNamespaces(msgContext.getEnvelope()));

                    response = skel.getUserQueuePosition(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), response, false);
                }
                else if ("checkResourceAvailability".equals(methodName))
                {
                    CheckResourceAvailabilityResponse response = null;
                    final CheckResourceAvailability wrappedParam = (CheckResourceAvailability) this.fromOM(msgContext
                            .getEnvelope().getBody().getFirstElement(), CheckResourceAvailability.class, this
                            .getEnvelopeNamespaces(msgContext.getEnvelope()));

                    response = skel.checkResourceAvailability(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), response, false);
                }
                else if ("isUserInQueue".equals(methodName))
                {
                    IsUserInQueueResponse response = null;
                    final IsUserInQueue wrappedParam = (IsUserInQueue) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), IsUserInQueue.class, this.getEnvelopeNamespaces(msgContext
                            .getEnvelope()));

                    response = skel.isUserInQueue(wrappedParam);
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

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final AddUserToQueueResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(AddUserToQueueResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final RemoveUserFromQueueResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(RemoveUserFromQueueResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final CheckPermissionAvailabilityResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(CheckPermissionAvailabilityResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final GetUserQueuePositionResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(GetUserQueuePositionResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final CheckResourceAvailabilityResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(CheckResourceAvailabilityResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final IsUserInQueueResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(IsUserInQueueResponse.MY_QNAME, factory));
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
            if (AddUserToQueue.class.equals(type))
            {
                return AddUserToQueue.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (AddUserToQueueResponse.class.equals(type))
            {
                return AddUserToQueueResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (RemoveUserFromQueue.class.equals(type))
            {
                return RemoveUserFromQueue.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (RemoveUserFromQueueResponse.class.equals(type))
            {
                return RemoveUserFromQueueResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (CheckPermissionAvailability.class.equals(type))
            {
                return CheckPermissionAvailability.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (CheckPermissionAvailabilityResponse.class.equals(type))
            {
                return CheckPermissionAvailabilityResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetUserQueuePosition.class.equals(type))
            {
                return GetUserQueuePosition.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetUserQueuePositionResponse.class.equals(type))
            {
                return GetUserQueuePositionResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (CheckResourceAvailability.class.equals(type))
            {
                return CheckResourceAvailability.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (CheckResourceAvailabilityResponse.class.equals(type))
            {
                return CheckResourceAvailabilityResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (IsUserInQueue.class.equals(type))
            {
                return IsUserInQueue.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (IsUserInQueueResponse.class.equals(type))
            {
                return IsUserInQueueResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
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
