package edu.pucmm.eict.users;

public class UserNotDeletableException extends RuntimeException {
    public UserNotDeletableException(String message) {
        super(message);
    }
}
