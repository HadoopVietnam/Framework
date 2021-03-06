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

import com.hadoopvietnam.commons.actions.context.ActionContext;

/**
 * Validation strategy that does nothing.
 */
@SuppressWarnings("unchecked")
public class NoValidationStrategy implements ValidationStrategy {

    /**
     * Validation strategy that does nothing.
     *
     * @param inActionContext the action context
     */
    @Override
    public void validate(final ActionContext inActionContext) {
        // no-op
    }
}
