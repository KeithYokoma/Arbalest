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
package com.arbalest.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class AsyncProcessor {
    public static final String TAG = AsyncProcessor.class.getSimpleName();
    private static final int CORE_THREAD_POOL_SIZE = 3;
    private static final int MAX_THREAD_POOL_SIZE = 64;
    private static final int KEEP_ALIVE = 1;
    private static final int POOL_WORK_QUEUE_CAPACITY = 10;
    private static final BlockingQueue<Runnable> sWorkQueue = new LinkedBlockingQueue<Runnable>(POOL_WORK_QUEUE_CAPACITY);
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);
        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, TAG + " #" + mCount.getAndIncrement());
        }
    };
    private static final Executor sThreadPoolExecutor = new ThreadPoolExecutor(
            CORE_THREAD_POOL_SIZE, MAX_THREAD_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, sWorkQueue, sThreadFactory);

    public static final void process(Runnable runnable) {
        sThreadPoolExecutor.execute(runnable);
    }
}
