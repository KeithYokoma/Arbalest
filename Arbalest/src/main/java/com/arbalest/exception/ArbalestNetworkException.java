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
