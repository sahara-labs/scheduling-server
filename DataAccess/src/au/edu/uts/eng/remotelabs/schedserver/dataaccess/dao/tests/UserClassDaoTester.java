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
 * @date 14th March 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.tests;

import java.util.Date;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.junit.Test;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.GenericDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserClassDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserAssociation;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserAssociationId;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClass;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.tests.DataAccessTestSetup;

/**
 * Tests the {@link UserClassDao} class. 
 */
public class UserClassDaoTester extends TestCase
{
    /** Object of class under test. */
    private UserClassDao dao;

    @Override
    public void setUp() throws Exception
    {
        DataAccessTestSetup.setup();
        this.dao = new UserClassDao();
    }
    
    @Test
    public void testFindByName()
    {
        UserClass cls = new UserClass();
        cls.setName("class");
        cls.setPriority((short) 10);
        this.dao.persist(cls);
        
        UserClass ldCls = this.dao.findByName("class");
        assertNotNull(ldCls);
        assertEquals("class", ldCls.getName());
        assertEquals(10, ldCls.getPriority());
        
        this.dao.delete(cls);
    }
    
    @Test
    public void testFindNotFound()
    {
        assertNull(this.dao.findByName("doesnotexist"));
    }
    
    @Test
    public void testDeleteId()
    {
        Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        RigType ty = new RigType("type123", 10, false);
        ses.save(ty);
        RigCapabilities caps = new RigCapabilities("a,b");
        ses.save(caps);
        Rig rig = new Rig(ty, caps, "rig123", "http://rig/", new Date(), true, null, false, true, true);
        ses.save(rig);
        
        User us = new User("user", "userns", "ADMIN");
        ses.save(us);
        UserClass uc = new UserClass("clazz", (short) 10, true, true, true, true);
        ses.save(uc);
        ResourcePermission perm = new ResourcePermission(uc, "RIG", 3600, 300, (short) 5, 300, 300);
        perm.setExpiryTime(new Date());
        perm.setStartTime(new Date());
        perm.setRig(rig);
        ses.save(perm);
        UserAssociationId id = new UserAssociationId(us.getId(), uc.getId());
        UserAssociation assoc = new UserAssociation(id, uc, us);
        ses.save(assoc);
        ses.getTransaction().commit();
        
        this.dao.delete(uc.getId());
        
        GenericDao<ResourcePermission> rpDao = new GenericDao<ResourcePermission>(ResourcePermission.class);
        assertNull(rpDao.get(perm.getId()));
        ses.refresh(us);
        assertEquals(0, us.getUserAssociations().size());
        
        
        ses.beginTransaction();
        ses.delete(us);
        ses.delete(rig);
        ses.delete(caps);
        ses.delete(ty);
        ses.getTransaction().commit();
    }
    
    @Test 
    public void testDeleteName()
    {
        Session ses = DataAccessActivator.getNewSession();
        ses.beginTransaction();
        RigType ty = new RigType("type23", 10, false);
        ses.save(ty);
        RigCapabilities caps = new RigCapabilities("a,b");
        ses.save(caps);
        Rig rig = new Rig(ty, caps, "rig23", "http://rig/", new Date(), true, null, false, true, true);
        ses.save(rig);
        
        User us = new User("user", "userns", "ADMIN");
        ses.save(us);
        UserClass uc = new UserClass("clazz", (short) 10, true, true, true, true);
        ses.save(uc);
        ResourcePermission perm = new ResourcePermission(uc, "RIG", 3600, 300, (short) 5, 300, 300);
        perm.setExpiryTime(new Date());
        perm.setStartTime(new Date());
        perm.setRig(rig);
        ses.save(perm);
        UserAssociationId id = new UserAssociationId(us.getId(), uc.getId());
        UserAssociation assoc = new UserAssociation(id, uc, us);
        ses.save(assoc);
        ses.getTransaction().commit();
        
        this.dao.delete(this.dao.findByName("clazz"));
        
        GenericDao<ResourcePermission> rpDao = new GenericDao<ResourcePermission>(ResourcePermission.class);
        assertNull(rpDao.get(perm.getId()));
        ses.refresh(us);
        assertEquals(0, us.getUserAssociations().size());
        
        
        ses.beginTransaction();
        ses.delete(us);
        ses.delete(rig);
        ses.delete(caps);
        ses.delete(ty);
        ses.getTransaction().commit();
    }
}
