/*
 * The MIT License
 *
 * Copyright (c) 2004-2011, Sun Microsystems, Inc., Kohsuke Kawaguchi, Frederik Fromm
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jenkinsci.plugins.viewer;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author c031
 */
public class XmlBlockTest {
    
    @Test
    public void testEquals() {
        XmlBlock xbA = new XmlBlock();
        xbA.setXmlblock(null);
        
        XmlBlock xbB = new XmlBlock();
        xbB.setXmlblock(null);
        
        XmlBlock xbC = new XmlBlock();
        xbC.setXmlblock(null);
        
        List< XmlBlock> xmlBlocks = new ArrayList<XmlBlock>();
        
        xmlBlocks.add(xbA);
        xmlBlocks.add(xbB);
        
        assertFalse(xbA.equals(xbB));
        assertTrue(xmlBlocks.contains(xbA));
        assertFalse(xmlBlocks.contains(xbC));
        
        xbA.setXmlblock("<A />");
        xbB.setXmlblock("<A />");
        xbC.setXmlblock("<A />");
        
        assertTrue(xbA.equals(xbB));
        assertTrue(xmlBlocks.contains(xbA));
        assertTrue(xmlBlocks.contains(xbC));
        
        xbA.setXmlblock("<A />");
        xbB.setXmlblock("<B />");
        xbC.setXmlblock("<C />");
        
        assertFalse(xbA.equals(xbB));
        assertFalse(xmlBlocks.contains(xbC));
        assertEquals(0, xmlBlocks.indexOf(xbA));
        assertEquals(1, xmlBlocks.indexOf(xbB));
        assertEquals(-1, xmlBlocks.indexOf(xbC));
    }
    
    @Test
    public void testGetEscapedXmlBlock() {
        XmlBlock xb = new XmlBlock();
        xb.setXmlblock("<A />");        
        assertEquals("&lt;A />", xb.getEscapedXmlblock());
    }
}
