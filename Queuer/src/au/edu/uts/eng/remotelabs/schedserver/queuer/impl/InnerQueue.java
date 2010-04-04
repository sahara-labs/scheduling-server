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
 * @date 1st April 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.queuer.impl;

import java.util.PriorityQueue;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;

/**
 * Inner queue holding a priority queue for holding sessions queuing for
 * the same resource.
 * <br />
 * This inner queue is not thread safe and must be externally synchronised.
 */
public class InnerQueue
{
    /** Underlying queue which orders sessions by priority. */
    private PriorityQueue<Session> underlyingQueue;

    /** The identifier of the queued resource. */
    private long resourceId;
    
    /** The name of the queued resource. */
    private String resourceName;
    
    /** The type of the queued resource. */
    private String resourceType;

    public InnerQueue(String type, long id, String name)
    {
        this.resourceId = id;
        this.resourceName = name;
        this.resourceType = type;
        
        this.underlyingQueue = new PriorityQueue<Session>(11, new QueueSessionComparator());
    }
    
    /**
     * Adds a session to the queue.
     * 
     * @param ses session to add to the queue
     */
    public void add(Session ses)
    {
        this.underlyingQueue.add(ses);
    }
    
    /**
     * Gets the head of the queue.
     * 
     * @return head of queue
     */
    public Session getHead()
    {
        return this.underlyingQueue.poll();
    }
    
    /**
     * Peeks the head of the queue. Returns the head of the queue without
     * removing it from the head of the queue.
     */
    public Session peekHead()
    {
        return this.underlyingQueue.peek();
    }
    
    /**
     * Removes a session from the queue.
     * 
     * @param ses session to remove from the queue
     */
    public void remove(Session ses)
    {
        Session removalInstance = null;
        
        /* The normal list remove isn't sufficient because identity may not
         * be the same, so search based on ID. */
        for (Session s : this.underlyingQueue)
        {
            if (s.getId().equals(ses.getId()))
            {
                removalInstance = s;
                break;
            }
        }
        
        if (removalInstance != null) this.underlyingQueue.remove(removalInstance);
    }
    
    /**
     * Checks if the session contains the specified session.
     * 
     * @param ses session to check
     * @return true if queue contains session
     */
    public boolean contains(Session ses)
    {
        /* The normal list remove isn't sufficient because identity may not
         * be the same, so search based on ID. */
        for (Session s : this.underlyingQueue)
        {
            if (s.getId().equals(ses.getId()))
            {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Returns the position of the session in the queue. -1 is returned
     * if the session isn't in the queue.
     * 
     * @param ses session to check
     * @return position in queue.
     */
    public int position(Session ses)
    {
        boolean found = false;
        int pos = 1;
        QueueSessionComparator comparator = new QueueSessionComparator();
        
        /* The normal list remove isn't sufficient because identity may not
         * be the same, so search based on ID. */
        for (Session s : this.underlyingQueue)
        {
            if (s.getId().equals(ses.getId()))
            {
                found = true;
            }
            else
            {
                if (comparator.compare(ses, s) > 0)
                {
                    pos++;
                }
            }
        }
        
        return found ? pos : -1;
    }
    
    /**
     * Returns the number of sessions who will be assigned to a rig
     * before the supplied session.
     * 
     * @param ses session
     * @return number who will be allocated first
     */
    public int numberBefore(Session ses)
    {
        int pos = 0;
        QueueSessionComparator comparator = new QueueSessionComparator();
        
        for (Session s : this.underlyingQueue)
        {
            if (!s.getId().equals(ses.getId())&& comparator.compare(ses, s) > 0) pos++;
        }
        
        return pos;
    }
    
    /**
     * Returns the size of queue.
     * 
     * @return size of queue
     */
    public int size()
    {
        return this.underlyingQueue.size();
    }

    /**
     * @return the resource identifier
     */
    public long getResourceId()
    {
        return this.resourceId;
    }

    /**
     * @return the resource name
     */
    public String getResourceName()
    {
        return this.resourceName;
    }

    /**
     * @return the queued resource type
     */
    public String getResourceType()
    {
        return this.resourceType;
    }
}
