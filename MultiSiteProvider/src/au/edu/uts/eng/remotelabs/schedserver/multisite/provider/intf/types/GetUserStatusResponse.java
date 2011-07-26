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
import org.apache.axis2.databinding.utils.BeanUtil;

/**
 * GetUserStatusResponse bean class
 */
public class GetUserStatusResponse implements ADBBean
{
    private static final long serialVersionUID = -5881738548430891162L;

    public static final QName MY_QNAME = new QName("http://remotelabs.eng.uts.edu.au/schedserver/multisite",
            "getUserStatusResponse", "ns1");

    protected UserStatusType getUserStatusResponse;

    public UserStatusType getGetUserStatusResponse()
    {
        return this.getUserStatusResponse;
    }

    public void setGetUserStatusResponse(final UserStatusType param)
    {
        this.getUserStatusResponse = param;
    }

    @Override
    public OMElement getOMElement(final QName parentQName, final OMFactory factory) throws ADBException
    {
        final OMDataSource dataSource = new ADBDataSource(this, GetUserStatusResponse.MY_QNAME);
        return factory.createOMElement(dataSource, GetUserStatusResponse.MY_QNAME);
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
        if (this.getUserStatusResponse == null)
        {
            throw new ADBException("getUserStatusResponse cannot be null!");
        }
        this.getUserStatusResponse.serialize(GetUserStatusResponse.MY_QNAME, xmlWriter);
    }

    @Override
    public XMLStreamReader getPullParser(final QName qName) throws ADBException
    {
        return this.getUserStatusResponse.getPullParser(GetUserStatusResponse.MY_QNAME);
    }

    public static class Factory
    {
        public static GetUserStatusResponse parse(final XMLStreamReader reader) throws Exception
        {
            final GetUserStatusResponse object = new GetUserStatusResponse();
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
                                        "getUserStatusResponse").equals(reader.getName()))
                        {
                            object.setGetUserStatusResponse(UserStatusType.Factory.parse(reader));
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

    }//end of factory class

}
