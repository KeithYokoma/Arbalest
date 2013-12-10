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
