package arbalest.rest;

import android.util.SparseArray;

import arbalest.rest.exception.ParseRestNetworkException;
import arbalest.rest.http.ParseRestClient;
import arbalest.rest.http.ParseRestClientFactory;
import arbalest.rest.net.annotation.ConvertedBy;
import arbalest.rest.net.converter.EntityConversionPolicy;
import arbalest.rest.net.converter.EntityConversionStrategy;

import java.util.HashMap;
import java.util.Map;

public class ParseRestContext {
    private static ParseRestContext sInstance;
    private final ParseRestClientFactory mFactory;
    private final Map<String, String> mHeaders;
    private SparseArray<ParseRestClient> mClients = new SparseArray<ParseRestClient>();

    protected ParseRestContext(ParseRestClientFactory factory, Map<String, String> headers) {
        mFactory = factory;
        mHeaders = headers;
    }

    public static synchronized final void initialize(ParseRestClientFactory factory, Map<String, String> headers) {
        if (sInstance != null) {
            return;
        }
        sInstance = new ParseRestContext(factory, headers);
    }

    public static synchronized final ParseRestContext getInstance() {
        return sInstance;
    }

    public static synchronized final void destroy() {
        if (sInstance == null) {
            return;
        }
        sInstance = null;
    }

    /**
     * 
     * @param responseType
     * @return
     */
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

    /**
     * 
     * @param seq
     * @param client
     */
    public void addClient(int seq, ParseRestClient client) {
        if (seq < 0) {
            return; // do nothing with an unexpected index
        }
        synchronized (this) {
            mClients.put(seq, client);
        }
    }

    /**
     * 
     * @param seq
     */
    public void removeClient(int seq) {
        if (seq < 0) {
            return; // do nothing with an unexpected index
        }
        synchronized (this) {
            mClients.remove(seq);
        }
    }

    /**
     * 
     * @param seq
     * @return
     */
    public ParseRestClient getClient(int seq) {
        synchronized (this) {
            return mClients.get(seq);
        }
    }

    /**
     * 
     * @param url
     * @return
     * @throws ParseRestNetworkException
     */
    public ParseRestClient createNewClient(String url) throws ParseRestNetworkException {
        return mFactory.create(url);
    }

    /**
     * 
     * @return
     */
    public Map<String, String> getBaseHeaders() {
        return new HashMap<String, String>(mHeaders);
    }
}