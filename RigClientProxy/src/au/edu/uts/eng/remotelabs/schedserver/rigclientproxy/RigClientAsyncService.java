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
 * @date 5th April 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.rigclientproxy;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.hibernate.Session;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.RigClientAsyncServiceImpl;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.Allocate;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.Release;
import au.edu.uts.eng.remotelabs.schedserver.rigclientproxy.intf.types.UserType;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.identok.IdentityToken;

/**
 * Proxies call to the rig client.
 * <br />
 * The response from operations are to maps containing the object hierarchy 
 * of the operation response. The map key is the field name, the value is
 * the response field value castable to the type specifed in the SOAP Schema. 
 * Simple data types are represented by their object counterparts (e.g. int - 
 * Integer). If there are nested objects in the hierarchy, they are also 
 * represented as maps. If optional fields are not supplied, the field name
 * key is not present in the response map. For example 
 * <tt>OperationResponseType</tt>:
 * <ul>
 *  <li><b>success</b> - Boolean</li>
 *  <li><b>error</b> - Map
 *      <ul>
 *          <li><b>code</b> - Integer</li>
 *          <li><b>operation</b> - String</li>
 *          <li><b>reason</b> - String</li>
 *          <li><b>trace</b> - String</li>
 *      </ul></li>
 * </ul>
 * All calls using this proxy are provided with an identity token
 * the rig client is registered with.
 */
public class RigClientAsyncService
{
    /** The proxy implementation. */
    private RigClientAsyncServiceImpl service;
    
    /** The rig clients name. */
    private String rig;
    
    /** Identity token register. */
    private IdentityToken tok;
    
    /** Logger. */
    private Logger logger;
    
    public RigClientAsyncService(String rig) throws RigClientProxyException
    {
        this.rig = rig;
        this.logger = LoggerActivator.getLogger();
        
        if ((this.tok = RigClientProxyActivator.getIdentityTokenRegister()) == null)
        {
            this.logger.error("Unable to create a rig client proxy because no identity token register is found. " +
                    "Ensure the SchedulingServer-LocalRigProvider bundle is installed and active.");
            throw new RigClientProxyException("Identity token register not found.");
        }
        
        RigDao dao = new RigDao();
        Rig record = dao.findByName(rig);
        dao.closeSession();
        if (record == null || record.getContactUrl() == null)
        {
            this.logger.error("Unable to create a rig client proxy to rig " + rig + " because unable to determine its address.");
            throw new RigClientProxyException("Unable to find rig address.");
        }
        
        this.logger.debug("Creating a rig client proxy to " + rig + " at address " + record.getContactUrl() + '.');
        
        try
        {
            this.service = new RigClientAsyncServiceImpl(record.getContactUrl());
        }
        catch (AxisFault e)
        {
            this.logger.error("Unable to create a rig client proxy because of Axis fault with message: " + e.getMessage() + ".");
            throw new RigClientProxyException(e.getMessage(), e);
        }
    }
    
    public RigClientAsyncService(String rig, Session db) throws RigClientProxyException
    {
        this.rig = rig;
        this.logger = LoggerActivator.getLogger();
        
        if ((this.tok = RigClientProxyActivator.getIdentityTokenRegister()) == null)
        {
            this.logger.error("Unable to create a rig client proxy because no identity token register is found. " +
                    "Ensure the SchedulingServer-LocalRigProvider bundle is installed and active.");
            throw new RigClientProxyException("Identity token register not found.");
        }
        
        Rig record = new RigDao(db).findByName(rig);
        if (record == null || record.getContactUrl() == null)
        {
            this.logger.error("Unable to create a rig client proxy to rig " + rig + " because unable to determine its address.");
            throw new RigClientProxyException("Unable to find rig address.");
        }
        
        this.logger.debug("Creating a rig client proxy to " + rig + " at address " + record.getContactUrl() + '.');
        
        try
        {
            this.service = new RigClientAsyncServiceImpl(record.getContactUrl());
            
        }
        catch (AxisFault e)
        {
            this.logger.error("Unable to create a rig client proxy because of Axis fault with message: " + e.getMessage() + ".");
            throw new RigClientProxyException(e.getMessage(), e);
        }
    }
    
    /**
     * Request to allocate a specified user to the rig.
     * 
     * @param name user to allocate
     * @param callback response call back handler
     * @throws RemoteException 
     */
    public void allocate(String name, RigClientAsyncServiceCallbackHandler callback) throws RemoteException 
    {        
        Allocate request = new Allocate();
        UserType user = new UserType();
        request.setAllocate(user);
        user.setUser(name);
        user.setIdentityToken(this.tok.getIdentityToken(this.rig));

        this.service.callAllocate(request, callback);
    }
    
    /**
     * Request to release the specified user from the rig.
     * 
     * @param name user to release
     * @param callback response call back handler
     * @throws RemoteException
     */
    public void release(String name, RigClientAsyncServiceCallbackHandler callback) throws RemoteException
    {
        Release request = new Release();
        UserType user = new UserType();
        request.setRelease(user);
        user.setUser(name);
        user.setIdentityToken(this.tok.getIdentityToken(this.rig));
        
        this.service.callRelease(request, callback);
    }
}
