package arbalest.rest.exception;

public class ParseRestRequestException extends ParseRestException {
    private static final long serialVersionUID = -1361911964593893044L;

    public ParseRestRequestException() {
        super();
    }

    public ParseRestRequestException(int statusCode) {
        super(statusCode);
    }

    public ParseRestRequestException(String detailMessage, int statusCode) {
        super(detailMessage, statusCode);
    }

    public ParseRestRequestException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public ParseRestRequestException(String detailMessage) {
        super(detailMessage);
    }

    public ParseRestRequestException(Throwable throwable) {
        super(throwable);
    }
}