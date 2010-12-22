/**
 * QuerySessionReportType.java
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4 Built on : Apr 26, 2008 (06:25:17 EDT)
 */

package au.edu.uts.eng.remotelabs.schedserver.reports.intf.types;

/**
 * QuerySessionReportType bean class
 */

public class QuerySessionReportType implements org.apache.axis2.databinding.ADBBean
{
    /*
     * This type was generated from the piece of schema that had
     * name = QuerySessionReportType
     * Namespace URI = http://remotelabs.eng.uts.edu.au/schedserver/reports
     * Namespace Prefix = ns1
     */

    /**
                 * 
                 */
    private static final long serialVersionUID = -5121246029757741056L;

    private static java.lang.String generatePrefix(final java.lang.String namespace)
    {
        if (namespace.equals("http://remotelabs.eng.uts.edu.au/schedserver/reports"))
        {
            return "ns1";
        }
        return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
    }

    /**
     * field for Requestor
     */

    protected au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.RequestorType localRequestor;

    /**
     * Auto generated getter method
     * 
     * @return au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.RequestorType
     */
    public au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.RequestorType getRequestor()
    {
        return this.localRequestor;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            Requestor
     */
    public void setRequestor(final au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.RequestorType param)
    {

        this.localRequestor = param;

    }

    /**
     * field for QuerySelect
     */

    protected au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryFilterType localQuerySelect;

    /**
     * Auto generated getter method
     * 
     * @return au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryFilterType
     */
    public au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryFilterType getQuerySelect()
    {
        return this.localQuerySelect;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            QuerySelect
     */
    public void setQuerySelect(final au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryFilterType param)
    {

        this.localQuerySelect = param;

    }

    /**
     * field for QueryConstraints
     */

    protected au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryFilterType localQueryConstraints;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method
     * for this attribute. It will be used to determine whether to include this field
     * in the serialized XML
     */
    protected boolean localQueryConstraintsTracker = false;

    /**
     * Auto generated getter method
     * 
     * @return au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryFilterType
     */
    public au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryFilterType getQueryConstraints()
    {
        return this.localQueryConstraints;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            QueryConstraints
     */
    public void setQueryConstraints(final au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryFilterType param)
    {

        if (param != null)
        {
            //update the setting tracker
            this.localQueryConstraintsTracker = true;
        }
        else
        {
            this.localQueryConstraintsTracker = false;

        }

        this.localQueryConstraints = param;

    }

    /**
     * field for StartTime
     */

    protected java.util.Calendar localStartTime;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method
     * for this attribute. It will be used to determine whether to include this field
     * in the serialized XML
     */
    protected boolean localStartTimeTracker = false;

    /**
     * Auto generated getter method
     * 
     * @return java.util.Calendar
     */
    public java.util.Calendar getStartTime()
    {
        return this.localStartTime;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            StartTime
     */
    public void setStartTime(final java.util.Calendar param)
    {

        if (param != null)
        {
            //update the setting tracker
            this.localStartTimeTracker = true;
        }
        else
        {
            this.localStartTimeTracker = false;

        }

        this.localStartTime = param;

    }

    /**
     * field for EndTime
     */

    protected java.util.Calendar localEndTime;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method
     * for this attribute. It will be used to determine whether to include this field
     * in the serialized XML
     */
    protected boolean localEndTimeTracker = false;

    /**
     * Auto generated getter method
     * 
     * @return java.util.Calendar
     */
    public java.util.Calendar getEndTime()
    {
        return this.localEndTime;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            EndTime
     */
    public void setEndTime(final java.util.Calendar param)
    {

        if (param != null)
        {
            //update the setting tracker
            this.localEndTimeTracker = true;
        }
        else
        {
            this.localEndTimeTracker = false;

        }

        this.localEndTime = param;

    }

    /**
     * field for Pagination
     */

    protected au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.PaginationType localPagination;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method
     * for this attribute. It will be used to determine whether to include this field
     * in the serialized XML
     */
    protected boolean localPaginationTracker = false;

    /**
     * Auto generated getter method
     * 
     * @return au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.PaginationType
     */
    public au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.PaginationType getPagination()
    {
        return this.localPagination;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            Pagination
     */
    public void setPagination(final au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.PaginationType param)
    {

        if (param != null)
        {
            //update the setting tracker
            this.localPaginationTracker = true;
        }
        else
        {
            this.localPaginationTracker = false;

        }

        this.localPagination = param;

    }

    /**
     * isReaderMTOMAware
     * 
     * @return true if the reader supports MTOM
     */
    public static boolean isReaderMTOMAware(final javax.xml.stream.XMLStreamReader reader)
    {
        boolean isReaderMTOMAware = false;

        try
        {
            isReaderMTOMAware = java.lang.Boolean.TRUE.equals(reader
                    .getProperty(org.apache.axiom.om.OMConstants.IS_DATA_HANDLERS_AWARE));
        }
        catch (final java.lang.IllegalArgumentException e)
        {
            isReaderMTOMAware = false;
        }
        return isReaderMTOMAware;
    }

    /**
     * @param parentQName
     * @param factory
     * @return org.apache.axiom.om.OMElement
     */
    public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName,
            final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException
    {

        final org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(this,
                parentQName)
        {

            @Override
            public void serialize(final org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter)
                    throws javax.xml.stream.XMLStreamException
            {
                QuerySessionReportType.this.serialize(this.parentQName, factory, xmlWriter);
            }
        };
        return new org.apache.axiom.om.impl.llom.OMSourcedElementImpl(parentQName, factory, dataSource);

    }

    public void serialize(final javax.xml.namespace.QName parentQName, final org.apache.axiom.om.OMFactory factory,
            final org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter)
            throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException
    {
        this.serialize(parentQName, factory, xmlWriter, false);
    }

    public void serialize(final javax.xml.namespace.QName parentQName, final org.apache.axiom.om.OMFactory factory,
            final org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter,
            final boolean serializeType) throws javax.xml.stream.XMLStreamException,
            org.apache.axis2.databinding.ADBException
    {

        java.lang.String prefix = null;
        java.lang.String namespace = null;

        prefix = parentQName.getPrefix();
        namespace = parentQName.getNamespaceURI();

        if ((namespace != null) && (namespace.trim().length() > 0))
        {
            final java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null)
            {
                xmlWriter.writeStartElement(namespace, parentQName.getLocalPart());
            }
            else
            {
                if (prefix == null)
                {
                    prefix = QuerySessionReportType.generatePrefix(namespace);
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

            final java.lang.String namespacePrefix = this.registerPrefix(xmlWriter,
                    "http://remotelabs.eng.uts.edu.au/schedserver/reports");
            if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0))
            {
                this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
                        + ":QuerySessionReportType", xmlWriter);
            }
            else
            {
                this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type",
                        "QuerySessionReportType", xmlWriter);
            }

        }

        if (this.localRequestor == null)
        {
            throw new org.apache.axis2.databinding.ADBException("requestor cannot be null!!");
        }
        this.localRequestor.serialize(new javax.xml.namespace.QName("", "requestor"), factory, xmlWriter);

        if (this.localQuerySelect == null)
        {
            throw new org.apache.axis2.databinding.ADBException("querySelect cannot be null!!");
        }
        this.localQuerySelect.serialize(new javax.xml.namespace.QName("", "querySelect"), factory, xmlWriter);
        if (this.localQueryConstraintsTracker)
        {
            if (this.localQueryConstraints == null)
            {
                throw new org.apache.axis2.databinding.ADBException("queryConstraints cannot be null!!");
            }
            this.localQueryConstraints.serialize(new javax.xml.namespace.QName("", "queryConstraints"), factory,
                    xmlWriter);
        }
        if (this.localStartTimeTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = QuerySessionReportType.generatePrefix(namespace);

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

            if (this.localStartTime == null)
            {
                // write the nil attribute

                throw new org.apache.axis2.databinding.ADBException("startTime cannot be null!!");

            }
            else
            {

                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                        .convertToString(this.localStartTime));

            }

            xmlWriter.writeEndElement();
        }
        if (this.localEndTimeTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = QuerySessionReportType.generatePrefix(namespace);

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

            if (this.localEndTime == null)
            {
                // write the nil attribute

                throw new org.apache.axis2.databinding.ADBException("endTime cannot be null!!");

            }
            else
            {

                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                        .convertToString(this.localEndTime));

            }

            xmlWriter.writeEndElement();
        }
        if (this.localPaginationTracker)
        {
            if (this.localPagination == null)
            {
                throw new org.apache.axis2.databinding.ADBException("pagination cannot be null!!");
            }
            this.localPagination.serialize(new javax.xml.namespace.QName("", "pagination"), factory, xmlWriter);
        }
        xmlWriter.writeEndElement();

    }

    /**
     * Util method to write an attribute with the ns prefix
     */
    private void writeAttribute(final java.lang.String prefix, final java.lang.String namespace,
            final java.lang.String attName, final java.lang.String attValue,
            final javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException
    {
        if (xmlWriter.getPrefix(namespace) == null)
        {
            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);

        }

        xmlWriter.writeAttribute(namespace, attName, attValue);

    }

    /**
     * Register a namespace prefix
     */
    private java.lang.String registerPrefix(final javax.xml.stream.XMLStreamWriter xmlWriter,
            final java.lang.String namespace) throws javax.xml.stream.XMLStreamException
    {
        java.lang.String prefix = xmlWriter.getPrefix(namespace);

        if (prefix == null)
        {
            prefix = QuerySessionReportType.generatePrefix(namespace);

            while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null)
            {
                prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
            }

            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
        }

        return prefix;
    }

