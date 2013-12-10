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
package com.arbalest.net.json;

import android.util.Log;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public enum ArbalestFieldNamingStrategy implements FieldNamingStrategy {
    PARSE_API_CAMEL_CASE() {
        @Override
        protected String fieldNameToJsonName(String fieldName) {
            return fieldName;
        }
    };

    private static final String TAG = ArbalestFieldNamingStrategy.class.getSimpleName();
    private static final String MEMBER_NAME_PREFIX = "m";

    @Override
    public String translateName(Field field) {
        StringBuilder fieldName = new StringBuilder(field.getName());
        int fieldModifiers = field.getModifiers();

        if ((!field.getDeclaringClass().isMemberClass()) && (!Modifier.isStatic(fieldModifiers))
                && (!Modifier.isPublic(fieldModifiers)) && field.getAnnotation(SerializedName.class) == null) {
            if ((fieldName.length() >= MEMBER_NAME_PREFIX.length())
                    && MEMBER_NAME_PREFIX.equals(fieldName.substring(0, MEMBER_NAME_PREFIX.length()))) {
                fieldName.delete(0, MEMBER_NAME_PREFIX.length());

                if (fieldName.length() > 0)
                    fieldName.setCharAt(0, Character.toLowerCase(fieldName.charAt(0)));
            } else {
                Log.i(TAG, "The following field naming doesn't follow Arbalest guidelines : " + field);
            }
        }

        return fieldNameToJsonName(fieldName.toString());
    }

    protected abstract String fieldNameToJsonName(String fieldName);
}
