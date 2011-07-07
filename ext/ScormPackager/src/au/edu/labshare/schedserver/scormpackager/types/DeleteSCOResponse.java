/**
 * SAHARA Scheduling Server
 *
 * DeleteSCOResponse.java 
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
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4  Built on : Apr 26, 2008 (06:25:17 EDT)
 *
 * @author Herber Yeung
 * @date 4th November 2010
 */
package au.edu.labshare.schedserver.scormpackager.types;
            
/**
 *  DeleteSCOResponse bean class
 */
public class DeleteSCOResponse implements org.apache.axis2.databinding.ADBBean
{   
	private static final long serialVersionUID = 1L;
	public static final javax.xml.namespace.QName MY_QNAME = new javax.xml.namespace.QName("http://labshare.edu.au:8080/ScormPackager/", "deleteSCOResponse", "ns1");

	private static java.lang.String generatePrefix(java.lang.String namespace) 
	{
		if(namespace.equals("http://labshare.edu.au:8080/ScormPackager/"))
		{
			return "ns1";
		}

		return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
	}

	/**
	 * field for DeleteResponse
	 */
	protected boolean localDeleteResponse ;

	/**
	 * Auto generated getter method
	 * @return boolean
	 */
	public boolean getDeleteResponse()
	{
		return localDeleteResponse;
	}
                            
	/**
	 * Auto generated setter method
	 * @param param DeleteResponse
	 */
	public void setDeleteResponse(boolean param)
	{
		this.localDeleteResponse=param;
	}

