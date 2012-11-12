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
package vn.topmedia.common.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DisableUrlSessionFilter
        implements Filter {

    private final Logger log = LoggerFactory.getLogger(DisableUrlSessionFilter.class);

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        try {
            if (!(request instanceof HttpServletRequest)) {
                chain.doFilter(request, response);
                return;
            }

            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;

            if (httpRequest.isRequestedSessionIdFromURL()) {
                HttpSession session = httpRequest.getSession();
                if (session != null) {
                    session.invalidate();
                }

            }

            HttpServletResponseWrapper wrappedResponse = new HttpServletResponseWrapper(httpResponse) {
                public String encodeRedirectUrl(String url) {
                    return url;
                }

                public String encodeRedirectURL(String url) {
                    return url;
                }

                public String encodeUrl(String url) {
                    return url;
                }

                public String encodeURL(String url) {
                    return url;
                }
            };
            chain.doFilter(request, wrappedResponse);
        } catch (Exception ex) {
            this.log.error("DisableUrlSessionFilter do filter error: " + ex, ex);
        }
    }

    public void init(FilterConfig config)
            throws ServletException {
    }

    public void destroy() {
    }
}
