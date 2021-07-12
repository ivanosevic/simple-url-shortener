package edu.pucmm.eict.common;

public class PaginationErrorException extends RuntimeException {
    public PaginationErrorException() {
    }

    public PaginationErrorException(String message) {
        super(message);
    }

    public PaginationErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaginationErrorException(Throwable cause) {
        super(cause);
    }
}
