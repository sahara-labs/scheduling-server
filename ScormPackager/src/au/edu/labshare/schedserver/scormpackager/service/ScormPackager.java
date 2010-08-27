package au.edu.labshare.schedserver.scormpackager.service;

import au.edu.labshare.schedserver.scormpackager.types.CreatePIF;
import au.edu.labshare.schedserver.scormpackager.types.CreatePIFResponse;
import au.edu.labshare.schedserver.scormpackager.types.CreateSCO;
import au.edu.labshare.schedserver.scormpackager.types.CreateSCOResponse;
import au.edu.labshare.schedserver.scormpackager.types.DeletePIF;
import au.edu.labshare.schedserver.scormpackager.types.DeletePIFResponse;
import au.edu.labshare.schedserver.scormpackager.types.DeleteSCO;
import au.edu.labshare.schedserver.scormpackager.types.DeleteSCOResponse;
import au.edu.labshare.schedserver.scormpackager.types.ValidateManifest;
import au.edu.labshare.schedserver.scormpackager.types.ValidateManifestResponse;
import au.edu.labshare.schedserver.scormpackager.types.ValidatePIF;
import au.edu.labshare.schedserver.scormpackager.types.ValidatePIFResponse;
import au.edu.labshare.schedserver.scormpackager.types.ValidateSCO;
import au.edu.labshare.schedserver.scormpackager.types.ValidateSCOResponse;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;

public class ScormPackager implements ScormPackagerSkeletonInterface
{
	 /** Logger. */
    private Logger logger;

    public ScormPackager()
    {
        this.logger = LoggerActivator.getLogger();
    }

	@Override
	public CreatePIFResponse createPIF(CreatePIF createPIF) 
	{
		createPIF.getContent();
		createPIF.getExperimentName();
		
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CreateSCOResponse createSCO(CreateSCO createSCO) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeletePIFResponse deletePIF(DeletePIF deletePIF) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeleteSCOResponse deleteSCO(DeleteSCO deleteSCO) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValidateManifestResponse validateManifest(ValidateManifest validateManifest) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValidatePIFResponse validatePIF(ValidatePIF validatePIF) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ValidateSCOResponse validateSCO(ValidateSCO validateSCO) 
	{
		// TODO Auto-generated method stub
		return null;
	}

}
