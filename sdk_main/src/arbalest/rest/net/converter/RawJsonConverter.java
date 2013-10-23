package arbalest.rest.net.converter;

import arbalest.rest.exception.ParseRestNetworkException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;

public class RawJsonConverter implements Converter {
    @Override
    public String convert(Object object) throws ParseRestNetworkException {
        return object.toString();
    }

    @Override
    public <T> void convert(OutputStream out, T object) throws ParseRestNetworkException {
        try {
            out.write(object.toString().getBytes());
        } catch (IOException e) {
            throw new ParseRestNetworkException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T convert(InputStream in, Type type) throws ParseRestNetworkException {
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();

        try {
            String line = null;
            reader = new BufferedReader(new InputStreamReader(in));
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            return (T) builder.toString();
        } catch (IOException e) {
            throw new ParseRestNetworkException(e);
        }
    }
}