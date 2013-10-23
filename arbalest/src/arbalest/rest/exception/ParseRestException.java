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
package arbalest.rest.exception;

public class ParseRestException extends Exception {
    private static final long serialVersionUID = -3199097734717331619L;
    private int mStatusCode;

    public ParseRestException() {
        this(0);
    }

    public ParseRestException(int statusCode) {
        super();
        setStatusCode(statusCode);
    }

    public ParseRestException(String detailMessage, int statusCode) {
        super(detailMessage);
        setStatusCode(statusCode);
    }

    public ParseRestException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ParseRestException(String detailMessage) {
        super(detailMessage);
    }

    public ParseRestException(Throwable throwable) {
        super(throwable);
    }

    public void setStatusCode(int statusCode) {
        mStatusCode = statusCode;
    }

    public int getStatusCode() {
        return mStatusCode;
    }
}