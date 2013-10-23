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