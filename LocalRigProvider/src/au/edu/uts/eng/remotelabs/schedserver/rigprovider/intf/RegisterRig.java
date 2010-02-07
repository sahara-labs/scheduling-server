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
 * RegisterRig bean class.
 */
public class RegisterRig implements ADBBean
{
    private static final long serialVersionUID = -354021647075760313L;

    public static final QName MY_QNAME = new QName(
            "http://remotelabs.eng.uts.edu.au/schedserver/localrigprovider",
            "registerRig", "ns1");

    protected RegisterRigType registerRig;

    public RegisterRigType getRegisterRig()
    {
        return this.registerRig;
    }

    public void setRegisterRig(final RegisterRigType param)
    {
        this.registerRig = param;
    }

    public static boolean isReaderMTOMAware(final XMLStreamReader reader)
    {
        boolean isReaderMTOMAware = false;

        try
        {
            isReaderMTOMAware = Boolean.TRUE.equals(reader
                    .getProperty(OMConstants.IS_DATA_HANDLERS_AWARE));
        }
        catch (final IllegalArgumentException e)
        {
            isReaderMTOMAware = false;
        }
        return isReaderMTOMAware;
    }

    public OMElement getOMElement(final QName parentQName,
            final OMFactory factory) throws ADBException
    {
        final OMDataSource dataSource = new ADBDataSource(this,
                RegisterRig.MY_QNAME)
        {
            @Override
            public void serialize(final MTOMAwareXMLStreamWriter xmlWriter)
                    throws XMLStreamException
            {
                RegisterRig.this.serialize(RegisterRig.MY_QNAME, factory,
                        xmlWriter);
            }
        };
        return new OMSourcedElementImpl(RegisterRig.MY_QNAME, factory,
                dataSource);
    }

    public void serialize(final QName parentQName, final OMFactory factory,
            final MTOMAwareXMLStreamWriter xmlWriter)
            throws XMLStreamException, ADBException
    {
        this.serialize(parentQName, factory, xmlWriter, false);
    }

    public void serialize(final QName parentQName, final OMFactory factory,
            final MTOMAwareXMLStreamWriter xmlWriter,
            final boolean serializeType) throws XMLStreamException,
            ADBException
    {
        if (this.registerRig == null)
        {
            throw new ADBException("Property cannot be null!");
        }
        this.registerRig.serialize(RegisterRig.MY_QNAME, factory, xmlWriter);
    }

    public XMLStreamReader getPullParser(final QName qName) throws ADBException
    {
        return this.registerRig.getPullParser(RegisterRig.MY_QNAME);
    }

    public static class Factory
    {
        public static RegisterRig parse(final XMLStreamReader reader) throws Exception
        {
            final RegisterRig object = new RegisterRig();
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
                                        "registerRig").equals(reader.getName()))
                        {
                            object.setRegisterRig(RegisterRigType.Factory.parse(reader));
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
