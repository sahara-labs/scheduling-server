package au.edu.uts.eng.remotelabs.schedserver.reports;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class ReportsActivator implements BundleActivator {

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
	    System.out.println("Reports Activator start");
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
	    System.out.println("Reports Activator stop");
	}

}
