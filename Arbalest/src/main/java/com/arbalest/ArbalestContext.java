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

import android.content.Context;
import android.os.Handler;
import android.util.SparseArray;

import com.amalgam.collection.ImmutableHashMap;
import com.arbalest.callback.ArbalestCallback;
import com.arbalest.callback.ArbalestCallbackManager;
import com.arbalest.constants.ArbalestConstants;
import com.arbalest.exception.ArbalestException;
import com.arbalest.exception.ArbalestNetworkException;
import com.arbalest.http.ArbalestClient;
import com.arbalest.http.ArbalestClientFactory;
import com.arbalest.net.annotation.ConvertedBy;
import com.arbalest.net.converter.EntityConversionPolicy;
import com.arbalest.net.converter.EntityConversionStrategy;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class ArbalestContext {
    private static ArbalestContext sInstance;
    private final ArbalestClientFactory mFactory;
    private final Map<String, String> mHeaders;
    private SparseArray<ArbalestClient> mClients = new SparseArray<ArbalestClient>();
    private ArbalestCallbackManager<ArbalestCallback<?>> mCallbackManager = new ArbalestCallbackManager<ArbalestCallback<?>>();

    protected ArbalestContext(ArbalestClientFactory factory, Map<String, String> headers) {
        mFactory = factory;
        mHeaders = new ImmutableHashMap<String, String>(headers);
    }

    public static synchronized void initialize(ArbalestClientFactory factory, String applicationId, String restKey) {
        if (sInstance != null) {
            return;
        }
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(ArbalestConstants.HEADER_PARSE_APPLICATION_ID, applicationId);
        headers.put(ArbalestConstants.HEADER_PARSE_REST_API_KEY, restKey);
        sInstance = new ArbalestContext(factory, headers);
    }

    public static synchronized ArbalestContext getInstance() {
        return sInstance;
    }

    public static synchronized void destroy() {
        if (sInstance == null) {
            return;
        }
        sInstance = null;
    }

    public EntityConversionStrategy getConversionStrategy(Class<?> responseType) {
        ConvertedBy annotation;
        if (String.class.equals(responseType)) {
            return EntityConversionPolicy.RAW_JSON;
        } else if (responseType.isArray()) {
            annotation = responseType.getComponentType().getAnnotation(ConvertedBy.class);
        } else {
            annotation = responseType.getAnnotation(ConvertedBy.class);
        }
        return annotation == null ? EntityConversionPolicy.DEFAULT_GSON : annotation.value();
    }

    public void addClient(int seq, ArbalestClient client) {
        if (seq < 0) {
            return; // do nothing with an unexpected index
        }
        synchronized (this) {
            mClients.put(seq, client);
        }
    }

    public void removeClient(int seq) {
        if (seq < 0) {
            return; // do nothing with an unexpected index
        }
        synchronized (this) {
            mClients.remove(seq);
        }
    }

    public ArbalestClient getClient(int seq) {
        synchronized (this) {
            return mClients.get(seq);
        }
    }

    public ArbalestClient createNewClient(String url) throws ArbalestNetworkException {
        return mFactory.create(url);
    }

    public synchronized Map<String, String> getBaseHeaders() {
        return new HashMap<String, String>(mHeaders);
    }

    public int getAndIncrementCallbackSeq() {
        return mCallbackManager.getAndIncrementSeq();
    }

    public <T> void putArbalestCallback(Context context, ArbalestCallback<T> callback, int seq) {
        mCallbackManager.put(context, callback, seq);
    }

    public <T> void callOnSuccess(final WeakReference<Context> ref, final int seq, final T result, Handler handler) {
        if (ref.get() == null) {
            return; // the context that has a callback already gone.
        }

        handler.post(new Runnable() {
            @SuppressWarnings("unchecked")
            @Override
            public void run() {
                ArbalestCallback<T> callback = (ArbalestCallback<T>) mCallbackManager.getAndRemove(ref.get(), seq);
                if (callback != null) {
                    callback.onSuccess(result);
                }
            }
        });
    }

    public <T> void callOnFailure(final WeakReference<Context> ref, final int seq, final ArbalestException exp, Handler handler) {
        if (ref.get() == null) {
            return; // the context that has a callback already gone.
        }

        handler.post(new Runnable() {
            @SuppressWarnings("unchecked")
            @Override
            public void run() {
                ArbalestCallback<T> callback = (ArbalestCallback<T>) mCallbackManager.getAndRemove(ref.get(), seq);
                if (callback != null) {
                    callback.onFailure(exp);
                }
            }
        });
    }

    public void removeCallbackFor(Context context) {
        mCallbackManager.remove(context);
    }
}