/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2009, University of Technology, Sydney
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
 * @author Tania Machet (tmachet)
 * @date 29th November 2010
 */

package au.edu.uts.eng.remotelabs.schedserver.reports.intf.tests;

import java.util.Calendar;
import java.util.Date;

import junit.framework.TestCase;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.AcademicPermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserAssociation;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserAssociationId;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClass;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.testsetup.DataAccessTestSetup;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.Reports;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.AccessReportType;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.OperatorType;
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
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionReportResponseType;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionReportType;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.RequestorType;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.SessionReportType;
import au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.TypeForQuery;

/**
 * Tests the {@link Reports} service.
 */
public class ReportsTester extends TestCase
{
    /** Object of class under test. */
    private Reports service;

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        
        DataAccessTestSetup.setup();
        this.service = new Reports();
    }

    public void testQuerySessionAccess() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 10000);
        Date after = new Date(System.currentTimeMillis() + 10000);
        Date now = new Date();
        
        db.beginTransaction();
        
        User us1 = new User();
        us1.setName("testuser1");
        us1.setNamespace("REPS");
        us1.setPersona("ADMIN");
        us1.setEmail("user1@uts");
        us1.setFirstName("user");
        us1.setLastName("test");
        db.save(us1);
        
        User us2 = new User();
        us2.setName("testuser2");
        us2.setNamespace("REPS");
        us2.setPersona("ADMIN");
        us2.setEmail("user2@uts");
        us2.setFirstName("user");
        us2.setLastName("test");
        db.save(us2);

        User user = new User("sessiontest", "testns", "ADMIN");
        user.setEmail("user@uts");
        user.setFirstName("user");
        user.setLastName("test");
        db.save(user);
        
        UserClass uc1 = new UserClass();
        uc1.setName("sessclass");
        uc1.setActive(true);
        uc1.setQueuable(true);
        uc1.setPriority((short)4);
        db.save(uc1);
        
        UserAssociation ass = new UserAssociation(new UserAssociationId(user.getId(), uc1.getId()), uc1, user);
        db.save(ass);
        
        RigType rt = new RigType();
        rt.setName("Session_Test_Rig_Type");
        db.save(rt);
        
        RigCapabilities caps = new RigCapabilities("session,test,rig,type");
        db.save(caps);
        
        Rig r = new Rig();
        r.setName("Session_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.save(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("RIG");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRig(r);
        p1.setAllowedExtensions((short)10);
        db.save(p1);
        
        Session ses = new Session();
        ses.setActive(true);
        ses.setReady(true);
        ses.setActivityLastUpdated(now);
        ses.setExtensions((short) 5);
        ses.setPriority((short) 5);
        ses.setRequestTime(now);
        ses.setRequestedResourceId(r.getId());
        ses.setRequestedResourceName(r.getName());
        ses.setResourceType("RIG");
        ses.setResourcePermission(p1);
        ses.setRemovalTime(after);
        ses.setUser(user);
        ses.setUserName(user.getName());
        ses.setUserNamespace(user.getNamespace());
        ses.setAssignedRigName(r.getName());
        ses.setAssignmentTime(now);
        ses.setRig(r);
        db.save(ses);
        db.getTransaction().commit();
        
        db.beginTransaction();
        db.refresh(uc1);
        db.refresh(us1);
        db.refresh(us2);
        db.refresh(user);
        db.refresh(p1);
        db.refresh(r);
        db.refresh(rt);
        db.getTransaction().commit();

        
        // Set up test parameters
        QuerySessionAccess qSA = new QuerySessionAccess();
        QuerySessionAccessType qSAType = new QuerySessionAccessType();
        
        RequestorType user1 = new RequestorType();
        user1.setUserQName("REPS:testuser1");
        qSAType.setRequestor(user1);
        
        QueryFilterType qSelect = new QueryFilterType();
        TypeForQuery type = TypeForQuery.RIG;
        OperatorType op = OperatorType.AND;
        qSelect.setTypeForQuery(type);
        qSelect.setOperator(op);
        qSelect.setQueryLike("Session_Rig_Test_Rig1");
        qSAType.setQuerySelect(qSelect);

        Calendar start = Calendar.getInstance();
        start.add(Calendar.MONTH, -1);
        qSAType.setStartTime(start);
        Calendar end = Calendar.getInstance();
        end.add(Calendar.MONTH, 2);
        qSAType.setEndTime(end);
        
        qSA.setQuerySessionAccess(qSAType);
        
        QuerySessionAccessResponse response = this.service.querySessionAccess(qSA);

        // Delete test data
        db.beginTransaction();
        db.delete(ses);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass);
        db.delete(uc1);
        db.delete(user);
        db.delete(us2);
        db.delete(us1);
        db.getTransaction().commit();

        assertNotNull(response);
        QuerySessionAccessResponseType resp = response.getQuerySessionAccessResponse();
        assertNotNull(resp);
        
        PaginationType page = resp.getPagination();
        assertNotNull(page);
        
        AccessReportType[] rep = resp.getAccessReportData();
        assertNotNull(rep);
        
        OMElement ele = response.getOMElement(QuerySessionAccessResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }

    public void testQueryInfoAcademicPermissionsUserClass() throws Exception
    {
        org.hibernate.Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        UserClass uclass1 = new UserClass();
        uclass1.setName("reportstestclass");
        uclass1.setActive(true);
        uclass1.setQueuable(false);
        uclass1.setBookable(true);
        uclass1.setTimeHorizon(1000);
        ses.save(uclass1);

        UserClass uclass2 = new UserClass();
        uclass2.setName("reportstest2");
        uclass2.setActive(true);
        uclass2.setQueuable(false);
        uclass2.setBookable(true);
        uclass2.setTimeHorizon(1000);
        ses.save(uclass2);

        User us1 = new User();
        us1.setName("testuser1");
        us1.setNamespace("REPS");
        us1.setPersona("ACADEMIC");
        us1.setEmail("user1@uts");
        us1.setFirstName("user");
        us1.setLastName("test");
        ses.save(us1);
        
        User us2 = new User();
        us2.setName("testuser2");
        us2.setNamespace("REPS");
        us2.setPersona("USER");
        us2.setEmail("user2@uts");
        us2.setFirstName("user");
        us2.setLastName("test");
        ses.save(us2);
        
        AcademicPermission ap1 = new AcademicPermission(us1, uclass1, true, true, true, true, true);
        ses.save(ap1);
        AcademicPermission ap2 = new AcademicPermission(us1, uclass2, true, true, true, true, false);
        ses.save(ap2);
        
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
        ses.getTransaction().commit();
        
        QueryInfo request = new QueryInfo();
        QueryInfoType reqTy = new QueryInfoType();
        
        QueryFilterType qSelect = new QueryFilterType();
        TypeForQuery type = TypeForQuery.USER_CLASS;
        OperatorType op = OperatorType.AND;
        qSelect.setTypeForQuery(type);
        qSelect.setOperator(op);
        qSelect.setQueryLike("rep%");
        
        RequestorType user = new RequestorType();
        user.setUserQName("REPS:testuser1");
        
        reqTy.setQuerySelect(qSelect);
        reqTy.setLimit(10);
        reqTy.setRequestor(user);
        
        request.setQueryInfo(reqTy);
        
        QueryInfoResponse response = this.service.queryInfo(request);
        
        ses.beginTransaction();
        ses.delete(perm1);
        ses.delete(rigType1);
        ses.delete(ap2);
        ses.delete(ap1);
        ses.delete(us2);
        ses.delete(us1);
        ses.delete(uclass2);
        ses.delete(uclass1);
        ses.getTransaction().commit();
        
        assertNotNull(response);
        QueryInfoResponseType resp = response.getQueryInfoResponse();
        assertNotNull(resp);
        
        String typeResp = resp.getTypeForQuery().toString();
        assertEquals("USER_CLASS", typeResp);
        
        String[] selectResult = resp.getSelectionResult();
        assertNotNull(selectResult);
        assertEquals(1,selectResult.length);
        assertEquals(uclass1.getName(),selectResult[0]);
    }

    public void testQuerySessionAccessTimePeriod() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 30000);
        Date after = new Date(System.currentTimeMillis() + 20000);
        Date later = new Date(System.currentTimeMillis() + 70000);
        Date now = new Date();
        
        db.beginTransaction();
        
        User user = new User("testuser1", "REPS", "ADMIN");
        user.setEmail("user1@uts");
        user.setFirstName("user");
        user.setLastName("test");
        db.save(user);
        
        UserClass uc1 = new UserClass();
        uc1.setName("sessclass");
        uc1.setActive(true);
        uc1.setQueuable(true);
        uc1.setPriority((short)4);
        db.save(uc1);
        
        UserAssociation ass = new UserAssociation(new UserAssociationId(user.getId(), uc1.getId()), uc1, user);
        db.save(ass);
        
        RigType rt = new RigType();
        rt.setName("Session_Test_Rig_Type");
        db.save(rt);
        
        RigCapabilities caps = new RigCapabilities("session,test,rig,type");
        db.save(caps);
        
        Rig r = new Rig();
        r.setName("Session_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.save(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("RIG");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRig(r);
        p1.setAllowedExtensions((short)10);
        db.save(p1);
        
        Session ses = new Session();
        ses.setActive(true);
        ses.setReady(true);
        ses.setActivityLastUpdated(now);
        ses.setExtensions((short) 5);
        ses.setPriority((short) 5);
        ses.setRequestTime(now);
        ses.setRequestedResourceId(r.getId());
        ses.setRequestedResourceName(r.getName());
        ses.setResourceType("RIG");
        ses.setResourcePermission(p1);
        ses.setRemovalTime(after);
        ses.setUser(user);
        ses.setUserName(user.getName());
        ses.setUserNamespace(user.getNamespace());
        ses.setAssignedRigName(r.getName());
        ses.setAssignmentTime(now);
        ses.setRig(r);
        db.persist(ses);

        
        Session ses2 = new Session();
        ses2.setActive(true);
        ses2.setReady(true);
        ses2.setActivityLastUpdated(later);
        ses2.setExtensions((short) 5);
        ses2.setPriority((short) 5);
        ses2.setRequestTime(before);
        ses2.setRequestedResourceId(r.getId());
        ses2.setRequestedResourceName(r.getName());
        ses2.setResourceType("RIG");
        ses2.setResourcePermission(p1);
        ses2.setRemovalTime(later);
        ses2.setUser(user);
        ses2.setUserName(user.getName());
        ses2.setUserNamespace(user.getNamespace());
        ses2.setAssignedRigName(r.getName());
        ses2.setAssignmentTime(after);
        ses2.setRig(r);
        db.persist(ses2);
        db.getTransaction().commit();
        
        db.beginTransaction();
        db.refresh(uc1);
        db.refresh(user);
        db.refresh(p1);
        db.refresh(r);
        db.refresh(rt);
        db.getTransaction().commit();

        
        // Set up test parameters
        QuerySessionAccess qSA = new QuerySessionAccess();
        QuerySessionAccessType qSAType = new QuerySessionAccessType();
        
        RequestorType user1 = new RequestorType();
        user1.setUserQName("REPS:testuser1");
        qSAType.setRequestor(user1);
        
        QueryFilterType qSelect = new QueryFilterType();
        TypeForQuery type = TypeForQuery.RIG;
        OperatorType op = OperatorType.AND;
        qSelect.setTypeForQuery(type);
        qSelect.setOperator(op);
        qSelect.setQueryLike("Session_Rig_Test_Rig1");
        qSAType.setQuerySelect(qSelect);

        Calendar start = Calendar.getInstance();
        start.add(Calendar.MONTH, -1);
        qSAType.setStartTime(start);
        Calendar end = Calendar.getInstance();
        end.add(Calendar.MONTH, 2);
        qSAType.setEndTime(end);
        
        qSA.setQuerySessionAccess(qSAType);
        
        QuerySessionAccessResponse response = this.service.querySessionAccess(qSA);

        // Delete test data
        db.beginTransaction();
        db.delete(ses2);
        db.delete(ses);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass);
        db.delete(uc1);
        db.delete(user);
        db.getTransaction().commit();

        assertNotNull(response);
        QuerySessionAccessResponseType resp = response.getQuerySessionAccessResponse();
        assertNotNull(resp);
        
        PaginationType page = resp.getPagination();
        assertNotNull(page);
        
        AccessReportType[] rep = resp.getAccessReportData();
        assertNotNull(rep);
        
        //rep[0] is ses2 - ordered by request time
        Calendar queueStart = rep[0].getQueueStartTime();
        assertEquals(before.getTime()/1000, queueStart.getTimeInMillis()/1000);
        Calendar sessionStart = rep[0].getSessionStartTime();
        assertEquals(after.getTime()/1000, sessionStart.getTimeInMillis()/1000);
        Calendar sessionEnd = rep[0].getSessionEndTime();
        assertEquals(later.getTime()/1000, sessionEnd.getTimeInMillis()/1000);
        assertEquals((after.getTime()- before.getTime())/1000, rep[0].getQueueDuration());
        assertEquals((later.getTime()- after.getTime())/1000, rep[0].getSessionDuration());

        Calendar queueStart1 = rep[1].getQueueStartTime();
        assertEquals(now.getTime()/1000, queueStart1.getTimeInMillis()/1000);
        Calendar sessionStart1 = rep[1].getSessionStartTime();
        assertEquals(now.getTime()/1000, sessionStart1.getTimeInMillis()/1000);
        Calendar sessionEnd1 = rep[1].getSessionEndTime();
        assertEquals(after.getTime()/1000, sessionEnd1.getTimeInMillis()/1000);
        assertEquals((now.getTime()- now.getTime())/1000, rep[1].getQueueDuration());
        assertEquals((after.getTime()-now.getTime())/1000, rep[1].getSessionDuration());


        OMElement ele = response.getOMElement(QuerySessionAccessResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }

    public void testQuerySessionAccessPagination() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 30000);
        Date after = new Date(System.currentTimeMillis() + 20000);
        Date later = new Date(System.currentTimeMillis() + 70000);
        Date now = new Date();
        
        db.beginTransaction();
        
        User user = new User("testuser1", "REPS", "ADMIN");
        user.setEmail("user1@uts");
        user.setFirstName("user");
        user.setLastName("test");
        db.persist(user);
        
        UserClass uc1 = new UserClass();
        uc1.setName("sessclass");
        uc1.setActive(true);
        uc1.setQueuable(true);
        uc1.setPriority((short)4);
        db.persist(uc1);
        
        UserAssociation ass = new UserAssociation(new UserAssociationId(user.getId(), uc1.getId()), uc1, user);
        db.persist(ass);
        
        RigType rt = new RigType();
        rt.setName("Session_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("session,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Session_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("RIG");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRig(r);
        p1.setAllowedExtensions((short)10);
        db.persist(p1);
        
        Session ses = new Session();
        ses.setActive(true);
        ses.setReady(true);
        ses.setActivityLastUpdated(now);
        ses.setExtensions((short) 5);
        ses.setPriority((short) 5);
        ses.setRequestTime(now);
        ses.setRequestedResourceId(r.getId());
        ses.setRequestedResourceName(r.getName());
        ses.setResourceType("RIG");
        ses.setResourcePermission(p1);
        ses.setRemovalTime(after);
        ses.setUser(user);
        ses.setUserName(user.getName());
        ses.setUserNamespace(user.getNamespace());
        ses.setAssignedRigName(r.getName());
        ses.setAssignmentTime(now);
        ses.setRig(r);
        db.persist(ses);

        
        Session ses2 = new Session();
        ses2.setActive(true);
        ses2.setReady(true);
        ses2.setActivityLastUpdated(later);
        ses2.setExtensions((short) 5);
        ses2.setPriority((short) 5);
        ses2.setRequestTime(before);
        ses2.setRequestedResourceId(r.getId());
        ses2.setRequestedResourceName(r.getName());
        ses2.setResourceType("RIG");
        ses2.setResourcePermission(p1);
        ses2.setRemovalTime(later);
        ses2.setUser(user);
        ses2.setUserName(user.getName());
        ses2.setUserNamespace(user.getNamespace());
        ses2.setAssignedRigName(r.getName());
        ses2.setAssignmentTime(after);
        ses2.setRig(r);
        db.persist(ses2);
        db.getTransaction().commit();
        
        db.beginTransaction();
        db.refresh(uc1);
        db.refresh(user);
        db.refresh(p1);
        db.refresh(r);
        db.refresh(rt);
        db.getTransaction().commit();

        
        // Set up test parameters
        QuerySessionAccess qSA = new QuerySessionAccess();
        QuerySessionAccessType qSAType = new QuerySessionAccessType();
        
        RequestorType user1 = new RequestorType();
        user1.setUserQName("REPS:testuser1");
        qSAType.setRequestor(user1);
        
        QueryFilterType qSelect = new QueryFilterType();
        TypeForQuery type = TypeForQuery.RIG;
        OperatorType op = OperatorType.AND;
        qSelect.setTypeForQuery(type);
        qSelect.setOperator(op);
        qSelect.setQueryLike("Session_Rig_Test_Rig1");
        qSAType.setQuerySelect(qSelect);

        Calendar start = Calendar.getInstance();
        start.add(Calendar.MONTH, -1);
        qSAType.setStartTime(start);
        Calendar end = Calendar.getInstance();
        end.add(Calendar.MONTH, 2);
        qSAType.setEndTime(end);
        
        qSA.setQuerySessionAccess(qSAType);
        
        PaginationType pages = new PaginationType();
        pages.setNumberOfPages(2);
        pages.setPageLength(1);
        pages.setPageNumber(2);
        qSAType.setPagination(pages);
        
        // Get response
        QuerySessionAccessResponse response = this.service.querySessionAccess(qSA);

        // Delete test data
        db.beginTransaction();
        db.delete(ses2);
        db.delete(ses);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass);
        db.delete(uc1);
        db.delete(user);
        db.getTransaction().commit();

        assertNotNull(response);
        QuerySessionAccessResponseType resp = response.getQuerySessionAccessResponse();
        assertNotNull(resp);
        
        PaginationType page = resp.getPagination();
        assertNotNull(page);
        
        AccessReportType[] rep = resp.getAccessReportData();
        assertNotNull(rep);
        
        //rep[0] is ses2 - ordered by request time
 /*      Calendar queueStart = rep[0].getQueueStartTime();
        assertEquals(before.getTime()/1000, queueStart.getTimeInMillis()/1000);
        Calendar sessionStart = rep[0].getSessionStartTime();
        assertEquals(after.getTime()/1000, sessionStart.getTimeInMillis()/1000);
        Calendar sessionEnd = rep[0].getSessionEndTime();
        assertEquals(later.getTime()/1000, sessionEnd.getTimeInMillis()/1000);
        assertEquals((after.getTime()- before.getTime())/1000, rep[0].getQueueDuration());
        assertEquals((later.getTime()- after.getTime())/1000, rep[0].getSessionDuration());
*/
        Calendar queueStart1 = rep[0].getQueueStartTime();
        assertEquals(now.getTime()/1000, queueStart1.getTimeInMillis()/1000);
        Calendar sessionStart1 = rep[0].getSessionStartTime();
        assertEquals(now.getTime()/1000, sessionStart1.getTimeInMillis()/1000);
        Calendar sessionEnd1 = rep[0].getSessionEndTime();
        assertEquals(after.getTime()/1000, sessionEnd1.getTimeInMillis()/1000);
        assertEquals((now.getTime()- now.getTime())/1000, rep[0].getQueueDuration());
        assertEquals((after.getTime()-now.getTime())/1000, rep[0].getSessionDuration());

        assertEquals(rep.length,1);
        
        OMElement ele = response.getOMElement(QuerySessionAccessResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }

    public void testQuerySessionAccessRigTypeTimePeriod() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 30000);
        Date after = new Date(System.currentTimeMillis() + 20000);
        Date later = new Date(System.currentTimeMillis() + 70000);
        Date now = new Date();
        
        db.beginTransaction();
        
        User user = new User("testuser1", "REPS", "ADMIN");
        user.setEmail("user1@uts");
        user.setFirstName("user");
        user.setLastName("test");
        db.persist(user);
        
        UserClass uc1 = new UserClass();
        uc1.setName("sessclass");
        uc1.setActive(true);
        uc1.setQueuable(true);
        uc1.setPriority((short)4);
        db.persist(uc1);
        
        UserAssociation ass = new UserAssociation(new UserAssociationId(user.getId(), uc1.getId()), uc1, user);
        db.persist(ass);
        
        RigType rt = new RigType();
        rt.setName("Session_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("session,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Session_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("RIG");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRig(r);
        p1.setAllowedExtensions((short)10);
        db.persist(p1);
        
        Session ses = new Session();
        ses.setActive(true);
        ses.setReady(true);
        ses.setActivityLastUpdated(now);
        ses.setExtensions((short) 5);
        ses.setPriority((short) 5);
        ses.setRequestTime(now);
        ses.setRequestedResourceId(r.getId());
        ses.setRequestedResourceName(r.getName());
        ses.setResourceType("RIG");
        ses.setResourcePermission(p1);
        ses.setRemovalTime(after);
        ses.setUser(user);
        ses.setUserName(user.getName());
        ses.setUserNamespace(user.getNamespace());
        ses.setAssignedRigName(r.getName());
        ses.setAssignmentTime(now);
        ses.setRig(r);
        db.persist(ses);

        
        Session ses2 = new Session();
        ses2.setActive(true);
        ses2.setReady(true);
        ses2.setActivityLastUpdated(later);
        ses2.setExtensions((short) 5);
        ses2.setPriority((short) 5);
        ses2.setRequestTime(before);
        ses2.setRequestedResourceId(r.getId());
        ses2.setRequestedResourceName(r.getName());
        ses2.setResourceType("RIG");
        ses2.setResourcePermission(p1);
        ses2.setRemovalTime(later);
        ses2.setUser(user);
        ses2.setUserName(user.getName());
        ses2.setUserNamespace(user.getNamespace());
        ses2.setAssignedRigName(r.getName());
        ses2.setAssignmentTime(after);
        ses2.setRig(r);
        db.persist(ses2);
        db.getTransaction().commit();
        
        db.beginTransaction();
        db.refresh(uc1);
        db.refresh(user);
        db.refresh(p1);
        db.refresh(r);
        db.refresh(rt);
        db.getTransaction().commit();

        
        // Set up test parameters
        QuerySessionAccess qSA = new QuerySessionAccess();
        QuerySessionAccessType qSAType = new QuerySessionAccessType();
        
        RequestorType user1 = new RequestorType();
        user1.setUserQName("REPS:testuser1");
        qSAType.setRequestor(user1);
        
        QueryFilterType qSelect = new QueryFilterType();
        TypeForQuery type = TypeForQuery.RIG_TYPE;
        OperatorType op = OperatorType.AND;
        qSelect.setTypeForQuery(type);
        qSelect.setOperator(op);
        qSelect.setQueryLike("Session_Test_Rig_Type");
        qSAType.setQuerySelect(qSelect);

        Calendar start = Calendar.getInstance();
        start.add(Calendar.MONTH, -1);
        qSAType.setStartTime(start);
        Calendar end = Calendar.getInstance();
        end.add(Calendar.MONTH, 2);
        qSAType.setEndTime(end);
        
        PaginationType pages = new PaginationType();
        pages.setNumberOfPages(2);
        pages.setPageLength(2);
        pages.setPageNumber(1);
        qSAType.setPagination(pages);

        qSA.setQuerySessionAccess(qSAType);
        

        QuerySessionAccessResponse response = this.service.querySessionAccess(qSA);

        // Delete test data
        db.beginTransaction();
        db.delete(ses2);
        db.delete(ses);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass);
        db.delete(uc1);
        db.delete(user);
        db.getTransaction().commit();

        assertNotNull(response);
        QuerySessionAccessResponseType resp = response.getQuerySessionAccessResponse();
        assertNotNull(resp);
        
        PaginationType page = resp.getPagination();
        assertNotNull(page);
        
        AccessReportType[] rep = resp.getAccessReportData();
        assertNotNull(rep);
        
        //rep[0] is ses2 - ordered by request time
        Calendar queueStart = rep[1].getQueueStartTime();
        assertEquals(before.getTime()/1000, queueStart.getTimeInMillis()/1000);
        Calendar sessionStart = rep[1].getSessionStartTime();
        assertEquals(after.getTime()/1000, sessionStart.getTimeInMillis()/1000);
        Calendar sessionEnd = rep[1].getSessionEndTime();
        assertEquals(later.getTime()/1000, sessionEnd.getTimeInMillis()/1000);
        assertEquals((after.getTime()- before.getTime())/1000, rep[1].getQueueDuration());
        assertEquals((later.getTime()- after.getTime())/1000, rep[1].getSessionDuration());

        Calendar queueStart1 = rep[0].getQueueStartTime();
        assertEquals(now.getTime()/1000, queueStart1.getTimeInMillis()/1000);
        Calendar sessionStart1 = rep[0].getSessionStartTime();
        assertEquals(now.getTime()/1000, sessionStart1.getTimeInMillis()/1000);
        Calendar sessionEnd1 = rep[0].getSessionEndTime();
        assertEquals(after.getTime()/1000, sessionEnd1.getTimeInMillis()/1000);
        assertEquals((now.getTime()- now.getTime())/1000, rep[0].getQueueDuration());
        assertEquals((after.getTime()-now.getTime())/1000, rep[0].getSessionDuration());


        OMElement ele = response.getOMElement(QuerySessionAccessResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }

    public void testQuerySessionAccessUserClassAcademicPermission() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 30000);
        Date after = new Date(System.currentTimeMillis() + 20000);
        Date later = new Date(System.currentTimeMillis() + 70000);
        Date now = new Date();
        
        db.beginTransaction();
        
        User user = new User("testuser1", "REPS", "ACADEMIC");
        user.setEmail("user1@uts");
        user.setFirstName("user");
        user.setLastName("test");
        db.persist(user);
        
        UserClass uc1 = new UserClass();
        uc1.setName("sessclass");
        uc1.setActive(true);
        uc1.setQueuable(true);
        uc1.setPriority((short)4);
        db.persist(uc1);
        
        AcademicPermission ap1 = new AcademicPermission(user, uc1, true, true, true, true, true);
        db.persist(ap1);

        UserAssociation ass = new UserAssociation(new UserAssociationId(user.getId(), uc1.getId()), uc1, user);
        db.persist(ass);
        
        RigType rt = new RigType();
        rt.setName("Session_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("session,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Session_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("RIG");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRig(r);
        p1.setAllowedExtensions((short)10);
        db.persist(p1);
        
        Session ses = new Session();
        ses.setActive(true);
        ses.setReady(true);
        ses.setActivityLastUpdated(now);
        ses.setExtensions((short) 5);
        ses.setPriority((short) 5);
        ses.setRequestTime(now);
        ses.setRequestedResourceId(r.getId());
        ses.setRequestedResourceName(r.getName());
        ses.setResourceType("RIG");
        ses.setResourcePermission(p1);
        ses.setRemovalTime(after);
        ses.setUser(user);
        ses.setUserName(user.getName());
        ses.setUserNamespace(user.getNamespace());
        ses.setAssignedRigName(r.getName());
        ses.setAssignmentTime(now);
        ses.setRig(r);
        db.persist(ses);

        
        Session ses2 = new Session();
        ses2.setActive(true);
        ses2.setReady(true);
        ses2.setActivityLastUpdated(later);
        ses2.setExtensions((short) 5);
        ses2.setPriority((short) 5);
        ses2.setRequestTime(before);
        ses2.setRequestedResourceId(r.getId());
        ses2.setRequestedResourceName(r.getName());
        ses2.setResourceType("RIG");
        ses2.setResourcePermission(p1);
        ses2.setRemovalTime(later);
        ses2.setUser(user);
        ses2.setUserName(user.getName());
        ses2.setUserNamespace(user.getNamespace());
        ses2.setAssignedRigName(r.getName());
        ses2.setAssignmentTime(after);
        ses2.setRig(r);
        db.persist(ses2);
        db.getTransaction().commit();
        
        db.beginTransaction();
        db.refresh(uc1);
        db.refresh(user);
        db.refresh(p1);
        db.refresh(r);
        db.refresh(rt);
        db.getTransaction().commit();

        
        // Set up test parameters
        QuerySessionAccess qSA = new QuerySessionAccess();
        QuerySessionAccessType qSAType = new QuerySessionAccessType();
        
        RequestorType user1 = new RequestorType();
        user1.setUserQName("REPS:testuser1");
        qSAType.setRequestor(user1);
        
        QueryFilterType qSelect = new QueryFilterType();
        TypeForQuery type = TypeForQuery.USER_CLASS;
        OperatorType op = OperatorType.AND;
        qSelect.setTypeForQuery(type);
        qSelect.setOperator(op);
        qSelect.setQueryLike("sessclass");
        qSAType.setQuerySelect(qSelect);

        Calendar start = Calendar.getInstance();
        start.add(Calendar.MONTH, -1);
        qSAType.setStartTime(start);
        Calendar end = Calendar.getInstance();
        end.add(Calendar.MONTH, 2);
        qSAType.setEndTime(end);
        
        PaginationType pages = new PaginationType();
        pages.setNumberOfPages(2);
        pages.setPageLength(2);
        pages.setPageNumber(1);
        qSAType.setPagination(pages);

        qSA.setQuerySessionAccess(qSAType);
        

        QuerySessionAccessResponse response = this.service.querySessionAccess(qSA);

        // Delete test data
        db.beginTransaction();
        db.delete(ses2);
        db.delete(ses);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass);
        db.delete(ap1);
        db.delete(uc1);
        db.delete(user);
        db.getTransaction().commit();

        assertNotNull(response);
        QuerySessionAccessResponseType resp = response.getQuerySessionAccessResponse();
        assertNotNull(resp);
        
        PaginationType page = resp.getPagination();
        assertNotNull(page);
        
        AccessReportType[] rep = resp.getAccessReportData();
        assertNotNull(rep);
        
        //rep[0] is ses2 - ordered by request time
        Calendar queueStart = rep[0].getQueueStartTime();
        assertEquals(before.getTime()/1000, queueStart.getTimeInMillis()/1000);
        Calendar sessionStart = rep[0].getSessionStartTime();
        assertEquals(after.getTime()/1000, sessionStart.getTimeInMillis()/1000);
        Calendar sessionEnd = rep[0].getSessionEndTime();
        assertEquals(later.getTime()/1000, sessionEnd.getTimeInMillis()/1000);
        assertEquals((after.getTime()- before.getTime())/1000, rep[0].getQueueDuration());
        assertEquals((later.getTime()- after.getTime())/1000, rep[0].getSessionDuration());

        Calendar queueStart1 = rep[1].getQueueStartTime();
        assertEquals(now.getTime()/1000, queueStart1.getTimeInMillis()/1000);
        Calendar sessionStart1 = rep[1].getSessionStartTime();
        assertEquals(now.getTime()/1000, sessionStart1.getTimeInMillis()/1000);
        Calendar sessionEnd1 = rep[1].getSessionEndTime();
        assertEquals(after.getTime()/1000, sessionEnd1.getTimeInMillis()/1000);
        assertEquals((now.getTime()- now.getTime())/1000, rep[1].getQueueDuration());
        assertEquals((after.getTime()-now.getTime())/1000, rep[1].getSessionDuration());


        OMElement ele = response.getOMElement(QuerySessionAccessResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }

    public void testQuerySessionAccessUserClassNOAcademicPermission() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 30000);
        Date after = new Date(System.currentTimeMillis() + 20000);
        Date later = new Date(System.currentTimeMillis() + 70000);
        Date now = new Date();
        
        db.beginTransaction();
        
        User user = new User("testuser1", "REPS", "ACADEMIC");
        user.setEmail("user1@uts");
        user.setFirstName("user");
        user.setLastName("test");
        db.persist(user);
        
        UserClass uc1 = new UserClass();
        uc1.setName("sessclass");
        uc1.setActive(true);
        uc1.setQueuable(true);
        uc1.setPriority((short)4);
        db.persist(uc1);
        
        UserClass uc2 = new UserClass();
        uc2.setName("sessclass2");
        uc2.setActive(true);
        uc2.setQueuable(true);
        uc2.setPriority((short)4);
        db.persist(uc2);

        AcademicPermission ap1 = new AcademicPermission(user, uc1, true, true, true, true, true);
        db.persist(ap1);

        UserAssociation ass = new UserAssociation(new UserAssociationId(user.getId(), uc1.getId()), uc1, user);
        db.persist(ass);
        
        RigType rt = new RigType();
        rt.setName("Session_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("session,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Session_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("RIG");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRig(r);
        p1.setAllowedExtensions((short)10);
        db.persist(p1);
        
        ResourcePermission p2 = new ResourcePermission();
        p2.setType("RIG");
        p2.setUserClass(uc2);
        p2.setStartTime(before);
        p2.setExpiryTime(after);
        p2.setRig(r);
        p2.setAllowedExtensions((short)10);
        db.persist(p2);

        Session ses = new Session();
        ses.setActive(true);
        ses.setReady(true);
        ses.setActivityLastUpdated(now);
        ses.setExtensions((short) 5);
        ses.setPriority((short) 5);
        ses.setRequestTime(now);
        ses.setRequestedResourceId(r.getId());
        ses.setRequestedResourceName(r.getName());
        ses.setResourceType("RIG");
        ses.setResourcePermission(p2);
        ses.setRemovalTime(after);
        ses.setUser(user);
        ses.setUserName(user.getName());
        ses.setUserNamespace(user.getNamespace());
        ses.setAssignedRigName(r.getName());
        ses.setAssignmentTime(now);
        ses.setRig(r);
        db.persist(ses);

        
        Session ses2 = new Session();
        ses2.setActive(true);
        ses2.setReady(true);
        ses2.setActivityLastUpdated(later);
        ses2.setExtensions((short) 5);
        ses2.setPriority((short) 5);
        ses2.setRequestTime(before);
        ses2.setRequestedResourceId(r.getId());
        ses2.setRequestedResourceName(r.getName());
        ses2.setResourceType("RIG");
        ses2.setResourcePermission(p2);
        ses2.setRemovalTime(later);
        ses2.setUser(user);
        ses2.setUserName(user.getName());
        ses2.setUserNamespace(user.getNamespace());
        ses2.setAssignedRigName(r.getName());
        ses2.setAssignmentTime(after);
        ses2.setRig(r);
        db.persist(ses2);
        db.getTransaction().commit();
        
        db.beginTransaction();
        db.refresh(uc1);
        db.refresh(user);
        db.refresh(p1);
        db.refresh(r);
        db.refresh(rt);
        db.getTransaction().commit();

        
        // Set up test parameters
        QuerySessionAccess qSA = new QuerySessionAccess();
        QuerySessionAccessType qSAType = new QuerySessionAccessType();
        
        RequestorType user1 = new RequestorType();
        user1.setUserQName("REPS:testuser1");
        qSAType.setRequestor(user1);
        
        QueryFilterType qSelect = new QueryFilterType();
        TypeForQuery type = TypeForQuery.USER_CLASS;
        OperatorType op = OperatorType.AND;
        qSelect.setTypeForQuery(type);
        qSelect.setOperator(op);
        qSelect.setQueryLike("sessclass");
        qSAType.setQuerySelect(qSelect);

        Calendar start = Calendar.getInstance();
        start.add(Calendar.MONTH, -1);
        qSAType.setStartTime(start);
        Calendar end = Calendar.getInstance();
        end.add(Calendar.MONTH, 2);
        qSAType.setEndTime(end);
        
        PaginationType pages = new PaginationType();
        pages.setNumberOfPages(2);
        pages.setPageLength(2);
        pages.setPageNumber(1);
        qSAType.setPagination(pages);

        qSA.setQuerySessionAccess(qSAType);
        

        QuerySessionAccessResponse response = this.service.querySessionAccess(qSA);

        // Delete test data
        db.beginTransaction();
        db.delete(ses2);
        db.delete(ses);
        db.delete(p2);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass);
        db.delete(ap1);
        db.delete(uc2);
        db.delete(uc1);
        db.delete(user);
        db.getTransaction().commit();

        assertNotNull(response);
        QuerySessionAccessResponseType resp = response.getQuerySessionAccessResponse();
        assertNotNull(resp);
        
        PaginationType page = resp.getPagination();
        assertNotNull(page);
        
        AccessReportType[] rep = resp.getAccessReportData();
        assertNull(rep);
        
        OMElement ele = response.getOMElement(QuerySessionAccessResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }
    
    
    
    public void testQuerySessionAccessUserClassNOAssignmentTime() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 30000);
        Date after = new Date(System.currentTimeMillis() + 20000);
        Date later = new Date(System.currentTimeMillis() + 70000);
        Date now = new Date();
        
        db.beginTransaction();
        
        User user = new User("testuser1", "REPS", "ACADEMIC");
        user.setEmail("user1@uts");
        user.setFirstName("user");
        user.setLastName("test");
        db.persist(user);
        
        UserClass uc1 = new UserClass();
        uc1.setName("sessclass");
        uc1.setActive(true);
        uc1.setQueuable(true);
        uc1.setPriority((short)4);
        db.persist(uc1);
        
        UserClass uc2 = new UserClass();
        uc2.setName("sessclass2");
        uc2.setActive(true);
        uc2.setQueuable(true);
        uc2.setPriority((short)4);
        db.persist(uc2);

        AcademicPermission ap1 = new AcademicPermission(user, uc1, true, true, true, true, true);
        db.persist(ap1);

        UserAssociation ass = new UserAssociation(new UserAssociationId(user.getId(), uc1.getId()), uc1, user);
        db.persist(ass);
        
        RigType rt = new RigType();
        rt.setName("Session_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("session,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Session_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("RIG");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRig(r);
        p1.setAllowedExtensions((short)10);
        db.persist(p1);
        
        ResourcePermission p2 = new ResourcePermission();
        p2.setType("RIG");
        p2.setUserClass(uc2);
        p2.setStartTime(before);
        p2.setExpiryTime(after);
        p2.setRig(r);
        p2.setAllowedExtensions((short)10);
        db.persist(p2);

        Session ses = new Session();
        ses.setActive(true);
        ses.setReady(true);
        ses.setActivityLastUpdated(now);
        ses.setExtensions((short) 5);
        ses.setPriority((short) 5);
        ses.setRequestTime(now);
        ses.setRequestedResourceId(r.getId());
        ses.setRequestedResourceName(r.getName());
        ses.setResourceType("RIG");
        ses.setResourcePermission(p1);
        ses.setRemovalTime(after);
        ses.setUser(user);
        ses.setUserName(user.getName());
        ses.setUserNamespace(user.getNamespace());
        ses.setAssignedRigName(r.getName());
        //ses.setAssignmentTime(now);
        ses.setRig(r);
        db.persist(ses);

        
        Session ses2 = new Session();
        ses2.setActive(true);
        ses2.setReady(true);
        ses2.setActivityLastUpdated(later);
        ses2.setExtensions((short) 5);
        ses2.setPriority((short) 5);
        ses2.setRequestTime(before);
        ses2.setRequestedResourceId(r.getId());
        ses2.setRequestedResourceName(r.getName());
        ses2.setResourceType("RIG");
        ses2.setResourcePermission(p1);
        ses2.setRemovalTime(later);
        ses2.setUser(user);
        ses2.setUserName(user.getName());
        ses2.setUserNamespace(user.getNamespace());
        ses2.setAssignedRigName(r.getName());
        ses2.setAssignmentTime(after);
        ses2.setRig(r);
        db.persist(ses2);
        db.getTransaction().commit();
        
        db.beginTransaction();
        db.refresh(uc1);
        db.refresh(user);
        db.refresh(p1);
        db.refresh(r);
        db.refresh(rt);
        db.getTransaction().commit();

        
        // Set up test parameters
        QuerySessionAccess qSA = new QuerySessionAccess();
        QuerySessionAccessType qSAType = new QuerySessionAccessType();
        
        RequestorType user1 = new RequestorType();
        user1.setUserQName("REPS:testuser1");
        qSAType.setRequestor(user1);
        
        QueryFilterType qSelect = new QueryFilterType();
        TypeForQuery type = TypeForQuery.USER_CLASS;
        OperatorType op = OperatorType.AND;
        qSelect.setTypeForQuery(type);
        qSelect.setOperator(op);
        qSelect.setQueryLike("sessclass");
        qSAType.setQuerySelect(qSelect);

        Calendar start = Calendar.getInstance();
        start.add(Calendar.MONTH, -1);
        qSAType.setStartTime(start);
        Calendar end = Calendar.getInstance();
        end.add(Calendar.MONTH, 2);
        qSAType.setEndTime(end);
        
        PaginationType pages = new PaginationType();
        pages.setNumberOfPages(2);
        pages.setPageLength(2);
        pages.setPageNumber(1);
        qSAType.setPagination(pages);

        qSA.setQuerySessionAccess(qSAType);
        

        QuerySessionAccessResponse response = this.service.querySessionAccess(qSA);

        // Delete test data
        db.beginTransaction();
        db.delete(ses2);
        db.delete(ses);
        db.delete(p2);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass);
        db.delete(ap1);
        db.delete(uc2);
        db.delete(uc1);
        db.delete(user);
        db.getTransaction().commit();

        assertNotNull(response);
        QuerySessionAccessResponseType resp = response.getQuerySessionAccessResponse();
        assertNotNull(resp);
        
        PaginationType page = resp.getPagination();
        assertNotNull(page);
        
        AccessReportType[] rep = resp.getAccessReportData();
        assertNotNull(rep);
        
        //rep[0] is ses2 - ordered by request time
        Calendar queueStart = rep[0].getQueueStartTime();
        assertEquals(before.getTime()/1000, queueStart.getTimeInMillis()/1000);
        Calendar sessionStart = rep[0].getSessionStartTime();
        assertEquals(after.getTime()/1000, sessionStart.getTimeInMillis()/1000);
        Calendar sessionEnd = rep[0].getSessionEndTime();
        assertEquals(later.getTime()/1000, sessionEnd.getTimeInMillis()/1000);
        assertEquals((after.getTime()- before.getTime())/1000, rep[0].getQueueDuration());
        assertEquals((later.getTime()- after.getTime())/1000, rep[0].getSessionDuration());

        Calendar queueStart1 = rep[1].getQueueStartTime();
        assertEquals(now.getTime()/1000, queueStart1.getTimeInMillis()/1000);
        Calendar sessionStart1 = rep[1].getSessionStartTime();
        assertNull(sessionStart1);
        Calendar sessionEnd1 = rep[1].getSessionEndTime();
        assertEquals(after.getTime()/1000, sessionEnd1.getTimeInMillis()/1000);
        assertEquals((after.getTime()- now.getTime())/1000, rep[1].getQueueDuration());
        assertEquals(0,rep[1].getSessionDuration());

        
        OMElement ele = response.getOMElement(QuerySessionAccessResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }
    
    public void testQuerySessionAccessUserAcademicPermission() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 30000);
        Date after = new Date(System.currentTimeMillis() + 20000);
        Date later = new Date(System.currentTimeMillis() + 70000);
        Date now = new Date();
        
        db.beginTransaction();
        
        User user = new User("testuser1", "REPS", "ACADEMIC");
        user.setEmail("user1@uts");
        user.setFirstName("user");
        user.setLastName("test");
        db.persist(user);
        
        User user2 = new User("testuser2", "REPS", "USER");
        user2.setEmail("user1@uts");
        user2.setFirstName("user");
        user2.setLastName("test");
        db.persist(user2);

        User user3 = new User("testuser3", "REPS", "USER");
        user3.setEmail("user1@uts");
        user3.setFirstName("user");
        user3.setLastName("test");
        db.persist(user3);

        UserClass uc1 = new UserClass();
        uc1.setName("sessclass");
        uc1.setActive(true);
        uc1.setQueuable(true);
        uc1.setPriority((short)4);
        db.persist(uc1);
        
        UserClass uc2 = new UserClass();
        uc2.setName("sessclass2");
        uc2.setActive(true);
        uc2.setQueuable(true);
        uc2.setPriority((short)4);
        db.persist(uc2);

        AcademicPermission ap1 = new AcademicPermission(user, uc1, true, true, true, true, true);
        db.persist(ap1);

        UserAssociation ass = new UserAssociation(new UserAssociationId(user.getId(), uc1.getId()), uc1, user);
        db.persist(ass);

        UserAssociation ass2 = new UserAssociation(new UserAssociationId(user2.getId(), uc1.getId()), uc1, user2);
        db.persist(ass2);

        UserAssociation ass3 = new UserAssociation(new UserAssociationId(user3.getId(), uc1.getId()), uc1, user3);
        db.persist(ass3);

        RigType rt = new RigType();
        rt.setName("Session_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("session,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Session_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("RIG");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRig(r);
        p1.setAllowedExtensions((short)10);
        db.persist(p1);
        
        Session ses = new Session();
        ses.setActive(true);
        ses.setReady(true);
        ses.setActivityLastUpdated(now);
        ses.setExtensions((short) 5);
        ses.setPriority((short) 5);
        ses.setRequestTime(now);
        ses.setRequestedResourceId(r.getId());
        ses.setRequestedResourceName(r.getName());
        ses.setResourceType("RIG");
        ses.setResourcePermission(p1);
        ses.setRemovalReason("Session ended");
        ses.setRemovalTime(after);
        ses.setUser(user2);
        ses.setUserName(user2.getName());
        ses.setUserNamespace(user2.getNamespace());
        ses.setAssignedRigName(r.getName());
        ses.setAssignmentTime(now);
        ses.setRig(r);
        db.persist(ses);

        
        Session ses2 = new Session();
        ses2.setActive(true);
        ses2.setReady(true);
        ses2.setActivityLastUpdated(later);
        ses2.setExtensions((short) 5);
        ses2.setPriority((short) 5);
        ses2.setRequestTime(before);
        ses2.setRequestedResourceId(r.getId());
        ses2.setRequestedResourceName(r.getName());
        ses2.setResourceType("RIG");
        ses2.setResourcePermission(p1);
        ses2.setRemovalTime(later);
        ses2.setRemovalReason("Session ended");
        ses2.setUser(user2);
        ses2.setUserName(user2.getName());
        ses2.setUserNamespace(user2.getNamespace());
        ses2.setAssignedRigName(r.getName());
        ses2.setAssignmentTime(after);
        ses2.setRig(r);
        db.persist(ses2);
        db.getTransaction().commit();
        
        db.beginTransaction();
        db.refresh(uc1);
        db.refresh(uc2);
        db.refresh(user);
        db.refresh(user2);
        db.refresh(user3);
        db.refresh(p1);
        db.refresh(r);
        db.refresh(rt);
        db.getTransaction().commit();

        
        // Set up test parameters
        QuerySessionAccess qSA = new QuerySessionAccess();
        QuerySessionAccessType qSAType = new QuerySessionAccessType();
        
        RequestorType user1 = new RequestorType();
        user1.setUserQName("REPS:testuser1");
        qSAType.setRequestor(user1);
        
        QueryFilterType qSelect = new QueryFilterType();
        TypeForQuery type = TypeForQuery.USER;
        OperatorType op = OperatorType.AND;
        qSelect.setTypeForQuery(type);
        qSelect.setOperator(op);
        qSelect.setQueryLike("REPS:testuser2");
        qSAType.setQuerySelect(qSelect);

        Calendar start = Calendar.getInstance();
        start.add(Calendar.MONTH, -1);
        qSAType.setStartTime(start);
        Calendar end = Calendar.getInstance();
        end.add(Calendar.MONTH, 2);
        qSAType.setEndTime(end);
        
        PaginationType pages = new PaginationType();
        pages.setNumberOfPages(2);
        pages.setPageLength(2);
        pages.setPageNumber(1);
        qSAType.setPagination(pages);

        qSA.setQuerySessionAccess(qSAType);
        

        QuerySessionAccessResponse response = this.service.querySessionAccess(qSA);

        // Delete test data
        db.beginTransaction();
        db.delete(ses2);
        db.delete(ses);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass3);
        db.delete(ass2);
        db.delete(ass);
        db.delete(ap1);
        db.delete(uc2);
        db.delete(uc1);
        db.delete(user3);
        db.delete(user2);
        db.delete(user);
        db.getTransaction().commit();

        assertNotNull(response);
        QuerySessionAccessResponseType resp = response.getQuerySessionAccessResponse();
        assertNotNull(resp);
        
        PaginationType page = resp.getPagination();
        assertNotNull(page);
        
        AccessReportType[] rep = resp.getAccessReportData();
        assertNotNull(rep);
        assertEquals(2,rep.length);
        
        //rep[0] is ses2 - ordered by request time
        Calendar queueStart = rep[1].getQueueStartTime();
        assertEquals(now.getTime()/1000, queueStart.getTimeInMillis()/1000);
        Calendar sessionStart = rep[1].getSessionStartTime();
        assertEquals(now.getTime()/1000, sessionStart.getTimeInMillis()/1000);
        Calendar sessionEnd1 = rep[1].getSessionEndTime();
        assertEquals(after.getTime()/1000, sessionEnd1.getTimeInMillis()/1000);
        assertEquals(0, rep[1].getQueueDuration());
        assertEquals((after.getTime()- now.getTime())/1000,rep[1].getSessionDuration());

        assertNotNull(rep[0].getReasonForTermination());

        OMElement ele = response.getOMElement(QuerySessionAccessResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }


    public void testQuerySessionAccessUserNOAcademicPermission() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 30000);
        Date after = new Date(System.currentTimeMillis() + 20000);
        Date later = new Date(System.currentTimeMillis() + 70000);
        Date now = new Date();
        
        db.beginTransaction();
        
        User user = new User("testuser1", "REPS", "ACADEMIC");
        user.setEmail("user1@uts");
        user.setFirstName("user");
        user.setLastName("test");
        db.persist(user);
        
        User user2 = new User("testuser2", "REPS", "USER");
        user2.setEmail("user1@uts");
        user2.setFirstName("user");
        user2.setLastName("test");
        db.persist(user2);

        User user3 = new User("testuser3", "REPS", "USER");
        user3.setEmail("user1@uts");
        user3.setFirstName("user");
        user3.setLastName("test");
        db.persist(user3);

        UserClass uc1 = new UserClass();
        uc1.setName("sessclass");
        uc1.setActive(true);
        uc1.setQueuable(true);
        uc1.setPriority((short)4);
        db.persist(uc1);
        
        UserClass uc2 = new UserClass();
        uc2.setName("sessclass2");
        uc2.setActive(true);
        uc2.setQueuable(true);
        uc2.setPriority((short)4);
        db.persist(uc2);

        AcademicPermission ap1 = new AcademicPermission(user, uc1, true, true, true, true, true);
        db.persist(ap1);

        UserAssociation ass = new UserAssociation(new UserAssociationId(user.getId(), uc1.getId()), uc1, user);
        db.persist(ass);

        UserAssociation ass2 = new UserAssociation(new UserAssociationId(user2.getId(), uc2.getId()), uc2, user2);
        db.persist(ass2);

        UserAssociation ass3 = new UserAssociation(new UserAssociationId(user3.getId(), uc2.getId()), uc2, user3);
        db.persist(ass3);

        RigType rt = new RigType();
        rt.setName("Session_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("session,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Session_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("RIG");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(after);
        p1.setRig(r);
        p1.setAllowedExtensions((short)10);
        db.persist(p1);
        
        Session ses = new Session();
        ses.setActive(true);
        ses.setReady(true);
        ses.setActivityLastUpdated(now);
        ses.setExtensions((short) 5);
        ses.setPriority((short) 5);
        ses.setRequestTime(now);
        ses.setRequestedResourceId(r.getId());
        ses.setRequestedResourceName(r.getName());
        ses.setResourceType("RIG");
        ses.setResourcePermission(p1);
        ses.setRemovalReason("Session ended");
        ses.setRemovalTime(after);
        ses.setUser(user2);
        ses.setUserName(user2.getName());
        ses.setUserNamespace(user2.getNamespace());
        ses.setAssignedRigName(r.getName());
        ses.setAssignmentTime(now);
        ses.setRig(r);
        db.persist(ses);

        
        Session ses2 = new Session();
        ses2.setActive(true);
        ses2.setReady(true);
        ses2.setActivityLastUpdated(later);
        ses2.setExtensions((short) 5);
        ses2.setPriority((short) 5);
        ses2.setRequestTime(before);
        ses2.setRequestedResourceId(r.getId());
        ses2.setRequestedResourceName(r.getName());
        ses2.setResourceType("RIG");
        ses2.setResourcePermission(p1);
        ses2.setRemovalTime(later);
        ses2.setRemovalReason("Session ended");
        ses2.setUser(user3);
        ses2.setUserName(user3.getName());
        ses2.setUserNamespace(user3.getNamespace());
        ses2.setAssignedRigName(r.getName());
        ses2.setAssignmentTime(after);
        ses2.setRig(r);
        db.persist(ses2);
        db.getTransaction().commit();
        
        db.beginTransaction();
        db.refresh(uc1);
        db.refresh(uc2);
        db.refresh(user);
        db.refresh(user2);
        db.refresh(user3);
        db.refresh(p1);
        db.refresh(r);
        db.refresh(rt);
        db.getTransaction().commit();

        
        // Set up test parameters
        QuerySessionAccess qSA = new QuerySessionAccess();
        QuerySessionAccessType qSAType = new QuerySessionAccessType();
        
        RequestorType user1 = new RequestorType();
        user1.setUserQName("REPS:testuser1");
        qSAType.setRequestor(user1);
        
        QueryFilterType qSelect = new QueryFilterType();
        TypeForQuery type = TypeForQuery.USER;
        OperatorType op = OperatorType.AND;
        qSelect.setTypeForQuery(type);
        qSelect.setOperator(op);
        qSelect.setQueryLike("REPS:testuser2");
        qSAType.setQuerySelect(qSelect);

        Calendar start = Calendar.getInstance();
        start.add(Calendar.MONTH, -1);
        qSAType.setStartTime(start);
        Calendar end = Calendar.getInstance();
        end.add(Calendar.MONTH, 2);
        qSAType.setEndTime(end);
        
        PaginationType pages = new PaginationType();
        pages.setNumberOfPages(2);
        pages.setPageLength(2);
        pages.setPageNumber(1);
        qSAType.setPagination(pages);

        qSA.setQuerySessionAccess(qSAType);
        

        QuerySessionAccessResponse response = this.service.querySessionAccess(qSA);

        // Delete test data
        db.beginTransaction();
        db.delete(ses2);
        db.delete(ses);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass3);
        db.delete(ass2);
        db.delete(ass);
        db.delete(ap1);
        db.delete(uc2);
        db.delete(uc1);
        db.delete(user3);
        db.delete(user2);
        db.delete(user);
        db.getTransaction().commit();

        assertNotNull(response);
        QuerySessionAccessResponseType resp = response.getQuerySessionAccessResponse();
        assertNotNull(resp);
        
        PaginationType page = resp.getPagination();
        assertNotNull(page);
        
        AccessReportType[] rep = resp.getAccessReportData();
        assertNull(rep);

        OMElement ele = response.getOMElement(QuerySessionAccessResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.reports.intf.Reports#querySessionReport(au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionReport)}.
     * @throws Exception 
     */
    public void testQuerySessionReportRIG() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();


        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);

        Date before = cal.getTime();
        
        // Queue time 60 min
        // Session time 120 min
        Date request = cal.getTime();
        cal.add(Calendar.HOUR, 1);
        Date assign = cal.getTime();
        cal.add(Calendar.HOUR, 2);
        Date remove = cal.getTime();
        cal.add(Calendar.HOUR, 1);
        Date after = cal.getTime();
        
        // Queue time 30min
        // Session time 60 min
        cal.add(Calendar.MINUTE, 30);
        Date request2 = cal.getTime();
        cal.add(Calendar.MINUTE, 30);
        Date assign2 = cal.getTime();
        cal.add(Calendar.MINUTE, 60);
        Date remove2 = cal.getTime();
        cal.add(Calendar.MINUTE, 30);
        // Queue time 0 hr
        // Session time 40 min
        cal.add(Calendar.MINUTE, 30);
        Date request3 = cal.getTime();
        Date assign3 = cal.getTime();
        cal.add(Calendar.MINUTE, 40);
        Date remove3 = cal.getTime();
        cal.add(Calendar.MINUTE, 30);
        // Queue time 20 min
        // Session time 0 min
        cal.add(Calendar.MINUTE, 30);
        Date request4 = cal.getTime();
        Date assign4 = null;
        cal.add(Calendar.MINUTE, 20);
        Date remove4 = cal.getTime();
        cal.add(Calendar.MINUTE, 30);
        cal.add(Calendar.MONTH, 2);
        Date later = cal.getTime();
        
        db.beginTransaction();
        
        User user = new User("testuser1", "REPS", "ACADEMIC");
        user.setEmail("user1@uts");
        user.setFirstName("user");
        user.setLastName("test");
        db.persist(user);
        
        User user2 = new User("testuser2", "REPS", "USER");
        user2.setEmail("user1@uts");
        user2.setFirstName("user");
        user2.setLastName("test");
        db.persist(user2);

        User user3 = new User("testuser3", "REPS", "USER");
        user3.setEmail("user1@uts");
        user3.setFirstName("user");
        user3.setLastName("test");
        db.persist(user3);

        UserClass uc1 = new UserClass();
        uc1.setName("sessclass");
        uc1.setActive(true);
        uc1.setQueuable(true);
        uc1.setPriority((short)4);
        db.persist(uc1);
        
        UserClass uc2 = new UserClass();
        uc2.setName("sessclass2");
        uc2.setActive(true);
        uc2.setQueuable(true);
        uc2.setPriority((short)4);
        db.persist(uc2);

        AcademicPermission ap1 = new AcademicPermission(user, uc1, true, true, true, true, true);
        db.persist(ap1);

        UserAssociation ass = new UserAssociation(new UserAssociationId(user.getId(), uc1.getId()), uc1, user);
        db.persist(ass);

        UserAssociation ass2 = new UserAssociation(new UserAssociationId(user2.getId(), uc1.getId()), uc1, user2);
        db.persist(ass2);

        UserAssociation ass3 = new UserAssociation(new UserAssociationId(user3.getId(), uc1.getId()), uc1, user3);
        db.persist(ass3);

        RigType rt = new RigType();
        rt.setName("Session_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("session,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Session_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("RIG");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(later);
        p1.setRig(r);
        p1.setAllowedExtensions((short)10);
        db.persist(p1);
        
        Session ses = new Session();
        ses.setActive(true);
        ses.setReady(true);
        ses.setActivityLastUpdated(after);
        ses.setExtensions((short) 5);
        ses.setPriority((short) 5);
        ses.setRequestTime(request);
        ses.setRequestedResourceId(r.getId());
        ses.setRequestedResourceName(r.getName());
        ses.setResourceType("RIG");
        ses.setResourcePermission(p1);
        ses.setRemovalReason("Session ended");
        ses.setRemovalTime(remove);
        ses.setUser(user2);
        ses.setUserName(user2.getName());
        ses.setUserNamespace(user2.getNamespace());
        ses.setAssignedRigName(r.getName());
        ses.setAssignmentTime(assign);
        ses.setRig(r);
        db.persist(ses);
        
        Session ses2 = new Session();
        ses2.setActive(true);
        ses2.setReady(true);
        ses2.setActivityLastUpdated(later);
        ses2.setExtensions((short) 5);
        ses2.setPriority((short) 5);
        ses2.setRequestTime(request2);
        ses2.setRequestedResourceId(r.getId());
        ses2.setRequestedResourceName(r.getName());
        ses2.setResourceType("RIG");
        ses2.setResourcePermission(p1);
        ses2.setRemovalTime(remove2);
        ses2.setRemovalReason("Session ended");
        ses2.setUser(user3);
        ses2.setUserName(user3.getName());
        ses2.setUserNamespace(user3.getNamespace());
        ses2.setAssignedRigName(r.getName());
        ses2.setAssignmentTime(assign2);
        ses2.setRig(r);
        db.persist(ses2);
        
        Session ses3 = new Session();
        ses3.setActive(true);
        ses3.setReady(true);
        ses3.setActivityLastUpdated(later);
        ses3.setExtensions((short) 5);
        ses3.setPriority((short) 5);
        ses3.setRequestTime(request3);
        ses3.setRequestedResourceId(r.getId());
        ses3.setRequestedResourceName(r.getName());
        ses3.setResourceType("RIG");
        ses3.setResourcePermission(p1);
        ses3.setRemovalTime(remove3);
        ses3.setRemovalReason("Session ended");
        ses3.setUser(user2);
        ses3.setUserName(user2.getName());
        ses3.setUserNamespace(user2.getNamespace());
        ses3.setAssignedRigName(r.getName());
        ses3.setAssignmentTime(assign3);
        ses3.setRig(r);
        db.persist(ses3);

        Session ses4 = new Session();
        ses4.setActive(true);
        ses4.setReady(true);
        ses4.setActivityLastUpdated(later);
        ses4.setExtensions((short) 5);
        ses4.setPriority((short) 5);
        ses4.setRequestTime(request4);
        ses4.setRequestedResourceId(r.getId());
        ses4.setRequestedResourceName(r.getName());
        ses4.setResourceType("RIG");
        ses4.setResourcePermission(p1);
        ses4.setRemovalTime(remove4);
        ses4.setRemovalReason("Session ended");
        ses4.setUser(user3);
        ses4.setUserName(user3.getName());
        ses4.setUserNamespace(user3.getNamespace());
        ses4.setAssignedRigName(r.getName());
        ses4.setAssignmentTime(assign4);
        ses4.setRig(r);
        db.persist(ses4);

        db.getTransaction().commit();

        db.beginTransaction();
        db.refresh(uc1);
        db.refresh(uc2);
        db.refresh(user);
        db.refresh(user2);
        db.refresh(user3);
        db.refresh(p1);
        db.refresh(r);
        db.refresh(rt);
        db.getTransaction().commit();

        
        // Set up test parameters
        QuerySessionReport qSR = new QuerySessionReport();
        QuerySessionReportType qSRType = new QuerySessionReportType();
        
        RequestorType user1 = new RequestorType();
        user1.setUserQName("REPS:testuser1");
        qSRType.setRequestor(user1);
        
        QueryFilterType qSelect = new QueryFilterType();
        TypeForQuery type = TypeForQuery.RIG;
        OperatorType op = OperatorType.AND;
        qSelect.setTypeForQuery(type);
        qSelect.setOperator(op);
        qSelect.setQueryLike("Session_Rig_Test_Rig1");
        qSRType.setQuerySelect(qSelect);

        Calendar start = Calendar.getInstance();
        start.add(Calendar.MONTH, -1);
        qSRType.setStartTime(start);
        Calendar end = Calendar.getInstance();
        end.add(Calendar.MONTH, 2);
        qSRType.setEndTime(end);
        
        PaginationType pages = new PaginationType();
        pages.setNumberOfPages(2);
        pages.setPageLength(2);
        pages.setPageNumber(1);
        qSRType.setPagination(pages);

        qSR.setQuerySessionReport(qSRType);
        
        QuerySessionReportResponse response = this.service.querySessionReport(qSR);

        // Delete test data
        db.beginTransaction();
        db.delete(ses4);
        db.delete(ses3);
        db.delete(ses2);
        db.delete(ses);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass3);
        db.delete(ass2);
        db.delete(ass);
        db.delete(ap1);
        db.delete(uc2);
        db.delete(uc1);
        db.delete(user3);
        db.delete(user2);
        db.delete(user);
        db.getTransaction().commit();

        assertNotNull(response);
        QuerySessionReportResponseType resp = response.getQuerySessionReportResponse();
        assertNotNull(resp);
        assertEquals(2,resp.getSessionCount());
        assertEquals(110*60,resp.getTotalQueueDuration());
        assertEquals(220*60,resp.getTotalSessionDuration());
        
        PaginationType page = resp.getPagination();
        assertNotNull(page);
        
        //Only single user, so one record
        SessionReportType[] rep = resp.getSessionReport();
        assertNotNull(rep);
        assertEquals(2,rep.length);
        
        //rep[0] session 1 and 3
        assertEquals((float)30*60, rep[0].getAveQueueDuration());
        assertEquals((float)60*60, rep[0].getMaxQueueDuration());
        assertEquals((float)0*60, rep[0].getMinQueueDuration());
        assertEquals((float)30*60, rep[0].getMedQueueDuration());
        assertEquals((float)60*60, rep[0].getTotalQueueDuration());
        
        assertEquals((float)80*60,rep[0].getAveSessionDuration());
        assertEquals((float)120*60, rep[0].getMaxSessionDuration());
        assertEquals((float)40*60, rep[0].getMinSessionDuration());
        assertEquals((float)80*60, rep[0].getMedSessionDuration());
        assertEquals((float)160*60, rep[0].getTotalSessionDuration());

        //rep[1] session 2 and 4
        assertEquals((float)25*60, rep[1].getAveQueueDuration());
        assertEquals((float)30*60, rep[1].getMaxQueueDuration());
        assertEquals((float)20*60, rep[1].getMinQueueDuration());
        assertEquals((float)25*60, rep[1].getMedQueueDuration());
        assertEquals((float)50*60, rep[1].getTotalQueueDuration());
        
        assertEquals((float)30*60,rep[1].getAveSessionDuration());
        assertEquals((float)60*60, rep[1].getMaxSessionDuration());
        assertEquals((float)0*60, rep[1].getMinSessionDuration());
        assertEquals((float)30*60, rep[1].getMedSessionDuration());
        assertEquals((float)60*60, rep[1].getTotalSessionDuration());

        OMElement ele = response.getOMElement(QuerySessionReportResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }

    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.reports.intf.Reports#querySessionReport(au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionReport)}.
     * @throws Exception 
     */
    public void testQuerySessionReportRIGPaginationPage1() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();


        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);

        Date before = cal.getTime();
        
        // Queue time 60 min
        // Session time 120 min
        Date request = cal.getTime();
        cal.add(Calendar.HOUR, 1);
        Date assign = cal.getTime();
        cal.add(Calendar.HOUR, 2);
        Date remove = cal.getTime();
        cal.add(Calendar.HOUR, 1);
        Date after = cal.getTime();
        
        // Queue time 30min
        // Session time 60 min
        cal.add(Calendar.MINUTE, 30);
        Date request2 = cal.getTime();
        cal.add(Calendar.MINUTE, 30);
        Date assign2 = cal.getTime();
        cal.add(Calendar.MINUTE, 60);
        Date remove2 = cal.getTime();
        cal.add(Calendar.MINUTE, 30);
        // Queue time 0 hr
        // Session time 40 min
        cal.add(Calendar.MINUTE, 30);
        Date request3 = cal.getTime();
        Date assign3 = cal.getTime();
        cal.add(Calendar.MINUTE, 40);
        Date remove3 = cal.getTime();
        cal.add(Calendar.MINUTE, 30);
        // Queue time 20 min
        // Session time 0 min
        cal.add(Calendar.MINUTE, 30);
        Date request4 = cal.getTime();
        Date assign4 = null;
        cal.add(Calendar.MINUTE, 20);
        Date remove4 = cal.getTime();
        cal.add(Calendar.MINUTE, 30);
        cal.add(Calendar.MONTH, 2);
        Date later = cal.getTime();
        
        db.beginTransaction();
        
        User user = new User("testuser1", "REPS", "ACADEMIC");
        user.setEmail("user1@uts");
        user.setFirstName("user");
        user.setLastName("test");
        db.persist(user);
        
        User user2 = new User("testuser2", "REPS", "USER");
        user2.setEmail("user2@uts");
        user2.setFirstName("user");
        user2.setLastName("test");
        db.persist(user2);

        User user3 = new User("testuser3", "REPS", "USER");
        user3.setEmail("user3@uts");
        user3.setFirstName("user");
        user3.setLastName("test");
        db.persist(user3);

        UserClass uc1 = new UserClass();
        uc1.setName("sessclass");
        uc1.setActive(true);
        uc1.setQueuable(true);
        uc1.setPriority((short)4);
        db.persist(uc1);
        
        UserClass uc2 = new UserClass();
        uc2.setName("sessclass2");
        uc2.setActive(true);
        uc2.setQueuable(true);
        uc2.setPriority((short)4);
        db.persist(uc2);

        AcademicPermission ap1 = new AcademicPermission(user, uc1, true, true, true, true, true);
        db.persist(ap1);

        UserAssociation ass = new UserAssociation(new UserAssociationId(user.getId(), uc1.getId()), uc1, user);
        db.persist(ass);

        UserAssociation ass2 = new UserAssociation(new UserAssociationId(user2.getId(), uc1.getId()), uc1, user2);
        db.persist(ass2);

        UserAssociation ass3 = new UserAssociation(new UserAssociationId(user3.getId(), uc1.getId()), uc1, user3);
        db.persist(ass3);

        RigType rt = new RigType();
        rt.setName("Session_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("session,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Session_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("RIG");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(later);
        p1.setRig(r);
        p1.setAllowedExtensions((short)10);
        db.persist(p1);
        
        Session ses = new Session();
        ses.setActive(true);
        ses.setReady(true);
        ses.setActivityLastUpdated(after);
        ses.setExtensions((short) 5);
        ses.setPriority((short) 5);
        ses.setRequestTime(request);
        ses.setRequestedResourceId(r.getId());
        ses.setRequestedResourceName(r.getName());
        ses.setResourceType("RIG");
        ses.setResourcePermission(p1);
        ses.setRemovalReason("Session ended");
        ses.setRemovalTime(remove);
        ses.setUser(user2);
        ses.setUserName(user2.getName());
        ses.setUserNamespace(user2.getNamespace());
        ses.setAssignedRigName(r.getName());
        ses.setAssignmentTime(assign);
        ses.setRig(r);
        db.persist(ses);
        
        Session ses2 = new Session();
        ses2.setActive(true);
        ses2.setReady(true);
        ses2.setActivityLastUpdated(later);
        ses2.setExtensions((short) 5);
        ses2.setPriority((short) 5);
        ses2.setRequestTime(request2);
        ses2.setRequestedResourceId(r.getId());
        ses2.setRequestedResourceName(r.getName());
        ses2.setResourceType("RIG");
        ses2.setResourcePermission(p1);
        ses2.setRemovalTime(remove2);
        ses2.setRemovalReason("Session ended");
        ses2.setUser(user3);
        ses2.setUserName(user3.getName());
        ses2.setUserNamespace(user3.getNamespace());
        ses2.setAssignedRigName(r.getName());
        ses2.setAssignmentTime(assign2);
        ses2.setRig(r);
        db.persist(ses2);
        
        Session ses3 = new Session();
        ses3.setActive(true);
        ses3.setReady(true);
        ses3.setActivityLastUpdated(later);
        ses3.setExtensions((short) 5);
        ses3.setPriority((short) 5);
        ses3.setRequestTime(request3);
        ses3.setRequestedResourceId(r.getId());
        ses3.setRequestedResourceName(r.getName());
        ses3.setResourceType("RIG");
        ses3.setResourcePermission(p1);
        ses3.setRemovalTime(remove3);
        ses3.setRemovalReason("Session ended");
        ses3.setUser(user2);
        ses3.setUserName(user2.getName());
        ses3.setUserNamespace(user2.getNamespace());
        ses3.setAssignedRigName(r.getName());
        ses3.setAssignmentTime(assign3);
        ses3.setRig(r);
        db.persist(ses3);

        Session ses4 = new Session();
        ses4.setActive(true);
        ses4.setReady(true);
        ses4.setActivityLastUpdated(later);
        ses4.setExtensions((short) 5);
        ses4.setPriority((short) 5);
        ses4.setRequestTime(request4);
        ses4.setRequestedResourceId(r.getId());
        ses4.setRequestedResourceName(r.getName());
        ses4.setResourceType("RIG");
        ses4.setResourcePermission(p1);
        ses4.setRemovalTime(remove4);
        ses4.setRemovalReason("Session ended");
        ses4.setUser(user3);
        ses4.setUserName(user3.getName());
        ses4.setUserNamespace(user3.getNamespace());
        ses4.setAssignedRigName(r.getName());
        ses4.setAssignmentTime(assign4);
        ses4.setRig(r);
        db.persist(ses4);

        db.getTransaction().commit();

        db.beginTransaction();
        db.refresh(uc1);
        db.refresh(uc2);
        db.refresh(user);
        db.refresh(user2);
        db.refresh(user3);
        db.refresh(p1);
        db.refresh(r);
        db.refresh(rt);
        db.getTransaction().commit();

        
        // Set up test parameters
        QuerySessionReport qSR = new QuerySessionReport();
        QuerySessionReportType qSRType = new QuerySessionReportType();
        
        RequestorType user1 = new RequestorType();
        user1.setUserQName("REPS:testuser1");
        qSRType.setRequestor(user1);
        
        QueryFilterType qSelect = new QueryFilterType();
        TypeForQuery type = TypeForQuery.RIG;
        OperatorType op = OperatorType.AND;
        qSelect.setTypeForQuery(type);
        qSelect.setOperator(op);
        qSelect.setQueryLike("Session_Rig_Test_Rig1");
        qSRType.setQuerySelect(qSelect);

        Calendar start = Calendar.getInstance();
        start.add(Calendar.MONTH, -1);
        qSRType.setStartTime(start);
        Calendar end = Calendar.getInstance();
        end.add(Calendar.MONTH, 2);
        qSRType.setEndTime(end);
        
        PaginationType pages = new PaginationType();
        pages.setNumberOfPages(2);
        pages.setPageLength(1);
        pages.setPageNumber(1);
        qSRType.setPagination(pages);

        qSR.setQuerySessionReport(qSRType);
        
        QuerySessionReportResponse response = this.service.querySessionReport(qSR);

        // Delete test data
        db.beginTransaction();
        db.delete(ses4);
        db.delete(ses3);
        db.delete(ses2);
        db.delete(ses);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass3);
        db.delete(ass2);
        db.delete(ass);
        db.delete(ap1);
        db.delete(uc2);
        db.delete(uc1);
        db.delete(user3);
        db.delete(user2);
        db.delete(user);
        db.getTransaction().commit();

        assertNotNull(response);
        QuerySessionReportResponseType resp = response.getQuerySessionReportResponse();
        assertNotNull(resp);
        assertEquals(2,resp.getSessionCount());
        assertEquals(110*60,resp.getTotalQueueDuration());
        assertEquals(220*60,resp.getTotalSessionDuration());
        
        PaginationType page = resp.getPagination();
        assertNotNull(page);
        
        //Only single user, so one record
        SessionReportType[] rep = resp.getSessionReport();
        assertNotNull(rep);
        assertEquals(1,rep.length);
        
        //rep[0] session 1 and 3
        assertEquals((float)30*60, rep[0].getAveQueueDuration());
        assertEquals((float)60*60, rep[0].getMaxQueueDuration());
        assertEquals((float)0*60, rep[0].getMinQueueDuration());
        assertEquals((float)30*60, rep[0].getMedQueueDuration());
        assertEquals((float)60*60, rep[0].getTotalQueueDuration());
        
        assertEquals((float)80*60,rep[0].getAveSessionDuration());
        assertEquals((float)120*60, rep[0].getMaxSessionDuration());
        assertEquals((float)40*60, rep[0].getMinSessionDuration());
        assertEquals((float)80*60, rep[0].getMedSessionDuration());
        assertEquals((float)160*60, rep[0].getTotalSessionDuration());

        //rep[1] session 2 and 4
  /*      assertEquals((float)25*60, rep[1].getAveQueueDuration());
        assertEquals((float)30*60, rep[1].getMaxQueueDuration());
        assertEquals((float)20*60, rep[1].getMinQueueDuration());
        assertEquals((float)25*60, rep[1].getMedQueueDuration());
        assertEquals((float)50*60, rep[1].getTotalQueueDuration());
        
        assertEquals((float)30*60,rep[1].getAveSessionDuration());
        assertEquals((float)60*60, rep[1].getMaxSessionDuration());
        assertEquals((float)0*60, rep[1].getMinSessionDuration());
        assertEquals((float)30*60, rep[1].getMedSessionDuration());
        assertEquals((float)60*60, rep[1].getTotalSessionDuration());
*/
        OMElement ele = response.getOMElement(QuerySessionReportResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.reports.intf.Reports#querySessionReport(au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionReport)}.
     * @throws Exception 
     */
    public void testQuerySessionReportRIGPaginationPage2() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();


        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);

        Date before = cal.getTime();
        
        // Queue time 60 min
        // Session time 120 min
        Date request = cal.getTime();
        cal.add(Calendar.HOUR, 1);
        Date assign = cal.getTime();
        cal.add(Calendar.HOUR, 2);
        Date remove = cal.getTime();
        cal.add(Calendar.HOUR, 1);
        Date after = cal.getTime();
        
        // Queue time 30min
        // Session time 60 min
        cal.add(Calendar.MINUTE, 30);
        Date request2 = cal.getTime();
        cal.add(Calendar.MINUTE, 30);
        Date assign2 = cal.getTime();
        cal.add(Calendar.MINUTE, 60);
        Date remove2 = cal.getTime();
        cal.add(Calendar.MINUTE, 30);
      
        // Queue time 0 hr
        // Session time 40 min
        cal.add(Calendar.MINUTE, 30);
        Date request3 = cal.getTime();
        Date assign3 = cal.getTime();
        cal.add(Calendar.MINUTE, 40);
        Date remove3 = cal.getTime();
        cal.add(Calendar.MINUTE, 30);
      
        // Queue time 20 min
        // Session time 0 min
        cal.add(Calendar.MINUTE, 30);
        Date request4 = cal.getTime();
        Date assign4 = null;
        cal.add(Calendar.MINUTE, 20);
        Date remove4 = cal.getTime();
        cal.add(Calendar.MINUTE, 30);
        
        cal.add(Calendar.MONTH, 2);
        Date later = cal.getTime();
        
        db.beginTransaction();
        
        User user = new User("testuser1", "REPS", "ACADEMIC");
        user.setEmail("user1@uts");
        user.setFirstName("user");
        user.setLastName("test");
        db.persist(user);
        
        User user2 = new User("testuser2", "REPS", "USER");
        user2.setEmail("user1@uts");
        user2.setFirstName("user");
        user2.setLastName("test");
        db.persist(user2);

        User user3 = new User("testuser3", "REPS", "USER");
        user3.setEmail("user1@uts");
        user3.setFirstName("user");
        user3.setLastName("test");
        db.persist(user3);

        UserClass uc1 = new UserClass();
        uc1.setName("sessclass");
        uc1.setActive(true);
        uc1.setQueuable(true);
        uc1.setPriority((short)4);
        db.persist(uc1);
        
        UserClass uc2 = new UserClass();
        uc2.setName("sessclass2");
        uc2.setActive(true);
        uc2.setQueuable(true);
        uc2.setPriority((short)4);
        db.persist(uc2);

        AcademicPermission ap1 = new AcademicPermission(user, uc1, true, true, true, true, true);
        db.persist(ap1);

        UserAssociation ass = new UserAssociation(new UserAssociationId(user.getId(), uc1.getId()), uc1, user);
        db.persist(ass);

        UserAssociation ass2 = new UserAssociation(new UserAssociationId(user2.getId(), uc1.getId()), uc1, user2);
        db.persist(ass2);

        UserAssociation ass3 = new UserAssociation(new UserAssociationId(user3.getId(), uc1.getId()), uc1, user3);
        db.persist(ass3);

        RigType rt = new RigType();
        rt.setName("Session_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("session,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Session_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(before);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("RIG");
        p1.setUserClass(uc1);
        p1.setStartTime(before);
        p1.setExpiryTime(later);
        p1.setRig(r);
        p1.setAllowedExtensions((short)10);
        db.persist(p1);
        
        Session ses = new Session();
        ses.setActive(true);
        ses.setReady(true);
        ses.setActivityLastUpdated(after);
        ses.setExtensions((short) 5);
        ses.setPriority((short) 5);
        ses.setRequestTime(request);
        ses.setRequestedResourceId(r.getId());
        ses.setRequestedResourceName(r.getName());
        ses.setResourceType("RIG");
        ses.setResourcePermission(p1);
        ses.setRemovalReason("Session ended");
        ses.setRemovalTime(remove);
        ses.setUser(user2);
        ses.setUserName(user2.getName());
        ses.setUserNamespace(user2.getNamespace());
        ses.setAssignedRigName(r.getName());
        ses.setAssignmentTime(assign);
        ses.setRig(r);
        db.persist(ses);
        
        Session ses2 = new Session();
        ses2.setActive(true);
        ses2.setReady(true);
        ses2.setActivityLastUpdated(later);
        ses2.setExtensions((short) 5);
        ses2.setPriority((short) 5);
        ses2.setRequestTime(request2);
        ses2.setRequestedResourceId(r.getId());
        ses2.setRequestedResourceName(r.getName());
        ses2.setResourceType("RIG");
        ses2.setResourcePermission(p1);
        ses2.setRemovalTime(remove2);
        ses2.setRemovalReason("Session ended");
        ses2.setUser(user3);
        ses2.setUserName(user3.getName());
        ses2.setUserNamespace(user3.getNamespace());
        ses2.setAssignedRigName(r.getName());
        ses2.setAssignmentTime(assign2);
        ses2.setRig(r);
        db.persist(ses2);
        
        Session ses3 = new Session();
        ses3.setActive(true);
        ses3.setReady(true);
        ses3.setActivityLastUpdated(later);
        ses3.setExtensions((short) 5);
        ses3.setPriority((short) 5);
        ses3.setRequestTime(request3);
        ses3.setRequestedResourceId(r.getId());
        ses3.setRequestedResourceName(r.getName());
        ses3.setResourceType("RIG");
        ses3.setResourcePermission(p1);
        ses3.setRemovalTime(remove3);
        ses3.setRemovalReason("Session ended");
        ses3.setUser(user2);
        ses3.setUserName(user2.getName());
        ses3.setUserNamespace(user2.getNamespace());
        ses3.setAssignedRigName(r.getName());
        ses3.setAssignmentTime(assign3);
        ses3.setRig(r);
        db.persist(ses3);

        Session ses4 = new Session();
        ses4.setActive(true);
        ses4.setReady(true);
        ses4.setActivityLastUpdated(later);
        ses4.setExtensions((short) 5);
        ses4.setPriority((short) 5);
        ses4.setRequestTime(request4);
        ses4.setRequestedResourceId(r.getId());
        ses4.setRequestedResourceName(r.getName());
        ses4.setResourceType("RIG");
        ses4.setResourcePermission(p1);
        ses4.setRemovalTime(remove4);
        ses4.setRemovalReason("Session ended");
        ses4.setUser(user3);
        ses4.setUserName(user3.getName());
        ses4.setUserNamespace(user3.getNamespace());
        ses4.setAssignedRigName(r.getName());
        ses4.setAssignmentTime(assign4);
        ses4.setRig(r);
        db.persist(ses4);

        db.getTransaction().commit();

        db.beginTransaction();
        db.refresh(uc1);
        db.refresh(uc2);
        db.refresh(user);
        db.refresh(user2);
        db.refresh(user3);
        db.refresh(p1);
        db.refresh(r);
        db.refresh(rt);
        db.getTransaction().commit();

        
        // Set up test parameters
        QuerySessionReport qSR = new QuerySessionReport();
        QuerySessionReportType qSRType = new QuerySessionReportType();
        
        RequestorType user1 = new RequestorType();
        user1.setUserQName("REPS:testuser1");
        qSRType.setRequestor(user1);
        
        QueryFilterType qSelect = new QueryFilterType();
        TypeForQuery type = TypeForQuery.RIG;
        OperatorType op = OperatorType.AND;
        qSelect.setTypeForQuery(type);
        qSelect.setOperator(op);
        qSelect.setQueryLike("Session_Rig_Test_Rig1");
        qSRType.setQuerySelect(qSelect);

        Calendar start = Calendar.getInstance();
        start.add(Calendar.MONTH, -1);
        qSRType.setStartTime(start);
        Calendar end = Calendar.getInstance();
        end.add(Calendar.MONTH, 2);
        qSRType.setEndTime(end);
        
        PaginationType pages = new PaginationType();
        pages.setNumberOfPages(2);
        pages.setPageLength(1);
        pages.setPageNumber(2);
        qSRType.setPagination(pages);

        qSR.setQuerySessionReport(qSRType);
        
        QuerySessionReportResponse response = this.service.querySessionReport(qSR);

        // Delete test data
        db.beginTransaction();
        db.delete(ses4);
        db.delete(ses3);
        db.delete(ses2);
        db.delete(ses);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass3);
        db.delete(ass2);
        db.delete(ass);
        db.delete(ap1);
        db.delete(uc2);
        db.delete(uc1);
        db.delete(user3);
        db.delete(user2);
        db.delete(user);
        db.getTransaction().commit();

        assertNotNull(response);
        QuerySessionReportResponseType resp = response.getQuerySessionReportResponse();
        assertNotNull(resp);
        assertEquals(2,resp.getSessionCount());
        assertEquals(110*60,resp.getTotalQueueDuration());
        assertEquals(220*60,resp.getTotalSessionDuration());
        
        PaginationType page = resp.getPagination();
        assertNotNull(page);
        
        //Only single user, so one record
        SessionReportType[] rep = resp.getSessionReport();
        assertNotNull(rep);
        assertEquals(1,rep.length);
        

        //rep[0] session 2 and 4
        assertEquals((float)25*60, rep[0].getAveQueueDuration());
        assertEquals((float)30*60, rep[0].getMaxQueueDuration());
        assertEquals((float)20*60, rep[0].getMinQueueDuration());
        assertEquals((float)25*60, rep[0].getMedQueueDuration());
        assertEquals((float)50*60, rep[0].getTotalQueueDuration());
        
        assertEquals((float)30*60,rep[0].getAveSessionDuration());
        assertEquals((float)60*60, rep[0].getMaxSessionDuration());
        assertEquals((float)0*60, rep[0].getMinSessionDuration());
        assertEquals((float)30*60, rep[0].getMedSessionDuration());
        assertEquals((float)60*60, rep[0].getTotalSessionDuration());

        OMElement ele = response.getOMElement(QuerySessionReportResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
    }
}
