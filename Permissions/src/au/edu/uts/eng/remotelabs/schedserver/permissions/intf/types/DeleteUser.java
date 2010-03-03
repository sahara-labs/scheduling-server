/**
 * DeleteUser.java
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1 Built on : Aug 19, 2008 (10:13:44 LKT)
 */

package au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types;

/**
 * DeleteUser bean class
 */

public class DeleteUser implements org.apache.axis2.databinding.ADBBean
{

    /**
                 * 
                 */
    private static final long serialVersionUID = -1753976071377542584L;
    public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
            "http://remotelabs.eng.uts.edu.au/schedserver/permissions", "deleteUser", "ns1");

    private static java.lang.String generatePrefix(final java.lang.String namespace)
    {
        if (namespace.equals("http://remotelabs.eng.uts.edu.au/schedserver/permissions"))
        {
            return "ns1";
        }
        return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
    }

    /**
     * field for DeleteUser
     */

    protected au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserIDType localDeleteUser;

    /**
     * Auto generated getter method
     * 
     * @return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserIDType
     */
    public au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserIDType getDeleteUser()
    {
        return this.localDeleteUser;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            DeleteUser
     */
    public void setDeleteUser(final au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserIDType param)
    {

        this.localDeleteUser = param;

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
                DeleteUser.MY_QNAME)
        {

            @Override
            public void serialize(final org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter)
                    throws javax.xml.stream.XMLStreamException
            {
                DeleteUser.this.serialize(DeleteUser.MY_QNAME, factory, xmlWriter);
            }
        };
        return new org.apache.axiom.om.impl.llom.OMSourcedElementImpl(DeleteUser.MY_QNAME, factory, dataSource);

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

        if (this.localDeleteUser == null)
        {
            throw new org.apache.axis2.databinding.ADBException("Property cannot be null!");
        }
        this.localDeleteUser.serialize(DeleteUser.MY_QNAME, factory, xmlWriter);

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
            prefix = DeleteUser.generatePrefix(namespace);

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
        return this.localDeleteUser.getPullParser(DeleteUser.MY_QNAME);

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
        public static DeleteUser parse(final javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception
        {
            final DeleteUser object = new DeleteUser();

            try
            {

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                new java.util.Vector();

                while (!reader.isEndElement())
                {
                    if (reader.isStartElement())
                    {

                        if (reader.isStartElement()
                                && new javax.xml.namespace.QName(
                                        "http://remotelabs.eng.uts.edu.au/schedserver/permissions", "deleteUser")
                                        .equals(reader.getName()))
                        {

                            object
                                    .setDeleteUser(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserIDType.Factory
                                            .parse(reader));

                        } // End of if for expected property start element

                        else
                        {
                            // A start element we are not expecting indicates an invalid parameter was passed
                            throw new org.apache.axis2.databinding.ADBException("Unexpected subelement "
                                    + reader.getLocalName());
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
