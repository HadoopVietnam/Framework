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
import com.hadoopvietnam.cache.Cache;
import java.util.Collection;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractCachedService<T> {

    private final Logger logger = LoggerFactory.getLogger(AbstractCachedService.class);
    private int maxListSize = 10000;
    protected final int timeExpiration = 180;
    @Resource(name = "memcachedCache")
    private Cache cache;

    public void setCache(Cache inCache) {
        this.cache = inCache;
    }

    public Cache getCache() {
        return this.cache;
    }

    public int getMaxListSize() {
        return this.maxListSize;
    }

    public void setMaxListSize(int inMaxListSize) {
        this.maxListSize = inMaxListSize;
    }

    protected abstract Collection<T> cacheToList(String paramString);

    protected void addToList(Collection<T> list, String key) {
        try {
            Gson gson = new Gson();
            String data = gson.toJson(list);
            getCache().set(key, data);
        } catch (Exception ex) {
            this.logger.error("AddToList: Cannot add data key " + key + " in cache.", ex);
        }
    }

    protected void addToList(Collection<T> list, String key, int timeExpiration) {
        try {
            Gson gson = new Gson();
            String data = gson.toJson(list);
            getCache().set(key, timeExpiration, data);
        } catch (Exception ex) {
            this.logger.error("AddToList: Cannot add data key " + key + " in cache.", ex);
        }
    }

    protected abstract T cacheToObject(String paramString);

    protected void addToObject(T object, String key) {
        try {
            Gson gson = new Gson();
            String data = gson.toJson(object);
            getCache().set(key, data);
        } catch (Exception ex) {
            this.logger.error("AddToObject: Cannot add data key " + key + " in cache.", ex);
        }
    }

    protected void addToObject(T object, String key, int timeExpiration) {
        try {
            Gson gson = new Gson();
            String data = gson.toJson(object);
            getCache().set(key, timeExpiration, data);
        } catch (Exception ex) {
            this.logger.error("AddToObject: Cannot add data key " + key + " in cache.", ex);
        }
    }
}
