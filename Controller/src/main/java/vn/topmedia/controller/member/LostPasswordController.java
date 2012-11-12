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
package vn.topmedia.controller.member;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.topmedia.common.ErrorMessage;
import vn.topmedia.common.ValidationResponse;
import vn.topmedia.controller.MyResourceMessage;
import vn.topmedia.form.admin.LostPasswordForm;
import vn.topmedia.form.validator.admin.LostPasswordValidator;

@RequestMapping({"/quen-mat-khau"})
@Controller
public class LostPasswordController {

    private final Logger logger = LoggerFactory.getLogger(LostPasswordController.class);
    @Autowired
    LostPasswordValidator validator;
    @Autowired
    MyResourceMessage resourceMessage;

    @RequestMapping(method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public String lostPasswordForm(Model model) {
        model.addAttribute("lostPasswordForm", new LostPasswordForm());
        model.addAttribute("title", "Quên mật khẩu");
        model.addAttribute("description", "Bạn quên mật khẩu? Hãy lấy lại mật khẩu của bạn một cách đơn giản.");
        return "user/lostpassword";
    }

    @RequestMapping(value = {"/json"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    @ResponseBody
    public ValidationResponse lostPasswordAjaxJson(Model model, @ModelAttribute("lostPasswordForm") LostPasswordForm form, HttpServletRequest request, BindingResult bindingResult) {
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
    public String lostPassword(LostPasswordForm form, Model model, BindingResult bindingResult) {
        this.validator.validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("lostPasswordForm", form);
            return "user/lostpassword";
        }
        model.addAttribute("email", form.getEmail());
        return "user/successlostpassword";
    }
}