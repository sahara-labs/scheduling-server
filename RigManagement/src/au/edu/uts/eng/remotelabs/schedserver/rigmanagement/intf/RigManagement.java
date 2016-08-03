/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2009 - 2011, University of Technology, Sydney
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
 * @date 29th January 2011
 */

package au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import au.edu.uts.eng.remotelabs.schedserver.bookings.pojo.BookingEngineService;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigLogDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigOfflineScheduleDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigTypeDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigLog;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigOfflineSchedule;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.listener.SessionEventListener.SessionEvent;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.RigManagementActivator;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.CancelRigOffline;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.CancelRigOfflineResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.CancelRigOfflineType;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.FreeRig;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.FreeRigResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.GetRig;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.GetRigResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.GetTypeStatus;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.GetTypeStatusResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.GetTypes;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.GetTypesResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.KickRigType;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.OfflinePeriodType;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.OperationRequestType;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.OperationResponseType;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.PutRigOffline;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.PutRigOfflineResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.PutRigOfflineType;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.RigLogType;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.RigStateType;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.RigType;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.RigTypeIDType;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.RigTypeType;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.RigTypesType;

/**
 * Rig management SOAP service.
 */
public class RigManagement implements RigManagementInterface
{
    /** Logger. */
    public Logger logger;
    
