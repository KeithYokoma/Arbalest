package arbalest.rest.net.converter;

import arbalest.rest.exception.ParseRestNetworkException;
import arbalest.rest.exception.ParseRestResponseException;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

public interface EntityConversionStrategy {
    public String convert(Object object) throws ParseRestNetworkException;
    public void convert(OutputStream out, Object object) throws ParseRestNetworkException;
    public Object convert(InputStream in, Type type) throws ParseRestNetworkException, ParseRestResponseException;
}