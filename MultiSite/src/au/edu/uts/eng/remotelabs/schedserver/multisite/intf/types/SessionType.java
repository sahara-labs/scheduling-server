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

package au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.axiom.om.OMDataSource;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axis2.databinding.ADBBean;
import org.apache.axis2.databinding.ADBDataSource;
import org.apache.axis2.databinding.ADBException;
import org.apache.axis2.databinding.utils.BeanUtil;
import org.apache.axis2.databinding.utils.ConverterUtil;
import org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl;

/**
 * SessionType bean class.
 */
public class SessionType implements ADBBean
{
    /*
     * This type was generated from the piece of schema that had
     * name = SessionType
     * Namespace URI = http://remotelabs.eng.uts.edu.au/schedserver/multisite
     * Namespace Prefix = ns1
     */
    private static final long serialVersionUID = -5973050738372973568L;

    protected boolean isReady;

    protected boolean isCodeAssigned;

    protected ResourceType resource;
    protected boolean resourceTracker = false;

    protected String rigType;
    protected boolean rigTypeTracker = false;
    
    protected String rigName;
    protected boolean rigNameTracker = false;

    protected String contactURL;
    protected boolean contactURLTracker = false;

    protected int duration;
    protected int extensions;
    protected int timeLeft;

    protected boolean inGrace;
    
    protected String warningMessage;
    protected boolean warningMessageTracker = false;

    private static String generatePrefix(final String namespace)
    {
        if (namespace.equals("http://remotelabs.eng.uts.edu.au/schedserver/multisite"))
        {
            return "ns1";
        }
        return BeanUtil.getUniquePrefix();
    }

    public String getContactURL()
    {
        return this.contactURL;
    }

    public int getExtensions()
    {
        return this.extensions;
    }

    public boolean getIsCodeAssigned()
    {
        return this.isCodeAssigned;
    }

    public boolean getIsReady()
    {
        return this.isReady;
    }
    
    public boolean getInGrace()
    {
        return this.inGrace;
    }

    @Override
    public OMElement getOMElement(final QName parentQName, final OMFactory factory) throws ADBException
    {
        final OMDataSource dataSource = new ADBDataSource(this, parentQName);
        return factory.createOMElement(dataSource, parentQName);
    }

    @Override
    public XMLStreamReader getPullParser(final QName qName) throws ADBException
    {
        final ArrayList<Serializable> elementList = new ArrayList<Serializable>();

        elementList.add(new QName("", "isReady"));
        elementList.add(ConverterUtil.convertToString(this.isReady));

        elementList.add(new QName("", "isCodeAssigned"));
        elementList.add(ConverterUtil.convertToString(this.isCodeAssigned));
        
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
            if (this.rigType != null)
            {
                elementList.add(ConverterUtil.convertToString(this.rigType));
            }
            else
            {
                throw new ADBException("rigType cannot be null!!");
            }
        }
        
        if (this.rigNameTracker)
        {
            elementList.add(new QName("", "rigName"));
            if (this.rigName != null)
            {
                elementList.add(ConverterUtil.convertToString(this.rigName));
            }
            else
            {
                throw new ADBException("rigName cannot be null");
            }
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
        
        elementList.add(new QName("", "duration"));
        elementList.add(ConverterUtil.convertToString(this.duration));
        
        elementList.add(new QName("", "extensions"));
        elementList.add(ConverterUtil.convertToString(this.extensions));

        elementList.add(new QName("", "timeLeft"));
        elementList.add(ConverterUtil.convertToString(this.timeLeft));
        
        elementList.add(new QName("", "inGrace"));
        elementList.add(ConverterUtil.convertToString(this.inGrace));
        
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

        return new ADBXMLStreamReaderImpl(qName, elementList.toArray(), new Object[0]);
    }

    public ResourceType getResource()
    {
        return this.resource;
    }

    public String getRigType()
    {
        return this.rigType;
    }
    
    public String getRigName()
    {
        return this.rigName;
    }

    public int getDuration()
    {
        return this.duration;
    }

    public int getTimeLeft()
    {
        return this.timeLeft;
    }

    public String getWarningMessage()
    {
        return this.warningMessage;
    }

    public boolean isContactURLSpecified()
    {
        return this.contactURLTracker;
    }

    public boolean isResourceSpecified()
    {
        return this.resourceTracker;
    }

    public boolean isRigTypeSpecified()
    {
        return this.rigTypeTracker;
    }

