/**
 * OperationRequestTypeNameNSSequence.java
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1 Built on : Aug 19, 2008 (10:13:44 LKT)
 */

package au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types;

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
import org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl;
import org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter;

/**
 * OperationRequestTypeNameNSSequence bean class
 */
public class OperationRequestTypeNameNSSequence implements ADBBean
{
    private static final long serialVersionUID = 7834615871687808581L;

    protected UserClassIDType localUserClass;

    private static String generatePrefix(final String namespace)
    {
        if (namespace.equals("http://remotelabs.eng.uts.edu.au/schedserver/permissions"))
        {
            return "ns1";
        }
        return BeanUtil.getUniquePrefix();
    }

    /**
     * Auto generated getter method
     * 
     * @return UserClassIDType
     */
    public UserClassIDType getUserClass()
    {
        return this.localUserClass;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            UserClass
     */
    public void setUserClass(final UserClassIDType param)
    {

        this.localUserClass = param;

    }

    /**
     * field for User
     */

    protected UserIDType localUser;

    /**
     * Auto generated getter method
     * 
     * @return UserIDType
     */
    public UserIDType getUser()
    {
        return this.localUser;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            User
     */
    public void setUser(final UserIDType param)
    {

        this.localUser = param;

    }

    /**
     * isReaderMTOMAware
     * 
     * @return true if the reader supports MTOM
     */
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

    /**
     * @param parentQName
     * @param factory
     * @return OMElement
     */
    public OMElement getOMElement(final QName parentQName, final OMFactory factory) throws ADBException
    {

        final OMDataSource dataSource = new ADBDataSource(this, parentQName)
        {

            @Override
            public void serialize(final MTOMAwareXMLStreamWriter xmlWriter) throws XMLStreamException
            {
                OperationRequestTypeNameNSSequence.this.serialize(this.parentQName, factory, xmlWriter);
            }
        };
        return new OMSourcedElementImpl(parentQName, factory, dataSource);

    }

    public void serialize(final QName parentQName, final OMFactory factory, final MTOMAwareXMLStreamWriter xmlWriter)
            throws XMLStreamException, ADBException
    {
        this.serialize(parentQName, factory, xmlWriter, false);
    }

    public void serialize(final QName parentQName, final OMFactory factory, final MTOMAwareXMLStreamWriter xmlWriter,
            final boolean serializeType) throws XMLStreamException, ADBException
    {

        if (serializeType)
        {

            final String namespacePrefix = this.registerPrefix(xmlWriter,
                    "http://remotelabs.eng.uts.edu.au/schedserver/permissions");
            if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0))
            {
                this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
                        + ":OperationRequestTypeNameNSSequence", xmlWriter);
            }
            else
            {
                this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type",
                        "OperationRequestTypeNameNSSequence", xmlWriter);
            }

        }

        if (this.localUserClass == null)
        {
            throw new ADBException("userClass cannot be null!!");
        }
        this.localUserClass.serialize(new QName("", "userClass"), factory, xmlWriter);

        if (this.localUser == null)
        {
            throw new ADBException("user cannot be null!!");
        }
        this.localUser.serialize(new QName("", "user"), factory, xmlWriter);

    }

    /**
     * Util method to write an attribute with the ns prefix
     */
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

    /**
     * Register a namespace prefix
     */
    private String registerPrefix(final XMLStreamWriter xmlWriter, final String namespace) throws XMLStreamException
    {
        String prefix = xmlWriter.getPrefix(namespace);

        if (prefix == null)
        {
            prefix = OperationRequestTypeNameNSSequence.generatePrefix(namespace);

            while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null)
            {
                prefix = BeanUtil.getUniquePrefix();
            }

            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
        }

        return prefix;
    }

    /**
     * databinding method to get an XML representation of this object
     */
    public XMLStreamReader getPullParser(final QName qName) throws ADBException
    {

        final java.util.ArrayList elementList = new java.util.ArrayList();
        final java.util.ArrayList attribList = new java.util.ArrayList();

        elementList.add(new QName("", "userClass"));

        if (this.localUserClass == null)
        {
            throw new ADBException("userClass cannot be null!!");
        }
        elementList.add(this.localUserClass);

        elementList.add(new QName("", "user"));

        if (this.localUser == null)
        {
            throw new ADBException("user cannot be null!!");
        }
        elementList.add(this.localUser);

        return new ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());

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
        public static OperationRequestTypeNameNSSequence parse(final XMLStreamReader reader) throws Exception
        {
            final OperationRequestTypeNameNSSequence object = new OperationRequestTypeNameNSSequence();

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

                if (reader.isStartElement() && new QName("", "userClass").equals(reader.getName()))
                {

                    object.setUserClass(UserClassIDType.Factory.parse(reader));

                    reader.next();

                } // End of if for expected property start element

                else
                {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new ADBException("Unexpected subelement " + reader.getLocalName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                if (reader.isStartElement() && new QName("", "user").equals(reader.getName()))
                {

                    object.setUser(UserIDType.Factory.parse(reader));

                    reader.next();

                } // End of if for expected property start element

                else
                {
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new ADBException("Unexpected subelement " + reader.getLocalName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

            }
            catch (final XMLStreamException e)
            {
                throw new Exception(e);
            }

            return object;
        }

    }//end of factory class

}
