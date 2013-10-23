package arbalest.utils;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * @author keishin.yokomaku
 * @hide
 */
public class DebugInputStream extends InputStream {
    public static final String TAG = DebugInputStream.class.getSimpleName();
    private InputStream mStream;

    public DebugInputStream(InputStream stream) {
        mStream = stream;
    }

    @Override
    public int read() throws IOException {
        return mStream.read();
    }

    @Override
    public int read(byte[] buffer, int offset, int length) throws IOException {
        int ret = super.read(buffer, offset, length);
        if (ret > 0) {
            Log.v(TAG, new String(buffer, offset, ret, "UTF-8"));
        }
        return ret;
    }

    @Override
    public void close() throws IOException {
        mStream.close();
    }
}
