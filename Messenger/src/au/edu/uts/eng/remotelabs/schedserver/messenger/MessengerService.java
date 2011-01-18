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
 * @date 17th January 2011
 */
package au.edu.uts.eng.remotelabs.schedserver.messenger;

import java.net.URL;
import java.util.List;
import java.util.Map;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;

/**
 * The service to send messages.
 */
public interface MessengerService
{
    /**
     * Sends the message.
     * 
     * @param message message to send
     */
    public void sendMessage(Message message);
    
    /**
     * Sends the message to the configured administration users. The message
     * recipients are ignored using this method.
     * 
     * @param message message to send
     */
    public void sendAdminMessage(Message message);
    
    /**
     * Sends a templated message. 
     * <br />
     * If the template contains a macro with a value not supplied in the macro 
     * list, the macro value will be loaded from configuration.
     * 
     * @param users list of recipients
     * @param templateId template identifier
     * @param macros marco list of inject into template
     */
    public void sendTemplatedMessage(User users, String templateId, Map<String, String> macros);
    
    /**
     * Sends a templated message. 
     * <br />
     * If the template contains a macro with a value not supplied in the macro 
     * list, the macro value will be loaded from configuration.
     * 
     * @param users list of recipients
     * @param templateId template identifier
     * @param macros marco list of inject into template
     */
    public void sendTemplatedMessage(List<User> users, String templateId, Map<String, String> macros);
    
    /**
     * Sends a templated message to the configured administration users. 
     * <br />
     * If the template contains a macro with a value not supplied in the macro 
     * list, the macro value will be loaded from configuration.
     * 
     * @param templateId template identifier
     * @param macros marco list of inject into template
     */
    public void sendAdminTemplatedMessage(String templateId, Map<String, String> macros);
    
    /**
     * Sends a templated message. 
     * <br />
     * If the template contains a macro with a value not supplied in the macro 
     * list, the macro value will be loaded from configuration.
     * 
     * @param users recipient
     * @param template URI to template
     * @param macros marco list of inject into template
     */
    public void sendTemplatedMessage(User user, URL template, Map<String, String> macros);
    
    /**
     * Sends a templated message. 
     * <br />
     * If the template contains a macro with a value not supplied in the macro 
     * list, the macro value will be loaded from configuration.
     * 
     * @param users list of recipients
     * @param template URI to template
     * @param macros marco list of inject into template
     */
    public void sendTemplatedMessage(List<User> users, URL template, Map<String, String> macros);
    
    /**
     * Sends a templated message to the configured administration users. 
     * <br />
     * If the template contains a macro with a value not supplied in the macro 
     * list, the macro value will be loaded from configuration.
     * 
     * @param template URI to template
     * @param macros marco list of inject into template
     */
    public void sendAdminTemplatedMessage(URL template, Map<String, String> macros);
}
