package arbalest.rest.exception;

public class ParseRestNetworkException extends ParseRestException {
    private static final long serialVersionUID = 6601015512942935488L;

    public ParseRestNetworkException() {
        super();
    }

    public ParseRestNetworkException(int statusCode) {
        super(statusCode);
    }

    public ParseRestNetworkException(String detailMessage, int statusCode) {
        super(detailMessage, statusCode);
    }

    public ParseRestNetworkException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ParseRestNetworkException(String detailMessage) {
        super(detailMessage);
    }

    public ParseRestNetworkException(Throwable throwable) {
        super(throwable);
    }
}