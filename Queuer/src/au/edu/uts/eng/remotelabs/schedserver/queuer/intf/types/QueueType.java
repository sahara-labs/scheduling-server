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
import java.util.List;

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
 * QueueType bean class.
 */
public class QueueType implements ADBBean
{
    /*
     * This type was generated from the piece of schema that had
     * name = QueueType
     * Namespace URI = http://remotelabs.eng.uts.edu.au/schedserver/queuer
     * Namespace Prefix = ns1
     */

    private static final long serialVersionUID = -7208129806520045695L;
    
    protected boolean viable;
    protected boolean hasFree;
    protected ResourceIDType queuedResource;
    protected QueueTargetType[] queueTarget;
    protected boolean queueTargetTracker = false;

    private static String generatePrefix(final String namespace)
    {
        if (namespace.equals("http://remotelabs.eng.uts.edu.au/schedserver/queuer"))
        {
            return "ns1";
        }
        return BeanUtil.getUniquePrefix();
    }

    public boolean getViable()
    {
        return this.viable;
    }

    public void setViable(final boolean param)
    {
        this.viable = param;
    }

    public boolean getHasFree()
    {
        return this.hasFree;
    }

    public void setHasFree(final boolean param)
    {
        this.hasFree = param;
    }

    public ResourceIDType getQueuedResource()
    {
        return this.queuedResource;
    }

    public void setQueuedResource(final ResourceIDType param)
    {
        this.queuedResource = param;
    }

    public QueueTargetType[] getQueueTarget()
    {
        return this.queueTarget;
    }

    public void setQueueTarget(final QueueTargetType[] param)
    {
        if (param != null)
        {
            this.queueTargetTracker = true;
        }
        else
        {
            this.queueTargetTracker = false;
        }

        this.queueTarget = param;
    }

    @SuppressWarnings("unchecked")
    public void addQueueTarget(final QueueTargetType param)
    {
        if (this.queueTarget == null)
        {
            this.queueTarget = new QueueTargetType[] {};
        }

        this.queueTargetTracker = true;

        final List<QueueTargetType> list = ConverterUtil.toList(this.queueTarget);
        list.add(param);
        this.queueTarget = list.toArray(new QueueTargetType[list.size()]);
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

    public OMElement getOMElement(final QName parentQName, final OMFactory factory) throws ADBException
    {
        final OMDataSource dataSource = new ADBDataSource(this, parentQName)
        {
            @Override
            public void serialize(final MTOMAwareXMLStreamWriter xmlWriter) throws XMLStreamException
            {
                QueueType.this.serialize(this.parentQName, factory, xmlWriter);
            }
        };
        return new OMSourcedElementImpl(parentQName, factory, dataSource);
    }

    public void serialize(final QName parentQName, final OMFactory factory, final MTOMAwareXMLStreamWriter xmlWriter)
            throws XMLStreamException, ADBException
    {
        this.serialize(parentQName, factory, xmlWriter, false);
    }

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
                    prefix = QueueType.generatePrefix(namespace);
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

        if (serializeType)
        {
            final String namespacePrefix = this.registerPrefix(xmlWriter,
                    "http://remotelabs.eng.uts.edu.au/schedserver/queuer");
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
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null)
            {
                prefix = QueueType.generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "viable", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            else
            {
                xmlWriter.writeStartElement(namespace, "viable");
            }
        }
        else
        {
            xmlWriter.writeStartElement("viable");
        }
        xmlWriter.writeCharacters(ConverterUtil.convertToString(this.viable));
        xmlWriter.writeEndElement();

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null)
            {
                prefix = QueueType.generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "hasFree", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            else
            {
                xmlWriter.writeStartElement(namespace, "hasFree");
            }
        }
        else
        {
            xmlWriter.writeStartElement("hasFree");
        }
        xmlWriter.writeCharacters(ConverterUtil.convertToString(this.hasFree));
        xmlWriter.writeEndElement();

        if (this.queuedResource == null)
        {
            throw new ADBException("queuedResource cannot be null!!");
        }
        this.queuedResource.serialize(new QName("", "queuedResource"), factory, xmlWriter);
        
        if (this.queueTargetTracker)
        {
            if (this.queueTarget != null)
            {
                for (final QueueTargetType element : this.queueTarget)
                {
                    if (element != null)
                    {
                        element.serialize(new QName("", "queueTarget"), factory, xmlWriter);
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

    public XMLStreamReader getPullParser(final QName qName) throws ADBException
    {
        final ArrayList<Serializable> elementList = new ArrayList<Serializable>();

        elementList.add(new QName("", "viable"));
        elementList.add(ConverterUtil.convertToString(this.viable));
        
        elementList.add(new QName("", "hasFree"));
        elementList.add(ConverterUtil.convertToString(this.hasFree));
        
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
                final ArrayList<QueueTargetType> targetList = new ArrayList<QueueTargetType>();
                
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
                    throw new ADBException("Unexpected subelement " + reader.getLocalName());
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
                    throw new ADBException("Unexpected subelement " + reader.getLocalName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "queuedResource").equals(reader.getName()))
                {
                    object.setQueuedResource(ResourceIDType.Factory.parse(reader));
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

                if (reader.isStartElement() && new QName("", "queueTarget").equals(reader.getName()))
                {
                    targetList.add(QueueTargetType.Factory.parse(reader));
                    boolean noMoreTargets = false;
                    while (!noMoreTargets)
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
                            noMoreTargets = true;
                        }
                        else
                        {
                            if (new QName("", "queueTarget").equals(reader.getName()))
                            {
                                targetList.add(QueueTargetType.Factory.parse(reader));
                            }
                            else
                            {
                                noMoreTargets = true;
                            }
                        }
                    }

                    object.setQueueTarget((QueueTargetType[]) ConverterUtil
                            .convertToArray(QueueTargetType.class, targetList));
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
