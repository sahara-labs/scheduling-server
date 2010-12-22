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
    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.reports.intf.Reports#queryInfo(au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QueryInfo)}.
     */
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
        ses.save(us1);
        
        User us2 = new User();
        us2.setName("testuser2");
        us2.setNamespace("REPS");
        us2.setPersona("USER");
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

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.reports.intf.Reports#querySessionAccess(au.edu.uts.eng.remotelabs.schedserver.reports.intf.types.QuerySessionAccess)}.
     */
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
        db.persist(us1);
        
        User us2 = new User();
        us2.setName("testuser2");
        us2.setNamespace("REPS");
        us2.setPersona("ADMIN");
        db.persist(us2);

        User user = new User("sessiontest", "testns", "ADMIN");
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

    public void testQuerySessionAccessTimePeriod() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        
        Date before = new Date(System.currentTimeMillis() - 30000);
        Date after = new Date(System.currentTimeMillis() + 20000);
        Date later = new Date(System.currentTimeMillis() + 70000);
        Date now = new Date();
        
        db.beginTransaction();
        
        User user = new User("testuser1", "REPS", "ADMIN");
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
        db.persist(user);
        
        User user2 = new User("testuser2", "REPS", "USER");
        db.persist(user2);

        User user3 = new User("testuser3", "REPS", "USER");
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
        db.persist(user);
        
        User user2 = new User("testuser2", "REPS", "USER");
        db.persist(user2);

        User user3 = new User("testuser3", "REPS", "USER");
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
     */
//    public void testQuerySessionReport()
//    {
//        fail("Not yet implemented");
//    }

}
