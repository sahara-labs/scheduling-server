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
 * @date 8th November 2010
 */

package au.edu.uts.eng.remotelabs.schedserver.bookings.intf.types;

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
 * BookingType bean class.
 */
public class BookingType extends BookingIDType implements ADBBean
{
    /*
     * This type was generated from the piece of schema that had
     * name = BookingType
     * Namespace URI = http://remotelabs.eng.uts.edu.au/schedserver/bookings
     * Namespace Prefix = ns1
     */

    private static final long serialVersionUID = -4835899445783457143L;
    
    protected ResourceIDType bookedResource;
    protected boolean bookedResourceTracker;
    
    protected PermissionIDType permissionID;
    
    protected Calendar startTime;
    
    protected Calendar endTime;
    
    protected int duration;
    protected boolean durationTracker;
    
    protected String displayName;
    protected boolean displayNameTracker;
    
    protected boolean isFinished;
    protected boolean isFinishedTracker;

    protected boolean isCancelled;
    protected boolean isCancelledTracker = false;
    
    protected String cancelReason;
    protected boolean cancelReasonTracker = false;

    protected String codeReference;
    protected boolean codeReferenceTracker = false;

    private static String generatePrefix(final String namespace)
    {
        if (namespace.equals("http://remotelabs.eng.uts.edu.au/schedserver/bookings"))
        {
            return "ns1";
        }
        return BeanUtil.getUniquePrefix();
    }

    public ResourceIDType getBookedResource()
    {
        return this.bookedResource;
    }

    public void setBookedResource(final ResourceIDType param)
    {
        if (param == null)
        {
            this.bookedResourceTracker = false;
        }
        else
        {
            this.bookedResourceTracker = true;
        }
        this.bookedResource = param;
    }

    public PermissionIDType getPermissionID()
    {
        return this.permissionID;
    }

    public void setPermissionID(final PermissionIDType param)
    {
        this.permissionID = param;
    }

    public Calendar getStartTime()
    {
        return this.startTime;
    }

    public void setStartTime(final Calendar param)
    {
        this.startTime = param;
    }
    
    public Calendar getEndTime()
    {
        return this.endTime;
    }
    
    public void setEndTime(final Calendar param)
    {
        this.endTime = param;
    }

    public int getDuration()
    {
        return this.duration;
    }

    public void setDuration(final int param)
    {
        if (param == Integer.MIN_VALUE)
        {
            this.durationTracker = false;
        }
        else
        {
            this.durationTracker = true;
        }
        this.duration = param;
    }
    
    public String getDisplayName()
    {
        return this.displayName;
    }
    
    public void setDisplayName(final String param)
    {
       this.displayNameTracker = param != null;
       this.displayName = param;
            
    }

    public boolean getIsFinished()
    {
        return this.isFinished;
    }

    public void setIsFinished(final boolean param)
    {
        this.isFinishedTracker = true;
        this.isFinished = param;
    }

    public boolean getIsCancelled()
    {
        return this.isCancelled;
    }

    public void setIsCancelled(final boolean param)
    {
        this.isCancelledTracker = true;
        this.isCancelled = param;
    }

    public String getCancelReason()
    {
        return this.cancelReason;
    }

    public void setCancelReason(final String param)
    {
        if (param != null)
        {
            this.cancelReasonTracker = true;
        }
        else
        {
            this.cancelReasonTracker = false;
        }

        this.cancelReason = param;
    }

    public String getCodeReference()
    {
        return this.codeReference;
    }

