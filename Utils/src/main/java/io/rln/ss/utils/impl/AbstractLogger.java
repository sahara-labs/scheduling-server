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
package io.rln.ss.utils.logger.impl;

import java.util.Map;

import io.rln.ss.utils.logger.Logger;
import io.rln.ss.utils.logger.LoggerActivator;

/**
 * Abstract logger, fills in some boilerplate code.
 */
abstract class AbstractLogger implements Logger
{
    /** Level to log to. */
    protected short logLevel;
    
    /** Log formatter. */
    private final LogFormatter formatter;
    
    /** Format strings for each log type. */
    private final Map<Integer, String> formatStrings;

    /**
     * Constructor - loads the logging level.
     */
    public AbstractLogger()
    {
        this.logLevel = LoggerActivator.getLoggingLevel();
        this.formatStrings = LoggerActivator.getFormatStrings();
        this.formatter = new LogFormatter();
     }

    @Override
    public void debug(final String message)
    {
        if (Logger.DEBUG <= this.logLevel)
        {
            this.log(Logger.DEBUG, 
                    this.formatter.formatLog(this.formatStrings.get(Logger.DEBUG), message, "DEBUG"));
        }
    }

    @Override
    public void error(final String message)
    {
        this.log(Logger.ERROR, 
                this.formatter.formatLog(this.formatStrings.get(Logger.ERROR), message, "ERROR"));
    }

    @Override
    public void fatal(final String message)
    {
        this.log(Logger.FATAL, 
                this.formatter.formatLog(this.formatStrings.get(Logger.FATAL), message, "FATAL"));        
    }

    @Override
    public void info(final String message)
    {
        if (Logger.INFO <= this.logLevel)
        {
            this.log(Logger.INFO, 
                    this.formatter.formatLog(this.formatStrings.get(Logger.INFO), message, "INFO"));
        }
    }

    @Override
    public void priority(final String message)
    {
        this.log(Logger.PRIORITY, 
                this.formatter.formatLog(this.formatStrings.get(Logger.PRIORITY), message, "PRIORITY")); 
    }

    @Override
    public void warn(final String message)
    {
        if (Logger.WARN <= this.logLevel)
        {
            this.log(Logger.WARN, 
                    this.formatter.formatLog(this.formatStrings.get(Logger.WARN), message, "WARN"));
        }
    }
}
