package com.parse.rest;

import android.app.Application;
import android.util.Log;

public class ParseRest {
    public static final String TAG = ParseRest.class.getSimpleName();
    private static ParseRest sInstance;

    protected ParseRest(Application app) {
        
    }

    public static final void initialize(Application app) {
        if (sInstance != null) {
            Log.v(TAG, "ParseRest is already initialized.");
            return;
        }

        sInstance = new ParseRest(app);
    }

    public static final ParseRest getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException("ParseRest is not yet initialized.");
        }

        return sInstance;
    }
}