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
 * @date 6th January 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.dataaccess.impl;

import java.util.Properties;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import au.edu.uts.eng.remotelabs.schedserver.config.Config;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;


/**
 * Hibernate configuration.
 */
public class DataAccessConfiguration
{    
    /* Hibernate configuration properties. */
    private final Properties props;
    
    /** Configuration. */
    private Config config;
    
    /** Logger. */
    private Logger logger;
    
    public DataAccessConfiguration(BundleContext context)
    {
        this.logger = LoggerActivator.getLogger();
        this.props = new Properties();
        
        ServiceReference ref = context.getServiceReference(Config.class.getName());
        this.config = (Config)context.getService(ref);
    }
    
    /**
     * Returns the Hibernate configuration properties.
     * 
     * @return configuration properties
     */
    public Properties getProperties()
    {
        this.populateDefaults();
        this.loadConfiguration();
        return this.props;
    }
    
    /**
     * Sets the default hibernate configuration properties. The following list 
     * contains the default configuration key - value pairs:
     * <ul>
     *  <li>Database JDBC driver: MySQL
     *  <tt>(hibernate.connection.driver_class=com.mysql.jdbc.Driver)</tt>
     *  <li>Connection URL: Local MySQL database called 'sahara'
     *  <tt>(hibernate.connection.url=jdbc:mysql://localhost/sahara)</tt></li>
     *  <li>Database dialect: MySQL InnoDB
     *  <tt>(hibernate.dialect=org.hibernate.dialect.MySQLInnoDBDialect)</tt></li>
     *  <li>Database user name: sahara
     *  <tt>(hibernate.connection.username=sahara)</tt></li>
     *  <li>Database password: saharapasswd
     *  <tt>(hibernate.connection.password=saharapasswd)</tt></li>
     *  
     *  <li>Minimum connection pool size: 3
     *  <tt>(hibernate.c3p0.min_size=3)</tt></li>
     *  <li>Maximum connection pool size: 50
     *  <tt>(hibernate.c3p0.max_size=50)</tt></li>
     *  <li>Connection timeout: 600
     *  <tt>(hibernate.c3p0.timeout=600)</tt></li>
     */
    private void populateDefaults()
    {
        /* Database specific defaults. */
        this.props.setProperty("hibernate.connection.driver_class", "com.mysql.jdbc.Driver");
        this.props.setProperty("hibernate.connection.url", "jdbc:mysql://127.0.0.1:3306/sahara");
        this.props.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQLInnoDBDialect");
        this.props.setProperty("hibernate.connection.username", "sahara");
        this.props.setProperty("hibernate.connection.password", "saharapasswd");
        
        /* hibernate.c3p0 connection pool defaults. */
        this.props.setProperty("hibernate.c3p0.min_size", "3");
        this.props.setProperty("hibernate.c3p0.max_size", "50");
        this.props.setProperty("hibernate.c3p0.timeout", "600");
        
        /* Property to enable c3p0 connection pooling. */
        this.props.setProperty("hibernate.connection.provider_class", 
                "org.hibernate.connection.C3P0ConnectionProvider");
        
        // TODO Remove SQL debug statements.
        this.props.setProperty("hibernate.show_sql", "false");
        this.props.setProperty("hibernate.format_sql", "false");
        this.props.setProperty("hibernate.use_sql_comments", "false");
        this.props.setProperty("hibernate.generate_statistics", "false");
    }
    
