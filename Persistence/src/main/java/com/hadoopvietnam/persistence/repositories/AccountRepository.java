/*
 * Copyright 2012 Hadoop Vietnam <admin@hadoopvietnam.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hadoopvietnam.persistence.repositories;

import com.hadoopvietnam.persistence.domain.AccountDomain;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AccountRepository {

    public List<AccountDomain> findAll();

    public List<AccountDomain> findByLimit(@Param("orderBy") String paramString, @Param("start") int paramInt1, @Param("end") int paramInt2);

    public List<AccountDomain> findByEnable(@Param("orderBy") String paramString, @Param("enabled") boolean paramBoolean, @Param("start") int paramInt1, @Param("end") int paramInt2);

    public AccountDomain findByUsernameAndPassword(@Param("username") String paramString1, @Param("password") String paramString2);

    public AccountDomain findById(@Param("id") long paramLong);

    public AccountDomain findByProfileId(@Param("profileId") long paramLong);

    public AccountDomain findByUsername(@Param("username") String paramString);

    public AccountDomain findByEmail(@Param("email") String paramString);

    public AccountDomain findByMobile(@Param("mobile") String paramString);

    public AccountDomain findByUsernameOrEmail(@Param("username") String paramString1, @Param("email") String paramString2);

    public long count();

    public void save(AccountDomain paramAccountDomain);

    public void active(@Param("username") String paramString);

    public void block(@Param("username") String paramString);

    public void mainProfile(@Param("mainProfile") long paramLong, @Param("username") String paramString);

    public void login(@Param("username") String paramString1, @Param("lastHostAddress") String paramString2, @Param("lastLoginTime") Date paramDate);

    public void changePassword(@Param("username") String paramString1, @Param("password") String paramString2, @Param("lastPasswordChangeTime") Date paramDate);

    public void delete(@Param("username") String paramString);
}