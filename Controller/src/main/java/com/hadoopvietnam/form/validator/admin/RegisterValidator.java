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

import com.hadoopvietnam.persistence.domain.AccountDomain;
import com.hadoopvietnam.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import com.hadoopvietnam.common.MyValidator;
import com.hadoopvietnam.form.admin.RegisterForm;

@Service
public class RegisterValidator
        implements Validator {

    @Autowired
    AccountService accountService;

    public boolean supports(Class<?> type) {
        return RegisterValidator.class.equals(type);
    }

    public void validate(Object target, Errors errors) {
        RegisterForm form = (RegisterForm) target;

        if (MyValidator.validateNullOrEmpty(form.getUsername())) {
            errors.rejectValue("username", "user.usernamenotnull");
        } else if (!MyValidator.validateUsername(form.getUsername())) {
            errors.rejectValue("username", "user.usernamenotmatch");
        } else {
            AccountDomain account = this.accountService.findByUsername(form.getUsername());
            if (account != null) {
                errors.rejectValue("username", "user.usernamenotavaiable");
            }
        }
        if (MyValidator.validateNullOrEmpty(form.getPassword())) {
            errors.rejectValue("password", "user.passwordnotnull");
        }
        if (MyValidator.validateNullOrEmpty(form.getRePassword())) {
            errors.rejectValue("rePassword", "user.passwordnotnull");
        }
        if (!form.getPassword().equals(form.getRePassword())) {
            errors.rejectValue("password", "user.passwordnotmatch");
            errors.rejectValue("rePassword", "user.passwordnotmatch");
        }
        if (MyValidator.validateNullOrEmpty(form.getEmail())) {
            errors.rejectValue("email", "user.emailnotnull");
        } else if (!MyValidator.validateEmail(form.getEmail())) {
            errors.rejectValue("email", "user.emailnotmatch");
        } else {
            AccountDomain account = this.accountService.findByEmail(form.getEmail());
            if (account != null) {
                errors.rejectValue("email", "user.emailnotavaiable");
            }
        }
    }
}