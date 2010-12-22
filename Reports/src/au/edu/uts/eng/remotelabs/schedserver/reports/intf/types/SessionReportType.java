/**
 * SessionReportType.java
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4 Built on : Apr 26, 2008 (06:25:17 EDT)
 */

package au.edu.uts.eng.remotelabs.schedserver.reports.intf.types;

/**
 * SessionReportType bean class
 */

public class SessionReportType implements org.apache.axis2.databinding.ADBBean
{
    /*
     * This type was generated from the piece of schema that had
     * name = SessionReportType
     * Namespace URI = http://remotelabs.eng.uts.edu.au/schedserver/reports
     * Namespace Prefix = ns1
     */

    /**
                 * 
                 */
    private static final long serialVersionUID = 8215676446095079500L;

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
     * field for AveQueueDuration
     */

    protected float localAveQueueDuration;

    /**
     * Auto generated getter method
     * 
     * @return float
     */
    public float getAveQueueDuration()
    {
        return this.localAveQueueDuration;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            AveQueueDuration
     */
    public void setAveQueueDuration(final float param)
    {

        this.localAveQueueDuration = param;

    }

    /**
     * field for MedQueueDuration
     */

    protected float localMedQueueDuration;

    /**
     * Auto generated getter method
     * 
     * @return float
     */
    public float getMedQueueDuration()
    {
        return this.localMedQueueDuration;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            MedQueueDuration
     */
    public void setMedQueueDuration(final float param)
    {

        this.localMedQueueDuration = param;

    }

    /**
     * field for MinQueueDuration
     */

    protected float localMinQueueDuration;

    /**
     * Auto generated getter method
     * 
     * @return float
     */
    public float getMinQueueDuration()
    {
        return this.localMinQueueDuration;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            MinQueueDuration
     */
    public void setMinQueueDuration(final float param)
    {

        this.localMinQueueDuration = param;

    }

    /**
     * field for TotalQueueDuration
     */

    protected float localTotalQueueDuration;

    /**
     * Auto generated getter method
     * 
     * @return float
     */
    public float getTotalQueueDuration()
    {
        return this.localTotalQueueDuration;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            TotalQueueDuration
     */
    public void setTotalQueueDuration(final float param)
    {

        this.localTotalQueueDuration = param;

    }

    /**
     * field for SessionCount
     */

    protected int localSessionCount;

    /**
     * Auto generated getter method
     * 
     * @return int
     */
    public int getSessionCount()
    {
        return this.localSessionCount;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            SessionCount
     */
    public void setSessionCount(final int param)
    {

        this.localSessionCount = param;

    }

    /**
     * field for UserCount
     */

    protected int localUserCount;

    /*
     * This tracker boolean wil be used to detect whether the user called the set method
     * for this attribute. It will be used to determine whether to include this field
     * in the serialized XML
     */
    protected boolean localUserCountTracker = false;

    /**
     * Auto generated getter method
     * 
     * @return int
     */
    public int getUserCount()
    {
        return this.localUserCount;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            UserCount
     */
    public void setUserCount(final int param)
    {

        // setting primitive attribute tracker to true

        if (param == java.lang.Integer.MIN_VALUE)
        {
            this.localUserCountTracker = false;

        }
        else
        {
            this.localUserCountTracker = true;
        }

        this.localUserCount = param;

    }

    /**
     * field for AveSessionDuration
     */

    protected float localAveSessionDuration;

    /**
     * Auto generated getter method
     * 
     * @return float
     */
    public float getAveSessionDuration()
    {
        return this.localAveSessionDuration;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            AveSessionDuration
     */
    public void setAveSessionDuration(final float param)
    {

        this.localAveSessionDuration = param;

    }

    /**
     * field for MedSessionDuration
     */

    protected float localMedSessionDuration;

    /**
     * Auto generated getter method
     * 
     * @return float
     */
    public float getMedSessionDuration()
    {
        return this.localMedSessionDuration;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            MedSessionDuration
     */
    public void setMedSessionDuration(final float param)
    {

        this.localMedSessionDuration = param;

    }

    /**
     * field for MaxSessionDuration
     */

    protected float localMaxSessionDuration;

    /**
     * Auto generated getter method
     * 
     * @return float
     */
    public float getMaxSessionDuration()
    {
        return this.localMaxSessionDuration;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            MaxSessionDuration
     */
    public void setMaxSessionDuration(final float param)
    {

        this.localMaxSessionDuration = param;

    }

    /**
     * field for MinSessionDuration
     */

    protected float localMinSessionDuration;

    /**
     * Auto generated getter method
     * 
     * @return float
     */
    public float getMinSessionDuration()
    {
        return this.localMinSessionDuration;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            MinSessionDuration
     */
    public void setMinSessionDuration(final float param)
    {

        this.localMinSessionDuration = param;

    }

    /**
     * field for TotalSessionDuration
     */

    protected float localTotalSessionDuration;

    /**
     * Auto generated getter method
     * 
     * @return float
     */
    public float getTotalSessionDuration()
    {
        return this.localTotalSessionDuration;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            TotalSessionDuration
     */
    public void setTotalSessionDuration(final float param)
    {

        this.localTotalSessionDuration = param;

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
                SessionReportType.this.serialize(this.parentQName, factory, xmlWriter);
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
                    prefix = SessionReportType.generatePrefix(namespace);
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
                        + ":SessionReportType", xmlWriter);
            }
            else
            {
                this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "SessionReportType",
                        xmlWriter);
            }

        }
        if (this.localUserTracker)
        {
            if (this.localUser == null)
            {
                throw new org.apache.axis2.databinding.ADBException("user cannot be null!!");
            }
            this.localUser.serialize(new javax.xml.namespace.QName("", "user"), factory, xmlWriter);
        }
        if (this.localUserClassTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = SessionReportType.generatePrefix(namespace);

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
                    prefix = SessionReportType.generatePrefix(namespace);

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
                    prefix = SessionReportType.generatePrefix(namespace);

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
                prefix = SessionReportType.generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "aveQueueDuration", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            }
            else
            {
                xmlWriter.writeStartElement(namespace, "aveQueueDuration");
            }

        }
        else
        {
            xmlWriter.writeStartElement("aveQueueDuration");
        }

        if (java.lang.Float.isNaN(this.localAveQueueDuration))
        {

            throw new org.apache.axis2.databinding.ADBException("aveQueueDuration cannot be null!!");

        }
        else
        {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                    .convertToString(this.localAveQueueDuration));
        }

        xmlWriter.writeEndElement();

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = SessionReportType.generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "medQueueDuration", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            }
            else
            {
                xmlWriter.writeStartElement(namespace, "medQueueDuration");
            }

        }
        else
        {
            xmlWriter.writeStartElement("medQueueDuration");
        }

        if (java.lang.Float.isNaN(this.localMedQueueDuration))
        {

            throw new org.apache.axis2.databinding.ADBException("medQueueDuration cannot be null!!");

        }
        else
        {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                    .convertToString(this.localMedQueueDuration));
        }

        xmlWriter.writeEndElement();

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = SessionReportType.generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "minQueueDuration", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            }
            else
            {
                xmlWriter.writeStartElement(namespace, "minQueueDuration");
            }

        }
        else
        {
            xmlWriter.writeStartElement("minQueueDuration");
        }

        if (java.lang.Float.isNaN(this.localMinQueueDuration))
        {

            throw new org.apache.axis2.databinding.ADBException("minQueueDuration cannot be null!!");

        }
        else
        {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                    .convertToString(this.localMinQueueDuration));
        }

