/**
 * ExtensionMapper.java
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1 Built on : Aug 19, 2008 (10:13:44 LKT)
 */

package au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf;

import javax.xml.stream.XMLStreamReader;

import org.apache.axis2.databinding.ADBException;

/**
 * ExtensionMapper class
 */

public class ExtensionMapper
{

    public static Object getTypeObject(final String namespaceURI, final String typeName, final XMLStreamReader reader)
            throws Exception
    {

        if ("http://remotelabs.eng.uts.edu.au/schedserver/localrigprovider".equals(namespaceURI)
                && "RigType".equals(typeName))
        {
            return RigType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/localrigprovider".equals(namespaceURI)
                && "UpdateRigType".equals(typeName))
        {
            return UpdateRigType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/localrigprovider".equals(namespaceURI)
                && "ProviderResponse".equals(typeName))
        {
            return ProviderResponse.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/localrigprovider".equals(namespaceURI)
                && "StatusType".equals(typeName))
        {
            return StatusType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/localrigprovider".equals(namespaceURI)
                && "RemoveRigType".equals(typeName))
        {
            return RemoveRigType.Factory.parse(reader);
        }

        if ("http://remotelabs.eng.uts.edu.au/schedserver/localrigprovider".equals(namespaceURI)
                && "RegisterRigType".equals(typeName))
        {
            return RegisterRigType.Factory.parse(reader);
        }

        throw new ADBException("Unsupported type " + namespaceURI + " " + typeName);
    }
}
