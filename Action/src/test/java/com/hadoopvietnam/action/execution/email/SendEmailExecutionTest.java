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
package com.hadoopvietnam.action.execution.email;

import com.hadoopvietnam.commons.emails.EmailerFactory;
import java.util.HashMap;
import java.util.Map;
import javax.mail.internet.MimeMessage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.velocity.VelocityEngineUtils;

/**
 *
 * @author Hadoop Vietnam <admin@hadoopvietnam.com>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
    //    "classpath:META-INF/spring/applicationContext-action-framework.xml", 
    "classpath:applicationContext-mybatis.xml",
    "classpath:META-INF/spring/applicationContext-emails.xml"
})
public class SendEmailExecutionTest {

    @Autowired
    EmailerFactory emailerFactory;

    @Test
    public void testSendMail() throws Exception {
        String emailAddress = "tuanta2@peacesoft.net";
        Map model = new HashMap();
        model.put("username", "tuanta");
        model.put("emailAddress", emailAddress);
        String text = VelocityEngineUtils.mergeTemplateIntoString(
                emailerFactory.getVelocityEngine(), "welcome.vm", model);

        MimeMessage msg = emailerFactory.createMessage();
        emailerFactory.setTo(msg, emailAddress);
        emailerFactory.setSubject(msg, "Chào mừng bạn đến với Mạng Tuyển Dụng");
        emailerFactory.setHtmlBody(msg, text);
        emailerFactory.sendMail(msg);
    }
}
