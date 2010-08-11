package au.edu.labshare.schedserver.scormpackager;

import org.apache.axis2.transport.http.AxisServlet;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

//Needed for Sahara
import au.edu.labshare.schedserver.scormpackager.ScormPackagerProperties;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.server.ServletContainer;
import au.edu.uts.eng.remotelabs.schedserver.server.ServletContainerService;

public class ScormPackagerActivator implements BundleActivator
{
	/** Servlet container service registration. */
    private ServiceRegistration serverReg;
    private Logger		logger;
    
    /*
     * (non-Javadoc)
     * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
     */
     @Override
     public void start(BundleContext context) throws Exception 
     {   
         /*Initialise the properties for the configuration*/
         this.logger = LoggerActivator.getLogger();
         new ScormPackagerProperties(context); 
        
         /* Service to host the LabConnector interface. */
		 ServletContainerService service = new ServletContainerService();
		 service.addServlet(new ServletContainer(new AxisServlet(), true));
		 this.serverReg = context.registerService(ServletContainerService.class.getName(), service, null);
     }

     /*
      * (non-Javadoc)
      * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
      */
      @Override
      public void stop(BundleContext context) throws Exception 
      {        
          /* Stop the servlet container service and log it */
          this.logger.info("Stopping " + context.getBundle().getSymbolicName() + " bundle.");
          this.serverReg.unregister();
      }

}
