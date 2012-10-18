/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2011, Michael Diponio
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
 * @date 22nd July 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.queuer.pojo.types;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;

/**
 * Queue availability bean.
 */
public class QueueAvailability
{
    /** Resource name. */
    protected String name;
    
    /** Resource type. */
    protected String type;
    
    /** Whether the permission is viable. */
    protected boolean viable;
    
    /** Whether the permission has free rigs. */
    protected boolean hasFree;
    
    /** Whether code can be assigned. */
    protected boolean isCodeAssignable;
    
    /** The list of queue targets. */
    protected Map<Rig, Boolean> targets;
    
    public QueueAvailability()
    {
        this.targets = new HashMap<Rig, Boolean>();
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getType()
    {
        return this.type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public boolean isViable()
    {
        return this.viable;
    }

    public void setViable(boolean viable)
    {
        this.viable = viable;
    }

    public boolean isHasFree()
    {
        return this.hasFree;
    }

    public void setHasFree(boolean hasFree)
    {
        this.hasFree = hasFree;
    }

    public boolean isCodeAssignable()
    {
        return this.isCodeAssignable;
    }

    public void setCodeAssignable(boolean isCodeAssignable)
    {
        this.isCodeAssignable = isCodeAssignable;
    }

    public Set<Rig> getTargets()
    {
        return this.targets.keySet();
    }
    
    public boolean isTargetFree(Rig rig)
    {
        if (!this.targets.containsKey(rig)) return false;
        return this.targets.get(rig);
    }
    
    public void addTarget(Rig rig, boolean isFree)
    {
        this.targets.put(rig, isFree);
    }
}
