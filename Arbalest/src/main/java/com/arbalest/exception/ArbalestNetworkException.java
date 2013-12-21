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

public class ArbalestNetworkException extends ArbalestException {
    private static final long serialVersionUID = 258008332672633638L;

    public ArbalestNetworkException() {
        super();
    }

    public ArbalestNetworkException(int statusCode) {
        super(statusCode);
    }

    public ArbalestNetworkException(String detailMessage) {
        super(detailMessage);
    }

    public ArbalestNetworkException(String detailMessage, int statusCode) {
        super(detailMessage, statusCode);
    }

    public ArbalestNetworkException(Throwable throwable) {
        super(throwable);
    }

    public ArbalestNetworkException(Throwable throwable, int statusCode) {
        super(throwable, statusCode);
    }

    public ArbalestNetworkException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ArbalestNetworkException(String detailMessage, Throwable throwable, int statusCode) {
        super(detailMessage, throwable, statusCode);
    }
}
