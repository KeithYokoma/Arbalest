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
package arbalest;

import android.app.Application;
import android.content.Context;

import arbalest.rest.ParseRest;

import java.util.HashMap;
import java.util.Map;

public final class Arbalest {
    private static boolean sInitialized;

    private Arbalest() {}

    public static final synchronized void initialize(Context context, String applicatinId, String restApiKey) {
        if (sInitialized) {
            return;
        }

        Application application = (Application) context.getApplicationContext();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(ParseRest.HEADER_PARSE_APPLICATION_ID, applicatinId);
        headers.put(ParseRest.HEADER_PARSE_REST_API_KEY, restApiKey);

        ParseRest.initialize(application, headers);

        sInitialized = true;
    }

    public static final synchronized void destroy() {
        if (!sInitialized) {
            return;
        }

        ParseRest.destroy();

        sInitialized = false;
    }
}