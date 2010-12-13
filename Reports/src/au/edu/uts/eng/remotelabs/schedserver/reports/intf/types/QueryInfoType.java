/**
 * QueryInfoType.java
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4 Built on : Apr 26, 2008 (06:25:17 EDT)
 */

package au.edu.uts.eng.remotelabs.schedserver.reports.intf.types;

/**
 * QueryInfoType bean class
 */

public class QueryInfoType implements org.apache.axis2.databinding.ADBBean
{
    /*
     * This type was generated from the piece of schema that had
     * name = QueryInfoType
     * Namespace URI = http://remotelabs.eng.uts.edu.au/schedserver/reports
     * Namespace Prefix = ns1
     */

    private static final long serialVersionUID = -5012027798455432727L;

    private static java.lang.String generatePrefix(final java.lang.String namespace)
    {
        if (namespace.equals("http://remotelabs.eng.uts.edu.au/schedserver/reports"))
        {
            return "ns1";
        }
        return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
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
     * field for QueryFilter
     * This was an Array!
     */

    protected au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryFilterType[] localQueryFilter;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method
     * for this attribute. It will be used to determine whether to include this field
     * in the serialized XML
     */
    protected boolean localQueryFilterTracker = false;

    /**
     * Auto generated getter method
     * 
     * @return au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryFilterType[]
     */
    public au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryFilterType[] getQueryFilter()
    {
        return this.localQueryFilter;
    }

    /**
     * validate the array for QueryFilter
     */
    protected void validateQueryFilter(
            final au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryFilterType[] param)
    {

    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            QueryFilter
     */
    public void setQueryFilter(final au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryFilterType[] param)
    {

        this.validateQueryFilter(param);

        if (param != null)
        {
            //update the setting tracker
            this.localQueryFilterTracker = true;
        }
        else
        {
            this.localQueryFilterTracker = false;

        }

        this.localQueryFilter = param;
    }

    /**
     * Auto generated add method for the array for convenience
     * 
     * @param param
     *            au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryFilterType
     */
    public void addQueryFilter(final au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryFilterType param)
    {
        if (this.localQueryFilter == null)
        {
            this.localQueryFilter = new au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryFilterType[] {};
        }

        //update the setting tracker
        this.localQueryFilterTracker = true;

        final java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(this.localQueryFilter);
        list.add(param);
        this.localQueryFilter = (au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryFilterType[]) list
                .toArray(new au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryFilterType[list.size()]);

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
     * field for Limit
     */

    protected int localLimit;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method
     * for this attribute. It will be used to determine whether to include this field
     * in the serialized XML
     */
    protected boolean localLimitTracker = false;

    /**
     * Auto generated getter method
     * 
     * @return int
     */
    public int getLimit()
    {
        return this.localLimit;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            Limit
     */
    public void setLimit(final int param)
    {

        // setting primitive attribute tracker to true

        if (param == java.lang.Integer.MIN_VALUE)
        {
            this.localLimitTracker = false;

        }
        else
        {
            this.localLimitTracker = true;
        }

        this.localLimit = param;

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
                QueryInfoType.this.serialize(this.parentQName, factory, xmlWriter);
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

            final java.lang.String namespacePrefix = this.registerPrefix(xmlWriter,
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

        if (this.localQuerySelect == null)
        {
            throw new org.apache.axis2.databinding.ADBException("querySelect cannot be null!!");
        }
        this.localQuerySelect.serialize(new javax.xml.namespace.QName("", "querySelect"), factory, xmlWriter);
        if (this.localQueryFilterTracker)
        {
            if (this.localQueryFilter != null)
            {
                for (final QueryFilterType element : this.localQueryFilter)
                {
                    if (element != null)
                    {
                        element.serialize(new javax.xml.namespace.QName("", "queryFilter"), factory, xmlWriter);
                    }
                    else
                    {

                        // we don't have to do any thing since minOccures is zero

                    }

                }
            }
            else
            {

                throw new org.apache.axis2.databinding.ADBException("queryFilter cannot be null!!");

            }
        }
        if (this.localRequestor == null)
        {
            throw new org.apache.axis2.databinding.ADBException("requestor cannot be null!!");
        }
        this.localRequestor.serialize(new javax.xml.namespace.QName("", "requestor"), factory, xmlWriter);
        if (this.localLimitTracker)
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

            if (this.localLimit == java.lang.Integer.MIN_VALUE)
            {

                throw new org.apache.axis2.databinding.ADBException("limit cannot be null!!");

            }
            else
            {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                        .convertToString(this.localLimit));
            }

            xmlWriter.writeEndElement();
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
            prefix = QueryInfoType.generatePrefix(namespace);

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

        elementList.add(new javax.xml.namespace.QName("", "querySelect"));

        if (this.localQuerySelect == null)
        {
            throw new org.apache.axis2.databinding.ADBException("querySelect cannot be null!!");
        }
        elementList.add(this.localQuerySelect);
        if (this.localQueryFilterTracker)
        {
            if (this.localQueryFilter != null)
            {
                for (final QueryFilterType element : this.localQueryFilter)
                {

                    if (element != null)
                    {
                        elementList.add(new javax.xml.namespace.QName("", "queryFilter"));
                        elementList.add(element);
                    }
                    else
                    {

                        // nothing to do

                    }

                }
            }
            else
            {

                throw new org.apache.axis2.databinding.ADBException("queryFilter cannot be null!!");

            }

        }
        elementList.add(new javax.xml.namespace.QName("", "requestor"));

        if (this.localRequestor == null)
        {
            throw new org.apache.axis2.databinding.ADBException("requestor cannot be null!!");
        }
        elementList.add(this.localRequestor);
        if (this.localLimitTracker)
        {
            elementList.add(new javax.xml.namespace.QName("", "limit"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(this.localLimit));
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
        public static QueryInfoType parse(final javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception
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

                        if (!"QueryInfoType".equals(type))
                        {
                            //find namespace for the prefix
                            final java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (QueryInfoType) au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.ExtensionMapper
                                    .getTypeObject(nsUri, type, reader);
                        }

                    }

                }

                new java.util.Vector();

                reader.next();

                final java.util.ArrayList list2 = new java.util.ArrayList();

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
                        && new javax.xml.namespace.QName("", "queryFilter").equals(reader.getName()))
                {

                    // Process the array and step past its final element's end.
                    list2.add(au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryFilterType.Factory
                            .parse(reader));

                    //loop until we find a start element that is not part of this array
                    boolean loopDone2 = false;
                    while (!loopDone2)
                    {
                        // We should be at the end element, but make sure
                        while (!reader.isEndElement())
                        {
                            reader.next();
                        }
                        // Step out of this element
                        reader.next();
                        // Step to next element event.
                        while (!reader.isStartElement() && !reader.isEndElement())
                        {
                            reader.next();
                        }
                        if (reader.isEndElement())
                        {
                            //two continuous end elements means we are exiting the xml structure
                            loopDone2 = true;
                        }
                        else
                        {
                            if (new javax.xml.namespace.QName("", "queryFilter").equals(reader.getName()))
                            {
                                list2
                                        .add(au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryFilterType.Factory
                                                .parse(reader));

                            }
                            else
                            {
                                loopDone2 = true;
                            }
                        }
                    }
                    // call the converter utility  to convert and set the array

                    object
                            .setQueryFilter((au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryFilterType[]) org.apache.axis2.databinding.utils.ConverterUtil
                                    .convertToArray(
                                            au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryFilterType.class,
                                            list2));

                } // End of if for expected property start element

                else
                {

                }

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

                if (reader.isStartElement() && new javax.xml.namespace.QName("", "limit").equals(reader.getName()))
                {

                    final java.lang.String content = reader.getElementText();

                    object.setLimit(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));

                    reader.next();

                } // End of if for expected property start element

                else
                {

                    object.setLimit(java.lang.Integer.MIN_VALUE);

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
