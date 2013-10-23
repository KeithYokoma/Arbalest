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
package arbalest.rest;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;

import arbalest.rest.callback.ParseCallback;
import arbalest.rest.concurrent.AsyncProcessor;
import arbalest.rest.exception.ParseRestAuthorizationException;
import arbalest.rest.exception.ParseRestConnectionException;
import arbalest.rest.exception.ParseRestException;
import arbalest.rest.exception.ParseRestNetworkException;
import arbalest.rest.exception.ParseRestResponseException;
import arbalest.rest.http.HttpUrlConnectionParseRestClientFactory;
import arbalest.rest.http.ParseRestClient;
import arbalest.rest.net.Binary;
import arbalest.utils.CloseableUtils;
import arbalest.utils.ConnectivtiyManagerUtils;
import arbalest.utils.ParseCallbackUtils;
import arbalest.utils.ThreadUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * @author keishin.yokomaku
 *
 */
public class ParseRest {
    public static final String TAG = ParseRest.class.getSimpleName();
    public static final String HEADER_PARSE_APPLICATION_ID = "X-Parse-Application-Id";
    public static final String HEADER_PARSE_REST_API_KEY = "X-Parse-REST-API-Key";
    private static final String DEFAULT_CONTENT_TYPE = "application/json";
    private static final String METHOD_DELETE = "DELETE";
    private static final String METHOD_PUT = "PUT";
    private static final String METHOD_GET = "GET";
    private static final String METHOD_POST = "POST";
    private static volatile ParseRest sInstance;
    private final Application mApplication;
    private final ParseRestContext mRestContext;
    private final Handler mHandler;

    /**
     * 
     * @param application
     * @param restContext
     */
    protected ParseRest(Application application, ParseRestContext restContext) {
        mApplication = application;
        mRestContext = restContext;
        mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 
     * @param app
     * @param headers
     */
    public static synchronized final void initialize(Application app, Map<String, String> headers) {
        if (sInstance != null) {
            Log.v(TAG, "ParseRest is already initialized.");
            return;
        }

        ParseRestContext.initialize(new HttpUrlConnectionParseRestClientFactory(app), headers);
        sInstance = new ParseRest(app, ParseRestContext.getInstance());
    }

    /**
     * 
     * @return
     */
    public static final ParseRest getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException("ParseRest is not yet initialized.");
        }

