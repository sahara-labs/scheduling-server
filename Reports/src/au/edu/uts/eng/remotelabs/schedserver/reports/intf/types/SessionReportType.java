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
 * @author Tania Machet (tmachet)
 * @date 13 December 2010
 */

package au.edu.uts.eng.remotelabs.schedserver.reports.intf.types;

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
 * SessionReportType bean class.
 */
public class SessionReportType implements ADBBean
{
    /*
     * This type was generated from the piece of schema that had
     * name = SessionReportType
     * Namespace URI = http://remotelabs.eng.uts.edu.au/schedserver/reports
     * Namespace Prefix = ns1
     */

    private static final long serialVersionUID = -1066261603037864664L;

    private static String generatePrefix(final String namespace)
    {
        if (namespace.equals("http://remotelabs.eng.uts.edu.au/schedserver/reports"))
        {
            return "ns1";
        }
        return BeanUtil.getUniquePrefix();
    }

    protected RequestorType user;
    protected boolean userTracker = false;

    public RequestorType getUser()
    {
        return this.user;
    }

    public void setUser(final RequestorType param)
    {
        this.user = param;
        this.userTracker = param != null;
    }

    protected String userClass;
    protected boolean userClassTracker = false;

    public String getUserClass()
    {
        return this.userClass;
    }

    public void setUserClass(final String param)
    {
        this.userClass = param;
        this.userClassTracker = param != null;
    }

    protected String rigType;
    protected boolean rigTypeTracker = false;

    public String getRigType()
    {
        return this.rigType;
    }

    public void setRigType(final String param)
    {
        this.rigType = param;
        this.rigTypeTracker = param != null;
    }

    protected String rigName;
    protected boolean rigNameTracker = false;

    public String getRigName()
    {
        return this.rigName;
    }

    public void setRigName(final String param)
    {
        this.rigName = param;
        this.rigNameTracker = param != null;
    }

    protected float aveQueueDuration;

    public float getAveQueueDuration()
    {
        return this.aveQueueDuration;
    }

    public void setAveQueueDuration(final float param)
    {
        this.aveQueueDuration = param;
    }

    protected float medQueueDuration;

    public float getMedQueueDuration()
    {
        return this.medQueueDuration;
    }

    public void setMedQueueDuration(final float param)
    {
        this.medQueueDuration = param;
    }

    protected float minQueueDuration;

    public float getMinQueueDuration()
    {
        return this.minQueueDuration;
    }

    public void setMinQueueDuration(final float param)
    {
        this.minQueueDuration = param;
    }

    protected float maxQueueDuration;

    public float getMaxQueueDuration()
    {
        return this.maxQueueDuration;
    }

    public void setMaxQueueDuration(final float param)
    {
        this.maxQueueDuration = param;
    }

    protected float totalQueueDuration;

    public float getTotalQueueDuration()
    {
        return this.totalQueueDuration;
    }

    public void setTotalQueueDuration(final float param)
    {
        this.totalQueueDuration = param;
    }

    protected int sessionCount;

    public int getSessionCount()
    {
        return this.sessionCount;
    }

    public void setSessionCount(final int param)
    {
        this.sessionCount = param;
    }

    protected int userCount;
    protected boolean userCountTracker = false;

    public int getUserCount()
    {
        return this.userCount;
    }

    public void setUserCount(final int param)
    {
        this.userCount = param;
        this.userClassTracker = param != Integer.MIN_VALUE;
    }

    protected float aveSessionDuration;

    public float getAveSessionDuration()
    {
        return this.aveSessionDuration;
    }

    public void setAveSessionDuration(final float param)
    {
        this.aveSessionDuration = param;
    }

    protected float medSessionDuration;

    public float getMedSessionDuration()
    {
        return this.medSessionDuration;
    }

    public void setMedSessionDuration(final float param)
    {
        this.medSessionDuration = param;
    }

    protected float maxSessionDuration;

    public float getMaxSessionDuration()
    {
        return this.maxSessionDuration;
    }

    public void setMaxSessionDuration(final float param)
    {
        this.maxSessionDuration = param;
    }

    protected float minSessionDuration;

