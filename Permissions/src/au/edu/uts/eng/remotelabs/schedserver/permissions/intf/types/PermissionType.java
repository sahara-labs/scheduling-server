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

package au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.axiom.om.OMConstants;
import org.apache.axiom.om.OMDataSource;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.impl.llom.OMSourcedElementImpl;
import org.apache.axis2.databinding.ADBBean;
import org.apache.axis2.databinding.ADBDataSource;
import org.apache.axis2.databinding.ADBException;
import org.apache.axis2.databinding.utils.BeanUtil;
import org.apache.axis2.databinding.utils.ConverterUtil;
import org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl;
import org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter;

/**
 * PermissionType bean class.
 */
public class PermissionType extends PermissionIDType implements ADBBean
{
    /*
     * This type was generated from the piece of schema that had
     * name = PermissionType
     * Namespace URI = http://remotelabs.eng.uts.edu.au/schedserver/permissions
     * Namespace Prefix = ns1
     */

    private static final long serialVersionUID = -3543313127108184582L;

    protected UserClassIDType userClass;
    protected ResourceClass localResourceClass;
    protected ResourceIDType resource;
    protected int sessionDuration;
    protected boolean sessionDurationTracker = false;
    protected int extensionDuration;
    protected boolean extensionDurationTracker = false;
    protected int allowedExtensions;
    protected boolean allowedExtensionsTracker = false;
    protected int queueActivityTmOut;
    protected boolean queueActivityTmOutTracker = false;
    protected String sessionActivityTmOut;
    protected boolean sessionActivityTmOutTracker = false;
    protected Calendar start;
    protected boolean startTracker = false;
    protected Calendar expiry;
    protected boolean expiryTracker = false;

    private static String generatePrefix(final String namespace)
    {
        if (namespace.equals("http://remotelabs.eng.uts.edu.au/schedserver/permissions"))
        {
            return "ns1";
        }
        return BeanUtil.getUniquePrefix();
    }

    public UserClassIDType getUserClass()
    {
        return this.userClass;
    }

    public void setUserClass(final UserClassIDType param)
    {
        this.userClass = param;
    }

    public ResourceClass getResourceClass()
    {
        return this.localResourceClass;
    }

    public void setResourceClass(final ResourceClass param)
    {

        this.localResourceClass = param;

    }

    public ResourceIDType getResource()
    {
        return this.resource;
    }

    public void setResource(final ResourceIDType param)
    {

        this.resource = param;

    }

    public int getSessionDuration()
    {
        return this.sessionDuration;
    }

    public void setSessionDuration(final int param)
    {
        if (param == Integer.MIN_VALUE)
        {
            this.sessionDurationTracker = false;
        }
        else
        {
            this.sessionDurationTracker = true;
        }

        this.sessionDuration = param;
    }

    public int getExtensionDuration()
    {
        return this.extensionDuration;
    }

    public void setExtensionDuration(final int param)
    {
        if (param == Integer.MIN_VALUE)
        {
            this.extensionDurationTracker = false;
        }
        else
        {
            this.extensionDurationTracker = true;
        }

        this.extensionDuration = param;
    }

    public int getAllowedExtensions()
    {
        return this.allowedExtensions;
    }

    public void setAllowedExtensions(final int param)
    {
        if (param == Integer.MIN_VALUE)
        {
            this.allowedExtensionsTracker = false;
        }
        else
        {
            this.allowedExtensionsTracker = true;
        }

        this.allowedExtensions = param;
    }

    public int getQueueActivityTmOut()
    {
        return this.queueActivityTmOut;
    }

    public void setQueueActivityTmOut(final int param)
    {
        if (param == Integer.MIN_VALUE)
        {
            this.queueActivityTmOutTracker = false;
        }
        else
        {
            this.queueActivityTmOutTracker = true;
        }

        this.queueActivityTmOut = param;
    }

    public String getSessionActivityTmOut()
    {
        return this.sessionActivityTmOut;
    }

    public void setSessionActivityTmOut(final String param)
    {
        if (param != null)
        {
            this.sessionActivityTmOutTracker = true;
        }
        else
        {
            this.sessionActivityTmOutTracker = false;
        }

        this.sessionActivityTmOut = param;
    }

