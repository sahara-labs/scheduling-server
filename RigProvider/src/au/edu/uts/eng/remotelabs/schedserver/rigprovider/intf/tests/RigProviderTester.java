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
 * @date 19th February 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.tests;

import java.util.Date;

import junit.framework.TestCase;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axis2.databinding.types.URI;
import org.apache.axis2.databinding.types.URI.MalformedURIException;
import org.junit.Before;
import org.junit.Test;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigDao;
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
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.identok.impl.IdentityTokenRegister;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.RigProviderSOAPImpl;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.types.AllocateCallback;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.types.AllocateCallbackResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.types.CallbackRequestType;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.types.ErrorType;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.types.ProviderResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.types.RegisterRig;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.types.RegisterRigResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.types.RegisterRigType;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.types.ReleaseCallback;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.types.ReleaseCallbackResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.types.RemoveRig;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.types.RemoveRigResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.types.RemoveRigType;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.types.StatusType;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.types.UpdateRigStatus;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.types.UpdateRigStatusResponse;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.types.UpdateRigType;

/**
 * Tests the {@link RigProviderSOAPImpl} class.
 */
public class RigProviderTester extends TestCase
{
    /** Object of class under test. */
    private RigProviderSOAPImpl provider;
    
    @Override
    @Before
    public void setUp() throws Exception
    {
        DataAccessTestSetup.setup();
        IdentityTokenRegister.getInstance().expunge();
        this.provider = new RigProviderSOAPImpl();
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.RigProviderSOAPImpl#registerRig(au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.types.RegisterRig)}.
     * @throws MalformedURIException 
     */
    @Test
    public void testRegisterRig() throws Exception
    {
        org.hibernate.Session ses = DataAccessActivator.getNewSession();
        
        String name = "lp1";
        String type = "localrigprovidertest";
        String caps = "localrig,providertest";
        String contactUrl = "http://lbremote1.eng.uts.edu.au:7070/services/RigClientService";
        
        RigType typeRecord = new RigType(type, 180, false);
        RigCapabilities capsRecord = new RigCapabilities(caps);
        ses.beginTransaction();
        ses.save(typeRecord);
        ses.save(capsRecord);
        ses.getTransaction().commit();
        ses.flush();
        
        RegisterRig reg = new RegisterRig();
        RegisterRigType regType = new RegisterRigType();
        reg.setRegisterRig(regType);
        StatusType status = new StatusType();
        regType.setStatus(status);
        
        regType.setName(name);
        regType.setType(type);
        regType.setCapabilities(caps);
        regType.setContactUrl(new URI(contactUrl));
        status.setIsOnline(true);
    
        RegisterRigResponse resp = this.provider.registerRig(reg);
        assertNotNull(resp);
        
        Rig rig = new RigDao(ses).findByName(name);
        ses.beginTransaction();
        ses.createQuery("DELETE RigLog WHERE rig='" + rig.getId() + "'").executeUpdate();
        ses.getTransaction().commit();
        
        ses.beginTransaction();        
        ses.delete(rig);
        ses.delete(typeRecord);
        ses.delete(capsRecord);
        ses.getTransaction().commit();
        
        ProviderResponse prov = resp.getRegisterRigResponse();
        assertNotNull(prov);
        assertTrue(prov.getSuccessful());
        assertNull(prov.getErrorReason());
        assertNotNull(prov.getIdentityToken());
        assertEquals(IdentityTokenRegister.getInstance().getIdentityToken(name), prov.getIdentityToken());
        
        OMElement ele = resp.getOMElement(RegisterRigResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("<successful>true</successful>"));
        assertFalse(xml.contains("<errorReason>"));
        assertTrue(xml.contains("<identityToken>"));
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.RigProviderSOAPImpl#registerRig(au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.types.RegisterRig)}.
     * @throws MalformedURIException 
     */
    @Test
    public void testRegisterRigError() throws Exception
    {
        org.hibernate.Session ses = DataAccessActivator.getNewSession();
        
        String name = "lp1";
        String type = "localrigprovidertest";
        String caps = "localrig,providertest";
        String contactUrl = "http://lbremote1.eng.uts.edu.au:7070/services/RigClientService";
        
        RigType typeRecord = new RigType(type, 180, false);
        RigCapabilities capsRecord = new RigCapabilities(caps);
        Rig rigRecord = new Rig(typeRecord, capsRecord, name, contactUrl, new Date(), false, null, false, true, true);
        ses.beginTransaction();
        ses.save(typeRecord);
        ses.save(capsRecord);
        ses.save(rigRecord);
        ses.getTransaction().commit();
        ses.flush();
        
        RegisterRig reg = new RegisterRig();
        RegisterRigType regType = new RegisterRigType();
        reg.setRegisterRig(regType);
        StatusType status = new StatusType();
        regType.setStatus(status);
        
        regType.setName(name);
        regType.setType(type);
        regType.setCapabilities(caps);
        regType.setContactUrl(new URI(contactUrl));
        status.setIsOnline(true);
    
        RegisterRigResponse resp = this.provider.registerRig(reg);
        assertNotNull(resp);
        
        ses.beginTransaction();
        ses.delete(new RigDao(ses).findByName(name));
        ses.delete(typeRecord);
        ses.delete(capsRecord);
        ses.getTransaction().commit();
        
        ProviderResponse prov = resp.getRegisterRigResponse();
        assertNotNull(prov);
        assertFalse(prov.getSuccessful());
        assertNotNull(prov.getErrorReason());
        assertNull(prov.getIdentityToken()); 
        
        OMElement ele = resp.getOMElement(RegisterRigResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("<successful>false</successful>"));
        assertTrue(xml.contains("<errorReason>"));
        assertFalse(xml.contains("<identityToken>"));
    }

    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.RigProviderSOAPImpl#updateRigStatus(au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.types.UpdateRigStatus)}.
     */
    @Test
    public void testUpdateRigStatus() throws Exception
    {
        org.hibernate.Session ses = DataAccessActivator.getNewSession();
        
        String name = "lp1";
        String type = "localrigprovidertest";
        String caps = "localrig,providertest";
        String contactUrl = "http://lbremote1.eng.uts.edu.au:7070/services/RigClientService";
        
        RigType typeRecord = new RigType(type, 180, false);
        RigCapabilities capsRecord = new RigCapabilities(caps);
        Rig rigRecord = new Rig(typeRecord, capsRecord, name, contactUrl, new Date(), false, "Is broken", false, true, true);
        ses.beginTransaction();
        ses.save(typeRecord);
        ses.save(capsRecord);
        ses.save(rigRecord);
        ses.getTransaction().commit();
        ses.flush();
        
        UpdateRigStatus up = new UpdateRigStatus();
        UpdateRigType upTy = new UpdateRigType();
        up.setUpdateRigStatus(upTy);
        upTy.setName(name);
        StatusType st = new StatusType();
        upTy.setStatus(st);
        st.setIsOnline(true);
    
        UpdateRigStatusResponse resp = this.provider.updateRigStatus(up);
        assertNotNull(resp); 
        
        Rig rig = new RigDao(ses).findByName(name);
        ses.beginTransaction();
        ses.createQuery("DELETE RigLog WHERE rig='" + rig.getId() + "'").executeUpdate();
        ses.getTransaction().commit();
        
        ses.beginTransaction();
        ses.delete(rig);
        ses.delete(typeRecord);
        ses.delete(capsRecord);
        ses.getTransaction().commit();
        
        ProviderResponse prov = resp.getUpdateRigStatusResponse();
        assertNotNull(prov);
        assertTrue(prov.getSuccessful());
        assertNull(prov.getErrorReason());
        assertNotNull(prov.getIdentityToken()); 
        assertEquals(IdentityTokenRegister.getInstance().getIdentityToken(name), prov.getIdentityToken());
        
        OMElement ele = resp.getOMElement(UpdateRigStatusResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("<successful>true</successful>"));
        assertFalse(xml.contains("<errorReason>"));
        assertTrue(xml.contains("<identityToken>"));
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.RigProviderSOAPImpl#updateRigStatus(au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.types.UpdateRigStatus)}.
     */
    @Test
    public void testUpdateRigStatusError() throws Exception
    {        
        String name = "lp1";
               
        UpdateRigStatus up = new UpdateRigStatus();
        UpdateRigType upTy = new UpdateRigType();
        up.setUpdateRigStatus(upTy);
        upTy.setName(name);
        StatusType st = new StatusType();
        upTy.setStatus(st);
    
        UpdateRigStatusResponse resp = this.provider.updateRigStatus(up);
        assertNotNull(resp);
       
        ProviderResponse prov = resp.getUpdateRigStatusResponse();
        assertNotNull(prov);
        assertFalse(prov.getSuccessful());
        assertNotNull(prov.getErrorReason());
        assertNull(prov.getIdentityToken()); 
        
        OMElement ele = resp.getOMElement(UpdateRigStatusResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("<successful>false</successful>"));
        assertTrue(xml.contains("<errorReason>"));
        assertFalse(xml.contains("<identityToken>"));
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.RigProviderSOAPImpl#removeRig(au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.types.UpdateRigStatus)}.
     */
    @Test
    public void testRemoveRig() throws Exception
    {
        org.hibernate.Session ses = DataAccessActivator.getNewSession();
        
        String name = "lp1";
        String type = "localrigprovidertest";
        String caps = "localrig,providertest";
        String contactUrl = "http://lbremote1.eng.uts.edu.au:7070/services/RigClientService";
        
        RigType typeRecord = new RigType(type, 180, false);
        RigCapabilities capsRecord = new RigCapabilities(caps);
        Rig rigRecord = new Rig(typeRecord, capsRecord, name, contactUrl, new Date(), false, "Is broken", false, true, true);
        ses.beginTransaction();
        ses.save(typeRecord);
        ses.save(capsRecord);
        ses.save(rigRecord);
        ses.getTransaction().commit();
        ses.flush();
        
        RemoveRig rm = new RemoveRig();
        RemoveRigType rmTy = new RemoveRigType();
        rm.setRemoveRig(rmTy);
        rmTy.setName(name);
        rmTy.setRemovalReason("shutting down");
        
        RemoveRigResponse resp = this.provider.removeRig(rm);
        assertNotNull(resp);
        
        Rig rig = new RigDao(ses).findByName(name);
        ses.beginTransaction();
        ses.createQuery("DELETE RigLog WHERE rig='" + rig.getId() + "'").executeUpdate();
        ses.getTransaction().commit();
        
        ses.beginTransaction();
        ses.delete(rig);
        ses.delete(typeRecord);
        ses.delete(capsRecord);
        ses.getTransaction().commit();
        
        ProviderResponse prov = resp.getRemoveRigResponse();
        assertNotNull(prov);
        assertTrue(prov.getSuccessful());
        assertNull(prov.getErrorReason());
        assertNull(prov.getIdentityToken()); 
        assertEquals(IdentityTokenRegister.getInstance().getIdentityToken(name), prov.getIdentityToken());
        
        OMElement ele = resp.getOMElement(UpdateRigStatusResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("<successful>true</successful>"));
        assertFalse(xml.contains("<errorReason>"));
        assertFalse(xml.contains("<identityToken>"));
    }
    
    /**
     * Test method for {@link au.edu.uts.eng.remotelabs.schedserver.rigprovider.intf.RigProviderSOAPImpl#removeRig(RemoveRig)}.
     */
    @Test
    public void testRemoveRigError() throws Exception
    {        
        RemoveRig rm = new RemoveRig();
        RemoveRigType rmTy = new RemoveRigType();
        rm.setRemoveRig(rmTy);
        rmTy.setName("lp1");
        rmTy.setRemovalReason("Doesn't exist");
    
        RemoveRigResponse resp = this.provider.removeRig(rm);
        assertNotNull(resp);
       
        ProviderResponse prov = resp.getRemoveRigResponse();
        assertNotNull(prov);
        assertFalse(prov.getSuccessful());
        assertNotNull(prov.getErrorReason());
        assertNull(prov.getIdentityToken()); 
        
        OMElement ele = resp.getOMElement(RemoveRigResponse.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);
        assertTrue(xml.contains("<successful>false</successful>"));
        assertTrue(xml.contains("<errorReason>"));
        assertFalse(xml.contains("<identityToken>"));
    }
    
    @Test
    public void testAllocateCallback()
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        Date now = new Date();
        db.beginTransaction();
        
        User user = new User("rcpperm1", "testns", "USER");
        db.persist(user);
        
        UserClass uc = new UserClass();
        uc.setName("uc1");
        uc.setActive(true);
        uc.setQueuable(true);
        uc.setPriority((short)4);
        db.persist(uc);
        
        UserAssociation ass = new UserAssociation(new UserAssociationId(user.getId(), uc.getId()), uc, user);
        db.persist(ass);
        
        RigType rt = new RigType();
        rt.setName("RCP_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("rcp,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("RCP_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(now);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission rp = new ResourcePermission();
        rp.setType("RIG");
        rp.setUserClass(uc);
        rp.setStartTime(now);
        rp.setExpiryTime(now);
        rp.setRig(r);
        rp.setAllowedExtensions((short)10);
        db.persist(rp);
        
        Session ses = new Session();
        ses.setActive(true);
        ses.setReady(false);
        ses.setActivityLastUpdated(now);
        ses.setExtensions((short) 5);
        ses.setPriority((short) 5);
        ses.setRequestTime(now);
        ses.setRequestedResourceId(r.getId());
        ses.setRequestedResourceName(r.getName());
        ses.setResourceType("RIG");
        ses.setResourcePermission(rp);
        ses.setAssignedRigName(r.getName());
        ses.setAssignmentTime(now);
        ses.setRig(r);
        ses.setUser(user);
        ses.setUserName(user.getName());
        ses.setUserNamespace(user.getNamespace());
        db.persist(ses);
        
        r.setSession(ses);
        db.getTransaction().commit();
        
        AllocateCallback alloc = new AllocateCallback();
        CallbackRequestType call = new CallbackRequestType();
        alloc.setAllocateCallback(call);
        call.setSuccess(true);
        call.setName(r.getName());
        
        AllocateCallbackResponse resp = this.provider.allocateCallback(alloc);
        assertNotNull(resp);
        
        ProviderResponse status = resp.getAllocateCallbackResponse();
        assertNotNull(status);
        assertTrue(status.getSuccessful());
        
        db.refresh(ses);
        assertTrue(ses.isReady());
        assertTrue(ses.isActive());
        assertNull(ses.getRemovalReason());

        db.beginTransaction();
        db.createQuery("DELETE RigLog WHERE rig='" + r.getId() + "'").executeUpdate();
        db.getTransaction().commit();
        
        db.beginTransaction();
        r.setSession(null);
        db.delete(ses);
        db.delete(rp);
        db.delete(r);
        db.delete(caps);
        db.delete(rt);
        db.delete(ass);
        db.delete(uc);
        db.delete(user);
        db.getTransaction().commit();
    }
    
    @Test
    public void testAllocateCallbackFailAlloc()
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        Date now = new Date();
        db.beginTransaction();
        
        User user = new User("rcpperm1", "testns", "USER");
        db.persist(user);
        
        UserClass uc = new UserClass();
        uc.setName("uc1");
        uc.setActive(true);
        uc.setQueuable(true);
        uc.setPriority((short)4);
        db.persist(uc);
        
        UserAssociation ass = new UserAssociation(new UserAssociationId(user.getId(), uc.getId()), uc, user);
        db.persist(ass);
        
        RigType rt = new RigType();
        rt.setName("RCP_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("rcp,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("RCP_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(now);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission rp = new ResourcePermission();
        rp.setType("RIG");
        rp.setUserClass(uc);
        rp.setStartTime(now);
        rp.setExpiryTime(now);
        rp.setRig(r);
        rp.setAllowedExtensions((short)10);
        db.persist(rp);
        
        Session ses = new Session();
        ses.setActive(true);
        ses.setReady(false);
        ses.setActivityLastUpdated(now);
        ses.setExtensions((short) 5);
        ses.setPriority((short) 5);
        ses.setRequestTime(now);
        ses.setRequestedResourceId(r.getId());
        ses.setRequestedResourceName(r.getName());
        ses.setResourceType("RIG");
        ses.setResourcePermission(rp);
        ses.setAssignedRigName(r.getName());
        ses.setAssignmentTime(now);
        ses.setRig(r);
        ses.setUser(user);
        ses.setUserName(user.getName());
        ses.setUserNamespace(user.getNamespace());
        db.persist(ses);
        
        r.setSession(ses);
        db.getTransaction().commit();
        
        AllocateCallback alloc = new AllocateCallback();
        CallbackRequestType call = new CallbackRequestType();
        alloc.setAllocateCallback(call);
        call.setSuccess(false);
        call.setName(r.getName());
        
        ErrorType err = new ErrorType();
        err.setCode(1);
        err.setReason("Allocation failed for some unknown reason");
        call.setError(err);
        
        AllocateCallbackResponse resp = this.provider.allocateCallback(alloc);
        assertNotNull(resp);
        
        ProviderResponse status = resp.getAllocateCallbackResponse();
        assertNotNull(status);
        assertTrue(status.getSuccessful());
        
        db.refresh(ses);
        db.refresh(r);
        
        assertFalse(ses.isActive());
        assertFalse(ses.isReady());
        assertNotNull(ses.getRemovalReason());
        assertNotNull(ses.getRemovalTime());
        
        assertFalse(r.isInSession());
        assertFalse(r.isOnline());
        assertNotNull(r.getOfflineReason());
        assertNull(r.getSession());
        
        db.beginTransaction();
        db.createQuery("DELETE RigLog WHERE rig='" + r.getId() + "'").executeUpdate();
        db.getTransaction().commit();
        
        db.beginTransaction();
        r.setSession(null);
        db.delete(ses);
        db.delete(rp);
        db.delete(r);
        db.delete(caps);
        db.delete(rt);
        db.delete(ass);
        db.delete(uc);
        db.delete(user);
        db.getTransaction().commit();
    }
    
    @Test
    public void testAllocateCallbackNoSession()
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        Date now = new Date();
        db.beginTransaction();
        
        User user = new User("rcpperm1", "testns", "USER");
        db.persist(user);
        
        UserClass uc = new UserClass();
        uc.setName("uc1");
        uc.setActive(true);
        uc.setQueuable(true);
        uc.setPriority((short)4);
        db.persist(uc);
        
        UserAssociation ass = new UserAssociation(new UserAssociationId(user.getId(), uc.getId()), uc, user);
        db.persist(ass);
        
        RigType rt = new RigType();
        rt.setName("RCP_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("rcp,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("RCP_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(now);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission rp = new ResourcePermission();
        rp.setType("RIG");
        rp.setUserClass(uc);
        rp.setStartTime(now);
        rp.setExpiryTime(now);
        rp.setRig(r);
        rp.setAllowedExtensions((short)10);
        db.persist(rp);

        db.getTransaction().commit();
        
        AllocateCallback alloc = new AllocateCallback();
        CallbackRequestType call = new CallbackRequestType();
        alloc.setAllocateCallback(call);
        call.setSuccess(true);
        call.setName(r.getName());
        
        AllocateCallbackResponse resp = this.provider.allocateCallback(alloc);
        assertNotNull(resp);
        
        ProviderResponse status = resp.getAllocateCallbackResponse();
        assertNotNull(status);
        assertFalse(status.getSuccessful());
        
        db.refresh(r);
        
        assertFalse(r.isInSession());
        assertTrue(r.isOnline());
        assertNull(r.getSession());
        
        db.beginTransaction();
        db.createQuery("DELETE RigLog WHERE rig='" + r.getId() + "'").executeUpdate();
        db.getTransaction().commit();
        
        db.beginTransaction();
        r.setSession(null);
        db.delete(rp);
        db.delete(r);
        db.delete(caps);
        db.delete(rt);
        db.delete(ass);
        db.delete(uc);
        db.delete(user);
        db.getTransaction().commit();
    }
    
    @Test
    public void testReleaseCallback()
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        Date now = new Date();
        db.beginTransaction();
        
        User user = new User("rcpperm1", "testns", "USER");
        db.persist(user);
        
        UserClass uc = new UserClass();
        uc.setName("uc1");
        uc.setActive(true);
        uc.setQueuable(true);
        uc.setPriority((short)4);
        db.persist(uc);
        
        UserAssociation ass = new UserAssociation(new UserAssociationId(user.getId(), uc.getId()), uc, user);
        db.persist(ass);
        
        RigType rt = new RigType();
        rt.setName("RCP_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("rcp,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("RCP_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(now);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission rp = new ResourcePermission();
        rp.setType("RIG");
        rp.setUserClass(uc);
        rp.setStartTime(now);
        rp.setExpiryTime(now);
        rp.setRig(r);
        rp.setAllowedExtensions((short)10);
        db.persist(rp);
        
        Session ses = new Session();
        ses.setActive(false);
        ses.setReady(false);
        ses.setActivityLastUpdated(now);
        ses.setExtensions((short) 5);
        ses.setPriority((short) 5);
        ses.setRequestTime(now);
        ses.setRequestedResourceId(r.getId());
        ses.setRequestedResourceName(r.getName());
        ses.setResourceType("RIG");
        ses.setResourcePermission(rp);
        ses.setAssignedRigName(r.getName());
        ses.setAssignmentTime(now);
        ses.setRig(r);
        ses.setUser(user);
        ses.setUserName(user.getName());
        ses.setUserNamespace(user.getNamespace());
        db.persist(ses);
        
        r.setSession(ses);
        db.getTransaction().commit();
        
        ReleaseCallback rel = new ReleaseCallback();
        CallbackRequestType call = new CallbackRequestType();
        rel.setReleaseCallback(call);
        call.setSuccess(true);
        call.setName(r.getName());
        
        ReleaseCallbackResponse resp = this.provider.releaseCallback(rel);
        assertNotNull(resp);
        
        ProviderResponse status = resp.getReleaseCallbackResponse();
        assertNotNull(status);
        
        db.refresh(r);
        assertTrue(r.isOnline());
        assertFalse(r.isInSession());
        assertNull(r.getSession());
        assertNull(r.getOfflineReason());
        
        db.beginTransaction();
        db.createQuery("DELETE RigLog WHERE rig='" + r.getId() + "'").executeUpdate();
        db.getTransaction().commit();
        
        db.beginTransaction();
        r.setSession(null);
        db.delete(ses);
        db.delete(rp);
        db.delete(r);
        db.delete(caps);
        db.delete(rt);
        db.delete(ass);
        db.delete(uc);
        db.delete(user);
        db.getTransaction().commit();
        
        assertTrue(status.getSuccessful());
    }
    
    @Test
    public void testReleaseCallbackFailed()
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();
        Date now = new Date();
        db.beginTransaction();
        
        User user = new User("rcpperm1", "testns", "USER");
        db.persist(user);
        
        UserClass uc = new UserClass();
        uc.setName("uc1");
        uc.setActive(true);
        uc.setQueuable(true);
        uc.setPriority((short)4);
        db.persist(uc);
        
        UserAssociation ass = new UserAssociation(new UserAssociationId(user.getId(), uc.getId()), uc, user);
        db.persist(ass);
        
        RigType rt = new RigType();
        rt.setName("RCP_Test_Rig_Type");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("rcp,test,rig,type");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("RCP_Rig_Test_Rig1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(now);
        r.setActive(true);
        r.setOnline(true);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission rp = new ResourcePermission();
        rp.setType("RIG");
        rp.setUserClass(uc);
        rp.setStartTime(now);
        rp.setExpiryTime(now);
        rp.setRig(r);
        rp.setAllowedExtensions((short)10);
        db.persist(rp);
        
        Session ses = new Session();
        ses.setActive(false);
        ses.setReady(false);
        ses.setActivityLastUpdated(now);
        ses.setExtensions((short) 5);
        ses.setPriority((short) 5);
        ses.setRequestTime(now);
        ses.setRequestedResourceId(r.getId());
        ses.setRequestedResourceName(r.getName());
        ses.setResourceType("RIG");
        ses.setResourcePermission(rp);
        ses.setAssignedRigName(r.getName());
        ses.setAssignmentTime(now);
        ses.setRig(r);
        ses.setUser(user);
        ses.setUserName(user.getName());
        ses.setUserNamespace(user.getNamespace());
        db.persist(ses);
        
        r.setSession(ses);
        db.getTransaction().commit();
        
        ReleaseCallback rel = new ReleaseCallback();
        CallbackRequestType call = new CallbackRequestType();
        rel.setReleaseCallback(call);
        call.setSuccess(false);
        call.setName(r.getName());
        ErrorType err = new ErrorType();
        err.setCode(1);
        err.setReason("Foo bar baz");
        call.setError(err);
        
        ReleaseCallbackResponse resp = this.provider.releaseCallback(rel);
        assertNotNull(resp);
        
        ProviderResponse status = resp.getReleaseCallbackResponse();
        assertNotNull(status);
        
        db.refresh(r);
        assertTrue(r.isActive());
        assertFalse(r.isOnline());
        assertFalse(r.isInSession());
        assertNull(r.getSession());
        assertNotNull(r.getOfflineReason());
        
        db.beginTransaction();
        db.createQuery("DELETE RigLog WHERE rig='" + r.getId() + "'").executeUpdate();
        db.getTransaction().commit();
        
        db.beginTransaction();
        r.setSession(null);
        db.delete(ses);
        db.delete(rp);
        db.delete(r);
        db.delete(caps);
        db.delete(rt);
        db.delete(ass);
        db.delete(uc);
        db.delete(user);
        db.getTransaction().commit();
        
        assertTrue(status.getSuccessful());
    }
    
    @Test
    public void testReleaseCallbackNoRig()
    {
        ReleaseCallback rel = new ReleaseCallback();
        CallbackRequestType call = new CallbackRequestType();
        rel.setReleaseCallback(call);
        call.setSuccess(true);
        call.setName("does_not_exist");
        
        ReleaseCallbackResponse resp = this.provider.releaseCallback(rel);
        assertNotNull(resp);
        
        ProviderResponse status = resp.getReleaseCallbackResponse();
        assertNotNull(status);
        assertFalse(status.getSuccessful());
    }
}