    public float getMinSessionDuration()
    {
        return this.minSessionDuration;
    }

    public void setMinSessionDuration(final float param)
    {
        this.minSessionDuration = param;
    }

    protected float totalSessionDuration;

    public float getTotalSessionDuration()
    {
        return this.totalSessionDuration;
    }

    public void setTotalSessionDuration(final float param)
    {
        this.totalSessionDuration = param;
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
                SessionReportType.this.serialize(this.parentQName, factory, xmlWriter);
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
                    prefix = SessionReportType.generatePrefix(namespace);
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
                    "http://remotelabs.eng.uts.edu.au/schedserver/reports");
            if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0))
            {
                this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
                        + ":SessionReportType", xmlWriter);
            }
            else
            {
                this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "SessionReportType",
                        xmlWriter);
            }
        }
        
        if (this.userTracker)
        {
            if (this.user == null)
            {
                throw new ADBException("user cannot be null!!");
            }
            this.user.serialize(new QName("", "user"), factory, xmlWriter);
        }
        
        if (this.userClassTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = SessionReportType.generatePrefix(namespace);

                    xmlWriter.writeStartElement(prefix, "userClass", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "userClass");
                }
            }
            else
            {
                xmlWriter.writeStartElement("userClass");
            }

            if (this.userClass == null)
            {
                throw new ADBException("userClass cannot be null!!");
            }
            else
            {
                xmlWriter.writeCharacters(this.userClass);
            }
            xmlWriter.writeEndElement();
        }
        
        if (this.rigTypeTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = SessionReportType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "rigType", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "rigType");
                }
            }
            else
            {
                xmlWriter.writeStartElement("rigType");
            }

            if (this.rigType == null)
            {
                throw new ADBException("rigType cannot be null!!");
            }
            else
            {
                xmlWriter.writeCharacters(this.rigType);
            }
            xmlWriter.writeEndElement();
        }
        
        if (this.rigNameTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = SessionReportType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "rigName", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "rigName");
                }
            }
            else
            {
                xmlWriter.writeStartElement("rigName");
            }

            if (this.rigName == null)
            {
                throw new ADBException("rigName cannot be null!!");
            }
            else
            {
                xmlWriter.writeCharacters(this.rigName);
            }

            xmlWriter.writeEndElement();
        }
        
        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null)
            {
                prefix = SessionReportType.generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "aveQueueDuration", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            else
            {
                xmlWriter.writeStartElement(namespace, "aveQueueDuration");
            }
        }
        else
        {
            xmlWriter.writeStartElement("aveQueueDuration");
        }

        xmlWriter.writeCharacters(ConverterUtil.convertToString(this.aveQueueDuration));
        xmlWriter.writeEndElement();

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = SessionReportType.generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "medQueueDuration", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            else
            {
                xmlWriter.writeStartElement(namespace, "medQueueDuration");
            }
        }
        else
        {
            xmlWriter.writeStartElement("medQueueDuration");
        }

        xmlWriter.writeCharacters(ConverterUtil.convertToString(this.medQueueDuration));
        xmlWriter.writeEndElement();

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = SessionReportType.generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "minQueueDuration", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            else
            {
                xmlWriter.writeStartElement(namespace, "minQueueDuration");
            }
        }
        else
        {
            xmlWriter.writeStartElement("minQueueDuration");
        }
        xmlWriter.writeCharacters(ConverterUtil.convertToString(this.minQueueDuration));
        xmlWriter.writeEndElement();

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = SessionReportType.generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "maxQueueDuration", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            else
            {
                xmlWriter.writeStartElement(namespace, "maxQueueDuration");
            }
        }
        else
        {
            xmlWriter.writeStartElement("maxQueueDuration");
        }
        xmlWriter.writeCharacters(ConverterUtil.convertToString(this.maxQueueDuration));
        xmlWriter.writeEndElement();

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = SessionReportType.generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "totalQueueDuration", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            else
            {
                xmlWriter.writeStartElement(namespace, "totalQueueDuration");
            }
        }
        else
        {
            xmlWriter.writeStartElement("totalQueueDuration");
        }

        xmlWriter.writeCharacters(ConverterUtil.convertToString(this.totalQueueDuration));
        xmlWriter.writeEndElement();

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = SessionReportType.generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "sessionCount", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            else
            {
                xmlWriter.writeStartElement(namespace, "sessionCount");
            }
        }
        else
        {
            xmlWriter.writeStartElement("sessionCount");
        }
        xmlWriter.writeCharacters(ConverterUtil.convertToString(this.sessionCount));
        xmlWriter.writeEndElement();
        
        if (this.userCountTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = SessionReportType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "userCount", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "userCount");
                }
            }
            else
            {
                xmlWriter.writeStartElement("userCount");
            }
            xmlWriter.writeCharacters(ConverterUtil.convertToString(this.userCount));
            xmlWriter.writeEndElement();
        }
        
        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null)
            {
                prefix = SessionReportType.generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "aveSessionDuration", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            else
            {
                xmlWriter.writeStartElement(namespace, "aveSessionDuration");
            }
        }
        else
        {
            xmlWriter.writeStartElement("aveSessionDuration");
        }
        xmlWriter.writeCharacters(ConverterUtil.convertToString(this.aveSessionDuration));
        xmlWriter.writeEndElement();

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = SessionReportType.generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "medSessionDuration", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            else
            {
                xmlWriter.writeStartElement(namespace, "medSessionDuration");
            }
        }
        else
        {
            xmlWriter.writeStartElement("medSessionDuration");
        }
        xmlWriter.writeCharacters(ConverterUtil.convertToString(this.medSessionDuration));
        xmlWriter.writeEndElement();

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = SessionReportType.generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "maxSessionDuration", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            else
            {
                xmlWriter.writeStartElement(namespace, "maxSessionDuration");
            }
        }
        else
        {
            xmlWriter.writeStartElement("maxSessionDuration");
        }
        xmlWriter.writeCharacters(ConverterUtil.convertToString(this.maxSessionDuration));
        xmlWriter.writeEndElement();

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null)
            {
                prefix = SessionReportType.generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "minSessionDuration", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            else
            {
                xmlWriter.writeStartElement(namespace, "minSessionDuration");
            }
        }
        else
        {
            xmlWriter.writeStartElement("minSessionDuration");
        }
        xmlWriter.writeCharacters(ConverterUtil.convertToString(this.minSessionDuration));
        xmlWriter.writeEndElement();

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = SessionReportType.generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "totalSessionDuration", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            else
            {
                xmlWriter.writeStartElement(namespace, "totalSessionDuration");
            }
        }
        else
        {
            xmlWriter.writeStartElement("totalSessionDuration");
        }
        xmlWriter.writeCharacters(ConverterUtil.convertToString(this.totalSessionDuration));
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
            prefix = SessionReportType.generatePrefix(namespace);

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

        if (this.userTracker)
        {
            elementList.add(new QName("", "user"));
            if (this.user == null)
            {
                throw new ADBException("user cannot be null!!");
            }
            elementList.add(this.user);
        }
        
        if (this.userClassTracker)
        {
            elementList.add(new QName("", "userClass"));
            if (this.userClass != null)
            {
                elementList.add(ConverterUtil.convertToString(this.userClass));
            }
            else
            {
                throw new ADBException("userClass cannot be null!!");
            }
        }
        
        if (this.rigTypeTracker)
        {
            elementList.add(new QName("", "rigType"));
            if (this.rigType != null)
            {
                elementList.add(ConverterUtil.convertToString(this.rigType));
            }
            else
            {
                throw new ADBException("rigType cannot be null!!");
            }
        }
        
        if (this.rigNameTracker)
        {
            elementList.add(new QName("", "rigName"));
            if (this.rigName != null)
            {
                elementList.add(ConverterUtil.convertToString(this.rigName));
            }
            else
            {
                throw new ADBException("rigName cannot be null!!");
            }
        }
        
        elementList.add(new QName("", "aveQueueDuration"));
        elementList.add(ConverterUtil.convertToString(this.aveQueueDuration));

        elementList.add(new QName("", "medQueueDuration"));
        elementList.add(ConverterUtil.convertToString(this.medQueueDuration));

        elementList.add(new QName("", "minQueueDuration"));
        elementList.add(ConverterUtil.convertToString(this.minQueueDuration));

        elementList.add(new QName("", "maxQueueDuration"));
        elementList.add(ConverterUtil.convertToString(this.maxQueueDuration));

        elementList.add(new QName("", "totalQueueDuration"));
        elementList.add(ConverterUtil.convertToString(this.totalQueueDuration));

        elementList.add(new QName("", "sessionCount"));
        elementList.add(ConverterUtil.convertToString(this.sessionCount));
        
        if (this.userCountTracker)
        {
            elementList.add(new QName("", "userCount"));
            elementList.add(ConverterUtil.convertToString(this.userCount));
        }
        
        elementList.add(new QName("", "aveSessionDuration"));
        elementList.add(ConverterUtil.convertToString(this.aveSessionDuration));

        elementList.add(new QName("", "medSessionDuration"));
        elementList.add(ConverterUtil.convertToString(this.medSessionDuration));

        elementList.add(new QName("", "maxSessionDuration"));
        elementList.add(ConverterUtil.convertToString(this.maxSessionDuration));

        elementList.add(new QName("", "minSessionDuration"));
        elementList.add(ConverterUtil.convertToString(this.minSessionDuration));

        elementList.add(new QName("", "totalSessionDuration"));
        elementList.add(ConverterUtil.convertToString(this.totalSessionDuration));

        return new ADBXMLStreamReaderImpl(qName, elementList.toArray(), new Object[0]);
    }

    public static class Factory
    {
        public static SessionReportType parse(final XMLStreamReader reader) throws Exception
        {
            final SessionReportType object = new SessionReportType();
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
                        if (!"SessionReportType".equals(type))
                        {
                            final String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (SessionReportType) ExtensionMapper.getTypeObject(nsUri, type, reader);
                        }
                    }
                }

                reader.next();
                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "user").equals(reader.getName()))
                {
                    object.setUser(RequestorType.Factory.parse(reader));
                    reader.next();
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "userClass").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setUserClass(ConverterUtil.convertToString(content));
                    reader.next();
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "rigType").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setRigType(ConverterUtil.convertToString(content));
                    reader.next();
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "rigName").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setRigName(ConverterUtil.convertToString(content));
                    reader.next();
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "aveQueueDuration").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setAveQueueDuration(ConverterUtil.convertToFloat(content));
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
                if (reader.isStartElement() && new QName("", "medQueueDuration").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setMedQueueDuration(ConverterUtil.convertToFloat(content));
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
                if (reader.isStartElement() && new QName("", "minQueueDuration").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setMinQueueDuration(ConverterUtil.convertToFloat(content));
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
                if (reader.isStartElement() && new QName("", "maxQueueDuration").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setMaxQueueDuration(ConverterUtil.convertToFloat(content));
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
                if (reader.isStartElement() && new QName("", "totalQueueDuration").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setTotalQueueDuration(ConverterUtil.convertToFloat(content));
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
                if (reader.isStartElement() && new QName("", "sessionCount").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setSessionCount(ConverterUtil.convertToInt(content));
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
                if (reader.isStartElement() && new QName("", "userCount").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setUserCount(ConverterUtil.convertToInt(content));
                    reader.next();
                }
                else
                {
                    object.setUserCount(Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "aveSessionDuration").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setAveSessionDuration(ConverterUtil.convertToFloat(content));
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
                if (reader.isStartElement() && new QName("", "medSessionDuration").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setMedSessionDuration(ConverterUtil.convertToFloat(content));
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
                if (reader.isStartElement() && new QName("", "maxSessionDuration").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setMaxSessionDuration(ConverterUtil.convertToFloat(content));
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
                if (reader.isStartElement() && new QName("", "minSessionDuration").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setMinSessionDuration(ConverterUtil.convertToFloat(content));
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
                if (reader.isStartElement() && new QName("", "totalSessionDuration").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setTotalSessionDuration(ConverterUtil.convertToFloat(content));
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
