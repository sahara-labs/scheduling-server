/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2011, University of Technology, Sydney
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
 * @date 29th January 2011
 */

package au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types;

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
 * PutRigOfflineType bean class.
 */
public class PutRigOfflineType extends OperationRequestType implements ADBBean
{
    /*
     * This type was generated from the piece of schema that had
     * name = PutRigOfflineType
     * Namespace URI = http://remotelabs.eng.uts.edu.au/rigmanagement
     * Namespace Prefix = ns1
     */

    private static final long serialVersionUID = 8642573656837998746L;

    private static String generatePrefix(final String namespace)
    {
        if (namespace.equals("http://remotelabs.eng.uts.edu.au/rigmanagement"))
        {
            return "ns1";
        }
        return BeanUtil.getUniquePrefix();
    }

    protected RigIDType rig;

    public RigIDType getRig()
    {
        return this.rig;
    }

    public void setRig(final RigIDType param)
    {
        this.rig = param;
    }

    protected Calendar start;

    public Calendar getStart()
    {
        return this.start;
    }

    public void setStart(final Calendar param)
    {
        this.start = param;
    }

    protected Calendar end;

    public Calendar getEnd()
    {
        return this.end;
    }

    public void setEnd(final Calendar param)
    {
        this.end = param;
    }

    protected String reason;

    public String getReason()
    {
        return this.reason;
    }

    public void setReason(final String param)
    {
        this.reason = param;
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
                PutRigOfflineType.this.serialize(this.parentQName, factory, xmlWriter);
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
                    prefix = PutRigOfflineType.generatePrefix(namespace);
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

        final String namespacePrefix = this.registerPrefix(xmlWriter, "http://remotelabs.eng.uts.edu.au/rigmanagement");
        if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0))
        {
            this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
                    + ":PutRigOfflineType", xmlWriter);
        }
        else
        {
            this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "PutRigOfflineType",
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
                    prefix = PutRigOfflineType.generatePrefix(namespace);
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

        if (this.requestorNSNameSequenceTracker)
        {
            if (this.requestorNSNameSequence == null)
            {
                throw new ADBException("OperationRequestTypeSequence_type0 cannot be null!!");
            }
            this.requestorNSNameSequence.serialize(null, factory, xmlWriter);
        }

        if (this.requestorQNameTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = PutRigOfflineType.generatePrefix(namespace);
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

        if (this.rig == null)
        {
            throw new ADBException("rig cannot be null!!");
        }
        this.rig.serialize(new QName("", "rig"), factory, xmlWriter);

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null)
            {
                prefix = PutRigOfflineType.generatePrefix(namespace);
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

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null)
            {
                prefix = PutRigOfflineType.generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "end", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            else
            {
                xmlWriter.writeStartElement(namespace, "end");
            }
        }
        else
        {
            xmlWriter.writeStartElement("end");
        }

        if (this.end == null)
        {
            throw new ADBException("end cannot be null!!");
        }
        else
        {
            xmlWriter.writeCharacters(ConverterUtil.convertToString(this.end));
        }
        xmlWriter.writeEndElement();

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null)
            {
                prefix = PutRigOfflineType.generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "reason", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            else
            {
                xmlWriter.writeStartElement(namespace, "reason");
            }
        }
        else
        {
            xmlWriter.writeStartElement("reason");
        }

        if (this.reason == null)
        {
            throw new ADBException("reason cannot be null!!");
        }
        else
        {
            xmlWriter.writeCharacters(this.reason);
        }

        xmlWriter.writeEndElement();

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
            prefix = PutRigOfflineType.generatePrefix(namespace);
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
        attribList.add(new QName("http://remotelabs.eng.uts.edu.au/rigmanagement", "PutRigOfflineType"));

        if (this.requestorIDTracker)
        {
            elementList.add(new QName("", "requestorID"));
            elementList.add(ConverterUtil.convertToString(this.requestorID));
        }

        if (this.requestorNSNameSequenceTracker)
        {
            elementList.add(new QName("http://remotelabs.eng.uts.edu.au/rigmanagement",
                    "OperationRequestTypeSequence_type0"));
            if (this.requestorNSNameSequence == null)
            {
                throw new ADBException("OperationRequestTypeSequence_type0 cannot be null!!");
            }
            elementList.add(this.requestorNSNameSequence);
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

        elementList.add(new QName("", "rig"));
        if (this.rig == null)
        {
            throw new ADBException("rig cannot be null!!");
        }
        elementList.add(this.rig);

        elementList.add(new QName("", "start"));
        if (this.start != null)
        {
            elementList.add(ConverterUtil.convertToString(this.start));
        }
        else
        {
            throw new ADBException("start cannot be null!!");
        }

        elementList.add(new QName("", "end"));
        if (this.end != null)
        {
            elementList.add(ConverterUtil.convertToString(this.end));
        }
        else
        {
            throw new ADBException("end cannot be null!!");
        }

        elementList.add(new QName("", "reason"));
        if (this.reason != null)
        {
            elementList.add(ConverterUtil.convertToString(this.reason));
        }
        else
        {
            throw new ADBException("reason cannot be null!!");
        }

        return new ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());
    }

    public static class Factory
    {
        public static PutRigOfflineType parse(final XMLStreamReader reader) throws Exception
        {
            final PutRigOfflineType object = new PutRigOfflineType();
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
                        if (!"PutRigOfflineType".equals(type))
                        {
                            final String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (PutRigOfflineType) ExtensionMapper.getTypeObject(nsUri, type, reader);
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
                        object.setRequestorNSNameSequence(RequestorNSNameSequence.Factory.parse(reader));
                    }
                }
                catch (final Exception e)
                { /* Min-occurs is zero. */ }

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

                if (reader.isStartElement() && new QName("", "rig").equals(reader.getName()))
                {
                    object.setRig(RigIDType.Factory.parse(reader));
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
                if (reader.isStartElement() && new QName("", "start").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setStart(ConverterUtil.convertToDateTime(content));
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
                if (reader.isStartElement() && new QName("", "end").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setEnd(ConverterUtil.convertToDateTime(content));
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
                if (reader.isStartElement() && new QName("", "reason").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setReason(ConverterUtil.convertToString(content));
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
