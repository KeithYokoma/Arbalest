/*
 * Copyright (C) 2013 KeithYokoma. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.arbalest;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.amalgam.app.ApplicationUtils;
import com.arbalest.http.ArbalestClientFactory;

import java.util.HashMap;
import java.util.Map;

public class Arbalest {
    public static final String TAG = Arbalest.class.getSimpleName();
    private static volatile Arbalest sInstance;
    private final Application mApplication;
    private final ArbalestContext mArbalestContext;
    private final Handler mHandler;

    protected Arbalest(Application application, ArbalestContext arbalestContext) {
        mApplication = application;
        mArbalestContext = arbalestContext;
        mHandler = new Handler(Looper.getMainLooper());
    }

    public static synchronized void initialize(Application application, String applicationId, String restKey) {
        if (sInstance != null) {
            Log.i(TAG, "Arbalest is already initialized.");
            return;
        }

        Map<String ,String> headers = new HashMap<String, String>();
        ArbalestContext.initialize(new ArbalestClientFactory(application), headers);
    }
}
