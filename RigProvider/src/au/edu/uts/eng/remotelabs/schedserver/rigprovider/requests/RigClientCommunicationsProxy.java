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
 * @date 31st July 2016
 */

package au.edu.uts.eng.remotelabs.schedserver.rigprovider.requests;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType.Context;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.RigCommunicationProxy;

/**
 * Proxy to call rig client operations.
 */
public class RigClientCommunicationsProxy implements RigCommunicationProxy
{

    @Override
    public void allocate(Session ses, org.hibernate.Session db)
    {
        if (this.isRigClient(ses.getRig()))
        {
            new RigAllocator().allocate(ses, db);
        }
    }

    @Override
    public void release(Session ses, org.hibernate.Session db)
    {
        if (this.isRigClient(ses.getRig()))
        {
            new RigReleaser().release(ses, db);
        }
    }

    @Override
    public void notify(String message, Session ses, org.hibernate.Session db)
    {
        if (this.isRigClient(ses.getRig()))
        {
            new RigNotifier().notify(message, ses, db);
        }
    }

    

    @Override
    public void putMaintenance(Rig rig, boolean runTests, org.hibernate.Session db)
    {   
        if (this.isRigClient(rig))
        {
            new RigMaintenance().putMaintenance(rig, runTests, db);
        }
    }

    @Override
    public void clearMaintenance(Rig rig, org.hibernate.Session db)
    {
        if (this.isRigClient(rig))
        {
            new RigMaintenance().clearMaintenance(rig, db);
        }
    }
    
    @Override
    public boolean hasActivity(Session ses, org.hibernate.Session db)
    {
        if (this.isRigClient(ses.getRig()))
        {
            return new RigActivity().isActivityDetectable(ses, db);
        }
        else
        {
            return false;
        }
    }
    
    @Override
    public void hasActivity(Session ses, org.hibernate.Session db, ActivityAsyncCallback callback)
    {
        if (this.isRigClient(ses.getRig()))
        {
            new RigActivity().isActivityDetectable(ses, db, callback);
        }        
    }
    
    /**
     * Returns whether the rig is a Rig Client. Other rig implementations are not 
     * communicated with this provider.
     * 
     * @param rig the rig to check
     * @return true if rig client
     */
    public boolean isRigClient(Rig rig)
    {
        return rig.getContext() == null || rig.getContext() == Context.SAHARA;
    }
}
