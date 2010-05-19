/**
 * SAHARA Scheduling Server - LabConnectorServiceClient
 * Web Service client to access the LabConnector WebService.
 * 
 * @license See LICENSE in the top level directory for complete license terms.
 * 
 * Copyright (c) 2010, University of Technology, Sydney
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *  * Neither the name of the University of Technology, Sydney nor the names
 *    of its contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *    
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * @author Herbert Yeung
 * @date 18th May 2010
 */

package au.edu.labshare.schedserver.labconnector.client;

import java.rmi.RemoteException;
import au.edu.labshare.schedserver.labconnector.client.LabConnectorStub;

public class LabConnectorServiceClient 
{
	LabConnectorStub labconnectorstub;
	String soapEndPoint = "http://ilabs-test.eng.uts.edu.au:7070/LabConnector/LabConenctor.asmx";
	
	public LabConnectorServiceClient()
	{
		try
		{
			labconnectorstub = new LabConnectorStub(soapEndPoint);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	//We want to create a labConnector service client to connect
	/**
	 * @param args
	 * @throws RemoteException
	 */
	public int submitBatchExperiment(String experimentSpecs, String labID, int priority, String userID)
	{
		try
		{
			//Derived from: http://www.codeweblog.com/axis2-the-entire-process-of-creating-webservice-client-called-net-web-service/
			//Axis2 -- HTTP transport adopted the "chunked" mode, while .NET Web server does not support this  
			labconnectorstub._getServiceClient().getOptions().setProperty(org.apache.axis2.transport.http.HTTPConstants.CHUNKED, Boolean.FALSE);
			
			//Setup the WSDL calling method
			LabConnectorStub.SubmitExperiment submitExpt = new LabConnectorStub.SubmitExperiment();
			
			//Set the parameters for submit call
			submitExpt.setExperimentSpecs(experimentSpecs);
			submitExpt.setLabID(labID);
			submitExpt.setPriority(priority);
			submitExpt.setUserID(userID);
			
			return labconnectorstub.submitExperiment(submitExpt).getExperimentID();
			
			//Setup the WSDL response
			//LabConnectorStub.SubmitExperimentResponse submissionResp = new LabConnectorStub.SubmitExperimentResponse();
			//return submissionResp.getExperimentID();
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return -1;
	}		
}
