

package au.edu.uts.eng.remotelabs.schedserver.multisite.intf.federation.types;

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
 * SiteShutdown bean class.
 */
public class SiteShutdown implements ADBBean
{
    private static final long serialVersionUID = 5543570250327627296L;

    public static final QName MY_QNAME = new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/",
            "siteShutdown", "ns2");

    protected SiteShutdownType siteShutdown;

    public SiteShutdownType getSiteShutdown()
    {
        return this.siteShutdown;
    }

    public void setSiteShutdown(final SiteShutdownType param)
    {
        this.siteShutdown = param;
    }

    @Override
    public OMElement getOMElement(final QName parentQName, final OMFactory factory) throws ADBException
    {
        final OMDataSource dataSource = new ADBDataSource(this, SiteShutdown.MY_QNAME);
        return factory.createOMElement(dataSource, SiteShutdown.MY_QNAME);
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
        if (this.siteShutdown == null)
        {
            throw new ADBException("siteShutdown cannot be null!");
        }
        this.siteShutdown.serialize(SiteShutdown.MY_QNAME, xmlWriter);
    }

    @Override
    public XMLStreamReader getPullParser(final QName qName) throws ADBException
    {
        return this.siteShutdown.getPullParser(SiteShutdown.MY_QNAME);
    }

    public static class Factory
    {
        public static SiteShutdown parse(final XMLStreamReader reader) throws Exception
        {
            final SiteShutdown object = new SiteShutdown();
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
                                && new QName("http://remotelabs.eng.uts.edu.au/schedserver/MultiSiteFederation/",
                                        "siteShutdown").equals(reader.getName()))
                        {
                            object.setSiteShutdown(SiteShutdownType.Factory.parse(reader));
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
