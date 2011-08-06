package au.edu.uts.eng.remotelabs.schedserver.multisite.provider.intf.types;

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

/**
 * FinishSessionResponse bean class.
 */
public class FinishSessionResponse implements ADBBean
{
    private static final long serialVersionUID = 7987906273832309633L;

    public static final QName MY_QNAME = new QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite",
            "finishSessionResponse", "ns1");

    protected OperationResponseType finishSessionResponse;

    public OperationResponseType getFinishSessionResponse()
    {
        return this.finishSessionResponse;
    }

    public void setFinishSessionResponse(final OperationResponseType param)
    {
        this.finishSessionResponse = param;
    }

    @Override
    public OMElement getOMElement(final QName parentQName, final OMFactory factory) throws ADBException
    {
        final OMDataSource dataSource = new ADBDataSource(this, FinishSessionResponse.MY_QNAME);
        return factory.createOMElement(dataSource, FinishSessionResponse.MY_QNAME);
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
        if (this.finishSessionResponse == null)
        {
            throw new ADBException("finishSessionResponse cannot be null!");
        }
        this.finishSessionResponse.serialize(FinishSessionResponse.MY_QNAME, xmlWriter);
    }

    @Override
    public XMLStreamReader getPullParser(final QName qName) throws ADBException
    {
        return this.finishSessionResponse.getPullParser(FinishSessionResponse.MY_QNAME);
    }

    public static class Factory
    {
        public static FinishSessionResponse parse(final XMLStreamReader reader) throws Exception
        {
            final FinishSessionResponse object = new FinishSessionResponse();

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
                                && new QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite",
                                        "finishSessionResponse").equals(reader.getName()))
                        {
                            object.setFinishSessionResponse(OperationResponseType.Factory.parse(reader));
                        }
                        else
                        {
                            throw new ADBException("Unexpected subelement " + reader.getName());
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
