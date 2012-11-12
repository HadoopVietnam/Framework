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

import com.hadoopvietnam.service.statistics.StatisticService;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

public class LoggingFilter
        implements Filter {

    @Autowired
    private StatisticService statisticService;
    private static Pattern excludeUrls = Pattern.compile("^.*/(resources|css|js|images|ckeditor)/.*$", 2);
    private static Pattern link = Pattern.compile("^.*/(crawl|crawlmember)", 2);
    private static Pattern imagePattern = Pattern.compile("([^\\s]+(\\.(?i)(jpg|png|gif|bmp|ico))$)", 2);

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

    public void init(FilterConfig filterConfig)
            throws ServletException {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        if (isWorthyRequest(httpServletRequest)) {
            this.statisticService.save(httpServletRequest);
        }
        chain.doFilter(request, response);
    }
}