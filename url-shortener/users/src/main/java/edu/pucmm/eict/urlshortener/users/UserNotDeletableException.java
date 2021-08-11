package edu.pucmm.eict.urlshortener.users;

public class UserNotDeletableException extends RuntimeException {
    public UserNotDeletableException(String message) {
        super(message);
    }
}
