package com.mgdev.om_paye.exception;

public class UserHasNoAccountException extends RuntimeException {
    public UserHasNoAccountException(String message) {
        super(message);
    }

    public UserHasNoAccountException(String message, Throwable cause) {
        super(message, cause);
    }
}