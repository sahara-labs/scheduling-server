/**
 * QueryInfoResponseType.java
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4 Built on : Apr 26, 2008 (06:25:17 EDT)
 */

package au.edu.uts.eng.remotelabs.schedserver.reports.intf.types;

/**
 * QueryInfoResponseType bean class
 */

public class QueryInfoResponseType implements org.apache.axis2.databinding.ADBBean
{
    /*
     * This type was generated from the piece of schema that had
     * name = QueryInfoResponseType
     * Namespace URI = http://remotelabs.eng.uts.edu.au/schedserver/reports
     * Namespace Prefix = ns1
     */

    /**
                 * 
                 */
    private static final long serialVersionUID = 8523138000617888079L;

    private static java.lang.String generatePrefix(final java.lang.String namespace)
    {
        if (namespace.equals("http://remotelabs.eng.uts.edu.au/schedserver/reports"))
        {
            return "ns1";
        }
        return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
    }

    /**
     * field for TypeForQuery
     */

    protected au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.TypeForQuery_type3 localTypeForQuery;

    /**
     * Auto generated getter method
     * 
     * @return au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.TypeForQuery_type3
     */
    public au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.TypeForQuery_type3 getTypeForQuery()
    {
        return this.localTypeForQuery;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            TypeForQuery
     */
    public void setTypeForQuery(final au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.TypeForQuery_type3 param)
    {

        this.localTypeForQuery = param;

    }

    /**
     * field for SelectionResult
     * This was an Array!
     */

    protected java.lang.String[] localSelectionResult;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method
     * for this attribute. It will be used to determine whether to include this field
     * in the serialized XML
     */
    protected boolean localSelectionResultTracker = false;

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String[]
     */
    public java.lang.String[] getSelectionResult()
    {
        return this.localSelectionResult;
    }

    /**
     * validate the array for SelectionResult
     */
    protected void validateSelectionResult(final java.lang.String[] param)
    {

    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            SelectionResult
     */
    public void setSelectionResult(final java.lang.String[] param)
    {

        this.validateSelectionResult(param);

        if (param != null)
        {
            //update the setting tracker
            this.localSelectionResultTracker = true;
        }
        else
        {
            this.localSelectionResultTracker = false;

        }

        this.localSelectionResult = param;
    }

    /**
     * Auto generated add method for the array for convenience
     * 
     * @param param
     *            java.lang.String
     */
    public void addSelectionResult(final java.lang.String param)
    {
        if (this.localSelectionResult == null)
        {
            this.localSelectionResult = new java.lang.String[] {};
        }

        //update the setting tracker
        this.localSelectionResultTracker = true;

        final java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(this.localSelectionResult);
        list.add(param);
        this.localSelectionResult = (java.lang.String[]) list.toArray(new java.lang.String[list.size()]);

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
                QueryInfoResponseType.this.serialize(this.parentQName, factory, xmlWriter);
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
                    prefix = QueryInfoResponseType.generatePrefix(namespace);
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
                        + ":QueryInfoResponseType", xmlWriter);
            }
            else
            {
                this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type",
                        "QueryInfoResponseType", xmlWriter);
            }

        }

        if (this.localTypeForQuery == null)
        {
            throw new org.apache.axis2.databinding.ADBException("typeForQuery cannot be null!!");
        }
        this.localTypeForQuery.serialize(new javax.xml.namespace.QName("", "typeForQuery"), factory, xmlWriter);
        if (this.localSelectionResultTracker)
        {
            if (this.localSelectionResult != null)
            {
                namespace = "";
                final boolean emptyNamespace = namespace == null || namespace.length() == 0;
                prefix = emptyNamespace ? null : xmlWriter.getPrefix(namespace);
                for (final String element : this.localSelectionResult)
                {

                    if (element != null)
                    {

                        if (!emptyNamespace)
                        {
                            if (prefix == null)
                            {
                                final java.lang.String prefix2 = QueryInfoResponseType.generatePrefix(namespace);

                                xmlWriter.writeStartElement(prefix2, "selectionResult", namespace);
                                xmlWriter.writeNamespace(prefix2, namespace);
                                xmlWriter.setPrefix(prefix2, namespace);

                            }
                            else
                            {
                                xmlWriter.writeStartElement(namespace, "selectionResult");
                            }

                        }
                        else
                        {
                            xmlWriter.writeStartElement("selectionResult");
                        }

                        xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                                .convertToString(element));

                        xmlWriter.writeEndElement();

                    }
                    else
                    {

                        // we have to do nothing since minOccurs is zero

                    }

                }
            }
            else
            {

                throw new org.apache.axis2.databinding.ADBException("selectionResult cannot be null!!");

            }

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
            prefix = QueryInfoResponseType.generatePrefix(namespace);

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

        elementList.add(new javax.xml.namespace.QName("", "typeForQuery"));

        if (this.localTypeForQuery == null)
        {
            throw new org.apache.axis2.databinding.ADBException("typeForQuery cannot be null!!");
        }
        elementList.add(this.localTypeForQuery);
        if (this.localSelectionResultTracker)
        {
            if (this.localSelectionResult != null)
            {
                for (final String element : this.localSelectionResult)
                {

                    if (element != null)
                    {
                        elementList.add(new javax.xml.namespace.QName("", "selectionResult"));
                        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(element));
                    }
                    else
                    {

                        // have to do nothing

                    }

                }
            }
            else
            {

                throw new org.apache.axis2.databinding.ADBException("selectionResult cannot be null!!");

            }

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
        public static QueryInfoResponseType parse(final javax.xml.stream.XMLStreamReader reader)
                throws java.lang.Exception
        {
            final QueryInfoResponseType object = new QueryInfoResponseType();

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

                        if (!"QueryInfoResponseType".equals(type))
                        {
                            //find namespace for the prefix
                            final java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (QueryInfoResponseType) au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.ExtensionMapper
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
                        && new javax.xml.namespace.QName("", "typeForQuery").equals(reader.getName()))
                {

                    object
                            .setTypeForQuery(au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.TypeForQuery_type3.Factory
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
                        && new javax.xml.namespace.QName("", "selectionResult").equals(reader.getName()))
                {

                    // Process the array and step past its final element's end.
                    list2.add(reader.getElementText());

                    //loop until we find a start element that is not part of this array
                    boolean loopDone2 = false;
                    while (!loopDone2)
                    {
                        // Ensure we are at the EndElement
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
                            if (new javax.xml.namespace.QName("", "selectionResult").equals(reader.getName()))
                            {
                                list2.add(reader.getElementText());

                            }
                            else
                            {
                                loopDone2 = true;
                            }
                        }
                    }
                    // call the converter utility  to convert and set the array

                    object.setSelectionResult((java.lang.String[]) list2.toArray(new java.lang.String[list2.size()]));

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
