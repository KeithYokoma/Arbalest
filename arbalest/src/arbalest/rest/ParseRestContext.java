package arbalest.rest;

import android.content.Context;
import android.os.Handler;
import android.util.SparseArray;

import arbalest.rest.callback.ParseCallback;
import arbalest.rest.callback.ParseCallbackManager;
import arbalest.rest.exception.ParseRestException;
import arbalest.rest.exception.ParseRestNetworkException;
import arbalest.rest.http.ParseRestClient;
import arbalest.rest.http.ParseRestClientFactory;
import arbalest.rest.net.annotation.ConvertedBy;
import arbalest.rest.net.converter.EntityConversionPolicy;
import arbalest.rest.net.converter.EntityConversionStrategy;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class ParseRestContext {
    private static ParseRestContext sInstance;
    private final ParseRestClientFactory mFactory;
    private final Map<String, String> mHeaders;
    private SparseArray<ParseRestClient> mClients = new SparseArray<ParseRestClient>();
    private ParseCallbackManager<ParseCallback<?>> mCallbackManager = new ParseCallbackManager<ParseCallback<?>>();

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
    public synchronized Map<String, String> getBaseHeaders() {
        return new HashMap<String, String>(mHeaders);
    }

    /**
     * 
     * @return
     */
    public int getAndIncrementCallbackSeq() {
        return mCallbackManager.getAndIncrementSeq();
    }

    public <T> void putParseCallback(Context context, ParseCallback<T> callback, int seq) {
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
                ParseCallback<T> callback = (ParseCallback<T>) mCallbackManager.getAndRemove(ref.get(), seq);
                if (callback != null) {
                    callback.onSuccess(result);
                }
            }
        });
    }

    public <T> void callOnFailure(final WeakReference<Context> ref, final int seq, final ParseRestException exp, Handler handler) {
        if (ref.get() == null) {
            return; // the context that has a callback already gone.
        }

        handler.post(new Runnable() {
            @SuppressWarnings("unchecked")
            @Override
            public void run() {
                ParseCallback<T> callback = (ParseCallback<T>) mCallbackManager.getAndRemove(ref.get(), seq);
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