    /**
     * databinding method to get an XML representation of this object
     */
    public javax.xml.stream.XMLStreamReader getPullParser(final javax.xml.namespace.QName qName)
            throws org.apache.axis2.databinding.ADBException
    {

        final java.util.ArrayList elementList = new java.util.ArrayList();
        final java.util.ArrayList attribList = new java.util.ArrayList();

        elementList.add(new javax.xml.namespace.QName("", "requestor"));

        if (this.localRequestor == null)
        {
            throw new org.apache.axis2.databinding.ADBException("requestor cannot be null!!");
        }
        elementList.add(this.localRequestor);

        elementList.add(new javax.xml.namespace.QName("", "querySelect"));

        if (this.localQuerySelect == null)
        {
            throw new org.apache.axis2.databinding.ADBException("querySelect cannot be null!!");
        }
        elementList.add(this.localQuerySelect);
        if (this.localQueryConstraintsTracker)
        {
            elementList.add(new javax.xml.namespace.QName("", "queryConstraints"));

            if (this.localQueryConstraints == null)
            {
                throw new org.apache.axis2.databinding.ADBException("queryConstraints cannot be null!!");
            }
            elementList.add(this.localQueryConstraints);
        }
        if (this.localStartTimeTracker)
        {
            elementList.add(new javax.xml.namespace.QName("", "startTime"));

            if (this.localStartTime != null)
            {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(this.localStartTime));
            }
            else
            {
                throw new org.apache.axis2.databinding.ADBException("startTime cannot be null!!");
            }
        }
        if (this.localEndTimeTracker)
        {
            elementList.add(new javax.xml.namespace.QName("", "endTime"));

            if (this.localEndTime != null)
            {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(this.localEndTime));
            }
            else
            {
                throw new org.apache.axis2.databinding.ADBException("endTime cannot be null!!");
            }
        }
        if (this.localPaginationTracker)
        {
            elementList.add(new javax.xml.namespace.QName("", "pagination"));

            if (this.localPagination == null)
            {
                throw new org.apache.axis2.databinding.ADBException("pagination cannot be null!!");
            }
            elementList.add(this.localPagination);
        }

        return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(),
                attribList.toArray());

    }

    /**
     * Factory class that keeps the parse method
     */
    public static class Factory
    {

        /**
         * static method to create the object
         * Precondition: If this object is an element, the current or next start element starts this object and any
         * intervening reader events are ignorable
         * If this object is not an element, it is a complex type and the reader is at the event just after the outer
         * start element
         * Postcondition: If this object is an element, the reader is positioned at its end element
         * If this object is a complex type, the reader is positioned at the end element of its outer element
         */
        public static QuerySessionReportType parse(final javax.xml.stream.XMLStreamReader reader)
                throws java.lang.Exception
        {
            final QuerySessionReportType object = new QuerySessionReportType();

            try
            {

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type") != null)
                {
                    final java.lang.String fullTypeName = reader.getAttributeValue(
                            "http://www.w3.org/2001/XMLSchema-instance", "type");
                    if (fullTypeName != null)
                    {
                        java.lang.String nsPrefix = null;
                        if (fullTypeName.indexOf(":") > -1)
                        {
                            nsPrefix = fullTypeName.substring(0, fullTypeName.indexOf(":"));
                        }
                        nsPrefix = nsPrefix == null ? "" : nsPrefix;

                        final java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":") + 1);

                        if (!"QuerySessionReportType".equals(type))
                        {
                            //find namespace for the prefix
                            final java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (QuerySessionReportType) au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.ExtensionMapper
                                    .getTypeObject(nsUri, type, reader);
                        }

                    }

                }

                new java.util.Vector();

                reader.next();

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                if (reader.isStartElement() && new javax.xml.namespace.QName("", "requestor").equals(reader.getName()))
                {

                    object.setRequestor(au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.RequestorType.Factory
                            .parse(reader));

                    reader.next();

                } // End of if for expected property start element

                else
                {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement "
                            + reader.getLocalName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("", "querySelect").equals(reader.getName()))
                {

                    object
                            .setQuerySelect(au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryFilterType.Factory
                                    .parse(reader));

                    reader.next();

                } // End of if for expected property start element

                else
                {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement "
                            + reader.getLocalName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("", "queryConstraints").equals(reader.getName()))
                {

                    object
                            .setQueryConstraints(au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryFilterType.Factory
                                    .parse(reader));

                    reader.next();

                } // End of if for expected property start element

                else
                {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                if (reader.isStartElement() && new javax.xml.namespace.QName("", "startTime").equals(reader.getName()))
                {

                    final java.lang.String content = reader.getElementText();

                    object.setStartTime(org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));

                    reader.next();

                } // End of if for expected property start element

                else
                {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                if (reader.isStartElement() && new javax.xml.namespace.QName("", "endTime").equals(reader.getName()))
                {

                    final java.lang.String content = reader.getElementText();

                    object.setEndTime(org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));

                    reader.next();

                } // End of if for expected property start element

                else
                {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                if (reader.isStartElement() && new javax.xml.namespace.QName("", "pagination").equals(reader.getName()))
                {

                    object
                            .setPagination(au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.PaginationType.Factory
                                    .parse(reader));

                    reader.next();

                } // End of if for expected property start element

                else
                {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                if (reader.isStartElement())
                {
                    // A start element we are not expecting indicates a trailing invalid property
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement "
                            + reader.getLocalName());
                }

            }
            catch (final javax.xml.stream.XMLStreamException e)
            {
                throw new java.lang.Exception(e);
            }

            return object;
        }

    }//end of factory class

}
