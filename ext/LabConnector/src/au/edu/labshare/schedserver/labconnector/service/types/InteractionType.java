/**
 * SAHARA Scheduling Server - LabConnector
 * Class type for the web service.
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
 * InteractionType.java
 * 
 * This file was auto-generated from WSDL by the Apache Axis2 version: 1.4 Built
 * on : Apr 26, 2008 (06:25:17 EDT)
 */

package au.edu.labshare.schedserver.labconnector.service.types;

/**
 * InteractionType bean class
 */
@SuppressWarnings({"unchecked", "unused", "serial"})
public class InteractionType implements org.apache.axis2.databinding.ADBBean
{

    public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName(
                                                                   "http://labshare.edu.au:8080/LabConnector/",
                                                                   "InteractionType",
                                                                   "ns1");

    private static java.lang.String generatePrefix(java.lang.String namespace)
    {
        if (namespace.equals("http://labshare.edu.au:8080/LabConnector/"))
        {
            return "ns1";
        }
        return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
    }

    /**
     * field for InteractionType
     */

    protected java.lang.String       localInteractionType;

    private static java.util.HashMap _table_ = new java.util.HashMap();

    // Constructor

    protected InteractionType(java.lang.String value, boolean isRegisterValue)
    {
        localInteractionType = value;
        if (isRegisterValue)
        {

            _table_.put(localInteractionType, this);

        }

    }

    public static final java.lang.String _MASTER        = org.apache.axis2.databinding.utils.ConverterUtil
                                                                .convertToString("MASTER");

    public static final java.lang.String _SLAVE_ACTIVE  = org.apache.axis2.databinding.utils.ConverterUtil
                                                                .convertToString("SLAVE_ACTIVE");

    public static final java.lang.String _SLAVE_PASSIVE = org.apache.axis2.databinding.utils.ConverterUtil
                                                                .convertToString("SLAVE_PASSIVE");

    public static final InteractionType  MASTER         = new InteractionType(
                                                                _MASTER, true);

    public static final InteractionType  SLAVE_ACTIVE   = new InteractionType(
                                                                _SLAVE_ACTIVE,
                                                                true);

    public static final InteractionType  SLAVE_PASSIVE  = new InteractionType(
                                                                _SLAVE_PASSIVE,
                                                                true);

    public java.lang.String getValue()
    {
        return localInteractionType;
    }

    public boolean equals(java.lang.Object obj)
    {
        return (obj == this);
    }

    public int hashCode()
    {
        return toString().hashCode();
    }

    public java.lang.String toString()
    {

        return localInteractionType.toString();

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
                this, MY_QNAME)
        {

            public void serialize(
                    org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter)
                    throws javax.xml.stream.XMLStreamException
            {
                InteractionType.this.serialize(MY_QNAME, factory, xmlWriter);
            }
        };
        return new org.apache.axiom.om.impl.llom.OMSourcedElementImpl(MY_QNAME,
                factory, dataSource);

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

        // We can safely assume an element has only one type associated with it

        java.lang.String namespace = parentQName.getNamespaceURI();
        java.lang.String localName = parentQName.getLocalPart();

        if (!namespace.equals(""))
        {
            java.lang.String prefix = xmlWriter.getPrefix(namespace);

            if (prefix == null)
            {
                prefix = generatePrefix(namespace);

                xmlWriter.writeStartElement(prefix, localName, namespace);
                xmlWriter.writeNamespace(prefix, namespace);
                xmlWriter.setPrefix(prefix, namespace);

            }
            else
            {
                xmlWriter.writeStartElement(namespace, localName);
            }

        }
        else
        {
            xmlWriter.writeStartElement(localName);
        }

        // add the type details if this is used in a simple type
        if (serializeType)
        {
            java.lang.String namespacePrefix = registerPrefix(xmlWriter,
                    "http://labshare.edu.au:8080/LabConnector/");
            if ((namespacePrefix != null)
                    && (namespacePrefix.trim().length() > 0))
            {
                writeAttribute("xsi",
                        "http://www.w3.org/2001/XMLSchema-instance", "type",
                        namespacePrefix + ":InteractionType", xmlWriter);
            }
            else
            {
                writeAttribute("xsi",
                        "http://www.w3.org/2001/XMLSchema-instance", "type",
                        "InteractionType", xmlWriter);
            }
        }

        if (localInteractionType == null)
        {

            throw new org.apache.axis2.databinding.ADBException(
                    "Value cannot be null !!");

        }
        else
        {

            xmlWriter.writeCharacters(localInteractionType);

        }

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

        // We can safely assume an element has only one type associated with it
        return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(
                MY_QNAME,
                new java.lang.Object[] {
                        org.apache.axis2.databinding.utils.reader.ADBXMLStreamReader.ELEMENT_TEXT,
                        org.apache.axis2.databinding.utils.ConverterUtil
                                .convertToString(localInteractionType) }, null);

    }

    /**
     * Factory class that keeps the parse method
     */
    public static class Factory
    {

        public static InteractionType fromValue(java.lang.String value)
                throws java.lang.IllegalArgumentException
        {
            InteractionType enumeration = (InteractionType)

            _table_.get(value);

            if (enumeration == null)
                throw new java.lang.IllegalArgumentException();
            return enumeration;
        }

        public static InteractionType fromString(java.lang.String value,
                java.lang.String namespaceURI)
                throws java.lang.IllegalArgumentException
        {
            try
            {

                return fromValue(org.apache.axis2.databinding.utils.ConverterUtil
                        .convertToString(value));

            }
            catch (java.lang.Exception e)
            {
                throw new java.lang.IllegalArgumentException();
            }
        }

        public static InteractionType fromString(
                javax.xml.stream.XMLStreamReader xmlStreamReader,
                java.lang.String content)
        {
            if (content.indexOf(":") > -1)
            {
                java.lang.String prefix = content.substring(0, content
                        .indexOf(":"));
                java.lang.String namespaceUri = xmlStreamReader
                        .getNamespaceContext().getNamespaceURI(prefix);
                return InteractionType.Factory
                        .fromString(content, namespaceUri);
            }
            else
            {
                return InteractionType.Factory.fromString(content, "");
            }
        }

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
        public static InteractionType parse(
                javax.xml.stream.XMLStreamReader reader)
                throws java.lang.Exception
        {
            InteractionType object = null;
            // initialize a hash map to keep values
            java.util.Map attributeMap = new java.util.HashMap();
            java.util.List extraAttributeList = new java.util.ArrayList();

            int event;
            java.lang.String nillableValue = null;
            java.lang.String prefix = "";
            java.lang.String namespaceuri = "";
            try
            {

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                // Note all attributes that were handled. Used to differ normal
                // attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();

                while (!reader.isEndElement())
                {
                    if (reader.isStartElement() || reader.hasText())
                    {

                        java.lang.String content = reader.getElementText();

                        if (content.indexOf(":") > 0)
                        {
                            // this seems to be a Qname so find the namespace
                            // and send
                            prefix = content.substring(0, content.indexOf(":"));
                            namespaceuri = reader.getNamespaceURI(prefix);
                            object = InteractionType.Factory.fromString(
                                    content, namespaceuri);
                        }
                        else
                        {
                            // this seems to be not a qname send and empty
                            // namespace incase of it is
                            // check is done in fromString method
                            object = InteractionType.Factory.fromString(
                                    content, "");
                        }

                    }
                    else
                    {
                        reader.next();
                    }
                } // end of while loop

            }
            catch (javax.xml.stream.XMLStreamException e)
            {
                throw new java.lang.Exception(e);
            }

            return object;
        }

    }// end of factory class

}