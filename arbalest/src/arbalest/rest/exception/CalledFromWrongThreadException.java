package arbalest.rest.exception;

public class CalledFromWrongThreadException extends RuntimeException {
    private static final long serialVersionUID = 3851123782516673708L;

    public CalledFromWrongThreadException() {
        super();
    }

    public CalledFromWrongThreadException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public CalledFromWrongThreadException(String detailMessage) {
        super(detailMessage);
    }

    public CalledFromWrongThreadException(Throwable throwable) {
        super(throwable);
    }
}