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
package com.hadoopvietnam.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hadoopvietnam.persistence.domain.RoleDomain;
import com.hadoopvietnam.persistence.repositories.RoleRepository;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.PlatformTransactionManager;

public class RoleService extends AbstractCachedService<RoleDomain> {

    private final Logger logger = LoggerFactory.getLogger(AccountService.class);
    @Autowired
    private RoleRepository repository;
    @Autowired
    private PlatformTransactionManager transMgr;

    @Override
    public Collection<RoleDomain> cacheToList(String key) {
        try {
            Object obj = getCache().get(key);
            if (obj != null) {
                Type list = new TypeToken<Collection<RoleDomain>>() {
                }.getType();

                Gson gson = new Gson();
                return gson.fromJson(obj.toString(), list);
            }
        } catch (Exception ex) {
            this.logger.error("CacheToList: Cannot find key " + key + " in cache.", ex);
        }
        return null;
    }

    @Override
    public RoleDomain cacheToObject(String key) {
        try {
            Object obj = getCache().get(key);
            if (obj != null) {
                Type object = new TypeToken<RoleDomain>() {
                }.getType();

                Gson gson = new Gson();
                return gson.fromJson(obj.toString(), object);
            }
        } catch (Exception ex) {
            this.logger.error("CacheToObject: Cannot find key " + key + " in cache.", ex);
        }
        return null;
    }

    public Collection<RoleDomain> findAll() {
        Collection role = null;
        try {
            this.logger.debug("Find all role from cache.");
            role = cacheToList("Role:findAll");
            if (role == null) {
                this.logger.debug("Find all role from database.");
                role = this.repository.findAll();
                addToList(role, "Role:findAll");
            }
        } catch (Exception ex) {
            this.logger.error("Find all role error: " + ex, ex);
        }
        if ((role != null) && (!role.isEmpty())) {
            this.logger.debug("Total get " + role.size() + " roles.");
            return role;
        }
        return new ArrayList();
    }

    public Collection<RoleDomain> findByLimit(int start, int end) {
        Collection role = null;
        try {
            this.logger.debug("Find list role from " + start + " to " + end + " in cache.");
            role = cacheToList("Role:limit:" + start + end);
            if (role == null) {
                this.logger.debug("Find list role from " + start + " to " + end + " in database.");
                role = this.repository.findByLimit(start, end);
                addToList(role, "Role:limit:" + start + end, 180);
            }
        } catch (Exception ex) {
            this.logger.error("Find list role error: " + ex, ex);
        }
        if ((role != null) && (!role.isEmpty())) {
            this.logger.debug("Total get " + role.size() + " roles.");
            return role;
        }
        return new ArrayList();
    }

    public Collection<RoleDomain> findByUsername(String username) {
        Collection role = null;
        try {
            this.logger.debug("Find role " + username + " from cache.");
            role = cacheToList("Role:" + username);
            if (role == null) {
                this.logger.debug("Find role " + username + " from database.");
                role = this.repository.findByUsername(username);
                addToList(role, "Role:" + username);
            }
        } catch (Exception ex) {
            this.logger.error("Find role " + username + " error: " + ex, ex);
        }
        if ((role != null) && (!role.isEmpty())) {
            this.logger.debug("Total get " + role.size() + " roles.");
            return role;
        }
        return new ArrayList();
    }

    public boolean save(RoleDomain role) {
        try {
            this.logger.debug("Save role " + role.toString() + " to database");

            this.repository.save(role);
            getCache().delete("Role:" + role.getUserId());
            clearCache();
        } catch (DuplicateKeyException dkex) {
            this.logger.warn("Role " + role.toString() + " duplicate.");
            return false;
        } catch (Exception ex) {
            this.logger.error("Cannot save role " + role.toString(), ex);
            return false;
        }
        return true;
    }

    public boolean remove(RoleDomain role) {
        try {
            this.logger.debug("Remove role " + role + " from database.");

            this.repository.remove(role);
            getCache().delete("Role:" + role.getUserId());
            clearCache();
        } catch (Exception ex) {
            this.logger.error("Cannot delete role " + role, ex);
            return false;
        }
        return true;
    }

    public boolean delete(String username) {
        try {
            this.logger.debug("Delete role " + username + " from database.");

            this.repository.delete(username);

            this.logger.debug("Delete role " + username + " from cache.");
            getCache().delete("Role:" + username);
            clearCache();
        } catch (Exception ex) {
            this.logger.error("Cannot delete role " + username, ex);
            return false;
        }
        return true;
    }

    private void clearCache() {
        getCache().delete("Role:findAll");
    }
}
