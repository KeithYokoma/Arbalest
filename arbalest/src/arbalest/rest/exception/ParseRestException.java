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