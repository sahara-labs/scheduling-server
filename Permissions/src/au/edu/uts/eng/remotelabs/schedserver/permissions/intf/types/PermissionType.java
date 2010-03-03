/**
 * PermissionType.java
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1 Built on : Aug 19, 2008 (10:13:44 LKT)
 */

package au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types;

/**
 * PermissionType bean class
 */

public class PermissionType extends au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.PermissionIDType
        implements org.apache.axis2.databinding.ADBBean
{
    /*
     * This type was generated from the piece of schema that had
     * name = PermissionType
     * Namespace URI = http://remotelabs.eng.uts.edu.au/schedserver/permissions
     * Namespace Prefix = ns1
     */

    /**
                 * 
                 */
    private static final long serialVersionUID = -3543313127108184582L;

    private static java.lang.String generatePrefix(final java.lang.String namespace)
    {
        if (namespace.equals("http://remotelabs.eng.uts.edu.au/schedserver/permissions"))
        {
            return "ns1";
        }
        return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
    }

    /**
     * field for UserClass
     */

    protected au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserClassIDType localUserClass;

    /**
     * Auto generated getter method
     * 
     * @return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserClassIDType
     */
    public au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserClassIDType getUserClass()
    {
        return this.localUserClass;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            UserClass
     */
    public void setUserClass(final au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserClassIDType param)
    {

        this.localUserClass = param;

    }

    /**
     * field for ResourceClass
     */

    protected au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.ResourceClass_type1 localResourceClass;

    /**
     * Auto generated getter method
     * 
     * @return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.ResourceClass_type1
     */
    public au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.ResourceClass_type1 getResourceClass()
    {
        return this.localResourceClass;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            ResourceClass
     */
    public void setResourceClass(
            final au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.ResourceClass_type1 param)
    {

        this.localResourceClass = param;

    }

    /**
     * field for Resource
     */

    protected au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.ResourceIDType localResource;

    /**
     * Auto generated getter method
     * 
     * @return au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.ResourceIDType
     */
    public au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.ResourceIDType getResource()
    {
        return this.localResource;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            Resource
     */
    public void setResource(final au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.ResourceIDType param)
    {

        this.localResource = param;

    }

    /**
     * field for SessionDuration
     */

    protected int localSessionDuration;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method
     * for this attribute. It will be used to determine whether to include this field
     * in the serialized XML
     */
    protected boolean localSessionDurationTracker = false;

    /**
     * Auto generated getter method
     * 
     * @return int
     */
    public int getSessionDuration()
    {
        return this.localSessionDuration;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            SessionDuration
     */
    public void setSessionDuration(final int param)
    {

        // setting primitive attribute tracker to true

        if (param == java.lang.Integer.MIN_VALUE)
        {
            this.localSessionDurationTracker = false;

        }
        else
        {
            this.localSessionDurationTracker = true;
        }

        this.localSessionDuration = param;

    }

    /**
     * field for ExtensionDuration
     */

    protected int localExtensionDuration;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method
     * for this attribute. It will be used to determine whether to include this field
     * in the serialized XML
     */
    protected boolean localExtensionDurationTracker = false;

    /**
     * Auto generated getter method
     * 
     * @return int
     */
    public int getExtensionDuration()
    {
        return this.localExtensionDuration;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            ExtensionDuration
     */
    public void setExtensionDuration(final int param)
    {

        // setting primitive attribute tracker to true

        if (param == java.lang.Integer.MIN_VALUE)
        {
            this.localExtensionDurationTracker = false;

        }
        else
        {
            this.localExtensionDurationTracker = true;
        }

        this.localExtensionDuration = param;

    }

    /**
     * field for AllowedExtensions
     */

    protected int localAllowedExtensions;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method
     * for this attribute. It will be used to determine whether to include this field
     * in the serialized XML
     */
    protected boolean localAllowedExtensionsTracker = false;

    /**
     * Auto generated getter method
     * 
     * @return int
     */
    public int getAllowedExtensions()
    {
        return this.localAllowedExtensions;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            AllowedExtensions
     */
    public void setAllowedExtensions(final int param)
    {

        // setting primitive attribute tracker to true

        if (param == java.lang.Integer.MIN_VALUE)
        {
            this.localAllowedExtensionsTracker = false;

        }
        else
        {
            this.localAllowedExtensionsTracker = true;
        }

        this.localAllowedExtensions = param;

    }

    /**
     * field for QueueActivityTmOut
     */

    protected int localQueueActivityTmOut;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method
     * for this attribute. It will be used to determine whether to include this field
     * in the serialized XML
     */
    protected boolean localQueueActivityTmOutTracker = false;

    /**
     * Auto generated getter method
     * 
     * @return int
     */
    public int getQueueActivityTmOut()
    {
        return this.localQueueActivityTmOut;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            QueueActivityTmOut
     */
    public void setQueueActivityTmOut(final int param)
    {

        // setting primitive attribute tracker to true

        if (param == java.lang.Integer.MIN_VALUE)
        {
            this.localQueueActivityTmOutTracker = false;

        }
        else
        {
            this.localQueueActivityTmOutTracker = true;
        }

        this.localQueueActivityTmOut = param;

    }

    /**
     * field for SessionActivityTmOut
     */

    protected java.lang.String localSessionActivityTmOut;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method
     * for this attribute. It will be used to determine whether to include this field
     * in the serialized XML
     */
    protected boolean localSessionActivityTmOutTracker = false;

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getSessionActivityTmOut()
    {
        return this.localSessionActivityTmOut;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            SessionActivityTmOut
     */
    public void setSessionActivityTmOut(final java.lang.String param)
    {

        if (param != null)
        {
            //update the setting tracker
            this.localSessionActivityTmOutTracker = true;
        }
        else
        {
            this.localSessionActivityTmOutTracker = false;

        }

        this.localSessionActivityTmOut = param;

    }

    /**
     * field for Start
     */

    protected java.util.Calendar localStart;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method
     * for this attribute. It will be used to determine whether to include this field
     * in the serialized XML
     */
    protected boolean localStartTracker = false;

    /**
     * Auto generated getter method
     * 
     * @return java.util.Calendar
     */
    public java.util.Calendar getStart()
    {
        return this.localStart;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            Start
     */
    public void setStart(final java.util.Calendar param)
    {

        if (param != null)
        {
            //update the setting tracker
            this.localStartTracker = true;
        }
        else
        {
            this.localStartTracker = false;

        }

        this.localStart = param;

    }

    /**
     * field for Expiry
     */

    protected java.util.Calendar localExpiry;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method
     * for this attribute. It will be used to determine whether to include this field
     * in the serialized XML
     */
    protected boolean localExpiryTracker = false;

    /**
     * Auto generated getter method
     * 
     * @return java.util.Calendar
     */
    public java.util.Calendar getExpiry()
    {
        return this.localExpiry;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            Expiry
     */
    public void setExpiry(final java.util.Calendar param)
    {

        if (param != null)
        {
            //update the setting tracker
            this.localExpiryTracker = true;
        }
        else
        {
            this.localExpiryTracker = false;

        }

        this.localExpiry = param;

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

            public void serialize(final org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter)
                    throws javax.xml.stream.XMLStreamException
            {
                PermissionType.this.serialize(this.parentQName, factory, xmlWriter);
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
                    prefix = PermissionType.generatePrefix(namespace);
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
                    + ":PermissionType", xmlWriter);
        }
        else
        {
            this
                    .writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "PermissionType",
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
                    prefix = PermissionType.generatePrefix(namespace);

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
                    prefix = PermissionType.generatePrefix(namespace);

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
                    prefix = PermissionType.generatePrefix(namespace);

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
        if (this.localUserClass == null)
        {
            throw new org.apache.axis2.databinding.ADBException("userClass cannot be null!!");
        }
        this.localUserClass.serialize(new javax.xml.namespace.QName("", "userClass"), factory, xmlWriter);

        if (this.localResourceClass == null)
        {
            throw new org.apache.axis2.databinding.ADBException("resourceClass cannot be null!!");
        }
        this.localResourceClass.serialize(new javax.xml.namespace.QName("", "resourceClass"), factory, xmlWriter);

        if (this.localResource == null)
        {
            throw new org.apache.axis2.databinding.ADBException("resource cannot be null!!");
        }
        this.localResource.serialize(new javax.xml.namespace.QName("", "resource"), factory, xmlWriter);
        if (this.localSessionDurationTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = PermissionType.generatePrefix(namespace);

                    xmlWriter.writeStartElement(prefix, "sessionDuration", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);

                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "sessionDuration");
                }

            }
            else
            {
                xmlWriter.writeStartElement("sessionDuration");
            }

            if (this.localSessionDuration == java.lang.Integer.MIN_VALUE)
            {

                throw new org.apache.axis2.databinding.ADBException("sessionDuration cannot be null!!");

            }
            else
            {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                        .convertToString(this.localSessionDuration));
            }

            xmlWriter.writeEndElement();
        }
        if (this.localExtensionDurationTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = PermissionType.generatePrefix(namespace);

                    xmlWriter.writeStartElement(prefix, "extensionDuration", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);

                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "extensionDuration");
                }

            }
            else
            {
                xmlWriter.writeStartElement("extensionDuration");
            }

            if (this.localExtensionDuration == java.lang.Integer.MIN_VALUE)
            {

                throw new org.apache.axis2.databinding.ADBException("extensionDuration cannot be null!!");

            }
            else
            {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                        .convertToString(this.localExtensionDuration));
            }

            xmlWriter.writeEndElement();
        }
        if (this.localAllowedExtensionsTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = PermissionType.generatePrefix(namespace);

                    xmlWriter.writeStartElement(prefix, "allowedExtensions", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);

                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "allowedExtensions");
                }

            }
            else
            {
                xmlWriter.writeStartElement("allowedExtensions");
            }

            if (this.localAllowedExtensions == java.lang.Integer.MIN_VALUE)
            {

                throw new org.apache.axis2.databinding.ADBException("allowedExtensions cannot be null!!");

            }
            else
            {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                        .convertToString(this.localAllowedExtensions));
            }

            xmlWriter.writeEndElement();
        }
        if (this.localQueueActivityTmOutTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = PermissionType.generatePrefix(namespace);

                    xmlWriter.writeStartElement(prefix, "queueActivityTmOut", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);

                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "queueActivityTmOut");
                }

            }
            else
            {
                xmlWriter.writeStartElement("queueActivityTmOut");
            }

            if (this.localQueueActivityTmOut == java.lang.Integer.MIN_VALUE)
            {

                throw new org.apache.axis2.databinding.ADBException("queueActivityTmOut cannot be null!!");

            }
            else
            {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                        .convertToString(this.localQueueActivityTmOut));
            }

            xmlWriter.writeEndElement();
        }
        if (this.localSessionActivityTmOutTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = PermissionType.generatePrefix(namespace);

                    xmlWriter.writeStartElement(prefix, "sessionActivityTmOut", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);

                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "sessionActivityTmOut");
                }

            }
            else
            {
                xmlWriter.writeStartElement("sessionActivityTmOut");
            }

            if (this.localSessionActivityTmOut == null)
            {
                // write the nil attribute

                throw new org.apache.axis2.databinding.ADBException("sessionActivityTmOut cannot be null!!");

            }
            else
            {

                xmlWriter.writeCharacters(this.localSessionActivityTmOut);

            }

            xmlWriter.writeEndElement();
        }
        if (this.localStartTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = PermissionType.generatePrefix(namespace);

                    xmlWriter.writeStartElement(prefix, "start", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);

                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "start");
                }

            }
            else
            {
                xmlWriter.writeStartElement("start");
            }

            if (this.localStart == null)
            {
                // write the nil attribute

                throw new org.apache.axis2.databinding.ADBException("start cannot be null!!");

            }
            else
            {

                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                        .convertToString(this.localStart));

            }

            xmlWriter.writeEndElement();
        }
        if (this.localExpiryTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = PermissionType.generatePrefix(namespace);

                    xmlWriter.writeStartElement(prefix, "expiry", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);

                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "expiry");
                }

            }
            else
            {
                xmlWriter.writeStartElement("expiry");
            }

            if (this.localExpiry == null)
            {
                // write the nil attribute

                throw new org.apache.axis2.databinding.ADBException("expiry cannot be null!!");

            }
            else
            {

                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                        .convertToString(this.localExpiry));

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
     * Util method to write an attribute without the ns prefix
     */
    private void writeAttribute(final java.lang.String namespace, final java.lang.String attName,
            final java.lang.String attValue, final javax.xml.stream.XMLStreamWriter xmlWriter)
            throws javax.xml.stream.XMLStreamException
    {
        if (namespace.equals(""))
        {
            xmlWriter.writeAttribute(attName, attValue);
        }
        else
        {
            this.registerPrefix(xmlWriter, namespace);
            xmlWriter.writeAttribute(namespace, attName, attValue);
        }
    }

    /**
     * Util method to write an attribute without the ns prefix
     */
    private void writeQNameAttribute(final java.lang.String namespace, final java.lang.String attName,
            final javax.xml.namespace.QName qname, final javax.xml.stream.XMLStreamWriter xmlWriter)
            throws javax.xml.stream.XMLStreamException
    {

        final java.lang.String attributeNamespace = qname.getNamespaceURI();
        java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);
        if (attributePrefix == null)
        {
            attributePrefix = this.registerPrefix(xmlWriter, attributeNamespace);
        }
        java.lang.String attributeValue;
        if (attributePrefix.trim().length() > 0)
        {
            attributeValue = attributePrefix + ":" + qname.getLocalPart();
        }
        else
        {
            attributeValue = qname.getLocalPart();
        }

        if (namespace.equals(""))
        {
            xmlWriter.writeAttribute(attName, attributeValue);
        }
        else
        {
            this.registerPrefix(xmlWriter, namespace);
            xmlWriter.writeAttribute(namespace, attName, attributeValue);
        }
    }

    /**
     * method to handle Qnames
     */

    private void writeQName(final javax.xml.namespace.QName qname, final javax.xml.stream.XMLStreamWriter xmlWriter)
            throws javax.xml.stream.XMLStreamException
    {
        final java.lang.String namespaceURI = qname.getNamespaceURI();
        if (namespaceURI != null)
        {
            java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
            if (prefix == null)
            {
                prefix = PermissionType.generatePrefix(namespaceURI);
                xmlWriter.writeNamespace(prefix, namespaceURI);
                xmlWriter.setPrefix(prefix, namespaceURI);
            }

            if (prefix.trim().length() > 0)
            {
                xmlWriter.writeCharacters(prefix + ":"
                        + org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }
            else
            {
                // i.e this is the default namespace
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
            }

        }
        else
        {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qname));
        }
    }

    private void writeQNames(final javax.xml.namespace.QName[] qnames, final javax.xml.stream.XMLStreamWriter xmlWriter)
            throws javax.xml.stream.XMLStreamException
    {

        if (qnames != null)
        {
            // we have to store this data until last moment since it is not possible to write any
            // namespace data after writing the charactor data
            final java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
            java.lang.String namespaceURI = null;
            java.lang.String prefix = null;

            for (int i = 0; i < qnames.length; i++)
            {
                if (i > 0)
                {
                    stringToWrite.append(" ");
                }
                namespaceURI = qnames[i].getNamespaceURI();
                if (namespaceURI != null)
                {
                    prefix = xmlWriter.getPrefix(namespaceURI);
                    if ((prefix == null) || (prefix.length() == 0))
                    {
                        prefix = PermissionType.generatePrefix(namespaceURI);
                        xmlWriter.writeNamespace(prefix, namespaceURI);
                        xmlWriter.setPrefix(prefix, namespaceURI);
                    }

                    if (prefix.trim().length() > 0)
                    {
                        stringToWrite.append(prefix).append(":").append(
                                org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                    }
                    else
                    {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil
                                .convertToString(qnames[i]));
                    }
                }
                else
                {
                    stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(qnames[i]));
                }
            }
            xmlWriter.writeCharacters(stringToWrite.toString());
        }

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
            prefix = PermissionType.generatePrefix(namespace);

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

        attribList.add(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema-instance", "type"));
        attribList.add(new javax.xml.namespace.QName("http://remotelabs.eng.uts.edu.au/schedserver/permissions",
                "PermissionType"));
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
        elementList.add(new javax.xml.namespace.QName("", "userClass"));

        if (this.localUserClass == null)
        {
            throw new org.apache.axis2.databinding.ADBException("userClass cannot be null!!");
        }
        elementList.add(this.localUserClass);

        elementList.add(new javax.xml.namespace.QName("", "resourceClass"));

        if (this.localResourceClass == null)
        {
            throw new org.apache.axis2.databinding.ADBException("resourceClass cannot be null!!");
        }
        elementList.add(this.localResourceClass);

        elementList.add(new javax.xml.namespace.QName("", "resource"));

        if (this.localResource == null)
        {
            throw new org.apache.axis2.databinding.ADBException("resource cannot be null!!");
        }
        elementList.add(this.localResource);
        if (this.localSessionDurationTracker)
        {
            elementList.add(new javax.xml.namespace.QName("", "sessionDuration"));

            elementList
                    .add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(this.localSessionDuration));
        }
        if (this.localExtensionDurationTracker)
        {
            elementList.add(new javax.xml.namespace.QName("", "extensionDuration"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil
                    .convertToString(this.localExtensionDuration));
        }
        if (this.localAllowedExtensionsTracker)
        {
            elementList.add(new javax.xml.namespace.QName("", "allowedExtensions"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil
                    .convertToString(this.localAllowedExtensions));
        }
        if (this.localQueueActivityTmOutTracker)
        {
            elementList.add(new javax.xml.namespace.QName("", "queueActivityTmOut"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil
                    .convertToString(this.localQueueActivityTmOut));
        }
        if (this.localSessionActivityTmOutTracker)
        {
            elementList.add(new javax.xml.namespace.QName("", "sessionActivityTmOut"));

            if (this.localSessionActivityTmOut != null)
            {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil
                        .convertToString(this.localSessionActivityTmOut));
            }
            else
            {
                throw new org.apache.axis2.databinding.ADBException("sessionActivityTmOut cannot be null!!");
            }
        }
        if (this.localStartTracker)
        {
            elementList.add(new javax.xml.namespace.QName("", "start"));

            if (this.localStart != null)
            {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(this.localStart));
            }
            else
            {
                throw new org.apache.axis2.databinding.ADBException("start cannot be null!!");
            }
        }
        if (this.localExpiryTracker)
        {
            elementList.add(new javax.xml.namespace.QName("", "expiry"));

            if (this.localExpiry != null)
            {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(this.localExpiry));
            }
            else
            {
                throw new org.apache.axis2.databinding.ADBException("expiry cannot be null!!");
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
        public static PermissionType parse(final javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception
        {
            final PermissionType object = new PermissionType();

            final int event;
            final java.lang.String nillableValue = null;
            final java.lang.String prefix = "";
            final java.lang.String namespaceuri = "";
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

                        if (!"PermissionType".equals(type))
                        {
                            //find namespace for the prefix
                            final java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (PermissionType) au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.ExtensionMapper
                                    .getTypeObject(nsUri, type, reader);
                        }

                    }

                }

                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                final java.util.Vector handledAttributes = new java.util.Vector();

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

                if (reader.isStartElement() && new javax.xml.namespace.QName("", "userClass").equals(reader.getName()))
                {

                    object
                            .setUserClass(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.UserClassIDType.Factory
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
                        && new javax.xml.namespace.QName("", "resourceClass").equals(reader.getName()))
                {

                    object
                            .setResourceClass(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.ResourceClass_type1.Factory
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

                if (reader.isStartElement() && new javax.xml.namespace.QName("", "resource").equals(reader.getName()))
                {

                    object
                            .setResource(au.edu.uts.eng.remotelabs.schedserver.permissions.intf.types.ResourceIDType.Factory
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
                        && new javax.xml.namespace.QName("", "sessionDuration").equals(reader.getName()))
                {

                    final java.lang.String content = reader.getElementText();

                    object.setSessionDuration(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));

                    reader.next();

                } // End of if for expected property start element

                else
                {

                    object.setSessionDuration(java.lang.Integer.MIN_VALUE);

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("", "extensionDuration").equals(reader.getName()))
                {

                    final java.lang.String content = reader.getElementText();

                    object.setExtensionDuration(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));

                    reader.next();

                } // End of if for expected property start element

                else
                {

                    object.setExtensionDuration(java.lang.Integer.MIN_VALUE);

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("", "allowedExtensions").equals(reader.getName()))
                {

                    final java.lang.String content = reader.getElementText();

                    object.setAllowedExtensions(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));

                    reader.next();

                } // End of if for expected property start element

                else
                {

                    object.setAllowedExtensions(java.lang.Integer.MIN_VALUE);

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("", "queueActivityTmOut").equals(reader.getName()))
                {

                    final java.lang.String content = reader.getElementText();

                    object
                            .setQueueActivityTmOut(org.apache.axis2.databinding.utils.ConverterUtil
                                    .convertToInt(content));

                    reader.next();

                } // End of if for expected property start element

                else
                {

                    object.setQueueActivityTmOut(java.lang.Integer.MIN_VALUE);

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("", "sessionActivityTmOut").equals(reader.getName()))
                {

                    final java.lang.String content = reader.getElementText();

                    object.setSessionActivityTmOut(org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToString(content));

                    reader.next();

                } // End of if for expected property start element

                else
                {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                if (reader.isStartElement() && new javax.xml.namespace.QName("", "start").equals(reader.getName()))
                {

                    final java.lang.String content = reader.getElementText();

                    object.setStart(org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));

                    reader.next();

                } // End of if for expected property start element

                else
                {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                if (reader.isStartElement() && new javax.xml.namespace.QName("", "expiry").equals(reader.getName()))
                {

                    final java.lang.String content = reader.getElementText();

                    object.setExpiry(org.apache.axis2.databinding.utils.ConverterUtil.convertToDateTime(content));

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
