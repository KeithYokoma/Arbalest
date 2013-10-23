package arbalest.rest.http;

import android.app.Application;
import android.content.Context;

import arbalest.rest.exception.ParseRestNetworkException;

import com.squareup.okhttp.OkHttpClient;

import java.security.GeneralSecurityException;

import javax.net.ssl.SSLContext;

public class HttpUrlConnectionParseRestClientFactory implements ParseRestClientFactory {
    private final Application mApplication;
    private final OkHttpClient mClient;

    public HttpUrlConnectionParseRestClientFactory(Context context) {
        mApplication = (Application) context.getApplicationContext();
        mClient = createOkHttpClient();
    }

    @Override
    public ParseRestClient create(String url) throws ParseRestNetworkException {
        return new HttpUrlConnectionParseRestClient(mApplication, mClient, url);
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