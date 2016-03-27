/**
 * SAHARA Scheduling Server
 *
 * Schedules and assigns local laboratory rigs.
 *
 * @license See LICENSE in the top level directory for complete license terms.
 *
 * Copyright (c) 2010, University of Technology, Sydney
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
 * @date 4th January 2010
 */
package io.rln.ss.framework;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.Constants;

/**
 * Properties for the OSGi framework. 
 */
public class FrameworkProperties
{
    /** List of packages that are exported from the framework. These include:
     * <ul>
     *  <li>OSGi framework packages (org.osgi.*)</li>
     *  <li>Java SE 8 packages that are not by default visible.
     * </ul> */
    public static final String SYS_PACKAGES = "org.osgi.framework; version=1.5.0," +
    		"org.osgi.framework.launch; version=1.0.0," +
    		"org.osgi.framework.hooks.service; version=1.0.0," +
    		"org.osgi.service.packageadmin; version=1.2.0," +
    		"org.osgi.service.startlevel; version=1.1.0," +
    		"org.osgi.service.url; version=1.0.0," +
    		"org.osgi.util.tracker; version=1.4.0," +
    		"javax.accessibility;" +
    		"javax.activation;" +
    		"javax.activity;" +
    		"javax.annotation;" +
    		"javax.annotation.processing;" +
    		"javax.crypto;" +
    		"javax.crypto.interfaces;" +
    		"javax.crypto.spec;" +
    		"javax.imageio;" +
    		"javax.imageio.event;" +
    		"javax.imageio.metadata;" +
    		"javax.imageio.plugins.bmp;" +
    		"javax.imageio.plugins.jpeg;" +
    		"javax.imageio.spi;" +
    		"javax.imageio.stream;" +
    		"javax.jws;" +
    		"javax.jws.soap;" +
    		"javax.lang.model;" +
    		"javax.lang.model.element;" +
    		"javax.lang.model.type;" +
    		"javax.lang.model.util;" +
    		"javax.management;" +
    		"javax.management.loading;" +
    		"javax.management.modelmbean;" +
    		"javax.management.monitor;" +
    		"javax.management.openmbean;" +
    		"javax.management.relation;" +
    		"javax.management.remote;" +
    		"javax.management.remote.rmi;" +
    		"javax.management.timer;" +
    		"javax.naming;" +
    		"javax.naming.directory;" +
    		"javax.naming.event;" +
    		"javax.naming.ldap;" +
    		"javax.naming.spi;" +
    		"javax.net;" +
    		"javax.net.ssl;" +
    		"javax.print;" +
    		"javax.print.attribute;" +
    		"javax.print.attribute.standard;" +
    		"javax.print.event;" +
    		"javax.rmi;" +
    		"javax.rmi.CORBA;" +
    		"javax.rmi.ssl;" +
    		"javax.script;" +
    		"javax.security;" + 
    		"javax.security.auth;" +
    		"javax.security.auth.callback;" +
    		"javax.security.auth.kerberos;" +
    		"javax.security.auth.login;" +
    		"javax.security.auth.spi;" +
    		"javax.security.auth.x500;" +
    		"javax.security.cert;" +
    		"javax.security.sasl;" +
    		"javax.sound.midi;" +
    		"javax.sound.midi.spi;" +
    		"javax.sound.sampled;" +
    		"javax.sound.sampled.spi;" +
    		"javax.sql;" +
    		"javax.sql.rowset;" +
    		"javax.sql.rowset.serial;" +
    		"javax.sql.rowset.spi;" +
    		"javax.swing;" +
    		"javax.swing.border;" +
    		"javax.swing.colorchooser;" +
    		"javax.swing.event;" +
    		"javax.swing.filechooser;" +
    		"javax.swing.plaf;" +
    		"javax.swing.plaf.basic;" +
    		"javax.swing.plaf.metal;" +
    		"javax.swing.plaf.multi;" +
    		"javax.swing.plaf.synth;" +
    		"javax.swing.table;" +
    		"javax.swing.text;" +
    		"javax.swing.text.html;" +
    		"javax.swing.text.html.parser;" +
    		"javax.swing.text.rtf;" +
    		"javax.swing.tree;" +
    		"javax.swing.undo;" +
    		"javax.tools;" +
    		"javax.transaction;" +
    		"javax.transaction.xa;" +
    		"javax.xml;" +
    		"javax.xml.bind;" +
    		"javax.xml.bind.annotation;" +
    		"javax.xml.bind.annotation.adapters;" +
    		"javax.xml.bind.attachment;" +
    		"javax.xml.bind.helpers;" +
    		"javax.xml.bind.util;" +
    		"javax.xml.crypto;" +
    		"javax.xml.crypto.dom;" +
    		"javax.xml.crypto.dsig;" +
    		"javax.xml.crypto.dsig.dom;" +
    		"javax.xml.crypto.dsig.keyinfo;" +
    		"javax.xml.crypto.dsig.spec;" +
    		"javax.xml.datatype;" +
    		"javax.xml.namespace;" +
    		"javax.xml.parsers;" +
    		"javax.xml.soap;" +
    		"javax.xml.stream;" +
    		"javax.xml.stream.events;" +
    		"javax.xml.stream.util;" +
    		"javax.xml.transform;" +
    		"javax.xml.transform.dom;" +
    		"javax.xml.transform.sax;" +
    		"javax.xml.transform.stax;" +
    		"javax.xml.transform.stream;" +
    		"javax.xml.validation;" +
    		"javax.xml.ws;" +
    		"javax.xml.ws.handler;" +
    		"javax.xml.ws.handler.soap;" +
    		"javax.xml.ws.http;" +
    		"javax.xml.ws.soap;" +
    		"javax.xml.ws.spi;" +
    		"javax.xml.xpath;" +
    		"org.ietf.jgss;" +
    		"org.omg.CORBA;" +
    		"org.omg.CORBA_2_3;" +
    		"org.omg.CORBA_2_3.portable;" +
    		"org.omg.CORBA.DynAnyPackage;" +
    		"org.omg.CORBA.ORBPackage;" +
    		"org.omg.CORBA.portable;" +
    		"org.omg.CORBA.TypeCodePackage;" +
    		"org.omg.CosNaming;" +
    		"org.omg.CosNaming.NamingContextExtPackage;" +
    		"org.omg.CosNaming.NamingContextPackage;" +
    		"org.omg.Dynamic;" +
    		"org.omg.DynamicAny;" +
    		"org.omg.DynamicAny.DynAnyFactoryPackage;" +
    		"org.omg.DynamicAny.DynAnyPackage;" +
    		"org.omg.IOP;" +
    		"org.omg.IOP.CodecFactoryPackage;" +
    		"org.omg.IOP.CodecPackage;" +
    		"org.omg.Messaging;" +
    		"org.omg.PortableInterceptor;" +
    		"org.omg.PortableInterceptor.ORBInitInfoPackage;" +
    		"org.omg.PortableServer;" +
    		"org.omg.PortableServer.CurrentPackage;" +
    		"org.omg.PortableServer.POAManagerPackage;" +
    		"org.omg.PortableServer.POAPackage;" +
    		"org.omg.PortableServer.portable;" +
    		"org.omg.PortableServer.ServantLocatorPackage;" +
    		"org.omg.SendingContext;" +
    		"org.omg.stub.java.rmi;" +
    		"org.omg.stub.javax.management.remote.rmi;" +
    		"org.w3c.dom;" +
    		"org.w3c.dom.bootstrap;" +
    		"org.w3c.dom.css;" +
    		"org.w3c.dom.events;" +
    		"org.w3c.dom.html;" +
    		"org.w3c.dom.ls;" +
    		"org.w3c.dom.ranges;" +
    		"org.w3c.dom.stylesheets;" +
    		"org.w3c.dom.traversal;" +
    		"org.w3c.dom.views;" +
    		"org.w3c.dom.xpath;" +
    		"org.xml.sax;" +
    		"org.xml.sax.ext;" +
    		"org.xml.sax.helpers;" +
    		"version=1.8.0";
   
    /** Relative directory to store the framework cache. */
    public static final String CACHE_DIR = "cache";
    
    /** Bundles directory. */
    public static final String BUNDLE_DIR = "bundles";
    
    /** Framework properties map. */
    private final Map<String, String> props;
    
    public FrameworkProperties()
    {
        this.props = new HashMap<String, String>();
        
        /* Set the default properties. */
        this.props.put(Constants.FRAMEWORK_SYSTEMPACKAGES, FrameworkProperties.SYS_PACKAGES);
        try
        {
            this.props.put(Constants.FRAMEWORK_STORAGE, new File(FrameworkProperties.CACHE_DIR).getCanonicalPath());
        }
        catch (IOException e)
        {
            this.props.put(Constants.FRAMEWORK_STORAGE, System.getProperty("user.dir" + "/" + 
                    FrameworkProperties.CACHE_DIR));
        }
        
        this.props.put(Constants.FRAMEWORK_STORAGE_CLEAN, "onFirstInit");
    }
    
    /**
     * Gets the frameworks properties.
     * 
     * @return framework property map
     */
    public Map<String, String> getProperties()
    {
        return this.props;
    }
}
