package au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.apache.axiom.om.OMDataSource;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axis2.databinding.ADBBean;
import org.apache.axis2.databinding.ADBDataSource;
import org.apache.axis2.databinding.ADBException;
import org.apache.axis2.databinding.utils.BeanUtil;
import org.apache.axis2.databinding.utils.ConverterUtil;
import org.apache.axis2.databinding.utils.reader.ADBXMLStreamReaderImpl;

/**
 * UserStatusType bean class.
 */
public class UserStatusType implements ADBBean
{
    /*
     * This type was generated from the piece of schema that had
     * name = UserStatusType
     * Namespace URI = http://remotelabs.eng.uts.edu.au/schedserver/multisite
     * Namespace Prefix = ns1
     */

    private static final long serialVersionUID = -6482391083726654472L;
    
    protected OperationResponseType operation;
    protected boolean operationTracker = false;
    
    protected boolean inQueue;

    protected boolean inSession;
    
    protected boolean inBooking;

    protected ResourceType queuedResource;
    protected boolean queuedResourceTracker = false;

    protected ResourceType bookedResource;
    protected boolean bookedResourceTracker = false;

    protected SessionType session;
    protected boolean sessionTracker = false;

    private static String generatePrefix(final String namespace)
    {
        if (namespace.equals("http://remotelabs.eng.uts.edu.au/schedserver/multisite"))
        {
            return "ns1";
        }
        return BeanUtil.getUniquePrefix();
    }

    public ResourceType getBookedResource()
    {
        return this.bookedResource;
    }

    public boolean getInBooking()
    {
        return this.inBooking;
    }

    public boolean getInQueue()
    {
        return this.inQueue;
    }

    public boolean getInSession()
    {
        return this.inSession;
    }

    @Override
    public OMElement getOMElement(final QName parentQName, final OMFactory factory) throws ADBException
    {
        final OMDataSource dataSource = new ADBDataSource(this, parentQName);
        return factory.createOMElement(dataSource, parentQName);
    }

    @Override
    public XMLStreamReader getPullParser(final QName qName) throws ADBException
    {
        final ArrayList<Serializable> elementList = new ArrayList<Serializable>();
        
        if (this.operationTracker)
        {
            elementList.add(new QName("", "operation"));
            elementList.add(this.operation);
        }
        
        elementList.add(new QName("", "inQueue"));
        elementList.add(ConverterUtil.convertToString(this.inQueue));

        elementList.add(new QName("", "inSession"));
        elementList.add(ConverterUtil.convertToString(this.inSession));

        elementList.add(new QName("", "inBooking"));
        elementList.add(ConverterUtil.convertToString(this.inBooking));
        
        if (this.queuedResourceTracker)
        {
            elementList.add(new QName("", "queuedResource"));

            if (this.queuedResource == null)
            {
                throw new ADBException("queuedResource cannot be null!!");
            }
            elementList.add(this.queuedResource);
        }
        
        if (this.bookedResourceTracker)
        {
            elementList.add(new QName("", "bookedResource"));

            if (this.bookedResource == null)
            {
                throw new ADBException("bookedResource cannot be null!!");
            }
            elementList.add(this.bookedResource);
        }
        
        if (this.sessionTracker)
        {
            elementList.add(new QName("", "session"));

            if (this.session == null)
            {
                throw new ADBException("session cannot be null!!");
            }
            elementList.add(this.session);
        }

        return new ADBXMLStreamReaderImpl(qName, elementList.toArray(), new Object[0]);
    }
    
    public OperationResponseType getOperation()
    {
        return this.operation;
    }

    public ResourceType getQueuedResource()
    {
        return this.queuedResource;
    }

    public SessionType getSession()
    {
        return this.session;
    }

    public boolean isBookedResourceSpecified()
    {
        return this.bookedResourceTracker;
    }

    public boolean isQueuedResourceSpecified()
    {
        return this.queuedResourceTracker;
    }

    public boolean isSessionSpecified()
    {
        return this.sessionTracker;
    }

    private String registerPrefix(final XMLStreamWriter xmlWriter, final String namespace) throws XMLStreamException
    {
        String prefix = xmlWriter.getPrefix(namespace);
        if (prefix == null)
        {
            prefix = UserStatusType.generatePrefix(namespace);
            while (xmlWriter.getNamespaceContext().getNamespaceURI(prefix) != null)
            {
                prefix = BeanUtil.getUniquePrefix();
            }
            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
        }
        return prefix;
    }

    @Override
    public void serialize(final QName parentQName, final XMLStreamWriter xmlWriter) throws XMLStreamException,
            ADBException
    {
        this.serialize(parentQName, xmlWriter, false);
    }

    @Override
    public void serialize(final QName parentQName, final XMLStreamWriter xmlWriter, final boolean serializeType)
            throws XMLStreamException, ADBException
    {
        String prefix = parentQName.getPrefix();
        String namespace = parentQName.getNamespaceURI();
        
        this.writeStartElement(prefix, namespace, parentQName.getLocalPart(), xmlWriter);
        
        if (serializeType)
        {
            final String namespacePrefix = this.registerPrefix(xmlWriter,
                    "http://remotelabs.eng.uts.edu.au/schedserver/multisite");
            if ((namespacePrefix != null) && (namespacePrefix.trim().length() > 0))
            {
                this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", namespacePrefix
                        + ":UserStatusType", xmlWriter);
            }
            else
            {
                this.writeAttribute("xsi", "http://www.w3.org/2001/XMLSchema-instance", "type", "UserStatusType",
                        xmlWriter);
            }

        }
        
