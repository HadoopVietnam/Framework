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
import java.util.regex.Pattern;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class XSSFilter
        implements Filter {

    public void init(FilterConfig filterConfig)
            throws ServletException {
    }

    public void destroy() {
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(new XSSRequestWrapper((HttpServletRequest) request), response);
    }

    public class XSSRequestWrapper extends HttpServletRequestWrapper {

        private Pattern[] patterns = {Pattern.compile("<script>(.*?)</script>", 2), Pattern.compile("src[\r\n]*=[\r\n]*\\'(.*?)\\'", 42), Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", 42), Pattern.compile("</script>", 2), Pattern.compile("<script(.*?)>", 42), Pattern.compile("eval\\((.*?)\\)", 42), Pattern.compile("expression\\((.*?)\\)", 42), Pattern.compile("javascript:", 2), Pattern.compile("vbscript:", 2), Pattern.compile("onload(.*?)=", 42)};

        public XSSRequestWrapper(HttpServletRequest servletRequest) {
            super(servletRequest);
        }

        public String[] getParameterValues(String parameter) {
            String[] values = super.getParameterValues(parameter);

            if (values == null) {
                return null;
            }

            int count = values.length;
            String[] encodedValues = new String[count];
            for (int i = 0; i < count; i++) {
                encodedValues[i] = stripXSS(values[i]);
            }

            return encodedValues;
        }

        public String getParameter(String parameter) {
            String value = super.getParameter(parameter);

            return stripXSS(value);
        }

        public String getHeader(String name) {
            String value = super.getHeader(name);
            return stripXSS(value);
        }

        private String stripXSS(String value) {
            if (value != null) {
//                value = ESAPI.encoder().canonicalize(value);

                value = value.replaceAll("", "");

                for (Pattern scriptPattern : this.patterns) {
                    value = scriptPattern.matcher(value).replaceAll("");
                }
            }
            return value;
        }
    }
}
