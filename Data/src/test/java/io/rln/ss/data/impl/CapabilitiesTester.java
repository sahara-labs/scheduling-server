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
package io.rln.ss.data.impl;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;

import io.rln.ss.data.impl.Capabilities;

/**
 * Tests the {@link Capabilities} class.
 */
public class CapabilitiesTester extends TestCase
{
    /**
     * Test method for {@link io.rln.ss.data.impl.Capabilities#asCapabilitiesString()}.
     */
    @Test
    public void testAsCapabilitiesString()
    {
        String capStr = "a , b ,f, F, E,d, c,e,F,G";
        Capabilities caps = new Capabilities(capStr);
        assertEquals("a,b,c,d,e,f,g", caps.asCapabilitiesString());
    }

    /**
     * Test method for {@link io.rln.ss.data.impl.Capabilities#asCapabilitiesList()}.
     */
    @Test
    public void testAsCapabilitiesList()
    {
        String capStr = "de, ef, fe, cd, ba, ba, fe, ";
        Capabilities caps = new Capabilities(capStr);
        List<String> capList = caps.asCapabilitiesList();
        assertEquals(5, capList.size());
        assertEquals("ba", capList.get(0));
        assertEquals("cd", capList.get(1));
        assertEquals("de", capList.get(2));
        assertEquals("ef", capList.get(3));
        assertEquals("fe", capList.get(4));
    }

    /**
     * Test method for {@link io.rln.ss.data.impl.Capabilities#asCapabilitiesArray()}.
     */
    @Test
    public void testAsCapabilitiesArray()
    {
        String capStr = "de, ef, fe, cd, ba, ba, fe, ";
        Capabilities caps = new Capabilities(capStr);
        String[] capsArr = caps.asCapabilitiesArray();
        assertEquals(5, capsArr.length);
        assertEquals("ba", capsArr[0]);
        assertEquals("cd", capsArr[1]);
        assertEquals("de", capsArr[2]);
        assertEquals("ef", capsArr[3]);
        assertEquals("fe", capsArr[4]);
    }

}