    /**
     * Loads the configuration properties. The following list contains
     * the configuration keys and expected data types:
     * <ul>
     *  <li>DB_Driver_Class - String - JDBC driver class.</li>
     *  <li>DB_Connection_URL - String - JDBC connection URL.</li>
     *  <li>DB_Database_Dialect - String - Hibernate database dialect.</li>
     *  <li>DB_Username - String - Connection user name.</li>
     *  <li>DB_Password - String - Connection password.</li>
     *  <li>DB_Conn_Pool_Min_Size - Connection pool minimum size.</li>
     *  <li>DB_Conn_Pool_Max_Size - Connection pool maximum size.</li>
     *  <li>DB_Conn_Timeout - Connection timeout.</li>
     * </ul>
     */
    private void loadConfiguration()
    {
        String prop = this.config.getProperty("DB_Database_Driver");
        if (prop == null)
        {
            this.logger.warn("Database driver configuration property (DB_Database_Driver) not found," +
                    " using the default (" + this.props.getProperty("hibernate.connection.driver_class") + ").");
        }
        else
        {
            this.logger.info("Loaded database driver name as " + prop + ".");
            this.props.setProperty("hibernate.connection.driver_class", prop);
        }
        
        prop = this.config.getProperty("DB_Connection_URL");
        if (prop == null)
        {
            this.logger.warn("Database connection url property (DB_Connection_URL) not found," +
                    " using the default (" + this.props.getProperty("hibernate.connection.url") + ").");
        }
        else
        {
            this.logger.info("Loaded database connection URL as " + prop + ".");
            this.props.setProperty("hibernate.connection.url", prop);
        }
        
        prop = this.config.getProperty("DB_Database_Dialect");
        if (prop == null)
        {
            this.logger.warn("Database dialect property (DB_Database_Dialect) not found," +
                    " using the default (" + this.props.getProperty("hibernate.dialect") + ").");
        }
        else
        {
            this.logger.info("Loaded database dialect as " + prop + ".");
            this.props.setProperty("hibernate.dialect", prop);
        }
        
        prop = this.config.getProperty("DB_Username");
        if (prop == null)
        {
            this.logger.warn("Database user name property (DB_Username) not found," +
                    " using the default (" + this.props.getProperty("hibernate.connection.username") + ").");
        }
        else
        {
            this.logger.info("Loaded database user name as " + prop + ".");
            this.props.setProperty("hibernate.connection.username", prop);
        }
        
        prop = this.config.getProperty("DB_Password");
        if (prop == null)
        {
            this.logger.warn("Database password property (DB_Password) not found," +
                    " using the default (" + this.props.getProperty("hibernate.connection.password") + ").");
        }
        else
        {
            StringBuilder msg = new StringBuilder(35);
            msg.append("Loaded database user password as ");
            for (int i = 0; i < prop.length(); i++) msg.append('*');
            msg.append('.');
            this.logger.info(msg.toString());
            this.props.setProperty("hibernate.connection.password", prop);
        }
        
        prop = this.config.getProperty("DB_Conn_Pool_Min_Size");
        if (prop == null)
        {
            this.logger.warn("Database connection pool minimum size (DB_Conn_Pool_Min_Size) not found," +
                    " using the default (" + this.props.getProperty("hibernate.c3p0.min_size") + ").");
        }
        else
        {
            this.logger.info("Loaded database connection pool minimum size as " + prop + ".");
            this.props.setProperty("hibernate.c3p0.min_size", prop);
        }
        
        prop = this.config.getProperty("DB_Conn_Pool_Max_Size");
        if (prop == null)
        {
            this.logger.warn("Database connection pool maximum size (DB_Conn_Pool_Max_Size) not found," +
                    " using the default (" + this.props.getProperty("hibernate.c3p0.max_size") + ").");
        }
        else
        {
            this.logger.info("Loaded database connection pool maximum size as " + prop + ".");
            this.props.setProperty("hibernate.c3p0.max_size", prop);
        }
        
        prop = this.config.getProperty("DB_Conn_Timeout");
        if (prop == null)
        {
            this.logger.warn("Database connection timeout (DB_Conn_Timeout) not found," +
                    " using the default (" + this.props.getProperty("hibernate.c3p0.timeout") + ").");
        }
        else
        {
            this.logger.info("Loaded database connection timeout as " + prop + ".");
            this.props.setProperty("hibernate.c3p0.timeout", prop);
        }
    }
}
