/*
 * Copyright (C) 2013 KeithYokoma. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package arbalest.rest.constant;

public final class ParseApiConstants {
    private static final String PARSE_API_URL = "https://api.parse.com/";
    private static final int PARSE_API_VERSION = 1;

    // https://api.parse.com/1/
    public static final String PARSE_API_URL_BASE = PARSE_API_URL + PARSE_API_VERSION + "/";
    public static final String PARSE_API_OBJECTS_URL = PARSE_API_URL_BASE + "classes/";
    public static final String PARSE_API_USERS_URL = PARSE_API_URL_BASE + "users/";
    public static final String PARSE_API_LOGIN_URL = PARSE_API_URL_BASE + "login/";
    public static final String PARSE_API_ROLES_URL = PARSE_API_URL_BASE + "roles/";
    public static final String PARSE_API_FILES_URL = PARSE_API_URL_BASE + "files/";
    public static final String PARSE_API_PUSH_NOTIFICATION_URL = PARSE_API_URL_BASE + "push/";
    public static final String PARSE_API_INSTALLATION_URL = PARSE_API_URL_BASE + "installations/";
    public static final String PARSE_API_CLOUD_FUNCTIONS_URL = PARSE_API_URL_BASE + "functions/";
}