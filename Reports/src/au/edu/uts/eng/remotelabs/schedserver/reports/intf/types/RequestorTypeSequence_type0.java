/**
 * RequestorTypeSequence_type0.java
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4 Built on : Apr 26, 2008 (06:25:17 EDT)
 */

package au.edu.uts.eng.remotelabs.schedserver.reports.intf.types;

/**
 * RequestorTypeSequence_type0 bean class
 */

public class RequestorTypeSequence_type0 implements org.apache.axis2.databinding.ADBBean
{
    /*
     * This type was generated from the piece of schema that had
     * name = RequestorTypeSequence_type0
     * Namespace URI = http://remotelabs.eng.uts.edu.au/schedserver/reports
     * Namespace Prefix = ns1
     */

    /**
                 * 
                 */
    private static final long serialVersionUID = -1839979339962406841L;

    private static java.lang.String generatePrefix(final java.lang.String namespace)
    {
        if (namespace.equals("http://remotelabs.eng.uts.edu.au/schedserver/reports"))
        {
            return "ns1";
        }
        return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
    }

    /**
     * field for UserName
     */

    protected java.lang.String localUserName;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method
     * for this attribute. It will be used to determine whether to include this field
     * in the serialized XML
     */
    protected boolean localUserNameTracker = false;

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getUserName()
    {
        return this.localUserName;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            UserName
     */
    public void setUserName(final java.lang.String param)
    {

        if (param != null)
        {
            //update the setting tracker
            this.localUserNameTracker = true;
        }
        else
        {
            this.localUserNameTracker = false;

        }

        this.localUserName = param;

    }

    /**
     * field for UserNamespace
     */

    protected java.lang.String localUserNamespace;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method
     * for this attribute. It will be used to determine whether to include this field
     * in the serialized XML
     */
    protected boolean localUserNamespaceTracker = false;

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getUserNamespace()
    {
        return this.localUserNamespace;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            UserNamespace
     */
    public void setUserNamespace(final java.lang.String param)
    {

        if (param != null)
        {
            //update the setting tracker
            this.localUserNamespaceTracker = true;
        }
        else
        {
            this.localUserNamespaceTracker = false;

        }

        this.localUserNamespace = param;

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
                RequestorTypeSequence_type0.this.serialize(this.parentQName, factory, xmlWriter);
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

        if (serializeType)
        {

            final java.lang.String namespacePrefix = this.registerPrefix(xmlWriter,
                    "http://remotelabs.eng.uts.edu.au/schedserver/reports");
            if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0))
            {
                this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
                        + ":RequestorTypeSequence_type0", xmlWriter);
            }
            else
            {
                this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type",
                        "RequestorTypeSequence_type0", xmlWriter);
            }

        }
        if (this.localUserNameTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = RequestorTypeSequence_type0.generatePrefix(namespace);

                    xmlWriter.writeStartElement(prefix, "userName", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);

                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "userName");
                }

            }
            else
            {
                xmlWriter.writeStartElement("userName");
            }

            if (this.localUserName == null)
            {
                // write the nil attribute

                throw new org.apache.axis2.databinding.ADBException("userName cannot be null!!");

            }
            else
            {

                xmlWriter.writeCharacters(this.localUserName);

            }

            xmlWriter.writeEndElement();
        }
        if (this.localUserNamespaceTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = RequestorTypeSequence_type0.generatePrefix(namespace);

                    xmlWriter.writeStartElement(prefix, "userNamespace", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);

                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "userNamespace");
                }

            }
            else
            {
                xmlWriter.writeStartElement("userNamespace");
            }

            if (this.localUserNamespace == null)
            {
                // write the nil attribute

                throw new org.apache.axis2.databinding.ADBException("userNamespace cannot be null!!");

            }
            else
            {

                xmlWriter.writeCharacters(this.localUserNamespace);

            }

            xmlWriter.writeEndElement();
        }

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
            prefix = RequestorTypeSequence_type0.generatePrefix(namespace);

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

        if (this.localUserNameTracker)
        {
            elementList.add(new javax.xml.namespace.QName("", "userName"));

            if (this.localUserName != null)
            {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(this.localUserName));
            }
            else
            {
                throw new org.apache.axis2.databinding.ADBException("userName cannot be null!!");
            }
        }
        if (this.localUserNamespaceTracker)
        {
            elementList.add(new javax.xml.namespace.QName("", "userNamespace"));

            if (this.localUserNamespace != null)
            {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil
                        .convertToString(this.localUserNamespace));
            }
            else
            {
                throw new org.apache.axis2.databinding.ADBException("userNamespace cannot be null!!");
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
        public static RequestorTypeSequence_type0 parse(final javax.xml.stream.XMLStreamReader reader)
                throws java.lang.Exception
        {
            final RequestorTypeSequence_type0 object = new RequestorTypeSequence_type0();

            try
            {

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                new java.util.Vector();

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                if (reader.isStartElement() && new javax.xml.namespace.QName("", "userName").equals(reader.getName()))
                {

                    final java.lang.String content = reader.getElementText();

                    object.setUserName(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                    reader.next();

                } // End of if for expected property start element

                else
                {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("", "userNamespace").equals(reader.getName()))
                {

                    final java.lang.String content = reader.getElementText();

                    object.setUserNamespace(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                    reader.next();

                } // End of if for expected property start element

                else
                {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
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
