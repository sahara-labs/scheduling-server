/**
 * UserListType.java
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1 Built on : Aug 19, 2008 (10:13:44 LKT)
 */

package au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types;

/**
 * UserListType bean class
 */

public class UserListType implements org.apache.axis2.databinding.ADBBean
{
    /*
     * This type was generated from the piece of schema that had
     * name = UserListType
     * Namespace URI = http://remotelabs.eng.uts.edu.au/schedserver/permissions
     * Namespace Prefix = ns1
     */

    /**
                 * 
                 */
    private static final long serialVersionUID = -887344501785101659L;

    private static java.lang.String generatePrefix(final java.lang.String namespace)
    {
        if (namespace.equals("http://remotelabs.eng.uts.edu.au/schedserver/permissions"))
        {
            return "ns1";
        }
        return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
    }

    /**
     * field for User
     * This was an Array!
     */

    protected au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserIDType[] localUser;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method
     * for this attribute. It will be used to determine whether to include this field
     * in the serialized XML
     */
    protected boolean localUserTracker = false;

    /**
     * Auto generated getter method
     * 
     * @return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserIDType[]
     */
    public au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserIDType[] getUser()
    {
        return this.localUser;
    }

    /**
     * validate the array for User
     */
    protected void validateUser(final au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserIDType[] param)
    {

    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            User
     */
    public void setUser(final au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserIDType[] param)
    {

        this.validateUser(param);

        if (param != null)
        {
            //update the setting tracker
            this.localUserTracker = true;
        }
        else
        {
            this.localUserTracker = false;

        }

        this.localUser = param;
    }

    /**
     * Auto generated add method for the array for convenience
     * 
     * @param param
     *            au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserIDType
     */
    public void addUser(final au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserIDType param)
    {
        if (this.localUser == null)
        {
            this.localUser = new au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserIDType[] {};
        }

        //update the setting tracker
        this.localUserTracker = true;

        final java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil.toList(this.localUser);
        list.add(param);
        this.localUser = (au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserIDType[]) list
                .toArray(new au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserIDType[list.size()]);

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
                UserListType.this.serialize(this.parentQName, factory, xmlWriter);
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
                    prefix = UserListType.generatePrefix(namespace);
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
                    "http://remotelabs.eng.uts.edu.au/schedserver/permissions");
            if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0))
            {
                this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
                        + ":UserListType", xmlWriter);
            }
            else
            {
                this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "UserListType",
                        xmlWriter);
            }

        }
        if (this.localUserTracker)
        {
            if (this.localUser != null)
            {
                for (final UserIDType element : this.localUser)
                {
                    if (element != null)
                    {
                        element.serialize(new javax.xml.namespace.QName("", "user"), factory, xmlWriter);
                    }
                    else
                    {

                        // we don't have to do any thing since minOccures is zero

                    }

                }
            }
            else
            {

                throw new org.apache.axis2.databinding.ADBException("user cannot be null!!");

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
            prefix = UserListType.generatePrefix(namespace);

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

        if (this.localUserTracker)
        {
            if (this.localUser != null)
            {
                for (final UserIDType element : this.localUser)
                {

                    if (element != null)
                    {
                        elementList.add(new javax.xml.namespace.QName("", "user"));
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

                throw new org.apache.axis2.databinding.ADBException("user cannot be null!!");

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
        public static UserListType parse(final javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception
        {
            final UserListType object = new UserListType();

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

                        if (!"UserListType".equals(type))
                        {
                            //find namespace for the prefix
                            final java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (UserListType) au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.ExtensionMapper
                                    .getTypeObject(nsUri, type, reader);
                        }

                    }

                }

                new java.util.Vector();

                reader.next();

                final java.util.ArrayList list1 = new java.util.ArrayList();

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                if (reader.isStartElement() && new javax.xml.namespace.QName("", "user").equals(reader.getName()))
                {

                    // Process the array and step past its final element's end.
                    list1.add(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserIDType.Factory
                            .parse(reader));

                    //loop until we find a start element that is not part of this array
                    boolean loopDone1 = false;
                    while (!loopDone1)
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
                            loopDone1 = true;
                        }
                        else
                        {
                            if (new javax.xml.namespace.QName("", "user").equals(reader.getName()))
                            {
                                list1
                                        .add(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserIDType.Factory
                                                .parse(reader));

                            }
                            else
                            {
                                loopDone1 = true;
                            }
                        }
                    }
                    // call the converter utility  to convert and set the array

                    object
                            .setUser((au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserIDType[]) org.apache.axis2.databinding.utils.ConverterUtil
                                    .convertToArray(
                                            au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserIDType.class,
                                            list1));

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
