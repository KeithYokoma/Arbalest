package com.arbalest.http;

import com.arbalest.exception.ArbalestAuthenticationException;
import com.arbalest.exception.ArbalestNetworkException;
import com.arbalest.exception.ArbalestResponseException;

import java.io.Closeable;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public interface IArbalestClient extends Closeable {
    public OutputStream getOutputStream(String method, Map<String, String> headers) throws ArbalestNetworkException;
    public InputStream getInputStream() throws ArbalestNetworkException, ArbalestResponseException, ArbalestAuthenticationException;
    public int getResponseCode() throws ArbalestNetworkException;
}
