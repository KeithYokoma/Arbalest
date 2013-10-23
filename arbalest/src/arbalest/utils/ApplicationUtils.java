package arbalest.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

/**
 * @author keishin.yokomaku
 * @hide
 */
public final class ApplicationUtils {
    public static final String TAG = ApplicationUtils.class.getSimpleName();
    private ApplicationUtils() {}

    public static final boolean isDebuggable(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(context.getPackageName(), 0);
            if ((ai.flags & ApplicationInfo.FLAG_DEBUGGABLE) == ApplicationInfo.FLAG_DEBUGGABLE)
                return true;
        } catch (NameNotFoundException e) {
            Log.e(TAG, "no such package: ", e);
        }
        return false;
    }
}