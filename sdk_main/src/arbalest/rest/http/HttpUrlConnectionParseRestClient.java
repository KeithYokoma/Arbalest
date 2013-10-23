package arbalest.rest.http;

import android.app.Application;
import android.util.Log;

import arbalest.rest.exception.ParseRestAuthorizationException;
import arbalest.rest.exception.ParseRestException;
import arbalest.rest.exception.ParseRestNetworkException;
import arbalest.rest.exception.ParseRestResponseException;
import arbalest.utils.ApplicationUtils;
import arbalest.utils.DebugInputStream;
import arbalest.utils.DebugOutputStream;

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
import java.util.Map.Entry;
import java.util.Set;

public class HttpUrlConnectionParseRestClient implements ParseRestClient {
    public static final String TAG = HttpUrlConnectionParseRestClient.class.getSimpleName();

    private final Application mApplication;
    private final HttpURLConnection mConnection;
    private final OkHttpClient mClient;

    protected HttpUrlConnectionParseRestClient(Application application, OkHttpClient client, String url) throws ParseRestNetworkException {
        mApplication = application;
        mClient = client;
        mConnection = openConnection(url);
    }

    protected HttpUrlConnectionParseRestClient(Application application, OkHttpClient client, HttpURLConnection connection) throws ParseRestNetworkException {
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
    public OutputStream getOutputStream(String method, Map<String, String> headers) throws ParseRestNetworkException {
        if (headers != null) {
            Set<Entry<String, String>> entries = headers.entrySet();
            for (Entry<String, String> entry : entries) {
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
            throw new ParseRestNetworkException(e);
        }
    }

    @SuppressWarnings("resource")
    @Override
    public InputStream getInputStream() throws ParseRestNetworkException, ParseRestResponseException, ParseRestAuthorizationException {
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
    public int getResponseCode() throws ParseRestNetworkException {
        try {
            return mConnection.getResponseCode();
        } catch (IOException e) {
            throw new ParseRestNetworkException(e);
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

    private ParseRestException handleErrorResponse() throws ParseRestAuthorizationException, ParseRestNetworkException, ParseRestResponseException {
        int code = getResponseCode();
        switch (code) {
        case HttpURLConnection.HTTP_UNAUTHORIZED:
            throw new ParseRestAuthorizationException("", code);
        default:
            throw new ParseRestResponseException(getErrorResponse(), code);
        }
    }

    private String getErrorResponse() throws ParseRestNetworkException {
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
            throw new ParseRestNetworkException(e);
        }
    }
}