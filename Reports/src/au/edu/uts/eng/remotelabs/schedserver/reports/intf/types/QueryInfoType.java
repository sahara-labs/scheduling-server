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
 * QueryInfoType bean class.
 */
public class QueryInfoType implements ADBBean
{
    /*
     * This type was generated from the piece of schema that had
     * name = QueryInfoType
     * Namespace URI = http://remotelabs.eng.uts.edu.au/schedserver/reports
     * Namespace Prefix = ns1
     */

    private static final long serialVersionUID = -5012027798455432727L;

    private static String generatePrefix(final String namespace)
    {
        if (namespace.equals("http://remotelabs.eng.uts.edu.au/schedserver/reports"))
        {
            return "ns1";
        }
        return BeanUtil.getUniquePrefix();
    }

    protected QueryFilterType querySelect;

    public QueryFilterType getQuerySelect()
    {
        return this.querySelect;
    }

    public void setQuerySelect(final QueryFilterType param)
    {
        this.querySelect = param;
    }

    protected QueryFilterType[] queryFilter;
    protected boolean queryFilterTracker = false;

    public QueryFilterType[] getQueryFilter()
    {
        return this.queryFilter;
    }

    public void setQueryFilter(final QueryFilterType[] param)
    {
        this.queryFilter = param;
        this.queryFilterTracker = param != null;
    }

    public void addQueryFilter(final QueryFilterType param)
    {
        if (this.queryFilter == null)
        {
            this.queryFilter = new QueryFilterType[] {};
        }

        this.queryFilterTracker = true;

        @SuppressWarnings("unchecked")
        final List<QueryFilterType> list = ConverterUtil.toList(this.queryFilter);
        list.add(param);
        this.queryFilter = list.toArray(new QueryFilterType[list.size()]);

    }

    protected RequestorType requestor;

    public RequestorType getRequestor()
    {
        return this.requestor;
    }

    public void setRequestor(final RequestorType param)
    {
        this.requestor = param;
    }

    protected int limit;
    protected boolean limitTracker = false;

    public int getLimit()
    {
        return this.limit;
    }

    public void setLimit(final int param)
    {
        this.limit = param;
        this.limitTracker = param != Integer.MIN_VALUE;
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
                QueryInfoType.this.serialize(this.parentQName, factory, xmlWriter);
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
                    prefix = QueryInfoType.generatePrefix(namespace);
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
                        + ":QueryInfoType", xmlWriter);
            }
            else
            {
                this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "QueryInfoType",
                        xmlWriter);
            }
        }

        if (this.querySelect == null)
        {
            throw new ADBException("querySelect cannot be null!!");
        }
        this.querySelect.serialize(new QName("", "querySelect"), factory, xmlWriter);
        
        if (this.queryFilterTracker)
        {
            if (this.queryFilter != null)
            {
                for (final QueryFilterType element : this.queryFilter)
                {
                    if (element != null)
                    {
                        element.serialize(new QName("", "queryFilter"), factory, xmlWriter);
                    }
                }
            }
            else
            {
                throw new ADBException("queryFilter cannot be null!!");
            }
        }
        if (this.requestor == null)
        {
            throw new ADBException("requestor cannot be null!!");
        }
        this.requestor.serialize(new QName("", "requestor"), factory, xmlWriter);
        
        if (this.limitTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);
                if (prefix == null)
                {
                    prefix = QueryInfoType.generatePrefix(namespace);
                    xmlWriter.writeStartElement(prefix, "limit", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);
                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "limit");
                }
            }
            else
            {
                xmlWriter.writeStartElement("limit");
            }
            
            xmlWriter.writeCharacters(ConverterUtil.convertToString(this.limit));
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
            prefix = QueryInfoType.generatePrefix(namespace);

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

        elementList.add(new QName("", "querySelect"));

        if (this.querySelect == null)
        {
            throw new ADBException("querySelect cannot be null!!");
        }
        elementList.add(this.querySelect);
        
        if (this.queryFilterTracker)
        {
            if (this.queryFilter != null)
            {
                for (final QueryFilterType element : this.queryFilter)
                {
                    if (element != null)
                    {
                        elementList.add(new QName("", "queryFilter"));
                        elementList.add(element);
                    }
                }
            }
            else
            {
                throw new ADBException("queryFilter cannot be null!!");
            }
        }
        elementList.add(new QName("", "requestor"));

        if (this.requestor == null)
        {
            throw new ADBException("requestor cannot be null!!");
        }
        elementList.add(this.requestor);
        
        if (this.limitTracker)
        {
            elementList.add(new QName("", "limit"));
            elementList.add(ConverterUtil.convertToString(this.limit));
        }

        return new ADBXMLStreamReaderImpl(qName, elementList.toArray(), new Object[0]);
    }

    public static class Factory
    {
        public static QueryInfoType parse(final XMLStreamReader reader) throws Exception
        {
            final QueryInfoType object = new QueryInfoType();
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

                        if (!"QueryInfoType".equals(type))
                        {
                            final String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (QueryInfoType) ExtensionMapper.getTypeObject(nsUri, type, reader);
                        }
                    }
                }

                reader.next();
                final ArrayList<QueryFilterType> filterList = new ArrayList<QueryFilterType>();
                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                
                if (reader.isStartElement() && new QName("", "querySelect").equals(reader.getName()))
                {
                    object.setQuerySelect(QueryFilterType.Factory.parse(reader));
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
                if (reader.isStartElement() && new QName("", "queryFilter").equals(reader.getName()))
                {
                    filterList.add(QueryFilterType.Factory.parse(reader));

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
                            if (new QName("", "queryFilter").equals(reader.getName()))
                            {
                                filterList.add(QueryFilterType.Factory.parse(reader));
                            }
                            else
                            {
                                noMore = true;
                            }
                        }
                    }

                    object.setQueryFilter((QueryFilterType[]) ConverterUtil
                            .convertToArray(QueryFilterType.class, filterList));
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                if (reader.isStartElement() && new QName("", "requestor").equals(reader.getName()))
                {
                    object.setRequestor(RequestorType.Factory.parse(reader));
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
                if (reader.isStartElement() && new QName("", "limit").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setLimit(ConverterUtil.convertToInt(content));
                    reader.next();
                }
                else
                {
                    object.setLimit(Integer.MIN_VALUE);
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
