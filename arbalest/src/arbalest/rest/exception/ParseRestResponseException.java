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

public class ParseRestResponseException extends ParseRestException {
    private static final long serialVersionUID = -3401825629484056812L;

    public ParseRestResponseException() {
        super();
    }

    public ParseRestResponseException(int statusCode) {
        super(statusCode);
    }

    public ParseRestResponseException(String detailMessage, int statusCode) {
        super(detailMessage, statusCode);
    }

    public ParseRestResponseException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ParseRestResponseException(String detailMessage) {
        super(detailMessage);
    }

    public ParseRestResponseException(Throwable throwable) {
        super(throwable);
    }
}