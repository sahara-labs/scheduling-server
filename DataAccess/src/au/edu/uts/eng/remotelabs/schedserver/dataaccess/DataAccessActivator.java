/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
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
 * @author Michael Diponio (mdiponio)
 * @date 5th January 2010
 */

package au.edu.uts.eng.remotelabs.schedserver.dataaccess;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.stat.Statistics;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import au.edu.uts.eng.remotelabs.schedserver.config.Config;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.AcademicPermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Bookings;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.MatchingCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.MatchingCapabilitiesId;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RemotePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RemoteSite;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RequestCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RequestablePermissionPeriod;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigLog;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigOfflineSchedule;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigTypeInformation;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigTypeMedia;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserAssociation;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserAssociationId;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClass;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserLock;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.impl.DataAccessConfiguration;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;

/**
 * Data Access bundle activator. The data access bundle is used to load and 
 * persist entity classes into a databases.
 * <br />
 * The activator is used to set up the Hibernate session factory.
 */
public class DataAccessActivator implements BundleActivator 
{
    /** Session factory to obtains sessions from. */
    private static SessionFactory sessionFactory;
    
    /** Configuration service tracker. */
    private static ServiceTracker<Config, Config> confTracker;
    
    /** Logger. */
    private Logger logger;

	@Override
	public void start(BundleContext context) throws Exception 
    {
        this.logger = LoggerActivator.getLogger();
        this.logger.info("Starting the Data Access bundle.");

        /* Configure Hibernate for use with annotations. */
        AnnotationConfiguration cfg = new AnnotationConfiguration();
        cfg.setProperties(new DataAccessConfiguration(context).getProperties());
        cfg.addAnnotatedClass(AcademicPermission.class);
        cfg.addAnnotatedClass(Bookings.class);
        cfg.addAnnotatedClass(au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Config.class);
        cfg.addAnnotatedClass(MatchingCapabilities.class);
        cfg.addAnnotatedClass(MatchingCapabilitiesId.class);
        cfg.addAnnotatedClass(RemotePermission.class);
        cfg.addAnnotatedClass(RequestablePermissionPeriod.class);
        cfg.addAnnotatedClass(RemoteSite.class);
        cfg.addAnnotatedClass(RequestCapabilities.class);
        cfg.addAnnotatedClass(ResourcePermission.class);
        cfg.addAnnotatedClass(Rig.class);
        cfg.addAnnotatedClass(RigCapabilities.class);
        cfg.addAnnotatedClass(RigOfflineSchedule.class);
        cfg.addAnnotatedClass(RigLog.class);
        cfg.addAnnotatedClass(RigType.class);
        cfg.addAnnotatedClass(RigTypeInformation.class);
        cfg.addAnnotatedClass(RigTypeMedia.class);
        cfg.addAnnotatedClass(au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session.class);
        cfg.addAnnotatedClass(User.class);
        cfg.addAnnotatedClass(UserAssociation.class);
        cfg.addAnnotatedClass(UserAssociationId.class);
        cfg.addAnnotatedClass(UserClass.class);
        cfg.addAnnotatedClass(UserLock.class);
        this.logger.debug("Hibernate properties: " + cfg.getProperties().toString());

        DataAccessActivator.sessionFactory = cfg.buildSessionFactory();
        if (DataAccessActivator.sessionFactory == null)
        {
            this.logger.fatal("Unable to open a session factory to the database. Ensure the database is running " +
            		"and restart the scheduling server.");
        }
        
        DataAccessActivator.confTracker = new ServiceTracker<Config, Config>(context, Config.class, null);
    }
	
	@Override
	public void stop(BundleContext context) throws Exception 
	{
        this.logger.debug("Shutting down the Data Access bundle.");

        Statistics stats = DataAccessActivator.sessionFactory.getStatistics();
        this.logger.info("Hibernate statistics: " + stats.toString());

        synchronized (DataAccessActivator.sessionFactory)
        {
            DataAccessActivator.sessionFactory.close();
            DataAccessActivator.sessionFactory = null;
        }
	}

    /**
     * Returns a Hibernate session. Returns null if a session cannot be
     * opened.
     * 
     * @return session or null
     */
    public static Session getNewSession()
    {
        synchronized (DataAccessActivator.sessionFactory)
        {
            if (sessionFactory == null)
            {
                return null;
            }

            return DataAccessActivator.sessionFactory.openSession();
        }
    }
    
    public static StatelessSession getNewStatelessSession()
    {
        synchronized (DataAccessActivator.sessionFactory)
        {
            if (sessionFactory == null)
            {
                return null;
            }

            return DataAccessActivator.sessionFactory.openStatelessSession();
        }
    }

    /**
     * Gets the specified properties value provided this bundle is loaded and 
     * the configuration service is registered. If not, the provided default
     * value is returned.
     * 
     * @param prop property whose value to provide
     * @param def default value
     * @return properties value or default value if this or the configuration \
     *         bundle not loaded
     */
    public static String getProperty(String prop, String def)
    {
        au.edu.uts.eng.remotelabs.schedserver.config.Config conf;
        if (DataAccessActivator.confTracker == null || (conf = DataAccessActivator.confTracker.getService()) == null)
        {
            return def;
        }
        
        return conf.getProperty(prop, def);
    }
}
