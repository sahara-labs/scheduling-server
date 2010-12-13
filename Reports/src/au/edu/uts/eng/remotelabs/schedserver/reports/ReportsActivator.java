package au.edu.uts.eng.remotelabs.schedserver.reports;

import org.apache.axis2.transport.http.AxisServlet;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.server.ServletContainer;
import au.edu.uts.eng.remotelabs.schedserver.server.ServletContainerService;

public class ReportsActivator implements BundleActivator {

    /** Service registration for the Reports SOAP interface. */
    private ServiceRegistration soapReg;
    
    /** Logger. */
    private Logger logger;
    
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
        this.logger = LoggerActivator.getLogger();
	    this.logger.debug("Reports Activator start");
	    
        /* Register the reports service. */
        this.logger.debug("Registering the Reports SOAP interface service.");
        ServletContainerService soapService = new ServletContainerService();
        soapService.addServlet(new ServletContainer(new AxisServlet(), true));
        this.soapReg = context.registerService(ServletContainerService.class.getName(), soapService, null);

	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
	    System.out.println("Reports Activator stop");
	    
       this.logger.info("Shutting down the Reports bundle.");
        this.soapReg.unregister();

	}

}
