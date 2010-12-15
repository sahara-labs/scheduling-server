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

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RequestCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClass;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryFilterType;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryInfo;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryInfoResponse;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryInfoResponseType;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryInfoType;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionAccess;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionAccessResponse;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionReport;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionReportResponse;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.TypeForQuery;


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
        
        /** Response parameters. */
        QueryInfoResponse resp = new QueryInfoResponse();
        QueryInfoResponseType respType = new QueryInfoResponseType();
        respType.setTypeForQuery(TypeForQuery.RIG);
        resp.setQueryInfoResponse(respType);
        
        Session ses = DataAccessActivator.getNewSession();

        /* First Query Filter - this contains the main selection type to be selected */        
        QueryFilterType query0 = qIReq.getQuerySelect();

        try
        {
            Criteria cri;

            //Get table to be queried
            if(query0.getTypeForQuery() == TypeForQuery.RIG)
            {
                cri = ses.createCriteria(Rig.class);
            }
            else if(query0.getTypeForQuery() == TypeForQuery.RIG_TYPE)
            {
                cri = ses.createCriteria(RigType.class);
            }
            else if(query0.getTypeForQuery() == TypeForQuery.USER_CLASS)
            {
                cri = ses.createCriteria(UserClass.class);
            }
            else if(query0.getTypeForQuery() == TypeForQuery.USER)
            {
                cri = ses.createCriteria(User.class);
            }
            else if(query0.getTypeForQuery() == TypeForQuery.REQUEST_CAPABILITIES)
            {
                cri = ses.createCriteria(RequestCapabilities.class);
            }
            else 
            {
                this.logger.error("QueryInfo request failed because TypeForQuery does not have a valid value.  Value is " +
                        query0.getTypeForQuery().toString());
                return resp;
            }
            
            /* TODO use ID to restrict query
            * Check permission method that looks for
            * ADMIN anything
            * ACADEMIC - get permissions
            *    check canGenerateReports
            * others - none
            */
        
            if(query0.getQueryLike() != null)
            {
                cri.add(Restrictions.like("name", query0.getQueryLike()));
            }

            QueryFilterType filter[] = qIReq.getQueryFilter(); 
            if(filter != null)
            {
                //DODGY Designed, not implemented
            }
            
            cri.setMaxResults(qIReq.getLimit());
            
            @SuppressWarnings("unchecked")
            List<User> list = cri.list();
            for (User user : list)
            {
                respType.addSelectionResult(user.toString());
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
    @Override
    public QuerySessionAccessResponse querySessionAccess(QuerySessionAccess querySessionAccess)
    {
        // TODO Auto-generated method stub
        return null;
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

}
