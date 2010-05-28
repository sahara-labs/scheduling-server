package au.edu.labshare.schedserver.labconnector.service.tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import au.edu.labshare.schedserver.labconnector.service.LabConnector; 
import au.edu.labshare.schedserver.labconnector.service.types.SubmitExperiment;
import au.edu.labshare.schedserver.labconnector.service.types.SubmitExperimentResponse;

public class LabConnectorServiceTest
{
        private LabConnector     labConnectorService;
        private SubmitExperiment submitExpt;
    
        //private final String PATH_TIMEOFDAY_XML_SPECS = "lib/LabConfigurationBatchTimeOfDay.xml";	
	int    priority;
	String experimentSpecs, labID, userID;
	
	@Before
	public void setUp() throws Exception 
	{
		//Setup for what iLabs expects - this is based on LabConnectorTests.cs in VS.NET
		this.experimentSpecs = "<experimentSpecification><setupId id=\"LocalClock\">LocalClock</setupId><formatName>12-Hour</formatName></experimentSpecification>"; 
		this.userID = "17";
		this.labID = "54FE7B0C423D41C1B06D8F189DFBED76";
		
		//Test the labConnector Service
		this.labConnectorService = new LabConnector();
		
		//
		this.submitExpt = new SubmitExperiment();
	        this.submitExpt.setExperimentSpecs(this.experimentSpecs);
	        this.submitExpt.setLabID(this.labID);
	        this.submitExpt.setPriority(0);
	        this.submitExpt.setUserID(this.userID);
	}

	@After
	public void tearDown() throws Exception 
	{
	}

	@Test
	public void testSubmitCall() 
	{
	    SubmitExperimentResponse submitExptResponse;
	    
	    //SubmitExperiment submitExperiment = new SubmitExperiment();
	    submitExptResponse = this.labConnectorService.submitExperiment(this.submitExpt);
	    assertTrue(submitExptResponse.getExperimentID() > 0);
	}
}
