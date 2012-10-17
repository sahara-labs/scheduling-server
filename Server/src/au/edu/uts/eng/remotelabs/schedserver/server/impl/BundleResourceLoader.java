/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2012, University of Technology, Sydney
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
 * @author Michael Diponio (mdiponio)
 * @date 17th October 2012
 */
package au.edu.uts.eng.remotelabs.schedserver.server.impl;

import java.io.InputStream;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.resource.Resource;
import org.apache.velocity.runtime.resource.loader.ResourceLoader;

import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;

/**
 * Resource loader which looks through the classloaders of the bundles
 * that are registered to host a web page to load template files.
 */
public class BundleResourceLoader extends ResourceLoader 
{
	/** Logger. */
	private final Logger logger;
	
	public BundleResourceLoader()
	{
		this.logger = LoggerActivator.getLogger();
	}
	
	@Override
	public void init(ExtendedProperties props) 
	{
		this.logger.error("Initialising velocity bundle resource loader.");
	}

	@Override
	public InputStream getResourceStream(String path) throws ResourceNotFoundException 
	{
		/* Check this bundles classloader for resource. */
		InputStream is = BundleResourceLoader.class.getResourceAsStream(path);
        
        if (is == null)
        {
        	/* Resource not found in this classloader, search through all registered
        	 * bundle classloaders to find the path. */
            for (ClassLoader cl : PageHostingServiceListener.getClassLoaders())
            {
                is = cl.getResourceAsStream(path);
                if (is != null) break;
            }
        }
        
        if (is == null)
        {
        	this.logger.warn("Velocity resource " + path + " not found.");
        	throw new ResourceNotFoundException("Velocity resource " + path + " not found.");
        }
        
        return is;
	}


	@Override
	public boolean isSourceModified(Resource arg0) 
	{
		/* Resources are not modified dynamically. */
		return false;
	}

	@Override
	public long getLastModified(Resource resource) 
	{
		/* Resources are not modified dynamically. */
		return 0;
	}
}
