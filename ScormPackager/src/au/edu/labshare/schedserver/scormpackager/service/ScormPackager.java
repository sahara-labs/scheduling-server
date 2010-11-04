/**
 * SAHARA Scheduling Server
 *
 * ScormPackager Web Service 
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
 * @author Herber Yeung
 * @date 4th November 2010
 */
package au.edu.labshare.schedserver.scormpackager.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;

import au.edu.labshare.schedserver.scormpackager.sahara.RigLaunchPageCreator;
import au.edu.labshare.schedserver.scormpackager.sahara.RigMedia;
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

//import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigTypeDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigTypeMedia;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;



public class ScormPackager implements ScormPackagerSkeletonInterface
{
	 /** Logger. */
    private Logger logger;
    //private org.hibernate.Session session;

    public ScormPackager()
    {
        this.logger = LoggerActivator.getLogger();
        //this.session = DataAccessActivator.getNewSession();
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
        Properties defaultProps = new Properties();
        //FileInputStream in = null;
        InputStream in = null;
		
        //LinkedList<String> rigSCOResources = new LinkedList<String>();
		LinkedList<File> content = new LinkedList<File>();
		
		//Start by adding extra content that was provided by user. Will not set if it is null
		if(createSCO.getContent() != null)
			content = ScormUtilities.getFilesFromPath(createSCO.getContent());
	
		//We want to get the content from the Rig DB Persistence end
        org.hibernate.Session db = new RigTypeDao().getSession();
        RigMedia saharaRigMedia = new RigMedia(db);
        
        //Go through the rig media information and add any data that is in them
        Iterator<RigTypeMedia> iter;
        if(saharaRigMedia.getRigType(createSCO.getExperimentName()) != null)
        	iter = saharaRigMedia.getRigType(createSCO.getExperimentName()).getMedia().iterator();
        else 
        {
        	CreateSCOResponse errorSCOResponse = new CreateSCOResponse();
        	errorSCOResponse.setPathSCO("NON EXISTENT RIGTYPE - SCORM WEB SERVICE ERROR"); //TODO: Place this as a status code static string
        	return errorSCOResponse;
        }
        
        while(iter.hasNext())
        	content.add(new File(iter.next().getFileName()));
        
        // We want to generate a LaunchPage (launchpage.html) that is to be added to SCO
        RigLaunchPageCreator rigLaunchPageCreator = new RigLaunchPageCreator();

        // create and load default properties
		try 
		{
			in = this.getClass().getClassLoader().getResourceAsStream("resources/scormpackager.properties"); //TODO: Should place this as a static string*/
						
			defaultProps.load(in);
	        in.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace(); //TODO: Need to replace with Sahara Logger
		}

		//TODO: Should place this as static string - scormpackager_output_path
		pathOfSCO = (String) defaultProps.getProperty("scormpackager_output_path");

		//Add the lmsstub.js
		//TODO: Should replace this with a more elegant solution: Bit of a hack
		//TODO: Maybe like Spring DM or ClassLoader. Preferably, Spring DM as it offers more options.
		try
		{
			int len;
			byte buf[]=new byte[1024];
			
			in = this.getClass().getClassLoader().getResourceAsStream(ManifestXMLDecorator.RESOURCES_PATH + "/lmsstub.js");

			File tmpFile = new File(pathOfSCO + "lmsstub.js");
			FileOutputStream fileOutStream = new FileOutputStream(tmpFile);

		    while((len=in.read(buf))>0)
		    	fileOutStream.write(buf,0,len);

		    fileOutStream.close();

		    content.add(tmpFile);
		}
		catch (Exception e)
		{
			e.printStackTrace(); //TODO: Need to replace with Sahara Logger
		}
		
		//Add the content - i.e. Add launchPage with Experiment/Rig name
		if(ShareableContentObjectCreator.VERSION_NUM <= 1) // According to email correspondence with LiLa currently only supports applet.html
			rigLaunchPageCreator.setOutputPath(pathOfSCO + "applet.html");
		else
			rigLaunchPageCreator.setOutputPath(pathOfSCO + ScormUtilities.replaceWhiteSpace(createSCO.getExperimentName(),"_") + ".html");
        
        content.add(new File(rigLaunchPageCreator.createLaunchPage(createSCO.getExperimentName(), db)));
        
		//Create the SCO to be sent out
		ShareableContentObjectCreator shrContentObj = new ShareableContentObjectCreator(logger);
		shrContentObj.createSCO(createSCO.getExperimentName(), content, pathOfSCO);
		
		//Setup the response with the data to return back to the user
		CreateSCOResponse createSCOResponse = new CreateSCOResponse();
		createSCOResponse.setPathSCO(pathOfSCO);
		
		return createSCOResponse;
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
