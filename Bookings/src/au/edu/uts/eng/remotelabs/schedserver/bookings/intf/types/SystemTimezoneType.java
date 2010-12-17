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
 * @date 17th December 2010
 */

package au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types;

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
 * SystemTimezoneType bean class.
 */
public class SystemTimezoneType implements ADBBean
{
    /*
     * This type was generated from the piece of schema that had
     * name = SystemTimezoneType
     * Namespace URI = http://remotelabs.eng.uts.edu.au/schedserver/bookings
     * Namespace Prefix = ns1
     */

    private static final long serialVersionUID = 1014558000219603430L;
    
    protected String systemTimezone;
    
    protected int offsetFromUTC;
    
    protected TimezoneType[] supportedTimezones;

    private static String generatePrefix(final String namespace)
    {
        if (namespace.equals("http://remotelabs.eng.uts.edu.au/schedserver/bookings"))
        {
            return "ns1";
        }
        return BeanUtil.getUniquePrefix();
    }

    public String getSystemTimezone()
    {
        return this.systemTimezone;
    }

    public void setSystemTimezone(final String param)
    {
        this.systemTimezone = param;
    }

    public int getOffsetFromUTC()
    {
        return this.offsetFromUTC;
    }

    public void setOffsetFromUTC(final int param)
    {
        this.offsetFromUTC = param;
    }

    public TimezoneType[] getSupportedTimezones()
    {
        return this.supportedTimezones;
    }

    protected void validateSupportedTimezones(final TimezoneType[] param)
    {
        if ((param != null) && (param.length < 1))
        {
            throw new RuntimeException();
        }
    }

    public void setSupportedTimezones(final TimezoneType[] param)
    {
        this.validateSupportedTimezones(param);
        this.supportedTimezones = param;
    }

    @SuppressWarnings("unchecked")
    public void addSupportedTimezones(final TimezoneType param)
    {
        if (this.supportedTimezones == null)
        {
            this.supportedTimezones = new TimezoneType[] {};
        }

        final List<TimezoneType> list = ConverterUtil.toList(this.supportedTimezones);
        list.add(param);
        this.supportedTimezones = list.toArray(new TimezoneType[list.size()]);
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
                SystemTimezoneType.this.serialize(this.parentQName, factory, xmlWriter);
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
                    prefix = SystemTimezoneType.generatePrefix(namespace);
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
                    "http://remotelabs.eng.uts.edu.au/schedserver/bookings");
            if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0))
            {
                this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
                        + ":SystemTimezoneType", xmlWriter);
            }
            else
            {
                this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "SystemTimezoneType",
                        xmlWriter);
            }
        }

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null)
            {
                prefix = SystemTimezoneType.generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "systemTimezone", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            else
            {
                xmlWriter.writeStartElement(namespace, "systemTimezone");
            }
        }
        else
        {
            xmlWriter.writeStartElement("systemTimezone");
        }

        if (this.systemTimezone == null)
        {
            throw new ADBException("systemTimezone cannot be null!!");
        }
        else
        {
            xmlWriter.writeCharacters(this.systemTimezone);
        }
        xmlWriter.writeEndElement();

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = SystemTimezoneType.generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "offsetFromUTC", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            else
            {
                xmlWriter.writeStartElement(namespace, "offsetFromUTC");
            }
        }
        else
        {
            xmlWriter.writeStartElement("offsetFromUTC");
        }

        if (this.offsetFromUTC == Integer.MIN_VALUE)
        {
            throw new ADBException("offsetFromUTC cannot be null!!");
        }
        else
        {
            xmlWriter.writeCharacters(ConverterUtil.convertToString(this.offsetFromUTC));
        }

        xmlWriter.writeEndElement();
        if (this.supportedTimezones != null)
        {
            for (final TimezoneType localSupportedTimezone : this.supportedTimezones)
            {
                if (localSupportedTimezone != null)
                {
                    localSupportedTimezone.serialize(new QName("", "supportedTimezones"), factory, xmlWriter);
                }
                else
                {
                    throw new ADBException("supportedTimezones cannot be null!!");
                }
            }
        }
        else
        {
            throw new ADBException("supportedTimezones cannot be null!!");
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
            prefix = SystemTimezoneType.generatePrefix(namespace);
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

        elementList.add(new QName("", "systemTimezone"));
        if (this.systemTimezone != null)
        {
            elementList.add(ConverterUtil.convertToString(this.systemTimezone));
        }
        else
        {
            throw new ADBException("systemTimezone cannot be null!!");
        }

        elementList.add(new QName("", "offsetFromUTC"));
        elementList.add(ConverterUtil.convertToString(this.offsetFromUTC));

        if (this.supportedTimezones != null)
        {
            for (final TimezoneType localSupportedTimezone : this.supportedTimezones)
            {
                if (localSupportedTimezone != null)
                {
                    elementList.add(new QName("", "supportedTimezones"));
                    elementList.add(localSupportedTimezone);
                }
                else
                {
                    throw new ADBException("supportedTimezones cannot be null !!");
                }
            }
        }
        else
        {
            throw new ADBException("supportedTimezones cannot be null!!");
        }

        return new ADBXMLStreamReaderImpl(qName, elementList.toArray(), new Object[0]);
    }

    public static class Factory
    {

        public static SystemTimezoneType parse(final XMLStreamReader reader) throws Exception
        {
            final SystemTimezoneType object = new SystemTimezoneType();

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
                        if (!"SystemTimezoneType".equals(type))
                        {
                            final String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (SystemTimezoneType) ExtensionMapper.getTypeObject(nsUri, type, reader);
                        }
                    }
                }

                reader.next();
                final ArrayList<TimezoneType> tzList = new ArrayList<TimezoneType>();
                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                if (reader.isStartElement() && new QName("", "systemTimezone").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setSystemTimezone(ConverterUtil.convertToString(content));
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
                if (reader.isStartElement() && new QName("", "offsetFromUTC").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setOffsetFromUTC(ConverterUtil.convertToInt(content));
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
                if (reader.isStartElement() && new QName("", "supportedTimezones").equals(reader.getName()))
                {
                    tzList.add(TimezoneType.Factory.parse(reader));

                    boolean noMore = false;
                    while (!noMore)
                    {
                        while (!reader.isEndElement())
                        {
                            reader.next();
                        }

                        reader.next();                        // Step to next element event.
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
                            if (new QName("", "supportedTimezones").equals(reader.getName()))
                            {
                                tzList.add(TimezoneType.Factory.parse(reader));
                            }
                            else
                            {
                                noMore = true;
                            }
                        }
                    }


                    object.setSupportedTimezones((TimezoneType[]) ConverterUtil.convertToArray(TimezoneType.class,
                            tzList));
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
