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
import android.content.Context;

import com.arbalest.exception.ArbalestNetworkException;
import com.squareup.okhttp.OkHttpClient;

import java.security.GeneralSecurityException;

import javax.net.ssl.SSLContext;

public class ArbalestClientFactory implements IArbalestClientFactory {
    private final Application mApplication;
    private final OkHttpClient mClient;

    public ArbalestClientFactory(Context context) {
        mApplication = (Application) context.getApplicationContext();
        mClient = createOkHttpClient();
    }

    @Override
    public ArbalestClient create(String url) throws ArbalestNetworkException {
        return new ArbalestClient(mApplication, mClient, url);
    }

    private OkHttpClient createOkHttpClient() {
        OkHttpClient client = new OkHttpClient();
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, null, null);
        } catch (GeneralSecurityException e) {
            throw new AssertionError(e);
        }
        client.setSslSocketFactory(sslContext.getSocketFactory());
        return client;
    }
}