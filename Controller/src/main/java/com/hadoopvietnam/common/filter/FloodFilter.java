/*
 * Copyright 2012 Hadoop Vietnam <admin@hadoopvietnam.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hadoopvietnam.common.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class FloodFilter
        implements Filter {

    private Map<String, Integer> pageRequests;
    private Map<String, Integer> clientRequests;
    private ServletContext context;
    private int maxPageRequests = 50;
    private int maxClientRequests = 10;
    private static Pattern excludeUrls = Pattern.compile("^.*/(resources|css|js|images|ckeditor)/.*$", 2);
    private static Pattern link = Pattern.compile("^.*/(crawl|crawlmember)", 2);
    private static Pattern imagePattern = Pattern.compile("([^\\s]+(\\.(?i)(jpg|png|gif|bmp|ico))$)", 2);
    private String busyPage = "/antiflood";

    private boolean isWorthyRequest(HttpServletRequest request) {
        String url = request.getRequestURI().toString();
        Matcher m = excludeUrls.matcher(url);
        if (!m.matches()) {
            m = imagePattern.matcher(url);
            if (!m.matches()) {
                m = link.matcher(url);
            }
        }
        return !m.matches();
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String page = null;
        String ip = null;
        try {
            if ((request instanceof HttpServletRequest)) {
                HttpServletRequest req = (HttpServletRequest) request;
                page = req.getRequestURI();

                if (isWorthyRequest(req)) {
                    System.out.println("Request: " + page);
                    if (page.indexOf(';') >= 0) {
                        page = page.substring(0, page.indexOf(';'));
                    }

                    ip = req.getRemoteAddr();

                    if (!tryRequest(page, ip)) {
                        this.context.log("Flood denied from " + ip + " on page " + page);
                        page = null;

                        this.context.getRequestDispatcher(this.busyPage).forward(request, response);
                        return;
                    }
                }
            }
            chain.doFilter(request, response);
        } finally {
            if (page != null) {
                releaseRequest(page, ip);
            }
        }
    }

    private synchronized boolean tryRequest(String page, String ip) {
        Integer pNum = (Integer) this.pageRequests.get(page);
        if (pNum == null) {
            pNum = Integer.valueOf(1);
        } else {
            if (pNum.intValue() > this.maxPageRequests) {
                return false;
            }

            pNum = Integer.valueOf(pNum.intValue() + 1);
        }

        Integer cNum = (Integer) this.clientRequests.get(ip);

        if (cNum == null) {
            cNum = Integer.valueOf(1);
        } else {
            if (cNum.intValue() > this.maxClientRequests) {
                return false;
            }

            cNum = Integer.valueOf(cNum.intValue() + 1);
        }

        System.out.println("Total request page: " + pNum);
        System.out.println("Total request ip: " + cNum);

        this.pageRequests.put(page, pNum);
        this.clientRequests.put(ip, cNum);

        return true;
    }

    private synchronized void releaseRequest(String page, String ip) {
        Integer pNum = (Integer) this.pageRequests.get(page);

        if (pNum == null) {
            return;
        }

        if (pNum.intValue() <= 1) {
            this.pageRequests.remove(page);
        } else {
            this.pageRequests.put(page, Integer.valueOf(pNum.intValue() - 1));
        }

        Integer cNum = (Integer) this.clientRequests.get(ip);

        if (cNum == null) {
            return;
        }

        if (cNum.intValue() <= 1) {
            this.clientRequests.remove(ip);
        } else {
            this.clientRequests.put(ip, Integer.valueOf(cNum.intValue() - 1));
        }
    }

    public synchronized void init(FilterConfig config)
            throws ServletException {
        this.context = config.getServletContext();
        this.pageRequests = new HashMap();
        this.clientRequests = new HashMap();
        String s = config.getInitParameter("maxPageRequests");

        if (s != null) {
            this.maxPageRequests = Integer.parseInt(s);
        }

        s = config.getInitParameter("maxClientRequests");

        if (s != null) {
            this.maxClientRequests = Integer.parseInt(s);
        }

        s = config.getInitParameter("busyPage");

        if (s != null) {
            this.busyPage = s;
        }
    }

    public synchronized void destroy() {
        this.pageRequests.clear();
        this.clientRequests.clear();
    }
}