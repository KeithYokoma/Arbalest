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
import arbalest.rest.net.Binary;
import arbalest.utils.ChannelUtils;
import arbalest.utils.CloseableUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

public class BinaryConverter implements Converter {
    @Override
    public String convert(Object object) {
        throw new UnsupportedOperationException("cannot perform this operation for the binary.");
    }

    @Override
    public <T> void convert(OutputStream out, T object) throws ParseRestNetworkException {
        Binary binary = (Binary) object;
        ReadableByteChannel src = null;
        try {
            src = Channels.newChannel(binary.getInputStream());
            WritableByteChannel dest = Channels.newChannel(out);
            ChannelUtils.copy(src, dest);
        } catch (IOException e) {
            throw new ParseRestNetworkException(e);
        } finally {
            CloseableUtils.close(src);
        }
    }

    @Override
    public <T> T convert(InputStream in, Type type) {
        // TODO
        return null;
    }
}