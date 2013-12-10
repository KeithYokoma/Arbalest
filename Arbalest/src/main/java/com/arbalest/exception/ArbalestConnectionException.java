package com.arbalest.exception;

public class ArbalestConnectionException extends ArbalestException {
    private static final long serialVersionUID = -1804578977897814863L;

    public ArbalestConnectionException() {
        super();
    }

    public ArbalestConnectionException(int statusCode) {
        super(statusCode);
    }

    public ArbalestConnectionException(String detailMessage) {
        super(detailMessage);
    }

    public ArbalestConnectionException(String detailMessage, int statusCode) {
        super(detailMessage, statusCode);
    }

    public ArbalestConnectionException(Throwable throwable) {
        super(throwable);
    }

    public ArbalestConnectionException(Throwable throwable, int statusCode) {
        super(throwable, statusCode);
    }

    public ArbalestConnectionException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ArbalestConnectionException(String detailMessage, Throwable throwable, int statusCode) {
        super(detailMessage, throwable, statusCode);
    }
}
