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
 * @date 23rd July 2011
 */
package au.edu.uts.eng.remotelabs.schedserver.queuer.pojo.impl;



import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.MatchingCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RequestCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.PermissionAvailabilityCheck;
import au.edu.uts.eng.remotelabs.schedserver.multisite.provider.PermissionAvailabilityCheck.QueueTarget;
import au.edu.uts.eng.remotelabs.schedserver.queuer.impl.Queue;
import au.edu.uts.eng.remotelabs.schedserver.queuer.impl.QueueEntry;
import au.edu.uts.eng.remotelabs.schedserver.queuer.impl.QueuerUtil;
import au.edu.uts.eng.remotelabs.schedserver.queuer.pojo.QueuerService;
import au.edu.uts.eng.remotelabs.schedserver.queuer.pojo.types.QueueAvailability;
import au.edu.uts.eng.remotelabs.schedserver.queuer.pojo.types.QueueSession;

/**
 * Implementation of the Queuer POJO service.
 */
public class QueuerServiceImpl implements QueuerService
{
    /** Logger. */
    private Logger logger;
    
    public QueuerServiceImpl()
    {
        this.logger = LoggerActivator.getLogger();
    }

    @Override
    public QueueAvailability checkAvailability(ResourcePermission perm, org.hibernate.Session ses)
    {
        this.logger.debug("Received " + this.getClass().getSimpleName() + "#checkAvailability for permission '" + 
                perm.getId() + "'.");
        
        QueueAvailability response = new QueueAvailability();
        response.setType(perm.getType());
        
        boolean free = false;
        if (ResourcePermission.RIG_PERMISSION.equals(perm.getType()))
        {
            /* Rig resource. */
            Rig rig = perm.getRig();
            response.setName(rig.getName());
            response.setHasFree(QueuerUtil.isRigFree(rig, perm, ses));
            response.setViable(rig.isOnline());
            
            /* Code assignable is defined by the rig type of the rig. */
            response.setCodeAssignable(rig.getRigType().isCodeAssignable());
            
            /* Only one resource, the actual rig. */
            response.addTarget(rig, QueuerUtil.isRigFree(rig, perm, ses));          
        }
        else if (ResourcePermission.TYPE_PERMISSION.equals(perm.getType()))
        {
            /* Rig type resource. */
            RigType rigType = perm.getRigType();
            response.setName(rigType.getName());
            response.setCodeAssignable(rigType.isCodeAssignable());
            
            /* The targets are the rigs in the rig type. */
            for (Rig rig : rigType.getRigs())
            {
                if (rig.isOnline()) response.setViable(true);
                if (free = QueuerUtil.isRigFree(rig, perm, ses)) response.setHasFree(true);
                
                response.addTarget(rig, free);
            }
        }
        else if (ResourcePermission.CAPS_PERMISSION.equals(perm.getType()))
        {
            /* Capabilities resource. */
            RequestCapabilities requestCaps = perm.getRequestCapabilities();
            response.setName(requestCaps.getCapabilities());

            /* For code assignable to be true, all rigs who match the
             * request capabilities, must be code assignable. */
            response.setCodeAssignable(true);
            
            /* Are all the rigs who have match rig capabilities to the
             * request capabilities. */
            for (MatchingCapabilities match : requestCaps.getMatchingCapabilitieses())
            {
                for (Rig capRig : match.getRigCapabilities().getRigs())
                {
                    if (!capRig.getRigType().isCodeAssignable()) response.setCodeAssignable(false);
                    
                    /* To be viable, only one rig needs to be online. */
                    if (capRig.isOnline()) response.setViable(true);
                    
                    /* To be 'has free', only one rig needs to be free. */
                    if (free = QueuerUtil.isRigFree(capRig, perm, ses)) response.setHasFree(true);
                    
                    /* Add target. */
                    response.addTarget(capRig, free);
                }
            }
        }
        else if (ResourcePermission.CONSUMER_PERMISSION.equals(perm.getType()))
        {
            /* Sanity check to make sure the permission actually has a mapping. */
            if (perm.getRemotePermission() == null)
            {
                this.logger.warn("Consumer type permission does not have a mapping to a remote permission so cannot " +
                		"make remote request to determine availability.");
                return response;
            }
            
            /* Make the remote call and populate the results. */
            PermissionAvailabilityCheck check = new PermissionAvailabilityCheck();
            if (check.checkAvailability(perm.getRemotePermission(), ses))
            {
                response.setViable(check.isViable());
                response.setHasFree(check.hasFree());
                response.setName(check.getResourceName());
                response.setType(check.getResourceType());
                
                for (QueueTarget qt : check.getQueueTargets())
                {
                    Rig rig = new Rig();
                    rig.setName(qt.getName());
                    response.addTarget(rig, qt.isFree());
                }
            }
        }
        
        return response;
    }

    @Override
    public QueueSession addUserToQueue(User user, ResourcePermission perm, String code, org.hibernate.Session db)
    {
        QueueEntry entry = new QueueEntry(db);
        
        /* If user is already in the queue, they can be added again. */
        if (entry.isInQueue(user))
        {
            this.logger.warn("User '" + user.qName() + "' cannot queue because they are already in the queue.");
            return new QueueSession(entry.getErrorMessage());
        }
        else if (!entry.hasPermission(perm))
        {
            this.logger.warn("User '" + user.qName() + "' cannot queue because they do not have permission to use " +
            		"the ri or the permission is inactive.");
            return new QueueSession(entry.getErrorMessage());
        }
        else if (!entry.canUserQueue())
        {
            this.logger.warn("User '" + user.qName() + "' cannot queue because the resource may be offline.");
            return new QueueSession(entry.getErrorMessage());
        }
        else if (entry.addToQueue(code))
        {
            Session ses = entry.getActiveSession();
            if (ses.getAssignmentTime() == null)
            {
                /* In queue. */
                return new QueueSession(ses, Queue.getInstance().getEntryPosition(ses, db));
            }
            else return new QueueSession(ses); // Assigned session
        }
        else
        {
            this.logger.warn("User '" + user.qName() + "' cannot queue because the resource may be offline or the " +
                    "user does not have permission to use the rig.");
            return new QueueSession("User does not have permission or rig is offline.");
        }
    }

}
