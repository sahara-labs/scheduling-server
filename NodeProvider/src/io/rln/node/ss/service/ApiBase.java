/*
 * VAS / RLN project.
 *
 * @author Michael Diponio <michael.diponio@uts.edu.au>
 * @date 3rd August 2016
 */

package io.rln.node.ss.service;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;

/**
 * Base class of API implementing classes.
 */
public class ApiBase extends HttpServlet
{
    private static final long serialVersionUID = -5797772302649685484L;

    /** The list of hosts that are allowed to access machines. */
    private final List<String> authorizedHosts;
    
    /** Logger. */
    protected final Logger logger;
    
    public ApiBase(List<String> hosts)
    {
        this.authorizedHosts = hosts;   
        this.logger = LoggerActivator.getLogger();
    }
    
    @Override
    public void service(HttpServletRequest request , HttpServletResponse response) throws ServletException, IOException
    {
        if (this.authorizedHosts.contains(request.getRemoteHost()))
        {
            super.service(request, response);
        }
        else
        {
            this.logger.warn("Not allowing remote host " + request.getRemoteHost() + " to call node API operations.");
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