	/**
     * isReaderMTOMAware
     * @return true if the reader supports MTOM
     */
	public static boolean isReaderMTOMAware(javax.xml.stream.XMLStreamReader reader) 
	{
		boolean isReaderMTOMAware = false;
        
        try
        {
        	isReaderMTOMAware = java.lang.Boolean.TRUE.equals(reader.getProperty(org.apache.axiom.om.OMConstants.IS_DATA_HANDLERS_AWARE));
        }
        catch(java.lang.IllegalArgumentException e)
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
	public org.apache.axiom.om.OMElement getOMElement(final javax.xml.namespace.QName parentQName, final org.apache.axiom.om.OMFactory factory) throws org.apache.axis2.databinding.ADBException
	{
		org.apache.axiom.om.OMDataSource dataSource = new org.apache.axis2.databinding.ADBDataSource(this,MY_QNAME)
		{
			public void serialize(org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException 
			{
				DeleteSCOResponse.this.serialize(MY_QNAME,factory,xmlWriter);
			}
		};

		return new org.apache.axiom.om.impl.llom.OMSourcedElementImpl(MY_QNAME,factory,dataSource);        
	}

	public void serialize(final javax.xml.namespace.QName parentQName, final org.apache.axiom.om.OMFactory factory,
						  org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException
    {
		serialize(parentQName,factory,xmlWriter,false);
    }

	public void serialize(final javax.xml.namespace.QName parentQName, final org.apache.axiom.om.OMFactory factory,
						  org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter xmlWriter, boolean serializeType)
            			  throws javax.xml.stream.XMLStreamException, org.apache.axis2.databinding.ADBException
    {
		java.lang.String prefix = null;
		java.lang.String namespace = null;

		prefix = parentQName.getPrefix();
		namespace = parentQName.getNamespaceURI();
                    
		if((namespace != null) && (namespace.trim().length() > 0)) 
		{
			java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);
			
			if(writerPrefix != null) 
			{
				xmlWriter.writeStartElement(namespace, parentQName.getLocalPart());
			} 
			else 
			{
				if(prefix == null) 
				{
					prefix = generatePrefix(namespace);
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
                
		if(serializeType)
		{
			java.lang.String namespacePrefix = registerPrefix(xmlWriter,"http://labshare.edu.au:8080/ScormPackager/");

			if((namespacePrefix != null) && (namespacePrefix.trim().length() > 0))
			{
				writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type", namespacePrefix+":deleteSCOResponse", xmlWriter);
			} 
			else 
			{
				writeAttribute("xsi","http://www.w3.org/2001/XMLSchema-instance","type", "deleteSCOResponse", xmlWriter);
			}
		}
        
		namespace = "";

		if(!namespace.equals("")) 
		{
			prefix = xmlWriter.getPrefix(namespace);

			if(prefix == null) 
			{
				prefix = generatePrefix(namespace);

				xmlWriter.writeStartElement(prefix,"deleteResponse", namespace);
				xmlWriter.writeNamespace(prefix, namespace);
				xmlWriter.setPrefix(prefix, namespace);
			} 
			else 
			{
				xmlWriter.writeStartElement(namespace,"deleteResponse");
			}

		} 
		else 
		{
			xmlWriter.writeStartElement("deleteResponse");
		}
        
		xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDeleteResponse));
		xmlWriter.writeEndElement();
		xmlWriter.writeEndElement();
    }

	/**
	 * Util method to write an attribute with the ns prefix
	 */
	private void writeAttribute(java.lang.String prefix,java.lang.String namespace,java.lang.String attName,
								java.lang.String attValue,javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException
    {
		if(xmlWriter.getPrefix(namespace) == null) 
		{
			xmlWriter.writeNamespace(prefix, namespace);
			xmlWriter.setPrefix(prefix, namespace);
		}

		xmlWriter.writeAttribute(namespace,attName,attValue);

    }

	/**
	 * Register a namespace prefix
	 */
	private java.lang.String registerPrefix(javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace) throws javax.xml.stream.XMLStreamException 
	{	                
		java.lang.String prefix = xmlWriter.getPrefix(namespace);

		if(prefix == null) 
		{                    
			prefix = generatePrefix(namespace);

			while(xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null) 
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
	 *
	 */
	@SuppressWarnings("unchecked")
	public javax.xml.stream.XMLStreamReader getPullParser(javax.xml.namespace.QName qName) throws org.apache.axis2.databinding.ADBException
	{
		@SuppressWarnings("rawtypes")
		java.util.ArrayList elementList = new java.util.ArrayList();
		@SuppressWarnings("rawtypes")
		java.util.ArrayList attribList = new java.util.ArrayList();
                
		elementList.add(new javax.xml.namespace.QName("", "deleteResponse"));
                                 
		elementList.add(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(localDeleteResponse));
                            
		return new org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl(qName, elementList.toArray(), attribList.toArray());
	}

	/**
	 *  Factory class that keeps the parse method
	 */
	public static class Factory
	{
		/**
		 * static method to create the object
		 * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
		 *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
		 * Postcondition: If this object is an element, the reader is positioned at its end element
		 *                If this object is a complex type, the reader is positioned at the end element of its outer element
		 */
		public static DeleteSCOResponse parse(javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception
		{
			DeleteSCOResponse object = new DeleteSCOResponse();

            try 
            {
                while(!reader.isStartElement() && !reader.isEndElement())
                	reader.next();

                if(reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance","type")!= null)
                {
                	java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type");
                  
                	if(fullTypeName != null)
                	{
                		java.lang.String nsPrefix = null;
                		
                		if(fullTypeName.indexOf(":") > -1)
                		{
                			nsPrefix = fullTypeName.substring(0,fullTypeName.indexOf(":"));
                		}

                		nsPrefix = nsPrefix == null ? "" : nsPrefix;

                		java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(":")+1);
                    
                		if(!"deleteSCOResponse".equals(type))
                		{
                			//find namespace for the prefix
                			java.lang.String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                			
                			return (DeleteSCOResponse)au.edu.labshare.schedserver.scormpackager.types.ExtensionMapper.getTypeObject(nsUri,type,reader);
                		}
                	}
                }
                
                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                reader.next();
                
                while(!reader.isStartElement() && !reader.isEndElement()) 
                	reader.next();
                                
                if(reader.isStartElement() && new javax.xml.namespace.QName("","deleteResponse").equals(reader.getName()))
                {
                	java.lang.String content = reader.getElementText();
                	object.setDeleteResponse(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(content));
                	reader.next();
                }// End of if for expected property start element
                else
                {
                	// A start element we are not expecting indicates an invalid parameter was passed
                	throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
                }
                              
                while(!reader.isStartElement() && !reader.isEndElement())
                	reader.next();
                            
                if(reader.isStartElement())
                	// A start element we are not expecting indicates a trailing invalid property
                	throw new org.apache.axis2.databinding.ADBException("Unexpected subelement " + reader.getLocalName());
            } 
            catch (javax.xml.stream.XMLStreamException e) 
            {
                throw new java.lang.Exception(e);
            }

            return object;
        }
}//end of factory class

        

        }
           
          