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
 * @date 16th February 2010
 */
package au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.tests;

import java.io.ByteArrayInputStream;

import javax.xml.stream.XMLStreamReader;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.util.StAXUtils;
import org.apache.axis2.databinding.types.URI;
import org.junit.Test;

import junit.framework.TestCase;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.RegisterRig;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.RegisterRigType;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.inf.types.StatusType;

/**
 * Tests the {@link RegisterRigType} class.
 */
public class RegisterRigTypeTester extends TestCase
{
    @Test
    public void testSerialise() throws Exception
    {
        RegisterRigType reg = new RegisterRigType();
        reg.setName("lb1");
        reg.setType("loadedbeam");
        reg.setCapabilities("foo,bar,baz");

        URI uri = new URI("http://138.25.47.151:8080/SchedulingServer-LocalRigProvider/services/LocalRigProvider");
        assertNotNull(uri);
        reg.setContactUrl(uri);
        
        StatusType stat = new StatusType();
        stat.setIsOnline(true);
        stat.setOfflineReason("Tomorrows problem.");
        reg.setStatus(stat);
        
        OMElement ele = reg.getOMElement(RegisterRig.MY_QNAME, OMAbstractFactory.getOMFactory());
        assertNotNull(ele);
        
        String xml = ele.toStringWithConsume();
        assertNotNull(xml);

        assertTrue(xml.contains("<name>lb1</name>"));
        assertTrue(xml.contains("<type>loadedbeam</type>"));
        assertTrue(xml.contains("<capabilities>foo,bar,baz</capabilities>"));
        assertTrue(xml.contains("<status>"));
        assertTrue(xml.contains("<isOnline>true</isOnline>"));
        assertTrue(xml.contains("<offlineReason>Tomorrows problem.</offlineReason>"));
        assertTrue(xml.contains("<contactUrl>http://138.25.47.151:8080/" +
        		"SchedulingServer-LocalRigProvider/services/LocalRigProvider</contactUrl>"));
    }
    
    @Test
    public void testParse() throws Exception
    {
        String xml = "<ns1:registerRig xmlns:ns1=\"http://remotelabs.eng.uts.edu.au/schedserver/localrigprovider\" " +
        		"xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:type=\"ns1:RegisterRigType\"><name>lb1</name>" +
        		"<type>loadedbeam</type>" +
        		"<capabilities>foo,bar,baz</capabilities>" +
        		"<contactUrl>http://138.25.47.151:8080/SchedulingServer-LocalRigProvider/services/LocalRigProvider</contactUrl>" +
        		"<status><isOnline>true</isOnline>" +
        		"<offlineReason>Tomorrows problem.</offlineReason></status>" +
        		"</ns1:registerRig>\n";
        
        XMLStreamReader reader = StAXUtils.createXMLStreamReader(new ByteArrayInputStream(xml.getBytes()));
        RegisterRigType reg = RegisterRigType.Factory.parse(reader);
        assertNotNull(reg);
        
        assertEquals("lb1", reg.getName());
        assertEquals("loadedbeam", reg.getType());
        assertEquals("foo,bar,baz", reg.getCapabilities());
        
        assertEquals("http://138.25.47.151:8080/SchedulingServer-LocalRigProvider/services/LocalRigProvider", 
                reg.getContactUrl().toString());
        
        StatusType st = reg.getStatus();
        assertNotNull(st);
        
        assertTrue(st.getIsOnline());
        assertEquals("Tomorrows problem.", st.getOfflineReason());
    }
}
