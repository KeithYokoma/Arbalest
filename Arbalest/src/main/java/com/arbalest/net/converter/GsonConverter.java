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
package com.arbalest.net.converter;

import com.amalgam.io.CloseableUtils;
import com.arbalest.exception.ArbalestNetworkException;
import com.arbalest.exception.ArbalestResponseException;
import com.arbalest.net.json.ArbalestFieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GsonConverter implements Converter {
    private static final String DEFAULT_CHARSET = "UTF-8";
    private final GsonBuilder mDefaultGsonBuilder = new GsonBuilder()
            .excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT)
            .setFieldNamingStrategy(ArbalestFieldNamingStrategy.PARSE_API_CAMEL_CASE);

    @Override
    public String convert(Object object) {
        return mDefaultGsonBuilder.create().toJson(object);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void convert(OutputStream out, T object) throws ArbalestNetworkException {
        if (object.getClass().isArray()) {
            convertArray(out, (T[]) object);
        } else {
            convertObject(out, object);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T convert(InputStream in, Type type) throws ArbalestNetworkException, ArbalestResponseException {
        Class<?> clazz;
        if (type instanceof ParameterizedType) {
            clazz = (Class<?>) ((ParameterizedType) type).getRawType();
        } else {
            clazz = (Class<?>) type;
        }
        return (T) (clazz.isArray() ? convertArray(in, clazz) : convertObject(in, type));
    }

    private <T> void convertObject(OutputStream out, T object) throws ArbalestNetworkException {
        JsonWriter writer = null;
        try {
            writer = new JsonWriter(new OutputStreamWriter(out, DEFAULT_CHARSET));
            Gson gson = mDefaultGsonBuilder.create();
            gson.toJson(object, object.getClass(), writer);
            writer.flush();
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            throw new ArbalestNetworkException(e);
        } finally {
            CloseableUtils.close(writer);
        }
    }

    private Object convertObject(InputStream in, Type type) throws ArbalestNetworkException, ArbalestResponseException {
        JsonReader reader = null;
        try {
            reader = new JsonReader(new InputStreamReader(in, DEFAULT_CHARSET));
            Gson gson = mDefaultGsonBuilder.create();
            return gson.fromJson(reader, type);
        } catch (JsonSyntaxException e) {
            throw new ArbalestResponseException(e);
        } catch (IOException e) {
            throw new ArbalestNetworkException(e);
        } finally {
            CloseableUtils.close(reader);
        }
    }

    private void convertArray(OutputStream out, Object[] objects) throws ArbalestNetworkException {
        JsonWriter writer = null;
        try {
            writer = new JsonWriter(new OutputStreamWriter(out, DEFAULT_CHARSET));
            writer.beginArray();

            Gson gson = mDefaultGsonBuilder.create();
            for (Object object : objects) {
                gson.toJson(object, object.getClass(), writer);
            }

            writer.endArray();
            writer.flush();
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        } catch (IOException e) {
            throw new ArbalestNetworkException(e);
        } finally {
            CloseableUtils.close(writer);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T convertArray(InputStream in, Class<T> clazz) throws ArbalestNetworkException, ArbalestResponseException {
        JsonReader reader = null;
        try {
            reader = new JsonReader(new InputStreamReader(in, DEFAULT_CHARSET));
            Class<?> type = clazz.getComponentType();
            Gson gson = mDefaultGsonBuilder.create();
            List<Object> list = new ArrayList<Object>();

            reader.beginArray();

            while (reader.hasNext()) {
                list.add(gson.fromJson(reader, type));
            }

            reader.endArray();

            return (T) list.toArray((T[]) Array.newInstance(type, list.size()));
        } catch (JsonSyntaxException e) {
            throw new ArbalestResponseException(e);
        } catch (IOException e) {
            throw new ArbalestNetworkException(e);
        } finally {
            CloseableUtils.close(reader);
        }
    }
}