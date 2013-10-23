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
package arbalest.utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * 
 * @author keishin.yokomaku
 * @hide
 */
public final class ChannelUtils {
    public static final void copy(ReadableByteChannel src, WritableByteChannel dest) throws IOException {
        ByteBuffer buf = ByteBuffer.allocateDirect(16 * 1024);

        while (src.read(buf) != -1) {
            buf.flip();
            dest.write(buf);
            buf.compact();
        }

        buf.flip();

        while (buf.hasRemaining()) {
            dest.write(buf);
        }
    }
}