/*
 * VAS / RLN project.
 *
 * @author Michael Diponio <michael.diponio@uts.edu.au>
 * @date 3rd August 2016
 */

package io.rln.node.ss.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    
    /** Date formatter. */
    private final SimpleDateFormat isoDateFormat;
    
    /** Logger. */
    protected final Logger logger;
    
    public ApiBase(List<String> hosts)
    {
        this.authorizedHosts = hosts;   
        this.logger = LoggerActivator.getLogger();
        
        this.isoDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
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
    
    /**
     * Encodes a Java object as JSON.
     * 
     * @param o object to encode
     * @return encoded JSON
     * @throws JsonProcessingException error encoding
     */
    protected String getJson(Object o) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(o);
    }
    
    
    /**
     * Parse date into a calendar.
     * 
     * @param str date string in ISO format.
     * @return calendar or null if invalid.
     */
    protected Calendar parseDate(String str)
    {
        if (str == null) return null;
        
        try
        {
            Date date = this.isoDateFormat.parse(str);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            return cal;
        }
        catch (ParseException e)
        {
            this.logger.debug("Invalid ISO date string: " + str);
            return null;
        }
    }

}
