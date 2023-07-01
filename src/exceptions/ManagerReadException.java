package exceptions;

public class ManagerReadException extends RuntimeException {
    public ManagerReadException(String message) {
        super(message);
    }

    public ManagerReadException(Throwable cause) {
        super(cause);
    }

    public ManagerReadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ManagerReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
