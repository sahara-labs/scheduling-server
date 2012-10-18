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
 * @date 5th August 2011
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
 * QueueType bean class.
 */
public class QueueType implements ADBBean
{
    /*
     * This type was generated from the piece of schema that had
     * name = QueueType
     * Namespace URI = http://remotelabs.eng.uts.edu.au/schedserver/multisite
     * Namespace Prefix = ns1
     */

    private static final long serialVersionUID = 3767325274804297870L;

    protected boolean inQueue;

    public boolean getInQueue()
    {
        return this.inQueue;
    }

    public void setInQueue(final boolean param)
    {
        this.inQueue = param;
    }

    protected boolean inSession;

    public boolean getInSession()
    {
        return this.inSession;
    }

    public void setInSession(final boolean param)
    {
        this.inSession = param;
    }

    protected boolean inBooking;

    public boolean getInBooking()
    {
        return this.inBooking;
    }

    public void setInBooking(final boolean param)
    {
        this.inBooking = param;
    }

    protected ResourceType queuedResource;
    protected boolean queuedResourceTracker = false;

    public boolean isQueuedResourceSpecified()
    {
        return this.queuedResourceTracker;
    }

    public ResourceType getQueuedResource()
    {
        return this.queuedResource;
    }

    public void setQueuedResource(final ResourceType param)
    {
        this.queuedResourceTracker = param != null;
        this.queuedResource = param;
    }

    protected int position;

    public int getPosition()
    {
        return this.position;
    }

    public void setPosition(final int param)
    {
        this.position = param;
    }

    protected int time;

    public int getTime()
    {
        return this.time;
    }

    public void setTime(final int param)
    {
        this.time = param;
    }

    @Override
    public OMElement getOMElement(final QName parentQName, final OMFactory factory) throws ADBException
    {
        final OMDataSource dataSource = new ADBDataSource(this, parentQName);
        return factory.createOMElement(dataSource, parentQName);
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
                        + ":QueueType", xmlWriter);
            }
            else
            {
                this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "QueueType", xmlWriter);
            }
        }
        
        namespace = "";
        this.writeStartElement(null, namespace, "inQueue", xmlWriter);
        xmlWriter.writeCharacters(ConverterUtil.convertToString(this.inQueue));
        xmlWriter.writeEndElement();

        namespace = "";
        this.writeStartElement(null, namespace, "inSession", xmlWriter);
        xmlWriter.writeCharacters(ConverterUtil.convertToString(this.inSession));
        xmlWriter.writeEndElement();

        namespace = "";
        this.writeStartElement(null, namespace, "inBooking", xmlWriter);
        xmlWriter.writeCharacters(ConverterUtil.convertToString(this.inBooking));
        xmlWriter.writeEndElement();
        
        if (this.queuedResourceTracker)
        {
            if (this.queuedResource == null)
            {
                throw new ADBException("queuedResource cannot be null!!");
            }
            this.queuedResource.serialize(new QName("", "queuedResource"), xmlWriter);
        }
        
        namespace = "";
        this.writeStartElement(null, namespace, "position", xmlWriter);
        if (this.position == Integer.MIN_VALUE)
        {
            throw new ADBException("position cannot be null!!");
        }
        else
        {
            xmlWriter.writeCharacters(ConverterUtil.convertToString(this.position));
        }
        xmlWriter.writeEndElement();

        namespace = "";
        this.writeStartElement(null, namespace, "time", xmlWriter);
        if (this.time == Integer.MIN_VALUE)
        {
            throw new ADBException("time cannot be null!!");
        }
        else
        {
            xmlWriter.writeCharacters(ConverterUtil.convertToString(this.time));
        }
        xmlWriter.writeEndElement();

        xmlWriter.writeEndElement();
    }

    private static String generatePrefix(final String namespace)
    {
        if (namespace.equals("http://remotelabs.eng.uts.edu.au/schedserver/multisite"))
        {
            return "ns1";
        }
        return BeanUtil.getUniquePrefix();
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
                prefix = QueueType.generatePrefix(namespace);
            }

            xmlWriter.writeStartElement(prefix, localPart, namespace);
            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
        }
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
            prefix = QueueType.generatePrefix(namespace);
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

        elementList.add(new QName("", "inQueue"));
        elementList.add(ConverterUtil.convertToString(this.inQueue));

        elementList.add(new QName("", "inSession"));
        elementList.add(ConverterUtil.convertToString(this.inSession));

        elementList.add(new QName("", "inBooking"));
        elementList.add(ConverterUtil.convertToString(this.inBooking));
        
        if (this.queuedResourceTracker)
        {
            elementList.add(new QName("", "queuedResource"));
            if (this.queuedResource == null)
            {
                throw new ADBException("queuedResource cannot be null!!");
            }
            elementList.add(this.queuedResource);
        }
        
        elementList.add(new QName("", "position"));
        elementList.add(ConverterUtil.convertToString(this.position));

        elementList.add(new QName("", "time"));
        elementList.add(ConverterUtil.convertToString(this.time));

        return new ADBXMLStreamReaderImpl(qName, elementList.toArray(), new Object[0]);
    }

    public static class Factory
    {
        public static QueueType parse(final XMLStreamReader reader) throws Exception
        {
            final QueueType object = new QueueType();
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
                        if (!"QueueType".equals(type))
                        {
                            final String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (QueueType) ExtensionMapper.getTypeObject(nsUri, type, reader);
                        }
                    }
                }

                reader.next();
                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                if (reader.isStartElement() && new QName("", "inQueue").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setInQueue(ConverterUtil.convertToBoolean(content));
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
                if (reader.isStartElement() && new QName("", "inSession").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setInSession(ConverterUtil.convertToBoolean(content));
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
                if (reader.isStartElement() && new QName("", "inBooking").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setInBooking(ConverterUtil.convertToBoolean(content));
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

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "position").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setPosition(ConverterUtil.convertToInt(content));
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
                if (reader.isStartElement() && new QName("", "time").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setTime(ConverterUtil.convertToInt(content));
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
