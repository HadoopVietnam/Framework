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
package com.hadoopvietnam.main;

import java.sql.Timestamp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author Hadoop Vietnam <admin@hadoopvietnam.com>
 */
public class TaskScheduler {

    private static Logger logger = LoggerFactory.getLogger(TaskScheduler.class);

    public static void main(String[] args) {
        AbstractApplicationContext context = new ClassPathXmlApplicationContext(
//                "classpath:META-INF/spring/applicationContext-action-framework.xml", 
                "classpath:applicationContext-mybatis.xml",
                "classpath:META-INF/spring/applicationContext-accounts.xml", 
                "classpath:META-INF/spring/applicationContext-configure.xml", 
                "classpath:META-INF/spring/applicationContext-emails.xml", 
                "classpath:META-INF/spring/applicationContext-framework-actions.xml", 
                "classpath:META-INF/spring/applicationContext-memcached.xml", 
                "classpath:META-INF/spring/applicationContext-service.xml", 
                "classpath:META-INF/spring/applicationContext-dequeue-actionrequest.xml");
        context.registerShutdownHook();
        if (logger.isDebugEnabled()) {
            logger.debug("Task Scheduler running .... " + new Timestamp(System.currentTimeMillis()));
        }
    }
}
