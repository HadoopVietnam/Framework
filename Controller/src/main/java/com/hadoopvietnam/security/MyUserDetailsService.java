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
package com.hadoopvietnam.security;

import com.hadoopvietnam.persistence.domain.AccountDomain;
import com.hadoopvietnam.service.AccountService;
import com.hadoopvietnam.service.RoleService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import com.hadoopvietnam.common.MyValidator;

public class MyUserDetailsService
        implements UserDetailsService {

    @Autowired
    private AccountService accountService;
    @Autowired
    private RoleService roleService;

    @Transactional
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException, DataAccessException {
        List granterdAuthorities = new ArrayList();
        granterdAuthorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        if (("tuanta".equals(username)) || ("thamnt".equals(username))) {
            granterdAuthorities.add(new SimpleGrantedAuthority("ROLE_MANAGER"));
        }
        AccountDomain account;
        if (MyValidator.validateEmail(username)) {
            account = this.accountService.findByEmail(username);
        } else {
            if (MyValidator.validateMobile(username)) {
                account = this.accountService.findByMobile(username);
            } else {
                account = this.accountService.findByUsername(username);
            }
        }
        if (account == null) {
            throw new UsernameNotFoundException("Tài khoản không tồn tại.");
        }
        return new User(account.getUsername(), account.getPassword(), account.isEnabled(), true, true, true, granterdAuthorities);
    }
}
