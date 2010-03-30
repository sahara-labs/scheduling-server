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

package au.edu.uts.eng.remotelabs.schedserver.queuer.intf.types;

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
 * UserQueueType bean class.
 */
public class UserQueueType extends InQueueType implements ADBBean
{
    /*
     * This type was generated from the piece of schema that had
     * name = UserQueueType
     * Namespace URI = http://remotelabs.eng.uts.edu.au/schedserver/queuer
     * Namespace Prefix = ns1
     */
    private static final long serialVersionUID = 5420048072377251360L;
    
    protected int position;
    protected int time;
    protected QueueType queue;
    protected boolean queueTracker = false;

    private static String generatePrefix(final String namespace)
    {
        if (namespace.equals("http://remotelabs.eng.uts.edu.au/schedserver/queuer"))
        {
            return "ns1";
        }
        return BeanUtil.getUniquePrefix();
    }

    public int getPosition()
    {
        return this.position;
    }

    public void setPosition(final int param)
    {
        this.position = param;
    }

    public int getTime()
    {
        return this.time;
    }

    public void setTime(final int param)
    {
        this.time = param;
    }

    public QueueType getQueue()
    {
        return this.queue;
    }

    public void setQueue(final QueueType param)
    {
        if (param != null)
        {
            this.queueTracker = true;
        }
        else
        {
            this.queueTracker = false;
        }

        this.queue = param;
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
                UserQueueType.this.serialize(this.parentQName, factory, xmlWriter);
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
                    prefix = UserQueueType.generatePrefix(namespace);
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
                "http://remotelabs.eng.uts.edu.au/schedserver/queuer");
        if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0))
        {
            this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
                    + ":UserQueueType", xmlWriter);
        }
        else
        {
            this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "UserQueueType", xmlWriter);
        }

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null)
            {
                prefix = UserQueueType.generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "inQueue", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            else
            {
                xmlWriter.writeStartElement(namespace, "inQueue");
            }
        }
        else
        {
            xmlWriter.writeStartElement("inQueue");
        }
        xmlWriter.writeCharacters(ConverterUtil.convertToString(this.inQueue));
        xmlWriter.writeEndElement();

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = UserQueueType.generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "inSession", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            else
            {
                xmlWriter.writeStartElement(namespace, "inSession");
            }
        }
        else
        {
            xmlWriter.writeStartElement("inSession");
        }
        xmlWriter.writeCharacters(ConverterUtil.convertToString(this.inSession));
        xmlWriter.writeEndElement();
        
        if (this.queueSuccessfulTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = UserQueueType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "queueSuccessful", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "queueSuccessful");
                }
            }
            else
            {
                xmlWriter.writeStartElement("queueSuccessful");
            }
            xmlWriter.writeCharacters(ConverterUtil.convertToString(this.queueSuccessful));
            xmlWriter.writeEndElement();
        }
        
        if (this.assignedResourceTracker)
        {
            if (this.assignedResource == null)
            {
                throw new ADBException("assignedResource cannot be null!!");
            }
            this.assignedResource.serialize(new QName("", "assignedResource"), factory, xmlWriter);
        }
        
        if (this.queuedResouceTracker)
        {
            if (this.queuedResouce == null)
            {
                throw new ADBException("queuedResouce cannot be null!!");
            }
            this.queuedResouce.serialize(new QName("", "queuedResouce"), factory, xmlWriter);
        }
        
        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = UserQueueType.generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "position", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            else
            {
                xmlWriter.writeStartElement(namespace, "position");
            }
        }
        else
        {
            xmlWriter.writeStartElement("position");
        }

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
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null)
            {
                prefix = UserQueueType.generatePrefix(namespace);
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
        if (this.queueTracker)
        {
            if (this.queue == null)
            {
                throw new ADBException("queue cannot be null!!");
            }
            this.queue.serialize(new QName("", "queue"), factory, xmlWriter);
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
            prefix = UserQueueType.generatePrefix(namespace);
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
        attribList.add(new QName("http://remotelabs.eng.uts.edu.au/schedserver/queuer", "UserQueueType"));
        
        elementList.add(new QName("", "inQueue"));
        elementList.add(ConverterUtil.convertToString(this.inQueue));
        elementList.add(new QName("", "inSession"));

        elementList.add(ConverterUtil.convertToString(this.inSession));
        if (this.queueSuccessfulTracker)
        {
            elementList.add(new QName("", "queueSuccessful"));
            elementList.add(ConverterUtil.convertToString(this.queueSuccessful));
        }
        
        if (this.assignedResourceTracker)
        {
            elementList.add(new QName("", "assignedResource"));
            if (this.assignedResource == null)
            {
                throw new ADBException("assignedResource cannot be null!!");
            }
            elementList.add(this.assignedResource);
        }
        
        if (this.queuedResouceTracker)
        {
            elementList.add(new QName("", "queuedResouce"));
            if (this.queuedResouce == null)
            {
                throw new ADBException("queuedResouce cannot be null!!");
            }
            elementList.add(this.queuedResouce);
        }
        
        elementList.add(new QName("", "position"));
        elementList.add(ConverterUtil.convertToString(this.position));

        elementList.add(new QName("", "time"));
        elementList.add(ConverterUtil.convertToString(this.time));
        
        if (this.queueTracker)
        {
            elementList.add(new QName("", "queue"));
            if (this.queue == null)
            {
                throw new ADBException("queue cannot be null!!");
            }
            elementList.add(this.queue);
        }

        return new ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());
    }

    public static class Factory
    {
        public static UserQueueType parse(final XMLStreamReader reader) throws Exception
        {
            final UserQueueType object = new UserQueueType();
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
                        if (!"UserQueueType".equals(type))
                        {
                            //find namespace for the prefix
                            final String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (UserQueueType) ExtensionMapper.getTypeObject(nsUri, type, reader);
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
                    throw new ADBException("Unexpected subelement " + reader.getLocalName());
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
                    throw new ADBException("Unexpected subelement " + reader.getLocalName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "queueSuccessful").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setQueueSuccessful(ConverterUtil.convertToBoolean(content));
                    reader.next();
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "assignedResource").equals(reader.getName()))
                {
                    object.setAssignedResource(ResourceIDType.Factory.parse(reader));
                    reader.next();
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "queuedResouce").equals(reader.getName()))
                {
                    object.setQueuedResouce(ResourceIDType.Factory.parse(reader));
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
                    throw new ADBException("Unexpected subelement " + reader.getLocalName());
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
                    throw new ADBException("Unexpected subelement " + reader.getLocalName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "queue").equals(reader.getName()))
                {
                    object.setQueue(QueueType.Factory.parse(reader));
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
