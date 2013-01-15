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
import com.hadoopvietnam.cache.CacheKeys;
import com.hadoopvietnam.persistence.domain.AccountDomain;
import com.hadoopvietnam.persistence.repositories.AccountRepository;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;

public class AccountService extends AbstractCachedService<AccountDomain> {
    private static final int MAX_TIME_LIST_CACHE = 5;

    private final Logger logger = LoggerFactory.getLogger(AccountService.class);
    @Autowired
    private AccountRepository repository;

    @Override
    protected Collection<AccountDomain> cacheToList(String key) {
        try {
            Object obj = getCache().get(key);
            if (obj != null) {
                Type list = new TypeToken<Collection<AccountDomain>>() {
                }.getType();

                Gson gson = new Gson();
                return (Collection) gson.fromJson(obj.toString(), list);
            }
        } catch (Exception ex) {
            this.logger.error("CacheToList: Cannot find key " + key + " in cache.", ex);
        }
        return null;
    }

    @Override
    public AccountDomain cacheToObject(String key) {
        try {
            Object obj = getCache().get(key);
            if (obj != null) {
                Gson gson = new Gson();
                return gson.fromJson(obj.toString(), AccountDomain.class);
            }
        } catch (Exception ex) {
            this.logger.error("CacheToObject: Cannot find key " + key + " in cache.", ex);
        }
        return null;
    }

    public Collection<AccountDomain> findAll() {
        Collection account = null;
        try {
            this.logger.debug("Find all account from cache.");
            account = cacheToList(CacheKeys.ACCOUNT_ALL);
            if (account == null) {
                this.logger.debug("Find all account from database.");
                account = this.repository.findAll();
                addToList(account, CacheKeys.ACCOUNT_ALL);
            }
        } catch (Exception ex) {
            this.logger.error("Find all account error: " + ex, ex);
        }
        if ((account != null) && (!account.isEmpty())) {
            this.logger.debug("Total get " + account.size() + " accounts.");
            return account;
        }
        return new ArrayList();
    }

    public Collection<AccountDomain> findByLimit(String orderBy, int start, int end) {
        Collection account = null;
        try {
            this.logger.debug("Find list account from " + start + " to " + end + " in cache.");
            account = cacheToList(CacheKeys.ACCOUNT_LIMIT + start + end);
            if (account == null) {
                this.logger.debug("Find list account from " + start + " to " + end + " in database.");
                account = this.repository.findByLimit(orderBy, start, end);
                addToList(account, CacheKeys.ACCOUNT_LIMIT + start + end, MAX_TIME_LIST_CACHE);
            }
        } catch (Exception ex) {
            this.logger.error("Find list account error: " + ex, ex);
        }
        if ((account != null) && (!account.isEmpty())) {
            this.logger.debug("Total get " + account.size() + " accounts.");
            return account;
        }
        return new ArrayList();
    }

    public Collection<AccountDomain> findByBlock(int start, int end) {
        Collection account = null;
        try {
            this.logger.debug("Find list block account from " + start + " to " + end + " in cache.");
            account = cacheToList(CacheKeys.ACCOUNT_BLOCK + start + end);
            if (account == null) {
                this.logger.debug("Find list block account from " + start + " to " + end + " in database.");
                account = this.repository.findByLimit("username", start, end);
                addToList(account, CacheKeys.ACCOUNT_BLOCK + start + end, MAX_TIME_LIST_CACHE);
            }
        } catch (Exception ex) {
            this.logger.error("Find list block account error: " + ex, ex);
        }
        if ((account != null) && (!account.isEmpty())) {
            this.logger.debug("Total get " + account.size() + " block accounts.");
            return account;
        }
        return new ArrayList();
    }

    public Collection<AccountDomain> findByActive(int start, int end) {
        Collection account = null;
        try {
            this.logger.debug("Find list active account from " + start + " to " + end + " in cache.");
            account = cacheToList(CacheKeys.ACCOUNT_ACTIVE + start + end);
            if (account == null) {
                this.logger.debug("Find list active account from " + start + " to " + end + " in database.");
                account = this.repository.findByLimit("username", start, end);
                addToList(account, CacheKeys.ACCOUNT_ACTIVE + start + end, MAX_TIME_LIST_CACHE);
            }
        } catch (Exception ex) {
            this.logger.error("Find list active account error: " + ex, ex);
        }
        if ((account != null) && (!account.isEmpty())) {
            this.logger.debug("Total get " + account.size() + " active accounts.");
            return account;
        }
        return new ArrayList();
    }

    public AccountDomain findByUsernameAndPassword(String username, String password) {
        AccountDomain account = null;
        try {
            this.logger.debug("Find account " + username + " from cache.");
            account = cacheToObject(CacheKeys.ACCOUNT_ID + username);
            if (account == null) {
                this.logger.debug("Find account " + username + " from database.");
                account = this.repository.findByUsernameAndPassword(username, password);
                addToObject(account, CacheKeys.ACCOUNT_ID + username);
            } else {
                if (!account.getPassword().equals(password)) {
                    return null;
                }
            }
        } catch (Exception ex) {
            this.logger.error("Find by username " + username + " and password ******** error: " + ex, ex);
        }
        return account;
    }

    public AccountDomain findByUsername(String username) {
        AccountDomain account = null;
        try {
            this.logger.debug("Find account " + username + " from cache.");
            account = cacheToObject(CacheKeys.ACCOUNT_ID + username);
            if (account == null) {
                this.logger.debug("Find account " + username + " from database.");
                account = this.repository.findByUsername(username);
                addToObject(account, CacheKeys.ACCOUNT_ID + username);
            }
        } catch (Exception ex) {
            this.logger.error("Find by username " + username + " and password ******** error: " + ex, ex);
        }
        return account;
    }

