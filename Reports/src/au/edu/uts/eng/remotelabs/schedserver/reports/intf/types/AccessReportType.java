/**
 * AccessReportType.java
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4 Built on : Apr 26, 2008 (06:25:17 EDT)
 */

package au.edu.uts.eng.remotelabs.schedserver.reports.intf.types;

/**
 * AccessReportType bean class
 */

public class AccessReportType implements org.apache.axis2.databinding.ADBBean
{
    /*
     * This type was generated from the piece of schema that had
     * name = AccessReportType
     * Namespace URI = http://remotelabs.eng.uts.edu.au/schedserver/reports
     * Namespace Prefix = ns1
     */

    /**
                 * 
                 */
    private static final long serialVersionUID = -3491982879303282077L;

    private static java.lang.String generatePrefix(final java.lang.String namespace)
    {
        if (namespace.equals("http://remotelabs.eng.uts.edu.au/schedserver/reports"))
        {
            return "ns1";
        }
        return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
    }

    /**
     * field for User
     */

    protected au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.RequestorType localUser;

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

        this.localUser = param;

    }

    /**
     * field for AccessReportTypeChoice_type0
     * This was an Array!
     */

    protected au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.AccessReportTypeChoice_type0[] localAccessReportTypeChoice_type0;

    /**
     * Auto generated getter method
     * 
     * @return au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.AccessReportTypeChoice_type0[]
     */
    public au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.AccessReportTypeChoice_type0[] getAccessReportTypeChoice_type0()
    {
        return this.localAccessReportTypeChoice_type0;
    }

    /**
     * validate the array for AccessReportTypeChoice_type0
     */
    protected void validateAccessReportTypeChoice_type0(
            final au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.AccessReportTypeChoice_type0[] param)
    {

        if ((param != null) && (param.length < 1))
        {
            throw new java.lang.RuntimeException();
        }

    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            AccessReportTypeChoice_type0
     */
    public void setAccessReportTypeChoice_type0(
            final au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.AccessReportTypeChoice_type0[] param)
    {

        this.validateAccessReportTypeChoice_type0(param);

        this.localAccessReportTypeChoice_type0 = param;
    }

    /**
     * Auto generated add method for the array for convenience
     * 
     * @param param
     *            au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.AccessReportTypeChoice_type0
     */
    public void addAccessReportTypeChoice_type0(
            final au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.AccessReportTypeChoice_type0 param)
    {
        if (this.localAccessReportTypeChoice_type0 == null)
        {
            this.localAccessReportTypeChoice_type0 = new au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.AccessReportTypeChoice_type0[] {};
        }

        final java.util.List list = org.apache.axis2.databinding.utils.ConverterUtil
                .toList(this.localAccessReportTypeChoice_type0);
        list.add(param);
        this.localAccessReportTypeChoice_type0 = (au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.AccessReportTypeChoice_type0[]) list
                .toArray(new au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.AccessReportTypeChoice_type0[list
                        .size()]);

    }

    /**
     * field for QueueDuration
     */

    protected java.lang.String localQueueDuration;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method
     * for this attribute. It will be used to determine whether to include this field
     * in the serialized XML
     */
    protected boolean localQueueDurationTracker = false;

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getQueueDuration()
    {
        return this.localQueueDuration;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            QueueDuration
     */
    public void setQueueDuration(final java.lang.String param)
    {

        if (param != null)
        {
            //update the setting tracker
            this.localQueueDurationTracker = true;
        }
        else
        {
            this.localQueueDurationTracker = false;

        }

        this.localQueueDuration = param;

    }

    /**
     * field for SessionStartTime
     */

    protected java.util.Calendar localSessionStartTime;

    /**
     * Auto generated getter method
     * 
     * @return java.util.Calendar
     */
    public java.util.Calendar getSessionStartTime()
    {
        return this.localSessionStartTime;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            SessionStartTime
     */
    public void setSessionStartTime(final java.util.Calendar param)
    {

        this.localSessionStartTime = param;

    }

    /**
     * field for SessionEndTime
     */

    protected java.util.Calendar localSessionEndTime;

    /**
     * Auto generated getter method
     * 
     * @return java.util.Calendar
     */
    public java.util.Calendar getSessionEndTime()
    {
        return this.localSessionEndTime;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            SessionEndTime
     */
    public void setSessionEndTime(final java.util.Calendar param)
    {

        this.localSessionEndTime = param;

    }

    /**
     * field for SessionDuration
     */

    protected int localSessionDuration;

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

        this.localSessionDuration = param;

    }

    /**
     * field for ReasonForTermination
     */

    protected java.lang.String localReasonForTermination;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method
     * for this attribute. It will be used to determine whether to include this field
     * in the serialized XML
     */
    protected boolean localReasonForTerminationTracker = false;

    /**
     * Auto generated getter method
     * 
     * @return java.lang.String
     */
    public java.lang.String getReasonForTermination()
    {
        return this.localReasonForTermination;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            ReasonForTermination
     */
    public void setReasonForTermination(final java.lang.String param)
    {

        if (param != null)
        {
            //update the setting tracker
            this.localReasonForTerminationTracker = true;
        }
        else
        {
            this.localReasonForTerminationTracker = false;

        }

        this.localReasonForTermination = param;

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
                AccessReportType.this.serialize(this.parentQName, factory, xmlWriter);
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
                    prefix = AccessReportType.generatePrefix(namespace);
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
                        + ":AccessReportType", xmlWriter);
            }
            else
            {
                this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "AccessReportType",
                        xmlWriter);
            }

        }

        if (this.localUser == null)
        {
            throw new org.apache.axis2.databinding.ADBException("user cannot be null!!");
        }
        this.localUser.serialize(new javax.xml.namespace.QName("", "user"), factory, xmlWriter);

        if (this.localAccessReportTypeChoice_type0 != null)
        {
            for (final AccessReportTypeChoice_type0 element : this.localAccessReportTypeChoice_type0)
            {
                if (element != null)
                {
                    element.serialize(null, factory, xmlWriter);
                }
                else
                {

                    throw new org.apache.axis2.databinding.ADBException("AccessReportTypeChoice_type0 cannot be null!!");

                }

            }
        }
        else
        {
            throw new org.apache.axis2.databinding.ADBException("AccessReportTypeChoice_type0 cannot be null!!");
        }
        if (this.localQueueDurationTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = AccessReportType.generatePrefix(namespace);

                    xmlWriter.writeStartElement(prefix, "queueDuration", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);

                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "queueDuration");
                }

            }
            else
            {
                xmlWriter.writeStartElement("queueDuration");
            }

            if (this.localQueueDuration == null)
            {
                // write the nil attribute

                throw new org.apache.axis2.databinding.ADBException("queueDuration cannot be null!!");

            }
            else
            {

                xmlWriter.writeCharacters(this.localQueueDuration);

            }

            xmlWriter.writeEndElement();
        }
        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = AccessReportType.generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "sessionStartTime", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            }
            else
            {
                xmlWriter.writeStartElement(namespace, "sessionStartTime");
            }

        }
        else
        {
            xmlWriter.writeStartElement("sessionStartTime");
        }

        if (this.localSessionStartTime == null)
        {
            // write the nil attribute

            throw new org.apache.axis2.databinding.ADBException("sessionStartTime cannot be null!!");

        }
        else
        {

            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                    .convertToString(this.localSessionStartTime));

        }

        xmlWriter.writeEndElement();

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = AccessReportType.generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "sessionEndTime", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            }
            else
            {
                xmlWriter.writeStartElement(namespace, "sessionEndTime");
            }

        }
        else
        {
            xmlWriter.writeStartElement("sessionEndTime");
        }

        if (this.localSessionEndTime == null)
        {
            // write the nil attribute

            throw new org.apache.axis2.databinding.ADBException("sessionEndTime cannot be null!!");

        }
        else
        {

            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                    .convertToString(this.localSessionEndTime));

        }

        xmlWriter.writeEndElement();

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = AccessReportType.generatePrefix(namespace);

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
        if (this.localReasonForTerminationTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = AccessReportType.generatePrefix(namespace);

                    xmlWriter.writeStartElement(prefix, "reasonForTermination", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);

                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "reasonForTermination");
                }

            }
            else
            {
                xmlWriter.writeStartElement("reasonForTermination");
            }

            if (this.localReasonForTermination == null)
            {
                // write the nil attribute

                throw new org.apache.axis2.databinding.ADBException("reasonForTermination cannot be null!!");

            }
            else
            {

                xmlWriter.writeCharacters(this.localReasonForTermination);

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
                prefix = AccessReportType.generatePrefix(namespaceURI);
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
                        prefix = AccessReportType.generatePrefix(namespaceURI);
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
            prefix = AccessReportType.generatePrefix(namespace);

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

        elementList.add(new javax.xml.namespace.QName("", "user"));

        if (this.localUser == null)
        {
            throw new org.apache.axis2.databinding.ADBException("user cannot be null!!");
        }
        elementList.add(this.localUser);

        if (this.localAccessReportTypeChoice_type0 != null)
        {
            for (final AccessReportTypeChoice_type0 element : this.localAccessReportTypeChoice_type0)
            {

                if (element != null)
                {
                    elementList.add(new javax.xml.namespace.QName(
                            "http://remotelabs.eng.uts.edu.au/schedserver/reports", "AccessReportTypeChoice_type0"));
                    elementList.add(element);
                }
                else
                {

                    throw new org.apache.axis2.databinding.ADBException(
                            "AccessReportTypeChoice_type0 cannot be null !!");

                }

            }
        }
        else
        {

            throw new org.apache.axis2.databinding.ADBException("AccessReportTypeChoice_type0 cannot be null!!");

        }

        if (this.localQueueDurationTracker)
        {
            elementList.add(new javax.xml.namespace.QName("", "queueDuration"));

            if (this.localQueueDuration != null)
            {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil
                        .convertToString(this.localQueueDuration));
            }
            else
            {
                throw new org.apache.axis2.databinding.ADBException("queueDuration cannot be null!!");
            }
        }
        elementList.add(new javax.xml.namespace.QName("", "sessionStartTime"));

        if (this.localSessionStartTime != null)
        {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil
                    .convertToString(this.localSessionStartTime));
        }
        else
        {
            throw new org.apache.axis2.databinding.ADBException("sessionStartTime cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "sessionEndTime"));

        if (this.localSessionEndTime != null)
        {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(this.localSessionEndTime));
        }
        else
        {
            throw new org.apache.axis2.databinding.ADBException("sessionEndTime cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "sessionDuration"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(this.localSessionDuration));
        if (this.localReasonForTerminationTracker)
        {
            elementList.add(new javax.xml.namespace.QName("", "reasonForTermination"));

            if (this.localReasonForTermination != null)
            {
                elementList.add(org.apache.axis2.databinding.utils.ConverterUtil
                        .convertToString(this.localReasonForTermination));
            }
            else
            {
                throw new org.apache.axis2.databinding.ADBException("reasonForTermination cannot be null!!");
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
        public static AccessReportType parse(final javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception
        {
            final AccessReportType object = new AccessReportType();

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

                        if (!"AccessReportType".equals(type))
                        {
                            //find namespace for the prefix
                            final java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (AccessReportType) au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.ExtensionMapper
                                    .getTypeObject(nsUri, type, reader);
                        }

                    }

                }

                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                final java.util.Vector handledAttributes = new java.util.Vector();

                reader.next();

                final java.util.ArrayList list2 = new java.util.ArrayList();

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                if (reader.isStartElement() && new javax.xml.namespace.QName("", "user").equals(reader.getName()))
                {

                    object.setUser(au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.RequestorType.Factory
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

                if (reader.isStartElement())
                {

                    // Process the array and step past its final element's end.
                    list2
                            .add(au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.AccessReportTypeChoice_type0.Factory
                                    .parse(reader));
                    //loop until we find a start element that is not part of this array
                    boolean loopDone2 = false;
                    while (!loopDone2)
                    {

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
                            list2
                                    .add(au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.AccessReportTypeChoice_type0.Factory
                                            .parse(reader));
                        }
                    }
                    // call the converter utility  to convert and set the array
                    object
                            .setAccessReportTypeChoice_type0((au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.AccessReportTypeChoice_type0[]) org.apache.axis2.databinding.utils.ConverterUtil
                                    .convertToArray(
                                            au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.AccessReportTypeChoice_type0.class,
                                            list2));

                } // End of if for expected property start element

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("", "queueDuration").equals(reader.getName()))
                {

                    final java.lang.String content = reader.getElementText();

                    object.setQueueDuration(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

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
                        && new javax.xml.namespace.QName("", "sessionStartTime").equals(reader.getName()))
                {

                    final java.lang.String content = reader.getElementText();

                    object.setSessionStartTime(org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToDateTime(content));

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
                        && new javax.xml.namespace.QName("", "sessionEndTime").equals(reader.getName()))
                {

                    final java.lang.String content = reader.getElementText();

                    object.setSessionEndTime(org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToDateTime(content));

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
                    // A start element we are not expecting indicates an invalid parameter was passed
                    throw new org.apache.axis2.databinding.ADBException("Unexpected subelement "
                            + reader.getLocalName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("", "reasonForTermination").equals(reader.getName()))
                {

                    final java.lang.String content = reader.getElementText();

                    object.setReasonForTermination(org.apache.axis2.databinding.utils.ConverterUtil
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
