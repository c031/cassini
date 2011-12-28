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

import hudson.Extension;
import hudson.model.ManagementLink;
import hudson.model.Hudson;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;

import net.sf.json.JSONObject;

import org.acegisecurity.AccessDeniedException;
import org.jenkinsci.lib.configprovider.ConfigProvider;
import org.jenkinsci.lib.configprovider.model.Config;
import org.kohsuke.stapler.HttpRedirect;
import org.kohsuke.stapler.HttpResponse;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

/**
 * Main class for the config viewer plugin.
 * 
 * @author c031
 * 
 */

@Extension
public class XPathConfigViewerPlugin extends ManagementLink {

    /**
     * the xpath config provider.
     */
    private ConfigProvider xPathConfigProvider;

    /**
     * initialize the xpath config provider;
     */
    public XPathConfigViewerPlugin() {
        for (ConfigProvider provider : ConfigProvider.all()) {
            if (provider.getProviderId().equals("XPathConfigProvider.")) {
                this.xPathConfigProvider = provider;
                break;
            }
        }      
    }
    
    

    /**
     * Returns the description of the plugin.
     * 
     * @return the description of the plugin
     */
    @Override
    public String getDescription() {
        return "View configurations across all jobs selected by xpath";
    }

    /**
     * Returns the icon filename of the plugin to be shown on the /manage page.
     * 
     * @return the icon filename
     */
    @Override
    public String getIconFileName() {
        return "orange-square.gif";
    }

    /**
     * Returns the url part to open the plugin page.
     * 
     * @return the url part
     */
    @Override
    public String getUrlName() {
        return "xpathviewer";
    }

    /**
     * Returns the display name of the plugin to be shown on the /manage page.
     * 
     * @return the display name
     */
    public String getDisplayName() {
        return "XPath Configuration Viewer";
    }

    /**
     * Returns the list of all config providers.
     * 
     * @return the list of all config providers
     */
    public List< ConfigProvider > getProviders() {
        return ConfigProvider.all();
    }

    /**
     * Requests a new config object from provider (defined by the given id) and forwards the request to "edit.jelly".
     * 
     * @param req
     *            request
     * @param rsp
     *            response
     * @throws IOException
     * @throws ServletException
     */
    public void doAddXPath(StaplerRequest req, StaplerResponse rsp) throws IOException, ServletException {
        checkAdminPermission();

        req.setAttribute("contentType", this.xPathConfigProvider.getContentType());
        req.setAttribute("provider", this.xPathConfigProvider);
        XPathConfig config = (XPathConfig) this.xPathConfigProvider.newConfig();
        req.setAttribute("config", config);

        req.getView(this, "edit.jelly").forward(req, rsp);
    }

    /**
     * Loads the config by its id and forwards the request to "edit.jelly".
     * 
     * @param req
     *            request
     * @param rsp
     *            response
     * @param confgiId
     *            the id of the config to be loaded in to the edit view.
     * @throws IOException
     * @throws ServletException
     */
    public void doEditConfig(StaplerRequest req, StaplerResponse rsp, @QueryParameter("id") String confgiId) throws IOException, ServletException {
        checkAdminPermission();

        Config config = this.xPathConfigProvider.getConfigById(confgiId);
        if (config != null) {
            req.setAttribute("config", config);
            req.setAttribute("contentType", this.xPathConfigProvider.getContentType());
            req.setAttribute("provider", this.xPathConfigProvider);
            
            req.getView(this, "edit.jelly").forward(req, rsp);
        } else {
            req.getView(this, "index").forward(req, rsp);
        }
    }

    /**
     * Returns the http response to redirect to /index after save.
     * 
     * @param req
     *            Request containing the xpathconfig form data.
     * @return the http response
     */
    public HttpResponse doSaveXPath(StaplerRequest req) {

        try {
            JSONObject json = req.getSubmittedForm().getJSONObject("xpathconfig");
            XPathConfig config = req.bindJSON(XPathConfig.class, json);

            this.xPathConfigProvider.save(config);

        } catch (ServletException e) {
            e.printStackTrace();
        }
        return new HttpRedirect("index");
    }
    
    /**
     * Removes a script from the config and filesystem.
     * 
     * @param res
     *            response
     * @param rsp
     *            request
     * @param configId
     *            the id of the config to be removed
     * @return forward to 'index'
     * @throws IOException
     */
    public HttpResponse doDelConfig(StaplerRequest res, StaplerResponse rsp, @QueryParameter("id") String configId) throws IOException {
        checkAdminPermission();
        this.xPathConfigProvider.remove(configId);
         
        return new HttpRedirect("index");
    }

    /**
     * Throws an AccessDeniedException if a non admin users tries to access the common config viewer plugin.
     *
     * @throws AccessDeniedException
     *             if the user doesn't have the permission.
     */
    private void checkAdminPermission() {
        Hudson.getInstance().checkPermission(Hudson.ADMINISTER);
    }
}