        xmlWriter.writeEndElement();

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = SessionReportType.generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "totalQueueDuration", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            }
            else
            {
                xmlWriter.writeStartElement(namespace, "totalQueueDuration");
            }

        }
        else
        {
            xmlWriter.writeStartElement("totalQueueDuration");
        }

        if (java.lang.Float.isNaN(this.localTotalQueueDuration))
        {

            throw new org.apache.axis2.databinding.ADBException("totalQueueDuration cannot be null!!");

        }
        else
        {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                    .convertToString(this.localTotalQueueDuration));
        }

        xmlWriter.writeEndElement();

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = SessionReportType.generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "sessionCount", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            }
            else
            {
                xmlWriter.writeStartElement(namespace, "sessionCount");
            }

        }
        else
        {
            xmlWriter.writeStartElement("sessionCount");
        }

        if (this.localSessionCount == java.lang.Integer.MIN_VALUE)
        {

            throw new org.apache.axis2.databinding.ADBException("sessionCount cannot be null!!");

        }
        else
        {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                    .convertToString(this.localSessionCount));
        }

        xmlWriter.writeEndElement();
        if (this.localUserCountTracker)
        {
            namespace = "";
            if (!namespace.equals(""))
            {
                prefix = xmlWriter.getPrefix(namespace);

                if (prefix == null)
                {
                    prefix = SessionReportType.generatePrefix(namespace);

                    xmlWriter.writeStartElement(prefix, "userCount", namespace);
                    xmlWriter.writeNamespace(prefix, namespace);
                    xmlWriter.setPrefix(prefix, namespace);

                }
                else
                {
                    xmlWriter.writeStartElement(namespace, "userCount");
                }

            }
            else
            {
                xmlWriter.writeStartElement("userCount");
            }

            if (this.localUserCount == java.lang.Integer.MIN_VALUE)
            {

                throw new org.apache.axis2.databinding.ADBException("userCount cannot be null!!");

            }
            else
            {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                        .convertToString(this.localUserCount));
            }

            xmlWriter.writeEndElement();
        }
        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = SessionReportType.generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "aveSessionDuration", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            }
            else
            {
                xmlWriter.writeStartElement(namespace, "aveSessionDuration");
            }

        }
        else
        {
            xmlWriter.writeStartElement("aveSessionDuration");
        }

        if (java.lang.Float.isNaN(this.localAveSessionDuration))
        {

            throw new org.apache.axis2.databinding.ADBException("aveSessionDuration cannot be null!!");

        }
        else
        {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                    .convertToString(this.localAveSessionDuration));
        }

        xmlWriter.writeEndElement();

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = SessionReportType.generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "medSessionDuration", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            }
            else
            {
                xmlWriter.writeStartElement(namespace, "medSessionDuration");
            }

        }
        else
        {
            xmlWriter.writeStartElement("medSessionDuration");
        }

        if (java.lang.Float.isNaN(this.localMedSessionDuration))
        {

            throw new org.apache.axis2.databinding.ADBException("medSessionDuration cannot be null!!");

        }
        else
        {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                    .convertToString(this.localMedSessionDuration));
        }

        xmlWriter.writeEndElement();

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = SessionReportType.generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "maxSessionDuration", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            }
            else
            {
                xmlWriter.writeStartElement(namespace, "maxSessionDuration");
            }

        }
        else
        {
            xmlWriter.writeStartElement("maxSessionDuration");
        }

        if (java.lang.Float.isNaN(this.localMaxSessionDuration))
        {

            throw new org.apache.axis2.databinding.ADBException("maxSessionDuration cannot be null!!");

        }
        else
        {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                    .convertToString(this.localMaxSessionDuration));
        }

        xmlWriter.writeEndElement();

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = SessionReportType.generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "minSessionDuration", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            }
            else
            {
                xmlWriter.writeStartElement(namespace, "minSessionDuration");
            }

        }
        else
        {
            xmlWriter.writeStartElement("minSessionDuration");
        }

        if (java.lang.Float.isNaN(this.localMinSessionDuration))
        {

            throw new org.apache.axis2.databinding.ADBException("minSessionDuration cannot be null!!");

        }
        else
        {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                    .convertToString(this.localMinSessionDuration));
        }

        xmlWriter.writeEndElement();

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = SessionReportType.generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "totalSessionDuration", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            }
            else
            {
                xmlWriter.writeStartElement(namespace, "totalSessionDuration");
            }

        }
        else
        {
            xmlWriter.writeStartElement("totalSessionDuration");
        }

        if (java.lang.Float.isNaN(this.localTotalSessionDuration))
        {

            throw new org.apache.axis2.databinding.ADBException("totalSessionDuration cannot be null!!");

        }
        else
        {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                    .convertToString(this.localTotalSessionDuration));
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
                prefix = SessionReportType.generatePrefix(namespaceURI);
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
                        prefix = SessionReportType.generatePrefix(namespaceURI);
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
            prefix = SessionReportType.generatePrefix(namespace);

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
            elementList.add(new javax.xml.namespace.QName("", "user"));

            if (this.localUser == null)
            {
                throw new org.apache.axis2.databinding.ADBException("user cannot be null!!");
            }
            elementList.add(this.localUser);
        }
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
        elementList.add(new javax.xml.namespace.QName("", "aveQueueDuration"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(this.localAveQueueDuration));

        elementList.add(new javax.xml.namespace.QName("", "medQueueDuration"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(this.localMedQueueDuration));

        elementList.add(new javax.xml.namespace.QName("", "minQueueDuration"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(this.localMinQueueDuration));

        elementList.add(new javax.xml.namespace.QName("", "totalQueueDuration"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(this.localTotalQueueDuration));

        elementList.add(new javax.xml.namespace.QName("", "sessionCount"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(this.localSessionCount));
        if (this.localUserCountTracker)
        {
            elementList.add(new javax.xml.namespace.QName("", "userCount"));

            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(this.localUserCount));
        }
        elementList.add(new javax.xml.namespace.QName("", "aveSessionDuration"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(this.localAveSessionDuration));

        elementList.add(new javax.xml.namespace.QName("", "medSessionDuration"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(this.localMedSessionDuration));

        elementList.add(new javax.xml.namespace.QName("", "maxSessionDuration"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(this.localMaxSessionDuration));

        elementList.add(new javax.xml.namespace.QName("", "minSessionDuration"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(this.localMinSessionDuration));

        elementList.add(new javax.xml.namespace.QName("", "totalSessionDuration"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil
                .convertToString(this.localTotalSessionDuration));

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
        public static SessionReportType parse(final javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception
        {
            final SessionReportType object = new SessionReportType();

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

                        if (!"SessionReportType".equals(type))
                        {
                            //find namespace for the prefix
                            final java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (SessionReportType) au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.ExtensionMapper
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
                        && new javax.xml.namespace.QName("", "aveQueueDuration").equals(reader.getName()))
                {

                    final java.lang.String content = reader.getElementText();

                    object
                            .setAveQueueDuration(org.apache.axis2.databinding.utils.ConverterUtil
                                    .convertToFloat(content));

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
                        && new javax.xml.namespace.QName("", "medQueueDuration").equals(reader.getName()))
                {

                    final java.lang.String content = reader.getElementText();

                    object
                            .setMedQueueDuration(org.apache.axis2.databinding.utils.ConverterUtil
                                    .convertToFloat(content));

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
                        && new javax.xml.namespace.QName("", "minQueueDuration").equals(reader.getName()))
                {

                    final java.lang.String content = reader.getElementText();

                    object
                            .setMinQueueDuration(org.apache.axis2.databinding.utils.ConverterUtil
                                    .convertToFloat(content));

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
                        && new javax.xml.namespace.QName("", "totalQueueDuration").equals(reader.getName()))
                {

                    final java.lang.String content = reader.getElementText();

                    object.setTotalQueueDuration(org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToFloat(content));

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
                        && new javax.xml.namespace.QName("", "sessionCount").equals(reader.getName()))
                {

                    final java.lang.String content = reader.getElementText();

                    object.setSessionCount(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));

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

                if (reader.isStartElement() && new javax.xml.namespace.QName("", "userCount").equals(reader.getName()))
                {

                    final java.lang.String content = reader.getElementText();

                    object.setUserCount(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(content));

                    reader.next();

                } // End of if for expected property start element

                else
                {

                    object.setUserCount(java.lang.Integer.MIN_VALUE);

                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("", "aveSessionDuration").equals(reader.getName()))
                {

                    final java.lang.String content = reader.getElementText();

                    object.setAveSessionDuration(org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToFloat(content));

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
                        && new javax.xml.namespace.QName("", "medSessionDuration").equals(reader.getName()))
                {

                    final java.lang.String content = reader.getElementText();

                    object.setMedSessionDuration(org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToFloat(content));

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
                        && new javax.xml.namespace.QName("", "maxSessionDuration").equals(reader.getName()))
                {

                    final java.lang.String content = reader.getElementText();

                    object.setMaxSessionDuration(org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToFloat(content));

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
                        && new javax.xml.namespace.QName("", "minSessionDuration").equals(reader.getName()))
                {

                    final java.lang.String content = reader.getElementText();

                    object.setMinSessionDuration(org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToFloat(content));

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
                        && new javax.xml.namespace.QName("", "totalSessionDuration").equals(reader.getName()))
                {

                    final java.lang.String content = reader.getElementText();

                    object.setTotalSessionDuration(org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToFloat(content));

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