    public void setCodeReference(final String param)
    {
        if (param != null)
        {
            this.codeReferenceTracker = true;
        }
        else
        {
            this.codeReferenceTracker = false;
        }

        this.codeReference = param;
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
                BookingType.this.serialize(this.parentQName, factory, xmlWriter);
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
                    prefix = BookingType.generatePrefix(namespace);
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
                "http://remotelabs.eng.uts.edu.au/schedserver/bookings");
        if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0))
        {
            this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
                    + ":BookingType", xmlWriter);
        }
        else
        {
            this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "BookingType", xmlWriter);
        }

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = BookingType.generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "bookingID", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            else
            {
                xmlWriter.writeStartElement(namespace, "bookingID");
            }
        }
        else
        {
            xmlWriter.writeStartElement("bookingID");
        }

        if (this.bookingID == Integer.MIN_VALUE)
        {
            throw new ADBException("bookingID cannot be null!!");
        }
        else
        {
            xmlWriter.writeCharacters(ConverterUtil.convertToString(this.bookingID));
        }
        xmlWriter.writeEndElement();
        
        if (this.bookedResourceTracker)
        {
            if (this.bookedResource == null)
            {
                throw new ADBException("bookedResource cannot be null!!");
            }
            this.bookedResource.serialize(new QName("", "bookedResource"), factory, xmlWriter);
        }

        if (this.permissionID == null)
        {
            throw new ADBException("permissionID cannot be null!!");
        }
        this.permissionID.serialize(new QName("", "permissionID"), factory, xmlWriter);

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null)
            {
                prefix = BookingType.generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "startTime", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            else
            {
                xmlWriter.writeStartElement(namespace, "startTime");
            }
        }
        else
        {
            xmlWriter.writeStartElement("startTime");
        }

        if (this.startTime == null)
        {
            throw new ADBException("startTime cannot be null!!");
        }
        else
        {
            xmlWriter.writeCharacters(ConverterUtil.convertToString(this.startTime));
        }
        xmlWriter.writeEndElement();
        
        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null)
            {
                prefix = BookingType.generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "endTime", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            else
            {
                xmlWriter.writeStartElement(namespace, "endTime");
            }
        }
        else
        {
            xmlWriter.writeStartElement("endTime");
        }

        if (this.endTime == null)
        {
            throw new ADBException("endTime cannot be null!!");
        }
        else
        {
            xmlWriter.writeCharacters(ConverterUtil.convertToString(this.endTime));
        }
        xmlWriter.writeEndElement();

        if (this.durationTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
    
                if (prefix == null)
                {
                    prefix = BookingType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "duration", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "duration");
                }
            }
            else
            {
                xmlWriter.writeStartElement("duration");
            }
    
            if (this.duration == Integer.MIN_VALUE)
            {
                throw new ADBException("duration cannot be null!!");
            }
            else
            {
                xmlWriter.writeCharacters(ConverterUtil.convertToString(this.duration));
            }
            xmlWriter.writeEndElement();
        }
        
        if (this.displayNameTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = BookingType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "displayName", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "displayName");
                }
            }
            else
            {
                xmlWriter.writeStartElement("displayName");
            }

            if (this.displayName == null)
            {
                throw new ADBException("displayName cannot be null");
            }
            else
            {
                xmlWriter.writeCharacters(this.displayName);
            }
            xmlWriter.writeEndElement();
        }
        
        if (this.isFinishedTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = BookingType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "isFinished", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "isFinished");
                }
            }
            else
            {
                xmlWriter.writeStartElement("isFinished");
            }

            xmlWriter.writeCharacters(ConverterUtil.convertToString(this.isFinished));
            xmlWriter.writeEndElement();
        }

        if (this.isCancelledTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = BookingType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "isCancelled", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "isCancelled");
                }
            }
            else
            {
                xmlWriter.writeStartElement("isCancelled");
            }

            xmlWriter.writeCharacters(ConverterUtil.convertToString(this.isCancelled));
            xmlWriter.writeEndElement();
        }

        if (this.cancelReasonTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = BookingType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "cancelReason", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "cancelReason");
                }
            }
            else
            {
                xmlWriter.writeStartElement("cancelReason");
            }

            if (this.cancelReason == null)
            {
                throw new ADBException("cancelReason cannot be null!!");
            }
            else
            {
                xmlWriter.writeCharacters(this.cancelReason);
            }
            xmlWriter.writeEndElement();
        }

        if (this.codeReferenceTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = BookingType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "codeReference", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "codeReference");
                }
            }
            else
            {
                xmlWriter.writeStartElement("codeReference");
            }

            if (this.codeReference == null)
            {
                throw new ADBException("codeReference cannot be null!!");
            }
            else
            {
                xmlWriter.writeCharacters(this.codeReference);
            }
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
            prefix = BookingType.generatePrefix(namespace);

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
        attribList.add(new QName("http://remotelabs.eng.uts.edu.au/schedserver/bookings", "BookingType"));

        elementList.add(new QName("", "bookingID"));
        elementList.add(ConverterUtil.convertToString(this.bookingID));

        if (this.bookedResourceTracker)
        {
            elementList.add(new QName("", "bookedResource"));
            if (this.bookedResource == null)
            {
                throw new ADBException("bookedResource cannot be null!!");
            }
            elementList.add(this.bookedResource);
        }

        elementList.add(new QName("", "permissionID"));
        if (this.permissionID == null)
        {
            throw new ADBException("permissionID cannot be null!!");
        }
        elementList.add(this.permissionID);

        elementList.add(new QName("", "startTime"));
        if (this.startTime != null)
        {
            elementList.add(ConverterUtil.convertToString(this.startTime));
        }
        else
        {
            throw new ADBException("startTime cannot be null!!");
        }
        
        elementList.add(new QName("", "endTime"));
        if (this.endTime != null)
        {
            elementList.add(ConverterUtil.convertToString(this.endTime));
        }
        else
        {
            throw new ADBException("endTime cannot be null");
        }

        if (this.durationTracker)
        {
            elementList.add(new QName("", "duration"));
            elementList.add(ConverterUtil.convertToString(this.duration));
        }
        
        if (this.displayNameTracker)
        {
            elementList.add(new QName("", "displayName"));
            elementList.add(this.displayName);
        }

        if (this.isFinishedTracker)
        {
            elementList.add(new QName("", "isFinished"));
            elementList.add(ConverterUtil.convertToString(this.isFinished));
        }

        if (this.isCancelledTracker)
        {
            elementList.add(new QName("", "isCancelled"));
            elementList.add(ConverterUtil.convertToString(this.isCancelled));
        }

        if (this.cancelReasonTracker)
        {
            elementList.add(new QName("", "cancelReason"));
            if (this.cancelReason != null)
            {
                elementList.add(ConverterUtil.convertToString(this.cancelReason));
            }
            else
            {
                throw new ADBException("cancelReason cannot be null!!");
            }
        }

        if (this.codeReferenceTracker)
        {
            elementList.add(new QName("", "codeReference"));
            if (this.codeReference != null)
            {
                elementList.add(ConverterUtil.convertToString(this.codeReference));
            }
            else
            {
                throw new ADBException("codeReference cannot be null!!");
            }
        }

        return new ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());
    }

    public static class Factory
    {
        public static BookingType parse(final XMLStreamReader reader) throws Exception
        {
            final BookingType object = new BookingType();
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
                        if (!"BookingType".equals(type))
                        {
                            final String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (BookingType) ExtensionMapper.getTypeObject(nsUri, type, reader);
                        }
                    }
                }

                reader.next();
                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "bookingID").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setBookingID(ConverterUtil.convertToInt(content));
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

                if (reader.isStartElement() && new QName("", "bookedResource").equals(reader.getName()))
                {
                    object.setBookedResource(ResourceIDType.Factory.parse(reader));
                    reader.next();
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "permissionID").equals(reader.getName()))
                {
                    object.setPermissionID(PermissionIDType.Factory.parse(reader));
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
                if (reader.isStartElement() && new QName("", "startTime").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setStartTime(ConverterUtil.convertToDateTime(content));
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
                if (reader.isStartElement() && new QName("", "endTime").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setEndTime(ConverterUtil.convertToDateTime(content));
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
                
                if (reader.isStartElement() && new QName("", "duration").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setDuration(ConverterUtil.convertToInt(content));
                    reader.next();
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                
                if (reader.isStartElement() && new QName("", "displayName").equals(reader.getName()))
                {
                    object.setDisplayName(reader.getElementText());
                    reader.next();
                }
                
                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "isFinished").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setIsFinished(ConverterUtil.convertToBoolean(content));
                    reader.next();
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "isCancelled").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setIsCancelled(ConverterUtil.convertToBoolean(content));
                    reader.next();
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "cancelReason").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setCancelReason(ConverterUtil.convertToString(content));
                    reader.next();
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "codeReference").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setCodeReference(ConverterUtil.convertToString(content));
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
