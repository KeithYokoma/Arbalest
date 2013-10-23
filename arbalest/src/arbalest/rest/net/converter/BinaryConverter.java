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