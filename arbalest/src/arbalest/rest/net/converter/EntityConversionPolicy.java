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
package arbalest.rest.net.converter;

import arbalest.rest.exception.ParseRestNetworkException;
import arbalest.rest.exception.ParseRestResponseException;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

/**
 * Policy enumeration for converting entity object with the serialized form.
 * @author keishin.yokomaku
 */
public enum EntityConversionPolicy implements EntityConversionStrategy {
    DEFAULT_GSON() {
        private GsonConverter mConverter = new GsonConverter();

        @Override
        public String convert(Object object) throws ParseRestNetworkException {
            return mConverter.convert(object);
        }

        @Override
        public void convert(OutputStream out, Object object) throws ParseRestNetworkException {
            mConverter.convert(out, object);
        }

        @Override
        public Object convert(InputStream in, Type type) throws ParseRestNetworkException, ParseRestResponseException {
            return mConverter.convert(in, type);
        }
    },
    RAW_JSON() {
        private RawJsonConverter mConverter = new RawJsonConverter();

        @Override
        public String convert(Object object) throws ParseRestNetworkException {
            return mConverter.convert(object);
        }

        @Override
        public void convert(OutputStream out, Object object) throws ParseRestNetworkException {
            mConverter.convert(out, object);
        }

        @Override
        public Object convert(InputStream in, Type type) throws ParseRestNetworkException, ParseRestResponseException {
            return mConverter.convert(in, type);
        }
    },
    BINARY() {
        private BinaryConverter mConverter = new BinaryConverter();

        @Override
        public String convert(Object object) throws ParseRestNetworkException {
            return mConverter.convert(object);
        }

        @Override
        public void convert(OutputStream out, Object object) throws ParseRestNetworkException {
            mConverter.convert(out, object);
        }

        @Override
        public Object convert(InputStream in, Type type) throws ParseRestNetworkException, ParseRestResponseException  {
            return mConverter.convert(in, type);
        }
    };
}