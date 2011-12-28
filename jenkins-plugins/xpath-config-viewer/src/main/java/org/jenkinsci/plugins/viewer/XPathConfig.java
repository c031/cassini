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

import hudson.model.AbstractProject;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jenkinsci.lib.configprovider.model.Config;
import org.jfree.util.Log;
import org.kohsuke.stapler.DataBoundConstructor;

/**
 * Represents a config viewer item.
 * 
 * @author c031
 * 
 */
public class XPathConfig extends Config {
    public static final String NOT_ASSIGNED = "<!-- not assigned -->";

    /**
     * serial.
     */
    private static final long serialVersionUID = -6773980474331173914L;

    /**
     * the xpath expression to select a config.xml xml block.
     */
    private String xpath;

    /**
     * the comment field is used as job name filter based on regexp.
     */
    private Pattern jobNameFilterPattern;


    /**
     * Constructor
     * 
     * @param id
     *            Config ID, automatcally created.
     * @param name
     *            Config name, what config part to show.
     * @param comment
     *            comment, what the config means.
     * @param content
     *            holds the xpath expression.
     */
    @SuppressWarnings("rawtypes")
    @DataBoundConstructor
    public XPathConfig(String id, String name, String comment, String content) {
        super(id, name, comment, content);
        
        this.xpath = content;  
        this.jobNameFilterPattern = StringUtils.isBlank(comment) ? null : Pattern.compile(comment);
    }

    /**
     * Returns the xpath expression.
     * 
     * @return the xpath expression
     */
    public String getXpath() {
        return this.xpath;
    }

    /**
     * @param xpath
     *            the xpath to set
     */
    public void setXpath(String xpath) {
        this.xpath = xpath;
    }

    /**
     * Returns the map of different xml blocks with corresponding config.xml files selected by the given xpath
     * expression.
     * 
     * @return the map of different xml blocks
     */
    @SuppressWarnings("rawtypes")
    public List< XmlBlock > getDistinctXmlBlocks() {

        if (StringUtils.isBlank(this.getXpath()) || ProjectCollector.getProjectsList() == null) {
            return Collections.emptyList();
        }

        List< XmlBlock > xmlBlocks = new ArrayList< XmlBlock >();
        
        List<AbstractProject> notAssignedList = new ArrayList<AbstractProject>();
        notAssignedList.addAll(ProjectCollector.getProjectsList());

        for (AbstractProject project : ProjectCollector.getProjectsList()) {
            if(this.jobNameFilterPattern != null) {
                Matcher m = this.jobNameFilterPattern.matcher(project.getName());
                if(!m.find()) {
                    notAssignedList.remove(project);
                    continue;
                }
            }
            
            Element xmlBlockElement = this.getXmlBlock(project.getConfigFile().getFile());
            
            if(xmlBlockElement != null) {
                if(notAssignedList.contains(project)) {
                    notAssignedList.remove(project);
                }
                
                String xmlBlockStr = xmlBlockElement.asXML();
                
                XmlBlock testBlock = new XmlBlock();
                testBlock.setXmlblock(xmlBlockStr);
                
                if (xmlBlocks.contains(testBlock)) {
                    XmlBlock xmlBlock = xmlBlocks.get(xmlBlocks.indexOf(testBlock));
                    xmlBlock.getProjects().add(project);
                } else {
                    XmlBlock xmlBlock = new XmlBlock();
                    xmlBlock.setXmlblock(xmlBlockStr);
                    List< AbstractProject > projects = new ArrayList< AbstractProject >();
                    projects.add(project);
                    xmlBlock.setProjects(projects);
                    xmlBlocks.add(xmlBlock);
                    
                    
                }
            }              
        }
        
        if(notAssignedList.size() > 0) {
            XmlBlock notAssignedBlock = new XmlBlock();
            notAssignedBlock.setXmlblock(NOT_ASSIGNED);
            notAssignedBlock.setProjects(notAssignedList);
            xmlBlocks.add(notAssignedBlock);
        }
        
        return xmlBlocks;
    }

    /**
     * Returns the xml block from the given file using the given xpath expression.
     * 
     * @return the xml block
     */
    @SuppressWarnings("rawtypes")
    public Element getXmlBlock(File xmlFile) {
        if (StringUtils.isEmpty(this.getXpath())) {
            return null;
        }

        try {
            Document dom = new SAXReader().read(xmlFile);

            List nodes = dom.selectNodes(this.xpath);

            if (nodes.size() > 0) {
                return (Element) nodes.get(0);
            }
        } catch (Exception e) {
            Log.error("Exception getting xml block from config.xml: ", e);
        }

        return null;
    }
}
