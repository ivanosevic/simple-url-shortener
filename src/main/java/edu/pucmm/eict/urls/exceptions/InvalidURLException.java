package edu.pucmm.eict.urls.exceptions;

public class InvalidURLException extends RuntimeException {
    public InvalidURLException() {
    }

    public InvalidURLException(String message) {
        super(message);
    }

    public InvalidURLException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidURLException(Throwable cause) {
        super(cause);
    }
}
