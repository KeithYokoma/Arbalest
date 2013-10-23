package arbalest.utils;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 
 * @author keishin.yokomaku
 * @hide
 */
public class DebugOutputStream extends OutputStream {
    public static final String TAG = DebugOutputStream.class.getSimpleName();
    private OutputStream mStream;

    public DebugOutputStream(OutputStream stream) {
        mStream = stream;
    }

    @Override
    public void write(int oneByte) throws IOException {
        mStream.write(oneByte);
    }

    @Override
    public void write(byte[] buffer, int offset, int count) throws IOException {
        Log.w(TAG, new String(buffer, offset, count));
        super.write(buffer, offset, count);
    }
    @Override
    public void close() throws IOException {
        mStream.close();
    }
}
