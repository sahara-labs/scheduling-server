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
 * @date 21st October 2011
 */
package au.edu.uts.eng.remotelabs.schedserver.permissions.pages;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RequestCapabilitiesDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.ResourcePermissionDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.RigTypeDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserClassDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Bookings;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RequestCapabilities;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.ResourcePermission;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Rig;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.RigType;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClass;
import au.edu.uts.eng.remotelabs.schedserver.server.HostedPage;

/**
 * User classes administration page.
 */
public class UserClassesPage extends AbstractPermissionsPage
{
    public UserClassesPage()
    {
        super();
        
        this.headJs.add("/js/jquery-ui-timepicker-addon.js");
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void setupView(HttpServletRequest req)
    {
        /* The view consists of all the existing groups. */
        List<UserClass> userClasses = this.db.createCriteria(UserClass.class)
                .addOrder(Order.desc("active"))
                .addOrder(Order.asc("name"))
                .list();
        
        
        Map<Long, List<ResourcePermission>> resourcePermissions = 
                new HashMap<Long, List<ResourcePermission>>(userClasses.size());
        for (UserClass uc : userClasses)
        {
            resourcePermissions.put(uc.getId(), this.db.createCriteria(ResourcePermission.class)
                    .add(Restrictions.eq("userClass", uc))
                    .addOrder(Order.asc("displayName"))
                    .addOrder(Order.desc("type"))
                    .addOrder(Order.desc("rigType"))
                    .addOrder(Order.desc("rig"))
                    .addOrder(Order.desc("requestCapabilities"))
                    .addOrder(Order.asc("startTime"))
                    .list());
        }
        
        this.context.put("userClasses", userClasses);
        this.context.put("resourcePermissions", resourcePermissions);
    }
    
    
    /**
     * Adds a new user class. 
     * 
     * @param req request
     * @return response JSON object
     */
    public JSONObject addClass(HttpServletRequest req) throws JSONException
    {
        JSONObject response = new JSONObject();
        response.put("wasSuccessful", false);
        
        String name = req.getParameter("name");
        /* Replace all spaces with underscores. */
        name = name.replace(' ', '_');
        
        
        if (name == null)
        {
            this.logger.warn("Unable to add new user class because the class name was not specified.");
            response.put("reason", "Name was not specified.");
            return response;
        }
        
        /* User class names must be unique. */
        UserClassDao dao = new UserClassDao(this.db);
        if (dao.findByName(name) != null)
        {
            this.logger.warn("Unable to add new user class because the class '" + name + "' already exists.");
            response.put("reason", "User class with name already exists.");
            return response;
        }
        
        UserClass uc = new UserClass();
        uc.setName(name);
        uc.setActive(Boolean.parseBoolean(req.getParameter("active")));
        uc.setBookable(Boolean.parseBoolean(req.getParameter("bookable")));
        uc.setQueuable(Boolean.parseBoolean(req.getParameter("queue")));
        uc.setPriority(Short.parseShort(req.getParameter("priority")));      
        uc.setTimeHorizon(Integer.parseInt(req.getParameter("horizon")));
        
        /* We are hard coding some parameters, because the features aren't
         * used much and they complicate the interface. */
        uc.setKickable(false);
        uc.setUsersLockable(false);
        
        dao.persist(uc);
        
        response.put("wasSuccessful", true);
        return response;
    }
    
    /**
     * Updates an existing class.
     * 
     * @param request 
     * @return response
     * @throws JSONException
     */
    public JSONObject updateClass(HttpServletRequest req) throws JSONException
    {
        JSONObject response = new JSONObject();
        response.put("wasSuccessful", false);
        
        String name = req.getParameter("name");
        if (name == null)
        {
            this.logger.warn("Unable to update user class because the class name was not specified.");
            response.put("reason", "Name was not specified.");
            return response;
        }
        
        UserClassDao dao = new UserClassDao(this.db);
        UserClass uc = dao.findByName(name);
        if (uc == null)
        {
            this.logger.warn("Unable to update user class because the class '" + name + "' was not found.");
            response.put("reason", "User class was not found.");
            return response;
        }

        uc.setActive(Boolean.parseBoolean(req.getParameter("active")));
        uc.setBookable(Boolean.parseBoolean(req.getParameter("bookable")));
        uc.setQueuable(Boolean.parseBoolean(req.getParameter("queue")));
        uc.setPriority(Short.parseShort(req.getParameter("priority")));      
        uc.setTimeHorizon(Integer.parseInt(req.getParameter("horizon")));
        
        dao.flush();
        
        response.put("wasSuccessful", true);
        return response;
    }
    
    /**
     * Deletes a user class.
     * 
     * @param req request
     * @return response
     * @throws JSONException
     */
    public JSONObject deleteClass(HttpServletRequest req) throws JSONException
    {
        JSONObject response = new JSONObject();
        response.put("wasSuccessful", false);
        
        String name = req.getParameter("name");
        if (name == null)
        {
            this.logger.warn("Unable to delete user class because the class name was not specified.");
            response.put("reason", "Name was not specified.");
            return response;
        }
        
        UserClassDao dao = new UserClassDao(this.db);
        UserClass uc = dao.findByName(name);
        if (uc == null)
        {
            this.logger.warn("Unable to delete user class because the class '" + name + "' was not found.");
            response.put("reason", "User class was not found.");
            return response;
        }
        
        
        this.logger.debug("Attempting to delete user class '" + uc.getName() + "'.");
        
        int num = (Integer) this.db.createCriteria(Session.class)
                .add(Restrictions.eq("active", Boolean.TRUE))
                .createCriteria("resourcePermission")
                    .add(Restrictions.eq("userClass", uc))
                    .setProjection(Projections.rowCount())
                    .uniqueResult();
        if (num > 0)
        {
            this.logger.warn("Unable to delete user class '" + uc.getName() + "' because a session from this classes " +
            		"permission is active.");
            response.put("reason", "A session from this class is in progress.");
            return response;
        }
        
        num = (Integer) this.db.createCriteria(Bookings.class)
                .add(Restrictions.eq("active", Boolean.TRUE))
                .createCriteria("resourcePermission")
                    .add(Restrictions.eq("userClass", uc))
                    .setProjection(Projections.rowCount())
                    .uniqueResult();
        if (num > 0)
        {
            this.logger.warn("Unable to delete user class '" + uc.getName() + "' because a booking from this classes " +
                    "permission is active.");
            response.put("reason", "User class has active reservations.");
            return response;
        }
        
        dao.delete(uc);
        this.logger.info("Deleted user class '" + uc.getName() + "'.");
        
        response.put("wasSuccessful", true);
        return response;
    }
    
    /**
     * Loads a list of resources for a specified resource type.
     * 
     * @param request
     * @return response
     */
    public JSONArray loadResources(HttpServletRequest request)
    {
        JSONArray list = new JSONArray();
        
        if ("RIG".equals(request.getParameter("type")))
        {
            for (Rig rig : new RigDao(this.db).list())
            {
                list.put(rig.getName());
            }
        }
        else if ("RIGTYPE".equals(request.getParameter("type")))
        {
            for (RigType rigType : new RigTypeDao(this.db).list())
            {
                list.put(rigType.getName());
            }
        }
        else if ("CAPABILITY".equals(request.getParameter("type")))
        {
            for (RequestCapabilities caps : new RequestCapabilitiesDao(this.db).list())
            {
                list.put(caps.getCapabilities());
            }
        }
        
        return list;
    }
    
    /**
     * Adds a new permission.
     * 
     * @param request
     * @return response
     * @throws JSONException
     * @throws ParseException
     */
    public JSONObject addPermission(HttpServletRequest request) throws JSONException, ParseException
    {
        JSONObject obj = new JSONObject();
        obj.put("success", false);

        UserClass uClass = new UserClassDao(this.db).findByName(request.getParameter("Class"));
        if (uClass == null)
        {
            this.logger.warn("Unable to create resource permission because the user class with name '" + 
                    request.getParameter("Class") + "' was not found.");
            return obj;
        }
        
        ResourcePermission perm = new ResourcePermission();
        perm.setUserClass(uClass);
        
        perm.setSessionActivityTimeout(Integer.parseInt(request.getParameter("SessionDetectionTimeout")));
        perm.setSessionDuration(Integer.parseInt(request.getParameter("SessionDuration")));
        perm.setMaximumBookings(Integer.parseInt(request.getParameter("MaximumBookings")));
        perm.setExtensionDuration(Integer.parseInt(request.getParameter("ExtensionDuration")));
        perm.setAllowedExtensions((short)Integer.parseInt(request.getParameter("AllowedExtensions")));
        perm.setQueueActivityTimeout(Integer.parseInt(request.getParameter("QueueActivityTimeout")));
        perm.setActivityDetected("true".equals(request.getParameter("UseActivityDectection")));
        
        if (!"".equals(request.getParameter("DisplayName")))
        {
            perm.setDisplayName(request.getParameter("DisplayName"));
        }
        
        if ("Rig".equals(request.getParameter("Type")))
        {
            perm.setType(ResourcePermission.RIG_PERMISSION);
            
            Rig rig = new RigDao(this.db).findByName(request.getParameter("Resource"));
            if (rig == null)
            {
                this.logger.warn("Unable to create a rig resource permission because the rig with name '" +
                        request.getParameter("Resource") + "' was not found.");
                return obj;
            }
            perm.setRig(rig);
        }
        else if ("Rig Type".equals(request.getParameter("Type")))
        {
            perm.setType(ResourcePermission.TYPE_PERMISSION);
            
            RigType rigType = new RigTypeDao(this.db).findByName(request.getParameter("Resource"));
            if (rigType == null)
            {
                this.logger.warn("Unable to create a rig type resource permission because the rig type with name '" +
                        request.getParameter("Resource") + "' was not found.");
                return obj;
            }
            perm.setRigType(rigType);
        }
        else if ("Capability".equals(request.getParameter("Type")))
        {
            perm.setType(ResourcePermission.CAPS_PERMISSION);
            
            RequestCapabilities caps = new RequestCapabilitiesDao(this.db).findCapabilites(request.getParameter("Resource"));
            if (caps == null)
            {
                this.logger.warn("Unable to create a request capabilities resource permission because the " +
                		"capabilities '" + request.getParameter("Resource") + "' was not found.");
                return obj;
            }
            perm.setRequestCapabilities(caps);
        }
        else
        {
            this.logger.warn("Unable to create a resource permission because the permission type of '" + 
                    request.getParameter("Type") + "' is not one of 'RIG', 'RIGTYPE' or 'CAPABILITY'.");
            return obj;
        }
        
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        perm.setStartTime(formatter.parse(request.getParameter("StartTime")));
        perm.setExpiryTime(formatter.parse(request.getParameter("ExpiryTime")));
        
        if (perm.getExpiryTime().before(perm.getStartTime()))
        {
            this.logger.warn("Unable to create a resource permission because the start time is after the " +
            		"expiry time.");
            return obj;
        }
        
        this.db.beginTransaction();
        this.db.persist(perm);
        this.db.getTransaction().commit();
        
        this.logger.info("Adding new resource permission (id=" + perm.getId() + ") for user class '" + 
                uClass.getName() + "'.");
        
        obj.put("success", true);
        obj.put("id", perm.getId());
        return obj;
    }
    
    /**
     * Updates an existing permission.
     * 
     * @param request
     * @return response
     * @throws JSONException
     * @throws ParseException
     */
    public JSONObject savePermission(HttpServletRequest request) throws JSONException, ParseException
    {
        JSONObject obj = new JSONObject();
        obj.put("success", false);
        
         
        ResourcePermission perm = new ResourcePermissionDao(this.db).get(Long.parseLong(request.getParameter("Id")));
        if (perm == null)
        {
            this.logger.warn("Unable to update a resource permission because the permission with identifier '" +
                    request.getParameter("Id") + "' was not found.");
            return obj;
        }
        
        perm.setSessionActivityTimeout(Integer.parseInt(request.getParameter("SessionDetectionTimeout")));
        perm.setSessionDuration(Integer.parseInt(request.getParameter("SessionDuration")));
        perm.setMaximumBookings(Integer.parseInt(request.getParameter("MaximumBookings")));
        perm.setExtensionDuration(Integer.parseInt(request.getParameter("ExtensionDuration")));
        perm.setAllowedExtensions((short)Integer.parseInt(request.getParameter("AllowedExtensions")));
        perm.setQueueActivityTimeout(Integer.parseInt(request.getParameter("QueueActivityTimeout")));
        perm.setActivityDetected("true".equals(request.getParameter("UseActivityDectection")));
        
        if ("".equals(request.getParameter("DisplayName")))
        {
            perm.setDisplayName(null);
        }
        else
        {
            perm.setDisplayName(request.getParameter("DisplayName"));
        }
        
        if ("Rig".equals(request.getParameter("Type")))
        {
            perm.setType(ResourcePermission.RIG_PERMISSION);
            
            Rig rig = new RigDao(this.db).findByName(request.getParameter("Resource"));
            if (rig == null)
            {
                this.logger.warn("Unable to update a rig resource permission because the rig with name '" +
                        request.getParameter("Resource") + "' was not found.");
                return obj;
            }
            perm.setRig(rig);
        }
        else if ("Rig Type".equals(request.getParameter("Type")))
        {
            perm.setType(ResourcePermission.TYPE_PERMISSION);
            
            RigType rigType = new RigTypeDao(this.db).findByName(request.getParameter("Resource"));
            if (rigType == null)
            {
                this.logger.warn("Unable to update a rig type resource permission because the rig type with name '" +
                        request.getParameter("Resource") + "' was not found.");
                return obj;
            }
            perm.setRigType(rigType);
        }
        else if ("Capability".equals(request.getParameter("Type")))
        {
            perm.setType(ResourcePermission.CAPS_PERMISSION);
            
            RequestCapabilities caps = new RequestCapabilitiesDao(this.db).findCapabilites(request.getParameter("Resource"));
            if (caps == null)
            {
                this.logger.warn("Unable to update a request capabilities resource permission because the " +
                        "capabilities '" + request.getParameter("Resource") + "' was not found.");
                return obj;
            }
            perm.setRequestCapabilities(caps);
        }
        else
        {
            this.logger.warn("Unable to update a resource permission because the permission type of '" + 
                    request.getParameter("Type") + "' is not one of 'RIG', 'RIGTYPE' or 'CAPABILITY'.");
            return obj;
        }
        
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        perm.setStartTime(formatter.parse(request.getParameter("StartTime")));
        perm.setExpiryTime(formatter.parse(request.getParameter("ExpiryTime")));
        
        if (perm.getExpiryTime().before(perm.getStartTime()))
        {
            this.logger.warn("Unable to update a resource permission because the start time is after the " +
                    "expiry time.");
            return obj;
        }
        
        this.db.beginTransaction();
        this.db.flush();
        this.db.getTransaction().commit();
        
        this.logger.info("Updaing resource permission (id=" + perm.getId() + ")."); 
        
        obj.put("success", true);
        obj.put("id", perm.getId());
        return obj;
    }
    
    /**
     * Deletes a permission.
     * 
     * @param request
     * @return response
     * @throws JSONException
     */
    public JSONObject deletePermission(HttpServletRequest request) throws JSONException
    {
        JSONObject obj = new JSONObject();
        obj.put("success", false);
        
        ResourcePermissionDao dao = new ResourcePermissionDao(this.db);
        
        ResourcePermission perm = dao.get(Long.parseLong(request.getParameter("pid")));
        if (perm == null)
        {
            this.logger.warn("Unable to delete resource permission with identifier '" + request.getParameter("pid") + 
                    "' because the permission was not found.");
            obj.put("reason", "Permission not found");
            return obj;
        }
        
        int num = (Integer) this.db.createCriteria(Session.class)
                .add(Restrictions.eq("active", Boolean.TRUE))
                .add(Restrictions.eq("resourcePermission", perm))
                .setProjection(Projections.rowCount())
                .uniqueResult();
        if (num > 0)
        {
            this.logger.warn("Unable to delete resource permission '" + perm.getId() + "' because a session from this " +
                    "permission is active.");
            obj.put("reason", "A session from this class is in progress.");
            return obj;
        }
        
        num = (Integer) this.db.createCriteria(Bookings.class)
                .add(Restrictions.eq("active", Boolean.TRUE))
                .add(Restrictions.eq("resourcePermission", perm))
                .setProjection(Projections.rowCount())
                .uniqueResult();
        if (num > 0)
        {
            this.logger.warn("Unable to delete permission '" + perm.getId() + "' because a booking from this " +
                    "permission is active.");
            obj .put("reason", "Permission has active reservations.");
            return obj;
        }
        
        dao.delete(perm);
        this.logger.info("Resource permission '" + perm.getId() + "' has been deleted.");
        
        obj.put("success", true);
        return obj;
    }

    /**
     * Gets a permissions details.
     * 
     * @param request
     * @return response
     * @throws JSONException
     */
    public JSONObject getPermission(HttpServletRequest request) throws JSONException
    {
        JSONObject obj = new JSONObject();
        
        ResourcePermission perm = new ResourcePermissionDao(this.db).get(Long.parseLong(request.getParameter("id")));
        if (perm != null)
        {
            obj.put("DisplayName", perm.getDisplayName());
            
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            obj.put("StartTime", formatter.format(perm.getStartTime()));
            obj.put("ExpiryTime", formatter.format(perm.getExpiryTime()));
            
            obj.put("MaximumBookings", perm.getMaximumBookings());
            obj.put("SessionDuration", perm.getSessionDuration());
            obj.put("AllowedExtensions", perm.getAllowedExtensions());
            obj.put("ExtensionDuration", perm.getExtensionDuration());
            obj.put("SessionDetectionTimeout", perm.getSessionActivityTimeout());
            obj.put("QueueActivityTimeout", perm.getQueueActivityTimeout());
            obj.put("UseActivityDectection", perm.isActivityDetected());
        
            if (ResourcePermission.RIG_PERMISSION.equals(perm.getType()))
            {
                obj.put("Type", "Rig");
                obj.put("Resource", perm.getRig().getName());
            }
            else if (ResourcePermission.TYPE_PERMISSION.equals(perm.getType()))
            {
                obj.put("Type", "Rig Type");
                obj.put("Resource", perm.getRigType().getName());
            }
            else if (ResourcePermission.CAPS_PERMISSION.equals(perm.getType()))
            {
                obj.put("Type", "Capabilities");
                obj.put("Resource", perm.getRequestCapabilities().getCapabilities());
            }
        }
        
        return obj;
    }
    
    /**
     * Deletes all user associations in the specified class.
     * 
     * @param request 
     * @return response
     * @throws JSONException
     */
    public JSONObject deleteAllUsersInClass(HttpServletRequest request) throws JSONException
    {
        JSONObject obj = new JSONObject();
        obj.put("success", false);
        
        UserClassDao dao = new UserClassDao(this.db);
        
        UserClass uc = dao.findByName(request.getParameter("name"));
        if (uc == null)
        {
            this.logger.warn("Unable to delete all user associations in user class with name '" + 
                    request.getParameter("name") + "' because the class was not found.");
            return obj;
        }
        
        dao.deleteUserAssociations(uc);
        this.logger.info("User associations for class '" + uc.getName() + "' have been deleted.");
        
        obj.put("success", true);
        return obj;
    }

    @Override
    protected String getPageType()
    {
        return "Permissions";
    }

    public static HostedPage getHostedPage()
    {
        return new HostedPage("Permissions", UserClassesPage.class, "perm-groups", 
                "Allows user classes (groupings of users) and their permissions to be created, read, updated " +
                "and deleted.", true, true);
    }
}
