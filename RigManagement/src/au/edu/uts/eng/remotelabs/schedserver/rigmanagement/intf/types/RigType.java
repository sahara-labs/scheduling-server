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
 * RigType bean class.
 */
public class RigType extends RigIDType implements ADBBean
{
    /*
     * This type was generated from the piece of schema that had
     * name = RigType
     * Namespace URI = http://remotelabs.eng.uts.edu.au/rigmanagement
     * Namespace Prefix = ns1
     */

    private static final long serialVersionUID = -2983406484876745866L;

    private static String generatePrefix(final String namespace)
    {
        if (namespace.equals("http://remotelabs.eng.uts.edu.au/rigmanagement"))
        {
            return "ns1";
        }
        return BeanUtil.getUniquePrefix();
    }

    protected RigTypeIDType rigType;

    public RigTypeIDType getRigType()
    {
        return this.rigType;
    }

    public void setRigType(final RigTypeIDType param)
    {
        this.rigType = param;
    }

    protected String capabilities;

    public String getCapabilities()
    {
        return this.capabilities;
    }

    public void setCapabilities(final String param)
    {
        this.capabilities = param;
    }

    protected boolean isRegistered;

    public boolean getIsRegistered()
    {
        return this.isRegistered;
    }

    public void setIsRegistered(final boolean param)
    {
        this.isRegistered = param;
    }

    protected boolean isOnline;

    public boolean getIsOnline()
    {
        return this.isOnline;
    }

    public void setIsOnline(final boolean param)
    {
        this.isOnline = param;
    }

    protected boolean isInSession;

    public boolean getIsInSession()
    {
        return this.isInSession;
    }

    public void setIsInSession(final boolean param)
    {
        this.isInSession = param;
    }
    
    protected String sessionUser;
    protected boolean sessionUserTracker = false;
    
    public String getSessionUser()
    {
        return this.sessionUser;
    }
    
    public void setSessionUser(final String param)
    {
        this.sessionUserTracker = param != null;
        this.sessionUser = param;
    }
     
    protected boolean isAlarmed;

    public boolean getIsAlarmed()
    {
        return this.isAlarmed;
    }

    public void setIsAlarmed(final boolean param)
    {
        this.isAlarmed = param;
    }

    protected String offlineReason;
    protected boolean offlineReasonTracker = false;

    public String getOfflineReason()
    {
        return this.offlineReason;
    }

    public void setOfflineReason(final String param)
    {
        if (param != null)
        {
            this.offlineReasonTracker = true;
        }
        else
        {
            this.offlineReasonTracker = false;
        }

        this.offlineReason = param;
    }

    protected String contactURL;
    protected boolean contactURLTracker = false;

    public String getContactURL()
    {
        return this.contactURL;
    }

    public void setContactURL(final String param)
    {
        if (param != null)
        {
            this.contactURLTracker = true;
        }
        else
        {
            this.contactURLTracker = false;
        }

        this.contactURL = param;
    }

    protected RigLogType[] lastLogs;
    protected boolean lastLogsTracker = false;

    public RigLogType[] getLastLogs()
    {
        return this.lastLogs;
    }

    public void setLastLogs(final RigLogType[] param)
    {
        if (param != null)
        {
            this.lastLogsTracker = true;
        }
        else
        {
            this.lastLogsTracker = false;
        }

        this.lastLogs = param;
    }

    public void addLastLog(final RigLogType param)
    {
        if (this.lastLogs == null)
        {
            this.lastLogs = new RigLogType[] {};
        }

        this.lastLogsTracker = true;

        @SuppressWarnings("unchecked")
        final List<ADBBean> list = ConverterUtil.toList(this.lastLogs);
        list.add(param);
        this.lastLogs = list.toArray(new RigLogType[list.size()]);
    }

    protected OfflinePeriodType[] localOfflinePeriods;
    protected boolean offlinePeriodsTracker = false;

