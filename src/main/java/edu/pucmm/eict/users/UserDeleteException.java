package edu.pucmm.eict.users;

public class UserDeleteException extends RuntimeException {
    public UserDeleteException() {
    }

    public UserDeleteException(String message) {
        super(message);
    }

    public UserDeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserDeleteException(Throwable cause) {
        super(cause);
    }
}
