/**
 * PermissionIDType.java
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1 Built on : Aug 19, 2008 (10:13:44 LKT)
 */

package au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types;

/**
 * PermissionIDType bean class
 */

public class PermissionIDType extends au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.OperationRequestType
        implements org.apache.axis2.databinding.ADBBean
{
    /*
     * This type was generated from the piece of schema that had
     * name = PermissionIDType
     * Namespace URI = http://remotelabs.eng.uts.edu.au/schedserver/permissions
     * Namespace Prefix = ns1
     */

    /**
                 * 
                 */
    private static final long serialVersionUID = -6530567336267229955L;

    private static java.lang.String generatePrefix(final java.lang.String namespace)
    {
        if (namespace.equals("http://remotelabs.eng.uts.edu.au/schedserver/permissions"))
        {
            return "ns1";
        }
        return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
    }

    /**
     * field for PermissionID
     */

    protected int localPermissionID;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method
     * for this attribute. It will be used to determine whether to include this field
     * in the serialized XML
     */
    protected boolean localPermissionIDTracker = false;

    /**
     * Auto generated getter method
     * 
     * @return int
     */
    public int getPermissionID()
    {
        return this.localPermissionID;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            PermissionID
     */
    public void setPermissionID(final int param)
    {

        // setting primitive attribute tracker to true

        if (param == java.lang.Integer.MIN_VALUE)
        {
            this.localPermissionIDTracker = false;

        }
        else
        {
            this.localPermissionIDTracker = true;
        }

        this.localPermissionID = param;

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
    @Override
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
                PermissionIDType.this.serialize(this.parentQName, factory, xmlWriter);
            }
        };
        return new org.apache.axiom.om.impl.llom.OMSourcedElementImpl(parentQName, factory, dataSource);

    }

    @Override
    public void serialize(final javax.xml.namespace.QName parentQName, final org.apache.axiom.om.OMFactory factory,
            final org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter)
            throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException
    {
        this.serialize(parentQName, factory, xmlWriter, false);
    }

    @Override
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
                    prefix = PermissionIDType.generatePrefix(namespace);
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

        final java.lang.String namespacePrefix = this.registerPrefix(xmlWriter,
                "http://remotelabs.eng.uts.edu.au/schedserver/permissions");
        if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0))
        {
            this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
                    + ":PermissionIDType", xmlWriter);
        }
        else
        {
            this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "PermissionIDType",
                    xmlWriter);
        }

        if (this.localRequestorIDTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = PermissionIDType.generatePrefix(namespace);

                    xmlWriter.writeStartElement(prefix, "requestorID", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);

                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "requestorID");
                }

            }
            else
            {
                xmlWriter.writeStartElement("requestorID");
            }

            if (this.localRequestorID == java.lang.Integer.MIN_VALUE)
            {

                throw new org.apache.axis2.databinding.ADBException("requestorID cannot be null!!");

            }
            else
            {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                        .convertToString(this.localRequestorID));
            }

            xmlWriter.writeEndElement();
        }
        if (this.localOperationRequestTypeSequence_type0Tracker)
        {
            if (this.localOperationRequestTypeSequence_type0 == null)
            {
                throw new org.apache.axis2.databinding.ADBException(
                        "OperationRequestTypeSequence_type0 cannot be null!!");
            }
            this.localOperationRequestTypeSequence_type0.serialize(null, factory, xmlWriter);
        }
        if (this.localRequestorQNameTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = PermissionIDType.generatePrefix(namespace);

                    xmlWriter.writeStartElement(prefix, "requestorQName", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);

                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "requestorQName");
                }

            }
            else
            {
                xmlWriter.writeStartElement("requestorQName");
            }

            if (this.localRequestorQName == null)
            {
                // write the nil attribute

                throw new org.apache.axis2.databinding.ADBException("requestorQName cannot be null!!");

            }
            else
            {

                xmlWriter.writeCharacters(this.localRequestorQName);

            }

            xmlWriter.writeEndElement();
        }
        if (this.localPermissionIDTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = PermissionIDType.generatePrefix(namespace);

                    xmlWriter.writeStartElement(prefix, "permissionID", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);

                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "permissionID");
                }

            }
            else
            {
                xmlWriter.writeStartElement("permissionID");
            }

            if (this.localPermissionID == java.lang.Integer.MIN_VALUE)
            {

                throw new org.apache.axis2.databinding.ADBException("permissionID cannot be null!!");

            }
            else
            {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                        .convertToString(this.localPermissionID));
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
            prefix = PermissionIDType.generatePrefix(namespace);

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
    @Override
    public javax.xml.stream.XMLStreamReader getPullParser(final javax.xml.namespace.QName qName)
            throws org.apache.axis2.databinding.ADBException
    {

        final java.util.ArrayList elementList = new java.util.ArrayList();
        final java.util.ArrayList attribList = new java.util.ArrayList();

        attribList.add(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema-instance", "type"));
        attribList.add(new javax.xml.namespace.QName("http://remotelabs.eng.uts.edu.au/schedserver/permissions",
                "PermissionIDType"));
        if (this.localRequestorIDTracker)
        {
            elementList.add(new javax.xml.namespace.QName("", "requestorID"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(this.localRequestorID));
        }
        if (this.localOperationRequestTypeSequence_type0Tracker)
        {
            elementList.add(new javax.xml.namespace.QName("http://remotelabs.eng.uts.edu.au/schedserver/permissions",
                    "OperationRequestTypeSequence_type0"));

            if (this.localOperationRequestTypeSequence_type0 == null)
            {
                throw new org.apache.axis2.databinding.ADBException(
                        "OperationRequestTypeSequence_type0 cannot be null!!");
            }
            elementList.add(this.localOperationRequestTypeSequence_type0);
        }
        if (this.localRequestorQNameTracker)
        {
            elementList.add(new javax.xml.namespace.QName("", "requestorQName"));

            if (this.localRequestorQName != null)
            {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil
                        .convertToString(this.localRequestorQName));
            }
            else
            {
                throw new org.apache.axis2.databinding.ADBException("requestorQName cannot be null!!");
            }
        }
        if (this.localPermissionIDTracker)
        {
            elementList.add(new javax.xml.namespace.QName("", "permissionID"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(this.localPermissionID));
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
        public static PermissionIDType parse(final javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception
        {
            final PermissionIDType object = new PermissionIDType();

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

                        if (!"PermissionIDType".equals(type))
                        {
                            //find namespace for the prefix
                            final java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (PermissionIDType) au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.ExtensionMapper
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

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("", "requestorID").equals(reader.getName()))
                {

                    final java.lang.String content = reader.getElementText();

                    object.setRequestorID(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));

                    reader.next();

                } // End of if for expected property start element

                else
                {

                    object.setRequestorID(java.lang.Integer.MIN_VALUE);

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
                                .setOperationRequestTypeSequence_type0(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.OperationRequestTypeSequence_type0.Factory
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

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("", "requestorQName").equals(reader.getName()))
                {

                    final java.lang.String content = reader.getElementText();

                    object.setRequestorQName(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

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
                        && new javax.xml.namespace.QName("", "permissionID").equals(reader.getName()))
                {

                    final java.lang.String content = reader.getElementText();

                    object.setPermissionID(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));

                    reader.next();

                } // End of if for expected property start element

                else
                {

                    object.setPermissionID(java.lang.Integer.MIN_VALUE);

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
