package au.edu.labshare.schedserver.scormpackager.service;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;

import au.edu.labshare.schedserver.scormpackager.SaharaRigMedia;
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
import au.edu.labshare.schedserver.scormpackager.utilities.ScormUtilities;
import au.edu.labshare.schedserver.scormpackager.lila.ManifestXMLDecorator;
import au.edu.labshare.schedserver.scormpackager.lila.ShareableContentObjectCreator;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigTypeDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigTypeMedia;
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
		au.edu.labshare.schedserver.scormpackager.types.CreateSCO SCOInfo = new au.edu.labshare.schedserver.scormpackager.types.CreateSCO();
		SCOInfo.setContent(createPIF.getContent());
		SCOInfo.setExperimentName(createPIF.getExperimentName());
		CreateSCOResponse SCOResponse = createSCO(SCOInfo);
		
		//Setup the response with the data to return back to the user
		CreatePIFResponse createPIFResponse = new CreatePIFResponse();
		createPIFResponse.setPathPIF(SCOResponse.getPathSCO());
		
		return createPIFResponse;
	}

	@Override
	public CreateSCOResponse createSCO(CreateSCO createSCO) 
	{
		String pathOfSCO = null;
		
		LinkedList<File> content = new LinkedList<File>();
		
		//Start by adding extra content that was provided by user. Will not set if it is null
		if(createSCO.getContent() != null)
			content = ScormUtilities.getFilesFromPath(createSCO.getContent());
	
		//Add the lmsstub.js
		content.add(new File(ManifestXMLDecorator.RESOURCES_PATH + "/lmsstub.js"));
		
		//We want to get the content from the Rig DB Persistence end
        org.hibernate.Session db = new RigTypeDao().getSession();
        SaharaRigMedia saharaRigMedia = new SaharaRigMedia(db);
        
        //Go through the rig media information and add any data that is in them
        Iterator<RigTypeMedia> iter = saharaRigMedia.getRigType(createSCO.getExperimentName()).getMedia().iterator();
        
        while(iter.hasNext())
        	content.add(new File(iter.next().getFileName()));
		
		//Create the PIF to be sent out
		ShareableContentObjectCreator shrContentObj = new ShareableContentObjectCreator(logger);
		pathOfSCO = shrContentObj.createSCO(createSCO.getExperimentName(), content, ShareableContentObjectCreator.OUTPUT_PATH);
		
		//Setup the response with the data to return back to the user
		CreateSCOResponse createPIFResponse = new CreateSCOResponse();
		createPIFResponse.setPathSCO(pathOfSCO);
		
		return createPIFResponse;
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
