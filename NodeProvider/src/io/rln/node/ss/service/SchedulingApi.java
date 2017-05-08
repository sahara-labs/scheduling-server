/*
 * VAS / RLN project.
 *
 * @author Michael Diponio <michael.diponio@uts.edu.au>
 * @date 8th February 2017
 */

package io.rln.node.ss.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.rln.node.ss.NodeProviderActivator;

/**
 * API for Scheduling managing operations.
 */
public class SchedulingApi extends ApiBase
{
    private static final long serialVersionUID = 1L;
    
    /** Path this API is accessible on. */
    public static final String PATH = "/scheduling";
    
    public SchedulingApi(List<String> hosts)
    {
        super(hosts);
    }

    /**
     * The HEAD method reloads the booking engine.
     */
    @Override
    public void doHead(HttpServletRequest req, HttpServletResponse resp)
    {
        this.logger.info("Reloading booking engine.");
        NodeProviderActivator.getBookingEngine().reloadEngine();
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
