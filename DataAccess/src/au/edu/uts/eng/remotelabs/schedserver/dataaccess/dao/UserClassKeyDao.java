/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2012, University of Technology, Sydney
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
 * @date 3rd January 2012
 */
package au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao;

import java.io.Serializable;

import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClassKey;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClassKeyConstraint;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClassKeyRedemption;

/**
 * Data access object for the {@link UserClassKey} class.
 */
public class UserClassKeyDao extends GenericDao<UserClassKey>
{

    /**
     * Opens a new database session.
     */
    public UserClassKeyDao()
    {
        super(UserClassKey.class);
    }
    
    /** 
     * Reuses the specified session.
     * 
     * @param ses session to reuse.
     */
    public UserClassKeyDao(Session ses)
    {
        super(ses, UserClassKey.class);
    }
    
    /**
     * Finds a key using is redeem key value. If not found 
     * <tt>null</tt> is returned.
     * 
     * @param key key value
     * @return key or null if not found
     */
    public UserClassKey findKey(String key)
    {
        return (UserClassKey) this.session.createCriteria(UserClassKey.class)
                .add(Restrictions.eq("redeemKey", key))
                .uniqueResult();
    }
    
    @Override
    public void delete(Serializable id)
    {
        UserClassKey key = this.get(id);
        if (key != null) this.delete(key);
    }
    
    @Override
    public void delete(UserClassKey key)
    {
        this.logger.info("Deleting user class key '" + key.getRedeemKey() + "'.");
        
        /* Delete all constraints. */
        if (key.getConstraints().size() > 0)
        {
            this.session.beginTransaction();
            for (UserClassKeyConstraint constraint : key.getConstraints())
            {
                this.session.delete(constraint);
            }
            this.session.getTransaction().commit();
        }
        
        /* As there are potentially a large number of redepemtions, a DML
         * operation is used. */
        int num = (Integer) this.session.createCriteria(UserClassKeyRedemption.class)
                .add(Restrictions.eq("classKey", key))
                .setProjection(Projections.count("id"))
                .uniqueResult();
        if (num > 0)
        {
            /* Delete all redemptions. */
            this.logger.debug("To delete user class key '" + key.getRedeemKey() + "', " + num + " redemptions have to" +
            		" deleted.");
            this.session.beginTransaction();
            int numDeleted = this.session.createQuery("delete " + UserClassKeyRedemption.class.getSimpleName() + 
                            " uk where uk.classKey = :key")
                    .setEntity("key", key)
                    .executeUpdate();
            
            this.logger.info("Deleted " + numDeleted + " redemptions when deleting user class key '" + 
                    key.getRedeemKey() + "'.");
            this.session.getTransaction().commit();
        }
        
        super.delete(key);
    }
}
