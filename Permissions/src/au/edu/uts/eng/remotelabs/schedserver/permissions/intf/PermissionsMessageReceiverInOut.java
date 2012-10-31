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
 * @date 3rd March 2009
 */
package au.edu.uts.eng.remotelabs.schedserver.permissions.intf;

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

import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermission;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForAcademic;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetAcademicPermissionsForAcademicResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermission;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUser;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserClass;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserClassResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetPermissionsForUserResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUser;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClass;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassesForUser;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserClassesForUserResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.GetUserResponse;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UnlockUserLock;
import au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UnlockUserLockResponse;

/**
 * Message receiver for the permissions SOAP operations.
 */
public class PermissionsMessageReceiverInOut extends AbstractInOutMessageReceiver
{
    @Override
    public void invokeBusinessLogic(final MessageContext msgContext, final MessageContext newMsgContext)
            throws AxisFault
    {
        try
        {
            final Object obj = this.getTheImplementationObject(msgContext);
            final PermissionsSOAP skel = (PermissionsSOAP) obj;

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
                if ("getUserClassesForUser".equals(methodName))
                {
                    GetUserClassesForUserResponse getUserClassesForUserResponse = null;
                    final GetUserClassesForUser wrappedParam = (GetUserClassesForUser) this.fromOM(msgContext
                            .getEnvelope().getBody().getFirstElement(), GetUserClassesForUser.class, this
                            .getEnvelopeNamespaces(msgContext.getEnvelope()));
                    getUserClassesForUserResponse = skel.getUserClassesForUser(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), getUserClassesForUserResponse, false);
                }
                else if ("getAcademicPermissionsForAcademic".equals(methodName))
                {
                    GetAcademicPermissionsForAcademicResponse getAcademicPermissionsForAcademicResponse = null;
                    final GetAcademicPermissionsForAcademic wrappedParam = (GetAcademicPermissionsForAcademic) this
                            .fromOM(msgContext.getEnvelope().getBody().getFirstElement(),
                            GetAcademicPermissionsForAcademic.class, this.getEnvelopeNamespaces(msgContext
                            .getEnvelope()));
                    getAcademicPermissionsForAcademicResponse = skel.getAcademicPermissionsForAcademic(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext),
                            getAcademicPermissionsForAcademicResponse, false);
                }
                else if ("getPermissionsForUserClass".equals(methodName))
                {
                    GetPermissionsForUserClassResponse getPermissionsForUserClassResponse = null;
                    final GetPermissionsForUserClass wrappedParam = (GetPermissionsForUserClass) this.fromOM(msgContext
                            .getEnvelope().getBody().getFirstElement(), GetPermissionsForUserClass.class, this
                            .getEnvelopeNamespaces(msgContext.getEnvelope()));
                    getPermissionsForUserClassResponse = skel.getPermissionsForUserClass(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), getPermissionsForUserClassResponse,
                            false);
                }
                else if ("getPermissionsForUser".equals(methodName))
                {
                    GetPermissionsForUserResponse getPermissionsForUserResponse = null;
                    final GetPermissionsForUser wrappedParam = (GetPermissionsForUser) this.fromOM(msgContext
                            .getEnvelope().getBody().getFirstElement(), GetPermissionsForUser.class, this
                            .getEnvelopeNamespaces(msgContext.getEnvelope()));
                    getPermissionsForUserResponse = skel.getPermissionsForUser(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), getPermissionsForUserResponse, false);
                }
                else if ("getUser".equals(methodName))
                {
                    GetUserResponse getUserResponse = null;
                    final GetUser wrappedParam = (GetUser) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), GetUser.class, this.getEnvelopeNamespaces(msgContext.getEnvelope()));
                    getUserResponse = skel.getUser(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), getUserResponse, false);
                }
                else if ("getAcademicPermission".equals(methodName))
                {
                    GetAcademicPermissionResponse getAcademicPermissionResponse = null;
                    final GetAcademicPermission wrappedParam = (GetAcademicPermission) this.fromOM(msgContext
                            .getEnvelope().getBody().getFirstElement(), GetAcademicPermission.class, this
                            .getEnvelopeNamespaces(msgContext.getEnvelope()));
                    getAcademicPermissionResponse = skel.getAcademicPermission(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), getAcademicPermissionResponse, false);
                }
                else if ("unlockUserLock".equals(methodName))
                {
                    UnlockUserLockResponse unlockUserLockResponse = null;
                    final UnlockUserLock wrappedParam = (UnlockUserLock) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), UnlockUserLock.class, this.getEnvelopeNamespaces(msgContext
                            .getEnvelope()));
                    unlockUserLockResponse = skel.unlockUserLock(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), unlockUserLockResponse, false);
                }
                else if ("getPermission".equals(methodName))
                {
                    GetPermissionResponse getPermissionResponse = null;
                    final GetPermission wrappedParam = (GetPermission) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), GetPermission.class, this.getEnvelopeNamespaces(msgContext
                            .getEnvelope()));
                    getPermissionResponse = skel.getPermission(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), getPermissionResponse, false);
                }
                else if ("getUserClass".equals(methodName))
                {
                    GetUserClassResponse getUserClassResponse = null;
                    final GetUserClass wrappedParam = (GetUserClass) this.fromOM(msgContext.getEnvelope().getBody()
                            .getFirstElement(), GetUserClass.class, this
                            .getEnvelopeNamespaces(msgContext.getEnvelope()));
                    getUserClassResponse = skel.getUserClass(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), getUserClassResponse, false);
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

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final GetUserClassesForUserResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(GetUserClassesForUserResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final GetAcademicPermissionsForAcademicResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(GetAcademicPermissionsForAcademicResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final GetPermissionsForUserClassResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(GetPermissionsForUserClassResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final GetPermissionsForUserResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(GetPermissionsForUserResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final GetUserResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(GetUserResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final GetAcademicPermissionResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(GetAcademicPermissionResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final UnlockUserLockResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(UnlockUserLockResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final GetPermissionResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(GetPermissionResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final GetUserClassResponse param,
            final boolean optimizeContent) throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(GetUserClassResponse.MY_QNAME, factory));
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
            if (GetUserClassesForUser.class.equals(type))
            {
                return GetUserClassesForUser.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetUserClassesForUserResponse.class.equals(type))
            {
                return GetUserClassesForUserResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetAcademicPermissionsForAcademic.class.equals(type))
            {
                return GetAcademicPermissionsForAcademic.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetAcademicPermissionsForAcademicResponse.class.equals(type))
            {
                return GetAcademicPermissionsForAcademicResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetPermissionsForUserClass.class.equals(type))
            {
                return GetPermissionsForUserClass.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetPermissionsForUserClassResponse.class.equals(type))
            {
                return GetPermissionsForUserClassResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetPermissionsForUser.class.equals(type))
            {
                return GetPermissionsForUser.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetPermissionsForUserResponse.class.equals(type))
            {
                return GetPermissionsForUserResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetUser.class.equals(type))
            {
                return GetUser.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetUserResponse.class.equals(type))
            {
                return GetUserResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetAcademicPermission.class.equals(type))
            {
                return GetAcademicPermission.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetAcademicPermissionResponse.class.equals(type))
            {
                return GetAcademicPermissionResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (UnlockUserLock.class.equals(type))
            {
                return UnlockUserLock.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (UnlockUserLockResponse.class.equals(type))
            {
                return UnlockUserLockResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetPermission.class.equals(type))
            {
                return GetPermission.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetPermissionResponse.class.equals(type))
            {
                return GetPermissionResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetUserClass.class.equals(type))
            {
                return GetUserClass.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (GetUserClassResponse.class.equals(type))
            {
                return GetUserClassResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }
        }
        catch (final Exception e)
        {
            throw AxisFault.makeFault(e);
        }
        return null;
    }

    @SuppressWarnings("rawtypes")
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
}