    public Calendar getStart()
    {
        return this.start;
    }

    public void setStart(final Calendar param)
    {
        if (param != null)
        {
            this.startTracker = true;
        }
        else
        {
            this.startTracker = false;
        }
        this.start = param;
    }

    public Calendar getExpiry()
    {
        return this.expiry;
    }

    public void setExpiry(final Calendar param)
    {
        if (param != null)
        {
            this.expiryTracker = true;
        }
        else
        {
            this.expiryTracker = false;
        }

        this.expiry = param;
    }

    public static boolean isReaderMTOMAware(final XMLStreamReader reader)
    {
        boolean isReaderMTOMAware = false;
        try
        {
            isReaderMTOMAware = Boolean.TRUE.equals(reader.getProperty(OMConstants.IS_DATA_HANDLERS_AWARE));
        }
        catch (final IllegalArgumentException e)
        {
            isReaderMTOMAware = false;
        }
        return isReaderMTOMAware;
    }

    @Override
    public OMElement getOMElement(final QName parentQName, final OMFactory factory) throws ADBException
    {
        final OMDataSource dataSource = new ADBDataSource(this, parentQName)
        {
            @Override
            public void serialize(final MTOMAwareXMLStreamWriter xmlWriter) throws XMLStreamException
            {
                PermissionType.this.serialize(this.parentQName, factory, xmlWriter);
            }
        };
        return new OMSourcedElementImpl(parentQName, factory, dataSource);
    }

    @Override
    public void serialize(final QName parentQName, final OMFactory factory, final MTOMAwareXMLStreamWriter xmlWriter)
            throws XMLStreamException, ADBException
    {
        this.serialize(parentQName, factory, xmlWriter, false);
    }

