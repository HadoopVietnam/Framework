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
package com.hadoopvietnam.persistence.domain.statistics;

import java.io.Serializable;
import java.util.Date;

public class StatisticDomain
        implements Serializable {

    private long id;
    private String IP;
    private String session;
    private String browser;
    private String version;
    private String system;
    private int count;
    private Date created;
    private long refererId;
    private String device;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIP() {
        return this.IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getSession() {
        return this.session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getBrowser() {
        return this.browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSystem() {
        return this.system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Date getCreated() {
        return this.created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public long getRefererId() {
        return this.refererId;
    }

    public void setRefererId(long refererId) {
        this.refererId = refererId;
    }

    public String getDevice() {
        return this.device;
    }

    public void setDevice(String device) {
        this.device = device;
    }
}