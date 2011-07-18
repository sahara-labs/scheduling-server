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

package au.edu.uts.eng.remotelabs.schedserver.multisite.intf;

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

import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.AddToQueue;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.AddToQueueResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.CancelBooking;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.CancelBookingResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.CheckAvailability;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.CheckAvailabilityResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.CreateBooking;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.CreateBookingResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.FindFreeBookings;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.FindFreeBookingsResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.FinishSession;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.FinishSessionResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.GetQueuePosition;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.GetQueuePositionResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.GetSessionInformation;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.GetSessionInformationResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.GetUserStatus;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.GetUserStatusResponse;

/**
 * MultiSiteMessageReceiverInOut messag/home/mdiponio/capstone/workspacee receiver.
 */
public class MultiSiteMessageReceiverInOut extends AbstractInOutMessageReceiver
{
    @Override
    public void invokeBusinessLogic(final MessageContext msgContext, final MessageContext newMsgContext)
            throws AxisFault
    {
        try
        {
            final Object obj = this.getTheImplementationObject(msgContext);

            final MultiSiteInterface skel = (MultiSiteInterface) obj;

            SOAPEnvelope envelope = null;
            final AxisOperation op = msgContext.getOperationContext().getAxisOperation();
            if (op == null)
            {
                throw new AxisFault("Operation is not located, if this is doclit style the SOAP-ACTION should be " +
                		"specified via the SOAP Action to use the RawXMLProvider");
            }

            String methodName;
            if ((op.getName() != null) && ((methodName = JavaUtils.xmlNameToJavaIdentifier(op.getName().getLocalPart())) != null))
            {
                if ("getUserStatus".equals(methodName))
                {
                    final GetUserStatus wrappedParam = (GetUserStatus) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), GetUserStatus.class, this.getEnvelopeNamespaces(msgContext.getEnvelope()));

                    GetUserStatusResponse response = skel.getUserStatus(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), response, false,
                            new QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite", "getUserStatus"));
                }
                else if ("cancelBooking".equals(methodName))
                {
                    final CancelBooking wrappedParam = (CancelBooking) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), CancelBooking.class,
                            this.getEnvelopeNamespaces(msgContext.getEnvelope()));

                    CancelBookingResponse response = skel.cancelBooking(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), response, false,
                            new QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite", "cancelBooking"));
                }
                else if ("checkAvailability".equals(methodName))
                {
                    final CheckAvailability wrappedParam = (CheckAvailability) this.fromOM(msgContext.getEnvelope()
                            .getBody().getFirstElement(), CheckAvailability.class,
                            this.getEnvelopeNamespaces(msgContext.getEnvelope()));

                    CheckAvailabilityResponse response = skel.checkAvailability(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), response, false,
                            new QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite", "checkAvailability"));
                }
                else if ("findFreeBookings".equals(methodName))
                {
                    final FindFreeBookings wrappedParam = (FindFreeBookings) this.fromOM(msgContext.getEnvelope()
                            .getBody().getFirstElement(), FindFreeBookings.class,
                            this.getEnvelopeNamespaces(msgContext.getEnvelope()));

                    FindFreeBookingsResponse response = skel.findFreeBookings(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), response, false,
                            new QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite", "findFreeBookings"));
                }
                else if ("getSessionInformation".equals(methodName))
                {
                    final GetSessionInformation wrappedParam = (GetSessionInformation) this.fromOM(msgContext
                            .getEnvelope().getBody().getFirstElement(), GetSessionInformation.class,
                            this.getEnvelopeNamespaces(msgContext.getEnvelope()));

                    GetSessionInformationResponse response = skel.getSessionInformation(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), response, false,
                            new QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite", "getSessionInformation"));
                }
                else if ("getQueuePosition".equals(methodName))
                {
                    final GetQueuePosition wrappedParam = (GetQueuePosition) this.fromOM(msgContext.getEnvelope()
                            .getBody().getFirstElement(), GetQueuePosition.class,
                            this.getEnvelopeNamespaces(msgContext.getEnvelope()));

                    GetQueuePositionResponse response = skel.getQueuePosition(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), response, false,
                            new QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite", "getQueuePosition"));
                }
                else if ("addToQueue".equals(methodName))
                {
                    final AddToQueue wrappedParam = (AddToQueue) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), AddToQueue.class, this.getEnvelopeNamespaces(msgContext.getEnvelope()));

                    AddToQueueResponse response = skel.addToQueue(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), response, false, new QName(
                            "http://remotelabs.eng.uts.edu.au/schedserver/multisite", "addToQueue"));
                }
                else if ("createBooking".equals(methodName))
                {
                    final CreateBooking wrappedParam = (CreateBooking) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), CreateBooking.class,
                            this.getEnvelopeNamespaces(msgContext.getEnvelope()));

                    CreateBookingResponse response = skel.createBooking(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), response, false,
                            new QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite", "createBooking"));
                }
                else if ("finishSession".equals(methodName))
                {
                    final FinishSession wrappedParam = (FinishSession) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), FinishSession.class,
                            this.getEnvelopeNamespaces(msgContext.getEnvelope()));

                    FinishSessionResponse response = skel.finishSession(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), response, false,
                            new QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite", "finishSession"));
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

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final GetUserStatusResponse param,
            final boolean optimizeContent, final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(GetUserStatusResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final CancelBookingResponse param,
            final boolean optimizeContent, final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(CancelBookingResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final CheckAvailabilityResponse param,
            final boolean optimizeContent, final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(CheckAvailabilityResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final FindFreeBookingsResponse param,
            final boolean optimizeContent, final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(FindFreeBookingsResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final GetSessionInformationResponse param,
            final boolean optimizeContent, final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(GetSessionInformationResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final GetQueuePositionResponse param,
            final boolean optimizeContent, final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(GetQueuePositionResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final AddToQueueResponse param,
            final boolean optimizeContent, final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(AddToQueueResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final CreateBookingResponse param,
            final boolean optimizeContent, final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(CreateBookingResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final FinishSessionResponse param,
            final boolean optimizeContent, final QName methodQName) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(FinishSessionResponse.MY_QNAME, factory));
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

    /**
     * A utility method that copies the namepaces from the SOAPEnvelope.
     */
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

}//end of class