    public boolean isWarningMessageSpecified()
    {
        return this.warningMessageTracker;
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
    public void serialize(final QName parentQName, final XMLStreamWriter xmlWriter) throws XMLStreamException,
            ADBException
    {
        this.serialize(parentQName, xmlWriter, false);
    }

    @Override
    public void serialize(final QName parentQName, final XMLStreamWriter xmlWriter, final boolean serializeType)
            throws XMLStreamException, ADBException
    {
        String prefix = parentQName.getPrefix();
        String namespace = parentQName.getNamespaceURI();
        this.writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

        if (serializeType)
        {
            final String namespacePrefix = this.registerPrefix(xmlWriter,
                    "http://remotelabs.eng.uts.edu.au/schedserver/multisite");
            if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0))
            {
                this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
                        + ":SessionType", xmlWriter);
            }
            else
            {
                this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "SessionType",
                        xmlWriter);
            }
        }

        namespace = "";
        this.writeStartElement(null, namespace, "isReady", xmlWriter);
        xmlWriter.writeCharacters(ConverterUtil.convertToString(this.isReady));
        xmlWriter.writeEndElement();

        namespace = "";
        this.writeStartElement(null, namespace, "isCodeAssigned", xmlWriter);
        xmlWriter.writeCharacters(ConverterUtil.convertToString(this.isCodeAssigned));
        xmlWriter.writeEndElement();
        
        if (this.resourceTracker)
        {
            if (this.resource == null)
            {
                throw new ADBException("resource cannot be null!!");
            }
            this.resource.serialize(new QName("", "resource"), xmlWriter);
        }
        
        if (this.rigTypeTracker)
        {
            namespace = "";
            this.writeStartElement(null, namespace, "rigType", xmlWriter);
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
        
        if (this.rigNameTracker)
        {
            namespace = "";
            this.writeStartElement(null, namespace, "rigName", xmlWriter);
            if (this.rigName == null)
            {
                throw new ADBException("rigName cannot be null");
            }
            else
            {
                xmlWriter.writeCharacters(this.rigName);
            }
            xmlWriter.writeEndElement();
        }
        
        if (this.contactURLTracker)
        {
            namespace = "";
            this.writeStartElement(null, namespace, "contactURL", xmlWriter);
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
        
        namespace = "";
        this.writeStartElement(null, namespace, "duration", xmlWriter);
        if (this.duration == Integer.MIN_VALUE)
        {
            throw new ADBException("duration cannot be null!!");
        }
        else
        {
            xmlWriter.writeCharacters(ConverterUtil.convertToString(this.duration));
        }
        xmlWriter.writeEndElement();
        
                namespace = "";
        this.writeStartElement(null, namespace, "extensions", xmlWriter);
        if (this.extensions == Integer.MIN_VALUE)
        {
            throw new ADBException("extensions cannot be null!!");
        }
        else
        {
            xmlWriter.writeCharacters(ConverterUtil.convertToString(this.extensions));
        }
        xmlWriter.writeEndElement();
        
        namespace = "";
        this.writeStartElement(null, namespace, "timeLeft", xmlWriter);
        if (this.timeLeft == Integer.MIN_VALUE)
        {
            throw new ADBException("timeLeft cannot be null!!");
        }
        else
        {
            xmlWriter.writeCharacters(ConverterUtil.convertToString(this.timeLeft));
        }
        xmlWriter.writeEndElement();

        namespace = "";
        this.writeStartElement(null, namespace, "inGrace", xmlWriter);
        xmlWriter.writeCharacters(ConverterUtil.convertToString(this.inGrace));
        xmlWriter.writeEndElement();
        
        if (this.warningMessageTracker)
        {
            namespace = "";
            this.writeStartElement(null, namespace, "warningMessage", xmlWriter);
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
        
        xmlWriter.writeEndElement();
    }

    public void setContactURL(final String param)
    {
        this.contactURLTracker = param != null;
        this.contactURL = param;
    }

    public void setExtensions(final int param)
    {
        this.extensions = param;
    }

    public void setIsCodeAssigned(final boolean param)
    {
        this.isCodeAssigned = param;
    }

    public void setIsReady(final boolean param)
    {
        this.isReady = param;
    }
    
    public void setInGrace(final boolean param)
    {
        this.inGrace = param;
    }

    public void setResource(final ResourceType param)
    {
        this.resourceTracker = param != null;
        this.resource = param;
    }
    
    public void setRigName(final String param)
    {
        this.rigNameTracker = param != null;
        this.rigName = param;
    }

    public void setRigType(final String param)
    {
        this.rigTypeTracker = param != null;
        this.rigType = param;
    }

    public void setDuration(final int param)
    {
        this.duration = param;
    }

    public void setTimeLeft(final int param)
    {
        this.timeLeft = param;
    }

    public void setWarningMessage(final String param)
    {
        this.warningMessageTracker = param != null;
        this.warningMessage = param;
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

    private void writeStartElement(String prefix, final String namespace, final String localPart,
            final XMLStreamWriter xmlWriter) throws XMLStreamException
    {
        final String writerPrefix = xmlWriter.getPrefix(namespace);
        if (writerPrefix != null)
        {
            xmlWriter.writeStartElement(namespace, localPart);
        }
        else
        {
            if (namespace.length() == 0)
            {
                prefix = "";
            }
            else if (prefix == null)
            {
                prefix = SessionType.generatePrefix(namespace);
            }

            xmlWriter.writeStartElement(prefix, localPart, namespace);
            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
        }
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

                if (reader.isStartElement() && new QName("", "isReady").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setIsReady(ConverterUtil.convertToBoolean(content));
                    reader.next();
                }
                else
                {
                    throw new ADBException("Unexpected subelement " + reader.getName());
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
                else
                {
                    throw new ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "resource").equals(reader.getName()))
                {
                    object.setResource(ResourceType.Factory.parse(reader));
                    reader.next();
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "rigType").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setRigType(ConverterUtil.convertToString(content));
                    reader.next();
                }
                
                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "rigName").equals(reader.getName()))
                {
                    object.setRigName(ConverterUtil.convertToString(reader.getElementText()));
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
                if (reader.isStartElement() && new QName("", "duration").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setDuration(ConverterUtil.convertToInt(content));
                    reader.next();
                }
                else
                {
                    throw new ADBException("Unexpected subelement " + reader.getName());
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
                    throw new ADBException("Unexpected subelement " + reader.getName());
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
                    throw new ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "inGrace").equals(reader.getName()))
                {
                    object.setInGrace(ConverterUtil.convertToBoolean(reader.getElementText()));
                    reader.next();
                }
                else
                {
                    throw new ADBException("Unexpected subelement " + reader.getName());
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
                if (reader.isStartElement())
                {
                    throw new ADBException("Unexpected subelement " + reader.getName());
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
