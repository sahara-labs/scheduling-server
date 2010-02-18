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
package au.edu.uts.eng.remotelabs.schedserver.rigprovider.identok.impl.tests;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import au.edu.uts.eng.remotelabs.schedserver.logger.LoggerActivator;
import au.edu.uts.eng.remotelabs.schedserver.logger.impl.SystemErrLogger;
import au.edu.uts.eng.remotelabs.schedserver.rigprovider.identok.impl.IdentityTokenRegister;

/**
 * Tests the {@link IdentityTokenRegister} class.
 */
public class IdentityTokenRegisterTester extends TestCase
{
    /** Object of class under test. */
    private IdentityTokenRegister register;

    @Override
    @Before
    public void setUp() throws Exception
    {
        /* Set up the logger. */
        Field f = LoggerActivator.class.getDeclaredField("logger");
        f.setAccessible(true);
        f.set(null, new SystemErrLogger());
  
        this.register = IdentityTokenRegister.getInstance();
        
        f = IdentityTokenRegister.class.getDeclaredField("registry");
        f.setAccessible(true);
        f.set(this.register, Collections.synchronizedMap(new HashMap<String, String>())); 
    }
    
    @Test
    public void testGenerateIdentityToken()
    {
        String tok, prevTok = this.register.generateIdentityToken("rig");
        assertNotNull(prevTok);
        assertEquals(20, prevTok.length());
        
        List<Character> charTable = new ArrayList<Character>(94);
        for (int i = 33; i <= 126; i++)
        {
            charTable.add((char)i);
        }
        
        for (int i = 0; i < 10000; i++)
        {
            tok = this.register.generateIdentityToken("rig");
            assertNotNull(tok);
            assertFalse(tok.equals(prevTok));
            
            for (char c : tok.toCharArray())
            {
                /* Make sure the characters are all visible. */
                assertTrue(c >= 33);  // '!' - minimum visible glyph in Ascii
                assertTrue(c <= 126); // '~' - maximum visible glyph in Ascii
                assertFalse(c == '\\'); // '\' character in Ascii 
                assertFalse(c == '\'');
                charTable.remove(Character.valueOf(c));
            }
        }

        assertEquals(2, charTable.size());
        assertEquals('\'', charTable.get(0).charValue());
        assertEquals('\\', charTable.get(1).charValue());
    }
    
    @Test
    public void testGetIdentityToken()
    {
        String tok[] = new String[100];
        
        for (int i = 0; i < 100; i++)
        {
            tok[i] = this.register.generateIdentityToken("tok" + i);
        }
        
        for (int i = 0; i < IdentityTokenRegister.AVERAGE_IDENTITY_TOKEN_LIFE * 10; i++)
        {
            for (int j = 0; j < 100; j++)
            {
                assertEquals(tok[j], this.register.getIdentityToken("tok" + j));
            }
        }
    }
    
    @Test
    public void testRemoveIdentityToken()
    {
        assertNotNull(this.register.generateIdentityToken("tok"));
        assertNotNull(this.register.getIdentityToken("tok"));
        this.register.removeIdentityToken("tok");
        assertNull(this.register.getIdentityToken("tok"));
    }
    
    @Test
    public void testGetOrGenerateIdentityToken()
    {
        String tok = this.register.generateIdentityToken("tok");
        int diff = 0;
        
        for (int i = 0; i < IdentityTokenRegister.AVERAGE_IDENTITY_TOKEN_LIFE * 100; i++)
        {
            if (!tok.equals(this.register.getOrGenerateIdentityToken("tok")))
            {
                tok = this.register.getIdentityToken("tok");
                diff++;
            }
        }
       
        /* The average is within 15 % of the expected value. */
        assertTrue(85 < diff && diff < 115); 
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testExpunge() throws Exception
    {
        for (int i = 0; i < 100; i++)
        {
            assertNotNull(this.register.generateIdentityToken("t" + i));
        }
        
        this.register.expunge();
        
        Field f = IdentityTokenRegister.class.getDeclaredField("registry");
        f.setAccessible(true);
        assertEquals(0, ((Map<String, String>)f.get(this.register)).size());
    }
}
