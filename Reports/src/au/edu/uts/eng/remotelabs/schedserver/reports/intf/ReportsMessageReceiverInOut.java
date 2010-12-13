/**
 * ReportsMessageReceiverInOut.java
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4 Built on : Apr 26, 2008 (06:24:30 EDT)
 */
package au.edu.uts.eng.remotelabs.schedserver.reports.intf;

/**
 * ReportsMessageReceiverInOut message receiver
 */

public class ReportsMessageReceiverInOut extends org.apache.axis2.receivers.AbstractInOutMessageReceiver
{

    @Override
    public void invokeBusinessLogic(final org.apache.axis2.context.MessageContext msgContext,
            final org.apache.axis2.context.MessageContext newMsgContext) throws org.apache.axis2.AxisFault
    {

        try
        {

            // get the implementation class for the Web Service
            final Object obj = this.getTheImplementationObject(msgContext);

            final ReportsSkeletonInterface skel = (ReportsSkeletonInterface) obj;
            //Out Envelop
            org.apache.axiom.soap.SOAPEnvelope envelope = null;
            //Find the axisOperation that has been set by the Dispatch phase.
            final org.apache.axis2.description.AxisOperation op = msgContext.getOperationContext().getAxisOperation();
            if (op == null)
            {
                throw new org.apache.axis2.AxisFault(
                        "Operation is not located, if this is doclit style the SOAP-ACTION should specified via the SOAP Action to use the RawXMLProvider");
            }

            java.lang.String methodName;
            if ((op.getName() != null)
                    && ((methodName = org.apache.axis2.util.JavaUtils.xmlNameToJava(op.getName().getLocalPart())) != null))
            {

                if ("querySessionAccess".equals(methodName))
                {

                    au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionAccessResponse querySessionAccessResponse7 = null;
                    final au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionAccess wrappedParam = (au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionAccess) this
                            .fromOM(msgContext.getEnvelope().getBody().getFirstElement(),
                                    au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionAccess.class,
                                    this.getEnvelopeNamespaces(msgContext.getEnvelope()));

                    querySessionAccessResponse7 =

                    skel.querySessionAccess(wrappedParam);

                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), querySessionAccessResponse7, false);
                }
                else

                if ("queryInfo".equals(methodName))
                {

                    au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryInfoResponse queryInfoResponse9 = null;
                    final au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryInfo wrappedParam = (au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryInfo) this
                            .fromOM(msgContext.getEnvelope().getBody().getFirstElement(),
                                    au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryInfo.class, this
                                            .getEnvelopeNamespaces(msgContext.getEnvelope()));

                    queryInfoResponse9 =

                    skel.queryInfo(wrappedParam);

                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), queryInfoResponse9, false);
                }
                else

                if ("querySessionReport".equals(methodName))
                {

                    au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionReportResponse querySessionReportResponse11 = null;
                    final au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionReport wrappedParam = (au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionReport) this
                            .fromOM(msgContext.getEnvelope().getBody().getFirstElement(),
                                    au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionReport.class,
                                    this.getEnvelopeNamespaces(msgContext.getEnvelope()));

                    querySessionReportResponse11 =

                    skel.querySessionReport(wrappedParam);

                    envelope = this.toEnvelope(this.getSOAPFactory(msgContext), querySessionReportResponse11, false);

                }
                else
                {
                    throw new java.lang.RuntimeException("method not found");
                }

                newMsgContext.setEnvelope(envelope);
            }
        }
        catch (final java.lang.Exception e)
        {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(final org.apache.axiom.soap.SOAPFactory factory,
            final au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionAccessResponse param,
            final boolean optimizeContent) throws org.apache.axis2.AxisFault
    {
        try
        {
            final org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();

            emptyEnvelope
                    .getBody()
                    .addChild(
                            param
                                    .getOMElement(
                                            au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionAccessResponse.MY_QNAME,
                                            factory));

            return emptyEnvelope;
        }
        catch (final org.apache.axis2.databinding.ADBException e)
        {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(final org.apache.axiom.soap.SOAPFactory factory,
            final au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryInfoResponse param,
            final boolean optimizeContent) throws org.apache.axis2.AxisFault
    {
        try
        {
            final org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();

            emptyEnvelope.getBody().addChild(
                    param.getOMElement(
                            au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryInfoResponse.MY_QNAME,
                            factory));

            return emptyEnvelope;
        }
        catch (final org.apache.axis2.databinding.ADBException e)
        {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private org.apache.axiom.soap.SOAPEnvelope toEnvelope(final org.apache.axiom.soap.SOAPFactory factory,
            final au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionReportResponse param,
            final boolean optimizeContent) throws org.apache.axis2.AxisFault
    {
        try
        {
            final org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();

            emptyEnvelope
                    .getBody()
                    .addChild(
                            param
                                    .getOMElement(
                                            au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionReportResponse.MY_QNAME,
                                            factory));

            return emptyEnvelope;
        }
        catch (final org.apache.axis2.databinding.ADBException e)
        {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
    }

    private java.lang.Object fromOM(final org.apache.axiom.om.OMElement param, final java.lang.Class type,
            final java.util.Map extraNamespaces) throws org.apache.axis2.AxisFault
    {

        try
        {

            if (au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionAccess.class.equals(type))
            {

                return au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionAccess.Factory.parse(param
                        .getXMLStreamReaderWithoutCaching());

            }

            if (au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionAccessResponse.class.equals(type))
            {

                return au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionAccessResponse.Factory
                        .parse(param.getXMLStreamReaderWithoutCaching());

            }

            if (au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryInfo.class.equals(type))
            {

                return au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryInfo.Factory.parse(param
                        .getXMLStreamReaderWithoutCaching());

            }

            if (au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryInfoResponse.class.equals(type))
            {

                return au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryInfoResponse.Factory.parse(param
                        .getXMLStreamReaderWithoutCaching());

            }

            if (au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionReport.class.equals(type))
            {

                return au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionReport.Factory.parse(param
                        .getXMLStreamReaderWithoutCaching());

            }

            if (au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionReportResponse.class.equals(type))
            {

                return au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionReportResponse.Factory
                        .parse(param.getXMLStreamReaderWithoutCaching());

            }

        }
        catch (final java.lang.Exception e)
        {
            throw org.apache.axis2.AxisFault.makeFault(e);
        }
        return null;
    }

    /**
     * A utility method that copies the namepaces from the SOAPEnvelope
     */
    private java.util.Map getEnvelopeNamespaces(final org.apache.axiom.soap.SOAPEnvelope env)
    {
        final java.util.Map returnMap = new java.util.HashMap();
        final java.util.Iterator namespaceIterator = env.getAllDeclaredNamespaces();
        while (namespaceIterator.hasNext())
        {
            final org.apache.axiom.om.OMNamespace ns = (org.apache.axiom.om.OMNamespace) namespaceIterator.next();
            returnMap.put(ns.getPrefix(), ns.getNamespaceURI());
        }
        return returnMap;
    }

}//end of class
