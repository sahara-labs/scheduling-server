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
 * @date 4th January 2010
 */
package io.rln.ss.utils.tasksched.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import io.rln.ss.utils.logger.Logger;
import io.rln.ss.utils.logger.LoggerActivator;

/**
 * The task scheduler contains a thread pool to run periodic tasks. The 
 * thread pool size is currently statically set at an initial size.
 */
public class TaskScheduler
{
    /** Thread pool size. */
    public static final int THREAD_POOL_SIZE = 4;
    
    /** Thread pool which runs scheduled tasks. */
    private ScheduledThreadPoolExecutor executor;
    
    /** The map of runnable tasks and their delays. */
    private Map<Runnable, Integer> tasks;
    
    /** The futures returned by adding the tasks. */
    private Map<Runnable, ScheduledFuture<?>> futures;
    
    /** Logger. */
    private Logger logger;
    
    public TaskScheduler()
    {
        this.logger = LoggerActivator.getLogger();
        this.tasks = new HashMap<Runnable, Integer>();
        this.futures = new HashMap<Runnable, ScheduledFuture<?>>();
        
        this.executor = new ScheduledThreadPoolExecutor(TaskScheduler.THREAD_POOL_SIZE);
    }
    
    /**
     * Adds a task to be run every <tt>period</tt> seconds.
     * 
     * @param task runnable task
     * @param period period to run task
     */
    public void addTask(Runnable task, int period)
    {
        this.logger.debug("Adding runnable task with type '" + task.getClass().getName() + "' to task scheduler to " +
        		"run every " + period + " seconds.");
        
        synchronized (this)
        {
            this.tasks.put(task, period);
            this.futures.put(task, this.executor.scheduleAtFixedRate(task, 0, period, TimeUnit.SECONDS));
        }
    }
    
    /**
     * Removes a task from being run.
     * 
     * @param task task to remove
     */
    public void removeTask(Runnable task)
    {
        this.logger.info("Removing runnable task with type '" + task.getClass().getName() + "' from the task scheduler.");
        synchronized (this)
        {
            if (this.tasks.containsKey(task))
            {
                this.tasks.remove(task);
                this.futures.remove(task).cancel(true);
                this.executor.remove(task);
                this.executor.purge();
            }
            else
            {
                this.logger.warn("Unable to remove task of type " + task.getClass().getName() + " as it is not " +
                		"currently running.");
            }
        }
    }
    
    /**
     * Shuts down the thread pool.
     */
    public void shutdown()
    {
        this.logger.info("Shutting down the task scheduler thread pool.");
        this.executor.shutdown();
    }
}
