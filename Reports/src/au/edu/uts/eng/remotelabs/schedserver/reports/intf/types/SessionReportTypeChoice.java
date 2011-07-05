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
 * SessionReportTypeChoice bean class.
 */
public class SessionReportTypeChoice implements ADBBean
{
    /*
     * This type was generated from the piece of schema that had
     * name = SessionReportTypeChoice_type0
     * Namespace URI = http://remotelabs.eng.uts.edu.au/schedserver/reports
     * Namespace Prefix = ns1
     */

    private static final long serialVersionUID = 8491570126377821952L;

    private static String generatePrefix(final String namespace)
    {
        if (namespace.equals("http://remotelabs.eng.uts.edu.au/schedserver/reports"))
        {
            return "ns1";
        }
        return BeanUtil.getUniquePrefix();
    }

    private void clearAllSettingTrackers()
    {
        this.userClassTracker = false;
        this.userTracker = false;
        this.rigTypeTracker = false;
        this.rigNameTracker = false;
    }

    protected String userClass;
    protected boolean userClassTracker = false;

    public String getUserClass()
    {
        return this.userClass;
    }

    public void setUserClass(final String param)
    {
        this.clearAllSettingTrackers();

        this.userClass = param;
        this.userClassTracker = param != null;
    }

    protected RequestorType user;
    protected boolean userTracker = false;

    public RequestorType getUser()
    {
        return this.user;
    }

    public void setUser(final RequestorType param)
    {
        this.clearAllSettingTrackers();

        this.user = param;
        this.userTracker = param != null;
    }

    protected String rigType;
    protected boolean rigTypeTracker = false;

    public String getRigType()
    {
        return this.rigType;
    }

    public void setRigType(final String param)
    {
        this.clearAllSettingTrackers();

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
        this.clearAllSettingTrackers();

        this.rigName = param;
        this.rigNameTracker = param != null;
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
                SessionReportTypeChoice.this.serialize(this.parentQName, factory, xmlWriter);
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
        String prefix = null;
        String namespace = null;

        if (serializeType)
        {
            final String namespacePrefix = this.registerPrefix(xmlWriter,
                    "http://remotelabs.eng.uts.edu.au/schedserver/reports");
            if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0))
            {
                this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
                        + ":SessionReportTypeChoice_type0", xmlWriter);
            }
            else
            {
                this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type",
                        "SessionReportTypeChoice_type0", xmlWriter);
            }
        }

        if (this.userClassTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = SessionReportTypeChoice.generatePrefix(namespace);
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

        if (this.userTracker)
        {
            if (this.user == null)
            {
                throw new ADBException("user cannot be null!!");
            }
            this.user.serialize(new QName("", "user"), factory, xmlWriter);
        }

        if (this.rigTypeTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = SessionReportTypeChoice.generatePrefix(namespace);
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
                    prefix = SessionReportTypeChoice.generatePrefix(namespace);
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
            prefix = SessionReportTypeChoice.generatePrefix(namespace);
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

        if (this.userTracker)
        {
            elementList.add(new QName("", "user"));
            if (this.user == null)
            {
                throw new ADBException("user cannot be null!!");
            }
            elementList.add(this.user);
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

        return new ADBXMLStreamReaderImpl(qName, elementList.toArray(), new Object[0]);
    }

    public static class Factory
    {
        public static SessionReportTypeChoice parse(final XMLStreamReader reader) throws Exception
        {
            final SessionReportTypeChoice object = new SessionReportTypeChoice();
            try
            {
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
                else if (reader.isStartElement() && new QName("", "user").equals(reader.getName()))
                {
                    object.setUser(RequestorType.Factory.parse(reader));
                    reader.next();
                }
                else if (reader.isStartElement() && new QName("", "rigType").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setRigType(ConverterUtil.convertToString(content));
                    reader.next();
                }
                else if (reader.isStartElement() && new QName("", "rigName").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setRigName(ConverterUtil.convertToString(content));
                    reader.next();
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
