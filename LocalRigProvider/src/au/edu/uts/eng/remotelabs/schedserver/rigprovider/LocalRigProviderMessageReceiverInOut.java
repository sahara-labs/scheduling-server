/**
 * LocalRigProviderMessageReceiverInOut.java
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1 Built on : Aug 19, 2008 (10:13:39 LKT)
 */
package au.edu.uts.eng.remotelabs.schedserver.rigprovider;

/**
 * LocalRigProviderMessageReceiverInOut message receiver
 */

public class LocalRigProviderMessageReceiverInOut extends
org.apache.axis2.receivers.AbstractInOutMessageReceiver
{

    @Override
    public void invokeBusinessLogic(
            final org.apache.axis2.context.MessageContext msgContext,
            final org.apache.axis2.context.MessageContext newMsgContext)
    throws org.apache.axis2.AxisFault
    {

        try
        {

            // get the implementation class for the Web Service
            final Object obj = this.getTheImplementationObject(msgContext);

            final LocalRigProviderSkeletonInterface skel = (LocalRigProviderSkeletonInterface) obj;
            //Out Envelop
            org.apache.axiom.soap.SOAPEnvelope envelope = null;
            //Find the axisOperation that has been set by the Dispatch phase.
            final org.apache.axis2.description.AxisOperation op = msgContext
            .getOperationContext().getAxisOperation();
            if (op == null)
            {
                throw new org.apache.axis2.AxisFault(
                "Operation is not located, if this is doclit style the SOAP-ACTION should specified via the SOAP Action to use the RawXMLProvider");
            }

            String methodName;
            if ((op.getName() != null)
                    && ((methodName = org.apache.axis2.util.JavaUtils
                            .xmlNameToJava(op.getName().getLocalPart())) != null))
            {

                if ("removeRig".equals(methodName))
                {

                    au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.RemoveRigResponse removeRigResponse7 = null;
                    final au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.RemoveRig wrappedParam = (au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.RemoveRig) this.fromOM(
                            msgContext.getEnvelope().getBody()
                            .getFirstElement(),
                            au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.RemoveRig.class,
                            this.getEnvelopeNamespaces(msgContext.getEnvelope()));

                    removeRigResponse7 =

                        skel.removeRig(wrappedParam);

                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext),
                            removeRigResponse7, false);
                }
                else

                    if ("updateRigStatus".equals(methodName))
                    {

                        au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.UpdateRigStatusResponse updateRigStatusResponse9 = null;
                        final au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.UpdateRigStatus wrappedParam = (au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.UpdateRigStatus) this.fromOM(
                                msgContext.getEnvelope().getBody()
                                .getFirstElement(),
                                au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.UpdateRigStatus.class,
                                this.getEnvelopeNamespaces(msgContext.getEnvelope()));

                        updateRigStatusResponse9 =

                            skel.updateRigStatus(wrappedParam);

                        envelope = this.toEnvelope(this.getSOAPFactory(msgContext),
                                updateRigStatusResponse9, false);
                    }
                    else

                        if ("registerRig".equals(methodName))
                        {

                            au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.RegisterRigResponse registerRigResponse11 = null;
                            final au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.RegisterRig wrappedParam = (au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.RegisterRig) this.fromOM(
                                    msgContext.getEnvelope().getBody()
                                    .getFirstElement(),
                                    au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.RegisterRig.class,
                                    this.getEnvelopeNamespaces(msgContext.getEnvelope()));

                            registerRigResponse11 =

                                skel.registerRig(wrappedParam);

                            envelope = this.toEnvelope(this.getSOAPFactory(msgContext),
                                    registerRigResponse11, false);

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
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
            final org.apache.axiom.soap.SOAPFactory factory,
            final au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.RemoveRigResponse param,
            final boolean optimizeContent) throws org.apache.axis2.AxisFault
            {
        try
        {
            final org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory
            .getDefaultEnvelope();

            emptyEnvelope
            .getBody()
            .addChild(
                    param
                    .getOMElement(
                            au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.RemoveRigResponse.MY_QNAME,
                            factory));

            return emptyEnvelope;
        }
        catch (final org.apache.axis2.databinding.ADBException e)
        {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
            }

    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
            final org.apache.axiom.soap.SOAPFactory factory,
            final au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.UpdateRigStatusResponse param,
            final boolean optimizeContent) throws org.apache.axis2.AxisFault
            {
        try
        {
            final org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory
            .getDefaultEnvelope();

            emptyEnvelope
            .getBody()
            .addChild(
                    param
                    .getOMElement(
                            au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.UpdateRigStatusResponse.MY_QNAME,
                            factory));

            return emptyEnvelope;
        }
        catch (final org.apache.axis2.databinding.ADBException e)
        {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
            }

    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
            final org.apache.axiom.soap.SOAPFactory factory,
            final au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.RegisterRigResponse param,
            final boolean optimizeContent) throws org.apache.axis2.AxisFault
            {
        try
        {
            final org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory
            .getDefaultEnvelope();

            emptyEnvelope
            .getBody()
            .addChild(
                    param
                    .getOMElement(
                            au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.RegisterRigResponse.MY_QNAME,
                            factory));

            return emptyEnvelope;
        }
        catch (final org.apache.axis2.databinding.ADBException e)
        {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
            }

    private Object fromOM(final org.apache.axiom.om.OMElement param,
            final Class type, final java.util.Map extraNamespaces)
    throws org.apache.axis2.AxisFault
    {

        try
        {

            if (au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.RemoveRig.class
                    .equals(type))
            {

                return au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.RemoveRig.Factory
                .parse(param.getXMLStreamReaderWithoutCaching());

            }

            if (au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.RemoveRigResponse.class
                    .equals(type))
            {

                return au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.RemoveRigResponse.Factory
                .parse(param.getXMLStreamReaderWithoutCaching());

            }

            if (au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.UpdateRigStatus.class
                    .equals(type))
            {

                return au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.UpdateRigStatus.Factory
                .parse(param.getXMLStreamReaderWithoutCaching());

            }

            if (au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.UpdateRigStatusResponse.class
                    .equals(type))
            {

                return au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.UpdateRigStatusResponse.Factory
                .parse(param.getXMLStreamReaderWithoutCaching());

            }

            if (au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.RegisterRig.class
                    .equals(type))
            {

                return au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.RegisterRig.Factory
                .parse(param.getXMLStreamReaderWithoutCaching());

            }

            if (au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.RegisterRigResponse.class
                    .equals(type))
            {

                return au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.RegisterRigResponse.Factory
                .parse(param.getXMLStreamReaderWithoutCaching());

            }

        }
        catch (final Exception e)
        {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
        return null;
    }

    /**
     * A utility method that copies the namepaces from the SOAPEnvelope
     */
     private java.util.Map getEnvelopeNamespaces(
             final org.apache.axiom.soap.SOAPEnvelope env)
     {
         final java.util.Map returnMap = new java.util.HashMap();
         final java.util.Iterator namespaceIterator = env.getAllDeclaredNamespaces();
         while (namespaceIterator.hasNext())
         {
             final org.apache.axiom.om.OMNamespace ns = (org.apache.axiom.om.OMNamespace) namespaceIterator
             .next();
             returnMap.put(ns.getPrefix(), ns.getNamespaceURI());
         }
         return returnMap;
     }

}//end of class
