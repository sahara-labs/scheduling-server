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
 * @date 27th March 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao;

import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Bookings;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserLock;

/**
 * Data access object for the {@link ResourcePermission} entity.
 */
public class ResourcePermissionDao extends GenericDao<ResourcePermission>
{
    public ResourcePermissionDao()
    {
        super(ResourcePermission.class);
    }
    
    public ResourcePermissionDao(Session ses)
    {
        super(ses, ResourcePermission.class);
    }
    
    @Override
    public void delete(ResourcePermission perm)
    {
        /* We need to delete any user locks of this permission. */
        int num = (Integer) this.session.createCriteria(UserLock.class)
                .add(Restrictions.eq("resourcePermission", perm))
                .setProjection(Projections.count("id"))
                .uniqueResult();
        if (num > 0)
        {
            /* Delete all user locks. */
            this.logger.debug("To delete resource permission '" + perm.getId() + "', " + num + " user locks have to" +
                    " removed.");
            this.session.beginTransaction();
            int numDeleted = this.session.createQuery("delete UserLock ul where ul.resourcePermission = :perm")
                    .setEntity("perm", perm)
                    .executeUpdate();
            
            this.logger.debug("Deleted " + numDeleted + " user locks when deleting resource permission '" + 
                    perm.getId() + "'.");
            this.session.getTransaction().commit();
        }
        
        /* Null out the sessions which use this resource permission. */
        num = (Integer) this.session.createCriteria(au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session.class)
                .add(Restrictions.eq("resourcePermission", perm))
                .setProjection(Projections.count("id"))
                .uniqueResult();
        if (num > 0)
        {
            this.logger.debug("To delete resource permission '" + perm.getId() + "', " + num + " sessions need the " +
            		"resource permission nulled.");
            this.session.beginTransaction();
            int numDeleted = this.session.createQuery("update Session ses set ses.resourcePermission = null " +
            		        "where ses.resourcePermission = :perm")
                    .setEntity("perm", perm)
                    .executeUpdate();
            
            this.logger.debug("Updated " + numDeleted + " session records when deleting resource permission '" + 
                    perm.getId() + "'.");
            this.session.getTransaction().commit();
        }
        
        /* Null out the bookings which use this resource permission. */
        num = (Integer) this.session.createCriteria(Bookings.class)
                .add(Restrictions.eq("resourcePermission", perm))
                .setProjection(Projections.count("id"))
                .uniqueResult();
        if (num > 0)
        {
            this.logger.debug("To delete resource permission '" + perm.getId() + "', " + num + " bookings need to " +
            		"be deleted.");
            this.session.beginTransaction();
            int numDeleted = this.session.createQuery("delete Bookings bk where bk.resourcePermission = :perm")
                    .setEntity("perm", perm)
                    .executeUpdate();
            
            this.logger.debug("Updated " + numDeleted + " bookings records when deleting resource permission '" + 
                    perm.getId() + "'.");
            this.session.getTransaction().commit();
        }
        
        /* Finally delete the permission. */
        super.delete(perm);
    }
}
