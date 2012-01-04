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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.UserClassKey;
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
            this.logger.warn("Unable to provide permission key list because the user class name was not provided.");
            return arr;
        }
        
        
        for (UserClassKey key : (List<UserClassKey>) this.db.createCriteria(UserClassKey.class)
                .addOrder(Order.asc("active"))
                .addOrder(Order.desc("id"))
                .createCriteria("userClass")
                    .add(Restrictions.eq("name", className)).list())
        {
            JSONObject keyObj = new JSONObject();
            keyObj.put("name", key.getRedeemKey());
            keyObj.put("active", key.isActive());
            keyObj.put("remaining", key.getRemaining());
            arr.put(keyObj);
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
        return new HostedPage("Keys", KeysPage.class, "perm", 
                "Allows permissions to be created, read, updated and deleted.", false, false);
    }
}
