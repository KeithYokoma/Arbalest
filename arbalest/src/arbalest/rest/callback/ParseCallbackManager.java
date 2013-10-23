package arbalest.rest.callback;

import android.content.Context;
import android.util.SparseArray;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ParseCallbackManager<T> {
    private final Map<Context, SparseArray<T>> mCallbacks = new WeakHashMap<Context, SparseArray<T>>();
    private final AtomicInteger mSeqId = new AtomicInteger(1);

    public int getAndIncrementSeq() {
        return mSeqId.getAndIncrement();
    }

    public synchronized T getAndRemove(Context context, int seq) {
        if (context == null) {
            return null; // attached context has gone, so just return null.
        }

        SparseArray<T> array = mCallbacks.get(context);
        if (array == null) {
            return null; // no callbacks for the context, so just return null.
        }

        T callback = array.get(seq);
        array.remove(seq);
        if (array.size() == 0) {
            mCallbacks.remove(context);
        }

        return callback;
    }

    public synchronized void put(Context context, T callback, int seq) {
        if (context == null || callback == null) {
            return; // we cannot do anything, so terminate this procedure.
        }

        SparseArray<T> array = mCallbacks.get(context);
        if (array == null) {
            array = new SparseArray<T>();
            mCallbacks.put(context, array);
        }
        array.put(seq, callback);
    }

    public synchronized void remove(Context context) {
        if (context == null) {
            return; // attached context has gone, so terminate this procedure.
        }

        mCallbacks.remove(context);
    }
}