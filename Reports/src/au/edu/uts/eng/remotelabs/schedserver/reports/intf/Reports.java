/**
 * SAHARA Rig Client
 * 
 * Software abstraction of physical rig to provide rig session control
 * and rig device control. Automatically tests rig hardware and reports
 * the rig status to ensure rig goodness.
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
 * @author <First> <Last> (tmachet)
 * @date <Day> <Month> 2010
 *
 * Changelog:
 * - 13/12/2010 - tmachet - Initial file creation.
 */
package au.edu.uts.eng.remotelabs.schedserver.reports.intf;

import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigTypeDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.AcademicPermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RequestCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClass;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.AccessReportType;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.PaginationType;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryFilterType;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryInfo;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryInfoResponse;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryInfoResponseType;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryInfoType;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionAccess;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionAccessResponse;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionAccessResponseType;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionAccessType;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionReport;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionReportResponse;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.RequestorType;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.TypeForQuery;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.UserNSNameSequence;


/**
 * @author tmachet
 *
 */
public class Reports implements ReportsSkeletonInterface
{
    /** Logger. */
    private Logger logger;
    
    
    public Reports()
    {
        this.logger = LoggerActivator.getLogger();
    }

    @SuppressWarnings("unchecked")
    @Override
    public QueryInfoResponse queryInfo(QueryInfo queryInfo)
    {
        /** Request parameters. **/
        QueryInfoType qIReq = queryInfo.getQueryInfo();
        String debug = "Received " + this.getClass().getSimpleName() + "#queryInfo with params:";
            debug += "Requestor: " + qIReq.getRequestor() + ", QuerySelect: " + qIReq.getQuerySelect().toString(); 
            if(qIReq.getQueryFilter() != null) debug += ", QueryFilter: " + qIReq.getQueryFilter().toString();
            debug += ", limit: " + qIReq.getLimit(); 
        this.logger.debug(debug);
        RequestorType uid = qIReq.getRequestor();
        
        /** Response parameters. */
        QueryInfoResponse resp = new QueryInfoResponse();
        QueryInfoResponseType respType = new QueryInfoResponseType();
        respType.setTypeForQuery(TypeForQuery.RIG);
        resp.setQueryInfoResponse(respType);
        
        org.hibernate.Session ses = DataAccessActivator.getNewSession();

        /* First Query Filter - this contains the main selection type to be selected */        
        QueryFilterType query0 = qIReq.getQuerySelect();

        try
        {
            Criteria cri;

            /* ----------------------------------------------------------------
             * -- Load the requestor.                                             --
             * ---------------------------------------------------------------- */
            User user = this.getUserFromUserID(uid, ses);
            if (user == null)
            {
                this.logger.info("Unable to generate report because the user has not been found. Supplied " +
                        "credentials ID=" + uid.getUserID() + ", namespace=" + uid.getUserNamespace() + ", " +
                        "name=" + uid.getUserName() + '.');
                return resp;
            }
            String persona = user.getPersona();
 
            //Get table to be queried
            if(query0.getTypeForQuery() == TypeForQuery.RIG)
            {
                //Rig Information only available to ADMIN, to be mediated by interface
                cri = ses.createCriteria(Rig.class);
                if(query0.getQueryLike() != null)  cri.add(Restrictions.like("name", query0.getQueryLike()));
                cri.setMaxResults(qIReq.getLimit());
                cri.addOrder(Order.asc("name"));
                List<Rig> list = cri.list();
                for (Rig o : list)
                {
                    respType.addSelectionResult(o.getName());
                }
            }
            else if(query0.getTypeForQuery() == TypeForQuery.RIG_TYPE)
            {
                //Rig Type Information only available to ADMIN, to be mediated by interface                cri = ses.createCriteria(RigType.class);
                cri = ses.createCriteria(RigType.class);

                if(query0.getQueryLike() != null)  cri.add(Restrictions.like("name", query0.getQueryLike()));
                cri.setMaxResults(qIReq.getLimit());
                cri.addOrder(Order.asc("name"));
                List<RigType> list = cri.list();
                for (RigType o : list)
                {
                    respType.addSelectionResult(o.getName());
                }
            }
            else if(query0.getTypeForQuery() == TypeForQuery.USER_CLASS)
            {
                cri = ses.createCriteria(UserClass.class);

                if(query0.getQueryLike() != null)  cri.add(Restrictions.like("name", query0.getQueryLike()));
                cri.setMaxResults(qIReq.getLimit());
                cri.addOrder(Order.asc("name"));
                List<UserClass> list = cri.list();
                for (UserClass o : list)
                {
                    /* ----------------------------------------------------------------
                     * Check that the requestor has permissions to request the report.
                     * If persona = USER, no reports (USERs will not get here)
                     * If persona = ADMIN, any report 
                     * If persona = ACADEMIC, only for classes they own if they can genrate reports
                     * ---------------------------------------------------------------- */

                    if (User.ACADEMIC.equals(persona))
                    {
                        /* An academic may generate reports for their own classes only. */
                        boolean hasPerm = false;
                            
                        Iterator<AcademicPermission> apIt = user.getAcademicPermissions().iterator();
                        while (apIt.hasNext())
                        {
                            AcademicPermission ap = apIt.next();
                            if (ap.getUserClass().getId().equals(o.getId()) && ap.isCanGenerateReports())
                            {
                                hasPerm = true;
                                break;
                             }    
                        }
                            
                        if (!hasPerm)
                        {
                            this.logger.info("Unable to generate report for user class " + o.getName() + 
                                    " because the user " + user.getNamespace() + ':' + user.getName() +
                                    " does not own or have academic permission to it.");
                            continue;
                        }
                            
                        this.logger.debug("Academic " + user.getNamespace() + ':' + user.getName() + " has permission to " +
                                   "generate report from user class" + o.getName() + '.');
                    }
                    
                    respType.addSelectionResult(o.getName());
                }
            }
            else if(query0.getTypeForQuery() == TypeForQuery.USER)
            {
                cri = ses.createCriteria(User.class);

                /* ----------------------------------------------------------------
                 * TODO Check that the requestor has permissions to request the report.
                 * If persona = USER, no reports (USERs will not get here)
                 * If persona = ADMIN, any report 
                 * If persona = ACADEMIC, only for users in classes they own if they can genrate reports
                 * 
                 *  NOTE generate academics class list
                 *  Get users in each class
                 *  Compare to users matching search criteria
                 * ---------------------------------------------------------------- */

                
                if(query0.getQueryLike() != null)  cri.add(Restrictions.like("name", query0.getQueryLike()));
                cri.setMaxResults(qIReq.getLimit());
                cri.addOrder(Order.asc("name"));
                List<User> userList = cri.list();
                for (User o : userList)
                {
                    respType.addSelectionResult(o.getNamespace() + ':' + o.getName());
                }
            }
            else if(query0.getTypeForQuery() == TypeForQuery.REQUEST_CAPABILITIES)
            {
                cri = ses.createCriteria(RequestCapabilities.class);
                if(query0.getQueryLike() != null)  cri.add(Restrictions.like("capabilities", query0.getQueryLike()));
                cri.setMaxResults(qIReq.getLimit());
                cri.addOrder(Order.asc("capabilities"));
                List<RequestCapabilities> list = cri.list();
                for (RequestCapabilities o : list)
                {
                    respType.addSelectionResult(o.getCapabilities());
                }
            }
            else 
            {
                this.logger.error("QueryInfo request failed because TypeForQuery does not have a valid value.  Value is " +
                        query0.getTypeForQuery().toString());
                return resp;
            }
            
            QueryFilterType filter[] = qIReq.getQueryFilter(); 
            if(filter != null)
            {
                //DODGY Designed, not implemented
            }
        }
        finally
        {
            ses.close();
        }
        
        respType.setTypeForQuery(query0.getTypeForQuery());
        resp.setQueryInfoResponse(respType);
        
        return resp;
    }

