/*
 * Copyright (c) 2010 Lockheed Martin Corporation
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

import java.io.Serializable;

import com.hadoopvietnam.commons.actions.context.ActionContext;
import com.hadoopvietnam.commons.exceptions.ExecutionException;

/**
 * The ExecutionStrategy contains the business logic of the Action.
 *
 * @param <T> action context type.
 */
public interface ExecutionStrategy<T extends ActionContext> {

    /**
     * Executes the business logic within the strategy given the specified
     * context.
     *
     * @param inActionContext - context of the action to execute this strategy
     * within.
     * @return results of the execution.
     * @throws ExecutionException on error.
     */
    Serializable execute(T inActionContext) throws ExecutionException;
}