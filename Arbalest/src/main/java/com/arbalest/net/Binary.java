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
package com.arbalest.net;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.os.ParcelFileDescriptor;

import com.amalgam.os.ParcelFileDescriptorUtils;
import com.arbalest.net.annotation.ConvertedBy;
import com.arbalest.net.converter.EntityConversionPolicy;

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