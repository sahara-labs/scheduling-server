/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2011, University of Technology, Sydney
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
 * @date 30th January 2011
 */
package au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.tests;

import java.util.Date;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigOfflineSchedule;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.testsetup.DataAccessTestSetup;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.RigManagement;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.GetTypeStatus;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.GetTypeStatusResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.GetTypes;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.GetTypesResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.RigTypeIDType;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.RigTypeType;
import au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.RigTypesType;

/**
 * Tests the {@link RigManagement} class.
 */
public class RigManagementTester extends TestCase
{
    /** Object under test. */
    private RigManagement service;

    @Override
    @Before
    public void setUp() throws Exception
    {
        super.setUp();
        
        DataAccessTestSetup.setup();
        this.service = new RigManagement();
    }

    @Test
    public void testGetTypes()
    {
        Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        RigType rt = new RigType();
        rt.setName("rigmanagetest");
        ses.save(rt);
        ses.getTransaction().commit();
        
        GetTypesResponse response = this.service.getTypes(new GetTypes()); 
        
        ses.beginTransaction();
        ses.delete(rt);
        ses.getTransaction().commit();
        
        assertNotNull(response);
        RigTypesType types = response.getGetTypesResponse();
        assertNotNull(response);
        
        RigTypeType ids[] = types.getTypes();
        assertNotNull(ids);
        assertTrue(ids.length > 0);
        
        boolean typeFound = false;
        for (RigTypeType ty : ids)
        {
            assertNotNull(ty.getName());
            if (ty.getName().equals(rt.getName()))
            {
                typeFound = true;
                assertFalse(ty.getIsOnline());
                assertFalse(ty.getIsAlarmed());
                assertEquals(0, ty.getNumberRigs());
            }
        }
        
        assertTrue(typeFound);
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.RigManagement#getTypeStatus(au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.GetTypeStatus)}.
     */
    @Test
    public void testGetTypeStatus()
    {
        Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        RigType rt = new RigType();
        rt.setName("rigmanagetest");
        ses.save(rt);
        RigType rt2 = new RigType();
        rt2.setName("rigmanagetest2");
        ses.save(rt2);
        RigCapabilities caps = new RigCapabilities("rig,management,test");
        ses.save(caps);
        Rig r = new Rig();
        r.setOnline(false);
        r.setOfflineReason("test reason");
        r.setName("rigmanagementrig");
        r.setLastUpdateTimestamp(new Date());
        r.setRigCapabilities(caps);
        r.setRigType(rt);
        ses.save(r);
        RigOfflineSchedule ro = new RigOfflineSchedule();
        ro.setStartTime(new Date(System.currentTimeMillis() - 60000));
        ro.setEndTime(new Date(System.currentTimeMillis() + 60000));
        ro.setRig(r);
        ro.setReason("testcase");
        ses.save(ro);
        Rig r2 = new Rig();
        r2.setName("rigmanagementrig2");
        r2.setLastUpdateTimestamp(new Date());
        r2.setRigCapabilities(caps);
        r2.setRigType(rt);
        r2.setActive(true);
        r2.setOnline(true);
        r2.setContactUrl("http://remotelabs/rig");
        ses.save(r2);
        Rig r3 = new Rig();
        r3.setName("rigmanagementrig3");
        r3.setLastUpdateTimestamp(new Date());
        r3.setRigCapabilities(caps);
        r3.setRigType(rt2);
        r3.setActive(true);
        r3.setOnline(true);
        r3.setContactUrl("http://remotelabs/rig");
        ses.save(r3);
        ses.getTransaction().commit();
        
        ses.refresh(rt);
        ses.refresh(r);
        ses.refresh(ro);
        
        GetTypeStatus request = new GetTypeStatus();
        RigTypeIDType id = new RigTypeIDType();
        id.setName(rt.getName());
        request.setGetTypeStatus(id);
        
        GetTypeStatusResponse response = this.service.getTypeStatus(request);
        
        ses.beginTransaction();
        ses.delete(ro);
        ses.delete(r);
        ses.delete(r2);
        ses.delete(r3);
        ses.delete(caps);
        ses.delete(rt);
        ses.delete(rt2);
        ses.getTransaction().commit();
        
        assertNotNull(response);
        
        RigTypeType rigType = response.getGetTypeStatusResponse();
        assertEquals(rt.getName(), rigType.getName());
        assertNotNull(rigType);
        assertTrue(rigType.getIsOnline());
        assertFalse(rigType.getIsAlarmed());
        assertEquals(2, rigType.getNumberRigs());
        
        au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.RigType rigs[] = rigType.getRigs();
        assertNotNull(rigs);
        assertEquals(rigs.length, 2);
        
        for (au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.RigType rig : rigs)
        {
            assertNotNull(r.getName());
            RigTypeIDType rtid = rig.getRigType();
            assertNotNull(rtid);
            assertEquals(rt.getName(), rtid.getName());
            assertEquals(caps.getCapabilities(), rig.getCapabilities());
            assertNull(rig.getLastLogs());
            assertNull(rig.getOfflinePeriods());
            
            if (rig.getName().equals(r.getName()))
            {
                assertFalse(rig.getIsRegistered());
                assertFalse(rig.getIsOnline());
                assertFalse(rig.getIsInSession());
                assertFalse(rig.getIsAlarmed());
                assertNull(rig.getSessionUser());
                assertEquals(r.getOfflineReason(), rig.getOfflineReason());
                assertNull(rig.getContactURL());
                
            }
            else if (rig.getName().equals(r2.getName()))
            {
                assertTrue(rig.getIsRegistered());
                assertTrue(rig.getIsOnline());
                assertFalse(rig.getIsInSession());
                assertFalse(rig.getIsAlarmed());
                assertNull(rig.getSessionUser());
                assertNull(rig.getOfflineReason());
                assertEquals(r2.getContactUrl(), rig.getContactURL());
            }
            else
            {
                fail("Unknown rig name");
            }
        }
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.RigManagement#putRigOffline(au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.PutRigOffline)}.
     */
    @Test
    public void testPutRigOffline()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.RigManagement#freeRig(au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.FreeRig)}.
     */
    @Test
    public void testFreeRig()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.RigManagement#getRig(au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.GetRig)}.
     */
    @Test
    public void testGetRig()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.RigManagement#putRigOnline(au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.PutRigOnline)}.
     */
    @Test
    public void testPutRigOnline()
    {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.RigManagement#cancelRigOffline(au.edu.uts.eng.remotelabs.schedserver.rigmanagement.intf.types.CancelRigOffline)}.
     */
    @Test
    public void testCancelRigOffline()
    {
        fail("Not yet implemented");
    }

}
