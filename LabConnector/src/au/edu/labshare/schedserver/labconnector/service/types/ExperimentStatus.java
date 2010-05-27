/**
 * SAHARA Scheduling Server - LabConnector ExperimentStatus
 * ExperimentStatus class for the web service.
 * 
 * @license See LICENSE in the top level directory for complete license terms.
 * 
 * Copyright (c) 2010, University of Technology, Sydney
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *  * Neither the name of the University of Technology, Sydney nor the names
 *    of its contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *    
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * @author Herbert Yeung
 * @date 27th May 2010
 */
/**
 * ExperimentStatus.java
 * 
 * This file was auto-generated from WSDL by the Apache Axis2 version: 1.4 Built
 * on : Apr 26, 2008 (06:25:17 EDT)
 */

package au.edu.labshare.schedserver.labconnector.service.types;

/**
 * ExperimentStatus bean class
 */

public class ExperimentStatus implements org.apache.axis2.databinding.ADBBean
{
    /*
     * This type was generated from the piece of schema that had name =
     * ExperimentStatus Namespace URI =
     * http://labshare.edu.au:8080/LabConnector/ Namespace Prefix = ns1
     */

    private static java.lang.String generatePrefix(java.lang.String namespace)
    {
        if (namespace.equals("http://labshare.edu.au:8080/LabConnector/"))
        {
            return "ns1";
        }
        return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
    }

    /**
     * field for QueueLength
     */

    protected int localQueueLength;

