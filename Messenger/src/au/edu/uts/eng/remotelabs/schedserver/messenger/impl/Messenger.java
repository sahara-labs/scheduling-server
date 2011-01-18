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
 * @date 4th January 2011
 */
package au.edu.uts.eng.remotelabs.schedserver.messenger.impl;

import java.net.URL;
import java.util.List;
import java.util.Map;

import au.edu.uts.eng.remotelabs.schedserver.config.Config;
import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;
import au.edu.uts.eng.remotelabs.schedserver.logger.Logger;
import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.messenger.Message;
import au.edu.uts.eng.remotelabs.schedserver.messenger.MessengerService;

/**
 * Messenger implementation.
 */
public class Messenger implements MessengerService
{
    /** SMTP sender. */
    private SMTPSender sender;
    
    /** Logger. */
    private Logger logger;
    
    public Messenger()
    {
        this.logger = LoggerActivator.getLogger();
        this.sender = new SMTPSender();
    }
    
    /**
     * Initalises the messenger.
     * 
     * @param config configuration class
     */
    public void init(Config config)
    {
        this.logger.debug("Initalising messenger which currently only uses SMTP send.");
        this.sender.init(config);
    }

    @Override
    public void sendMessage(Message message)
    {
        this.sender.send(message);
    }

    @Override
    public void sendAdminMessage(Message message)
    {
        this.sender.sendAdmin(message);
    }
    
    @Override
    public void sendTemplatedMessage(User user, String templateId, Map<String, String> macros)
    {
        URL template = this.getClass().getResource("/META-INF/templates/" + templateId);
        if (template == null)
        {
            this.logger.error("Unable to send email with template '" + templateId + "' because the " +
                    "template was not found.");
        }
        else
        {
            this.sendTemplatedMessage(user, template, macros);
        }
    }

    @Override
    public void sendTemplatedMessage(List<User> users, String templateId, Map<String, String> macros)
    {
        URL template = this.getClass().getResource("/META-INF/templates/" + templateId);
        if (template == null)
        {
            this.logger.error("Unable to send email with template '" + templateId + "' because the " +
            		"template was not found.");
        }
        else
        {
            this.sendTemplatedMessage(users, template, macros);
        }
    }

    @Override
    public void sendAdminTemplatedMessage(String templateId, Map<String, String> macros)
    {
        URL template = this.getClass().getResource("/META-INF/templates/" + templateId);
        if (template == null)
        {
            this.logger.error("Unable to send email with template '" + templateId + "' because the " +
            		"template was not found.");
        }
        else
        {
            this.sendAdminTemplatedMessage(template, macros);
        }
    }

    @Override
    public void sendTemplatedMessage(User user, URL template, Map<String, String> macros)
    {
        try
        {
            Message message = new Templator(template, macros).generate();
            message.addRecipent(user);
            this.sender.send(message);
        }
        catch (Exception e)
        {
            this.logger.error("Failed to send email because of exception: " + e.getClass().getSimpleName() + 
                    ", message: " + e.getMessage() + ".");
        }
    }
    
    @Override
    public void sendTemplatedMessage(List<User> users, URL template, Map<String, String> macros)
    {
        try
        {
            Message message = new Templator(template, macros).generate();
            message.setRecipient(users);
            this.sender.send(message);
        }
        catch (Exception e)
        {
            this.logger.error("Failed to send email because of exception: " + e.getClass().getSimpleName() + 
                    ", message: " + e.getMessage() + ".");
        } 
    }

    @Override
    public void sendAdminTemplatedMessage(URL template, Map<String, String> macros)
    {
        try
        {
            Message message = new Templator(template, macros).generate();
            this.sender.sendAdmin(message);
        }
        catch (Exception e)
        {
            this.logger.error("Failed to send email because of exception: " + e.getClass().getSimpleName() + 
                    ", message: " + e.getMessage() + ".");
        } 
    }

    /**
     * Cleans ups the messenger.
     */
    public void cleanUp()
    {
        /* Does nothing currently. */
    }

   
}
