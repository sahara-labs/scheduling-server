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
 * @date 27th August 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.session.impl.tests;


import java.util.Date;

import junit.framework.TestCase;

import org.junit.Before;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
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
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.RigEventListener.RigStateChangeEvent;
import au.edu.uts.eng.remotelabs.schedserver.session.impl.RigShutdownSessonStopper;

/**
 * Tests the {@link RigShutdownSessonStopper} class.
 */
public class RigShutdownSessionStopperTester extends TestCase
{
    /** Object of class under test. */
    private RigShutdownSessonStopper stopper;

    @Override
    @Before
    public void setUp() throws Exception
    {
        DataAccessTestSetup.setup();
        this.stopper = new RigShutdownSessonStopper();
    }

    public void testReleaseResponseCallback() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();

        Date now = new Date();
        
        db.beginTransaction();
        User user = new User("rcterm", "ses", "USER");
        db.persist(user);
        
        UserClass uc1 = new UserClass();
        uc1.setName("rcf");
        uc1.setActive(true);
        uc1.setQueuable(true);
        uc1.setPriority((short)4);
        db.persist(uc1);
        
        UserAssociation ass = new UserAssociation(new UserAssociationId(user.getId(), uc1.getId()), uc1, user);
        db.persist(ass);
        
        RigType rt = new RigType();
        rt.setName("Rig_Shutdown_Test");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("rig,client,shutdown");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Rig_Shutdown_Test_1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(now);
        r.setActive(true);
        r.setOnline(false);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("RIG");
        p1.setUserClass(uc1);
        p1.setStartTime(now);
        p1.setExpiryTime(now);
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
        ses.setAssignedRigName(r.getName());
        ses.setRig(r);
        ses.setUser(user);
        ses.setUserName(user.getName());
        ses.setUserNamespace(user.getNamespace());
        db.persist(ses);

        db.getTransaction().commit();
        
        this.stopper.eventOccurred(RigStateChangeEvent.REMOVED, r, db);
        

        db.refresh(ses);
        
        db.beginTransaction();
        db.delete(ses);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass);
        db.delete(uc1);
        db.delete(user);
        db.getTransaction().commit();
        
        assertFalse(ses.isActive());
        assertNotNull(ses.getRemovalTime());
        assertNotNull(ses.getRemovalReason());
    }
    
    public void testReleaseResponseCallbackDifferentOnline() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();

        Date now = new Date();
        
        db.beginTransaction();
        User user = new User("rcterm", "ses", "USER");
        db.persist(user);
        
        UserClass uc1 = new UserClass();
        uc1.setName("rcf");
        uc1.setActive(true);
        uc1.setQueuable(true);
        uc1.setPriority((short)4);
        db.persist(uc1);
        
        UserAssociation ass = new UserAssociation(new UserAssociationId(user.getId(), uc1.getId()), uc1, user);
        db.persist(ass);
        
        RigType rt = new RigType();
        rt.setName("Rig_Shutdown_Test");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("rig,client,shutdown");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Rig_Shutdown_Test_1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(now);
        r.setActive(true);
        r.setOnline(false);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("RIG");
        p1.setUserClass(uc1);
        p1.setStartTime(now);
        p1.setExpiryTime(now);
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
        ses.setAssignedRigName(r.getName());
        ses.setRig(r);
        ses.setUser(user);
        ses.setUserName(user.getName());
        ses.setUserNamespace(user.getNamespace());
        db.persist(ses);

        db.getTransaction().commit();
        
        this.stopper.eventOccurred(RigStateChangeEvent.ONLINE, r, db);
        

        db.refresh(ses);
        
        db.beginTransaction();
        db.delete(ses);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass);
        db.delete(uc1);
        db.delete(user);
        db.getTransaction().commit();
        
        assertTrue(ses.isActive());
        assertNull(ses.getRemovalTime());
        assertNull(ses.getRemovalReason());
    }
    
    public void testReleaseResponseCallbackDifferentOffline() throws Exception
    {
        org.hibernate.Session db = DataAccessActivator.getNewSession();

        Date now = new Date();
        
        db.beginTransaction();
        User user = new User("rcterm", "ses", "USER");
        db.persist(user);
        
        UserClass uc1 = new UserClass();
        uc1.setName("rcf");
        uc1.setActive(true);
        uc1.setQueuable(true);
        uc1.setPriority((short)4);
        db.persist(uc1);
        
        UserAssociation ass = new UserAssociation(new UserAssociationId(user.getId(), uc1.getId()), uc1, user);
        db.persist(ass);
        
        RigType rt = new RigType();
        rt.setName("Rig_Shutdown_Test");
        db.persist(rt);
        
        RigCapabilities caps = new RigCapabilities("rig,client,shutdown");
        db.persist(caps);
        
        Rig r = new Rig();
        r.setName("Rig_Shutdown_Test_1");
        r.setRigType(rt);
        r.setRigCapabilities(caps);
        r.setLastUpdateTimestamp(now);
        r.setActive(true);
        r.setOnline(false);
        r.setInSession(true);
        db.persist(r);
        
        ResourcePermission p1 = new ResourcePermission();
        p1.setType("RIG");
        p1.setUserClass(uc1);
        p1.setStartTime(now);
        p1.setExpiryTime(now);
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
        ses.setAssignedRigName(r.getName());
        ses.setRig(r);
        ses.setUser(user);
        ses.setUserName(user.getName());
        ses.setUserNamespace(user.getNamespace());
        db.persist(ses);

        db.getTransaction().commit();
        
        this.stopper.eventOccurred(RigStateChangeEvent.OFFLINE, r, db);
        

        db.refresh(ses);
        
        db.beginTransaction();
        db.delete(ses);
        db.delete(p1);
        db.delete(r);
        db.delete(rt);
        db.delete(caps);
        db.delete(ass);
        db.delete(uc1);
        db.delete(user);
        db.getTransaction().commit();
        
        assertTrue(ses.isActive());
        assertNull(ses.getRemovalTime());
        assertNull(ses.getRemovalReason());
    }
}
