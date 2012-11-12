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
package vn.topmedia.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyValidator {

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String MOBILE_PATTERN = "\\d{10,12}";
    private static final String USERNAME_PATTERN = "^[a-z0-9_-]{3,50}$";

    public static boolean validateEmail(String value) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public static boolean validateUsername(String value) {
        Pattern pattern = Pattern.compile(USERNAME_PATTERN);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public static boolean validateMobile(String value) {
        Pattern pattern = Pattern.compile(MOBILE_PATTERN);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public static boolean validateNullOrEmpty(String value) {
        if (value == null) {
            return true;
        }
        if (value.isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean validateNumberInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (Exception ex) {
        }
        return false;
    }
}
