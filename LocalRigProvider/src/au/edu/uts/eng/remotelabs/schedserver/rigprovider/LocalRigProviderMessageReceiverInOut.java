package au.edu.uts.eng.remotelabs.schedserver.rigprovider;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.databinding.ADBBean;
import org.apache.axis2.databinding.ADBException;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.receivers.AbstractInOutMessageReceiver;
import org.apache.axis2.util.JavaUtils;

import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.RegisterRig;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.RegisterRigResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.RemoveRig;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.RemoveRigResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.UpdateRigStatus;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.UpdateRigStatusResponse;

/**
 * LocalRigProviderMessageReceiverInOut message receiver.
 */
public class LocalRigProviderMessageReceiverInOut extends AbstractInOutMessageReceiver
{
    @Override
    public void invokeBusinessLogic(final MessageContext msgContext, final MessageContext newMsgContext) 
            throws AxisFault
    {
        try
        {
            final Object obj = this.getTheImplementationObject(msgContext);
            final LocalRigProviderSkeletonInterface skel = (LocalRigProviderSkeletonInterface) obj;

            
            SOAPEnvelope envelope = null;
            final AxisOperation op = msgContext.getOperationContext().getAxisOperation();
            if (op == null)
            {
                throw new AxisFault("Operation is not located, if this is doclit style the SOAP-ACTION should " +
                		"specified via the SOAP Action to use the RawXMLProvider");
            }

            String methodName;
            if ((op.getName() != null) && ((methodName = JavaUtils.xmlNameToJava(op.getName().getLocalPart())) != null))
            {
                if ("removeRig".equals(methodName))
                {

                    RemoveRigResponse removeRigResponse = null;
                    final RemoveRig wrappedParam = (RemoveRig) this.fromOM(
                            msgContext.getEnvelope().getBody().getFirstElement(), RemoveRig.class, 
                            this.getEnvelopeNamespaces(msgContext.getEnvelope()));
                    removeRigResponse = skel.removeRig(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), removeRigResponse, false);
                }
                else if ("updateRigStatus".equals(methodName))
                {
                    UpdateRigStatusResponse updateRigStatusResponse = null;
                    final UpdateRigStatus wrappedParam = (UpdateRigStatus) this.fromOM(
                            msgContext.getEnvelope().getBody().getFirstElement(), UpdateRigStatus.class, 
                            this.getEnvelopeNamespaces(msgContext.getEnvelope()));
                    updateRigStatusResponse = skel.updateRigStatus(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), updateRigStatusResponse, false);
                }
                else if ("registerRig".equals(methodName))
                {
                    RegisterRigResponse registerRigResponse = null;
                    final RegisterRig wrappedParam = (RegisterRig) this.fromOM(
                            msgContext.getEnvelope().getBody().getFirstElement(), RegisterRig.class, 
                            this.getEnvelopeNamespaces(msgContext.getEnvelope()));
                    registerRigResponse = skel.registerRig(wrappedParam);
                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), registerRigResponse, false);
                }
                else
                {
                    throw new RuntimeException("method not found");
                }

                newMsgContext.setEnvelope(envelope);
            }
        }
        catch (final Exception e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final RemoveRigResponse param, final boolean optimizeContent)
            throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(RemoveRigResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final UpdateRigStatusResponse param, final boolean optimizeContent)
            throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(UpdateRigStatusResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private SOAPEnvelope toEnvelope(final SOAPFactory factory, final RegisterRigResponse param, final boolean optimizeContent)
            throws AxisFault
    {
        try
        {
            final SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
            emptyEnvelope.getBody().addChild(param.getOMElement(RegisterRigResponse.MY_QNAME, factory));
            return emptyEnvelope;
        }
        catch (final ADBException e)
        {
            throw AxisFault.makeFault(e);
        }
    }

    private Object fromOM(final OMElement param, final Class<? extends ADBBean> type, final Map<String, String> extraNamespaces) 
            throws AxisFault
    {
        try
        {
            if (RemoveRig.class.equals(type))
            {
                return RemoveRig.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (RemoveRigResponse.class.equals(type))
            {
                return RemoveRigResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (UpdateRigStatus.class.equals(type))
            {
                return UpdateRigStatus.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (UpdateRigStatusResponse.class.equals(type))
            {
                return UpdateRigStatusResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (RegisterRig.class.equals(type))
            {
                return RegisterRig.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }

            if (RegisterRigResponse.class.equals(type))
            {
                return RegisterRigResponse.Factory.parse(param.getXMLStreamReaderWithoutCaching());
            }
        }
        catch (final Exception e)
        {
            throw AxisFault.makeFault(e);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> getEnvelopeNamespaces(final SOAPEnvelope env)
    {
        final Map<String, String> returnMap = new HashMap<String, String>();
        final Iterator namespaceIterator = env.getAllDeclaredNamespaces();
        while (namespaceIterator.hasNext())
        {
            final OMNamespace ns = (OMNamespace) namespaceIterator.next();
            returnMap.put(ns.getPrefix(), ns.getNamespaceURI());
        }
        return returnMap;
    }
}
