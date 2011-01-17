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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities.User;

/**
 * Message to send to a recipient.
 */
public class Message
{
    /** The message recipient. */
    private List<User> recipients;
    
    /** Message subject. */
    private String subject;
    
    /** Message body. */
    private String body;
    
    /** Message attachments. */
    private List<File> attachments;
    
    public Message()
    {
        this.recipients = new ArrayList<User>();
        this.attachments = new ArrayList<File>();
    }

    public List<User> getRecipients()
    {
        return this.recipients;
    }
    
    public void addRecipent(User user)
    {
        this.recipients.add(user);
    }

    public void setRecipient(List<User> recipient)
    {
        this.recipients = recipient;
    }

    public String getSubject()
    {
        return this.subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    public String getBody()
    {
        return this.body;
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    public List<File> getAttachments()
    {
        return this.attachments;
    }
    
    public void addAttachment(File attachment)
    {
        this.attachments.add(attachment);
    }

    public void setAttachments(List<File> attachments)
    {
        this.attachments = attachments;
    }
}
