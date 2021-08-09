package edu.pucmm.eict.urlshortener.urls;

public class InvalidUrlException extends RuntimeException {
    public InvalidUrlException(String message) {
        super(message);
    }
}
