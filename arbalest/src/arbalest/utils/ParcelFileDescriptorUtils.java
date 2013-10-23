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
package arbalest.utils;

import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.IOException;

public final class ParcelFileDescriptorUtils {
    public static final String TAG = ParcelFileDescriptorUtils.class.getSimpleName();

    private ParcelFileDescriptorUtils() {}

    public static final void close(ParcelFileDescriptor descriptor) {
        if (descriptor == null) {
            return;
        }

        try {
            descriptor.close();
        } catch (IOException e) {
            Log.e(TAG, "something went wrong on close parcel file descriptor: ", e);
        }
    }
}