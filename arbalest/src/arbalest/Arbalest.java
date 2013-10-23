package arbalest;

import android.app.Application;
import android.content.Context;

import arbalest.rest.ParseRest;

import java.util.HashMap;
import java.util.Map;

public final class Arbalest {
    private static boolean sInitialized;

    private Arbalest() {}

    public static final synchronized void initialize(Context context, String applicatinId, String restApiKey) {
        if (sInitialized) {
            return;
        }

        Application application = (Application) context.getApplicationContext();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put(ParseRest.HEADER_PARSE_APPLICATION_ID, applicatinId);
        headers.put(ParseRest.HEADER_PARSE_REST_API_KEY, restApiKey);

        ParseRest.initialize(application, headers);

        sInitialized = true;
    }

    public static final synchronized void destroy() {
        if (!sInitialized) {
            return;
        }

        ParseRest.destroy();

        sInitialized = false;
    }
}