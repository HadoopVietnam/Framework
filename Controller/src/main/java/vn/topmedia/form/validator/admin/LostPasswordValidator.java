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
package vn.topmedia.form.validator.admin;

import com.hadoopvietnam.persistence.domain.AccountDomain;
import com.hadoopvietnam.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import vn.topmedia.common.MyValidator;
import vn.topmedia.form.admin.LostPasswordForm;

@Service
public class LostPasswordValidator
        implements Validator {

    @Autowired
    AccountService accountService;

    public boolean supports(Class<?> clazz) {
        return LostPasswordForm.class.equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        LostPasswordForm form = (LostPasswordForm) target;

        if (MyValidator.validateNullOrEmpty(form.getEmail())) {
            errors.rejectValue("email", "user.emailnotnull");
        } else if (!MyValidator.validateEmail(form.getEmail())) {
            errors.rejectValue("email", "user.emailnotmatch");
        } else {
            AccountDomain account = this.accountService.findByEmail(form.getEmail());
            if (account == null) {
                errors.rejectValue("email", "user.emailnotfound");
            }
        }
    }
}
