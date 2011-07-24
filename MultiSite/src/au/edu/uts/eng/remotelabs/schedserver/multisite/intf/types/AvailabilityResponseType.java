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
import java.util.List;

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
 * AvailabilityResponseType bean class.
 */
public class AvailabilityResponseType implements ADBBean
{
    /*
     * This type was generated from the piece of schema that had
     * name = AvailabilityResponseType
     * Namespace URI = http://remotelabs.eng.uts.edu.au/schedserver/multisite
     * Namespace Prefix = ns1
     */

    private static final long serialVersionUID = 8891307562109218510L;
    
    protected boolean viable;

    protected boolean hasFree;

    protected boolean isQueueable;

    protected boolean isBookable;

    protected boolean isCodeAssignable;

    protected ResourceType queuedResource;
    
    protected boolean queueTargetTracker = false;
    protected QueueTargetType[] queueTarget;

    private static String generatePrefix(final String namespace)
    {
        if (namespace.equals("http://remotelabs.eng.uts.edu.au/schedserver/multisite"))
        {
            return "ns1";
        }
        return BeanUtil.getUniquePrefix();
    }

    public void addQueueTarget(final QueueTargetType param)
    {
        if (this.queueTarget == null)
        {
            this.queueTarget = new QueueTargetType[] {};
        }

        this.queueTargetTracker = true;

        @SuppressWarnings("unchecked")
        final List<QueueTargetType> list = ConverterUtil.toList(this.queueTarget);
        list.add(param);
        this.queueTarget = list.toArray(new QueueTargetType[list.size()]);
    }

    public boolean getHasFree()
    {
        return this.hasFree;
    }

    public boolean getIsBookable()
    {
        return this.isBookable;
    }

    public boolean getIsCodeAssignable()
    {
        return this.isCodeAssignable;
    }

    public boolean getIsQueueable()
    {
        return this.isQueueable;
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

        elementList.add(new QName("", "viable"));
        elementList.add(ConverterUtil.convertToString(this.viable));

        elementList.add(new QName("", "hasFree"));
        elementList.add(ConverterUtil.convertToString(this.hasFree));

        elementList.add(new QName("", "isQueueable"));
        elementList.add(ConverterUtil.convertToString(this.isQueueable));

        elementList.add(new QName("", "isBookable"));
        elementList.add(ConverterUtil.convertToString(this.isBookable));

        elementList.add(new QName("", "isCodeAssignable"));
        elementList.add(ConverterUtil.convertToString(this.isCodeAssignable));

        elementList.add(new QName("", "queuedResource"));
        if (this.queuedResource == null)
        {
            throw new ADBException("queuedResource cannot be null!!");
        }
        elementList.add(this.queuedResource);
        
        if (this.queueTargetTracker)
        {
            if (this.queueTarget != null)
            {
                for (final QueueTargetType element : this.queueTarget)
                {
                    if (element != null)
                    {
                        elementList.add(new QName("", "queueTarget"));
                        elementList.add(element);
                    }
                }
            }
            else
            {
                throw new ADBException("queueTarget cannot be null!!");
            }
        }

        return new ADBXMLStreamReaderImpl(qName, elementList.toArray(), new Object[0]);
    }

    public ResourceType getQueuedResource()
    {
        return this.queuedResource;
    }

    public QueueTargetType[] getQueueTarget()
    {
        return this.queueTarget;
    }

    public boolean getViable()
    {
        return this.viable;
    }

    public boolean isQueueTargetSpecified()
    {
        return this.queueTargetTracker;
    }

