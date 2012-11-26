/*
 * Copyright (c) 2010-2011 Lockheed Martin Corporation
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
package com.hadoopvietnam.commons.server.service;

import com.hadoopvietnam.commons.actions.TaskHandlerAction;
import com.hadoopvietnam.commons.actions.context.PrincipalActionContext;
import com.hadoopvietnam.commons.actions.service.ServiceAction;
import java.io.Serializable;

/**
 * Interface for executing a ServiceAction.
 */
public interface ActionController {

    /**
     * Execute the supplied {@link ServiceAction} with the given
     * {@link PrincipalActionContext}.
     *
     * @param inServiceActionContext - instance of the
     * {@link PrincipalActionContext} with which to execution the
     * {@link ServiceAction}.
     * @param inServiceAction - instance of the {@link ServiceAction} to
     * execute.
     * @return - results from the execution of the ServiceAction.
     *
     * - GeneralException - when an unexpected error occurs. -
     * ValidationException - when a {@link ValidationException} occurs. -
     * AuthorizationException - when an {@link AuthorizationException} occurs. -
     * ExecutionException - when an {@link ExecutionException} occurs.
     */
    Serializable execute(final PrincipalActionContext inServiceActionContext, final ServiceAction inServiceAction);

    /**
     * This method executes a {@link TaskHandlerAction} with the supplied
     * {@link PrincipalActionContext}.
     *
     * @param inServiceActionContext - instance of the
     * {@link PrincipalActionContext} associated with this request.
     * @param inTaskHandlerAction - instance of the {@link TaskHandlerAction}.
     * @return - results of the execution.
     *
     * - GeneralException - when an unexpected error occurs. -
     * ValidationException - when a {@link ValidationException} occurs. -
     * AuthorizationException - when an {@link AuthorizationException} occurs. -
     * ExecutionException - when an {@link ExecutionException} occurs.
     */
    Serializable execute(final PrincipalActionContext inServiceActionContext, // \n
            final TaskHandlerAction inTaskHandlerAction);
}
