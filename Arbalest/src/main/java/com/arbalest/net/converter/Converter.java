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

import com.arbalest.exception.ArbalestNetworkException;
import com.arbalest.exception.ArbalestResponseException;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

public interface Converter {
    public String convert(Object object) throws ArbalestNetworkException;
    public <T> void convert(OutputStream out, T object) throws ArbalestNetworkException;
    public <T> T convert(InputStream in, Type type) throws ArbalestNetworkException, ArbalestResponseException;
}