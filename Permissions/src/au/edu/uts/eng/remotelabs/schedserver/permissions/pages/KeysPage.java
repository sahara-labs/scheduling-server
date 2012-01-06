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
 * @date 20th October 2011
 */
package au.edu.uts.eng.remotelabs.schedserver.permissions.pages;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserClassDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClass;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClassKey;
import au.edu.uts.eng.remotelabs.schedserver.messenger.MessengerService;
import au.edu.uts.eng.remotelabs.schedserver.permissions.PermissionActivator;
import au.edu.uts.eng.remotelabs.schedserver.server.HostedPage;

/**
 * The permissions page.
 */
public class KeysPage extends AbstractPermissionsPage
{
    
    @Override
    public void setupView(HttpServletRequest req)
    {
        /* Does not have page. */
        throw new UnsupportedOperationException();
    }
    
    /**
     * Returns the list of keys for a user class.
     * 
     * @param request request
     * @return list of permission keys
     * @throws JSONException 
     */
    @SuppressWarnings("unchecked")
    public JSONArray getList(HttpServletRequest request) throws JSONException
    {
        JSONArray arr = new JSONArray();
        
        String className = request.getParameter("name");
        if (className == null)
        {
            this.logger.warn("Unable to provide access key list because the user class name was not provided.");
            return arr;
        }
        
        
        for (UserClassKey key : (List<UserClassKey>) this.db.createCriteria(UserClassKey.class)
                .add(Restrictions.eq("active", Boolean.TRUE))
                .add(Restrictions.eq("userTargeted", Boolean.FALSE))
                .addOrder(Order.desc("id"))
                .createCriteria("userClass")
                    .add(Restrictions.eq("name", className)).list())
        {
            JSONObject keyObj = new JSONObject();
            keyObj.put("key", key.getRedeemKey());
            keyObj.put("active", key.isActive());
            keyObj.put("remaining", key.getRemaining());
            arr.put(keyObj);
        }
        
        return arr;
    }

    /**
     * Adds a new access key.
     * 
     * @param request web request
     * @return whether successful
     * @throws JSONException
     */
    public JSONObject addKey(HttpServletRequest request) throws JSONException 
    {
        JSONObject obj = new JSONObject();
        obj.put("success", false);
        
        String className = request.getParameter("name");
        if (className == null)
        {
            this.logger.warn("Unable to create access key because the user class name was not provided.");
            return obj;
        }
        
        UserClass userClass = new UserClassDao(this.db).findByName(className);
        if (userClass == null)
        {
            this.logger.warn("Unable to create access key because the user class with name '" + className + "' was " +
            		"not found.");
            return obj;
        }
        
        UserClassKey newKey = new UserClassKey();
        newKey.setActive(true);
        newKey.setUserClass(userClass);
        
        try
        {
            newKey.setRemaining(Integer.parseInt(request.getParameter("uses")));
        }
        catch (NumberFormatException nfe)
        {
            this.logger.warn("Unable to create access key because the uses number was not supplied or invalid.");
            return obj;
        }
        
        if ("true".equals(request.getParameter("timelimited")) && 
                !(request.getParameter("expiry") == null || "".equals(request.getParameter("expiry"))))
        {
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            try
            {
                newKey.setExpiry(formatter.parse(request.getParameter("expiry")));
            }
            catch (ParseException e)
            {
                this.logger.warn("Not adding expiry time for key because the provided expiry '" + 
                        request.getParameter("expiry") + "' is not in the correct format.");
            }
            
        }
        
        newKey.generateKey();
        
        this.db.beginTransaction();
        this.db.persist(newKey);
        this.db.getTransaction().commit();
        
        this.logger.info("Added new access key '" + newKey.getRedeemKey() + "' for user class '" + 
                userClass.getName() + "'.");
        
        obj.put("success", true);
        obj.put("key", newKey.getRedeemKey());
        obj.put("active", newKey.isActive());
        obj.put("remaining", newKey.getRemaining());
        return obj;
    }
    
    /**
     * Generates a key and emails it to the specified user.
     * 
     * @param request request
     * @return whether successful
     * @throws JSONException
     */
    public JSONObject emailKey(HttpServletRequest request) throws JSONException
    {
        JSONObject obj = new JSONObject();
        obj.put("success", false);
        
        String ucName = request.getParameter("name");
        if (ucName == null)
        {
            this.logger.warn("Unable to email user access key because the user class name was not supplied.");
            obj.put("reason", "User class name not supplied.");
            return obj;
        }
        
        UserClass userClass = new UserClassDao(this.db).findByName(ucName);
        if (userClass == null)
        {
            this.logger.warn("Unable to email user access key because the user class '" + ucName + "' was not found.");
            obj.put("reason", "User class not found.");
            return obj;
        }
        
        if (request.getAttribute("first") == null || request.getAttribute("last") == null || 
                request.getAttribute("email") == null)
        {
            this.logger.warn("Unable to email user class access key because not all parameters were supplied.");
            obj.put("reason", "Missing parameter.");
            return obj;
        }
            
        UserClassKey key = new UserClassKey();
        key.setActive(true);
        key.setUserTargeted(true);
        key.setUserClass(userClass);
        key.setRemaining(1);
        
        this.db.beginTransaction();
        this.db.persist(key);
        this.db.getTransaction().commit();
        
        MessengerService service = PermissionActivator.getMessenger();

        
        obj.put("success", true);
        return obj;
    }
    
    private void sendAccessKeyEmail(String key, String email, String first, String last)
    {
        
    }

    @Override
    protected String getPageType()
    {
        return "Keys";
    }

    public static HostedPage getHostedPage()
    {
        return new HostedPage("Keys", KeysPage.class, null, null, false, false);
    }
}
