package arbalest.rest.http;

import arbalest.rest.exception.ParseRestAuthorizationException;
import arbalest.rest.exception.ParseRestNetworkException;
import arbalest.rest.exception.ParseRestResponseException;

import java.io.Closeable;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public interface ParseRestClient extends Closeable {
    public OutputStream getOutputStream(String method, Map<String, String> headers) throws ParseRestNetworkException;
    public InputStream getInputStream() throws ParseRestNetworkException, ParseRestResponseException, ParseRestAuthorizationException;
    public int getResponseCode() throws ParseRestNetworkException;
}
