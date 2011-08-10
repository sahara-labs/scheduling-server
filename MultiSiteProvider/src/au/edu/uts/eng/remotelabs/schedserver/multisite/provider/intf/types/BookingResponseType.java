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

package au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types;

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
 * BookingResponseType bean class
 */
public class BookingResponseType extends OperationResponseType implements ADBBean
{
    private static final long serialVersionUID = 5663030886271555788L;

    protected BookingIDType bookingID;
    protected boolean bookingIDTracker = false;

    protected TimePeriodType[] bestFits;
    protected boolean bestFitsTracker = false;

    private static String generatePrefix(final String namespace)
    {
        if (namespace.equals("http://remotelabs.eng.uts.edu.au/schedserver/multisite"))
        {
            return "ns1";
        }
        return BeanUtil.getUniquePrefix();
    }

    public void addBestFits(final TimePeriodType param)
    {
        if (this.bestFits == null)
        {
            this.bestFits = new TimePeriodType[] {};
        }
        this.bestFitsTracker = true;

        @SuppressWarnings("unchecked")
        final List<TimePeriodType> list = ConverterUtil.toList(this.bestFits);
        list.add(param);
        this.bestFits = list.toArray(new TimePeriodType[list.size()]);
    }

    public TimePeriodType[] getBestFits()
    {
        return this.bestFits;
    }

    public BookingIDType getBookingID()
    {
        return this.bookingID;
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
        final ArrayList<QName> attribList = new ArrayList<QName>();

        attribList.add(new QName("http://www.w3.org/2001/XMLSchema-instance", "type"));
        attribList.add(new QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite", "BookingResponseType"));

        elementList.add(new QName("", "wasSuccessful"));
        elementList.add(ConverterUtil.convertToString(this.wasSuccessful));

        if (this.reasonTracker)
        {
            elementList.add(new QName("", "reason"));

            if (this.reason != null)
            {
                elementList.add(ConverterUtil.convertToString(this.reason));
            }
            else
            {
                throw new ADBException("reason cannot be null!!");
            }
        }

        if (this.bookingIDTracker)
        {
            elementList.add(new QName("", "bookingID"));

            if (this.bookingID == null)
            {
                throw new ADBException("bookingID cannot be null!!");
            }
            elementList.add(this.bookingID);
        }

        if (this.bestFitsTracker)
        {
            if (this.bestFits != null)
            {
                for (final TimePeriodType bestFit : this.bestFits)
                {
                    if (bestFit != null)
                    {
                        elementList.add(new QName("", "bestFits"));
                        elementList.add(bestFit);
                    }
                }
            }
            else
            {
                throw new ADBException("bestFits cannot be null!!");
            }
        }

        return new ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());
    }

    public boolean isBestFitsSpecified()
    {
        return this.bestFitsTracker;
    }

    public boolean isBookingIDSpecified()
    {
        return this.bookingIDTracker;
    }

    private String registerPrefix(final XMLStreamWriter xmlWriter, final String namespace) throws XMLStreamException
    {
        String prefix = xmlWriter.getPrefix(namespace);
        if (prefix == null)
        {
            prefix = BookingResponseType.generatePrefix(namespace);
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
        final String prefix = parentQName.getPrefix();
        String namespace = parentQName.getNamespaceURI();
        this.writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);

        final String namespacePrefix = this.registerPrefix(xmlWriter,
                "http://remotelabs.eng.uts.edu.au/schedserver/multisite");
        if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0))
        {
            this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
                    + ":BookingResponseType", xmlWriter);
        }
        else
        {
            this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "BookingResponseType",
                    xmlWriter);
        }

        namespace = "";
        this.writeStartElement(null, namespace, "wasSuccessful", xmlWriter);
        xmlWriter.writeCharacters(ConverterUtil.convertToString(this.wasSuccessful));
        xmlWriter.writeEndElement();

        if (this.reasonTracker)
        {
            namespace = "";
            this.writeStartElement(null, namespace, "reason", xmlWriter);
            if (this.reason == null)
            {
                throw new ADBException("reason cannot be null!!");
            }
            else
            {
                xmlWriter.writeCharacters(this.reason);
            }
            xmlWriter.writeEndElement();
        }

        if (this.bookingIDTracker)
        {
            if (this.bookingID == null)
            {
                throw new ADBException("bookingID cannot be null!!");
            }
            this.bookingID.serialize(new QName("", "bookingID"), xmlWriter);
        }

        if (this.bestFitsTracker)
        {
            if (this.bestFits != null)
            {
                for (final TimePeriodType bestFit : this.bestFits)
                {
                    if (bestFit != null)
                    {
                        bestFit.serialize(new QName("", "bestFits"), xmlWriter);
                    }
                }
            }
            else
            {
                throw new ADBException("bestFits cannot be null!!");
            }
        }

        xmlWriter.writeEndElement();
    }

    public void setBestFits(final TimePeriodType[] param)
    {
        this.bestFitsTracker = param != null;
        this.bestFits = param;
    }

    public void setBookingID(final BookingIDType param)
    {
        this.bookingIDTracker = param != null;
        this.bookingID = param;
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
                prefix = BookingResponseType.generatePrefix(namespace);
            }

            xmlWriter.writeStartElement(prefix, localPart, namespace);
            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
        }
    }

    public static class Factory
    {
        public static BookingResponseType parse(final XMLStreamReader reader) throws Exception
        {
            final BookingResponseType object = new BookingResponseType();
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
                        if (!"BookingResponseType".equals(type))
                        {
                            final String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (BookingResponseType) ExtensionMapper.getTypeObject(nsUri, type, reader);
                        }
                    }
                }

                reader.next();
                final ArrayList<TimePeriodType> bestFitList = new ArrayList<TimePeriodType>();
                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                if (reader.isStartElement() && new QName("", "wasSuccessful").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setWasSuccessful(ConverterUtil.convertToBoolean(content));
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
                if (reader.isStartElement() && new QName("", "reason").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setReason(ConverterUtil.convertToString(content));
                    reader.next();
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "bookingID").equals(reader.getName()))
                {
                    object.setBookingID(BookingIDType.Factory.parse(reader));
                    reader.next();
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "bestFits").equals(reader.getName()))
                {
                    bestFitList.add(TimePeriodType.Factory.parse(reader));
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
                            if (new QName("", "bestFits").equals(reader.getName()))
                            {
                                bestFitList.add(TimePeriodType.Factory.parse(reader));
                            }
                            else
                            {
                                noMore = true;
                            }
                        }
                    }

                    object.setBestFits((TimePeriodType[]) ConverterUtil.convertToArray(TimePeriodType.class, bestFitList));
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
