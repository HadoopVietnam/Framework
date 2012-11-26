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
package com.hadoopvietnam.commons.integrations;

import com.hadoopvietnam.commons.actions.UserActionRequest;
import com.hadoopvietnam.commons.task.TaskHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 *
 * @author Hadoop Vietnam <admin@hadoopvietnam.com>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    //    "classpath:META-INF/spring/applicationContext-action-framework.xml", 
    //    "classpath:applicationContext-mybatis.xml",
    "classpath:META-INF/spring/applicationContext-configure.xml",
    "classpath:META-INF/spring/applicationContext-enqueue-actionrequest.xml"
})
public class SpringIntegrationTest {

    @Autowired
    TaskHandler taskHandler;
//    @Resource(name = "userActionRequestChannel")
//    MessageChannel userActionRequestChannel;
//    private volatile MessagingTemplate messagingTemplate;

    @Test
    public void testSetup() throws Exception {
//        Assert.assertNotNull(userActionRequestChannel);
//        messagingTemplate = new MessagingTemplate(userActionRequestChannel);
//        Assert.assertNotNull(messagingTemplate);
        UserActionRequest request = new UserActionRequest();
        request.setActionKey("helloworld");
        request.setParams("Do action " + System.currentTimeMillis() + "");
        taskHandler.handleTask(request);
//        messagingTemplate.convertAndSend(request);
    }
}