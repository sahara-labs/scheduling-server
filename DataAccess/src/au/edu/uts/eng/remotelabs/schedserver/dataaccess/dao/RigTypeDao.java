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
 * @date 11th January 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.DataAccessActivator;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RemoteSite;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;

/**
 * Data access object for the {@link RigType} class.
 */
public class RigTypeDao extends GenericDao<RigType>
{ 
    /**
     * Constructor that opens a new session.
     * 
     * @throws IllegalStateException if a session factory cannot be obtained
     */
    public RigTypeDao()
    {
        super(RigType.class);
    }
    
    /**
     * Constructor that uses the provided session. The session must be 
     * not-null and open.
     * 
     * @param ses open session
     * @throws IllegalStateException if the provided use session is null or
     *         closed
     */
    public RigTypeDao(Session ses)
    {
        super(ses, RigType.class);
    }
    
    /**
     * Returns the list of rig types ordered by name.
     * 
     * @return list of entities
     */
    @SuppressWarnings("unchecked")
	public List<RigType> orderedList()
    {
        return this.session.createCriteria(this.clazz).addOrder(Order.asc("name")).list();
    }
    
    /**
     * Returns the rig type with the specified name. If non exist, 
     * <code>null</code> is returned.
     * 
     * @param name rig type name
     * @return rig type or null if not found
     */
    public RigType findByName(String name)
    {
        return (RigType) this.session.createCriteria(this.clazz)
                .add(Restrictions.eq("name", name))
                .uniqueResult();
    }
    
    /**
     * Finds a meta rig with the specified name and meta data.
     * 
     * @param name name of rig type
     * @param meta meta data
     * @return rig type or null if not found
     */
    public RigType findMetaType(String name, String meta)
    {
        return (RigType) this.session.createCriteria(this.clazz)
                .add(Restrictions.eq("name", name))
                .add(Restrictions.like("meta", '%' + meta + '%'))
                .uniqueResult();
    }
    
    /**
     * If a rig type exists, a persistent instance of it's record is returned,
     * if not a new rig type is created with all default parameters and 
     * returned.
     * <br />
     * If the managed flag already exists, the managed flag is ignored.
     * 
     * @param name the name of a rig type
     * @param managed whether the rig type is managed
     * @return rig type persistent instance
     */
    public RigType loadOrCreate(final String name, boolean managed)
    {
        return this.loadOrCreate(name, managed, null, null);
    }
    
    /**
     * If a rig type exists, a persistent instance of it's record is returned,
     * if not a new rig type is created with all default parameters and 
     * returned.
     * <br />
     * If the rig type already exists, the managed flag is ignored.
     * 
     * @param name the name of a rig type
     * @param managed whether the rig type is managed
     * @param meta meta data about rig (optional)
     * @param site the site the rig type resides
     * @return rig type persistent instance
     */
    public RigType loadOrCreate(final String name, boolean managed, String meta, RemoteSite site)
    {
        RigType rigType = this.findByName(name);
        if (rigType == null)
        {
            rigType = new RigType();
            rigType.setName(name);
            rigType.setCodeAssignable(Boolean.parseBoolean(
                    DataAccessActivator.getProperty("Default_Rig_Type_Is_Code_Assignable", "false")));
            rigType.setManaged(managed);
            
            /* Load the default log off grace period. */
            try
            {
                rigType.setLogoffGraceDuration(Integer.parseInt(
                        DataAccessActivator.getProperty("Default_Rig_Type_Logoff_Grace", "180")));
            }
            catch (NumberFormatException ex)
            {
                rigType.setLogoffGraceDuration(180);
            }
            
            /* Add default setup and tear down times. */
            rigType.setSetUpTime(0);
            rigType.setTearDownTime(0);
            
            if (meta != null)
            {
                rigType.setMeta(meta);
            }
            
            if (site != null)
            {
                rigType.setSite(site);
            }
            
            this.logger.debug("Rig type '" + name + "' does not exist, creating a new rig type with parameters: " +
                    "name=" + rigType.getName() + ", code assignable=" + rigType.isCodeAssignable() + 
                    ". logoff grace=" + rigType.getLogoffGraceDuration() + " seconds.");
            rigType = this.persist(rigType);
        }
        
        return rigType;
    }
}
