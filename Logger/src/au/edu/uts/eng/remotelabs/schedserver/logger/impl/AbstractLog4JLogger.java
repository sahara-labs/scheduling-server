/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2009, University of Technology, Sydney
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
 * @date 28th December 2009
 */
package au.edu.uts.eng.remotelabs.schedserver.logger.impl;

import java.io.IOException;

import org.apache.log4j.Level;

import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;

/**
 * Apache Log4j abstract logger.  Sub-classes implement the setAppender 
 * to set where Log4J logs to.
 */
abstract class AbstractLog4JLogger extends AbstractLogger
{
    /** Log4j layout have the message and a line break. */
    protected static final String PATTERN_LAYOUT = "%m%n";

    /** Log4j logger. */
    protected org.apache.log4j.Logger logger;

    /**
     * Constructor.
     * 
     * @throws IOException error loading appender
     */
    public AbstractLog4JLogger() 
    {
        this.logger = org.apache.log4j.Logger.getLogger("ISG Client");

        // Logging level
        switch(LoggerActivator.getLoggingLevel())
        {
            case Logger.DEBUG:
                this.logger.setLevel(Level.DEBUG);
                break;
            case Logger.INFO:
                this.logger.setLevel(Level.INFO);
                break;
            case Logger.WARN:
                this.logger.setLevel(Level.WARN);
                break;
            case Logger.ERROR:
                this.logger.setLevel(Level.ERROR);
                break;
            default:
                this.logger.setLevel(Level.INFO);
        }   

        this.setAppeneder();
    }

    /**
     * Sets an appender for the Log4J logger.
     * 
     * @param config class to load configuration
     */
    protected abstract void setAppeneder();

    @Override
    public void log(final int level, final String message)
    {
        switch (level)
        {
            case Logger.DEBUG:
                this.logger.debug(message);
                break;
            case Logger.INFO:
                this.logger.info(message);
                break;
            case Logger.WARN:
                this.logger.warn(message);
                break;
            case Logger.ERROR:
                this.logger.error(message);
                break;
            default:    
                this.logger.debug(message);
                break;
        }
    }

    /**
     * Gets an integer from a configuration value which may not be a valid integer.
     * 
     * @param str config string
     * @param def value if string is not a valid int
     * @return valid integer 
     */
    protected int getConfigInt(String str, int def)
    {
        try
        {
            return Integer.parseInt(str);
        }
        catch (NumberFormatException ex)
        {
            return def;
        }
    }
}
