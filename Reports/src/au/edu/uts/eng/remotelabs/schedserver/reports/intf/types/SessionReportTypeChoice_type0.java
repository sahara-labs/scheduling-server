/**
 * SessionReportTypeChoice_type0.java
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4 Built on : Apr 26, 2008 (06:25:17 EDT)
 */

package au.edu.uts.eng.remotelabs.schedserver.reports.intf.types;

/**
 * SessionReportTypeChoice_type0 bean class
 */

public class SessionReportTypeChoice_type0 implements org.apache.axis2.databinding.ADBBean
{
    /*
     * This type was generated from the piece of schema that had
     * name = SessionReportTypeChoice_type0
     * Namespace URI = http://remotelabs.eng.uts.edu.au/schedserver/reports
     * Namespace Prefix = ns1
     */

    /**
                 * 
                 */
    private static final long serialVersionUID = 8491570126377821952L;

    private static java.lang.String generatePrefix(final java.lang.String namespace)
    {
        if (namespace.equals("http://remotelabs.eng.uts.edu.au/schedserver/reports"))
        {
            return "ns1";
        }
        return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
    }

    /**
     * Whenever a new property is set ensure all others are unset
     * There can be only one choice and the last one wins
     */
    private void clearAllSettingTrackers()
    {

        this.localUserClassTracker = false;

        this.localUserTracker = false;

        this.localRigTypeTracker = false;

        this.localRigNameTracker = false;

    }

    /**
     * field for UserClass
     */

    protected java.lang.String localUserClass;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method
     * for this attribute. It will be used to determine whether to include this field
     * in the serialized XML
     */
    protected boolean localUserClassTracker = false;

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getUserClass()
    {
        return this.localUserClass;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            UserClass
     */
    public void setUserClass(final java.lang.String param)
    {

        this.clearAllSettingTrackers();

        if (param != null)
        {
            //update the setting tracker
            this.localUserClassTracker = true;
        }
        else
        {
            this.localUserClassTracker = false;

        }

        this.localUserClass = param;

    }

    /**
     * field for User
     */

    protected au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.RequestorType localUser;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method
     * for this attribute. It will be used to determine whether to include this field
     * in the serialized XML
     */
    protected boolean localUserTracker = false;

    /**
     * Auto generated getter method
     * 
     * @return au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.RequestorType
     */
    public au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.RequestorType getUser()
    {
        return this.localUser;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            User
     */
    public void setUser(final au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.RequestorType param)
    {

        this.clearAllSettingTrackers();

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
     * field for RigType
     */

    protected java.lang.String localRigType;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method
     * for this attribute. It will be used to determine whether to include this field
     * in the serialized XML
     */
    protected boolean localRigTypeTracker = false;

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getRigType()
    {
        return this.localRigType;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            RigType
     */
    public void setRigType(final java.lang.String param)
    {

        this.clearAllSettingTrackers();

        if (param != null)
        {
            //update the setting tracker
            this.localRigTypeTracker = true;
        }
        else
        {
            this.localRigTypeTracker = false;

        }

        this.localRigType = param;

    }

    /**
     * field for RigName
     */

    protected java.lang.String localRigName;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method
     * for this attribute. It will be used to determine whether to include this field
     * in the serialized XML
     */
    protected boolean localRigNameTracker = false;

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getRigName()
    {
        return this.localRigName;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            RigName
     */
    public void setRigName(final java.lang.String param)
    {

        this.clearAllSettingTrackers();

        if (param != null)
        {
            //update the setting tracker
            this.localRigNameTracker = true;
        }
        else
        {
            this.localRigNameTracker = false;

        }

        this.localRigName = param;

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
                SessionReportTypeChoice_type0.this.serialize(this.parentQName, factory, xmlWriter);
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
                        + ":SessionReportTypeChoice_type0", xmlWriter);
            }
            else
            {
                this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type",
                        "SessionReportTypeChoice_type0", xmlWriter);
            }

        }
        if (this.localUserClassTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = SessionReportTypeChoice_type0.generatePrefix(namespace);

                    xmlWriter.writeStartElement(prefix, "userClass", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);

                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "userClass");
                }

            }
            else
            {
                xmlWriter.writeStartElement("userClass");
            }

