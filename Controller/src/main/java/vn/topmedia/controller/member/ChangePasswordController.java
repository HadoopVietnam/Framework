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

import com.hadoopvietnam.service.AccountService;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import vn.topmedia.common.ErrorMessage;
import vn.topmedia.common.ValidationResponse;
import vn.topmedia.controller.AbstractController;
import vn.topmedia.controller.MyResourceMessage;
import vn.topmedia.form.admin.ChangePasswordForm;
import vn.topmedia.form.validator.admin.ChangePasswordValidator;

@RequestMapping({"/thanh-vien/doi-mat-khau"})
@Controller
public class ChangePasswordController extends AbstractController {

    private static final Log logger = LogFactory.getLog(ChangePasswordController.class);
    @Autowired
    ChangePasswordValidator validator;
    @Autowired
    AccountService accountService;
    @Autowired
    MyResourceMessage resourceMessage;
    @Autowired
    ShaPasswordEncoder passwordEncoder;

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = {"/json"}, method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    @ResponseBody
    public ValidationResponse changePasswordAjaxJson(Model model, @ModelAttribute("changePasswordForm") ChangePasswordForm form, HttpServletRequest request, BindingResult bindingResult) {
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

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = {org.springframework.web.bind.annotation.RequestMethod.POST})
    public String changePassword(@ModelAttribute("changePasswordForm") ChangePasswordForm form, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        this.validator.validate(form, bindingResult);
        if (bindingResult.hasErrors()) {
            uiModel.addAttribute("changePasswordForm", form);
            return "user/changepassword";
        }
        try {
            this.accountService.changePassword(getUsername(), this.passwordEncoder.encodePassword(form.getNewPassword(), null));
            return "user/successchangepassword";
        } catch (Exception ex) {
            logger.error("Change password for user " + getUsername() + " error.", ex);
            bindingResult.reject("changepassword.error");
            uiModel.addAttribute("changePasswordForm", new ChangePasswordForm());
        }
        return "user/changepassword";
    }

    @PreAuthorize("isAuthenticated()")
    @RequestMapping(method = {org.springframework.web.bind.annotation.RequestMethod.GET})
    public String index(Model uiModel) {
        uiModel.addAttribute("changePasswordForm", new ChangePasswordForm());
        return "user/changepassword";
    }
}
