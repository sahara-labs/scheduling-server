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
package au.edu.uts.eng.remotelabs.schedserver.rigprovider.proxy;

import java.rmi.RemoteException;

import org.apache.axis2.AxisFault;
import org.hibernate.Session;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.identok.IdentityToken;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.identok.impl.IdentityTokenRegister;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.proxy.intf.RigClientAsyncServiceImpl;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.proxy.intf.types.Allocate;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.proxy.intf.types.IsActivityDetectable;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.proxy.intf.types.MaintenanceRequestType;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.proxy.intf.types.NotificationRequestType;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.proxy.intf.types.Notify;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.proxy.intf.types.NullType;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.proxy.intf.types.Release;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.proxy.intf.types.SetMaintenance;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.proxy.intf.types.UserType;

/**
 * Proxies call to the rig client, using a asynchronous message exchange pattern.
 * All calls take the same a callback of base case {@link RigClientAsyncServiceCallbackHandler}
 * which contains methods that will be invoked on operation response. For each service
 * (for exampele <tt>allocate</tt>), there are two methods which can be overridden which may
 * be invoked on response or error of the call:
 * <ul>
 *  <li><tt><em>&lt;operation&gt;</em>ResponseCallback</tt> - Invoked on response from the operation.
 *  </li>
 *  <li><tt><em>&lt;operation&gt;</em>ErrorCallback</tt> - Invoked if a SOAP fault is received instead
 *  of the operation response.</li>
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
    
    public RigClientAsyncService(String rig) throws RigProxyException
    {
        this.rig = rig;
        this.logger = LoggerActivator.getLogger();
        
        if ((this.tok = IdentityTokenRegister.getInstance()) == null)
        {
            this.logger.error("Unable to create a rig client proxy because no identity token register is found. " +
                    "Ensure the SchedulingServer-LocalRigProvider bundle is installed and active.");
            throw new RigProxyException("Identity token register not found.");
        }
        
        RigDao dao = new RigDao();
        Rig record = dao.findByName(rig);
        dao.closeSession();
        if (record == null || record.getContactUrl() == null)
        {
            this.logger.error("Unable to create a rig client proxy to rig " + rig + " because unable to determine its address.");
            throw new RigProxyException("Unable to find rig address.");
        }
        
        this.logger.debug("Creating a rig client proxy to " + rig + " at address " + record.getContactUrl() + '.');
        
        try
        {
            this.service = new RigClientAsyncServiceImpl(record.getContactUrl());
        }
        catch (AxisFault e)
        {
            this.logger.error("Unable to create a rig client proxy because of Axis fault with message: " + e.getMessage() + ".");
            throw new RigProxyException(e.getMessage(), e);
        }
    }
    
    public RigClientAsyncService(String rig, Session db) throws RigProxyException
    {
        this.rig = rig;
        this.logger = LoggerActivator.getLogger();
        
        if ((this.tok = IdentityTokenRegister.getInstance()) == null)
        {
            this.logger.error("Unable to create a rig client proxy because no identity token register is found. " +
                    "Ensure the SchedulingServer-LocalRigProvider bundle is installed and active.");
            throw new RigProxyException("Identity token register not found.");
        }
        
        Rig record = new RigDao(db).findByName(rig);
        if (record == null || record.getContactUrl() == null)
        {
            this.logger.error("Unable to create a rig client proxy to rig " + rig + " because unable to determine its address.");
            throw new RigProxyException("Unable to find rig address.");
        }
        
        this.logger.debug("Creating a rig client proxy to " + rig + " at address " + record.getContactUrl() + '.');
        
        try
        {
            this.service = new RigClientAsyncServiceImpl(record.getContactUrl());
            
        }
        catch (AxisFault e)
        {
            this.logger.error("Unable to create a rig client proxy because of Axis fault with message: " + e.getMessage() + ".");
            throw new RigProxyException(e.getMessage(), e);
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
     * Request to allocate a specified user to the rig. This method allows a 
     * async 'hint' to be specified.
     * 
     * @param name user to allocate
     * @param async async hint 
     * @param callback response call back handler
     * @throws RemoteException 
     */
    public void allocate(String name, boolean async, RigClientAsyncServiceCallbackHandler callback) 
            throws RemoteException 
    {        
        Allocate request = new Allocate();
        UserType user = new UserType();
        request.setAllocate(user);
        user.setUser(name);
        user.setIdentityToken(this.tok.getIdentityToken(this.rig));
        user.setAsync(async);

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
    
    /**
     * Request to release the specified user from the rig. This method allows a 
     * async 'hint' to specified.
     * 
     * @param name user to release
     * @param async hint
     * @param callback response call back handler
     * @throws RemoteException
     */
    public void release(String name, boolean async, RigClientAsyncServiceCallbackHandler callback) 
            throws RemoteException
    {
        Release request = new Release();
        UserType user = new UserType();
        request.setRelease(user);
        user.setUser(name);
        user.setIdentityToken(this.tok.getIdentityToken(this.rig));
        user.setAsync(async);
        
        this.service.callRelease(request, callback);
    }
    
    /**
     * Request to notify the in session users of a message.
     * 
     * @param message message to notify
     * @param callback response call back handler
     * @throws RemoteException
     */
    public void notify(String message, RigClientAsyncServiceCallbackHandler callback) throws RemoteException
    {
        Notify request = new Notify();
        NotificationRequestType notif = new NotificationRequestType();
        request.setNotify(notif);
        notif.setIdentityToken(this.tok.getIdentityToken(this.rig));
        notif.setMessage(message);
        
        this.service.callNotify(request, callback);
    }
    
    /**
     * Request to check if there is session activity on a rig.
     * 
     * @param callback response call back handler
     * @throws RemoteException
     */
    public void isActivityDetectable(RigClientAsyncServiceCallbackHandler callback) throws RemoteException
    {
        IsActivityDetectable request = new IsActivityDetectable();
        NullType nll = new NullType();
        request.setIsActivityDetectable(nll);
        nll.set_void("Hello, World!");
        
        this.service.callIsActivityDetectable(request, callback);
    }
    
    /**
     * Request to set or clear maintenance state on the rig.
     * 
     * @param putOffline whether maintenance is being set or cleared
     * @param runTests whether exerciser tests are to be run
     * @param callback response callback handler
     * @throws RemoteException
     */
    public void setMaintenance(boolean putOffline, boolean runTests, RigClientAsyncServiceCallbackHandler callback)
            throws RemoteException
    {
        SetMaintenance request = new SetMaintenance();
        MaintenanceRequestType params = new MaintenanceRequestType();
        request.setSetMaintenance(params);
        params.setIdentityToken(this.tok.getIdentityToken(this.rig));
        params.setPutOffline(putOffline);
        params.setRunTests(runTests);
        
        this.service.callSetMaintenance(request, callback);
    }
}
