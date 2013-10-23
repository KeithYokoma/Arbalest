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