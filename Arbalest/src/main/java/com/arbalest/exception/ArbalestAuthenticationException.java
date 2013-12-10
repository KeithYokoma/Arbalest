package com.arbalest.exception;

public class ArbalestAuthenticationException extends ArbalestException {
    private static final long serialVersionUID = 566998501253719081L;

    public ArbalestAuthenticationException() {
        super();
    }

    public ArbalestAuthenticationException(int statusCode) {
        super(statusCode);
    }

    public ArbalestAuthenticationException(String detailMessage) {
        super(detailMessage);
    }

    public ArbalestAuthenticationException(String detailMessage, int statusCode) {
        super(detailMessage, statusCode);
    }

    public ArbalestAuthenticationException(Throwable throwable) {
        super(throwable);
    }

    public ArbalestAuthenticationException(Throwable throwable, int statusCode) {
        super(throwable, statusCode);
    }

    public ArbalestAuthenticationException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ArbalestAuthenticationException(String detailMessage, Throwable throwable, int statusCode) {
        super(detailMessage, throwable, statusCode);
    }
}