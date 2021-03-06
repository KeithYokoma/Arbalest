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

import android.content.Entity;

import com.arbalest.exception.ArbalestNetworkException;
import com.arbalest.exception.ArbalestResponseException;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

/**
 * Policy enumeration for converting entity object with the serialized form.
 * @author keishin.yokomaku
 */
public enum EntityConversionPolicy implements EntityConversionStrategy {
    DEFAULT_GSON(new GsonConverter()) {

        @Override
        public String convert(Object object) throws ArbalestNetworkException {
            return getConverter().convert(object);
        }

        @Override
        public void convert(OutputStream out, Object object) throws ArbalestNetworkException {
            getConverter().convert(out, object);
        }

        @Override
        public Object convert(InputStream in, Type type) throws ArbalestNetworkException, ArbalestResponseException {
            return getConverter().convert(in, type);
        }
    },
    RAW_JSON(new RawJsonConverter()) {

        @Override
        public String convert(Object object) throws ArbalestNetworkException {
            return getConverter().convert(object);
        }

        @Override
        public void convert(OutputStream out, Object object) throws ArbalestNetworkException {
            getConverter().convert(out, object);
        }

        @Override
        public Object convert(InputStream in, Type type) throws ArbalestNetworkException, ArbalestResponseException {
            return getConverter().convert(in, type);
        }
    },
    BINARY(new BinaryConverter()) {

        @Override
        public String convert(Object object) throws ArbalestNetworkException {
            return getConverter().convert(object);
        }

        @Override
        public void convert(OutputStream out, Object object) throws ArbalestNetworkException {
            getConverter().convert(out, object);
        }

        @Override
        public Object convert(InputStream in, Type type) throws ArbalestNetworkException, ArbalestResponseException  {
            return getConverter().convert(in, type);
        }
    };

    private final Converter mConverter;

    private EntityConversionPolicy(final Converter converter) {
        mConverter = converter;
    }

    protected Converter getConverter() {
        return mConverter;
    }
}