    @Override
    public void serialize(final QName parentQName, final OMFactory factory, final MTOMAwareXMLStreamWriter xmlWriter,
            final boolean serializeType) throws XMLStreamException, ADBException
    {
        String prefix = parentQName.getPrefix();
        String namespace = parentQName.getNamespaceURI();

        if ((namespace != null) && (namespace.trim().length() > 0))
        {
            final String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null)
            {
                xmlWriter.writeStartElement(namespace, parentQName.getLocalPart());
            }
            else
            {
                if (prefix == null)
                {
                    prefix = PermissionType.generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, parentQName.getLocalPart(), namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
        }
        else
        {
            xmlWriter.writeStartElement(parentQName.getLocalPart());
        }

        final String namespacePrefix = this.registerPrefix(xmlWriter,
                "http://remotelabs.eng.uts.edu.au/schedserver/permissions");
        if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0))
        {
            this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
                    + ":PermissionType", xmlWriter);
        }
        else
        {
            this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "PermissionType",
                    xmlWriter);
        }

        if (this.requestorIDTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = PermissionType.generatePrefix(namespace);

                    xmlWriter.writeStartElement(prefix, "requestorID", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "requestorID");
                }
            }
            else
            {
                xmlWriter.writeStartElement("requestorID");
            }

            if (this.requestorID == Integer.MIN_VALUE)
            {
                throw new ADBException("requestorID cannot be null!!");
            }
            else
            {
                xmlWriter.writeCharacters(ConverterUtil.convertToString(this.requestorID));
            }

            xmlWriter.writeEndElement();
        }
        if (this.nameNameSpaceSequenceTracker)
        {
            if (this.nameNameSpaceSequence == null)
            {
                throw new ADBException("OperationRequestTypeSequence_type0 cannot be null!!");
            }
            this.nameNameSpaceSequence.serialize(null, factory, xmlWriter);
        }
        if (this.requestorQNameTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = PermissionType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "requestorQName", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "requestorQName");
                }
            }
            else
            {
                xmlWriter.writeStartElement("requestorQName");
            }

            if (this.requestorQName == null)
            {
                throw new ADBException("requestorQName cannot be null!!");
            }
            else
            {
                xmlWriter.writeCharacters(this.requestorQName);
            }

            xmlWriter.writeEndElement();
        }
        
        if (this.permissionIDTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = PermissionType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "permissionID", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "permissionID");
                }
            }
            else
            {
                xmlWriter.writeStartElement("permissionID");
            }

            if (this.permissionID == Integer.MIN_VALUE)
            {
                throw new ADBException("permissionID cannot be null!!");
            }
            else
            {
                xmlWriter.writeCharacters(ConverterUtil.convertToString(this.permissionID));
            }

            xmlWriter.writeEndElement();
        }
        
        if (this.userClass == null)
        {
            throw new ADBException("userClass cannot be null!!");
        }
        this.userClass.serialize(new QName("", "userClass"), factory, xmlWriter);

        if (this.localResourceClass == null)
        {
            throw new ADBException("resourceClass cannot be null!!");
        }
        this.localResourceClass.serialize(new QName("", "resourceClass"), factory, xmlWriter);

        if (this.resource == null)
        {
            throw new ADBException("resource cannot be null!!");
        }
        this.resource.serialize(new QName("", "resource"), factory, xmlWriter);
        
        if (this.sessionDurationTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = PermissionType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "sessionDuration", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "sessionDuration");
                }
            }
            else
            {
                xmlWriter.writeStartElement("sessionDuration");
            }

            if (this.sessionDuration == Integer.MIN_VALUE)
            {
                throw new ADBException("sessionDuration cannot be null!!");
            }
            else
            {
                xmlWriter.writeCharacters(ConverterUtil.convertToString(this.sessionDuration));
            }
            xmlWriter.writeEndElement();
        }
        
        if (this.extensionDurationTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = PermissionType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "extensionDuration", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "extensionDuration");
                }
            }
            else
            {
                xmlWriter.writeStartElement("extensionDuration");
            }

            if (this.extensionDuration == Integer.MIN_VALUE)
            {
                throw new ADBException("extensionDuration cannot be null!!");
            }
            else
            {
                xmlWriter.writeCharacters(ConverterUtil.convertToString(this.extensionDuration));
            }
            xmlWriter.writeEndElement();
        }
        
        if (this.allowedExtensionsTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = PermissionType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "allowedExtensions", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "allowedExtensions");
                }
            }
            else
            {
                xmlWriter.writeStartElement("allowedExtensions");
            }

            if (this.allowedExtensions == Integer.MIN_VALUE)
            {
                throw new ADBException("allowedExtensions cannot be null!!");
            }
            else
            {
                xmlWriter.writeCharacters(ConverterUtil.convertToString(this.allowedExtensions));
            }
            xmlWriter.writeEndElement();
        }
        
        if (this.queueActivityTmOutTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = PermissionType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "queueActivityTmOut", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "queueActivityTmOut");
                }
            }
            else
            {
                xmlWriter.writeStartElement("queueActivityTmOut");
            }

            if (this.queueActivityTmOut == Integer.MIN_VALUE)
            {
                throw new ADBException("queueActivityTmOut cannot be null!!");
            }
            else
            {
                xmlWriter.writeCharacters(ConverterUtil.convertToString(this.queueActivityTmOut));
            }
            xmlWriter.writeEndElement();
        }
        
        if (this.sessionActivityTmOutTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = PermissionType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "sessionActivityTmOut", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "sessionActivityTmOut");
                }
            }
            else
            {
                xmlWriter.writeStartElement("sessionActivityTmOut");
            }

            if (this.sessionActivityTmOut == null)
            {
                throw new ADBException("sessionActivityTmOut cannot be null!!");
            }
            else
            {
                xmlWriter.writeCharacters(this.sessionActivityTmOut);
            }
            xmlWriter.writeEndElement();
        }
        
        if (this.startTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = PermissionType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "start", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "start");
                }
            }
            else
            {
                xmlWriter.writeStartElement("start");
            }

            if (this.start == null)
            {
                throw new ADBException("start cannot be null!!");
            }
            else
            {
                xmlWriter.writeCharacters(ConverterUtil.convertToString(this.start));
            }
            xmlWriter.writeEndElement();
        }
        
        if (this.expiryTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = PermissionType.generatePrefix(namespace);

                    xmlWriter.writeStartElement(prefix, "expiry", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "expiry");
                }
            }
            else
            {
                xmlWriter.writeStartElement("expiry");
            }

            if (this.expiry == null)
            {
                throw new ADBException("expiry cannot be null!!");
            }
            else
            {
                xmlWriter.writeCharacters(ConverterUtil.convertToString(this.expiry));
            }
            xmlWriter.writeEndElement();
        }
        xmlWriter.writeEndElement();
    }

    private void writeAttribute(final String prefix, final String namespace, final String attName,
            final String attValue, final XMLStreamWriter xmlWriter) throws XMLStreamException
    {
        if (xmlWriter.getPrefix(namespace) == null)
        {
            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
        }
        xmlWriter.writeAttribute(namespace, attName, attValue);
    }

    private String registerPrefix(final XMLStreamWriter xmlWriter, final String namespace) throws XMLStreamException
    {
        String prefix = xmlWriter.getPrefix(namespace);
        if (prefix == null)
        {
            prefix = PermissionType.generatePrefix(namespace);
            while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null)
            {
                prefix = BeanUtil.getUniquePrefix();
            }

            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
        }

        return prefix;
    }

    @Override
    public XMLStreamReader getPullParser(final QName qName) throws ADBException
    {
        final ArrayList<Serializable> elementList = new ArrayList<Serializable>();
        final ArrayList<QName> attribList = new ArrayList<QName>();

        attribList.add(new QName("http://www.w3.org/2001/XMLSchema-instance", "type"));
        attribList.add(new QName("http://remotelabs.eng.uts.edu.au/schedserver/permissions", "PermissionType"));
        if (this.requestorIDTracker)
        {
            elementList.add(new QName("", "requestorID"));
            elementList.add(ConverterUtil.convertToString(this.requestorID));
        }
        
        if (this.nameNameSpaceSequenceTracker)
        {
            elementList.add(new QName("http://remotelabs.eng.uts.edu.au/schedserver/permissions",
                    "OperationRequestTypeSequence_type0"));
            if (this.nameNameSpaceSequence == null)
            {
                throw new ADBException("OperationRequestTypeSequence_type0 cannot be null!!");
            }
            elementList.add(this.nameNameSpaceSequence);
        }
        
        if (this.requestorQNameTracker)
        {
            elementList.add(new QName("", "requestorQName"));
            if (this.requestorQName != null)
            {
                elementList.add(ConverterUtil.convertToString(this.requestorQName));
            }
            else
            {
                throw new ADBException("requestorQName cannot be null!!");
            }
        }
        
        if (this.permissionIDTracker)
        {
            elementList.add(new QName("", "permissionID"));
            elementList.add(ConverterUtil.convertToString(this.permissionID));
        }
        elementList.add(new QName("", "userClass"));

        if (this.userClass == null)
        {
            throw new ADBException("userClass cannot be null!!");
        }
        elementList.add(this.userClass);
        
        elementList.add(new QName("", "resourceClass"));
        if (this.localResourceClass == null)
        {
            throw new ADBException("resourceClass cannot be null!!");
        }
        elementList.add(this.localResourceClass);

        elementList.add(new QName("", "resource"));
        if (this.resource == null)
        {
            throw new ADBException("resource cannot be null!!");
        }
        elementList.add(this.resource);
        
        if (this.sessionDurationTracker)
        {
            elementList.add(new QName("", "sessionDuration"));
            elementList.add(ConverterUtil.convertToString(this.sessionDuration));
        }
        if (this.extensionDurationTracker)
        {
            elementList.add(new QName("", "extensionDuration"));
            elementList.add(ConverterUtil.convertToString(this.extensionDuration));
        }
        
        if (this.allowedExtensionsTracker)
        {
            elementList.add(new QName("", "allowedExtensions"));
            elementList.add(ConverterUtil.convertToString(this.allowedExtensions));
        }
        
        if (this.queueActivityTmOutTracker)
        {
            elementList.add(new QName("", "queueActivityTmOut"));
            elementList.add(ConverterUtil.convertToString(this.queueActivityTmOut));
        }
        
        if (this.sessionActivityTmOutTracker)
        {
            elementList.add(new QName("", "sessionActivityTmOut"));
            if (this.sessionActivityTmOut != null)
            {
                elementList.add(ConverterUtil.convertToString(this.sessionActivityTmOut));
            }
            else
            {
                throw new ADBException("sessionActivityTmOut cannot be null!!");
            }
        }
        
        if (this.startTracker)
        {
            elementList.add(new QName("", "start"));
            if (this.start != null)
            {
                elementList.add(ConverterUtil.convertToString(this.start));
            }
            else
            {
                throw new ADBException("start cannot be null!!");
            }
        }
        
        if (this.expiryTracker)
        {
            elementList.add(new QName("", "expiry"));
            if (this.expiry != null)
            {
                elementList.add(ConverterUtil.convertToString(this.expiry));
            }
            else
            {
                throw new ADBException("expiry cannot be null!!");
            }
        }

        return new ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());
    }

    public static class Factory
    {
        public static PermissionType parse(final XMLStreamReader reader) throws Exception
        {
            final PermissionType object = new PermissionType();

            try
            {
                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type") != null)
                {
                    final String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "type");
                    if (fullTypeName != null)
                    {
                        String nsPrefix = null;
                        if (fullTypeName.indexOf(":") > -1)
                        {
                            nsPrefix = fullTypeName.substring(0, fullTypeName.indexOf(":"));
                        }
                        nsPrefix = nsPrefix == null ? "" : nsPrefix;

                        final String type = fullTypeName.substring(fullTypeName.indexOf(":") + 1);
                        if (!"PermissionType".equals(type))
                        {
                            final String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (PermissionType) ExtensionMapper.getTypeObject(nsUri, type, reader);
                        }
                    }
                }

                reader.next();
                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "requestorID").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setRequestorID(ConverterUtil.convertToInt(content));
                    reader.next();
                }
                else
                {
                    object.setRequestorID(Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                try
                {
                    if (reader.isStartElement())
                    {
                        object.setOperationRequestTypeSequence_type0(OperationRequestTypeSequence.Factory
                                .parse(reader));
                    }
                }
                catch (final Exception e)
                {
                    /* Optional. */
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "requestorQName").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setRequestorQName(ConverterUtil.convertToString(content));
                    reader.next();
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "permissionID").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setPermissionID(ConverterUtil.convertToInt(content));
                    reader.next();
                }
                else
                {
                    object.setPermissionID(Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "userClass").equals(reader.getName()))
                {
                    object.setUserClass(UserClassIDType.Factory.parse(reader));
                    reader.next();
                }
                else
                {
                    throw new ADBException("Unexpected subelement " + reader.getLocalName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "resourceClass").equals(reader.getName()))
                {
                    object.setResourceClass(ResourceClass.Factory.parse(reader));
                    reader.next();
                }
                else
                {
                    throw new ADBException("Unexpected subelement " + reader.getLocalName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "resource").equals(reader.getName()))
                {
                    object.setResource(ResourceIDType.Factory.parse(reader));
                    reader.next();
                }
                else
                {
                    throw new ADBException("Unexpected subelement " + reader.getLocalName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "sessionDuration").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setSessionDuration(ConverterUtil.convertToInt(content));
                    reader.next();
                }
                else
                {
                    object.setSessionDuration(Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "extensionDuration").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setExtensionDuration(ConverterUtil.convertToInt(content));
                    reader.next();
                }
                else
                {
                    object.setExtensionDuration(Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "allowedExtensions").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setAllowedExtensions(ConverterUtil.convertToInt(content));
                    reader.next();
                }
                else
                {
                    object.setAllowedExtensions(Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "queueActivityTmOut").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setQueueActivityTmOut(ConverterUtil.convertToInt(content));
                    reader.next();
                }
                else
                {
                    object.setQueueActivityTmOut(Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "sessionActivityTmOut").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setSessionActivityTmOut(ConverterUtil.convertToString(content));
                    reader.next();
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "start").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setStart(ConverterUtil.convertToDateTime(content));
                    reader.next();
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "expiry").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setExpiry(ConverterUtil.convertToDateTime(content));
                    reader.next();
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement())
                {
                    throw new ADBException("Unexpected subelement " + reader.getLocalName());
                }

            }
            catch (final XMLStreamException e)
            {
                throw new Exception(e);
            }
            return object;
        }
    }
}