    public RigManagement()
    {
        this.logger = LoggerActivator.getLogger();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public GetTypesResponse getTypes(GetTypes getTypes)
    {
        this.logger.debug("Received RigManagement#getTypes.");
        
        GetTypesResponse response = new GetTypesResponse();
        RigTypesType types = new RigTypesType();
        response.setGetTypesResponse(types);
        
        Session db = DataAccessActivator.getNewSession();
        try
        {
            
            for (au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType rigType : 
                (List<au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType>)db.createCriteria(
                    au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType.class).addOrder(Order.asc("name")).list())
           {
               RigTypeType rigTypeParam = new RigTypeType();
               rigTypeParam.setName(rigType.getName());
               types.addType(rigTypeParam);
               
               Set<Rig> rigs = rigType.getRigs();
               rigTypeParam.setNumberRigs(rigs.size());
               
               rigTypeParam.setIsOnline(false);
               for (Rig rig : rigs)
               {
                   if (rig.isActive() && rig.isOnline())
                   {
                       rigTypeParam.setIsOnline(true);
                       break;
                   }
               }
               
               rigTypeParam.setIsAlarmed(false);
               for (Rig rig : rigs)
               {
                   if (!(rig.isActive() && rig.isOnline()) && !(new RigOfflineScheduleDao(db).isOffline(rig)))
                   {
                       rigTypeParam.setIsAlarmed(true);
                       break;
                   }
               }
           }
        }
        finally
        {
            db.close();
        }
        
        return response;
    }
    
    @Override
    public GetTypeStatusResponse getTypeStatus(GetTypeStatus request)
    {
        String name = request.getGetTypeStatus().getName();
        this.logger.debug("Received RigManagement#getTypeStatus with params: name=" + name + '.');
        
        GetTypeStatusResponse response = new GetTypeStatusResponse();
        RigTypeType rigTypeParam = new RigTypeType();
        rigTypeParam.setName(name);
        response.setGetTypeStatusResponse(rigTypeParam);
        
        RigTypeDao dao = new RigTypeDao();
        try
        {
            au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType rigType = dao.findByName(name);
            if (rigType == null)
            {
                this.logger.info("Unable to find rig type because the no rig type with name '" + name + "' found.");
                rigTypeParam.setNumberRigs(0);
                return response;
            }
            
            boolean online = false;
            boolean alarmed = false;
            
            Set<Rig> rigs = rigType.getRigs();
            rigTypeParam.setNumberRigs(rigs.size());
            
            RigTypeIDType typeID = new RigTypeIDType();
            typeID.setName(name);
            
            for (Rig rig : rigs)
            {
                RigType rigParam = new RigType();
                rigTypeParam.addRig(rigParam);
                
                rigParam.setName(rig.getName());
                rigParam.setRigType(typeID);
                rigParam.setCapabilities(rig.getRigCapabilities() != null ? rig.getRigCapabilities().getCapabilities() : "");             
                
                rigParam.setIsRegistered(rig.isActive());
                rigParam.setIsOnline(rig.isOnline());
                rigParam.setIsInSession(rig.isInSession());
                if (rig.isInSession()) rigParam.setSessionUser(rig.getSession().getUser().qName());
                
                if (rig.isOnline() && rig.isActive()) online = true;
                else
                {
                    if (rig.getOfflineReason() != null) rigParam.setOfflineReason(rig.getOfflineReason());
                    
                    /* If the rig is off-line and not scheduled to be off-line, the rig is alarmed. */
                    if (!(new RigOfflineScheduleDao(dao.getSession()).isOffline(rig)))
                    {
                        alarmed = true;
                        rigParam.setIsAlarmed(true);
                    }
                }
                
                if (rig.getContactUrl() != null) rigParam.setContactURL(rig.getContactUrl()); 
            }
            
            rigTypeParam.setIsOnline(online);
            rigTypeParam.setIsAlarmed(alarmed);
        }
        finally
        {
            dao.closeSession();
        }
        
        return response;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public GetRigResponse getRig(GetRig request)
    {
        String name = request.getGetRig().getName();
        this.logger.debug("Received RigManagement#getRig with parameters: name=" + name + '.');
        
        GetRigResponse response = new GetRigResponse();
        RigType rigParam = new RigType();
        rigParam.setName(name);
        RigTypeIDType typeID = new RigTypeIDType();
        typeID.setName("Unknown.");
        rigParam.setRigType(typeID);
        rigParam.setCapabilities("Unknown.");
        response.setGetRigResponse(rigParam);
        
        RigDao dao = new RigDao();
        try
        {
            Rig rig = dao.findByName(name);
            if (rig == null) return response;
            
            typeID.setName(rig.getRigType().getName());
            rigParam.setCapabilities(rig.getRigCapabilities() != null ? rig.getRigCapabilities().getCapabilities() : "");
            
            rigParam.setIsRegistered(rig.isActive());
            rigParam.setIsOnline(rig.isOnline());
            if (rig.isInSession())
            {
                rigParam.setIsInSession(true);
                rigParam.setSessionUser(rig.getSession().getUser().qName());
            }
            
            rigParam.setIsAlarmed(!(rig.isActive() && rig.isOnline()) && 
                    !(new RigOfflineScheduleDao(dao.getSession()).isOffline(rig)));
            
            if (rig.getOfflineReason() != null) rigParam.setOfflineReason(rig.getOfflineReason());
            if (rig.getContactUrl() != null) rigParam.setContactURL(rig.getContactUrl());
            
            for (RigLog log : (List<RigLog>) dao.getSession().createCriteria(RigLog.class)
                    .add(Restrictions.eq("rig", rig))
                    .addOrder(Order.desc("timeStamp"))
                    .setMaxResults(10).list())
            {
                RigLogType logParam = new RigLogType();
                Calendar ts = Calendar.getInstance();
                ts.setTime(log.getTimeStamp());
                logParam.setTimestamp(ts);
                logParam.setReason(log.getReason());
                
                if      (RigLog.ONLINE.equals(log.getOldState())) logParam.setOldState(RigStateType.ONLINE);
                else if (RigLog.OFFLINE.equals(log.getOldState())) logParam.setOldState(RigStateType.OFFLINE);
                else if (RigLog.NOT_REGISTERED.equals(log.getOldState())) logParam.setOldState(RigStateType.NOT_REGISTERED);
                
                if      (RigLog.ONLINE.equals(log.getNewState())) logParam.setNewState(RigStateType.ONLINE);
                else if (RigLog.OFFLINE.equals(log.getNewState())) logParam.setNewState(RigStateType.OFFLINE);
                else if (RigLog.NOT_REGISTERED.equals(log.getNewState())) logParam.setNewState(RigStateType.NOT_REGISTERED);
                
                rigParam.addLastLog(logParam);
            }
            
            for (RigOfflineSchedule sched : new RigOfflineScheduleDao(dao.getSession()).getOfflinePeriods(rig))
            {
                OfflinePeriodType offline = new OfflinePeriodType();
                offline.setId(sched.getId().intValue());
                offline.setReason(sched.getReason());
                
                Calendar start = Calendar.getInstance();
                start.setTime(sched.getStartTime());
                offline.setStart(start);
                
                Calendar end = Calendar.getInstance();
                end.setTime(sched.getEndTime());
                offline.setEnd(end);
                
                rigParam.addOfflinePeriods(offline);
            }
        }
        finally
        {
            dao.closeSession();
        }
        
        return response;
    }
    
    @Override
    public PutRigOfflineResponse putRigOffline(PutRigOffline putRigOffline)
    {
        PutRigOfflineType param = putRigOffline.getPutRigOffline();
        this.logger.debug("Received RigManagement#putRigOffline with params: requestor ID=" + param.getRequestorID() +
                "requestor namespace=" + param.getRequestorNameSpace() + ", requestor name" + param.getRequestorName() +
                ", rig name=" + param.getRig().getName() + ", offline start=" + param.getStart().getTime() +
                ", offline end=" + param.getEnd().getTime() + ", reason=" + param.getReason() + '.');
        
        PutRigOfflineResponse response = new PutRigOfflineResponse();
        OperationResponseType result = new OperationResponseType();
        response.setPutRigOfflineResponse(result);
        
        RigDao dao = new RigDao();
        try
        {
            if (!this.isAuthorised(param, dao.getSession()))
            {
                this.logger.warn("Unable to put a rig offline because the user is not authorised to perform this " +
                		"operation (not a ADMIN).");
                result.setFailureCode(1);
                result.setFailureReason("Not authorised.");
                return response;
            }
            
            Rig rig = dao.findByName(param.getRig().getName());
            if (rig == null)
            {
                this.logger.warn("Unable to put a rig offline because the rig with name " + param.getRig().getName() +
                        " was not found.");
                result.setFailureCode(2);
                result.setFailureReason("Rig not found.");
                return response;
            }
            
            if (param.getStart().after(param.getEnd()))
            {
                this.logger.warn("Unable to put a rig offline because the offline start " + param.getStart().getTime() +
                        " is after the offline end " + param.getEnd().getTime() + ".");
                result.setFailureCode(3);
                result.setFailureReason("Start after end.");
                return response;
            }
            
            Date startDate = param.getStart().getTime();
            Date endDate = param.getEnd().getTime();
            
            if ((Integer)dao.getSession().createCriteria(RigOfflineSchedule.class)
                .add(Restrictions.eq("active", Boolean.TRUE))
                .add(Restrictions.eq("rig", rig))
                .add(Restrictions.disjunction()
                    .add(Restrictions.and(
                            Restrictions.gt("startTime", startDate),
                            Restrictions.lt("endTime", endDate)
                    ))
                    .add(Restrictions.and(
                            Restrictions.lt("startTime", startDate),
                            Restrictions.gt("endTime", endDate)
                    ))
                    .add(Restrictions.and(
                            Restrictions.lt("startTime", startDate),
                            Restrictions.gt("endTime", endDate)
                    ))
                    .add(Restrictions.and(
                            Restrictions.lt("startTime", startDate),
                            Restrictions.gt("endTime", endDate)
                    )
                )).setProjection(Projections.rowCount()).uniqueResult() > 0)
            {
                this.logger.warn("Unable to put a rig offline because there is a concurrent rig offline period.");
                result.setFailureCode(4);
                result.setFailureReason("Concurrent offline period.");
                return response;
            }

            result.setSuccessful(true);
            
            RigOfflineSchedule offline = new RigOfflineSchedule();
            offline.setActive(true);
            offline.setRig(rig);
            offline.setStartTime(startDate);
            offline.setEndTime(endDate);
            offline.setReason(param.getReason());
            new RigOfflineScheduleDao(dao.getSession()).persist(offline);
            
            /* Notify the booking engine. */
            BookingEngineService service = RigManagementActivator.getBookingService();
            if (service != null) service.putRigOffline(offline, dao.getSession());
            
            /* If the period is currently active, clear the rig maintenance state. */
            Date now = new Date();
            if (rig.isActive() && rig.isOnline() &&
                    offline.getStartTime().before(now) && offline.getEndTime().after(now))
            {
                this.logger.info("Setting maintenance state on rig " + rig.getName() + '.');
                RigManagementActivator.putMaintenance(rig, true, dao.getSession());
                
                rig.setOnline(false);
                rig.setOfflineReason("In maintenance.");
                new RigLogDao(dao.getSession()).addOfflineLog(rig, "In maintenance.");
                dao.flush();
            }
        }
        finally 
        {
            dao.closeSession();
        }
        
        return response;
    }
    
    @Override
    public CancelRigOfflineResponse cancelRigOffline(CancelRigOffline request)
    {
        CancelRigOfflineType cancelParam = request.getCancelRigOffline();
        this.logger.debug("Received RigManagement#cancelRigOffline with params: requestor ID=" + cancelParam.getRequestorID() +
                "requestor namespace=" + cancelParam.getRequestorNameSpace() + ", requestor name" + cancelParam.getRequestorName() +
                ", offline ID=" + cancelParam.getPeriod().getId() + '.');
        
        CancelRigOfflineResponse response = new CancelRigOfflineResponse();
        OperationResponseType result = new OperationResponseType();
        response.setCancelRigOfflineResponse(result);
        
        RigOfflineScheduleDao dao = new RigOfflineScheduleDao();
        try
        {
            if (!this.isAuthorised(cancelParam, dao.getSession()))
            {
                this.logger.warn("Unable to cancel a rig offline period because the user is not authorised to perform " +
                		"this operation (not a ADMIN).");
                result.setFailureCode(1);
                result.setFailureReason("Not authorised.");
                return response;
            }
            
            RigOfflineSchedule offline = dao.get(Long.valueOf(cancelParam.getPeriod().getId()));
            if (offline == null)
            {
                this.logger.warn("Unable to cancel a rig offline period because the offline period with ID " + 
                        cancelParam.getPeriod().getId() + " was not found.");
                result.setFailureCode(2);
                result.setFailureReason("Period not found.");
                return response;
            }
            
            if (!offline.isActive())
            {
                this.logger.warn("Offline period with ID " + offline.getId() + " is already canceled. Not cancelling " +
                		"it again.");
                result.setFailureCode(3);
                result.setFailureReason("Already cancelled.");
                return response;
            }
            
            result.setSuccessful(true);
            
            /* Cancel the offline period. */
            offline.setActive(false);
            dao.flush();
            
            /* Notify the booking engine. */
            BookingEngineService service = RigManagementActivator.getBookingService();
            if (service != null) service.clearRigOffline(offline, dao.getSession());
            
            /* If the period is currently active, clear the rig maintenance state. */
            Date now = new Date();
            Rig rig = offline.getRig();
            if (rig.isActive() && !rig.isOnline() &&
                    offline.getStartTime().before(now) && offline.getEndTime().after(now))
            {
                this.logger.info("Clearning maintenance state on rig " + offline.getRig().getName() + '.');
                RigManagementActivator.clearMaintenance(rig, dao.getSession());
            }
        }
        finally
        {
            dao.closeSession();
        }
        
        return response;
    }
    
    @Override
    public FreeRigResponse freeRig(FreeRig freeRig)
    {
        KickRigType param = freeRig.getFreeRig();
        this.logger.debug("Received RigManagement#freeRig with params: requestor ID=" + param.getRequestorID() +
                "requestor namespace=" + param.getRequestorNameSpace() + ", requestor name" + param.getRequestorName() +
                ", rig name=" + param.getRig().getName() + ", reason=" + param.getReason() + '.');
        
        FreeRigResponse response = new FreeRigResponse();
        OperationResponseType result = new OperationResponseType();
        response.setFreeRigResponse(result);
        
        RigDao dao = new RigDao();
        try
        {
            if (!this.isAuthorised(param, dao.getSession()))
            {
                this.logger.warn("Unable to free rig because the requesting user is not authorised to perform this " +
                		"operation (not a ADMIN).");
                result.setFailureCode(1);
                result.setFailureReason("No authorised.");
                return response;
            }
            
            Rig rig = dao.findByName(param.getRig().getName());
            if (rig == null)
            {
                this.logger.warn("Unable to free rig because rig with name " + param.getRig().getName() + " was not " +
                		"found.");
                result.setFailureCode(2);
                result.setFailureReason("Rig not found.");
                return response;
            }
            
            if (!rig.isInSession() || rig.getSession() == null)
            {
                this.logger.warn("Unable to free rig with name " + rig.getName() + " because the rig is not in session.");
                result.setFailureCode(3);
                result.setFailureReason("Rig not in session.");
                return response;
            }
            
            result.setSuccessful(true);
            
            au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session ses = rig.getSession();
            this.logger.info("Admin kicking off user " + ses.getUser().qName() + " from rig " + rig.getName() + ". " +
            		"Reason: " + param.getReason() + '.');
            ses.setActive(false);
            ses.setRemovalTime(new Date());
            ses.setRemovalReason("User was kicked off by admin. Reason: " + param.getReason());
            dao.flush();
            RigManagementActivator.notifySessionEvent(SessionEvent.FINISHED, ses, dao.getSession());
            RigManagementActivator.release(ses, dao.getSession());
        }
        finally
        {
            dao.closeSession();
        }
        
        return response;
    }
    
    /**
     * Checks whether the requestor is authorised to perform an operation. The
     * requestors are only authorised if they are admins.
     * 
     * @param request
     * @return true if the requestor is an admin
     */
    private boolean isAuthorised(OperationRequestType request, Session db)
    {
        UserDao dao = new UserDao(db);
        
        String ns = request.getRequestorNameSpace(), name = request.getRequestorName();
        User user = null;
        if (request.getRequestorID() > 0)
        {
            user = dao.get(Long.valueOf(request.getRequestorID()));
        }
        else if (ns != null && name != null)
        {
            user = dao.findByName(ns, name);
        }
        
        if (user == null) return false;
        
        return User.ADMIN.equals(user.getPersona());
    }
}
