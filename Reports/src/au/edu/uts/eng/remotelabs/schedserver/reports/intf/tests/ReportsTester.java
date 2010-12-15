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
 * - 15/12/2010 - tmachet - Initial file creation.
 */
package au.edu.uts.eng.remotelabs.schedserver.reports.intf.tests;

import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import org.hibernate.Session;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClass;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.testsetup.DataAccessTestSetup;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.Reports;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.OperatorType;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryFilterType;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryInfo;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryInfoResponse;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryInfoResponseType;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryInfoType;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.RequestorType;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.TypeForQuery;

/**
 * @author tmachet
 *
 */
public class ReportsTester extends TestCase
{

    private Reports service;

    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        
        DataAccessTestSetup.setup();
        this.service = new Reports();
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.reports.intf.Reports#queryInfo(au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryInfo)}.
     */
    public void testQueryInfo()
    {
        Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        UserClass uclass1 = new UserClass();
        uclass1.setName("reportstestclass");
        uclass1.setActive(true);
        uclass1.setQueuable(false);
        uclass1.setBookable(true);
        uclass1.setTimeHorizon(1000);
        ses.save(uclass1);

        User us1 = new User();
        us1.setName("testuser1");
        us1.setNamespace("REPS");
        us1.setPersona("USER");
        ses.save(us1);
        
        User us2 = new User();
        us2.setName("testuser2");
        us2.setNamespace("REPS");
        us2.setPersona("USER");
        ses.save(us2);
        
        RigType rigType1 = new RigType("reportstestrigtype", 300, false);
        ses.save(rigType1);
        
        ResourcePermission perm1 = new ResourcePermission();
        perm1.setUserClass(uclass1);
        perm1.setType("TYPE");
        perm1.setSessionDuration(3600);
        perm1.setQueueActivityTimeout(300);
        perm1.setAllowedExtensions((short)10);
        perm1.setSessionActivityTimeout(300);
        perm1.setExtensionDuration(300);
        perm1.setMaximumBookings(10);
        perm1.setRigType(rigType1);
        perm1.setStartTime(new Date());
        perm1.setExpiryTime(new Date());
        perm1.setDisplayName("reportsperm");
        ses.save(perm1);
        
        QueryInfo request = new QueryInfo();
        QueryInfoType reqTy = new QueryInfoType();
        
        QueryFilterType qSelect = new QueryFilterType();
        TypeForQuery type = TypeForQuery.RIG;
        OperatorType op = OperatorType.AND;
        qSelect.setTypeForQuery(type);
        qSelect.setOperator(op);
        //qSelect.setQueryLike("test%");
        
        RequestorType user = new RequestorType();
        user.setUserName("testuser1");
        
        reqTy.setQuerySelect(qSelect);
        reqTy.setLimit(10);
        reqTy.setRequestor(user);
        
        request.setQueryInfo(reqTy);
        
        QueryInfoResponse response = this.service.queryInfo(request);
        
        ses.beginTransaction();
        ses.delete(perm1);
        ses.delete(rigType1);
        ses.delete(us2);
        ses.delete(us1);
        ses.delete(uclass1);
        ses.getTransaction().commit();
        
        assertNotNull(response);
        QueryInfoResponseType resp = response.getQueryInfoResponse();
        assertNotNull(resp);
        
        String typeResp = resp.getTypeForQuery().toString();
        assertEquals("RIG", typeResp);
        
        String[] selectResult = resp.getSelectionResult();
        assertNotNull(selectResult);
        assertEquals(1,selectResult.length);
        assertEquals("Structural_Visualisation_1",selectResult[0]);
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.reports.intf.Reports#querySessionAccess(au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionAccess)}.
     */
//    public void testQuerySessionAccess()
//    {
//        fail("Not yet implemented");
//    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.reports.intf.Reports#querySessionReport(au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionReport)}.
     */
//    public void testQuerySessionReport()
//    {
//        fail("Not yet implemented");
//    }

}
