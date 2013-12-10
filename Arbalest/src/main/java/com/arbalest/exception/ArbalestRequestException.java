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

public class ArbalestRequestException extends ArbalestException {
    private static final long serialVersionUID = -9093156485753245898L;

    public ArbalestRequestException() {
        super();
    }

    public ArbalestRequestException(int statusCode) {
        super(statusCode);
    }

    public ArbalestRequestException(String detailMessage) {
        super(detailMessage);
    }

    public ArbalestRequestException(String detailMessage, int statusCode) {
        super(detailMessage, statusCode);
    }

    public ArbalestRequestException(Throwable throwable) {
        super(throwable);
    }

    public ArbalestRequestException(Throwable throwable, int statusCode) {
        super(throwable, statusCode);
    }

    public ArbalestRequestException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ArbalestRequestException(String detailMessage, Throwable throwable, int statusCode) {
        super(detailMessage, throwable, statusCode);
    }
}