        if (this.operationTracker)
        {
            if (this.operation == null)
            {
                throw new ADBException("operation cannot be null");
            }
            this.operation.serialize(new QName("operation"), xmlWriter);
        }

        namespace = "";
        this.writeStartElement(null, namespace, "inQueue", xmlWriter);
        xmlWriter.writeCharacters(ConverterUtil.convertToString(this.inQueue));
        xmlWriter.writeEndElement();

        namespace = "";
        this.writeStartElement(null, namespace, "inSession", xmlWriter);
        xmlWriter.writeCharacters(ConverterUtil.convertToString(this.inSession));
        xmlWriter.writeEndElement();

        namespace = "";
        this.writeStartElement(null, namespace, "inBooking", xmlWriter);

        xmlWriter.writeEndElement();

        if (this.queuedResourceTracker)
        {
            if (this.queuedResource == null)
            {
                throw new ADBException("queuedResource cannot be null!!");
            }
            this.queuedResource.serialize(new QName("", "queuedResource"), xmlWriter);
        }
        
        if (this.bookedResourceTracker)
        {
            if (this.bookedResource == null)
            {
                throw new ADBException("bookedResource cannot be null!!");
            }
            this.bookedResource.serialize(new QName("", "bookedResource"), xmlWriter);
        }
        
        if (this.sessionTracker)
        {
            if (this.session == null)
            {
                throw new ADBException("session cannot be null!!");
            }
            this.session.serialize(new QName("", "session"), xmlWriter);
        }
        
        xmlWriter.writeEndElement();
    }
    public void setBookedResource(final ResourceType param)
    {
        this.bookedResourceTracker = param != null;
        this.bookedResource = param;
    }

    public void setInBooking(final boolean param)
    {
        this.inBooking = param;
    }

    public void setInQueue(final boolean param)
    {
        this.inQueue = param;
    }

    public void setInSession(final boolean param)
    {
        this.inSession = param;
    }

    public void setOperation(final OperationResponseType param)
    {
        this.operation = param;
        this.operationTracker = param != null;
    }

    public void setQueuedResource(final ResourceType param)
    {
        this.queuedResourceTracker = param != null;
        this.queuedResource = param;
    }

    public void setSession(final SessionType param)
    {
        this.sessionTracker = param != null;
        this.session = param;
    }

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

    private void writeStartElement(String prefix, final String namespace, final String localPart,
            final XMLStreamWriter xmlWriter) throws XMLStreamException
    {
        final String writerPrefix = xmlWriter.getPrefix(namespace);
        if (writerPrefix != null)
        {
            xmlWriter.writeStartElement(namespace, localPart);
        }
        else
        {
            if (namespace.length() == 0)
            {
                prefix = "";
            }
            else if (prefix == null)
            {
                prefix = UserStatusType.generatePrefix(namespace);
            }

            xmlWriter.writeStartElement(prefix, localPart, namespace);
            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
        }
    }


    public static class Factory
    {
        public static UserStatusType parse(final XMLStreamReader reader) throws Exception
        {
            final UserStatusType object = new UserStatusType();
            try
            {
                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                if (reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance", "type") != null)
                {
                    final String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "type");
                    if (fullTypeName != null)
                    {
                        String nsPrefix = null;
                        if (fullTypeName.indexOf(":") > -1)
                        {
                            nsPrefix = fullTypeName.substring(0, fullTypeName.indexOf(":"));
                        }
                        nsPrefix = nsPrefix == null ? "" : nsPrefix;
                        final String type = fullTypeName.substring(fullTypeName.indexOf(":") + 1);

                        if (!"UserStatusType".equals(type))
                        {
                            final String nsUri = reader.getNamespaceContext().getNamespaceURI(nsPrefix);
                            return (UserStatusType) ExtensionMapper.getTypeObject(nsUri, type, reader);
                        }
                    }                    
                }
                reader.next();
                
                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "operation").equals(reader.getName()))
                {
                    object.setOperation(OperationResponseType.Factory.parse(reader));
                    reader.next();
                }
                
                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "inQueue").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setInQueue(ConverterUtil.convertToBoolean(content));
                    reader.next();
                }
                else
                {
                    throw new ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "inSession").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setInSession(ConverterUtil.convertToBoolean(content));
                    reader.next();
                }
                else
                {
                    throw new ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "inBooking").equals(reader.getName()))
                {
                    final String content = reader.getElementText();
                    object.setInBooking(ConverterUtil.convertToBoolean(content));
                    reader.next();
                }
                else
                {
                    throw new ADBException("Unexpected subelement " + reader.getName());
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "queuedResource").equals(reader.getName()))
                {
                    object.setQueuedResource(ResourceType.Factory.parse(reader));
                    reader.next();
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement() && new QName("", "bookedResource").equals(reader.getName()))
                {
                    object.setBookedResource(ResourceType.Factory.parse(reader));
                    reader.next();
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }

                if (reader.isStartElement() && new QName("", "session").equals(reader.getName()))
                {
                    object.setSession(SessionType.Factory.parse(reader));
                    reader.next();
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                if (reader.isStartElement())
                {
                    throw new ADBException("Unexpected subelement " + reader.getName());
                }
            }
            catch (final XMLStreamException e)
            {
                throw new Exception(e);
            }

            return object;
        }
    }
}
