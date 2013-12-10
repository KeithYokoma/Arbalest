package com.arbalest.exception;

public class ArbalestResponseException extends ArbalestException {
    private static final long serialVersionUID = -4488572278427881515L;

    public ArbalestResponseException() {
        super();
    }

    public ArbalestResponseException(int statusCode) {
        super(statusCode);
    }

    public ArbalestResponseException(String detailMessage) {
        super(detailMessage);
    }

    public ArbalestResponseException(String detailMessage, int statusCode) {
        super(detailMessage, statusCode);
    }

    public ArbalestResponseException(Throwable throwable) {
        super(throwable);
    }

    public ArbalestResponseException(Throwable throwable, int statusCode) {
        super(throwable, statusCode);
    }

    public ArbalestResponseException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ArbalestResponseException(String detailMessage, Throwable throwable, int statusCode) {
        super(detailMessage, throwable, statusCode);
    }
}