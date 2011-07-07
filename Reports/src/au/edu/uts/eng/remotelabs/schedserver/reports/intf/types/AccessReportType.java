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
 * AccessReportType bean class.
 */
public class AccessReportType implements ADBBean
{
    /*
     * This type was generated from the piece of schema that had
     * name = AccessReportType
     * Namespace URI = http://remotelabs.eng.uts.edu.au/schedserver/reports
     * Namespace Prefix = ns1
     */

    private static final long serialVersionUID = -2061621727367281105L;

    private static String generatePrefix(final String namespace)
    {
        if (namespace.equals("http://remotelabs.eng.uts.edu.au/schedserver/reports"))
        {
            return "ns1";
        }
        return BeanUtil.getUniquePrefix();
    }

    protected RequestorType user;

    public RequestorType getUser()
    {
        return this.user;
    }

    public void setUser(final RequestorType param)
    {
        this.user = param;
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

    protected Calendar queueStartTime;
     
    public Calendar getQueueStartTime()
    {
        return this.queueStartTime;
    }

    public void setQueueStartTime(final Calendar param)
    {
        this.queueStartTime = param;
    }

    protected int queueDuration;
    protected boolean queueDurationTracker = false;

    public int getQueueDuration()
    {
        return this.queueDuration;
    }

    public void setQueueDuration(final int param)
    {
        this.queueDuration = param;
        this.queueDurationTracker = param != Integer.MIN_VALUE;
    }

    protected Calendar sessionStartTime;
    protected boolean sessionStartTimeTracker = false;

    public Calendar getSessionStartTime()
    {
        return this.sessionStartTime;
    }

    public void setSessionStartTime(final Calendar param)
    {
        this.sessionStartTime = param;
        this.sessionStartTimeTracker = param != null;
    }

    protected Calendar sessionEndTime;

    public Calendar getSessionEndTime()
    {
        return this.sessionEndTime;
    }

    public void setSessionEndTime(final Calendar param)
    {
        this.sessionEndTime = param;
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

    protected String reasonForTermination;
    protected boolean reasonForTerminationTracker = false;

    public String getReasonForTermination()
    {
        return this.reasonForTermination;
    }

    public void setReasonForTermination(final String param)
    {
        this.reasonForTermination = param;
        this.reasonForTerminationTracker = param != null;
    }

    protected boolean wasAssigned;

    public boolean getWasAssigned()
    {
        return this.wasAssigned;
    }

    public void setWasAssigned(final boolean param)
    {
        this.wasAssigned = param;
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
                AccessReportType.this.serialize(this.parentQName, factory, xmlWriter);
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
                    prefix = AccessReportType.generatePrefix(namespace);
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
                        + ":AccessReportType", xmlWriter);
            }
            else
            {
                this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "AccessReportType",
                        xmlWriter);
            }
        }

        if (this.user == null)
        {
            throw new ADBException("user cannot be null!!");
        }
        this.user.serialize(new QName("", "user"), factory, xmlWriter);
        
        if (this.userClassTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = AccessReportType.generatePrefix(namespace);
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
                    prefix = AccessReportType.generatePrefix(namespace);
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
                    prefix = AccessReportType.generatePrefix(namespace);
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
                prefix = AccessReportType.generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "queueStartTime", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            else
            {
                xmlWriter.writeStartElement(namespace, "queueStartTime");
            }
        }
        else
        {
            xmlWriter.writeStartElement("queueStartTime");
        }

        if (this.queueStartTime == null)
        {
            throw new ADBException("queueStartTime cannot be null!!");
        }
        else
        {
            xmlWriter.writeCharacters(ConverterUtil.convertToString(this.queueStartTime));
        }

        xmlWriter.writeEndElement();
        if (this.queueDurationTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = AccessReportType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "queueDuration", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "queueDuration");
                }
            }
            else
            {
                xmlWriter.writeStartElement("queueDuration");
            }

            if (this.queueDuration == Integer.MIN_VALUE)
            {
                throw new ADBException("queueDuration cannot be null!!");
            }
            else
            {
                xmlWriter.writeCharacters(ConverterUtil.convertToString(this.queueDuration));
            }
            xmlWriter.writeEndElement();
        }
        
        if (this.sessionStartTimeTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = AccessReportType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "sessionStartTime", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "sessionStartTime");
                }
            }
            else
            {
                xmlWriter.writeStartElement("sessionStartTime");
            }

            if (this.sessionStartTime == null)
            {
                throw new ADBException("sessionStartTime cannot be null!!");
            }
            else
            {
                xmlWriter.writeCharacters(ConverterUtil.convertToString(this.sessionStartTime));
            }
            xmlWriter.writeEndElement();
        }
        
        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null)
            {
                prefix = AccessReportType.generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "sessionEndTime", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            else
            {
                xmlWriter.writeStartElement(namespace, "sessionEndTime");
            }
        }
        else
        {
            xmlWriter.writeStartElement("sessionEndTime");
        }

        if (this.sessionEndTime == null)
        {
            throw new ADBException("sessionEndTime cannot be null!!");
        }
        else
        {
            xmlWriter.writeCharacters(ConverterUtil.convertToString(this.sessionEndTime));
        }
        xmlWriter.writeEndElement();

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = AccessReportType.generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "sessionDuration", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            else
            {
                xmlWriter.writeStartElement(namespace, "sessionDuration");
            }
        }
        else
        {
            xmlWriter.writeStartElement("sessionDuration");
        }
        xmlWriter.writeCharacters(ConverterUtil.convertToString(this.sessionDuration));
        xmlWriter.writeEndElement();
        
        if (this.reasonForTerminationTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = AccessReportType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "reasonForTermination", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "reasonForTermination");
                }
            }
            else
            {
                xmlWriter.writeStartElement("reasonForTermination");
            }

            if (this.reasonForTermination == null)
            {
                throw new ADBException("reasonForTermination cannot be null!!");
            }
            else
            {
                xmlWriter.writeCharacters(this.reasonForTermination);
            }

            xmlWriter.writeEndElement();
        }
        
        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null)
            {
                prefix = AccessReportType.generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "wasAssigned", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            else
            {
                xmlWriter.writeStartElement(namespace, "wasAssigned");
            }
        }
        else
        {
            xmlWriter.writeStartElement("wasAssigned");
        }
        xmlWriter.writeCharacters(ConverterUtil.convertToString(this.wasAssigned));
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
            prefix = AccessReportType.generatePrefix(namespace);
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

        elementList.add(new QName("", "user"));
        if (this.user == null)
        {
            throw new ADBException("user cannot be null!!");
        }
        elementList.add(this.user);
        
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
        
        elementList.add(new QName("", "queueStartTime"));
        if (this.queueStartTime != null)
        {
            elementList.add(ConverterUtil.convertToString(this.queueStartTime));
        }
        else
        {
            throw new ADBException("queueStartTime cannot be null!!");
        }
        
        if (this.queueDurationTracker)
        {
            elementList.add(new QName("", "queueDuration"));
            elementList.add(ConverterUtil.convertToString(this.queueDuration));
        }
        
        if (this.sessionStartTimeTracker)
        {
            elementList.add(new QName("", "sessionStartTime"));
            if (this.sessionStartTime != null)
            {
                elementList.add(ConverterUtil.convertToString(this.sessionStartTime));
            }
            else
            {
                throw new ADBException("sessionStartTime cannot be null!!");
            }
        }
        
        elementList.add(new QName("", "sessionEndTime"));
        if (this.sessionEndTime != null)
        {
            elementList.add(ConverterUtil.convertToString(this.sessionEndTime));
        }
        else
        {
            throw new ADBException("sessionEndTime cannot be null!!");
        }

        elementList.add(new QName("", "sessionDuration"));
        elementList.add(ConverterUtil.convertToString(this.sessionDuration));
        
        if (this.reasonForTerminationTracker)
        {
            elementList.add(new QName("", "reasonForTermination"));
            if (this.reasonForTermination != null)
            {
                elementList.add(ConverterUtil.convertToString(this.reasonForTermination));
            }
            else
            {
                throw new ADBException("reasonForTermination cannot be null!!");
            }
        }
        
        elementList.add(new QName("", "wasAssigned"));
        elementList.add(ConverterUtil.convertToString(this.wasAssigned));

        return new ADBXMLStreamReaderImpl(qName, elementList.toArray(), new Object[0]);
    }

    public static class Factory
    {
        public static AccessReportType parse(final XMLStreamReader reader) throws Exception
        {
            final AccessReportType object = new AccessReportType();
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

                        if (!"AccessReportType".equals(type))
                        {
                            final String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (AccessReportType) ExtensionMapper.getTypeObject(nsUri, type, reader);
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
                else
                {
                    throw new ADBException("Unexpected subelement " + reader.getLocalName());
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

                if (reader.isStartElement() && new QName("", "queueStartTime").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setQueueStartTime(ConverterUtil.convertToDateTime(content));
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

                if (reader.isStartElement() && new QName("", "queueDuration").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setQueueDuration(ConverterUtil.convertToInt(content));
                    reader.next();
                }
                else
                {
                    object.setQueueDuration(Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "sessionStartTime").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setSessionStartTime(ConverterUtil.convertToDateTime(content));
                    reader.next();
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "sessionEndTime").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setSessionEndTime(ConverterUtil.convertToDateTime(content));
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
                if (reader.isStartElement() && new QName("", "sessionDuration").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setSessionDuration(ConverterUtil.convertToInt(content));
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
                if (reader.isStartElement() && new QName("", "reasonForTermination").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setReasonForTermination(ConverterUtil.convertToString(content));
                    reader.next();
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "wasAssigned").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setWasAssigned(ConverterUtil.convertToBoolean(content));
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
