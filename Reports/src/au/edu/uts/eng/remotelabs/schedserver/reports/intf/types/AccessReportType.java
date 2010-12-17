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
    private static final long serialVersionUID = -2061621727367281105L;

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
     * field for QueueStartTime
     */

    protected java.util.Calendar localQueueStartTime;

    /**
     * Auto generated getter method
     * 
     * @return java.util.Calendar
     */
    public java.util.Calendar getQueueStartTime()
    {
        return this.localQueueStartTime;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            QueueStartTime
     */
    public void setQueueStartTime(final java.util.Calendar param)
    {

        this.localQueueStartTime = param;

    }

    /**
     * field for QueueDuration
     */

    protected int localQueueDuration;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method
     * for this attribute. It will be used to determine whether to include this field
     * in the serialized XML
     */
    protected boolean localQueueDurationTracker = false;

    /**
     * Auto generated getter method
     * 
     * @return int
     */
    public int getQueueDuration()
    {
        return this.localQueueDuration;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            QueueDuration
     */
    public void setQueueDuration(final int param)
    {

        // setting primitive attribute tracker to true

        if (param == java.lang.Integer.MIN_VALUE)
        {
            this.localQueueDurationTracker = false;

        }
        else
        {
            this.localQueueDurationTracker = true;
        }

        this.localQueueDuration = param;

    }

    /**
     * field for SessionStartTime
     */

    protected java.util.Calendar localSessionStartTime;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method
     * for this attribute. It will be used to determine whether to include this field
     * in the serialized XML
     */
    protected boolean localSessionStartTimeTracker = false;

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

        if (param != null)
        {
            //update the setting tracker
            this.localSessionStartTimeTracker = true;
        }
        else
        {
            this.localSessionStartTimeTracker = false;

        }

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
     * field for WasAssigned
     */

    protected boolean localWasAssigned;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getWasAssigned()
    {
        return this.localWasAssigned;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            WasAssigned
     */
    public void setWasAssigned(final boolean param)
    {

        this.localWasAssigned = param;

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
        if (this.localUserClassTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = AccessReportType.generatePrefix(namespace);

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
        if (this.localRigTypeTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = AccessReportType.generatePrefix(namespace);

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
                    prefix = AccessReportType.generatePrefix(namespace);

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
        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = AccessReportType.generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "queueStartTime", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            }
            else
            {
                xmlWriter.writeStartElement(namespace, "queueStartTime");
            }

        }
        else
        {
            xmlWriter.writeStartElement("queueStartTime");
        }

        if (this.localQueueStartTime == null)
        {
            // write the nil attribute

            throw new org.apache.axis2.databinding.ADBException("queueStartTime cannot be null!!");

        }
        else
        {

            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                    .convertToString(this.localQueueStartTime));

        }

        xmlWriter.writeEndElement();
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

            if (this.localQueueDuration == java.lang.Integer.MIN_VALUE)
            {

                throw new org.apache.axis2.databinding.ADBException("queueDuration cannot be null!!");

            }
            else
            {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                        .convertToString(this.localQueueDuration));
            }

            xmlWriter.writeEndElement();
        }
        if (this.localSessionStartTimeTracker)
        {
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
        }
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
        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = AccessReportType.generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "wasAssigned", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            }
            else
            {
                xmlWriter.writeStartElement(namespace, "wasAssigned");
            }

        }
        else
        {
            xmlWriter.writeStartElement("wasAssigned");
        }

        if (false)
        {

            throw new org.apache.axis2.databinding.ADBException("wasAssigned cannot be null!!");

        }
        else
        {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                    .convertToString(this.localWasAssigned));
        }

        xmlWriter.writeEndElement();

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
        elementList.add(new javax.xml.namespace.QName("", "queueStartTime"));

        if (this.localQueueStartTime != null)
        {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(this.localQueueStartTime));
        }
        else
        {
            throw new org.apache.axis2.databinding.ADBException("queueStartTime cannot be null!!");
        }
        if (this.localQueueDurationTracker)
        {
            elementList.add(new javax.xml.namespace.QName("", "queueDuration"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(this.localQueueDuration));
        }
        if (this.localSessionStartTimeTracker)
        {
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
        elementList.add(new javax.xml.namespace.QName("", "wasAssigned"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(this.localWasAssigned));

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

                if (reader.isStartElement() && new javax.xml.namespace.QName("", "userClass").equals(reader.getName()))
                {

                    final java.lang.String content = reader.getElementText();

                    object.setUserClass(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                    reader.next();

                } // End of if for expected property start element

                else
                {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                if (reader.isStartElement() && new javax.xml.namespace.QName("", "rigType").equals(reader.getName()))
                {

                    final java.lang.String content = reader.getElementText();

                    object.setRigType(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

                    reader.next();

                } // End of if for expected property start element

                else
                {

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                if (reader.isStartElement() && new javax.xml.namespace.QName("", "rigName").equals(reader.getName()))
                {

                    final java.lang.String content = reader.getElementText();

                    object.setRigName(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(content));

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
                        && new javax.xml.namespace.QName("", "queueStartTime").equals(reader.getName()))
                {

                    final java.lang.String content = reader.getElementText();

                    object.setQueueStartTime(org.apache.axis2.databinding.utils.ConverterUtil
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
                        && new javax.xml.namespace.QName("", "queueDuration").equals(reader.getName()))
                {

                    final java.lang.String content = reader.getElementText();

                    object.setQueueDuration(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));

                    reader.next();

                } // End of if for expected property start element

                else
                {

                    object.setQueueDuration(java.lang.Integer.MIN_VALUE);

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

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("", "wasAssigned").equals(reader.getName()))
                {

                    final java.lang.String content = reader.getElementText();

                    object.setWasAssigned(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));

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