    /* (non-Javadoc)
     * @see au.edu.uts.eng.remotelabs.schedserver.reports.intf.ReportsSkeletonInterface#querySessionAccess(au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionAccess)
     */
    @SuppressWarnings({ "unchecked" })
    @Override
    public QuerySessionAccessResponse querySessionAccess(QuerySessionAccess querySessionAccess)
    {
        /** Request parameters. **/
        QuerySessionAccessType qSAReq = querySessionAccess.getQuerySessionAccess();
        String debug = "Received " + this.getClass().getSimpleName() + "#querySessionAccess with params:";
            debug += "Requestor: " + qSAReq.getRequestor() + ", QuerySelect: " + qSAReq.getQuerySelect().toString(); 
            if(qSAReq.getQueryConstraints() != null) debug += ", QueryConstraints: " + qSAReq.getQueryConstraints().toString();  //DODGY only first displayed
            debug += ", start time: " + qSAReq.getStartTime() + ", end time: " + qSAReq.getEndTime() + ", pagination: " + qSAReq.getPagination();
        this.logger.debug(debug);
        
        /** Response parameters. */
        QuerySessionAccessResponse resp = new QuerySessionAccessResponse();
        QuerySessionAccessResponseType respType = new QuerySessionAccessResponseType();
        PaginationType page = new PaginationType();
        page.setNumberOfPages(1);
        page.setPageLength(30);
        page.setPageNumber(1);
        respType.setPagination(page);
        resp.setQuerySessionAccessResponse(respType);
        
        AccessReportType reportType = new AccessReportType();
        
        org.hibernate.Session ses = DataAccessActivator.getNewSession();

        /* Set up query from request parameters*/
        try
        {
            Criteria cri = ses.createCriteria(Session.class);
            cri.addOrder(Order.asc("requestTime"));

            /* TODO use ID to restrict query
             * Check permission method that looks for
             * ADMIN anything
             * ACADEMIC - get permissions
             *    check canGenerateReports
             * others - none
             */
         
            /* First Query Filter - this contains grouping for the query */        
            QueryFilterType query0 = qSAReq.getQuerySelect();
            //Get table to be queried
            if(query0.getTypeForQuery() == TypeForQuery.RIG)
            {
                cri.add(Restrictions.eq("assignedRigName", query0.getQueryLike()));
                cri.add(Restrictions.between("requestTime", qSAReq.getStartTime().getTime(), qSAReq.getEndTime().getTime()));
                List<Session> list = cri.list();
                for (Session o : list)
                {
                    //Get user from session object
                    RequestorType user0 = new RequestorType();
                    UserNSNameSequence nsSequence = new UserNSNameSequence();
                    nsSequence.setUserName(o.getUser().getName());
                    nsSequence.setUserNamespace(o.getUser().getNamespace());
                    user0.setRequestorTypeSequence_type0(nsSequence);
                    reportType.setUser(user0);
                                        
                    reportType.setRigName(query0.getQueryLike());
                    
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(o.getRequestTime());
                    reportType.setQueueStartTime(cal);

                    if(o.getAssignmentTime() != null)
                    {
                        int queueD = (int) ((o.getAssignmentTime().getTime() - o.getRequestTime().getTime())/1000);
                        reportType.setQueueDuration(queueD);
                        cal = Calendar.getInstance();
                        cal.setTime(o.getAssignmentTime());
                        reportType.setSessionStartTime(cal);

                        int sessionD = (int) ((o.getRemovalTime().getTime() - o.getAssignmentTime().getTime())/1000);
                        reportType.setSessionDuration(sessionD);
                        cal = Calendar.getInstance();
                        cal.setTime(o.getRemovalTime());
                        reportType.setSessionEndTime(cal);
                    }
                    else
                    {
                        int queueD = (int) ((o.getRemovalTime().getTime() - o.getRequestTime().getTime())/1000);
                        reportType.setQueueDuration(queueD);
                        reportType.setSessionDuration(0);
                    }
                    
                    respType.addAccessReportData(reportType);
                }
            }
            else if(query0.getTypeForQuery() == TypeForQuery.RIG_TYPE)
            {
                RigTypeDao rTypeDAO = new RigTypeDao(ses);
                RigType rType = rTypeDAO.findByName(query0.getQueryLike());
                cri = ses.createCriteria(Session.class);
                if (rType == null)
                {
                    this.logger.warn("No valid rig type found - " + query0.getTypeForQuery().toString());
                    return resp;
                }
                cri.createCriteria("rig").add(Restrictions.eq("rigType",rType));
                List<Session> list = cri.list();
                for (Session o : list)
                {
                    //Get user from session object
                    RequestorType user0 = new RequestorType();
                    UserNSNameSequence nsSequence = new UserNSNameSequence();
                    nsSequence.setUserName(o.getUser().getName());
                    nsSequence.setUserNamespace(o.getUser().getNamespace());
                    user0.setRequestorTypeSequence_type0(nsSequence);
                    reportType.setUser(user0);

                    reportType.setRigType(query0.getQueryLike());
                    
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(o.getRequestTime());
                    reportType.setQueueStartTime(cal);
                    
                    if(o.getAssignmentTime() != null)
                    {
                        int queueD = (int) ((o.getAssignmentTime().getTime() - o.getRequestTime().getTime())/1000);
                        reportType.setQueueDuration(queueD);
                        cal = Calendar.getInstance();
                        cal.setTime(o.getAssignmentTime());
                        reportType.setSessionStartTime(cal);
                        int sessionD = (int) ((o.getRemovalTime().getTime() - o.getAssignmentTime().getTime())/1000);
                        reportType.setSessionDuration(sessionD);
                        cal = Calendar.getInstance();
                        cal.setTime(o.getRemovalTime());
                        reportType.setSessionEndTime(cal);
                    }
                    else
                    {
                        int queueD = (int) ((o.getRemovalTime().getTime() - o.getRequestTime().getTime())/1000);
                        reportType.setQueueDuration(queueD);
                        reportType.setSessionDuration(0);
                    }
                    
                    respType.addAccessReportData(reportType);
                }
                
            }
            else if(query0.getTypeForQuery() == TypeForQuery.USER_CLASS)
            {
                // TODO  Does it make sense to have user class and not permissions here???
            }
            else if(query0.getTypeForQuery() == TypeForQuery.USER)
            {
                cri.add(Restrictions.like("user", query0.getQueryLike()));
            }
            else if(query0.getTypeForQuery() == TypeForQuery.REQUEST_CAPABILITIES)
            {
                this.logger.error("Request capabilities not implemented yet - " +
                        query0.getTypeForQuery().toString());
                return resp;
            }
            else 
            {
                this.logger.error("queryAccessReport request failed because TypeForQuery does not have a valid value.  Value is " +
                        query0.getTypeForQuery().toString());
                return resp;
            }
            

            
        }
        finally
        {
            ses.close();
        }
        
        return resp;    
    }
    

