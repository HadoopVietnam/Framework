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
package com.hadoopvietnam.persistence.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class AccountDomain
        implements Serializable {

    private long id;
    private String username;
    private String password;
    private String email;
    private String mobile;
    private boolean enabled;
    private String screenName;
    private int failedLoginCount;
    private Date lastFailedLoginTime;
    private String lastHostAddress;
    private Date lastLoginTime;
    private Date lastPasswordChangeTime;
    private Date expirationDate;
    private String CSRFToken;
    private List<RoleDomain> roles;
    private boolean isLogin;
    private long mainProfile;
    private String firstName;
    private String lastName;
    private Date created;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getFailedLoginCount() {
        return this.failedLoginCount;
    }

    public void setFailedLoginCount(int failedLoginCount) {
        this.failedLoginCount = failedLoginCount;
    }

    public Date getLastFailedLoginTime() {
        return this.lastFailedLoginTime;
    }

    public void setLastFailedLoginTime(Date lastFailedLoginTime) {
        this.lastFailedLoginTime = lastFailedLoginTime;
    }

    public String getLastHostAddress() {
        return this.lastHostAddress;
    }

    public void setLastHostAddress(String lastHostAddress) {
        this.lastHostAddress = lastHostAddress;
    }

    public Date getLastLoginTime() {
        return this.lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Date getLastPasswordChangeTime() {
        return this.lastPasswordChangeTime;
    }

    public void setLastPasswordChangeTime(Date lastPasswordChangeTime) {
        this.lastPasswordChangeTime = lastPasswordChangeTime;
    }

    public String getScreenName() {
        return this.screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getCSRFToken() {
        return this.CSRFToken;
    }

    public void setCSRFToken(String CSRFToken) {
        this.CSRFToken = CSRFToken;
    }

    public String getAccountName() {
        return getUsername();
    }

    public Date getExpirationDate() {
        return this.expirationDate;
    }

    public boolean isLocked() {
        return isEnabled();
    }

    public boolean isLoggedIn() {
        return this.isLogin;
    }

    public void setLocked(boolean b) {
        setEnabled(this.enabled);
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public void setAccountName(String accountName) {
        setUsername(this.username);
    }

    public void setExpirationDate(Date expirationTime) {
        this.expirationDate = expirationTime;
    }

    public long getMainProfile() {
        return this.mainProfile;
    }

    public void setMainProfile(long mainProfile) {
        this.mainProfile = mainProfile;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean isSuperUser() {
        if ("root".equals(getUsername())) {
            return true;
        }
        return false;
    }

    public List<RoleDomain> getRoles() {
        return this.roles;
    }

    public void setRoles(List<RoleDomain> roles) {
        this.roles = roles;
    }

    public Date getCreated() {
        return this.created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[Username:").append(getUsername()).append(",");
        sb.append("Password:").append("********").append(",");
        sb.append("Enable:").append(isEnabled()).append("]");
        return sb.toString();
    }
}