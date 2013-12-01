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
 * @date 6th April 2010
 */

package au.edu.uts.eng.remotelabs.schedserver.session.intf.types;

import java.io.Serializable;
import java.util.ArrayList;

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
 * SessionType bean class.
 */
public class SessionType extends InSessionType implements ADBBean
{
    /*
     * This type was generated from the piece of schema that had
     * name = SessionType
     * Namespace URI = http://remotelabs.eng.uts.edu.au/schedserver/session
     * Namespace Prefix = ns1
     */

    private static final long serialVersionUID = -8391148571667003667L;

    protected boolean isReady;
    protected boolean isReadyTracker = false;

    protected boolean isCodeAssigned;
    protected boolean isCodeAssignedTracker = false;

    protected ResourceIDType resource;
    protected boolean resourceTracker = false;
    
    protected String rigType;
    protected boolean rigTypeTracker = false;

    protected String contactURL;
    protected boolean contactURLTracker = false;

    protected int time;
    protected boolean timeTracker = false;

    protected int timeLeft;
    protected boolean timeLeftTracker = false;

    protected int extensions;
    protected boolean extensionsTracker = false;

    protected String warningMessage;
    protected boolean warningMessageTracker = false;
    
    protected int userClass;
    protected boolean userClassTracker = false;
    
    protected String userClassName;
    protected boolean userClassNameTracker = false;
    
    protected int resourcePermission;
    protected boolean resourcePermissionTracker = false;

    private static String generatePrefix(final String namespace)
    {
        if (namespace.equals("http://remotelabs.eng.uts.edu.au/schedserver/session"))
        {
            return "ns1";
        }
        return BeanUtil.getUniquePrefix();
    }

    public boolean getIsReady()
    {
        return this.isReady;
    }

    public void setIsReady(final boolean param)
    {
        this.isReadyTracker = true;
        this.isReady = param;
    }

    public boolean getIsCodeAssigned()
    {
        return this.isCodeAssigned;
    }

    public void setIsCodeAssigned(final boolean param)
    {
        this.isCodeAssignedTracker = true;
        this.isCodeAssigned = param;
    }

    public ResourceIDType getResource()
    {
        return this.resource;
    }

    public void setResource(final ResourceIDType param)
    {
        if (param != null)
        {
            this.resourceTracker = true;
        }
        else
        {
            this.resourceTracker = false;
        }

        this.resource = param;
    }
    
    public String getRigType()
    {
        return this.rigType;
    }
    
    public void setRigType(final String param)
    {
        if (param != null)
        {
            this.rigTypeTracker = true;
        }
        else
        {
            this.rigTypeTracker = false;
        }
        this.rigType = param;
    }

    public String getContactURL()
    {
        return this.contactURL;
    }

    public void setContactURL(final String param)
    {
        if (param != null)
        {
            this.contactURLTracker = true;
        }
        else
        {
            this.contactURLTracker = false;
        }

        this.contactURL = param;
    }

    public int getTime()
    {
        return this.time;
    }

    public void setTime(final int param)
    {
        if (param == Integer.MIN_VALUE)
        {
            this.timeTracker = false;
        }
        else
        {
            this.timeTracker = true;
        }

        this.time = param;
    }

    public int getTimeLeft()
    {
        return this.timeLeft;
    }

    public void setTimeLeft(final int param)
    {
        if (param == Integer.MIN_VALUE)
        {
            this.timeLeftTracker = false;
        }
        else
        {
            this.timeLeftTracker = true;
        }

        this.timeLeft = param;
    }

    public int getExtensions()
    {
        return this.extensions;
    }

    public void setExtensions(final int param)
    {
        if (param == Integer.MIN_VALUE)
        {
            this.extensionsTracker = false;
        }
        else
        {
            this.extensionsTracker = true;
        }

        this.extensions = param;
    }

    public String getWarningMessage()
    {
        return this.warningMessage;
    }

    public void setWarningMessage(final String param)
    {
        if (param != null)
        {
            this.warningMessageTracker = true;
        }
        else
        {
            this.warningMessageTracker = false;
        }

        this.warningMessage = param;
    }
    
    public int getUserClass()
    {
        return this.userClass;
    }
    
    public void setUserClass(final int param)
    {
        this.userClassTracker = param == Integer.MIN_VALUE;
        this.userClass = param;
    }
    
    public String getUserClassName()
    {
        return this.userClassName;
    }
    
    public void setUserClassName(final String param)
    {
        this.userClassNameTracker = param == null;
        this.userClassName = param;
    }
    
    public int getResourcePermission()
    {
        return this.resourcePermission;
    }
    
