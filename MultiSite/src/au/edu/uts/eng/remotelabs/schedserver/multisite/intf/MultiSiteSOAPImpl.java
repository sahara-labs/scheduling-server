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
 * @date 17th July 2011
 */
package au.edu.uts.eng.remotelabs.schedserver.multisite.intf;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RemotePermissionDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RemoteSiteDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.SessionDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserAssociationDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RemotePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RemoteSite;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserAssociation;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserAssociationId;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClass;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.multisite.MultiSiteActivator;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.AddToQueue;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.AddToQueueResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.AvailabilityResponseType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.CancelBooking;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.CancelBookingResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.CheckAvailability;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.CheckAvailabilityResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.CreateBooking;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.CreateBookingResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.FindFreeBookings;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.FindFreeBookingsResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.FinishSession;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.FinishSessionResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.GetQueuePosition;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.GetQueuePositionResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.GetSessionInformation;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.GetSessionInformationResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.GetUserStatus;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.GetUserStatusResponse;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.OperationResponseType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.QueueRequestType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.QueueTargetType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.ResourceType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.SessionType;
import au.edu.uts.eng.remotelabs.schedserver.multisite.intf.types.UserStatusType;
import au.edu.uts.eng.remotelabs.schedserver.queuer.pojo.QueuerService;
import au.edu.uts.eng.remotelabs.schedserver.queuer.pojo.types.QueueAvailability;
import au.edu.uts.eng.remotelabs.schedserver.queuer.pojo.types.QueueSession;
import au.edu.uts.eng.remotelabs.schedserver.session.pojo.SessionService;

/**
 * MultiSite service implementation.
 */
public class MultiSiteSOAPImpl implements MultiSiteSOAP
{
    /** Logger. */
    private Logger logger;
    
    public MultiSiteSOAPImpl()
    {
        this.logger = LoggerActivator.getLogger();
    }
    
    @Override
    public GetUserStatusResponse getUserStatus(GetUserStatus getUserStatus)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CheckAvailabilityResponse checkAvailability(CheckAvailability checkAvailability)
    {
        String site = checkAvailability.getCheckAvailability().getSiteID();
        String pid = checkAvailability.getCheckAvailability().getPermissionID();
        
        this.logger.debug("Received " + this.getClass().getSimpleName() + "#checkAvailability with params site=" + 
                site + ", permission=" + pid + '.');
        
        /* Response parameters. */
        CheckAvailabilityResponse response = new CheckAvailabilityResponse();
        AvailabilityResponseType avail = new AvailabilityResponseType();
        response.setCheckAvailabilityResponse(avail);
        ResourceType res = new ResourceType();
        res.setName("");
        res.setType("");
        avail.setQueuedResource(res);
        
        RemotePermissionDao dao = new RemotePermissionDao();
        try
        {
            RemotePermission remotePerm = dao.findByGuid(pid);
            if (remotePerm == null)
            {
                /* Permission not found. */
                this.logger.warn("Cannot provide availability of permission '" + pid + "' because it was not found.");
                return response;
            }
            
            if (!remotePerm.getSite().getGuid().equals(site))
            {
                /* Origin is incorrect. */
                this.logger.warn("Cannot provide availability of permission '" + pid + "' because the site '" + 
                        site + "' does not match the stored site '" + remotePerm.getSite().getGuid() + "'.");
                return response;
            }
            
            QueuerService queuer = MultiSiteActivator.getQueuerService();
            if (queuer == null)
            {
                this.logger.warn("Cannot provider availability of permission '" + pid + "' becase the Queuer " +
                		"service was not found.");
                return response;
            }
            
            ResourcePermission perm = remotePerm.getPermission();
            avail.setIsBookable(perm.getUserClass().isBookable());
            avail.setIsQueueable(perm.getUserClass().isQueuable());
            
            QueueAvailability availability = queuer.checkAvailability(perm, dao.getSession());
            avail.setViable(availability.isViable());
            avail.setHasFree(availability.isHasFree());
            avail.setIsCodeAssignable(availability.isCodeAssignable());
            res.setName(availability.getName());
            res.setType(availability.getType());
            
            for (Rig rig : availability.getTargets())
            {
                QueueTargetType target = new QueueTargetType();
                target.setIsFree(availability.isTargetFree(rig));
                target.setViable(rig.isActive() && rig.isOnline());
                
                ResourceType targetRes = new ResourceType();
                targetRes.setName(rig.getName());
                targetRes.setType(ResourcePermission.RIG_PERMISSION);
                target.setResource(targetRes);
                
                avail.addQueueTarget(target);
            }
        }
        finally
        {
            dao.closeSession();
        }
        
        return response;
    }

