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
package com.hadoopvietnam.action.execution;

import com.google.gson.Gson;
import com.hadoopvietnam.action.request.CreatePersonRequest;
import com.hadoopvietnam.action.request.emails.SendWelcomeEmailRequest;
import com.hadoopvietnam.commons.actions.TaskHandlerExecutionStrategy;
import com.hadoopvietnam.commons.actions.UserActionRequest;
import com.hadoopvietnam.commons.actions.context.ActionContext;
import com.hadoopvietnam.commons.actions.context.TaskHandlerActionContext;
import com.hadoopvietnam.persistence.domain.AccountDomain;
import com.hadoopvietnam.service.AccountService;
import java.io.Serializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Hadoop Vietnam <admin@hadoopvietnam.com>
 */
public class CreatePersonExecution implements TaskHandlerExecutionStrategy<ActionContext> {

    /**
     * Logger.
     */
    private final Logger logger = LoggerFactory.getLogger(CreatePersonExecution.class);
    /**
     * Send welcome email action key.
     */
    private String sendWelcomeEmailAction = null;
    /**
     * Person mapper.
     */
    private final AccountService accountService;

    /**
     * Constructor.
     *
     * @param inAccountService mapper to get people.
     * @param inSendWelcomeEmailAction Send welcome email action key.
     */
    public CreatePersonExecution(final AccountService inAccountService, final String inSendWelcomeEmailAction) {
        accountService = inAccountService;
        sendWelcomeEmailAction = inSendWelcomeEmailAction;
    }

    /**
     * Add person to the system.
     *
     * @param inActionContext The action context
     *
     * @return true on success.
     */
    @Override
    public Serializable execute(final TaskHandlerActionContext<ActionContext> inActionContext) {
        Gson gson = new Gson();
        String sRequest = inActionContext.getActionContext().getParams().toString();
        CreatePersonRequest createRequest = gson.fromJson(sRequest, CreatePersonRequest.class);
        AccountDomain inPerson = createRequest.getPerson();
        logger.info("Save account " + inPerson.getUsername() + " to database");
        if (accountService.save(inPerson)) // Send email notification if necessary
        {
            if (createRequest.getSendEmail() && sendWelcomeEmailAction != null && !sendWelcomeEmailAction.isEmpty()) {
                UserActionRequest request = new UserActionRequest();
                request.setActionKey(sendWelcomeEmailAction);
                String activeLink = "";
                SendWelcomeEmailRequest sendWelcomeEmailRequest = new SendWelcomeEmailRequest(inPerson.getEmail(), inPerson.getUsername(), activeLink);
                request.setParams(gson.toJson(sendWelcomeEmailRequest));
                inActionContext.getUserActionRequests().add(request);
            }
        }
        return inPerson;
    }
}
