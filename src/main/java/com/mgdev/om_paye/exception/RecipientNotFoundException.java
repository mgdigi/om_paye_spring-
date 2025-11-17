package com.mgdev.om_paye.exception;

public class RecipientNotFoundException extends RuntimeException {
    public RecipientNotFoundException(String message) {
        super(message);
    }

    public RecipientNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}