    public AccountDomain findByEmail(String email) {
        AccountDomain account = null;
        try {
            this.logger.debug("Find email " + email + " from database.");
            account = this.repository.findByEmail(email);
        } catch (Exception ex) {
            this.logger.error("Find by email " + email + " error: " + ex, ex);
        }
        return account;
    }

    public AccountDomain findByMobile(String mobile) {
        AccountDomain account = null;
        try {
            this.logger.debug("Find mobile " + mobile + " from database.");
            account = this.repository.findByMobile(mobile);
        } catch (Exception ex) {
            this.logger.error("Find by mobile " + mobile + " error: " + ex, ex);
        }
        return account;
    }

    public AccountDomain findById(long id) {
        AccountDomain account = null;
        try {
            this.logger.debug("Find account id " + id + " from cache.");
            account = cacheToObject(CacheKeys.ACCOUNT_ID + id);
            if (account == null) {
                this.logger.debug("Find account id " + id + " from database.");
                account = this.repository.findById(id);
                addToObject(account, CacheKeys.ACCOUNT_ID + id);
            }
        } catch (Exception ex) {
            this.logger.error("Find by account id " + id + " error: " + ex, ex);
        }
        return account;
    }

    public boolean isExsit(String username, String email) {
        AccountDomain account = null;
        try {
            account = this.repository.findByUsernameOrEmail(username, email);
        } catch (Exception ex) {
            this.logger.error("Find by username " + username + " email " + email + " error: " + ex, ex);
        }
        if (account == null) {
            return false;
        }
        return true;
    }

    public boolean save(AccountDomain account) {
        try {
            this.logger.debug("Save account " + account.toString() + " to database");

            this.repository.save(account);

            this.logger.debug("Save account " + account.toString() + " to cache");
            //Add to cache.
            addToObject(account, CacheKeys.ACCOUNT_ID + account.getUsername());
            clearCache();
        } catch (DuplicateKeyException dkex) {
            this.logger.warn("Username " + account.toString() + " duplicate.");
            return false;
        } catch (Exception ex) {
            this.logger.error("Cannot save account " + account.toString(), ex);
            return false;
        }
        return true;
    }

    public boolean active(String username) {
        try {
            this.logger.debug("Active account " + username + " from database.");

            this.repository.active(username);

            this.logger.debug("Active account " + username + " from cache.");
            AccountDomain accountDomain = cacheToObject(CacheKeys.ACCOUNT_ID + username);
            if (accountDomain != null) {
                accountDomain.setEnabled(true);
                addToObject(accountDomain, CacheKeys.ACCOUNT_ID + username);
            }
            clearCache();
        } catch (Exception ex) {
            this.logger.error("Cannot active account " + username, ex);
            return false;
        }
        return true;
    }

    public boolean block(String username) {
        try {
            this.logger.debug("Block account " + username + " from database.");

            this.repository.block(username);

            this.logger.debug("Block account " + username + " from cache.");
            AccountDomain accountDomain = cacheToObject(CacheKeys.ACCOUNT_ID + username);
            if (accountDomain != null) {
                accountDomain.setEnabled(false);
                addToObject(accountDomain, CacheKeys.ACCOUNT_ID + username);
            }
            clearCache();
        } catch (Exception ex) {
            this.logger.error("Cannot block account " + username, ex);
            return false;
        }
        return true;
    }

    public boolean login(String username, String lastHostAddress) {
        try {
            this.logger.debug("Log time login account " + username + " to database");

            this.repository.login(username, lastHostAddress, new Date());

            getCache().delete(CacheKeys.ACCOUNT_ID + username);
        } catch (Exception ex) {
            this.logger.error("Cannot update login account " + username, ex);
            return false;
        }
        return true;
    }

    public boolean changePassword(String username, String password) {
        try {
            this.logger.debug("Change password account " + username + " to database");

            this.repository.changePassword(username, password, new Date());

            getCache().delete(CacheKeys.ACCOUNT_ID + username);
        } catch (Exception ex) {
            this.logger.error("Cannot change password account " + username, ex);
            return false;
        }
        return true;
    }

    public boolean mainProfile(long mainProfile, String username) {
        try {
            this.logger.debug("Set main profile account " + username + " to database");

            this.repository.mainProfile(mainProfile, username);

            getCache().delete(CacheKeys.ACCOUNT_ID + username);
        } catch (Exception ex) {
            this.logger.error("Set main profile account " + username, ex);
            return false;
        }
        return true;
    }

    public boolean delete(String username) {
        try {
            this.logger.debug("Delete account " + username + " from database.");

            this.repository.delete(username);

            this.logger.debug("Delete account " + username + " from cache.");
            getCache().delete(CacheKeys.ACCOUNT_ID + username);
            clearCache();
        } catch (Exception ex) {
            this.logger.error("Cannot delete account " + username, ex);
            return false;
        }
        return true;
    }

    public long count() {
        try {
            this.logger.debug("Total account from cache.");
            long count = 0L;
            Object obj = getCache().get(CacheKeys.ACCOUNT_TOTAL);
            if (obj == null) {
                this.logger.debug("Total account from database.");
                count = this.repository.count();
                getCache().set(CacheKeys.ACCOUNT_TOTAL, Long.valueOf(count));
                return count;
            }
            return ((Long) obj).longValue();
        } catch (Exception ex) {
            this.logger.error("Count total account in database error.", ex);
        }
        return 0L;
    }

    private void clearCache() {
        getCache().delete(CacheKeys.ACCOUNT_ALL);
        getCache().delete(CacheKeys.ACCOUNT_ACTIVE);
        getCache().delete(CacheKeys.ACCOUNT_BLOCK);
        getCache().delete(CacheKeys.ACCOUNT_TOTAL);
    }
}
