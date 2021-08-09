package edu.pucmm.eict.urlshortener.persistence;

public class PaginationErrorException extends RuntimeException {
    public PaginationErrorException(String message) {
        super(message);
    }
}
