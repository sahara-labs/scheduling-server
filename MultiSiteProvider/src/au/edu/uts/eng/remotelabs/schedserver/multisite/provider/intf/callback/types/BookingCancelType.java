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
 * @date 28th August 2011
 */


package au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.callback.types;

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

import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.BookingType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.SiteIDType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types.UserIDType;

/**
 * BookingCancelType bean class
 */
public class BookingCancelType extends SiteIDType implements ADBBean
{
    private static final long serialVersionUID = -7311971154096157447L;

    protected UserIDType user;

    public UserIDType getUser()
    {
        return this.user;
    }

    public void setUser(final UserIDType param)
    {
        this.user = param;
    }

    protected BookingType booking;

    public BookingType getBooking()
    {
        return this.booking;
    }

    public void setBooking(final BookingType param)
    {
        this.booking = param;
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
        final String namespacePrefix = this.registerPrefix(xmlWriter,
                "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteCallback/");
        if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0))
        {
            this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
                    + ":BookingCancelType", xmlWriter);
        }
        else
        {
            this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "BookingCancelType",
                    xmlWriter);
        }

        namespace = "";
        this.writeStartElement(null, namespace, "siteID", xmlWriter);
        if (this.siteID == null)
        {
            throw new ADBException("siteID cannot be null!!");
        }
        else
        {
            xmlWriter.writeCharacters(this.siteID);
        }
        xmlWriter.writeEndElement();

        if (this.user == null)
        {
            throw new ADBException("user cannot be null!!");
        }
        this.user.serialize(new QName("", "user"), xmlWriter);

        if (this.booking == null)
        {
            throw new ADBException("booking cannot be null!!");
        }
        this.booking.serialize(new QName("", "booking"), xmlWriter);

        xmlWriter.writeEndElement();
    }

    private static String generatePrefix(final String namespace)
    {
        if (namespace.equals("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteCallback/"))
        {
            return "ns2";
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
                prefix = BookingCancelType.generatePrefix(namespace);
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
            prefix = BookingCancelType.generatePrefix(namespace);
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
        attribList
                .add(new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteCallback/", "BookingCancelType"));
        elementList.add(new QName("", "siteID"));

        if (this.siteID != null)
        {
            elementList.add(ConverterUtil.convertToString(this.siteID));
        }
        else
        {
            throw new ADBException("siteID cannot be null!!");
        }

        elementList.add(new QName("", "user"));
        if (this.user == null)
        {
            throw new ADBException("user cannot be null!!");
        }
        elementList.add(this.user);

        elementList.add(new QName("", "booking"));
        if (this.booking == null)
        {
            throw new ADBException("booking cannot be null!!");
        }
        elementList.add(this.booking);

        return new ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());
    }

    public static class Factory
    {
        public static BookingCancelType parse(final XMLStreamReader reader) throws Exception
        {
            final BookingCancelType object = new BookingCancelType();
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

                        if (!"BookingCancelType".equals(type))
                        {
                            final String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (BookingCancelType) ExtensionMapper.getTypeObject(nsUri, type, reader);
                        }
                    }
                }

                reader.next();
                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "siteID").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setSiteID(ConverterUtil.convertToString(content));
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
                if (reader.isStartElement() && new QName("", "user").equals(reader.getName()))
                {
                    object.setUser(UserIDType.Factory.parse(reader));
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
                if (reader.isStartElement() && new QName("", "booking").equals(reader.getName()))
                {
                    object.setBooking(BookingType.Factory.parse(reader));
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