    /* (non-Javadoc)
     * @see au.edu.uts.eng.remotelabs.schedserver.reports.intf.ReportsSkeletonInterface#querySessionReport(au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionReport)
     */
    @Override
    public QuerySessionReportResponse querySessionReport(QuerySessionReport querySessionReport)
    {
        // TODO Auto-generated method stub
        return null;
    }

    
    /**
     * Gets the user identified by the user id type. 
     * 
     * @param uid user identity 
     * @param ses database session
     * @return user or null if not found
     */
    private User getUserFromUserID(RequestorType uid, org.hibernate.Session ses)
    {
        UserDao dao = new UserDao(ses);
        User user;
        
        long recordId = this.getIdentifier(uid.getUserID());
        String ns = uid.getUserNamespace(), nm = uid.getUserName();
        
        if (recordId > 0 && (user = dao.get(recordId)) != null)
        {
            return user;
        }
        else if (ns != null && nm != null && (user = dao.findByName(ns, nm)) != null)
        {
            return user;
        }
        
        return null;
    }
    
    /**
     * Converts string identifiers to a long.
     * 
     * @param idStr string containing a long  
     * @return long or 0 if identifier not valid
     */
    private long getIdentifier(String idStr)
    {
        if (idStr == null) return 0;
        
        try
        {
            return Long.parseLong(idStr);
        }
        catch (NumberFormatException nfe)
        {
            return 0;
        }
    }


}
