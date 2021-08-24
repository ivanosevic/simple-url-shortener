package edu.pucmm.eict.persistence;

public class PaginationErrorException extends RuntimeException {
    public PaginationErrorException(String message) {
        super(message);
    }
}
