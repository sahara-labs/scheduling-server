/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2012, University of Technology, Sydney
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
 * @date 29th October 2012
 */
package au.edu.uts.eng.remotelabs.schedserver.dataaccess.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Entity that stores session files. 
 */
@Entity
@Table(name = "session_file")
public class SessionFile implements Serializable
{
    /** File transfer where the file is appended to a SOAP request. */
    public static final String ATTACHMENT_TRANSFER = "ATTACHMENT";
    
    /** File transfer where the file is transferred using a shared file system. */
    public static final String FILESYSTEM_TRANSFER = "FILESYSTEM";
    
    /** File transfer where the file is transferred using a WebDAV server. */
    public static final String WEBDAV_TRANSFER = "WEBDAV";
    
    /** Serializable class. */
    private static final long serialVersionUID = 5304061154397701117L;
    
    /** Record primary key. */
    private Long id;
    
    /** The session which generated the file. */
    private Session session;
    
    /** The name of the file. */
    private String name;
    
    /** The path to the file. */
    private String path;
    
    /** Creation timestamp. */
    private Date timestamp;
    
    /** The transfer method used to get the file to the Scheduling Server. */
    private String transferMethod;
    
    /** Whether the file has been transferred. */
    private boolean isTransferred;

    public SessionFile()
    {
        /* Bean style constructor. */
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    public Long getId()
    {
        return this.id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "session_id", nullable = false)
    public Session getSession()
    {
        return this.session;
    }

    public void setSession(Session session)
    {
        this.session = session;
    }

    @Column(name = "name", nullable = false)
    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    @Column(name = "path", nullable = false)
    public String getPath()
    {
        return this.path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    @Column(name = "timestamp", nullable = false)
    public Date getTimestamp()
    {
        return this.timestamp;
    }

    public void setTimestamp(Date timestamp)
    {
        this.timestamp = timestamp;
    }

    @Column(name = "transfer", nullable = false, length = 10)
    public String getTransferMethod()
    {
        return this.transferMethod;
    }

    public boolean isTransferred()
    {
        return this.isTransferred;
    }

    @Column(name = "is_transferred", nullable = false)
    public void setTransferMethod(String transferMethod)
    {
        this.transferMethod = transferMethod;
    }

    public void setTransferred(boolean isTransferred)
    {
        this.isTransferred = isTransferred;
    }
}