    @Override
    public AddToQueueResponse addToQueue(AddToQueue addToQueue)
    {
        QueueRequestType request = addToQueue.getAddToQueue();
        final String site = request.getSiteID();
        final String pid = request.getPermission().getPermissionID();
        final String username = request.getUser().getUserID();
        
        this.logger.debug("Received " + this.getClass().getSimpleName() + "#addToQueue with params site=" + site + 
                ", permission=" + pid + ", user=" + username + '.');
        
        AddToQueueResponse response = new AddToQueueResponse();
        OperationResponseType opResp = new OperationResponseType();
        UserStatusType userStatus = new UserStatusType();
        response.setAddToQueueResponse(userStatus);
        userStatus.setOperation(opResp);
        opResp.setWasSuccessful(false);
                
        RemotePermissionDao dao = new RemotePermissionDao();
        try
        {
            RemotePermission remotePerm = dao.findByGuid(pid);
            if (remotePerm == null)
            {
                /* Permission not found. */
                this.logger.warn("Cannot add user '" + username + "' to queue because the remote permission '" + pid + 
                        "' was not found.");
                opResp.setReason("Permission not found.");
                return response;
            }
            
            if (!remotePerm.getSite().getGuid().equals(site))
            {
                this.logger.warn("Cannot add user '" + username + "' to queue because the because the site '" + site + 
                        "' does not match the store site '" + remotePerm.getSite().getGuid() + "'.");
                opResp.setReason("Incorrect origin.");
                return response;
            }
            
            QueuerService queuer = MultiSiteActivator.getQueuerService();
            if (queuer == null)
            {
                this.logger.warn("Cannot add user to '" + username + "' to queue because the because the Queuer " +
                		"service was not found.");
                opResp.setReason("Queuer service not found.");
                return response;
            }
            
            User user = this.getUser(username, remotePerm.getSite(), dao.getSession());
            this.ensureAssociation(user, remotePerm.getPermission().getUserClass(), dao.getSession());
            
            // TODO Batch support
            QueueSession ses = queuer.addUserToQueue(user, remotePerm.getPermission(), null, dao.getSession());
            if (ses.wasSuccessful())
            {
                opResp.setWasSuccessful(true);
                this.populateSessionDetails(userStatus, ses.getSession());
            }
            else
            {
                opResp.setReason(ses.getErrorMessage());
            }
        }
        finally
        {
            dao.closeSession();
        }

        return response;
    }

