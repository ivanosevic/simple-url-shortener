package edu.pucmm.eict.users;

public class UserGrantPrivilegeException extends RuntimeException {
    public UserGrantPrivilegeException() {
    }

    public UserGrantPrivilegeException(String message) {
        super(message);
    }

    public UserGrantPrivilegeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserGrantPrivilegeException(Throwable cause) {
        super(cause);
    }
}
