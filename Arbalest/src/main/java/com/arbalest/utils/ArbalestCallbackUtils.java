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
package com.arbalest.utils;

import com.arbalest.callback.ArbalestCallback;
import com.arbalest.http.ArbalestClient;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public final class ArbalestCallbackUtils {
    private ArbalestCallbackUtils() {}

    public static final <T> Type getType(ArbalestCallback<T> callback) {
        if (callback == null) {
            return null;
        }

        return ((ParameterizedType) callback.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0];
    }
}