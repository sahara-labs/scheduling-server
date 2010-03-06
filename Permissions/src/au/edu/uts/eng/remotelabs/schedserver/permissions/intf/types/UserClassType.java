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
 * @date 3rd March 2009
 */

package au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types;

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
 * UserClassType bean class
 */

public class UserClassType extends UserClassIDType
        implements ADBBean
{
    /*
     * This type was generated from the piece of schema that had
     * name = UserClassType
     * Namespace URI = http://remotelabs.eng.uts.edu.au/schedserver/permissions
     * Namespace Prefix = ns1
     */

    private static final long serialVersionUID = -1359390318675458329L;

    protected int priority;
    protected boolean priorityTracker = false;
    protected boolean isQueuable;
    protected boolean isQueuableTracker = false;
    protected boolean isKickable;
    protected boolean isKickableTracker = false;
    protected boolean isUserLockable;
    protected boolean isUserLockableTracker = false;
    protected boolean isActive;
    protected boolean isActiveTracker = false;

    public int getPriority()
    {
        return this.priority;
    }

    public void setPriority(final int param)
    {
        if (param == Integer.MIN_VALUE)
        {
            this.priorityTracker = false;
        }
        else
        {
            this.priorityTracker = true;
        }

        this.priority = param;
    }

    public boolean getIsQueuable()
    {
        return this.isQueuable;
    }

    public void setIsQueuable(final boolean param)
    {
        this.isQueuableTracker = true;
        this.isQueuable = param;
    }

    public boolean getIsKickable()
    {
        return this.isKickable;
    }

    public void setIsKickable(final boolean param)
    {
        this.isKickableTracker = true;
        this.isKickable = param;
    }

    public boolean getIsUserLockable()
    {
        return this.isUserLockable;
    }

    public void setIsUserLockable(final boolean param)
    {
        this.isUserLockableTracker = true;
        this.isUserLockable = param;
    }

    public boolean getIsActive()
    {
        return this.isActive;
    }

    public void setIsActive(final boolean param)
    {
        this.isActiveTracker = true;
        this.isActive = param;
    }

    private static String generatePrefix(final String namespace)
    {
        if (namespace.equals("http://remotelabs.eng.uts.edu.au/schedserver/permissions"))
        {
            return "ns1";
        }
        return BeanUtil.getUniquePrefix();
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
                UserClassType.this.serialize(this.parentQName, factory, xmlWriter);
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
                    prefix = UserClassType.generatePrefix(namespace);
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
                "http://remotelabs.eng.uts.edu.au/schedserver/permissions");
        if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0))
        {
            this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
                    + ":UserClassType", xmlWriter);
        }
        else
        {
            this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "UserClassType", xmlWriter);
        }

        if (this.localRequestorIDTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = UserClassType.generatePrefix(namespace);

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

            if (this.localRequestorID == Integer.MIN_VALUE)
            {
                throw new ADBException("requestorID cannot be null!!");
            }
            else
            {
                xmlWriter.writeCharacters(ConverterUtil.convertToString(this.localRequestorID));
            }
            xmlWriter.writeEndElement();
        }
        
        if (this.localOperationRequestTypeSequence_type0Tracker)
        {
            if (this.localOperationRequestTypeSequence_type0 == null)
            {
                throw new ADBException("OperationRequestTypeSequence_type0 cannot be null!!");
            }
            this.localOperationRequestTypeSequence_type0.serialize(null, factory, xmlWriter);
        }
        
        if (this.localRequestorQNameTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = UserClassType.generatePrefix(namespace);
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

            if (this.localRequestorQName == null)
            {
                throw new ADBException("requestorQName cannot be null!!");
            }
            else
            {
                xmlWriter.writeCharacters(this.localRequestorQName);
            }

            xmlWriter.writeEndElement();
        }
        
        if (this.userClassIDTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = UserClassType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "userClassID", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "userClassID");
                }
            }
            else
            {
                xmlWriter.writeStartElement("userClassID");
            }

            if (this.userClassID == Integer.MIN_VALUE)
            {
                throw new ADBException("userClassID cannot be null!!");
            }
            else
            {
                xmlWriter.writeCharacters(ConverterUtil.convertToString(this.userClassID));
            }
            xmlWriter.writeEndElement();
        }
        
        if (this.userClassNameTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = UserClassType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "userClassName", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "userClassName");
                }
            }
            else
            {
                xmlWriter.writeStartElement("userClassName");
            }

            if (this.userClassName == null)
            {
                throw new ADBException("userClassName cannot be null!!");
            }
            else
            {
                xmlWriter.writeCharacters(this.userClassName);
            }
            xmlWriter.writeEndElement();
        }
        
        if (this.priorityTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = UserClassType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "priority", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "priority");
                }
            }
            else
            {
                xmlWriter.writeStartElement("priority");
            }

            if (this.priority == Integer.MIN_VALUE)
            {
                throw new ADBException("priority cannot be null!!");
            }
            else
            {
                xmlWriter.writeCharacters(ConverterUtil.convertToString(this.priority));
            }

            xmlWriter.writeEndElement();
        }
        
        if (this.isQueuableTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = UserClassType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "isQueuable", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "isQueuable");
                }
            }
            else
            {
                xmlWriter.writeStartElement("isQueuable");
            }
            xmlWriter.writeCharacters(ConverterUtil.convertToString(this.isQueuable));
            xmlWriter.writeEndElement();
        }
        
        if (this.isKickableTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = UserClassType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "isKickable", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "isKickable");
                }
            }
            else
            {
                xmlWriter.writeStartElement("isKickable");
            }

            xmlWriter.writeCharacters(ConverterUtil.convertToString(this.isKickable));
            xmlWriter.writeEndElement();
        }
        
        if (this.isUserLockableTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = UserClassType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "isUserLockable", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "isUserLockable");
                }
            }
            else
            {
                xmlWriter.writeStartElement("isUserLockable");
            }

            xmlWriter.writeCharacters(ConverterUtil.convertToString(this.isUserLockable));
            xmlWriter.writeEndElement();
        }
        
        if (this.isActiveTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = UserClassType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "isActive", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "isActive");
                }

            }
            else
            {
                xmlWriter.writeStartElement("isActive");
            }
            xmlWriter.writeCharacters(ConverterUtil.convertToString(this.isActive));
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
            prefix = UserClassType.generatePrefix(namespace);
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
        attribList.add(new QName("http://remotelabs.eng.uts.edu.au/schedserver/permissions", "UserClassType"));
        
        if (this.localRequestorIDTracker)
        {
            elementList.add(new QName("", "requestorID"));
            elementList.add(ConverterUtil.convertToString(this.localRequestorID));
        }
        
        if (this.localOperationRequestTypeSequence_type0Tracker)
        {
            elementList.add(new QName("http://remotelabs.eng.uts.edu.au/schedserver/permissions",
                    "OperationRequestTypeSequence_type0"));
            if (this.localOperationRequestTypeSequence_type0 == null)
            {
                throw new ADBException("OperationRequestTypeSequence_type0 cannot be null!!");
            }
            elementList.add(this.localOperationRequestTypeSequence_type0);
        }
        
        if (this.localRequestorQNameTracker)
        {
            elementList.add(new QName("", "requestorQName"));
            if (this.localRequestorQName != null)
            {
                elementList.add(ConverterUtil.convertToString(this.localRequestorQName));
            }
            else
            {
                throw new ADBException("requestorQName cannot be null!!");
            }
        }
        
        if (this.userClassIDTracker)
        {
            elementList.add(new QName("", "userClassID"));
            elementList.add(ConverterUtil.convertToString(this.userClassID));
        }
        
        if (this.userClassNameTracker)
        {
            elementList.add(new QName("", "userClassName"));
            if (this.userClassName != null)
            {
                elementList.add(ConverterUtil.convertToString(this.userClassName));
            }
            else
            {
                throw new ADBException("userClassName cannot be null!!");
            }
        }
        
        if (this.priorityTracker)
        {
            elementList.add(new QName("", "priority"));
            elementList.add(ConverterUtil.convertToString(this.priority));
        }
        
        if (this.isQueuableTracker)
        {
            elementList.add(new QName("", "isQueuable"));
            elementList.add(ConverterUtil.convertToString(this.isQueuable));
        }
        
        if (this.isKickableTracker)
        {
            elementList.add(new QName("", "isKickable"));
            elementList.add(ConverterUtil.convertToString(this.isKickable));
        }
        
        if (this.isUserLockableTracker)
        {
            elementList.add(new QName("", "isUserLockable"));
            elementList.add(ConverterUtil.convertToString(this.isUserLockable));
        }
        
        if (this.isActiveTracker)
        {
            elementList.add(new QName("", "isActive"));
            elementList.add(ConverterUtil.convertToString(this.isActive));
        }

        return new ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());
    }

    public static class Factory
    {
        public static UserClassType parse(final XMLStreamReader reader) throws Exception
        {
            final UserClassType object = new UserClassType();
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

                        if (!"UserClassType".equals(type))
                        {
                            final String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (UserClassType) ExtensionMapper.getTypeObject(nsUri, type, reader);
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
                        object.setOperationRequestTypeSequence_type0(OperationRequestTypeSequence_type0.Factory
                                .parse(reader));
                    }
                }
                catch (final Exception e)
                {
                    /* Optional. */
                }

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
                if (reader.isStartElement() && new QName("", "userClassID").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setUserClassID(ConverterUtil.convertToInt(content));
                    reader.next();
                }
                else
                {
                    object.setUserClassID(Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "userClassName").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setUserClassName(ConverterUtil.convertToString(content));
                    reader.next();
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "priority").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setPriority(ConverterUtil.convertToInt(content));
                    reader.next();
                }
                else
                {
                    object.setPriority(Integer.MIN_VALUE);
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

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "isKickable").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setIsKickable(ConverterUtil.convertToBoolean(content));
                    reader.next();
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "isUserLockable").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setIsUserLockable(ConverterUtil.convertToBoolean(content));
                    reader.next();
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "isActive").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setIsActive(ConverterUtil.convertToBoolean(content));
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
