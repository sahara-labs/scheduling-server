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

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.Criteria;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserAssociationDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserClassDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.dao.UserDao;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClass;
import au.edu.uts.eng.remotelabs.schedserver.server.HostedPage;

/**
 * User administration page.
 */
public class UsersPage extends AbstractPermissionsPage
{
    @Override
    public void setupView(HttpServletRequest req)
    {
        // TODO Implement user management page.
    }
    
    /**
     * Gets a list of users with a specific search term. The users may be 
     * excluded from a specific class. 
     * 
     * @param request
     * @return response
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public JSONArray list(HttpServletRequest request) throws JSONException
    {
        JSONArray arr = new JSONArray();
        
        Criteria qu = this.db.createCriteria(User.class);
            
        String search = request.getParameter("search");
        if (search != null)
        {
            /* Search filter. */
            qu.add(Restrictions.disjunction()
                    .add(Restrictions.like("name", search, MatchMode.ANYWHERE))
                    .add(Restrictions.like("firstName", search, MatchMode.ANYWHERE))
                    .add(Restrictions.like("lastName", search, MatchMode.ANYWHERE)));
        }
        
        if (request.getParameter("max") != null)
        {
            /* Max results. */
            qu.setMaxResults(Integer.parseInt(request.getParameter("max")));
        }
        
        if (request.getParameter("in") != null)
        {
            /* Users in class. */
            UserClass inClass = new UserClassDao(this.db).findByName(request.getParameter("in"));
            if (inClass == null)
            {
                this.logger.warn("Not going to add in class as a user list restriction because the class '" + 
                        request.getParameter("in") + "' was not found.");
            }
            else
            {
                qu.createCriteria("userAssociations")
                    .add(Restrictions.eq("userClass", inClass));
            }
        }
        
        if (request.getParameter("notIn") != null)
        {
            /* Users not in class. */
            UserClass notInClass = new UserClassDao(this.db).findByName(request.getParameter("notIn"));
            if (notInClass == null)
            {
                this.logger.warn("Not going to add not in class as a user list restriction because the class '" +
                		request.getParameter("notIn") + "' was not found.");
            }
            else
            {
                DetachedCriteria subQu = DetachedCriteria.forClass(User.class)
                        .setProjection(Property.forName("name"))
                        .createCriteria("userAssociations")
                            .add(Restrictions.eq("userClass", notInClass));
                        
                qu.add(Restrictions.not(Restrictions.in("name", subQu.getExecutableCriteria(this.db).list())));
            }
        }
        
        qu.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
        qu.addOrder(Order.asc("lastName"));
        qu.addOrder(Order.asc("name"));
        
        for (User user : (List<User>)qu.list())
        {
            JSONObject uo = new JSONObject();
            uo.put("name", user.getNamespace() + "-_-" + user.getName());
            
            if (user.getFirstName() == null || user.getLastName() == null)
            {
                uo.put("display", user.getName());
            }
            else
            {
                uo.put("display", user.getLastName() + ", " + user.getFirstName());
            }
            
            arr.put(uo);
        }
        
        
        return arr;
    }
    
    /**
     * Deletes users from a user class.n
     * 
     * @param request
     * @return response
     * @throws JSONException
     */
    public JSONObject deleteUsersInClass(HttpServletRequest request) throws JSONException
    {
        JSONObject obj = new JSONObject();
        obj.put("success", false);
        
        UserClass uc = new UserClassDao(this.db).findByName(request.getParameter("name"));
        if (uc == null)
        {
            this.logger.warn("Unable to delete users in user class because the class with name '" + 
                    request.getParameter("name") + "' was not found.");
            return obj;
        }
        
        UserDao dao = new UserDao(this.db);
        UserAssociationDao uaDao = new UserAssociationDao(this.db);
        
        for (String userName : request.getParameter("users").split(","))
        {
            String ns = userName.substring(0, userName.indexOf("-_-"));
            String name = userName.substring(userName.indexOf("-_-") + 3);
            
            User user = dao.findByName(ns, name);
            if (user == null)
            {
                this.logger.warn("Unable to delete user assocition of user with name '" + ns + ':' + name + 
                        "' because the user was not found");
                continue;
            }
            
            this.logger.info("Deleted association with class '" + uc.getName() + "' and user '" + user.qName() + "'.");
            uaDao.delete(user, uc);
        }
        
        return obj;
    }

    @Override
    protected String getPageType()
    {
        return "Users";
    }

    public static HostedPage getHostedPage()
    {
        return new HostedPage("Users", UsersPage.class, "perm-users", 
                "Allows users to be created, read, updated and deleted.", false, false);
    }
}
