package arbalest.utils;

import android.util.Log;

import java.io.Closeable;
import java.io.IOException;

/**
 * 
 * @author keishin.yokomaku
 * @hide
 */
public final class CloseableUtils {
    public static final String TAG = CloseableUtils.class.getSimpleName();
    private CloseableUtils() {}

    public static final void close(Closeable closeable) {
        if (closeable == null) {
            return; // do nothing.
        }
        try {
            closeable.close();
        } catch (IOException e) {
            Log.e(TAG, "something went wrong with i/o: ", e);
        }
    }
}