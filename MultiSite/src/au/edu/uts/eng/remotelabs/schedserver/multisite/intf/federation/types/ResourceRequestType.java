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
 * @date 26th October 2011
 */

package au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

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

import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.ResourceType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.SiteIDType;

/**
 * ResourceRequestType bean class.
 */
public class ResourceRequestType extends SiteIDType implements ADBBean
{
    private static final long serialVersionUID = -2665163739074774718L;

    protected String permissionID;
    protected boolean permissionIDTracker = false;

    public boolean isPermissionIDSpecified()
    {
        return this.permissionIDTracker;
    }

    public String getPermissionID()
    {
        return this.permissionID;
    }

    public void setPermissionID(final String param)
    {
        this.permissionIDTracker = param != null;
        this.permissionID = param;
    }

    protected ResourceType resource;

    public ResourceType getResource()
    {
        return this.resource;
    }

    public void setResource(final ResourceType param)
    {
        this.resource = param;
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

    protected Calendar expiry;

    public Calendar getExpiry()
    {
        return this.expiry;
    }

    public void setExpiry(final Calendar param)
    {
        this.expiry = param;
    }

    protected String name;

    public String getName()
    {
        return this.name;
    }

    public void setName(final String param)
    {
        this.name = param;
    }

    protected int sessionDuration;

    public int getSessionDuration()
    {
        return this.sessionDuration;
    }

    public void setSessionDuration(final int param)
    {
        this.sessionDuration = param;
    }

    protected int allowedExtensions;

    public int getAllowedExtensions()
    {
        return this.allowedExtensions;
    }

    public void setAllowedExtensions(final int param)
    {
        this.allowedExtensions = param;
    }

    protected int extensionDuration;

    public int getExtensionDuration()
    {
        return this.extensionDuration;
    }
    public void setExtensionDuration(final int param)
    {
        this.extensionDuration = param;
    }

    protected boolean isQueuable;

    public boolean getIsQueuable()
    {
        return this.isQueuable;
    }

    public void setIsQueuable(final boolean param)
    {
        this.isQueuable = param;
    }

    protected boolean isBookable;

    public boolean getIsBookable()
    {
        return this.isBookable;
    }

    public void setIsBookable(final boolean param)
    {
        this.isBookable = param;
    }

    protected String maxBookings;
    protected boolean localMaxBookingsTracker = false;

    public boolean isMaxBookingsSpecified()
    {
        return this.localMaxBookingsTracker;
    }

    public String getMaxBookings()
    {
        return this.maxBookings;
    }

    public void setMaxBookings(final String param)
    {
        this.localMaxBookingsTracker = param != null;
        this.maxBookings = param;
    }

    protected String note;
    protected boolean noteTracker = false;

    public boolean isNoteSpecified()
    {
        return this.noteTracker;
    }

    public String getNote()
    {
        return this.note;
    }

    public void setNote(final String param)
    {
        this.noteTracker = param != null;
        this.note = param;
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
                "http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/");
        if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0))
        {
            this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
                    + ":ResourceRequestType", xmlWriter);
        }
        else
        {
            this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "ResourceRequestType",
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
        
        if (this.permissionIDTracker)
        {
            namespace = "";
            this.writeStartElement(null, namespace, "permissionID", xmlWriter);
            if (this.permissionID == null)
            {
                throw new ADBException("permissionID cannot be null!!");
            }
            else
            {
                xmlWriter.writeCharacters(this.permissionID);
            }
            xmlWriter.writeEndElement();
        }
        
        if (this.resource == null)
        {
            throw new ADBException("resource cannot be null!!");
        }
        this.resource.serialize(new QName("", "resource"), xmlWriter);

        namespace = "";
        this.writeStartElement(null, namespace, "start", xmlWriter);
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
        this.writeStartElement(null, namespace, "expiry", xmlWriter);
        if (this.expiry == null)
        {
            throw new ADBException("expiry cannot be null!!");
        }
        else
        {
            xmlWriter.writeCharacters(ConverterUtil.convertToString(this.expiry));
        }
        xmlWriter.writeEndElement();

        namespace = "";
        this.writeStartElement(null, namespace, "name", xmlWriter);
        if (this.name == null)
        {
            throw new ADBException("name cannot be null!!");
        }
        else
        {
            xmlWriter.writeCharacters(this.name);
        }
        xmlWriter.writeEndElement();

        namespace = "";
        this.writeStartElement(null, namespace, "sessionDuration", xmlWriter);
        if (this.sessionDuration == Integer.MIN_VALUE)
        {
            throw new ADBException("sessionDuration cannot be null!!");
        }
        else
        {
            xmlWriter.writeCharacters(ConverterUtil.convertToString(this.sessionDuration));
        }
        xmlWriter.writeEndElement();

        namespace = "";
        this.writeStartElement(null, namespace, "allowedExtensions", xmlWriter);
        if (this.allowedExtensions == Integer.MIN_VALUE)
        {
            throw new ADBException("allowedExtensions cannot be null!!");
        }
        else
        {
            xmlWriter.writeCharacters(ConverterUtil.convertToString(this.allowedExtensions));
        }
        xmlWriter.writeEndElement();

        namespace = "";
        this.writeStartElement(null, namespace, "extensionDuration", xmlWriter);
        if (this.extensionDuration == Integer.MIN_VALUE)
        {
            throw new ADBException("extensionDuration cannot be null!!");
        }
        else
        {
            xmlWriter.writeCharacters(ConverterUtil.convertToString(this.extensionDuration));
        }
        xmlWriter.writeEndElement();

        namespace = "";
        this.writeStartElement(null, namespace, "isQueuable", xmlWriter);
        xmlWriter.writeCharacters(ConverterUtil.convertToString(this.isQueuable));
        xmlWriter.writeEndElement();

        namespace = "";
        this.writeStartElement(null, namespace, "isBookable", xmlWriter);
        xmlWriter.writeCharacters(ConverterUtil.convertToString(this.isBookable));
        xmlWriter.writeEndElement();
        
        if (this.localMaxBookingsTracker)
        {
            namespace = "";
            this.writeStartElement(null, namespace, "maxBookings", xmlWriter);
            if (this.maxBookings == null)
            {
                throw new ADBException("maxBookings cannot be null!!");
            }
            else
            {
                xmlWriter.writeCharacters(this.maxBookings);
            }
            xmlWriter.writeEndElement();
        }
        
        if (this.noteTracker)
        {
            namespace = "";
            this.writeStartElement(null, namespace, "note", xmlWriter);
            if (this.note == null)
            {
                throw new ADBException("note cannot be null!!");
            }
            else
            {
                xmlWriter.writeCharacters(this.note);
            }

            xmlWriter.writeEndElement();
        }
        
        xmlWriter.writeEndElement();
    }

    private static String generatePrefix(final String namespace)
    {
        if (namespace.equals("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/"))
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
                prefix = ResourceRequestType.generatePrefix(namespace);
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
            prefix = ResourceRequestType.generatePrefix(namespace);
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
        attribList.add(new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/",
                "ResourceRequestType"));

        elementList.add(new QName("", "siteID"));
        if (this.siteID != null)
        {
            elementList.add(ConverterUtil.convertToString(this.siteID));
        }
        else
        {
            throw new ADBException("siteID cannot be null!!");
        }
        
        if (this.permissionIDTracker)
        {
            elementList.add(new QName("", "permissionID"));
            if (this.permissionID != null)
            {
                elementList.add(ConverterUtil.convertToString(this.permissionID));
            }
            else
            {
                throw new ADBException("permissionID cannot be null!!");
            }
        }
        
        elementList.add(new QName("", "resource"));
        if (this.resource == null)
        {
            throw new ADBException("resource cannot be null!!");
        }
        elementList.add(this.resource);

        elementList.add(new QName("", "start"));
        if (this.start != null)
        {
            elementList.add(ConverterUtil.convertToString(this.start));
        }
        else
        {
            throw new ADBException("start cannot be null!!");
        }

        elementList.add(new QName("", "expiry"));
        if (this.expiry != null)
        {
            elementList.add(ConverterUtil.convertToString(this.expiry));
        }
        else
        {
            throw new ADBException("expiry cannot be null!!");
        }

        elementList.add(new QName("", "name"));
        if (this.name != null)
        {
            elementList.add(ConverterUtil.convertToString(this.name));
        }
        else
        {
            throw new ADBException("name cannot be null!!");
        }

        elementList.add(new QName("", "sessionDuration"));
        elementList.add(ConverterUtil.convertToString(this.sessionDuration));

        elementList.add(new QName("", "allowedExtensions"));
        elementList.add(ConverterUtil.convertToString(this.allowedExtensions));

        elementList.add(new QName("", "extensionDuration"));
        elementList.add(ConverterUtil.convertToString(this.extensionDuration));

        elementList.add(new QName("", "isQueuable"));
        elementList.add(ConverterUtil.convertToString(this.isQueuable));

        elementList.add(new QName("", "isBookable"));
        elementList.add(ConverterUtil.convertToString(this.isBookable));
        
        if (this.localMaxBookingsTracker)
        {
            elementList.add(new QName("", "maxBookings"));
            if (this.maxBookings != null)
            {
                elementList.add(ConverterUtil.convertToString(this.maxBookings));
            }
            else
            {
                throw new ADBException("maxBookings cannot be null!!");
            }
        }
        
        if (this.noteTracker)
        {
            elementList.add(new QName("", "note"));
            if (this.note != null)
            {
                elementList.add(ConverterUtil.convertToString(this.note));
            }
            else
            {
                throw new ADBException("note cannot be null!!");
            }
        }

        return new ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());
    }

    public static class Factory
    {
        public static ResourceRequestType parse(final XMLStreamReader reader) throws Exception
        {
            final ResourceRequestType object = new ResourceRequestType();
            
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
                        if (!"ResourceRequestType".equals(type))
                        {
                            final String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (ResourceRequestType) ExtensionMapper.getTypeObject(nsUri, type, reader);
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
                if (reader.isStartElement() && new QName("", "permissionID").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setPermissionID(ConverterUtil.convertToString(content));
                    reader.next();
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
                else
                {
                    throw new ADBException("Unexpected subelement " + reader.getName());
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
                    throw new ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "expiry").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setExpiry(ConverterUtil.convertToDateTime(content));
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
                if (reader.isStartElement() && new QName("", "name").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setName(ConverterUtil.convertToString(content));
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
                if (reader.isStartElement() && new QName("", "sessionDuration").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setSessionDuration(ConverterUtil.convertToInt(content));
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
                if (reader.isStartElement() && new QName("", "allowedExtensions").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setAllowedExtensions(ConverterUtil.convertToInt(content));
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
                if (reader.isStartElement() && new QName("", "extensionDuration").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setExtensionDuration(ConverterUtil.convertToInt(content));
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
                if (reader.isStartElement() && new QName("", "isQueuable").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setIsQueuable(ConverterUtil.convertToBoolean(content));
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
                if (reader.isStartElement() && new QName("", "maxBookings").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setMaxBookings(ConverterUtil.convertToString(content));
                    reader.next();
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "note").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setNote(ConverterUtil.convertToString(content));
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
