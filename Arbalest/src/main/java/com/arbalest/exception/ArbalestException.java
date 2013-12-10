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
package com.arbalest.exception;

public class ArbalestException extends Exception {
    private static final long serialVersionUID = -3199097734717331619L;
    private int mStatusCode;

    public ArbalestException() {
        this(0);
    }

    public ArbalestException(int statusCode) {
        super();
        setStatusCode(statusCode);
    }

    public ArbalestException(String detailMessage) {
        this(detailMessage, 0);
    }

    public ArbalestException(String detailMessage, int statusCode) {
        super(detailMessage);
        setStatusCode(statusCode);
    }

    public ArbalestException(Throwable throwable) {
        this(throwable, 0);
    }

    public ArbalestException(Throwable throwable, int statusCode) {
        super(throwable);
        setStatusCode(statusCode);
    }

    public ArbalestException(String detailMessage, Throwable throwable) {
        this(detailMessage, throwable, 0);
    }

    public ArbalestException(String detailMessage, Throwable throwable, int statusCode) {
        super(detailMessage, throwable);
        setStatusCode(statusCode);
    }

    public void setStatusCode(int statusCode) {
        mStatusCode = statusCode;
    }

    public int getStatusCode() {
        return mStatusCode;
    }
}
