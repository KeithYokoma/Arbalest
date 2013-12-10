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
package com.arbalest.http;

import android.app.Application;
import android.util.Log;

import com.amalgam.app.ApplicationUtils;
import com.amalgam.io.DebugInputStream;
import com.amalgam.io.DebugOutputStream;
import com.arbalest.Arbalest;
import com.arbalest.exception.ArbalestAuthenticationException;
import com.arbalest.exception.ArbalestException;
import com.arbalest.exception.ArbalestNetworkException;
import com.arbalest.exception.ArbalestResponseException;
import com.squareup.okhttp.OkHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

public class ArbalestClient implements IArbalestClient {
    public static final String TAG = ArbalestClient.class.getSimpleName();

    private final Application mApplication;
    private final HttpURLConnection mConnection;
    private final OkHttpClient mClient;

    protected ArbalestClient(Application application, OkHttpClient client, String url) throws ArbalestNetworkException {
        mApplication = application;
        mClient = client;
        mConnection = openConnection(url);
    }

    protected ArbalestClient(Application application, OkHttpClient client, HttpURLConnection connection) throws ArbalestNetworkException {
        mApplication = application;
        mClient = client;
        mConnection = connection;
    }

    @Override
    public void close() throws IOException {
        if (mConnection != null) {
            mConnection.disconnect();
        }
    }

    @SuppressWarnings("resource")
    @Override
    public OutputStream getOutputStream(String method, Map<String, String> headers) throws ArbalestNetworkException {
        if (headers != null) {
            Set<Map.Entry<String, String>> entries = headers.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                mConnection.setRequestProperty(entry.getKey(), entry.getValue());
            }
        }

        try {
            if ("GET".equals(method) || "DELETE".equals(method)) {
                mConnection.setDoOutput(false);
                return null;
            }

            mConnection.setDoOutput(true);
            OutputStream out = mConnection.getOutputStream();
            return ApplicationUtils.isDebuggable(mApplication) ? new DebugOutputStream(out) : out;
        } catch (IOException e) {
            throw new ArbalestNetworkException(e);
        }
    }

    @SuppressWarnings("resource")
    @Override
    public InputStream getInputStream() throws ArbalestNetworkException, ArbalestResponseException, ArbalestAuthenticationException{
        try {
            mConnection.connect();
            InputStream in = mConnection.getInputStream();
            return ApplicationUtils.isDebuggable(mApplication) ? new DebugInputStream(in) : in;
        } catch (IOException e) {
            handleErrorResponse();
        }
        return null; // we cannot take care for the input stream
    }

    @Override
    public int getResponseCode() throws ArbalestNetworkException {
        try {
            return mConnection.getResponseCode();
        } catch (IOException e) {
            throw new ArbalestNetworkException(e);
        }
    }

    protected HttpURLConnection openConnection(String url) {
        try {
            HttpURLConnection connection = mClient.open(new URL(url));
            connection.setUseCaches(false);
            connection.setDoInput(true);
            return connection;
        } catch (MalformedURLException e) {
            Log.e(TAG, "malformed url: ", e);
            throw new IllegalArgumentException(e);
        }
    }

    private ArbalestException handleErrorResponse() throws ArbalestAuthenticationException, ArbalestNetworkException, ArbalestResponseException {
        int code = getResponseCode();
        switch (code) {
            case HttpURLConnection.HTTP_UNAUTHORIZED:
                throw new ArbalestAuthenticationException("", code);
            default:
                throw new ArbalestResponseException(getErrorResponse(), code);
        }
    }

    private String getErrorResponse() throws ArbalestNetworkException {
        StringBuilder builder = new StringBuilder();
        InputStream stream = mConnection.getErrorStream();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(stream));
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            return builder.toString();
        } catch (IOException e) {
            throw new ArbalestNetworkException(e);
        }
    }
}