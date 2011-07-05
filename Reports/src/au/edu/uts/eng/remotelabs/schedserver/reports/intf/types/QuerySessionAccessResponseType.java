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
 * QuerySessionAccessResponseType bean class.
 */
public class QuerySessionAccessResponseType implements ADBBean
{
    /*
     * This type was generated from the piece of schema that had
     * name = QuerySessionAccessResponseType
     * Namespace URI = http://remotelabs.eng.uts.edu.au/schedserver/reports
     * Namespace Prefix = ns1
     */

    private static final long serialVersionUID = 429396383072800544L;

    private static String generatePrefix(final String namespace)
    {
        if (namespace.equals("http://remotelabs.eng.uts.edu.au/schedserver/reports"))
        {
            return "ns1";
        }
        return BeanUtil.getUniquePrefix();
    }

    protected PaginationType pagination;

    public PaginationType getPagination()
    {
        return this.pagination;
    }

    public void setPagination(final PaginationType param)
    {
        this.pagination = param;
    }

    protected AccessReportType[] accessReportData;
    protected boolean accessReportDataTracker = false;

    public AccessReportType[] getAccessReportData()
    {
        return this.accessReportData;
    }

    public void setAccessReportData(final AccessReportType[] param)
    {
        this.accessReportData = param;
        this.accessReportDataTracker = param != null;
    }

    public void addAccessReportData(final AccessReportType param)
    {
        if (this.accessReportData == null)
        {
            this.accessReportData = new AccessReportType[] {};
        }

        this.accessReportDataTracker = true;

        @SuppressWarnings("unchecked")
        final List<AccessReportType> list = ConverterUtil.toList(this.accessReportData);
        list.add(param);
        this.accessReportData = list.toArray(new AccessReportType[list.size()]);
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
                QuerySessionAccessResponseType.this.serialize(this.parentQName, factory, xmlWriter);
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
                    prefix = QuerySessionAccessResponseType.generatePrefix(namespace);
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
                        + ":QuerySessionAccessResponseType", xmlWriter);
            }
            else
            {
                this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type",
                        "QuerySessionAccessResponseType", xmlWriter);
            }
        }

        if (this.pagination == null)
        {
            throw new ADBException("pagination cannot be null!!");
        }
        this.pagination.serialize(new QName("", "pagination"), factory, xmlWriter);
        
        if (this.accessReportDataTracker)
        {
            if (this.accessReportData != null)
            {
                for (final AccessReportType element : this.accessReportData)
                {
                    if (element != null)
                    {
                        element.serialize(new QName("", "accessReportData"), factory, xmlWriter);
                    }
                }
            }
            else
            {
                throw new ADBException("accessReportData cannot be null!!");
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
            prefix = QuerySessionAccessResponseType.generatePrefix(namespace);

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

        elementList.add(new QName("", "pagination"));
        if (this.pagination == null)
        {
            throw new ADBException("pagination cannot be null!!");
        }
        elementList.add(this.pagination);
        
        if (this.accessReportDataTracker)
        {
            if (this.accessReportData != null)
            {
                for (final AccessReportType element : this.accessReportData)
                {
                    if (element != null)
                    {
                        elementList.add(new QName("", "accessReportData"));
                        elementList.add(element);
                    }
                }
            }
            else
            {
                throw new ADBException("accessReportData cannot be null!!");
            }
        }

        return new ADBXMLStreamReaderImpl(qName, elementList.toArray(), new Object[0]);

    }

    public static class Factory
    {
        public static QuerySessionAccessResponseType parse(final XMLStreamReader reader) throws Exception
        {
            final QuerySessionAccessResponseType object = new QuerySessionAccessResponseType();
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
                        if (!"QuerySessionAccessResponseType".equals(type))
                        {
                            final String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (QuerySessionAccessResponseType) ExtensionMapper.getTypeObject(nsUri, type, reader);
                        }
                    }
                }

                reader.next();
                final ArrayList<AccessReportType> reportList = new ArrayList<AccessReportType>();
                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                if (reader.isStartElement() && new QName("", "pagination").equals(reader.getName()))
                {
                    object.setPagination(PaginationType.Factory.parse(reader));
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
                if (reader.isStartElement() && new QName("", "accessReportData").equals(reader.getName()))
                {
                    reportList.add(AccessReportType.Factory.parse(reader));

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
                            if (new QName("", "accessReportData").equals(reader.getName()))
                            {
                                reportList.add(AccessReportType.Factory.parse(reader));
                            }
                            else
                            {
                                noMore = true;
                            }
                        }
                    }

                    object.setAccessReportData((AccessReportType[]) ConverterUtil.convertToArray(
                            AccessReportType.class, reportList));
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
