package com.arbalest.exception;

/**
 * Created by yokomakukeishin on 2013/12/10.
 */
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
