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
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import com.hadoopvietnam.common.MyValidator;
import com.hadoopvietnam.form.admin.ChangePasswordForm;

@Service
public class ChangePasswordValidator
        implements Validator {

    @Autowired
    AccountService accountService;
    @Autowired
    ShaPasswordEncoder passwordEncoder;

    public boolean supports(Class<?> clazz) {
        return ChangePasswordForm.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        ChangePasswordForm form = (ChangePasswordForm) target;
        if (MyValidator.validateNullOrEmpty(form.getOldPassword())) {
            errors.rejectValue("oldPassword", "changepassword.oldpasswordnotnull");
        }
        if (MyValidator.validateNullOrEmpty(form.getNewPassword())) {
            errors.rejectValue("newPassword", "changepassword.newpasswordnotnull");
        }
        if (MyValidator.validateNullOrEmpty(form.getAgainPassword())) {
            errors.rejectValue("againPassword", "changepassword.againpasswordnotnull");
        } else if (!form.getNewPassword().equals(form.getAgainPassword())) {
            errors.rejectValue("newPassword", "changepassword.passwordsnomatch");

            errors.rejectValue("againPassword", "changepassword.passwordsnomatch");
        } else {
            String oldPassword = form.getOldPassword();
            String encryptPassword = this.passwordEncoder.encodePassword(oldPassword, null);
            try {
                AccountDomain accountDomain = this.accountService.findByUsernameAndPassword(getUsername(), encryptPassword);
                if (accountDomain == null) {
                    errors.rejectValue("oldPassword", "changepassword.invalidpassword");
                }
            } catch (Exception e) {
                errors.rejectValue("oldPassword", "changepassword.invalidpassword");
            }
        }
    }

    public String getUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if ((auth != null) && ((auth.getPrincipal() instanceof UserDetails))) {
            return ((UserDetails) auth.getPrincipal()).getUsername();
        }
        return auth.getPrincipal().toString();
    }
}
