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