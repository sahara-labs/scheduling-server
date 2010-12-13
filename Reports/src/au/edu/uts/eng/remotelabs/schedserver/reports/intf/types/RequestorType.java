/**
 * RequestorType.java
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4 Built on : Apr 26, 2008 (06:25:17 EDT)
 */

package au.edu.uts.eng.remotelabs.schedserver.reports.intf.types;

/**
 * RequestorType bean class
 */

public class RequestorType implements org.apache.axis2.databinding.ADBBean
{
    /*
     * This type was generated from the piece of schema that had
     * name = RequestorType
     * Namespace URI = http://remotelabs.eng.uts.edu.au/schedserver/reports
     * Namespace Prefix = ns1
     */

    /**
                 * 
                 */
    private static final long serialVersionUID = 6135164550661709912L;

    private static java.lang.String generatePrefix(final java.lang.String namespace)
    {
        if (namespace.equals("http://remotelabs.eng.uts.edu.au/schedserver/reports"))
        {
            return "ns1";
        }
        return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
    }

    /**
     * field for UserID
     */

    protected java.lang.String localUserID;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method
     * for this attribute. It will be used to determine whether to include this field
     * in the serialized XML
     */
    protected boolean localUserIDTracker = false;

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getUserID()
    {
        return this.localUserID;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            UserID
     */
    public void setUserID(final java.lang.String param)
    {

        if (param != null)
        {
            //update the setting tracker
            this.localUserIDTracker = true;
        }
        else
        {
            this.localUserIDTracker = false;

        }

        this.localUserID = param;

    }

    /**
     * field for RequestorTypeSequence_type0
     */

    protected au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.RequestorTypeSequence_type0 localRequestorTypeSequence_type0;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method
     * for this attribute. It will be used to determine whether to include this field
     * in the serialized XML
     */
    protected boolean localRequestorTypeSequence_type0Tracker = false;

    /**
     * Auto generated getter method
     * 
     * @return au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.RequestorTypeSequence_type0
     */
    public au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.RequestorTypeSequence_type0 getRequestorTypeSequence_type0()
    {
        return this.localRequestorTypeSequence_type0;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            RequestorTypeSequence_type0
     */
    public void setRequestorTypeSequence_type0(
            final au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.RequestorTypeSequence_type0 param)
    {

        if (param != null)
        {
            //update the setting tracker
            this.localRequestorTypeSequence_type0Tracker = true;
        }
        else
        {
            this.localRequestorTypeSequence_type0Tracker = false;

        }

        this.localRequestorTypeSequence_type0 = param;

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
                RequestorType.this.serialize(this.parentQName, factory, xmlWriter);
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
                    prefix = RequestorType.generatePrefix(namespace);
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
                        + ":RequestorType", xmlWriter);
            }
            else
            {
                this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "RequestorType",
                        xmlWriter);
            }

        }
        if (this.localUserIDTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = RequestorType.generatePrefix(namespace);

                    xmlWriter.writeStartElement(prefix, "userID", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);

                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "userID");
                }

            }
            else
            {
                xmlWriter.writeStartElement("userID");
            }

            if (this.localUserID == null)
            {
                // write the nil attribute

                throw new org.apache.axis2.databinding.ADBException("userID cannot be null!!");

            }
            else
            {

                xmlWriter.writeCharacters(this.localUserID);

            }

            xmlWriter.writeEndElement();
        }
        if (this.localRequestorTypeSequence_type0Tracker)
        {
            if (this.localRequestorTypeSequence_type0 == null)
            {
                throw new org.apache.axis2.databinding.ADBException("RequestorTypeSequence_type0 cannot be null!!");
            }
            this.localRequestorTypeSequence_type0.serialize(null, factory, xmlWriter);
        }
        if (this.localUserNameTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = RequestorType.generatePrefix(namespace);

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
            prefix = RequestorType.generatePrefix(namespace);

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

        if (this.localUserIDTracker)
        {
            elementList.add(new javax.xml.namespace.QName("", "userID"));

            if (this.localUserID != null)
            {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(this.localUserID));
            }
            else
            {
                throw new org.apache.axis2.databinding.ADBException("userID cannot be null!!");
            }
        }
        if (this.localRequestorTypeSequence_type0Tracker)
        {
            elementList.add(new javax.xml.namespace.QName("http://remotelabs.eng.uts.edu.au/schedserver/reports",
                    "RequestorTypeSequence_type0"));

            if (this.localRequestorTypeSequence_type0 == null)
            {
                throw new org.apache.axis2.databinding.ADBException("RequestorTypeSequence_type0 cannot be null!!");
            }
            elementList.add(this.localRequestorTypeSequence_type0);
        }
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
        public static RequestorType parse(final javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception
        {
            final RequestorType object = new RequestorType();

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

                        if (!"RequestorType".equals(type))
                        {
                            //find namespace for the prefix
                            final java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (RequestorType) au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.ExtensionMapper
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

                if (reader.isStartElement() && new javax.xml.namespace.QName("", "userID").equals(reader.getName()))
                {

                    final java.lang.String content = reader.getElementText();

                    object.setUserID(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                    reader.next();

                } // End of if for expected property start element

                else
                {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                try
                {

                    if (reader.isStartElement())
                    {

                        object
                                .setRequestorTypeSequence_type0(au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.RequestorTypeSequence_type0.Factory
                                        .parse(reader));

                    } // End of if for expected property start element

                    else
                    {

                    }

                }
                catch (final java.lang.Exception e)
                {
                }

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