    public void setResourcePermission(final int param)
    {
        this.resourcePermissionTracker = param == Integer.MIN_VALUE;
        this.resourcePermission = param;
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
                SessionType.this.serialize(this.parentQName, factory, xmlWriter);
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
                    prefix = SessionType.generatePrefix(namespace);
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
                "http://remotelabs.eng.uts.edu.au/schedserver/session");
        if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0))
        {
            this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
                    + ":SessionType", xmlWriter);
        }
        else
        {
            this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "SessionType", xmlWriter);
        }

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = SessionType.generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "isInSession", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            else
            {
                xmlWriter.writeStartElement(namespace, "isInSession");
            }

        }
        else
        {
            xmlWriter.writeStartElement("isInSession");
        }
        xmlWriter.writeCharacters(ConverterUtil.convertToString(this.isInSession));
        xmlWriter.writeEndElement();

        if (this.isReadyTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = SessionType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "isReady", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "isReady");
                }
            }
            else
            {
                xmlWriter.writeStartElement("isReady");
            }

            xmlWriter.writeCharacters(ConverterUtil.convertToString(this.isReady));
            xmlWriter.writeEndElement();
        }

        if (this.isCodeAssignedTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = SessionType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "isCodeAssigned", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "isCodeAssigned");
                }
            }
            else
            {
                xmlWriter.writeStartElement("isCodeAssigned");
            }
            xmlWriter.writeCharacters(ConverterUtil.convertToString(this.isCodeAssigned));
            xmlWriter.writeEndElement();
        }

        if (this.resourceTracker)
        {
            if (this.resource == null)
            {
                throw new ADBException("resource cannot be null!!");
            }
            this.resource.serialize(new QName("", "resource"), factory, xmlWriter);
        }
        
        if (this.rigTypeTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = SessionType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "rigType", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "rigType");
                }
            }
            else
            {
                xmlWriter.writeStartElement("rigType");
            }

            if (this.rigType == null)
            {
                throw new ADBException("rigType cannot be null!!");
            }
            else
            {
                xmlWriter.writeCharacters(this.rigType);
            }
            xmlWriter.writeEndElement();
        }

        if (this.contactURLTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = SessionType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "contactURL", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "contactURL");
                }
            }
            else
            {
                xmlWriter.writeStartElement("contactURL");
            }

            if (this.contactURL == null)
            {
                throw new ADBException("contactURL cannot be null!!");
            }
            else
            {
                xmlWriter.writeCharacters(this.contactURL);
            }
            xmlWriter.writeEndElement();
        }

        if (this.timeTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = SessionType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "time", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "time");
                }
            }
            else
            {
                xmlWriter.writeStartElement("time");
            }

            if (this.time == Integer.MIN_VALUE)
            {
                throw new ADBException("time cannot be null!!");
            }
            else
            {
                xmlWriter.writeCharacters(ConverterUtil.convertToString(this.time));
            }
            xmlWriter.writeEndElement();
        }

        if (this.timeLeftTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = SessionType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "timeLeft", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "timeLeft");
                }
            }
            else
            {
                xmlWriter.writeStartElement("timeLeft");
            }

            if (this.timeLeft == Integer.MIN_VALUE)
            {
                throw new ADBException("timeLeft cannot be null!!");
            }
            else
            {
                xmlWriter.writeCharacters(ConverterUtil.convertToString(this.timeLeft));
            }

            xmlWriter.writeEndElement();
        }

        if (this.extensionsTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = SessionType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "extensions", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "extensions");
                }
            }
            else
            {
                xmlWriter.writeStartElement("extensions");
            }

            if (this.extensions == Integer.MIN_VALUE)
            {
                throw new ADBException("extensions cannot be null!!");
            }
            else
            {
                xmlWriter.writeCharacters(ConverterUtil.convertToString(this.extensions));
            }

            xmlWriter.writeEndElement();
        }

        if (this.warningMessageTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = SessionType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "warningMessage", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "warningMessage");
                }
            }
            else
            {
                xmlWriter.writeStartElement("warningMessage");
            }

            if (this.warningMessage == null)
            {
                throw new ADBException("warningMessage cannot be null!!");
            }
            else
            {
                xmlWriter.writeCharacters(this.warningMessage);
            }
            xmlWriter.writeEndElement();
        }
        
        if (this.userClassTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = SessionType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "userClass", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "userClass");
                }
            }
            else
            {
                xmlWriter.writeStartElement("userClass");
            }

            xmlWriter.writeCharacters(ConverterUtil.convertToString(this.userClass));
            xmlWriter.writeEndElement();
        }
        
        if (this.userClassNameTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = SessionType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "userClassName", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "userClassName");
                }
            }
            else
            {
                xmlWriter.writeStartElement("userClassName");
            }

            xmlWriter.writeCharacters(this.userClassName);
            xmlWriter.writeEndElement();
        }
        
        if (this.resourcePermissionTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = SessionType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "resourcePermission", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "resourcePermission");
                }
            }
            else
            {
                xmlWriter.writeStartElement("resourcePermission");
            }

            xmlWriter.writeCharacters(ConverterUtil.convertToString(this.resourcePermission));
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
            prefix = SessionType.generatePrefix(namespace);
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
        attribList.add(new QName("http://remotelabs.eng.uts.edu.au/schedserver/session", "SessionType"));

        elementList.add(new QName("", "isInSession"));
        elementList.add(ConverterUtil.convertToString(this.isInSession));

        if (this.isReadyTracker)
        {
            elementList.add(new QName("", "isReady"));
            elementList.add(ConverterUtil.convertToString(this.isReady));
        }

        if (this.isCodeAssignedTracker)
        {
            elementList.add(new QName("", "isCodeAssigned"));
            elementList.add(ConverterUtil.convertToString(this.isCodeAssigned));
        }

        if (this.resourceTracker)
        {
            elementList.add(new QName("", "resource"));
            if (this.resource == null)
            {
                throw new ADBException("resource cannot be null!!");
            }
            elementList.add(this.resource);
        }

        if (this.rigTypeTracker)
        {
            elementList.add(new QName("", "rigType"));
            if (this.rigType == null)
            {
                throw new ADBException("rigType cannot be null.");
            }
            elementList.add(this.rigType);
        }
        
        if (this.contactURLTracker)
        {
            elementList.add(new QName("", "contactURL"));
            if (this.contactURL != null)
            {
                elementList.add(ConverterUtil.convertToString(this.contactURL));
            }
            else
            {
                throw new ADBException("contactURL cannot be null!!");
            }
        }

        if (this.timeTracker)
        {
            elementList.add(new QName("", "time"));
            elementList.add(ConverterUtil.convertToString(this.time));
        }

        if (this.timeLeftTracker)
        {
            elementList.add(new QName("", "timeLeft"));
            elementList.add(ConverterUtil.convertToString(this.timeLeft));
        }

        if (this.extensionsTracker)
        {
            elementList.add(new QName("", "extensions"));
            elementList.add(ConverterUtil.convertToString(this.extensions));
        }

        if (this.warningMessageTracker)
        {
            elementList.add(new QName("", "warningMessage"));
            if (this.warningMessage != null)
            {
                elementList.add(ConverterUtil.convertToString(this.warningMessage));
            }
            else
            {
                throw new ADBException("warningMessage cannot be null!!");
            }
        }
        
        if (this.userClassTracker)
        {
            elementList.add(new QName("", "userClass"));
            elementList.add(ConverterUtil.convertToString(this.userClass));
        }
        
        if (this.userClassNameTracker)
        {
            elementList.add(new QName("", "userClassName"));
            elementList.add(this.userClassName);
        }
        
        if (this.resourcePermissionTracker)
        {
            elementList.add(new QName("", "resourcePermission"));
            elementList.add(ConverterUtil.convertToString(this.resourcePermission));
        }

        return new ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());
    }

    public static class Factory
    {

        public static SessionType parse(final XMLStreamReader reader) throws Exception
        {
            final SessionType object = new SessionType();
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
                        if (!"SessionType".equals(type))
                        {
                            final String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (SessionType) ExtensionMapper.getTypeObject(nsUri, type, reader);
                        }
                    }
                }

                reader.next();
                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "isInSession").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setIsInSession(ConverterUtil.convertToBoolean(content));
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
                if (reader.isStartElement() && new QName("", "isReady").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setIsReady(ConverterUtil.convertToBoolean(content));
                    reader.next();
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "isCodeAssigned").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setIsCodeAssigned(ConverterUtil.convertToBoolean(content));
                    reader.next();
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
                
                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "rigType").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setRigType(content);
                    reader.next();
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "contactURL").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setContactURL(ConverterUtil.convertToString(content));
                    reader.next();
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "time").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setTime(ConverterUtil.convertToInt(content));
                    reader.next();
                }
                else
                {
                    object.setTime(Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "timeLeft").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setTimeLeft(ConverterUtil.convertToInt(content));
                    reader.next();
                }
                else
                {
                    object.setTimeLeft(Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "extensions").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setExtensions(ConverterUtil.convertToInt(content));
                    reader.next();
                }
                else
                {
                    object.setExtensions(Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "warningMessage").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setWarningMessage(ConverterUtil.convertToString(content));
                    reader.next();
                }
                
                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "userClass").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setUserClass(ConverterUtil.convertToInt(content));
                    reader.next();
                }
                
                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "userClassName").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setUserClassName(ConverterUtil.convertToString(content));
                    reader.next();
                }
                
                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "resourcePermission").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setResourcePermission(ConverterUtil.convertToInt(content));
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
