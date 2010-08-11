package au.edu.labshare.schedserver.scormpackager;


//Packages needed for reading properties file
import java.util.Properties;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import au.edu.uts.eng.remotelabs.schedserver.config.Config;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;

public class ScormPackagerProperties 
{
    /** ScormPackager properties configuration file location. */
    public static String SCORMPACKAGER_SCO_PATH           = "scormpackager_scopath";
    public static String SCORMPACKAGER_EXPERIMENT_TITLE   = "scormpackager_scotitle";

    /** ScormPackager configuration. */
    private Config       scormPackagerConfig;
    private Logger       logger;
    private final Properties   scormPackagerProperties;

    public ScormPackagerProperties(BundleContext context)
    {        
        /* 
         * Load the default properties
         */
        this();
        
        /*
         * Log anything that goes wrong        
         */
        this.logger = LoggerActivator.getLogger();
        
        ServiceReference ref = context.getServiceReference(Config.class.getName());
        this.scormPackagerConfig = (Config)context.getService(ref);
        
        /*
         * Get the properties from either SchedServer/trunk/ScormPackager/scormpackager.properties file
         * or from default values
         */
        loadConfiguration();
    }
    

    public ScormPackagerProperties()
    {
        this.scormPackagerProperties = new Properties();
        populateDefaults();
    }
    
    /**
     * Loads the configuration properties. The following list contains
     * the configuration keys and expected data types:
     * <ul>
     *  <li>scormpackager_scopath - String - The storage path for the SCOs.</li>
     *  <li>scormpackager_scotitle - String - The title used if none is provided for the SCO.</li>
     * </ul>
     */
    private void loadConfiguration()
    { 
        String titleProperty = this.scormPackagerConfig.getProperty(ScormPackagerProperties.SCORMPACKAGER_EXPERIMENT_TITLE);
        if (titleProperty == null)
        {
            this.logger.warn("ScormPackager Title configuration property not set(scormpackager_scotitle) not found," +
                    " using the default (" + this.scormPackagerProperties.getProperty(ScormPackagerProperties.SCORMPACKAGER_EXPERIMENT_TITLE) + ").");
        }
        else
        {
            this.logger.info("ScormPackager SCO Title configuration property as " + titleProperty + ".");
        }
        
        String pathProperty = this.scormPackagerConfig.getProperty(ScormPackagerProperties.SCORMPACKAGER_SCO_PATH);
        if (titleProperty == null)
        {
            this.logger.warn("ScormPackager SCO Storage Path configuration property not set(scormpackager_scopath) not found," +
                    " using the default (" + this.scormPackagerProperties.getProperty(ScormPackagerProperties.SCORMPACKAGER_SCO_PATH) + ").");
        }
        else
        {
            this.logger.info("ScormPackager SCO Storage Path configuration property as " + pathProperty + ".");
        }
        
    }
    
    private void populateDefaults()
    {        
        /* ScormPackager specific defaults. */
        this.scormPackagerProperties.setProperty(ScormPackagerProperties.SCORMPACKAGER_SCO_PATH, "/SCOs/");
        this.scormPackagerProperties.setProperty(ScormPackagerProperties.SCORMPACKAGER_EXPERIMENT_TITLE, "Generic Title");
    }
	
}
