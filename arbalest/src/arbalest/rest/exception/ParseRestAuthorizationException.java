package arbalest.rest.exception;

public class ParseRestAuthorizationException extends ParseRestException {
    private static final long serialVersionUID = -7078839282624080693L;

    public ParseRestAuthorizationException() {
        super();
    }

    public ParseRestAuthorizationException(int statusCode) {
        super(statusCode);
    }

    public ParseRestAuthorizationException(String detailMessage, int statusCode) {
        super(detailMessage, statusCode);
    }

    public ParseRestAuthorizationException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ParseRestAuthorizationException(String detailMessage) {
        super(detailMessage);
    }

    public ParseRestAuthorizationException(Throwable throwable) {
        super(throwable);
    }
}