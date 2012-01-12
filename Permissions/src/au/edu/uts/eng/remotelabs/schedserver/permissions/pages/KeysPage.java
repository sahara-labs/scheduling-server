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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserClassDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserClassKeyDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClass;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClassKey;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClassKeyConstraint;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClassKeyRedemption;
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
     * Returns the list of keys for a user class. If the parameter 'historical'
     * is part of the request parameters and has a value of 'true', non-usable
     * keys are returned otherwise usable keys are returned. Usable keys are 
     * those that are 'active', have remaining uses and have not elasped
     * their expiry time. 
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
        
        Criteria qu = this.db.createCriteria(UserClassKey.class)
                .add(Restrictions.eq("userTargeted", Boolean.FALSE))
                .addOrder(Order.asc("id"));
                
        if ("true".equals(request.getAttribute("historical")))
        {
            qu.add(Restrictions.disjunction()
                    .add(Restrictions.eq("active", Boolean.FALSE))
                    .add(Restrictions.eq("remaining", 0)))
              .add(Restrictions.or(
                      Restrictions.isNull("expiry"),
                      Restrictions.lt("expiry", new Date())));
        }
        else
        {
            qu.add(Restrictions.eq("active", Boolean.TRUE))
              .add(Restrictions.gt("remaining", 0))
              .add(Restrictions.or(
                      Restrictions.isNull("expiry"), 
                      Restrictions.gt("expiry", new Date())));
        }
        
        qu = qu.createCriteria("userClass")
                    .add(Restrictions.eq("name", className));
        
        for (UserClassKey key : (List<UserClassKey>) qu.list())
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
     * Returns all the details of an access key.
     * 
     * @param request request with key param
     * @return key details
     * @throws JSONException 
     */
    public JSONObject keyDetails(HttpServletRequest request) throws JSONException
    {
        JSONObject obj = new JSONObject();
        
        String keyStr = request.getParameter("key");
        if (keyStr == null)
        {
            this.logger.warn("Unable to provide key details because no 'key' parameter was provided.");
            obj.put("error", "No key specified.");
            return obj;
        }
     
        UserClassKey key = new UserClassKeyDao(this.db).findKey(keyStr);
        if (key == null)
        {
            this.logger.warn("Unable to provide key details because key '" + keyStr + "' was not found.");
            obj.put("error", "Key not found.");
            return obj;
        }
        
        obj.put("id", key.getId());
        obj.put("key", key.getRedeemKey());
        obj.put("userClass", key.getUserClass().getName());
        obj.put("active", key.isActive());
        obj.put("remaining", key.getRemaining());
        obj.put("userTargetted", key.isUserTargeted());
        obj.put("hasExpiry", key.getExpiry() != null);
        
        if (key.getExpiry() != null)
        {
            obj.put("expiry", key.getExpiry());
        }
        
        for (UserClassKeyConstraint constraint : key.getConstraints())
        {
            JSONObject cObj = new JSONObject();
            cObj.put("name", constraint.getName());
            cObj.put("value", constraint.getValue());
            obj.append("constraints", cObj);
        }

        for (UserClassKeyRedemption redemption : key.getRedemptions())
        {
            User user = redemption.getUser();
            if (user.getFirstName() != null && user.getLastName() != null)
            {
                obj.append("user", user.getLastName() + ", " + user.getFirstName());
            }
            else obj.append("user", user.getName());
        }
        
        return obj;
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
        
        /* Check if any constraints are set. */
        String constraints = PermissionActivator.getConfig("Access_Keys_Constraints");
        if (constraints != null)
        {
            for (String c : constraints.split(","))
            {
                c = c.trim();
                if (request.getParameter(c) == null) continue;
                
                UserClassKeyConstraint co = new UserClassKeyConstraint();
                co.setClassKey(newKey);
                co.setName(c);
                co.setValue(request.getParameter(c));
                this.db.persist(co);
                
                this.logger.info("Adding constraint for key '" + newKey.getRedeemKey() + "' with '" + c + "' = '" +
                        request.getParameter(c) + "'.");
            }
        }

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
     * Invalidates a key so it may no longer be used for redemption. 
     *  
     * @param request
     * @return response 
     * @throws JSONException
     */
    public JSONObject invalidateKey(HttpServletRequest request) throws JSONException
    {
        JSONObject obj = new JSONObject();
        obj.put("success", false);
        
        String keyStr = request.getParameter("key");
        if (keyStr == null)
        {
            this.logger.warn("Unable to invalidate key because the key was not specified.");
            obj.put("reason", "Key not specified.");
            return obj;
        }
        
        UserClassKey key = new UserClassKeyDao(this.db).findKey(keyStr);
        if (key == null)
        {
            this.logger.warn("Unable to invalidate key because the key '" + keyStr + "' was not found.");
            obj.put("reason", "Key not found.");
            return obj;
        }
        
        this.logger.info("Invalidating key '" + key.getRedeemKey() + "'.");
        key.setActive(false);
        
        this.db.beginTransaction();
        this.db.flush();
        this.db.getTransaction().commit();
        
        obj.put("success", true);
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
        
        if (request.getParameter("first") == null || request.getParameter("last") == null || 
                request.getParameter("email") == null || request.getParameter("expiry") == null ||
                request.getParameter("type") == null)
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
        key.generateKey();
        
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try
        {
            key.setExpiry(formatter.parse(request.getParameter("expiry")));
        }
        catch (ParseException e)
        {
            this.logger.warn("Unable to email user class access key because the expiry time '" + request.getParameter("expiry") +
            		"' was not in the correct format.");
            obj.put("reason", "Expiry in incorrect format.");
            return obj;
        }
        
        this.db.beginTransaction();
        this.db.persist(key);
        this.db.getTransaction().commit();
        
        /* Transient user who is the receipt of the message. */
        User user = new User();
        user.setFirstName(request.getParameter("first"));
        user.setLastName(request.getParameter("last"));
        user.setEmail(request.getParameter("email"));
        
        Map<String, String> macros = new HashMap<String, String>();
        macros.put("classname", userClass.getName().replaceAll("_", " "));
        macros.put("firstname", user.getFirstName());
        macros.put("lastname", user.getLastName());
        macros.put("expiry", key.getExpiry().toString());
        macros.put("key", key.getRedeemKey());
        macros.put("targeturl", this.generateEmailTarget(request.getParameter("type"), key.getRedeemKey())); 
        
        String message = request.getParameter("message");
        boolean useMessage = message != null && !"".equals(message);
        if (useMessage) macros.put("message", message);
        
        URL template = useMessage ? this.getClass().getResource("/META-INF/resources/Access_Key_Email_with_Message") :
            this.getClass().getResource("/META-INF/resources/Access_Key_Email");
        if (template == null)
        {
            this.logger.error("Unable to send access key email because the email template was not found. This is " +
            		"likely a build error.");
            obj.put("reason", "Template not found.");
            return obj;
        }
        
        MessengerService service = PermissionActivator.getMessenger();
        service.sendTemplatedMessage(user, template, macros);
        
        obj.put("success", true);
        return obj;
    }
    
    /**
     * Returns the configured email types.
     * 
     * @param request
     * @return response
     */
    public JSONArray getEmailKeyTypes(HttpServletRequest request)
    {
        JSONArray arr = new JSONArray();
        
        String types = PermissionActivator.getConfig("Access_Keys_Email_Targets");
        if (types == null)
        {
            this.logger.warn("No access key email types have been configured, atleast one is needed.");
            return arr;
        }
        
        for (String s : types.split(","))
        {
            s = s.trim();
            arr.put(s.substring(0, s.indexOf('=')));
        }
        
        return arr;
    }
    
    /**
     * Generate email target.
     *
     * @param type 
     * @param key key to email
     * @return email target
     */
    public String generateEmailTarget(String type, String key)
    {
        
        
        
        
        return key;
    }
    
    /**
     * Gets the possible constraints that may be used to validate key redemption. 
     * 
     * 
     * @param request
     * @return response
     */
    public JSONArray getConstraints(HttpServletRequest request) throws JSONException
    {
        JSONArray arr = new JSONArray();
        
        String constraints = PermissionActivator.getConfig("Access_Keys_Constraints");
        if (constraints == null)
        {
            this.logger.debug("No constraints configured for use access key restriction.");
            return arr;
        }
        
        for (String c : constraints.split(","))
        {
            c = c.trim();
            
            JSONObject cObj = new JSONObject();
            cObj.put("name", c);
            
            InputStream is = this.getClass().getResourceAsStream("/META-INF/resources/Constraints_" + c);
            if (is == null)
            {
                this.logger.debug("No constraint restrictions for access key constraint '" + c + "'.");
                cObj.put("hasRestriction", false);
            }
            else
            {
                cObj.put("hasRestriction", true);
                
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line = null;
                try
                {
                    while ((line = reader.readLine()) != null)
                    {
                        if (line.startsWith("#")) continue; // Comment line
                        cObj.accumulate("restriction", line);
                    }
                }
                catch (IOException e)
                {
                    this.logger.error("Error reading constraint restriction file '" + c + "'. Error message: " + 
                                e.getMessage());
                    cObj.put("hasRestriction", false);
                }
                finally
                {
                    try
                    {
                        reader.close();
                    }
                    catch (IOException e)
                    {
                        this.logger.error("Error closing constraint restriction file '" + c + "'. Error message: " + 
                                e.getMessage());
                    }
                }
                        
            }
            
            arr.put(cObj);
        }
        
        
        return arr;
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
