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
package com.hadoopvietnam.action.execution.emails;

import com.google.gson.Gson;
import com.hadoopvietnam.action.request.emails.SendWelcomeEmailRequest;
import com.hadoopvietnam.commons.actions.ExecutionStrategy;
import com.hadoopvietnam.commons.actions.context.ActionContext;
import com.hadoopvietnam.commons.emails.EmailerFactory;
import java.util.HashMap;
import java.util.Map;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.velocity.VelocityEngineUtils;

/**
 *
 * @author Hadoop Vietnam <admin@hadoopvietnam.com>
 */
/**
 * Execution strategy for sending a new user welcome email.
 */
public class SendWelcomeEmailExecution implements ExecutionStrategy<ActionContext> {

    /**
     * The logger.
     */
    private final Logger logger = LoggerFactory.getLogger(SendWelcomeEmailExecution.class);
    /**
     * Emailer factory that sends email.
     */
    private EmailerFactory emailerFactory;
    /**
     * Subject of welcome email.
     */
    private String subject;

    /**
     * Constructor.
     *
     * @param inEmailerFactory {@link EmailerFactory}.
     * @param inSubject subject of welcome email.
     */
    public SendWelcomeEmailExecution(final EmailerFactory inEmailerFactory,
            final String inSubject) {
        emailerFactory = inEmailerFactory;
        subject = inSubject;
    }

    /**
     * Send welcome email.
     *
     * @param inActionContext {@link ActionContext}.
     * @return True if successful.
     */
    @Override
    public Boolean execute(final ActionContext inActionContext) {
        Gson gson = new Gson();
        String sRequest = inActionContext.getParams().toString();
        SendWelcomeEmailRequest request = gson.fromJson(sRequest, SendWelcomeEmailRequest.class);
        String emailAddress = request.getEmailAddress();
        Map model = new HashMap();
        model.put("emailAddress", emailAddress);
        model.put("username", request.getAccountId());
        model.put("activeLink", request.getActiveLink());
        
        String text = VelocityEngineUtils.mergeTemplateIntoString(
                emailerFactory.getVelocityEngine(), "welcome.vm", model);

        if (emailAddress != null && !"".equals(emailAddress)) {
            try {
                MimeMessage msg = emailerFactory.createMessage();
                emailerFactory.setTo(msg, emailAddress);
                emailerFactory.setSubject(msg, subject);
                emailerFactory.setHtmlBody(msg, text);
                emailerFactory.sendMail(msg);

                if (logger.isInfoEnabled()) {
                    logger.info("New user email sent to: " + emailAddress);
                }
            } catch (Exception ex) {
                logger.error("Failed to send new user email to: " + emailAddress + ". Log: " + ex.toString());
            }
        }
        return Boolean.TRUE;
    }
}
