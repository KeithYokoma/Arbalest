package arbalest.rest.exception;

public class ParseRestConnectionException extends ParseRestException {
    private static final long serialVersionUID = -7580113593228974732L;

    public ParseRestConnectionException() {
        super();
    }

    public ParseRestConnectionException(int statusCode) {
        super(statusCode);
    }

    public ParseRestConnectionException(String detailMessage, int statusCode) {
        super(detailMessage, statusCode);
    }

    public ParseRestConnectionException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ParseRestConnectionException(String detailMessage) {
        super(detailMessage);
    }

    public ParseRestConnectionException(Throwable throwable) {
        super(throwable);
    }
}