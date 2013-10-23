package arbalest.rest.net.converter;

import arbalest.rest.exception.ParseRestNetworkException;
import arbalest.rest.exception.ParseRestResponseException;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

public interface Converter {
    public String convert(Object object) throws ParseRestNetworkException;
    public <T> void convert(OutputStream out, T object) throws ParseRestNetworkException;
    public <T> T convert(InputStream in, Type type) throws ParseRestNetworkException, ParseRestResponseException;
}