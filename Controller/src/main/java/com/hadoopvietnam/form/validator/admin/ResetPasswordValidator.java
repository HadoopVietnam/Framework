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
package com.hadoopvietnam.form.validator.admin;

import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import com.hadoopvietnam.form.admin.ResetPasswordForm;

@Service
public class ResetPasswordValidator
        implements Validator {

    public boolean supports(Class<?> clazz) {
        return ResetPasswordValidator.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        ResetPasswordForm form = (ResetPasswordForm) target;
        if (!form.getNewPassword().equals(form.getAgainPassword())) {
            errors.rejectValue("againPassword", "user.resetpasswordnotmatch");
        }
    }
}