            if (this.localUserClass == null)
            {
                // write the nil attribute

                throw new org.apache.axis2.databinding.ADBException("userClass cannot be null!!");

            }
            else
            {

                xmlWriter.writeCharacters(this.localUserClass);

            }

            xmlWriter.writeEndElement();
        }
        if (this.localUserTracker)
        {
            if (this.localUser == null)
            {
                throw new org.apache.axis2.databinding.ADBException("user cannot be null!!");
            }
            this.localUser.serialize(new javax.xml.namespace.QName("", "user"), factory, xmlWriter);
        }
        if (this.localRigTypeTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = SessionReportTypeChoice_type0.generatePrefix(namespace);

                    xmlWriter.writeStartElement(prefix, "rigType", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);

                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "rigType");
                }

            }
            else
            {
                xmlWriter.writeStartElement("rigType");
            }

            if (this.localRigType == null)
            {
                // write the nil attribute

                throw new org.apache.axis2.databinding.ADBException("rigType cannot be null!!");

            }
            else
            {

                xmlWriter.writeCharacters(this.localRigType);

            }

            xmlWriter.writeEndElement();
        }
        if (this.localRigNameTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = SessionReportTypeChoice_type0.generatePrefix(namespace);

                    xmlWriter.writeStartElement(prefix, "rigName", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);

                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "rigName");
                }

            }
            else
            {
                xmlWriter.writeStartElement("rigName");
            }

            if (this.localRigName == null)
            {
                // write the nil attribute

                throw new org.apache.axis2.databinding.ADBException("rigName cannot be null!!");

            }
            else
            {

                xmlWriter.writeCharacters(this.localRigName);

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
            prefix = SessionReportTypeChoice_type0.generatePrefix(namespace);

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

        if (this.localUserClassTracker)
        {
            elementList.add(new javax.xml.namespace.QName("", "userClass"));

            if (this.localUserClass != null)
            {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(this.localUserClass));
            }
            else
            {
                throw new org.apache.axis2.databinding.ADBException("userClass cannot be null!!");
            }
        }
        if (this.localUserTracker)
        {
            elementList.add(new javax.xml.namespace.QName("", "user"));

            if (this.localUser == null)
            {
                throw new org.apache.axis2.databinding.ADBException("user cannot be null!!");
            }
            elementList.add(this.localUser);
        }
        if (this.localRigTypeTracker)
        {
            elementList.add(new javax.xml.namespace.QName("", "rigType"));

            if (this.localRigType != null)
            {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(this.localRigType));
            }
            else
            {
                throw new org.apache.axis2.databinding.ADBException("rigType cannot be null!!");
            }
        }
        if (this.localRigNameTracker)
        {
            elementList.add(new javax.xml.namespace.QName("", "rigName"));

            if (this.localRigName != null)
            {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(this.localRigName));
            }
            else
            {
                throw new org.apache.axis2.databinding.ADBException("rigName cannot be null!!");
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
        public static SessionReportTypeChoice_type0 parse(final javax.xml.stream.XMLStreamReader reader)
                throws java.lang.Exception
        {
            final SessionReportTypeChoice_type0 object = new SessionReportTypeChoice_type0();

            try
            {

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                new java.util.Vector();

                if (reader.isStartElement() && new javax.xml.namespace.QName("", "userClass").equals(reader.getName()))
                {

                    final java.lang.String content = reader.getElementText();

                    object.setUserClass(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                    reader.next();

                } // End of if for expected property start element

                else

                if (reader.isStartElement() && new javax.xml.namespace.QName("", "user").equals(reader.getName()))
                {

                    object.setUser(au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.RequestorType.Factory
                            .parse(reader));

                    reader.next();

                } // End of if for expected property start element

                else

                if (reader.isStartElement() && new javax.xml.namespace.QName("", "rigType").equals(reader.getName()))
                {

                    final java.lang.String content = reader.getElementText();

                    object.setRigType(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                    reader.next();

                } // End of if for expected property start element

                else

                if (reader.isStartElement() && new javax.xml.namespace.QName("", "rigName").equals(reader.getName()))
                {

                    final java.lang.String content = reader.getElementText();

                    object.setRigName(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                    reader.next();

                } // End of if for expected property start element

            }
            catch (final javax.xml.stream.XMLStreamException e)
            {
                throw new java.lang.Exception(e);
            }

            return object;
        }

    }//end of factory class

}
