package arbalest.rest.net;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import arbalest.rest.net.annotation.ConvertedBy;
import arbalest.rest.net.converter.EntityConversionPolicy;
import arbalest.utils.ParcelFileDescriptorUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

@ConvertedBy(EntityConversionPolicy.BINARY)
public class Binary {
    public static final String TAG = Binary.class.getSimpleName();
    private static final String MODE_READ = "r";
    private final String mName;
    private final Uri mUri;
    private final Application mApplication;

    public Binary(Context context, String name, Uri uri) {
        mApplication = (Application) context.getApplicationContext();
        mName = name;
        mUri = uri;
    }

    public String getName() {
        return mName;
    }

    public String getPath() {
        return mUri.getPath();
    }

    public long getLength() {
        ParcelFileDescriptor descriptor = null;

        try {
            descriptor = mApplication.getContentResolver().openFileDescriptor(mUri, MODE_READ);
            return descriptor.getStatSize();
        } catch (FileNotFoundException e) {
            return 0L;
        } finally {
            ParcelFileDescriptorUtils.close(descriptor);
        }
    }

    public String getContentType() {
        return mApplication.getContentResolver().getType(mUri);
    }

    public InputStream getInputStream() throws FileNotFoundException, IOException {
        return mApplication.getContentResolver().openInputStream(mUri);
    }
}