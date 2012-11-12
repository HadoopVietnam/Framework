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
package com.hadoopvietnam.service.statistics;

import com.hadoopvietnam.persistence.domain.statistics.StatisticBotsDomain;
import com.hadoopvietnam.persistence.domain.statistics.StatisticDomain;
import com.hadoopvietnam.persistence.domain.statistics.StatisticLogDomain;
import com.hadoopvietnam.persistence.domain.statistics.StatisticPageDomain;
import com.hadoopvietnam.persistence.domain.statistics.StatisticRefererDomain;
import com.hadoopvietnam.persistence.repositories.statistics.StatisticBotsRepository;
import com.hadoopvietnam.persistence.repositories.statistics.StatisticLogRepository;
import com.hadoopvietnam.persistence.repositories.statistics.StatisticPageRepository;
import com.hadoopvietnam.persistence.repositories.statistics.StatisticRefererRepository;
import com.hadoopvietnam.persistence.repositories.statistics.StatisticRepository;
import com.hadoopvietnam.service.AccountService;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import nl.bitwalker.useragentutils.Browser;
import nl.bitwalker.useragentutils.OperatingSystem;
import nl.bitwalker.useragentutils.UserAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.HtmlUtils;

public class StatisticService {

    private final Logger logger = LoggerFactory.getLogger(AccountService.class);
    @Autowired
    StatisticBotsRepository botsRepository;
    @Autowired
    StatisticLogRepository logRepository;
    @Autowired
    StatisticPageRepository pageRepository;
    @Autowired
    StatisticRefererRepository refererRepository;
    @Autowired
    StatisticRepository repository;
    static Map<String, String> searchEngine = new HashMap();

    public boolean save(HttpServletRequest request) {
        try {
            if (this.logger.isDebugEnabled()) {
                this.logger.debug("Log http request from id " + request.getRemoteAddr());
            }
            String myUserAgent = request.getHeader("User-Agent");
            UserAgent userAgent = UserAgent.parseUserAgentString(myUserAgent);
            OperatingSystem operatingSystem = userAgent.getOperatingSystem();
            Browser browser = userAgent.getBrowser();
            String ip = request.getRemoteAddr();

            if (browser == Browser.BOT) {
                StatisticBotsDomain botsDomain = new StatisticBotsDomain();
                botsDomain.setBot(myUserAgent);
                botsDomain.setIP(request.getRemoteAddr());
                botsDomain.setCount(0);
                botsDomain.setPageName(request.getRequestURI());
                botsDomain.setCreated(new Date());
                this.botsRepository.save(botsDomain);
            } else {
                String referer = request.getHeader("Referer") != null ? request.getHeader("Referer") : "";
                StatisticRefererDomain refererDomain = this.refererRepository.findBy(referer);
                if (refererDomain == null) {
                    refererDomain = new StatisticRefererDomain();
                    refererDomain.setReferer(referer);
                    refererDomain.setKeyword(extractKeywords(referer));
                    refererDomain.setDomain(request.getRemoteHost());
                    refererDomain.setCount(0);
                    refererDomain.setCreated(new Date());
                    this.refererRepository.save(refererDomain);
                } else {
                    this.refererRepository.update(refererDomain);
                }
                StatisticDomain domain = this.repository.findBy(ip);
                if (domain == null) {
                    domain = new StatisticDomain();
                    domain.setRefererId(refererDomain.getId());
                    domain.setDevice(browser.getBrowserType().getName());
                    domain.setBrowser(browser.getName());
                    domain.setIP(ip);
                    domain.setCount(0);
                    String sessionId = request.getSession().getId();
                    sessionId = sessionId.replaceAll("\\.tomcatA", "");
                    sessionId = sessionId.replaceAll("\\.tomcatB", "");
                    sessionId = sessionId.replaceAll("\\.tomcatC", "");
                    domain.setSession(sessionId);
                    if (operatingSystem != null) {
                        domain.setSystem(operatingSystem.getName());
                    }
                    if (userAgent.getBrowserVersion() != null) {
                        domain.setVersion(userAgent.getBrowserVersion().getMajorVersion());
                    }
                    domain.setCreated(new Date());
                    this.repository.save(domain);

                    StatisticLogDomain logDomain = new StatisticLogDomain();
                    logDomain.setSource("");
                    logDomain.setUserAgent(myUserAgent);
                    logDomain.setCreated(new Date());
                    this.logRepository.save(logDomain);

                    StatisticPageDomain pageDomain = new StatisticPageDomain();
                    pageDomain.setCount(1);
                    pageDomain.setPageName(request.getRequestURI());
                    pageDomain.setCreated(new Date());
                    this.pageRepository.save(pageDomain);
                } else {
                    this.repository.update(domain);
                }
            }
            return true;
        } catch (Exception ex) {
            this.logger.error("Log statistic http request error", ex);
        }
        return false;
    }

    private static String extractKeywords(String referer) {
        int index = referer.indexOf("?");
        String query;
        if (index > 0) {
            query = referer.substring(index + 1);
            initSearchEngine();
            Set<String> keys = searchEngine.keySet();
            for (String key : keys) {
                if (referer.indexOf(key) > 0) {
                    return (String) getQueryMap(query).get(searchEngine.get(key));
                }
            }
        }
        return "";
    }

    private static void initSearchEngine() {
        searchEngine.put("google", "q");
        searchEngine.put("bing", "q");
        searchEngine.put("conduit", "q");
        searchEngine.put("sweetim", "q");
        searchEngine.put("alltheweb", "query");
        searchEngine.put("altavista", "q");
        searchEngine.put("aol", "query");
        searchEngine.put("excite", "search");
        searchEngine.put("hotbot", "query");
        searchEngine.put("lycos", "query");
        searchEngine.put("yahoo", "p");
        searchEngine.put("live", "q");
        searchEngine.put("t-online", "q");
        searchEngine.put("msn", "q");
        searchEngine.put("netscape", "search");
        searchEngine.put("tiscali", "key");
        searchEngine.put("blogcatalog.com/search.frame.php", "term");
        searchEngine.put("blogcatalog.com/dashboard.search.php", "q");
        searchEngine.put("74.125.77.132", "q");
        searchEngine.put("youtube", "q");
        searchEngine.put("ask", "q");
        searchEngine.put("search", "q");
        searchEngine.put("abcsok", "q");
        searchEngine.put("search.earthlink.net", "q");
        searchEngine.put("eniro", "search_word");
        searchEngine.put("babylon", "q");
        searchEngine.put("search.comcast.net", "q");
        searchEngine.put("startlap", "q");
    }

    public static Map<String, String> getQueryMap(String query) {
        String[] params = query.split("&");
        Map map = new HashMap();
        for (String param : params) {
            String[] tmp = param.split("=");
            if (tmp.length == 2) {
                String name = tmp[0];
                String value = tmp[1];
                value = HtmlUtils.htmlUnescape(value);
                value = URLDecoder.decode(value);
                map.put(name, value);
            }
        }
        return map;
    }
}