    @Override
    public GetQueuePositionResponse getQueuePosition(GetQueuePosition getQueuePosition)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GetSessionInformationResponse getSessionInformation(GetSessionInformation getSessionInformation)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FindFreeBookingsResponse findFreeBookings(FindFreeBookings findFreeBookings)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CreateBookingResponse createBooking(CreateBooking createBooking)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CancelBookingResponse cancelBooking(CancelBooking cancelBooking)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public FinishSessionResponse finishSession(FinishSession finishSession)
    {
        String siteId = finishSession.getFinishSession().getSiteID();
        String userId = finishSession.getFinishSession().getUserID();
       
        this.logger.debug("Received " + this.getClass().getSimpleName() + "#finishSession with params site=" + siteId +
                ", user=" + userId);
        
        FinishSessionResponse response = new FinishSessionResponse();
        OperationResponseType opResp = new OperationResponseType();
        response.setFinishSessionResponse(opResp);
        
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        try
        {
            /* Load site. */
            RemoteSite site = new RemoteSiteDao(db).findSite(siteId);
            if (site == null)
            {
                this.logger.warn("Unable to finish session for user '" + userId + "' because the site '" + siteId + 
                        "' was not found.");
                opResp.setReason("Site not found");
                return response;
            }
            
            /* Load user. */
            User user = new UserDao(db).findByName(site.getUserNamespace(), userId);
            if (user == null)
            {
                this.logger.warn("Unable to finish session for user '" + userId + "' because the user was not found.");
                opResp.setReason("User not found.");
                return response;
            }
            
            Session ses = new SessionDao(db).findActiveSession(user);
            if (ses == null)
            {
                this.logger.warn("Unable to finish session for user '" + userId + "' because the user does not have " +
                		"an active session.");
                opResp.setReason("User does not have active session");
                return response;
            }
            
            SessionService service = MultiSiteActivator.getSessionService();
            if (service == null)
            {
                this.logger.error("Unable to finish session because the Session Service was not loaded.");
                opResp.setReason("Session not loaded.");
                return response;
            }
            
            opResp.setWasSuccessful(service.finishSession(ses, db));
        }
        finally
        {
            db.close();
        }

        return response;
    }

    /**
     * Gets a user. If the user does not exist, a new user is created.
     * 
     * @param name the name of a user
     * @param site the site the user is from
     * @param session database session
     * @return user 
     */
    private User getUser(String name, RemoteSite site, org.hibernate.Session db)
    {
        UserDao dao = new UserDao(db);
        String ns = site.getUserNamespace();

        User user = dao.findByName(ns, name);
        if (user == null)
        {
            this.logger.info("Remote user '" + name + "' from site '" + site + "' with namespace '" + ns + 
                    "' does not exist so will be created.");
            
            user = new User();
            user.setNamespace(ns);
            user.setName(name);
            user.setPersona(User.USER); // Remote users are always uses.
            dao.persist(user);
        }
        
        return user;
    }
    
    /**
     * Makes sure the user has an association to the user class. If the user doesn't have
     * the association, then the association is added.
     * 
     * @param user user 
     * @param userClass class to
     * @param db
     */
    private void ensureAssociation(User user, UserClass userClass, org.hibernate.Session db)
    {
        UserAssociationDao dao = new UserAssociationDao(db);
        UserAssociationId id = new UserAssociationId(user.getId(), userClass.getId());
        
        if (dao.get(id) == null)
        {
            /* User association does not exist, so add it. */
            UserAssociation assoc = new UserAssociation();
            assoc.setId(id);
            assoc.setUser(user);
            assoc.setUserClass(userClass);
            dao.persist(assoc);
        }
    }
    
    /**
     * Populates the details of a session to a response type.
     * 
     * @param status user status type
     * @param ses session
     */
    private void populateSessionDetails(UserStatusType status, Session ses)
    {
        status.setInBooking(false);
        status.setInQueue(false);
        status.setInSession(false);
        
        ResourceType res = new ResourceType();
        res.setType(ses.getResourceType());
        res.setName(ses.getRequestedResourceName());

        if (ses.getAssignmentTime() == null)
        {
            /* User in queue. */
            status.setInQueue(true);
            status.setQueuedResource(res);
        }
        else
        {
            /* User in session. */
            status.setInSession(true);
            
            SessionType sesType = new SessionType();
            status.setSession(sesType);
            
            sesType.setIsReady(true); //ses.isReady());
            sesType.setIsCodeAssigned(ses.getCodeReference() != null);
            sesType.setInGrace(ses.isInGrace());
            
            sesType.setResource(res);
            
            Rig rig = ses.getRig();
            sesType.setRigType(rig.getRigType().getName());
            sesType.setRigName(rig.getName());
            sesType.setContactURL(rig.getContactUrl());
            
            // FIXME  Add session timing details & warning details
            sesType.setTime(100);
            sesType.setTimeLeft(200);
            sesType.setExtensions(2);
        }
    }
}
