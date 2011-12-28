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

import static org.junit.Assert.assertEquals;

import hudson.XmlFile;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import hudson.model.AbstractProject;

import java.io.File;

import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * @author Frederik Fromm
 *
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(AbstractProject.class)
public class XPathConfigMockTest {
    
    private AbstractProject project1;
    private AbstractProject project2;
    private AbstractProject project3;
    private AbstractProject project4;
    private AbstractProject project5;

    @Before
    public void setup() throws Throwable {
        project1 = PowerMockito.mock(AbstractProject.class);
        PowerMockito.when(project1.getConfigFile()).thenReturn(new XmlFile(new File("src/test/resources/xml/1.xml")));
        PowerMockito.when(project1.getName()).thenReturn("job1");
        
        project2 = PowerMockito.mock(AbstractProject.class);
        PowerMockito.when(project2.getConfigFile()).thenReturn(new XmlFile(new File("src/test/resources/xml/2.xml")));
        PowerMockito.when(project2.getName()).thenReturn("job2");
        
        project3 = PowerMockito.mock(AbstractProject.class);
        PowerMockito.when(project3.getConfigFile()).thenReturn(new XmlFile(new File("src/test/resources/xml/3.xml")));
        PowerMockito.when(project3.getName()).thenReturn("job3");
        
        project4 = PowerMockito.mock(AbstractProject.class);
        PowerMockito.when(project4.getConfigFile()).thenReturn(new XmlFile(new File("src/test/resources/xml/4.xml")));
        PowerMockito.when(project4.getName()).thenReturn("job4");
        
        project5 = PowerMockito.mock(AbstractProject.class);
        PowerMockito.when(project5.getConfigFile()).thenReturn(new XmlFile(new File("src/test/resources/xml/5.xml")));
        PowerMockito.when(project5.getName()).thenReturn("job5");
    }
    
    @Test
    public void testProject() {
        File file = this.project1.getConfigFile().getFile();
        
        assertNotNull(file);
        assertTrue(file.canRead());
        
        XPathConfig config = new XPathConfig(null, null, null, null);

        assertEquals(0, config.getDistinctXmlBlocks().size());

        List< XmlBlock > distinctXmlBlocks = config.getDistinctXmlBlocks();

        assertEquals(0, distinctXmlBlocks.size());

        List< AbstractProject > projectsList = new ArrayList<AbstractProject>();
        
        projectsList.add(project1);
        projectsList.add(project2);
        projectsList.add(project3);
        projectsList.add(project4);
        projectsList.add(project5);

        ProjectCollector.setProjectsListForTest(projectsList);
        
        config.setXpath("/root/sub");

        distinctXmlBlocks = config.getDistinctXmlBlocks();
        
        assertNotNull(distinctXmlBlocks);
        
        assertEquals(4, distinctXmlBlocks.size());
        
        int i = 0;
        Integer [] expectedSizes = new Integer [] {2, 1, 1, 1};
        for(XmlBlock xmlBlock : distinctXmlBlocks) {
            assertEquals(expectedSizes[i].intValue(), xmlBlock.getProjects().size());
            i++;
        }
        String notAssignedBlock = distinctXmlBlocks.get(3).getXmlblock();
        
        assertEquals(XPathConfig.NOT_ASSIGNED, notAssignedBlock);
        
    }
}
