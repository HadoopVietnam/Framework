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
package com.hadoopvietnam.controller.member;

import com.hadoopvietnam.persistence.domain.AccountDomain;
import com.hadoopvietnam.service.AccountService;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.hadoopvietnam.common.ErrorMessage;
import com.hadoopvietnam.common.ValidationResponse;
import com.hadoopvietnam.controller.MyResourceMessage;
import com.hadoopvietnam.form.admin.RegisterForm;
import com.hadoopvietnam.form.validator.admin.RegisterValidator;

@Controller
@RequestMapping({"/dang-ky-thanh-vien"})
public class RegisterController {

    private final Logger logger = LoggerFactory.getLogger(RegisterController.class);
    @Autowired
    RegisterValidator validator;
    @Autowired
    MyResourceMessage resourceMessage;
    @Autowired
    ShaPasswordEncoder passwordEncoder;
    @Autowired
    AccountService accountService;

    @RequestMapping(method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public String registerMemberForm(Model model) {
        model.addAttribute("registerForm", new RegisterForm());
        model.addAttribute("title", "Đăng ký thành viên");
        model.addAttribute("description", "Hãy đăng ký thành viên và tham gia vào mạng xã hội tuyển dụng. Hàng ngàn công việc phù hợp với bạn");
        return "user/create";
    }

    @RequestMapping(value = {"/json"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    @ResponseBody
    public ValidationResponse registerMemberAjaxJson(Model model, @ModelAttribute("registerForm") RegisterForm form, HttpServletRequest request, BindingResult bindingResult) {
        ValidationResponse res = new ValidationResponse();
        this.validator.validate(form, bindingResult);
        if (!bindingResult.hasErrors()) {
            res.setStatus("SUCCESS");
        } else {
            res.setStatus("FAIL");
            List<FieldError> allErrors = bindingResult.getFieldErrors();
            List<ErrorMessage> errorMesages = new ArrayList<ErrorMessage>();
            for (FieldError objectError : allErrors) {
                errorMesages.add(new ErrorMessage(objectError.getField(), this.resourceMessage.getMessage(objectError.getCode(), request)));
            }
            res.setErrorMessageList(errorMesages);
        }
        return res;
    }

    @RequestMapping(method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public String registerMember(@ModelAttribute("registerForm") RegisterForm form, BindingResult bindingResult, Model model) {
        this.validator.validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("registerForm", form);
            return "user/create";
        }
        try {
            AccountDomain account = new AccountDomain();
            account.setId(System.currentTimeMillis());
            account.setUsername(form.getUsername());
            account.setPassword(this.passwordEncoder.encodePassword(form.getPassword(), null));
            account.setEmail(form.getEmail());
            account.setEnabled(true);
            if (this.accountService.save(account)) {
                model.addAttribute("email", form.getEmail());
                return "user/successregister";
            }
        } catch (Exception ex) {
            this.logger.error("Register account error: ", ex);
        }
        bindingResult.rejectValue("username", "register.save.error");
        model.addAttribute("registerForm", form);
        return "user/create";
    }
}