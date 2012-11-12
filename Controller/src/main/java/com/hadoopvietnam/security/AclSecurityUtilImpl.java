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

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class AclSecurityUtilImpl
        implements AclSecurityUtil {

    private static Logger logger = LoggerFactory.getLogger(AclSecurityUtil.class);
    private MutableAclService mutableAclService;

    @Override
    public void addPermission(AbstractSecureObject secureObject, Permission permission, Class clazz) {
        addPermission(secureObject, new PrincipalSid(getUsername()), permission, clazz);
    }

    @Override
    public void addPermission(AbstractSecureObject securedObject, Sid recipient, Permission permission, Class clazz) {
        ObjectIdentity oid = new ObjectIdentityImpl(clazz.getCanonicalName(), securedObject.getId());
        MutableAcl acl;
        try {
            acl = (MutableAcl) this.mutableAclService.readAclById(oid);
        } catch (NotFoundException nfe) {
            acl = this.mutableAclService.createAcl(oid);
        }

        acl.insertAce(acl.getEntries().size(), permission, recipient, true);
        this.mutableAclService.updateAcl(acl);

        if (logger.isDebugEnabled()) {
            logger.debug("Added permission " + permission + " for Sid " + recipient + " securedObject " + securedObject);
        }
    }

    @Override
    public void deletePermission(AbstractSecureObject securedObject, Sid recipient, Permission permission, Class clazz) {
        ObjectIdentity oid = new ObjectIdentityImpl(clazz.getCanonicalName(), securedObject.getId());
        MutableAcl acl = (MutableAcl) this.mutableAclService.readAclById(oid);

        List entries = acl.getEntries();

        for (int i = 0; i < entries.size(); i++) {
            if ((((AccessControlEntry) entries.get(i)).getSid().equals(recipient)) && (((AccessControlEntry) entries.get(i)).getPermission().equals(permission))) {
                acl.deleteAce(i);
            }
        }

        this.mutableAclService.updateAcl(acl);

        if (logger.isDebugEnabled()) {
            logger.debug("Deleted securedObject " + securedObject + " ACL permissions for recipient " + recipient);
        }
    }

    protected String getUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if ((auth.getPrincipal() instanceof UserDetails)) {
            return ((UserDetails) auth.getPrincipal()).getUsername();
        }
        return auth.getPrincipal().toString();
    }

    public void setMutableAclService(MutableAclService mutableAclService) {
        this.mutableAclService = mutableAclService;
    }

    @Override
    public void deletePermission(AbstractSecureObject securedObject, Class clazz) {
        ObjectIdentity oid = new ObjectIdentityImpl(clazz.getCanonicalName(), securedObject.getId());
        this.mutableAclService.deleteAcl(oid, false);
    }
}
