/*
 * Copyright (c) 2009-2011 Lockheed Martin Corporation
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
package com.hadoopvietnam.commons.actions;

import com.hadoopvietnam.commons.actions.context.Principal;
import java.io.Serializable;

/**
 * This class encapsulates in one object everything an action needs to process a
 * request, including passing this object as is to a producer that puts it on
 * the queue to be picked up by the consumer and passed to an async action.
 */
public class UserActionRequest implements Serializable {

    /**
     * Generated serial version Id.
     */
    private static final long serialVersionUID = -5787537910924233440L;
    /**
     * Uniquely represents one ServerAction.
     */
    private String actionKey;
    /**
     * User under whose authority or on whose behalf the action should be
     * executed.
     */
    private Principal user;
    /**
     * Parameters to be passed to the action.
     */
    private String params = null;

    /**
     * Constructor.
     *
     * @param inActionKey the action to be called to respond to the request;
     * this action is to implement {@link RequestAction}
     * @param inUser User under whose authority or on whose behalf the action
     * should be executed.
     * @param inParams parameters to send to the request.
     */
    public UserActionRequest() {
    }

    /**
     * Getter.
     *
     * @return the action key
     */
    public String getActionKey() {
        return actionKey;
    }

    /**
     * Getter.
     *
     * @return parameters for the ServerAction
     */
    public String getParams() {
        return params;
    }

    /**
     * Getter.
     *
     * @return the user
     */
    public Principal getUser() {
        return user;
    }

    /**
     * Setter.
     *
     * @param inUser the user
     */
    public void setUser(final Principal inUser) {
        user = inUser;
    }

    public void setActionKey(String actionKey) {
        this.actionKey = actionKey;
    }

    public void setParams(String params) {
        this.params = params;
    }

    /**
     * Retrieve information about the user request overriding the toString
     * method.
     *
     * @return String description of the contents of this User Request.
     */
    @Override
    public String toString() {
        String stringOutput = "UserActionRequest actionKey: " + actionKey + " requesting params: "
                + params;
        return stringOutput;
    }
}
