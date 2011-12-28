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

import hudson.Util;
import hudson.model.AbstractProject;

import java.util.List;
import org.apache.commons.lang.StringUtils;

/**
 * Represents an xml element which exists in the linked AbstractProjects.
 * 
 * @author c031
 *
 */
public class XmlBlock {

    /**
     * xml block from config.xml
     */
    private String xmlblock;
    /**
     * list of related projects.
     */
    @SuppressWarnings("rawtypes")
    private List< AbstractProject> projects;

    /**
     * @return the xmlblock
     */
    public String getXmlblock() {
        return xmlblock;
    }

    /**
     * @param xmlblock the xmlblock to set
     */
    public void setXmlblock(String xmlblock) {
        this.xmlblock = xmlblock;
    }

    /**
     * @return the projects
     */
    @SuppressWarnings("rawtypes")
    public List< AbstractProject> getProjects() {
        return projects;
    }

    /**
     * @param projects the projects to set
     */
    @SuppressWarnings("rawtypes")
    public void setProjects(List< AbstractProject> projects) {
        this.projects = projects;
    }
    
    /**
     * Returns the HTML escaped xml block.
     * @return the HTML escaped xml block
     */
    public String getEscapedXmlblock() {
        return Util.escape(this.xmlblock);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        if (StringUtils.isNotBlank(xmlblock)) {
            return this.xmlblock.hashCode();
        }
        return super.hashCode();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {

        if (obj instanceof XmlBlock && StringUtils.isNotBlank(xmlblock) && StringUtils.isNotBlank(((XmlBlock) obj).xmlblock)) {
            return this.xmlblock.equals(((XmlBlock) obj).xmlblock);
        }

        return super.equals(obj);
    }
}
