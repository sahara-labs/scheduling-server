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

package io.rln.ss.data;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.stat.Statistics;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

import au.edu.uts.eng.remotelabs.schedserver.config.Config;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import io.rln.ss.data.entities.AcademicPermission;
import io.rln.ss.data.entities.Bookings;
import io.rln.ss.data.entities.Collection;
import io.rln.ss.data.entities.MatchingCapabilities;
import io.rln.ss.data.entities.MatchingCapabilitiesId;
import io.rln.ss.data.entities.Project;
import io.rln.ss.data.entities.ProjectMetadata;
import io.rln.ss.data.entities.ProjectMetadataType;
import io.rln.ss.data.entities.RemotePermission;
import io.rln.ss.data.entities.RemotePermissionLog;
import io.rln.ss.data.entities.RemoteSite;
import io.rln.ss.data.entities.RequestCapabilities;
import io.rln.ss.data.entities.RequestablePermissionPeriod;
import io.rln.ss.data.entities.ResourcePermission;
import io.rln.ss.data.entities.Rig;
import io.rln.ss.data.entities.RigCapabilities;
import io.rln.ss.data.entities.RigLog;
import io.rln.ss.data.entities.RigOfflineSchedule;
import io.rln.ss.data.entities.RigType;
import io.rln.ss.data.entities.SessionFile;
import io.rln.ss.data.entities.ShibbolethUsersMap;
import io.rln.ss.data.entities.User;
import io.rln.ss.data.entities.UserAssociation;
import io.rln.ss.data.entities.UserAssociationId;
import io.rln.ss.data.entities.UserClass;
import io.rln.ss.data.entities.UserClassKey;
import io.rln.ss.data.entities.UserClassKeyConstraint;
import io.rln.ss.data.entities.UserClassKeyRedemption;
import io.rln.ss.data.entities.UserLock;
import io.rln.ss.data.impl.DataAccessConfiguration;

/**
 * Data Access bundle activator. The data access bundle is used to load and 
 * persist entity classes into a databases.
 * <br />
 * The activator is used to set up the Hibernate session factory.
 */
public class DataActivator implements BundleActivator 
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
        cfg.addAnnotatedClass(Collection.class);
        cfg.addAnnotatedClass(io.rln.ss.data.entities.Config.class);
        cfg.addAnnotatedClass(MatchingCapabilities.class);
        cfg.addAnnotatedClass(MatchingCapabilitiesId.class);
        cfg.addAnnotatedClass(Project.class);
        cfg.addAnnotatedClass(ProjectMetadata.class);
        cfg.addAnnotatedClass(ProjectMetadataType.class);
        cfg.addAnnotatedClass(RemotePermission.class);
        cfg.addAnnotatedClass(RemotePermissionLog.class);
        cfg.addAnnotatedClass(RequestablePermissionPeriod.class);
        cfg.addAnnotatedClass(RemoteSite.class);
        cfg.addAnnotatedClass(RequestCapabilities.class);
        cfg.addAnnotatedClass(ResourcePermission.class);
        cfg.addAnnotatedClass(Rig.class);
        cfg.addAnnotatedClass(RigCapabilities.class);
        cfg.addAnnotatedClass(RigOfflineSchedule.class);
        cfg.addAnnotatedClass(RigLog.class);
        cfg.addAnnotatedClass(RigType.class);
        cfg.addAnnotatedClass(io.rln.ss.data.entities.Session.class);
        cfg.addAnnotatedClass(SessionFile.class);
        cfg.addAnnotatedClass(ShibbolethUsersMap.class);
        cfg.addAnnotatedClass(User.class);
        cfg.addAnnotatedClass(UserAssociation.class);
        cfg.addAnnotatedClass(UserAssociationId.class);
        cfg.addAnnotatedClass(UserClass.class);
        cfg.addAnnotatedClass(UserClassKey.class);
        cfg.addAnnotatedClass(UserClassKeyConstraint.class);
        cfg.addAnnotatedClass(UserClassKeyRedemption.class);
        cfg.addAnnotatedClass(UserLock.class);
        this.logger.debug("Hibernate properties: " + cfg.getProperties().toString());

        DataActivator.sessionFactory = cfg.buildSessionFactory();
        if (DataActivator.sessionFactory == null)
        {
            this.logger.fatal("Unable to open a session factory to the database. Ensure the database is running " +
            		"and restart the scheduling server.");
        }
        
        DataActivator.confTracker = new ServiceTracker<Config, Config>(context, Config.class, null);
    }
	
	@Override
	public void stop(BundleContext context) throws Exception 
	{
        this.logger.debug("Shutting down the Data Access bundle.");

        Statistics stats = DataActivator.sessionFactory.getStatistics();
        this.logger.info("Hibernate statistics: " + stats.toString());

        synchronized (DataActivator.sessionFactory)
        {
            DataActivator.sessionFactory.close();
            DataActivator.sessionFactory = null;
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
        synchronized (DataActivator.sessionFactory)
        {
            if (sessionFactory == null)
            {
                return null;
            }

            return DataActivator.sessionFactory.openSession();
        }
    }
    
    public static StatelessSession getNewStatelessSession()
    {
        synchronized (DataActivator.sessionFactory)
        {
            if (sessionFactory == null)
            {
                return null;
            }

            return DataActivator.sessionFactory.openStatelessSession();
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
        if (DataActivator.confTracker == null || (conf = DataActivator.confTracker.getService()) == null)
        {
            return def;
        }
        
        return conf.getProperty(prop, def);
    }
}
