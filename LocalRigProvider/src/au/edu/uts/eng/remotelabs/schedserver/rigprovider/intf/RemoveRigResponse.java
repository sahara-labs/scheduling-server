package au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.axiom.om.OMConstants;
import org.apache.axiom.om.OMDataSource;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.impl.llom.OMSourcedElementImpl;
import org.apache.axis2.databinding.ADBBean;
import org.apache.axis2.databinding.ADBDataSource;
import org.apache.axis2.databinding.ADBException;
import org.apache.axis2.databinding.utils.writer.MTOMAwareXMLStreamWriter;

/**
 * RemoveRigResponse bean class.
 */
public class RemoveRigResponse implements ADBBean
{
    private static final long serialVersionUID = 7333419381053061409L;
    
    public static final QName MY_QNAME = new QName("http://remotelabs.eng.uts.edu.au/schedserver/localrigprovider",
            "removeRigResponse", "ns1");

    protected ProviderResponse removeRigResponse;
    
    public ProviderResponse getRemoveRigResponse()
    {
        return this.removeRigResponse;
    }

    public void setRemoveRigResponse(final ProviderResponse param)
    {
        this.removeRigResponse = param;
    }

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

    public OMElement getOMElement(final QName parentQName, final OMFactory factory) throws ADBException
    {
        final OMDataSource dataSource = new ADBDataSource(this, RemoveRigResponse.MY_QNAME)
        {
            @Override
            public void serialize(final MTOMAwareXMLStreamWriter xmlWriter)
                    throws XMLStreamException
            {
                RemoveRigResponse.this.serialize(RemoveRigResponse.MY_QNAME, factory, xmlWriter);
            }
        };
        return new OMSourcedElementImpl(RemoveRigResponse.MY_QNAME, factory, dataSource);
    }

    public void serialize(final QName parentQName, final OMFactory factory, final MTOMAwareXMLStreamWriter xmlWriter)
            throws XMLStreamException, ADBException
    {
        this.serialize(parentQName, factory, xmlWriter, false);
    }

    public void serialize(final QName parentQName, final OMFactory factory, final MTOMAwareXMLStreamWriter xmlWriter,
            final boolean serializeType) throws XMLStreamException, ADBException
    {
        if (this.removeRigResponse == null)
        {
            throw new ADBException("Property cannot be null!");
        }
        this.removeRigResponse.serialize(RemoveRigResponse.MY_QNAME, factory, xmlWriter);
    }

    public XMLStreamReader getPullParser(final QName qName) throws ADBException
    {
        return this.removeRigResponse.getPullParser(RemoveRigResponse.MY_QNAME);
    }

    public static class Factory
    {
        public static RemoveRigResponse parse(final XMLStreamReader reader) throws Exception
        {
            final RemoveRigResponse object = new RemoveRigResponse();
            try
            {
                while (!reader.isStartElement() && !reader.isEndElement())
                {
                    reader.next();
                }
                while (!reader.isEndElement())
                {
                    if (reader.isStartElement())
                    {
                        if (reader.isStartElement()
                                && new QName("http://remotelabs.eng.uts.edu.au/schedserver/localrigprovider",
                                        "removeRigResponse").equals(reader.getName()))
                        {
                            object.setRemoveRigResponse(ProviderResponse.Factory.parse(reader));
                        }
                        else
                        {
                            throw new ADBException("Unexpected subelement " + reader.getLocalName());
                        }
                    }
                    else
                    {
                        reader.next();
                    }
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