    /**
     * Auto generated getter method
     * 
     * @return int
     */
    public int getQueueLength()
    {
        return localQueueLength;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            QueueLength
     */
    public void setQueueLength(int param)
    {

        this.localQueueLength = param;

    }

    /**
     * field for WaitTime
     */

    protected org.apache.axis2.databinding.types.Time localWaitTime;

    /**
     * Auto generated getter method
     * 
     * @return org.apache.axis2.databinding.types.Time
     */
    public org.apache.axis2.databinding.types.Time getWaitTime()
    {
        return localWaitTime;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            WaitTime
     */
    public void setWaitTime(org.apache.axis2.databinding.types.Time param)
    {

        this.localWaitTime = param;

    }

    /**
     * field for RunTime
     */

    protected org.apache.axis2.databinding.types.Time localRunTime;

    /**
     * Auto generated getter method
     * 
     * @return org.apache.axis2.databinding.types.Time
     */
    public org.apache.axis2.databinding.types.Time getRunTime()
    {
        return localRunTime;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            RunTime
     */
    public void setRunTime(org.apache.axis2.databinding.types.Time param)
    {

        this.localRunTime = param;

    }

    /**
     * field for RemainingRunTime
     */

    protected org.apache.axis2.databinding.types.Time localRemainingRunTime;

    /**
     * Auto generated getter method
     * 
     * @return org.apache.axis2.databinding.types.Time
     */
    public org.apache.axis2.databinding.types.Time getRemainingRunTime()
    {
        return localRemainingRunTime;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            RemainingRunTime
     */
    public void setRemainingRunTime(
            org.apache.axis2.databinding.types.Time param)
    {

        this.localRemainingRunTime = param;

    }

    /**
     * field for Start
     */

    protected java.util.Calendar localStart;

    /**
     * Auto generated getter method
     * 
     * @return java.util.Calendar
     */
    public java.util.Calendar getStart()
    {
        return localStart;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            Start
     */
    public void setStart(java.util.Calendar param)
    {

        this.localStart = param;

    }

    /**
     * field for QueueExpiry
     */

    protected java.util.Calendar localQueueExpiry;

    /**
     * Auto generated getter method
     * 
     * @return java.util.Calendar
     */
    public java.util.Calendar getQueueExpiry()
    {
        return localQueueExpiry;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            QueueExpiry
     */
    public void setQueueExpiry(java.util.Calendar param)
    {

        this.localQueueExpiry = param;

    }

    /**
     * field for RunTimeExpiry
     */

    protected java.util.Calendar localRunTimeExpiry;

    /**
     * Auto generated getter method
     * 
     * @return java.util.Calendar
     */
    public java.util.Calendar getRunTimeExpiry()
    {
        return localRunTimeExpiry;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            RunTimeExpiry
     */
    public void setRunTimeExpiry(java.util.Calendar param)
    {

        this.localRunTimeExpiry = param;

    }

    /**
     * field for AllowedExtension
     */

    protected boolean localAllowedExtension;

    /**
     * Auto generated getter method
     * 
     * @return boolean
     */
    public boolean getAllowedExtension()
    {
        return localAllowedExtension;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            AllowedExtension
     */
    public void setAllowedExtension(boolean param)
    {

        this.localAllowedExtension = param;

    }

    /**
     * field for ExtensionTime
     */

    protected org.apache.axis2.databinding.types.Time localExtensionTime;

    /**
     * Auto generated getter method
     * 
     * @return org.apache.axis2.databinding.types.Time
     */
    public org.apache.axis2.databinding.types.Time getExtensionTime()
    {
        return localExtensionTime;
    }

    /**
     * Auto generated setter method
     * 
     * @param param
     *            ExtensionTime
     */
    public void setExtensionTime(org.apache.axis2.databinding.types.Time param)
    {

        this.localExtensionTime = param;

    }

    /**
     * isReaderMTOMAware
     * 
     * @return true if the reader supports MTOM
     */
    public static boolean isReaderMTOMAware(
            javax.xml.stream.XMLStreamReader reader)
    {
        boolean isReaderMTOMAware = false;

        try
        {
            isReaderMTOMAware = java.lang.Boolean.TRUE
                    .equals(reader
                            .getProperty(org.apache.axiom.om.OMConstants.IS_DATA_HANDLERS_AWARE));
        }
        catch (java.lang.IllegalArgumentException e)
        {
            isReaderMTOMAware = false;
        }
        return isReaderMTOMAware;
    }

    /**
     * 
     * @param parentQName
     * @param factory
     * @return org.apache.axiom.om.OMElement
     */
    public org.apache.axiom.om.OMElement getOMElement(
            final javax.xml.namespace.QName parentQName,
            final org.apache.axiom.om.OMFactory factory)
            throws org.apache.axis2.databinding.ADBException
    {

        org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(
                this, parentQName)
        {

            public void serialize(
                    org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter)
                    throws javax.xml.stream.XMLStreamException
            {
                ExperimentStatus.this
                        .serialize(parentQName, factory, xmlWriter);
            }
        };
        return new org.apache.axiom.om.impl.llom.OMSourcedElementImpl(
                parentQName, factory, dataSource);

    }

    public void serialize(
            final javax.xml.namespace.QName parentQName,
            final org.apache.axiom.om.OMFactory factory,
            org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter)
            throws javax.xml.stream.XMLStreamException,
            org.apache.axis2.databinding.ADBException
    {
        serialize(parentQName, factory, xmlWriter, false);
    }

    public void serialize(
            final javax.xml.namespace.QName parentQName,
            final org.apache.axiom.om.OMFactory factory,
            org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter,
            boolean serializeType) throws javax.xml.stream.XMLStreamException,
            org.apache.axis2.databinding.ADBException
    {

        java.lang.String prefix = null;
        java.lang.String namespace = null;

        prefix = parentQName.getPrefix();
        namespace = parentQName.getNamespaceURI();

        if ((namespace != null) && (namespace.trim().length() > 0))
        {
            java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
            if (writerPrefix != null)
            {
                xmlWriter.writeStartElement(namespace, parentQName
                        .getLocalPart());
            }
            else
            {
                if (prefix == null)
                {
                    prefix = generatePrefix(namespace);
                }

                xmlWriter.writeStartElement(prefix, parentQName.getLocalPart(),
                        namespace);
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

            java.lang.String namespacePrefix = registerPrefix(xmlWriter,
                    "http://labshare.edu.au:8080/LabConnector/");
            if ((namespacePrefix != null)
                    && (namespacePrefix.trim().length() > 0))
            {
                writeAttribute("xsi",
                        "http://www.w3.org/2001/XMLSchema-instance", "type",
                        namespacePrefix + ":ExperimentStatus", xmlWriter);
            }
            else
            {
                writeAttribute("xsi",
                        "http://www.w3.org/2001/XMLSchema-instance", "type",
                        "ExperimentStatus", xmlWriter);
            }

        }

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "queueLength", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            }
            else
            {
                xmlWriter.writeStartElement(namespace, "queueLength");
            }

        }
        else
        {
            xmlWriter.writeStartElement("queueLength");
        }

        if (localQueueLength == java.lang.Integer.MIN_VALUE)
        {

            throw new org.apache.axis2.databinding.ADBException(
                    "queueLength cannot be null!!");

        }
        else
        {
            xmlWriter
                    .writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToString(localQueueLength));
        }

        xmlWriter.writeEndElement();

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "waitTime", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            }
            else
            {
                xmlWriter.writeStartElement(namespace, "waitTime");
            }

        }
        else
        {
            xmlWriter.writeStartElement("waitTime");
        }

        if (localWaitTime == null)
        {
            // write the nil attribute

            throw new org.apache.axis2.databinding.ADBException(
                    "waitTime cannot be null!!");

        }
        else
        {

            xmlWriter
                    .writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToString(localWaitTime));

        }

        xmlWriter.writeEndElement();

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "runTime", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            }
            else
            {
                xmlWriter.writeStartElement(namespace, "runTime");
            }

        }
        else
        {
            xmlWriter.writeStartElement("runTime");
        }

        if (localRunTime == null)
        {
            // write the nil attribute

            throw new org.apache.axis2.databinding.ADBException(
                    "runTime cannot be null!!");

        }
        else
        {

            xmlWriter
                    .writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToString(localRunTime));

        }

        xmlWriter.writeEndElement();

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "remainingRunTime",
                        namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            }
            else
            {
                xmlWriter.writeStartElement(namespace, "remainingRunTime");
            }

        }
        else
        {
            xmlWriter.writeStartElement("remainingRunTime");
        }

        if (localRemainingRunTime == null)
        {
            // write the nil attribute

            throw new org.apache.axis2.databinding.ADBException(
                    "remainingRunTime cannot be null!!");

        }
        else
        {

            xmlWriter
                    .writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToString(localRemainingRunTime));

        }

        xmlWriter.writeEndElement();

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = generatePrefix(namespace);

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

        if (localStart == null)
        {
            // write the nil attribute

            throw new org.apache.axis2.databinding.ADBException(
                    "start cannot be null!!");

        }
        else
        {

            xmlWriter
                    .writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToString(localStart));

        }

        xmlWriter.writeEndElement();

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "queueExpiry", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            }
            else
            {
                xmlWriter.writeStartElement(namespace, "queueExpiry");
            }

        }
        else
        {
            xmlWriter.writeStartElement("queueExpiry");
        }

        if (localQueueExpiry == null)
        {
            // write the nil attribute

            throw new org.apache.axis2.databinding.ADBException(
                    "queueExpiry cannot be null!!");

        }
        else
        {

            xmlWriter
                    .writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToString(localQueueExpiry));

        }

        xmlWriter.writeEndElement();

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "runTimeExpiry", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            }
            else
            {
                xmlWriter.writeStartElement(namespace, "runTimeExpiry");
            }

        }
        else
        {
            xmlWriter.writeStartElement("runTimeExpiry");
        }

        if (localRunTimeExpiry == null)
        {
            // write the nil attribute

            throw new org.apache.axis2.databinding.ADBException(
                    "runTimeExpiry cannot be null!!");

        }
        else
        {

            xmlWriter
                    .writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToString(localRunTimeExpiry));

        }

        xmlWriter.writeEndElement();

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "allowedExtension",
                        namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            }
            else
            {
                xmlWriter.writeStartElement(namespace, "allowedExtension");
            }

        }
        else
        {
            xmlWriter.writeStartElement("allowedExtension");
        }

        if (false)
        {

            throw new org.apache.axis2.databinding.ADBException(
                    "allowedExtension cannot be null!!");

        }
        else
        {
            xmlWriter
                    .writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToString(localAllowedExtension));
        }

        xmlWriter.writeEndElement();

        namespace = "";
        if (!namespace.equals(""))
        {
            prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, "extensionTime", namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            }
            else
            {
                xmlWriter.writeStartElement(namespace, "extensionTime");
            }

        }
        else
        {
            xmlWriter.writeStartElement("extensionTime");
        }

        if (localExtensionTime == null)
        {
            // write the nil attribute

            throw new org.apache.axis2.databinding.ADBException(
                    "extensionTime cannot be null!!");

        }
        else
        {

            xmlWriter
                    .writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToString(localExtensionTime));

        }

        xmlWriter.writeEndElement();

        xmlWriter.writeEndElement();

    }

    /**
     * Util method to write an attribute with the ns prefix
     */
    private void writeAttribute(java.lang.String prefix,
            java.lang.String namespace, java.lang.String attName,
            java.lang.String attValue,
            javax.xml.stream.XMLStreamWriter xmlWriter)
            throws javax.xml.stream.XMLStreamException
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
    private void writeAttribute(java.lang.String namespace,
            java.lang.String attName, java.lang.String attValue,
            javax.xml.stream.XMLStreamWriter xmlWriter)
            throws javax.xml.stream.XMLStreamException
    {
        if (namespace.equals(""))
        {
            xmlWriter.writeAttribute(attName, attValue);
        }
        else
        {
            registerPrefix(xmlWriter, namespace);
            xmlWriter.writeAttribute(namespace, attName, attValue);
        }
    }

    /**
     * Util method to write an attribute without the ns prefix
     */
    private void writeQNameAttribute(java.lang.String namespace,
            java.lang.String attName, javax.xml.namespace.QName qname,
            javax.xml.stream.XMLStreamWriter xmlWriter)
            throws javax.xml.stream.XMLStreamException
    {

        java.lang.String attributeNamespace = qname.getNamespaceURI();
        java.lang.String attributePrefix = xmlWriter
                .getPrefix(attributeNamespace);
        if (attributePrefix == null)
        {
            attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
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
            registerPrefix(xmlWriter, namespace);
            xmlWriter.writeAttribute(namespace, attName, attributeValue);
        }
    }

    /**
     * method to handle Qnames
     */

    private void writeQName(javax.xml.namespace.QName qname,
            javax.xml.stream.XMLStreamWriter xmlWriter)
            throws javax.xml.stream.XMLStreamException
    {
        java.lang.String namespaceURI = qname.getNamespaceURI();
        if (namespaceURI != null)
        {
            java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);
            if (prefix == null)
            {
                prefix = generatePrefix(namespaceURI);
                xmlWriter.writeNamespace(prefix, namespaceURI);
                xmlWriter.setPrefix(prefix, namespaceURI);
            }

            if (prefix.trim().length() > 0)
            {
                xmlWriter.writeCharacters(prefix
                        + ":"
                        + org.apache.axis2.databinding.utils.ConverterUtil
                                .convertToString(qname));
            }
            else
            {
                // i.e this is the default namespace
                xmlWriter
                        .writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                                .convertToString(qname));
            }

        }
        else
        {
            xmlWriter
                    .writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil
                            .convertToString(qname));
        }
    }

    private void writeQNames(javax.xml.namespace.QName[] qnames,
            javax.xml.stream.XMLStreamWriter xmlWriter)
            throws javax.xml.stream.XMLStreamException
    {

        if (qnames != null)
        {
            // we have to store this data until last moment since it is not
            // possible to write any
            // namespace data after writing the charactor data
            java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
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
                        prefix = generatePrefix(namespaceURI);
                        xmlWriter.writeNamespace(prefix, namespaceURI);
                        xmlWriter.setPrefix(prefix, namespaceURI);
                    }

                    if (prefix.trim().length() > 0)
                    {
                        stringToWrite
                                .append(prefix)
                                .append(":")
                                .append(
                                        org.apache.axis2.databinding.utils.ConverterUtil
                                                .convertToString(qnames[i]));
                    }
                    else
                    {
                        stringToWrite
                                .append(org.apache.axis2.databinding.utils.ConverterUtil
                                        .convertToString(qnames[i]));
                    }
                }
                else
                {
                    stringToWrite
                            .append(org.apache.axis2.databinding.utils.ConverterUtil
                                    .convertToString(qnames[i]));
                }
            }
            xmlWriter.writeCharacters(stringToWrite.toString());
        }

    }

    /**
     * Register a namespace prefix
     */
    private java.lang.String registerPrefix(
            javax.xml.stream.XMLStreamWriter xmlWriter,
            java.lang.String namespace)
            throws javax.xml.stream.XMLStreamException
    {
        java.lang.String prefix = xmlWriter.getPrefix(namespace);

        if (prefix == null)
        {
            prefix = generatePrefix(namespace);

            while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null)
            {
                prefix = org.apache.axis2.databinding.utils.BeanUtil
                        .getUniquePrefix();
            }

            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
        }

        return prefix;
    }

    /**
     * databinding method to get an XML representation of this object
     * 
     */
    public javax.xml.stream.XMLStreamReader getPullParser(
            javax.xml.namespace.QName qName)
            throws org.apache.axis2.databinding.ADBException
    {

        java.util.ArrayList elementList = new java.util.ArrayList();
        java.util.ArrayList attribList = new java.util.ArrayList();

        elementList.add(new javax.xml.namespace.QName("", "queueLength"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil
                .convertToString(localQueueLength));

        elementList.add(new javax.xml.namespace.QName("", "waitTime"));

        if (localWaitTime != null)
        {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil
                    .convertToString(localWaitTime));
        }
        else
        {
            throw new org.apache.axis2.databinding.ADBException(
                    "waitTime cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "runTime"));

        if (localRunTime != null)
        {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil
                    .convertToString(localRunTime));
        }
        else
        {
            throw new org.apache.axis2.databinding.ADBException(
                    "runTime cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "remainingRunTime"));

        if (localRemainingRunTime != null)
        {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil
                    .convertToString(localRemainingRunTime));
        }
        else
        {
            throw new org.apache.axis2.databinding.ADBException(
                    "remainingRunTime cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "start"));

        if (localStart != null)
        {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil
                    .convertToString(localStart));
        }
        else
        {
            throw new org.apache.axis2.databinding.ADBException(
                    "start cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "queueExpiry"));

        if (localQueueExpiry != null)
        {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil
                    .convertToString(localQueueExpiry));
        }
        else
        {
            throw new org.apache.axis2.databinding.ADBException(
                    "queueExpiry cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "runTimeExpiry"));

        if (localRunTimeExpiry != null)
        {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil
                    .convertToString(localRunTimeExpiry));
        }
        else
        {
            throw new org.apache.axis2.databinding.ADBException(
                    "runTimeExpiry cannot be null!!");
        }

        elementList.add(new javax.xml.namespace.QName("", "allowedExtension"));

        elementList.add(org.apache.axis2.databinding.utils.ConverterUtil
                .convertToString(localAllowedExtension));

        elementList.add(new javax.xml.namespace.QName("", "extensionTime"));

        if (localExtensionTime != null)
        {
            elementList.add(org.apache.axis2.databinding.utils.ConverterUtil
                    .convertToString(localExtensionTime));
        }
        else
        {
            throw new org.apache.axis2.databinding.ADBException(
                    "extensionTime cannot be null!!");
        }

        return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(
                qName, elementList.toArray(), attribList.toArray());

    }

    /**
     * Factory class that keeps the parse method
     */
    public static class Factory
    {

        /**
         * static method to create the object Precondition: If this object is an
         * element, the current or next start element starts this object and any
         * intervening reader events are ignorable If this object is not an
         * element, it is a complex type and the reader is at the event just
         * after the outer start element Postcondition: If this object is an
         * element, the reader is positioned at its end element If this object
         * is a complex type, the reader is positioned at the end element of its
         * outer element
         */
        public static ExperimentStatus parse(
                javax.xml.stream.XMLStreamReader reader)
                throws java.lang.Exception
        {
            ExperimentStatus object = new ExperimentStatus();

            int event;
            java.lang.String nillableValue = null;
            java.lang.String prefix = "";
            java.lang.String namespaceuri = "";
            try
            {

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.getAttributeValue(
                        "http://www.w3.org/2001/XMLSchema-instance", "type") != null)
                {
                    java.lang.String fullTypeName = reader
                            .getAttributeValue(
                                    "http://www.w3.org/2001/XMLSchema-instance",
                                    "type");
                    if (fullTypeName != null)
                    {
                        java.lang.String nsPrefix = null;
                        if (fullTypeName.indexOf(":") > -1)
                        {
                            nsPrefix = fullTypeName.substring(0, fullTypeName
                                    .indexOf(":"));
                        }
                        nsPrefix = nsPrefix == null ? "" : nsPrefix;

                        java.lang.String type = fullTypeName
                                .substring(fullTypeName.indexOf(":") + 1);

                        if (!"ExperimentStatus".equals(type))
                        {
                            // find namespace for the prefix
                            java.lang.String nsUri = reader
                                    .getNamespaceContext().getNamespaceURI(
                                            nsPrefix);
                            return (ExperimentStatus) au.edu.labshare.schedserver.labconnector.service.types.ExtensionMapper
                                    .getTypeObject(nsUri, type, reader);
                        }

                    }

                }

                // Note all attributes that were handled. Used to differ normal
                // attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();

                reader.next();

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("", "queueLength")
                                .equals(reader.getName()))
                {

                    java.lang.String content = reader.getElementText();

                    object
                            .setQueueLength(org.apache.axis2.databinding.utils.ConverterUtil
                                    .convertToInt(content));

                    reader.next();

                } // End of if for expected property start element

                else
                {
                    // A start element we are not expecting indicates an invalid
                    // parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                            "Unexpected subelement " + reader.getLocalName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("", "waitTime")
                                .equals(reader.getName()))
                {

                    java.lang.String content = reader.getElementText();

                    object
                            .setWaitTime(org.apache.axis2.databinding.utils.ConverterUtil
                                    .convertToTime(content));

                    reader.next();

                } // End of if for expected property start element

                else
                {
                    // A start element we are not expecting indicates an invalid
                    // parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                            "Unexpected subelement " + reader.getLocalName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("", "runTime")
                                .equals(reader.getName()))
                {

                    java.lang.String content = reader.getElementText();

                    object
                            .setRunTime(org.apache.axis2.databinding.utils.ConverterUtil
                                    .convertToTime(content));

                    reader.next();

                } // End of if for expected property start element

                else
                {
                    // A start element we are not expecting indicates an invalid
                    // parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                            "Unexpected subelement " + reader.getLocalName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("", "remainingRunTime")
                                .equals(reader.getName()))
                {

                    java.lang.String content = reader.getElementText();

                    object
                            .setRemainingRunTime(org.apache.axis2.databinding.utils.ConverterUtil
                                    .convertToTime(content));

                    reader.next();

                } // End of if for expected property start element

                else
                {
                    // A start element we are not expecting indicates an invalid
                    // parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                            "Unexpected subelement " + reader.getLocalName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("", "start")
                                .equals(reader.getName()))
                {

                    java.lang.String content = reader.getElementText();

                    object
                            .setStart(org.apache.axis2.databinding.utils.ConverterUtil
                                    .convertToDateTime(content));

                    reader.next();

                } // End of if for expected property start element

                else
                {
                    // A start element we are not expecting indicates an invalid
                    // parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                            "Unexpected subelement " + reader.getLocalName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("", "queueExpiry")
                                .equals(reader.getName()))
                {

                    java.lang.String content = reader.getElementText();

                    object
                            .setQueueExpiry(org.apache.axis2.databinding.utils.ConverterUtil
                                    .convertToDateTime(content));

                    reader.next();

                } // End of if for expected property start element

                else
                {
                    // A start element we are not expecting indicates an invalid
                    // parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                            "Unexpected subelement " + reader.getLocalName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("", "runTimeExpiry")
                                .equals(reader.getName()))
                {

                    java.lang.String content = reader.getElementText();

                    object
                            .setRunTimeExpiry(org.apache.axis2.databinding.utils.ConverterUtil
                                    .convertToDateTime(content));

                    reader.next();

                } // End of if for expected property start element

                else
                {
                    // A start element we are not expecting indicates an invalid
                    // parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                            "Unexpected subelement " + reader.getLocalName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("", "allowedExtension")
                                .equals(reader.getName()))
                {

                    java.lang.String content = reader.getElementText();

                    object
                            .setAllowedExtension(org.apache.axis2.databinding.utils.ConverterUtil
                                    .convertToBoolean(content));

                    reader.next();

                } // End of if for expected property start element

                else
                {
                    // A start element we are not expecting indicates an invalid
                    // parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                            "Unexpected subelement " + reader.getLocalName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()
                        && new javax.xml.namespace.QName("", "extensionTime")
                                .equals(reader.getName()))
                {

                    java.lang.String content = reader.getElementText();

                    object
                            .setExtensionTime(org.apache.axis2.databinding.utils.ConverterUtil
                                    .convertToTime(content));

                    reader.next();

                } // End of if for expected property start element

                else
                {
                    // A start element we are not expecting indicates an invalid
                    // parameter was passed
                    throw new org.apache.axis2.databinding.ADBException(
                            "Unexpected subelement " + reader.getLocalName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement())
                    // A start element we are not expecting indicates a trailing
                    // invalid property
                    throw new org.apache.axis2.databinding.ADBException(
                            "Unexpected subelement " + reader.getLocalName());

            }
            catch (javax.xml.stream.XMLStreamException e)
            {
                throw new java.lang.Exception(e);
            }

            return object;
        }

    }// end of factory class

}
