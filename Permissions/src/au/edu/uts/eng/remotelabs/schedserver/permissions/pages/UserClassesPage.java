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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserClassDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Bookings;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.Session;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClass;
import au.edu.uts.eng.remotelabs.schedserver.server.HostedPage;

/**
 * User classes administration page.
 */
public class UserClassesPage extends AbstractPermissionsPage
{
    @SuppressWarnings("unchecked")
    @Override
    public void setupView(HttpServletRequest req)
    {
        /* The view consists of all the existing groups. */
        List<UserClass> userClasses = this.db.createCriteria(UserClass.class)
                .addOrder(Order.desc("active"))
                .addOrder(Order.asc("name"))
                .list();
        
        
        this.context.put("userClasses", userClasses);
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


    @Override
    protected String getPageType()
    {
        return "User Classes";
    }

    public static HostedPage getHostedPage()
    {
        return new HostedPage("User Classes", UserClassesPage.class, "perm-groups", 
                "Allows user classes (groupings of users) to be created, read, updated and deleted.", true, true);
    }
}