    private String registerPrefix(final XMLStreamWriter xmlWriter, final String namespace) throws XMLStreamException
    {
        String prefix = xmlWriter.getPrefix(namespace);
        if (prefix == null)
        {
            prefix = AvailabilityResponseType.generatePrefix(namespace);
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
                        + ":AvailabilityResponseType", xmlWriter);
            }
            else
            {
                this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type",
                        "AvailabilityResponseType", xmlWriter);
            }
        }

        namespace = "";
        this.writeStartElement(null, namespace, "viable", xmlWriter);
        xmlWriter.writeCharacters(ConverterUtil.convertToString(this.viable));
        xmlWriter.writeEndElement();

        namespace = "";
        this.writeStartElement(null, namespace, "hasFree", xmlWriter);
        xmlWriter.writeCharacters(ConverterUtil.convertToString(this.hasFree));
        xmlWriter.writeEndElement();

        namespace = "";
        this.writeStartElement(null, namespace, "isQueueable", xmlWriter);
        xmlWriter.writeCharacters(ConverterUtil.convertToString(this.isQueueable));
        xmlWriter.writeEndElement();

        namespace = "";
        this.writeStartElement(null, namespace, "isBookable", xmlWriter);
        xmlWriter.writeCharacters(ConverterUtil.convertToString(this.isBookable));
        xmlWriter.writeEndElement();

        namespace = "";
        this.writeStartElement(null, namespace, "isCodeAssignable", xmlWriter);
        xmlWriter.writeCharacters(ConverterUtil.convertToString(this.isCodeAssignable));
        xmlWriter.writeEndElement();

        if (this.queuedResource == null)
        {
            throw new ADBException("queuedResource cannot be null!!");
        }
        this.queuedResource.serialize(new QName("", "queuedResource"), xmlWriter);
        
        if (this.queueTargetTracker)
        {
            if (this.queueTarget != null)
            {
                for (final QueueTargetType element : this.queueTarget)
                {
                    if (element != null)
                    {
                        element.serialize(new QName("", "queueTarget"), xmlWriter);
                    }
                }
            }
            else
            {
                throw new ADBException("queueTarget cannot be null!!");
            }
        }
        xmlWriter.writeEndElement();
    }

    public void setHasFree(final boolean param)
    {
        this.hasFree = param;
    }

    public void setIsBookable(final boolean param)
    {
        this.isBookable = param;
    }

    public void setIsCodeAssignable(final boolean param)
    {
        this.isCodeAssignable = param;
    }

    public void setIsQueueable(final boolean param)
    {
        this.isQueueable = param;
    }

    public void setQueuedResource(final ResourceType param)
    {
        this.queuedResource = param;
    }

    public void setQueueTarget(final QueueTargetType[] param)
    {
        this.queueTargetTracker = param != null;
        this.queueTarget = param;
    }

    public void setViable(final boolean param)
    {
        this.viable = param;
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
                prefix = AvailabilityResponseType.generatePrefix(namespace);
            }

            xmlWriter.writeStartElement(prefix, localPart, namespace);
            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
        }
    }

    public static class Factory
    {
        public static AvailabilityResponseType parse(final XMLStreamReader reader) throws Exception
        {
            final AvailabilityResponseType object = new AvailabilityResponseType();

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
                        if (!"AvailabilityResponseType".equals(type))
                        {
                            final String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (AvailabilityResponseType) ExtensionMapper.getTypeObject(nsUri, type, reader);
                        }
                    }
                }

                reader.next();
                final ArrayList<QueueTargetType> qtList = new ArrayList<QueueTargetType>();
                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "viable").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setViable(ConverterUtil.convertToBoolean(content));
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
                if (reader.isStartElement() && new QName("", "hasFree").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setHasFree(ConverterUtil.convertToBoolean(content));
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
                if (reader.isStartElement() && new QName("", "isQueueable").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setIsQueueable(ConverterUtil.convertToBoolean(content));
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
                if (reader.isStartElement() && new QName("", "isBookable").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setIsBookable(ConverterUtil.convertToBoolean(content));
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
                if (reader.isStartElement() && new QName("", "isCodeAssignable").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setIsCodeAssignable(ConverterUtil.convertToBoolean(content));
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
                if (reader.isStartElement() && new QName("", "queuedResource").equals(reader.getName()))
                {
                    object.setQueuedResource(ResourceType.Factory.parse(reader));
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
                if (reader.isStartElement() && new QName("", "queueTarget").equals(reader.getName()))
                {
                    qtList.add(QueueTargetType.Factory.parse(reader));

                    boolean noMore = false;
                    while (!noMore)
                    {
                        while (!reader.isEndElement())
                        {
                            reader.next();
                        }

                        reader.next();
                        while (!reader.isStartElement() && !reader.isEndElement())
                        {
                            reader.next();
                        }
                        if (reader.isEndElement())
                        {
                            noMore = true;
                        }
                        else
                        {
                            if (new QName("", "queueTarget").equals(reader.getName()))
                            {
                                qtList.add(QueueTargetType.Factory.parse(reader));
                            }
                            else
                            {
                                noMore = true;
                            }
                        }
                    }

                    object.setQueueTarget((QueueTargetType[]) ConverterUtil.convertToArray(QueueTargetType.class, qtList));
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
