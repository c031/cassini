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

import java.util.Collection;

import hudson.Extension;

import org.jenkinsci.lib.configprovider.AbstractConfigProvider;
import org.jenkinsci.lib.configprovider.model.Config;
import org.jenkinsci.lib.configprovider.model.ConfigDescription;
import org.jenkinsci.lib.configprovider.model.ContentType;

/**
 * Represents a config provider to manage the xpath configs.
 * @author c031
 *
 */
@Extension
public class XPathConfigProvider extends AbstractConfigProvider {

    /* (non-Javadoc)
     * @see org.jenkinsci.lib.configprovider.AbstractConfigProvider#getConfigDescription()
     */
    @Override
    public ConfigDescription getConfigDescription() {        
        return new ConfigDescription("xpath config selector", "select xml block using an xpath expression.");
    }

    /* (non-Javadoc)
     * @see org.jenkinsci.lib.configprovider.AbstractConfigProvider#getXmlFileName()
     */
    @Override
    protected String getXmlFileName() {       
        return "xpath-config-selectors.xml";
    }

    /* (non-Javadoc)
     * @see org.jenkinsci.lib.configprovider.ConfigProvider#getContentType()
     */
    @Override
    public ContentType getContentType() {       
        return ContentType.DefinedType.XML;
    }

    /* (non-Javadoc)
     * @see org.jenkinsci.lib.configprovider.AbstractConfigProvider#newConfig()
     */
    @Override
    public Config newConfig() {
        String id = getProviderId() + System.currentTimeMillis();
        return new XPathConfig(id, "config.xml block", "", "/project");
    }

    /* (non-Javadoc)
     * @see org.jenkinsci.lib.configprovider.AbstractConfigProvider#getAllConfigs()
     */
    @Override
    public Collection< Config > getAllConfigs() {
        ProjectCollector.init();
        return super.getAllConfigs();
    }

}