    public OfflinePeriodType[] getOfflinePeriods()
    {
        return this.localOfflinePeriods;
    }

    public void setOfflinePeriods(final OfflinePeriodType[] param)
    {
        if (param != null)
        {
            this.offlinePeriodsTracker = true;
        }
        else
        {
            this.offlinePeriodsTracker = false;
        }

        this.localOfflinePeriods = param;
    }

    public void addOfflinePeriods(final OfflinePeriodType param)
    {
        if (this.localOfflinePeriods == null)
        {
            this.localOfflinePeriods = new OfflinePeriodType[] {};
        }

        this.offlinePeriodsTracker = true;

        @SuppressWarnings("unchecked")
        final List<ADBBean> list = ConverterUtil.toList(this.localOfflinePeriods);
        list.add(param);
        this.localOfflinePeriods = list.toArray(new OfflinePeriodType[list.size()]);
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
                RigType.this.serialize(this.parentQName, factory, xmlWriter);
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
                    prefix = RigType.generatePrefix(namespace);
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
                    + ":RigType", xmlWriter);
        }
        else
        {
            this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "RigType", xmlWriter);
        }

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = RigType.generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "name", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            else
            {
                xmlWriter.writeStartElement(namespace, "name");
            }
        }
        else
        {
            xmlWriter.writeStartElement("name");
        }

        if (this.name == null)
        {
            throw new ADBException("name cannot be null!!");
        }
        else
        {
            xmlWriter.writeCharacters(this.name);
        }
        xmlWriter.writeEndElement();

        if (this.rigType == null)
        {
            throw new ADBException("rigType cannot be null!!");
        }
        this.rigType.serialize(new QName("", "rigType"), factory, xmlWriter);

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null)
            {
                prefix = RigType.generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "capabilities", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            else
            {
                xmlWriter.writeStartElement(namespace, "capabilities");
            }
        }
        else
        {
            xmlWriter.writeStartElement("capabilities");
        }

        if (this.capabilities == null)
        {
            throw new ADBException("capabilities cannot be null!!");
        }
        else
        {
            xmlWriter.writeCharacters(this.capabilities);
        }
        xmlWriter.writeEndElement();

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null)
            {
                prefix = RigType.generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "isRegistered", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            else
            {
                xmlWriter.writeStartElement(namespace, "isRegistered");
            }
        }
        else
        {
            xmlWriter.writeStartElement("isRegistered");
        }
        xmlWriter.writeCharacters(ConverterUtil.convertToString(this.isRegistered));
        xmlWriter.writeEndElement();

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null)
            {
                prefix = RigType.generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "isOnline", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            else
            {
                xmlWriter.writeStartElement(namespace, "isOnline");
            }
        }
        else
        {
            xmlWriter.writeStartElement("isOnline");
        }
        xmlWriter.writeCharacters(ConverterUtil.convertToString(this.isOnline));
        xmlWriter.writeEndElement();

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);
            if (prefix == null)
            {
                prefix = RigType.generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "isInSession", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            else
            {
                xmlWriter.writeStartElement(namespace, "isInSession");
            }
        }
        else
        {
            xmlWriter.writeStartElement("isInSession");
        }
        xmlWriter.writeCharacters(ConverterUtil.convertToString(this.isInSession));
        xmlWriter.writeEndElement();
        
        if (this.sessionUserTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = RigType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "sessionUser", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "sessionUser");
                }
            }
            else
            {
                xmlWriter.writeStartElement("sessionUser");
            }
            xmlWriter.writeCharacters(this.sessionUser);
            xmlWriter.writeEndElement();
        }

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = RigType.generatePrefix(namespace);
                xmlWriter.writeStartElement(prefix, "isAlarmed", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);
            }
            else
            {
                xmlWriter.writeStartElement(namespace, "isAlarmed");
            }
        }
        else
        {
            xmlWriter.writeStartElement("isAlarmed");
        }
        xmlWriter.writeCharacters(ConverterUtil.convertToString(this.isAlarmed));
        xmlWriter.writeEndElement();

        if (this.offlineReasonTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = RigType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "offlineReason", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "offlineReason");
                }
            }
            else
            {
                xmlWriter.writeStartElement("offlineReason");
            }

            if (this.offlineReason == null)
            {
                throw new ADBException("offlineReason cannot be null!!");
            }
            else
            {
                xmlWriter.writeCharacters(this.offlineReason);
            }
            xmlWriter.writeEndElement();
        }

        if (this.contactURLTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = RigType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "contactURL", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "contactURL");
                }
            }
            else
            {
                xmlWriter.writeStartElement("contactURL");
            }

            if (this.contactURL == null)
            {
                throw new ADBException("contactURL cannot be null!!");
            }
            else
            {
                xmlWriter.writeCharacters(this.contactURL);
            }
            xmlWriter.writeEndElement();
        }

        if (this.lastLogsTracker)
        {
            if (this.lastLogs != null)
            {
                for (final RigLogType lastLog : this.lastLogs)
                {
                    if (lastLog != null)
                    {
                        lastLog.serialize(new QName("", "lastLogs"), factory, xmlWriter);
                    }
                }
            }
            else
            {
                throw new ADBException("lastLogs cannot be null!!");
            }
        }

        if (this.offlinePeriodsTracker)
        {
            if (this.localOfflinePeriods != null)
            {
                for (final OfflinePeriodType offlinePeriod : this.localOfflinePeriods)
                {
                    if (offlinePeriod != null)
                    {
                        offlinePeriod.serialize(new QName("", "offlinePeriods"), factory, xmlWriter);
                    }
                }
            }
            else
            {
                throw new ADBException("offlinePeriods cannot be null!!");
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
            prefix = RigType.generatePrefix(namespace);

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
        attribList.add(new QName("http://remotelabs.eng.uts.edu.au/rigmanagement", "RigType"));

        elementList.add(new QName("", "name"));

        if (this.name != null)
        {
            elementList.add(ConverterUtil.convertToString(this.name));
        }
        else
        {
            throw new ADBException("name cannot be null!!");
        }

        elementList.add(new QName("", "rigType"));

        if (this.rigType == null)
        {
            throw new ADBException("rigType cannot be null!!");
        }
        elementList.add(this.rigType);

        elementList.add(new QName("", "capabilities"));
        if (this.capabilities != null)
        {
            elementList.add(ConverterUtil.convertToString(this.capabilities));
        }
        else
        {
            throw new ADBException("capabilities cannot be null!!");
        }

        elementList.add(new QName("", "isRegistered"));
        elementList.add(ConverterUtil.convertToString(this.isRegistered));

        elementList.add(new QName("", "isOnline"));
        elementList.add(ConverterUtil.convertToString(this.isOnline));

        elementList.add(new QName("", "isInSession"));
        elementList.add(ConverterUtil.convertToString(this.isInSession));
        
        if (this.sessionUserTracker)
        {
            elementList.add(new QName("", "sessionUser"));
            if (this.sessionUser != null)
            {
                elementList.add(ConverterUtil.convertToString(this.sessionUser));
            }
            else
            {
                throw new ADBException("sessionUser cannot be null");
            }
        }
        

        elementList.add(new QName("", "isAlarmed"));
        elementList.add(ConverterUtil.convertToString(this.isAlarmed));

        if (this.offlineReasonTracker)
        {
            elementList.add(new QName("", "offlineReason"));
            if (this.offlineReason != null)
            {
                elementList.add(ConverterUtil.convertToString(this.offlineReason));
            }
            else
            {
                throw new ADBException("offlineReason cannot be null!!");
            }
        }

        if (this.contactURLTracker)
        {
            elementList.add(new QName("", "contactURL"));
            if (this.contactURL != null)
            {
                elementList.add(ConverterUtil.convertToString(this.contactURL));
            }
            else
            {
                throw new ADBException("contactURL cannot be null!!");
            }
        }

        if (this.lastLogsTracker)
        {
            if (this.lastLogs != null)
            {
                for (final RigLogType localLastLog : this.lastLogs)
                {
                    if (localLastLog != null)
                    {
                        elementList.add(new QName("", "lastLogs"));
                        elementList.add(localLastLog);
                    }
                }
            }
            else
            {
                throw new ADBException("lastLogs cannot be null!!");
            }
        }

        if (this.offlinePeriodsTracker)
        {
            if (this.localOfflinePeriods != null)
            {
                for (final OfflinePeriodType localOfflinePeriod : this.localOfflinePeriods)
                {
                    if (localOfflinePeriod != null)
                    {
                        elementList.add(new QName("", "offlinePeriods"));
                        elementList.add(localOfflinePeriod);
                    }
                }
            }
            else
            {
                throw new ADBException("offlinePeriods cannot be null!!");
            }
        }

        return new ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());
    }

    public static class Factory
    {
        public static RigType parse(final XMLStreamReader reader) throws Exception
        {
            final RigType object = new RigType();
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

                        if (!"RigType".equals(type))
                        {
                            final String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (RigType) ExtensionMapper.getTypeObject(nsUri, type, reader);
                        }
                    }
                }

                reader.next();
                final ArrayList<ADBBean> rigLogs = new ArrayList<ADBBean>();
                final ArrayList<ADBBean> offlinePeriods = new ArrayList<ADBBean>();

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
                    throw new ADBException("Unexpected subelement " + reader.getLocalName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "rigType").equals(reader.getName()))
                {
                    object.setRigType(RigTypeIDType.Factory.parse(reader));
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
                if (reader.isStartElement() && new QName("", "capabilities").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setCapabilities(ConverterUtil.convertToString(content));
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
                if (reader.isStartElement() && new QName("", "isRegistered").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setIsRegistered(ConverterUtil.convertToBoolean(content));
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
                if (reader.isStartElement() && new QName("", "isOnline").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setIsOnline(ConverterUtil.convertToBoolean(content));
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
                if (reader.isStartElement() && new QName("", "isInSession").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setIsInSession(ConverterUtil.convertToBoolean(content));
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
                if (reader.isStartElement() && new QName("", "sessionUser").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setSessionUser(content);
                    reader.next();
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "isAlarmed").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setIsAlarmed(ConverterUtil.convertToBoolean(content));
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
                if (reader.isStartElement() && new QName("", "offlineReason").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setOfflineReason(ConverterUtil.convertToString(content));
                    reader.next();
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "contactURL").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setContactURL(ConverterUtil.convertToString(content));
                    reader.next();
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "lastLogs").equals(reader.getName()))
                {
                    rigLogs.add(RigLogType.Factory.parse(reader));

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
                            if (new QName("", "lastLogs").equals(reader.getName()))
                            {
                                rigLogs.add(RigLogType.Factory.parse(reader));
                            }
                            else
                            {
                                noMore = true;
                            }
                        }
                    }
                    object.setLastLogs((RigLogType[]) ConverterUtil.convertToArray(RigLogType.class, rigLogs));
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                if (reader.isStartElement() && new QName("", "offlinePeriods").equals(reader.getName()))
                {
                    offlinePeriods.add(OfflinePeriodType.Factory.parse(reader));
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
                            if (new QName("", "offlinePeriods").equals(reader.getName()))
                            {
                                offlinePeriods.add(OfflinePeriodType.Factory.parse(reader));
                            }
                            else
                            {
                                noMore = true;
                            }
                        }
                    }
                    object.setOfflinePeriods((OfflinePeriodType[]) ConverterUtil.convertToArray(
                            OfflinePeriodType.class, offlinePeriods));
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
