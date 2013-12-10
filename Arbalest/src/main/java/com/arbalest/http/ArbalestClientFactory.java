package com.arbalest.http;

import android.app.Application;
import android.content.Context;

import com.arbalest.exception.ArbalestNetworkException;
import com.squareup.okhttp.OkHttpClient;

import java.security.GeneralSecurityException;

import javax.net.ssl.SSLContext;

/**
 * Created by yokomakukeishin on 2013/12/10.
 */
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