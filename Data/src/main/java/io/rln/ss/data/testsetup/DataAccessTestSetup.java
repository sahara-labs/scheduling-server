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
 * @date 31st May 2009
 */
package io.rln.ss.data.testsetup;

import java.lang.reflect.Field;
import java.util.Properties;

import org.hibernate.cfg.AnnotationConfiguration;

import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.logger.impl.SystemErrLogger;
import io.rln.ss.data.DataActivator;
import io.rln.ss.data.entities.AcademicPermission;
import io.rln.ss.data.entities.Bookings;
import io.rln.ss.data.entities.Collection;
import io.rln.ss.data.entities.Config;
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

/**
 * Sets up the database for testing.
 */
public class DataAccessTestSetup
{
    /** Whether the database is setup. */
    public static boolean isSetup = false;
    
    /** JDBC string. */
    public static String jdbc = "jdbc:mysql://127.0.0.1:3306/sahara";
    
    /** JDBC driver. */
    public static String driver = "com.mysql.jdbc.Driver";
    
    /** Hibernate dialect. */
    public static String dialect = "org.hibernate.dialect.MySQLInnoDBDialect";
    
    /** Database username. */
    public static String username = "sahara";
    
    /** Database password. */
    public static String password = "saharapasswd";
    
    public static void setup() throws Exception
    {
        if (!DataAccessTestSetup.isSetup)
        {
            /* Set up the logger. */
            Field f = LoggerActivator.class.getDeclaredField("logger");
            f.setAccessible(true);
            f.set(null, new SystemErrLogger());
            
            /* Set up the SessionFactory. */
            AnnotationConfiguration cfg = new AnnotationConfiguration();
            Properties props = new Properties();
            props.setProperty("hibernate.connection.driver_class", DataAccessTestSetup.driver);
            props.setProperty("hibernate.connection.url", DataAccessTestSetup.jdbc);
            props.setProperty("hibernate.dialect", DataAccessTestSetup.dialect);
            props.setProperty("hibernate.connection.username", DataAccessTestSetup.username);
            props.setProperty("hibernate.connection.password", DataAccessTestSetup.password);
            props.setProperty("hibernate.show_sql", "true");
            props.setProperty("hibernate.format_sql", "true");
            props.setProperty("hibernate.use_sql_comments", "true");
            props.setProperty("hibernate.generate_statistics", "true");
            cfg.setProperties(props);
            cfg.addAnnotatedClass(AcademicPermission.class);
            cfg.addAnnotatedClass(Bookings.class);
            cfg.addAnnotatedClass(Config.class);
            cfg.addAnnotatedClass(Collection.class);
            cfg.addAnnotatedClass(MatchingCapabilities.class);
            cfg.addAnnotatedClass(MatchingCapabilitiesId.class);
            cfg.addAnnotatedClass(Project.class);
            cfg.addAnnotatedClass(ProjectMetadata.class);
            cfg.addAnnotatedClass(ProjectMetadataType.class);
            cfg.addAnnotatedClass(RemotePermission.class);
            cfg.addAnnotatedClass(RemotePermissionLog.class);
            cfg.addAnnotatedClass(RemoteSite.class);
            cfg.addAnnotatedClass(RequestCapabilities.class);
            cfg.addAnnotatedClass(ResourcePermission.class);
            cfg.addAnnotatedClass(RequestablePermissionPeriod.class);
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

            f = DataActivator.class.getDeclaredField("sessionFactory");
            f.setAccessible(true);
            f.set(null, cfg.buildSessionFactory());
        }
    }
}
