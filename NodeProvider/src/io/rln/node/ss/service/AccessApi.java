package io.rln.node.ss.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Queue access API.
 */
public class AccessApi extends ApiBase
{
    private static final long serialVersionUID = -3676979340749696907L;
    
    public static final String PATH = "/access";

    public AccessApi(List<String> hosts)
    {
        super(hosts);
    }

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response)
    {
        /* Put request is assigning access. */
        
    }
    
    public void doDelete(HttpServletRequest request, HttpServletResponse response)
    {
        /* Delete request is removing access. */
    }
}
