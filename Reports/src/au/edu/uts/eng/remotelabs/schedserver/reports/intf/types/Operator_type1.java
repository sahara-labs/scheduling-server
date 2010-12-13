/**
 * Operator_type1.java
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4 Built on : Apr 26, 2008 (06:25:17 EDT)
 */

package au.edu.uts.eng.remotelabs.schedserver.reports.intf.types;

/**
 * Operator_type1 bean class
 */

public class Operator_type1 implements org.apache.axis2.databinding.ADBBean
{

    /**
                 * 
                 */
    private static final long serialVersionUID = -3507814399887274645L;

    public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
            "http://remotelabs.eng.uts.edu.au/schedserver/reports", "operator_type1", "ns1");

    private static java.lang.String generatePrefix(final java.lang.String namespace)
    {
        if (namespace.equals("http://remotelabs.eng.uts.edu.au/schedserver/reports"))
        {
            return "ns1";
        }
        return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
    }

    /**
     * field for Operator_type0
     */

    protected java.lang.String localOperator_type0;

    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor

    protected Operator_type1(final java.lang.String value, final boolean isRegisterValue)
    {
        this.localOperator_type0 = value;
        if (isRegisterValue)
        {

            Operator_type1._table_.put(this.localOperator_type0, this);

        }

    }

    public static final java.lang.String _value1 = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("AND");

    public static final java.lang.String _value2 = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("OR");

    public static final java.lang.String _value3 = org.apache.axis2.databinding.utils.ConverterUtil
            .convertToString("AND NOT");

    public static final Operator_type1 value1 = new Operator_type1(Operator_type1._value1, true);

    public static final Operator_type1 value2 = new Operator_type1(Operator_type1._value2, true);

    public static final Operator_type1 value3 = new Operator_type1(Operator_type1._value3, true);

    public java.lang.String getValue()
    {
        return this.localOperator_type0;
    }

    @Override
    public boolean equals(final java.lang.Object obj)
    {
        return (obj == this);
    }

    @Override
    public int hashCode()
    {
        return this.toString().hashCode();
    }

    @Override
    public java.lang.String toString()
    {

        return this.localOperator_type0.toString();

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
                Operator_type1.MY_QNAME)
        {

            @Override
            public void serialize(final org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter)
                    throws javax.xml.stream.XMLStreamException
            {
                Operator_type1.this.serialize(Operator_type1.MY_QNAME, factory, xmlWriter);
            }
        };
        return new org.apache.axiom.om.impl.llom.OMSourcedElementImpl(Operator_type1.MY_QNAME, factory, dataSource);

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

        //We can safely assume an element has only one type associated with it

        final java.lang.String namespace = parentQName.getNamespaceURI();
        final java.lang.String localName = parentQName.getLocalPart();

        if (!namespace.equals(""))
        {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = Operator_type1.generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, localName, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            }
            else
            {
                xmlWriter.writeStartElement(namespace, localName);
            }

        }
        else
        {
            xmlWriter.writeStartElement(localName);
        }

        // add the type details if this is used in a simple type
        if (serializeType)
        {
            final java.lang.String namespacePrefix = this.registerPrefix(xmlWriter,
                    "http://remotelabs.eng.uts.edu.au/schedserver/reports");
            if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0))
            {
                this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
                        + ":operator_type1", xmlWriter);
            }
            else
            {
                this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "operator_type1",
                        xmlWriter);
            }
        }

        if (this.localOperator_type0 == null)
        {

            throw new org.apache.axis2.databinding.ADBException("Value cannot be null !!");

        }
        else
        {

            xmlWriter.writeCharacters(this.localOperator_type0);

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
            prefix = Operator_type1.generatePrefix(namespace);

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

        //We can safely assume an element has only one type associated with it
        return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(Operator_type1.MY_QNAME,
                new java.lang.Object[] { org.apache.axis2.databinding.utils.reader.ADBXMLStreamReader.ELEMENT_TEXT,
                        org.apache.axis2.databinding.utils.ConverterUtil.convertToString(this.localOperator_type0) },
                null);

    }

    /**
     * Factory class that keeps the parse method
     */
    public static class Factory
    {

        public static Operator_type1 fromValue(final java.lang.String value) throws java.lang.IllegalArgumentException
        {
            final Operator_type1 enumeration = (Operator_type1)

            Operator_type1._table_.get(value);

            if (enumeration == null)
            {
                throw new java.lang.IllegalArgumentException();
            }
            return enumeration;
        }

        public static Operator_type1 fromString(final java.lang.String value, final java.lang.String namespaceURI)
                throws java.lang.IllegalArgumentException
        {
            try
            {

                return Factory.fromValue(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(value));

            }
            catch (final java.lang.Exception e)
            {
                throw new java.lang.IllegalArgumentException();
            }
        }

        public static Operator_type1 fromString(final javax.xml.stream.XMLStreamReader xmlStreamReader,
                final java.lang.String content)
        {
            if (content.indexOf(":") > -1)
            {
                final java.lang.String prefix = content.substring(0, content.indexOf(":"));
                final java.lang.String namespaceUri = xmlStreamReader.getNamespaceContext().getNamespaceURI(prefix);
                return Operator_type1.Factory.fromString(content, namespaceUri);
            }
            else
            {
                return Operator_type1.Factory.fromString(content, "");
            }
        }

        /**
         * static method to create the object
         * Precondition: If this object is an element, the current or next start element starts this object and any
         * intervening reader events are ignorable
         * If this object is not an element, it is a complex type and the reader is at the event just after the outer
         * start element
         * Postcondition: If this object is an element, the reader is positioned at its end element
         * If this object is a complex type, the reader is positioned at the end element of its outer element
         */
        public static Operator_type1 parse(final javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception
        {
            Operator_type1 object = null;
            new java.util.HashMap();
            new java.util.ArrayList();

            java.lang.String prefix = "";
            java.lang.String namespaceuri = "";
            try
            {

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                new java.util.Vector();

                while (!reader.isEndElement())
                {
                    if (reader.isStartElement() || reader.hasText())
                    {

                        final java.lang.String content = reader.getElementText();

                        if (content.indexOf(":") > 0)
                        {
                            // this seems to be a Qname so find the namespace and send
                            prefix = content.substring(0, content.indexOf(":"));
                            namespaceuri = reader.getNamespaceURI(prefix);
                            object = Operator_type1.Factory.fromString(content, namespaceuri);
                        }
                        else
                        {
                            // this seems to be not a qname send and empty namespace incase of it is
                            // check is done in fromString method
                            object = Operator_type1.Factory.fromString(content, "");
                        }

                    }
                    else
                    {
                        reader.next();
                    }
                } // end of while loop

            }
            catch (final javax.xml.stream.XMLStreamException e)
            {
                throw new java.lang.Exception(e);
            }

            return object;
        }

    }//end of factory class

}
