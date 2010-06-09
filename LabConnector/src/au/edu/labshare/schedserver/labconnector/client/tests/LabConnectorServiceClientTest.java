package au.edu.labshare.schedserver.labconnector.client.tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import au.edu.labshare.schedserver.labconnector.client.LabConnectorServiceClient;
import au.edu.labshare.schedserver.labconnector.client.LabConnectorStub.SubmitExperimentResponse;

public class LabConnectorServiceClientTest
{
    // private final String PATH_TIMEOFDAY_XML_SPECS =
    // "lib/LabConfigurationBatchTimeOfDay.xml";

    int priority;
    String experimentSpecs, labID, userID;

    @Before
    public void setUp() throws Exception
    {
        // Setup for what iLabs expects - this is based on LabConnectorTests.cs
        // in VS.NET
        experimentSpecs = "<experimentSpecification><setupId id=\"LocalClock\">LocalClock</setupId><formatName>12-Hour</formatName></experimentSpecification>";
        userID = "17";
        labID = "54FE7B0C423D41C1B06D8F189DFBED76";
    }

    @After
    public void tearDown() throws Exception
    {
    }

    @Test
    public void testSubmitCall()
    {
        //int experimentID = 0;

        // Test the submit call
        LabConnectorServiceClient labConnectorServiceClient;
        labConnectorServiceClient = new LabConnectorServiceClient();

        SubmitExperimentResponse  submitResp = labConnectorServiceClient.submitBatchExperiment(
                experimentSpecs, labID, priority, userID);

        assertNotNull(submitResp);
        //assertTrue(experimentID > 0);
        // fail("Not yet implemented");
    }

}
