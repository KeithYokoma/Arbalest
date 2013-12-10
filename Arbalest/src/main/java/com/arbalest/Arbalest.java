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
package com.arbalest;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;

import com.amalgam.app.ApplicationUtils;
import com.amalgam.io.CloseableUtils;
import com.amalgam.net.ConnectivityManagerUtils;
import com.amalgam.os.ThreadUtils;
import com.arbalest.callback.ArbalestCallback;
import com.arbalest.concurrent.AsyncProcessor;
import com.arbalest.constants.ArbalestConstants;
import com.arbalest.exception.ArbalestAuthenticationException;
import com.arbalest.exception.ArbalestConnectionException;
import com.arbalest.exception.ArbalestException;
import com.arbalest.exception.ArbalestNetworkException;
import com.arbalest.exception.ArbalestResponseException;
import com.arbalest.http.ArbalestClient;
import com.arbalest.http.ArbalestClientFactory;
import com.arbalest.net.Binary;
import com.arbalest.utils.ArbalestCallbackUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class Arbalest {
    public static final String TAG = Arbalest.class.getSimpleName();
    private static volatile Arbalest sInstance;
    private final Application mApplication;
    private final ArbalestContext mArbalestContext;
    private final Handler mHandler;

    protected Arbalest(Application application, ArbalestContext arbalestContext) {
        mApplication = application;
        mArbalestContext = arbalestContext;
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static synchronized void initialize(Application application, String applicationId, String restKey) {
        if (sInstance != null) {
            Log.i(TAG, "Arbalest is already initialized.");
            return;
        }

        ArbalestContext.initialize(new ArbalestClientFactory(application), applicationId, restKey);
        sInstance = new Arbalest(application, ArbalestContext.getInstance());
    }

    public static synchronized Arbalest getInstance() {
        if (sInstance == null) {
            throw new IllegalArgumentException("Arbalest is not yet initialized.");
        }
        return sInstance;
    }

    public static synchronized void destroy() {
        ArbalestContext.destroy();
        sInstance = null;
    }

    public void cancel(int seq) {
        ArbalestClient client = mArbalestContext.getClient(seq);
        if (client != null) {
            CloseableUtils.close(client);
        }
    }

    public void onDestroy(Context context) {
        mArbalestContext.removeCallbackFor(context);
    }

    @SuppressWarnings("unchecked")
    public <T> int post(Context context, String url, Object requestBody, ArbalestCallback<T> callback) {
        return post(context, url, requestBody, callback, (Class<T>) ArbalestCallbackUtils.getType(callback));
    }

    public <T> int post(Context context, final String url, final Object requestBody, ArbalestCallback<T> callback, final Class<?> responseType) {
        final WeakReference<Context> ref = new WeakReference<Context>(context);
        final int seq = mArbalestContext.getAndIncrementCallbackSeq();
        mArbalestContext.putArbalestCallback(context, callback, seq);

        AsyncProcessor.process(new Runnable() {
            @SuppressWarnings("unchecked")
            @Override
            public void run() {
                try {
                    T result = (T) blockingPost(url, requestBody, responseType, seq);
                    mArbalestContext.callOnSuccess(ref, seq, result, mHandler);
                } catch (ArbalestException e) {
                    mArbalestContext.callOnFailure(ref, seq, e, mHandler);
                }
            }
        });

        return seq;
    }

    public <T> T blockingPost(String url, Object requestBody, Class<T> responseType) throws ArbalestConnectionException, ArbalestNetworkException, ArbalestResponseException, ArbalestAuthenticationException {
        return blockingPost(url, requestBody, responseType, -1);
    }

    public <T> T blockingPost(String url, Object requestBody, Class<T> responseType, int seq) throws ArbalestConnectionException, ArbalestNetworkException, ArbalestResponseException, ArbalestAuthenticationException {
        ThreadUtils.checkNotMainThread();
        return sendAndReceive(url, ArbalestConstants.METHOD_POST, ArbalestConstants.DEFAULT_CONTENT_TYPE, requestBody, responseType, mArbalestContext.getBaseHeaders(), seq);
    }

    @SuppressWarnings("unchecked")
    public <T> int postBinary(Context context, String url, Binary binary, ArbalestCallback<T> callback) {
        return postBinary(context, url, binary, callback, (Class<T>) ArbalestCallbackUtils.getType(callback));
    }

    public <T> int postBinary(Context context, final String url, final Binary binary, ArbalestCallback<T> callback, final Class<T> responseType) {
        final WeakReference<Context> ref = new WeakReference<Context>(context);
        final int seq = mArbalestContext.getAndIncrementCallbackSeq();
        mArbalestContext.putArbalestCallback(context, callback, seq);

        AsyncProcessor.process(new Runnable() {
            @Override
            public void run() {
                try {
                    T result = (T) blockingPostBinary(url, binary, responseType, seq);
                    mArbalestContext.callOnSuccess(ref, seq, result, mHandler);
                } catch (ArbalestException e) {
                    mArbalestContext.callOnFailure(ref, seq, e, mHandler);
                }
            }
        });

        return seq;
    }

    public <T> T blockingPostBinary(String url, Binary binary, Class<T> responseType) throws ArbalestConnectionException, ArbalestNetworkException, ArbalestResponseException, ArbalestAuthenticationException {
        return blockingPostBinary(url, binary, responseType, -1);
    }

    public <T> T blockingPostBinary(String url, Binary binary, Class<T> responseType, int seq) throws ArbalestConnectionException, ArbalestNetworkException, ArbalestResponseException, ArbalestAuthenticationException {
        ThreadUtils.checkNotMainThread();
        return sendAndReceive(url, ArbalestConstants.METHOD_POST, binary.getContentType(), binary, responseType, mArbalestContext.getBaseHeaders(), seq);
    }

    @SuppressWarnings("unchecked")
    public <T> int get(Context context, String url, String key, Object requestBody, ArbalestCallback<T> callback) {
        return get(context, url, key, requestBody, callback, (Class<T>) ArbalestCallbackUtils.getType(callback));
    }

    public <T> int get(Context context, final String url, final String key, final Object requestBody, ArbalestCallback<T> callback, final Class<?> responseType) {
        final WeakReference<Context> ref = new WeakReference<Context>(context);
        final int seq = mArbalestContext.getAndIncrementCallbackSeq();
        mArbalestContext.putArbalestCallback(context, callback, seq);

        AsyncProcessor.process(new Runnable() {
            @SuppressWarnings("unchecked")
            @Override
            public void run() {
                try {
                    T result = (T) blockingGet(url, key, requestBody, responseType, seq);
                    mArbalestContext.callOnSuccess(ref, seq, result, mHandler);
                } catch (ArbalestException e) {
                    mArbalestContext.callOnFailure(ref, seq, e, mHandler);
                }
            }
        });

        return seq;
    }

    public <T> T blockingGet(String url, String key, Object requestBody, Class<T> responseType) throws ArbalestConnectionException, ArbalestNetworkException, ArbalestResponseException, ArbalestAuthenticationException {
        return blockingGet(url, key, requestBody, responseType, -1);
    }

    public <T> T blockingGet(String url, String key, Object requestBody, Class<T> responseType, int seq) throws ArbalestConnectionException, ArbalestNetworkException, ArbalestResponseException, ArbalestAuthenticationException {
        ThreadUtils.checkNotMainThread();

        String baseUrl = url;

        if (key != null && requestBody != null) {
            baseUrl += "?" + key + "="
                    + Base64.encodeToString(mArbalestContext.getConversionStrategy(requestBody.getClass()).convert(requestBody).getBytes(), Base64.NO_WRAP);
        }

        return sendAndReceive(baseUrl, ArbalestConstants.METHOD_GET, ArbalestConstants.DEFAULT_CONTENT_TYPE, requestBody, responseType, mArbalestContext.getBaseHeaders(), seq);
    }

    @SuppressWarnings("unchecked")
    public <T> int put(Context context, String url, Object requestBody, ArbalestCallback<T> callback) {
        return put(context, url, requestBody, callback, (Class<T>) ArbalestCallbackUtils.getType(callback));
    }

    public <T> int put(Context context, final String url, final Object requestBody, ArbalestCallback<T> callback, final Class<T> responseType) {
        final WeakReference<Context> ref = new WeakReference<Context>(context);
        final int seq = mArbalestContext.getAndIncrementCallbackSeq();
        mArbalestContext.putArbalestCallback(context, callback, seq);

        AsyncProcessor.process(new Runnable() {
            @Override
            public void run() {
                try {
                    T result = (T) blockingPut(url, requestBody, responseType, seq);
                    mArbalestContext.callOnSuccess(ref, seq, result, mHandler);
                } catch (ArbalestException e) {
                    mArbalestContext.callOnFailure(ref, seq, e, mHandler);
                }
            }
        });

        return seq;
    }

    public <T> T blockingPut(String url, Object requestBody, Class<T> responseType) throws ArbalestConnectionException, ArbalestNetworkException, ArbalestResponseException, ArbalestAuthenticationException {
        return blockingPut(url, requestBody, responseType, -1);
    }

    public <T> T blockingPut(String url, Object requestBody, Class<T> responseType, int seq) throws ArbalestConnectionException, ArbalestNetworkException, ArbalestResponseException, ArbalestAuthenticationException {
        ThreadUtils.checkNotMainThread();
        return sendAndReceive(url, ArbalestConstants.METHOD_PUT, ArbalestConstants.DEFAULT_CONTENT_TYPE, requestBody, responseType, mArbalestContext.getBaseHeaders(), seq);
    }

    @SuppressWarnings("unchecked")
    public <T> int putBinary(Context context, String url, Binary binary, ArbalestCallback<T> callback) {
        return putBinary(context, url, binary, callback, (Class<T>) ArbalestCallbackUtils.getType(callback));
    }

    public <T> int putBinary(Context context, final String url, final Binary binary, ArbalestCallback<T> callback, final Class<T> responseType) {
        final WeakReference<Context> ref = new WeakReference<Context>(context);
        final int seq = mArbalestContext.getAndIncrementCallbackSeq();
        mArbalestContext.putArbalestCallback(context, callback, seq);

        AsyncProcessor.process(new Runnable() {
            @Override
            public void run() {
                try {
                    T result = (T) blockingPutBinary(url, binary, responseType, seq);
                    mArbalestContext.callOnSuccess(ref, seq, result, mHandler);
                } catch (ArbalestException e) {
                    mArbalestContext.callOnFailure(ref, seq, e, mHandler);
                }
            }
        });

        return seq;
    }

    public <T> T blockingPutBinary(String url, Binary binary, Class<T> responseType) throws ArbalestConnectionException, ArbalestNetworkException, ArbalestResponseException, ArbalestAuthenticationException {
        return blockingPutBinary(url, binary, responseType, -1);
    }

    public <T> T blockingPutBinary(String url, Binary binary, Class<T> responseType, int seq) throws ArbalestConnectionException, ArbalestNetworkException, ArbalestResponseException, ArbalestAuthenticationException {
        ThreadUtils.checkNotMainThread();
        return sendAndReceive(url, ArbalestConstants.METHOD_PUT, binary.getContentType(), binary, responseType, mArbalestContext.getBaseHeaders(), seq);
    }

    @SuppressWarnings("unchecked")
    public <T> int delete(Context context, String url, Object requestBody, ArbalestCallback<T> callback) {
        return delete(context, url, requestBody, callback, (Class<T>) ArbalestCallbackUtils.getType(callback));
    }

    public <T> int delete(Context context, final String url, final Object requestBody, ArbalestCallback<T> callback, final Class<T> responseType) {
        final WeakReference<Context> ref = new WeakReference<Context>(context);
        final int seq = mArbalestContext.getAndIncrementCallbackSeq();
        mArbalestContext.putArbalestCallback(context, callback, seq);

        AsyncProcessor.process(new Runnable() {
            @Override
            public void run() {
                try {
                    T result = (T) blockingDelete(url, requestBody, responseType, seq);
                    mArbalestContext.callOnSuccess(ref, seq, result, mHandler);
                } catch (ArbalestException e) {
                    mArbalestContext.callOnFailure(ref, seq, e, mHandler);
                }
            }
        });

        return seq;
    }

    public <T> T blockingDelete(String url, Object requestBody, Class<T> responseType) throws ArbalestConnectionException, ArbalestNetworkException, ArbalestResponseException, ArbalestAuthenticationException {
        return blockingDelete(url, requestBody, responseType, -1);
    }

    public <T> T blockingDelete(String url, Object requestBody, Class<T> responseType, int seq) throws ArbalestConnectionException, ArbalestNetworkException, ArbalestResponseException, ArbalestAuthenticationException {
        ThreadUtils.checkNotMainThread();
        return sendAndReceive(url, ArbalestConstants.METHOD_DELETE, ArbalestConstants.DEFAULT_CONTENT_TYPE, requestBody, responseType, mArbalestContext.getBaseHeaders(), seq);
    }

    private <T> T sendAndReceive(String url, String method, String contentType, Object requestBody, Class<T> responseType, Map<String, String> headers, int seq) throws ArbalestConnectionException, ArbalestNetworkException, ArbalestResponseException, ArbalestAuthenticationException {
        if (!ConnectivityManagerUtils.isNetworkConnected(mApplication)) {
            throw new ArbalestConnectionException("network connection not found.");
        }

        ArbalestClient client = null;

        headers.put("Content-Type", contentType);
        try {
            client = mArbalestContext.createNewClient(url);
            mArbalestContext.addClient(seq, client);
            return sendAndReceive(client, method, requestBody, responseType, headers);
        } finally {
            mArbalestContext.removeClient(seq);
            CloseableUtils.close(client);
        }
    }

    private <T> T sendAndReceive(ArbalestClient client, String method, Object requestBody, Class<T> responseType, Map<String, String> headers) throws ArbalestConnectionException, ArbalestNetworkException, ArbalestResponseException, ArbalestAuthenticationException {
        send(client, method, requestBody, headers);
        return receive(client, responseType);
    }

    private void send(ArbalestClient client, String method, Object requestBody, Map<String, String> headers) throws ArbalestConnectionException, ArbalestNetworkException {
        if (!ConnectivityManagerUtils.isNetworkConnected(mApplication)) {
            throw new ArbalestConnectionException("connection not found.");
        }

        OutputStream out = null;
        try {
            out = client.getOutputStream(method, headers);
            if (out != null && requestBody != null) {
                mArbalestContext.getConversionStrategy(requestBody.getClass()).convert(out, requestBody);
            }
        } finally {
            CloseableUtils.close(out);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T receive(ArbalestClient client, Class<T> responseType) throws ArbalestNetworkException, ArbalestResponseException, ArbalestAuthenticationException {
        InputStream in = null;
        try {
            in = client.getInputStream();
            if (responseType == null) {
                return null;
            }
            return (T) mArbalestContext.getConversionStrategy(responseType).convert(in, responseType);
        } finally {
            CloseableUtils.close(in);
        }
    }
}