        return sInstance;
    }

    /**
     * Terminate whole life-cycle of this instance.
     */
    public static synchronized final void destroy() {
        ParseRestContext.destroy();
        sInstance = null;
    }

    /**
     * 
     * @param seq
     */
    public void cancel(int seq) {
        ParseRestClient client = mRestContext.getClient(seq);
        if (client != null) {
            CloseableUtils.close(client);
        }
    }

    public void onDestroy(Context context) {
        mRestContext.removeCallbackFor(context);
    }

    /**
     * 
     * @param context
     * @param url
     * @param requestBody
     * @param callback
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> int post(Context context, String url, Object requestBody, ParseCallback<T> callback) {
        return post(context, url, requestBody, callback, (Class<T>) ParseCallbackUtils.getType(callback));
    }

    /**
     * 
     * @param context
     * @param url
     * @param requestBody
     * @param callback
     * @param responseType
     * @return
     */
    public <T> int post(Context context, final String url, final Object requestBody, ParseCallback<T> callback, final Class<?> responseType) {
        final WeakReference<Context> ref = new WeakReference<Context>(context);
        final int seq = mRestContext.getAndIncrementCallbackSeq();
        mRestContext.putParseCallback(context, callback, seq);

        AsyncProcessor.process(new Runnable() {
            @SuppressWarnings("unchecked")
            @Override
            public void run() {
                try {
                    T result = (T) blockingPost(url, requestBody, responseType, seq);
                    mRestContext.callOnSuccess(ref, seq, result, mHandler);
                } catch (ParseRestException e) {
                    mRestContext.callOnFailure(ref, seq, e, mHandler);
                }
            }
        });

        return seq;
    }

    /**
     * 
     * @param url
     * @param requestBody
     * @param responseType
     * @return
     * @throws ParseRestConnectionException
     * @throws ParseRestNetworkException
     * @throws ParseRestResponseException
     * @throws ParseRestAuthorizationException
     */
    public <T> T blockingPost(String url, Object requestBody, Class<T> responseType) throws ParseRestConnectionException, ParseRestNetworkException, ParseRestResponseException, ParseRestAuthorizationException {
        return blockingPost(url, requestBody, responseType, -1);
    }

    /**
     * 
     * @param url
     * @param requestBody
     * @param responseType
     * @param seq
     * @return
     * @throws ParseRestConnectionException
     * @throws ParseRestNetworkException
     * @throws ParseRestResponseException
     * @throws ParseRestAuthorizationException
     */
    public <T> T blockingPost(String url, Object requestBody, Class<T> responseType, int seq) throws ParseRestConnectionException, ParseRestNetworkException, ParseRestResponseException, ParseRestAuthorizationException {
        ThreadUtils.checkNotMainThread();
        return sendAndReceive(url, METHOD_POST, DEFAULT_CONTENT_TYPE, requestBody, responseType, mRestContext.getBaseHeaders(), seq);
    }

    /**
     * 
     * @param context
     * @param url
     * @param binary
     * @param callback
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> int postBinary(Context context, String url, Binary binary, ParseCallback<T> callback) {
        return postBinary(context, url, binary, callback, (Class<T>) ParseCallbackUtils.getType(callback));
    }

    /**
     * 
     * @param context
     * @param url
     * @param binary
     * @param callback
     * @param responseType
     * @return
     */
    public <T> int postBinary(Context context, final String url, final Binary binary, ParseCallback<T> callback, final Class<T> responseType) {
        final WeakReference<Context> ref = new WeakReference<Context>(context);
        final int seq = mRestContext.getAndIncrementCallbackSeq();
        mRestContext.putParseCallback(context, callback, seq);

        AsyncProcessor.process(new Runnable() {
            @Override
            public void run() {
                try {
                    T result = (T) blockingPostBinary(url, binary, responseType, seq);
                    mRestContext.callOnSuccess(ref, seq, result, mHandler);
                } catch (ParseRestException e) {
                    mRestContext.callOnFailure(ref, seq, e, mHandler);
                }
            }
        });

        return seq;
    }

    /**
     * 
     * @param url
     * @param binary
     * @param responseType
     * @return
     * @throws ParseRestConnectionException
     * @throws ParseRestNetworkException
     * @throws ParseRestResponseException
     * @throws ParseRestAuthorizationException
     */
    public <T> T blockingPostBinary(String url, Binary binary, Class<T> responseType) throws ParseRestConnectionException, ParseRestNetworkException, ParseRestResponseException, ParseRestAuthorizationException {
        return blockingPostBinary(url, binary, responseType, -1);
    }

    /**
     * 
     * @param url
     * @param binary
     * @param responseType
     * @param seq
     * @return
     * @throws ParseRestConnectionException
     * @throws ParseRestNetworkException
     * @throws ParseRestResponseException
     * @throws ParseRestAuthorizationException
     */
    public <T> T blockingPostBinary(String url, Binary binary, Class<T> responseType, int seq) throws ParseRestConnectionException, ParseRestNetworkException, ParseRestResponseException, ParseRestAuthorizationException {
        ThreadUtils.checkNotMainThread();
        return sendAndReceive(url, METHOD_POST, binary.getContentType(), binary, responseType, mRestContext.getBaseHeaders(), seq);
    }

    @SuppressWarnings("unchecked")
    public <T> int get(Context context, String url, String key, Object requestBody, ParseCallback<T> callback) {
        return get(context, url, key, requestBody, callback, (Class<T>) ParseCallbackUtils.getType(callback));
    }

    public <T> int get(Context context, final String url, final String key, final Object requestBody, ParseCallback<T> callback, final Class<?> responseType) {
        final WeakReference<Context> ref = new WeakReference<Context>(context);
        final int seq = mRestContext.getAndIncrementCallbackSeq();
        mRestContext.putParseCallback(context, callback, seq);

        AsyncProcessor.process(new Runnable() {
            @SuppressWarnings("unchecked")
            @Override
            public void run() {
                try {
                    T result = (T) blockingGet(url, key, requestBody, responseType, seq);
                    mRestContext.callOnSuccess(ref, seq, result, mHandler);
                } catch (ParseRestException e) {
                    mRestContext.callOnFailure(ref, seq, e, mHandler);
                }
            }
        });

        return seq;
    }
    /**
     * 
     * @param url
     * @param key
     * @param requestBody
     * @param responseType
     * @return
     * @throws ParseRestConnectionException
     * @throws ParseRestNetworkException
     * @throws ParseRestResponseException
     * @throws ParseRestAuthorizationException
     */
    public <T> T blockingGet(String url, String key, Object requestBody, Class<T> responseType) throws ParseRestConnectionException, ParseRestNetworkException, ParseRestResponseException, ParseRestAuthorizationException {
        return blockingGet(url, key, requestBody, responseType, -1);
    }

    /**
     * 
     * @param url
     * @param key
     * @param requestBody
     * @param responseType
     * @param seq
     * @return
     * @throws ParseRestConnectionException
     * @throws ParseRestNetworkException
     * @throws ParseRestResponseException
     * @throws ParseRestAuthorizationException
     */
    public <T> T blockingGet(String url, String key, Object requestBody, Class<T> responseType, int seq) throws ParseRestConnectionException, ParseRestNetworkException, ParseRestResponseException, ParseRestAuthorizationException {
        ThreadUtils.checkNotMainThread();

        String baseUrl = url;

        if (key != null && requestBody != null) {
            baseUrl += "?" + key + "="
                    + Base64.encodeToString(mRestContext.getConversionStrategy(requestBody.getClass()).convert(requestBody).getBytes(), Base64.NO_WRAP);
        }

        return sendAndReceive(baseUrl, METHOD_GET, DEFAULT_CONTENT_TYPE, requestBody, responseType, mRestContext.getBaseHeaders(), seq);
    }

    @SuppressWarnings("unchecked")
    public <T> int put(Context context, String url, Object requestBody, ParseCallback<T> callback) {
        return put(context, url, requestBody, callback, (Class<T>) ParseCallbackUtils.getType(callback));
    }

    public <T> int put(Context context, final String url, final Object requestBody, ParseCallback<T> callback, final Class<T> responseType) {
        final WeakReference<Context> ref = new WeakReference<Context>(context);
        final int seq = mRestContext.getAndIncrementCallbackSeq();
        mRestContext.putParseCallback(context, callback, seq);

        AsyncProcessor.process(new Runnable() {
            @Override
            public void run() {
                try {
                    T result = (T) blockingPut(url, requestBody, responseType, seq);
                    mRestContext.callOnSuccess(ref, seq, result, mHandler);
                } catch (ParseRestException e) {
                    mRestContext.callOnFailure(ref, seq, e, mHandler);
                }
            }
        });

        return seq;
    }

    /**
     * 
     * @param url
     * @param requestBody
     * @param responseType
     * @return
     * @throws ParseRestConnectionException
     * @throws ParseRestNetworkException
     * @throws ParseRestResponseException
     * @throws ParseRestAuthorizationException
     */
    public <T> T blockingPut(String url, Object requestBody, Class<T> responseType) throws ParseRestConnectionException, ParseRestNetworkException, ParseRestResponseException, ParseRestAuthorizationException {
        return blockingPut(url, requestBody, responseType, -1);
    }

    /**
     * 
     * @param url
     * @param requestBody
     * @param responseType
     * @param seq
     * @return
     * @throws ParseRestConnectionException
     * @throws ParseRestNetworkException
     * @throws ParseRestResponseException
     * @throws ParseRestAuthorizationException
     */
    public <T> T blockingPut(String url, Object requestBody, Class<T> responseType, int seq) throws ParseRestConnectionException, ParseRestNetworkException, ParseRestResponseException, ParseRestAuthorizationException {
        ThreadUtils.checkNotMainThread();
        return sendAndReceive(url, METHOD_PUT, DEFAULT_CONTENT_TYPE, requestBody, responseType, mRestContext.getBaseHeaders(), seq);
    }

    @SuppressWarnings("unchecked")
    public <T> int putBinary(Context context, String url, Binary binary, ParseCallback<T> callback) {
        return putBinary(context, url, binary, callback, (Class<T>) ParseCallbackUtils.getType(callback));
    }

    public <T> int putBinary(Context context, final String url, final Binary binary, ParseCallback<T> callback, final Class<T> responseType) {
        final WeakReference<Context> ref = new WeakReference<Context>(context);
        final int seq = mRestContext.getAndIncrementCallbackSeq();
        mRestContext.putParseCallback(context, callback, seq);

        AsyncProcessor.process(new Runnable() {
            @Override
            public void run() {
                try {
                    T result = (T) blockingPutBinary(url, binary, responseType, seq);
                    mRestContext.callOnSuccess(ref, seq, result, mHandler);
                } catch (ParseRestException e) {
                    mRestContext.callOnFailure(ref, seq, e, mHandler);
                }
            }
        });

        return seq;
    }

    /**
     * 
     * @param url
     * @param binary
     * @param responseType
     * @return
     * @throws ParseRestConnectionException
     * @throws ParseRestNetworkException
     * @throws ParseRestResponseException
     * @throws ParseRestAuthorizationException
     */
    public <T> T blockingPutBinary(String url, Binary binary, Class<T> responseType) throws ParseRestConnectionException, ParseRestNetworkException, ParseRestResponseException, ParseRestAuthorizationException {
        return blockingPutBinary(url, binary, responseType, -1);
    }

    /**
     * 
     * @param url
     * @param binary
     * @param responseType
     * @param seq
     * @return
     * @throws ParseRestConnectionException
     * @throws ParseRestNetworkException
     * @throws ParseRestResponseException
     * @throws ParseRestAuthorizationException
     */
    public <T> T blockingPutBinary(String url, Binary binary, Class<T> responseType, int seq) throws ParseRestConnectionException, ParseRestNetworkException, ParseRestResponseException, ParseRestAuthorizationException {
        ThreadUtils.checkNotMainThread();
        return sendAndReceive(url, METHOD_PUT, binary.getContentType(), binary, responseType, mRestContext.getBaseHeaders(), seq);
    }

    @SuppressWarnings("unchecked")
    public <T> int delete(Context context, String url, Object requestBody, ParseCallback<T> callback) {
        return delete(context, url, requestBody, callback, (Class<T>) ParseCallbackUtils.getType(callback));
    }

    public <T> int delete(Context context, final String url, final Object requestBody, ParseCallback<T> callback, final Class<T> responseType) {
        final WeakReference<Context> ref = new WeakReference<Context>(context);
        final int seq = mRestContext.getAndIncrementCallbackSeq();
        mRestContext.putParseCallback(context, callback, seq);

        AsyncProcessor.process(new Runnable() {
            @Override
            public void run() {
                try {
                    T result = (T) blockingDelete(url, requestBody, responseType, seq);
                    mRestContext.callOnSuccess(ref, seq, result, mHandler);
                } catch (ParseRestException e) {
                    mRestContext.callOnFailure(ref, seq, e, mHandler);
                }
            }
        });

        return seq;
    }

    /**
     * 
     * @param url
     * @param requestBody
     * @param responseType
     * @return
     * @throws ParseRestConnectionException
     * @throws ParseRestNetworkException
     * @throws ParseRestResponseException
     * @throws ParseRestAuthorizationException
     */
    public <T> T blockingDelete(String url, Object requestBody, Class<T> responseType) throws ParseRestConnectionException, ParseRestNetworkException, ParseRestResponseException, ParseRestAuthorizationException {
        return blockingDelete(url, requestBody, responseType, -1);
    }

    /**
     * 
     * @param url
     * @param requestBody
     * @param responseType
     * @param seq
     * @return
     * @throws ParseRestConnectionException
     * @throws ParseRestNetworkException
     * @throws ParseRestResponseException
     * @throws ParseRestAuthorizationException
     */
    public <T> T blockingDelete(String url, Object requestBody, Class<T> responseType, int seq) throws ParseRestConnectionException, ParseRestNetworkException, ParseRestResponseException, ParseRestAuthorizationException {
        ThreadUtils.checkNotMainThread();
        return sendAndReceive(url, METHOD_DELETE, DEFAULT_CONTENT_TYPE, requestBody, responseType, mRestContext.getBaseHeaders(), seq);
    }

    /**
     * 
     * @param url
     * @param method
     * @param contentType
     * @param requestBody
     * @param responseType
     * @param headers
     * @param seq
     * @return
     * @throws ParseRestConnectionException
     * @throws ParseRestNetworkException
     * @throws ParseRestResponseException
     * @throws ParseRestAuthorizationException
     */
    private <T> T sendAndReceive(String url, String method, String contentType, Object requestBody, Class<T> responseType, Map<String, String> headers, int seq) throws ParseRestConnectionException, ParseRestNetworkException, ParseRestResponseException, ParseRestAuthorizationException {
        if (!ConnectivtiyManagerUtils.isNetworkConnected(mApplication)) {
            throw new ParseRestConnectionException("network connection not found.");
        }

        ParseRestClient client = null;

        headers.put("Content-Type", contentType);
        try {
            client = mRestContext.createNewClient(url);
            mRestContext.addClient(seq, client);
            return sendAndReceive(client, method, requestBody, responseType, headers);
        } finally {
            mRestContext.removeClient(seq);
            CloseableUtils.close(client);
        }
    }

    /**
     * 
     * @param client
     * @param method
     * @param requestBody
     * @param responseType
     * @param headers
     * @return
     * @throws ParseRestConnectionException
     * @throws ParseRestNetworkException
     * @throws ParseRestResponseException
     * @throws ParseRestAuthorizationException
     */
    private <T> T sendAndReceive(ParseRestClient client, String method, Object requestBody, Class<T> responseType, Map<String, String> headers) throws ParseRestConnectionException, ParseRestNetworkException, ParseRestResponseException, ParseRestAuthorizationException {
        send(client, method, requestBody, headers);
        return receive(client, responseType);
    }

    /**
     * 
     * @param client
     * @param method
     * @param requestBody
     * @param headers
     * @throws ParseRestConnectionException
     * @throws ParseRestNetworkException
     */
    private void send(ParseRestClient client, String method, Object requestBody, Map<String, String> headers) throws ParseRestConnectionException, ParseRestNetworkException {
        if (!ConnectivtiyManagerUtils.isNetworkConnected(mApplication)) {
            throw new ParseRestConnectionException("connection not found.");
        }

        OutputStream out = null;
        try {
            out = client.getOutputStream(method, headers);
            if (out != null && requestBody != null) {
                mRestContext.getConversionStrategy(requestBody.getClass()).convert(out, requestBody);
            }
        } finally {
            CloseableUtils.close(out);
        }
    }

    /**
     * 
     * @param client
     * @param responseType
     * @return
     * @throws ParseRestNetworkException
     * @throws ParseRestResponseException
     * @throws ParseRestAuthorizationException
     */
    @SuppressWarnings("unchecked")
    private <T> T receive(ParseRestClient client, Class<T> responseType) throws ParseRestNetworkException, ParseRestResponseException, ParseRestAuthorizationException {
        InputStream in = null;
        try {
            in = client.getInputStream();
            if (responseType == null) {
                return null;
            }
            return (T) mRestContext.getConversionStrategy(responseType).convert(in, responseType);
        } finally {
            CloseableUtils.close(in);
        }
    }
}